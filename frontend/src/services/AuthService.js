import axios from 'axios';

const API_URL = '/api/auth/'; // O proxy redireciona para localhost:8080

class AuthService {
  login(user) {
    return axios
      .post(API_URL + 'login', {
        login: user.username,
        password: user.password
      })
      .then(response => {
        if (response.data.token) {
          localStorage.setItem('user_token', response.data.token);
        }
        return response.data;
      });
  }

  logout() {
    localStorage.removeItem('user_token');
  }

  getToken() {
    return localStorage.getItem('user_token');
  }
}

export default new AuthService();