/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arcadis.dao;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

@Named
public class DaoService implements Serializable {

    @PersistenceUnit(unitName = "HydraulicProfileWebPU")
    private EntityManagerFactory factory;
    private EntityManager entityManager;

    @Resource
    private UserTransaction utx;
    
    private boolean committed;

    public DaoService() {
        committed = false;
    }

    @PostConstruct
    private void createEntityManager() {
        entityManager = factory.createEntityManager();
    }
    
    public boolean isCommitted() {
        return committed;
    }

    public EntityManagerFactory getEntityManagerFactory() {
        return factory;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void closeEntityManager() {
        entityManager.close();
    }

    public void BeginTransaction() throws NotSupportedException, SystemException {
        utx.begin();
        committed = false;
        try {
            entityManager.joinTransaction();
        } catch (Exception ex) {
            utx.rollback();
            throw ex;
        }
    }

    public void CommitTransaction() throws RollbackException, HeuristicMixedException, HeuristicRollbackException, SecurityException, IllegalStateException, SystemException {
        utx.commit();
        committed = true;
    }

    public void RollbackTransaction() throws NotSupportedException, SystemException {
        utx.rollback();
    }

}
