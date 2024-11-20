--drop database trabalhobd
--use master
--drop table comanda
--drop table comanda_produto
--drop table cliente
--drop table produto

CREATE DATABASE trabalhobd
GO
USE trabalhobd
GO
CREATE TABLE cliente(
	id			int,
	nome		varchar(100),
	telefone	char(11),
	cpf			char(11)	null
	PRIMARY KEY (id)
)
GO
CREATE TABLE comanda(
	id			int,
	valorTotal	decimal(7,2)	default(0.0),
	valorPago	decimal(7,2)	default(0.0),
	clienteId	int				null
	PRIMARY KEY (id)
	FOREIGN KEY (clienteId) REFERENCES cliente(id)
)
GO
CREATE TABLE produto(
	id		int,
	valor	decimal(4,2),
	nome	varchar(30)
	PRIMARY KEY (id)
) 
GO
CREATE TABLE comanda_produto(
	produtoId		int,
	comandaId		int,
	qtd				int			default 0
	PRIMARY KEY (produtoId, comandaId)
	FOREIGN KEY (produtoId) REFERENCES produto(id),
	FOREIGN KEY (comandaID) REFERENCES comanda(id)
)
GO
insert into comanda(id) values
(1)
GO
select  * from comanda_produto