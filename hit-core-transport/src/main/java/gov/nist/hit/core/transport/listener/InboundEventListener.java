package gov.nist.hit.core.transport.listener;

import gov.nist.hit.core.transport.event.InboundEvent;
import gov.nist.hit.core.transport.event.OutboundEvent;

import org.springframework.context.ApplicationListener;

public class InboundEventListener implements ApplicationListener<InboundEvent> {

  @Override
  public void onApplicationEvent(InboundEvent event) {
     System.out.println(" InboundEvent received ");
  }
}
