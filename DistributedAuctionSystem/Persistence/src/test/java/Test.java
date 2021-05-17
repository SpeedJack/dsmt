import it.unipi.dsmt.das.UserEntity;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Test {

    public static void main(String args[]) {
        EntityManagerFactory emfactory = Persistence.createEntityManagerFactory( "default" );
        EntityManager entitymanager = emfactory.createEntityManager( );
        entitymanager.getTransaction( ).begin( );
        UserEntity employee = new UserEntity( );
        employee.setId( 1201 );
        employee.setUsername( "Gopal" );
        employee.setPassword( "Ciao" );

        entitymanager.persist( employee );
        entitymanager.getTransaction( ).commit( );

        entitymanager.close( );
        emfactory.close( );
    }
}
