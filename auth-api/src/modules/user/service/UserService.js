import bcrypt from "bcrypt";
import jwt from "jsonwebtoken";

import userRepository from "../repository/userRepository.js";
import * as httpStatus from "../../../config/constants/httpStats.js";
import * as secrets from "../../../config/constants/secrets.js";
import UserException from "../exception/UserException.js";


class UserService {
    async findByEmail(req) {
        try {
            const { email } = req.params;
            const { authUser } = req;
            this.validateRequestEmail(email);
            let user = await userRepository.findByEmail(email);
            this.validateUserNotFound(user);           
            this.validateAthenticadUser(user, authUser);
            return {
                status: httpStatus.SUCCESS,
                user: {
                    id: user.id,
                    name: user.name,
                    email: user.email
                }
            }
        } catch (error) {
            return {
                status: error.status ? error.status : httpStatus.INTERNAL_SERVER_ERROR,
                message: error.message
            };
        }
    }

    validateRequestEmail(email) {
        if (!email) {
            throw new UserException(
                httpStatus.BAD_REQUEST,
                'Usuário de e-mail não informado'
            );
        }
    }

    validateUserNotFound(user) {
        if (!user) {
            throw new UserException(
                httpStatus.BAD.REQUEST,
                'Usuário não encontrado'
            ); 
        }
    }

    validateAthenticadUser(user, authUser) {
        if (!authUser || user.id !== authUser.id) {
            throw new UserException(httpStatus.FORBIDDEN, 'Você não pode ver os dados dessa usuário');
        }
    }

    async getAcessToken(req) {
        try {
            const {email, password} = req.body;
            this.validateAcessToken(email, password);   
            let user = await userRepository.findByEmail(email);
            this.validateUserNotFound(user);            
            await this.validatePassword(password, user.password);
            const authUser = {id: user.id, name: user.name, email: user.email};
            const acessToken = jwt.sign({authUser}, secrets.API_SECRET, {expiresIn: "1d"});
            return {
                status: httpStatus.SUCCESS,
                acessToken
            }
        } catch (error) {
            return {
                status: error.status ? error.status : httpStatus.INTERNAL_SERVER_ERROR,
                message: error.message
            };
        }
        

    }

    validateAcessToken(email, password) {
        if (!email || !password) {
            throw new UserException(httpStatus.UNAUTHORIZED, "Informe o email e a senha!")
        }
    }
    async validatePassword(password, hashPassword) {
        if (!await bcrypt.compare(password, hashPassword)) {
            throw new UserException(httpStatus.UNAUTHORIZED, "Usuário incorreto!")
        }
    }
}

export default new UserService;