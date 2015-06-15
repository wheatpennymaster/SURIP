import java.util.ArrayList;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import java.io.BufferedWriter;
import java.io.FileWriter;

import java.util.Arrays;
public class Filter
{
	ArrayList<Nuclei>nuclei;
	Image_nuclei[]i_nuclei;

	ArrayList<String>filenames;
	BufferedImage[]images;

	public Filter(ArrayList<Nuclei>nuclei, ArrayList<String>filenames)
	{
		this.nuclei=nuclei;
		this.filenames=filenames;
		images = new BufferedImage[filenames.size()+1];

		i_nuclei = new Image_nuclei[filenames.size()+1];
		for(int i=0;i<nuclei.size();i++)
		{
			int cur_image = Integer.parseInt(nuclei.get(i).image);
			int last = i;
			ArrayList<Nuclei>temp = new ArrayList<Nuclei>();			
			for(int j=i;(j<nuclei.size()) && (Integer.parseInt(nuclei.get(j).image) == cur_image);j++)
			{
				temp.add(nuclei.get(j));
				last = j;
			}
			i_nuclei[cur_image] = new Image_nuclei(temp);
			i=last;
		}
		getImages();

		//go();
		goAnew();
		goAgain();

		write_images();
		write_csv();
	}

	void go()
	{
		System.out.println("Starting filter.");
		int count = 0;
		for(int i=0;i<nuclei.size();i++)
		{
			boolean del = true;
			double dist = 1.5;
			Image_nuclei ln = i_nuclei[Integer.parseInt(nuclei.get(i).image)-1];
			Image_nuclei un = i_nuclei[Integer.parseInt(nuclei.get(i).image)+1];

			if((ln==null) && (un==null))							//no image in either preceding or succeeding slice
			{
				del = true;
			}
			else if(ln==null)								//no image in preceding slice
			{
				for(int j=0;j<un.nuclei.size();j++)
				{
					double ux_diff = Math.abs(un.nuclei.get(j).x - nuclei.get(i).x);
					double uy_diff = Math.abs(un.nuclei.get(j).y - nuclei.get(i).y);
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
				for(int j=0;j<ln.nuclei.size();j++)
				{
					double lx_diff = Math.abs(ln.nuclei.get(j).x - nuclei.get(i).x);
					double ly_diff = Math.abs(ln.nuclei.get(j).y - nuclei.get(i).y);
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
				for(int j=0;j<ln.nuclei.size();j++)
				{
					double lx_diff = Math.abs(ln.nuclei.get(j).x - nuclei.get(i).x);
					double ly_diff = Math.abs(ln.nuclei.get(j).y - nuclei.get(i).y);
					double l_dist = Math.pow( Math.pow(lx_diff,2) + Math.pow(ly_diff,2) , .5);

					if(l_dist <= Global.dist)
					{
						del = false;
						break;
					}
				}
				for(int j=0;j<un.nuclei.size();j++)
				{
					double ux_diff = Math.abs(un.nuclei.get(j).x - nuclei.get(i).x);
					double uy_diff = Math.abs(un.nuclei.get(j).y - nuclei.get(i).y);
					double u_dist = Math.pow( Math.pow(ux_diff,2) + Math.pow(uy_diff,2) , .5);

					if(u_dist <= Global.dist)
					{
						del = false;
						break;
					}
				}
			}
			if(del)
			{	//4 pixel square
				count++;
				//System.out.println("Removing nuclei " + nuclei.get(i).number + " from image " + nuclei.get(i).image + " by editing file " + filenames.get(Integer.parseInt(nuclei.get(i).image)-1) );

				BufferedImage cur = images[Integer.parseInt(nuclei.get(i).image)-1];
				BufferedImage edit = images[Integer.parseInt(nuclei.get(i).image)-1];

				int pixel = cur.getRGB((int) (nuclei.get(i).x),(int) (nuclei.get(i).y));
				if(pixel != Color.BLACK.getRGB())
				{
					for(int xx=((int) (nuclei.get(i).x))-4;xx<((int) (nuclei.get(i).x))+4;xx++)
					{
						for(int yy=((int) (nuclei.get(i).y))-4;yy<((int) (nuclei.get(i).y))+4;yy++)
						{
							if((cur.getRGB(xx,yy)==pixel))
								edit.setRGB(xx,yy,0);
						}
					}
					//edit.setRGB((int) (nuclei.get(i).x),(int) (nuclei.get(i).y), Color.RED.getRGB());
					images[Integer.parseInt(nuclei.get(i).image)-1] = edit;
				}
				remove(i);
				i=i-1;
			}
		}

		System.out.println("Finished filter. " + count + " nuclei removed.");
	}

	void goAgain()
	{
		System.out.println("Starting second filter.");
		int count = 0;

		for(int i=0;i<nuclei.size();i++)
		{
			double[]mins = new double[Global.num_neighbors];
			for(int m=0;m<mins.length;m++)
				mins[m] = Double.MAX_VALUE;
			double sum = 0;
			if(i_nuclei[Integer.parseInt(nuclei.get(i).image)].nuclei.size() > Global.min_neighbors)
			{
				for(int j=0;j<i_nuclei[Integer.parseInt(nuclei.get(i).image)].nuclei.size();j++)
				{
					double dadist = Math.pow( Math.pow(nuclei.get(i).x - i_nuclei[Integer.parseInt(nuclei.get(i).image)].nuclei.get(j).x,2) + Math.pow(nuclei.get(i).y - i_nuclei[Integer.parseInt(nuclei.get(i).image)].nuclei.get(j).y,2),.5);
					Arrays.sort(mins);
					if(mins[mins.length-1]>dadist)
						mins[mins.length-1] = dadist;
				}
				for(int j=0;j<mins.length;j++)
				{
					sum = sum + mins[j];
					//System.out.println(sum);
				}
			}
			//System.out.println(sum);
			if(sum>Global.min_dist)
			{
				count++;
				//System.out.println("Removing nuclei " + nuclei.get(i).number + " from image " + nuclei.get(i).image + " by editing file " + filenames.get(Integer.parseInt(nuclei.get(i).image)-1) );

				BufferedImage cur = images[Integer.parseInt(nuclei.get(i).image)-1];
				BufferedImage edit = images[Integer.parseInt(nuclei.get(i).image)-1];

				int pixel = cur.getRGB((int) (nuclei.get(i).x),(int) (nuclei.get(i).y));
				if(pixel != Color.BLACK.getRGB())
				{
					for(int xx=((int) (nuclei.get(i).x))-4;xx<((int) (nuclei.get(i).x))+4;xx++)
					{
						for(int yy=((int) (nuclei.get(i).y))-4;yy<((int) (nuclei.get(i).y))+4;yy++)
						{
							if(cur.getRGB(xx,yy)==pixel)
								edit.setRGB(xx,yy,0);
						}
					}
					//edit.setRGB((int) (nuclei.get(i).x),(int) (nuclei.get(i).y), Color.YELLOW.getRGB());
					images[Integer.parseInt(nuclei.get(i).image)-1] = edit;	 
				}
				remove(i);
				i=i-1;
			}
		}	
		System.out.println("Finished second filter. " + count + " nuclei removed.");
	}

	void goAnew()
	{
		System.out.println("Starting third filter.");
		int count = 0;

		for(int i=0;i<nuclei.size();i++)
		{
			boolean del = false;
			BufferedImage cur = images[Integer.parseInt(nuclei.get(i).image)-1];
			int pixel = cur.getRGB((int) (nuclei.get(i).x),(int) (nuclei.get(i).y));

			if(pixel != Color.BLACK.getRGB())
			{
				for(int xx=((int) (nuclei.get(i).x))-4;xx<((int) (nuclei.get(i).x))+4;xx++)
				{
					for(int yy=((int) (nuclei.get(i).y))-4;yy<((int) (nuclei.get(i).y))+4;yy++)
					{
						if(cur.getRGB(xx,yy)==pixel)
						{
							int n_count = 0;
							for(int px=xx-1;px<xx+2;px++)
							{
								for(int py=yy-1;py<yy+2;py++)
								{
									if(cur.getRGB(px,py)==cur.getRGB(xx,yy))
										n_count++;
								}
							}
							if(n_count<4)
								del = true;
						}
					}
				}
			}

			if(del)
			{
				count++;

				BufferedImage edit = images[Integer.parseInt(nuclei.get(i).image)-1];

				if(pixel != Color.BLACK.getRGB())
				{
					for(int xx=((int) (nuclei.get(i).x))-4;xx<((int) (nuclei.get(i).x))+4;xx++)
					{
						for(int yy=((int) (nuclei.get(i).y))-4;yy<((int) (nuclei.get(i).y))+4;yy++)
						{
							if(cur.getRGB(xx,yy)==pixel)
								edit.setRGB(xx,yy,0);
						}
					}
					//edit.setRGB((int) (nuclei.get(i).x),(int) (nuclei.get(i).y), Color.ORANGE.getRGB());
					images[Integer.parseInt(nuclei.get(i).image)-1] = edit;
				}
				remove(i);
				i=i-1;
			}		
		}
		System.out.println("Finished third filter. " + count + " nuclei removed.");

	}

	void remove(int i)
	{
		for(int j=0;j<i_nuclei[Integer.parseInt(nuclei.get(i).image)].nuclei.size();j++)
		{
			if(i_nuclei[Integer.parseInt(nuclei.get(i).image)].nuclei.get(j).number==nuclei.get(i).number)
			{
				i_nuclei[Integer.parseInt(nuclei.get(i).image)].nuclei.remove(j);
				break;
			}
		}
		if(i_nuclei[Integer.parseInt(nuclei.get(i).image)].nuclei.size()==0)
			i_nuclei[Integer.parseInt(nuclei.get(i).image)]=null;
		nuclei.remove(i);
		Global.csvfile.remove(i);
	}

	void write_images()
	{
		System.out.println("Writing images.");
		try
		{
			int white = new Color(255,255,255).getRGB();
			int black = new Color(0,0,0).getRGB();
			new File(Global.outPath).mkdir();
			for(int i=0;i<filenames.size();i++)
			{
				for(int j=0;j<images[i].getWidth();j++)
				{
					for(int k=0;k<images[i].getHeight();k++)
					{
						//images[i].setRGB(j,k,images[i].getRGB(j,k));
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
		System.out.println("Finished writing images.");
	}

	void write_csv()
	{
		System.out.println("Writing csv.");
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
				
		System.out.println("Finished writing csv.");			
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
				System.out.println(i_nuclei[i].nuclei.size());
		} 
	}
}
