package ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;

/**
 * Created by wenxi on 2017/6/24.
 */
public class SaveFileWin extends Stage{

    AnchorPane root;
    public static String code;
    /*
    constructor
    @param 要保存的代码
     */
    public SaveFileWin(String codeToSave){
        code = codeToSave;
        try {
            root=FXMLLoader.load(getClass().getResource("SaveFileWin.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }


        Scene scene = new Scene(root,400,400);
        scene.getStylesheets().add(getClass().getResource("LoginWin.css").toExternalForm());

        this.setScene(scene);
        this.initStyle(StageStyle.TRANSPARENT);
    }
}
