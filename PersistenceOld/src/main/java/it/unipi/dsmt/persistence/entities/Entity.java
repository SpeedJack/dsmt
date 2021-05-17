package it.unipi.dsmt.persistence.entities;

import javax.persistence.*;

/**
 * Represents a JPA entity.
 */
@MappedSuperclass
public abstract class Entity
{
    /**
     * The id of the entity.
     */
    @Id
    //@GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="ID", nullable=false)
    protected int id;

    /**
     * Creates a new entity, with the specified id.
     * @param id Entity's id.
     */
    protected Entity(int id)
    {
        setId(id);
    }

    /**
     * Creates a new entity, with id=0.
     */
    protected Entity()
    {
        this(0);
    }

    /**
     * Sets entity's id.
     * @param id The new entity's id.
     */
    public void setId(int id)
    {
        this.id = id;
    }

    /**
     * Gets entity's id.
     * @return The entity's id.
     */
    public int getId()
    {
        return id;
    }
}

