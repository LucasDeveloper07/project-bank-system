# Bank System (JDBC)

*[Ler em português](README.pt-br.md)*

A simple command-line bank system built in Java with JDBC and MySQL. Built from scratch as a way to practice Java and JDBC persistence concepts.

## Features

- Registration of individual (CPF) and business (CNPJ) clients
- Opening a current account or a savings account
- Login with CPF/CNPJ, email and password
- Deposits, withdrawals and transfers between accounts
- Automatic maintenance fee (current account, every 30 days)
- Automatic interest credit (savings account, 1% every 30 days)
- Name and password changes
- Transaction history with receipts

## Tech stack

- Java
- JDBC
- MySQL

## Project structure

```
src/
├── application/   # Console screens and flow (login, menu)
├── entities/      # Domain classes (User, Account, Transaction, etc.)
├── dao/           # Data access interfaces
│   └── daoImpl/   # JDBC implementations of the DAO interfaces
│   └── db/        # Database connection and DAO factory
├── enums/         # Account and transaction types
├── exceptions/    # Custom exceptions
└── services/      # Interest calculation rules
```

Other folders:
- `dumpBd/` — MySQL database dump
- `docs/` — the project's UML and DAO diagrams

## How to run

1. Create the database by importing the dump at `dumpBd/bank_system_bd.sql` into MySQL.
2. Download the MySQL JDBC driver and place the .jar file in the `lib` folder.
2. Configure the `db.properties` file at the project root with your connection details (URL, username and password).
3. Compile and run the `application.Program` class.
