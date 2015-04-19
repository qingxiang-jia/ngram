import java.io.*;

/**
 * Handles all operations related to file, such as reading a file, writing a file, and de/serialization.
 * The idea is all IO operations are the same and long, so I decided to group them here.
 */
public class IOUtil
{

    /**
     * Reads in a file, and return its byte array representation.
     * Java 1.7+ built-in class can handle this but CLIC machines have only JVM 1.6.
     * @param path Path to file to be read
     * @return Byte array that contains all bytes of the file.
     */
    public static byte[] readFile(String path)
    {
        File file = new File(path);
        FileInputStream stream = null;
        byte[] fileInBytes = new byte[(int) file.length()];
        try {
            stream = new FileInputStream(file);
            int count = stream.read(fileInBytes);
        } catch (FileNotFoundException e) {
            System.out.println("File " + path + " not found.");
        } catch (IOException e) {
            System.out.println("Cannot read " + path + ".");
        } finally {
            try {
                if (stream != null)
                    stream.close();
            } catch (IOException e) {
                System.out.println("Failed to close stream.");
            }
        }
        return fileInBytes;
    }

    /**
     * Write a String to file.
     * @param path Path of the output file.
     * @param content The String to be written.
     */
    public static void stringToFile(String path, String content)
    {
        PrintWriter out = null;
        try {
            out = new PrintWriter(path);
            out.println(content);
        } catch (FileNotFoundException e) {
            System.out.println("File " + path + " not found.");
        } finally {
            if (out != null)
                out.close();
        }
    }
}
