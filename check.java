import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.io.File;
import java.util.ArrayList;

import java.util.LinkedList;

import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import javax.imageio.ImageIO;

public class check
{

        static String[][] read_csv(String filename)
        {
                BufferedReader br = null;
                String line = "";
                LinkedList<String[]>l = new LinkedList<String[]>();

                try
                {
                        br = new BufferedReader(new FileReader(filename));
                        while ((line = br.readLine()) != null)
                        {
                                String[]a = line.split(",");
                                l.add(a);
                        }

                }
                catch (FileNotFoundException e) { e.printStackTrace(); }
                catch (IOException e) { e.printStackTrace(); }
                finally
                {
                        if (br != null)
                        {
                                try { br.close(); }
                                catch (IOException e) { e.printStackTrace();}
                        }
                }

                String[][]out = new String[l.size()][4];
                for(int i=0;i<out.length;i++)
                        out[i] = l.get(i);

                return out;
        }

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
		String[][]csv1 = read_csv("output1/input.csv");
		String[][]csv2 = read_csv("output2/input.csv");
		ArrayList<String>imageNames1 = getImageNames("output1/");
		ArrayList<String>imageNames2 = getImageNames("output2/");
		BufferedImage[]images1 = getImages(imageNames1);
		BufferedImage[]images2 = getImages(imageNames2);

		if(csv1.length != csv2.length)
		{
			System.out.println("Fuck columns");
			System.exit(0);
		}
		if(csv1[0].length != csv2[0].length);
		{
			System.out.println("Fuck rows");
			System.exit(0);
		}
		for(int i=0;i<csv1.length;i++)
		{
			for(int j=0;j<csv1[i].length;j++)
			{
				if(!(csv1[i][j].equals(csv2[i][j]))) {
					System.out.println("Fuck the csv");
					System.exit(0);
				}
			}
		}

		if(images1.length!=images2.length)
		{
			System.out.println("Fuck images");
			System.exit(0);
		}
                for(int i=0;i<images1.length;i++)
                {
                        for(int j=0;j<images1[i].getWidth();j++)
                        {
                                for(int k=0;k<images1[i].getHeight();k++)
				{
					if(images1[i].getRGB(j,k)!=images2[i].getRGB(j,k))
					{
						System.out.println("Fuck this program");
						System.exit(0);
					}
				}
			}
		}
		System.out.println("You luck bastard");
	}
}
