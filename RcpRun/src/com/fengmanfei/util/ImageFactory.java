package com.fengmanfei.util;

import java.util.Enumeration;
import java.util.Hashtable;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class ImageFactory {

	
	private ImageFactory() {}

	public static final String REAL_PATH = System.getProperty("user.dir");
  
	public static final String DELETE_EDIT ="\\icons\\full\\etool16\\"+ "delete_edit.gif";

	public static final String SAVE_EDIT ="\\icons\\full\\etool16\\"+ "save_edit.gif";

	public static final String SCOPY_EDIT = "\\icons\\full\\etool16\\"+"copy_edit.gif";

	public static final String CUT_EDIT = "\\icons\\full\\etool16\\"+"cut_edit.gif";

	public static final String PRINT_EDIT = "\\icons\\full\\etool16\\"+"print_edit.gif";

	public static final String HELP_CONTENTS = "\\icons\\full\\etool16\\"+"help_contents.gif";
	
	public static final String ECLIPSE = "\\icons\\full\\etool16\\"+"eclipse.gif";
	
	public static final String SAMPLES ="\\icons\\full\\etool16\\"+ "samples.gif";
	public static final String ADD_OBJ = "\\icons\\full\\etool16\\"+"add_obj.gif";
	public static final String DELETE_OBJ = "\\icons\\full\\etool16\\"+"delete_obj.gif";
	
	public static final String BACKWARD_NAV ="\\icons\\full\\etool16\\"+ "backward_nav.gif";
	public static final String FORWARD_NAV = "\\icons\\full\\etool16\\"+"forward_nav.gif";
	public static final String ICON_GIRL ="\\icons\\full\\etool16\\"+ "girl.gif";
	public static final String ICON_BOY = "\\icons\\full\\etool16\\"+"boy.gif";
	
	public static final String TOC_CLOSED = "\\icons\\full\\etool16\\"+"toc_closed.gif";
	public static final String TOC_OPEN = "\\icons\\full\\etool16\\"+"toc_open.gif";
	public static final String TOPIC ="\\icons\\full\\etool16\\"+ "topic.gif";
	
	public static final String UNDERLIN = "\\icons\\full\\etool16\\"+"underline.gif";
	public static final String ITALIC = "\\icons\\full\\etool16\\"+"italic.gif";
	public static final String BOLD = "\\icons\\full\\etool16\\"+"bold.gif";
	public static final String BGCOLOR ="\\icons\\full\\etool16\\"+ "bgcol.gif";
	public static final String FORCOLOR ="\\icons\\full\\etool16\\"+ "forecol.gif";
	public static final String PROGRESS_TASK ="\\icons\\full\\etool16\\"+ "progress_task.gif";
	public static final String SAMPLE_ICON = "\\icons\\full\\etool16\\"+"sample_icon.gif";
	public static final String FILE = "\\icons\\full\\etool16\\"+"file.gif";
	public static final String FOLDER = "\\icons\\full\\etool16\\"+"folder.gif";
  
	private static Hashtable htImage = new Hashtable();
   
	public static Image loadImage(Display display, String imageName) {
		Image image = (Image) htImage.get(imageName.toUpperCase());
		if (image == null) {
			image = new Image(display, REAL_PATH + imageName);
			htImage.put(imageName.toUpperCase(), image);
		}
		return image;
	}

	//�ͷ�htImage�е����е�ͼƬ��Դ
	public static void dispose() {
		Enumeration e = htImage.elements();
		while (e.hasMoreElements()) {
			Image image = (Image) e.nextElement();
			if (!image.isDisposed())
				image.dispose();
		}
	}
}
