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

            addAccountToDB(statement, "rasmus", "pswd");
            addAccountToDB(statement, "abcd", "1234");


            ResultSet rs = getAllRegisteredUsers(statement);


            // Testing
            System.out.println("Kõik kasutajad: ");
            while(rs.next()) {
                // read the result set
                System.out.println("kasutaja = " + rs.getString("username"));
            }

            System.out.println(isAccountRegistered(statement, "rasmus"));
            System.out.println(isAccountRegistered(statement, "brrrrrr"));

        }

        catch(SQLException e)
        {
            // if the error message is "out of memory",
            // siis arvatavasti vale db asukoht vms
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

    //Meetodid



    public static void addAccountToDB(Statement statement, String username, String password) throws SQLException {
        statement.executeUpdate("insert into account values(\'"+ username +"\', \'" + password+ "\')");
    }

    public static ResultSet getAllRegisteredUsers(Statement statement) throws SQLException {
        return statement.executeQuery("select * from account");
    }

    public static boolean isAccountRegistered(Statement statement, String username) throws SQLException {
        ResultSet rs = statement.executeQuery("select * from account where account.username = \'" + username + "\'");
        return rs.next();
    }

}