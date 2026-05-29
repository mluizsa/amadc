package com.ong.amadc.domain.business;

import com.ong.amadc.domain.model.VoluntarioEntidade;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class VoluntarioBusiness {

    /**
     * Exemplo: Regra que determina se um voluntário precisa de
     * autorização especial (ex: menor de idade).
     */
    public boolean precisaDeTermoResponsabilidade(VoluntarioEntidade voluntario) {
        return voluntario.getDataNascimento().isAfter(LocalDate.now().minusYears(18));
    }

    /**
     * Exemplo: Formatação ou processamento padrão para documentos.
     */
    public String gerarCodigoIdentificacao(VoluntarioEntidade voluntario) {
        return "VOL-" + voluntario.getCpf().substring(0, 3) + "-" + LocalDate.now().getYear();
    }
}