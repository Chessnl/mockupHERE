package DataParsing;

/**
 * The type of timestamp used in the DataParsing.XMLParser.
 * It extends the abstract timestamp with an availability field.
 */

public class TimestampDataset extends TimestampAbstract {

    private boolean avail;

    public TimestampDataset (double lat, double lon, boolean avail, int time) {
        super(lat, lon, time);
        this.avail = avail;
    }

    public boolean getAvail() {
        return avail;
    }
}

