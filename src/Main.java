import javafx.application.Application;
import javafx.stage.Stage;


public class Main extends Application {

    Stage window;
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        DirectoryViewer dv = new DirectoryViewer();
        Stage stage = new Stage();
        dv.start(stage);

    }
}