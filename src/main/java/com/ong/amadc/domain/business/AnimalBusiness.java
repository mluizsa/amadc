package com.ong.amadc.domain.business;

import com.ong.amadc.config.infra.exception.BusinessException;
import com.ong.amadc.domain.model.AnimalEntidade;
import com.ong.amadc.domain.model.ArquivoSistemaEntidade;
import com.ong.amadc.domain.validator.AnimalValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AnimalBusiness {

    private final AnimalValidator animalValidator; // 🆕 Injeção via Lombok

    /**
     * Valida se o arquivo que o usuário deseja definir como capa realmente pertence ao animal informado.
     */
    public void validarPertencimentoDoArquivo(Long arquivoId, List<ArquivoSistemaEntidade> arquivos) {
        boolean pertence = arquivos.stream()
                .anyMatch(arq -> arq.getId().equals(arquivoId));

        if (!pertence) {
            throw new BusinessException("O arquivo especificado de ID " + arquivoId + " não pertence a este animal.");
        }
    }

    /**
     * Reúne todas as validações de consistência cadastral que o animal deve obedecer
     * tanto na hora de Salvar quanto na hora de Atualizar.
     */
    public void validarRegrasDeCadastro(AnimalEntidade animal) {
        animalValidator.validar(animal);
    }
}