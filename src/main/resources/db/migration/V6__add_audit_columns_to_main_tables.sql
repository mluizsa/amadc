-- Adicionando colunas de auditoria na tabela USUARIOS
ALTER TABLE usuarios
    ADD COLUMN data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ADD COLUMN data_alteracao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ADD COLUMN registrado_por VARCHAR(255),
    ADD COLUMN alterado_por VARCHAR(255);

-- Adicionando colunas de auditoria na tabela PERFIS
ALTER TABLE perfis
    ADD COLUMN data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ADD COLUMN data_alteracao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ADD COLUMN registrado_por VARCHAR(255),
    ADD COLUMN alterado_por VARCHAR(255);

-- Adicionando colunas de auditoria na tabela PERMISSOES
ALTER TABLE permissoes
    ADD COLUMN data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ADD COLUMN data_alteracao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ADD COLUMN registrado_por VARCHAR(255),
    ADD COLUMN alterado_por VARCHAR(255);