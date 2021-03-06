import java.io.*;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * N-gram counter that counts the occurrence of each byte in a binary file.
 */
public class Ngram
{
    static private final int BUF_SIZE = 16000; // set buffer size big to minimize number of system i/o calls

    /**
     * Read a binary file in buffer, perform n-gram counting until the file is finished.
     * @param path Path to the file to count.
     * @param n An integer that is the length of the n-grams
     * @param s The length of the slide.
     * @return The Map<n-gram, count> for further calculation.
     */
    public static Map<ByteBuffer, Integer> count(String path, int n, int s)
    {
        /** set up stream to read from file **/
        File programFile = new File(path);
        FileInputStream fileInputStream  = null;
        try {
            fileInputStream = new FileInputStream(programFile);
        } catch (FileNotFoundException e) {
            System.out.println(path + " not found.");
        }
        if (fileInputStream == null) return null;
        InputStream inputStream = new BufferedInputStream(fileInputStream, BUF_SIZE);
        int bytesRead = 0;
        byte[] programData = new byte[BUF_SIZE];

        /** start counting **/
        Map<ByteBuffer, Integer> counts = new HashMap<ByteBuffer, Integer>();

        while (bytesRead != -1) {
            try {
                bytesRead = inputStream.read(programData);
                /** count byte distribution **/
                if (bytesRead > 0)
                    count(programData, bytesRead, counts, n, s);
            } catch (IOException e) {
                System.out.println("Failed to read file.");
                try {
                    fileInputStream.close();
                } catch (IOException e1) {
                    System.out.println("Failed to close fileInputStream.");
                }
                try {
                    inputStream.close();
                } catch (IOException e2) {
                    System.out.println("Failed to close inputStream.");
                }
                return null;
            }
        }
        try {
            fileInputStream.close();
        } catch (IOException e) {
            System.out.println("Failed to close fileInputStream.");
        }
        try {
            inputStream.close();
        } catch (IOException e) {
            System.out.println("Failed to close inputStream.");
        }
        return counts;
    }

    /**
     * This is the universal count method that takes care of all possible n and s values. Under the
     * hood, the has a mapping between the n-gram (ByteBuffer) and the counts. I use ByteBuffer
     * instead of byte[] because the former uses the contents of the bytes as key, not the memory
     * location, thus I can retrieve the key without having to have a reference to the original n-gram.
     * If the remaining bytes does not fill the sliding window fully, discard the last window.
     * @param programData The array that contains binary content of a program file.
     * @param bytesRead Since programData may not necessarily fully filled, use this as for loop boundary.
     * @param n An integer that is the length of the n-grams
     * @param s The length of the slide.
     */
    private static void count(byte[] programData, int bytesRead, Map<ByteBuffer, Integer> counts, int n, int s)
    {
        int end = bytesRead - n + 1;
        for (int windowStart = 0; windowStart < end; windowStart += s) {
            /** sliding window **/
            byte[] window = new byte[n];
            int windowEnd = windowStart + n;
            for (int j = windowStart; j < windowEnd; j++)
                window[j - windowStart] = programData[j];
            /** update counts **/
            ByteBuffer win = ByteBuffer.wrap(window); // wrap the n-gram as ByteBuffer, which acts as a key of the Map
            if (counts.containsKey(win))
                counts.put(win, counts.get(win) + 1);
            else
                counts.put(win, 1);
        }
    }

    /**
     * Print out the final counts for this program file.
     * @param counts The Map (Map<n-gram, count>) stores the count.
     */
    public static void printCounts(Map<ByteBuffer, Integer> counts, StringBuilder sb)
    {
        sb.setLength(0); // reset
        for (Map.Entry<ByteBuffer, Integer> entry : counts.entrySet())
            System.out.println(byteArrToString(entry.getKey().array(), sb) + ": " + entry.getValue());
    }

    /**
     * Convert the byte value into hex format for printing.
     * @param arr The byte array to print.
     * @param sb StringBuilder that generates the string. Passing it in so that we don't need to
     *           ask for a new one.
     * @return The constructed string.
     */
    public static String byteArrToString(byte[] arr, StringBuilder sb)
    {
        sb.setLength(0); // reset
        for (byte b : arr)
            sb.append(String.format("%02x", b)); // Notice for better looking, I didn't add the 0x part, so 0x2b is 2b.
        return sb.toString();
    }
}
