import java.nio.ByteBuffer;
import java.util.Map;

/**
 * This class takes exactly 4 inputs and outputs the n-gram along with the counts as required by the assignment.
 * Usage: java NgramSTDOutput n s in out
 * n    - an integer that is the length of the n-grams
 * s    - the length of the slide
 * in   - the name of the file to analyze
 * out  - the name of the output file
 */
public class NgramSTDOutput
{
    static final String usage = "Usage: java NgramSTDOutput [n-grams length] [slide length] [input file name] [output file name]";
    public static void main(String[] args)
    {
        /** check input **/
        if (args.length != 4) {
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
        in = args[2];
        out = args[3];

        /** output n-gram and associated counts to file **/
        Map<ByteBuffer, Integer> counts = Ngram.count(in, n, s);
        StringBuilder sb = new StringBuilder();
        NgramAnalyzer.stringfySortedCounts(sb, counts, false);
        IOUtil.stringToFile(out, sb.toString());
    }
}
