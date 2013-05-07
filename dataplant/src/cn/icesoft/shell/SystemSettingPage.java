package cn.icesoft.shell;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;



public class SystemSettingPage extends PreferencePage {
	private Text ipaddress_;
	private Text port_;
	public SystemSettingPage() {
	}
						


	@Override
	protected Control createContents(Composite parent) {
		// TODO Auto-generated method stub
		IPreferenceStore preferenceStore=this.getPreferenceStore();
		Composite comp=new Composite(parent,SWT.NONE);
		comp.setLayout(new GridLayout(5, false));
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		
		Label lblNewLabel = new Label(comp, SWT.LEFT);
		lblNewLabel.setText("IP address:");
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		
		ipaddress_ = new Text(comp, SWT.BORDER);
		ipaddress_.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		ipaddress_.setText(preferenceStore.getString("hiveserver_ip"));
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		
		Label lblNewLabel_1 = new Label(comp, SWT.LEFT);
		lblNewLabel_1.setText("Port:");
		new Label(comp, SWT.NONE);
		new Label(comp, SWT.NONE);
		
		port_ = new Text(comp, SWT.BORDER);
		port_.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		port_.setText(preferenceStore.getString("hiveserver_port"));
		
		
		
		
		
		
		return comp;
	}
	
	protected void performDefaults()
	{
		ipaddress_.setText("10.71.84.233");
		port_.setText("10000");
	}

	public boolean performOk()
	{
		System.out.println("ipaddress_.getText()："+ipaddress_.getText());
		System.out.println("port_.getText()："+port_.getText());
		
		
		IPreferenceStore preferenceStore=this.getPreferenceStore();
		if(ipaddress_!=null)preferenceStore.setValue("hiveserver_ip", ipaddress_.getText());
		if(port_!=null)preferenceStore.setValue("hiveserver_port", port_.getText());
		return true;
		
	}
}
