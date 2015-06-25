import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class driver extends JFrame implements KeyListener
{
	JLabel l;

	public driver()
	{
		JPanel p = new JPanel();
		l = new JLabel("Key Listener");
		p.add(l);
		add(p);
		addKeyListener(this);
		setSize(200,200);
		setVisible(true);
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
			System.out.println("right");
		if(e.getKeyCode() == KeyEvent.VK_LEFT)
			System.out.println("left");
		if(e.getKeyCode() == KeyEvent.VK_UP)
			System.out.println("up");
		if(e.getKeyCode() == KeyEvent.VK_DOWN)
			System.out.println("down");
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

	public static void main(String[]args) throws IOException
	{

		/*
		File file = new File(args[0]);
		BufferedImage image = ImageIO.read(file);
		JLabel label = new JLabel(new ImageIcon(image));
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.getContentPane().add(label);
		f.pack();
		f.setLocation(200,200);
		f.setVisible(true);
		*/

		new driver();
	}
}
