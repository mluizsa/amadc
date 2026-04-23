-- 1. Garante que os Perfis existam
INSERT INTO perfis (perf_nome) VALUES ('ADMIN') ON CONFLICT (perf_nome) DO NOTHING;

-- 2. Insere o Usuário (Senha: admin123)
-- O hash abaixo corresponde a 'admin123' usando BCrypt
INSERT INTO usuarios (usu_username, usu_password, usu_email, usu_ativo) VALUES ('admin', '$2a$10$M8dQmog5pl1Y0HoawYdMcuvq31Gq3RxJcKIyXZeFwODr1BMgbl9I.', 'admin@amadc.org', TRUE)
ON CONFLICT (usu_username) DO NOTHING;

-- 3. Vincula o Usuário ao Perfil ADMIN
INSERT INTO usuario_perfis (usu_id, perf_id)
VALUES (
    (SELECT usu_id FROM usuarios WHERE usu_username = 'admin'),
    (SELECT perf_id FROM perfis WHERE perf_nome = 'ADMIN')
);