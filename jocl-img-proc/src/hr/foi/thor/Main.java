package hr.foi.thor;

import static org.jocl.CL.CL_CONTEXT_PLATFORM;
import static org.jocl.CL.CL_DEVICE_TYPE_ALL;
import static org.jocl.CL.clCreateCommandQueue;
import static org.jocl.CL.clCreateContext;
import hr.foi.thor.filters.Sobel;

import org.jocl.CL;
import org.jocl.cl_command_queue;
import org.jocl.cl_context;
import org.jocl.cl_context_properties;
import org.jocl.cl_device_id;
import org.jocl.cl_platform_id;

public class Main
{
    /**
     * Entry point for this sample.
     * 
     * @param args not used
     */
	public static void main (String args[]){	
		// VARIABLES
	
		final int deviceIndex = 0;
		final long deviceType = CL_DEVICE_TYPE_ALL;
		
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
		contextProperties.addProperty(CL_CONTEXT_PLATFORM, platform);
		// 4. get devices
		devices = env.getAllDevices(deviceType, platform);
		
		// print device info
		env.printDeviceInfo(devices, platform);
		
		// 5. create context for the device
		cl_context ctx = clCreateContext(contextProperties, 1, new cl_device_id[]{devices[deviceIndex]}, null, null, null);
		// 6. create command queue
		commandQueue = clCreateCommandQueue(ctx, devices[deviceIndex], 0, null);
		
		// create memory - different each time (inside a class)
		Sobel s = new Sobel();

	}
	
}
