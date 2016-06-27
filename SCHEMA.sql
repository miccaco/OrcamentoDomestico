drop database controlefinanc;
create database controlefinanc;

use controlefinanc;

create table usuario (
    login varchar(50)  not null,
    nome  varchar(100) not null,
    senha varchar(100) not null,
    constraint pk_usuario primary key(login)
);

create table categoria (
    codigo    int          not null auto_increment,
    usuario   VARCHAR(50)  not null,
    descricao VARCHAR(100) not null,
    tipo      bit          not null,
    constraint pk_categoria primary key(codigo),
    constraint fk_categoria_usuario foreign key(usuario) references usuario(login)
);

create table lancamento (
    numero    INT           not null auto_increment,
    categoria INT           not null,
    valor     DECIMAL(16,2) not null,
    data	  DATE          not null,
    descricao VARCHAR(100)  not null,
    constraint pk_lancamento primary key(numero),
    constraint fk_lancamento_categoria foreign key(categoria) references categoria(codigo)
);