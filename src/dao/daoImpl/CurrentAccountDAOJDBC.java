package dao.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import dao.CurrentAccountDAO;
import dao.UserDAO;
import dao.db.DAOFactory;
import dao.db.DB;
import dao.db.DbException;
import entities.Account;
import entities.CurrentAccount;
import entities.SavingsAccount;
import exceptions.UserException;

public class CurrentAccountDAOJDBC implements CurrentAccountDAO {

    private Connection conn = null;

    // Construtor para iniciar a classe com a conexão com o banco de dados já criada
    public CurrentAccountDAOJDBC(Connection conn) {
        this.conn = conn;
    }

    // Método para inserir a currentAccount criada no banco de dados
    @Override
    public void insert(CurrentAccount currentAccount) {
        PreparedStatement st = null;
        
        try {
            st = conn.prepareStatement("INSERT INTO current_account "
                + "(num, agency, balance, transfer_key, creation_date, maintenance_date, user_id) "
                + "VALUES "
                + "(?, ?, ?, ?, ?, ?, ?)");

            st.setString(1, currentAccount.getNum());
            st.setString(2, currentAccount.getAgencyNum());
            st.setDouble(3, currentAccount.getBalance());
            st.setInt(4, currentAccount.getTransferKey());
            st.setDate(5, java.sql.Date.valueOf(currentAccount.getCreationDate()));
            st.setDate(6, java.sql.Date.valueOf(currentAccount.getMaintenanceDate()));
            st.setInt(7, currentAccount.getUser().getId());

            int rowsAffect = st.executeUpdate();

            if (rowsAffect == 0) {
                throw new DbException("Erro inesperado! Nenhuma linha foi atualizada.");
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    // Método para atualizar o saldo da conta após as operações bancárias (DEPOSIT, WITHDRAW, TRANSFER, INTEREST_CREDIT, MAINTENANCE_FEE)
    @Override
    public void updateBalance(CurrentAccount currentAccount) {
        PreparedStatement st = null;

        try {
            st = conn.prepareStatement("UPDATE current_account "
                + "SET balance = ? "
                + "WHERE num = ?");

            st.setDouble(1, currentAccount.getBalance());
            st.setString(2, currentAccount.getNum());

            int rowsAffect = st.executeUpdate();

            if (rowsAffect == 0) {
                throw new DbException("Erro inesperado! Nenhuma linha foi atualizada.");
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    // Método para atualizar a data de desconto da taxa de manutenção
    @Override
    public void updateMaintenanceDate(LocalDate date, String num) {
        PreparedStatement st = null;

        try {
            st = conn.prepareStatement("UPDATE current_account "
                + "SET maintenance_date = ? "
                + "WHERE num = ?");

            st.setDate(1, java.sql.Date.valueOf(date));
            st.setString(2, num);

            int rowsAffect = st.executeUpdate();

            if (rowsAffect == 0) {
                throw new DbException("Erro inesperado! Nenhuma linha foi atualizada.");
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    // Método para deletar a conta do banco de dados após o usuário encerrar a sua conta
    @Override
    public void delete(CurrentAccount currentAccount, String password) {
        // Verificação da senha do usuário para confirmar a operação
        if (!password.equals(currentAccount.getUser().getPassword())) {
            throw new UserException("Senha incorreta!");
        }
        
        PreparedStatement st = null;

        try {
            conn.setAutoCommit(false); // Controle manual da transação para manter a integridade do banco de dados

            // Chamada da operação delete da classe UserDAO para apagar o usuário
            UserDAO userDao = DAOFactory.createUserDAO();
            userDao.delete(currentAccount.getUser());

            // Operação para apagar a currentAccount
            st = conn.prepareStatement("DELETE FROM current_account "
                + "WHERE num = ?");

            st.setString(1, currentAccount.getNum());

            int rowsAffect = st.executeUpdate();

            if (rowsAffect == 0) {
                throw new DbException("Erro inesperado! Nenhuma linha foi atualizada.");
            }

            conn.commit(); // Chamada do método para confirmar a operação
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Em caso de exceção, o rollback desfaz todas as operações
                } catch (SQLException e1) {
                    throw new DbException(e1.getMessage());
                }
            }

            throw new DbException(e.getMessage());
        } catch (DbException e1) {
            if (conn != null) {
                try {
                    conn.rollback(); // Em caso de exceção, o rollback desfaz todas as operações
                } catch (SQLException e2) {
                    throw new DbException(e2.getMessage());
                }
            }

            throw new DbException(e1.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    throw new DbException(e.getMessage());
                }
            }

            DB.closeStatement(st);
        }
    }

    // Método para buscar currentAccount pelo id do usuário (Este método é usado no login para iniciar a conta do usuário)
    @Override
    public CurrentAccount findByUserId(int id) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement("SELECT * FROM current_account "
                + "WHERE user_id = ?");

            st.setInt(1, id);

            rs = st.executeQuery();

            CurrentAccount account = createCurrentAccount(rs);

            return account;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    // Método para buscar currentAccount pela chave de transferência (Usado na operação bancária TRANSFER)
    @Override
    public CurrentAccount findByTransferKey(int transferKey) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement("SELECT * FROM current_account "
                + "WHERE transfer_key = ?");

            st.setInt(1, transferKey);

            rs = st.executeQuery();

            CurrentAccount account = createCurrentAccount(rs);

            return account;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    // Método para buscar currentAccount pelo numero da conta
    @Override
    public CurrentAccount findByNum(String num) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement("SELECT * FROM current_account "
                + "WHERE num = ?");

            st.setString(1, num);

            rs = st.executeQuery();

            CurrentAccount account = createCurrentAccount(rs);

            return account;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    // Método para realizar a transferência entre contas
    @Override
    public void transfer(CurrentAccount originAccount, Account destinationAccount, double value) {
        PreparedStatement st = null;

        try {
            conn.setAutoCommit(false);  // Controle manual da transação para manter a integridade do banco de dados

            // Retirada do saldo da conta de origem
            st = conn.prepareStatement("UPDATE current_account "
                + "SET balance = ? "
                + "WHERE num = ?");

            st.setDouble(1, originAccount.getBalance() - value);
            st.setString(2, originAccount.getNum());

            int rowsAffect = st.executeUpdate();

            if (rowsAffect == 0) {
                throw new DbException("Erro inesperado! Nenhuma linha foi atualizada.");
            }

            DB.closeStatement(st);

            // Verificação para saber a instância da conta destino e buscar no banco de dados
            if (destinationAccount instanceof CurrentAccount) {
                st = conn.prepareStatement("UPDATE current_account "
                    + "SET balance = ? "
                    + "WHERE num = ?");

                st.setDouble(1, destinationAccount.getBalance() + value);
                st.setString(2, destinationAccount.getNum());

                rowsAffect = st.executeUpdate();

                if (rowsAffect == 0) {
                    throw new DbException("Erro inesperado! Nenhuma linha foi atualizada.");
                }
            } else if (destinationAccount instanceof SavingsAccount) {
                st = conn.prepareStatement("UPDATE savings_account "
                    + "SET balance = ? "
                    + "WHERE num = ?");

                st.setDouble(1, destinationAccount.getBalance() + value);
                st.setString(2, destinationAccount.getNum());

                rowsAffect = st.executeUpdate();

                if (rowsAffect == 0) {
                    throw new DbException("Erro inesperado! Nenhuma linha foi atualizada.");
                }
            } else {
                throw new DbException("Erro de transferência!");
            }

            conn.commit(); // Chamada do método para confirmar a operação
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Em caso de exceção, o rollback desfaz todas as operações
                } catch (SQLException e1) {
                    throw new DbException(e1.getMessage());
                }
            }

            throw new DbException(e.getMessage());
        } catch (DbException e1) {
            if (conn != null) {
                try {
                    conn.rollback(); // Em caso de exceção, o rollback desfaz todas as operações
                } catch (SQLException e2) {
                    throw new DbException(e2.getMessage());
                }
            }

            throw new DbException(e1.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    throw new DbException(e.getMessage());
                }
            }

            DB.closeStatement(st);
        }
    }

    // Método privado para criar a currentAccount através do ResultSet
    private CurrentAccount createCurrentAccount(ResultSet rs) throws SQLException {
        UserDAO userDao = DAOFactory.createUserDAO();

        CurrentAccount account = null;

        if (rs.next()) {
            String num = rs.getString(1);
            String agencyNum = rs.getString(2);
            Double balance = rs.getDouble(3);
            Integer transferKey = rs.getInt(4);
            LocalDate creationDate = rs.getDate(5).toLocalDate();
            LocalDate maintenanceDate = rs.getDate(6).toLocalDate();
            int user_id = rs.getInt(7);
            
            account = new CurrentAccount(num, agencyNum, balance, creationDate, transferKey, maintenanceDate);
            account.setUser(userDao.findById(user_id, account));

            return account;
        }

        return null;
    }
}
