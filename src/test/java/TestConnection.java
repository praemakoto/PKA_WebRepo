import org.hibernate.Session;

import utils.HibernateUtil;


public class TestConnection {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Session s =  HibernateUtil.currentSession();
		System.out.println("Hibernate isConnected : "+s.isConnected());
	}

}
