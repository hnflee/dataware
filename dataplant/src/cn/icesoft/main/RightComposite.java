package cn.icesoft.main;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.layout.GridLayout;

public class RightComposite extends Composite {

	Rectangle area = Display.getDefault().getClientArea();
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

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
