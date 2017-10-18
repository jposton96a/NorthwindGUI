package core;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBSession {
    private Connection con;

    public DBSession(String host, String database, String username, String password) {
        con = null;
        try {
            SQLServerDataSource ds = new SQLServerDataSource();
            ds.setIntegratedSecurity(true);
            ds.setServerName(host);
            ds.setDatabaseName(database);
            ds.setUser(username);
            ds.setPassword(password);
            con = ds.getConnection();
        } catch (SQLServerException e) {
            System.err.printf("Unable to connect to database : %s@%s:%s:%s\n", database, host, username, password);
            System.err.println(e.toString());
            //e.printStackTrace();
        }
    }

    public ResultSet executeQuery(String query) {
        ResultSet rs = null;
        try {
            Statement statement = con.createStatement();
            rs = statement.executeQuery(query);
        } catch (SQLException e) {
            System.err.printf("");
            e.printStackTrace();
        }
        return rs;
    }

    public boolean isConnected(){
        return con != null;
    }
}
