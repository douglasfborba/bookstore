CREATE TABLE dis_comentario (
	id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    codigo VARCHAR(100) UNIQUE NOT NULL,
    descricao VARCHAR(50) NOT NULL,
    usuario VARCHAR (50) NOT NULL,
    data_hora_criacao TIMESTAMP NOT NULL,
    produto_id BIGINT NOT NULL
);

ALTER TABLE dis_comentario ADD CONSTRAINT fk_cmtr_prod FOREIGN KEY (produto_id) REFERENCES dis_produto (id);

INSERT INTO dis_comentario (codigo, descricao, usuario, data_hora_criacao, produto_id) VALUES ('MDFKVUwtMTEgMTY6MzE6NDA=', 'Bom.', 'fulano.teste', CURTIME(), 1);
INSERT INTO dis_comentario (codigo, descricao, usuario, data_hora_criacao, produto_id) VALUES ('MDJKVUwtMTEgMTY6MzM6MTA=', 'Ótimo livro, recomento a todos.', 'beltrano.teste', CURTIME(), 1)