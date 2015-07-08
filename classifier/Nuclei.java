public class Nuclei
{
	String image;
	int number;
	double x;
	double y;
	int index;

	public Nuclei(String[]input)
	{
		image = input[0];
		number = Integer.parseInt(input[1]);
		x = Double.parseDouble(input[3]);
		y = Double.parseDouble(input[4]);
	}

	public Nuclei(String image, int number, double x, double y, int index)
	{
		this.image=image;
		this.number=number;
		this.x=x;
		this.y=y;
		this.index=index;
	}
}
