import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.Graphics;

import java.util.ArrayList;

public class Flipper extends JComponent implements KeyListener
{
	ArrayList<String>filenames;
	BufferedImage[]images;
	ArrayList<String>filenames2;
	BufferedImage[]images2;

	JFrame f;
	JPanel p;
	JLabel im;
	BufferedImage cur;
	int cur_i;
	boolean ct;

	public Flipper(String inPath, String inPath2)
	{
		filenames = getImageNames(inPath);
		images = getImages(filenames);
		filenames2 = getImageNames(inPath2);
		images2 = getImages(filenames2);

		cur = images[1];
		cur_i = 1;
		ct = true;
		im = new JLabel(new ImageIcon(cur));
		f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.getContentPane().add(im);
		f.pack();
		f.addKeyListener(this);
		f.setLocation(200,200);
		f.setVisible(true);

/*
		p = new JPanel();
		im = new JLabel(new ImageIcon(images[1]));
		p.add(im);
		add(p);
		addKeyListener(this);
		setSize(images[0].getWidth()+20,images[0].getHeight()+30);
		setLocation(200,200);
		setVisible(true);
*/
	}
	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.drawImage(cur,0,0,this);
	}

	ArrayList<String> getImageNames(String inPath)
	{
		ArrayList<String> resultList = new ArrayList<String>();

		try
		{
			File[] f = new File(inPath).listFiles();
			for (File file : f)
			{
				if (file != null && (file.getName().toLowerCase().endsWith(".png") || file.getName().toLowerCase().endsWith(".jpg")))
					resultList.add(file.getCanonicalPath());
			}
		}
		catch (IOException e) { e.printStackTrace(); }

		return resultList;
	}

	BufferedImage[] getImages(ArrayList<String>filenames)
	{
		BufferedImage[]images = new BufferedImage[filenames.size()];
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
		return images;
	}

	@Override
	public void keyTyped(KeyEvent e)
	{
		/*
		if(e.getKeyCode() == KeyEvent.VK_RIGHT)
			System.out.println("right");
		if(e.getKeyCode() == KeyEvent.VK_LEFT)
			System.out.println("left");
		if(e.getKeyCode() == KeyEvent.VK_UP)
			System.out.println("up");
		if(e.getKeyCode() == KeyEvent.VK_DOWN)
			System.out.println("down");
		*/
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		if(e.getKeyCode() == KeyEvent.VK_RIGHT)
		{
			f.remove(im);
			f.repaint();
			cur = images2[cur_i];
			ct = false;
			im = new JLabel(new ImageIcon(cur));
			f.getContentPane().add(im);
			f.repaint();
			f.pack();
			//System.out.println("right");
		}
		if(e.getKeyCode() == KeyEvent.VK_LEFT)
		{
			f.remove(im);
			f.repaint();
			cur = images[cur_i];
			ct = true;
			im = new JLabel(new ImageIcon(cur));
			f.getContentPane().add(im);
			f.repaint();
			f.pack();
			//System.out.println("left");
		}
		if(e.getKeyCode() == KeyEvent.VK_UP)
		{
			if(cur_i==images.length-1)
			{}
			else
			{
				f.remove(im);
				f.repaint();
				if(ct)
					cur = images[cur_i+1];
				else
					cur = images2[cur_i+1];
				cur_i++;
				im = new JLabel(new ImageIcon(cur));
				f.getContentPane().add(im);
				f.repaint();
				f.pack();
			}
			//System.out.println("up");
		}
		if(e.getKeyCode() == KeyEvent.VK_DOWN)
		{
			if(cur_i==1)
			{}
			else
			{
				f.remove(im);
				f.repaint();
				if(ct)
					cur = images[cur_i-1];
				else
					cur = images2[cur_i-1];
				cur_i--;
				im = new JLabel(new ImageIcon(cur));
				f.getContentPane().add(im);
				f.repaint();
				f.pack();
			}
			//System.out.println("down");
		}
	}


	@Override
	public void keyReleased(KeyEvent e)
	{
		/*
		if(e.getKeyCode() == KeyEvent.VK_RIGHT)
			System.out.println("right");
		if(e.getKeyCode() == KeyEvent.VK_LEFT)
			System.out.println("left");
		if(e.getKeyCode() == KeyEvent.VK_UP)
			System.out.println("up");
		if(e.getKeyCode() == KeyEvent.VK_DOWN)
			System.out.println("down");
		*/
	}
}
