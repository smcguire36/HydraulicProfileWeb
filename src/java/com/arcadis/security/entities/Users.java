/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arcadis.security.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.id.GUIDGenerator;

/**
 *
 * @author Stewart E. McGuire
 */
@Entity
public class Users implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GenericGenerator(name="GUID", strategy="org.hibernate.id.GUIDGenerator")
    @GeneratedValue(generator="GUID")
    @Column(name="userId", columnDefinition="uniqueidentifier", updatable=false, nullable=false)
    private String userId;
    @Column(length=50)
    private String firstName;
    @Column(length=50)
    private String lastName;
    @Column(length=100)
    private String emailAddress;
    @Column(length=50)
    private String username;
    @Column(length=50)    
    private String password;
    @Column(length=50)
    private String bootswatchTheme;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String id) {
        this.userId = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String name) {
        this.firstName = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String name) {
        this.lastName = name;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String email) {
        this.emailAddress = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBootswatchTheme() {
        return bootswatchTheme;
    }

    public void setBootswatchTheme(String bootswatchTheme) {
        this.bootswatchTheme = bootswatchTheme;
    }

    public Users() {
    }   // Empty constructor required by JPA

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
        return "com.arcadis.security.Users[ id=" + userId + " ]";
    }

}
