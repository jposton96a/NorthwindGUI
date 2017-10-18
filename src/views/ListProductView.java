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

public class ListProductView extends View {
    @FXML private GridPane productList;
    @FXML private Button btnBack;

    @FXML
    public void initialize() {
        btnBack.setOnAction(event -> this.goBack());
        productList.setHgap(10);
        productList.setVgap(5);
    }

    @Override
    public String getTitle() {
        return "Product List";
    }

    @Override
    public void onStart() {
        if (context == null || !context.containsKey("db")) {
            System.err.println("Lost connection to database");
            System.exit(1);
        }
        DBSession db = (DBSession) context.get("db");

        ResultSet rs = db.executeQuery("SELECT * FROM PRODUCTS");
        try {
            int i = 0;
            while (rs.next()) {
                Node[] rowNodes = createProductEntry(
                        db,
                        rs.getInt("ProductID"),
                        rs.getString("ProductName"),
                        rs.getString("QuantityPerUnit"));

                productList.addRow(i++, rowNodes);
            }
        } catch (SQLException e) {
            System.err.println("Failed to execute query");
            e.printStackTrace();
        }
    }

    private Node[] createProductEntry(DBSession db, int id, String name, String qPerUnit) {
        // Create the edit button node and set initial properties
        Button editBtn = new Button("Edit");
        editBtn.setOnAction(event -> {
            Map<String, Object> ctx = new HashMap<>();
            ctx.put("db", db);
            ctx.put("productID", id);

            View.loadView("layout_edit_product", ctx).start();
        });

        // Create and order and return remaining nodes with an array
        return new Node[] {
                new Label(Integer.toString(id)),
                new Label(name),
                new Label(qPerUnit),
                editBtn
        };
    }
}