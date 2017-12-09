package ulcrs.models.schedule;

import java.util.ArrayList;
import java.util.List;

public class Node {

    private List<Tuple> tuples;

    public Node() {
        this.tuples = new ArrayList<>();
    }

    public Node(Node node) {
        this();
        for (Tuple tuple : node.getTuples()){
            this.tuples.add(tuple);
        }
    }

    public void addTuple(Tuple tuple) {
        this.tuples.add(tuple);
    }

    public List<Tuple> getTuples() {
        return this.tuples;
    }

    public void removeTuple(Tuple tuple) {
        tuples.remove(tuple);
    }
}
