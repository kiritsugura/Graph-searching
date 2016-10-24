
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.Stack;

/*Search through a graph*/
public class GraphSearch {
    public type tp;
    /*Type of search.*/
    public enum type{
        DEPTH_FIRST,BREADTH_FIRST,DIJKSTRA,A_STAR;
    }    
    /*default constructor.*/
    public GraphSearch(){
        tp=type.DEPTH_FIRST;
    }
    /*Specified constructor for a search.*/
    public GraphSearch(type t){
        tp=t;
    }
    /*@param:initial is the node the search starts at, finish is the 'stop' node if applicable.
      @Return: returns a Stack that contains the path from start to finish with start on the top.*/
    public Stack<DrawableGraph.DrawableNode> search(DrawableGraph.DrawableNode initial,DrawableGraph.DrawableNode finish){
        if(tp==type.A_STAR && finish!=null){
            return aStar(initial,finish);
        }else if(tp==type.BREADTH_FIRST){
            return bFSHelper(initial,finish);
        }else if(tp==type.DEPTH_FIRST){
            SearchNode init=new SearchNode(initial,null);    
            return getPath(dFSHelper(init,finish));            
        }else if(tp==type.DIJKSTRA){
            return djikstra(initial,finish);
        }
        return null;
    }
    /*A Depth First Search.*/
    public SearchNode dFSHelper(SearchNode start,DrawableGraph.DrawableNode finish){
        start.setSearchedType(false);
        Stack<DrawableGraph.DrawableNode> path=new Stack();
        for(DrawableGraph.DrawableNode neighbor: (ArrayList<DrawableGraph.DrawableNode>)start.neighbors()){         
            if(!neighbor.searched){
                SearchNode child=new SearchNode(neighbor,start);                 
                if(neighbor==finish && finish!=null){
                    child.setSearchedType(false);
                    return child;
                }               
                SearchNode n=dFSHelper(child,finish);
                if(n!=null){
                    return n;
                }
            }
        }    
        return null;
    }
    /*A Breadth First Search*/
    public Stack<DrawableGraph.DrawableNode> bFSHelper(DrawableGraph.DrawableNode start,DrawableGraph.DrawableNode finish){
        SearchNode init=new SearchNode(start,null);              
        ArrayList<SearchNode> front=new ArrayList();
        front.add(init);
        init.setSearchedType(false);
        while(front.size()>0){
            SearchNode current=front.remove(0);
            for(DrawableGraph.DrawableNode neighbor: (ArrayList<DrawableGraph.DrawableNode>)current.neighbors()){              
                if(!neighbor.searched){
                    SearchNode child=new SearchNode(neighbor,current);
                    front.add(child);    
                    System.out.println(front.size());
                    child.setSearchedType(false);
                    if(neighbor==finish && finish!=null){
                        child.setSearchedType(false);
                        return getPath(child);
                    }
                }
            }    
        }
        return null;        
    }
    /* A* shortest path search.*/
    public Stack<DrawableGraph.DrawableNode> aStar(DrawableGraph.DrawableNode start,DrawableGraph.DrawableNode finish){
        PriorityQueue<SearchNode> nodes=new PriorityQueue();
        SearchNode init=new SearchNode(start,null);
        nodes.add(init);
        while(true){
            SearchNode current=nodes.poll();
            current.setSearchedType(true);
            for(DrawableGraph.DrawableNode conn: (ArrayList<DrawableGraph.DrawableNode>)current.neighbors()){             
                if(!conn.searched){
                    SearchNode child=new SearchNode(conn,current);
                    child.distance=distance(conn.xCoor,conn.yCoor,finish.xCoor,finish.yCoor);
                    nodes.add(child);
                    if(conn==finish){
                        child.setSearchedType(true);
                        return getPath(child);
                    }
                }
            }
        }
    }    
    /*Simple distance formula.*/
    public float distance(float x1,float y1,float x2,float y2){
        return (float)Math.pow(Math.pow(x1-x2,2)+Math.pow(y1-y2,2),.5);
    }
    /*Dijkstra search.*/
    public Stack<DrawableGraph.DrawableNode> djikstra(DrawableGraph.DrawableNode start,DrawableGraph.DrawableNode finish){
        PriorityQueue<SearchNode> nodes=new PriorityQueue();
        SearchNode init=new SearchNode(start,null);
        nodes.add(init);
        int number=0;
        while(nodes.size()>0){
            SearchNode current=nodes.poll();
            current.setSearchedType(true);
            for(DrawableGraph.DrawableNode conn: (ArrayList<DrawableGraph.DrawableNode>)current.neighbors()){           
                if(!conn.searched){
                    number++;
                    SearchNode child=new SearchNode(conn,current);
                    nodes.add(child);
                    if(conn==finish){
                        child.setSearchedType(true);
                        return getPath(child);
                    }
                }
            }
        }
        return null;
    }
    /*@param: last is the final search node.
      @return: returns a stack that contains a stack of parent references.*/
    public Stack<DrawableGraph.DrawableNode> getPath(SearchNode last){
        SearchNode current=last;
        Stack<DrawableGraph.DrawableNode> path=new Stack();
        while(current!=null){
            path.add(current.corr);
            current=current.parent;
        }
        return path;
    }
}
/*A SearchNode used to store a DrawableNode and other necessary items for a search.*/
class SearchNode<N> implements Comparable{
    /*Corresponding node in drawable graph.*/
    public DrawableGraph.DrawableNode corr;
    /*Depth of the node*/
    public int depth;
    /*The heuristic cost and distance to the finish node.*/
    public float cost,distance;
    /*Parent node reference.*/
    public SearchNode parent;
    /*Default constructor.*/
    public SearchNode(DrawableGraph.DrawableNode twin,SearchNode prt){
        corr=twin;
        cost=0.0f;
        distance=0.0f;
        parent=prt;
        if(prt!=null){
            depth=prt.depth+1;
        }else{
            depth=1;
        }
    }
    /*Returns the neighbors of this node.*/
    public ArrayList<DrawableGraph.DrawableNode> neighbors(){
        return corr.getConnections();
    }
    /*Sets a node to searched.*/
    public void setSearchedType(boolean dij){
        if(parent!=null){
            DrawableGraph.DrawableEdge connection=parent.corr.findEdge(parent.corr, corr);
            connection.depth=parent.corr.depth;
            corr.searched=true;
            connection.searched=true;
            if(dij){
                cost=parent.cost+connection.cost();
            }else{
                cost=0.0f;
            }
        }else{
            corr.searched=true;
        }
    }
    /*Comparison used for heuristic and cost in searches.*/
    @Override
    public int compareTo(Object t) {
        if(((SearchNode)t).cost+((SearchNode)t).distance>cost+distance){
            return -1;
        }else if(((SearchNode)t).cost+((SearchNode)t).distance<cost+distance){
            return 1;
        }else{
            return 0;
        }
    }
}
