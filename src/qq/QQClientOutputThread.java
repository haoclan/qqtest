package qq;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class QQClientOutputThread extends Thread
{
	QQClient qqClient;
	Myawttest myawttest;//这次构造的时候把这个对象传过来。看看能在这里直接控制按钮的输入吗
	
	public QQClientOutputThread( QQClient qqClient,Myawttest myawttest)
	{
		
		this.qqClient=qqClient;
		this.myawttest=myawttest;
	}

	@Override
	public void run()
	{
		myawttest.buttonsend.addActionListener(new ActionListener()  //发送信息是不是需要多线程啊？貌似不用吧
		{
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				
				
				/*
				try
				{
					String output=myawttest.textsend.getText();
					OutputStream os = qqClient.socket.getOutputStream();
					BufferedReader reader=new BufferedReader(new InputStreamReader(System.in));				
					String line=reader.readLine();				
					//这里可以使用父线程的，如果不行的话，就使用 继承父类的，就可以调用父类的属性了
					line=qqClient.name+"@"+qqClient.localip+":"+line;
					os.write(line.getBytes());
					
					myawttest.textsend.setText("");
					
					
				} catch (IOException e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				*/

				
			}
		});
		
		// TODO Auto-generated method stub
		
		

		
	}
	
	

}
