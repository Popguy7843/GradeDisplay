import java.text.DecimalFormat;
import processing.core.PApplet;

/**
 * <h1>Interactions</h1>
 * <ul>
 * 	<li>q - Switch the alternating colors</li>
 * 	<li>w - Spin/Stop the pie chart</li>
 * 	<li>r - Reset the colors to alternating colors</li>
 *  <li>z - Switch dataset</li>
 * 	<li>Select a bucket by pressing a number</li>
 * 	<li>Right-click to drag/place the pie chart and statistics' x position</li>
 * </ul>
 * <h2>Note</h2>
 * 	<ul>
 * 	 <li>The pie chart randomly starts spinning after waiting for a bit</li>
 * 	</ul>
 * @author 19ngu
 *
 */
public class GradeDisplay extends PApplet{
	
	private int WIN_WIDTH = 1920;
	private int WIN_HEIGHT = 1080;
	private Grade grade;
	private int barY = WIN_HEIGHT - 70;
	private int barX = 10;
	private int barWidth = WIN_WIDTH/10;
	private int incrementX = barWidth;
	private int textSpace = 25;
	private int selected = 0;
	private int q = 0;
	private String filename = "scores.txt";
	private DecimalFormat df = new DecimalFormat("#.00");
	private float pieChartX = WIN_WIDTH/8;
	private float pieChartY = WIN_HEIGHT/4.5f;
	private float pieChartScaleX = WIN_WIDTH/4.5f;
	private float pieChartScaleY = WIN_WIDTH/4.5f;
	private float statsX = pieChartX + pieChartScaleX/2 + 50;
	private float statsY = pieChartY - pieChartScaleY/4;
	private float lastAngle = 0;
	private float textSize = WIN_HEIGHT/50;
	private boolean spinning = false;
	private boolean dragging = false;
	
	public static void main(String[] args) {
		PApplet.main("GradeDisplay");
	}
	
	public void settings()
	{
		size(WIN_WIDTH, WIN_HEIGHT);
	}
	
	public void setup()
	{
		background(50, 100, 200);
		fill(45);
		rect(0, WIN_HEIGHT-70, WIN_WIDTH, WIN_HEIGHT);
		grade = new Grade(filename);
		noStroke();
		this.textSize(textSize);
		surface.setTitle("Eric Nguyen - GradeDisplay");
	}
	
	public void draw()
	{
		setup();
		drawHistogram();
		statisticsText(statsX, statsY);
		// Drag the pie chart and statistics
		if(dragging)
		{
			pieChartX = mouseX;
			statsX = pieChartX + pieChartScaleX/2 + 50;
		}
	}
	
	/**
	 * Draw the histogram
	 */
	public void drawHistogram()
	{
		for(int i = 0; i < grade.histogram(10, 1, 100).length; i++)
		{
			selectColor(i);
			bars(i);
			buckets(i);
			pieChart(i);
			barAmount(i);
		}
	}
	
	/**
	 * Draw the bars
	 * @param i index
	 */
	public void bars(int i)
	{
		rect(barX + i * incrementX, barY, barWidth, barHeight(i));
	}
	
	/**
	 * Draw the bucket range under the bar
	 * @param i index
	 */
	public void buckets(int i)
	{
		text((i*10+1) + "-" + (i*10+10), barX + i * incrementX, barY + textSpace);
	}
	
	/**
	 * Get bar i's height
	 * @param i index
	 * @return bar's height
	 */
	public float barHeight(int i)
	{
		float barHeight = grade.histogram(10, 1, 100)[i] * -10;
		return barHeight;
	}
	
	/**
	 * Draw bar i's amount above bar i
	 * @param i index
	 */
	public void barAmount(int i)
	{
		String barAmount = str(grade.histogram(10, 1, 100)[i]);
		this.text(barAmount, barX + i * incrementX + barWidth/2 - barAmount.length(), barY + barHeight(i) - 5);
	}
	
	/**
	 * Draw pie chart of the data
	 * @param i index
	 */
	public void pieChart(int i)
	{
		float angle = radians(grade.histogram(10, 1, 100)[i]/total()*360);
		arc(pieChartX , pieChartY, pieChartScaleX, pieChartScaleY, lastAngle, lastAngle+angle);
		lastAngle += angle;
		// Donut effect [Draw a circle]
		ellipse(pieChartX, pieChartY, pieChartScaleX/2, pieChartScaleY/2);
	}
	
	/**
	 * @return Total number of tests
	 */
	public float total()
	{
		float total = 0;
		for(double n : grade.histogram(10, 1, 100))
		{
			total += n;
		}
		return total;
	}
	
	/**
	 * @return Total sum of tests
	 */
	public float sum()
	{
		float sum = 0;
		for(double n : grade.getGrades())
		{
			sum += n;
		}
		return sum;
	}
	
	/**
	 * Changes the color
	 * @param i
	 */
	public void selectColor(int i)
	{
		if(i % 2 == q && selected == 0)
		{
			// Unselected
			fill((float) ((i+4)*5), (float) ((i+4)*15), (float) ((i+4)*25));
		}
		else if(selected != 0)
		{
			// Select bucket
			if(i == selected-1)
			{
				fill((float) ((i+4)*25), (float) ((i+4)*15), (float) ((i+4)*5));
			}
			else
			{
				fill((float) ((i+4)*5), (float) ((i+4)*15), (float) ((i+4)*25));
			}
		}
		else
		{
			// Unselected
			fill((float) ((i+4)*25), (float) ((i+4)*15), (float) ((i+4)*5));
		}
	}
	
	/**
	 * Draw the statistics in text
	 * @param statsX
	 * @param statsY
	 */
	public void statisticsText(float statsX, float statsY)
	{
		fill(250, 170, 100);
		fileName(statsX, statsY - WIN_HEIGHT/20);
		mean(statsX, statsY);
		median(statsX, statsY + WIN_HEIGHT/20);
		mode(statsX, statsY + WIN_HEIGHT/20*2);
		variance(statsX, statsY + WIN_HEIGHT/20*3);
		standardDeviation(statsX, statsY + WIN_HEIGHT/20*4);
		histogram(statsX, statsY + WIN_HEIGHT/20*5);
		numberOfTests(statsX, statsY + WIN_HEIGHT/20*6);
		sumOfTests(statsX, statsY + WIN_HEIGHT/20*7);
	}
	
	/**
	 * Draw the file's name
	 * @param x position
	 * @param y position
	 */
	public void fileName(float x, float y)
	{
		text("Filename = " + filename, x, y);
	}
	
	/**
	 * Draw mean
	 * @param x position
	 * @param y position
	 */
	public void mean(float x, float y)
	{
		text("Mean = " + df.format(grade.mean()), x, y);
	}
	
	/**
	 * Draw median
	 * @param x position
	 * @param y position
	 */
	public void median(float x, float y)
	{
		text("Median = " + grade.median(), x, y);
	}
	
	/**
	 * Draw modes
	 * @param x position
	 * @param y position
	 */
	public void mode(float x, float y)
	{
		if(grade.mode().length == 1)
		{
			text("Modes = " + grade.mode(), x, y);
		}
		else
		{
			String mode = "Modes = ["; 
			text(mode, x, y);
			for(int k = 0; k < grade.mode().length; k++)
			{
				if(k == grade.mode().length - 1)
				{
					text(grade.mode()[k] + " ]", x + k * WIN_WIDTH/50 * 1.5f + mode.length() * 12, y); // Last value
				}
				else
				{
					text(grade.mode()[k] + ",", x + k * WIN_WIDTH/50 * 1.5f + mode.length() * 12, y);
				}
			}
		}
	}
	
	/**
	 * Draw variance
	 * @param x position
	 * @param y position
	 */
	public void variance(float x, float y)
	{
		text("Variance = " + df.format(grade.variance()), x, y);
	}
	
	/**
	 * Draw standard deviation
	 * @param x position
	 * @param y position
	 */
	public void standardDeviation(float x, float y)
	{
		text("Standard Deviation = " + df.format(grade.standardDeviation()), x, y);
	}
	
	/**
	 * Draw histogram values
	 * @param x position
	 * @param y position
	 */
	public void histogram(float x, float y)
	{
		if(grade.histogram(10, 1, 100).length == 1)
		{
			text("Histogram = " + grade.histogram(10, 1, 100), x, y + 250);
		}
		else
		{
			String histogram = "Histogram = ["; 
			text(histogram, x, y);
			for(int i = 0; i < grade.histogram(10, 1, 100).length; i++)
			{
				if(i == grade.histogram(10, 1, 100).length-1)
				{
					text(str(grade.histogram(10, 1, 100)[i]) + " ]", x + i * WIN_WIDTH/50 * 1.25f + textSize/2 + histogram.length() * textSize/2, y); // Last value
				}
				else
				{
					text(str(grade.histogram(10, 1, 100)[i]) + ",", x + i * WIN_WIDTH/50 * 1.25f + textSize/2 + histogram.length() * textSize/2, y);
				}
			}
		}
	}
	
	/**
	 * Draw total number of tests
	 * @param x position
	 * @param y position
	 */
	public void numberOfTests(float x, float y)
	{
		text("Number of Tests = " + total(), x, y);
	}
	
	/**
	 * Draw total sum of test grades
	 * @param x position
	 * @param y position
	 */
	public void sumOfTests(float x, float y)
	{
		text("Sum of Grades = " + sum(), x, y);
	}
	
	public void keyPressed()
	{
		// Switch alternating colors
		if(key == 'q')
		{
			if(q == 0)
			{
				q = 1;
			}
			else
			{
				q = 0;
			}
		}
		// Select bucket
		if(key == 49)
		{
			selected = 1;
		}
		if(key == 50)
		{
			selected = 2;
		}
		if(key == 51)
		{
			selected = 3;
		}
		if(key == 52)
		{
			selected = 4;
		}
		if(key == 53)
		{
			selected = 5;
		}
		if(key == 54)
		{
			selected = 6;
		}
		if(key == 55)
		{
			selected = 7;
		}
		if(key == 56)
		{
			selected = 8;
		}
		if(key == 57)
		{
			selected = 9;
		}
		if(key == 48)
		{
			selected = 10;
		}
		// Deselect buckets
		if(key == 'r')
		{
			selected = 0;
		}
		// Spin pie chart
		if(key == 'w' && !spinning)
		{
			lastAngle = 1000000;
			spinning = true;
		}
		else if(key == 'w' && spinning)
		{
			lastAngle = 0;
			spinning = false;
		}
		// Switch dataset [scores.txt and scores2.txt]
		if(key == 'z' && filename == "scores.txt")
		{
			filename = "scores2.txt";
		} 
		else if(key == 'z' && filename == "scores2.txt")
		{
			filename = "scores.txt";
		}
	}
	
	public void mouseClicked() {
		// Detect if dragging
		if(mouseButton == RIGHT)
		{
			if(!dragging)
			{
				dragging = true;
			}
			else if(dragging)
			{
				dragging = false;
			}
		}
	}
}
