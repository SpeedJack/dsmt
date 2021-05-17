import java.sql.*;
import java.sql.DriverManager;
import com.mysql.jdbc.Driver;

public class UserUtility {

    private final String jdbcURL = "jdbc:mysql://localhost:3306/auctions?useSSL=false";
    private final String dbUser = "root";
    private final String dbPassword = "root";

    public User login(String username, String password) throws SQLException,
            ClassNotFoundException {

        Class.forName("com.mysql.jdbc.Driver");
        Connection connection = DriverManager.getConnection(jdbcURL, dbUser, dbPassword);
        String sql = "SELECT * FROM user WHERE username = ? and password = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, username);
        statement.setString(2, password);

        ResultSet result = statement.executeQuery();
        User user = null;

        if (result.next()) {
                user = new User();
                user.setUsername(result.getString("username"));
            }
        connection.close();

        return user;
        }

    public User registration(String username, String password) throws SQLException,
            ClassNotFoundException {

        User user = null;
        Class.forName("com.mysql.jdbc.Driver");
        Connection connection = DriverManager.getConnection(jdbcURL, dbUser, dbPassword);
        String sql1 = "SELECT * FROM user WHERE username = ?";
        PreparedStatement statement1 = connection.prepareStatement(sql1);
        statement1.setString(1, username);
        ResultSet result = statement1.executeQuery();

        if (result.next()) {
            connection.close();
            return user;
        }

        String sql2 = "INSERT INTO user VALUES (?,?)";
        PreparedStatement statement2 = connection.prepareStatement(sql2);
        statement2.setString(1, username);
        statement2.setString(2, password);
        statement2.execute();
        user = new User();
        user.setUsername(username);
        connection.close();
        return user;
    }
    public User modify(String username, String oldPassword, String newPassword) throws SQLException,
            ClassNotFoundException {

        User user = null;
        Class.forName("com.mysql.jdbc.Driver");
        Connection connection = DriverManager.getConnection(jdbcURL, dbUser, dbPassword);
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
        }

        connection.close();
        return user;
    }

}
