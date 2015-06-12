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

        static BufferedImage getImages(ArrayList<String>filenames, int q)
        {
		BufferedImage image = null;
                        try
                        {
                                image = ImageIO.read(new File(filenames.get(q)));
                        }
                        catch(Exception e){e.printStackTrace();}
		return image;
        }


	public static void main(String[]args)
	{
		String[][]csv1 = read_csv("/Users/thomas/SURIP/jfilter/output/output.csv");
		String[][]csv2 = read_csv("/Users/thomas/SURIP/jfilter_overhaul/output/output.csv");
		ArrayList<String>imageNames1 = getImageNames("/Users/thomas/SURIP/jfilter/output/");
		ArrayList<String>imageNames2 = getImageNames("/Users/thomas/SURIP/jfilter_overhaul/output/");

		if(csv1.length != csv2.length)
		{
			System.out.println("Fuck columns");
			System.exit(0);
		}
	/*	for(int i=0;i<csv1.length;i++)
		{
			if(csv1[i].length != csv2[i].length);
			{
				System.out.println("Fuck rows " + i + " " + csv1[i].length + " " + csv2[i].length);
				System.exit(0);
			}
		}
	*/	for(int i=0;i<csv1.length;i++)
		{
			for(int j=0;j<csv1[i].length;j++)
			{
				if(!(csv1[i][j].equals(csv2[i][j]))) {
					System.out.println("Fuck the csv at " + i + " " + j);
					System.exit(0);
				}
			}
		}

                for(int q=0;q<imageNames1.size();q++)
                {
			BufferedImage image1 = getImages(imageNames1,q);
			BufferedImage image2 = getImages(imageNames2,q);
                        for(int j=0;j<image1.getWidth();j++)
                        {
                                for(int k=0;k<image1.getHeight();k++)
				{
					if(image1.getRGB(j,k)!=image2.getRGB(j,k))
					{
						System.out.println("Fuck this program");
						System.exit(0);
					}
				}
			}
		}
		System.out.println("You lucky bastard");
	}
}
