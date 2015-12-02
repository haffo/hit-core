package gov.nist.hit.core.transport.event;

 
public class OutboundEvent extends MessageEvent{
  
  private static final long serialVersionUID = 1L;

    
  public OutboundEvent(Object source, final String message) {
      super(source,message);
    }
  
 

}
