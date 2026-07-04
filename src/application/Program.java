package application;

import java.util.Scanner;

import dao.CurrentAccountDAO;
import dao.SavingsAccountDAO;
import dao.UserDAO;
import dao.db.DAOFactory;
import entities.User;

public class Program {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        UserDAO userDao = DAOFactory.createUserDAO();
        CurrentAccountDAO currentDao = DAOFactory.createCurrentAccountDAO();
        SavingsAccountDAO savingsDao = DAOFactory.createSavingsAccountDAO();

        User user = LoginProgram.login(sc, userDao, currentDao, savingsDao);

        if (user != null) {
            MenuProgram.Menu(sc, user);
        }

        sc.close();
    }
}
