package cn.icesoft;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.log4j.Logger;
import org.springframework.jms.core.MessageCreator;


public class DataMessageCreator implements  MessageCreator {
	static Logger log = Logger.getLogger(DataMessageCreator.class);//log4j的日志文件
	private String text_;
	DataMessageCreator(String content)
	{
		text_=content;
	}
	@Override
	public Message createMessage(Session arg0) throws JMSException {
		// TODO Auto-generated method stub
		log.debug("text_:"+text_);
        return  arg0.createTextMessage(text_);
	}

}
