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

public class QQServer
{
	//private static ArrayList<Socket> al; //把这个修改下。弄个内部类好管理 名字不要修改
	private static ArrayList<Cliententy> al; //把这个修改下。弄个内部类好管理 名字不要修改
	
	public static void main(String[] args) throws IOException, InterruptedException
	{
		ServerSocket ss=new ServerSocket(Integer.parseInt(args[0]));//这里只是绑定了端口
		
		al=new ArrayList<Cliententy>();
		//这个输出流，只建立一次啦
		QQServerOutputThread qqServerOutputThread=new QQServerOutputThread(al); //每次建立一个输出流就不对了 发送的时候再建立？还是使用UDP？
		qqServerOutputThread.start();
		
		
		while(true)  //这是一连接后的东西
		{
			
		Socket socket=ss.accept();//接到连接够返回
		
		//这里管理第一次收到它的name等信
		InputStream login= socket.getInputStream();
		
		byte[] buffer=new byte[200];		
		int length=login.read(buffer);//这里也会阻塞，等待流中的数据写入		
		String word=new String(buffer,0,length);
		//解析一下words 然后提取信息，然后加入静态属性ArrayList<Cliententy> al
		String[] words=word.split("#");
		Cliententy cliententy=new Cliententy(socket);
		cliententy.setname(words[1]);
		cliententy.setclientip(words[2]);
		
		
		al.add(cliententy);
		
		
		//System.out.println(socket.getInetAddress().toString());//这里输出本机Ip没有意义啊
		showAll();
		
		new QQServerInputThread(socket,qqServerOutputThread).start();  //这个倒是没什么的
		
		
		}
		
		
		
	}//Main
	
	static void showAll()
	{
		for (Iterator iterator = al.iterator(); iterator.hasNext();)
		{
			Cliententy cliententy = (Cliententy) iterator.next();
			System.out.println(cliententy.name+"@"+cliententy.clientip);
		}
		
	}
	
	static class Cliententy
	{
	//
	public	String name=null;
	public  String clientip=null;
	public	Socket socket=null;
		//其他信息
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
		void setclientip(String clientip)
		{
			this.clientip=clientip;
		}
		Socket getSocket()
		{
			return socket;
		}
		
		
	}

}
