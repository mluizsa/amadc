import axios from 'axios';

const API_URL = '/api/auth/';

// Configura o Axios para SEMPRE enviar cookies nas requisições
axios.defaults.withCredentials = true;

class AuthService {
  login(user) {
    return axios
      .post(API_URL + 'login', {
        login: user.username,
        password: user.password
      })
      .then(response => {
        return response.data;
      });
  }

  logout() {
    return axios.post(API_URL + 'logout').then(() => {
        console.log("Sessão encerrada");
    });
  }

}

export default new AuthService();