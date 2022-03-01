create table cidade (
    id bigserial primary key,
    nome varchar(100) not null,
    id_estado bigint not null,
    foreign key (id_estado) references estado(id)
)