import axios from "axios"

import { PRODUCT_API_URL } from "../../config/constants/secrets.js"

class ProductClient {
    async checkProductStock(products, token) {
        try {
            const headers = { Authorization: token }
            console.log(`Enviando requisição para API de Produto: ${JSON.stringify(products)}`);
            let response = false;
            await axios.post(`${PRODUCT_API_URL}/check-stock`, { products: productsData.products }, { headers })
                .then(res => {
                    response = true;
                })
                .catch(error => {
                    console.error(error);
                    response = false;
                })
            return response;
        } catch (error) {
            return false;
        }
    }
}

export default new ProductClient();