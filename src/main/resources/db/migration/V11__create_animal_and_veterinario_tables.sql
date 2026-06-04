-- Tabela de Veterinários
CREATE TABLE IF NOT EXISTS veterinarios (
    vete_id BIGSERIAL PRIMARY KEY,
    vete_nome VARCHAR(255) NOT NULL,
    vete_crmv VARCHAR(50),
    vete_telefone VARCHAR(20),
    vete_especialidade VARCHAR(100),
    vete_observacoes_parceria TEXT,
    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_alteracao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    registrado_por VARCHAR(255),
    alterado_por VARCHAR(255)
);

-- Auditoria de Veterinários
CREATE TABLE IF NOT EXISTS veterinarios_aud (
    vete_id BIGINT NOT NULL,
    rev BIGINT NOT NULL,
    revtype SMALLINT NOT NULL,
    vete_nome VARCHAR(255),
    vete_crmv VARCHAR(50),
    vete_telefone VARCHAR(20),
    vete_especialidade VARCHAR(100),
    vete_observacoes_parceria TEXT,
    PRIMARY KEY (vete_id, rev),
    CONSTRAINT fk_veterinarios_aud_revinfo FOREIGN KEY (rev) REFERENCES revinfo_custom (id)
);

-- Tabela de Animais
CREATE TABLE IF NOT EXISTS animais (
    anim_id BIGSERIAL PRIMARY KEY,
    anim_nome VARCHAR(255) NOT NULL,
    anim_especie VARCHAR(50),
    anim_raca VARCHAR(100),
    anim_porte VARCHAR(50),
    anim_data_resgate DATE,
    anim_peso_entrada DECIMAL(10,2),
    stan_id BIGINT,
    anim_possivel_adocao BOOLEAN DEFAULT TRUE,
    anim_historia TEXT,
    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_alteracao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    registrado_por VARCHAR(255),
    alterado_por VARCHAR(255)
);

-- Auditoria de Animais
CREATE TABLE IF NOT EXISTS animais_aud (
    anim_id BIGINT NOT NULL,
    rev BIGINT NOT NULL,
    revtype SMALLINT NOT NULL,
    anim_nome VARCHAR(255),
    anim_especie VARCHAR(50),
    anim_raca VARCHAR(100),
    anim_porte VARCHAR(50),
    anim_data_resgate DATE,
    stan_id BIGINT,
    anim_possivel_adocao BOOLEAN,
    anim_historia TEXT,
    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_alteracao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    registrado_por VARCHAR(255),
    alterado_por VARCHAR(255),
    PRIMARY KEY (anim_id, rev),
    CONSTRAINT fk_animais_aud_revinfo FOREIGN KEY (rev) REFERENCES revinfo_custom (id)
);

-- Tabela de Fotos (Relacionamento ElementCollection)
CREATE TABLE IF NOT EXISTS animal_fotos (
    animal_id BIGINT NOT NULL,
    url_foto TEXT NOT NULL,
    CONSTRAINT fk_animal_fotos_animal FOREIGN KEY (animal_id) REFERENCES animais (anim_id)
);

-- Tabela de Atendimentos (Prontuário)
CREATE TABLE IF NOT EXISTS atendimentos (
    aten_id BIGSERIAL PRIMARY KEY,
    anim_id BIGINT NOT NULL,
    vete_id BIGINT,
    aten_data_atendimento TIMESTAMP,
    tiat_id BIGINT,
    aten_descricao TEXT,
    aten_peso_na_data DECIMAL(10,2),
    aten_valor_cobrado DECIMAL(10,2),
    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_alteracao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    registrado_por VARCHAR(255),
    alterado_por VARCHAR(255),
    CONSTRAINT fk_atendimento_animal FOREIGN KEY (anim_id) REFERENCES animais (anim_id),
    CONSTRAINT fk_atendimento_veterinario FOREIGN KEY (vete_id) REFERENCES veterinarios (vete_id)
);

-- Auditoria de Atendimentos
CREATE TABLE IF NOT EXISTS atendimentos_aud (
    aten_id BIGINT NOT NULL,
    rev BIGINT NOT NULL,
    revtype SMALLINT NOT NULL,
    anim_id BIGINT NOT NULL,
    vete_id BIGINT,
    aten_data_atendimento TIMESTAMP,
    tiat_id BIGINT,
    aten_descricao TEXT,
    aten_valor_cobrado DECIMAL(10,2),
    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_alteracao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    registrado_por VARCHAR(255),
    alterado_por VARCHAR(255),
    PRIMARY KEY (aten_id, rev),
    CONSTRAINT fk_atendimentos_aud_revinfo FOREIGN KEY (rev) REFERENCES revinfo_custom (id)
);