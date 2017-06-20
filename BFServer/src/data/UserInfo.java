package data;

import java.io.*;
import java.nio.file.Path;
import java.util.HashMap;

/**
 * Created by wenxi on 2017/6/7.
 * 存储用户名与密码
 * 可以 序列化
 */
public class UserInfo implements Serializable {

    private  HashMap<String,String> users;

    public UserInfo(){
        readUserInfo();
    }

    public void print(){
        for (String key: users.keySet()){
            System.out.println(key+":"+users.get(key));
        }
    }
    /*
    增加用户
     */
    public void add(String key,String value){
        users.put(key, value);
        writeUserInfo();//加完后及时写回
    }
    /*
    查找用户名是否存在
     */
    public boolean search(String name){
        return users.containsKey(name);
    }

    /*
    检查用户名与密码
     */
    public boolean checkPassword(String username,String password){
            return users.get(username).equals(password);

    }

    /*
    将一个对象序列化，写到文件中
     */
    public  void writeUserInfo(){
        try {
            String path="File/UserInfo";
            File file = new File(path);
            ObjectOutputStream outputStream=new ObjectOutputStream(new FileOutputStream(file));
            outputStream.writeObject(users);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    读取序列化文件
     */
    public  void   readUserInfo(){
        try {
//            System.out.println("读取序列化文件");
            String path="File/UserInfo";
            File file = new File(path);
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file));
            users = (HashMap<String, String>) inputStream.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }


}
