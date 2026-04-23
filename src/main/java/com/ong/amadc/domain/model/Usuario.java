package com.ong.amadc.domain.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usu_id")
    private Long id;

    @Column(name = "usu_username", nullable = false, unique = true)
    private String username; // Nome do atributo na classe

    @Column(name = "usu_password", nullable = false)
    private String password;

    @Column(name = "usu_email", nullable = false, unique = true)
    private String email;

    @Column(name = "usu_ativo")
    private Boolean ativo;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "usuario_perfis",
            joinColumns = @JoinColumn(name = "usu_id"),
            inverseJoinColumns = @JoinColumn(name = "perf_id")
    )
    private Set<Perfil> perfis;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return perfis.stream()
                .flatMap(perfil -> perfil.getPermissoes().stream())
                .map(permissao -> new SimpleGrantedAuthority(permissao.getChave()))
                .collect(Collectors.toSet());
    }

    // CORREÇÃO: O retorno deve ser o atributo 'username' definido acima
    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public boolean isAccountNonExpired() { return true; }
    @Override
    public boolean isAccountNonLocked() { return true; }
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    @Override
    public boolean isEnabled() { return this.ativo; }
}