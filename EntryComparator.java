import java.nio.ByteBuffer;
import java.util.Comparator;
import java.util.Map;

/**
 * Enables sorting on Map.Entry by val in <key, val> of an Entry object in descending order.
 */
public class EntryComparator implements Comparator<Map.Entry<ByteBuffer, Integer>>
{
    public int compare(Map.Entry<ByteBuffer, Integer> entry1, Map.Entry<ByteBuffer, Integer> entry2)
    {
        if (entry1.getValue() > entry2.getValue())
            return -1;
        else if (entry1.getValue() < entry2.getValue())
            return 1;
        else
            return 0;
    }
}
