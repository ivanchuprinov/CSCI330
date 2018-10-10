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
				System.out.println("Total crazy days = " + (crazies.get(i).size() - 1));
				if(crazies.get(i).size() - 1 > 1)
					System.out.println("The craziest day: " + getCraziest(crazies.get(i)));
				System.out.println();
			}



			scanIn.close();
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
	public static ArrayList<ArrayList<String>> getCrazies(Scanner sc)
	{
		ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
		HashMap<String, Integer> indexMap = new HashMap<String, Integer>();
		int companyIndex = 0;
		String candidate;
		String[] candArr;
		double highPrice;
		double lowPrice;
		double percentage;

		while(sc.hasNext())
		{
			candidate = sc.nextLine();
			candArr = candidate.split("\t");

			highPrice = Double.valueOf(candArr[3]);
			lowPrice = Double.valueOf(candArr[4]);
			percentage = (highPrice - lowPrice) / highPrice * 100;

			if(!indexMap.containsKey(candArr[0]))
			{
				indexMap.put(candArr[0], companyIndex);
				companyIndex++;
				result.add(new ArrayList<String>());
				result.get(indexMap.get(candArr[0])).add(candArr[0]);
			}
			if(percentage >= CRAZY_PERCENTAGE)
			{
				result.get(indexMap.get(candArr[0])).add(String.format("%s\t%.2f", candArr[1], percentage));
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

	public static ArrayList<ArrayList<String>> getSplits(Scanner sc)
	{

		ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
		if(sc.hasNext()){
			String newDay = sc.nextLine();
			String oldDay;
			String[] newArr;
			String[] oldArr;

			HashMap<String, Integer> indexMap = new HashMap<String, Integer>();
			int companyIndex = 0;

			while(sc.hasNext())
			{
				newDay = sc.nextLine();
				oldDay = newDay;
				newArr = newDay.split("\t");
				oldArr = oldDay.split("\t");
				double oldClose = Double.valueOf(oldArr[3]);
				double newOpen = Double.valueOf(candArr[4]);
				double percentage = (highPrice - lowPrice) / highPrice * 100;
			}

			sc.reset();
		}
		return result;
	}

	private static int splitType(double diff)
	{

	}

}
