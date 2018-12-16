/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arcadis.dao;

import com.arcadis.entities.Users;
import java.io.Serializable;
import java.util.List;
import javax.inject.Named;
import javax.persistence.Query;

@Named
public class UserDao implements IDao<Users, String>, Serializable {

    private DaoService service;
    
    public UserDao() {
    }
    
    public void setDaoService(DaoService service) {
        this.service= service;
    }
    
    @Override
    public void persist(Users entity) {
        service.getEntityManager().persist(entity);
    }

    @Override
    public void merge(Users entity) {
        service.getEntityManager().merge(entity);
    }

    @Override
    public void delete(Users entity) {
        service.getEntityManager().remove(entity);
    }

    @Override
    public Users findById(String id) {
        Query query = service.getEntityManager().createNamedQuery(Users.QUERY_FIND_BY_USER_ID).setParameter("userId", id);
        Users user = (Users) query.getSingleResult();
        return user;
    }

    @Override
    public Users findByName(String name) {
        Query query = service.getEntityManager().createNamedQuery(Users.QUERY_FIND_BY_USERNAME).setParameter("username", name);
        Users user = (Users) query.getSingleResult();
        return user;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Users> findAll() {
        List<Users> users = (List<Users>) service.getEntityManager().createNamedQuery(Users.QUERY_FIND_ALL).getResultList();
        return users;
    }

    @Override
    public void deleteAll() {
        List<Users> entityList = findAll();
        entityList.forEach((entity) -> {
            delete(entity);
        });
    }

}
