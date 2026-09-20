CREATE TABLE cupons (

    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    codigo VARCHAR(255) NOT NULL UNIQUE,
    desconto NUMERIC(10, 2) NOT NULL,
    quantidade INTEGER NOT NULL
);

ALTER TABLE carrinhos ADD COLUMN cupom_id BIGINT;
ALTER TABLE carrinhos ADD CONSTRAINT fk_carrinhos_cupom
    FOREIGN KEY(cupom_id)   
    REFERENCES cupons(id);