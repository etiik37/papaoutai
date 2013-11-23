package utils;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

public class HibernateUtils {
	private static final SessionFactory sessionFactory;

	// Creates a unique instance of SessionFactory from hibernate.cfg.xml
	static {
		try {
			sessionFactory = new AnnotationConfiguration().configure()
					.buildSessionFactory();
		} catch (HibernateException ex) {
			throw new RuntimeException("Problème de configuration : "
					+ ex.getMessage(), ex);
		}
	}

	// Returns a Hibernate session
	public static Session getSession() throws HibernateException {
		return sessionFactory.openSession();
	}
}
