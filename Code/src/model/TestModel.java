package model;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import utils.HibernateUtils;

public class TestModel {
	public static void main(String[] args) {
		// Retrieve Hibernate session
		Session s = HibernateUtils.getSession();

		// Start transaction
		Transaction t = s.beginTransaction();

		// Create Event object
		TermesDB terme1 = new TermesDB();
		terme1.setTerme("test");

		// Make it persistent
		s.save(terme1);

		TermesDB terme2 = new TermesDB();
		terme2.setTerme("test2");

		// Make it persistent
		s.save(terme2);
		
		TypesDB type = new TypesDB();
		type.setType("test type");
		type.setXpath("/path/cool");
		
		s.save(type);
		
		ContenirTermeDB ct = new ContenirTermeDB();
		ct.setFrequence(1);
		ct.setIdTerme(terme1.getId());
		ct.setIdTypes(type.getId());
		
		s.save(ct);
		
		// End of transaction
		// send changes to database
		t.commit();
		
		org.hibernate.Query q = s.createQuery("from ContenirTermeDB");
		List<ContenirTermeDB> results = q.list();
		for (ContenirTermeDB ttt : results)
			System.out.println(ttt.getId());
		// Close Hibernate session
		s.close();
	}

}
