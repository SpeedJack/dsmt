package it.unipi.dsmt.persistence.entities;

import javax.persistence.*;
import javax.persistence.Entity;
import java.util.Objects;


@Entity
@Table(name = "USERS", schema = "DSMT")
public class UsersEntity extends it.unipi.dsmt.persistence.entities.Entity {

    private String username;
    private String password;

/**
    @Id
    @Column(name = "ID")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
**/
    public UsersEntity(String username, String password){
        super(0);
        this.username = username;
        this.password = password;
    }

    public UsersEntity(){
        this("","");
    }

    @Basic
    @Column(name = "USERNAME")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Basic
    @Column(name = "PASSWORD")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UsersEntity that = (UsersEntity) o;
        return id == that.id && Objects.equals(username, that.username) && Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password);
    }
}
