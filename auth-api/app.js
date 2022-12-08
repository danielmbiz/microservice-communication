import express from "express";

import * as db from "./src/config/db/initialData.js";
import userRouter from "./src/modules/user/routes/UserRoute.js";
import checkToken from "./src/config/auth/checkToken.js";
import tracing from "./src/config/tracing.js"

const app = express();
const env = process.env;
const PORT = env.PORT || 8080;

db.createInitialData();

app.use(express.json());

app.get("/api/status", (req, res) => {
    return res.status(200).json({
        service: "Auth-API",
        status: "up",
        httpStatus: 200,
    })
})

app.use(tracing);
app.use(userRouter);

app.listen(PORT, () => {
    console.info(`Servidor iniciado com sucesso na porta  ${PORT}`);
})