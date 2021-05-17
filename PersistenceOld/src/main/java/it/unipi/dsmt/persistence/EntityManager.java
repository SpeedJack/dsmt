package it.unipi.dsmt.persistence;

import it.unipi.dsmt.persistence.entities.Entity;
import org.hibernate.Session;

import javax.persistence.*;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

public abstract class EntityManager implements AutoCloseable
{
    private static EntityManagerFactory factory;
    private static final ThreadLocal<javax.persistence.EntityManager> threadLocal;

    static {
        threadLocal = new ThreadLocal<javax.persistence.EntityManager>();
    }

    /**
     * Initializes the EntityManager.
     * @param properties The properties to pass to the createEntityManagerFactory method.
     */
    public static void init(Properties properties)
    {
        factory = Persistence.createEntityManagerFactory("default");
    }


    public static void createEM()
    {
        javax.persistence.EntityManager em = factory.createEntityManager();
        em.setFlushMode(FlushModeType.COMMIT);
        threadLocal.set(em);
    }

    protected static javax.persistence.EntityManager getEM()
    {
        Logger.getLogger(EntityManager.class.getName()).entering(EntityManager.class.getName(), "getEM");
        return threadLocal.get();
    }

    public static void closeEM()
    {
        if (threadLocal.get() != null)
            threadLocal.get().close();
        threadLocal.set(null);
    }

    public void close()
    {
        closeEM();
    }

    /**
     * Closes the EntityManager factory.
     */
    public static void closeFactory()
    {
        Logger.getLogger(EntityManager.class.getName()).entering(EntityManager.class.getName(), "closeFactory");

        if (factory != null)
            factory.close();
    }


    /**
     * Get all entities of this type.
     * @return All entites of this type.
     */
    abstract public List<? extends Entity> getAll();

    /**
     * Inserts an entity.
     * @param entity The entity to insert.
     */
    public void insert(Entity entity)
    {
        Logger.getLogger(EntityManager.class.getName()).entering(EntityManager.class.getName(), "insert", entity);
        try {
            beginTransaction();
            persist(entity);
            commitTransaction();
        } catch (PersistenceException ex) {
            Logger.getLogger(EntityManager.class.getName()).throwing(EntityManager.class.getName(), "insert", ex);
            rollbackTransaction();
            throw ex;
        }
        Logger.getLogger(EntityManager.class.getName()).exiting(EntityManager.class.getName(), "insert", entity);
    }

    /**
     * Get an entity of id entityId and class entityClass.
     * @param entityClass The type of the entity to find.
     * @param entityId The entity's id.
     * @return The entity.
     */
    public Entity get(Class<? extends Entity> entityClass, int entityId)
    {
        Logger.getLogger(EntityManager.class.getName()).entering(EntityManager.class.getName(), "get", new Object[]{entityClass, entityId});
        createEM();
        Entity entity = getEM().find(entityClass, entityId);
        detach(entity);
        closeEM();
        return entity;
    }

    /**
     * Returns an entity that exists in the actual session. This method DOES
     * NOT close the EM (this task is left to the caller: this behavior is
     * to allow the caller to access lazy collections inside the retrieved
     * entity that requires the session to be open).
     * @param entityClass The class of the entity to retrieve.
     * @param entityId The entity's id.
     * @return The entity.
     */
    public Entity load(Class<? extends Entity> entityClass, int entityId)
    {
        createEM();
        return getEM().unwrap(Session.class).load(entityClass, entityId);
    }

    /**
     * Returns an entity that exists in the actual session.
     * @param entity The entity to retrieve.
     * @return The entity.
     */
    public Entity load(Entity entity)
    {
        return load(entity.getClass(), entity.getId());
    }

    /**
     * Update an entity.
     * @param entity The entity.
     */
    public void update(Entity entity)
    {
        Logger.getLogger(EntityManager.class.getName()).entering(EntityManager.class.getName(), "update", entity);
        try {
            beginTransaction();
            merge(entity);
            commitTransaction();
        } catch (PersistenceException ex) {
            Logger.getLogger(EntityManager.class.getName()).throwing(EntityManager.class.getName(), "update", ex);
            rollbackTransaction();
            throw ex;
        }
        Logger.getLogger(EntityManager.class.getName()).exiting(EntityManager.class.getName(), "update", entity);
    }

    /**
     * Delete an entity.
     * @param entity The entity.
     */
    public void delete(Entity entity)
    {
        delete(entity.getClass(), entity.getId());
    }

    /**
     * Delete an entity of type entityClass and with id entityId.
     * @param entityClass The type of entity to delete.
     * @param entityId The entity's id.
     */
    public void delete(Class<? extends Entity> entityClass, int entityId)
    {
        Logger.getLogger(EntityManager.class.getName()).entering(EntityManager.class.getName(), "delete", new Object[]{entityClass, entityId});
        try {
            beginTransaction();
            Entity entity = getEM().getReference(entityClass, entityId);
            remove(entity);
            commitTransaction();
        } catch (PersistenceException ex) {
            Logger.getLogger(EntityManager.class.getName()).throwing(EntityManager.class.getName(), "delete", ex);
            rollbackTransaction();
            throw ex;
        }
        Logger.getLogger(EntityManager.class.getName()).exiting(EntityManager.class.getName(), "delete", new Object[]{entityClass, entityId});
    }

    /**
     * Refresh the cache of all the entities specified.
     * @param entities The entities to refresh.
     */
    public void refresh(List<? extends Entity> entities)
    {
        for (Entity entity: entities)
            refresh(entity);
    }

    /**
     * Refresh the cache of the entity specified.
     * @param entity The entity to refresh.
     */
    public void refresh(Entity entity)
    {
        Logger.getLogger(EntityManager.class.getName()).entering(EntityManager.class.getName(), "refresh", entity);

        createEM();
        try {
            getEM().refresh(entity);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(EntityManager.class.getName()).warning(ex.getMessage());
        } finally {
            closeEM();
        }

        Logger.getLogger(EntityManager.class.getName()).exiting(EntityManager.class.getName(), "refresh", entity);
    }

    /**
     * Starts a transaction.
     */
    public static void beginTransaction()
    {
        Logger.getLogger(EntityManager.class.getName()).finer("Transaction: begin.");
        createEM();
        EntityTransaction tx = getEM().getTransaction();
        if (tx != null && !tx.isActive())
            getEM().getTransaction().begin();
    }

    /**
     * Commits a transaction.
     */
    public static void commitTransaction()
    {
        Logger.getLogger(EntityManager.class.getName()).finer("Transaction: commit.");
        EntityTransaction tx = getEM().getTransaction();
        if (tx != null && tx.isActive())
            getEM().getTransaction().commit();
        closeEM();
    }

    /**
     * Rollback a transaction.
     */
    public static void rollbackTransaction()
    {
        Logger.getLogger(EntityManager.class.getName()).finer("Transaction: rollback");
        EntityTransaction tx = getEM().getTransaction();
        if (tx != null && tx.isActive())
            tx.rollback();
        closeEM();
    }

    /**
     * Detaches an entity, making it no more managed by JPA.
     * @param entity The entity to detach.
     */
    public void detach(Entity entity)
    {
        Logger.getLogger(EntityManager.class.getName()).entering(EntityManager.class.getName(), "detach", entity);
        getEM().detach(entity);
        Logger.getLogger(EntityManager.class.getName()).exiting(EntityManager.class.getName(), "detach", entity);
    }

    /**
     * Store an entity (low-level method: does not start a transaction).
     * @param entity The entity to store.
     */
    public void persist(Entity entity)
    {
        Logger.getLogger(EntityManager.class.getName()).entering(EntityManager.class.getName(), "persist", entity);
        getEM().persist(entity);
        Logger.getLogger(EntityManager.class.getName()).exiting(EntityManager.class.getName(), "persist", entity);
    }

    /**
     * Merges an entity (low-level method: does not start a transaction).
     * @param entity The entity to merge.
     */
    public void merge(Entity entity)
    {
        Logger.getLogger(EntityManager.class.getName()).entering(EntityManager.class.getName(), "merge", entity);
        getEM().merge(entity);
        Logger.getLogger(EntityManager.class.getName()).exiting(EntityManager.class.getName(), "merge", entity);
    }

    /**
     * Removes an entity (low-level method: does not start a transaction).
     * @param entity The entity to remove.
     */
    public void remove(Entity entity)
    {
        Logger.getLogger(EntityManager.class.getName()).entering(EntityManager.class.getName(), "remove", entity);
        getEM().remove(entity);
        Logger.getLogger(EntityManager.class.getName()).exiting(EntityManager.class.getName(), "remove", entity);
    }
}
