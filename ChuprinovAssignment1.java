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

			ArrayList<ArrayList<String>> crazies = getCrazies(scanIn);

			for(int i = 0; i < crazies.size(); i++)
			{
				System.out.println("Processing " + crazies.get(i).get(0) + ":");
				System.out.println("======================================");
				for(int j = 1; j < crazies.get(i).size(); j++)
				{
					System.out.println("Crazy day: " + crazies.get(i).get(j));
				}
				System.out.println();
				System.out.println("The craziest day is: " + getCraziest(crazies.get(i)));
				System.out.println();
			}
		}
		catch(FileNotFoundException e)
		{
			System.out.println(e);
		}
		

		
	}

	/* Get Crazies
	 * Searches through the text file database and stores all crazy days and their percentages
	 * of companies into ArrayLists which are all stored in one big ArrayList. The first entry
	 * of a company's list is always the company's ticker symbol.
	 */

	/* TODO
		-Add companies even if they don't have crazy days by fiddling with the if statements
	*/

	public static ArrayList<ArrayList<String>> getCrazies(Scanner sc)
	{
		ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
		HashMap<String, Integer> indexMap = new HashMap<String, Integer>();
		int companyIndex = 0;

		while(sc.hasNext())
		{
			String candidate = sc.nextLine();
			String[] candArr = candidate.split("\t");

			double highPrice = Double.valueOf(candArr[3]);
			double lowPrice = Double.valueOf(candArr[4]);
			double percentage = (highPrice - lowPrice) / highPrice * 100;

			if(percentage >= CRAZY_PERCENTAGE)
			{
				if(indexMap.containsKey(candArr[0]))
				{
					result.get(indexMap.get(candArr[0])).add(String.format("%s\t%.2f", candArr[1], percentage));
				}
				else
				{
					indexMap.put(candArr[0], companyIndex);
					companyIndex++;
					result.add(new ArrayList<String>());
					result.get(indexMap.get(candArr[0])).add(candArr[0]);
					result.get(indexMap.get(candArr[0])).add(String.format("%s\t%.2f", candArr[1], percentage));
				}
			}
		}

		sc.reset();

		return result;
	}

	public static String getCraziest(ArrayList<String> crazyList)
	{
		int craziestIndex = 0;
		double craziestPerc = 0;

		for(int i = 1; i < crazyList.size(); i++)
		{
			String[] candArr = crazyList.get(i).split("\t");
			double percentage = Double.valueOf(candArr[1]);
			if(craziestPerc < percentage)
			{
				craziestPerc = percentage;
				craziestIndex = i;
			}
		}

		return crazyList.get(craziestIndex);
	}

}
