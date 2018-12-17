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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.inject.spi.CDI;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;

/**
 *
 * @author mcguire
 */
@Named(value = "customerListBean")
@ViewScoped
public class CustomerListBean implements Serializable {

    private List<Customer> customers;

    @Inject
    private UserManager userMgr;

    /**
     * Creates a new instance of CustomerBean
     */
    public CustomerListBean() {
        customers = new ArrayList<Customer>();
    }

        @PostConstruct
    public void InitializeBean() {
        loadCustomerList();
    }

    public List<Customer> getCustomers() {
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

                customers = customerDao.findAll();

                service.CommitTransaction();
            } finally {
                if (!service.isCommitted()) {
                    service.RollbackTransaction();
                }
            }
        } catch (Exception ex) {
            Logger.getLogger("com.arcadis").log(Level.SEVERE, "Customer list load failed", ex);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Customer list load failed: " + ex.getMessage(), ""));
        } finally {
            service.closeEntityManager();
        }

    }

}
