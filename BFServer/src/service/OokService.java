package service;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by wenxi on 2017/6/13.
 */
public interface OokService extends Remote {
    /**
     * @param code bf源代码
     * @return 运行结果
     * @throws RemoteException
     */
    public String translate(String code, String param) throws RemoteException;
}
