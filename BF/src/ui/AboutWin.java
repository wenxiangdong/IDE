package ui;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Created by wenxi on 2017/6/13.
 */
public class AboutWin extends Stage {
    public AboutWin() {
        AnchorPane root = new AnchorPane();
        root.setBackground(new Background(new BackgroundImage(
                new Image("ui/img/bg3.png"),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT
        )));
        root.setOnMouseClicked(e -> {   //点击窗口关闭
            this.hide();
        });

        Scene scene = new Scene(root, 600, 400);
        this.setScene(scene);
        this.initStyle(StageStyle.TRANSPARENT);
        this.show();
    }
}
