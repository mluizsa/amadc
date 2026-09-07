package com.ong.amadc.domain.validator;

import com.ong.amadc.config.infra.exception.BusinessException;
import com.ong.amadc.domain.model.AnimalEntidade;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class AnimalValidator {

    /**
     * Executa todas as validações de integridade e consistência para o cadastro de um animal.
     * Dispara BusinessException (400 Bad Request) se alguma regra for violada.
     */
    public void validar(AnimalEntidade animal) {
        if (animal == null) {
            throw new BusinessException("Os dados do animal não podem estar nulos.");
        }

        // 1. Validação de Data de Resgate Futura
        if (animal.getDataResgate() != null && animal.getDataResgate().isAfter(LocalDate.now())) {
            throw new BusinessException("A data de resgate do animal não pode ser uma data futura.");
        }

        // 2. Validação de Consistência de Castração
        if (Boolean.TRUE.equals(animal.getCastrado())) {
            if (animal.getDataCastracao() != null && animal.getDataResgate() != null
                    && animal.getDataCastracao().isBefore(animal.getDataResgate())) {
                throw new BusinessException("A data de castração não pode ser anterior à data de resgate.");
            }
        } else {
            // Se não está castrado, não deve possuir data de castração preenchida
            if (animal.getDataCastracao() != null) {
                throw new BusinessException("Não é possível informar uma data de castração para um animal não castrado.");
            }
        }

        // 3. Validação de Vínculo de Status Coerente
        if (animal.getStatus() != null && "ADOTADO".equalsIgnoreCase(animal.getStatus().getDescricao())) {
            // Regra complementar: Se o status for alterado diretamente para ADOTADO,
            // garante que a flag de adoção externa seja desmarcada de forma segura
            animal.setPossivelAdocao(false);
        }
    }
}
