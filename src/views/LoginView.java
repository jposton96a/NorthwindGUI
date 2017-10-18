package views;

import core.DBSession;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

public class LoginView extends View {
    @FXML private Text textStatus;
    @FXML private TextField textHost;
    @FXML private TextField textUser;
    @FXML private PasswordField textPass;

    @FXML
    public void handleConnectBtn() {
        textStatus.setText("Connecting...");
        DBSession db = new DBSession(
                textHost.getText(),
                "Northwind",
                textUser.getText(),
                textPass.getText());

        if (db != null && db.isConnected()) {
            textStatus.setText("Connection established!");
            Map<String, Object>  ctx = new HashMap<>();
            ctx.put("db", db);
            View.loadView("layout_main_menu", ctx).start();
        } else {
            textStatus.setText("Unable to establish connection to database.");
        }

        /*new Thread(() -> {
        }).start();*/
    }

    @FXML
    public void initialize() {
        try {
            String hostName = InetAddress.getLocalHost().getHostName();
            textHost.setText(hostName + "\\MSSQLSERVER01");
        }
        catch (UnknownHostException ex) {
            System.out.println("Hostname can not be resolved");
        }
        //textHost.setText("DESKTOP-4P4A4L1\\MSSQLSERVER01");
        textUser.setText("test");
        textPass.setText("test");
    }

    @Override
    public void onStart() {

    }

    @Override
    public String getTitle() {
        return "Database Connect";
    }
}
