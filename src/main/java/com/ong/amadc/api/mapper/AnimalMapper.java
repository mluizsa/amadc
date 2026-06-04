package com.ong.amadc.api.mapper;

import com.ong.amadc.api.dto.request.AnimalRequestDTO;
import com.ong.amadc.domain.model.AnimalEntidade;
import com.ong.amadc.domain.model.StatusAnimalEntidade;
import com.ong.amadc.domain.repository.StatusAnimalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AnimalMapper {

    private final StatusAnimalRepository statusAnimalRepository;

    public void mapRequestToEntity(AnimalRequestDTO request, AnimalEntidade animal) {
        animal.setNome(request.nome());
        animal.setEspecie(request.especie());
        animal.setRaca(request.raca());
        animal.setPorte(request.porte());
        animal.setSexo(request.sexo());
        animal.setIdadeEstimada(request.idadeEstimada());
        animal.setPelagemCor(request.pelagemCor());
        animal.setPelagemTipo(request.pelagemTipo());
        animal.setMarcasCicatrizes(request.marcasCicatrizes());
        animal.setDataResgate(request.dataResgate());
        animal.setHoraResgate(request.horaResgate());
        animal.setLocalResgateBairro(request.localResgateBairro());
        animal.setLocalResgateRua(request.localResgateRua());
        animal.setLocalResgateReferencia(request.localResgateReferencia());
        animal.setOrigem(request.origem());
        animal.setResgatadorNome(request.resgatadorNome());
        animal.setResgatadorContato(request.resgatadorContato());
        animal.setPesoEntrada(request.pesoEntrada());
        animal.setCondicaoEntrada(request.condicaoEntrada());
        animal.setMicrochip(request.microchip());
        animal.setPossivelAdocao(request.possivelAdocao());
        animal.setHistoria(request.historia());

        if (request.statusId() != null) {
            StatusAnimalEntidade status = statusAnimalRepository.findById(request.statusId())
                    .orElseThrow(() -> new RuntimeException("Status inválido"));
            animal.setStatus(status);
        }
    }
}