import org.imgscalr.Scalr;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Iterator;

public class ImageResizer extends Thread
{
    private File[] files;
    private int newWidth = 0;
    private int newHeight = 0;
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


    public ImageResizer(File[] files, String dstFolder, long start, int threadNumber) {
        this.files = files;
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

                    newHeight = (int) Math.round(
                        image.getHeight() / (image.getWidth() / (double) newWidth)
                );

                BufferedImage scaledImg = Scalr.resize(image, Scalr.Method.SPEED, newWidth * 2, newHeight * 2);

                scaledImg = Scalr.resize(scaledImg, Scalr.Method.ULTRA_QUALITY, newWidth, newHeight);

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
