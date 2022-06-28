package MessageOperations;

import ClientOperations.Client;

import java.io.Serializable;
import java.time.LocalDateTime;

public class PrivateChatMessage extends Message implements Serializable {
    private Client to;


    public PrivateChatMessage(TypeMVF type,Client from,Client to,Object message) {
        super(type,from, message);
        this.to = to;
    }




    public Client getTo() {
        return to;
    }


}
