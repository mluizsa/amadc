-- Adiciona coluna de voluntário na tabela de atendimentos
ALTER TABLE atendimentos ADD COLUMN IF NOT EXISTS volu_id BIGINT;

-- Adiciona a constraint de chave estrangeira de forma segura
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_atendimento_voluntario') THEN
        ALTER TABLE atendimentos
            ADD CONSTRAINT fk_atendimento_voluntario 
            FOREIGN KEY (volu_id) REFERENCES voluntarios (volu_id);
    END IF;
END $$;

-- Adiciona a coluna na tabela de auditoria
ALTER TABLE atendimentos_aud ADD COLUMN IF NOT EXISTS volu_id BIGINT;