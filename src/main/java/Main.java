import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;


public class Main
{
    private static int newWidth = 300;
    public static void main(String[] args) {
        int processors = Runtime.getRuntime().availableProcessors();
        String srcFolder = "src/main/Data/SRC";
        String dstFolder = "src/main/Data/DST";

        File srcDir = new File(srcFolder);

        long start = System.currentTimeMillis();

        File[] files = srcDir.listFiles();

        int loops = files.length / processors;
        int position = 0;
        int threadNumber = 1;
        if (loops > 1)
        {
            for (int i = 0; i < processors; i++)
            {
                startCopy(files, position, loops, dstFolder, start, threadNumber);
                position += loops;
                threadNumber += 1;
            }
        }
        else
        {
            startCopy(files, 0, files.length, dstFolder, start, 1);
        }


        if ((files.length % processors) != 0 && (files.length / processors) > 1)
        {
            startCopy(files, position, (files.length % processors), dstFolder, start, threadNumber);
        }

        System.out.println("CPU cores: " + processors);

    }

    private static void startCopy(File[] files, int position, int length, String dstFolder, long start, int threadNumber)
    {
        File[] filesToCopy = new File[length];
        System.arraycopy(files, position, filesToCopy, 0, filesToCopy.length);
        ImageResizer imageResizer = new ImageResizer(filesToCopy, newWidth, dstFolder, start, threadNumber);
        imageResizer.start();
    }
}
