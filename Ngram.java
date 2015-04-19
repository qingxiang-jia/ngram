import java.io.*;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * N-gram counter that count the occurrence of each byte in a binary file.
 */
public class Ngram
{
    static private final int BUF_SIZE = 16000; // set buffer size big to minimize number of system i/o calls

    /**
     * Read a binary file in buffer, perform n-gram counting until the file is finished.
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
                if (bytesRead > 0 && n > 1)
                    count(programData, bytesRead, counts, n, s);
                if (bytesRead > 0 && n == 1)
                    countN1(programData, bytesRead, counts);
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
     * The point of this method really is just because Dr. Cook said for n = 1 we should handle it separately.
     * I see that using arrays for n = 1 will be slightly faster than HashMap, but my other methods were written
     * to accept HashMap. So, putting this method here may actually slows down the program since after using
     * arrays to store counts, eventually I still have to convert the counts into HashMap so that the rest of the
     * program will run properly. Yes, it is true I can rewrite a different version of the rest of methods so that
     * I don't have to convert to HashMap, but it's tedious to implement. Therefore, you will see a conversion
     * code block at the end of this method.
     * @param programData
     * @param bytesRead
     * @param counts
     */
    private static void countN1(byte[] programData, int bytesRead, Map<ByteBuffer, Integer> counts)
    {
        int[] minus = new int[129], plus = new int[129]; // when treated as int, it's signed
        for (int i = 0; i < bytesRead; i++) {
            if (programData[i] >= 0) {
                plus[ programData[i]]++;
            } else {
                minus[-programData[i]]++;
            }
        }
        /** update counts **/
        for (int i = 0; i < plus.length; i++) {
            ByteBuffer key1 = ByteBuffer.wrap(new byte[]{(byte) i});
            if (counts.containsKey(key1)) {
                counts.put(key1, counts.get(key1) + plus[i]);
            } else {
                counts.put(key1, plus[i]);
            }
            ByteBuffer key2 = ByteBuffer.wrap(new byte[]{(byte) -i});
            if (counts.containsKey(key2)) {
                counts.put(key2, counts.get(key2) + minus[i]);
            } else {
                counts.put(key2, minus[i]);
            }
        }
    }

    /**
     * This is the universal count method that takes care of all possible n and s values. Under the
     * hood, the has a mapping between the n-gram (ByteBuffer) and the counts. I use ByteBuffer
     * instead of byte[] because the former uses the contents of the bytes as key, not the memory
     * location, thus I can retrieve the key without having to have a reference to the original n-gram.
     * If the remaining bytes does not fill the sliding window fully, discard the last window.
     * @param programData
     * @param bytesRead
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
            ByteBuffer win = ByteBuffer.wrap(window);
            if (counts.containsKey(win))
                counts.put(win, counts.get(win) + 1);
            else
                counts.put(win, 1);
        }
    }

    /**
     * Print out the final counts for this program file.
     * @param counts
     */
    public static void printCounts(Map<ByteBuffer, Integer> counts, StringBuilder sb)
    {
        sb.setLength(0); // reset
        for (Map.Entry<ByteBuffer, Integer> entry : counts.entrySet())
            System.out.println(byteArrToString(entry.getKey().array(), sb) + ": " + entry.getValue());
    }

    /**
     * Convert the byte value into hex format for printing.
     * @param arr
     * @param sb
     * @return
     */
    public static String byteArrToString(byte[] arr, StringBuilder sb)
    {
        sb.setLength(0); // reset
        for (byte b : arr)
            sb.append(String.format("%02x", b));
        return sb.toString();
    }
}
