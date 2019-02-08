package DataParsing;

import COMSETsystem.*;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.io.Serializable;

public class MapWithData implements Serializable {
    private static final long serialVersionUID = 3477769768992824431L;
    
    public CityMap map;
    private String dataPath;
    private PriorityQueue<Main.Event> events;

    public MapWithData (CityMap map, String dataPath) {
        this.map = map;
        this.dataPath = dataPath;
        events = new PriorityQueue<>();
    }
    
    public void createMapWithData(Main main) {
        
        CSVParser parser = new CSVParser(dataPath);
        ArrayList<TimestampAgRe> eventsParsed = parser.parse();

        try {
            System.out.println("start of for");
            long counter = 0;
            for (TimestampAgRe event : eventsParsed) {
                counter++;
                if (counter % 1000 == 0) {
                System.out.println(counter);
                }
                Intersection i = map.getNearestIntersection(event.getLon(), event.getLat());
                if (event.getType().equals("agent")) {
                    Main.AgentEvent ev = main.new AgentEvent(i, event.getTime());
                    events.add(ev);
                } else {
                    Main.ResourceEvent ev = main.new ResourceEvent(i, event.getTime());
                    events.add(ev);
                }
            }
            map.removeGrid();
            //map.fixStructure();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    public CityMap getMap() {
        return map;
    }
    
    public PriorityQueue<Main.Event> getEvents() {
        return events;
    }
}
