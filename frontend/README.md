# Stock Control UI

Frontend SPA para controle de estoque e sugestão de produção, consumindo API REST desenvolvida em **Quarkus**.

---

## Sobre o Projeto

Este projeto é uma aplicação **Single Page Application (SPA)** desenvolvida com **Vue 3 + TypeScript + Vuetify + Tailwindcss**, responsável por consumir uma API backend implementada em Quarkus.

O objetivo do sistema é permitir:

* Cadastro e gerenciamento de produtos
* Cadastro e controle de matérias-primas
* Associação de matérias-primas aos produtos (BOM – Bill of Materials)
* Visualização de sugestões de produção com base no estoque disponível

---

## Arquitetura e Decisões Técnicas

### 🔹 Framework Frontend

* **Vue 3** (Composition API)
* **TypeScript**
* **Tailwindcss**
* **Vite** (build tool)

### 🔹 UI Framework

* **Vuetify 3**

Motivação:

* Biblioteca madura e amplamente utilizada
* Componentes prontos para CRUD (DataTable, Dialog, Forms)
* Layout administrativo robusto (Drawer + App Bar)
* Padrão Material Design consistente

### 🔹 Gerenciamento de Estado

* **Pinia**

Motivação:

* Store moderna e oficial para Vue 3
* Melhor integração com TypeScript
* Código mais simples que Vuex

### 🔹 Comunicação com Backend

* **Axios**
* Base URL configurável via `.env`

### 🔹 Backend

* API REST desenvolvida com **Quarkus**
* Padrão de endpoints RESTful

---

## 📂 Estrutura do Projeto

```
src/
  services/         # Comunicação HTTP com backend
  stores/           # Gerenciamento de estado (Pinia)
  views/            # Telas principais
  components/       # Componentes reutilizáveis
  router/           # Configuração de rotas
  types/            # Tipagens TypeScript
  main.ts
  App.vue
```

---

## 🚀 Funcionalidades Implementadas

### Produtos

* Listagem com busca
* Criação
* Edição
* Exclusão
* Visualização de detalhes

### Matérias-Primas

* Listagem
* Criação
* Edição
* Exclusão

### BOM (Bill of Materials)

* Associação de matérias-primas ao produto
* Definição de quantidade necessária
* Edição de quantidade
* Remoção de associação

### Sugestão de Produção

* Lista produtos possíveis de fabricar
* Quantidade sugerida
* Valor total por produto
* Valor total geral

---

## Requisitos

* Node.js 18+
* NPM 9+
* Backend Quarkus rodando em `http://localhost:8080`

---

## Instalação

Instale as dependências:

```bash
npm install
```

---

## Configuração de Ambiente

Crie um arquivo `.env` na raiz do projeto:

```env
VITE_API_BASE_URL=http://localhost:8080
```

---

## Executando em Ambiente de Desenvolvimento

```bash
npm run dev
```

A aplicação ficará disponível em:

```
http://localhost:5173
```

---

## Build para Produção

```bash
npm run build
```

Os arquivos compilados estarão em:

```
dist/
```

---

## 🔐 Configuração Necessária no Backend (CORS)

No `application.properties` do Quarkus:

```properties
quarkus.http.cors=true
quarkus.http.cors.origins=http://localhost:5173
quarkus.http.cors.headers=accept,authorization,content-type,x-requested-with
quarkus.http.cors.methods=GET,POST,PUT,DELETE,OPTIONS
```

---

## 📌 Padrões Utilizados

* Arquitetura modular por domínio
* Separação clara entre:

  * API layer
  * Store layer
  * View layer
* Tipagem forte com TypeScript
* Feedback visual com Snackbar
* Confirmação de exclusão com Dialog
* Interface responsiva

---

## Melhorias Futuras

* Endpoint próprio para Dashboard
* Autenticação (Tela de Login)
* Paginação no Backend
* Cache e Otimizações
* Controle de Permissões (RBAC)
* Dockerização
