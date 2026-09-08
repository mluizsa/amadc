CREATE TABLE IF NOT EXISTS public.custos_animais (
    cust_id BIGSERIAL PRIMARY KEY,
    anim_id BIGINT NOT NULL,
    aten_id BIGINT,
    cust_descricao VARCHAR(255) NOT NULL,
    cust_valor NUMERIC(19, 2) NOT NULL,
    cust_data TIMESTAMP NOT NULL,
    data_criacao TIMESTAMP,
    data_alteracao TIMESTAMP,
    registrado_por VARCHAR(255),
    alterado_por VARCHAR(255),
    CONSTRAINT fk_custos_animais_animal FOREIGN KEY (anim_id) REFERENCES public.animais(anim_id),
    CONSTRAINT fk_custos_animais_atendimento FOREIGN KEY (aten_id) REFERENCES public.tb_atendimentos(aten_id)
);