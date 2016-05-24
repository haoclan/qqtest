package qq;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class QQServerInputThread extends Thread
{
	private Socket socket;
	private QQServerOutputThread qqServerOutputThread;
	
	public QQServerInputThread(Socket socket,QQServerOutputThread qqServerOutputThread)
	{
		this.socket=socket;
		this.qqServerOutputThread=qqServerOutputThread;
	}

	@Override
	public void run()
	{
		// TODO Auto-generated method stub
		try
		{
			InputStream is=socket.getInputStream();
			
			while (true)
			{
				byte[] buffer=new byte[200];
				
				int length=is.read(buffer);//这里也会阻塞，等待流中的数据写入
				
				String words=new String(buffer,0,length);
				System.out.println(words);
				//这里转给输出流就好了嘛 
				qqServerOutputThread.broadcast(words);
				
			}

			
			
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}//run
	
	

}
