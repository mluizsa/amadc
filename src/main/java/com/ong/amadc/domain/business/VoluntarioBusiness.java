package com.ong.amadc.domain.business;

import com.ong.amadc.api.dto.request.VoluntarioRequestDTO;
import com.ong.amadc.domain.model.PerfilEntidade;
import com.ong.amadc.domain.model.UsuarioEntidade;
import com.ong.amadc.domain.model.VoluntarioEntidade;
import com.ong.amadc.domain.repository.PerfilRepository; // Importar
import com.ong.amadc.domain.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors; // Importar

@Component
@AllArgsConstructor
public class VoluntarioBusiness {

    private final PasswordEncoder passwordEncoder;
    private final UsuarioRepository usuarioRepository;
    private final PerfilRepository perfilRepository; // Injetar

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

    public void processarRegrasDeAcesso(VoluntarioEntidade voluntario, VoluntarioRequestDTO dto) {

        // Regra 1: Se o DTO indica que este voluntário NÃO deve ter acesso ao sistema
        if (dto.permitirAcesso() == null || !dto.permitirAcesso()) {
            // Se ele já tinha um usuário antes, nós desativamos o login por segurança
            if (voluntario.getUsuario() != null) {
                voluntario.getUsuario().setAtivo(false);
            }
            return;
        }

        // Regra 2: Se o voluntário DEVE ter acesso, validamos e preparamos as credenciais
        UsuarioEntidade usuario = voluntario.getUsuario();

        if (usuario == null) {
            // Cenário A: O voluntário não tinha acesso antes e ganhou AGORA (na edição)
            usuario = new UsuarioEntidade();
            usuario.setVoluntario(voluntario);
            usuario.setAtivo(true);

            // Criptografia obrigatória da nova senha informada
            String senhaCriptografada = passwordEncoder.encode(dto.senhaProvisoria());
            usuario.setPassword(senhaCriptografada);

            voluntario.setUsuario(usuario);
        } else {
            // Cenário B: O usuário já existia. Garantimos que ele está ativo.
            usuario.setAtivo(true);

            // SEGURANÇA NA EDIÇÃO: Só altera a senha se o operador digitou uma senha nova no campo
            if (dto.senhaProvisoria() != null && !dto.senhaProvisoria().isBlank()) {
                String novaSenhaCriptografada = passwordEncoder.encode(dto.senhaProvisoria());
                usuario.setPassword(novaSenhaCriptografada);
            }
        }

        // Regra 3: Atualização dos dados básicos do Usuário baseados no Voluntário
        usuario.setUsername(dto.username());
        usuario.setEmail(voluntario.getEmail()); // Sincroniza se o e-mail do voluntário mudou

        // Regra 4: Associação dos Perfis de Acesso (Roles) enviados pelo Front
        Set<PerfilEntidade> perfisMapeados = traduzirIdsParaPerfis(dto.perfilIds());
        usuario.setPerfis(perfisMapeados);
    }

    /**
     * Regra auxiliar para converter a lista de IDs vinda do Front-end em entidades gerenciadas.
     */
    private Set<PerfilEntidade> traduzirIdsParaPerfis(Set<Long> perfilIds) {
        if (perfilIds == null || perfilIds.isEmpty()) {
            return new HashSet<>();
        }
        Set<PerfilEntidade> perfis = new HashSet<>(perfilRepository.findAllById(perfilIds));
        if (perfis.size() != perfilIds.size()) {
            // Lança uma exceção se algum ID de perfil não foi encontrado
            throw new IllegalArgumentException("Um ou mais IDs de perfil são inválidos.");
        }
        return perfis;
    }


}