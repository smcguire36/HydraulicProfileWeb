/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arcadis.entities;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author mcguire
 */
@Entity
@Table(name="Users")
@NamedQueries({
    @NamedQuery(name = Users.QUERY_FIND_ALL, query = "SELECT u FROM Users u"),
    @NamedQuery(name = Users.QUERY_FIND_BY_USER_ID, query = "SELECT u FROM Users u WHERE u.userId = :userId"),
    @NamedQuery(name = Users.QUERY_FIND_BY_EMAIL_ADDRESS, query = "SELECT u FROM Users u WHERE u.emailAddress = :emailAddress"),
    @NamedQuery(name = Users.QUERY_FIND_BY_USERNAME, query = "SELECT u FROM Users u WHERE u.username = :username")
})
public class Users implements Serializable {

    public static final String QUERY_FIND_ALL = "Users.findAll";
    public static final String QUERY_FIND_BY_USER_ID = "Users.findByUserId";
    public static final String QUERY_FIND_BY_EMAIL_ADDRESS = "Users.findByEmailAddress";
    public static final String QUERY_FIND_BY_USERNAME = "Users.findByUsername";
    private static final long serialVersionUID = 1L;
    @Id
    @NotNull
    @Size(min = 1, max = 36)
    @GenericGenerator(name = "GUID", strategy = "org.hibernate.id.GUIDGenerator")
    @GeneratedValue(generator = "GUID")
    @Column(name = "userId", columnDefinition = "uniqueidentifier", updatable = false, nullable = false, length = 36)
    private String userId;
    @Basic
    @Size(max = 100)
    @Column(length = 100)
    private String emailAddress;
    @Basic
    @Size(max = 50)
    @Column(length = 50)
    private String firstName;
    @Basic
    @Size(max = 50)
    @Column(length = 50)
    private String lastName;
    @Basic
    @Size(max = 50)
    @Column(length = 50)
    private String password;
    @Basic
    @Size(max = 50)
    @Column(length = 50)
    private String username;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(nullable = false, length = 50)
    private String bootswatchTheme;
    @Basic(optional = false)
    @NotNull
    @Column(name = "isAdministrator", nullable = false)
    private boolean administrator;
    @ManyToMany(mappedBy = "users", fetch = FetchType.EAGER)
    @OrderBy("customerName ASC")
    private Set<Customer> customers;

    public Users() {
        this.customers = new HashSet<>();
    }

    public Users(String userId) {
        this.customers = new HashSet<>();
        this.userId = userId;
    }

    public Users(String userId, String bootswatchTheme) {
        this.customers = new HashSet<>();
        this.userId = userId;
        this.bootswatchTheme = bootswatchTheme;
    }

    public Users(String userId, String bootswatchTheme, boolean isAdministrator) {
        this.customers = new HashSet<>();
        this.userId = userId;
        this.bootswatchTheme = bootswatchTheme;
        this.administrator = isAdministrator;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBootswatchTheme() {
        return bootswatchTheme;
    }

    public void setBootswatchTheme(String bootswatchTheme) {
        this.bootswatchTheme = bootswatchTheme;
    }

    public boolean isAdministrator() {
        return administrator;
    }

    public void setAdministrator(boolean administrator) {
        this.administrator = administrator;
    }

    public Set<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(Set<Customer> customers) {
        this.customers = customers;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userId != null ? userId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Users)) {
            return false;
        }
        Users other = (Users) object;
        if ((this.userId == null && other.userId != null) || (this.userId != null && !this.userId.equals(other.userId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.arcadis.entities.Users[ userId=" + userId + " ]";
    }

    public String getFullname() {
        return firstName + " " + lastName;
    }
    
}
