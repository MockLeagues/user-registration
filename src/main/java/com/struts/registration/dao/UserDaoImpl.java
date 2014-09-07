package com.struts.registration.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.struts.registration.domain.User;
import com.struts.registration.exception.ApplicationException;
import com.struts.registration.utils.HibernateUtil;

public class UserDaoImpl implements UserDao {
    private Log log = LogFactory.getLog(UserDaoImpl.class);

    @Override
    public User findById(Long id) {
        User user = null;
        try {
            Session session = HibernateUtil.currentSession();
            user = (User) session.get(User.class, id);
        } catch (HibernateException e) {
            log.error("No User found with id " + id, e);
            throw new ApplicationException("No User found with id " + id, e);
        } finally {
            HibernateUtil.closeSession();
        }
        return user;
    }

    @Override
    public List<User> findAll() {
        Session session = HibernateUtil.currentSession();
        Criteria criteria = session.createCriteria(User.class);
        return criteria.list();
    }

    @Override
    public User save(User entity) {
        Session session = HibernateUtil.currentSession();
        Transaction txn = null;
        try {
            txn = session.beginTransaction();
            session.saveOrUpdate(entity);
            txn.commit();
        } catch (HibernateException e) {
            throw new ApplicationException("User " + entity.getFirstName() + " unable to save", e);
        } finally {
            if (txn != null) {
                txn.rollback();
            }
            HibernateUtil.closeSession();
        }
        return entity;
    }

    @Override
    public void delete(User entity) {
        Session session = HibernateUtil.currentSession();
        Transaction txn = null;
        try {
            txn = session.beginTransaction();
            session.delete(entity);
            txn.commit();
        } catch (HibernateException e) {
            throw new ApplicationException("User " + entity.getFirstName() + " unable to delete", e);
        } finally {
            if (txn != null) {
                txn.rollback();
            }
            HibernateUtil.closeSession();
        }
    }
}