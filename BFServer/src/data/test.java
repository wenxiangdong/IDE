package data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;

/**
 * Created by wenxi on 2017/6/7.
 */
public class test {
    public static void main(String[] args) {


        writeUserInfo();

        UserInfo userInfo=new UserInfo();
        userInfo.add("admin","123456");
        userInfo.print();
        userInfo.writeUserInfo();
        userInfo.readUserInfo();
        System.out.println("读取成功");
        userInfo.print();

    }
    public static  void writeUserInfo(){
        try {
            File file = new File("UserInfo");
            ObjectOutputStream outputStream=new ObjectOutputStream(new FileOutputStream(file));
            HashMap<String,String> users=new HashMap<>();
            outputStream.writeObject(users);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
