/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arcadis.security;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author mcguire
 */
@Named("userManager")
@SessionScoped
public class UserManager implements Serializable {
    private String username;
    private String fullname;
    private boolean loggedIn;
    private String bootswatchTheme;

    public UserManager() {
        username = "";
        fullname = "";
        loggedIn = false;
        bootswatchTheme = "spacelab";
    }
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }
    
    public String getBootswatchTheme() {
        return bootswatchTheme;
    }

    public void setBootswatchTheme(String bootswatchTheme) {
        this.bootswatchTheme = bootswatchTheme;
    }

}
