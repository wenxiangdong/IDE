package ui;

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
    }

    public void onSaveMenu(ActionEvent actionEvent) {
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
    }

    public void onSelectAllMenu(ActionEvent actionEvent) {
    }

    public void onClearMenu(ActionEvent actionEvent) {
    }

    public void onAboutMenu(ActionEvent actionEvent) {
    }
}
