import java.util.ArrayList;
import java.util.PriorityQueue;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Gradient
{
	ArrayList<Nuclei>nuclei;
	BufferedImage[]images;
	BufferedImage[]images_orig;
	ArrayList<String>filenames;

	ArrayList<PriorityQueue<BorderPixel>>borders;

	public Gradient(ArrayList<Nuclei>nuclei, BufferedImage[]images, ArrayList<String>filenames)
	{
		this.nuclei=nuclei;
		this.images=images;
		this.filenames=filenames;

		borders = new ArrayList<PriorityQueue<BorderPixel>>();

		getImages();
	}

	ArrayList<PriorityQueue<BorderPixel>> getGradients()
	{
		return borders;
	}

	void calcBorders()
	{
		for(int i=0;i<nuclei.size();i++)
		{
			BufferedImage cur = images[Integer.parseInt(nuclei.get(i).image)-1];
			int pixel = cur.getRGB((int) (nuclei.get(i).x),(int) (nuclei.get(i).y));

			PriorityQueue<BorderPixel>cur_q = new PriorityQueue<BorderPixel>();
			for(int xx=((int) (nuclei.get(i).x))-4;(xx<((int) (nuclei.get(i).x))+4) && (xx>-1) && (xx<cur.getWidth());xx++)
			{
				for(int yy=((int) (nuclei.get(i).y))-4;(yy<((int) (nuclei.get(i).y))+4) && (yy>-1) && (yy<cur.getHeight());yy++)
				{
					if((cur.getRGB(xx,yy)==pixel))
					{
						boolean bor = false;
						for(int px=xx-1;px<xx+2;px++)
						{
							for(int py=yy-1;py<yy+2;py++)
							{
								if(cur.getRGB(px,py)!=cur.getRGB(xx,yy))
								{
									bor = true;
									py=yy+2;
									px=xx+2;
								}
							}
						}
						if(bor)
							cur_q.add(new BorderPixel(xx,yy,getGradient(images_orig[Integer.parseInt(nuclei.get(i).image)-1],nuclei.get(i),xx,yy)));
					}
				}
			}

			borders.add(cur_q);
		}
	}

	double getGradient(BufferedImage im, Nuclei n, int x, int y)
	{
		double x_g1 = im.getRGB(x,y)&0xFF - im.getRGB(x-1,y)&0xFF;
		double x_g2 = im.getRGB(x,y)&0xFF - im.getRGB(x+1,y)&0xFF;
		double y_g1 = im.getRGB(x,y)&0xFF - im.getRGB(x,y-1)&0xFF;
		double y_g2 = im.getRGB(x,y)&0xFF - im.getRGB(x,y+1)&0xFF;

		double x_g = (x_g1>0) ? x_g1 : (x_g2>0) ? x_g2 : 0;
		double y_g = (y_g1>0) ? y_g1 : (y_g2>0) ? y_g2 : 0;

		double gradient = Math.pow( Math.pow(x_g,2) + Math.pow(x_g,2), .5);
		return gradient;
	}

	void getImages()
	{
		System.out.println("Reading images");
		for(int i=0;i<filenames.size();i++)
		{
			BufferedImage in = null;
			try
			{
				in = ImageIO.read(new File(filenames.get(i)));
				images_orig[i] = in;
			}
			catch(Exception e){e.printStackTrace();}
		}
		System.out.println("Successfully read images.");
	}

}
