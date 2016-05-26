package qq;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class QQClientInputThread extends Thread
{
	//private Socket socket;
	QQClient qqClient;
	Myawttest myawttest;//这次构造的时候把这个对象传过来。看看能在这里直接控制按钮的输入吗
	
	
	public QQClientInputThread(QQClient qqClient,Myawttest myawttest)
	{
		this.qqClient=qqClient;
		this.myawttest=myawttest;
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
				String newwords=new String(buffer,0,length); //newwords是接收到数据

				
				
				//这里判断下输入的内容  #upusers#:yourname2:yourname3:yourname5#
				String[] shellusers=newwords.split("#");  
				//myawttest.textarea.append("\n"+newwords);
				
				if (shellusers.length==3)
				{
					if (shellusers[1].equals("upusers"))
					{
						//提取信息，组装成String，然后让控件显示
						String users=shellusers[2].replace(":","\n");
						myawttest.textareausers.setText(users);
						
						
					}
					
				}
				else{
				myawttest.textarea.append("\n"+newwords);
				}
				

				
			}

			
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		
	}

	
}
