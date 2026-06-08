package com.ong.amadc.domain.repository;

import com.ong.amadc.domain.model.AnimalEntidade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AnimalRepository extends JpaRepository<AnimalEntidade, Long> {

    @Query("SELECT a FROM AnimalEntidade a WHERE " +
           "((:statusId IS NOT NULL AND a.status.id = :statusId) " +
           "   OR (:statusId IS NULL AND a.status.descricao != 'FALECIDO')) AND " +
           "(:porte IS NULL OR a.porte = :porte) AND " +
           "((:possivelAdocao IS NOT NULL AND a.possivelAdocao = :possivelAdocao) " +
           " OR (:possivelAdocao IS NULL AND a.possivelAdocao = true)) AND " +
           "(:sexo IS NULL OR a.sexo = :sexo) AND " +
           "(:nome IS NULL OR LOWER(a.nome) LIKE :nome)") // Corrigido para usar o parâmetro 'nome' já formatado
    Page<AnimalEntidade> findAllWithFilters(Pageable pageable,
                                            @Param("nome") String nome,
                                            @Param("statusId") Long statusId,
                                            @Param("porte") String porte,
                                            @Param("possivelAdocao") Boolean possivelAdocao,
                                            @Param("sexo") String sexo);
}