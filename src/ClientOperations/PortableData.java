package ClientOperations;

import java.io.Serializable;

public class PortableData implements Serializable {
    private String order;
    private Object object;

    public PortableData(String order, Object object){
        this.object = object;
        this.order = order;
    }

    public String getOrder() {
        return order;
    }

    public Object getObject() {
        return object;
    }
}
