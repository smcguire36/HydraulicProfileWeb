package com.arcadis.profile;

import com.arcadis.dao.DaoService;
import com.arcadis.dao.UserDao;
import com.arcadis.entities.Users;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.spi.CDI;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;

/**
 *
 * @author mcguire
 */
@Named("theme")
@RequestScoped
public class BootswatchThemeBean implements Serializable {

    @Inject
    private UserManager userMgr;

    /**
     *
     */
    public BootswatchThemeBean() {
    }

    /**
     *
     */
    public void saveTheme() {
        try {
            doSaveTheme();
        } catch (Exception ex) {
            Logger.getLogger("com.arcadis").log(Level.SEVERE, "Save theme failed", ex);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "A fatal error occured while saving theme: " + ex.getMessage(), ""));
        }
    }

    private void doSaveTheme() throws NotSupportedException, SystemException,
            RollbackException, HeuristicMixedException, HeuristicRollbackException {
        DaoService service =CDI.current().select(DaoService.class).get();
        UserDao userDao = CDI.current().select(UserDao.class).get();
        userDao.setDaoService(service);
        
        // Don't try to save the theme setting if there is no user logged in.
        if (!userMgr.isLoggedIn()) {
            return;
        }

        try {
            service.BeginTransaction();
            try {

                Users user = userDao.findById(userMgr.getUserId());
                if (user != null) {
                    user.setBootswatchTheme(userMgr.getBootswatchTheme());
//                    userDao.persist(user);
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
