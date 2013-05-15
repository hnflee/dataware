package cn.icesoft;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

public class MinaTranFileServerHandler extends IoHandlerAdapter {

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
		
		
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		log.error("客户端发生异常...", cause);
	}

}
