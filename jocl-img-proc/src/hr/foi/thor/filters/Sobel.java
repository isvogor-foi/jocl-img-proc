package hr.foi.thor.filters;

import static org.jocl.CL.*;

import org.jocl.*;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.Kernel;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;


public class Sobel {
	
	private cl_mem inputImageMemory;
	private cl_mem outputImageMemory;
	private String KERNEL_SOURCE_FILE = "kernels/sobel.cl";
	
	public void prepareMemmoryResources(cl_context ctx, BufferedImage src){
		
		int imageSizeX = src.getWidth();
		int imageSizeY = src.getHeight();
		
		DataBufferInt dataBufferSrc = (DataBufferInt)src.getRaster().getDataBuffer();
		int dataSrc[] = dataBufferSrc.getData();
		
		inputImageMemory = clCreateBuffer(ctx, CL_MEM_READ_ONLY | CL_MEM_USE_HOST_PTR, dataSrc.length * Sizeof.cl_uint, Pointer.to(dataSrc), null);
		outputImageMemory = clCreateBuffer(ctx, CL_MEM_WRITE_ONLY, imageSizeX * imageSizeY * Sizeof.cl_uint, null, null);
        
		// build the program... 
		
		/*
		 // Create a command-queue
        System.out.println("Creating command queue...");
        long properties = 0;
        properties |= CL_QUEUE_PROFILING_ENABLE;
        properties |= CL_QUEUE_OUT_OF_ORDER_EXEC_MODE_ENABLE;
        commandQueue = clCreateCommandQueue(context, device, properties, null);

        // Create the program
        System.out.println("Creating program...");
        cl_program program = clCreateProgramWithSource(context,1, new String[]{ programSource }, null, null);

        // Build the program
        System.out.println("Building program...");
        clBuildProgram(program, 0, null, null, null, null);

        // Create the kernel
        System.out.println("Creating kernel...");
        kernel = clCreateKernel(program, "rotateImage", null);
		 */
		
	}
	
	public void cleanMemory(){
		clReleaseMemObject(inputImageMemory);
		clReleaseMemObject(outputImageMemory);
	}
	
	public void loadKernel(cl_context ctx){
		
		// call buffer creation
        
		
	}
	
    /**
     * Helper function which reads the file with the given name and returns 
     * the contents of this file as a String. Will exit the application
     * if the file can not be read.
     * 
     * @param fileName The name of the file to read.
     * @return The contents of the file
     */
    private static String readFile(String fileName)
    {
        try
        {
            BufferedReader br = new BufferedReader( new InputStreamReader(new FileInputStream(fileName)));
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
}
