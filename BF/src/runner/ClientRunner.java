package runner;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import javafx.application.Application;
import javafx.stage.Stage;
import rmi.RemoteHelper;
import ui.LoginWin;

public class ClientRunner extends Application{
	private RemoteHelper remoteHelper;
	
	public ClientRunner() {
		linkToServer();
//		initGUI();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		new LoginWin();
	}

	public static void main(String[] args) {
		launch();
//		new ClientRunner().test();
	}

	private void linkToServer() {
		try {
			remoteHelper = RemoteHelper.getInstance();
			remoteHelper.setRemote(Naming.lookup("rmi://127.0.0.1:8887/DataRemoteObject"));
			System.out.println("linked");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}

	}
	
	private void initGUI(){
	}
	
	public void test(){
		try {
//			System.out.println(remoteHelper.getUserService().login("admin", "123456a"));
			System.out.println(remoteHelper.getIOService().writeFile("2", "admin", "testFile"));
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

}
