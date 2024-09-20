# Sistema de Gestão para ONG de Animais - AMA DC

## Descrição do Projeto

Este projeto tem como objetivo desenvolver um sistema para auxiliar no controle das atividades da ONG **AMA DC** (Associação Amigos dos Animais de Dores de Campos), que atua no resgate de animais de rua, fornecendo suporte no cuidado e no processo de adoção responsável.

## Objetivo

O sistema oferece uma plataforma para gerenciar o cadastro de animais resgatados, voluntários, adoções, doações, além de permitir o acompanhamento médico dos animais e o controle financeiro da ONG.

## Tecnologias Utilizadas

- **Back-End:** Java com Spring Boot
- **Front-End:** Angular
- **Banco de Dados:** MySQL
- **Gerenciamento de Dependências:** Maven
- **Controle de Versão:** Git

## Módulos do Sistema

### Módulo Básico
- **Gerenciamento de Perfis de Acesso:** Controle de permissões de usuários.
- **Cadastro de Usuários:** Registro de usuários do sistema.

### Módulo Administrativo
- **Cadastro de Voluntários:** Gerenciamento de informações sobre os voluntários da ONG.
- **Cadastro de Animais Resgatados:** Registro de animais que foram resgatados.
- **Cadastro de Clínicas Veterinárias e Veterinários:** Informação de profissionais de saúde e clínicas.
- **Histórico de Consultas:** Acompanhamento médico dos animais (prontuário).
- **Cadastro de Vacinas:** Registro de vacinas administradas aos animais.
- **Cadastro de Adoções:** Gerenciamento de adotantes e animais adotados.

### Módulo Financeiro
- **Cadastro de Doações:** Registro e acompanhamento de doações recebidas pela ONG.
- **Cadastro de Compras:** Controle de compras realizadas para a manutenção da ONG.

## Como Executar o Projeto

### Pré-requisitos

- **Java 23**
- **Node.js** (para o front-end Angular)
- **Intellij Community** (Usado para desenvolvimento)
- **MySQL** (para o banco de dados)
- **Debeaver** (Utilizado para manuseo do banco de dados)
- **Maven** (para gerenciamento de dependências)
- **GitHub** (para controle de versão)

### Passos para Execução

1. **Clone o repositório:**
   ```bash
   https://github.com/mluizsa/amadc.git




2. **Back-End:**

   Navegue até a pasta do back-end:
   "cd backend"

   Execute o comando para baixar as dependências:
   "mvn install"

   Configure o arquivo application.properties com as informações do banco de dados.
   Execute a aplicação:

   "mvn spring-boot:run"


3. **Front-End:**

   Navegue até a pasta do front-end:
   "cd frontend"

   Instale as dependências do Angular:
   "npm install"

   Execute o front-end:
   "ng serve"


4. **Banco de Dados:**

   Crie o banco de dados no MySQL:
   "CREATE DATABASE ong_amadc;"

        As tabelas serão criadas automaticamente na primeira execução.



**Contribuições**

Contribuições são bem-vindas! Por favor, siga as diretrizes de contribuição e abra um pull request com sua melhoria.
Licença

Este projeto está sob a licença MIT. Veja o arquivo LICENSE para mais detalhes.



Esse README cobre a descrição do projeto, suas tecnologias, módulos, instruções de execução e detalhes sobre contribuições.