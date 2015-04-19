import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Given the counts provided by Ngarm.java, this class generates a sorted counts (by occurrence),
 * with percentage of each gram.
 */
public class NgramAnalyzer
{
    @SuppressWarnings("unchecked")
    public static void stringfySortedCounts(StringBuilder sBuilder, Map<ByteBuffer, Integer> counts, boolean showPercent)
    {
        sBuilder.setLength(0); // reset StringBuilder

        Map.Entry<ByteBuffer, Integer>[] entries = new Map.Entry[counts.size()];
        counts.entrySet().toArray(entries);
        Arrays.sort(entries, new EntryComparator());

        /** count percentage **/
        float[] percent = null;
        if (showPercent)
            percent = new float[counts.size()];
        int total = 0;
        for (int i = 0; i < entries.length; i++) {
            if (showPercent)
                percent[i] = entries[i].getValue();
            total += entries[i].getValue();
        }
        if (showPercent)
            for (int i = 0; i < percent.length; i++)
                percent[i] = percent[i] / total;

        /** print out sorted <n-gram, count> pair **/
        StringBuilder sb = new StringBuilder();
        if (showPercent) {
            sBuilder.append(String.format("%8s   %8s  %8s\n", "n-gram", "occurrence", "percent"));
            for (int i = 0; i < entries.length; i++) {
                sBuilder.append(String.format("%8s   %10d  %8f\n",
                        Ngram.byteArrToString(entries[i].getKey().array(), sb), entries[i].getValue(), percent[i]));
            }
        } else {
            sBuilder.append(String.format("%8s   %8s\n", "n-gram", "occurrence"));
            for (int i = 0; i < entries.length; i++) {
                sBuilder.append(String.format("%8s   %10d\n",
                        Ngram.byteArrToString(entries[i].getKey().array(), sb), entries[i].getValue()));
            }
        }
    }

    /**
     * For two n-gram distribution, compute how many grams are in common.
     * @param counts1
     * @param counts2
     */
    public static void intersectRate(Map<ByteBuffer, Integer> counts1, String fileName1,
                                     Map<ByteBuffer, Integer> counts2, String fileName2)
    {
        Set<ByteBuffer> set1 = counts1.keySet();
        Set<ByteBuffer> set2 = counts2.keySet();
        Set<ByteBuffer> intersect = new HashSet<ByteBuffer>(set1);
        intersect.retainAll(set2); // computes the intersect of set1 and set2
        float set1CommonRate = intersect.size() / (float) set1.size();
        float set2CommonRate = intersect.size() / (float) set2.size();
        int accuDiff = 0;
        for (ByteBuffer gram : intersect)
            accuDiff += Math.abs(counts1.get(gram) - counts2.get(gram));
        System.out.printf("%s has %d grams, %d of them are in common with %s, common rate: %f\n" +
                        "%s has %d grams, %d of them are in common with %s, common rate: %f\n" +
                        "average difference: %f\n\n",
                fileName1, set1.size(), intersect.size(), fileName2, set1CommonRate,
                fileName2, set2.size(), intersect.size(), fileName1, set2CommonRate,
                accuDiff / (float) intersect.size());
    }
}