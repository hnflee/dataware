package cn.icesoft.activemq.backend;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import org.springframework.jms.core.JmsTemplate;


public class HiveCmdProcess extends BackendProcess {
	public HiveCmdProcess(JmsTemplate jmsTemplate, TextMessage message) {
		super(jmsTemplate, message);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			String hiveCMd=this.message_.getText();
			
			
			
			
			
			
			
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	

}
