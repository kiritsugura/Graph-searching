
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

/*Adjacency list implementation of a Graph data structure.*/
public class AdjacencyGraph<N,E> implements Graph<N,E>{
    /*The items in the Graph.*/
    public HashSet<Node> items;
    /*The current size of the graph.*/
    public int currentSize;
    /*The last added item.*/
    public Node last;
    /*Default constructor.*/
    public AdjacencyGraph(){
        items=new HashSet(100,20,.8);
        currentSize=0;
    }
    /*Add a node to graph.
      @param: node is the node being added to the graph.*/
    public void addNode(N node){
        Node n=new Node(node);
        items.add(n);
        currentSize++;
        last=n;
    }
    /*Returns the last node added to the graph*/
    protected Node getLast(){
        return last;
    }
    /*Adds an edge value between node1 and node2. If bidirectional is true,node1 and node2 both contain a reference to the edg,.
      else only node1 contains a reference to the edge.*/
    public boolean addEdge(E value,NodeN node1,NodeN node2,boolean bidirectional){
        HashMapIterator it=items.iterator(true);
        Node n1 = null,n2=null;
        while(it.hasNext()){
            Node next=(Node)it.next();
            if(((Node)node2).index==next.index){
                n2=next;
            } 
            if(((Node)node1).index==next.index){
                n1=next;
            }
        }
        Edge item=new Edge(value,n1,n2);
        return n1.addEdge(item,n2);
    }
    /*Returns the node at the index num
      @param: num is the index of the desired Node.
      @return: returns the item associated with the index num.*/
    public Node getNodeNum(int num){
        HashMapIterator it=items.iterator(true);
        while(it.hasNext()){
            Node next=(Node)it.next();
            if(next.index==num){
                return next;
            }
        }
        return null;
    }    
    /*Removes val from the graph.
      @param: val is the item being removed from the graph.
      @return: returns the value of the node removed from the graph.*/
    public N removeNode(N val){
        HashMapIterator it=items.iterator(true);
        while(it.hasNext()){
            Node next=(Node)it.next();
            if(next.contains(val)){
                next.removeEdge(val);
            }
        }
        return (N)items.remove(val);
    }
    /*Process a line file.
      @param: the current line being loaded by a file.*/
    public void processFileLine(String line){}
    /*Load each line from a file. Throws an exception if the file does not exist.
      @param: fileName is the name of the file being loaded.*/
    public void loadFromFile(String fileName) throws IOException{
        Scanner reader=new Scanner(new File(fileName));
        while(reader.hasNext()){
            processFileLine(reader.nextLine());
        }
        
    }
    /*Node for this graph.*/
    protected class Node extends NodeN{
        public N nodeVal;
        ArrayList<Edge> edges;
        int index;
        Node(N node){
            nodeVal=node;
            edges=new ArrayList();
            index=currentSize;
        }        
        protected void setEdges(ArrayList<Edge> edgeList){
            edges=edgeList;
        }
        /*Adds an Edge to this node.*/
        protected boolean addEdge(Edge newEdge,Node other){
            boolean bi=false;
            Edge cor=null;
            for(Edge e:edges){
                if((e.item1==this && newEdge.item2==this && e.item2==other && newEdge.item1==other)||(e.item2==this && newEdge.item1==this && e.item1==other && newEdge.item2==other)){
                    bi=true;
                    e.bidirectional=true;                
                }
            }
            if(!bi){
                edges.add(newEdge);
                other.edges.add(newEdge);
                return true;
            }else{
                return false;
            }
        }
        protected boolean contains(N item){
            for(int i=0;i<edges.size();i++){
                if(edges.get(i).linksItem(item)){
                    return true;
                }
            }
            return false;
        }
        protected Edge removeEdge(N item){
            for(int i=0;i<edges.size();i++){
                if(edges.get(i).linksItem(item)){
                    return edges.remove(i);
                }
            }
            return null;
        } 
        @Override
        public String toString(){
            return nodeVal.toString();
        }
        /*@return: returns an arraylist containing the neighbors of this node.*/
        protected ArrayList<Node> getConnections(){
            Iterator<Edge> it=edges.iterator();
            ArrayList<Node> neighbors=new ArrayList();
            while(it.hasNext()){
                Edge next=(Edge)it.next();
                if(next.item1==this){
                    neighbors.add(next.item2);
                }
                if(next.item2==this){
                    neighbors.add(next.item1);
                }
            }
            return neighbors;
        }
    }
    /*Edge for this graph.*/
    public class Edge extends EdgeE{
        E edgeVal;
        Node item1,item2;
        boolean bidirectional;
        Edge(E value,Node i1,Node i2){
            edgeVal=value;
            item1=i1;
            item2=i2;
        }
        public boolean linksItem(N node){
            return node.equals(item1.nodeVal) || node.equals(item2.nodeVal);
        }
        public boolean links(Node n){
            return item2==n || item1==n;
        }
        @Override
        public String toString(){
            return "["+edgeVal+"]";
        }
    }    
}
