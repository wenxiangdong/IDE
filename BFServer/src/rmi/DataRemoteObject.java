package rmi;

import service.ExecuteService;
import service.IOService;
import service.OokService;
import service.UserService;
import serviceImpl.ExecuteServiceImpl;
import serviceImpl.IOServiceImpl;
import serviceImpl.OokServiceImpl;
import serviceImpl.UserServiceImpl;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class DataRemoteObject extends UnicastRemoteObject implements IOService, UserService, ExecuteService, OokService {
    /**
     *
     */
    private static final long sr = 4029039744279087114L;
    private IOService iOService;
    private UserService userService;
    private ExecuteService BF;
    private OokService Ook;

    protected DataRemoteObject() throws RemoteException {
        iOService = new IOServiceImpl();
        userService = new UserServiceImpl();
        BF = new ExecuteServiceImpl();
        Ook = new OokServiceImpl();
    }

    @Override
    public boolean writeFile(String file, String userId, String fileName) throws RemoteException {
        // TODO Auto-generated method stub
        return iOService.writeFile(file, userId, fileName);
    }

    @Override
    public String readFile(String fileName) throws RemoteException {
        // TODO Auto-generated method stub
        return iOService.readFile(fileName);
    }

    @Override
    public String readFileList(String userId) throws RemoteException {
        // TODO Auto-generated method stub
        return iOService.readFileList(userId);
    }

    @Override
    public boolean login(String username, String password) throws RemoteException {
        // TODO Auto-generated method stub
        return userService.login(username, password);
    }

    @Override
    public boolean logout(String username) throws RemoteException {
        // TODO Auto-generated method stub
        return userService.logout(username);
    }

    @Override
    public void add(String username, String password) throws RemoteException {
        userService.add(username, password);
    }

    @Override
    public boolean isExisting(String username) throws RemoteException {
        return userService.isExisting(username);
    }

    @Override
    public String execute(String code, String param) throws RemoteException {
        return BF.execute(code, param);
    }

    @Override
    public String translate(String code, String param) throws RemoteException {
        return Ook.translate(code, param);
    }
}
