package it.unipi.dsmt.persistence;

import it.unipi.dsmt.persistence.entities.UsersEntity;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class UsersManager extends EntityManager {

    @Override
    public List<UsersEntity> getAll() {
        createEM();
        javax.persistence.EntityManager em = getEM();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<UsersEntity> cq = cb.createQuery(UsersEntity.class);
        Root<UsersEntity> from = cq.from(UsersEntity.class);
        cq.select(from);
        TypedQuery<UsersEntity> query = em.createQuery(cq);
        List<UsersEntity> users;
        try {
            users = query.getResultList();
        } catch (NoResultException ex) {
            Logger.getLogger(UsersManager.class.getName()).info("getResultList() returned no result.");
            users = new ArrayList<UsersEntity>();
        } finally {
            closeEM();
        }
        return users;
    }
}
