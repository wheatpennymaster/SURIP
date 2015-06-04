import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class remove_filter
{
	static String[][] readall(String filename) throws IOException
	{
		String[][] r = new String[2163][53];
		BufferedReader br_r = new BufferedReader(new FileReader(filename));
		String line_r;
		int count_r = 0;
		
		while (count_r<2162)
		{
			line_r = br_r.readLine();
			String[]temp = line_r.split(",");
			System.out.print(count_r + ": ");
			for(int i=0;i<53;i++)
			{
				r[count_r][i]=temp[i];
				System.out.print(i + " ");
			}
			System.out.println();
			
			count_r++;
		}
		br_r.close();
		
		return r;
	}

	static String[][] readfilter(String filename) throws IOException
	{
                String[][] r = new String[261][2];
                BufferedReader br_r = new BufferedReader(new FileReader(filename));
                String line_r;
                int count_r = 0;

                while (count_r<261)
                {
                        line_r = br_r.readLine();
                        String[]temp = line_r.split("\t");
			System.out.println(temp[0] + " " + temp[1]);
                        r[count_r][0]=temp[0];
			r[count_r][1]=temp[1];

                        count_r++;
                }
                br_r.close();

                return r;
	}

	static void write(String filename)
	{

	}

	public static void main(String[]args) throws IOException
	{
		String[][]input = readall("export_filterNuclei.csv");
		String[][]filter = readfilter("remove.txt");

		int filter_count = 0;
		for(int i=0;i<2162;i++)
		{
			if((input[i][0].equals(filter[filter_count][0])) && (input[i][1].equals(filter[filter_count][1])))
			{filter_count++;}
			else
			{
				for(int j=0;j<53;j++)
				{
					System.out.print(input[i][j] + ",");
				}
				System.out.println();
			}
		}
				
	}
}
