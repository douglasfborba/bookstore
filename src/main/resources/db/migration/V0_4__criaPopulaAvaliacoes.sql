CREATE TABLE dis_avaliacao (
	id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
	codigo VARCHAR(100) UNIQUE NOT NULL,
	valor DECIMAL(38, 2) NOT NULL,
	usuario VARCHAR(50) NOT NULL,
	comentario_id BIGINT NULL,
	produto_id BIGINT NULL
);

ALTER TABLE dis_avaliacao ADD CONSTRAINT FOREIGN KEY (comentario_id) REFERENCES dis_comentario (id);
ALTER TABLE dis_avaliacao ADD CONSTRAINT FOREIGN KEY (produto_id) REFERENCES dis_produto (id);

INSERT INTO dis_avaliacao (codigo, valor, usuario, comentario_id, produto_id) VALUES ('MDFKVUwtMTEhGM27846MzE6NDA==', 3.0, 'beltrano.teste', NULL, 1);

ALTER TABLE dis_avaliacao RENAME COLUMN `valor` TO `rating`;

INSERT INTO dis_avaliacao (codigo, rating, usuario, comentario_id, produto_id) VALUES ('HRFKVUwtmTHt34Kmgf7846MztFGOP==', 5.0, 'beltrano.teste', 2, NULL);
INSERT INTO dis_avaliacao (codigo, rating, usuario, comentario_id, produto_id) VALUES ('B23MNFwtmTHt346MztFxRJKPxC06==', 4.5, 'fulano.teste', NULL, 1);