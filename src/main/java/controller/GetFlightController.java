package controller;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Textbox;

public class GetFlightController extends BaseController {

	private Button bb;
	private Textbox aa;
	
	@Override
	protected void initBefore(Component comp) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initAfter(Component comp) throws Exception {
		// TODO Auto-generated method stub
		initEvent();
	}

	public void initEvent() {
		bb.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				// TODO Auto-generated method stub
				aa.setValue("99999999999999999");
			}
		});
	}

}
