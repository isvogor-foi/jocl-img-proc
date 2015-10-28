package hr.foi.thor;

import static org.jocl.CL.CL_CONTEXT_PLATFORM;
import static org.jocl.CL.CL_DEVICE_TYPE_ALL;
import static org.jocl.CL.clCreateCommandQueue;
import static org.jocl.CL.clCreateContext;
import hr.foi.thor.filters.Dilate;
import hr.foi.thor.filters.Erode;
import hr.foi.thor.filters.Gauss;
import hr.foi.thor.filters.Hyst;
import hr.foi.thor.filters.Sobel;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.jocl.CL;
import org.jocl.cl_command_queue;
import org.jocl.cl_context;
import org.jocl.cl_context_properties;
import org.jocl.cl_device_id;
import org.jocl.cl_platform_id;

// INSTALL OPENCL ON YOUR SYSTEM
public class Main
{
    /**
     * Entry point for this sample.
     * 
     * @param args not used
     */
	@SuppressWarnings("unused")
	public static void main (String args[]){	
		// VARIABLES
	
		final int deviceIndex = 0;
		final long deviceType = org.jocl.CL.CL_DEVICE_TYPE_ALL;
		
		cl_command_queue commandQueue;
		cl_device_id[] devices;
		cl_platform_id platform;
		cl_context_properties contextProperties = new cl_context_properties();
		CLEnvironment env = new CLEnvironment();

		// IMPLEMENTATION	
		// 1. enable exceptions, omit errors
		CL.setExceptionsEnabled(true);
		// 2. get current platform
		platform = env.getCurrentPlatform(env.getNumberOfPlatforms(), 0);
		// 3. initialize the context
		System.out.println("Platforms: " + env.getNumberOfPlatforms());
		contextProperties.addProperty(CL_CONTEXT_PLATFORM, platform);
		// 4. get devices
		devices = env.getAllDevices(deviceType, platform);
		// print device info
		System.out.println("Devices: " + devices.length);
		env.printDeviceInfo(devices, platform);
		// 5. create context for the device
		cl_context ctx = clCreateContext(contextProperties, 1, new cl_device_id[]{devices[deviceIndex]}, null, null, null);
		// 6. create command queue
		commandQueue = clCreateCommandQueue(ctx, devices[deviceIndex], 0, null);
		
		// create memory - different each time (inside a class)
		BufferedImage inImage = createBufferedImage("/home/ivan/Dev/java/temp/cap2g.jpg");
		//
		//BufferedImage inImage = createBufferedImage("lena512color.png");
		
		Hyst h = new Hyst(ctx, devices[deviceIndex]);
		h.applyFilter(inImage);
		
		//Sobel s = new Sobel(ctx, devices[deviceIndex]);
		//s.applyFilter(inImage);
		
		//Gauss g = new Gauss(ctx, devices[deviceIndex]);
		//g.applyFilter(inImage);
		
		//Erode er = new Erode(ctx, devices[deviceIndex]);
		//er.applyFilter(inImage);
		
		//Dilate d = new Dilate(ctx, devices[deviceIndex]);
		//d.applyFilter(inImage);
		
	}
	
    
    private static BufferedImage createBufferedImage(String fileName)
    {
        BufferedImage image = null;
        try
        {
            image = ImageIO.read(new File(fileName));
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
        
        int sizeX = image.getWidth();
        int sizeY = image.getHeight();

        BufferedImage result = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_RGB);
        Graphics g = result.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return result;
    }
	
}

