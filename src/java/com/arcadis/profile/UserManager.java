/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arcadis.profile;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 * Manages the current user for the session
 *
 * @author mcguire
 */
@Named("userManager")
@SessionScoped
public class UserManager implements Serializable {

    private String userId;
    private String username;
    private String fullname;
    private boolean loggedIn;
    private String bootswatchTheme;
    private boolean administrator;

    /**
     * Initialize the object
     */
    public UserManager() {
        username = "";
        fullname = "";
        loggedIn = false;
        bootswatchTheme = "spacelab";
    }

    /**
     *
     * @return the current user's user id
     */
    public String getUserId() {
        return userId;
    }

    /**
     *
     * @param userId
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     *
     * @return the current user's username
     */
    public String getUsername() {
        return username;
    }

    /**
     *
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     *
     * @return the current user's full name
     */
    public String getFullname() {
        return fullname;
    }

    /**
     *
     * @param fullname
     */
    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    /**
     *
     * @return the logged in flag
     */
    public boolean isLoggedIn() {
        return loggedIn;
    }

    /**
     *
     * @param loggedIn
     */
    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    /**
     *
     * @return the current user's selected bootswatch theme. Defaults to
     * "spacelab".
     */
    public String getBootswatchTheme() {
        return bootswatchTheme;
    }

    /**
     *
     * @param bootswatchTheme
     */
    public void setBootswatchTheme(String bootswatchTheme) {
        this.bootswatchTheme = bootswatchTheme;
    }

    /**
     *
     * @return a flag indicating if the current user is an administrator.
     */
    public boolean isAdministrator() {
        return administrator;
    }

    /**
     *
     * @param administrator
     */
    public void setAdministrator(boolean administrator) {
        this.administrator = administrator;
    }

    /**
     * Run as a preRenderView JSF event to check to see if there is a user
     * logged in. If not, redirect the client's browser to the login page.
     */
    public void verifyLogin() {
        if (!isLoggedIn()) {
            // Manual redirection as this isn't an action button or link
            FacesContext fc = FacesContext.getCurrentInstance();
            fc.getApplication().getNavigationHandler().handleNavigation(fc, null, "/user/login?faces-redirect=true");
        }
    }

    public void isInRole(String roleName) {
        if ("Administrator".equals(roleName) && !isAdministrator()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Permission denied ... you are not a member of the Administrator role", ""));
            // Manual redirection as this isn't an action button or link
            FacesContext fc = FacesContext.getCurrentInstance();
            fc.getApplication().getNavigationHandler().handleNavigation(fc, null, "/index?faces-redirect=true");
        }
    }

}
