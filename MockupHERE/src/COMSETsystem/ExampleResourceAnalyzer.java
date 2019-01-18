package COMSETsystem;

import java.util.ArrayList;

public class ExampleResourceAnalyzer extends ResourceAnalyzerModule{

    private int sampleSize;
    // size of coefficient
    private int degree;
    private ArrayList<Intersection> prevResources;

    public ExampleResourceAnalyzer(Map map) {
        super(map);
        //list of all previous n resources
        prevResources = new ArrayList<>();
        //set the sample size of the previous resources to be considered
        sampleSize = 30;
        degree = 10;
    }

    void updateList(Intersection e){

        if(prevResources.size() == sampleSize ){

            prevResources.remove(1);
        }
        prevResources.add(e);
        giveStatistics();

    }

    private Intersection giveStatistics(){

        Intersection statistic = null;
        double latitude = 0;
        double longitude = 0 ;

        if(prevResources.size() == sampleSize){
            //least recent information saved is most important and is at postiton 1 of array
            //assign the biggest probability to the position 1 value
            for (int i = 0 ; i < sampleSize ; i++) {
                latitude += Math.pow((1/degree)*prevResources.get(i).latitude, i+1);
                longitude += Math.pow((1/degree)*prevResources.get(i).longitude, i+1);
            }

            // create the node statistic that will have the latitude and longitude created as above
            return null;
        }else{

            // sample size not big enough, do random walk instead
            return null;
        }

    }
}
