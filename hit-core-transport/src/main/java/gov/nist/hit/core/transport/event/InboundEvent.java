package gov.nist.hit.core.transport.event;

import org.springframework.context.ApplicationEvent;

public class InboundEvent extends MessageEvent{
  
  private static final long serialVersionUID = 1L;

    
  public InboundEvent(Object source, final String message) {
      super(source,message);
    }
  
 

}
