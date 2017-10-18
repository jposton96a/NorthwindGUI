package sample;

import views.LoginView;
import views.View;

import javafx.application.Application;
import javafx.stage.Stage;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        View.setStage(primaryStage);
        View.loadView("layout_login").start();
        //View.loadView("layout_login").start();

        System.out.println("Application started successfully.");
        /*View v = new LoginView();
        v.start(ctx);*/
    }


    public static void main(String[] args) {
        launch(args);
    }
}
