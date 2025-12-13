package Server;

import Utils.Request;

public interface Task {

    public default void execute(Request req){

    }
}
