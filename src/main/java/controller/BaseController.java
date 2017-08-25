package controller;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.util.GenericForwardComposer;

public abstract class BaseController extends GenericForwardComposer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);
		initAfter(comp);
		
	}

	@Override
	public void doBeforeComposeChildren(Component comp) throws Exception
	{
		super.doBeforeComposeChildren(comp);
		initBefore(comp);
	}
	
	  

	protected abstract void initBefore(Component comp) throws Exception;

	protected abstract void initAfter(Component comp) throws Exception;

}
