import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.io.File;
import java.util.ArrayList;

import java.util.LinkedList;

public class driver
{
	static ArrayList<String[]> read_csv(String filename)
	{
		BufferedReader br = null;
		String line = "";
		ArrayList<String[]>l = new ArrayList<String[]>();

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

		return l;
	}

	static ArrayList<String> getImageNames()
	{
		ArrayList<String> resultList = new ArrayList<String>();

		try
		{
			File[] f = new File(Global.inPath).listFiles();
			for (File file : f)
			{
				if (file != null && file.getName().toLowerCase().endsWith(".png"))
					resultList.add(file.getCanonicalPath());
			}
		}
		catch (IOException e) { e.printStackTrace(); }

		return resultList;
	}

	static void getArguments(String[]args)
	{
		
	}

	public static void main(String[]args)
	{
		//java driver inPath outPath input_csv.csv dist num_neighbors min_neighbors min_dist
		if(args.length < 9)
			System.out.println("Usage:\njava driver inPath outPath path_to_input_csv.csv dist");
		else
		{
			Global.inPath = args[0];
			Global.outPath = args[1];
			Global.csv = args[2];
			Global.dist = Double.parseDouble(args[3]);
			Global.num_neighbors = Integer.parseInt(args[4]);
			Global.min_neighbors = Integer.parseInt(args[5]);
			Global.min_dist = Double.parseDouble(args[6]);
			Global.min_pixel_n = Integer.parseInt(args[7]);
			Global.f_order = args[8];

			System.out.println("Started reading in .csv and image filenames.");

			ArrayList<String[]>input = read_csv(args[2]);
			Global.csvfile = input;

			ArrayList<Nuclei>nuclei = new ArrayList<Nuclei>();
			for(int i=1;i<input.size();i++)
				nuclei.add(new Nuclei(input.get(i)));

			ArrayList<String> imageNames = getImageNames();


			//making sure the name of the image is consistent between the csv and actual file
			String[]split1 = imageNames.get(imageNames.size()-1).split("/");
			String f = split1[split1.length-1];
			String[]split2 = f.split("\\.");
			int l = split2[0].length();
			for(int i=0;i<nuclei.size();i++)
			{
				String n = "";
				for(int j=l;j>nuclei.get(i).image.length();j--)
				{
					n = "0" + n;
				}
				n = n + nuclei.get(i).image;
				String[]nn = new String[5];
				nn[0]=n; nn[1]=String.valueOf(nuclei.get(i).number); nn[3]=String.valueOf(nuclei.get(i).x); nn[4]=String.valueOf(nuclei.get(i).y);
				nuclei.set(i,new Nuclei(nn));
			}

			System.out.println("Finished reading in .csv and read image filenames.");
			System.out.println(nuclei.get(0).x);
			new Filter(nuclei, imageNames);

		}
	}
}
