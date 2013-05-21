package cn.icesoft;

import java.io.BufferedInputStream;
import java.io.File;
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
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;

import com.fengmanfei.util.ImageFactory;

public class RcpRun {
	
	private static int BUFFER_SIZE = 8096; //锟斤拷锟斤拷锟斤拷锟叫�
	private static long sizeForJar=0;
	private static long targetSize=21229179;
	protected Shell shell;
	private static int fileStatus=1;
	static Logger log = Logger.getLogger(RcpRun.class);//log4j锟斤拷锟斤拷志锟侥硷拷
	ProgressBar progressBar=null;
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			RcpRun window = new RcpRun();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell(SWT.BORDER);
		shell.setAlpha(210);
		shell.setSize(450, 300);
		shell.setText("");
		shell.setBackgroundImage(ImageFactory.loadImage(Display.getDefault(), "\\icons\\back.png"));
		
		progressBar= new ProgressBar(shell, SWT.NONE);
		progressBar.setBounds(36, 208, 370, 17);
		sizeForJar=0;
		
		Runnable runnable2=new Runnable(){
			public void run()
			{
				initProcess();
			}
		};
		
		new Thread(runnable2).start();
		
		Runnable runnable1=new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				log.debug("fileStatus:"+fileStatus);
				while(fileStatus==1)
				{	
					 try {
						
						Display.getDefault().asyncExec(new Runnable(){

							@Override
							public void run() {
								
								
								try {
									sizeForJar = getFileSizes(new File("dataplant.jar"));
									log.debug("sz:"+sizeForJar+" "+(int) (sizeForJar*100/targetSize));
									// TODO Auto-generated method stub
									progressBar.setSelection((int) (sizeForJar*100/targetSize));
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
							}

						
						});	
						
						
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					 try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				Display.getDefault().asyncExec(new Runnable(){

					@Override
					public void run() {
						
						
						shell.dispose();
						
					}

				
				});	
				
			}};
			new Thread(runnable1).start();

	}
	
	private void initProcess() {
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
			propUpdateTmp.load(fisTmp);//
			fisTmp.close();//
			String versionNew=propUpdateTmp.getProperty("version");
			
			if(versionNew.equals(version))
			{
				fileStatus=0;
				log.info("版本一致，不需要升級");
				Runtime.getRuntime().exec("java -jar dataplant.jar");
			}
			else
			{
				String fileNameURl=propUpdateTmp.getProperty("fileName");
				log.info("版本不一致，開始下載");
			
					
					
					
					
				saveToFile(fileNameURl,"dataplant.jar");
				fileStatus=0;
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
	}

	public long getFileSizes(File f) throws Exception{//
        long s=0;
        if (f.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(f);
           s= fis.available();
        } else {
            f.createNewFile();
            System.out.println("");
        }
        return s;
    }
	   /**
     * 锟斤拷HTTP锟斤拷源锟斤拷锟轿拷募锟�
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
       


        url = new URL(destUrl);
        httpUrl = (HttpURLConnection) url.openConnection();

        httpUrl.connect();

        bis = new BufferedInputStream(httpUrl.getInputStream());

        fos = new FileOutputStream(fileName);
        log.debug("正在获取链接[" + destUrl + "]的内容...\n将其保存为文件[" +                               fileName + "]");
      

        while ((size = bis.read(buf)) != -1)
        {
            fos.write(buf, 0, size);
            sizeForJar+=size;
        }

        fos.close();
        bis.close();
        
       
        
        httpUrl.disconnect();
    }
}
