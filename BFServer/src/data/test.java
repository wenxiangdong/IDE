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


//        writeUserInfo();
//
//        UserInfo userInfo=new UserInfo();
//        userInfo.add("admin","123456");
//        userInfo.print();
//        userInfo.writeUserInfo();
//        userInfo.readUserInfo();
//        System.out.println("读取成功");
//        userInfo.print();



        String path="D:/软工/GitHub/IDE/File";
        File root=new File(path);
        File[] files=root.listFiles();
        System.out.println("输出文件列表");
        for(File s:files){
            System.out.println(s.toString());
        }

    }
    public static  void writeUserInfo(){
        try {
            String path="D:/软工/GitHub/IDE/File/UserInfo";
            File file = new File(path);
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
