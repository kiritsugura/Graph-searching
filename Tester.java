
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.*;

//Change between searches.
public class Tester extends BasicGame implements MouseListener,KeyListener{
    /*The current zoom aspect.*/
    private float zoomFactor=1.0f,dx,dy,x,y;
    /*The graph that is currently being drawn.*/
    private DrawableGraph graph;
    /*Boolean for if a transaltion occurs, boolean for if a zoom occured.*/
    private boolean wasShifted,wasZM;
    /*The current Graphics Object being used for rendering.*/
    private Graphics drawing;
    private DrawableGraph.DrawableNode initial,destination;
    private GraphSearch search;
    private String[] maps;
    private int mapNum;
    /*Constructor for Tester.
      @param: The name of this GUI.*/
    public Tester(String title){
        super(title);
        wasShifted=false;
        x=0;
        y=0;
        search=new GraphSearch();
        String[] maps={"map01.txt","map02.txt","map03.txt","map04.txt"};
        this.maps=maps;
        mapNum=0;
        
    }
    @Override
    public void mouseWheelMoved(int change){
        /*Change the zoom.*/
        if(change>0 && zoomFactor<8.0){
            zoomFactor+=.20;
            x=0;
            y=0;
        }else if(zoomFactor>.60){
            zoomFactor-=.20;
            x=0;
            y=0;
        }
    }
    public void mouseClicked(int button, int x, int y, int clickCount){
        DrawableGraph.DrawableNode node=graph.contains(x,y,this.x,this.y,zoomFactor);
        if(node!=null){
            if(initial==null){
                initial=node;
            }else if(initial==node){
                initial=null;
            }else if(destination==null){
                destination=node;           
                Stack<DrawableGraph.DrawableNode> path=search.search(initial, destination); 
                System.out.println(path.size());
                while(path.size()>0){
                    System.out.println(path.pop().me);
                }
            }else if(destination==node){
                destination=null;
            }
        }
    }
    @Override
    public void keyPressed(int key,char c){
        /*Translate the screen.*/
        if(c=='w'){
            dy=.80f;
            wasShifted=true;
        }else if(c=='d'){
            dx=-.80f;
            wasShifted=true;
        }else if(c=='s'){
            dy=-.80f;
            wasShifted=true;
        }else if(c=='a'){
            dx=.80f;
            wasShifted=true;
        //toggle names of edges and nodes.    
        }else if(c=='e'){
            graph.showName=!graph.showName;
        }else if(c=='u'){
            if(destination==null){
                search.search(initial,null);
            }
        /*Reset the search.*/     
        }else if(c=='r'){
            graph.reset();
            destination=null;
            initial=null;
        /*Change the map*/    
        }else if(c=='m'){
            mapNum++;
            if(mapNum>4){
                mapNum=0;
            }
            graph=new DrawableGraph(maps[mapNum]);
        }else if(c=='n'){
            mapNum--;
            if(mapNum<0){
                mapNum=3;
            }
            graph=new DrawableGraph(maps[mapNum]);            
        }else if(c=='c'){
            changeType(search.tp);
        }
            
    }
    public void changeType(GraphSearch.type ty){
        if(ty==GraphSearch.type.A_STAR){
            search=new GraphSearch(GraphSearch.type.BREADTH_FIRST);
			System.out.println("Current: Breadth-first.");
        }else if(ty==GraphSearch.type.BREADTH_FIRST){
            search=new GraphSearch(GraphSearch.type.DEPTH_FIRST);
			System.out.println("Current: depth-first.");
        }else if(ty==GraphSearch.type.DEPTH_FIRST){
            search=new GraphSearch(GraphSearch.type.DIJKSTRA);
			System.out.println("Current: Djikstra.");
        }else if(ty==GraphSearch.type.DIJKSTRA){
            search=new GraphSearch(GraphSearch.type.A_STAR);
			System.out.println("Current: A-star.");
        }
    }
    @Override
    public void keyReleased(int key,char c){
        /*Stop the translation.*/
        if(c=='w' && wasShifted){
            dy=0;
        }else if(c=='d' && wasShifted){
            dx=0;
        }else if(c=='s' && wasShifted){
            dy=0;
        }else if(c=='a' && wasShifted){
            dx=0;
        }    
        if(dx==0 && dy==0){
            wasShifted=false;
        }
    }   
    @Override
    //////////////////////////////////////////////////////////////////////////////////////////////////////
    public void init(GameContainer gc) throws SlickException{ 
        gc.getInput().addMouseListener(this);
        graph=new DrawableGraph("map01.txt");
    }
    @Override
    public void update(GameContainer gc, int i) throws SlickException { 
        /*Update the translation.*/
        x+=dx*(float)i;
        y+=dy*(float)i;
    }
    @Override
    public void render(GameContainer gc, Graphics g) throws SlickException{
        drawing=g;
        drawing.scale(zoomFactor,zoomFactor);
        g.translate(x, y);
        graph.draw(g);
        if(initial!=null){
            initial.draw(g, true);
        }
        if(destination!=null){
            destination.draw(g, true);
        }
    }    
    public static void main(String[] args)
    {
        try
        {
            AppGameContainer appgc;
            Tester my_app = new Tester("Graphs");
            appgc = new AppGameContainer(my_app);
            appgc.setDisplayMode(640,480, false);
            appgc.start();
        }
        catch (SlickException ex)
        {
            Logger.getLogger(Tester.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
