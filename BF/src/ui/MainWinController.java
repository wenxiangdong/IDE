package ui;

import data.Temp;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.TextArea;
import rmi.RemoteHelper;

import java.rmi.RemoteException;

/**
 * Created by wenxi on 2017/6/7.
 */
public class MainWinController {
    public TextArea codeText;
    public TextArea inputText;
    public TextArea outputText;

    public void onOpenMenu(ActionEvent actionEvent) {
        try {
            String code = RemoteHelper.getInstance().getIOService().readFile(Temp.currentUser, "code");
            codeText.setText(code);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void onSaveMenu(ActionEvent actionEvent) {
        String code=codeText.getText();
        if(code.equals("")){
            codeText.setPromptText("You've coded nothing!!!");
        }else{
            try {
                RemoteHelper.getInstance().getIOService().writeFile(code, Temp.currentUser,"code");
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void onExecuteMenu(ActionEvent actionEvent) {
        String code=codeText.getText();
        String input=inputText.getText();
        String output="";
        if(code.equals("")){
            codeText.setPromptText("You've coded nothing!!!");
        }else{
            try {
                output=RemoteHelper.getInstance().getExecuteService().execute(code,input);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            outputText.setText(output);
        }
    }

    public void onExitMenu(ActionEvent actionEvent) {
        Platform.exit();
    }

    public void onSelectAllMenu(ActionEvent actionEvent) {
        codeText.selectAll();
    }

    public void onClearMenu(ActionEvent actionEvent) {
        codeText.clear();
    }

    public void onAboutMenu(ActionEvent actionEvent) {
    }
}
