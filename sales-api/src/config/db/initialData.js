import Order from "../../modulos/model/Order.js";

export async function createInitialData() {
    await Order.collection.drop();
    await Order.create({
        products: [
            {
                productId: 1001,
                quantity: 2
            },
            {
                productId: 1003,
                quantity: 1
            },
            {
                productId: 1004,
                quantity: 1
            }
        ],
        user: {
            id: "j123k2j",
            name: "User Teste1",
            email: "usertest1@gmail.com"
        },
        status: "APROVADO",
        createdAt: new Date(),
        updateAt: new Date()
    })
    await Order.create({
        products: [
            {
                productId: 1001,
                quantity: 1
            },
            {
                productId: 1004,
                quantity: 2
            }
        ],
        user: {
            id: "2fj123k2j",
            name: "User Teste2",
            email: "usertest2@gmail.com"
        },
        status: "REJEITADO",
        createdAt: new Date(),
        updateAt: new Date()
    })
}