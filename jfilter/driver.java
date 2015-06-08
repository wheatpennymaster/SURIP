import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.io.File;
import java.util.ArrayList;

import java.util.LinkedList;

public class driver
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

	static ArrayList<String> getImageNames()
	{
		ArrayList<String> resultList = new ArrayList<String>();

		try
		{
			File[] f = new File("images/").listFiles();
			for (File file : f)
			{
				if (file != null && file.getName().toLowerCase().endsWith(".tif"))
					resultList.add(file.getCanonicalPath());
			}
		}
		catch (IOException e) { e.printStackTrace(); }

		return resultList;
	}		

	public static void main(String[]args)
	{
		if(args.length == 0)
			System.out.println("Need to specify input file.");
		else
		{
			String[][]input = read_csv(args[0]);
			Nuclei[]nuclei = new Nuclei[input.length-1];
			for(int i=0;i<nuclei.length;i++)
				nuclei[i] = new Nuclei(input[i+1]);

			ArrayList<String> imageNames = getImageNames();


			//making sure the name of the image is consistent between the csv and actual file
			String[]split1 = imageNames.get(imageNames.size()-1).split("/");
			String f = split1[split1.length-1];
			String[]split2 = f.split("\\.");
			int l = split2[0].length();
			for(int i=0;i<nuclei.length;i++)
			{
				String n = "";
				for(int j=l;j>nuclei[i].image.length();j--)
				{
					n = "0" + n;
				}
				n = n + nuclei[i].image;
				nuclei[i].image = n;
			}

			System.out.println("Successfully read .csv and read image filenames.");

			new Filter(nuclei, imageNames);

		}
	}
}
