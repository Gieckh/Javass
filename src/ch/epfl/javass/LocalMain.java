package ch.epfl.javass;

import java.util.List;

import javafx.application.Application;
import javafx.stage.Stage;
import static javafx.application.Application.Parameters;

public class LocalMain extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception {
        List<String> args = getParameters().getRaw();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
