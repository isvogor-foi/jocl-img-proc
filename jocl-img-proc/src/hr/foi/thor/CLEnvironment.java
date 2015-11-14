package hr.foi.thor;

import static org.jocl.CL.CL_DEVICE_NAME;
import static org.jocl.CL.CL_PLATFORM_NAME;
import static org.jocl.CL.clGetDeviceIDs;
import static org.jocl.CL.clGetDeviceInfo;
import static org.jocl.CL.clGetPlatformIDs;
import static org.jocl.CL.clGetPlatformInfo;

import org.jocl.Pointer;
import org.jocl.cl_device_id;
import org.jocl.cl_platform_id;

public class CLEnvironment {
	
	/**
	 * System.out.prinln of the current environment/platform devices
	 * @param devices - get all devices
	 * @param platform - get current platform
	 */
	public void printDeviceInfo(cl_device_id[] devices, cl_platform_id platform) {
		System.out.println("Platform: " + getString(platform, CL_PLATFORM_NAME));
		for(cl_device_id current : devices){
			System.out.println("Device name:" + getString(current, CL_DEVICE_NAME));
		}
	}
	
	public void printDeviceName(cl_device_id device, cl_platform_id platform){
		System.out.println("Device name:" + getString(device, CL_DEVICE_NAME));
	}

	/**
	 * Get device by id
	 * @param deviceIndex - desired device index
	 * @param deviceType - type of the device 
	 * @param platform - current platform
	 * @return selected device
	 */
	public cl_device_id[] getAllDevices(final long deviceType, cl_platform_id platform) {
		cl_device_id devices[] = new cl_device_id[getNumberOfDevices(platform, deviceType)];
		clGetDeviceIDs(platform, deviceType, getNumberOfDevices(platform, deviceType), devices, null);
		return devices;
	}
	
	/**
	 * Returns the device by it's index
	 * @param platform - current platform
	 * @param deviceType - example CL_DEVICE_TYPE_ALL
	 * @return numberOfDevices - number of devices 
	 */
	public int getNumberOfDevices(cl_platform_id platform, long deviceType){
		int numberOfDevicesArray[] = new int[1];
		int numberOfDevices;
		
		clGetDeviceIDs(platform, deviceType, 0, null, numberOfDevicesArray);
		numberOfDevices = numberOfDevicesArray[0];
		
		return numberOfDevices;
	}
	
	/**
	 * Return the target platform from the current environment
	 * @param numberOfPlatforms - number of platforms
	 * @param platformIndex - index of the platform to return
	 * @return target platform
	 */
	public cl_platform_id getCurrentPlatform(int numberOfPlatforms, int platformIndex) {
       
		cl_platform_id platforms[] = new cl_platform_id[numberOfPlatforms];
        clGetPlatformIDs(platforms.length, platforms, null);
		
        return platforms[platformIndex];
	}
	

	/**
	 * Simple procedure to get the number of platforms in the environment
	 * @return numberOfPlatforms
	 */
	public int getNumberOfPlatforms(){
		// for getting platforms
		int numberOfPlatformsArray[] = new int[1];
		int numberOfPlatforms;
		
		clGetPlatformIDs(0, null, numberOfPlatformsArray);
		numberOfPlatforms = numberOfPlatformsArray[0];
		
		return numberOfPlatforms;
	}

    /**
     * Returns the value of the platform info parameter with the given name
     *
     * @param platform The platform
     * @param paramName The parameter name
     * @return The value
     */
    public String getString(cl_platform_id platform, int paramName)
    {
        long size[] = new long[1];
        clGetPlatformInfo(platform, paramName, 0, null, size);
        byte buffer[] = new byte[(int)size[0]];
        clGetPlatformInfo(platform, paramName,  buffer.length, Pointer.to(buffer), null);
        return new String(buffer, 0, buffer.length-1);
    }
    
    /**
     * Returns the value of the device info parameter with the given name
     *
     * @param device The device
     * @param paramName The parameter name
     * @return The value
     */
    public String getString(cl_device_id device, int paramName)
    {
        long size[] = new long[1];
        clGetDeviceInfo(device, paramName, 0, null, size);
        byte buffer[] = new byte[(int)size[0]];
        clGetDeviceInfo(device, paramName, 
            buffer.length, Pointer.to(buffer), null);
        return new String(buffer, 0, buffer.length-1);
    }
}
