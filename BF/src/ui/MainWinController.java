package ui;

import data.Mode;
import data.Temp;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
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
    public HBox noticeBox;
    public Label undo=new Label("undo");



    /*
    检查版本
     */
    void getVersions(){
        IOService ioService=RemoteHelper.getInstance().getIOService();
        //检查版本号
        String temp= null;
        try {
            temp = ioService.readFileList(Temp.currentUser);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
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
                    inputText.clear();
                    outputText.clear();
                    changeNotice("Open file:"+name+" successfully!",false);

                } catch (RemoteException e1) {
                    e1.printStackTrace();
                }
            });
            versionsMenu.getItems().add(item);
        }
    }

    @FXML
    void initialize() throws RemoteException {

        getVersions();

        //undo按钮
        undo.setUnderline(true);
        undo.setTextFill(Color.BLUE);
        undo.setOnMouseClicked(e->{
            codeText.setText(Temp.lastCodes);
        });
        noticeLabel.setText(Temp.currentMode.toString());
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

                String ext="."+Temp.currentMode.toString().toLowerCase();

                RemoteHelper.getInstance().getIOService().writeFile(code, Temp.currentUser,"code_"+version+ext);

                changeNotice("Save successfully!",false);
                //重新检查版本
                versionsMenu.getItems().clear();
                getVersions();

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void onExecuteMenu(ActionEvent actionEvent) {
        String code=codeText.getText();
        String input=inputText.getText();
        System.out.println(input);
        String output="";
        if(code.equals("")){
            codeText.setPromptText("You've coded nothing!!!");
        }else{
            switch (Temp.currentMode){
                case BF:
                    try {
                        output=RemoteHelper.getInstance().getBFService().execute(code,input);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                case Ook:
                    try {
                        code = RemoteHelper.getInstance().getOokService().translate(code,input);
                        System.out.println("after change:"+code);
                        output =RemoteHelper.getInstance().getBFService().execute(code, input);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
            }


            outputText.setText(output);

            changeNotice("Execute successfully!",false);
        }
    }

    public void onExitMenu(ActionEvent actionEvent) {
        Platform.exit();
    }

    public void onSelectAllMenu(ActionEvent actionEvent) {
        codeText.selectAll();
    }

    public void onClearMenu(ActionEvent actionEvent) {
        Temp.lastCodes=codeText.getText();
        codeText.clear();
        changeNotice("All the codes has been cleared!",true);
    }

    public void onAboutMenu(ActionEvent actionEvent) {
//        codeText.getScene().getWindow().hide();
        new AboutWin();
    }

    public void changeNotice(String msg,boolean undoable){
        String origin=Temp.currentMode.toString();
        noticeLabel.setText(msg);
        if(undoable){
            noticeBox.getChildren().add(undo);
        }
        Timer timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(()->{
                    noticeLabel.setText(origin);
                    if(undoable){
                        noticeBox.getChildren().remove(undo);
                    }
                });
            }
        },3000);
    }

    public void onModeSwiftClicked(ActionEvent actionEvent) {
        MenuItem menuItem=(MenuItem)actionEvent.getSource();
        if(Temp.currentMode== Mode.BF){
            Temp.currentMode=Mode.Ook;
            menuItem.setText("Swift to BF Mode");
            changeNotice("You are now in Ook Mode",false);
        }else{
            Temp.currentMode= Mode.BF;
            menuItem.setText("Swift to Ook Mode");
            changeNotice("You are now in Bf Mode ",false);
        }
    }
}
