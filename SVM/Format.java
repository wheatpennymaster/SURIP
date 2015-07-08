import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.io.File;
import java.util.ArrayList;

import java.io.BufferedWriter;
import java.io.FileWriter;

public class Format
{
	public static void main(String[]args)
	{
		System.out.println("Reading " + args[0]);
		BufferedReader br = null;
		String line = "";
		ArrayList<String[]>l = new ArrayList<String[]>();

		try
		{
			br = new BufferedReader(new FileReader(args[0]));
			while ((line = br.readLine()) != null)
			{
				String[]a = line.split("\t");
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

		System.out.println("Finished writing " + args[0]);


		System.out.println("Writing " + args[1]);
		try
		{
			String filename = args[1];
			File file = new File(filename);
				file.delete();
				file.createNewFile();	
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			for(int i=0;i<l.size();i++)
			{
				String writeMe = args[2] + " ";
				String[]temp = l.get(i);
				for(int j=5;j<temp.length-2;j++)		//skipping image number, object number, size, x, y
				{
					writeMe = writeMe + String.valueOf(j-4) + ":" + temp[j] + " ";
				}
				bw.write(writeMe + "\n");
			}
			bw.close();
			fw.close();
		} catch(Exception e){e.printStackTrace();}
				
		System.out.println("Finished writing " + args[1]);

	}
}
