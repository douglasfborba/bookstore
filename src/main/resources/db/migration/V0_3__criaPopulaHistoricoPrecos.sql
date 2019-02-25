CREATE TABLE dis_historico (
	id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
	data_hora_alteracao TIMESTAMP NOT NULL,
	preco DECIMAL(38,2) NOT NULL,
	produto_id BIGINT NOT NULL
);

ALTER TABLE dis_historico ADD CONSTRAINT fk_hist_prod FOREIGN KEY (produto_id) REFERENCES dis_produto (id);

INSERT INTO dis_historico (data_hora_alteracao, preco, produto_id) VALUES (CURTIME(), 35.00, 1);
INSERT INTO dis_historico (data_hora_alteracao, preco, produto_id) VALUES (CURTIME(), 42.90, 2);