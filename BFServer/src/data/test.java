package data;

import java.util.HashSet;

/**
 * Created by wenxi on 2017/6/7.
 */
public class test {
    public static void main(String[] args) {

//        HashMap<String ,String> users;
//        String path="File/UserInfo";
//        File file = new File(path);
//        ObjectInputStream inputStream = null;
//        try {
//            inputStream = new ObjectInputStream(new FileInputStream(file));
//            users = (HashMap<String, String>) inputStream.readObject();
//
//            users.clear();
//            users.put("eric","1997");
//
//            ObjectOutputStream outputStream=new ObjectOutputStream(new FileOutputStream(file));
//            outputStream.writeObject(users);
//            outputStream.flush();
//            outputStream.close();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }

        HashSet<String> set = new HashSet<>();
        System.out.println(set.add("a"));
        System.out.println(set.add("a"));


    }

    public static void writeUserInfo() {

    }
}
