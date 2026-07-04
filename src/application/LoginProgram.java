package application;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import dao.CurrentAccountDAO;
import dao.SavingsAccountDAO;
import dao.UserDAO;
import entities.ClientPf;
import entities.ClientPj;
import entities.CurrentAccount;
import entities.SavingsAccount;
import entities.User;
import enums.TypeAccount;

public class LoginProgram {
    
    public static User login(Scanner sc, UserDAO userDao, CurrentAccountDAO currentDao, SavingsAccountDAO savingsDao) {
        User user = null;

        do {
            System.out.println("=====SISTEMA BANCARIO=====");
            System.out.println("1 - Login");
            System.out.println("2 - Criar conta");
            System.out.print("Digite a opção desejada: ");
            int opcao = sc.nextInt();
            sc.nextLine();

            switch (opcao) {
                case 1:
                    System.out.println("\nInforme seus dados de login:");
                    System.out.print("CPF/CNPJ: ");
                    String cpf_cnpj = sc.nextLine();

                    System.out.print("Email: ");
                    String email = sc.nextLine();

                    System.out.print("Senha: ");
                    String password = String.valueOf(sc.nextLine().hashCode());

                    user = userDao.login(cpf_cnpj, email, password);

                    if (user.getAccount() instanceof CurrentAccount currentAccount) {
                        currentAccount.maintenanceDiscount(LocalDate.now());
                    } else if (user.getAccount() instanceof SavingsAccount savingsAccount) {
                        savingsAccount.processInterestRate(savingsAccount, LocalDate.now());
                    }

                    System.out.println("Login realizado com sucesso!");
                    break;
                case 2:
                    System.out.println("\n1 - Cliente PF");
                    System.out.println("2 - Cliente PJ");
                    System.out.print("Escolha a opção desejada: ");
                    int opcao1 = sc.nextInt();

                    switch (opcao1) {
                        case 1:
                            System.out.println("\n1 - Conta corrente");
                            System.out.println("2 - Conta poupança");
                            System.out.print("Escolha a opção desejada: ");
                            int opcao2 = sc.nextInt();
                            
                            if (opcao2 == 1) {
                                System.out.println("\nInforme seus dados:");
                                System.out.print("CPF: ");
                                Long cpf = sc.nextLong();
                                sc.nextLine();

                                System.out.print("Nome: ");
                                String nome = sc.nextLine();
            
                                System.out.print("Email: ");
                                String email1 = sc.nextLine();

                                System.out.print("Data de nascimento (dd/mm/yyyy): ");
                                LocalDate birthDate = LocalDate.parse(sc.nextLine(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));

                                System.out.print("Chave de transferência: ");
                                int transferKey = sc.nextInt();
                                sc.nextLine();
            
                                System.out.print("Senha: ");
                                String password1 = String.valueOf(sc.nextLine().hashCode());
                                
                                user = new ClientPf(nome, email1, password1, birthDate, cpf, TypeAccount.CURRENT, transferKey);

                                userDao.insert(user);
                                currentDao.insert((CurrentAccount) user.getAccount());

                                System.out.println("Conta criada com sucesso!");
                            } else if (opcao2 == 2) {
                                System.out.println("\nInforme seus dados:");
                                System.out.print("CPF: ");
                                Long cpf = sc.nextLong();
                                sc.nextLine();

                                System.out.print("Nome: ");
                                String nome = sc.nextLine();
            
                                System.out.print("Email: ");
                                String email1 = sc.nextLine();

                                System.out.print("Data de nascimento (dd/mm/yyyy): ");
                                LocalDate birthDate = LocalDate.parse(sc.nextLine(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));

                                System.out.print("Chave de transferência: ");
                                int transferKey = sc.nextInt();
                                sc.nextLine();
            
                                System.out.print("Senha: ");
                                String password1 = String.valueOf(sc.nextLine().hashCode());
                                
                                user = new ClientPf(nome, email1, password1, birthDate, cpf, TypeAccount.SAVINGS, transferKey);

                                userDao.insert(user);
                                savingsDao.insert((SavingsAccount) user.getAccount());

                                System.out.println("Conta criada com sucesso!");
                            }
                            break;
                        case 2:
                            System.out.println("\n1 - Conta corrente");
                            System.out.println("2 - Conta poupança");
                            System.out.print("Escolha a opção desejada: ");
                            int opcao3 = sc.nextInt();
                            
                            if (opcao3 == 1) {
                                System.out.println("\nInforme seus dados:");
                                System.out.print("CNPJ: ");
                                Long cnpj = sc.nextLong();
                                sc.nextLine();

                                System.out.print("Nome: ");
                                String nome = sc.nextLine();
            
                                System.out.print("Email: ");
                                String email1 = sc.nextLine();

                                System.out.print("Data de abertura (dd/mm/yyyy): ");
                                LocalDate birthDate = LocalDate.parse(sc.nextLine(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));

                                System.out.print("Chave de transferência: ");
                                int transferKey = sc.nextInt();
                                sc.nextLine();
            
                                System.out.print("Senha: ");
                                String password1 = String.valueOf(sc.nextLine().hashCode());
                                
                                user = new ClientPj(nome, email1, password1, birthDate, cnpj, TypeAccount.CURRENT, transferKey);

                                userDao.insert(user);
                                currentDao.insert((CurrentAccount) user.getAccount());

                                System.out.println("Conta criada com sucesso!");
                            } else if (opcao3 == 2) {
                                System.out.println("\nInforme seus dados:");
                                System.out.print("CNPJ: ");
                                Long cnpj = sc.nextLong();
                                sc.nextLine();

                                System.out.print("Nome: ");
                                String nome = sc.nextLine();
            
                                System.out.print("Email: ");
                                String email1 = sc.nextLine();

                                System.out.print("Data de abertura (dd/mm/yyyy): ");
                                LocalDate birthDate = LocalDate.parse(sc.nextLine(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));

                                System.out.print("Chave de transferência: ");
                                int transferKey = sc.nextInt();
                                sc.nextLine();
            
                                System.out.print("Senha: ");
                                String password1 = String.valueOf(sc.nextLine().hashCode());
                                
                                user = new ClientPj(nome, email1, password1, birthDate, cnpj, TypeAccount.SAVINGS, transferKey);

                                userDao.insert(user);
                                savingsDao.insert((SavingsAccount) user.getAccount());

                                System.out.println("Conta criada com sucesso!");
                            }
                            break;
                    }
                    break;
            }
        } while (user == null);

        return user;
    }
}
