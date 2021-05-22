package it.unipi.dsmt.das.ejbs.beans.interfaces;

import it.unipi.dsmt.das.model.User;

import javax.ejb.Local;
import javax.ejb.Remote;
import java.sql.SQLException;


@Remote
public interface UserManager {
    User login(String username, String password);
    User registration(String username, String password) throws SQLException;
    User modify(String username, String oldPassword, String newPassword);

}
