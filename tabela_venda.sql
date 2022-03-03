create table venda(
                      id bigserial primary key,
                      id_comprador bigint not null,
                      id_vendedor bigint,
                      dt_venda timestamp,

                      constraint fk_vd_comprador foreign key (id_comprador) references usuario(id),
                      constraint fk_vd_vendedor foreign key (id_vendedor) references usuario(id)
)