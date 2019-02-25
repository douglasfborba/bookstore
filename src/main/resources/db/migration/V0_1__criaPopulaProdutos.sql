CREATE TABLE dis_produto (
	id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    codigo VARCHAR(50) UNIQUE NOT NULL,
    descricao VARCHAR(50) NOT NULL,
    preco DECIMAL(30, 2) NOT NULL,
    data_cadastro DATE NOT NULL
);

INSERT INTO dis_produto (codigo, descricao, preco, data_cadastro) VALUES ('LIV0112457', 'LIVRO O SENHOR DOS ANEIS', 82.50, '2000-02-01');
INSERT INTO dis_produto (codigo, descricao, preco, data_cadastro) VALUES ('LIV0181246', 'LIVRO VIAGEM A LUA', 35.00, '2007-06-15');