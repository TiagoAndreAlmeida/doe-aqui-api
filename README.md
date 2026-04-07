# Doe Aqui API 🎁

A **Doe Aqui API** é uma plataforma robusta desenvolvida para conectar pessoas que desejam doar itens que não utilizam mais a pessoas que precisam deles. O projeto foca em facilidade de uso, segurança de dados e alta performance.

---

## 🚀 Tecnologias Utilizadas

O projeto foi construído com o que há de mais moderno no ecossistema Java:

*   **Java 21**: Utilizando recursos modernos como Records e Virtual Threads (potencial).
*   **Spring Boot 4.x**: Base do projeto para Injeção de Dependências, Web MVC e Segurança.
*   **Spring Security + JWT (JSON Web Token)**: Autenticação **Stateless** customizada para alta performance.
*   **Spring Data JPA**: Abstração de persistência de dados.
*   **PostgreSQL**: Banco de dados relacional para produção.
*   **Flyway**: Gerenciamento de migrações (versionamento) do banco de dados.
*   **Argon2id (Bouncy Castle)**: Criptografia de senhas de última geração, resistente a ataques de força bruta.
*   **JUnit 5 & Mockito**: Suíte completa de testes unitários e de integração.
*   **Maven**: Gerenciador de dependências e build.

---

## 🏗️ Arquitetura e Modelagem

O sistema segue uma **Arquitetura em Camadas (Layered Architecture)** organizada por **Pacotes por Funcionalidade (Package-by-Feature)** como `user`, `product` e `category`. Essa abordagem facilita a localização de componentes relacionados a uma mesma regra de negócio.

### 1. Camada de Domínio e Persistência
As entidades (`UserEntity`, `ProductEntity`) utilizam mapeamento JPA. Destacamos o relacionamento entre Usuário e Produto, onde um usuário pode ser **Doador** ou **Recebedor** de múltiplos itens.

### 2. DTOs com Java Records
Utilizamos **Java Records** para todos os objetos de transferência de dados (DTOs). Isso garante:
*   **Imutabilidade**: Dados que não mudam durante o transporte.
*   **Performance**: Records são mais leves que classes tradicionais.
*   **Código Limpo**: Eliminação de getters, setters e boilerplates.

### 3. Segurança Stateless (Alta Performance)
Diferente de aplicações tradicionais, a Doe Aqui API implementa um modelo **Stateless JWT**:
*   O ID e o Email do usuário são embutidos no Token.
*   O filtro de segurança valida o token **sem consultar o banco de dados** em cada requisição.
*   Isso reduz drasticamente o estresse no PostgreSQL, permitindo que a API suporte muito mais usuários simultâneos.

---

## 🔄 Fluxo de Doação

O ciclo de vida de um produto é gerenciado por uma máquina de estados simples e segura:

1.  **AVAILABLE**: O produto é criado e está visível no feed público.
2.  **RESERVED**: Um interessado solicita o item. O sistema vincula o recebedor e retira o item da lista pública.
3.  **DONATED**: O doador confirma a entrega. O ciclo se encerra.

---

## 🛡️ Regras de Negócio Implementadas

*   Um usuário não pode reservar o próprio produto.
*   Apenas o doador original tem permissão para confirmar a doação.
*   Tanto doador quanto recebedor podem cancelar uma reserva para liberar o item.
*   Busca de usuários otimizada via Proxy (`getReferenceById`) para evitar consultas desnecessárias ao associar entidades.

---

## 🚦 Como Executar o Projeto

### Pré-requisitos
*   Java 21 ou superior.
*   Docker (opcional, para o PostgreSQL).
*   Maven.

### Passos
1.  Configure as variáveis de ambiente no `application.properties` (DB_URL, DB_USER, DB_PASS, JWT_SECRET).
2.  Execute as migrações e o projeto:
    ```bash
    ./mvnw spring-boot:run
    ```
3.  Para rodar os testes:
    ```bash
    ./mvnw test
    ```

---

## 📝 Documentação da API

### Usuários e Autenticação
*   `POST /users`: Cadastro de novo usuário.
*   `POST /auth/login`: Autenticação e geração de Token JWT.
*   `GET /auth/me`: Retorna os dados do usuário logado (Protegido).

### Produtos (Doações)
*   `GET /products`: Lista produtos disponíveis (Público/Paginado).
*   `POST /products`: Cria um novo anúncio (Protegido).
*   `PATCH /products/{id}/reserve`: Reserva um item (Protegido).
*   `PATCH /products/{id}/confirm`: Finaliza a doação (Protegido/Apenas Doador).

---
Desenvolvido com foco em impacto social e excelência técnica. 🚀
