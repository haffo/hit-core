package gov.nist.hit.core.transport.listener;

import gov.nist.hit.core.transport.event.InboundEvent;
import gov.nist.hit.core.transport.event.OutboundEvent;

import org.springframework.context.ApplicationListener;
//import org.springframework.context.event.EventListener;
// import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class InboundEventListener {

//  @EventListener
  public void handleInboundEvent(InboundEvent event) {
    System.out.println(" InboundEvent received ");
  }
}
