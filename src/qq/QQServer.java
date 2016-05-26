package qq;

import java.awt.List;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.AlgorithmConstraints;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class QQServer //参数1 端口号
{
	
	public static ArrayList<Cliententy> al; //把这个修改下。弄个内部类好管理 名字不要修改
	public static QQServerOutputThread qqServerOutputThread;
	
	public static void main(String[] args) throws IOException, InterruptedException
	{
		ServerSocket ss=new ServerSocket(Integer.parseInt(args[0]));//这里只是绑定了端口
		
		al=new ArrayList<Cliententy>();
		//这个输出流，只建立一次啦
		qqServerOutputThread=new QQServerOutputThread(al); //每次建立一个输出流就不对了 发送的时候再建立？还是使用UDP？
		qqServerOutputThread.start();
		
		
		while(true)  //监听实体，一直在等候
		{
			
		Socket socket=ss.accept();//接到连接够返回
		
		//这里连接后  来个特殊的 输入流，单独接受它发送过来的东西。 但是用户退出的时候就不能从这里玩了。
		InputStream login= socket.getInputStream();
		
		byte[] buffer=new byte[200];		
		int length=login.read(buffer);//这里也会阻塞，等待流中的数据写入		
		String word=new String(buffer,0,length);
		//解析一下words 然后提取信息，然后加入静态属性ArrayList<Cliententy> al
		String[] words=word.split("#");
		
		//解析下登陆的信息
		System.out.println("将要登陆者为"+words[2]);
		
		int loginkey=0;
		if (haveit(words[2])==1) //因为把haveit修改了。。
		{ //socket返回 #1#
			OutputStream os=socket.getOutputStream();
			os.write("#1#".getBytes());
			System.out.println("当期用户可以登录");
			loginkey=1;
		}else //这里不可以加入
		{
			OutputStream os=socket.getOutputStream();
			os.write("#0#".getBytes());
			System.out.println("当期用户不可以登录");
			loginkey=0;
			continue;
		}
		
		
		if(loginkey==1)
		{
		//建立cliententy实体，方便服务器端管理
		Cliententy cliententy=new Cliententy(socket);
		cliententy.setname(words[2]);
		//服务器端加入用户信息
		al.add(cliententy);
		
		QQServerInputThread qqServerInputThread=new QQServerInputThread(socket,qqServerOutputThread);
		qqServerInputThread.start();  
		
		Thread.sleep(500);//休眠1秒钟。。麻烦大了。不会吧。这只是连接这个线程
		notifyusers();
		
				

		
		}//可以加入

		
		
		}
		
		
		
	}//Main
	
	
	public static void notifyusers()//通知所有用户当前的用户列表
	{
		// TODO Auto-generated method stub
		//服务器端发出用户列表更新通知 通知内容如下
				ArrayList<String> users=showAll();//返回的是A
				//使用String
				StringBuffer sb=new StringBuffer();
				for (Iterator iterator = users.iterator(); iterator.hasNext();)
				{
				
					sb.append((String)iterator.next()+":");
				}
				String notify="#upusers#"+sb.toString()+"#";
				//让其广播 #upusers#user1:user2:user3# 此条信息
				qqServerOutputThread.broadcast(notify);
	}

	
	private static int haveit(String string)
	{
		// TODO Auto-generated method stub
		int flag=1;
		for (Iterator iterator = al.iterator(); iterator.hasNext();)
		{
			Cliententy cliententy = (Cliententy) iterator.next();
			if (cliententy.name.equals(string))
			{
				flag=0;
				break;
			}
		}
		

		return flag;
		
	}

	static ArrayList<String> showAll()//这个函数调用 显示出当前所有的用户
	{
		ArrayList<String> users=new ArrayList<String>();
		System.out.println("当前所有用户如下");
		for (Iterator iterator = al.iterator(); iterator.hasNext();)
		{
			Cliententy cliententy = (Cliententy) iterator.next();
			System.out.println(cliententy.name);
		   users.add(cliententy.name);
		}
		return users;
	}
	
	static class Cliententy
	{
	//
	public	String name=null;
	//public  String clientip=null;//这是对方自己看到的ip
	public	Socket socket=null;

	
		public Cliententy()
		{
		}
		
		Cliententy(String name,Socket socket)
		{
			this.name=name;
			this.socket=socket;
			
		}
		Cliententy(Socket socket)
		{
			this(null,socket);
			//Cliententy();
		}
		void setname(String name)
		{
			this.name=name;
		}

		Socket getSocket()
		{
			return socket;
		}
		
		
	}

}
