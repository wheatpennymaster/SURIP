import java.util.ArrayList;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Filter
{
	Nuclei[]nuclei;
	Image_nuclei[]i_nuclei;

	ArrayList<String>filenames;
	BufferedImage[]images;

	public Filter(Nuclei[]nuclei, ArrayList<String>filenames)
	{
		this.nuclei=nuclei;
		this.filenames=filenames;
		images = new BufferedImage[filenames.size()];
		i_nuclei = new Image_nuclei[filenames.size()];
		for(int i=0;i<nuclei.length;i++)
		{
			int cur_image = Integer.parseInt(nuclei[i].image);
			int last = i;
			ArrayList<Nuclei>temp = new ArrayList<Nuclei>();			
			for(int j=i;(j<nuclei.length) && (Integer.parseInt(nuclei[j].image) == cur_image);j++)
			{
				temp.add(nuclei[j]);
				last = j;
			}
			Nuclei[]temp2 = new Nuclei[temp.size()];
			for(int j=0;j<temp2.length;j++)
				temp2[j]=temp.get(j);
			i_nuclei[cur_image] = new Image_nuclei(temp2);
			i=last;
		}
		print();			
		go();
	}

	void go()
	{
		getImages();
		System.out.println("Starting filter...");

		for(int i=0;i<nuclei.length;i++)
		{
			Nuclei[]ln = i_nuclei[Integer.parseInt(nuclei[i].image)-1].nuclei;
			Nuclei[]un = i_nuclei[Integer.parseInt(nuclei[i].image)+1].nuclei;

			if((ln==null) && (un==null))	//no image in either preceding or succeeding slice
			{
				System.out.println(nuclei[i].image + " has no neighbors");
			}
			else if(ln==null)								//no image in preceding slice
			{
				System.out.println(nuclei[i].image + " has an upper neighbor");
			}
			else if(un==null)								//no image in succeeding slice
			{
				System.out.println(nuclei[i].image + " has a lower neighbor");
			}
			else										//image in both preceding and succeeding slice
			{
				System.out.println(nuclei[i].image + " has both neighbors");
			}
		}
			
	}

	void getImages()
	{
		for(int i=0;i<filenames.size();i++)
		{
			BufferedImage in = null;
			try
			{
				in = ImageIO.read(new File(filenames.get(i)));
				images[i] = in;	
			}
			catch(Exception e){}
		}
		System.out.println("Successfully read images.");
	}

	void print()
	{
		for(int i=0;i<i_nuclei.length;i++)
		{
			System.out.print(i + " ");
			if(i_nuclei[i]==null)
				System.out.println("null");
			else
				System.out.println(i_nuclei[i].nuclei.length);
		} 
	}
}
