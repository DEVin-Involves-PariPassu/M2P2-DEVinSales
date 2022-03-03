create table item_venda (
    id bigserial primary key,
    id_venda bigint not null,
    id_produto bigint not null,
    preco_unitario decimal not null,
    quantidade int not null,

    constraint fk_itemvenda_venda foreign key (id_venda) references venda(id),
    constraint fk_itemvenda_produto foreign key (id_produto) references produto(id)
);
