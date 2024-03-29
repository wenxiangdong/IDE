package runner;

import javafx.application.Application;
import javafx.stage.Stage;
import rmi.RemoteHelper;
import ui.LoginWin;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class ClientRunner extends Application {
    private RemoteHelper remoteHelper;

    public ClientRunner() {
        linkToServer();
    }

    public static void main(String[] args) {
        launch();
//		new ClientRunner().test();
//		new ClientRunner().test();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        new LoginWin();
    }

    /*
    连接到服务器
     */
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

    /*
    测试方法，调试用
     */
    public void test() {
        try {
            System.out.println(RemoteHelper.getInstance().getBFService().execute(
                    "++++++++++[>+++++++>++++++++++>+++>+<<<<-] >++.>+.+++++++..+++.>++.<<+++++++++++++++. >.+++.------.--------.>+.>.",
                    ""));
//			RemoteHelper.getInstance().getIOService().writeFile("kdkdkd", "eric","code_1123444");
//			RemoteHelper.getInstance().getIOService().writeFile("ddd","eric","code_1234566");
            System.out.println(RemoteHelper.getInstance().getIOService().readFileList("eric"));
            System.out.println(RemoteHelper.getInstance().getBFService().execute(",>++++++[<-------->-],,[<+>-],<.>.", "4+3\n"));
            RemoteHelper.getInstance().getUserService().add("Test", "");


        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

}
