package gov.nist.hit.core.transport.listener;

import gov.nist.hit.core.transport.event.OutboundEvent;
 


//@Component
public class OutboundEventListener  {
 
//  @EventListener
   public void handleOutboundEvent(OutboundEvent event) {
     System.out.println(" OutboundEvent received ");
  }
}
