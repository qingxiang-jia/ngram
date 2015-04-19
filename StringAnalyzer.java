import java.util.HashSet;
import java.util.Set;

/**
 * Analyze the output of strings command, compute how many strings are in common.
 */
public class StringAnalyzer
{
    /**
     * Takes two file names, read the file, put each line in an entry of the array.
     * Compute the number of strings the two files have in common.
     * @param fileName1 Path of the first file.
     * @param fileName2 Path of the second file.
     */
    public static void intersectRate(String fileName1, String fileName2)
    {
        String strings1[] = new String(IOUtil.readFile(fileName1)).split("\\r?\\n"); // split by new line
        String strings2[] = new String(IOUtil.readFile(fileName2)).split("\\r?\\n");
        Set<String> set1 = new HashSet<String>();
        Set<String> set2 = new HashSet<String>();
        for (String phrase : strings1)
            set1.add(phrase);
        for (String phrase : strings2)
            set2.add(phrase);
        Set<String> intersect = new HashSet<String>(set1);
        intersect.retainAll(set2); // computes intersection two set1 and set2
        System.out.printf("%s and %s have %d strings in common, common rate \nwith " +
                "respect to %s is %f, \nwith respect to %s, is %f\n\n", fileName1, fileName2, intersect.size(),
                fileName1, intersect.size() / (float) set1.size(), fileName2, intersect.size() / (float) set2.size());
    }
}
