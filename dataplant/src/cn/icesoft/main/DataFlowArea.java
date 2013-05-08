package cn.icesoft.main;


import java.util.UUID;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.fengmanfei.util.ImageFactory;

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
		
		final Composite composite = new Composite(this, SWT.NONE);
		composite.setBounds(5, 5, (int)(area.width*0.7), (int)(area.height*0.7));
		formToolkit.adapt(composite);
		formToolkit.paintBordersFor(composite);
		
		DropTarget dropTarget = new DropTarget(composite,DND.DROP_DEFAULT | DND.DROP_COPY);
		dropTarget.setTransfer(new Transfer[]{TextTransfer.getInstance()});
		
	
		
		dropTarget.addDropListener(new DropTargetListener(){

			@Override
			public void dragEnter(DropTargetEvent event) {
				// TODO Auto-generated method stub
				if(event.detail==DND.DROP_DEFAULT)event.detail= DND.DROP_COPY;
			}

			@Override
			public void dragLeave(DropTargetEvent event) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void dragOperationChanged(DropTargetEvent event) {
				// TODO Auto-generated method stub
				if(event.detail==DND.DROP_DEFAULT)event.detail= DND.DROP_COPY;
			}

			@Override
			public void dragOver(DropTargetEvent event) {
				// TODO Auto-generated method stub
				event.feedback=DND.FEEDBACK_EXPAND|DND.FEEDBACK_SELECT;
				
			}

			@Override
			public void drop(DropTargetEvent event) {
				// TODO Auto-generated method stub
				System.out.println("drop begin:"+event.data);
				final Button btnNewButton;
				btnNewButton = new Button(composite, SWT.NONE);
				btnNewButton.setBounds(event.x, event.y-63, 108, 63);
				formToolkit.adapt(btnNewButton, true, true);
				
				UUID uuid = UUID.randomUUID();
				btnNewButton.setText(((String)event.data).split("\001")[2]);
				btnNewButton.setData("objectID", uuid);
				
				if(((String)event.data).split("\001")[1].equals("Q"))
				{
					btnNewButton.setImage(ImageFactory.loadImage(composite.getDisplay(), "\\icons\\eclipse_icons\\defcon_wiz(1).gif"));
				}
				//memory_view.gif
				if(((String)event.data).split("\001")[1].equals("S"))
				{
					btnNewButton.setImage(ImageFactory.loadImage(composite.getDisplay(), "\\icons\\eclipse_icons\\memory_view.gif"));
				}
				
				if(event.data!=null&&((String)event.data).substring(0,1).equals("1"))
				{
					
					btnNewButton.setData("value", "0"+((String)event.data).substring(1,((String)event.data).length()));
					
					
				}
				else
				{
					btnNewButton.setData("value",((String)event.data).split("\010")[0]);
					
					
					for(int i=0;i<composite.getChildren().length;i++)
					{
						UUID tmp=(UUID)composite.getChildren()[i].getData("objectID");
						
						System.out.println("当前存在的UUID  "+tmp.toString());
						
						if(tmp.toString().equals(((String)event.data).split("\10")[1]))
						{
							composite.getChildren()[i].setVisible(false);
						}
					}
					
				}
				
				
				DragSource dragSource = new DragSource(btnNewButton, DND.DROP_MOVE|DND.DROP_COPY);
				dragSource.setTransfer(new Transfer[]{TextTransfer.getInstance()});
				dragSource.addDragListener(new DragSourceListener(){

					@Override
					public void dragStart(DragSourceEvent dragevent) {
						// TODO Auto-generated method stub
						
						System.out.println("....... start");
					}

					@Override
					public void dragSetData(DragSourceEvent dragevent) {
						// TODO Auto-generated method stub
						dragevent.data=btnNewButton.getData("value")+"\10"+btnNewButton.getData("objectID");
						
					}

					@Override
					public void dragFinished(DragSourceEvent dragevent) {
						// TODO Auto-generated method stub
						System.out.println("....... finished");
					}});
				
				
			}

			@Override
			public void dropAccept(DropTargetEvent event) {
				// TODO Auto-generated method stub
				
			}});
		

	
		
		
		
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
