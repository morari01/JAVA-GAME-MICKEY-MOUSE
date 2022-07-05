package view;

import controller.Game;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class MainMenu {
    private HighScore highScore;
    private Game game;
    private Stage stage;
    public MainMenu(Stage stage) {
        init(stage);
    }

    public Stage getStage() {
        return stage;
    }

    public void init(Stage stage){
        this.stage = stage;
        stage.setTitle("Main Menu");
        stage.setHeight(635);
        stage.setWidth(918);
        stage.setOnCloseRequest(windowEvent -> System.exit(0));

        ImageView img1 = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("../images/mainmenu1.jpg"))));
        GridPane.setConstraints(img1, 0, 0);
        GridPane gp = new GridPane();
        gp.add(img1,0,0);

        EventHandler<MouseEvent> mouseHandler = mouseEvent -> {
            double pressedX = mouseEvent.getX();
            double pressedY = mouseEvent.getY();
            //game
            if(pressedX > 352 && pressedX < 552 && pressedY < 234 && pressedY>174){
                game = new Game();
            }
            //high scores
            if(pressedX > 352 && pressedX < 552 && pressedY < 330 && pressedY>270){
                try {
                    highScore = new HighScore();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            //exit
            if(pressedX > 352 && pressedX < 552 && pressedY < 421 && pressedY>361){
                System.exit(0);
            }
        };
        Scene scene = new Scene(gp);
        stage.getIcons().add(new Image("images/logo.png"));
        stage.setScene(scene);
        scene.setOnMouseClicked(mouseHandler);
        stage.setResizable(false);
        stage.show();
    }

 /*   public void swap(Stage s1, Stage s2){
        s1.close();
        s2.show();
    }*/
}
