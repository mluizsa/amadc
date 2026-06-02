-- Tabela de Status do Animal
CREATE TABLE status_animal (
    stan_id BIGSERIAL PRIMARY KEY,
    stan_descricao VARCHAR(50) NOT NULL UNIQUE,
    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_alteracao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    registrado_por VARCHAR(255),
    alterado_por VARCHAR(255)
);

-- Tabela de Auditoria de Status
CREATE TABLE status_animal_aud (
    stan_id BIGINT NOT NULL,
    rev BIGINT NOT NULL,
    revtype SMALLINT NOT NULL,
    stan_descricao VARCHAR(50),
    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_alteracao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    registrado_por VARCHAR(255),
    alterado_por VARCHAR(255),
    PRIMARY KEY (stan_id, rev),
    CONSTRAINT fk_status_animal_aud_revinfo FOREIGN KEY (rev) REFERENCES revinfo_custom (id)
);

-- Carga Inicial de Status (baseado no Enum anterior)
INSERT INTO status_animal (stan_descricao, registrado_por) VALUES 
('RESGATADO', 'SISTEMA'),
('EM_TRATAMENTO', 'SISTEMA'),
('DISPONIVEL_PARA_ADOCAO', 'SISTEMA'),
('EM_ADAPTACAO', 'SISTEMA'),
('ADOTADO', 'SISTEMA'),
('FALECIDO', 'SISTEMA'),
('QUARENTENA', 'SISTEMA');

-- Ajuste na tabela de Animais para usar FK
ALTER TABLE animais ADD CONSTRAINT fk_animais_status FOREIGN KEY (stan_id) REFERENCES status_animal (stan_id);

-- Garantir que a coluna não seja nula após a carga inicial
ALTER TABLE animais ALTER COLUMN stan_id SET NOT NULL;