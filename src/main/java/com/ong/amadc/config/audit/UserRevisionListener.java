package com.ong.amadc.config.audit;

import org.hibernate.envers.RevisionListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserRevisionListener implements RevisionListener {

    @Override
    public void newRevision(Object revisionEntity) {
        CustomRevisionEntity customEntity = (CustomRevisionEntity) revisionEntity;

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated()) {
            customEntity.setUsuario(auth.getName());
        } else {
            customEntity.setUsuario("SISTEMA");
        }
    }
}