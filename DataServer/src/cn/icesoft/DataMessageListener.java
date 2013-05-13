package cn.icesoft;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;

public class DataMessageListener implements MessageListener {
	static Logger log = Logger.getLogger(DataMessageListener.class);//log4j的日志文件
	@Override
	public void onMessage(Message arg0) {
		// TODO Auto-generated method stub
	    try {

            if (arg0 instanceof TextMessage) {

            	 log.debug("coming in listener"+((TextMessage)arg0).getText().toString());

            } else if (arg0 instanceof MapMessage) {

                       MapMessage mmsg = (MapMessage)arg0;

                

            } else {

                       throw new IllegalStateException("Message Type Not Supported");

            }

                  } catch (JMSException e) {

                           e.printStackTrace();

                  }
	}

	

}
