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
			
		//һ�ζ���һ�У�ֱ������nullΪ�ļ�����  
		while( data!=null){  
		      

			tmp = "\""+data+"\",";
			System.out.println(tmp);
		      data = br.readLine(); //���Ŷ���һ��  
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
