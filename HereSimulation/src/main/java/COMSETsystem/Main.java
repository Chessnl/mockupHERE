package COMSETsystem;

import MapCreation.*;
import UserExamples.Agent;
import UserExamples.ResourceAnalyzer;

import java.text.NumberFormat;
import java.util.*;
import DataParsing.*;
import java.io.*;

public class Main {

	private CityMap map;
	private PriorityQueue<Event> events;
	private ResourceAnalyzerModule resMod = new ResourceAnalyzer(map);
	private long score = 0;
	private HashSet<AgentEvent> activeAgents = new HashSet<>();
	private HashSet<ResourceEvent> activeResources = new HashSet<>();

	public Main (String filename) {
		Deserializer(filename);
		run();
	}



	void run() {
		
		// Getting the memory used
		Runtime runtime = Runtime.getRuntime();
		NumberFormat format = NumberFormat.getInstance();

		StringBuilder sb = new StringBuilder();
		long maxMemory = runtime.maxMemory();
		long allocatedMemory = runtime.totalMemory();
		long freeMemory = runtime.freeMemory();

		// probably unnecessary
		sb.append("Performance Report: " + "<br/>");
		sb.append("free memory: " + format.format(freeMemory / 1024) + "<br/>");
		sb.append("allocated memory: " + format.format(allocatedMemory / 1024)
				+ "<br/>");
		sb.append("max memory: " + format.format(maxMemory / 1024) + "<br/>");

		// still looking into this one "freeMemory + (maxMemory -
		// allocatedMemory)"
		sb.append("total free memory: "
				+ format.format(
						(freeMemory + (maxMemory - allocatedMemory)) / 1024)
				+ "<br/>");

		long startTime = System.nanoTime();

		try {
			while (!events.isEmpty()) {
				events.add(events.poll().trigger());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		long endTime = System.nanoTime();
		long totalTime = endTime - startTime;

		sb.append("running time: " + totalTime + "<br/>");
		sb.append("score: " + score + "<br/>");

		long allocatedMemoryAfter = runtime.totalMemory();
		sb.append("allocated memory: "
				+ format.format((allocatedMemoryAfter - allocatedMemory) / 1024)
				+ "<br/>");
	}

	// TODO? can this function be removed @Bobby
	final void createMap(String mapName) {
		MapCreator creator = new MapCreator(mapName);
		creator.clearAndGroup();
		creator.createGrid();
		creator.outputMapAndGrid();
	}
	
	void Serializer(String filename) {
		MapCreator creator = new MapCreator(filename);
		creator.clearAndGroup();
		creator.createGrid();
		MapWithData mapWD = new MapWithData(creator.outputCityMap(), "datasets/sftaxi_stream.csv");
		mapWD.createMapWithData(this);
		try {
			FileOutputStream fileOut = new FileOutputStream("maps/" + filename + "_map.sec");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(mapWD);		
			out.close();
			fileOut.close();
		} catch (IOException i) {
			i.printStackTrace();
		}
	}

	void Deserializer(String filename) {
		MapWithData mapWD = null;
		try {
			FileInputStream fileIn = new FileInputStream(
					"maps/" + filename + "_map.sec");
			ObjectInputStream in = new ObjectInputStream(fileIn);
			mapWD = (MapWithData) in.readObject();
			in.close();
			fileIn.close();
		} catch (IOException i) {
			i.printStackTrace();
			return;
		} catch (ClassNotFoundException c) {
			System.out.println("MapWithData class not found");
			c.printStackTrace();
			return;
		}
		events = mapWD.getEvents();
		map = mapWD.getMap();
	}

	/**
	 * Events correspond to either agents or resources becoming available. The
	 * function trigger() is invoked to handle the events. Each event has compareTo
	 * such that earlier events will appear earlier in a sorted datastructure.
	 */
	public abstract class Event implements Comparable<Event>, Serializable {

		private static final long serialVersionUID = -5581201375598360701L;

		long time;

		Event(long time) {
			this.time = time;
		}

		/**
		 * function called when the Event needs to be executed
		 * 
		 * @return new Event if needed
		 */
		abstract Event trigger() throws Exception;

		@Override
		public int compareTo(Event o) {
			return (int) (o.time - this.time);
		}
	}

	/**
	 * Agent events correspond to a single agent. This event maintains the location
	 * and state of a single agent. Each time it becomes available, it checks if
	 * there are ActiveResources to be assigned to. When this is the case, the
	 * agent chooses the nearest available resource. Else it will invoke the
	 * move() function implemented by the Users to find its next location. It throws
	 * an exception when the user tries to make an invalid move.
	 */
	
	public class AgentEvent extends Event {

		private static final long serialVersionUID = -1629720845574445021L;

		Intersection loc;
		final AgentModule agent;

		public AgentEvent(Intersection loc, long available) {
			super(available);
			this.loc = loc;
			agent = new Agent(loc, resMod);
		}

		@Override
		Event trigger() throws Exception {

			// if there are resources available that were not assigned
			if (!activeResources.isEmpty()) {
				// assign agent to the closest resource
				ResourceEvent bestChoice = null;
				long earliest = Long.MAX_VALUE;
				for (ResourceEvent resource : activeResources) {
					long reach = map.timeBetween(loc, resource.loc);
					if (reach < earliest) {
						bestChoice = resource;
						earliest = reach;
					}
				}
				// add the time from when resource became available till arrival
				score += earliest + time - bestChoice.time; 
				
				// remove chosen resource as activeResource
				activeResources.remove(bestChoice); 
													
				return null;
			}

			activeAgents.add(this); // indicate that this agent is active
			Intersection nextLoc = agent.move(); // ask where to move next
			if (!loc.isAdjacent(nextLoc))
				throw new Exception("move not made to an adjacent location");

			// update agentEvent to be put back on the event queue
			time += loc.roadTo(nextLoc).time;
			loc = nextLoc;
			return this;
		}
	}

	/**
	 * Resource events, as the name suggests are events denoting a resource that
	 * becomes available. It is alongside the agent events in the events queue.
	 * Upon Trigger(); it finds the nearest active agent and assigns itself to 
	 * this agent. When there are no active agents available, it will denote itself
	 * as being an ActiveResource and waits for an agent to assign himself to this
	 * resource. Each time a resource becomes available, the resource analyser
	 * module newResource() is invoked. 
	 */
	public class ResourceEvent extends Event {
		private static final long serialVersionUID = -8271159525284882873L;
		final Intersection loc;

		public ResourceEvent(Intersection loc, long available) {
			super(available);
			this.loc = loc;
		}

		@Override
		Event trigger() throws Exception {

			resMod.newResource(loc);

			// When a resource becomes available find nearest active Agent
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

			// When there are no active Agents
			if (bestChoice == null) { 
				// add this resource as active Resource
				activeResources.add(this);
				return null;
			}

			// Add score being the time from the nearest Agent to the Resource
			score += earliest - time;
			events.remove(bestChoice);
			activeAgents.remove(bestChoice);

			return null;
		}
	}
}
