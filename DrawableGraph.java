
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Transform;

/*A Graph that can be drawn when given a formated file. Can extend an AjacencyGraph or MatrixGraph.*/
public class DrawableGraph<N,E> extends AdjacencyGraph{
    /*HashSet of Drawable Nodes.*/
    private HashSet<DrawableNode> nodes;
    /*HashSet of drawable edges.*/
    private HashSet<DrawableEdge> drEdge;
    /*The Misc Lines present in the file format.*/
    private ArrayList<Float[]> pLines;
    /*If true, draws the name of the edge or node.*/
    public boolean showName;
    /*Constructor for a drawable graph.
      @param: filen is the name of the file that contains the graph data.*/
    public DrawableGraph(String filen){
        nodes=new HashSet(80,20,.8);
        drEdge=new HashSet(80,20,.8);
        pLines=new ArrayList();
        showName=false;
        try{
            super.loadFromFile(filen);
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    public int getNodeSize(){
        return nodes.getSize();
    }
    /*Contains method for this graph so a node can be selected.*/
    public DrawableNode contains(float mx, float my,float xTrans,float yTrans,float zf){
        HashMapIterator it=nodes.iterator(true);
        int index=0;
        while(it.hasNext()){
            DrawableNode node=(DrawableNode)it.next();
            if(node.contains(mx, my,xTrans,yTrans,zf)){
                return node;
            }
            index++;
        }
        return null;
    }
    /*Processes a line and adds items to the graph based on the file contents.
      @param: line is the current line of data.*/
    public void processFileLine(String line){
        String[] lines=line.split(" ");
            if(lines[0].indexOf("n")>=0){
                N n=(N)lines[1];
                super.addNode(n);
                Node node=super.getLast();
                nodes.add(new DrawableNode(node,Float.valueOf(lines[2]),Float.valueOf(lines[3]),Float.valueOf(lines[4])));
            }else if(lines[0].indexOf("e")>=0){
                Node node1=getNodeNum(Integer.valueOf(lines[1])),node2=getNodeNum(Integer.valueOf(lines[2]));
                Edge newEdge=new Edge((E)lines[3],node1,node2);
                if(super.addEdge(newEdge, node1, node2, false)){
                    drEdge.add(new DrawableEdge(newEdge,getDNode(Integer.valueOf(lines[1])),getDNode(Integer.valueOf(lines[2]))));
                }
            }else if(lines[0].indexOf("L")>=0){
                Float xp1=Float.valueOf(lines[1]),yp1=Float.valueOf(lines[2]),xp2=Float.valueOf(lines[3]),yp2=Float.valueOf(lines[4]);
                Float[] points=new Float[4];
                points[0]=xp1;
                points[1]=yp1;
                points[2]=xp2;
                points[3]=yp2;
                pLines.add(points);
            }   
    }
    /*@param: an index in the drawable node hashset nodes.
      @return: returns the DrawableNode with the index 'index'.*/
    public DrawableNode getDNode(int index){
        HashMapIterator it=nodes.iterator(true);
        while(it.hasNext()){
            DrawableNode drn=(DrawableNode)it.next();
            if(drn.getIndex()==index){
                return drn;
            }
        }
        return null;
    }
    /*Resets the current search.*/
    public void reset(){
        Iterator iter=nodes.iterator(true);
        Iterator it=drEdge.iterator(true);
        while(it.hasNext()){
            ((DrawableEdge)it.next()).searched=false;
        }
        while(iter.hasNext()){
            ((DrawableNode)iter.next()).searched=false;
        }              
    }
    /*Returns a drawable edge that connects start and end nodes.*/
    public DrawableEdge getDEdge(DrawableNode start,DrawableNode end){
        HashMapIterator it=drEdge.iterator(true);
        while(it.hasNext()){
            DrawableEdge drn=(DrawableEdge)it.next();
            if((drn.con1==start && drn.con2==end) ||(drn.con2==start && drn.con1==end)){
                return drn;
            }
        }
        return null;        
    }
    /*Draw the current DrawableGraph.*/
    public void draw(Graphics g){
        Iterator iter=nodes.iterator(true);
        Iterator it=drEdge.iterator(true);
        Iterator listI=pLines.iterator();
        while(it.hasNext()){
            ((DrawableEdge)it.next()).draw(g);
        }
        while(iter.hasNext()){
            ((DrawableNode)iter.next()).draw(g,false);
        }       
        while(listI.hasNext()){
            Float[] next=(Float [])listI.next();
            g.setColor(Color.blue);
            g.drawLine(next[0],next[1],next[2],next[3]);
        }
    }
    /*A drawable node.*/
    public class DrawableNode{
        public float xCoor,yCoor,diameter;
        public Node me;
        public boolean searched;
        public int depth;
        public DrawableNode(Node node,float x,float y,float r){
            xCoor=x;
            yCoor=y;
            diameter=r;
            me=node;
            searched=false;
        }
        public void draw(Graphics g,boolean selected){
            if(searched){
                if(depth<90){
                    g.setColor(new Color(255,255,0,1.0f-((float)depth)*.01f));
                }else{
                    g.setColor(new Color(255,255,0,.09f));
                }
            }else if(!selected){
                g.setColor(Color.white);         
            }else{
                g.setColor(Color.yellow);
            }
            g.fillOval(xCoor,yCoor, diameter,diameter);
            if(showName){ 
                g.setColor(Color.red);
                Font off=g.getFont();
                g.drawString(me.toString(), xCoor-off.getWidth(me.toString())/2,yCoor-off.getHeight(me.toString()));
            }
        }
        public int getIndex(){
            return me.index;
        }
        /*Returns the neighbors of this drawablenode*/
        public ArrayList<DrawableNode> getConnections(){
            ArrayList<Node> nds=me.getConnections();
            HashMapIterator it=nodes.iterator(true);
            ArrayList<DrawableNode> drCon=new ArrayList();
            while(it.hasNext()){
                DrawableNode next=(DrawableNode)it.next();
                if(nds.contains(next.me)){
                    drCon.add(next);
                }
            }
            return drCon;
        }
        public boolean contains(float mx,float my,float xDis,float yDis,float zoom){
            return Math.pow(Math.pow(mx-(xCoor+diameter/2+xDis)*zoom,2)+Math.pow(my-(yCoor+diameter/2+yDis)*zoom,2), .5)<diameter/2*zoom;
        }
        public DrawableEdge findEdge(DrawableNode start,DrawableNode end){
            return getDEdge(start,end);
        }
    }
    /*Used for djikstra and A* searches.*/
    public abstract class dVal{
        float cost;
    }
    /*A drawable edge.*/
    public class DrawableEdge extends dVal implements Comparable{
        public Edge me;
        public DrawableNode con1,con2;
        public boolean searched;
        public float depth;
        public DrawableEdge(Edge edge,DrawableNode d1,DrawableNode d2){
            me=edge;
            con1=d1;
            con2=d2;
            searched=false;
            //cost=Float.valueOf((float)me.edgeVal);
            if(me.edgeVal instanceof String){
                cost=Float.parseFloat((String)me.edgeVal);
            }
        }
        public float cost(){
            return cost;
        }
        //Proper alpha
        public void draw(Graphics g){
            if(searched){
                if(depth<90){
                    g.setColor(new Color(255,255,0,1.0f-((float)depth)*.01f));
                }else{
                    g.setColor(new Color(255,255,0,.09f));
                }
            }else{
                g.setColor(Color.red);
            }
            g.drawLine(con1.xCoor+con1.diameter/2,con1.yCoor+con1.diameter/2,con2.xCoor+con2.diameter/2,con2.yCoor+con2.diameter/2);
            if(showName){
                g.setColor(Color.white);
                String s=me.edgeVal.toString().substring(0,me.edgeVal.toString().indexOf('.')+2);
                Font off=g.getFont();
                g.fillRect(((con1.xCoor+con2.xCoor)/2)-off.getWidth(s)/2, ((con1.yCoor+con2.yCoor)/2)-off.getHeight(s)/2,off.getWidth(s),off.getHeight(s));
                g.setColor(Color.black);
                g.drawString(s,((con1.xCoor+con2.xCoor)/2)-off.getWidth(s)/2,((con1.yCoor+con2.yCoor)/2)-off.getHeight(s)/2);
            }
        }

        @Override
        public int compareTo(Object t) {
            if(((DrawableEdge)t).cost()>cost){
                return -1;
            }else if(((DrawableEdge)t).cost()<cost){
                return 1;
            }else{
                return 0;
            }
        }
    }
    
}
