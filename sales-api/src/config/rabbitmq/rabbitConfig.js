import amqp from "amqplib/callback_api.js";
import { listenToSalesConfirmationListiner } from "../../modulos/rabbitmq/salesConfirmationListiner.js";

import {
    PRODUCT_TOPIC,
    PRODUCT_STOCK_UPDATE_QUEUE,
    PRODUCT_STOCK_UPDATE_ROUTING_KEY,
    SALES_CONFIRMATION_QUEUE,
    SALES_CONFIRMATION_ROUTING_KEY
} from "./queue.js";
import { RABBIT_MQ_URL } from "../constants/secrets.js";

const TWO_SECOND = 2000;
const HALF_MINUTE = 30000;
const CONTAINER_ENV = "container";

export async function connectRabbitMq() {
    const env = process.env.NODE_ENV;
    if (CONTAINER_ENV == env) {
        console.info("Aguarde o RabbitMQ inicializar");
        setInterval(() => {
            connectRabbitMqAndCreateQueues();
        }, HALF_MINUTE);
    }
    connectRabbitMqAndCreateQueues();
}


function connectRabbitMqAndCreateQueues() {
    amqp.connect(RABBIT_MQ_URL, (error, connection) => {
        if (error) {
            throw error;
        }
        createQueue(connection, PRODUCT_STOCK_UPDATE_QUEUE, PRODUCT_STOCK_UPDATE_ROUTING_KEY, PRODUCT_TOPIC);
        createQueue(connection, SALES_CONFIRMATION_QUEUE, SALES_CONFIRMATION_ROUTING_KEY, PRODUCT_TOPIC);
        setTimeout(() => {
            connection.close();
        }, TWO_SECOND);
    });

    setTimeout(() => {
        listenToSalesConfirmationListiner();
    }, TWO_SECOND);
}

function createQueue(connection, queue, routingKey, topic) {
    connection.createChannel((error, channel) => {
        if (error) {
            throw error;
        }
        channel.assertExchange(topic, "topic", { durable: true });
        channel.assertQueue(queue, { durable: true });
        channel.bindQueue(queue, topic, routingKey);
    });
}
