import javafx.application.Application;
import javafx.stage.Stage;
import view.MainMenu;


public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        new MainMenu(stage);
    }
}