package cn.icesoft.main;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.custom.ScrolledComposite;


public class OptionPlants extends Composite {
	Rectangle area = Display.getDefault().getClientArea();
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public OptionPlants(Composite parent, int style) {
		super(parent, style);
		
		this.setLayout(new FillLayout());
		
		
				
		TabFolder tabFolder = new TabFolder(this, SWT.TOP);
		tabFolder.setLocation(0, 0);
		tabFolder.setSize(338,30);
		tabFolder.setLayout(new FillLayout());
		
		TabItem item=new TabItem(tabFolder,SWT.NONE);
		item.setText("Ô´");
		
		ScrolledComposite scrolledComposite = new ScrolledComposite(tabFolder, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		item.setControl(scrolledComposite);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		scrolledComposite.setMinSize((int)(area.width*0.98), (int)(area.height*0.20));
		
		
		
		
		TabItem item1=new TabItem(tabFolder,SWT.NONE);
		item1.setText("Êä³ö");

		tabFolder.pack();
		
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
