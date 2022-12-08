import Order from "../model/Order.js";

class OrderRepository {
    async save(order) {
        try {
            return await Order.create(order);
        } catch (error) {
            console.error(`Erro Order Repository 1: ${error.message}`);
            return null;
        }
    }

    async findById(id) {
        try {            
            return await Order.findById(id);
            //busca apenas 1 item
            //return await Order.findOne({ where: { id } });
        } catch (error) {
            console.error(`Erro Order Repository 2: ${error.message}`);
            return null;
        }
    }

    async findByProductId(productId) {
        try {            
            return await Order.find({ "products.productId" : Number(productId), status : "APROVADO"  });
            //busca apenas 1 item
            //return await Order.findOne({ where: { id } });
        } catch (error) {
            console.error(`Erro Order Repository 2: ${error.message}`);
            return null;
        }
    }

    async findAll() {
        try {
            return await Order.find();
        } catch (error) {
            console.error(`Erro Order Repository 3: ${error.message}`);
            return null;
        }
    }
}

export default new OrderRepository();