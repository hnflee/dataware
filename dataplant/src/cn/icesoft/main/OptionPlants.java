package cn.icesoft.main;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import com.fengmanfei.util.ImageFactory;


public class OptionPlants extends Composite {
	Rectangle area = Display.getDefault().getClientArea();
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public OptionPlants(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(1, false));
		
		
				
		TabFolder tabFolder = new TabFolder(this, SWT.TOP);
		GridData gd_tabFolder = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_tabFolder.widthHint = (int)(area.width*0.99);
		gd_tabFolder.heightHint = (int)(area.height*0.18);
		tabFolder.setLayoutData(gd_tabFolder);
		tabFolder.setLocation(0, 0);
		tabFolder.setLayout(new FillLayout());
		
		
		
		TabItem item=new TabItem(tabFolder,SWT.NONE);
		item.setText("数据源");
		
		Composite composite = new Composite(tabFolder, SWT.NONE);
		item.setControl(composite);
		
		Button btnNewButton = new Button(composite, SWT.NONE);
		btnNewButton.setBounds(10, 81, 108, 63);
		btnNewButton.setText("hadoop源(UAT)");
		
		Button btnNewButton_1 = new Button(composite, SWT.NONE);
		btnNewButton_1.setBounds(124, 81, 113, 63);
		btnNewButton_1.setText("oracle源");
		
		ToolBar toolBar = new ToolBar(composite, SWT.FLAT | SWT.RIGHT);
		toolBar.setBounds(10, 10, 97, 25);
		final ToolItem add =new ToolItem(toolBar,SWT.PUSH);
		add.setText("添加");
		add.setImage(ImageFactory.loadImage(toolBar.getDisplay(), ImageFactory.ADD_OBJ));
		
		
		TabItem item1=new TabItem(tabFolder,SWT.NONE);
		item1.setText("SQL");
		
		TabItem item2=new TabItem(tabFolder,SWT.NONE);
		item2.setText("输出格式");
		
		
		TabItem item3=new TabItem(tabFolder,SWT.NONE);
		item3.setText("图形化");
		
		TabItem item4=new TabItem(tabFolder,SWT.NONE);
		item4.setText("控制台");
		

		tabFolder.pack();
		
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
