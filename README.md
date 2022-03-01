# M2P2-DEVinSales - Rodando localmente

Crie um arquivo chamado application.properties em src/main/resources

Tal arquivo deve ter o seguinte conteúdo:

```
spring.datasource.url=jdbc:postgresql://localhost:5432/postgres?currentSchema=devin_sales
spring.datasource.username=postgres
spring.datasource.password=password
```

Além disso, execute o script disponível em `inicializacao_projeto.sql` dentro do seu DBeaver.  