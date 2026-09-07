-- Tabela Principal de Voluntários
CREATE TABLE voluntarios (
     volu_id BIGSERIAL PRIMARY KEY,
     volu_nome VARCHAR(255) NOT NULL,
     volu_cpf VARCHAR(14) NOT NULL UNIQUE,
     volu_telefone VARCHAR(20),
     volu_email VARCHAR(100) UNIQUE,
     volu_data_nascimento DATE,
     volu_ocupacao VARCHAR(100),
     volu_observacoes TEXT,
     volu_ativo BOOLEAN DEFAULT TRUE,
     data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
     data_alteracao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
     registrado_por VARCHAR(255),
     alterado_por VARCHAR(255)
);

-- Tabela de Auditoria de Voluntários
CREATE TABLE voluntarios_aud (
     volu_id BIGINT NOT NULL,
     rev BIGINT NOT NULL,
     revtype SMALLINT NOT NULL,
     volu_nome VARCHAR(255),
     volu_cpf VARCHAR(14),
     volu_telefone VARCHAR(20),
     volu_email VARCHAR(100),
     volu_data_nascimento DATE,
     volu_ocupacao VARCHAR(100),
     volu_observacoes TEXT,
     volu_ativo BOOLEAN,
     data_criacao TIMESTAMP,
     data_alteracao TIMESTAMP,
     registrado_por VARCHAR(255),
     alterado_por VARCHAR(255),

     PRIMARY KEY (volu_id, rev),
     CONSTRAINT fk_voluntarios_aud_revinfo FOREIGN KEY (rev) REFERENCES revinfo_custom (id)
);