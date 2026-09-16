ALTER TABLE produtos
    ADD COLUMN categoria VARCHAR(50);

UPDATE produtos
    SET categoria = 'GERAL'
    WHERE categoria IS NULL;

ALTER TABLE produtos
    ALTER COLUMN categoria SET NOT NULL;

ALTER TABLE produtos
    ALTER COLUMN categoria SET DEFAULT 'GERAL';
