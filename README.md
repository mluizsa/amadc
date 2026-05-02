# Sistema de Gestão para ONG de Animais - AMA DC

# 🐾 AMA DC - Gestão de Resgates e Adoções

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![Vue.js](https://img.shields.io/badge/vuejs-%2335495e.svg?style=for-the-badge&logo=vuedotjs&logoColor=%234FC08D)
![Postgres](https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white)

Sistema centralizado desenvolvido para a **AMA DC** (Associação Amigos dos Animais de Dores de Campos). O objetivo é automatizar e organizar o fluxo de trabalho da ONG, garantindo um acompanhamento eficiente desde o resgate até a adoção responsável.

---

## 🚀 Funcionalidades Principais

*   **Autenticação Blindada:** Login seguro utilizando **JWT** e **Cookies HttpOnly**, protegendo contra ataques XSS.
*   **Gestão de Animais:** Cadastro completo com fotos, histórico de saúde e status de disponibilidade.
*   **Controle de Resgates:** Registro geográfico e cronológico de resgates efetuados.
*   **Fluxo de Adoção:** Cadastro de adotantes e gerenciamento de termos de responsabilidade.
*   **Auditoria Automática:** Rastreabilidade total (quem criou/editou e quando) em todas as tabelas principais através do **JPA Auditing**.

---

## 🛠️ Stack Tecnológica

### **Back-end**
*   **JDK:** 21 (OpenJDK)
*   **Framework:** Spring Boot 3.x
*   **Segurança:** Spring Security + Stateless JWT com Cookies
*   **Banco de Dados:** PostgreSQL
*   **Migrações:** Flyway (Versionamento de banco de dados)

### **Front-end**
*   **Framework:** Vue.js 3
*   **Estilização:** Bootstrap / Light Bootstrap Dashboard Theme
*   **Comunicação:** Axios com suporte a Credenciais (CORS/Cookies)

---

## ⚙️ Configuração e Execução

### 1. Pré-requisitos
*   Java 21 instalado.
*   Node.js 18+ instalado.
*   PostgreSQL rodando localmente ou via Docker.

### 2. Variáveis de Ambiente
Configure as seguintes variáveis antes de iniciar (no seu terminal ou arquivo `.env`):

| Variável | Descrição | Valor Sugerido |
| :--- | :--- | :--- |
| `DB_URL` | Conexão JDBC | `jdbc:postgresql://localhost:5432/amadc` |
| `DB_USERNAME` | Usuário do Postgres | `seu_usuario` |
| `DB_PASSWORD` | Senha do Postgres | `sua_senha` |
| `JWT_SECRET` | Chave de criptografia | `uma_chave_longa_e_aleatoria` |

### 3. Rodando o Back-end
```bash
mvn spring-boot:run
```

### 4. Rodando o Front-end
```bash
cd frontend
npm install
npm run serve
```
O sistema estará disponível em: http://localhost:8081


### 🤝 Contribuição
Faça um Fork do projeto.
Crie uma Branch (git checkout -b feature/minha-feature).
Dê um Commit (git commit -m 'Adicionei X').
Dê um Push para o seu repositório (git push origin feature/minha-feature).

Abra um Pull Request.

### 📄 Licença
Este projeto é distribuído sob a licença MIT. Consulte o arquivo LICENSE para mais informações.


## Desenvolvido com ❤️ para a causa animal.