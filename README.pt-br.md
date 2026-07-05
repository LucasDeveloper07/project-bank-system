# Sistema Bancário (JDBC)

*[Read in English](README.md)*

Sistema bancário simples, feito em Java com JDBC e MySQL, executado via linha de comando. Projeto criado do zero como prática dos conceitos de Java e persistência com JDBC.

## Funcionalidades

- Cadastro de clientes Pessoa Física (CPF) e Pessoa Jurídica (CNPJ)
- Abertura de conta corrente ou conta poupança
- Login com CPF/CNPJ, e-mail e senha
- Depósito, saque e transferência entre contas
- Cobrança automática de taxa de manutenção (conta corrente, a cada 30 dias)
- Crédito automático de juros (conta poupança, 1% a cada 30 dias)
- Alteração de nome e senha
- Histórico de transações com comprovante

## Tecnologias

- Java
- JDBC
- MySQL

## Estrutura do projeto

```
src/
├── application/   # Telas e fluxo do console (login, menu)
├── entities/      # Classes de domínio (User, Account, Transaction, etc.)
├── dao/           # Interfaces de acesso a dados
│   └── daoImpl/   # Implementações JDBC das interfaces DAO
│   └── db/        # Conexão com o banco e fábrica de DAOs
├── enums/         # Tipos de conta e de transação
├── exceptions/    # Exceções personalizadas
└── services/      # Regras de cálculo de juros
```

Outras pastas:
- `dumpBd/` — dump do banco de dados MySQL
- `docs/` — diagramas UML e de DAO do projeto

## Como executar

1. Crie o banco de dados importando o dump em `dumpBd/bank_system_bd.sql` no MySQL.
2. Realize o download do driver JDBC do MySQL e cole o arquivo .jar na pasta `lib`.
2. Configure o arquivo `db.properties` na raiz do projeto com os dados de conexão (URL, usuário e senha).
3. Compile e execute a classe `application.Program`.
