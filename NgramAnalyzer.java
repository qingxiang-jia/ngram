import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Map;

/**
 * Given the counts provided by Ngarm.java, this class generates a sorted counts (by occurrence),
 * with percentage of each gram.
 */
public class NgramAnalyzer
{
    @SuppressWarnings("unchecked")
    public static void printSortedCounts(Map<ByteBuffer, Integer> counts)
    {
        Map.Entry<ByteBuffer, Integer>[] entries = new Map.Entry[counts.size()];
        counts.entrySet().toArray(entries);
        Arrays.sort(entries, new EntryComparator());
        /** print out sorted <n-gram, count> pair **/
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<ByteBuffer, Integer> entry : entries) {
            System.out.println(Ngram.byteArrToString(entry.getKey().array(), sb) + ": " + entry.getValue());
        }
    }

}
