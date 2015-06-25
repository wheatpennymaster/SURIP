import java.util.ArrayList;

import java.io.File;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;

import java.text.DecimalFormat;
public class Count
{
	ArrayList<Nuclei>nuclei;
	Image_nuclei[]i_nuclei;
	Node[]nodes;

	public Count(ArrayList<Nuclei>nuclei, ArrayList<String>filenames)
	{
		this.nuclei = new ArrayList<Nuclei>();

		nodes = new Node[nuclei.size()];

		for(int i=0;i<nuclei.size();i++)
		{
			nodes[i] = new Node(nuclei.get(i),i);
			this.nuclei.add(new Nuclei(nuclei.get(i).image, nuclei.get(i).number, nuclei.get(i).x, nuclei.get(i).y, i));
		}

		i_nuclei = new Image_nuclei[filenames.size()+1+1];

		for(int i=0;i<nuclei.size();i++)
		{
			int cur_image = Integer.parseInt(this.nuclei.get(i).image);
			int last = i;
			ArrayList<Nuclei>temp = new ArrayList<Nuclei>();
			for(int j=i;(j<this.nuclei.size()) && (Integer.parseInt(this.nuclei.get(j).image) == cur_image);j++)
			{
				temp.add(this.nuclei.get(j));
				last = j;
			}
			i_nuclei[cur_image] = new Image_nuclei(temp);
			i=last;
		}

		build();
		explore(0);
		//write_csv();
	}

	void build()
	{
		System.out.println("Building graph.");
		double dist = 1.4;
		for(int i=0;i<nuclei.size();i++)
		{
			Image_nuclei ln = i_nuclei[Integer.parseInt(nuclei.get(i).image)-1];
			Image_nuclei un = i_nuclei[Integer.parseInt(nuclei.get(i).image)+1];

			int test = 0;
			if(ln==null)
			{
				for(int j=0;j<un.nuclei.size();j++)
				{
					if( (un.nuclei.get(j).x == nuclei.get(i).x) && (un.nuclei.get(j).y == nuclei.get(i).y) )
					{
						nodes[i].un = new Node(un.nuclei.get(j),un.nuclei.get(j).index);
						break;
					}
				}
				if(nodes[i].un == null)
				{
					for(int j=0;j<un.nuclei.size();j++)
					{
						double d = Math.pow( Math.pow( Math.abs(un.nuclei.get(j).x - nuclei.get(i).x), 2) + Math.pow( Math.abs(un.nuclei.get(j).y - nuclei.get(i).y), 2), .5);
						if(d<dist)
						{
							nodes[i].un = new Node(un.nuclei.get(j),un.nuclei.get(j).index);
							test++;//break;
						}
					}
				}
			}
			else if(un==null)
			{
				for(int j=0;j<ln.nuclei.size();j++)
				{
					if( (ln.nuclei.get(j).x == nuclei.get(i).x) && (ln.nuclei.get(j).y == nuclei.get(i).y) )
					{
						nodes[i].ln = new Node(ln.nuclei.get(j),ln.nuclei.get(j).index);
						break;
					}
				}
				if(nodes[i].ln == null)
				{
					for(int j=0;j<ln.nuclei.size();j++)
					{
						double d = Math.pow( Math.pow( Math.abs(ln.nuclei.get(j).x - nuclei.get(i).x), 2) + Math.pow( Math.abs(ln.nuclei.get(j).y - nuclei.get(i).y), 2), .5);
						if(d<dist)
						{
							nodes[i].ln = new Node(ln.nuclei.get(j),ln.nuclei.get(j).index);
							test++;//break;
						}
					}
				}
			}
			else
			{
				for(int j=0;j<un.nuclei.size();j++)
				{
					if( (un.nuclei.get(j).x == nuclei.get(i).x) && (un.nuclei.get(j).y == nuclei.get(i).y) )
					{
						nodes[i].un = new Node(un.nuclei.get(j),un.nuclei.get(j).index);
						break;
					}
				}
				if(nodes[i].un == null)
				{
					for(int j=0;j<un.nuclei.size();j++)
					{
						double d = Math.pow( Math.pow( Math.abs(un.nuclei.get(j).x - nuclei.get(i).x), 2) + Math.pow( Math.abs(un.nuclei.get(j).y - nuclei.get(i).y), 2), .5);
						if(d<dist)
						{
							nodes[i].un = new Node(un.nuclei.get(j),un.nuclei.get(j).index);
							test++;//break;
						}
					}
				}

				for(int j=0;j<ln.nuclei.size();j++)
				{
					if( (ln.nuclei.get(j).x == nuclei.get(i).x) && (ln.nuclei.get(j).y == nuclei.get(i).y) )
					{
						nodes[i].ln = new Node(ln.nuclei.get(j),ln.nuclei.get(j).index);
						break;
					}
				}
				if(nodes[i].ln == null)
				{
					for(int j=0;j<ln.nuclei.size();j++)
					{
						double d = Math.pow( Math.pow( Math.abs(ln.nuclei.get(j).x - nuclei.get(i).x), 2) + Math.pow( Math.abs(ln.nuclei.get(j).y - nuclei.get(i).y), 2), .5);
						if(d<dist)
						{
							nodes[i].ln = new Node(ln.nuclei.get(j),ln.nuclei.get(j).index);
							test++;//break;
						}
					}
				}
			}
			if(test>2)
				System.out.println(nuclei.get(i).image + " " +  nuclei.get(i).x + " " + nuclei.get(i).y);
		}
		System.out.println("Finished building graph.");
	}

	public void explore_help(int i)
	{
		//System.out.print(nodes[i].n.image + " (" + nodes[i].n.x + "," + nodes[i].n.y + ") ");
		String[]temp = Global.csvfile.get(i+1);
		System.out.print(new DecimalFormat("#.##").format(Double.parseDouble(temp[2])) + ",");
		if(nodes[i].un==null)
		{
			System.out.println();
			return;
		}
		else
			explore_help(nodes[i].un.index);
	}

	public void explore(int s)
	{
		for(int i=0;i<nodes.length;i++)
		{
			if(nodes[i].ln==null)
			{
				//System.out.print("(" + nodes[i].n.x + "," + nodes[i].n.y + ") ");
				explore_help(i);
			}
				
		}
	}

	void write_csv()
	{
		System.out.println("Writing analyzed csv.");
		try
		{
			String filename = "output.csv";
			File file = new File(Global.outPath + filename);
				file.delete();
				file.createNewFile();	
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			for(int i=0;i<Global.csvfile.size();i++)
			{
				String writeMe = "";
				String[]temp = Global.csvfile.get(i);
				for(int j=0;j<temp.length;j++)
				{
					writeMe = writeMe + temp[j] + ",";
				}
				bw.write(writeMe + "\n");
			}
			bw.close();
			fw.close();
		} catch(Exception e){e.printStackTrace();}
				
		System.out.println("Finished writing analyzed csv.");
	}

}
