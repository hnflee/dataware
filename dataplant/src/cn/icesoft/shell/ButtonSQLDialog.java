package cn.icesoft.shell;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public class ButtonSQLDialog extends Dialog {
	static Logger log = Logger.getLogger(ButtonSQLDialog.class);//log4j的日志文件
	
	private Button button_;
	private Document document_;
	
	public Button getButton() {
		return button_;
	}

	public void setButton(Button button) {
		this.button_ = button;
	}

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public ButtonSQLDialog(Shell parentShell) {
		super(parentShell);
		
		
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		parent.getShell().setText("SQL编辑器 v0.1");
		
		Composite container = (Composite) super.createDialogArea(parent);
		new Label(container, SWT.NONE);
		TextViewer textViewer = new TextViewer(container, SWT.BORDER);
		document_=new Document();
		
		if(button_.getData("SQL")!=null)
		{
			document_.set(button_.getData("SQL").toString());
		}
		
		
		textViewer.setDocument(document_);
		
		StyledText styledText = textViewer.getTextWidget();
		styledText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		return container;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 300);
	}

	 protected void buttonPressed(int buttonId) {
	        //如果是点了OK按钮，则将值设置回类变量
	        if (buttonId == IDialogConstants.OK_ID)
	        {
	        	button_.setData("SQL",document_.get());
	        	
	        	if(document_.get()!=null&&document_.get().toLowerCase().indexOf("delete")!=-1)
	        	{
	        		MessageBox messageBox =
						    new MessageBox(this.getShell(),SWT.ICON_WARNING);
						messageBox.setMessage("SQl语句中不能包含'delete'关键字!");
						messageBox.open(); 
	        	}
	        	
	        	log.debug("对话框输入的内容内容："+document_.get());
	        }
	        super.buttonPressed(buttonId);
	}
	
}
