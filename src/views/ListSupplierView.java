package views;

import core.DBSession;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ListSupplierView extends View {
    @FXML private GridPane supplierList;

    @Override
    public String getTitle() {
        return "List Suppliers";
    }

    @Override
    public void onStart() {
        if (context == null || !context.containsKey("db")) {
            System.err.println("Lost connection to database");
            System.exit(1);
        }
        DBSession db = (DBSession) context.get("db");

        ResultSet rs = db.executeQuery("SELECT * FROM Suppliers");
        try {
            int i = 0;
            while (rs.next()) {
                Node[] rowNodes = createSupplierEntry(
                        db,
                        rs.getInt("SupplierID"),
                        rs.getString("CompanyName"),
                        rs.getString("ContactName"),
                        rs.getString("City"));

                supplierList.addRow(i++, rowNodes);
            }
        } catch (SQLException e) {
            System.err.println("Failed to execute query");
            e.printStackTrace();
        }
    }

    private Node[] createSupplierEntry(DBSession db, int id, String companyName, String contact, String city) {
        // Create the edit button node and set initial properties
        Button editBtn = new Button("Edit");
        editBtn.setOnAction(event -> {
            Map<String, Object> ctx = new HashMap<>();
            ctx.put("db", db);
            ctx.put("supplierID", id);

            View.loadView("layout_edit_supplier", ctx).start();
        });

        // Create and order and return remaining nodes with an array
        return new Node[] {
                new Label(Integer.toString(id)),
                new Label(companyName),
                new Label(contact),
                new Label(city),
                editBtn
        };
    }
}
