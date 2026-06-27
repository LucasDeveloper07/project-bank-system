package dao.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

import dao.UserDAO;
import dao.db.DB;
import dao.db.DbException;
import entities.ClientPf;
import entities.ClientPj;
import entities.User;
import exceptions.UserException;

public class UserDAOJDBC implements UserDAO {

    private Connection conn = null;

    public UserDAOJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(User user) {
        PreparedStatement st = null;
        
        try {
            st = conn.prepareStatement("INSERT INTO user " 
                + "(name, email, password, birth_date, type, cpf, cnpj) "
                + "VALUES "
                + "(?, ?, ?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS);

            st.setString(1, user.getName());
            st.setString(2, user.getEmail());
            st.setString(3, user.getPassword());
            st.setDate(4, java.sql.Date.valueOf(user.getBirthDate()));

            if (user instanceof ClientPf clientPf) {
                st.setString(5, "PF");
                st.setString(6, clientPf.getCpf());
                st.setString(7, null);
            } else if (user instanceof ClientPj clientPj) {
                st.setString(5, "PJ");
                st.setString(7, clientPj.getCnpj());
                st.setString(6, null);
            }
            
            int rowsAffect = st.executeUpdate();

            if (rowsAffect > 0) {
                ResultSet rs = st.getGeneratedKeys();

                if (rs.next()) {
                    int id = rs.getInt(1);
                    user.setId(id);
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

    @Override
    public User login(String cpf_cnpj, String email, String password) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement("SELECT email, cpf, cnpj, password FROM user "
                + "WHERE email = ?");

            st.setString(1, email);

            rs = st.executeQuery();

            if (rs.next()) {
                String verifDate;
                boolean verifCpf;
    
                if (cpf_cnpj.length() == 11) {
                    verifDate = rs.getString(2);
                    verifCpf = true;
    
                    if (!cpf_cnpj.equals(verifDate)) {
                        throw new UserException("Usuário ou senha inválidos");
                    }
                } else if (cpf_cnpj.length() == 14) {
                    verifDate = rs.getString(3);
                    verifCpf = false;
                    
                    if (!cpf_cnpj.equals(verifDate)) {
                        throw new UserException("Usuário ou senha inválidos");
                    }
                } else {
                    throw new UserException("Erro! CPF ou CNPJ inválido.");
                }
                
                verifDate = rs.getString(4);
                
                if (!password.equals(verifDate)) {
                    throw new UserException("Usuário ou senha inválidos");
                }
    
                st = conn.prepareStatement("SELECT * FROM user "
                    + "WHERE email = ?");
    
                st.setString(1, email);
    
                rs = st.executeQuery();
                
                if (rs.next()) {
                    User user = createUser(rs, verifCpf);
                    
                    return user;
                }

                return null;
            } else {
                throw new UserException("Usuário ou senha inválidos!");
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    private User createUser(ResultSet rs, boolean verifCpf) throws SQLException {
        User user = null;

        if (verifCpf) {
            int id = rs.getInt(1);
            String name = rs.getString(2);
            String email = rs.getString(3);
            String password = rs.getString(4);
            LocalDate birthDate = rs.getDate(5).toLocalDate();
            Long cpf = rs.getLong(7);

            user = new ClientPf(name, email, password, birthDate, cpf);
            user.setId(id);

            return user;
        } else {
            int id = rs.getInt(1);
            String name = rs.getString(2);
            String email = rs.getString(3);
            String password = rs.getString(4);
            LocalDate birthDate = rs.getDate(5).toLocalDate();
            Long cnpj = rs.getLong(8);

            user = new ClientPf(name, email, password, birthDate, cnpj);
            user.setId(id);

            return user;
        }
    }
}
