package ui;

import data.Temp;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import jdk.nashorn.internal.runtime.regexp.joni.Regex;
import rmi.RemoteHelper;
import service.IOService;

import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wenxi on 2017/6/24.
 */
public class SaveFileWinController {

    public TextField filenameText;
    public Label noticLabel;
    public VBox vBox;
    public VBox btnBox;
    public Button saveBtn;

    public void onSaveBtnClick(ActionEvent actionEvent) {
        String code=SaveFileWin.code;

        String filename=filenameText.getText();

        Pattern pattern=Pattern.compile("[\\\\/:*?\"<>|]");
        Matcher matcher=pattern.matcher(filename);
        if(matcher.find()){
            noticLabel.setText("Illegel Name!");
            filenameText.requestFocus();
            filenameText.selectAll();
        }else{
            //时间作为版本号
            DateFormat df=new SimpleDateFormat("yyyyMMddHHmmss");
            Date now=new Date();
            String version=df.format(now);

            IOService ioService = RemoteHelper.getInstance().getIOService();
            try {
                ioService.writeFile(code, Temp.currentUser,filename+"."+Temp.currentMode.toString().toLowerCase()+"_"+version);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            //保存成功反馈
            Temp.openingFile=filename+"."+Temp.currentMode.toString().toLowerCase();
            vBox.getChildren().remove(filenameText);
            noticLabel.setText("Done!");
            btnBox.getChildren().remove(saveBtn);

        }
    }

    public void onBackBtnClick(ActionEvent actionEvent) {
        noticLabel.getScene().getWindow().hide();
    }
}
