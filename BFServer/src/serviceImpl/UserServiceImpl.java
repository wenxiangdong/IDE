package serviceImpl;

import data.UserInfo;
import service.UserService;

import java.io.File;
import java.rmi.RemoteException;

public class UserServiceImpl implements UserService {

    private UserInfo userInfo = new UserInfo();

    public UserServiceImpl() {
        userInfo.readUserInfo();
        userInfo.print();
        System.out.println("成功读取userInfo");
    }

    @Override
    public boolean login(String username, String password) throws RemoteException {
        if (userInfo.checkPassword(username, password)) {
            if (userInfo.addOnLineUser(username)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean logout(String username) throws RemoteException {
        return userInfo.removerOnLineUser(username);
    }

    @Override
    public void add(String username, String password) throws RemoteException {
        userInfo.add(username, password);
        userInfo.print();
        madeDirectory("File/" + username);//建立用户个人文件夹

    }

    @Override
    public boolean isExisting(String username) throws RemoteException {
//        userInfo.print();
        return userInfo.search(username);
    }


    /*
    创立文件夹
    path 文件夹路径
     */
    void madeDirectory(String path) {
        File dir = new File(path);
        dir.mkdir();
    }

}
