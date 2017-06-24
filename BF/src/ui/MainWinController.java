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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import rmi.RemoteHelper;
import service.IOService;

import java.awt.*;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by wenxi on 2017/6/7.
 */
public class MainWinController {
    public TextArea codeText;


    String code;
    public String getCode(){
        return this.code;
    }

    public TextArea inputText;
    public TextArea outputText;
    public Menu versionsMenu;
    public Label noticeLabel;
    public HBox noticeBox;
    public Label undo=new Label("undo");
    public MenuItem userNameMenu;


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
        System.out.println(temp);
        if(temp==null) return;
        System.out.println("temp:"+temp);
        String[] fileList=temp.split(";");
        if(fileList.length==0) return;

        HashMap<String,ArrayList<String>> map=new HashMap<>();
//        HashSet<String> filenames=new HashSet<>();
        //获取文件
        for(String file:fileList){
            String[] patterns=file.split("_");
            String[] pathPattern=patterns[0].split("\\\\");
            String name=pathPattern[pathPattern.length-1];
            String version=patterns[1];
            if (!map.containsKey(name)) {
                map.put(name, new ArrayList<>());
            }
            map.get(name).add(version);
        }

        for(String key:map.keySet()){
            System.out.println("key:"+key);
            Menu file=new Menu(key);
            for(String version:map.get(key)){
                System.out.println("version"+version);
                MenuItem item=new MenuItem(version);
                item.setOnAction(e->{
                    try {
                        String name=key+"_"+version;
                        String path="File/"+Temp.currentUser+"/"+name;
                        System.out.println(name);
                        String data=ioService.readFile(path);
                        codeText.setText(data);
                        inputText.clear();
                        outputText.clear();
                        Temp.openingFile=key;
                        changeNotice("Open: "+key+" successfully!",false);
                    } catch (RemoteException e1) {
                        e1.printStackTrace();
                    }
                });
                file.getItems().add(item);
            }
            versionsMenu.getItems().add(file);
        }
    }

    @FXML
    void initialize() throws RemoteException {

        getVersions();

        //codeText无法使用
        codeText.setEditable(false);
        codeText.setPromptText("Please new a file or open one");

        //undo按钮
        undo.setUnderline(true);
        undo.setTextFill(Color.BLUE);
        undo.setOnMouseClicked(e->{
            codeText.setText(Temp.lastCodes);
            codeText.positionCaret(Temp.lastCodes.length());//光标移到最后
        });
        noticeLabel.setText(Temp.currentMode.toString());

        //userNameMenu
        userNameMenu.setText("Hello "+Temp.currentUser);

        //codeText自动补全
        codeText.setOnKeyReleased(e->{
            switch (e.getCode()){
                case O:
                    String temp=codeText.getText();
                    codeText.setText(temp.substring(0,temp.length()-1)+"Ook");
                    codeText.positionCaret(codeText.getText().length());
                    break;
                case OPEN_BRACKET:
                    codeText.appendText("]");
                    codeText.positionCaret(codeText.getText().length()-1);
                    break;
            }
        });
//        codeText.textProperty().addListener((observable, oldValue, newValue) -> {
//            if(newValue.length()==0) return;
//            if(newValue.equals(oldValue)) return;
//            if(oldValue.length()<newValue.length()){
//
//                if(newValue.charAt(oldValue.length())=='['){
//                    codeText.appendText("]");
//                    //移动光标到[]中间
//                    try {
//                        Robot robot = new Robot();
//                        robot.keyPress(java.awt.event.KeyEvent.VK_LEFT);
//                        robot.keyRelease(java.awt.event.KeyEvent.VK_LEFT);
//                    } catch (AWTException e) {
//                        e.printStackTrace();
//                    }
//                }
//                if(newValue.charAt(newValue.length()-1)=='o'||newValue.charAt(newValue.length()-1)=='O'){
//                    String temp=codeText.getText();
//                    codeText.setText(temp.substring(0,temp.length()-1)+"Ook");
//                }
//            }
//        });
    }

    public void onSaveMenu(ActionEvent actionEvent) {
        String code=codeText.getText();
        if(code.equals("")){
            codeText.setPromptText("You've coded nothing!!!");
            changeNotice("Failed: You've coded nothing!!!",false);
        }else{
            System.out.println("opening:"+Temp.openingFile);
            if(Temp.openingFile==null) {
                new SaveFileWin(code).showAndWait();
            }else{
                //时间作为版本号
                DateFormat df=new SimpleDateFormat("yyyyMMddHHmmss");
                Date now=new Date();
                String version=df.format(now);

                try {
                    RemoteHelper.getInstance().getIOService().writeFile(code,Temp.currentUser,Temp.openingFile+"_"+version);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            //重新检查版本
            versionsMenu.getItems().clear();
            getVersions();

        }
    }

    public void onExecuteMenu(ActionEvent actionEvent) {
        String code=codeText.getText();
        String input=inputText.getText();
        System.out.println(input);
        String output="";
        if(code.equals("")){
            codeText.setPromptText("You've coded nothing!!!");
            changeNotice("Failed: You've coded nothing!!!",false);
        }else{
            switch (Temp.currentMode){
                case BF:
                    try {
                        output=RemoteHelper.getInstance().getBFService().execute(code,input);
                    } catch (RemoteException e) {
                       outputText.setText(e.toString());
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
        try {
            RemoteHelper.getInstance().getUserService().logout(Temp.currentUser);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
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
        String mode=menuItem.getText().split(" ")[0];
        switch (mode){
            case "BF":
                Temp.currentMode= Mode.BF;
                break;
            case "Ook":
                Temp.currentMode=Mode.Ook;
                break;
        }

        changeNotice("You are now in "+mode+" Mode",false);
//        if(Temp.currentMode== Mode.BF){
//            Temp.currentMode=Mode.Ook;
//            menuItem.setText("Swift to BF Mode");
//            changeNotice("You are now in Ook Mode",false);
//        }else{
//            Temp.currentMode= Mode.BF;
//            menuItem.setText("Swift to Ook Mode");
//            changeNotice("You are now in Bf Mode ",false);
//        }
    }

    public void onCodeTextMouseClicked(MouseEvent mouseEvent) {
        Temp.lastCodes=codeText.getText();
    }

    public void onUndoMenu(ActionEvent actionEvent) {
        Temp.nextCodes=codeText.getText();
        codeText.setText(Temp.lastCodes);
        codeText.positionCaret(Temp.lastCodes.length());//光标移到最后
    }

    public void onRedoMenu(ActionEvent actionEvent) {
        codeText.setText(Temp.nextCodes);
        codeText.selectRange(Temp.lastCodes.length(),Temp.nextCodes.length());
    }

    public void onLogoutMenu(ActionEvent actionEvent) {
        try {
            if(RemoteHelper.getInstance().getUserService().logout(Temp.currentUser)){
                codeText.getScene().getWindow().hide();
                new LoginWin();
            }

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void onNewMenu(ActionEvent actionEvent) {
        codeText.setEditable(true);
        codeText.clear();
        inputText.clear();
        outputText.clear();
        codeText.setPromptText("Coding here");
        changeNotice("New a file successfully!",false);
        Temp.openingFile=null;

    }
}
