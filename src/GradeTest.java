/**
 * Test the Grade.java class
 * @author 19ngu
 * 
 */
public class GradeTest {

	private static Grade grade;
	public static void main(String[] args) {
		grade = new Grade("scores.txt");
		testMean();
		testMedian();
		testMode();
		testVariance();
		testStandardDeviation();
		testHistogram();
	}
	
	/**
	 * Print out the mean
	 */
	public static void testMean()
	{
		System.out.println("Mean: " + grade.mean());
	}
	
	/**
	 * Print out the median
	 */
	public static void testMedian()
	{
		System.out.println("Median: " + grade.median());
	}
	
	/**
	 * Print out the mode
	 */
	public static void testMode()
	{
		System.out.print("Mode: [");
		for(int i = 0; i < grade.mode().length; i++)
		{
			if(i == grade.mode().length - 1)
			{
				System.out.print(grade.mode()[i] + "]\n");
			}
			else
			{
				System.out.print(grade.mode()[i] + ", ");
			}
		}
	}
	
	/**
	 * Print out the variance
	 */
	public static void testVariance()
	{
		System.out.println("Variance: " + grade.variance());
	}
	
	/**
	 * Print out the standard deviation
	 */
	public static void testStandardDeviation()
	{
		System.out.println("Standard deviation: " + grade.standardDeviation());
	}

	/**
	 * Print out the values of the histogram
	 */
	public static void testHistogram()
	{
		System.out.print("Histogram: [");
		for(double d : grade.histogram(10, 1, 100))
		{
			if(grade.histogram(10, 1, 100)[grade.histogram(10, 1, 100).length-1] == d)
			{
				System.out.print(d + "]");
			}
			else
			{
				System.out.print(d + ", ");
			}
		}
	}
}
