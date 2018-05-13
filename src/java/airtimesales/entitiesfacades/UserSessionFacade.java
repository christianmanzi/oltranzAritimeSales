/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package airtimesales.entitiesfacades;

import airtimesales.entities.UserSession;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author manzi
 */
@Stateless
public class UserSessionFacade extends AbstractFacade<UserSession> {
    @PersistenceContext(unitName = "AirtimeSellsPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UserSessionFacade() {
        super(UserSession.class);
    }
    
}
