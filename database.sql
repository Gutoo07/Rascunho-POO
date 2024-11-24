--use master
--drop database trabalhobd
--drop table comanda
--drop table comanda_produto
--drop table cliente
--drop table produto

SELECT *
FROM [dbo].[cliente]

SELECT *
FROM [dbo].comanda

SELECT *
FROM [dbo].comanda_produto

SELECT *
FROM [dbo].produto





--------------------------------------
CREATE DATABASE trabalhobd
GO
--------------------------------------------------------------------
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
	nome	varchar(30),
	valor	decimal(7,2)
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

--Valor Total da Comanda
SELECT co.id as comanda, SUM(cop.qtd * p.valor) AS valor_total
FROM comanda co
INNER JOIN comanda_produto cop
ON cop.comandaId = co.id
INNER JOIN produto p
ON cop.produtoId = p.id
--WHERE co.id = 2
GROUP BY co.id

--Valor Total dos Produtos em uma Comanda
select SUM(cop.qtd * p.valor) as valor_total_produto
from comanda co
inner join comanda_produto cop
on co.id = cop.comandaId
inner join produto p
on cop.produtoId = p.id
--where co.id = 0 and p.id = 1
group by p.id, co.id
--order by co.id

--Total de Comandas Abertas
select COUNT(co.id) as comandas_abertas
from comanda co

--Total Consumido de Todas as Comandas
SELECT SUM(cop.qtd * p.valor) AS total_comandas
FROM comanda co
INNER JOIN comanda_produto cop
ON cop.comandaId = co.id
INNER JOIN produto p
ON cop.produtoId = p.id

--Select Comandas Vazias
SELECT co.id
FROM comanda co
LEFT OUTER JOIN comanda_produto cop
ON co.id = cop.comandaId
WHERE cop.comandaId IS NULL -- and co.id = 3

--Pesquisar produto que nao esta em nenhuma comanda
SELECT p.id
FROM produto p
LEFT OUTER JOIN comanda_produto cop
ON p.id = cop.produtoId
WHERE cop.produtoId IS NULL AND p.id = 6

--Pesquisar quantas unidades de um produto foram consumidas atraves de todas as comandas
SELECT COUNT(p.id) as qtd
FROM produto p
INNER JOIN comanda_produto cop
ON p.id = cop.produtoId
WHERE p.id = 4
GROUP BY p.id, p.nome, p.valor
ORDER BY p.id

--Pesquisar cliente sem comandas abertas
SELECT c.id
FROM cliente c
LEFT OUTER JOIN comanda co
ON c.id = co.clienteId
WHERE co.clienteId IS NULL


select 0 as zero

select * from comanda
select * from comanda_produto
select * from cliente
select * from produto



delete from cliente