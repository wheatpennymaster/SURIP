import java.util.ArrayList;

import java.awt.Graphics2D;
import java.awt.Color;
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
		go();
	}

	void go()
	{
		getImages();
		System.out.println("Starting filter.");
		int count = 0;

		for(int i=0;i<nuclei.length;i++)
		{
			boolean del = true;
			double dist = 1.5;
			Image_nuclei ln = i_nuclei[Integer.parseInt(nuclei[i].image)-1];
			Image_nuclei un = i_nuclei[Integer.parseInt(nuclei[i].image)+1];

			if((ln==null) && (un==null))							//no image in either preceding or succeeding slice
			{
				del = true;
			}
			else if(ln==null)								//no image in preceding slice
			{
				for(int j=0;j<un.nuclei.length;j++)
				{
					double ux_diff = Math.abs(un.nuclei[j].x - nuclei[i].x);
					double uy_diff = Math.abs(un.nuclei[j].y - nuclei[i].y);
					double u_dist = Math.pow( Math.pow(ux_diff,2) + Math.pow(uy_diff,2) , .5);

					if(u_dist <= Global.dist)
					{
						del = false;
						break;
					}
				}
			}
			else if(un==null)								//no image in succeeding slice
			{
				for(int j=0;j<ln.nuclei.length;j++)
				{
					double lx_diff = Math.abs(ln.nuclei[j].x - nuclei[i].x);
					double ly_diff = Math.abs(ln.nuclei[j].y - nuclei[i].y);
					double l_dist = Math.pow( Math.pow(lx_diff,2) + Math.pow(ly_diff,2) , .5);

					if(l_dist <= Global.dist)
					{
						del = false;
						break;
					}
				}
			}
			else										//image in both preceding and succeeding slice
			{
				for(int j=0;j<ln.nuclei.length;j++)
				{
					double lx_diff = Math.abs(ln.nuclei[j].x - nuclei[i].x);
					double ly_diff = Math.abs(ln.nuclei[j].y - nuclei[i].y);
					double l_dist = Math.pow( Math.pow(lx_diff,2) + Math.pow(ly_diff,2) , .5);

					if(l_dist <= Global.dist)
					{
						del = false;
						break;
					}
				}
				for(int j=0;j<un.nuclei.length;j++)
				{
					double ux_diff = Math.abs(un.nuclei[j].x - nuclei[i].x);
					double uy_diff = Math.abs(un.nuclei[j].y - nuclei[i].y);
					double u_dist = Math.pow( Math.pow(ux_diff,2) + Math.pow(uy_diff,2) , .5);

					if(u_dist <= Global.dist)
					{
						del = false;
						break;
					}
				}
			}
			if(del)
			{	//4 pixel radius
				count++;
				//System.out.println("Removing nuclei " + nuclei[i].number + " from image " + nuclei[i].image + " by editing file " + filenames.get(Integer.parseInt(nuclei[i].image)-1) );

				BufferedImage cur = images[Integer.parseInt(nuclei[i].image)-1];
				BufferedImage edit = images[Integer.parseInt(nuclei[i].image)-1];

				int pixel = cur.getRGB((int) (nuclei[i].x),(int) (nuclei[i].y));
				for(int xx=((int) (nuclei[i].x))-4;xx<((int) (nuclei[i].x))+4;xx++)
				{
					for(int yy=((int) (nuclei[i].y))-4;yy<((int) (nuclei[i].y))+4;yy++)
					{
						if(cur.getRGB(xx,yy)==pixel)
							edit.setRGB(xx,yy,0);
					}
				}
				images[Integer.parseInt(nuclei[i].image)-1] = edit;
			}
		}

		try
		{
			int white = new Color(255,255,255).getRGB();
			int black = new Color(0,0,0).getRGB();
			new File("output").mkdir();
			for(int i=0;i<filenames.size();i++)
			{
				for(int j=0;j<images[i].getWidth();j++)
				{
					for(int k=0;k<images[i].getHeight();k++)
					{
						if(images[i].getRGB(j,k)!=black)
							images[i].setRGB(j,k,white);
					}
				}
				String[]split = filenames.get(i).split("/");
				String new_path = Global.outPath + split[split.length-1];
				/*
				for(int j=1;j<split.length-2;j++)
					new_path = new_path + "/" + split[j];
				new_path = new_path + "/output/" + split[split.length-1];
				*/
				//System.out.println("Writing to " + new_path);
				ImageIO.write(images[i], "png", new File(new_path));
			}
		} catch(Exception e){}

		System.out.println("Finished filter. " + count + " nuclei removed.");
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
			catch(Exception e){e.printStackTrace();}
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
