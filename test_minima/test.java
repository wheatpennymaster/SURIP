import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class test
{
	public static void main(String[]args)
	{
		BufferedImage in = null;
		try{in = ImageIO.read(new File("test_im.jpg"));} catch(Exception e){}
		BufferedImage newImage = in;

		int cur = 0;
		int up = 0;
		int down = 0;
		int left = 0;
		int right = 0;

		for(int x=1;x<in.getWidth()-1;x++)
		{
			for(int y=1;y<in.getHeight()-1;y++)
			{
				cur = in.getRGB(x, y);
				up = in.getRGB(x, y-1);
				down = in.getRGB(x, y+1);
				left = in.getRGB(x-1, y);
				right = in.getRGB(x+1, y);

				if((cur>down) && (cur>up) && (cur>right) && (cur>left))
					newImage.setRGB(x,y,65280);
				
			}
		}

		try{ImageIO.write(newImage, "jpg", new File("test_out.jpg"));} catch(Exception e){}
	}
}	
