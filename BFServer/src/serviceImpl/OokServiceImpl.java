package serviceImpl;

import service.OokService;

import java.rmi.RemoteException;

/**
 * Created by wenxi on 2017/6/13.
 */
public class OokServiceImpl implements OokService {
    @Override
    public String translate(String code, String param) throws RemoteException {
        StringBuilder tranlation=new StringBuilder();
        for(int i=0;i<code.length();i+=10){
//            System.out.println(code.substring(i,i+8));
            String pattern=code.substring(i,i+10);
            System.out.println(pattern);
            switch (pattern){
                case "Ook. Ook? ":
                    tranlation.append(">");
                    break;
                case "Ook? Ook. ":
                    tranlation.append("<");
                    break;
                case "Ook. Ook. ":
                    tranlation.append("+");
                    break;
                case "Ook! Ook! ":
                    tranlation.append("-");
                    break;
                case "Ook! Ook. ":
                    tranlation.append(".");
                    break;
                case "Ook. Ook! ":
                    tranlation.append(",");
                    break;
                case "Ook! Ook? ":
                    tranlation.append("[");
                    break;
                case "Ook? Ook! ":
                    tranlation.append("]");
                    break;
            }
        }
        return tranlation.toString();
    }

}
