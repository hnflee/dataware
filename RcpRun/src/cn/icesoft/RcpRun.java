package cn.icesoft;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.fengmanfei.util.ImageFactory;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;

public class RcpRun {

	private static int BUFFER_SIZE = 8096; //
	private static long sizeForJar = 0;
	private static long targetSize = 21229179;
	protected Shell shell;
	private static int fileStatus = 1;
	static Logger log = Logger.getLogger(RcpRun.class);// log4j锟斤拷锟斤拷志锟侥硷拷
	ProgressBar progressBar = null;

	private Text text_2;
	private Text text_3;

	private PreferenceStore prestore = new PreferenceStore("options.properties");
	private Label lblNewLabel_2;

	/**
	 * Launch the application.
	 * 
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
		Rectangle area = Display.getDefault().getClientArea();
		try {
			prestore.load();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		
		shell = new Shell(SWT.DIALOG_TRIM);
		shell.setLocation(area.width/2-225, area.height/2-150);
		shell.setAlpha(210);
		shell.setSize(450, 300);
		shell.setText("数据平台");
		shell.setBackgroundImage(ImageFactory.loadImage(Display.getDefault(), "\\icons\\back.png"));
		shell.setLayout(null);
		shell.setImage(ImageFactory.loadImage(Display.getDefault(), "\\icons\\logo.png"));
		shell.setBackgroundMode(SWT.INHERIT_FORCE);
		
		final Button btnNewButton = new Button(shell, SWT.NONE);
		
		Label lblNewLabel = new Label(shell, SWT.NONE);
		lblNewLabel.setBounds( 125, 94, 35, 17);
		lblNewLabel.setText("用户:");
		
		
		Label lblNewLabel_1 = new Label(shell, SWT.NONE);
		lblNewLabel_1.setBounds(125, 134, 35, 17);
		lblNewLabel_1.setText("密码:");
		
		
		text_2 = new Text(shell, SWT.BORDER);
		text_2.setBounds(166, 91, 141, 23);
		text_2.setBackground(Display.getCurrent().getSystemColor(SWT.TRANSPARENCY_ALPHA));
		
		
		text_3 = new Text(shell, SWT.PASSWORD|SWT.BORDER);
		text_3.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				
				  if (e.keyCode== SWT.CR) {
					 
					  checkLogin();
					  
		        }
				
				
			}
		});
		text_3.setBounds(166, 134, 141, 23);
		text_3.setBackground(Display.getCurrent().getSystemColor(SWT.TRANSPARENCY_ALPHA));
		
		
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				
				checkLogin();
				
			}

			
		});
		
		btnNewButton.setBounds(197, 178, 80, 27);
		btnNewButton.setText("确定");
		
		lblNewLabel_2 = new Label(shell, SWT.NONE);
		
		lblNewLabel_2.setForeground(SWTResourceManager.getColor(SWT.COLOR_LIST_BACKGROUND));
		lblNewLabel_2.setFont(SWTResourceManager.getFont("微软雅黑", 16, SWT.NORMAL));
		lblNewLabel_2.setBounds(152, 10, 254, 63);
	
		
		
		
		

	}

	private void checkUpdate() {
		progressBar = new ProgressBar(shell, SWT.NONE);
		progressBar.setEnabled(false);
		progressBar.setBounds(42, 217, 370, 17);
		sizeForJar = 0;

		Runnable runnable2 = new Runnable() {
			public void run() {
				initProcess();
			}
		};

		new Thread(runnable2).start();

		Runnable runnable1 = new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				log.debug("fileStatus:" + fileStatus);
				while (fileStatus == 1) {
					try {

						Display.getDefault().asyncExec(new Runnable() {

							@Override
							public void run() {

								try {
									sizeForJar = getFileSizes(new File(
											"dataplant.jar"));
									log.debug("sz:"
											+ sizeForJar
											+ " "
											+ (int) (sizeForJar * 100 / targetSize));
									// TODO Auto-generated method stub
									progressBar
											.setSelection((int) (sizeForJar * 100 / targetSize));
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
				Display.getDefault().asyncExec(new Runnable() {

					@Override
					public void run() {

						shell.dispose();

					}

				});

			}
		};
		new Thread(runnable1).start();
	}

	private void initProcess() {
		// TODO Auto-generated method stub

		try {

			

			String version = prestore.getString("version");
			String destUrl = prestore.getString("updateUrl");

			saveToFile(destUrl, "Update.tmp");

			Properties propUpdateTmp = new Properties();
			FileInputStream fisTmp = new FileInputStream("Update.tmp");
			propUpdateTmp.load(fisTmp);//
			fisTmp.close();//
			String versionNew = propUpdateTmp.getProperty("version");
			targetSize = Long.parseLong(propUpdateTmp.getProperty("filesize"));

			if (versionNew.equals(version)) {
				fileStatus = 0;
				Display.getDefault().asyncExec(new Runnable() {

					@Override
					public void run() {

						lblNewLabel_2.setText("已经是最新版本");

					}

				});
				
				
				log.info("版本一致，不需要升級");
				Runtime.getRuntime().exec("java -jar dataplant.jar");
			} else {
				String fileNameURl = propUpdateTmp.getProperty("fileName");
				log.info("版本不一致，開始下載");
				
				Display.getDefault().asyncExec(new Runnable() {

					@Override
					public void run() {

						lblNewLabel_2.setText("有最新版本，升级中....");

					}

				});
				
				
				saveToFile(fileNameURl, "dataplant.jar");
				fileStatus = 0;
				log.info("版本不一致，下載完成");
				
				Display.getDefault().asyncExec(new Runnable() {

					@Override
					public void run() {

						lblNewLabel_2.setText("下載完成");

					}

				});
				
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

	public long getFileSizes(File f) throws Exception {//
		long s = 0;
		if (f.exists()) {
			FileInputStream fis = null;
			fis = new FileInputStream(f);
			s = fis.available();
		} else {
			f.createNewFile();
			System.out.println("");
		}
		return s;
	}

	/**
	 * 锟斤拷HTTP锟斤拷源锟斤拷锟轿拷募锟�
	 * 
	 * @param destUrl
	 *            String
	 * @param fileName
	 *            String
	 * @throws Exception
	 */
	public static void saveToFile(String destUrl, String fileName)
			throws IOException {
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
		log.debug("正在获取链接[" + destUrl + "]的内容...\n将其保存为文件[" + fileName + "]");

		while ((size = bis.read(buf)) != -1) {
			fos.write(buf, 0, size);
			sizeForJar += size;
		}

		fos.close();
		bis.close();

		httpUrl.disconnect();
	}

	private String Md5(String plainText) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes());
			byte b[] = md.digest();

			int i;

			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}

			System.out.println("result: " + buf.toString());// 32位的加密
			return buf.toString();

		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	private void checkLogin() {
		String db_username=prestore.getString("db_username");
		String db_pass=prestore.getString("db_pass");
		String db_name=prestore.getString("db_name");
		String db_ip=prestore.getString("db_ip");
		
		String fill_name=text_2.getText().trim();
		String fill_pass=text_3.getText().trim();
		
		if(fill_name.equals("")||	fill_pass.equals(""))
		{
			MessageBox messageBox =
				    new MessageBox(shell,SWT.ICON_WARNING);
				messageBox.setMessage("请填写用户名和密码再次提交！");
				messageBox.open(); 	
			return;
		}
		
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection("jdbc:mysql://"+db_ip+":3306/"+db_name+"?user="+db_username+"&password="+db_pass);
			String sql="SELECT t.password FROM easyhadoop.ehm_pha_user t where t.username=?";
			
			PreparedStatement  ps=conn.prepareStatement(sql);
			ps.setString(1,fill_name );
			
			ResultSet rs = ps.executeQuery();
			boolean resule_login=false;
			
			while(rs.next())
			{
				String pass=rs.getString(1);
				if(pass!=null&&Md5(fill_pass).equals(pass))
				{
					resule_login=true;
					checkUpdate();
				}
			}
			rs.close();
			ps.close();
			conn.close();
			if(resule_login==false)
			{
				MessageBox messageBox =
				    new MessageBox(shell,SWT.ICON_WARNING);
				messageBox.setMessage("用户名或密码错误");
				messageBox.open(); 
			
			}
			
			
			
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
