import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import java.util.ArrayList;

public class Convert
{
	static ArrayList<String> getImageNames(String path)
	{
		ArrayList<String> resultList = new ArrayList<String>();

		try
		{
			File[] f = new File(path).listFiles();
			for (File file : f)
			{
				if (file != null && file.getName().toLowerCase().endsWith(".png"))
					resultList.add(file.getCanonicalPath());
			}
		}
		catch (IOException e) { e.printStackTrace(); }

		return resultList;
	}

	static BufferedImage[] getImages(ArrayList<String>filenames)
	{
		BufferedImage[]images = new BufferedImage[filenames.size()];
		System.out.println("Reading images");
		for(int i=0;i<filenames.size();i++)
		{
			BufferedImage in = null;
			try
			{
				in = ImageIO.read(new File(filenames.get(i)));
				images[i] = in;
			}
			catch(Exception e){e.printStackTrace();}
		}
		System.out.println("Successfully read images.");
		return images;
	}

	public static void main(String[]args)
	{
		ArrayList<String>filenames = getImageNames(args[0]);
		BufferedImage[]images = getImages(filenames);

		for(int i=0;i<images[0].getWidth();i++)
		{
			BufferedImage temp = new BufferedImage(images.length,images[0].getHeight(),BufferedImage.TYPE_BYTE_GRAY);
			for(int j=images.length-1;j>=0;j--)
			{
				for(int k=0;k<images[0].getHeight();k++)
				{
				//	System.out.println(images[j].getWidth() + " " + images[j].getHeight() + " , " + i + " " + k);
					temp.setRGB(j,k,images[j].getRGB(i,k));
				}
			}
			try
			{
				new File(args[1]).mkdir();
				String new_path = args[1] + String.valueOf(i+1) + ".png";
				ImageIO.write(temp, "png", new File(new_path));
			}
			catch (Exception e) {e.printStackTrace();}
			System.out.println("Finished image " + i);
					
		}

				
			

	}
}
