package cn.icesoft.main;


import java.util.LinkedList;
import java.util.UUID;

import org.apache.log4j.Logger;
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
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.fengmanfei.util.ImageFactory;

public class DataFlowArea extends Composite {
	static Logger log = Logger.getLogger(DataFlowArea.class);//log4j的日志文件
	Rectangle area = Display.getDefault().getClientArea();
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	
	private LinkedList linklist = new LinkedList();
	
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
				final GC gc=new GC(composite);
				gc.setAdvanced(true);
				
				
				
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
					log.debug("set value detail.... for drog"+((String)event.data).split("\010")[0]);
					log.debug("get pre objectID .... for drog"+((String)event.data).split("\010")[1]);
					
					btnNewButton.setData("value",((String)event.data).split("\010")[0]);
					if(btnNewButton.getData("value").toString().split("\001").length==6)
					{
						
						String[] contents=btnNewButton.getData("value").toString().split("\001")[5].split(" ");
						String[]  btnNewButtons=btnNewButton.getData("value").toString().split("\001");
						btnNewButton.setData("value",btnNewButtons[0]+"\001"+
								btnNewButtons[1]+"\001"+
								btnNewButtons[2]+"\001"+
								btnNewButtons[3]+"\001"+
								btnNewButtons[4]);
						
						
						for(int i=0;i<contents.length;i++)
						{
							String tmp=contents[i];
							if(tmp.split(":").length==2&&tmp.split(":")[0].equals("sendLineToObjectID"))
							{
								btnNewButton.setData("sendLineToObjectID",tmp.split(":")[1]);
								
							}
							else if(tmp.split(":").length==2&&tmp.split(":")[0].equals("recieveLineToObjectID"))
							{
								btnNewButton.setData("recieveLineToObjectID",tmp.split(":")[1]);
								
								
							}
							
						}
						//reflash link id
						for(int j=0;j<composite.getChildren().length;j++)
						{
							//問題
							Control ctl_source=composite.getChildren()[j];
							if(ctl_source.getData("sendLineToObjectID")!=null&&ctl_source.getData("sendLineToObjectID").equals(((String)event.data).split("\010")[1]))
							{
								ctl_source.setData("sendLineToObjectID", uuid.toString());
							}
							else if(ctl_source.getData("recieveLineToObjectID")!=null&&ctl_source.getData("recieveLineToObjectID").toString().equals(((String)event.data).split("\010")[1]))
							{
								ctl_source.setData("recieveLineToObjectID", uuid.toString());
							}
							
						}
								
					}
					
					relink(gc,composite,btnNewButton);
				}
				
				
				DragSource dragSource = new DragSource(btnNewButton, DND.DROP_MOVE|DND.DROP_COPY);
				dragSource.setTransfer(new Transfer[]{TextTransfer.getInstance()});
				dragSource.addDragListener(new DragSourceListener(){

					@Override
					public void dragStart(DragSourceEvent dragevent) {
						// TODO Auto-generated method stub
						composite.pack();
						composite.setBounds(5, 5, (int)(area.width*0.7), (int)(area.height*0.7));
						formToolkit.adapt(composite);
						formToolkit.paintBordersFor(composite);
						log.debug("....... start");
					}

					@Override
					public void dragSetData(DragSourceEvent dragevent) {
						// TODO Auto-generated method stub
						String firstcontent=btnNewButton.getData("value").toString();
						String tmp="";
						if(btnNewButton.getData("recieveLineToObjectID")!=null)
						{
							tmp+="recieveLineToObjectID:"+btnNewButton.getData("recieveLineToObjectID").toString()+" ";
							
						}
						if(btnNewButton.getData("sendLineToObjectID")!=null)
						{
							tmp+="sendLineToObjectID:"+btnNewButton.getData("sendLineToObjectID").toString()+" ";
						}
						if(!tmp.equals(""))
						{
							firstcontent+="\001"+tmp.trim();
						}
						
						dragevent.data=firstcontent+"\010"+btnNewButton.getData("objectID");
						log.debug("set data detail.... for drag"+dragevent.data);
						
					}

					@Override
					public void dragFinished(DragSourceEvent dragevent) {
						// TODO Auto-generated method stub
						log.debug("....... finished");
						btnNewButton.dispose();
						
					}

					});
				
				
				//write mouse event 
				
				btnNewButton.addMouseListener(new MouseListener(){

					
					String selectObjectUUId="";
					
					@Override
					public void mouseDoubleClick(MouseEvent e) {
						// TODO Auto-generated method stub
						if(selectObjectUUId.equals(""))
						{
							MessageBox messageBox =
								    new MessageBox(composite.getShell(),SWT.ICON_WARNING);
								messageBox.setMessage("www.korayguclu.de");
								messageBox.open(); 
						}
						
						log.debug("mouseDoubleClick selectObjectUUId:"+selectObjectUUId);
						
						while(linklist.isEmpty()==false)
						{
							
							if(linklist.getFirst().toString().equals(((UUID)btnNewButton.getData("objectID")).toString()))
							{
								linklist.removeFirst();
							}
							else
							{
								log.debug("begin draw line from "+((UUID)btnNewButton.getData("objectID")).toString()+"   to   "+linklist.getFirst().toString());
								//设置个对象的  来源  与  发送 
								
								Control ctl_source;
								Control ctl_target=btnNewButton;
								
								
								for(int i=0;i<composite.getChildren().length;i++)
								{
									Control ctl=composite.getChildren()[i];
									if(((UUID)ctl.getData("objectID")).toString().equals(linklist.getFirst().toString()))
									{
										ctl_source=ctl;
										ctl_source.setData("sendLineToObjectID",btnNewButton.getData("objectID"));
										ctl_target.setData("recieveLineToObjectID",ctl_source.getData("objectID"));
										
										gc.setLineWidth(2);
										paintk(gc,ctl_source.getBounds().x+108,ctl_source.getBounds().y+31,ctl_target.getBounds().x,ctl_target.getBounds().y+31);
										
										
										break;
									}
									log.debug("child detail...."+((UUID)ctl.getData("objectID")).toString());
								}
								
								//开始绘图
								
								
								break;
							}
							
							
						}
						
						
						linklist.clear();
						
					}

					@Override
					public void mouseDown(MouseEvent e) {
						
						// TODO Auto-generated method stub
						selectObjectUUId=((UUID)btnNewButton.getData("objectID")).toString();
						log.debug("mouse down selectObjectUUId:"+selectObjectUUId);
						linklist.addFirst(selectObjectUUId);
								
					}

					@Override
					public void mouseUp(MouseEvent e) {
						// TODO Auto-generated method stub
						
					}});
				
				//设置键盘事件
				btnNewButton.addKeyListener(new KeyListener(){

					@Override
					public void keyPressed(KeyEvent e) {
						// TODO Auto-generated method stub
						if(e.keyCode==127)//delete key
						{
							btnNewButton.dispose();
						}
					}

					@Override
					public void keyReleased(KeyEvent e) {
						// TODO Auto-generated method stub
						
					}});
				
			

			
					
				
				
			}

			@Override
			public void dropAccept(DropTargetEvent event) {
				// TODO Auto-generated method stub
				
			}});
		
			
	
		
		
		
	}

	private void relink(GC gc, Composite composite,Button boo) {
		
			
			
			for(int i=0;i<composite.getChildren().length;i++)
			{
				Control ctl_source=composite.getChildren()[i];
				if(ctl_source.getData("sendLineToObjectID")!=null&&!(ctl_source.getData("sendLineToObjectID")).toString().equals(""))
				{
					Control ctl_target;
					
					for(int j=0;j<composite.getChildren().length;j++)
					{
						
						ctl_target=composite.getChildren()[j];
						
						if(ctl_target.getData("recieveLineToObjectID")!=null
								&&!ctl_target.getData("recieveLineToObjectID").toString().equals("")
								&&ctl_target.getData("recieveLineToObjectID").toString().equals(((UUID)ctl_source.getData("objectID")).toString()))
						{
							
							gc.setLineWidth(2);
							paintk(gc,ctl_source.getBounds().x+108,ctl_source.getBounds().y+31,ctl_target.getBounds().x,ctl_target.getBounds().y+31);
							
							
							
							break;
							
						}
						
							
						
					}
				
					
				}
				log.debug("child detail...."+((UUID)ctl_source.getData("objectID")).toString());
			}
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
	
	 public   void  paintk(GC g,  int  x1,  int  y1,  int  x2,  int  y2)  {

         double  H  =   10 ;  // 箭头高度   
         double  L  =   7 ; // 底边的一半  
         int  x3  =   0 ;
         int  y3  =   0 ;
         int  x4  =   0 ;
         int  y4  =   0 ;
         double  awrad  =  Math.atan(L  /  H);  // 箭头角度   
         double  arraow_len  =  Math.sqrt(L  *  L  +  H  *  H); // 箭头的长度   
         double [] arrXY_1  =  rotateVec(x2  -  x1, y2  -  y1, awrad,  true , arraow_len);
         double [] arrXY_2  =  rotateVec(x2  -  x1, y2  -  y1,  - awrad,  true , arraow_len);
         double  x_3  =  x2  -  arrXY_1[ 0 ];  // (x3,y3)是第一端点   
         double  y_3  =  y2  -  arrXY_1[ 1 ];
         double  x_4  =  x2  -  arrXY_2[ 0 ]; // (x4,y4)是第二端点   
         double  y_4  =  y2  -  arrXY_2[ 1 ];

        Double X3  =   new  Double(x_3);
        x3  =  X3.intValue();
        Double Y3  =   new  Double(y_3);
        y3  =  Y3.intValue();
        Double X4  =   new  Double(x_4);
        x4  =  X4.intValue();
        Double Y4  =   new  Double(y_4);
        y4  =  Y4.intValue();
         // g.setColor(SWT.COLOR_WHITE);
         // 画线
        g.drawLine(x1, y1, x2, y2);
         // 画箭头的一半
        g.drawLine(x2, y2, x3, y3);
         // 画箭头的另一半
        g.drawLine(x2, y2, x4, y4);

    } 
	 
	 public   double [] rotateVec( int  px,  int  py,  double  ang,  boolean  isChLen,
             double  newLen)  {

         double  mathstr[]  =   new   double [ 2 ];
         // 矢量旋转函数，参数含义分别是x分量、y分量、旋转角、是否改变长度、新长度   
         double  vx  =  px  *  Math.cos(ang)  -  py  *  Math.sin(ang);
         double  vy  =  px  *  Math.sin(ang)  +  py  *  Math.cos(ang);
         if  (isChLen)  {
             double  d  =  Math.sqrt(vx  *  vx  +  vy  *  vy);
            vx  =  vx  /  d  *  newLen;
            vy  =  vy  /  d  *  newLen;
            mathstr[ 0 ]  =  vx;
            mathstr[ 1 ]  =  vy;
        }
         return  mathstr;
    } 
}
