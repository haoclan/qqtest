package qq;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Iterator;

import qq.QQServer.Cliententy;

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
				
				String word=new String(buffer,0,length);
				
				//在这里解析下 如果是某个用户发来的退出信息，则调用函数管理socket，删除当前用户实体，并且广播当前所有用户列表，
				String[] words=word.split("#");
				//System.out.println("#分割后有几段"+words.length);
				if (words.length==3)//尾巴貌似不算。。
				{
					 //如果是退出指令
				
					if (words[1].equals("exit"))
					{
//						System.out.println("进入退出指令");
						//找这个
						String exitname=words[2];
						//QQServer.al   //中有没有Cliententy .name
						for (Iterator iterator = QQServer.al.iterator(); iterator.hasNext();)
						{
							QQServer.Cliententy cliententy =  (Cliententy) iterator.next();
							if (cliententy.name.equals(exitname))
							{
	
								
								QQServer.al.remove(cliententy);//这样删除这个节点
								cliententy.socket.close();
							
								
								break;
							}
							
						}
						
//						
//						for (Iterator iterator = QQServer.al.iterator(); iterator
//								.hasNext();)
//						{
//							System.out.println(( (Cliententy) iterator.next()).name);
//							
//						}
			
						//并通知各个客户端修改当前list
						QQServer.notifyusers();
						
						
						//把当前线程也要关闭
						Thread.currentThread().interrupt();//中断此线程
						
					}//if退出指令
					
		
					
					
					
				}//这是指令
				else{
					
				//这里说明它是普通的指令
				System.out.println("转发广播用户信息:"+word);
				//这里转给输出流就好了嘛 
				qqServerOutputThread.broadcast(word);
				}
				
			}

			
			
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}//run
	
	

}
