ALTER TABLE public.usuarios ADD COLUMN volu_id BIGINT UNIQUE;

-- Cria a restrição de Chave Estrangeira (FK)
ALTER TABLE public.usuarios ADD CONSTRAINT fk_usuario_voluntario
        FOREIGN KEY (volu_id) REFERENCES public.voluntarios(volu_id)
            ON DELETE SET NULL;