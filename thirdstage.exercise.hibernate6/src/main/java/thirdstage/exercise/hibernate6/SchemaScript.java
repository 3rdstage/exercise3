package thirdstage.exercise.hibernate6;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceProperty;
import jakarta.persistence.PersistenceUnit;

public class SchemaScript {


  public static void main(String... args){

    System.out.println("Generating DDL script.");

    Persistence.generateSchema("sto", null);

  }
  
}
