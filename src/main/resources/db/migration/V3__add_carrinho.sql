CREATE TABLE carrinhos(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    cart_token VARCHAR(255) UNIQUE,
    sub_total NUMERIC(10, 2) NOT NULL,
    usuario_id BIGINT UNIQUE,

    CONSTRAINT fk_carrinho_usuario
        FOREIGN KEY (usuario_id)
        REFERENCES usuarios(id)
        ON DELETE CASCADE,

    CONSTRAINT chk_carrinho_sub_total
        CHECK (sub_total >= 0)
);