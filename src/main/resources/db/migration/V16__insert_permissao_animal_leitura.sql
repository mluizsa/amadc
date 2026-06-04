INSERT INTO permissoes (perm_chave, perm_descricao)
VALUES ('ANIMAL_READ', 'Leitura de animais cadastrados')
ON CONFLICT (perm_chave) DO NOTHING;