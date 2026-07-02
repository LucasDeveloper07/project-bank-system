package dao.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

import dao.CurrentAccountDAO;
import dao.SavingsAccountDAO;
import dao.UserDAO;
import dao.db.DAOFactory;
import dao.db.DB;
import dao.db.DbException;
import entities.ClientPf;
import entities.ClientPj;
import entities.CurrentAccount;
import entities.SavingsAccount;
import entities.User;
import exceptions.UserException;

public class UserDAOJDBC implements UserDAO {

    private Connection conn = null;

    // Construtor para iniciar a classe com a conexão com o banco de dados já criada
    public UserDAOJDBC(Connection conn) {
        this.conn = conn;
    }

    // Método para inserir user criado no banco de dados
    @Override
    public void insert(User user) {
        PreparedStatement st = null;
        
        try {
            st = conn.prepareStatement("INSERT INTO user " 
                + "(name, email, password, birth_date, type, type_account, cpf, cnpj) "
                + "VALUES "
                + "(?, ?, ?, ?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS);

            // A coluna type_account no banco, auxilia na busca da conta quando o usuário fizer o login 

            st.setString(1, user.getName());
            st.setString(2, user.getEmail());
            st.setString(3, user.getPassword());
            st.setDate(4, java.sql.Date.valueOf(user.getBirthDate()));

            // Verificação para saber a instância de User
            if (user instanceof ClientPf clientPf) {
                st.setString(5, "PF");
                st.setString(7, clientPf.getCpf());
                st.setString(8, null);

                // Verificação para saber a instância de Account
                if (user.getAccount() instanceof CurrentAccount) {
                    st.setString(6, "CURRENT");
                } else if (user.getAccount() instanceof SavingsAccount) {
                    st.setString(6, "SAVINGS");
                }
            } else if (user instanceof ClientPj clientPj) {
                st.setString(5, "PJ");
                st.setString(8, clientPj.getCnpj());
                st.setString(7, null);

                if (user.getAccount() instanceof CurrentAccount) {
                    st.setString(6, "CURRENT");
                } else if (user.getAccount() instanceof SavingsAccount) {
                    st.setString(6, "SAVINGS");
                }
            }
            
            int rowsAffect = st.executeUpdate();

            if (rowsAffect > 0) {
                ResultSet rs = st.getGeneratedKeys();

                if (rs.next()) {
                    int id = rs.getInt(1);
                    user.setId(id); // Inserção da chave primária de user na instância
                }
            } else {
                throw new DbException("Erro inesperado! Nenhuma linha foi atualizada.");
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    // Método para atualizar a senha do usuário
    @Override
    public void updatePassword(User user) {
        PreparedStatement st = null;

        try {
            st = conn.prepareStatement("UPDATE user "
                + "SET password = ? "
                + "WHERE id = ?");

            st.setString(1, user.getPassword());
            st.setInt(2, user.getId());

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

    // Método para atualizar o nome do usuário
    @Override
    public void updateName(User user) {
        PreparedStatement st = null;

        try {
            st = conn.prepareStatement("UPDATE user "
                + "SET name = ? "
                + "WHERE id = ?");

            st.setString(1, user.getName());
            st.setInt(2, user.getId());

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

    // Método para deletar User do banco de dados
    @Override
    public void delete(User user) {
        PreparedStatement st = null;

        try {
            st = conn.prepareStatement("DELETE FROM user "
                + "WHERE id = ?");

            st.setInt(1, user.getId());

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

    // Método para realizar login do user
    @Override
    public User login(String cpf_cnpj, String email, String password) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement("SELECT * FROM user "
                + "WHERE email = ?");

            st.setString(1, email);

            rs = st.executeQuery();

            // Condição para verificar o email
            if (rs.next()) {
                String verifData; // Variável para realizar a verificação dos dados de login
                boolean verifCpf; // Variável para saber se é cpf ou cnpj
    
                if (cpf_cnpj.length() == 11) {
                    verifData = rs.getString(8);
                    verifCpf = true;
    
                    // Verifição do CPF informado pelo usuário
                    if (!cpf_cnpj.equals(verifData)) {
                        throw new UserException("Usuário ou senha inválidos");
                    }
                } else if (cpf_cnpj.length() == 14) {
                    verifData = rs.getString(9);
                    verifCpf = false;
                    
                    // Verificação do CNPJ informado pelo usuário
                    if (!cpf_cnpj.equals(verifData)) {
                        throw new UserException("Usuário ou senha inválidos");
                    }
                } else {
                    throw new UserException("Erro! CPF ou CNPJ inválido."); 
                }
                
                verifData = rs.getString(4);
                
                // Verificação da senha informada pelo usuário
                if (!password.equals(verifData)) {
                    throw new UserException("Usuário ou senha inválidos");
                }

                // Chamada do método para instanciar usuário após a verificação dos dados
                User user = createUser(rs, verifCpf);
                
                return user;
            } else {
                throw new UserException("Usuário ou senha inválidos!"); // Lançamento de exceção caso o email esteja inválido
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    // Método privado para criar usuário através do ResultSet e da variável verifCpf
    private User createUser(ResultSet rs, boolean verifCpf) throws SQLException {
        User user = null;

        // Instânciação do user com CPF
        if (verifCpf) {
            int id = rs.getInt(1);
            String name = rs.getString(2);
            String email = rs.getString(3);
            String password = rs.getString(4);
            LocalDate birthDate = rs.getDate(5).toLocalDate();
            Long cpf = rs.getLong(8);
            String typeAccount = rs.getString(7);

            // Verificação do tipo de conta do usuário para realizar a busca no banco de dados
            if (typeAccount.equals("CURRENT")) {
                CurrentAccountDAO currentDao = DAOFactory.createCurrentAccountDAO();

                // Chamada do construtor de ClientPf passando os atributos e a chamada do método findByUserId de CurrentAccount que retorna uma CurrentAccount
                user = new ClientPf(name, email, password, birthDate, cpf, currentDao.findByUserId(id));
                user.setId(id);

                return user;
            } else if (typeAccount.equals("SAVINGS")) {
                SavingsAccountDAO savingsDao = DAOFactory.createSavingsAccountDAO();

                // Chamada do construtor de ClientPf passando os atributos e a chamada do método findByUserId de SavingsAccount que retorna uma SavingsAccount
                user = new ClientPf(name, email, password, birthDate, cpf, savingsDao.findByUserId(id));
                user.setId(id);

                return user;
            }
        } else {
            // Instânciação do user com CNPJ
            int id = rs.getInt(1);
            String name = rs.getString(2);
            String email = rs.getString(3);
            String password = rs.getString(4);
            LocalDate birthDate = rs.getDate(5).toLocalDate();
            Long cnpj = rs.getLong(9);
            String typeAccount = rs.getString(7);

            // Verificação do tipo de conta do usuário para realizar a busca no banco de dados
            if (typeAccount.equals("CURRENT")) {
                CurrentAccountDAO currentDao = DAOFactory.createCurrentAccountDAO();

                // Chamada do construtor de ClientPj passando os atributos e a chamada do método findByUserId de CurrentAccount que retorna uma CurrentAccount
                user = new ClientPj(name, email, password, birthDate, cnpj, currentDao.findByUserId(id));
                user.setId(id);

                return user;
            } else if (typeAccount.equals("SAVINGS")) {
                SavingsAccountDAO savingsDao = DAOFactory.createSavingsAccountDAO();

                // Chamada do construtor de ClientPj passando os atributos e a chamada do método findByUserId de SavingsAccount que retorna uma SavingsAccount
                user = new ClientPf(name, email, password, birthDate, cnpj, savingsDao.findByUserId(id));
                user.setId(id);

                return user;
            }
        }

        return null;
    }
}
