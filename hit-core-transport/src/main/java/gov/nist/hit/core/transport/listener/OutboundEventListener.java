package gov.nist.hit.core.transport.listener;

//import org.springframework.context.event.EventListener;
//import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import gov.nist.hit.core.transport.event.OutboundEvent;
 


@Component
public class OutboundEventListener  {
 
//  @EventListener
   public void handleOutboundEvent(OutboundEvent event) {
     System.out.println(" OutboundEvent received ");
  }
}
