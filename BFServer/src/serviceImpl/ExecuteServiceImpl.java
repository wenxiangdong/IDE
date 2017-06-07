//请不要修改本文件名
package serviceImpl;

import service.ExecuteService;

import java.rmi.RemoteException;
import java.util.Arrays;

public class ExecuteServiceImpl implements ExecuteService {

	String input;
	String code;
	char[] inputs;
	char[] codes;
	int inputPointer;
	int pointer=0;//指针

	char[] memory=new char[1000];//内存



	/**
	 * 请实现该方法
	 */
	@Override
	public String execute(String code, String param) throws RemoteException {
		// TODO Auto-generated method stub
		//初始化编译条件
		this.code = code;
		this.input = param;
		codes=this.code.toCharArray();
		inputs = this.input.toCharArray();

		Arrays.fill(memory,(char)0);

		pointer=0;
		inputPointer=0;

		//开始编译
		try{
			return compile(0);
		}
		catch (Exception e){
			return e.toString();
		}

	}

	/*
   @return 输出的数据
    */
	public String compile(int index){
		StringBuilder outputBuilder=new StringBuilder();
		System.out.println(outputBuilder.toString());
		for(int i=index;i<codes.length;i++){
			switch (codes[i]){
				case '>':
					this.pointer++;
					//System.out.println("pointer:"+this.pointer);
					break;
				case '<':
					this.pointer--;
					//System.out.println("pointer:"+this.pointer);
					break;
				case '+':
					memory[this.pointer]++;
					//System.out.println("memory["+this.pointer+"]："+(int)memory[this.pointer]);
					break;
				case '-':
					memory[this.pointer]--;
					//System.out.println("memory["+this.pointer+"]："+(int)memory[this.pointer]);
					break;
				case '.':
					outputBuilder.append(memory[this.pointer]);
					System.out.println(outputBuilder.toString());
					//System.out.print(memory[this.pointer]);break;
				case ',':
					if(inputs.length!=0){
						memory[this.pointer]=inputs[inputPointer];
						inputPointer++;
					}
					break;
				case '[':
					int tempPointer=this.pointer;
					//找出循环结束的位置
					int endLoop=i+1;
					int leftCount=1;//左括号的数量，可能出现内循环
					for(int j=i+1;j<codes.length;j++){
						if(codes[j]=='[') leftCount++;
						if(codes[j]==']'){
							leftCount--;
							if(leftCount==0){
								endLoop=j;
								break;
							}
						}
					}
					while (memory[tempPointer]!='\0'){
//						System.out.println("index:"+i);
						compile(i+1);//编译 从[后的代码
//						System.out.println((int)memory[tempPointer]);
					}
					//System.out.println("loop end");
					i=endLoop;//将代码的读取位置移到]的下一位
					break;
				case ']':
					//System.out.println("return");
					return "";
			}
		}
//		System.out.println(outputBuilder.toString());
		return outputBuilder.toString();
	}

}
