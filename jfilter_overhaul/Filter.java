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
		images = new BufferedImage[filenames.size()+1+1];	//0-th entry is null to allow indexing by image number; n-th index is null for filter 1)

		i_nuclei = new Image_nuclei[filenames.size()+1+1];	//0-th entry is null to allow indexing by image number; n-th index is null for filter 1)
		for(int i=0;i<nuclei.size();i++)			//building the structure i_nuclei[] which keeps a list of objects for a given image, indexed by the image number
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
		//getImages();

		int cc = 0;
		/*
		for(int i=0;i<nuclei.size();i++)
		{
			BufferedImage cur = images[Integer.parseInt(nuclei.get(i).image)-1];
			int pixel = cur.getRGB((int) (nuclei.get(i).x),(int) (nuclei.get(i).y));
			if(pixel==Color.BLACK.getRGB())
				remove(i);
		}
		*/
		//filter();

		//write_images();
		//write_csv();

		new Count(nuclei,filenames);
		//new Gradient(nuclei, images, filenames);
	}

	//chooses a which filters to use and in what order based on command line argument
	void filter()
	{
		switch(Global.f_order)
		{
			case "123":
				go();
				goAgain();
				goAnew();
				break;
			case "132":
				go();
				goAnew();
				goAgain();
				break;
			case "213":
				goAgain();
				go();
				goAnew();
				break;
			case "231":
				goAgain();
				goAnew();
				go();
				break;
			case "312":
				goAnew();
				go();
				goAgain();
				break;
			case "321":
				goAnew();
				goAgain();
				go();
				break;
			case "12":
				go();
				goAgain();
				break;
			case "13":
				go();
				goAnew();
				break;
			case "21":
				goAgain();
				go();
				break;
			case "23":
				goAgain();
				goAnew();
				break;
			case "31":
				goAnew();
				go();
				break;
			case "32":
				goAnew();
				goAgain();
				break;
			case "1":
				go();
				break;
			case "2":
				goAgain();
				break;
			case "3":
				goAnew();
				break;
			case "0":
				break;
			default:
				go();
				goAnew();
				goAgain();
				break;
		}
	}

	//this filters out objects that only appear on one image slice; a command line value is given to specify what is considered the same object across images 
	void go()
	{
		System.out.println("Starting first filter.");
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
				Image_nuclei uun = i_nuclei[Integer.parseInt(nuclei.get(i).image)+2];
				if(uun==null)
					del=true;
				else
				{
					for(int j=0;j<un.nuclei.size();j++)					//comparing the current object to all the objects on the succeeding slice
					{
						double ux_diff = Math.abs(un.nuclei.get(j).x - nuclei.get(i).x);
						double uy_diff = Math.abs(un.nuclei.get(j).y - nuclei.get(i).y);
						double u_dist = Math.pow( Math.pow(ux_diff,2) + Math.pow(uy_diff,2) , .5);

						if(u_dist <= Global.dist)
						{
							for(int jj=0;jj<uun.nuclei.size();jj++)
							{
								double uux_diff = Math.abs(un.nuclei.get(j).x - uun.nuclei.get(jj).x);
								double uuy_diff = Math.abs(un.nuclei.get(j).y - uun.nuclei.get(jj).y);
								double uu_dist = Math.pow( Math.pow(uux_diff,2) + Math.pow(uuy_diff,2) , .5);
								//if(uu_dist <= Global.dist)
								//{
									del=false;
									break;
								//}
							}
							if(!del)
								break;
						}
					}
				}
			}
			else if(un==null)								//no image in succeeding slice
			{
				Image_nuclei lln = i_nuclei[Integer.parseInt(nuclei.get(i).image)-2];
				if(lln==null)
					del=true;
				else
				{
					for(int j=0;j<ln.nuclei.size();j++)					//comparing the current object to all the objects on the preceding slice
					{
						double lx_diff = Math.abs(ln.nuclei.get(j).x - nuclei.get(i).x);
						double ly_diff = Math.abs(ln.nuclei.get(j).y - nuclei.get(i).y);
						double l_dist = Math.pow( Math.pow(lx_diff,2) + Math.pow(ly_diff,2) , .5);

						if(l_dist <= Global.dist)
						{
							for(int jj=0;jj<lln.nuclei.size();jj++)
							{
								double llx_diff = Math.abs(ln.nuclei.get(j).x - lln.nuclei.get(jj).x);
								double lly_diff = Math.abs(ln.nuclei.get(j).y - lln.nuclei.get(jj).y);
								double ll_dist = Math.pow( Math.pow(llx_diff,2) + Math.pow(lly_diff,2) , .5);

								//if(ll_dist <= Global.dist)
								//{
									del = false;
									break;
								//}
							}
							if(!del)
								break;
						}
					}
				}
			}
			else										//image in both preceding and succeeding slice
			{
				for(int j=0;j<ln.nuclei.size();j++)					//comparing the current object to all the objects on the preceding slice
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
				for(int j=0;j<un.nuclei.size();j++)					//comparing the current object to all the objects on the succeeding slice
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
			{	//if it is determined that a nucleus should be removed, we blackout 7x7 pixel square around the center of the object
				count++;
				//System.out.println("Removing nuclei " + nuclei.get(i).number + " from image " + nuclei.get(i).image + " by editing file " + filenames.get(Integer.parseInt(nuclei.get(i).image)-1) );

				BufferedImage cur = images[Integer.parseInt(nuclei.get(i).image)-1];
				BufferedImage edit = images[Integer.parseInt(nuclei.get(i).image)-1];

				int pixel = cur.getRGB((int) (nuclei.get(i).x),(int) (nuclei.get(i).y));
				if(pixel != Color.BLACK.getRGB())
				{
					for(int xx=((int) (nuclei.get(i).x))-4;(xx<((int) (nuclei.get(i).x))+4) && (xx>-1) && (xx<cur.getWidth());xx++)
					{
						for(int yy=((int) (nuclei.get(i).y))-4;(yy<((int) (nuclei.get(i).y))+4) && (yy>-1) && (yy<cur.getHeight());yy++)
						{
							if((cur.getRGB(xx,yy)==pixel))
								edit.setRGB(xx,yy,0);
						}
					}
					//edit.setRGB((int) (nuclei.get(i).x),(int) (nuclei.get(i).y), Color.RED.getRGB());
					images[Integer.parseInt(nuclei.get(i).image)-1] = edit;
				}
				remove(i);
				i=i-1;		//we decrement i because an object has been removed from arraylist nuclei
			}
		}

		System.out.println("Finished first filter. " + count + " nuclei removed.");
	}

	//filters out objects that are far away from other objects; an image must first have a specified number of objects, then it finds a specified number of closest neighbors and sums the distance 
	//between the current object and the neighbors; if this sum is above a specified threshold, the object is removed
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
			if(i_nuclei[Integer.parseInt(nuclei.get(i).image)].nuclei.size() > Global.min_neighbors)	//we want a mininmum number of objects in the image to avoid end cases
			{
				for(int j=0;j<i_nuclei[Integer.parseInt(nuclei.get(i).image)].nuclei.size();j++)	//finding closest neighbors
				{
					double dadist = Math.pow( Math.pow(nuclei.get(i).x - i_nuclei[Integer.parseInt(nuclei.get(i).image)].nuclei.get(j).x,2) + Math.pow(nuclei.get(i).y - i_nuclei[Integer.parseInt(nuclei.get(i).image)].nuclei.get(j).y,2),.5);
					Arrays.sort(mins);
					if(mins[mins.length-1]>dadist)
						mins[mins.length-1] = dadist;
				}
				for(int j=0;j<mins.length;j++)	//summing distances between current object and closests neighbors
				{
					sum = sum + mins[j];
					//System.out.println(sum);
				}
			}
			//System.out.println(sum);
			if(sum>Global.min_dist)		//deleting
			{
				count++;
				//System.out.println("Removing nuclei " + nuclei.get(i).number + " from image " + nuclei.get(i).image + " by editing file " + filenames.get(Integer.parseInt(nuclei.get(i).image)-1) );

				BufferedImage cur = images[Integer.parseInt(nuclei.get(i).image)-1];
				BufferedImage edit = images[Integer.parseInt(nuclei.get(i).image)-1];

				int pixel = cur.getRGB((int) (nuclei.get(i).x),(int) (nuclei.get(i).y));
				if(pixel != Color.BLACK.getRGB())
				{
					for(int xx=((int) (nuclei.get(i).x))-4;(xx<((int) (nuclei.get(i).x))+4)  && (xx>-1) && (xx<cur.getWidth());xx++)
					{
						for(int yy=((int) (nuclei.get(i).y))-4;(yy<((int) (nuclei.get(i).y))+4) && (yy>-1) && (yy<cur.getHeight());yy++)
						{
							if(cur.getRGB(xx,yy)==pixel)
								edit.setRGB(xx,yy,0);
						}
					}
					//edit.setRGB((int) (nuclei.get(i).x),(int) (nuclei.get(i).y), Color.YELLOW.getRGB());
					images[Integer.parseInt(nuclei.get(i).image)-1] = edit;	 
				}
				remove(i);
				i=i-1;	//we decrement i because an object has been removed from the arraylist nuclei
			}
		}	
		System.out.println("Finished second filter. " + count + " nuclei removed.");
	}

	//filters out oddly shaped objects; if an object has a pixel that has fewer than a specified number of neighbors, the object is removed
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
					for(int yy=((int) (nuclei.get(i).y))-4;(yy<((int) (nuclei.get(i).y))+4) && (yy>-1) && (yy<cur.getHeight());yy++)
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
							if(n_count<Global.min_pixel_n)
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
					for(int xx=((int) (nuclei.get(i).x))-4;(xx<((int) (nuclei.get(i).x))+4) && (xx>-1) && (xx<cur.getWidth());xx++)
					{
						for(int yy=((int) (nuclei.get(i).y))-4;(yy<((int) (nuclei.get(i).y))+4) && (yy>-1) && (yy<cur.getHeight());yy++)
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

	//removes an object from all associated data structures. 
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
		Global.csvfile.remove(i+1);
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
		System.out.println("Reading images");
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
