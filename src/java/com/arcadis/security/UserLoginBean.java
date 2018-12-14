package com.arcadis.security;

import com.arcadis.security.entities.Users;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

@Named("userLogin")
@ViewScoped
public class UserLoginBean implements Serializable {

    private String username;
    private String password;

    @PersistenceUnit(unitName = "HydraulicProfileWebPU")
    private EntityManagerFactory emf;

    @Inject
    private UserManager userMgr;

    @Resource
    private UserTransaction utx;
    
    public UserLoginBean() {
    }

    public String getUsername() {
        if (username == "") {
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
        //return "/security/userLogin";
        return null;
    }

    public void doLogin() throws NotSupportedException, SystemException,
            RollbackException, HeuristicMixedException, HeuristicRollbackException {
        EntityManager em = emf.createEntityManager();
        try {
            utx.begin();
            em.joinTransaction();
            boolean committed = false;
            try {
                Query query = em.createQuery("SELECT u FROM Users u WHERE u.username = :username")
                        .setParameter("username", username);
                @SuppressWarnings("unchecked")
                List<Users> result = query.getResultList();

                if (result.size() == 1) {
                    Users u = result.get(0);
                    if (u.getPassword().equals(password)) {
                        userMgr.setLoggedIn(true);
                        userMgr.setUsername(username);
                        userMgr.setFullname(u.getFirstName() + " " + u.getLastName());
                        userMgr.setBootswatchTheme(u.getBootswatchTheme());
                    }
                }
                utx.commit();
                committed = true;
            } finally {
                if (!committed) {
                    utx.rollback();
                }
            }
        } finally {
            em.close();
        }
    }
}
