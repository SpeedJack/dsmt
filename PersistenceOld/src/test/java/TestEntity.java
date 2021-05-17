import it.unipi.dsmt.persistence.EntityManager;
import it.unipi.dsmt.persistence.UsersManager;
import it.unipi.dsmt.persistence.entities.UsersEntity;

import java.util.Properties;

public class TestEntity {
    public static void main(String[] args){

        Properties properties = new Properties();
        String host = "localhost";
        String dbport = "1527";
        properties.setProperty("javax.persistence.jdbc.url", "jdbc:derby://"+host+":"+dbport+"/DSMT;create=true");
        properties.setProperty("javax.persistence.jdbc.user", "APP");
        properties.setProperty("javax.persistence.jdbc.password", "APP");


        UsersEntity ue = new UsersEntity();
        ue.setUsername("Nico");
        ue.setPassword("Nico");
        EntityManager.init(properties);
        EntityManager.beginTransaction();
        UsersManager manager = new UsersManager();
        manager.persist(ue);
        EntityManager.commitTransaction();
    }
}
