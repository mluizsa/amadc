-- 1. Tabela auxiliar (mantém igual)
CREATE TABLE IF NOT EXISTS public.tipos_vinculo_arquivo (
    tva_id varchar(50) NOT NULL,
    tva_descricao varchar(255) NOT NULL,
    data_criacao timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT tipos_vinculo_arquivo_pkey PRIMARY KEY (tva_id)
);
INSERT INTO public.tipos_vinculo_arquivo (tva_id, tva_descricao) VALUES
    ('ANIMAL_EVOLUCAO', 'Linha do Tempo / Evolução do Animal'),
    ('ATENDIMENTO', 'Prontuário / Atendimento Médico'),
    ('VOLUNTARIO', 'Documentação do Trabalho Voluntário'),
    ('VISTORIA_ADOCAO', 'Vistoria de Habitação para Adoção')
    ON CONFLICT (tva_id) DO NOTHING;


CREATE TABLE IF NOT EXISTS public.arquivos_sistema (
    arq_id bigserial NOT NULL,
    arq_url varchar(1000) NOT NULL,
    arq_nome_original varchar(255) NULL,
    tva_id varchar(50) NOT NULL,

    -- Relacionamentos
    anim_id int8 NULL,
    aten_id int8 NULL,
    volu_id int8 NULL,

    data_criacao timestamp NOT NULL,
    data_alteracao timestamp NOT NULL,
    registrado_por varchar(255) NULL,
    alterado_por varchar(255) NULL,

    CONSTRAINT arquivos_sistema_pkey PRIMARY KEY (arq_id),
    CONSTRAINT fk_arquivos_tipo_vinculo FOREIGN KEY (tva_id) REFERENCES public.tipos_vinculo_arquivo(tva_id),
    CONSTRAINT fk_arquivos_animal FOREIGN KEY (anim_id) REFERENCES public.animais(anim_id)
);