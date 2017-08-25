package controller;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Textbox;

import model.Airport;

public class GetFlightController extends BaseController {

	private Button search;
	private Combobox source;
	private Combobox destination;
	
	@Override
	protected void initBefore(Component comp) throws Exception {
		

	}

	@Override
	protected void initAfter(Component comp) throws Exception {
		// TODO Auto-generated method stub
		initEvent();
	}

	public void initEvent() {
		search.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				// TODO Auto-generated method stub
				source.setValue("someting");
			}
		});
	}

}
