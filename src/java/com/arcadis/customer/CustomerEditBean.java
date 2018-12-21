/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arcadis.customer;

import com.arcadis.dao.CustomerDao;
import com.arcadis.dao.DaoService;
import com.arcadis.dao.UserDao;
import com.arcadis.entities.Customer;
import com.arcadis.entities.Users;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.enterprise.inject.spi.CDI;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author mcguire
 */
@Named(value = "customerEditBean")
@ViewScoped
public class CustomerEditBean implements Serializable {

    private String customerId;
    private Customer customer;
    private boolean readonly;

    private String addUserId;
    private List<Users> nonCustomerUsers;

    /**
     * Creates a new instance of CustomerEditBean
     */
    public CustomerEditBean() {
    }

    @PostConstruct
    public void InitializeBean() {
        // Load URL request parameters into bean
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
        customerId = params.get("customerId");

        // Load the customer record from the database
        DaoService service = CDI.current().select(DaoService.class).get();
        CustomerDao customerDao = CDI.current().select(CustomerDao.class).get();
        customerDao.setDaoService(service);

        try {
            service.BeginTransaction();
            try {

                customer = customerDao.findById(customerId);
                if (customer == null) {
                    throw new Exception("Customer data not found.");
                }
//                customerName = customer.getCustomerName();
//                customerUsers = new ArrayList<Users>();
//                customerUsers.addAll(customer.getUsers());

                service.CommitTransaction();
            } finally {
                if (!service.isCommitted()) {
                    service.RollbackTransaction();
                }
            }
        } catch (Exception ex) {
            readonly = true;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error loading customer data: " + ex.getMessage(), ""));
        } finally {
            service.closeEntityManager();
        }

        // Initialize the NonCustomerUsers
        nonCustomerUsers = new ArrayList<Users>();
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customer.getCustomerName();
    }

    public void setCustomerName(String customerName) {
        customer.setCustomerName(customerName);
    }

    public List<Users> getCustomerUsers() {
        List<Users> users = new ArrayList<Users>();
        users.addAll(customer.getUsers());
        return users;
    }

    public String getAddUserId() {
        return addUserId;
    }

    public void setAddUserId(String addUserId) {
        this.addUserId = addUserId;
    }

    public List<Users> getNonCustomerUsers() {
        if (nonCustomerUsers.isEmpty()) {
            // Try to load user list from the database
            DaoService service = CDI.current().select(DaoService.class).get();
            UserDao userDao = CDI.current().select(UserDao.class).get();
            userDao.setDaoService(service);

            try {
                service.BeginTransaction();
                try {
                    // This needs to be modified to eliminate from the list all the existing users in this customer account
                    nonCustomerUsers = userDao.findAll();

                    service.CommitTransaction();
                } finally {
                    if (!service.isCommitted()) {
                        service.RollbackTransaction();
                    }
                }
            } catch (Exception ex) {
                readonly = true;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error loading customer data: " + ex.getMessage(), ""));
            } finally {
                service.closeEntityManager();
            }
        }
        return nonCustomerUsers;
    }

    public boolean isReadonly() {
        return readonly;
    }

    public String saveCustomer() {
        // Save the customer data        
        DaoService service = CDI.current().select(DaoService.class).get();
        CustomerDao customerDao = CDI.current().select(CustomerDao.class).get();
        customerDao.setDaoService(service);
        try {
            service.BeginTransaction();
            try {

                customerDao.merge(customer);

                service.CommitTransaction();
            } finally {
                if (!service.isCommitted()) {
                    service.RollbackTransaction();
                }
            }
        } catch (Exception ex) {
            readonly = true;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error saving customer data: " + ex.getMessage(), ""));
            return null;
        } finally {
            service.closeEntityManager();
        }

        // If all is well, return to the list page
        return "/customer/list?faces-redirect=true";
    }

    public String addUser() {
        Iterator<Users> userIterator = nonCustomerUsers.iterator();
        while (userIterator.hasNext()) {
            Users u = userIterator.next();
            if (addUserId.equals(u.getUserId())) {
                customer.getUsers().add(u);
                break;
            }
        }
        return null;
    }

    public String removeUser() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String userId = params.get("userId");

        Iterator<Users> userIterator = customer.getUsers().iterator();
        while (userIterator.hasNext()) {
            Users u = userIterator.next();
            if (userId.equals(u.getUserId())) {
                customer.getUsers().remove(u);
            }
        }
        return null;
    }

}
