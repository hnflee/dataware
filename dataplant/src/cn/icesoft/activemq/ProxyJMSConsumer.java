package cn.icesoft.activemq;

import javax.jms.Destination;
import javax.jms.TextMessage;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;

import cn.icesoft.activemq.backend.HiveCmdProcess;



/**
 * JMS消费者
 * 消息题的内容定义
 * 消息对象 接收消息对象后： 接收到的消息体* <p> 
 */
public class ProxyJMSConsumer {

    public ProxyJMSConsumer() {

    }
    private JmsTemplate jmsTemplate;

    public JmsTemplate getJmsTemplate() {
        return jmsTemplate;
    }
    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }
    
    /**
     * 监听到消息目的有消息后自动调用onMessage(Message message)方法
     */
    public void recive() {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext-jms.xml");
        Destination destination = (Destination) applicationContext.getBean("destination_DataMessageCore");
        
        while (true) {
            try {
            	System.out.println("while in " );
                final TextMessage txtmsg = (TextMessage) jmsTemplate.receive(destination);
                
                
                if (null != txtmsg) {
                    System.out.println("[DataMessageCore] " + txtmsg);
                    System.out.println("[DataMessageCore] 收到消息内容为: "+ txtmsg.getText());
                } else
                    break;
                
                if(txtmsg.getJMSDestination()!=null)
                {
                	if(txtmsg.getStringProperty("MessageType").equals("HIVE_CMD"))
                	{
                		new HiveCmdProcess(jmsTemplate,txtmsg).run();
                	}
                	
                	
                	
                	
                	
                	
               	    System.out.println("[DataMessageCore] return ok " );
                }
                else
                {
                	System.out.println("[DataMessageCore] 作废掉 " );
                }
              
                
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
    
    

}