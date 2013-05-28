package cn.icesoft.main;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

public class RightComposite extends Composite {
	static Logger log = Logger.getLogger(RightComposite.class);//log4j的日志文件
	Rectangle area = Display.getDefault().getClientArea();
	
	private Browser browser;
	
	public Browser getBrowser() {
		return browser;
	}

	public void setBrowser(Browser browser) {
		this.browser = browser;
	}

	String messageFromMina_;


	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public RightComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(1, false));
		
		TabFolder tabFolder = new TabFolder(this, SWT.NONE);
		GridData gd_tabFolder = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_tabFolder.widthHint = (int)(area.width*0.28);
		gd_tabFolder.heightHint = (int)(area.height*0.7);
		tabFolder.setLayoutData(gd_tabFolder);
		tabFolder.setLocation(0, 0);
		tabFolder.setLayout(new FillLayout());
		
		TabItem tbtmNewItem_1 = new TabItem(tabFolder, SWT.NONE);
		tbtmNewItem_1.setText("数据下载");
		
		browser = new Browser(tabFolder, SWT.NONE);
		tbtmNewItem_1.setControl(browser);
		

			
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
