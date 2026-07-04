package application;

import java.util.List;
import java.util.Scanner;

import entities.Transaction;
import entities.User;

public class MenuProgram {

    public static void Menu(Scanner sc, User user) {

        int opcao;
        double value;
        String password;

        do {

            System.out.println("\n=====SISTEMA BANCARIO=====");
            System.out.println("1 - Depositar");
            System.out.println("2 - Sacar");
            System.out.println("3 - Transferir");
            System.out.println("4 - Ver comprovantes");
            System.out.println("5 - Visualizar saldo");
            System.out.println("6 - Ver dados bancários");
            System.out.println("7 - Ver dados de usuário");
            System.out.println("8 - Logout");
            System.out.print("Escolha a opção desejada: ");
            opcao = sc.nextInt();

            switch (opcao) {
                case 1:
                    System.out.print("\nQual o valor do depósito? ");
                    value = sc.nextDouble();
                    sc.nextLine();

                    System.out.print("Confirme sua senha: ");
                    password = String.valueOf(sc.nextLine().hashCode());

                    user.getAccount().deposit(value, password);

                    System.out.println("Depósito realizado com sucesso!");
                    break;
                case 2:
                    System.out.print("\nQual o valor do saque? ");
                    value = sc.nextDouble();
                    sc.nextLine();

                    System.out.print("Confirme sua senha: ");
                    password = String.valueOf(sc.nextLine().hashCode());

                    user.getAccount().withdraw(value, password);

                    System.out.println("Saque realizado com sucesso!");
                    break;
                case 3:
                    System.out.print("\nDigite a chave de transferência: ");
                    int transferKey = sc.nextInt();

                    System.out.print("Qual o valor da trasnferência? ");
                    value = sc.nextDouble();
                    sc.nextLine();

                    System.out.print("Confirme sua senha: ");
                    password = String.valueOf(sc.nextLine().hashCode());

                    user.getAccount().transfer(value, password, transferKey);
                    break;
                case 4:
                    user.getAccount().loadTransactions();

                    List<Transaction> transactions = user.getAccount().getTransactions();

                    for (Transaction t : transactions) {
                        System.out.println();
                        System.out.println(t.viewProof());
                    }
                    break;
                case 5:
                    System.out.printf("\nSaldo: R$%.2f\n", user.getAccount().getBalance());
                    break;
                case 6:
                    System.out.println();
                    System.out.println(user.getAccount().viewDataAccount());
                    break;
                case 7:
                    System.out.println();
                    System.out.println(user.viewDataUser());
                    break;
                case 8:
                    System.out.println("Logout realizado com sucesso!");
                    break;
                default:
                    System.out.println("Opção inválida!");
                    break; 
            }

        } while (opcao != 8);
    }
}
