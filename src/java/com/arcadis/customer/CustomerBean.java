/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arcadis.customer;

import com.arcadis.dao.CustomerDao;
import com.arcadis.dao.DaoService;
import com.arcadis.entities.Customer;
import com.arcadis.profile.UserManager;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.inject.spi.CDI;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

/**
 *
 * @author mcguire
 */
@Named(value = "customerBean")
@SessionScoped
public class CustomerBean implements Serializable {

    private String customerId;
    private List<Customer> customers;

    @Inject
    private UserManager userMgr;

    /**
     * Creates a new instance of CustomerBean
     */
    public CustomerBean() {
        customers = new ArrayList<Customer>();
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public void clearCustomers() {
        customers.clear();
    }
    
    public List<Customer> getCustomers() {
        if (customers.isEmpty()) {
            loadCustomerList();
        }
        return customers;
    }

    private void loadCustomerList() {
        // Manually inject customerDAO object into method
        DaoService service =CDI.current().select(DaoService.class).get();
        CustomerDao customerDao = CDI.current().select(CustomerDao.class).get();
        customerDao.setDaoService(service);

        try {
            service.BeginTransaction();
            try {

                customers = customerDao.findByUser(userMgr.getUserId());

                service.CommitTransaction();
            } finally {
                if (!service.isCommitted()) {
                    service.RollbackTransaction();
                }
            }
        } catch (Exception ex) {
            Logger.getLogger("com.arcadis").log(Level.SEVERE, "Available customers list load failed", ex);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Available customers list load failed: " + ex.getMessage(), ""));
        } finally {
            service.closeEntityManager();
        }

    }

}
