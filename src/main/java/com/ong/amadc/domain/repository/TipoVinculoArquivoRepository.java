package com.ong.amadc.domain.repository;

import com.ong.amadc.domain.model.TipoVinculoArquivoEntidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoVinculoArquivoRepository extends JpaRepository<TipoVinculoArquivoEntidade, String> {
}