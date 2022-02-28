create schema devin_sales; 

create table usuario (
	id bigserial primary key,
	login varchar(50),
	senha varchar(255),
	nome varchar(70),
	dt_nascimento date
);

create table feature (
	id bigserial primary key,
	nome_feature varchar(100)
);

create table usuario_feature(
	id_usuario bigint,
	id_feature bigint,
	read boolean,
	write boolean ,
	
	constraint pk_us_ft primary key (id_usuario, id_feature),
	constraint fk_us_ft_user foreign key (id_usuario) references usuario(id),
	constraint fk_us_ft_feature foreign key (id_feature) references feature(id)
);

select  * from usuario ;

insert into feature (nome_feature) values 
	('usuario'),
	('estado'),
	('cidade'),
	('endereco'),
	('venda'),
	('produto'),
	('entrega')	;

insert into usuario (login, senha, nome, dt_nascimento) values ('admin', 'admin123', 'Administrador do DEVin Sales', '1991-02-08');

insert into usuario_feature (id_usuario, id_feature, read, write) values 
	(1,1,true,true),
	(1,2,true,true),
	(1,3,true,true),
	(1,4,true,true),
	(1,5,true,true),
	(1,6,true,true),
	(1,7,true,true)
;

create table produto (
	id bigserial primary key,
	nome varchar (150),
	preco_sugerido decimal (10,2)
);

create table endereco (
	id bigserial primary key ,
	rua varchar(150),
	numero int,
	complemento varchar(255)
);