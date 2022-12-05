import Sequelize from "sequelize";

const sequelize = new Sequelize("auth-db", "admin", "123456", {
    host: "localhost",
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