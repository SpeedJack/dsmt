import it.unipi.dsmt.das.UserEntity;

import javax.annotation.Resource;
import javax.naming.InitialContext;

import javax.naming.NamingException;
import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;

public class Test2 {

    @Resource(name = "jdbc/dsmt")
    static private DataSource dataSource;

    public static void main(String args[]) {

        Statement stmt;
        ResultSet rs;

        try {
            InitialContext ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("jdbc/DemoJNDI");
            Connection conn = (Connection) ds.getConnection();
            stmt = (Statement) conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM user");
            while(rs.next()) {
                System.out.println(rs.getInt("id"));
                System.out.println(rs.getString("name"));
                System.out.println(rs.getString("password"));
            }

        } catch (SQLException | NamingException ne) {
            System.out.println(ne.getMessage());
        }
    }
}
