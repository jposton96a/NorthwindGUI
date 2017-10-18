package views;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MainMenu extends View {
    @FXML private Button btnListProd;
    @FXML private Button btnEditProd;
    @FXML private Button btnListSupp;
    @FXML private Button btnEditSupp;

    @FXML
    public void initialize() {

    }

    @Override
    public String getTitle() {
        return "Northwind Menu";
    }

    @Override
    public void onStart() {
        btnListProd.setOnAction( event -> View.loadView("layout_list_product", context).start());
        btnEditProd.setOnAction( event -> View.loadView("layout_edit_product", context).start());
        btnListSupp.setOnAction( event -> View.loadView("layout_list_supplier", context).start());
        btnEditSupp.setOnAction( event -> View.loadView("layout_edit_supplier", context).start());
    }
}
