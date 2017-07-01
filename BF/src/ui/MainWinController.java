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

import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

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
    public Label undo = new Label("undo");
    public MenuItem userNameMenu;
    String code;
    String undoType = "code";

    public String getCode() {
        return this.code;
    }

    /*
    检查版本
     */
    public void refreshVersions() {
        IOService ioService = RemoteHelper.getInstance().getIOService();
        //检查版本号
        String temp = null;
        try {
            temp = ioService.readFileList(Temp.currentUser);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
//        System.out.println(temp);
        if (temp == null) return;
//        System.out.println("temp:" + temp);
        String[] fileList = temp.split(";");//将得到的文件名 串 分割开
        if (fileList.length == 0) return;

        HashMap<String, ArrayList<String>> map = new HashMap<>();//key为文件名，value为版本号集合
        //获取文件
        for (String file : fileList) {
            String[] patterns = file.split("_");
            String[] pathPattern = patterns[0].split("\\\\");
            String name = pathPattern[pathPattern.length - 1];
            String version = patterns[1];
            if (!map.containsKey(name)) {
                map.put(name, new ArrayList<>());
            }
            map.get(name).add(version);
        }

        //构建菜单项目
        for (String key : map.keySet()) {
            Menu file = new Menu(key);
            for (String version : map.get(key)) {
//                System.out.println("version" + version);
                MenuItem item = new MenuItem(version);
                item.setOnAction(e -> {     //点击打开相关版本文件
                    try {
                        String name = key + "_" + version;
                        String path = "File/" + Temp.currentUser + "/" + name;
                        System.out.println(name);
                        String data = ioService.readFile(path);
                        codeText.setEditable(true);
                        codeText.setText(data);
                        inputText.clear();
                        outputText.clear();
                        Temp.openingFile = key;
                        changeNotice("Open: " + key + " successfully!", false);
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

        //刷新版本
        refreshVersions();

        //令codeText无法使用
        codeText.setEditable(false);
        codeText.setPromptText("Please new a file or open one");

        //undo按钮
        undo.setTextFill(Color.BLUE);
        undo.setOnMouseClicked(e -> {
            codeText.setText(Temp.lastCodes);
            codeText.positionCaret(Temp.lastCodes.length());//光标移到最后
            if (undoType != null && undoType.equals("file")) {
                Temp.openingFile = Temp.lastFile;//重置正在打开的文件
            }
        });
        noticeLabel.setText(Temp.currentMode.toString());

        //userNameMenu
        //显示当前用户
        userNameMenu.setText("Hello " + Temp.currentUser);

        //codeText自动补全
        codeText.setOnKeyReleased(e -> {
            int pos = codeText.caretPositionProperty().intValue();//保存当前光标位置
            switch (e.getCode()) {
                case O:
                    if (Temp.currentMode == Mode.Ook) {
                        StringBuilder temp = new StringBuilder(codeText.getText());
                        temp.replace(pos - 1, pos, "Ook");
                        codeText.setText(temp.toString());
                        codeText.positionCaret(pos + 2);
                    }
                    break;
                case OPEN_BRACKET:
                    if (Temp.currentMode == Mode.BF) {
                        StringBuilder builder = new StringBuilder(codeText.getText());
                        builder.insert(pos, ']');
                        codeText.setText(builder.toString());
                        codeText.positionCaret(pos);
                    }
                    break;

            }
        });
    }

    public void onSaveMenu(ActionEvent actionEvent) {
        String code = codeText.getText();
        if (code.equals("")) {
            codeText.setPromptText("You've coded nothing!!!");
            changeNotice("Failed: You've coded nothing!!!", false);
        } else {
//            System.out.println("opening:" + Temp.openingFile);
            if (Temp.openingFile == null) {     //文件是新建的
                new SaveFileWin(code).showAndWait();
            } else {    //文件是从版本打开的
                //时间作为版本号
                DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
                Date now = new Date();
                String version = df.format(now);

                try {
                    RemoteHelper.getInstance().getIOService().writeFile(code, Temp.currentUser, Temp.openingFile + "_" + version);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            //重新检查版本
            versionsMenu.getItems().clear();
            refreshVersions();

        }
    }

    public void onExecuteMenu(ActionEvent actionEvent) {
        String code = codeText.getText();
        String input = inputText.getText();
        System.out.println(input);
        String output = "";
        if (code.equals("")) {
            codeText.setPromptText("You've coded nothing!!!");
            changeNotice("Failed: You've coded nothing!!!", false);
        } else {
            switch (Temp.currentMode) {
                case BF:
                    try {
                        output = RemoteHelper.getInstance().getBFService().execute(code, input);
                    } catch (RemoteException e) {
                        outputText.setText(e.toString());
                    }
                    break;
                case Ook:
                    try {
                        code = RemoteHelper.getInstance().getOokService().translate(code, input);
                        System.out.println("after change:" + code);
                        output = RemoteHelper.getInstance().getBFService().execute(code, input);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
            }

            //execute 成功的反馈
            outputText.setText(output);
            changeNotice("Execute successfully!", false);
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
        Temp.lastCodes = codeText.getText();
        codeText.clear();
        undoType = "code";
        changeNotice("All the codes has been cleared!", true);
    }

    public void onAboutMenu(ActionEvent actionEvent) {
//        codeText.getScene().getWindow().hide();
        new AboutWin();
    }

    public void changeNotice(String msg, boolean undoable) {
        String origin = Temp.currentMode.toString();
        noticeLabel.setText(msg);
        if (undoable) {
            noticeBox.getChildren().add(undo);
        }
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {     //提示只显示一段时间,3s
                Platform.runLater(() -> {
                    noticeLabel.setText(origin);
                    if (undoable) {
                        noticeBox.getChildren().remove(undo);
                    }
                });
            }
        }, 3000);
    }

    public void onModeSwiftClicked(ActionEvent actionEvent) {
        MenuItem menuItem = (MenuItem) actionEvent.getSource();
        String mode = menuItem.getText().split(" ")[0];
        switch (mode) {
            case "BF":
                Temp.currentMode = Mode.BF;
                break;
            case "Ook":
                Temp.currentMode = Mode.Ook;
                break;
        }

        changeNotice("You are now in " + mode + " Mode", false);

    }

    /*
    每次点击codeText就将当前代码暂存
     */
    public void onCodeTextMouseClicked(MouseEvent mouseEvent) {
        Temp.lastCodes = codeText.getText();
    }

    public void onUndoMenu(ActionEvent actionEvent) {
        Temp.nextCodes = codeText.getText();
        codeText.setText(Temp.lastCodes);
        codeText.positionCaret(Temp.lastCodes.length());//光标移到最后
    }

    public void onRedoMenu(ActionEvent actionEvent) {
        codeText.setText(Temp.nextCodes);
        codeText.selectRange(Temp.lastCodes.length(), Temp.nextCodes.length());
    }

    public void onLogoutMenu(ActionEvent actionEvent) {
        try {
            if (RemoteHelper.getInstance().getUserService().logout(Temp.currentUser)) {
                codeText.getScene().getWindow().hide();
                new LoginWin();
            }

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void onNewMenu(ActionEvent actionEvent) {
        Temp.lastCodes = codeText.getText();
        codeText.setEditable(true);
        codeText.clear();
        inputText.clear();
        outputText.clear();
        codeText.setPromptText("Coding here");
        undoType = "file";
        changeNotice("New a file successfully!", true);
        Temp.lastFile = Temp.openingFile;
        Temp.openingFile = null;// 将正在打开的文件设为Null
        Temp.openingFile = null;// 将正在打开的文件设为Null
    }
}
