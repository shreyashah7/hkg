/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.sync.impl;

import com.argusoft.hkg.sync.HkSyncSqlGenericService;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.stereotype.Service;

/**
 *
 * @author akta
 */
@Service
public class HkSyncSqlGenericServiceImpl implements HkSyncSqlGenericService {

    @Autowired
    LocalSessionFactoryBean sessionFactory;

    @Override
    public void updateEntity(Object hkNotificationRecipientEntity) {
        SessionFactory sessionFactoryObj = sessionFactory.getObject();
        Session openSession;
        try {
            openSession = sessionFactoryObj.getCurrentSession();
        } catch (Exception e) {
            openSession = sessionFactoryObj.openSession();
        }
        Transaction tx = null;
        try {
            tx = openSession.beginTransaction();
            openSession.saveOrUpdate(hkNotificationRecipientEntity);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            openSession.close();
        }

    }

}
