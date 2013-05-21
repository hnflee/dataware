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

	static Logger log = Logger.getLogger(updateMain.class);//log4j����־�ļ�
	private static int BUFFER_SIZE = 8096; //��������С
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
			propUpdateTmp.load(fisTmp);// �������ļ���װ�ص�Properties������    
			fisTmp.close();// �ر���    
			String versionNew=propUpdateTmp.getProperty("version");
			
			if(versionNew.equals(version))
			{
				log.info("�汾һ�£�����Ҫ����");
				Runtime.getRuntime().exec("java -jar dataplant.jar");
			}
			else
			{
				String fileNameURl=propUpdateTmp.getProperty("fileName");
				log.info("�汾��һ�£��_ʼ���d");
				saveToFile(fileNameURl,"dataplant.jar");
				log.info("�汾��һ�£����d���");
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
     * ��HTTP��Դ���Ϊ�ļ�
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
       

//��������
        url = new URL(destUrl);
        httpUrl = (HttpURLConnection) url.openConnection();
//����ָ������Դ
        httpUrl.connect();
//��ȡ����������
        bis = new BufferedInputStream(httpUrl.getInputStream());
//�����ļ�
        fos = new FileOutputStream(fileName);

        log.debug("���ڻ�ȡ����[" + destUrl + "]������...\n���䱣��Ϊ�ļ�[" +                               fileName + "]");
//�����ļ�
        while ((size = bis.read(buf)) != -1)
            fos.write(buf, 0, size);

        fos.close();
        bis.close();
        httpUrl.disconnect();
    }
	
}
