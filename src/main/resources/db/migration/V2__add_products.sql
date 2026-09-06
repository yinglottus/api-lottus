CREATE TABLE produtos(

    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,

    nome VARCHAR(255) NOT NULL,

    descricao VARCHAR(255) NOT NULL,

    preco NUMERIC(10, 2) NOT NULL,
    
    ativo BOOLEAN NOT NULL DEFAULT TRUE,

    CONSTRAINT chk_produto_preco
        CHECK (preco >= 0),

    CONSTRAINT chk_preco_quantidade
        CHECK (quantidade >= 0)
);