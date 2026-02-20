# Stock Control API

API REST desenvolvida para controle de estoque de matérias-primas, cadastro de produtos e cálculo de sugestão de produção com base no estoque disponível.

O sistema foi implementado utilizando Quarkus, Hibernate ORM com Panache e MySQL, seguindo o conceito de separação entre backend (API) e frontend conforme os requisitos do projeto.

---

## 1. Visão Geral

Este projeto é uma API REST responsável por:

* Gerenciar produtos
* Gerenciar matérias-primas
* Controlar a associação entre produtos e matérias-primas (Bill of Materials – BOM)
* Calcular sugestões de produção com base no estoque disponível

A aplicação foi construída utilizando Quarkus, com foco em:

* Alta performance
* Baixo consumo de memória
* Inicialização rápida
* Boa integração com JPA/Hibernate

---

## 2. Tecnologias Utilizadas


### Framework

* Quarkus (REST API)

  * `quarkus-rest`
  * `quarkus-rest-jackson`
  * `quarkus-arc` (CDI)

### Persistência

* Hibernate ORM

  * `quarkus-hibernate-orm`
  * `quarkus-hibernate-orm-panache`
* Hibernate Validator

  * `quarkus-hibernate-validator`
* Banco de dados:

  * MySQL (`quarkus-jdbc-mysql`)

### Documentação

* SmallRye OpenAPI

  * `quarkus-smallrye-openapi`

### Testes

* JUnit (`quarkus-junit`)
* Rest-Assured

---

## 3. Decisões Arquiteturais

### 3.1 Separação de Entidades

As entidades principais foram separadas da seguinte forma:

* Product
* RawMaterial
* ProductMaterial (entidade de associação)

A modelagem utiliza uma relação 1:N entre:

* Product → ProductMaterial
* RawMaterial → ProductMaterial

Isso evita um ManyToMany direto e permite armazenar o campo adicional:

* quantityRequired

Essa decisão facilita a implementação do conceito de Bill of Materials.

---

### 3.2 Camadas da Aplicação

Estrutura lógica típica:

```
controlle
repository (Panache)
entity
dto
```

Motivações:

* Separação de responsabilidades
* Facilidade de manutenção
* Facilidade de testes
* Evitar lógica de negócio dentro das entidades

---

### 3.3 Uso de DTOs

DTOs foram utilizados para:

* Evitar exposição direta das entidades
* Evitar problemas de serialização (lazy loading)
* Controlar o formato de resposta da API

---

### 3.4 Cálculo de Sugestão de Produção

A lógica de sugestão de produção considera:

* Estoque disponível de matérias-primas
* Quantidade necessária por produto
* Produto com maior valor agregado

A responsabilidade do cálculo está no backend para:

* Centralizar regra de negócio
* Evitar inconsistências no frontend
* Permitir futura reutilização por outros clientes (ex: mobile)

---

## 4. Requisitos

* Java 17+
* Maven 3.9+
* Banco de dados configurado
* Porta padrão: 8080

---

## 5. Configuração

Arquivo: `application.properties`

```properties
quarkus.http.cors.enabled=true
quarkus.http.cors.origins=http://localhost:5173
quarkus.http.cors.headers=accept,authorization,content-type,x-requested-with
quarkus.http.cors.methods=GET,POST,PUT,DELETE,OPTIONS
```

---

## 6. Executando o Projeto

Modo desenvolvimento:

```bash
./mvnw quarkus:dev
```

ou

```bash
mvn quarkus:dev
```

A aplicação ficará disponível em:

```
http://localhost:8080
```

Console do Quarkus:

```
http://localhost:8080/q/dev
```

---

## 7. Build para Produção

```bash
./mvnw clean package
```

Arquivo gerado:

```
target/quarkus-app/
```

Executar:

```bash
java -jar target/quarkus-app/quarkus-run.jar
```

---

## 8. Melhorias Futuras

### 8.1 Endpoint Específico para Dashboard

Criar:

```
GET /api/dashboard
```

Retornando:

* Total de produtos
* Total de matérias-primas
* Valor total possível de produção
* Indicadores agregados

Benefícios:

* Reduz múltiplas consultas
* Centraliza agregação no backend
* Melhora performance

---

### 8.2 Autenticação com JWT

Implementar:

* Endpoint: POST /api/auth/login
* Geração de JWT
* Filtro de segurança
* Proteção de endpoints

Sugestão:

* Usar quarkus-smallrye-jwt

---

### 8.3 Controle de Permissões (RBAC)

Perfis:

* ADMIN
* USER

Aplicar controle por:

* Endpoint
* Operação (ex: apenas ADMIN pode deletar)

---

### 8.4 Paginação e Filtros

Atualmente os endpoints retornam listas completas.

Melhoria:

* Paginação com parâmetros:

  * page
  * size
  * sort

Exemplo:

```
GET /api/products?page=0&size=10
```

---

### 8.5 Testes Automatizados

Adicionar:

* Testes unitários com JUnit
* Testes de integração com @QuarkusTest
* Testes de API com RestAssured

---

### 8.6 Observabilidade

Adicionar:

* Health checks
* Métricas (Micrometer)
* Logs estruturados
* Integração com Prometheus / Grafana

---

### 8.7 Dockerização

Criar:

* Dockerfile para backend
* Docker Compose com banco de dados
* Configuração para ambiente de produção

