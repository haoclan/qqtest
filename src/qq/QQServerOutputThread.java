package qq;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

import qq.QQServer.Cliententy;

public class QQServerOutputThread extends Thread
{

	private  ArrayList<Cliententy> al;

	public QQServerOutputThread(ArrayList<Cliententy> al)
	{
		this.al=al;
	}

	@Override
	public void run()
	{
	//这是输出流
	//应该是先获取输出，然后遍历转发	
		while(true)
		{
		BufferedReader reader=new BufferedReader(new InputStreamReader(System.in));		
		
		String line;
		
		try
		{
			line = reader.readLine();
			for (Iterator iterator = al.iterator(); iterator.hasNext();)
			{
				Socket socket = ((Cliententy)iterator.next()).getSocket();
				
				OutputStream os=socket.getOutputStream();
				
				os.write(line.getBytes());
				
			}
			
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
					
		
				//输入的格式是什么？  System.in可以用 bufferreader，字符流流过滤流包装。这个字符流的构造函数需要字符流，InputStreamReader
				//这里是关键

				
		}//while
			
					
	}//run方法
	
	//为了代码重用广播方法传入string
	public void broadcast(String words)
	{
		try
		{
		
			for (Iterator iterator = al.iterator(); iterator.hasNext();)
			{
				Socket socket = ((Cliententy)iterator.next()).getSocket();
				
				OutputStream os=socket.getOutputStream();
				
				os.write(words.getBytes());
				
			}
			
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
	}
	
	
	
}
