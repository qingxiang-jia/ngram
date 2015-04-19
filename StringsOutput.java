/**
 * This program does a pairwise comparison on each pair of strings of the program to compute how
 * many string they have in common.
 *      Usage: java StringOutput [no arguments!]
 * To run, you must have the results of strings on each of the five files in the same directory
 * with this program, and name them as:
 * prog1_strings.txt, prog2_strings.txt, prog3_strings.txt, prog4_strings.txt, prog5_strings.txt
 */
public class StringsOutput
{
    public static void main(String[] args)
    {
        String[] fileNames = new String[]{"prog1_strings.txt", "prog2_strings.txt", "prog3_strings.txt",
                "prog4_strings.txt", "prog5_strings.txt"};
        for (int i = 0; i < fileNames.length; i++) // pairwise calculation for similarity
            for (int j = i + 1; j < fileNames.length; j++)
                StringAnalyzer.intersectRate(fileNames[i], fileNames[j]);
    }
}
