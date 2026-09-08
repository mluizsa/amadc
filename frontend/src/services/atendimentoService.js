import axios from 'axios';

export default {
  listarPorAnimal(animalId) {
    return axios.get(`/api/atendimentos/animal/${animalId}`);
  },
  cadastrar(atendimento) {
    return axios.post('/api/atendimentos', atendimento);
  },
  deletar(id) {
    return axios.delete(`/api/atendimentos/${id}`);
  }
};