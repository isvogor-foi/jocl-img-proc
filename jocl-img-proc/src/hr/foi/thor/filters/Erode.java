package hr.foi.thor.filters;

import static org.jocl.CL.CL_MEM_READ_ONLY;
import static org.jocl.CL.CL_MEM_USE_HOST_PTR;
import static org.jocl.CL.CL_MEM_WRITE_ONLY;
import static org.jocl.CL.CL_TRUE;
import static org.jocl.CL.clBuildProgram;
import static org.jocl.CL.clCreateBuffer;
import static org.jocl.CL.clCreateCommandQueue;
import static org.jocl.CL.clCreateKernel;
import static org.jocl.CL.clCreateProgramWithSource;
import static org.jocl.CL.clEnqueueNDRangeKernel;
import static org.jocl.CL.clEnqueueReadBuffer;
import static org.jocl.CL.clReleaseCommandQueue;
import static org.jocl.CL.clReleaseContext;
import static org.jocl.CL.clReleaseKernel;
import static org.jocl.CL.clReleaseMemObject;
import static org.jocl.CL.clReleaseProgram;
import static org.jocl.CL.clSetKernelArg;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBufferInt;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import org.jocl.Pointer;
import org.jocl.Sizeof;
import org.jocl.cl_command_queue;
import org.jocl.cl_context;
import org.jocl.cl_device_id;
import org.jocl.cl_kernel;
import org.jocl.cl_mem;
import org.jocl.cl_program;

public class Erode {

    private String KERNEL_SOURCE_FILE_NAME = "src/hr/foi/thor/kernels/erode.cl";

    private cl_mem inputImageMem;
    private cl_mem outputImageMem;
    private cl_command_queue commandQueue;
    private cl_context context;
    private cl_kernel clKernel;
    
    //private Kernel kernel;
    
    public Erode(cl_context context, cl_device_id device){
    	this.context = context;
        cl_command_queue commandQueue = clCreateCommandQueue(context, device, 0, null);
    	JOCLConvolveOp(context, commandQueue); //, kernel);
    }
  

	public void applyFilter(BufferedImage inputImage) {
    	
        long before = System.nanoTime();
    	BufferedImage outputImage1 = new BufferedImage(inputImage.getWidth(), inputImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        outputImage1 = filter(inputImage, outputImage1);
        
        File outputfile = new File("saved_e.png");
        
        try {
			ImageIO.write(outputImage1, "png", outputfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        long after = System.nanoTime();
        float durationMS = (float) ((after-before)/1e6);
        System.out.println("JOCL: "+String.format("%.2f", durationMS)+" ms");
        
        shutdown();
    }
	
    public BufferedImage filter(BufferedImage src, BufferedImage dst)
    {
        // Validity checks for the given images
        if (src.getType() != BufferedImage.TYPE_INT_RGB)
        {
            throw new IllegalArgumentException("Source image is not TYPE_INT_RGB");
        }
        if (dst == null)
        {
            dst = createCompatibleDestImage(src, null);
        }
        else if (dst.getType() != BufferedImage.TYPE_INT_RGB)
        {
            throw new IllegalArgumentException("Destination image is not TYPE_INT_RGB");
        }
        if (src.getWidth() != dst.getWidth() || src.getHeight() != dst.getHeight())
        {
            throw new IllegalArgumentException("Images do not have the same size");
        }
        
        int imageSizeX = src.getWidth();
        int imageSizeY = src.getHeight();

        // Create the memory object for the input- and output image
        
        DataBufferInt dataBufferSrc = (DataBufferInt)src.getRaster().getDataBuffer();
        int dataSrc[] = dataBufferSrc.getData();
        
        inputImageMem = clCreateBuffer(context,  CL_MEM_READ_ONLY | CL_MEM_USE_HOST_PTR, dataSrc.length * Sizeof.cl_uint, Pointer.to(dataSrc), null);
        outputImageMem = clCreateBuffer(context, CL_MEM_WRITE_ONLY,  imageSizeX * imageSizeY * Sizeof.cl_uint, null, null);

        long localWorkSize[] = new long[2];
        localWorkSize[0] = 4;
        localWorkSize[1] = 4;

        long globalWorkSize[] = new long[2];
        // needs to be a multiple of local work size
        globalWorkSize[0] = imageSizeX;
        globalWorkSize[1] = imageSizeY; 

        int imageSize[] = new int[]{ imageSizeX, imageSizeY };
     
        clSetKernelArg(clKernel, 0, Sizeof.cl_mem, Pointer.to(inputImageMem));
        clSetKernelArg(clKernel, 1, Sizeof.cl_mem, Pointer.to(outputImageMem));
        //clSetKernelArg(clKernel, 2, Sizeof.cl_int2, Pointer.to(imageSize));
        
        System.out.println("Kernel " + imageSizeX + ", " + imageSizeY);
        System.out.println("Global: " + globalWorkSize[0] + ", " + globalWorkSize[1] + ", " + localWorkSize[0] + ", " + localWorkSize[1]);
        
        clEnqueueNDRangeKernel(commandQueue, clKernel, 2, null, globalWorkSize, localWorkSize, 0, null, null);
        
        // Read the pixel data into the BufferedImage
        DataBufferInt dataBufferDst = (DataBufferInt)dst.getRaster().getDataBuffer();
        int dataDst[] = dataBufferDst.getData();
        clEnqueueReadBuffer(commandQueue, outputImageMem, CL_TRUE, 0, dataDst.length * Sizeof.cl_uint, Pointer.to(dataDst), 0, null, null);

        // Clean up
        clReleaseMemObject(inputImageMem);
        clReleaseMemObject(outputImageMem);
        
        return dst;
    }
    
    
    /**
     * Creates a JOCLConvolveOp for the given context and command queue, 
     * which may be used to apply the given kernel to a BufferedImage.
     * 
     * @param context The context
     * @param commandQueue The command queue
     * @param kernel The kernel to apply
     */
    public void JOCLConvolveOp(cl_context context, cl_command_queue commandQueue) //, Kernel kernel)
    {
        this.context = context;
        this.commandQueue = commandQueue;
        
        // Create the OpenCL kernel from the program
        String source = readFile(KERNEL_SOURCE_FILE_NAME);
        cl_program program = clCreateProgramWithSource(context, 1, new String[]{ source }, null, null);
        
        String compileOptions = "-cl-mad-enable";
        
        clBuildProgram(program, 0, null, compileOptions, null, null);
        clKernel = clCreateKernel(program, "erode", null);
        clReleaseProgram(program);
    }
    
   
    public BufferedImage createCompatibleDestImage(BufferedImage src, ColorModel destCM)
    {
        int w = src.getWidth();
        int h = src.getHeight();
        BufferedImage result = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        return result;
    }
    
    
    /**
     * Helper function which reads the file with the given name and returns 
     * the contents of this file as a String. Will exit the application
     * if the file can not be read.
     * 
     * @param fileName The name of the file to read.
     * @return The contents of the file
     */
    private String readFile(String fileName)
    {
        try
        {
            @SuppressWarnings("resource")
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
            StringBuffer sb = new StringBuffer();
            String line = null;
            while (true)
            {
                line = br.readLine();
                if (line == null)
                {
                    break;
                }
                sb.append(line).append("\n");
            }
            return sb.toString();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.exit(1);
            return null;
        }
    }
    
    public void shutdown()
    {
        clReleaseKernel(clKernel);
        clReleaseCommandQueue(commandQueue);
        clReleaseContext(context);
    }

}
