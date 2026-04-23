# Sistema de Gestão para ONG de Animais - AMA DC

## Descrição do Projeto
Este projeto auxilia no controle das atividades da ONG **AMA DC** (Associação Amigos dos Animais de Dores de Campos), gerenciando desde o resgate até a adoção responsável.

## Tecnologias Utilizadas
- **Back-End:** Java 21 com Spring Boot 3
- **Front-End:** Vue.js 3
- **Banco de Dados:** PostgreSQL
- **Migrações:** Flyway
- **Segurança:** Spring Security + JWT

## Como Executar o Projeto

### Pré-requisitos
- **Java 21** (ou superior)
- **Node.js** (para o front-end)
- **PostgreSQL**
- **Maven**

### Passos para Execução

1. **Clone o repositório:**
   ```bash
   git clone [https://github.com/mluizsa/amadc.git](https://github.com/mluizsa/amadc.git)


2. **Configuração do Banco de Dados:**

   - Crie um banco de dados chamado amadc no PostgreSQL.
   - O sistema utiliza o Flyway, então as tabelas serão criadas automaticamente na primeira execução através das migrations em src/main/resources/db/migration.

2. **Back-End:**
   - Configure as variáveis de ambiente no seu terminal ou IDE:
      * DB_URL: jdbc:postgresql://localhost:5432/amadc
      * DB_USERNAME: seu_usuario 
      * DB_PASSWORD: sua_senha 
      * JWT_SECRET: uma_chave_segura

 * Execute a aplicação:

   ```Bash
   mvn spring-boot:run

 * Front-End:
   - Navegue até a pasta: cd frontend
 
   - Instale as dependências: npm install 
     Execute o projeto: 
   ```npm run dev