package com.arcadis.dao;

import java.io.Serializable;
import java.util.List;

public interface IDao<T, Id extends Serializable> {

	public void persist(T entity);
	
	public void merge(T entity);
	
	public T findById(Id id);
	
	public T findByName(String name);

        public void delete(T entity);
	
	public List<T> findAll();
	
	public void deleteAll();
	
}