-- Inserindo Permissões (Recursos)
INSERT INTO permissoes (perm_chave, perm_descricao) VALUES
  ('CLINICA_WRITE', 'Cadastrar clínicas parceiras'),
  ('FINANCE_WRITE', 'Registrar entradas/saídas e compras'),
  ('FINANCE_READ', 'Visualizar relatórios financeiros'),
  ('ANIMAL_WRITE', 'Cadastrar e atualizar animais/prontuários'),
  ('VISIT_MANAGE', 'Agendar e gerenciar visitas'),
  ('ADOPTION_WRITE', 'Realizar doações e processos de retorno');

-- Inserindo Perfis e Vinculando Recursos
INSERT INTO perfis (perf_nome) VALUES ('ADMIN'), ('VOLUNTARIO_FINANCEIRO'), ('VOLUNTARIO_ANIMAIS');

-- Admin tem tudo
INSERT INTO perfil_permissoes (perf_id, perm_id)
SELECT (SELECT perf_id FROM perfis WHERE perf_nome = 'ADMIN'), perm_id FROM permissoes;

-- Voluntário Financeiro (Escrita e Leitura)
INSERT INTO perfil_permissoes (perf_id, perm_id)
SELECT (SELECT perf_id FROM perfis WHERE perf_nome = 'VOLUNTARIO_FINANCEIRO'), perm_id FROM permissoes WHERE perm_chave LIKE 'FINANCE_%';