package qq;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class QQClientOutputThread extends Thread
{
	QQClient qqClient;
	
	public QQClientOutputThread( QQClient qqClient)
	{
		// TODO Auto-generated constructor stub
		this.qqClient=qqClient;
	}

	@Override
	public void run()
	{
		// TODO Auto-generated method stub
		try
		{
			OutputStream os = qqClient.socket.getOutputStream();
			
			while(true)
			{
				//输入的格式是什么？  System.in可以用 bufferreader，字符流流过滤流包装。这个字符流的构造函数需要字符流，InputStreamReader
				//这里是关键
				BufferedReader reader=new BufferedReader(new InputStreamReader(System.in));
				
				String line=reader.readLine();
				
				//这里可以使用父线程的，如果不行的话，就使用 继承父类的，就可以调用父类的属性了
				line=qqClient.name+"@"+qqClient.localip+":"+line;
				
				
				
				os.write(line.getBytes());
				
			}
			
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		
	}
	
	

}
