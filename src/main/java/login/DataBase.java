package login;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBase {
    public static void main(String[] args) {

        Connection connection = null;
        try {

            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:src/main/java/login/sample.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.


            statement.executeUpdate("drop table if exists account");
            statement.executeUpdate("create table account (username string, password string)");

            statement.executeUpdate("insert into account values('rasmus', 'pswd')");
            statement.executeUpdate("insert into account values('abcd', '1234')");

            ResultSet rs = getAllRegisteredUsers(statement);

            System.out.println("Kõik kasutajad: ");
            while(rs.next()) {
                // read the result set
                System.out.println("name = " + rs.getString("username"));
            }
        }

        catch(SQLException e)
        {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }

        finally
        {
            try {
                if(connection != null)
                    connection.close();
            }
            catch(SQLException e)
            {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }
    }

    public static ResultSet getAllRegisteredUsers(Statement statement) throws SQLException {
        return statement.executeQuery("select * from account");
    }


}