import java.util.*;

public class Main {

	private long score = 0;
	private PriorityQueue<Event> events = new PriorityQueue<>();
	private HashSet<AgentEvent> activeAgents = new HashSet<>();
	private HashSet<ResourceEvent> activeResources = new HashSet<>();

	private Map map;

	ResourceAvailabilityModule resMod = new ResourceAvailability(map);

	MapReader mapReader;
	MapVisualizer mapVisualizer;

	Main () {
		// @TODO loads map and the resource streams
		createMap();
		ArrayDeque<ResourceEvent> resourceStream = new ArrayDeque<>();
		ArrayDeque<AgentEvent> agentStream = new ArrayDeque<>();

		// @TODO events; needs to set events
		run();
	}

	// @TODO the code for creating a map, should not be in Main. This considers building an unique test-input
	final void createMap() {
		mapReader = new MapReader();
		MapReader.readMap();
		System.out.println(MapReader.intersections().size());
		MapReader.clearMap();
		
		// @TODO, this should be changed, such that mapreader creates a map object instead of intersections
		// intersections = MapReader.intersections();
		System.out.println(map.getIntersections().size());
		mapVisualizer = new MapVisualizer(map.getIntersections());
	}

	void run() {
		try {
			while (!events.isEmpty()) {
				events.add(events.poll().trigger());
			}
		} catch (Exception e) {
			e.printStackTrace();
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

		Intersection loc;
		final AgentModule agent;

		AgentEvent (Intersection loc, long available) {
			super(available);
			this.loc = loc;
			agent = new Agent(loc, resMod);
		}

		@Override
		Event trigger() throws Exception {
			// if there are resources available that were not assigned, assign agent to the closest resource
			if (!activeResources.isEmpty()) {
				ResourceEvent bestChoice = null;
				long earliest = Long.MAX_VALUE;
				for (ResourceEvent resource : activeResources) {
					long reach = map.timeBetween(loc, resource.loc);
					if (reach < earliest) {
						bestChoice = resource;
						earliest = reach;
					}
				}

				score += earliest + time - bestChoice.time;
				activeResources.remove(bestChoice);
				return null;
			}

			activeAgents.add(this);
			Intersection nextLoc = agent.move();
			if (!loc.isAdjacent(nextLoc)) throw new Exception("move not made to an adjacent location");
			time += loc.roadTo(nextLoc).time;
			loc = nextLoc;
			return this;
		}
	}

	class ResourceEvent extends Event {

		final Intersection loc;

		ResourceEvent (Intersection loc, long available) {
			super(available);
			this.loc = loc;
		}

		@Override
		Event trigger() throws Exception {
			activeResources.remove(this);
			AgentEvent bestChoice = null;
			long earliest = Long.MAX_VALUE;
			for (AgentEvent agent : activeAgents) {
				long reach = map.timeBetween(agent.loc, loc) + agent.time;
				if (reach < earliest) {
					bestChoice = agent;
					earliest = reach;
				}
			}

			if (bestChoice == null) {
				activeResources.add(this);
				return null;
			}

			score += earliest - time;
			events.remove(bestChoice);
			activeAgents.remove(bestChoice);

			return null;
		}
	}
}
