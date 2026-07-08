ALTER TABLE animais
ADD COLUMN anim_castrado BOOLEAN DEFAULT FALSE,
ADD COLUMN anim_data_castracao DATE,
ADD COLUMN anim_data_castracao_desconhecida BOOLEAN DEFAULT FALSE;

ALTER TABLE animais_aud
ADD COLUMN anim_castrado BOOLEAN,
ADD COLUMN anim_data_castracao DATE,
ADD COLUMN anim_data_castracao_desconhecida BOOLEAN;
