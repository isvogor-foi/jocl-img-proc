package hr.foi.thor.filters;

import static org.jocl.CL.*;
import org.jocl.*;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;


public class Sobel {
	
	private cl_mem inputImageMemory;
	private cl_mem outputImageMemory;
	
	public void prepareMemmoryResources(cl_context ctx, BufferedImage src){
		
		int imageSizeX = src.getWidth();
		int imageSizeY = src.getHeight();
		
		DataBufferInt dataBufferSrc = (DataBufferInt)src.getRaster().getDataBuffer();
		int dataSrc[] = dataBufferSrc.getData();
		
		inputImageMemory = clCreateBuffer(ctx, CL_MEM_READ_ONLY | CL_MEM_USE_HOST_PTR, dataSrc.length * Sizeof.cl_uint, Pointer.to(dataSrc), null);
		outputImageMemory = clCreateBuffer(ctx, CL_MEM_WRITE_ONLY, imageSizeX * imageSizeY * Sizeof.cl_uint, null, null);
        
	}
	
	public void cleanMemory(){
		clReleaseMemObject(inputImageMemory);
		clReleaseMemObject(outputImageMemory);
	}
	
	

}
