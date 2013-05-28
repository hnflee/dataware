package cn.icesoft.activemq.backend;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

public abstract class  BackendProcess {
	 JmsTemplate jmsTemplate_;
	 TextMessage message_;
	public BackendProcess( JmsTemplate jmsTemplate,	 TextMessage message)
	{
		jmsTemplate_=jmsTemplate;
		message_=message;
	}
	public abstract void run(); 
	protected void sendMessage(final String content) {

		try {
			jmsTemplate_.send(message_.getJMSReplyTo(), new MessageCreator() {
				public Message createMessage(Session session)
						throws JMSException {
					Message message = session
							.createTextMessage(content);
					message.setJMSCorrelationID(message.getJMSCorrelationID());
					return message;
				}
			});
		} catch (JmsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
