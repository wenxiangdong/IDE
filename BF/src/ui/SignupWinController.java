package ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import rmi.RemoteHelper;
import service.UserService;

import java.io.IOException;
import java.rmi.RemoteException;

/**
 * Created by wenxi on 2017/6/10.
 */
public class SignupWinController {
    public TextField usernameText;
    public PasswordField passwordText;
    public AnchorPane root;

    public void onSignBtnClick(ActionEvent actionEvent) throws RemoteException {
        String username = usernameText.getText();
        String password = passwordText.getText();
        UserService userService = RemoteHelper.getInstance().getUserService();
        boolean isExisting = userService.isExisting(username);

        if (isExisting) {
            usernameText.clear();
            usernameText.setPromptText("该用户已经存在！");
        } else if (password.equals("")) {
            passwordText.setPromptText("密码不得为空");
        } else {
            userService.add(username, password);
            changeRoot();
        }


    }

    public void onBackBtnClick(ActionEvent actionEvent) {
        changeRoot();
    }

    /*
    回到登陆界面
     */
    private void changeRoot() {
        try {
            AnchorPane root2 = FXMLLoader.load(getClass().getResource("LoginWin.fxml"));
            Scene scene = root.getScene();
            scene.setRoot(root2);
            scene.getStylesheets().clear();
            scene.getStylesheets().add(getClass().getResource("LoginWin.css").toExternalForm());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
