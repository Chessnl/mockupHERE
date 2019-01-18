package DataParsing;

/**
 * Represents an agent in the orignal XML dataset.
 * Each agent has a list of timestamps.
 */

import java.util.ArrayList;

public class AgentDataset {

    private String ID;
    private String update;
    private ArrayList<TimestampDataset> timestamps = new ArrayList();

    public void setID (String ID) {
        this.ID = ID;
    }

    public String getID () {
        return this.ID;
    }

    public String getUpdate() {
        return update;
    }

    public void setUpdate (String update) {
        this.update = update;
    }

    public void addStamp(TimestampDataset timestamp) {
        timestamps.add(timestamp);
    }

    public ArrayList<TimestampDataset> getStamps() {
        return timestamps;
    }

}
