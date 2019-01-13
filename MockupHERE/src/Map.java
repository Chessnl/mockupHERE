import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

public class Map implements Serializable {
	private static final long serialVersionUID = -8020298097735108102L;

	private Set<Intersection> intersections = new HashSet<>();
	private Set<Road> roads = new HashSet<>();
	private HashMap<Intersection, HashMap<Intersection, Long>> distance = new HashMap<>();

	public Set<Intersection> getIntersections() {
		return intersections;
	}

	public Set<Road> getRoads() {
		return roads;
	}

	public long timeBetween (Intersection start, Intersection stop) {
		return distance.get(start).get(stop);
	}

	protected void addIntersection(Intersection i) {
		intersections.add(i);
	}

	protected void addIntersection(double longitude, double latitude, long id) {
		intersections.add(new Intersection(longitude, latitude, id));
	}

	protected void addRoad (Intersection from, Intersection to, double distance, long speed) {
		from.addEdge(to, distance, speed);
	}

	protected void fixStructure () {
		for (Intersection i : intersections) {
			i.fixStructure();
			roads.addAll(i.getAdjacentRoads());
		}
		calcDistances();
		intersections = Collections.unmodifiableSet(intersections);
		roads = Collections.unmodifiableSet(roads);
	}

	protected void calcDistances() {
		for (Intersection from : intersections) {
			HashMap<Intersection, DijkstraQueueEntry> queueEntry = new HashMap<>();
			PriorityQueue<DijkstraQueueEntry> queue = new PriorityQueue<>();
			for (Intersection i : intersections) {
				queueEntry.put(i, new DijkstraQueueEntry(i));
				queue.add(queueEntry.get(i));
			}
			DijkstraQueueEntry fromEntry = queueEntry.get(from);
			queue.remove(fromEntry);
			fromEntry.cost = 0;
			queue.add(fromEntry);

			while (!queue.isEmpty()) {
				DijkstraQueueEntry entry = queue.poll();
				entry.inQueue = false;

				for (Road r : entry.i.getAdjacentRoads()) {
					DijkstraQueueEntry v = queueEntry.get(r.to);
					if (!v.inQueue)
						continue;
					long ncost = entry.cost + r.time;
					if (v.cost > ncost) {
						queue.remove(v);
						v.cost = ncost;
						queue.add(v);
					}
				}
			}

			HashMap<Intersection, Long> fromDistances = new HashMap<>();
			for (DijkstraQueueEntry entry : queueEntry.values()) {
				fromDistances.put(entry.i, entry.cost);
			}
			distance.put(from, fromDistances);
		}
	}

	private class DijkstraQueueEntry implements Comparable<DijkstraQueueEntry> {
		Intersection i;
		long cost = Long.MAX_VALUE;
		boolean inQueue = true;

		DijkstraQueueEntry(Intersection i) {
			this.i = i;
		}

		@Override
		public int compareTo(DijkstraQueueEntry j) {
			if (this.cost < j.cost) {
				return -1;
			} else if (this.cost > j.cost) {
				return 1;
			} else {
				return 0;
			}
		}

	}
}
