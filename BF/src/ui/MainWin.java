package ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

/**
 * Created by wenxi on 2017/6/7.
 */
public class MainWin extends Stage{
    AnchorPane root;

    public MainWin(){
        try {
            root = FXMLLoader.load(getClass().getResource("MainWin.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scene scene=new Scene(root,800,600);

        this.setScene(scene);
        this.initStyle(StageStyle.TRANSPARENT);
        this.show();
    }
}
