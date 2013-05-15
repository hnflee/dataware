package cn.icesoft;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;

import cn.icesoft.process.RecieveDill;

public class DataMessageListener implements MessageListener {
	static Logger log = Logger.getLogger(DataMessageListener.class);//

	@Override
	public void onMessage(Message arg0) {
		// TODO Auto-generated method stub
		try {

			if (arg0 instanceof TextMessage) {

				log.debug("coming in listener"
						+ ((TextMessage) arg0).getText().toString());
				new RecieveDill(arg0).process();
				
				
			} else {

				throw new IllegalStateException("Message Type Not Supported");

			}

		} catch (JMSException e) {

			e.printStackTrace();

		}
	}

}
