package cn.icesoft.shell;

import java.io.IOException;

import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.preference.PreferenceNode;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;

public class InitOptionPreference {
	
	public InitOptionPreference()
	{
		
	}

	
	public void init()
	{
		PreferenceManager manager=new PreferenceManager();
		
		PreferenceStore prestore=new PreferenceStore("options.properties");
		PreferenceNode nodeOne=new PreferenceNode("TCP SERVER","TCP SERVER",null,SystemSettingPage.class.getName());
		PreferenceNode nodeOne1=new PreferenceNode("关于我们","易建科技",null,AboutPropertyPage.class.getName());
		
		manager.addToRoot(nodeOne);
		manager.addToRoot(nodeOne1);
		PreferenceDialog dlg=new PreferenceDialog(null,manager);
		
		prestore.addPropertyChangeListener( new IPropertyChangeListener()
		{

			@Override
			public void propertyChange(PropertyChangeEvent event) {
				// TODO Auto-generated method stub
				System.out.println("changer value");
			}
			
		}
		);
		
		try {
			
			prestore.load();
			dlg.setPreferenceStore(prestore);
			dlg.open();
			prestore.save();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
