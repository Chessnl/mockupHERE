package UserExamples;

import COMSETsystem.AgentModule;
import COMSETsystem.Intersection;
import COMSETsystem.ResourceAnalyzerModule;

import java.util.*;

/**
 * Random walk search algorithm:
 * Moves randomly to one of the adjacent intersection of the current intersection
 */
public class AgentRandomWalk extends AgentModule {

    public AgentRandomWalk(Intersection loc, ResourceAnalyzerModule resMod) {
        super(loc, resMod);
    }

    @Override
    public Intersection move() {
    	Random rnd = new Random();
        int i = rnd.nextInt(currentLoc.getAdjacentFrom().size());
        return (Intersection) currentLoc.getAdjacentFrom().toArray()[i];
    }
}
