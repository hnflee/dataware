package cn.icesoft.main;


import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Button;


public class DataFlowArea extends Composite {

	Rectangle area = Display.getDefault().getClientArea();
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public DataFlowArea(Composite parent, int style) {
		
		super(parent, style);
		setLayout(new GridLayout(1, false));
		

	
		
		
		
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
