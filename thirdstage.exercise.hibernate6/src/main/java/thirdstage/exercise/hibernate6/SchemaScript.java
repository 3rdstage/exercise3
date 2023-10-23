package thirdstage.exercise.hibernate6;

import javax.persistence.Persistence;

public class SchemaScript {

  public static void main(String... args){

    System.out.println("Generating DDL script.");

    Persistence.createEntityManagerFactory("sto"); 
    //Persistence.generateSchema("sto", null);
  }
  
}
