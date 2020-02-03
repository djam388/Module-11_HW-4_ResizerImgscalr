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
        this.newWidth = newWidth * 2;
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

                if (newWidth == 0)
                {
                    newWidth = image.getWidth() / 2;
                    newHeight = image.getHeight() / 2;
                }
                else
                {
                    newHeight = (int) Math.round(
                        image.getHeight() / (image.getWidth() / (double) newWidth)
                );
                }





                BufferedImage scaledImg = Scalr.resize(image, newWidth, newHeight);

                File newFile = new File(dstFolder + "/" + file.getName());


                //https://examples.javacodegeeks.com/desktop-java/imageio/compress-a-jpeg-file/
                OutputStream os = new FileOutputStream(newFile);

                float quality = 0.5f;

                // get all image writers for JPG format
                Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");

                ImageWriter writer = writers.next();
                ImageOutputStream ios = ImageIO.createImageOutputStream(os);
                writer.setOutput(ios);

                ImageWriteParam param = writer.getDefaultWriteParam();

                // compress to a given quality
                param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                param.setCompressionQuality(quality);

                // appends a complete image stream containing a single image and
                //associated stream and image metadata and thumbnails to the output
                writer.write(null, new IIOImage(scaledImg, null, null), param);

                // close all streams
                os.close();
                ios.close();
                writer.dispose();


//                ImageIO.write(scaledImg, "jpg", newFile);
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
