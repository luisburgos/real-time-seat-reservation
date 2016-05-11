/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.data;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 *
 * @author luisburgos
 */
public class SessionGenerator {
    private static final SessionFactory sessionFactory;

    static {
        try {
            sessionFactory = new Configuration().configure().buildSessionFactory();                                     
        } catch (HibernateException hibernateExcepcion) {
            hibernateExcepcion.printStackTrace();
            System.err.println("Error on SessionFactory initialization. " + hibernateExcepcion);
            throw new ExceptionInInitializerError(hibernateExcepcion);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}

