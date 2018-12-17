package com.arcadis.user;

import com.arcadis.dao.DaoService;
import com.arcadis.dao.UserDao;
import com.arcadis.entities.Users;
import com.arcadis.profile.UserManager;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.inject.spi.CDI;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;

@Named("authenticate")
@ViewScoped
public class AuthenticateBean implements Serializable {

    private String username;
    private String password;

    @Inject
    private UserManager userMgr;

    public AuthenticateBean() {
    }

    public String getUsername() {
        if ("".equals(username)) {
            return userMgr.getUsername();
        }
        return username;
    }

    public void setUsername(String newValue) {
        username = newValue;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String newValue) {
        password = newValue;
    }

    public String login() {
        try {
            doLogin();
        } catch (Exception ex) {
            Logger.getLogger("com.arcadis").log(Level.SEVERE, "login failed", ex);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "A fatal error occured: " + ex.getMessage(), ""));
            return null;
        }
        if (userMgr.isLoggedIn()) {
            setPassword("");
            return "/index?faces-redirect=true";
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Invalid username or password...", ""));
            // ?faces-redirect=true
            return null;
        }
    }

    public String logout() {
        userMgr.setLoggedIn(false);
        userMgr.setAdministrator(false);
        userMgr.setBootswatchTheme("spacelab");
        userMgr.setFullname("");
        return "/index";
    }

    public void doLogin() throws NotSupportedException, SystemException,
            RollbackException, HeuristicMixedException, HeuristicRollbackException {
        DaoService service = CDI.current().select(DaoService.class).get();
        UserDao userDao = CDI.current().select(UserDao.class).get();
        userDao.setDaoService(service);

        try {
            service.BeginTransaction();
            try {

                Users user = userDao.findByName(username);
                if (user != null) {
                    if (user.getPassword().equals(password)) {
                        userMgr.setLoggedIn(true);
                        userMgr.setUserId(user.getUserId());
                        userMgr.setUsername(username);
                        userMgr.setFullname(user.getFirstName() + " " + user.getLastName());
                        userMgr.setBootswatchTheme(user.getBootswatchTheme());
                        userMgr.setAdministrator(user.isAdministrator());
                    }
                }

                service.CommitTransaction();
            } finally {
                if (!service.isCommitted()) {
                    service.RollbackTransaction();
                }
            }
        } finally {
            service.closeEntityManager();
        }

    }

}
