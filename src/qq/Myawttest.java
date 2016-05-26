package qq;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Myawttest
{

	private Frame frame;
	public Button buttonsend;//,button3;
	private Button buttonlogin;
	public TextField textsend;
	private TextField textip;
	private TextField textport;
	private TextField textname;
	public TextArea textarea;//不然引用不到
	public TextArea textareausers;
	
	private Panel panel1;
	private Panel panel2;
	private Panel panel3;
	
	//这是逻辑的东西
	public 	QQClient qqClient; //不能静态，这个类又不是只生成一个对象
	public  Myawttest myawttest; //这里很好玩的一个东西，就是自己类中有个自己的引用
	
	public void go()
	{
		frame=new Frame("QQClient");
		frame.setLayout(new BorderLayout());//FlowLayout   替换BorderLayout
		
		panel1=new Panel();//发送
		panel2=new Panel();//登陆
		panel3=new Panel();//对话内容
		panel1.setLayout(new BorderLayout());
		panel2.setLayout(new FlowLayout());
		panel3.setLayout(new FlowLayout());
		
		
		


		buttonsend=new Button("send");
		textsend=new TextField(70);
		textsend.setText("say something");

		panel1.add(textsend,BorderLayout.WEST);
		panel1.add(buttonsend,BorderLayout.EAST);

		
		//textip=new TextField("10.3.242.98");
		textip=new TextField("127.0.0.1");	
		textport=new TextField("1234");
		textname=new TextField("yourname");
		buttonlogin=new Button("loginin");
		panel2.add(textip);
		panel2.add(textport);
		panel2.add(textname);
		panel2.add(buttonlogin);
		
		textarea=new TextArea(20,80);
		textareausers=new TextArea(20,13);
		panel3.add(textarea);
		panel3.add(textareausers);
		

		
	

	
		

		


		frame.add(panel1,BorderLayout.SOUTH);
		frame.add(panel2,BorderLayout.NORTH);
		frame.add(panel3,BorderLayout.CENTER);
		
		
		frame.setSize(900,600);
		//frame.pack();
		frame.setVisible(true);
	
		frame.addWindowListener(new WindowAdapter()
		{

			@Override
			public void windowClosing(WindowEvent e)
			{
				// TODO Auto-generated method stub
				//发送退出信息给服务器端
				try
				{
					OutputStream os=qqClient.socket.getOutputStream();
					String exit="#"+"exit"+"#"+qqClient.name+"#";
					os.write(exit.getBytes());//发送给服务器端退出信息
					
					try
					{
						Thread.sleep(1000);
					} catch (InterruptedException e1)
					{
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					qqClient.socket.close();
					
				} catch (IOException e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			
	
			System.exit(0);
			}
		
			
		}
		);
		
		buttonlogin.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// TODO Auto-generated method stub
				
				textarea.setText("正在登陆...");
				System.out.println("正在登陆");
				
				//在这里新建一个用户类的对象
				

				try
				{
					qqClient.socket=new Socket(textip.getText(),Integer.parseInt(textport.getText()));
					qqClient.name=textname.getText();
					
					qqClient.localip=qqClient.socket.getLocalAddress().toString();
					qqClient.localip=qqClient.localip.substring(1);
					
					
					
					
					//在这里要向服务器注册一下，告诉它我上线了  注册格式自己 #name#当前ip#
					
					String loginstr="#"+"login"+"#"+qqClient.name+"#";
					OutputStream login= qqClient.socket.getOutputStream();
					login.write(loginstr.getBytes());
					
					//理论上这里需要接收下服务器返回的信息，是同意，还是不同意 登录系统
					//判断#1# 
					InputStream is =qqClient.socket.getInputStream();					
					byte[] buffer=new byte[200];		
					int length=is.read(buffer);//这里也会阻塞，等待流中的数据写入		
					String word=new String(buffer,0,length);
					if (word.equals("#1#"))
					{
						//System.out.println("连接成功建立");
						textarea.append("\n"+"连接成功建立");
						
					}else{
						//System.out.println("连接没有成功建立");
						textarea.append(word);
						textarea.append("\n"+"连接没有成功建立,很可能是因为用户名重复了");
						
					}
					
					//获取用户列表。并展示..这个不着急放在inputThread里一起处理
					
					
					//注册完了后启动正常的输入输出子进程
					new QQClientInputThread(qqClient,myawttest).start();	//这里想要获取输入流，使用引用是可以的
					//new QQClientOutputThread(qqClient,myawttest).start();  //客户端awt控件直接使用
					
					
				} catch (NumberFormatException | IOException e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				System.out.println(qqClient.localip+":"+qqClient.socket.getLocalPort());
				
			}
			
		});
		
		
		buttonsend.addActionListener(new ActionListener()  //发送信息是不是需要多线程啊？貌似不用吧
		{	
			@Override
			public void actionPerformed(ActionEvent e)
			{
			
				String output=textsend.getText();
				
				System.out.println(output);
				//把这句改成网络发送就行了嘛
				OutputStream os;
				try
				{
					os = qqClient.socket.getOutputStream();
					String line=qqClient.name+"@"+qqClient.localip+":"+output;
					os.write(line.getBytes());
				} catch (IOException e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		
				
				textsend.setText("");
						
			}
				
		});
		textsend.addKeyListener(new KeyListener()
		{
			
			@Override
			public void keyTyped(KeyEvent e)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyReleased(KeyEvent e)
			{
				
			}
			
			@Override
			public void keyPressed(KeyEvent e)
			{
				// TODO Auto-generated method stub
				if (e.getKeyCode()==KeyEvent.VK_ENTER)
				{
					String output=textsend.getText();
					
					System.out.println(output);
					//把这句改成网络发送就行了嘛
					OutputStream os;
					try
					{
						os = qqClient.socket.getOutputStream();
						String line=qqClient.name+"@"+qqClient.localip+":"+output;
						os.write(line.getBytes());
					} catch (IOException e1)
					{
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
			
					
					textsend.setText("");
						
					
				}
				
			}
		});
		
	}//go
	
	public static void main(String[] args) throws NumberFormatException, UnknownHostException, IOException
	{
		Myawttest awt=new Myawttest();
		awt.myawttest=awt;//这一步是不是看起来很诡异。。。。是为了输入线程中可以方便获取awt对象
		awt.go();
		
		awt.qqClient=new QQClient();//QQClient的main方法已经没用了。只是用这个类当做结构体用
		//然后开始逻辑
		//应该再建立一个输入用户名的，先用本机IP代替
		

		
	}
	
}
