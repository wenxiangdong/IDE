package ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

/**
 * Created by wenxi on 2017/6/7.
 */
public class LoginWin extends Stage {
    AnchorPane root;

    public LoginWin(){
        try {
            root= FXMLLoader.load(getClass().getResource("LoginWin.fxml"));
            Scene scene =new Scene(root,400,600);
            scene.getStylesheets().add(getClass().getResource("LoginWin.css").toExternalForm());

            this.setScene(scene);
            this.initStyle(StageStyle.TRANSPARENT);
            this.show();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
