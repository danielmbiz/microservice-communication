import User from "../model/User.js";

class UserRepository {


    async findById(id) {
        try {
            //busca apenas 1 item
            return await User.findOne({where: { id }});
        } catch(err)  {
            console.error(err.message);
            return null;
        }
    }

    async findByEmail(email) {
        try {
            //busca apenas 1 item
            return await User.findOne({where: { email }});
        } catch(err)  {
            console.error(err.message);
            return null;
        }
    }

}

export default new UserRepository;