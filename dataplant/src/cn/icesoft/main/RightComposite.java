package cn.icesoft.main;

import org.apache.log4j.Logger;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.jface.viewers.TableTreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.TableTree;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

public class RightComposite extends Composite {
	static Logger log = Logger.getLogger(RightComposite.class);//log4j的日志文件
	Rectangle area = Display.getDefault().getClientArea();
	private TableTree tableTree;
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
		
		TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText("执行过程");
		
		Composite composite = new Composite(tabFolder, SWT.NONE);
		tabItem.setControl(composite);
		composite.setLayout(new GridLayout(1, false));
		
		TableTreeViewer tableTreeViewer = new TableTreeViewer(composite, SWT.BORDER | SWT.FULL_SELECTION);
		tableTree = tableTreeViewer.getTableTree();
		tableTree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		TabItem tbtmNewItem_1 = new TabItem(tabFolder, SWT.NONE);
		tbtmNewItem_1.setText("数据下载");
		
		browser = new Browser(tabFolder, SWT.NONE);
		tbtmNewItem_1.setControl(browser);
		
		
		
		
		
		
		
		tableTree.addFocusListener(new FocusListener(){

			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				log.debug("coming in FocusListener");
			}

			@Override
			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub
				
			}});

			
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
