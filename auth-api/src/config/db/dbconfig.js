import Sequelize from "sequelize";
import { DB_HOST, DB_NAME, DB_USER, DB_PASSWORD, DB_PORT  } from "../constants/secrets.js";

const sequelize = new Sequelize(DB_NAME, DB_USER, DB_PASSWORD, {
    host: DB_HOST,
    port: DB_PORT,
    dialect: "postgres",
    quoteIdentifiers: false,
    define: {
        syncOnAssociation: true,
        timestamps: false,
        underscored: true,
        underscoredAll: true,
        freezeTableName: true
    }
});

sequelize.authenticate().then(() => {
    console.info("Conexão estabelecida");
}).catch((err) => {
    console.error("Erro de conexão");
    console.error(err.mensage);
})

export default sequelize;