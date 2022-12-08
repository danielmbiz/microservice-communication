package com.example.productapi.service;

import static com.example.productapi.config.RequestUtil.getCurrentRequest;
import static org.springframework.util.ObjectUtils.isEmpty;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.example.productapi.client.SalesClient;
import com.example.productapi.dto.ProductCheckStockRequest;
import com.example.productapi.dto.ProductQuantityDTO;
import com.example.productapi.dto.ProductRequest;
import com.example.productapi.dto.ProductResponse;
import com.example.productapi.dto.ProductSalesResponse;
import com.example.productapi.dto.ProductStockDTO;
import com.example.productapi.dto.SalesConfirmationDTO;
import com.example.productapi.dto.enums.SalesStatus;
import com.example.productapi.model.Category;
import com.example.productapi.model.Product;
import com.example.productapi.model.Supplier;
import com.example.productapi.rabbitmq.SalesConfirmationSender;
import com.example.productapi.repository.ProductRepository;
import com.example.productapi.service.exceptions.DatabaseException;
import com.example.productapi.service.exceptions.ResourceNotFoundException;
import com.example.productapi.service.exceptions.ValidationException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProductService {

	@Autowired
	private ProductRepository repository;

	@Autowired
	private SupplierService supplierService;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private SalesConfirmationSender salesConfirmationSender;

	@Autowired
	private SalesClient salesClient;
	
    private static final String TRANSACTION_ID = "transactionid";
    private static final String SERVICE_ID = "serviceid";

	public ProductResponse findById(Integer id) {
		if (isEmpty(id)) {
			throw new ValidationException("Informe o Id");
		}
		var product = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado Id: " + id));
		return new ProductResponse(product);
	}

	public List<ProductResponse> findAll() {
		List<ProductResponse> list = repository.findAll().stream().map(x -> new ProductResponse(x))
				.collect(Collectors.toList());
		return list;
	}

	public List<ProductResponse> findByNameContainingIgnoreCase(String name) {
		if (isEmpty(name)) {
			throw new ValidationException("Informe o nome");
		}
		List<ProductResponse> list = repository.findByNameContainingIgnoreCase(name).stream()
				.map(x -> new ProductResponse(x)).collect(Collectors.toList());
		return list;
	}

	public List<ProductResponse> findByCategoryId(Integer categoryId) {
		if (isEmpty(categoryId)) {
			throw new ValidationException("Informe o id da categoria");
		}
		List<ProductResponse> list = repository.findByCategoryId(categoryId).stream().map(x -> new ProductResponse(x))
				.collect(Collectors.toList());
		return list;
	}

	public List<ProductResponse> findBySupplierId(Integer supplierId) {
		if (isEmpty(supplierId)) {
			throw new ValidationException("Informe o Id do fornecedor");
		}
		List<ProductResponse> list = repository.findBySupplierId(supplierId).stream().map(x -> new ProductResponse(x))
				.collect(Collectors.toList());
		return list;
	}

	public ProductResponse save(ProductRequest request) {
		validateProductDataInformed(request);
		validateSupplierAndCategoryInformed(request);
		var category = new Category(categoryService.findById(request.getCategoryId()));
		var supplier = new Supplier(supplierService.findById(request.getSupplierId()));
		var product = new ProductResponse(repository.save(new Product(request, supplier, category)));
		return product;
	}

	public ProductResponse update(Integer id, ProductRequest obj) {
		try {
			findById(id);
			System.out.println(obj.getCategoryId());
			var category = new Category(categoryService.findById(obj.getCategoryId()));
			var supplier = new Supplier(supplierService.findById(obj.getSupplierId()));
			var product = new Product(obj, supplier, category);
			return new ProductResponse(repository.save(product));
		} catch (RuntimeException e) {
			throw new ResourceNotFoundException(e.getMessage());
		}
	}

	@Transactional
	public void updateProductStock(ProductStockDTO product) {
		try {
			var productsForUpdate = new ArrayList<Product>();
			validateStockUpdateData(product);
			product.getProducts().forEach(sales -> {
				var productResponse = findById(sales.getProductId());
				var existingProduct = new Product(productResponse);
				validateQuantityInStock(sales, existingProduct);
				existingProduct.updateStock(sales.getQuantity());
				productsForUpdate.add(existingProduct);
			});
			if (!productsForUpdate.isEmpty()) {
				repository.saveAll(productsForUpdate);
				var aprovedMessage = new SalesConfirmationDTO(product.getSalesId(), SalesStatus.APROVADO, product.getTransactionid());
				salesConfirmationSender.sendSalesConfirmationMessages(aprovedMessage);
			}

		} catch (Exception e) {
			log.error("Erro no processamento da venda ", e.getMessage(), e);
			salesConfirmationSender.sendSalesConfirmationMessages(
					new SalesConfirmationDTO(product.getSalesId(), SalesStatus.REJEITADO, product.getTransactionid()));
		}
	}

	private void validateStockUpdateData(ProductStockDTO productStockDTO) {
		if (isEmpty(productStockDTO) || (isEmpty(productStockDTO.getSalesId()))) {
			throw new ValidationException("Venda não informada");
		}
		if (isEmpty(productStockDTO.getProducts())) {
			throw new ValidationException("Produto não informado");
		}
		productStockDTO.getProducts().forEach(salesProduct -> {
			if (isEmpty(salesProduct.getQuantity()) || isEmpty(salesProduct.getProductId())) {
				throw new ValidationException("Quantidade e produto devem ser informados");
			}
		});
	}

	private void validateQuantityInStock(ProductQuantityDTO productQuantityDTO, Product product) {
		if (productQuantityDTO.getQuantity() > product.getQuantityAvailable()) {
			throw new ValidationException(
					"Quantidade do produto não disponível em estoque Id: " + productQuantityDTO.getProductId());
		}
	}

	public void delete(Integer id) {
		try {
			repository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Produto não encontrado Id: " + id);
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException(e.getMessage());
		} catch (Exception e) {
			e.getMessage();
		}

	}

	private void validateProductDataInformed(ProductRequest request) {
		if (isEmpty(request.getName())) {
			throw new ValidationException("Nome não informada");
		}
		if (isEmpty(request.getQuantityAvailable())) {
			throw new ValidationException("Quantidade não informada");
		}
		if (request.getQuantityAvailable() <= 0) {
			throw new ValidationException("Quantidade precisa ser maior que zero");
		}
	}

	private void validateSupplierAndCategoryInformed(ProductRequest request) {
		if (isEmpty(request.getCategoryId())) {
			throw new ValidationException("Categoria não informada");
		}
		if (isEmpty(request.getSupplierId())) {
			throw new ValidationException("Fornecedor não informado");
		}
	}

	public ProductSalesResponse findProductSales(Integer id) {
		
		try {
			var currentRequest = getCurrentRequest();
            var transactionid = currentRequest.getHeader(TRANSACTION_ID);
            var serviceid = currentRequest.getAttribute(SERVICE_ID);
            log.info("Sending GET request to orders by productId with data {} | [transactionID: {} | serviceID: {}]",
                id, transactionid, serviceid);
            var product = findById(id);
			var sales = salesClient.findSalesByProduct(product.getId());
			var response = new ProductSalesResponse(product, sales.getSalesId());
			log.info("Recieving response from orders by productId with data {} | [transactionID: {} | serviceID: {}]",
	                new ObjectMapper().writeValueAsString(response), transactionid, serviceid);
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ValidationException("Erro ao recuperar as vendas do produto");
		}
	}

	public void checkProductsStock(ProductCheckStockRequest request) {
		try {
			var currentRequest = getCurrentRequest();
	        var transactionid = currentRequest.getHeader(TRANSACTION_ID);
	        var serviceid = currentRequest.getAttribute(SERVICE_ID);
	        log.info("Request to POST product stock with data {} | [transactionID: {} | serviceID: {}]",
	            new ObjectMapper().writeValueAsString(request), transactionid, serviceid);
			if (isEmpty(request) || isEmpty(request.getProducts())) {
				throw new ValidationException("Requisição e produtos devem ser informados");
			}
			request.getProducts().forEach(this::validateStock);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}

	private void validateStock(ProductQuantityDTO productQuantityDTO) {
		if (isEmpty(productQuantityDTO.getQuantity()) || isEmpty(productQuantityDTO.getProductId())) {
			throw new ValidationException("Produto e quantidade precisam ser informados");
		}
		var productResponse = findById(productQuantityDTO.getProductId());
		var product = new Product(productResponse);
		if (product.getQuantityAvailable() < productQuantityDTO.getQuantity()) {
			throw new ValidationException("Quantidade não disponível no estoque Id do produto: " + product.getId());
		}
	}
}
