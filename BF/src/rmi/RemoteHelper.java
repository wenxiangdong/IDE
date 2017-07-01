package rmi;

import service.ExecuteService;
import service.IOService;
import service.OokService;
import service.UserService;

import java.rmi.Remote;

public class RemoteHelper {
    private static RemoteHelper remoteHelper = new RemoteHelper();
    private Remote remote;

    private RemoteHelper() {
    }

    public static RemoteHelper getInstance() {
        return remoteHelper;
    }

    public void setRemote(Remote remote) {
        this.remote = remote;
    }

    public IOService getIOService() {
        return (IOService) remote;
    }

    public UserService getUserService() {
        return (UserService) remote;
    }

    public ExecuteService getBFService() {
        return (ExecuteService) remote;
    }

    public OokService getOokService() {
        return (OokService) remote;
    }
}
