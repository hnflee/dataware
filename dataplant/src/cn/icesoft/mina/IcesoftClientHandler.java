package cn.icesoft.mina;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import cn.icesoft.main.OptionPlants;
import cn.icesoft.main.RightComposite;

public class IcesoftClientHandler extends IoHandlerAdapter {
	private static Logger log = Logger.getLogger(IcesoftClientHandler.class);

	private RightComposite rightomp;
	private OptionPlants optionPlants_;
	

	public OptionPlants getOptionPlants_() {
		return optionPlants_;
	}

	public void setOptionPlants_(OptionPlants optionPlants_) {
		this.optionPlants_ = optionPlants_;
	}

	public RightComposite getRightomp() {
		return rightomp;
	}

	public void setRightomp(RightComposite rightomp) {
		this.rightomp = rightomp;
	}

	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		final String 	messageReturn = message.toString();
	
		if(messageReturn!=null&&messageReturn.substring(0,5).equals("10100"))
		{
			if(messageReturn.substring(5,10).equals("00000"))
			{
				optionPlants_.getDisplay().asyncExec(new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						optionPlants_.getTextConsole().setText("");
					}

				});	
				
				if(messageReturn.indexOf("\002EOF\00200000")!=-1)
				{
					session.write("1020000000"+messageReturn.substring(10, messageReturn.length()).split("\001")[1]+"\001"+"0000");
					log.debug("sendMsg:"+"1020000000"+"0000"+"\001"+messageReturn.substring(10, messageReturn.length()).split("\001")[1]);
				}
				else
				{
					log.warn("process error:"+messageReturn);
					session.write("quit");
				}
			}
		}
		else if (messageReturn!=null&&messageReturn.substring(0,5).equals("10200"))
		{
			if(messageReturn.substring(5,10).equals("00000"))
			{
				if(messageReturn.indexOf("\002EOF\002")!=-1)
				{
					if(messageReturn.indexOf("\002EOF\00200000")!=-1)
					{
						//开始下一个任务
						final String[] tmp=messageReturn.substring(10,messageReturn.length()).split("\001");
					
						optionPlants_.getDisplay().asyncExec(new Runnable(){

							@Override
							public void run() {
								// TODO Auto-generated method stub
								optionPlants_.getTextConsole().setText(optionPlants_.getTextConsole().getText()+tmp[2]+"\n<<<<<<<<<<	开始生成excel	<<<<<<<<");
								optionPlants_.getTextConsole().setSelection(optionPlants_.getTextConsole().getCharCount()); 
							}

						
						});	
						//下一个任务
						session.write("1030000000"+messageReturn.substring(10, messageReturn.length()).split("\001")[0]);
					}
					else
					{
						final String[] tmp=messageReturn.substring(10,messageReturn.length()).split("\001");
						optionPlants_.getDisplay().asyncExec(new Runnable(){

							@Override
							public void run() {
								// TODO Auto-generated method stub
								optionPlants_.getTextConsole().setText(optionPlants_.getTextConsole().getText()+tmp[2]+"\n!!!!!!失败退出!!!!!!");
								optionPlants_.getTextConsole().setSelection(optionPlants_.getTextConsole().getCharCount()); 
							}

						
						});	
						log.warn("process error:"+messageReturn);
						session.write("quit");
					}
				}
				else
				{
					final String[] tmp=messageReturn.substring(10,messageReturn.length()).split("\001");
					session.write("1020000000"+tmp[0]+"\001"+tmp[1]);
					if(tmp.length>=3)
					{
						optionPlants_.getDisplay().asyncExec(new Runnable(){
	
							@Override
							public void run() {
								// TODO Auto-generated method stub
								optionPlants_.getTextConsole().setText(optionPlants_.getTextConsole().getText()+tmp[2]);
								optionPlants_.getTextConsole().setSelection(optionPlants_.getTextConsole().getCharCount()); 
							}
	
						
						});		
					}	
					
						//	get(messageReturn.substring(10, messageReturn.length()));
					log.info("查询日志的输出信息:"+messageReturn);
				}
			}
			
		}
		else if (messageReturn!=null&&messageReturn.substring(0,5).equals("10300"))
		{
			if(messageReturn.substring(5,10).equals("00000"))
			{
				if(messageReturn.indexOf("\002EOF\002")!=-1)
				{
					if(messageReturn.indexOf("\002EOF\00200000")!=-1)
					{
						
						final String[] tmp=messageReturn.substring(10,messageReturn.length()).split("\001");
						
							rightomp.getDisplay().asyncExec(new Runnable(){
							@Override
							public void run() {
								// TODO Auto-generated method stub
								rightomp.getBrowser().setText("<a href=\"http://10.71.84.233:8080/"+tmp[0]+".xlsx\">excel数据文件下载</a> <br><a href=\"http://10.71.84.233:8080/"+tmp[0]+"\">csv数据文件下载</a>");
								
							}
							});
							optionPlants_.getDisplay().asyncExec(new Runnable(){

								@Override
								public void run() {
									// TODO Auto-generated method stub
									optionPlants_.getTextConsole().setText(optionPlants_.getTextConsole().getText()+"\n<<<<<<<<<<	excel生成成功	<<<<<<<<");
									optionPlants_.getTextConsole().setSelection(optionPlants_.getTextConsole().getCharCount()); 
								}

							
							});	
						
						
						log.info("excel create ok:"+messageReturn);
						session.write("quit");
					}else
					{
						log.warn("process error:"+messageReturn);
						session.write("quit");
					}
				}
				
			}
			else
			{
				log.warn("process error:"+messageReturn);
				session.write("quit");
			}
		}
		else
		{
			log.warn("process error:"+messageReturn);
			session.write("quit");
		}
		log.debug("客户端接收到的信息为：" + messageReturn);
		Thread.sleep(500);		
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		log.error("客户端发生异常...", cause);
	}
}
