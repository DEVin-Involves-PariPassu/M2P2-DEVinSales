create table entrega(
                        id bigserial primary key,
                        id_endereço bigint not null,
                        id_venda bigint not null,
                        previsão_entrega date not null,
                        constraint fk_entrega_venda foreign key(id_venda) references venda(id)

);