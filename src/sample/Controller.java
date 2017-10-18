package sample;

import core.DBSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

import java.sql.*;
import com.microsoft.sqlserver.jdbc.*;

public class Controller {
    @FXML private Text actionTarget;

    @FXML
    protected void handleSubmitButtonAction(ActionEvent event) {
        actionTarget.setText("Sign in button pressed");

        Connection con = null;
        try {
            // Establish the connection.
            DBSession db = new DBSession(
                    "DESKTOP-4P4A4L1\\MSSQLSERVER01",
                    "Northwind",
                    "test",
                    "test");
            if (db.isConnected()){
                ResultSet rs = db.executeQuery("SELECT * FROM Products");
                while (rs.next()) {
                    for (int i = 1; i < rs.getMetaData().getColumnCount(); i++)
                        System.out.print(rs.getString(i) + " ");
                    System.out.println();
                }
            }
            actionTarget.setText("Success!");
        } catch (SQLServerException e) {
            actionTarget.setText("Fail!");
            e.printStackTrace();
        } catch (SQLException e) {
            actionTarget.setText("Fail!");
            e.printStackTrace();
        }
    }
}
