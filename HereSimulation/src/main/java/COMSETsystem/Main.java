package COMSETsystem;

import MapCreation.*;
import java.text.NumberFormat;
import java.util.*;
import DataParsing.*;
import java.io.*;

public class Main implements Serializable {

        private CityMap map;
	ResourceAnalyzerModule resMod = new ResourceAnalyzer(map);
	private long score = 0;
	private PriorityQueue<Event> events = new PriorityQueue<>();
	private HashSet<AgentEvent> activeAgents = new HashSet<>();
	private HashSet<ResourceEvent> activeResources = new HashSet<>();



	public void create() throws Exception {
                // to create a new map, see ReadMe
		//createMap("SanFrancisco");
                
                // To visualize the map, see ReadMe 
                // MapVisualizer visualizer = new MapVisualizer(map, true);

                Serializer("SanFrancisco");
                Deserializer("SanFrancisco");
                
		run();
	}

	final void createMap(String mapName) {
		MapCreator creator = new MapCreator(mapName);
		creator.clearAndGroup();
                creator.createGrid();
                creator.outputMapAndGrid();
                
	}
	
	
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
                FileInputStream fileIn = new FileInputStream("maps/" + filename + "_map.sec");
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

	public abstract class Event implements Comparable<Event>, Serializable {

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

	public class AgentEvent extends Event {

		Intersection loc;
		final AgentModule agent;

		public AgentEvent (Intersection loc, long available) {
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

	public class ResourceEvent extends Event {

		final Intersection loc;

		public ResourceEvent (Intersection loc, long available) {
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
