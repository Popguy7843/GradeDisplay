import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

/**
 * 
 * @author 19ngu
 * 
 */
public class Grade {
	private double[] grades;
	
	public Grade(String filename)
	{
		int count = 0;
		File f = new File(filename);
		try {
			Scanner sc = new Scanner(f);
			grades = new double[1];
			while(sc.hasNextDouble())
			{
				grades[0] = sc.nextDouble();
				count++;
			}
			if(count == 0)
			{
				throw new IllegalArgumentException("There must be at least one score");
			}
			grades = new double[count];
			sc = new Scanner(f);
			int i = 0;
			while(sc.hasNextDouble())
			{
				grades[i] = sc.nextDouble();
				i++;
			}
			Arrays.sort(grades);
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
	}
	
	public double[] getGrades()
	{
		return this.grades;
	}
	
	public double sum()
	{
		double sum = 0;
		for(double grade : this.grades)
		{
			sum += grade;
		}
		return sum;
	}
	
	public double mean()
	{
		return sum() / this.grades.length;
	}
	
	public double median()
	{
		if(this.grades.length % 2 == 0)
		{
			return (this.grades[this.grades.length/2] + this.grades[this.grades.length/2 - 1]) / 2;
		}
		else
		{
			return this.grades[this.grades.length/2];
		}		
	}
	
	public double[] mode()
	{
		double[] modes = new double[0];
		int highestCount = 0;
		for(int i = 0; i < this.grades.length - 1; i++)
		{
			if(findCount(this.grades[i]) > highestCount)
			{
				highestCount = findCount(this.grades[i]);
			}
		}
		int count = 0;
		for(double grade : this.grades)
		{
			if(findCount(grade) == highestCount)
			{
				if(modes.length < count+1)
				{
					modes = new double[count+1];
					if(this.grades[count] != this.grades[count+1])
					{
						count++;
					}
				}
			}
		}
		int count1 = 0;
		for(double grade : this.grades)
		{
			if(findCount(grade) == highestCount)
			{
				modes[count1] = grade;
				if(count1 < modes.length - 1)
				{
					count1++;
				}
			}
		}
		return modes;
	}
	
	public int findCount(double grade)
	{
		int count = 0;
		for(double n : this.grades)
		{
			if(grade == n)
			{
				count++;
			}
		}
		return count;
	}
	
	public double variance()
	{
		double variance = 0;
		for(double grade : grades)
		{
			variance += Math.pow(grade - this.mean(), 2);
		}
		variance /= this.grades.length;
		return variance;
	}
	
	public double standardDeviation()
	{
		return Math.sqrt(this.variance());
	}
	
	public int[] histogram(int sizeOfBucket, int minValue, int maxValue)
	{
		int interval = maxValue/sizeOfBucket;
		int[] buckets = new int[interval];
		for(int i = 0; i < buckets.length; i++)
		{
			for(double grade : this.grades)
			{
				if(grade > sizeOfBucket * i && grade <= sizeOfBucket * i + sizeOfBucket)
				{
					buckets[i]++;
				}
			}
		}
		return buckets;
	}
}