import java.util.*;

//Random walk search algorithm
//Moves randomly to one of the adjacent intersection of the current intersection
public class AgentNaive extends AgentModule {

    public AgentNaive(Intersection loc, ResourceAnalyzerModule resMod) {
        super(loc, resMod);
    }

    @Override
    Intersection move() {
        return (Intersection)getRandomObject(currentLoc.getAdjacentIntersections() );
    }

    //returns a random object from a collection
    private Object getRandomObject(Collection from) {
        Random rnd = new Random();
        int i = rnd.nextInt(from.size());
        return from.toArray()[i];
    }
}
