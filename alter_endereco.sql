ALTER TABLE endereco ADD COLUMN id_cidade BIGINT NOT NULL;
ALTER TABLE endereco ADD FOREIGN KEY (id_cidade) REFERENCES cidade(id);