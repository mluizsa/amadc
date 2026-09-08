package com.ong.amadc.domain.business;

import com.ong.amadc.api.dto.request.AtendimentoRequestDTO;
import com.ong.amadc.domain.model.AnimalEntidade;
import com.ong.amadc.domain.model.AtendimentoEntidade;
import com.ong.amadc.domain.model.TipoAtendimentoEntidade;
import org.springframework.stereotype.Component;

@Component
public class AtendimentoBusiness {

    // Se precisar injetar validadores da pasta validator, pode fazer aqui:
    // @Autowired private AtendimentoValidator atendimentoValidator;

    public AtendimentoEntidade prepararEntidadeParaSalvar(AtendimentoRequestDTO requestDTO) {
        // Exemplo de validação de regra de negócio se necessário
        // atendimentoValidator.validar(requestDTO);

        AtendimentoEntidade entidade = new AtendimentoEntidade();
        entidade.setDataAtendimento(requestDTO.dataAtendimento());
        entidade.setPesoNaData(requestDTO.pesoNaData());
        entidade.setValorCobrado(requestDTO.valorCobrado());
        entidade.setDescricao(requestDTO.descricao());

        if (requestDTO.animalId() != null) {
            AnimalEntidade animal = new AnimalEntidade();
            animal.setId(requestDTO.animalId());
            entidade.setAnimal(animal);
        }

        if (requestDTO.tipoAtendimentoId() != null) {
            TipoAtendimentoEntidade tipo = new TipoAtendimentoEntidade();
            tipo.setId(requestDTO.tipoAtendimentoId());
            entidade.setTipoAtendimento(tipo);
        }

        return entidade;
    }
}