import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class ImageResizer extends Thread
{
    private File[] files;
    private int newWidth;
    private String dstFolder;
    private long start;
    private int threadNumber;

    public ImageResizer(File[] files, int newWidth, String dstFolder, long start, int threadNumber) {
        this.files = files;
        this.newWidth = newWidth;
        this.dstFolder = dstFolder;
        this.start = start;
        this.threadNumber = threadNumber;
    }

    @Override
    public void run()
    {
        try
        {
            for(File file : files)
            {
                BufferedImage image = ImageIO.read(file);
                if(image == null) {
                    continue;
                }

                int newHeight = (int) Math.round(
                        image.getHeight() / (image.getWidth() / (double) newWidth)
                );


                BufferedImage scaledImg = Scalr.resize(image, Scalr.Mode.AUTOMATIC, newWidth, newHeight);


                File newFile = new File(dstFolder + "/" + file.getName());
                ImageIO.write(scaledImg, "jpg", newFile);
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println( "Thread number: " + threadNumber
                + " ->> Resized files number: " + files.length
                + " ->> Duration: " + (System.currentTimeMillis() - start + " ms."));
    }
}
