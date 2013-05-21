package cn.icesoft.mina;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

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

	static String errorCode = "";

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

		if (msg != null && msg.length() > 10) {
			if (msg.substring(0, 10).equals("1010000000"))// send command
			{
				String[] tmp = msg.substring(10, msg.length()).split("\001");
				// hql explain
				runlib("explain " + tmp[0], tmp[1] + "_explain");
				// run hql
				runlib(tmp[0], tmp[1]);
				session.write("1010000000\002EOF\00200000"+"\001"+tmp[1]);
				
			} else if (msg.substring(0, 10).equals("1020000000"))// query
			{
				String[] tmp = msg.substring(10, msg.length()).split("\001");
				String tmpquery = getQueryMsg(tmp[1], tmp[0]);
				session.write("1020000000"+tmpquery);
				
				
			} else if (msg.substring(0, 10).equals("1030000000"))// create ecxel
			{
				String[] tmp = msg.substring(10, msg.length()).split("\001");
				String datafile = "/opt/hadoop/tomcat/apache-tomcat-6.0.37/webapps/ROOT/";
				if (!(new File(datafile + tmp[0] + ".xls").exists())) {
					List<String> listtmp = readHql(tmp[0]);
					for (int i = 0; i < listtmp.size(); i++) {
						String a = listtmp.get(i);
						logger.debug("content:" + a);
					}
					int returncode=writeExcel(listtmp, tmp[0]);
					if(returncode==0)session.write("1030000000"+tmp[0]+"\001\002EOF\00200000");
					else session.write("1030000000"+tmp[0]+"\001\002EOF\00210000");
					//runCMD
					runcmd("cp /opt/hadoop/"+tmp[0]+" "+datafile+tmp[0]);
					
				} else {
					session.write("1030000000"+tmp[0]+"\001\002EOF\00200000");
				}

			}
		} else if (msg != null && msg.equalsIgnoreCase("quit")) {
			session.close();
		} 

	}

	protected String getQueryMsg(String postion, String uuid_) {
		logger.debug("postion:" + postion);
		logger.debug("uuid_:" + uuid_);

		int status = 1;
		RandomAccessFile rd = null;
		StringBuffer strbuf = new StringBuffer();
		try {

			if (rd != null)
				rd.close();
			rd = new RandomAccessFile("/opt/hadoop/" + uuid_
					+ "_console", "r");

			rd.seek(Integer.parseInt(postion));
			String temp;
			String lastcontent = "";
			while ((temp = rd.readLine()) != null) {// 逐行读取该文件，如果定位到文件中一行的中间，则只读取从定位的位置开始的后半部分
				// temp = new String(temp.getBytes("8859_1"),
				// "gb2312");//转变编码格式：不进行转换的话读出的中文显示为乱码，很令人头疼的
				strbuf.append(temp + "\n");

				System.out.println("content:" + temp);
				System.out.println("postion:" + rd.getFilePointer());

				postion = String.valueOf(rd.getFilePointer());
				lastcontent = temp;
				if (temp.indexOf("FAILED:") != -1) {
					strbuf.append("\002EOF\00210002" + "\n");//failed
					postion= "\002EOF\00210002";
					break;
				}
			}

			if (lastcontent != null&&!postion.equals("\002EOF\00210002")&& (lastcontent.indexOf("Time taken:") != -1)) {
				System.out.println("lastcontent:" + lastcontent);
				
				strbuf.append(lastcontent);
				postion="\002EOF\00200000";	

			}
			
			if (rd != null)
				rd.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		
		
		return uuid_+"\001"+postion+ "\001" + strbuf.toString();
	}

	private List readHql(String fileName) throws IOException {
		// TODO Auto-generated method stub
		List<String> params = new ArrayList();
		RandomAccessFile rddata = new RandomAccessFile("/opt/hadoop/"
				+ fileName + "_explain", "r");
		rddata.seek(0);

		String dataline;
		int open = 1;
		int operate=1;
		
		while ((dataline = rddata.readLine()) != null) {
			if (dataline.indexOf("Select Operator") != -1) {
				operate = 0;
				continue;
			}//Select Operator
			
			if (operate==0&&dataline.indexOf("expressions:") != -1) {
				open = 0;
				continue;
			}
			if (open==0&&dataline.indexOf("outputColumnNames:") != -1) {
				break;
			}

			if (open == 0) {
				params.add(dataline.split(":")[1]);
			}

		}

		rddata.close();
		// Write the output to a file

		return params;
	}

	private int writeExcel(List listconlume, String fileName) {
		int status = 1;
		RandomAccessFile rddata;
		try {
			rddata = new RandomAccessFile("/opt/hadoop/" + fileName, "r");

			rddata.seek(0);

			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFSheet sheet = wb.createSheet();

			String dataline;
			int lines = 0;
			while ((dataline = rddata.readLine()) != null) {

				HSSFRow row = sheet.createRow(lines);
				String dataTmp[] = dataline.split("\t");
				for (int i = 0; i < dataTmp.length; i++) {
					HSSFCell cell = row.createCell(i);
					if (listconlume.get(i * 2 + 1).toString().trim()
							.equals("string")) {
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellValue(new String(dataTmp[i]
								.getBytes("iso-8859-1"), "UTF-8"));
					} else if (listconlume.get(i * 2 + 1).toString().trim()
							.equals("double")) {
						cell.setCellValue(Double.parseDouble(dataTmp[i]));

					} else if (listconlume.get(i * 2 + 1).toString().trim()
							.equals("bigint")) {
						cell.setCellValue(Integer.parseInt(dataTmp[i]));

					}

				}
				lines++;
			}

			rddata.close();
			// Write the output to a file

			FileOutputStream fileOut = new FileOutputStream(
					"/opt/hadoop/tomcat/apache-tomcat-6.0.37/webapps/ROOT/"
							+ fileName + ".xls");
			wb.write(fileOut);
			fileOut.close();
			status = 0;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			status=1;
		}
		return status;
	}

	protected void runlib(String hql, String uuid_) {
		logger.debug("hql:" + hql);

		runHql runobj = new runHql();
		runobj.run(hql, uuid_);

	}
	
	protected void runcmd(String cmd_)
	{
		logger.debug("cmd" + cmd_);

		runHql runobj = new runHql();
		runobj.runCMD(cmd_);
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
