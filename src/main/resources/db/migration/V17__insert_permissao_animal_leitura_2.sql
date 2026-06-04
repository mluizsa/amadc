-- Admin tem tudo
INSERT INTO perfil_permissoes (perf_id, perm_id)
SELECT (SELECT perf_id FROM perfis WHERE perf_nome = 'ADMIN'), perm_id FROM permissoes
WHERE perm_chave = 'ANIMAL_READ'
ON CONFLICT (perf_id, perm_id) DO NOTHING;

-- Voluntário Financeiro (Escrita e Leitura)
INSERT INTO perfil_permissoes (perf_id, perm_id)
SELECT (SELECT perf_id FROM perfis WHERE perf_nome = 'VOLUNTARIO_ANIMAIS'), perm_id FROM permissoes
WHERE perm_chave = 'ANIMAL_READ'
ON CONFLICT (perf_id, perm_id) DO NOTHING;