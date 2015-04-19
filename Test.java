import java.io.FileOutputStream;

/**
 * A test suite for Ngram.
 */
public class Test
{
    /**
     * Generate a trivial binary file to see if Ngram works as expected.
     */
    public static void genBinary1() throws Exception
    {
        FileOutputStream fileOutputStream = new FileOutputStream("test1");
        fileOutputStream.write(new byte[]{0x0, 0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x8, 0x9, 0xA, 0xB, 0xC, 0xD, 0xE, 0xF});
        fileOutputStream.close();
    }

    /**
     * Generate a trivial binary file to see if Ngram works as expected.
     */
    public static void genBinary2() throws Exception
    {
        FileOutputStream fileOutputStream = new FileOutputStream("test2");
        fileOutputStream.write(new byte[]{0x0, 0x0, 0x0, 0x3});
        fileOutputStream.close();
    }
}
