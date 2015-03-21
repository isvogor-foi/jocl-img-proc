/**********************************************************************
 Author: Ivan Svogor, FOI 
***********************************************************************/

__kernel void gauss(__global uchar4* inputImage, __global uchar4* outputImage)
{
	uint x = get_global_id(0);
    uint y = get_global_id(1);

	uint width = get_global_size(0);
	uint height = get_global_size(1);

	float4 currentPx = (float4)(0);	
	int c = x + y * width;
	
	// gauss = 1/16 [1 2 1, 2 4 2, 1 2 1]
	float4 factor = (float4) 1 / 16; 
	float4 two = factor * 2;
	float4 four = factor * 4;  
	

	/* Read each texel component and calculate the filtered value using neighbouring texel components */
	// skip edge pixels	
	if( x >= 1 && x < (width-1) && y >= 1 && y < height - 1)
	{
		float4 i00 = convert_float4(inputImage[c - 1 - width]);
		float4 i10 = convert_float4(inputImage[c - width]);
		float4 i20 = convert_float4(inputImage[c + 1 - width]);
		float4 i01 = convert_float4(inputImage[c - 1]);
		float4 i11 = convert_float4(inputImage[c]);
		float4 i21 = convert_float4(inputImage[c + 1]);
		float4 i02 = convert_float4(inputImage[c - 1 + width]);
		float4 i12 = convert_float4(inputImage[c + width]);
		float4 i22 = convert_float4(inputImage[c + 1 + width]);		
		
		currentPx = (float4)i00 * factor + (float4)i01 * two + (float4)i02 * factor + 
		     (float4)i10 * two + (float4)i11 * four + (float4)i12 * two + 
		     (float4)i20 * factor + (float4)i21 * two + (float4)i22 * factor;		
		
		outputImage[c] = convert_uchar4(currentPx);

	}
			
}