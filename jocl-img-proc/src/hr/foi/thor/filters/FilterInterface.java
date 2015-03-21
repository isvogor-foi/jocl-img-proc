package hr.foi.thor.filters;

import java.awt.image.BufferedImage;

public interface FilterInterface {
	public void applyFilter(BufferedImage inputImage);
    public BufferedImage filter(BufferedImage src, BufferedImage dst);
}
