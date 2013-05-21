package cn.icesoft.native_c;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
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
		String fileName="bbf5b1e7-0825-43c7-ae34-2b0a809cc564";
		try {
			String datafile = "/opt/hadoop/tomcat/apache-tomcat-6.0.37/webapps/ROOT/";
			runcmd("cp /opt/hadoop/"+fileName+" "+datafile+fileName);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	protected static void runcmd(String cmd_)
	{
		

		runHql runobj = new runHql();
		runobj.runCMD(cmd_);
	}
	private static List readHql(String fileName) throws IOException {
		// TODO Auto-generated method stub
		List<String> params = new ArrayList();
		RandomAccessFile rddata = new RandomAccessFile("/opt/hadoop/"
				+ fileName + "_explain", "r");
		rddata.seek(0);

		String dataline;
		int open = 1;
		int operate=1;
		
		while ((dataline = rddata.readLine()) != null) {
			System.out.println(dataline);
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

	private static int writeExcel(List listconlume, String fileName) {
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
					"/opt/hadoop/"
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

}
