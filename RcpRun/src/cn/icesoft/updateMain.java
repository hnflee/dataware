package cn.icesoft;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.preference.PreferenceStore;

public class updateMain {

	static Logger log = Logger.getLogger(updateMain.class);//log4j的日志文件
	private static int BUFFER_SIZE = 8096; //缓冲区大小
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		
		
       
		try {
		
			PreferenceManager manager=new PreferenceManager();
			PreferenceStore prestore=new PreferenceStore("options.properties");
			prestore.load();
			
	
			String version=prestore.getString("version");
			String destUrl=prestore.getString("updateUrl");
		
			saveToFile(destUrl,"Update.tmp");

			
			Properties propUpdateTmp=new Properties();
			FileInputStream  fisTmp = new FileInputStream("Update.tmp");
			propUpdateTmp.load(fisTmp);// 将属性文件流装载到Properties对象中    
			fisTmp.close();// 关闭流    
			String versionNew=propUpdateTmp.getProperty("version");
			
			if(versionNew.equals(version))
			{
				log.info("版本一致，不需要升級");
				Runtime.getRuntime().exec("java -jar dataplant.jar");
			}
			else
			{
				String fileNameURl=propUpdateTmp.getProperty("fileName");
				log.info("版本不一致，開始下載");
				saveToFile(fileNameURl,"dataplant.jar");
				log.info("版本不一致，下載完成");
				prestore.setValue("version", versionNew);
				prestore.save();
				Runtime.getRuntime().exec("java -jar dataplant.jar");
			}
			
			
			
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
				
//		try {
//			Runtime.getRuntime().exec("java -jar dataplant.jar");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	
		
	}

    /**
     * 将HTTP资源另存为文件
     *
     * @param destUrl String
     * @param fileName String
     * @throws Exception
     */
    public static void saveToFile(String destUrl, String fileName) throws IOException {
        FileOutputStream fos = null;
        BufferedInputStream bis = null;
        HttpURLConnection httpUrl = null;
        URL url = null;
        byte[] buf = new byte[BUFFER_SIZE];
        int size = 0;
       

//建立链接
        url = new URL(destUrl);
        httpUrl = (HttpURLConnection) url.openConnection();
//连接指定的资源
        httpUrl.connect();
//获取网络输入流
        bis = new BufferedInputStream(httpUrl.getInputStream());
//建立文件
        fos = new FileOutputStream(fileName);

        log.debug("正在获取链接[" + destUrl + "]的内容...\n将其保存为文件[" +                               fileName + "]");
//保存文件
        while ((size = bis.read(buf)) != -1)
            fos.write(buf, 0, size);

        fos.close();
        bis.close();
        httpUrl.disconnect();
    }
	
}
