public class time
{
	public static void main(String[]args)
	{
		System.out.println("Start");
		long m = System.currentTimeMillis();
		for(int x=0;x<100;x++)
		{
			for(int x1=0;x1<100;x1++)
			{
				for(int x2=0;x2<100;x2++)
				{
					for(int x3=0;x3<100;x3++)
					{
						for(int x4=0;x4<100;x4++)
						{
							for(int x5=0;x5<100;x5++)
							{
								
							}
						}
					}
				}
			}
		}
		long m1 = System.currentTimeMillis();
		System.out.println("Done");
		System.out.println((m1-m)/1000);
	}
}
