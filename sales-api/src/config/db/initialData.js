import Order from "../../modulos/model/Order.js";

export async function createInitialData() {
    await Order.collection.drop();

    await Order.create({
        products: [
          { productId: 1001, quantity: 2 },
          { productId: 1002, quantity: 1 }
        ],
        user: { id: 1, name: 'User Test 1', email: 'testeuser1@gmail.com' },
        status: 'PENDENTE',
        createdAt: new Date(),
        updatedAt: new Date()
      })

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
            id: 1,
            name: "User Teste1",
            email: "usertest1@gmail.com"
        },
        status: "APROVADO",
        createdAt: new Date(),
        updatedAt: new Date()
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
            id: 2,
            name: "User Teste2",
            email: "usertest2@gmail.com"
        },
        status: "REJEITADO",
        createdAt: new Date(),
        updatedAt: new Date()
    })
}