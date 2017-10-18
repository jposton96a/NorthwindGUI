package views;

import core.DBSession;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class EditSupplierView extends View {
    private Map<Integer, CheckBox> productsByID = new HashMap<>();
    private CheckboxGroup productsGroup = new CheckboxGroup();

    @FXML private TextField textCompany;
    @FXML private TextField textContact;
    @FXML private TextField textTitle;

    @FXML private TextField textAddr;
    @FXML private TextField textCity;
    @FXML private TextField textCountry;
    @FXML private TextField textZIP;
    @FXML private TextField textRegion;

    @FXML private TextField textHomepage;
    @FXML private TextField textPhoneNum;
    @FXML private TextField textFaxNum;

    @FXML private VBox vboxProducts;

    @FXML private Button btnExit;

    @FXML
    public void initialize(){
        btnExit.setOnAction(event -> this.goBack());
    }

    @Override
    public String getTitle() {
        return "Edit Supplier";
    }

    @Override
    public void onStart() {
        if (context == null || !context.containsKey("db")){
            System.err.println("Lost connection to database");
            System.exit(1);
        }
        DBSession db = (DBSession) context.get("db");

        loadProducts(db);

        if (context.containsKey("supplierID")) {
            loadSupplier(db, (Integer) context.get("supplierID"));
        }
    }

    private void loadSupplier(DBSession db, int supplierID) {
        ResultSet rs = db.executeQuery("SELECT * FROM Suppliers WHERE SupplierID = " + supplierID);
        ResultSet suppliedProducts = db.executeQuery("SELECT ProductID FROM Products WHERE SupplierID = " + supplierID);
        try {
            if (rs.next()) {
                textCompany.setText(rs.getString("CompanyName"));
                textContact.setText(rs.getString("ContactName"));
                textTitle.setText(rs.getString("ContactTitle"));

                textAddr.setText(rs.getString("Address"));
                textCity.setText(rs.getString("City"));
                textRegion.setText(rs.getString("Region"));
                textZIP.setText(rs.getString("PostalCode"));
                textCountry.setText(rs.getString("Country"));

                textPhoneNum.setText(rs.getString("Phone"));
                textFaxNum.setText(rs.getString("Fax"));
                textHomepage.setText(rs.getString("HomePage"));

                while (suppliedProducts.next()){
                    int prodID = suppliedProducts.getInt("ProductID");
                    productsByID.get(prodID).setSelected(true);
                }
            }
        } catch (SQLException e) {
            System.err.println("Unable to find product matching ID = " + supplierID);
        }
    }

    private void loadProducts(DBSession db) {
        ResultSet rs = db.executeQuery("SELECT * FROM Products ORDER BY ProductName");
        try {
            while (rs.next()) {
                CheckBox entry = new CheckBox();
                entry.setText(rs.getString("ProductName"));

                String tipText = "Whoops... This still needs to be implemented!";
                entry.setTooltip(new Tooltip(tipText));

                //entry.setToggleGroup(supplierGroup);
                entry.setPadding(new Insets(3, 0,3,0));

                productsByID.put(Integer.valueOf(rs.getString("ProductID")), entry);
                vboxProducts.getChildren().add(entry);
            }
        } catch (SQLException e){
            System.err.println("Unable to load suppliers");
            e.printStackTrace();
        }
    }

    @Override
    public int getWidth() {
        return 700;
    }
}