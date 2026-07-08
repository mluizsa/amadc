-- Auditoria de Usuários
CREATE TABLE usuarios_aud (
    usu_id BIGINT NOT NULL,
    rev BIGINT NOT NULL,
    revtype SMALLINT,
    usu_username varchar(255),
    usu_password varchar(255),
    usu_email varchar(255),
    usu_ativo boolean,
    data_criacao timestamp,
    data_alteracao timestamp,
    registrado_por varchar(255),
    alterado_por varchar(255),
    PRIMARY KEY (usu_id, rev),
    CONSTRAINT fk_usuarios_aud_revinfo FOREIGN KEY (rev) REFERENCES revinfo_custom (id)
);

-- Auditoria de Perfis
CREATE TABLE perfis_aud (
    perf_id BIGINT NOT NULL,
    rev BIGINT NOT NULL,
    revtype SMALLINT,
    perf_nome varchar(255),
    data_criacao timestamp,
    data_alteracao timestamp,
    registrado_por varchar(255),
    alterado_por varchar(255),
    PRIMARY KEY (perf_id, rev),
    CONSTRAINT fk_perfis_aud_revinfo FOREIGN KEY (rev) REFERENCES revinfo_custom (id)
);

-- Auditoria de Permissões
CREATE TABLE permissoes_aud (
    perm_id BIGINT NOT NULL,
    rev BIGINT NOT NULL,
    revtype SMALLINT,
    perm_chave varchar(255),
    perm_descricao varchar(255),
    data_criacao timestamp,
    data_alteracao timestamp,
    registrado_por varchar(255),
    alterado_por varchar(255),
    PRIMARY KEY (perm_id, rev),
    CONSTRAINT fk_permissoes_aud_revinfo FOREIGN KEY (rev) REFERENCES revinfo_custom (id)
);

-- Se houver tabelas de relacionamento (ManyToMany), o Envers também audita:
CREATE TABLE usuario_perfis_aud (
    rev BIGINT NOT NULL,
    usu_id BIGINT NOT NULL,
    perf_id BIGINT NOT NULL,
    revtype SMALLINT,
    PRIMARY KEY (rev, usu_id, perf_id),
    CONSTRAINT fk_usuario_perfis_aud_revinfo FOREIGN KEY (rev) REFERENCES revinfo_custom (id)
);