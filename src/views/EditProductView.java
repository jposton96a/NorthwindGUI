package views;

import core.DBSession;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class EditProductView extends View {
    Map<Integer, RadioButton> categoriesByID = new HashMap<>();
    Map<Integer, RadioButton> suppliersByID = new HashMap<>();
    ToggleGroup categoryGroup = new ToggleGroup();
    ToggleGroup supplierGroup = new ToggleGroup();

    @FXML private VBox vboxCategories;
    @FXML private VBox vboxSuppliers;
    @FXML private TextField textName;
    @FXML private TextField textQuantity;
    @FXML private TextField textCost;
    @FXML private Spinner<Integer> spinStock;
    @FXML private Spinner<Integer> spinOrdered;
    @FXML private Spinner<Integer> spinReLvl;
    @FXML private CheckBox checkDiscont;
    @FXML private Button btnExit;


    @FXML
    public void initialize(){
        spinStock.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0));
        spinOrdered.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0));
        spinReLvl.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0));
        btnExit.setOnAction(event -> this.goBack());
    }

    @Override
    public void onStart() {
        if (context == null || !context.containsKey("db")){
            System.err.println("Lost connection to database");
            System.exit(1);
        }
        DBSession db = (DBSession) context.get("db");

        loadCategories(db);
        loadSuppliers(db);

        if (context.containsKey("productID")) {
            loadProduct(db, (Integer) context.get("productID"));
        }
    }

    private void loadProduct(DBSession db, int productID) {
        ResultSet rs = db.executeQuery("SELECT * FROM Products WHERE ProductID = " + productID);
        try {
            if (rs.next()) {
                textName.setText(rs.getString("ProductName"));
                suppliersByID.get(Integer.valueOf(rs.getString("SupplierID"))).setSelected(true);
                categoriesByID.get(Integer.valueOf(rs.getString("CategoryID"))).setSelected(true);
                textQuantity.setText(rs.getString("QuantityPerUnit"));
                textCost.setText(String.valueOf(rs.getDouble("UnitPrice")));

                spinStock.getValueFactory().setValue(rs.getInt("UnitsInStock"));
                spinOrdered.getValueFactory().setValue(rs.getInt("UnitsOnOrder"));
                spinReLvl.getValueFactory().setValue(rs.getInt("ReorderLevel"));

                if(rs.getInt("Discontinued") == 1)
                    checkDiscont.setSelected(true);
            }
        } catch (SQLException e) {
            System.err.println("Unable to find product matching ID = " + productID);
        }
    }

    private void loadCategories(DBSession db) {
        ResultSet rs = db.executeQuery("SELECT CategoryID, CategoryName, Description FROM Categories ORDER BY CategoryName");
        try {
            while (rs.next()) {
                RadioButton entry = new RadioButton();
                entry.setText(rs.getString("CategoryName"));
                entry.setTooltip(new Tooltip(rs.getString("Description")));
                entry.setToggleGroup(categoryGroup);
                entry.setPadding(new Insets(3, 0,3,0));
                categoriesByID.put(Integer.valueOf(rs.getString("CategoryID")), entry);
                vboxCategories.getChildren().add(entry);
            }
        } catch (SQLException e){
            System.err.println("Unable to load categories");
            e.printStackTrace();
        }
    }

    private void loadSuppliers(DBSession db) {
        ResultSet rs = db.executeQuery("SELECT SupplierID, CompanyName, City, Region, Country FROM Suppliers ORDER BY CompanyName");
        try {
            while (rs.next()) {
                RadioButton entry = new RadioButton();
                entry.setText(rs.getString("CompanyName"));

                String tipText = String.format("%s, %s - %s",
                        rs.getString("City"),
                        rs.getString("Region"),
                        rs.getString("Country"));

                entry.setTooltip(new Tooltip(tipText));
                entry.setToggleGroup(supplierGroup);
                entry.setPadding(new Insets(3, 0,3,0));

                suppliersByID.put(Integer.valueOf(rs.getString("SupplierID")), entry);
                vboxSuppliers.getChildren().add(entry);
            }
        } catch (SQLException e){
            System.err.println("Unable to load suppliers");
            e.printStackTrace();
        }
    }

    @Override
    public int getWidth() {
        return 750;
    }

    @Override
    public int getHeight() {
        return 415;
    }

    @Override
    public String getTitle() {
        return "Edit Product";
    }
}
