//请不要修改本文件名
package serviceImpl;

import service.ExecuteService;

import java.rmi.RemoteException;
import java.util.Arrays;

public class ExecuteServiceImpl implements ExecuteService {

    int inputPointer = 0;
    int pointer = 0;//指针

    char[] memory = new char[100];//内存


    /**
     * 请实现该方法
     */
    @Override
    public String execute(String code, String param) throws RemoteException {
        // TODO Auto-generated method stub
        //初始化编译条件
        inputPointer = 0;
        pointer = 0;
        Arrays.fill(memory, (char) 0);
        StringBuilder output = new StringBuilder();
        compile(output, code, param);
        return output.toString();


    }

    public void compile(StringBuilder output, String code, String param) {
        for (int i = 0; i < code.length(); i++) {
            switch (code.charAt(i)) {
                case '>':
                    pointer++;
                    break;
                case '<':
                    pointer--;
                    break;
                case '.':
                    //System.out.println("append:" + memory[pointer]);
                    output.append(memory[pointer]);
                    break;
                case ',':
                    memory[pointer] = param.charAt(inputPointer);
                    inputPointer++;
                    break;
                case '+':
                    memory[pointer]++;
                    break;
                case '-':
                    memory[pointer]--;
                    break;
                case '[':
                    int end;
                    int left = 1;
                    //找出循环体
                    for (end = i + 1; end < code.length(); end++) {
                        if (code.charAt(end) == '[') {
                            left++;
                        }
                        if (code.charAt(end) == ']') {
                            left--;
                        }
                        if (left == 0) {
                            break;
                        }
                    }
                    //System.out.println(code.substring(i + 1, end));
                    while (memory[pointer] != 0) {
                        System.out.println((int) memory[pointer]);
                        compile(output, code.substring(i + 1, end + 1), param);
                    }
                    i = end;//转到 ']'处
                    break;
                case ']':
                    return;
            }
        }
    }


}
