package cn.icesoft.native_c;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.UUID;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class NativeMain {

	static long postion = 0;
	static int status = 1;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		UUID uuid_ = UUID.randomUUID();
		runHql runobj = new runHql();
		/*runobj.run(
				"select t.tkt_nm, t.flight_seg, t.market_fare, t.net_fare, t.source from et_prd.rt_et_sales_report t where t.airline_3code = '898' and t.airline_2code = 'JD' and t.trans_type = 'NORMAL' and t.flight_date between '2013-06-01' and '2013-06-30' and t.product_code = 'WZZX'",
				uuid_.toString());*/

		while (true) {
			try {
				RandomAccessFile rd = new RandomAccessFile("/opt/hadoop/"
						+ "96efe439-65d5-47be-9a8a-e80baf1c8611" + "_console", "r");

				while (true) {
					rd.seek(postion);
					String temp;
					String lastcontent = "";
					while ((temp = rd.readLine()) != null) {// 逐行读取该文件，如果定位到文件中一行的中间，则只读取从定位的位置开始的后半部分
						// temp = new String(temp.getBytes("8859_1"),
						// "gb2312");//转变编码格式：不进行转换的话读出的中文显示为乱码，很令人头疼的
						System.out.println("content:" + temp);
						System.out.println("postion:" + rd.getFilePointer());
						postion = rd.getFilePointer();

						lastcontent = temp;
					}

					if (lastcontent != null
							&& (lastcontent.indexOf("Time taken:") != -1)) {
						System.out.println("lastcontent:" + lastcontent);
						status = 0;
						break;
					}
					Thread.sleep(2000);
				}

				if (status == 0) {
					// begin recreate datafile to excel
					RandomAccessFile rddata = new RandomAccessFile(
							"/opt/hadoop/96efe439-65d5-47be-9a8a-e80baf1c8611", "r");
					rddata.seek(0);

					

					HSSFWorkbook wb = new HSSFWorkbook();
					HSSFSheet sheet = wb.createSheet();
					

					
					String dataline;
					int lines=0;
					while ((dataline = rddata.readLine()) != null) {// 逐行读取该文件，如果定位到文件中一行的中间，则只读取从定位的位置开始的后半部分
						// temp = new String(temp.getBytes("8859_1"),
						// "gb2312");//转变编码格式：不进行转换的话读出的中文显示为乱码，很令人头疼的
						System.out.println("content:" + dataline);
						HSSFRow row = sheet.createRow(lines);
						String dataTmp[]=dataline.split("\t");
						for(int i=0;i<dataTmp.length;i++)
						{
							HSSFCell cell = row.createCell((short) i);
							cell.setCellValue(dataTmp[i]);
						}
						lines++;
					}
					
					
					
					
					
					

					// Write the output to a file

					FileOutputStream fileOut = new FileOutputStream(
							"/opt/hadoop/96efe439-65d5-47be-9a8a-e80baf1c8611.xls");

					wb.write(fileOut);

					fileOut.close();

					break;
				}

			} catch (IOException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

		}

	}

}
