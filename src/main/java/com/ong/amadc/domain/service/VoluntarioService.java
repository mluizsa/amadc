package com.ong.amadc.domain.service;

import com.ong.amadc.api.dto.request.VoluntarioRequestDTO;
import com.ong.amadc.api.dto.response.VoluntarioResponseDTO;
import com.ong.amadc.config.infra.exception.BusinessException;
import com.ong.amadc.domain.business.VoluntarioBusiness;
import com.ong.amadc.domain.model.VoluntarioEntidade;
import com.ong.amadc.domain.repository.VoluntarioRepository;
import com.ong.amadc.domain.validator.VoluntarioValidator;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VoluntarioService {

    @Autowired
    private VoluntarioRepository repository;

    @Autowired
    private VoluntarioValidator validator;

    @Autowired
    private VoluntarioBusiness business;

    @Transactional
    public VoluntarioEntidade cadastrar(VoluntarioRequestDTO dto) {
        // 1. Converte a parte básica do voluntário para a entidade
        VoluntarioEntidade voluntario = dto.toEntity();

        // 2. Executa as validações existentes de banco/regras de novos voluntários
        validator.validarNovoVoluntario(voluntario);

        // 3. Sua regra de negócio existente do Termo de Responsabilidade
        if (business.precisaDeTermoResponsabilidade(voluntario)) {
            voluntario.setObservacoes(
                    voluntario.getObservacoes() != null
                            ? voluntario.getObservacoes() + " [PENDENTE TERMO]"
                            : "[PENDENTE TERMO]"
            );
        }

        // 4. NOVA REGRA: Processa o vínculo de usuário e perfis se o acesso foi concedido
        business.processarRegrasDeAcesso(voluntario, dto);

        // 5. Salva no banco. O JPA vai inserir na tabela 'voluntarios' e,
        // se houver usuário preenchido, vai inserir na tabela 'usuarios' por causa do Cascade.
        return repository.save(voluntario);
    }

    @Transactional(readOnly = true)
    public List<VoluntarioResponseDTO> listarTodos(Boolean apenasAtivos) {
        var lista = apenasAtivos ? repository.findAllByAtivoTrue() : repository.findAllByAtivoFalse();

        return lista.stream()
                .map(VoluntarioResponseDTO::new)
                .toList();
    }

    @Transactional
    public VoluntarioEntidade atualizar(Long id, VoluntarioRequestDTO dto) {
        // 1. Busca o voluntário existente (já trazendo o relacionamento do usuário se houver)
        var voluntario = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Voluntário não encontrado"));

        // 2. Copia as propriedades básicas do Voluntário (ignorando o ID e os campos de auditoria)
        BeanUtils.copyProperties(dto, voluntario, "id", "dataCriacao", "registradoPor");

        // 3. EXECUTA A BUSINESS: Atualiza, cria ou remove o acesso do usuário de forma dinâmica
        business.processarRegrasDeAcesso(voluntario, dto);

        // 4. Salva no banco. O Hibernate cuidará de atualizar a tabela 'voluntarios'
        // e espelhar as alterações na tabela 'usuarios' via CascadeType.ALL
        return repository.save(voluntario);
    }

    @Transactional
    public void desativar(Long id) {
        var voluntario = repository.findById(id)
                .orElseThrow(() -> new BusinessException("Voluntário não encontrado"));

        if (!voluntario.getAtivo()) {
            throw new BusinessException("Este voluntário já se encontra inativo no sistema.");
        }

        voluntario.setAtivo(false);
        repository.save(voluntario);
    }

    @Transactional(readOnly = true)
    public VoluntarioResponseDTO buscarPorId(Long idVoluntario) {
         VoluntarioEntidade voluntarioEntidade = repository.findById(idVoluntario)
                .orElseThrow(() ->
                        new BusinessException("Voluntário não encontrado com o ID: " + idVoluntario));
        return new VoluntarioResponseDTO(voluntarioEntidade);
    }
}