package DataParsing;

/**
 * The type of timestamp used in the DataParsing.CSVParser.
 * It extends the abstract timestamp with a type field, which is either Agent or Resource.
 */

public class TimestampAgRe extends TimestampAbstract {

    private String type;

    public TimestampAgRe (double lat, double lon, int time, String type) {
        super(lat, lon, time);
        this.type = type;

    }

    public String getType() {
        return type;
    }
}