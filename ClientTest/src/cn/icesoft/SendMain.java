package cn.icesoft;

import javax.jms.Destination;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;


public class SendMain {

	static Logger log = Logger.getLogger(SendMain.class);//log4j的日志文件
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		 ApplicationContext context = new ClassPathXmlApplicationContext(
	                new String[] { "applicationContext.xml" });
		
		
		
		   
		JmsTemplate jmsTemplate = (JmsTemplate) context.getBean("jmsTemplate");
        Destination destination = (Destination) context.getBean("destination");
        for (int i = 1; i < 10; i++) {
        	MessageCreator mc = new DataMessageCreator("test "+i);//
            jmsTemplate.send(destination, mc);
            log.debug("send Message:"+i);
            try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
		
	}

}
