package administration.DAL;

import administration.DTO.UserDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO_3_Database implements IUserDAO {
    private Connection con;

    public UserDAO_3_Database(){
        connectToSql();
    }

    @Override
    public UserDTO getUser(int userId) throws DALException {
        try{
            ResultSet userResultSet = executeSelect("SELECT * FROM users WHERE id=" + userId + ";");
            if(userResultSet.next()) return getUserDB(userResultSet);

        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public UserDTO getUser(String username) throws DALException {
        try{
            ResultSet userResultSet = executeSelect("SELECT * FROM users WHERE username='" + username + "';");
            if(userResultSet.next()) return getUserDB(userResultSet);

        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public List<UserDTO> getUserList() throws DALException {
        List<UserDTO> list = new ArrayList<>();
        try{
            ResultSet userResultSet = executeSelect("SELECT * FROM users");
            while (userResultSet.next()){
                list.add(getUserDB(userResultSet));
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void createUser(UserDTO user) throws DALException {
        try{
            executeUpdate("INSERT INTO users VALUES ("+user.getUserId()+", \""+user.getUserName()+"\", \""+user.getPassword()+"\", \""+user.getCpr()+"\");");
            updateUserRolesDB(user);
        } catch (SQLException e){
            e.printStackTrace();
            throw new DALException("Brugeren kunne ikke oprettes");
        }
    }

    @Override
    public void updateUser(UserDTO user) throws DALException {
        try{
            executeUpdate("UPDATE users SET username=\""+user.getUserName()+"\", password=\""+user.getPassword()+"\", cprnummer=\""+user.getCpr()+"\" WHERE id="+user.getUserId()+";");
            executeUpdate("DELETE FROM userroles WHERE userid=" + user.getUserId() + ";");
            updateUserRolesDB(user);
        } catch (SQLException e){
            e.printStackTrace();
            throw new DALException("Brugeren kunne ikke opdateres");
        }
    }

    @Override
    public void deleteUser(int userId) throws DALException {
        try{
            executeUpdate("DELETE FROM userroles WHERE userid=" + userId + ";");
            executeUpdate("DELETE FROM users WHERE id=" + userId + ";");
        } catch (SQLException e){
            e.printStackTrace();
            throw new DALException("Brugeren kunne ikke slettes");
        }
    }


    private UserDTO getUserDB(ResultSet userResultSet){
        try {
            UserDTO user = new UserDTO();
            user.setUserId(userResultSet.getInt(1));
            user.setUserName(userResultSet.getString(2));
            user.setPassword(userResultSet.getString(3));
            user.setCpr(userResultSet.getString(4));

            ResultSet rolesResultSet = executeSelect("SELECT username, rolename FROM roles, userroles, users WHERE roles.id = userroles.roleid AND users.id = userroles.userid AND users.id = " + user.getUserId() + ";");
            List<String> roles = new ArrayList<>();
            while(rolesResultSet.next()){
                roles.add(rolesResultSet.getString(2));
            }
            user.setRoles(roles);
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void updateUserRolesDB(UserDTO user) throws SQLException, DALException {
        for (String role: user.getRoles()) {
            ResultSet resultSet = executeSelect("SELECT id FROM roles WHERE rolename='" + role + "';");
            if(resultSet.next()) {
                executeUpdate("INSERT INTO userroles VALUES (" + user.getUserId() + ", "+resultSet.getInt(1)+");");
            }
            else {
                throw new DALException("Rollen " + role + " eksisterer ikke i DB");
            }
        }
    }

    private void connectToSql() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/userdao3", "root", "kagemand123");
        } catch (SQLException | ClassNotFoundException sqlException) {
            System.out.println(sqlException);
        }
    }
    private ResultSet executeSelect(String statement) throws SQLException{
        Statement stmt = con.createStatement();
        return stmt.executeQuery(statement);
    }
    private void executeUpdate(String statement) throws SQLException{
        Statement stmt = con.createStatement();
        stmt.executeUpdate(statement);
    }

    public void closeSql(){
        try{
            con.close();
        } catch(SQLException e){
            e.printStackTrace();
        }
    }
}
