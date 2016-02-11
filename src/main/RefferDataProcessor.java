package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class RefferDataProcessor {

	public static void processor()
	{
		BufferedReader br;  
		String data;
		String tmp;
		try {
			br = new BufferedReader(new FileReader("thp.txt"));  
			data = br.readLine();
			
		//一次读入一行，直到读入null为文件结束  
		while( data!=null){  
		      

			tmp = "\""+data+"\",";
			System.out.println(tmp);
		      data = br.readLine(); //接着读下一行  
		}  
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args)
	{
		processor();
	}
	
}
