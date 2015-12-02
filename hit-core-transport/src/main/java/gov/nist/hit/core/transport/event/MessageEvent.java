package gov.nist.hit.core.transport.event;

import org.springframework.context.ApplicationEvent;


public class MessageEvent extends ApplicationEvent{
  
  private static final long serialVersionUID = 1L;

  final String message;
   
  public MessageEvent(Object source, final String message) {
      super(source);
      this.message = message;
   }
  
  

  public String getMessage() {
    return message;
  }



  @Override
  public String toString() {
      return "MessageReceivedEvent message: " + this.message;
  }


}
