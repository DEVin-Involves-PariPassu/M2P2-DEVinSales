insert into estado (nome, sigla) values
    ('Acre', 'AC'),
    ('Alagoas', 'AL'),
    ('Amapá', 'AP'),
    ('Amazonas', 'AM'),
    ('Bahia', 'BA'),
    ('Ceará', 'CE'),
    ('Espírito Santo', 'ES'),
    ('Goiás', 'GO'),
    ('Maranhão', 'MA'),
    ('Mato Grosso', 'MT'),
    ('Mato Grosso do Sul', 'MS'),
    ('Minas Gerais', 'MG'),
    ('Pará', 'PA'),
    ('Paraíba', 'PB'),
    ('Paraná', 'PR'),
    ('Pernambuco', 'PE'),
    ('Piauí', 'PI'),
    ('Rio de Janeiro', 'RJ'),
    ('Rio Grande do Norte', 'RN'),
    ('Rio Grande do Sul', 'RS'),
    ('Rondônia', 'RO'),
    ('Roraima', 'RR'),
    ('Santa Catarina', 'SC'),
    ('São Paulo', 'SP'),
    ('Sergipe', 'SE'),
    ('Tocantins', 'TO')
;

insert into cidade (nome, id_estado) values
    ('Rio Branco', 1),
    ('Maceió', 2),
    ('Macapá', 3),
    ('Manaus', 4),
    ('Salvador', 5),
    ('Fortaleza', 6),
    ('Vitória', 7),
    ('Goiânia', 8),
    ('São Luís', 9),
    ('Cuiabá', 10),
    ('Campo Grande', 11),
    ('Belo Horizonte', 12),
    ('Belém', 13),
    ('João Pessoa', 14),
    ('Curitiba', 15),
    ('Recife', 16),
    ('Teresina', 17),
    ('Rio de Janeiro', 18),
    ('Natal', 19),
    ('Porto Alegre',20),
    ('Porto Velho', 21),
    ('Boa Vista', 22),
    ('Florianópolis', 23),
    ('São Paulo', 24),
    ('Aracaju', 25),
    ('Palmas', 26)
;

insert into endereco (rua, numero, complemento, id_cidade) values
    ('Avenida Governador Ivo Silveira', 2445, 'Supermercado Angeloni Capoeiras', 23)
;

