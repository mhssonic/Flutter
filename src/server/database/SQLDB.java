package server.database;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class SQLDB {
    private static Connection connection;
    protected static Statement statement;

    private static void creatConnection() throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/Flutter";
        String user = "postgres";
        String password = "pashmak";

        connection = DriverManager.getConnection(url, user, password);
        statement = connection.createStatement();
    }

    private static void creatTable(String tableName){
        
    }


}
