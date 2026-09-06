CREATE TABLE produto_imagens (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    imagem_url VARCHAR(500) NOT NULL,
    produto_id BIGINT NOT NULL,

    CONSTRAINT fk_produto_imagens_produto
        FOREIGN KEY (produto_id)
        REFERENCES produtos(id)
);