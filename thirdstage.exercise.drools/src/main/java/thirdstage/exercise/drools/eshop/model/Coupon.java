package thirdstage.exercise.drools.eshop.model;

import java.time.LocalDateTime;

public class Coupon{
  
  private long id; 
  
  private Customer customer;
  
  private LocalDateTime validFrom;
  
  private LocalDateTime validTo;
  
  public long getId(){ return this.id; }
  
  public void setId(long id){ this.id = id; }

  public Customer getCustomer(){ return this.customer; }
  
  public void setCustomer(Customer customer){ this.customer = customer; }
  
  public LocalDateTime getValidFrom(){ return this.validFrom; }
  
  public void setValidFrom(LocalDateTime from){ this.validFrom = from; }
  
  public LocalDateTime getValidTo(){ return this.validTo; }
  
  public void setValidTo(LocalDateTime to){ this.validTo = to; }
  
}
