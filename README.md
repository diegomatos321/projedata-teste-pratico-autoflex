# Projeto Prático - Autoflex

Sistema web para controle de estoque de matérias-primas e cálculo de produção possível com base nos insumos disponíveis.

O projeto foi desenvolvido como solução para o teste técnico proposto, seguindo arquitetura API-first, separando backend e frontend.

---

## Visão Geral

A aplicação permite:

* Cadastro de Produtos
* Cadastro de Matérias-Primas
* Associação de matérias-primas aos produtos (Bill of Materials)
* Cálculo automático de quais produtos podem ser produzidos com o estoque atual
* Priorização por produtos de maior valor
* Cálculo do valor total potencial da produção sugerida

---

## Arquitetura

O sistema foi dividido em duas camadas principais:

### Backend (API)

* Java 17
* Quarkus
* Hibernate ORM com Panache
* MySQL
* OpenAPI (Swagger)
* JUnit + Rest-Assured para testes

Responsável por:

* Regras de negócio
* Persistência
* Cálculo da sugestão de produção

---

### Frontend

* Vue.js 3
* TypeScript
* TailwindCSS
* Consumo via API REST

Responsável por:

* Interface CRUD de Produtos
* Interface CRUD de Matérias-Primas
* Associação de insumos aos produtos
* Tela de sugestão de produção

---

## Principais Decisões Técnicas

* Separação clara entre backend e frontend (RNF002)
* Uso de framework moderno (Quarkus) conforme sugerido
* Persistência relacional (MySQL)
* Organização por camadas (Resource, Service, Entity)
* Cálculo de produção priorizando produtos de maior valor
* Uso de entidade intermediária para modelar a quantidade necessária de matéria-prima

---

## Como Executar

### Backend

```bash
cd backend
./mvnw quarkus:dev
```

Swagger:

```
http://localhost:8080/q/swagger-ui
```

---

### Frontend

```bash
cd frontend
npm install
npm run dev
```
