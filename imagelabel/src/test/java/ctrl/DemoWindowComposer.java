package ctrl;

import org.zkoss.imagelabel.ImageLabel;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.event.MouseEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;

public class DemoWindowComposer extends GenericForwardComposer {

	private static final long serialVersionUID = 20110728175153L;
	private ImageLabel myComp;
	
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		myComp.setLabel("imageWithLabel");
		myComp.setImage("image/bird6.gif");
	}
	
	public void onClick$myComp (ForwardEvent event) {
		MouseEvent mouseEvent = (MouseEvent) event.getOrigin();
		alert("You listen onClick: " + mouseEvent.getTarget());
	}
}