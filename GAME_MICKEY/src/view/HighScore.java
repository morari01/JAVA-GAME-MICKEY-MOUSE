package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.Player;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

public class HighScore extends Stage {
    private final Color bg =  Color.rgb(140,0,0);
    public ListView<String> list;
    HighScore() throws IOException {
        init();
    }

    public void init() throws IOException {
        ArrayList<Player> players = new ArrayList<>();
        this.setWidth(918);
        this.setHeight(635);
        this.setTitle("High scores");
        Button button = new Button("Menu");

        this.list = new ListView<>();
        Collections.sort(players);
        //String[] strings = players.toArray(String[]::new);
        ObservableList<String> items = FXCollections.observableArrayList(players.toArray(String[]::new));
        list.setItems(items);
        VBox hBox = new VBox(list);
        hBox.getChildren().addAll(button);

        button.setOnAction(e-> buttonClicked());
        String cssLayout = "-fx-border-color: E0DDC2;\n" +
                "-fx-border-insets: 5;\n" +
                "-fx-border-width: 3;\n" +
                "-fx-border-style: dashed;\n";
        hBox.setStyle(cssLayout);
        hBox.setPadding(new Insets(10,10,10,10));
        hBox.setSpacing(10);
        hBox.setBackground(new Background(new BackgroundFill(bg, CornerRadii.EMPTY, Insets.EMPTY)));

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


        Scene scene = new Scene(hBox, 904, 600);
        addPlayer("result.txt", players);
        this.getIcons().add(new Image("images/logo.png"));
        this.setScene(scene);
        this.setResizable(false);
        this.show();
    }

    public void addPlayer(String path, ArrayList<Player> playerArrayList) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
        String st = bufferedReader.readLine();
        while (st != null){
            String name = st.split(" ")[0];
            int result = Integer.parseInt(st.split(" ")[1]);
            playerArrayList.add(new Player(name, result));
            st = bufferedReader.readLine();
            list.getItems().add(name + "," + result);
        }
        bufferedReader.close();
    }

    public void buttonClicked(){
        this.close();
    }
}
