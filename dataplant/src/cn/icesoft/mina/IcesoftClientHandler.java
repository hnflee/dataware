package cn.icesoft.mina;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import cn.icesoft.main.OptionPlants;
import cn.icesoft.main.RightComposite;

public class IcesoftClientHandler extends IoHandlerAdapter {
	private static Logger log = Logger.getLogger(IcesoftClientHandler.class);
	private static String uuid="";
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
		if(messageReturn!=null&&messageReturn.indexOf("\002EOF\002")!=-1)
		{
			if(messageReturn.equals("\002EOF\00200000"))
			{
				rightomp.getDisplay().asyncExec(new Runnable(){
				@Override
				public void run() {
					// TODO Auto-generated method stub
					rightomp.getBrowser().setText(" <a href=\"http://10.21.11.238:8080/"+uuid+".xls\">excel数据文件下载</a>");
				}
				});
			}

			session.write("quit");
		}
		else if (messageReturn!=null&&messageReturn.indexOf("1010000100")!=-1)
		{
			session.write("1020000000"+"0000"+"\001"+messageReturn.substring(10, messageReturn.length()));
			uuid=messageReturn.substring(10, messageReturn.length());
			log.debug("sendMsg:"+"1020000000"+"0000"+"\001"+messageReturn.substring(10, messageReturn.length()));
			
		}
		else if (messageReturn!=null&&messageReturn.indexOf("1020000100")!=-1)
		{
			session.write("1020000000"+messageReturn.substring(10,messageReturn.length()));
		}
		else if (messageReturn!=null&&messageReturn.indexOf("1020000101")!=-1)
		{
			
			optionPlants_.getDisplay().asyncExec(new Runnable(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					optionPlants_.getTextConsole().setText(optionPlants_.getTextConsole().getText()+messageReturn.substring(10, messageReturn.length()));
					optionPlants_.getTextConsole().setSelection(optionPlants_.getTextConsole().getCharCount()); 
				}

			
			});		
					
				//	get(messageReturn.substring(10, messageReturn.length()));
			log.info("查询日志的输出信息:"+messageReturn.substring(10, messageReturn.length()));
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
