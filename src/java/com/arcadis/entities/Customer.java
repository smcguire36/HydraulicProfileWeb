/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arcadis.entities;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OrderBy;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author mcguire
 */
@Entity
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = Customer.QUERY_FIND_ALL, query = "SELECT c FROM Customer c"),
    @NamedQuery(name = Customer.QUERY_FIND_BY_CUSTOMER_ID, query = "SELECT c FROM Customer c WHERE c.customerId = :customerId"),
    @NamedQuery(name = Customer.QUERY_FIND_BY_CUSTOMER_NAME, query = "SELECT c FROM Customer c WHERE c.customerName = :customerName"),
    @NamedQuery(name = Customer.QUERY_FIND_BY_CUSTOMER_USER, query = "SELECT c FROM Customer c JOIN c.users u WHERE u.userId = :userId")
})
public class Customer implements Serializable {

    public static final String QUERY_FIND_ALL = "Customer.findAll";
    public static final String QUERY_FIND_BY_CUSTOMER_ID = "Customer.findByCustomerId";
    public static final String QUERY_FIND_BY_CUSTOMER_NAME = "Customer.findByCustomerName";
    public static final String QUERY_FIND_BY_CUSTOMER_USER = "Customer.findByCustomerUser";

    private static final long serialVersionUID = 1L;
    @Id
    @GenericGenerator(name="GUID", strategy="org.hibernate.id.GUIDGenerator")
    @GeneratedValue(generator="GUID")
    @Column(name="customerId", columnDefinition="uniqueidentifier", updatable=false, nullable=false)
    @Size(min = 1, max = 36)
    private String customerId;
    @NotNull
    @Size(min = 1, max = 100)
    @Column(length=100, nullable=false)
    private String customerName;
    @JoinTable(name = "CustomerUsers", joinColumns = {
        @JoinColumn(name = "customerId", referencedColumnName = "customerId", nullable = false)
    }, inverseJoinColumns = {
        @JoinColumn(name = "userId", referencedColumnName = "userId", nullable = false)
    })
    @ManyToMany(cascade={ CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.EAGER)
    @OrderBy("lastName ASC")
    private Set<Users> users;

    public Customer() {
        this.users = new HashSet<Users>();
    }

    public Customer(String customerId) {
        this.users = new HashSet<Users>();
        this.customerId = customerId;
    }

    public Customer(String customerId, String customerName) {
        this.users = new HashSet<Users>();
        this.customerId = customerId;
        this.customerName = customerName;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Set<Users> getUsers() {
        return users;
    }

    public void setUsers(Set<Users> users) {
        this.users = users;
    }
    
    public void addUsers(Users user) {
        users.add(user);
        user.getCustomers().add(this);
    }
    
    public void removeUsers(Users user) {
        users.remove(user);
        user.getCustomers().remove(this);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (customerId != null ? customerId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Customer)) {
            return false;
        }
        Customer other = (Customer) object;
        if ((this.customerId == null && other.customerId != null) || (this.customerId != null && !this.customerId.equals(other.customerId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.arcadis.entities.Customer[ customerId=" + customerId + " ]";
    }
    
}
