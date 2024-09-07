## Desafio Técnico - Sistema de Gerenciamento de Tarefas Abstrato

O Desafio foi construido utilizando as seguintes tecnologias:
- Docker (PostgreSQL)
- Java (Spring Boot, Maven, Lombok, JUnit, H2 Database e Swagger)
- NextJS (Formik, Yup e Shadcn UI)

### Como executar o projeto:

### #1 Executar o container Docker
ou deletar o Compose.yaml e configurar o application.yaml no diretório src/main/resources para utilizar o PostgreSQL já instalado em seu computador.

```bash
docker-compose up --build -d
```

### #2 Baixar as dependencias do projeto
ou utilizar a IDE para baixa-las.
```bash
./mvnw dependency:go-offline
```

### #3 Executar os testes

```bash
./mvnw test
```

### #4 Executar o projeto Spring-Boot

```bash
./mvnw spring-boot:run
```

### #5 Documentação da API está disponivel no endereço:

http://localhost:8080/swagger-ui/index.html
onde poderá testar todos os endpoint da API.

### #6 Executar o front-end no diretório src/main/resources/static/list-manager-cli
caso a porta do projeto Spring-Boot tenha sido modificada no application.yaml, favor editar o arquivo .env.development no diretório src/main/resources/static/list-manager-cli substituindo pela nova porta escolhida.
```bash
npm run dev
```

#### Observações: Ao executar o projeto Spring-Boot será persistida de forma automática 2 Tarefas e Itens.

![image](https://github.com/user-attachments/assets/df9c2f67-5725-44ce-8f52-b58e7d546236)
