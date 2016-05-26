package qq;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class QQClient // 这个类现在只是当做数据结构来使用!!!!!!!!!!!!!!!!!!!!!!
{
	public  String name;
	public  String localip=null; //这个ip可以通过socket获得的
	public 	Socket socket;
	
	/*
	public static void main(String[] args) throws UnknownHostException, IOException
	{
		//应该再建立一个输入用户名的，先用本机IP代替
		
		
		//在这里新建一个用户类的对象。
		QQClient qqClient=new QQClient();
		 qqClient.socket=new Socket(args[0],Integer.parseInt(args[1]));//这里是找那个对方的ip
		
		qqClient.name=args[2];
		qqClient.localip=qqClient.socket.getLocalAddress().toString();
		qqClient.localip=qqClient.localip.substring(1);
		
		System.out.println(qqClient.localip+":"+qqClient.socket.getLocalPort());
		
		//在这里要向服务器注册一下，告诉它我上线了  注册格式自己 #name#当前ip#
		String loginstr="#"+"login"+"#"+qqClient.name+"#";
		OutputStream login= qqClient.socket.getOutputStream();
		login.write(loginstr.getBytes());
		
		//理论上这里需要接收下服务器返回的信息，是同意，还是不同意 登录系统
		
		//注册完了后启动正常的输入输出子进程
		//new QQClientInputThread(qqClient).start();		
		//new QQClientOutputThread(qqClient).start();
		
	}
	*/

}
