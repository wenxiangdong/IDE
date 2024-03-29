package serviceImpl;

import service.IOService;

import java.io.*;

public class IOServiceImpl implements IOService {

    @Override
    public boolean writeFile(String file, String userId, String fileName) {
        String path = "File/" + userId + "/";//定位到 用户文件夹
        File f = new File(path + fileName);
        try {
            FileWriter fw = new FileWriter(f, false);
            fw.write(file);
            fw.flush();
            fw.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public String readFile(String fileName) {
        // TODO Auto-generated method stub
        StringBuilder data = new StringBuilder();
        String line;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            while ((line = reader.readLine()) != null) {
                data.append(line);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data.toString();
    }

    @Override
    public String readFileList(String userId) {
        // TODO Auto-generated method stu
        StringBuilder list = new StringBuilder();
        String path = "File/" + userId;
        File root = new File(path);
        File[] files = root.listFiles();
        for (File file : files) {
            list.append(file.toPath());
            list.append(";");
        }
        if (list.toString().equals("")) {
            return null;
        } else {
            return list.substring(0, list.length() - 1);
        }

    }

}
