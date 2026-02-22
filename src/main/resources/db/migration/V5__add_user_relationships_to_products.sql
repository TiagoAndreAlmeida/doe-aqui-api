-- Adiciona a coluna para o doador (obrigatória)
ALTER TABLE products ADD COLUMN donor_id BIGINT NOT NULL;

-- Adiciona a coluna para o receptor (opcional)
ALTER TABLE products ADD COLUMN receiver_id BIGINT;

-- Adiciona a chave estrangeira para o doador
-- Isso garante que todo produto tenha um doador válido na tabela de usuários
ALTER TABLE products ADD CONSTRAINT fk_products_users_donor
FOREIGN KEY (donor_id) REFERENCES users(id);

-- Adiciona a chave estrangeira para o receptor
-- Isso garante que, se um produto tiver um receptor, ele seja um usuário válido
ALTER TABLE products ADD CONSTRAINT fk_products_users_receiver
FOREIGN KEY (receiver_id) REFERENCES users(id);

-- Cria um índice na coluna do doador para otimizar as consultas de junção (JOIN)
CREATE INDEX idx_products_donor_id ON products(donor_id);

-- Cria um índice na coluna do receptor para otimizar as consultas de junção (JOIN)
CREATE INDEX idx_products_receiver_id ON products(receiver_id);
