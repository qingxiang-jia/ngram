import java.nio.ByteBuffer;
import java.util.Map;

/**
 * This class takes exactly 2 inputs and prints the pairwise comparison results of the five sample
 * programs.
 * It requires you have prog1 through prog5 in the same folder with the program
 * Usage: java NgramAnalyticalOutput n s in out
 * n    - an integer that is the length of the n-grams
 * s    - the length of the slide
 */
public class NgramAnalyticalOutput
{
    static final String usage = "Usage: java NgramAnalyticalOutput [n-grams length] [slide length]";
    public static void main(String[] args)
    {
        /** check input **/
        if (args.length != 2) {
            System.out.println(usage);
            System.exit(1);
        }
        int n = 0, s = 0;
        String in = null, out = null;
        try {
            n = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.out.println("The first argument should a number.");
            System.out.println(usage);
            System.exit(1);
        }
        try {
            s = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.out.println("The second argument should a number.");
            System.out.println(usage);
            System.exit(1);
        }
        if (n < 1 || n > 3 || s > n) {
            System.out.println("N-grams length should be between 1 and 3;\n slide length should be between 1 and n.");
            System.exit(1);
        }
        /** pairwise comparison **/
        String[] fileNames = new String[]{"prog1", "prog2", "prog3", "prog4", "prog5"};
        for (int i = 0; i < fileNames.length; i++)
            for (int j = i + 1; j < fileNames.length; j++) {
                Map<ByteBuffer, Integer> counts1 = Ngram.count(fileNames[i], 3, 1);
                Map<ByteBuffer, Integer> counts2 = Ngram.count(fileNames[j], 3, 1);
                NgramAnalyzer.intersectRate(counts1, fileNames[i], counts2, fileNames[j]);
            }
    }
}
