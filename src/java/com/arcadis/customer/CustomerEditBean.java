/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arcadis.customer;

import java.io.Serializable;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.annotation.ManagedProperty;
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

    @ManagedProperty("#{param.customerId}")
    private String customerId;
    
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
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
    
    public String editCustomer() {
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
        customerId = params.get("customerId");
//        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, customerId, ""));
        return "/customer/edit";
//        return null;
    }
    
}
