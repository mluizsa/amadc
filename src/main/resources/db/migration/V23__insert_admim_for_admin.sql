WITH permissao_existente AS (
INSERT INTO public.permissoes (perm_chave, perm_descricao, data_criacao, data_alteracao, registrado_por, alterado_por)
SELECT 'ADMIN', 'ACESSO COMPLETO', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL
    WHERE NOT EXISTS (
        SELECT 1 FROM public.permissoes WHERE perm_chave = 'ADMIN'
    )
    RETURNING perm_id
),
permissao_final AS (
SELECT perm_id FROM permissao_existente
UNION ALL
SELECT perm_id FROM public.permissoes WHERE perm_chave = 'ADMIN' AND NOT EXISTS (SELECT 1 FROM permissao_existente)
    )

INSERT INTO public.perfil_permissoes (perf_id, perm_id)
SELECT 1, perm_id FROM permissao_final
WHERE NOT EXISTS (
    SELECT 1 FROM public.perfil_permissoes
    WHERE perf_id = 1 AND perm_id = (SELECT perm_id FROM permissao_final)
);