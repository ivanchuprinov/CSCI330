import java.util.*;
import java.io.*;

public class ChuprinovAssignment1
{

	final static double CRAZY_PERCENTAGE = 15;
	final static double SPLIT_VARIATION = 0.05;
	final static String FILE_NAME = "./Stockmarket-1990-2015.txt";

	public static void main(String[] args)
	{
		
		try{
			ArrayList<ArrayList<String>> crazies = getCrazies(new Scanner(new File(FILE_NAME)));
			ArrayList<ArrayList<String>> splits = getSplits(new Scanner(new File(FILE_NAME)));

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
				for(int j = 0; j < splits.get(i).size(); j++)
				{
					System.out.println(splits.get(i).get(j));
				}
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

		while(sc.hasNextLine())
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

		sc.close();

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
		if(sc.hasNextLine()){
			int companyIndex = 0;
			double oldClose = 0;
			double newOpen = 0;
			double ratio = 0;
			String newDay = sc.nextLine();
			String oldDay = null;
			String[] newArr = null;
			String[] oldArr = null;
			String splType = null;

			HashMap<String, Integer> indexMap = new HashMap<String, Integer>();

			while(sc.hasNextLine())
			{				

				newDay = oldDay;
				oldDay = sc.nextLine();				
				oldArr = oldDay.split("\t");
				newArr = newDay.split("\t");

				if(oldArr[0].equals(newArr[0]))
				{
					oldClose = Double.valueOf(oldArr[5]);
					newOpen = Double.valueOf(newArr[2]);
					ratio = oldClose/newOpen;
					System.out.println(oldClose + " / " + newOpen + " = " + ratio);
					if(!indexMap.containsKey(oldArr[0]))
					{
						indexMap.put(oldArr[0], companyIndex);
						companyIndex++;
						result.add(new ArrayList<String>());
						result.get(indexMap.get(oldArr[0])).add(oldArr[0]);
					}
					splType = splitType(ratio);
					if(splType != null)
						result.get(indexMap.get(oldArr[0])).add(String.format(
							"%s split on %s\t%.2f --> %.2f", splType, oldArr[1], oldClose, newOpen));
				}
			}

			sc.close();
		}
		return result;
	}

	private static String splitType(double ratio)
	{
		if(Math.abs(ratio - 1.5) < SPLIT_VARIATION || ratio > 1.5)
		{
			if (Math.abs(ratio - 2) < SPLIT_VARIATION || ratio > 2)
			{
				if (Math.abs(ratio - 3) < SPLIT_VARIATION || ratio > 3)
					return "3:1";
				return "2:1";
			}
			return "3:2";
		}
		else
			return null;
	}

}
