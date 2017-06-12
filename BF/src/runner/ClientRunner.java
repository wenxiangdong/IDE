package runner;

import java.io.File;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.application.Application;
import javafx.stage.Stage;
import rmi.RemoteHelper;
import sun.net.www.content.text.Generic;
import ui.LoginWin;

public class ClientRunner extends Application{
	private RemoteHelper remoteHelper;
	
	public ClientRunner() {
		linkToServer();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		new LoginWin();
	}

	public static void main(String[] args) {
		launch();
//		new ClientRunner().test();
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

	
	public  void test(){
		try {
			System.out.println(RemoteHelper.getInstance().getExecuteService().execute(
                    "++++++++++[>+++++++>++++++++++>+++>+<<<<-] >++.>+.+++++++..+++.>++.<<+++++++++++++++. >.+++.------.--------.>+.>.",
                    ""));
			RemoteHelper.getInstance().getIOService().writeFile("kdkdkd", "eric","code_1123444");
			RemoteHelper.getInstance().getIOService().writeFile("ddd","eric","code_1234566");
			System.out.println(RemoteHelper.getInstance().getIOService().readFileList("eric"));


		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

}
