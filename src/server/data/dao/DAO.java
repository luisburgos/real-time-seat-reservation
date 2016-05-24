/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.data.dao;

import server.data.SessionGenerator;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.sql.SQLException;
import java.util.ArrayList;
/**
 *
 * @author luisburgos
 */
public abstract class DAO<Entity> {
    protected Session session;
    protected Transaction transaction;

    protected void openSession() throws HibernateException {
        session = SessionGenerator.getSessionFactory().openSession();
        transaction = session.beginTransaction();              
    }

    protected void exceptionManagement(HibernateException hibernateException) 
            throws HibernateException {
        transaction.rollback();
        throw new HibernateException(
                "Error on data access", 
                hibernateException
        );
    }

    public abstract void add(Entity entity) throws SQLException;

    public abstract void delete(Entity entity);

    public abstract void update(Entity entity);

    public abstract Entity get(int objectId);

    public abstract ArrayList<Entity> getList();


    protected void saveEntity(Entity entity) {
        try {
            openSession();
            session.save(entity);
            transaction.commit();
        } catch (HibernateException hibernateException) {
            exceptionManagement(hibernateException);
            throw hibernateException;
        } finally {
            session.close();
        }
    }

    protected void deleteEntity(Entity entity) {
        try {
            openSession();
            session.delete(entity);
            transaction.commit();
        } catch (HibernateException hibernateException) {
            exceptionManagement(hibernateException);
            throw hibernateException;
        } finally {
            session.close();
        }
    }

    protected void updateEntity(Entity entity) {
        try {
            openSession();
            session.update(entity);
            transaction.commit();
        } catch (HibernateException hibernateException) {
            exceptionManagement(hibernateException);
            throw hibernateException;
        } finally {
            session.close();
        }
    }


}
