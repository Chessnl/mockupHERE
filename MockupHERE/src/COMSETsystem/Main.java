package COMSETsystem;

import MapCreation.MapReader;

import java.text.NumberFormat;
import java.util.*;

public class Main {

    private Map map;
	ResourceAnalyzerModule resMod = new ResourceAnalyzer(map);
	private long score = 0;
	private PriorityQueue<Event> events = new PriorityQueue<>();
	private HashSet<AgentEvent> activeAgents = new HashSet<>();
	private HashSet<ResourceEvent> activeResources = new HashSet<>();



	MapReader mapReader;

	Main () throws Exception {
		// @TODO loads map and the resource streams
		//createMap();
        Initializer();

		// @TODO events; needs to set events
		run();
	}

	// @TODO the code for creating a map, should not be in COMSETsystem.Main. This considers building an unique COMSETsystem.test-input
//	final void createMap() {
//		mapReader = new MapReader();
//		MapReader.readMap();
//		System.out.println(MapReader.intersections().size());
//		MapReader.clearMap();
//
//		// @TODO, this should be changed, such that mapreader creates a map object instead of intersections
//		// intersections = MapCreation.MapReader.intersections();
//		System.out.println(map.getIntersections().size());
//	}
	
	
	void run() throws Exception {

		//Getting the memory used
		Runtime runtime = Runtime.getRuntime();
		NumberFormat format = NumberFormat.getInstance();

		StringBuilder sb = new StringBuilder();
		long maxMemory = runtime.maxMemory();
		long allocatedMemory = runtime.totalMemory();
		long freeMemory = runtime.freeMemory();

		//probably unnecessary
		sb.append("Performance Report: " + "<br/>");
		sb.append("free memory: " + format.format(freeMemory / 1024) + "<br/>");
		sb.append("allocated memory: " + format.format(allocatedMemory / 1024) + "<br/>");
		sb.append("max memory: " + format.format(maxMemory / 1024) + "<br/>");

		//still looking into this one "freeMemory + (maxMemory - allocatedMemory)"
		sb.append("total free memory: " + format.format((freeMemory + (maxMemory - allocatedMemory)) / 1024) + "<br/>");

		long startTime = System.nanoTime();

		try {
			while (!events.isEmpty()) {
				events.add(events.poll().trigger());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		long endTime   = System.nanoTime();
		long totalTime = endTime - startTime;

		sb.append("running time: " + totalTime + "<br/>");
		sb.append("score: " + score + "<br/>");

		long allocatedMemoryAfter = runtime.totalMemory();
		sb.append("allocated memory: " + format.format((allocatedMemoryAfter - allocatedMemory) / 1024) + "<br/>");


	}

	void Initializer() {
        ArrayDeque<ResourceEvent> resourceStream = new ArrayDeque<>();
        ArrayDeque<AgentEvent> agentStream = new ArrayDeque<>();
        for(ResourceEvent r: resourceStream) {
            events.add(r);
        }
        for(AgentEvent a: agentStream) {
            events.add(a);
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
			agent = new AgentNaive(loc, resMod);
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
