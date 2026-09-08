INSERT INTO public.permissoes (perm_chave, perm_descricao, data_criacao, data_alteracao, registrado_por)
SELECT 'ATENDIMENTO_READ', 'Visualizar listagem e detalhes de atendimentos', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SISTEMA'
    WHERE NOT EXISTS (
    SELECT 1 FROM public.permissoes WHERE perm_chave = 'ATENDIMENTO_READ'
);

INSERT INTO public.permissoes (perm_chave, perm_descricao, data_criacao, data_alteracao, registrado_por)
SELECT 'ATENDIMENTO_WRITE', 'Cadastrar e atualizar atendimentos médicos', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SISTEMA'
    WHERE NOT EXISTS (
    SELECT 1 FROM public.permissoes WHERE perm_chave = 'ATENDIMENTO_WRITE'
);

