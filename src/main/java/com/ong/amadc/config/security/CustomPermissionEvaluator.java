package com.ong.amadc.config.security;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;

public class CustomPermissionEvaluator implements PermissionEvaluator {

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        if (authentication == null || !authentication.isAuthenticated() || !(permission instanceof String)) {
            return false;
        }

        // Se o usuário tiver a role 'ADMIN', ele tem todas as permissões
        if (authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch("ADMIN"::equals)) {
            return true;
        }

        // Caso contrário, verifica se o usuário tem a permissão específica
        String requiredPermission = (String) permission;
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(requiredPermission::equals);
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        // Este método é para permissões baseadas em instâncias de objetos,
        // mas para o seu caso de permissões baseadas em strings (ANIMAL_READ, ANIMAL_WRITE),
        // podemos simplesmente chamar o outro método hasPermission.
        return hasPermission(authentication, null, permission);
    }
}
