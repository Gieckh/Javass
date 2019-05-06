package ch.epfl.javass;

import javafx.application.Application;
import javafx.stage.Stage;

import java.util.List;

public class LocalMain extends Application {
    /** ============================================== **/
    /** ==============    ATTRIBUTES    ============== **/
    /** ============================================== **/

    /** ============================================== **/
    /** ==============   CONSTRUCTORS   ============== **/
    /** ============================================== **/

    /** ============================================== **/
    /** ===============    METHODS    ================ **/
    /** ============================================== **/

    @Override
    public void start(Stage primaryStage) throws Exception {
        List<String> args = getParameters().getRaw();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
