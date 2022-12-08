import express from "express";

import { connectMongoDb } from "./src/config/db/mongoDbConfig.js";
import { createInitialData } from "./src/config/db/initialData.js";
import { connectRabbitMq } from "./src/config/rabbitmq/rabbitConfig.js";
import { sendProductStockUpdateQueue } from "./src/modulos/rabbitmq/productStockUpdateSender.js";

import checkToken from "./src/config/auth/checkToken.js";
import orderRouter from "./src/modulos/routes/OrderRoute.js";
import tracing from "./src/config/tracing.js";

const app = express();
const env = process.env;
const PORT = env.PORT || 8082;

connectMongoDb();
createInitialData();
connectRabbitMq();

app.use(express.json());
app.use(tracing);
app.use(checkToken);
app.use(orderRouter);

app.get("/teste", async (req, res) => {
    try {
        sendProductStockUpdateQueue([
            {
                productId: 1001,
                quantity: 2
            },
            {
                productId: 1002,
                quantity: 1
            }
        ])
        return res.status(200).json({
            status: "up",
            httpStatus: 200
        })
    } catch (error) {
        console.error(error);
        return res.status(500).json({ error: true });
    }
});

app.get("/api/status", async (req, res) => {
    return res.status(200).json({
        service: "Sales-API",
        status: "up",
        httpStatus: 200
    })
})

app.listen(PORT, () => {
    console.info(`Servidor iniciado com sucesso na porta  ${PORT}`);
})


