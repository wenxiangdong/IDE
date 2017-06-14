package rmi;

import java.rmi.Remote;

import service.BFService;
import service.IOService;
import service.OokService;
import service.UserService;

public class RemoteHelper {
	private Remote remote;
	private static RemoteHelper remoteHelper = new RemoteHelper();
	public static RemoteHelper getInstance(){
		return remoteHelper;
	}
	
	private RemoteHelper() {
	}
	
	public void setRemote(Remote remote){
		this.remote = remote;
	}
	
	public IOService getIOService(){
		return (IOService)remote;
	}
	
	public UserService getUserService(){
		return (UserService)remote;
	}

	public BFService getBFService(){
		return (BFService)remote;
	}
	public OokService getOokService(){
		return (OokService)remote;
	}
}
