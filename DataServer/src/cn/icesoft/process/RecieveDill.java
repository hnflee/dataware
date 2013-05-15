package cn.icesoft.process;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.UUID;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import cn.icesoft.native_c.runHql;

public class RecieveDill {
	static Logger log = Logger.getLogger(RecieveDill.class);// 
	private TextMessage message_;
	private String activeMqUrl = "tcp://10.71.84.233:61616";
	static long postion = 0;
	static int status = 1;
	static String errorCode="";
	public String getActiveMqUrl() {
		return activeMqUrl;
	}

	public void setActiveMqUrl(String activeMqUrl) {
		this.activeMqUrl = activeMqUrl;
	}

	public RecieveDill(Message message) {
		message_ = (TextMessage) message;
	}

	public void process() {

		try {
			String messageText = message_.getText();
			if (message_.getStringProperty("tmpQ") != null) {
				String tmpqName = message_.getStringProperty("tmpQ").toString();
				ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
						activeMqUrl);
				Connection connection = connectionFactory.createConnection();
				connection.start();
				Session session = connection.createSession(Boolean.TRUE,
						Session.AUTO_ACKNOWLEDGE);
				Destination destination = session.createQueue(tmpqName);
				MessageProducer producer = session.createProducer(destination);

				// hdfs excute hql with hive
				String hql = message_.getStringProperty("HQL");
				runlib(hql, producer, session);

				if(status==0)
				{
					TextMessage message2 = session.createTextMessage("EOF");
					message2.setStringProperty("EOF", "0");
					producer.send(message2);
				}
				else
				{
					TextMessage message2 = session.createTextMessage("EORROR:"+errorCode);
					message2.setStringProperty("EOF", "1");
					producer.send(message2);
				}
				
				session.commit();
				session.close();
				connection.close();
			}

		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	protected void runlib(String hql, MessageProducer producer, Session session) {
		UUID uuid_ = UUID.randomUUID();
		runHql runobj = new runHql();
		runobj.run(hql, uuid_.toString());
		RandomAccessFile rd=null;
		while (true) {
			try {
				Thread.sleep(5000);
				if(rd!=null)rd.close();
				 rd= new RandomAccessFile("/opt/hadoop/"
						+ uuid_.toString() + "_console", "r");

				while (true) {
					rd.seek(postion);
					String temp;
					String lastcontent = "";
					while ((temp = rd.readLine()) != null) {// 逐行读取该文件，如果定位到文件中一行的中间，则只读取从定位的位置开始的后半部分
						// temp = new String(temp.getBytes("8859_1"),
						// "gb2312");//转变编码格式：不进行转换的话读出的中文显示为乱码，很令人头疼的

						TextMessage message2 = session.createTextMessage(temp);
						producer.send(message2);
						session.commit();

						System.out.println("content:" + temp);
						System.out.println("postion:" + rd.getFilePointer());
						postion = rd.getFilePointer();
						lastcontent = temp;
						
						if(temp.indexOf("FAILED: Execution Error") != -1)
						{	
							break;
						}
					}

					if (lastcontent != null
							&& (lastcontent.indexOf("Time taken:") != -1)) {
						System.out.println("lastcontent:" + lastcontent);
						status = 0;
						break;
					} else if (lastcontent != null
							&& (lastcontent.indexOf("FAILED: Execution Error") != -1)) {
						errorCode=lastcontent;
						break;
					}

					Thread.sleep(2000);
				}
				if(rd!=null)rd.close();
				
				if (status == 0) {
					// begin recreate datafile to excel
					RandomAccessFile rddata = new RandomAccessFile(
							"/opt/hadoop/" + uuid_.toString(), "r");
					rddata.seek(0);

					HSSFWorkbook wb = new HSSFWorkbook();
					HSSFSheet sheet = wb.createSheet();

					String dataline;
					int lines = 0;
					while ((dataline = rddata.readLine()) != null) {// 逐行读取该文件，如果定位到文件中一行的中间，则只读取从定位的位置开始的后半部分
						// temp = new String(temp.getBytes("8859_1"),
						// "gb2312");//转变编码格式：不进行转换的话读出的中文显示为乱码，很令人头疼的
						System.out.println("content:" + dataline);
						HSSFRow row = sheet.createRow(lines);
						String dataTmp[] = dataline.split("\t");
						for (int i = 0; i < dataTmp.length; i++) {
							HSSFCell cell = row.createCell((short) i);
							cell.setCellValue(dataTmp[i]);
						}
						lines++;
					}

					rddata.close();
					// Write the output to a file

					FileOutputStream fileOut = new FileOutputStream(
							"/opt/hadoop/" + uuid_.toString() + ".xls");
					wb.write(fileOut);
					fileOut.close();
				}

				break;
			} catch (IOException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
}
