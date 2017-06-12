package ui;

import data.Temp;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import rmi.RemoteHelper;

import java.io.IOException;
import java.rmi.RemoteException;

/**
 * Created by wenxi on 2017/6/7.
 */
public class LoginWinController {
    public TextField usernameText;
    public PasswordField passwordText;
    public AnchorPane root;

    public void onLoginBtnClick(ActionEvent actionEvent) {
        String username=usernameText.getText();
        String password=passwordText.getText();
        if((!username.equals(""))){//用户名不为空
            try {

                if(RemoteHelper.getInstance().getUserService().isExisting(username)){//用户名存在

                    if(RemoteHelper.getInstance().getUserService().login(username,password)){
                    //new a window
                        Temp.currentUser=username;
                        usernameText.getScene().getWindow().hide();
                        new MainWin();
                    }else{
                        passwordText.clear();
                        passwordText.setPromptText("密码有误");
//                        passwordText.requestFocus();
                    }
                }else{
                    usernameText.clear();
                    usernameText.setPromptText("该用户不存在");
                    passwordText.clear();
                    passwordText.setPromptText("");
                }


            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }else{
            passwordText.clear();
            usernameText.clear();
            usernameText.setPromptText("用户名不能为空");
        }
    }

    public void onSignUpLabelClicked(MouseEvent mouseEvent) {
        try {
            AnchorPane root2=FXMLLoader.load(getClass().getResource("SignupWin.fxml"));
            Scene scene=root.getScene();
            scene.setRoot(root2);
            scene.getStylesheets().add(getClass().getResource("SignupWin.css").toExternalForm());
            System.out.println("change");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
