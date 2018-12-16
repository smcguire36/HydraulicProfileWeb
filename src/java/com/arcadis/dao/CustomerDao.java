/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arcadis.dao;

import com.arcadis.entities.Customer;
import java.io.Serializable;
import java.util.List;
import javax.inject.Named;
import javax.persistence.Query;

@Named
public class CustomerDao implements IDao<Customer, String>, Serializable {

    private DaoService service;
    
    public CustomerDao() {
    }
   
    public void setDaoService(DaoService service) {
        this.service= service;
    }

    @Override
    public void persist(Customer entity) {
        service.getEntityManager().persist(entity);
    }

    @Override
    public void merge(Customer entity) {
        service.getEntityManager().merge(entity);
    }

    @Override
    public void delete(Customer entity) {
        service.getEntityManager().remove(entity);
    }

    @Override
    public Customer findById(String id) {
        Query query = service.getEntityManager().createNamedQuery(Customer.QUERY_FIND_BY_CUSTOMER_ID).setParameter("customerId", id);
        Customer customer = (Customer) query.getSingleResult();
        return customer;
    }

    @Override
    public Customer findByName(String name) {
        Query query = service.getEntityManager().createNamedQuery(Customer.QUERY_FIND_BY_CUSTOMER_NAME).setParameter("customerName", name);
        Customer user = (Customer) query.getSingleResult();
        return user;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Customer> findAll() {
        List<Customer> users = (List<Customer>) service.getEntityManager().createNamedQuery(Customer.QUERY_FIND_ALL).getResultList();
        return users;
    }

    @Override
    public void deleteAll() {
        List<Customer> entityList = findAll();
        entityList.forEach((entity) -> {
            delete(entity);
        });
    }

    //=============================================================================================
    // The following methods are custom for the customer table
    //=============================================================================================
    public List<Customer> findByUser(String userId) {
        Query query = service.getEntityManager().createNamedQuery(Customer.QUERY_FIND_BY_CUSTOMER_USER).setParameter("userId", userId);
        List<Customer> users = (List<Customer>) query.getResultList();
        return users;
    }

}
