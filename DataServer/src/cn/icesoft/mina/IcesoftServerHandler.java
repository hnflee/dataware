package cn.icesoft.mina;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import cn.icesoft.native_c.runHql;

public class IcesoftServerHandler extends IoHandlerAdapter {
	public static Logger logger = Logger.getLogger(IcesoftServerHandler.class);
	
	
	static String errorCode="";
	@Override
	public void sessionCreated(IoSession session) throws Exception {
		logger.info("服务端与客户端创建连接...");
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		logger.info("服务端与客户端连接打开...");
	}

	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		String msg = message.toString();
		logger.info("服务端接收到的数据为：" + msg);
		
		
		if(msg!=null&&msg.length()>10)
		{
			if(msg.substring(0, 10).equals("1010000000"))//send command
			{
				String[] tmp=msg.substring(10, msg.length()).split("\001");
				runlib(tmp[0],tmp[1]);
				session.write("1010000100"+tmp[1]);
			}
			else if(msg.substring(0, 10).equals("1020000000"))//query
			{
				String[] tmp=msg.substring(10, msg.length()).split("\001");
				String tmpquery=getQueryMsg( Long.parseLong(tmp[0]), tmp[1]);
				if(tmpquery.split("\001")[0]!=null&&!tmpquery.split("\001")[0].trim().equals(""))
				{
					session.write("1020000101"+tmpquery.split("\001")[0]);
				}
				session.write("1020000100"+tmpquery.split("\001")[1]+"\001"+ tmp[1]);
			}
		}
		else if (msg!=null&&msg.equalsIgnoreCase("quit"))
		{
			session.close();
		}
		else
		{
			session.write("\002EOF\00210001");//format error
		}
		
	
	}

	protected String getQueryMsg(long postion,String uuid_)
	{
		logger.debug("postion:"+postion);
		logger.debug("uuid_:"+uuid_);
		
		
		int status=1;
		RandomAccessFile rd=null;
		StringBuffer strbuf=new StringBuffer();
		try {
			
			if(rd!=null)rd.close();
			 rd= new RandomAccessFile("/opt/hadoop/"
					+ uuid_.toString() + "_console", "r");

			
				rd.seek(postion);
				String temp;
				String lastcontent = "";
				while ((temp = rd.readLine()) != null) {// 逐行读取该文件，如果定位到文件中一行的中间，则只读取从定位的位置开始的后半部分
					// temp = new String(temp.getBytes("8859_1"),
					// "gb2312");//转变编码格式：不进行转换的话读出的中文显示为乱码，很令人头疼的
					strbuf.append(temp+"\n");
					
					System.out.println("content:" + temp);
					System.out.println("postion:" + rd.getFilePointer());
					
					postion = rd.getFilePointer();
					lastcontent = temp;
					if(temp.indexOf("FAILED:") != -1)
					{	
						strbuf.append("\002EOF\00210002"+"\n");
						break;
					}
				}

				if (lastcontent != null
						&& (lastcontent.indexOf("Time taken:") != -1)) {
					System.out.println("lastcontent:" + lastcontent);
					status = 0;
					strbuf.append("\002EOF\00200000"+"\n");
					
				} else if (lastcontent != null
						&& (lastcontent.indexOf("FAILED:") != -1)) {
					errorCode=lastcontent;
				}
			
			if(rd!=null)rd.close();
			
			String datafile="/home/hadoop/jetty-distribution-9.0.2.v20130417/webapps/ROOT/";
			if (status == 0&&!(new File(datafile + uuid_.toString() + ".xls").exists()))
			{
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
						datafile + uuid_.toString() + ".xls");
				wb.write(fileOut);
				fileOut.close();
			}

			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} 
		return strbuf.toString()+"\001"+postion;
	}
	
	
	protected void runlib(String hql,String uuid_) {
		logger.debug("hql:"+hql);
		
		runHql runobj = new runHql();
		runobj.run(hql, uuid_);
		
	}
	
	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		logger.info("服务端发送信息成功...");
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {

	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status)
			throws Exception {
		logger.info("服务端进入空闲状态...");
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		logger.error("服务端发送异常...", cause);
	}
}
