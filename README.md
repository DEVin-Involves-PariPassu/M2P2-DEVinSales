# M2P2-DEVinSales - Rodando localmente

Crie um arquivo chamado application.properties em src/main/resources

Tal arquivo deve ter o seguinte conteúdo:

```
spring.jpa.database=postgresql
spring.datasource.url=${DEVINSALES DATABASE URL:jdbc:postgresql://ec2-3-228-222-169.compute-1.amazonaws.com/da36o7dhs76h42}
spring.datasource.username=${DEVINSALES DATABASE USER:chwmzpzmmycxkm}
spring.datasource.password=${DEVINSALES DATABASE PASSWORD:f11041a5e49a141314173c638c99009819bb4bd48122fe24a66ab060f89444b1}
spring.jpa.hibernate.ddl-auto=update
security.jwt.expiration=86400000
security.jwt.secret=A+X;fTJP&Pd,TD9dwVq(hsHX,ya^<wsD_UK7L+@=S;{'CydP]{v@}G'b>et;yz$*\yL5S8EJN:%P:X%H9>#nYLrX}@\s?CQcpspH,2emzBc!Q[V'AYa~uzF8WR~AUrMzxp/V$9([S9X#zj/CH('#]B_Hc+%fGhe27YB;^j4\Xk=Ju"Ap~_&<L;=!Z;!,2UP;!hF3P]j85#*`&T]/kB/W^6$v~u6qpejL>kY^f)sy4:qTq_Ec!-z!@aAp~sLKGU>$
```