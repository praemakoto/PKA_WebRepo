package controller;

import model.UserInfo;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Textbox;

public class ConfirmController extends BaseController {


	private Textbox tbName,tbLName,tbEmail,tbTel,tbID;
	private Button bConfirm,bCancel; 
	private Combobox c = new Combobox();
	
	
	
	@Override
	protected void initBefore(Component comp) throws Exception {
		// TODO Auto-generated method stub
	}

	@Override
	protected void initAfter(Component comp) throws Exception {
		// TODO Auto-generated method stub
		initEvent();
//		java.util.Map<String,Object> arg =   (java.util.Map<String, Object>) Executions.getCurrent().getArg();
//		arg.put(key, value);
//		
//		Executions.getCurrent().createComponents("/comfirm.zul", arg);
	}
	
	
	public void initEvent(){
		bConfirm.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				UserInfo info = new UserInfo();
				info.setName(tbName.getValue());
				info.setId(Integer.parseInt(tbID.getValue()));
				info.setEmails(tbEmail.getValue());
				info.setTel(tbTel.getValue());
//				Executions.getCurrent().createComponents("/thank.zul", null);
			}
		
		});
		
		bCancel.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				Executions.getCurrent().sendRedirect("/flight_from.zul");
				
			}
		
		});
		
	}

}
