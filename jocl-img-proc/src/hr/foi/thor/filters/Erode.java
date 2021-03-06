package hr.foi.thor.filters;

import static org.jocl.CL.clSetKernelArg;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.jocl.Pointer;
import org.jocl.Sizeof;
import org.jocl.cl_context;
import org.jocl.cl_device_id;

public class Erode extends AbstractFilter{

    public static String kernelFileName = "src/hr/foi/thor/kernels/erode.cl";
    public static String kernelName = "erode";

    public Erode(cl_context context, cl_device_id device){
    	super(context, device, kernelFileName, kernelName);
    }
  
	public void applyFilter(BufferedImage inputImage) {
    	
    	BufferedImage outputImage1 = new BufferedImage(inputImage.getWidth(), inputImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        long before = System.nanoTime();
    		outputImage1 = filter(inputImage, outputImage1);
        long after = System.nanoTime();
        
        float durationMS = (float) ((after-before)/1e6);
        System.out.println("JOCL: "+String.format("%.2f", durationMS)+" ms");
        
        
        try {
            File outputfile = new File("saved_e.png");
			ImageIO.write(outputImage1, "png", outputfile);
		} catch (IOException e) {
			e.printStackTrace();
		}  
        
        shutdown();
    }
	
    public BufferedImage filter(BufferedImage src, BufferedImage dst)
    {
        prepareImageMemory(src, dst);
        setWorkSize(4, 4, 1, 1); 
     
        clSetKernelArg(clKernel, 0, Sizeof.cl_mem, Pointer.to(inputImageMem));
        clSetKernelArg(clKernel, 1, Sizeof.cl_mem, Pointer.to(outputImageMem));
        
        System.out.println("Kernel " + imageSizeX + ", " + imageSizeY);
        System.out.println("Global: " + globalWorkSize[0] + ", " + globalWorkSize[1] + ", " + localWorkSize[0] + ", " + localWorkSize[1]);
        
        applyKernel(dst);
        
        return dst;
    }
  
    
}
