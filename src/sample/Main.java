package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import jdk.net.SocketFlow;

import java.io.IOException;
import java.util.ArrayList;

public class Main extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;

    @Override
    public void stop() throws Exception {
        super.stop();
        System.exit(0);
    }

    public void run(ArrayList<String> args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Клиент мгновенных сообщений");

        initRootLayout();
        showAppView();

    }

    private void initRootLayout(){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("RootLayout.fxml"));
        try {
            rootLayout = (BorderPane) loader.load();
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public  void showAppView(){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("appView.fxml"));
        try {
            AnchorPane personOverview = (AnchorPane) loader.load();
            rootLayout.setCenter(personOverview);
            AppViewController controller = loader.getController();
            controller.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        launch(args);
    }
}
