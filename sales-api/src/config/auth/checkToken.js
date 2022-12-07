import jwt from "jsonwebtoken";
import { promisify } from "util";

import AuthException from "./AcessTokenExceptions.js";

import { API_SECRET } from "../constants/secrets.js";
import {UNAUTHORIZED, INTERNAL_SERVER_ERROR } from "../constants/httpStats.js";


const emptySpace = " ";

export default async (req, res, next) => {
    try {
        let { authorization } = req.headers;
        if (!authorization) {
            throw new AuthException(UNAUTHORIZED, "Acesso não autorizado");
        }
        let acessToken = authorization;
        if (acessToken.includes(emptySpace)) {
            acessToken = acessToken.split(emptySpace)[1];
        }
        const decoded = await promisify(jwt.verify)(acessToken, API_SECRET);
        req.authUser = decoded.authUser;
        return next();
    } catch (error) {
        const status = error.status ? error.status : INTERNAL_SERVER_ERROR;
        return res.status(status).json({ status, message: error.message });
    }
};