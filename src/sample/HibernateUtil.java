package sample;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static SessionFactory sessionFactory;

    private static SessionFactory buildSessionFactory() {
        try {
            Configuration configuration = new Configuration();
            configuration.configure("hibernate.cfg.xml");

            return new Configuration().configure().buildSessionFactory();

        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        if(sessionFactory == null){
            sessionFactory = buildSessionFactory();
        }
        return sessionFactory;
    }

    public static void shutdown() {
        getSessionFactory().close();
    }

    public static void create(Object o) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        try {
            session.persist(o);
        } catch (HibernateException e) {
            System.out.println("Create Error");
            e.getStackTrace();
        }
        tx.commit();
        session.close();
    }

    public static void update(Object o) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        try {
            //session.merge(o);
            session.update(o);
        } catch (HibernateException e) {
            System.out.println("Update Error");
        }
        tx.commit();
        session.close();
    }

    public static void delete(Object o) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        try {
            session.delete(o);
        } catch (HibernateException e) {
            System.out.println("Delete Error");
        }

        tx.commit();
        session.close();
    }
}
