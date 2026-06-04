-- Adiciona novos campos à tabela de animais
ALTER TABLE animais 
    ADD COLUMN IF NOT EXISTS anim_sexo VARCHAR(20),
    ADD COLUMN IF NOT EXISTS anim_idade_estimada VARCHAR(50),
    ADD COLUMN IF NOT EXISTS anim_pelagem_cor VARCHAR(100),
    ADD COLUMN IF NOT EXISTS anim_pelagem_tipo VARCHAR(50),
    ADD COLUMN IF NOT EXISTS anim_marcas_cicatrizes TEXT,
    ADD COLUMN IF NOT EXISTS anim_hora_resgate TIME,
    ADD COLUMN IF NOT EXISTS anim_local_resgate_bairro VARCHAR(100),
    ADD COLUMN IF NOT EXISTS anim_local_resgate_rua VARCHAR(255),
    ADD COLUMN IF NOT EXISTS anim_local_resgate_referencia VARCHAR(255),
    ADD COLUMN IF NOT EXISTS anim_origem VARCHAR(100),
    ADD COLUMN IF NOT EXISTS anim_resgatador_nome VARCHAR(255),
    ADD COLUMN IF NOT EXISTS anim_resgatador_contato VARCHAR(100),
    ADD COLUMN IF NOT EXISTS anim_condicao_entrada TEXT,
    ADD COLUMN IF NOT EXISTS anim_microchip VARCHAR(100);

-- Adiciona os mesmos campos à tabela de auditoria
ALTER TABLE animais_aud 
    ADD COLUMN IF NOT EXISTS anim_sexo VARCHAR(20),
    ADD COLUMN IF NOT EXISTS anim_idade_estimada VARCHAR(50),
    ADD COLUMN IF NOT EXISTS anim_pelagem_cor VARCHAR(100),
    ADD COLUMN IF NOT EXISTS anim_pelagem_tipo VARCHAR(50),
    ADD COLUMN IF NOT EXISTS anim_marcas_cicatrizes TEXT,
    ADD COLUMN IF NOT EXISTS anim_hora_resgate TIME,
    ADD COLUMN IF NOT EXISTS anim_local_resgate_bairro VARCHAR(100),
    ADD COLUMN IF NOT EXISTS anim_local_resgate_rua VARCHAR(255),
    ADD COLUMN IF NOT EXISTS anim_local_resgate_referencia VARCHAR(255),
    ADD COLUMN IF NOT EXISTS anim_origem VARCHAR(100),
    ADD COLUMN IF NOT EXISTS anim_resgatador_nome VARCHAR(255),
    ADD COLUMN IF NOT EXISTS anim_resgatador_contato VARCHAR(100),
    ADD COLUMN IF NOT EXISTS anim_condicao_entrada TEXT,
    ADD COLUMN IF NOT EXISTS anim_microchip VARCHAR(100);

-- Comentários para documentação de metadados (Opcional)
COMMENT ON COLUMN animais.anim_origem IS 'Origem do resgate: RUA, MAUS_TRATOS, ENTREGA_VOLUNTARIA';
COMMENT ON COLUMN animais.anim_sexo IS 'Sexo do animal: MACHO, FEMEA';
COMMENT ON COLUMN animais.anim_idade_estimada IS 'Idade aproximada: FILHOTE, ADULTO, IDOSO';