package qq;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class QQClientInputThread extends Thread
{
	//private Socket socket;
	QQClient qqClient;
	
	public QQClientInputThread(QQClient qqClient)
	{
		this.qqClient=qqClient;
	}

	@Override
	public void run()
	{
	
		try
		{
			InputStream is = qqClient.socket.getInputStream();
			
			while (true)
			{
				byte[] buffer=new byte[200];
				
				int length=is.read(buffer);//这里也会阻塞，等待流中的数据写入
				
				System.out.println(new String(buffer,0,length));
				
			}

			
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		
	}

	
}
