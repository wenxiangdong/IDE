package ui;

import data.Temp;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import rmi.RemoteHelper;
import service.IOService;

import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by wenxi on 2017/6/7.
 */
public class MainWinController {
    public TextArea codeText;
    public TextArea inputText;
    public TextArea outputText;
    public Menu versionsMenu;
    public Label noticeLabel;

    @FXML
    void initialize() throws RemoteException {
       IOService ioService=RemoteHelper.getInstance().getIOService();
        String temp=ioService.readFileList(Temp.currentUser);
        String[] fileList=temp.split(";");
        if(fileList.length==0) return;

        for(String file:fileList){
            String[] patterns=file.split("code_");
            String name=patterns[patterns.length-1];
            MenuItem item=new MenuItem(name);
            item.setOnAction(e->{
                try {
                    System.out.println(file);
                    String data=ioService.readFile(file);
                    codeText.setText(data);
                    changeNotice("Open file:"+name+" successfully!");

                } catch (RemoteException e1) {
                    e1.printStackTrace();
                }
            });
            versionsMenu.getItems().add(item);
        }
    }

    public void onOpenMenu(ActionEvent actionEvent) {
//        try {
//            String code = RemoteHelper.getInstance().getIOService().readFile(Temp.currentUser, "code");
//            codeText.setText(code);
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
    }

    public void onSaveMenu(ActionEvent actionEvent) {
        String code=codeText.getText();
        if(code.equals("")){
            codeText.setPromptText("You've coded nothing!!!");
        }else{
            try {
                //用当前时间作为版本号
                DateFormat df=new SimpleDateFormat("yyyyMMddHHmmss");
                Date now=new Date();
                String version=df.format(now);

                RemoteHelper.getInstance().getIOService().writeFile(code, Temp.currentUser,"code_"+version);

                changeNotice("Save successfully!");
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

            changeNotice("Execute successfully!");
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
        changeNotice("All the codes has been cleared!");
    }

    public void onAboutMenu(ActionEvent actionEvent) {
    }

    public void changeNotice(String msg){
        String origin="IDE";
        noticeLabel.setText(msg);
        Timer timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(()->{
                    noticeLabel.setText(origin);
                });
            }
        },1500);
    }
}
