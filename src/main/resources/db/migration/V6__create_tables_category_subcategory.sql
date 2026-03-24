CREATE TABLE categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    slug VARCHAR(50) UNIQUE NOT NULL,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE subcategories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    slug VARCHAR(50) UNIQUE NOT NULL,
    category_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE products ADD COLUMN subcategory_id BIGINT NOT NULL;
ALTER TABLE products ADD CONSTRAINT fk_products_subcategory_id
FOREIGN KEY (subcategory_id) REFERENCES subcategories(id);

ALTER TABLE subcategories ADD CONSTRAINT fk_subcategories_category_id
FOREIGN KEY (category_id) REFERENCES categories(id);

CREATE INDEX idx_subcategories_category_id ON subcategories(category_id);
CREATE INDEX idx_products_subcategory_id ON products(subcategory_id);

