
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/*Interface for a Graph Object.*/
public interface Graph<N,E>{
    /*Adds a Node to the Graph.*/
    public void addNode(N node);
    /*Adds an edge value between node1 and node2. when bidirectional is true.*/
    public boolean addEdge(E value,NodeN node1,NodeN node2,boolean bidirectional);
    /*Removes val from the graph.*/
    public N removeNode(N val);
    /*Return a Node at an index.*/
    public abstract NodeN getNodeNum(int num);
    /*Node for a graph.*/
    abstract class NodeN{}
    /*Edge for a graph.*/
    abstract class EdgeE{}
}
