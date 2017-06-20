package serviceImpl;

import java.io.File;
import java.rmi.RemoteException;

import data.UserInfo;
import service.UserService;

public class UserServiceImpl implements UserService{

	private UserInfo userInfo=new UserInfo();
	public UserServiceImpl(){
		userInfo.readUserInfo();
		userInfo.print();
		System.out.println("成功读取userInfo");
	}

	@Override
	public boolean login(String username, String password) throws RemoteException {
		return userInfo.checkPassword(username, password);
	}

	@Override
	public boolean logout(String username) throws RemoteException {
		return true;
	}

	@Override
	public void add(String username, String password) throws RemoteException {
		userInfo.add(username, password);
		userInfo.print();
		madeDirectory("File/"+username);

	}

	@Override
	public boolean isExisting(String username) throws RemoteException {
		userInfo.print();
		return userInfo.search(username);
	}

	boolean madeDirectory(String path){
		File dir=new File(path);
		dir.mkdir();
		return false;
	}

}
