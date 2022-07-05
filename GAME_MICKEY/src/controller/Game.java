package controller;

import javafx.animation.Animation;
import javafx.animation.ParallelTransition;
import javafx.animation.PathTransition;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;

import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Player;
import view.MainMenu;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

import static java.lang.Thread.sleep;

public class Game extends Stage {
    private int BrokenEggs = 4;
    private String MickeyPosition;
    Label MickeyMouseLabel;
    private int PlayerScores = 0;
    boolean endOfGame = false;
    private int seconds = 0, minutes = 0;
    private String ddSecond, ddMinute;
    TimerTask timerTask;
    private final DecimalFormat format = new DecimalFormat("00");
    StackPane stackPane;
    private GameThread gameThread;
    private Thread t;
    public Game() {
        init();
    }
    public void init() {
        this.setTitle("Main Menu");
        this.setWidth(918);
        this.setHeight(635);

        stackPane = new StackPane();
        ImageView img1 = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("../images/game.jpg"))));
        stackPane.getChildren().add(img1);

        Label label = new Label("00:00");
        label.setStyle("-fx-text-fill:BLACK; -fx-font-family: CourierNew;-fx-font-size: 36;");
        label.setTranslateX(-200);
        label.setTranslateY(-180);
        gameTimer(label);

        MickeyMouseLabel = new Label();
        MickeyMouseLabel.setTranslateY(30);
        MickeyMouseLabel.setGraphic(new ImageView(new Image("images/LT.png")));
        stackPane.getChildren().add(label);
        stackPane.getChildren().add(MickeyMouseLabel);


        //ctrl+shift+q exit
        this.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<>() {
            final KeyCombination keyCombination = new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN);
            public void handle(KeyEvent ke) {
                if (keyCombination.match(ke)) {
                    close();
                    ke.consume();
                }
            }
        });

        Scene scene = new Scene(stackPane);

        //click on keyboard
        scene.addEventFilter(KeyEvent.KEY_PRESSED, keyEvent -> {
            if (keyEvent.getCode() == KeyCode.A) {
                setMickey("images/LD.png", "LD");
                keyEvent.consume();
            } else if (keyEvent.getCode() == KeyCode.W) {
                setMickey("images/LT.png", "LT");
                keyEvent.consume();
            } else if (keyEvent.getCode() == KeyCode.S) {
                setMickey("images/RT.png", "RT");
                keyEvent.consume();
            } else if (keyEvent.getCode() == KeyCode.D) {
                setMickey("images/RD.png", "RD");
                keyEvent.consume();
            }
        });

        //or click on red circles
        EventHandler<MouseEvent> mouseHandler = mouseEvent -> {
            double pressedX = mouseEvent.getX();
            double pressedY = mouseEvent.getY();
            //left top
            if(pressedX > 81 && pressedX < 142 && pressedY < 394 && pressedY > 333){
                setMickey("images/LT.png", "LT");
            }
            //left down
            if(pressedX > 81 && pressedX < 142 && pressedY < 497 && pressedY > 436){
                setMickey("images/LD.png", "LD");
            }
            //right top
            if(pressedX > 756 && pressedX < 817 && pressedY < 394 && pressedY > 333){
                setMickey("images/RT.png", "RT");
            }
            //right down
            if(pressedX > 756 && pressedX < 817 && pressedY < 497 && pressedY > 436){
                setMickey("images/RD.png", "RD");
            }
        };
        stackPane.setOnMouseClicked(mouseHandler);

        gameThread = new GameThread();
        t = new Thread(gameThread);
        t.start();

        this.getIcons().add(new Image("images/logo.png"));
        this.setScene(scene);
        this.setResizable(false);
        this.show();
    }

    //timer
    private void gameTimer(Label label) {
        Timer timer = new java.util.Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    seconds++;
                    ddSecond = format.format(seconds);
                    ddMinute = format.format(minutes);
                    label.setText(ddMinute + ":" + ddSecond);
                    if (seconds == 60) {
                        seconds = 0;
                        minutes++;
                        ddSecond = format.format(seconds);
                        ddMinute = format.format(minutes);
                        label.setText(ddMinute + ":" + ddSecond);
                    }
                });
            }
        };
        timer.schedule(timerTask, 1000, 1000L);
    }

    public Label setMickey(String path, String poss){
        MickeyMouseLabel.setPrefHeight(206);
        MickeyMouseLabel.setPrefWidth(221);
        MickeyMouseLabel.setGraphic(new ImageView(new Image(path)));
        MickeyPosition=poss;
        return MickeyMouseLabel;
    }

    public void addEgg(String position){
        //stackPane = new StackPane();
        //stackPane.setPrefHeight(600);
        //stackPane.setPrefWidth(904);
        MoveTo moveTo;
        LineTo lineTo;
        switch (position) {
            case "LT" -> {
                // moveTo = new MoveTo(281,230);
                moveTo = new MoveTo(-180, -80);
                lineTo = new LineTo(-60, -10);
            }
            case "LD" -> {
                moveTo = new MoveTo(-200, 0);
                // moveTo = new MoveTo(281,330);
                //lineTo = new LineTo(350,380);
                lineTo = new LineTo(-100, 60);
            }
            case "RT" -> {
                moveTo = new MoveTo(180, -80);
                // moveTo = new MoveTo(630,230);
                // lineTo = new LineTo(560,280);
                lineTo = new LineTo(80, -10);
            }
            default -> {
                moveTo = new MoveTo(200, 0);
                //moveTo = new MoveTo(630,330);
                // lineTo = new LineTo(560,380);
                lineTo = new LineTo(100, 80);
            }
        }

        Rectangle rectangle = new Rectangle(30,30);
        rectangle.setFill(new ImagePattern(new Image("images/egg2.png")));
        stackPane.getChildren().add(rectangle);
        ParallelTransition parallelTransition = new ParallelTransition();
        PathTransition pathTransition = new PathTransition(Duration.seconds(2),rectangle);
        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(1),rectangle);
        rotateTransition.setFromAngle(0);
        if(position.equals("RD")||position.equals("RT")) rotateTransition.setToAngle(-360);
        else rotateTransition.setToAngle(360);
        rotateTransition.setCycleCount(Animation.INDEFINITE);
        Path path = new Path();
        path.getElements().add(moveTo);
        path.getElements().add(lineTo);
        pathTransition.setNode(rectangle);
        pathTransition.setPath(path);
        parallelTransition.getChildren().addAll(pathTransition, rotateTransition);
        parallelTransition.play();

        Thread checkEggMickey = new Thread(()->{
            try {
                sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Platform.runLater(()->{
                stackPane.getChildren().remove(rectangle);
                if(!position.equals(MickeyPosition)){
                    eggBroken();
                }else{
                    addScores();
                }
            });
        });
        checkEggMickey.start();
    }

    public void addScores(){
        PlayerScores++;
    }

    public void eggBroken(){
        BrokenEggs--;
        if(BrokenEggs == 0){
            timerTask.cancel();
            endOfGame = true;
            t.interrupt();
            System.out.println("Game OVER!");
        }
    }

    //im dluzej gra tym wiecej ma scores
    public int getPlayerScores() {
        int scores;
        if(Integer.parseInt(ddSecond) <= 30 && ddMinute.equals("00")){
            scores = PlayerScores * 100;
        }
        else if(Integer.parseInt(ddSecond) <= 60){
            scores = PlayerScores * 500;
        }
        else scores = PlayerScores * 1000;
        return scores;
    }

    public void saveResult(File path, Player player) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        try{
            FileWriter fileWriter = new FileWriter(path,true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            stringBuilder.append(player);
            bufferedWriter.write(stringBuilder.toString());
            bufferedWriter.flush();
            bufferedWriter.close();
        }
        catch (IOException e ){
            e.printStackTrace();
        }
    }

    private class GameThread implements Runnable{
        @Override
        public void run() {
            new Thread(()->{
                String[] allPositions = {"LT","LD","RT","RD"};
                String random;
                while(!endOfGame){
                    try {
                        sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(Math.random()>0.5){
                        random = allPositions[(int)(Math.random()*4)];
                        String eggPosition = random;
                        Platform.runLater(()-> addEgg(eggPosition));
                    }
                }
                Platform.runLater(()-> {
                    String name;
                    TextInputDialog dialog = new TextInputDialog();
                    dialog.setHeaderText("The end!");
                    dialog.setContentText("Enter your name:");
                    Optional<String> result = dialog.showAndWait();
                    if (result.isPresent()){
                        name=result.get();
                        if(name.equals("")) name="Player";
                        try {
                            Player player = new Player(name, getPlayerScores());
                            saveResult(new File("result.txt"), player);
                            System.out.println(name + "," + getPlayerScores());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    new MainMenu(Game.this);
                });
            }).start();
        }
    }
}

