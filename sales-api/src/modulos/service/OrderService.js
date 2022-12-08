import OrderRepository from "../repository/OrderRepository.js";
import { sendProductStockUpdateQueue } from "../rabbitmq/productStockUpdateSender.js"
import { PENDING, ACCEPTED, REJECT } from "../../config/constants/orderStatus.js"
import Exception from "../exception/Exception.js";
import { SUCCESS, BAD_REQUEST, INTERNAL_SERVER_ERROR } from "../../config/constants/httpStats.js"
import ProductClient from "../client/ProductClient.js";

class OrderService {
    async findById(req) {
        try {
            const { id } = req.params;
            const { transactionid, serviceid } = req.headers;
            console.info(
                `Requisição GET de PEDIDO: ${id} | [transationId: ${transactionid}] | [serviceId: ${serviceid}]`
            );
            this.validateIdInformed(id);
            let order = await OrderRepository.findById(id);
            if (!order) {
                throw new Exception(BAD_REQUEST, "Pedido não encontrado");
            }
            let response = {
                status: SUCCESS,
                order
            }
            console.info(
                `Resposta GET do PEDIDO: ${JSON.stringify(response)} | [transationId: ${transactionid}] | [serviceId: ${serviceid}]`
            );
            return response;
        } catch (error) {
            return {
                status: error.status ? error.status : INTERNAL_SERVER_ERROR,
                message: error.message
            };
        }
    }

    async findByProductId(req) {
        try {
            const { productId } = req.params;
            const { transactionid, serviceid } = req.headers;
            console.info(
                `Requisição GET de PRODUTOS DE PEDIDO: ${productId} | [transationId: ${transactionid}] | [serviceId: ${serviceid}]`
            );
            this.validateIdInformed(productId);
            let orders = await OrderRepository.findByProductId(productId);
            if (!orders) {
                throw new Exception(BAD_REQUEST, "Pedidos não encontrados");
            }
            let response = {
                status: SUCCESS,
                salesId : orders.map((order) => {
                    return order.id;
                })
            }
            console.info(
                `Resposta do GET de PRODUTOS DE PEDIDO: ${JSON.stringify(response)} | [transationId: ${transactionid}] | [serviceId: ${serviceid}]`
            );
            return response;
        } catch (error) {
            return {
                status: error.status ? error.status : INTERNAL_SERVER_ERROR,
                message: error.message
            };
        }
    }

    async findAll(req) {
        try {
            const { transactionid, serviceid } = req.headers;
            console.info(
                `Requisição GET de TODOS PEDIDOS: | [transationId: ${transactionid}] | [serviceId: ${serviceid}]`
            );

            let orders = await OrderRepository.findAll();
            if (!orders) {
                throw new Exception(BAD_REQUEST, "Pedidos não encontrados");
            }
            let response = {
                status: SUCCESS,
                orders
            }
            console.info(
                `Resposta do POST do LOGIN: ${JSON.stringify(response)} | [transationId: ${transactionid}] | [serviceId: ${serviceid}]`
            );
            return response;
        } catch (error) {
            return {
                status: error.status ? error.status : INTERNAL_SERVER_ERROR,
                message: error.message
            };
        }
    }

    validateIdInformed(id) {
        if (!id) {
            throw new Exception(BAD_REQUEST, "Id do pedido não informado");
        }
    }

    async createOrder(req) {
        try {
            let orderData = req.body;
            const { transactionid, serviceid } = req.headers;
            console.info(
                `Requisição POST de novo PEDIDO: ${JSON.stringify(orderData)} | [transationId: ${transactionid}] | [serviceId: ${serviceid}]`
            );
            this.validateOrderData(orderData);
            const { authUser } = req;
            const { authorization } = req.headers;
            let order = this.createInitialOrderData(orderData, authUser)
            await this.validateProductStock(order, authorization, transactionid);
            let createdOrder = await OrderRepository.save(order);
            
            this.sendMessage(createdOrder, transactionid);
            let response = {
                status: SUCCESS,
                createdOrder
            }
            console.info(
                `Resposta do POST do PEDIDO: ${JSON.stringify(response)} | [transationId: ${transactionid}] | [serviceId: ${serviceid}]`
            );

            return response;
        } catch (error) {
            console.error(`Erro Service 1: ${error}`);
            return {
                status: error.status ? error.status : INTERNAL_SERVER_ERROR,
                message: error.message
            };
        }
    }

    createInitialOrderData(orderData, authUser) {
        return {
            products: orderData.products,
            user: authUser,
            status: PENDING,
            createdAt: new Date(),
            updatedAt: new Date(),
            
        }
    }

    async updateOrder(orderMessage) {
        try {
            const order = JSON.parse(orderMessage);
            if (order.salesId || order.status) {
                let existingOrder = await OrderRepository.findById(order.salesId);
                if (existingOrder && order.status != existingOrder.status) {
                    existingOrder.status = order.status;
                    existingOrder.updatedAt = new Date();
                    await OrderRepository.save(existingOrder);
                }
            }
            else {
                console.warn("Mensagem não está completa");
            }
        } catch (error) {
            console.error("Erro ao realizar o parse");
            console.error(error);
        }
    }

    validateOrderData(data) {
        if (!data || !data.products) {
            throw new Exception(BAD_REQUEST, "Lista de produtos não informada");
        }
    }

    async validateProductStock(order, token, transactionid) {
        let stockIsOk  = await ProductClient.checkProductStock(order, token, transactionid);
        if (stockIsOk) {
            throw new Exception(BAD_REQUEST, "Não existe estoque para o produto")
        }
    }

    sendMessage(createdOrder, transactionid) {
        const message = {
            salesId: createdOrder.id,
            products: createdOrder.products,
            transactionid
        }
        sendProductStockUpdateQueue(message);
    }
}

export default new OrderService();