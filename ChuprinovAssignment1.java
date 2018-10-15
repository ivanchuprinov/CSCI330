import java.util.*;
import java.io.*;

/* TODO:
 * Check where output needs to go
 */

public class ChuprinovAssignment1
{

	/* Constants */
	final static double CRAZY_PERCENTAGE = 15;
	final static double SPLIT_VARIATION = 0.05;
	final static String INPUT_FILE_NAME = "./Stockmarket-1990-2015.txt";
	//final static String OUTPUT_FILE_NAME = "./";

	public static void main(String[] args)
	{
		/* Required to catch IOException */
		try{
			// Check the validity of the given file
			if(!validInput(INPUT_FILE_NAME))
			{
				System.out.println("Error: Invalid input.");
				System.exit(0);
			}

			// Run functions to read and interpret data from the text file
			ArrayList<ArrayList<String>> crazies = getCrazies(new Scanner(new File(INPUT_FILE_NAME)));
			ArrayList<ArrayList<String>> splits = getSplits(new Scanner(new File(INPUT_FILE_NAME)));
			//PrintWriter writer = new PrintWriter(new File(OUTPUT_FILE_NAME));

			// Print out the data
			for(int i = 0; i < crazies.size(); i++)
			{
				System.out.println("Processing " + crazies.get(i).get(0) + ":");
				System.out.println("======================================");

				// Print crazy days
				for(int j = 1; j < crazies.get(i).size(); j++)
				{
					System.out.println("Crazy day: " + crazies.get(i).get(j));
				}
				System.out.println("Total crazy days = " + (crazies.get(i).size() - 1));

				// Only print out the craziest day if there are more than 1 crazy days
				if(crazies.get(i).size() - 1 > 1)
					System.out.println("The craziest day: " + getCraziest(crazies.get(i)));
				System.out.println();

				// Print splits
				for(int j = 1; j < splits.get(i).size(); j++)
				{
					System.out.println(splits.get(i).get(j));
				}
				System.out.println("Total number of splits: " + (splits.get(i).size() - 1) + "\n");
			}

		}
		catch(IOException e)
		{
			System.out.println(e);
		}
		

		
	}

	/* Get Crazies
	 * Searches through the text file database and stores all crazy days and their percentages
	 * of companies into ArrayLists which are all stored in one big ArrayList. The first entry
	 * of a company's list is always the company's ticker symbol. The HashMap is used to keep
	 * track of company indices. Nested ArrayList is used to make sure that entries of
	 * different companies are separated.
	 */
	private static ArrayList<ArrayList<String>> getCrazies(Scanner sc)
	{
		/* Local variables */
		ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
		HashMap<String, Integer> indexMap = new HashMap<String, Integer>();
		int companyIndex = 0;
		String candidate = null;
		String[] candArr = null;
		double highPrice = 0;
		double lowPrice = 0;
		double percentage = 0;

		/* Input file traversal loop */
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

	/* Get craziest
	 * Returns the craziest day entry.
	 */
	private static String getCraziest(ArrayList<String> crazyList)
	{
		/* Local variables */
		int craziestIndex = 0;
		double craziestPerc = 0;
		double percentage = 0;

		/* Finding entry with greatest percentage */
		for(int i = 1; i < crazyList.size(); i++)
		{
			String[] candArr = crazyList.get(i).split("\t");
			percentage = Double.valueOf(candArr[1]);
			if(craziestPerc < percentage)
			{
				craziestPerc = percentage;
				craziestIndex = i;
			}
		}

		return crazyList.get(craziestIndex);
	}

	/* Get Splits
	 * Searches through the text file database and stores all days, on which splits have occurred,
	 * for each company. Creates an ArrayList of Strings with that data represented nicely for
	 * each of the companies all grouped in one ArrayList. The first entry of a company's list is 
	 * always the company's ticker symbol. The HashMap is used to keep track of company indices. 
	 * Nested ArrayList is used to make sure that entries of different companies are separated.
	 *
	 * NOTE: REQUIRES THE ENTRIES TO BE SORTED BY COMPANY, MOST RECENT TO OLDEST TOP TO BOTTOM
	 */
	private static ArrayList<ArrayList<String>> getSplits(Scanner sc)
	{
		/* Local variables */
		ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
		HashMap<String, Integer> indexMap = new HashMap<String, Integer>();
		int companyIndex = 0;
		double oldClose = 0;
		double newOpen = 0;
		double ratio = 0;
		String laterDay = null;
		String priorDay = sc.nextLine();
		String[] laterArr = null;
		String[] priorArr = null;
		String splType = null;

		/* Input file traversal loop */
		while(sc.hasNextLine())
		{
			laterDay = priorDay;
			priorDay = sc.nextLine();				
			priorArr = priorDay.split("\t");
			laterArr = laterDay.split("\t");

			if(priorArr[0].equals(laterArr[0]))
			{
				oldClose = Double.valueOf(priorArr[5]);
				newOpen = Double.valueOf(laterArr[2]);
				ratio = oldClose/newOpen;
				if(!indexMap.containsKey(priorArr[0]))
				{
					indexMap.put(priorArr[0], companyIndex);
					companyIndex++;
					result.add(new ArrayList<String>());
					result.get(indexMap.get(priorArr[0])).add(priorArr[0]);
				}
				// Get split type and build the string (if necessary)
				splType = splitType(ratio);
				if(splType != null)
					result.get(indexMap.get(priorArr[0])).add(String.format(
						"%s split on %s\t%.2f --> %.2f", splType, priorArr[1], oldClose, newOpen));
			}
		}

		sc.close();

		return result;
	}

	/* Split type
	 * Returns one of three most common types of splits represented by the given ratio.
	 * Returns null String if the ratio is not a significant split.
	 * TODO:
	 * Specify the criteria for the variation
	 */
	private static String splitType(double ratio)
	{
		String result = null;
		if(Math.abs(ratio - 1.5) < SPLIT_VARIATION )//|| ratio > 1.5)
		{
			if (Math.abs(ratio - 2) < SPLIT_VARIATION )//|| ratio > 2)
			{
				if (Math.abs(ratio - 3) < SPLIT_VARIATION )//|| ratio > 3)
					result = "3:1";
				result = "2:1";
			}
			result = "3:2";
		}
		return result;
	}


	/* Valid Input
	 * Takes file name as an input and checks if the file with that name follows the rules:
	 * - Has to exist
	 * - Has to have at least 2 lines
	 *
	 * Idea: expand this method to check for the correct format of the file.
	 */
	private static boolean validInput(String fileName)
	{
		boolean valid = false;
		/* Required to catch IOException */
		try
		{
			Scanner testScan = new Scanner(new File(fileName));
			
			if(testScan.hasNextLine())
			{
				testScan.nextLine();
				if(testScan.hasNextLine())
				{
					if(testScan.nextLine().length() > 0)
						valid = true;
				}
			}
			testScan.close();
				
		}
		catch(IOException e)
		{
			System.out.println(e);
		}
		return valid;
	}

}
