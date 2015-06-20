import java.util.ArrayList;

public class Node
{
	Nuclei n;
	int index;

	Node ln;
	Node un;

	boolean visited = false;
	public Node(Nuclei n, int index)
	{
		this.n=n;
		this.index=index;

		ln = null;
		un = null;
	}

}
