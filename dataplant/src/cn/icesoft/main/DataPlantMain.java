package cn.icesoft.main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CBanner;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

import cn.icesoft.shell.InitOptionPreference;

import com.fengmanfei.util.ImageFactory;

public class DataPlantMain {

	protected Shell shell;
	Display display = new Display();

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			
			DataPlantMain window = new DataPlantMain();
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

		Properties prop = new Properties();// 属性集合对象    
        FileInputStream fis;
		try {
			fis = new FileInputStream("options.properties");
			prop.load(fis);// 将属性文件流装载到Properties对象中    
		    fis.close();// 关闭流    
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// 属性文件输入流    
      
		
		Rectangle area = Display.getDefault().getClientArea();
		shell = new Shell();
		shell.setImage(ImageFactory.loadImage(display, "\\icons\\eclipse_icons\\import_brkpts_wizban.gif"));//import_brkpts_wizban.gif
		shell.setLocation(0, 0);
		shell.setSize(area.width, area.height);
		shell.setText("智能数据平台");
		shell.setLayout(new FillLayout());
		shell.setMaximized(true);

		Menu main = setMenu();

		shell.setMenuBar(main);

		CBanner banner = new CBanner(shell, SWT.BORDER);
		banner.setLayout(new FillLayout());

		DataFlowArea dataflow = new DataFlowArea(banner, SWT.NONE);
		dataflow.setPORT(Integer.parseInt(prop.getProperty("hiveserver_port")));
		dataflow.setHOST(prop.getProperty("hiveserver_ip"));
		RightComposite rightcomp = new RightComposite(banner, SWT.NONE);
		OptionPlants option = new OptionPlants(banner, SWT.NONE);

		banner.setLeft(dataflow);
		banner.setRight(rightcomp);
		banner.setBottom(option);

		((DataFlowArea)dataflow).setRightcomp(rightcomp);
		((DataFlowArea)dataflow).setOptionPlants_(option);
		
	}

	/**
	 * 
	 * @return
	 */
	private Menu setMenu() {
		Menu main = new Menu(shell, SWT.BAR);
		MenuItem file = new MenuItem(main, SWT.CASCADE);
		file.setText("文件(&F)");

		MenuItem option = new MenuItem(main, SWT.CASCADE);
		option.setText("设置(&O)");

		Menu optionMenu = new Menu(shell, SWT.DROP_DOWN);
		MenuItem optionItem = new MenuItem(optionMenu, SWT.PUSH);

		optionItem.setText("首选项(&P)");
		optionItem.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event event) {
				// TODO Auto-generated method stub
				new InitOptionPreference().init();
			}

		}

		);
		option.setMenu(optionMenu);

		return main;
	}

}
