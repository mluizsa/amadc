CREATE TABLE permissoes (
    perm_id BIGSERIAL PRIMARY KEY, -- Alterado para BIGSERIAL
    perm_chave VARCHAR(50) NOT NULL UNIQUE,
    perm_descricao VARCHAR(255)
);

CREATE TABLE perfis (
    perf_id BIGSERIAL PRIMARY KEY, -- Alterado para BIGSERIAL
    perf_nome VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE perfil_permissoes (
    perf_id BIGINT REFERENCES perfis(perf_id), -- Deve ser BIGINT para bater com o PK
    perm_id BIGINT REFERENCES permissoes(perm_id), -- Deve ser BIGINT para bater com o PK
    PRIMARY KEY (perf_id, perm_id)
);

CREATE TABLE usuarios (
    usu_id BIGSERIAL PRIMARY KEY, -- Alterado para BIGSERIAL
    usu_username VARCHAR(50) NOT NULL UNIQUE,
    usu_password VARCHAR(255) NOT NULL,
    usu_email VARCHAR(100) NOT NULL UNIQUE,
    usu_ativo BOOLEAN DEFAULT TRUE
);

CREATE TABLE usuario_perfis (
    usu_id BIGINT REFERENCES usuarios(usu_id), -- Deve ser BIGINT
    perf_id BIGINT REFERENCES perfis(perf_id), -- Deve ser BIGINT
    PRIMARY KEY (usu_id, perf_id)
);