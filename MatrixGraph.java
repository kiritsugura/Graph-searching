
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Scanner;

/*Adjacency Matrix implementation of a Graph data structure.*/
public class MatrixGraph<N,E> implements Graph<N,E>{
    /*The items in the Graph.*/
    public ArrayList<ArrayList<Integer>>items;
    /*List of the things in the matrix.*/
    public ArrayList<Node> things;
    /*The current size of the graph.*/
    public int currentSize;
    /*Last item added to the graph*/
    public Node last;
    /*Default constructor.*/
    public MatrixGraph(){
        items=new ArrayList();
        things=new ArrayList();
        currentSize=0;
    }
    /*Returns the last node added to the Graph.*/
    public Node getLast(){
        return last;
    }
    /*Add a node to graph.
      @param: node is the node being added to the graph.*/
    public void addNode(N node){
        Node n=new Node(node);
        things.add(n);
        items.add(new ArrayList());
        int index=0;
        while(index<items.size()){
            while(items.get(index).size()<items.size()){
                items.get(index).add(new Integer(0));
            }
            index++;
        }
        last=n;
        currentSize++;
    }
    /*Adds an edge value between node1 and node2. If bidirectional is true,node1 and node2 both contain a reference to the edg,.
      else only node1 contains a reference to the edge.*/
    public boolean addEdge(E value,NodeN node1,NodeN node2,boolean bidirectional){
        Iterator it=things.iterator();
        Node n1 = null,n2=null;
        int index1=0,index2=0,tIndex=0;
        while(it.hasNext()){
            Node next=(Node)it.next();
            if(((Node)node1).equals(next)){
                index1=tIndex;
                n1=next;
            }else if(((Node)node2).equals(next)){
                index2=tIndex;
                n2=next;
            }
            tIndex++;
        }
        Edge item=new Edge(value,n1,n2);
        n1.addEdge(item);
        items.get(index1).set(index2, new Integer(1));
        if(bidirectional){
            n2.addEdge(item);
            items.get(index2).set(index1, new Integer(1));
        }
        if(n1==n2){
            items.get(index2).set(index1, new Integer(2));
        }
        return true;
    }
    /*Removes val from the graph.
      @param: val is the value of the item being removed from the graph.*/
    public N removeNode(N val){
        Iterator it=things.iterator();
        boolean wasRem=false;
        int index=0,fIndex=0;
        while(it.hasNext()){
            Node next=(Node)it.next();
            if(next.contains(val)){
                next.removeEdge(val);
                wasRem=true;
                fIndex=index;
            }
            index++;
        }
        if(wasRem){
            Iterator iter=items.iterator();
            while(iter.hasNext()){
                ((ArrayList)iter.next()).remove(fIndex);
            }
            return (N)items.remove(fIndex);
        }else
            throw new NoSuchElementException();
    }
    /*Process a line file.
      @param: a line of data read from a file.*/
    public void processFileLine(String line){}
    /*Load each line from a file. Throws an Exception if the file does not exist.
      @param: the name of the file being read from.*/
    public void loadFromFile(String fileName) throws IOException{
        Scanner reader=new Scanner(new File(fileName));
        while(reader.hasNext()){
            processFileLine(reader.nextLine());
        }
        
    }
    /*@param:num is the index of the Node in the node arraylist things.
      @return: returns the Node with the index num.*/
    public Node getNodeNum(int num){
        return things.get(num);
    }    
    /*Node for this graph.*/
    protected class Node extends NodeN{
        N nodeVal;
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
        protected void addEdge(Edge newEdge){
            edges.add(newEdge);
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
        public int getIndex(){
            return index;
        }
        @Override
        public String toString(){
            return nodeVal.toString();
        }
    }
    /*Edge for this Graph.*/
    protected class Edge extends EdgeE{
        E edgeVal;
        Node item1,item2;
        Edge(E value,Node i1,Node i2){
            edgeVal=value;
            item1=i1;
            item2=i2;
        }
        public boolean linksItem(N node){
            return node.equals(item1) || node.equals(item2);
        }
    }    
}

