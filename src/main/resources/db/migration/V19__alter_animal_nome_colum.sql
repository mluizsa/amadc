-- 1. Corrige e reconstrói a coluna na tabela principal
ALTER TABLE animais
ALTER COLUMN anim_nome TYPE VARCHAR(255)
  USING TRIM(encode(anim_nome::bytea, 'escape'));

-- 2. Corrige e reconstrói a coluna na tabela de auditoria do Envers
ALTER TABLE animais_aud
ALTER COLUMN anim_nome TYPE VARCHAR(255)
  USING TRIM(encode(anim_nome::bytea, 'escape'));