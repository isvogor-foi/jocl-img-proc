/**********************************************************************
 Author: Ivan Svogor, FOI 
***********************************************************************/

__kernel void dilate(__global uchar4* inputImage, __global uchar4* outputImage)
{
	uint x = get_global_id(0);
    uint y = get_global_id(1);

	uint width = get_global_size(0);
	uint height = get_global_size(1);

	int c = x + y * width;
	float4 a[9];
	
	// skip edge pixels	
	if( x >= 1 && x < (width-1) && y >= 1 && y < height - 1)
	{
		a[0] = convert_float4(inputImage[c - 1 - width]);
		a[1] = convert_float4(inputImage[c - width]);
		a[2] = convert_float4(inputImage[c + 1 - width]);
		a[3] = convert_float4(inputImage[c - 1]);
		a[4] = convert_float4(inputImage[c]);
		a[5] = convert_float4(inputImage[c + 1]);
		a[6] = convert_float4(inputImage[c - 1 + width]);
		a[7] = convert_float4(inputImage[c + width]);
		a[8] = convert_float4(inputImage[c + 1 + width]);		
		
		float4 maxVal = (float4)(0);
		
		// select the max neighbor 
		for(int i = 0; i < 9; i++){
			maxVal = max(maxVal, a[i]); 
		}
		
		outputImage[c] = maxVal;
	}		
}