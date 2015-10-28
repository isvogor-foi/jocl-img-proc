package hr.foi.thor;

import static org.jocl.CL.CL_CONTEXT_PLATFORM;
import static org.jocl.CL.CL_DEVICE_TYPE_ALL;
import static org.jocl.CL.clCreateCommandQueue;
import static org.jocl.CL.clCreateContext;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import hr.foi.thor.filters.Sobel;

import org.jocl.CL;
import org.jocl.cl_command_queue;
import org.jocl.cl_context;
import org.jocl.cl_context_properties;
import org.jocl.cl_device_id;
import org.jocl.cl_platform_id;

// INSTALL OPENCL ON YOUR SYSTEM
public class InitializerS1
{
	private cl_device_id[] devices;
	private int device;
	private cl_context ctx;


	public InitializerS1(int deviceIndex){	
		// VARIABLES
		this.device = deviceIndex;

		final long deviceType = org.jocl.CL.CL_DEVICE_TYPE_ALL;
		
		cl_command_queue commandQueue;
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
		ctx = clCreateContext(contextProperties, 1, new cl_device_id[]{devices[device]}, null, null, null);
		// 6. create command queue
		commandQueue = clCreateCommandQueue(ctx, devices[device], 0, null);
		
		
	}
	
	public cl_device_id[] getDevices(){
		return devices;
	}
	
	public void runSobelVga(BufferedImage inImage){
		
		Sobel s = new Sobel(ctx, devices[device]);
		s.applyFilter(inImage);
		
	}
	
    public BufferedImage createBufferedImage(String fileName)
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
