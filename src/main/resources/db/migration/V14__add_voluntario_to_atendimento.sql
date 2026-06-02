-- Adiciona coluna de voluntário na tabela de atendimentos
ALTER TABLE atendimentos ADD COLUMN volu_id BIGINT;

-- Adiciona a constraint de chave estrangeira
ALTER TABLE atendimentos
    ADD CONSTRAINT fk_atendimento_voluntario 
    FOREIGN KEY (volu_id) REFERENCES voluntarios (volu_id);

-- Adiciona a coluna na tabela de auditoria
ALTER TABLE atendimentos_aud ADD COLUMN volu_id BIGINT;