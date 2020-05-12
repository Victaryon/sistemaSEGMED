<---------------------------------------------BD SEGMED POSTGRE (E ORACLE)---------------------------------------------------------------
<-- Os trigrers sao usados para o Oracle exclusivamente
<--------------------------------------------------SCHEMA/USUARIO SEGMED-------------------------------------------------------------------------------------
-- Cria tablespace p/ dados

create tablespace tbs_segmed
datafile 'C:\oraclexe\app\oracle\oradata\tbd_segmed.dbf' size 100M reuse
autoextend on next 10M maxsize 200M
online;


-- Cria usuario (dono das tabelas)
create user segmed --usuario
identified by "12345"  --senha
default tablespace tbs_segmed
temporary tablespace temp;

-- Cria e define a "role" de privilegios (perfil)
create role perfil_desenv;

grant
create cluster,
create database link,
create procedure,
create session,
create sequence,
create synonym,
create table,
create any type,
create trigger,
create view
to perfil_desenv;


grant alter session to perfil_desenv;


grant create trigger to segmed;


grant perfil_desenv to segmed;

grant unlimited tablespace to segmed;



<------------------------------------------------CLIENTES(EMPRESAS)------------------------------------------------------------
create table clientes(
	codigo_cliente integer not null,
	nome varchar(30),
	bairro varchar(30),
	cidade varchar(30),
	endereco varchar(30),
	uf varchar(2),
	cep varchar(30),
	telefone varchar(30),
	celular varchar(30),
	email varchar(30),
	cnpj varchar(30),

	constraint pk_clientes primary key(codigo_cliente)
);

create sequence seq_clientes
        start with 1
        increment by 1
        minvalue 1
        NOCACHE;

create or replace trigger trig_cliente
before insert on clientes
for each row
begin
    select seq_clientes.currval
    into :new.codigo_cliente
    from dual;
end;

<-----------------------------------------------COLABORADORES(FUNCIONARIOS DA EMPRESA)------------------------------------------

create table clientecolaborador(
	codigo_colaborador integer not null,
	codigo_cliente integer,
	nome varchar(30),
	cpf varchar(30),
	rg varchar(30),
	nascimento date,
	funcao varchar(30),
	telefone varchar(30),

	constraint pk_colaborador primary key(codigo_colaborador),
	constraint fk_cliente foreign key(codigo_cliente) references clientes
);

create sequence seq_colaborador
        start with 1
        increment by 1
        minvalue 1
        NOCACHE;

create or replace trigger trig_colaboradores
before insert on clientecolaborador
for each row
begin
    select seq_colaborador.currval
    into :new.codigo_colaborador
    from dual;
end;
<----------------------------------------------------------SERVIÇO---------------------------------------------------------------------
create table servico(
	codigo_servico integer not null,
	nome varchar(30),

	constraint pk_servico primary key(codigo_servico)
);



create sequence seq_servico
        start with 1
        increment by 1
        minvalue 1
        NOCACHE;

create or replace trigger trig_servico
before insert on servico
for each row
begin
    select seq_servico.currval
    into :new.codigo_servico
    from dual;
end;


<---------------------------------------------------------EXAME-------------------------------------------------------------------------
create table exame(
	codigo_exame serial not null,
	codigo_colaborador integer,
	codigo_cliente integer,
	data_exame date,
	valor_total float,
	tipo varchar(30),
	usuario varchar(30),

	constraint pk_exame primary key(codigo_exame),
	constraint fk_colaborador foreign key(codigo_colaborador) references clientecolaborador,
	constraint fk_cliente_exame foreign key(codigo_cliente) references clientes <--tive q mudar o nome fk_cliente, ver se nao bugou o codigo java
);

create sequence seq_exame
        start with 1
        increment by 1
        minvalue 1
        NOCACHE;

create or replace trigger trig_exame
before insert on exame
for each row
begin
    select seq_exame.currval
    into :new.codigo_exame
    from dual;
end;

SELECT * FROM exame;
<----------------------------------------------------------EXAMEITEM-------------------------------------------------------------------------

create table exameitem(
	codigo_exameitem integer not null,
	codigo_descricao integer,
	codigo_servico integer,
	codigo_exame integer,


	constraint pk_exameitem primary key(codigo_exameitem),
	constraint fk_servico_exameitem foreign key(codigo_servico) references servico on delete cascade,
	constraint fk_exame foreign key (codigo_exame) references exame on delete cascade,
	constraint fk_descricao foreign key (codigo_descricao) references descricao on delete cascade
);


create sequence seq_exameitem
        start with 1
        increment by 1
        minvalue 1
        NOCACHE;

create or replace trigger trig_exameitem
before insert on exameitem
for each row
begin
    select seq_exameitem.currval
    into :new.codigo_exameitem
    from dual;
end;

commit;

<------------------------------------------------------------------DESCRIÇÃO-----------------------------------------------------------------

create table descricao(
	codigo_descricao integer not null,
	codigo_cliente integer not null,
	codigo_servico integer not null,
	valor float,
	descricaotexto VARCHAR(4000),
	data_descricao date,

	constraint pk_descricao primary key(codigo_descricao),
	constraint fk_cliente_descricao foreign key(codigo_cliente) references clientes on delete cascade,
	constraint fk_servico_descricao foreign key(codigo_servico) references servico on delete cascade
);

create sequence seq_descricao
        start with 1
        increment by 1
        minvalue 1
        NOCACHE;

create or replace trigger trig_descricao
before insert on descricao
for each row
begin
    select seq_descricao.currval
    into :new.codigo_descricao
    from dual;
end;

SELECT * FROM descricao;
<-----------------------------------------------------------------TIPOEXAME-----------------------------------------------------------------

create table tipo_exame(
	codigo_tipo integer,
	tipo varchar(30),

	constraint pk_tipo primary key(codigo_tipo)
);

create sequence seq_tipo
        start with 1
        increment by 1
        minvalue 1;
insert into tipo_exame values(1,'Admissional');
insert into tipo_exame values(2,'Periodico');
insert into tipo_exame values(3,'Mudan�a de fun��o');
insert into tipo_exame values(4,'Retorno ao trabalho');
insert into tipo_exame values(5,'Demissional');

SELECT * FROM tipo_exame;

commit;
<---------------------------------------------------------------------LOGIN------------------------------------------------------------------

create table login_tabela(
	codigo_login integer not null,
	usuario varchar(30),
	senha varchar(30) default '12345',

	constraint pk_login primary key(codigo_login)
);

insert into login_tabela (codigo_login,usuario) values (1,'juliano');
insert into login_tabela (codigo_login,usuario) values (2,'jovenilia');


COMMIT;
<--------------------------------------------------------------OUTROS COMANDOS----------------------------------------------------------------

drop sequence seq_vendas;
drop sequence seq_descricao;

drop table vendas;
drop table descricao;

alter table exame rename constraint fk_cliente to fk_cliente_exame;

delete from exame;

update descricao
set data_descricao = '03/07/2017'
where codigo_descricao= 3;

select * from descricao where to_char(data_descricao,'MM/yyyy')='07/2017'; <--procura descricao pelo mês/ano

insert into descricao values(4,1,2,40,'Olá brasil','01/01/2018');

select * from descricao where
data_descricao= (select max(data_descricao) from descricao where codigo_cliente=2 and codigo_servico=4)
and codigo_cliente=2
and codigo_servico=4; <--Seleciona a data mais atual dado o código do cliente e serviço

delete from servico;
ROLLBACK;
delete from descricao where codigo_descricao=6;

select * from descricao;

select * from servico;

select max(data_descricao) from descricao where codigo_cliente=1 and codigo_servico=3;

delete from exame;

select * from clientes;

select * from pg_stat_activity where state = 'active';

select pg_terminate_backend(1600);

select * from exame;

select * from exame where data_exame between to_date('01/01/2017','dd/MM/yyyy') and to_date('02/02/2018','dd/MM/yyyy');