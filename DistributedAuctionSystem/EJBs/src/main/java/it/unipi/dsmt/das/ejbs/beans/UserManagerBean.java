package it.unipi.dsmt.das.ejbs.beans;

import it.unipi.dsmt.das.ejbs.beans.interfaces.UserManager;
import it.unipi.dsmt.das.model.User;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.sql.DataSource;
import java.sql.*;

@Stateless(name = "UserManagerEJB")
public class UserManagerBean implements UserManager {
    @Resource(lookup = "jdbc/dsmt")
    DataSource ds;

    public UserManagerBean() {

    }

    @Override
    public User login(String username, String password) {
        System.out.println(username + " " + password);


        Connection connection;
        User user = null;
        try {
            connection = ds.getConnection();

            String sql = "SELECT * FROM user WHERE username = ? and password = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, password);

            ResultSet result = statement.executeQuery();

            if (result.next()) {
                user = new User();
                user.setId(result.getInt("id"));
                user.setUsername(result.getString("username"));
            }
            connection.close();

        } catch (SQLException ex) {
            return null;
        }
        return user;

    }

    public User registration(String username, String password) throws SQLException {

        User user = null;
        Connection connection = null;
        System.out.println("REGISTRATION");
        try {
            System.out.println("GET CONNECTION");
            connection = ds.getConnection();

            String sql1 = "SELECT * FROM user WHERE username = ?";
            PreparedStatement statement1 = connection.prepareStatement(sql1);
            statement1.setString(1, username);
            ResultSet result = statement1.executeQuery();
            System.out.println("EXECUTE QUERY");
            if (result.next()) {
                connection.close();
                return null;
            }
            String sql2 = "INSERT INTO user(username, password) VALUES (?,?)";
            PreparedStatement statement2 = connection.prepareStatement(sql2);
            statement2.setString(1, username);
            statement2.setString(2, password);
            statement2.execute();
            user = new User();
            user.setUsername(username);
            connection.close();
        } catch (SQLException ex){
            ex.printStackTrace();
            return null;
        }
        return user;
    }

    public User modify(String username, String oldPassword, String newPassword) {

        User user = null;

        Connection connection = null;
        try {
            connection = ds.getConnection();

            String sql1 = "SELECT * FROM user WHERE username = ? AND password = ?";
            PreparedStatement statement1 = connection.prepareStatement(sql1);
            statement1.setString(1, username);

            statement1.setString(2, oldPassword);
            ResultSet result = statement1.executeQuery();

            if (result.next()) {
                String sql2 = "UPDATE user SET password = ? WHERE username = ?";
                PreparedStatement statement2 = connection.prepareStatement(sql2);
                statement2.setString(1, newPassword);
                statement2.setString(2, username);
                statement2.execute();
                user = new User();
                user.setUsername(result.getString("username"));
                connection.close();
            }
        } catch (SQLException ex) {
            return null;
        }

        return user;
    }
}