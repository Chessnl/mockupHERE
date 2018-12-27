import java.io.Serializable;
import java.util.*;
import java.lang.Math.*;

public class Intersection implements Serializable {
	private static final long serialVersionUID = -6287246803998233161L;
	
	final public double longitude, latitude;
	final public int index; // unique
        final public long id;
        
        // from i to this
        private Map<Intersection, Road> roadsMapTo = new HashMap<>();
        
        // from this to i
        private Map<Intersection, Road> roadsMapFrom = new HashMap<>();
	
	Intersection (double longitude, double latitude, int index, long id) {
            this.longitude = longitude;
            this.latitude = latitude;
            this.index = index;
            this.id = id;
	}
	
	protected void addEdge (Intersection i, double distance, long speed) {
            if (this.id == i.id) {
                return;
            }
            Road r = new Road(this, i, distance, speed);
            roadsMapFrom.put(i, r);
            i.roadsMapTo.put(this, r);
	}
        
        protected void removeEdge (Intersection inter) {
            for (Intersection i : roadsMapFrom.keySet()) {
                if (i.equals(inter)) {
                    roadsMapFrom.remove(i);
                    i.roadsMapTo.remove(this);
                    return;
                }
            }
            throw new IllegalArgumentException("Trying to remove road" +
                        " that doesn't exist.");
        }
        
        protected void cutNode () {
            for (Road roadFrom : roadsMapFrom.values()) {
                for (Road roadTo : roadsMapTo.values()) {
                    roadTo.from.addEdge(roadFrom.to, roadFrom.distance + 
                            roadTo.distance, Math.min(roadFrom.speed, roadTo.speed));
                }
            }
            
            boolean check = true;
            while (check) {
                check = false;
                for (Intersection i : roadsMapFrom.keySet()) {
                    removeEdge(i);
                    check = true;
                    break;
                }
            }
            
            check = true;
            while (check) {
                check = false;
                for (Intersection i : roadsMapTo.keySet()) {
                    i.removeEdge(this);
                    check = true;
                    break;
                }
            } 
        }
	
	protected void fixStructure () {
            roadsMapFrom = Collections.unmodifiableMap(roadsMapFrom);
            roadsMapTo = Collections.unmodifiableMap(roadsMapTo);
	}
	
	protected boolean isAdjacent (Intersection i) {
            return (roadsMapFrom.keySet().contains(i) || roadsMapTo.keySet().contains(i));
	}
	
        // from this to i
	public Road roadTo (Intersection i) throws Exception {
            if (roadsMapFrom.keySet().contains(i)) {
                return roadsMapFrom.get(i);
            }
            throw new IllegalArgumentException("no road between " +
                            "this and i");
	}
	
	public Set<Road> getRoadsFrom () {
            return new HashSet<>(roadsMapFrom.values());
	}
        
        public Set<Road> getRoadsTo () {
            return new HashSet<>(roadsMapTo.values());
	}
	
	public Set<Intersection> getAdjacentFrom () {
            return roadsMapFrom.keySet();
	}
        
        public Set<Intersection> getAdjacentTo () {
            return roadsMapTo.keySet();
        }
        
        public boolean equals(Intersection inter) {
            return (inter.id == this.id);
        }
}
