package MessageOperations;

import ClientOperations.Client;

import java.io.Serializable;

public class GroupMessage implements Serializable {
    private String name;
    private Client from;
    private Object body;

}
