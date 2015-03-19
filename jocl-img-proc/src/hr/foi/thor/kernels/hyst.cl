/**********************************************************************
 Original: https://github.com/smskelley/canny-opencl/blob/master/src/kernels/gpu/hyst_kernel.cl
 Adapted by: Ivan Svogor, FOI 
***********************************************************************/

// Hysteresis Threshold Kernel
// data: image input data with each pixel taking up 1 byte (8Bit 1Channel)
// out: image output data (8B1C)

__kernel void hyst(__global uchar *data,__global uchar *out, const int2 imageSize)
{
	float lowThresh = 90;
	float highThresh = 100;
	
	int x = get_global_id(0);
    int y = get_global_id(1);

	int width = get_global_size(0);
	int height = get_global_size(1);
	
	int pos = mul24(y, imageSize.x) + x;
	
    const uchar EDGE = 255;
    const uchar ZERO = 0;
    
    uchar magnitude = data[pos];
	
	if (magnitude >= highThresh){
    	out[pos] = EDGE;
	}
    else if (magnitude <= lowThresh){
        out[pos] = ZERO;
    }
    else
    {
        float med = (highThresh + lowThresh) / 2; 
        if (magnitude >= med){
        	out[pos] = EDGE;
        }
        else {
        	out[pos] = ZERO;
        }
    }
}



