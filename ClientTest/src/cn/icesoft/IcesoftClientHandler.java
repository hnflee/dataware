package cn.icesoft;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

public class IcesoftClientHandler extends IoHandlerAdapter {
	private static Logger log = Logger.getLogger(IcesoftClientHandler.class);
	
	private String messageReturn;
	
	public String getMessageReturn() {
		return messageReturn;
	}

	public void setMessageReturn(String messageReturn) {
		this.messageReturn = messageReturn;
	}

	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		messageReturn = message.toString();
		if(messageReturn!=null&&messageReturn.indexOf("\002EOF\002")!=-1)
		{
			session.write("quit");
		}
		else if (messageReturn!=null&&messageReturn.indexOf("1010000100")!=-1)
		{
			session.write("1020000000"+"0000"+"\001"+messageReturn.substring(10, messageReturn.length()));
			log.debug("sendMsg:"+"1020000000"+"0000"+"\001"+messageReturn.substring(10, messageReturn.length()));
			
		}
		else if (messageReturn!=null&&messageReturn.indexOf("1020000100")!=-1)
		{
			session.write("1020000000"+messageReturn.substring(10,messageReturn.length()));
		}
		else if (messageReturn!=null&&messageReturn.indexOf("1020000101")!=-1)
		{
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
