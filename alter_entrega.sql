alter table entrega rename column id_endereço to id_endereco;
alter table entrega add constraint fk_entrega_endereco foreign key(id_endereco) references endereco(id);
alter table entrega rename column previsão_entrega to previsao_entrega;
