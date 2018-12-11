package com.arcadis.security;

import com.arcadis.security.entities.Users;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.inject.Named;
// or import javax.faces.bean.ManagedBean;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
// or import javax.faces.bean.SessionScoped;
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

@Named("user")
@SessionScoped
public class UserBean implements Serializable {

    private String name;
    private String password;
    private int count;
    private boolean loggedIn;

    @PersistenceUnit(unitName = "HydraulicProfileWebPU")
    private EntityManagerFactory emf;

    @Resource
    private UserTransaction utx;

    public String getName() {
        return name;
    }

    public void setName(String newValue) {
        name = newValue;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String newValue) {
        password = newValue;
    }

    public int getCount() {
        return count;
    }

    public String login() {
        try {
            doLogin();
        } catch (Exception ex) {
            Logger.getLogger("com.arcadis").log(Level.SEVERE, "login failed", ex);
            return "loginError";
        }
        if (loggedIn) {
            return "/index?faces-redirect=true";
        } else {
//            navigateTo("security/loginFailure");
            // ?faces-redirect=true
            return "loginFailure";
        }
    }

    public String logout() {
        loggedIn = false;
        return "login";
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
                        .setParameter("username", name);
                @SuppressWarnings("unchecked")
                List<Users> result = query.getResultList();

                if (result.size() == 1) {
                    Users u = result.get(0);
                    if (u.getPassword().equals(password)) {
                        loggedIn = true;
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

    public void navigateTo(String href) {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("MYSERVERADDRESS" + href);
        } catch (IOException ex) {
        }
    }
}
