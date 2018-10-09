import java.util.*;
import java.io.*;

public class ChuprinovAssignment1
{

	final static double CRAZY_PERCENTAGE = 15;

	public static void main(String[] args)
	{
		
		try{
			File input = new File("./Stockmarket-1990-2015.txt");
			Scanner scanIn = new Scanner(input);
		}
		catch(FileNotFoundException e)
		{
			System.out.println(e);
		}

	}

	public static ArrayList<String> getCrazies(Scanner sc)
	{
		ArrayList<String> result = new ArrayList<String>();
;
		while(sc.hasNext())
		{
			String candidate = sc.nextLine();
			String[] candArr = candidate.split("\t");
			double highPrice = Double.valueOf(candArr[3]);
			double lowPrice = Double.valueOf(candArr[4]);
			if((highPrice - lowPrice) / highPrice * 100 <= CRAZY_PERCENTAGE)
				result.add(candArr[0] + "\t" + candArr[1]);
		}
		return result;
	}

}
