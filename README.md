# Sistema de Votação

Aplicação full stack para gerenciamento de pautas e sessões de votação. O sistema permite cadastrar associados, criar pautas, abrir uma sessão de votação, registrar votos e consultar os resultados das pautas finalizadas.

## Aviso importante:

- **Seed de dados**: Para facilitar o uso da aplicação em um cenario de testes de votos, existe um seed de +- 3k de Associados, mas o fluxo de cadastro ainda existe.

## Visão geral

O projeto é dividido em três serviços:

- **Frontend**: aplicação React + TypeScript construída com Vite e servida pelo Nginx.
- **API**: aplicação Spring Boot com Java 21, Spring Data JPA, validação, Flyway e documentação OpenAPI.
- **Banco de dados**: MySQL 8.


## Requisitos

Para executar com Docker, instale:

- Docker Desktop com Docker Compose habilitado.
- Git, caso o projeto ainda não esteja disponível localmente.

Não é necessário instalar Java, Maven, Node.js ou MySQL para executar a aplicação pelo Docker Compose.

## Executando com Docker (Prefira este método)

Na raiz do projeto, onde está o arquivo `docker-compose.yml`, execute:

```bash
docker compose up --build
```

Para executar em segundo plano:

```bash
docker compose up --build -d
```

O primeiro build pode levar alguns minutos porque compila a API e instala as dependências do frontend. Quando os containers estiverem prontos, acesse:

- **Aplicação web**: http://localhost:3000
- **API**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui/index.html
- **MySQL**: `localhost:3307`

Para parar e remover os containers:

```bash
docker compose down
```

O Compose atual não configura volume para o MySQL. Portanto, remover o container do banco com `docker compose down` também remove os dados armazenados nele.

Os contratos completos, parâmetros e modelos de request/response estão disponíveis no Swagger UI.

## Executando sem Docker

### API

Requer Java 21 e Maven. Entre na pasta da API e execute:

```bash
cd projeto-votacao
./mvnw spring-boot:run
```

No Windows, use:

```powershell
cd projeto-votacao
.\mvnw.cmd spring-boot:run
```

Nesse modo, a configuração padrão aponta para um MySQL em `localhost:3306`. A senha pode ser informada pela variável `MYSQL_PASSWORD`.

Para executar os testes da API:

```powershell
.\mvnw.cmd test
```

### Frontend

Requer Node.js e npm. Em outro terminal:

```bash
cd votacao-app
npm install
npm run dev
```

O Vite disponibiliza a aplicação no endereço mostrado no terminal, normalmente http://localhost:5173. Nesse modo, a API precisa estar disponível em http://localhost:8080.

Comandos úteis do frontend:

```bash
npm run build
npm run typecheck
npm run lint
npm test
```

## Configurações de desenvolvimento

As credenciais definidas no `docker-compose.yml` são destinadas ao ambiente local:

- Banco: `db_projeto_votacao`
- Usuário: `root`
- Senha: `db1234#`

Para fins de desenvolvimento as credenciais foram expostas direntamente no compose.
