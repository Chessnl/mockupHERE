package DataParsing;

/**
 * Represents a state of an agent at a specific time.
 * Used to parse datasets. Each agent in a dataset has many timestamps.
 * A timestamp consists of a latitude, longitude, whether the agent was available and the time.
 */

public abstract class TimestampAbstract {

    private double lat;
    private double lon;
    private int time;

    public TimestampAbstract(double lat, double lon, int time) {
        this.lat = lat;
        this.lon = lon;
        this.time = time;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public int getTime() {
        return time;
    }

}