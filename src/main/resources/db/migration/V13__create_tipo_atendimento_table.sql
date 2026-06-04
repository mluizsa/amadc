-- Tabela de Tipos de Atendimento
CREATE TABLE IF NOT EXISTS tipo_atendimento (
    tiat_id BIGSERIAL PRIMARY KEY,
    tiat_descricao VARCHAR(100) NOT NULL UNIQUE,
    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_alteracao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    registrado_por VARCHAR(255),
    alterado_por VARCHAR(255)
);

-- Auditoria de Tipos de Atendimento
CREATE TABLE IF NOT EXISTS tipo_atendimento_aud (
    tiat_id BIGINT NOT NULL,
    rev BIGINT NOT NULL,
    revtype SMALLINT NOT NULL,
    tiat_descricao VARCHAR(100),
    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_alteracao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    registrado_por VARCHAR(255),
    alterado_por VARCHAR(255),
    PRIMARY KEY (tiat_id, rev),
    CONSTRAINT fk_tipo_atendimento_aud_revinfo FOREIGN KEY (rev) REFERENCES revinfo_custom (id)
);

-- Carga inicial de tipos comuns
INSERT INTO tipo_atendimento (tiat_descricao, registrado_por) VALUES 
('CONSULTA_ROTINA', 'SISTEMA'),
('VACINACAO', 'SISTEMA'),
('CASTRACAO', 'SISTEMA'),
('CIRURGIA', 'SISTEMA'),
('EXAME_LABORATORIAL', 'SISTEMA'),
('URGENCIA_EMERGENCIA', 'SISTEMA')
ON CONFLICT (tiat_descricao) DO NOTHING;

-- Garante que a coluna de referência existe na tabela de atendimentos
ALTER TABLE atendimentos ADD COLUMN IF NOT EXISTS tiat_id BIGINT;

-- Adiciona a nova coluna de FK de forma segura
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_atendimentos_tipo') THEN
        ALTER TABLE atendimentos 
            ADD CONSTRAINT fk_atendimentos_tipo 
            FOREIGN KEY (tiat_id) 
            REFERENCES tipo_atendimento (tiat_id);
    END IF;
END $$;

-- Define como obrigatória após a criação (assumindo que novos registros precisarão de um tipo)
ALTER TABLE atendimentos ALTER COLUMN tiat_id SET NOT NULL;