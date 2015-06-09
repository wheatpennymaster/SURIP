public class Nuclei
{
	String image;
	int number;
	double x;
	double y;

	public Nuclei(String[]input)
	{
		image = input[0];
		number = Integer.parseInt(input[1]);
		x = Double.parseDouble(input[3]);
		y = Double.parseDouble(input[4]);
	}
}
