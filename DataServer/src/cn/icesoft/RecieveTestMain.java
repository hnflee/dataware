package cn.icesoft;



import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;

public class RecieveTestMain {
	static Logger log = Logger.getLogger(RecieveTestMain.class);//log4j的日志文件
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 ApplicationContext context = new ClassPathXmlApplicationContext(
	                new String[] { "applicationContext.xml" });
	        
	}

}
