package cn.icesoft.main;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import com.fengmanfei.util.ImageFactory;


public class OptionPlants extends Composite {
	Rectangle area = Display.getDefault().getClientArea();
	static Logger log = Logger.getLogger(OptionPlants.class);//log4j的日志文件
	private StyledText textConsole;
	public StyledText getTextConsole() {
		return textConsole;
	}

	public void setTextConsole(StyledText textConsole) {
		this.textConsole = textConsole;
	}

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
		
		final Button btnNewButton = new Button(composite, SWT.NONE);
		btnNewButton.setBounds(10, 81, 108, 63);
		btnNewButton.setText("hadoop源(UAT)");
		btnNewButton.setData("value", "1\001S\001hadoop源(UAT)\001H\00110.71.84.233,10000");
		btnNewButton.setImage(ImageFactory.loadImage(composite.getDisplay(), "\\icons\\eclipse_icons\\memory_view.gif"));
		//ImageFactory.loadImage(composite.getDisplay(), "\\icons\\eclipse_icons\\memory_view.gif.gif")
		
		DragSource dragSource = new DragSource(btnNewButton, DND.DROP_MOVE|DND.DROP_COPY);
		dragSource.setTransfer(new Transfer[]{TextTransfer.getInstance()});
		
		dragSource.addDragListener(new DragSourceListener(){

			@Override
			public void dragStart(DragSourceEvent event) {
				// TODO Auto-generated method stub
				if(btnNewButton.forceFocus()==false)
				{
					event.doit=false;
					log.debug("....... forceFocus false");
				}
				log.debug("....... start");
			}

			@Override
			public void dragSetData(DragSourceEvent event) {
				// TODO Auto-generated method stub
				if(TextTransfer.getInstance().isSupportedType(event.dataType))
				{
					event.data=btnNewButton.getData("value");
					log.debug("....... set value");
				}
			}

			@Override
			public void dragFinished(DragSourceEvent event) {
				// TODO Auto-generated method stub
				log.debug("....... finished");
			}});
		
		
		
		final Button btnNewButton_1 = new Button(composite, SWT.NONE);
		btnNewButton_1.setBounds(124, 81, 113, 63);
		btnNewButton_1.setText("oracle源");
		
		btnNewButton_1.setData("value", "1\001S\001oracle源\001O\00110.71.84.233,10000");
		
		DragSource dragSource_1 = new DragSource(btnNewButton_1, DND.DROP_MOVE|DND.DROP_COPY);
		
		dragSource_1.setTransfer(new Transfer[]{TextTransfer.getInstance()});
		dragSource_1.addDragListener(new DragSourceListener(){

			@Override
			public void dragStart(DragSourceEvent event) {
				// TODO Auto-generated method stub
				if(btnNewButton_1.forceFocus()==false)
				{
					event.doit=false;
					log.debug("....... forceFocus false");
				}
				log.debug("....... start");
			}

			@Override
			public void dragSetData(DragSourceEvent event) {
				// TODO Auto-generated method stub
				if(TextTransfer.getInstance().isSupportedType(event.dataType))
				{
					event.data=btnNewButton_1.getData("value");
					log.debug("....... set value");
				}
			}

			@Override
			public void dragFinished(DragSourceEvent event) {
				// TODO Auto-generated method stub
				log.debug("....... finished");
			}});
		
		
		ToolBar toolBar = new ToolBar(composite, SWT.FLAT | SWT.RIGHT);
		toolBar.setBounds(10, 10, 97, 25);
		final ToolItem add =new ToolItem(toolBar,SWT.PUSH);
		add.setText("添加");
		add.setImage(ImageFactory.loadImage(toolBar.getDisplay(), ImageFactory.ADD_OBJ));
		
		
		TabItem item1=new TabItem(tabFolder,SWT.NONE);
		item1.setText("SQL");
		
		Composite composite_1 = new Composite(tabFolder, SWT.NONE);
		item1.setControl(composite_1);
		
		final Button btnNewButton_2 = new Button(composite_1, SWT.NONE);

		btnNewButton_2.setBounds(10, 81, 108, 63);
		btnNewButton_2.setText("Hive Hql");
		btnNewButton_2.setData("value", "1\001Q\001Hive Hql\001H\00110.71.84.233,10000");
		btnNewButton_2.setImage(ImageFactory.loadImage(toolBar.getDisplay(), "\\icons\\eclipse_icons\\defcon_wiz(1).gif"));
		
		
		
		DragSource dragSource_3 = new DragSource(btnNewButton_2, DND.DROP_MOVE|DND.DROP_COPY);
		
		dragSource_3.setTransfer(new Transfer[]{TextTransfer.getInstance()});
		dragSource_3.addDragListener(new DragSourceListener(){

			@Override
			public void dragStart(DragSourceEvent event) {
				// TODO Auto-generated method stub
				if(btnNewButton_2.forceFocus()==false)
				{
					event.doit=false;
					System.out.println("....... forceFocus false");
				}
				System.out.println("....... start");
			}

			@Override
			public void dragSetData(DragSourceEvent event) {
				// TODO Auto-generated method stub
				if(TextTransfer.getInstance().isSupportedType(event.dataType))
				{
					event.data=btnNewButton_2.getData("value");
					System.out.println("....... set value");
				}
			}

			@Override
			public void dragFinished(DragSourceEvent event) {
				// TODO Auto-generated method stub
				System.out.println("....... finished");
			}});
		
		
		
		
		
		
		
		TabItem item2=new TabItem(tabFolder,SWT.NONE);
		item2.setText("输出格式");
		
		Composite composite_2 = new Composite(tabFolder, SWT.NONE);
		item2.setControl(composite_2);
		
		final Button btnNewButton_3 = new Button(composite_2, SWT.NONE);

		btnNewButton_3.setBounds(10, 81, 108, 63);
		btnNewButton_3.setText("EXCEL");
		btnNewButton_3.setData("value", "1\001O\001EXCEL\001E\001EXCEL");
		btnNewButton_3.setImage(ImageFactory.loadImage(toolBar.getDisplay(), "\\icons\\eclipse_icons\\up_nav(1).gif"));
		
		DragSource dragSource_4 = new DragSource(btnNewButton_3, DND.DROP_MOVE|DND.DROP_COPY);
		dragSource_4.setTransfer(new Transfer[]{TextTransfer.getInstance()});
		dragSource_4.addDragListener(new DragSourceListener(){

			@Override
			public void dragStart(DragSourceEvent event) {
				// TODO Auto-generated method stub
				if(btnNewButton_3.forceFocus()==false)
				{
					event.doit=false;
					System.out.println("....... forceFocus false");
				}
				System.out.println("....... start");
			}

			@Override
			public void dragSetData(DragSourceEvent event) {
				// TODO Auto-generated method stub
				if(TextTransfer.getInstance().isSupportedType(event.dataType))
				{
					event.data=btnNewButton_3.getData("value");
					System.out.println("....... set value");
				}
			}

			@Override
			public void dragFinished(DragSourceEvent event) {
				// TODO Auto-generated method stub
				System.out.println("....... finished");
			}});
		
		
		
		
		
		
		TabItem item3=new TabItem(tabFolder,SWT.NONE);
		item3.setText("图形化");
		
		TabItem item4=new TabItem(tabFolder,SWT.NONE);
		item4.setText("控制台");
		
		textConsole = new StyledText(tabFolder, SWT.BORDER|SWT.MULTI| SWT.WRAP|SWT.H_SCROLL|SWT.V_SCROLL);
		
		textConsole.setWordWrap(true);
		item4.setControl(textConsole);
		

		tabFolder.pack();
		
	}
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
