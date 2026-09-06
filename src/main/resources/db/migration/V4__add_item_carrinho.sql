CREATE TABLE item_carrinho (

    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,

    quantidade INTEGER NOT NULL,

    carrinho_id BIGINT NOT NULL,

    produto_id BIGINT NOT NULL,

    CONSTRAINT fk_item_carrinho_carrinho
        FOREIGN KEY (carrinho_id)
        REFERENCES carrinhos(id),

    CONSTRAINT fk_item_carrinho_produto
        FOREIGN KEY (produto_id)
        REFERENCES produtos(id),

    CONSTRAINT uk_item_carrinho_produto
        UNIQUE (produto_id, carrinho_id),

    CONSTRAINT chk_item_carrinho_quantidade
        CHECK (quantidade > 0)
);