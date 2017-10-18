package views;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class View {
    private static final int DEFAULT_WIDTH = 640;
    private static final int DEFAULT_HEIGHT = 480;

    private static Stage viewStage;
    private static View currentView;

    private View nextView, lastView;
    private Scene scene;

    protected Map<String, Object> context;

    public abstract String getTitle();
    public abstract void onStart();

    private void show() {
        currentView = this;

        viewStage.setTitle(this.getTitle());
        viewStage.setScene(this.getScene());

        // Display the updated stage
        viewStage.show();
    }

    public void goBack() {
        if (this.lastView != null) {
            // Shift the current view pointer back
            currentView = this.lastView;
        }

        // Display the new view
        currentView.show();
    }

    public void start() {
        if (currentView == null) {
            currentView = this;
        } else {
            // Double link the views for forward/back functions
            currentView.nextView = this;
            this.lastView = currentView;

            // Update the current view pointer
            currentView = this;
        }
        // Display the new view
        currentView.show();

        // Call the onStart() method to initialize the view
        this.onStart();
    }

    public static View loadView(String layoutName) {
        return View.loadView(layoutName, null);
    }

    public static View loadView(String layoutName, Map<String, Object> ctx) {
        String layoutPath = String.format("../layouts/%s.fxml", layoutName);

        View v = null;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(View.class.getResource(layoutPath));
            Parent root = fxmlLoader.load();
            v = fxmlLoader.getController();
            v.scene = new Scene(root, v.getWidth(), v.getHeight());
            v.context = ctx;
        } catch (IOException e) {
            System.err.println("Failed to load layout fxml file: " + layoutPath);
            e.printStackTrace();
            System.exit(1);
        }

        return v;
    }

    public static void setStage(Stage stage) {
        View.viewStage = stage;
    }

    public int getWidth() {
        return DEFAULT_WIDTH;
    }
    public int getHeight() {
        return DEFAULT_HEIGHT;
    }

    private Scene getScene() {
        return this.scene;
    }
}
