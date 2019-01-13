import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MapVisualizer extends JFrame {
    static Set<Intersection> intersections;
    static double maxLon;
    static double minLon;
    static double maxLat;
    static double minLat;
    static double diffLon;
    static double diffLat;
    final static int SCREENWIDTH = 1500;
    final static int SCREENHEIGHT = 1000;
    final static int CIRCLEDIAM = 4;
    final static int BORDER = 100;
    
    public MapVisualizer(Set<Intersection> list) {
        intersections = list;
        setFrame();
        computeExtremes();
    }
    
    public final void setFrame() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Map");
        this.setSize(SCREENWIDTH, SCREENHEIGHT);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setResizable(false);
    }
    
    public final void computeExtremes() {
        maxLon = -9999999;
        minLon = 9999999;
        maxLat = -9999999;
        minLat = 9999999;
        for (Intersection inter : intersections) {
            if (inter.longitude > maxLon) {
                maxLon = inter.longitude;
            }
            if (inter.longitude < minLon) {
                minLon = inter.longitude;
            }
            if (inter.latitude > maxLat) {
                maxLat = inter.latitude;
            }
            if (inter.latitude < minLat) {
                minLat = inter.latitude;
            }
        }
        diffLon = maxLon - minLon;
        diffLat = maxLat - minLat;
    }
    
    @Override
    public void paint(Graphics g) {
        for (Intersection inter : intersections) {
            drawIntersection(g, inter);
            for(Road road : inter.getRoadsFrom()) {
                drawRoad(g, road);
            }
        }
    }
    
    public void drawIntersection (Graphics g, Intersection inter) {
        int x = BORDER + (int)(((inter.longitude - minLon)/diffLon) * (SCREENWIDTH - 2 * BORDER));
        int y = BORDER + (int)(((maxLat - inter.latitude)/diffLat) * (SCREENHEIGHT - 2 * BORDER));
        g.drawOval(x - CIRCLEDIAM/2, y - CIRCLEDIAM/2, CIRCLEDIAM, CIRCLEDIAM);
    }
    
    public void drawRoad (Graphics g, Road road) {
        Intersection inter1 = road.from;
        Intersection inter2 = road.to;
        int x1 = BORDER + (int)(((inter1.longitude - minLon)/diffLon) * (SCREENWIDTH - 2 * BORDER));
        int y1 = BORDER + (int)(((maxLat - inter1.latitude)/diffLat) * (SCREENHEIGHT - 2 * BORDER));
        int x2 = BORDER + (int)(((inter2.longitude - minLon)/diffLon) * (SCREENWIDTH - 2 * BORDER));
        int y2 = BORDER + (int)(((maxLat - inter2.latitude)/diffLat) * (SCREENHEIGHT - 2 * BORDER));
        g.drawLine(x1, y1, x2, y2);
        
    }
    
}
