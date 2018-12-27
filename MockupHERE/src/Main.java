import java.util.*;

public class Main {

	long score = 0;
	PriorityQueue<Event> events = new PriorityQueue<>(); 
	List<Intersection> intersections;
	HashMap<Intersection, ArrayDeque<Resource>> activeResources = new HashMap<>();
	ResourceAvailabilityModule resMod = new ResourceAvailability(intersections);
        
        MapReader mapReader;
        MapVisualizer mapVisualizer;
        
	Main () {
            createMap();
            intersections = Collections.unmodifiableList(intersections);
	}
        
        final void createMap() {
            mapReader = new MapReader();
            MapReader.readMap();
            System.out.println(MapReader.intersections().size());
            MapReader.clearMap();
            intersections = MapReader.intersections();
            System.out.println(intersections.size());
            mapVisualizer = new MapVisualizer(intersections);
        }
        
        void printIntersections() {
            for(Intersection inter : intersections) {
                System.out.println(inter.id);
            }
        }
	
	
	void run() throws Exception {
		while (!events.isEmpty()) {
			events.add(events.poll().trigger());
		}
	}
			
	abstract class Event implements Comparable<Event> {
		
		long time;
		
		Event (long time) {
			this.time = time;
		}
		
		/**
		 * function called when the Event needs to be executed
		 * @return new Event if needed
		 */
		abstract Event trigger() throws Exception;
		
		@Override
		public int compareTo(Event o) {
			return (int) (o.time - this.time);
		}
	}

	class AgentEvent extends Event {
		
		final Intersection loc;
		
		AgentEvent (Intersection loc, long available) {
			super(available);
			this.loc = loc;
		}

		@Override
		Event trigger() throws Exception {
			AgentModule agent = new Agent(loc, resMod);
			Intersection nextLoc = agent.move();
			
			if (!loc.isAdjacent(nextLoc)) {
				throw new Exception("move not made to an adjacent location");
			}
			
			Road nextRoad = loc.roadTo(nextLoc);
			
			if (!activeResources.get(nextLoc).isEmpty()) {
				Resource res = activeResources.get(nextLoc).pop();
				score += res.arrivalTime - nextRoad.time - time;
				return null;
			}
			
			return new AgentEvent(nextLoc, time + nextRoad.time);
		}
	}
}