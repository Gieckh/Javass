package ch.epfl.javass;

import java.util.List;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.application.Application;

public class LocalMain extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception {
        List<String> args2 = Application.getParameters().getRaw();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
