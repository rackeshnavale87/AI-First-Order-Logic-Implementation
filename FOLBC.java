import java.util.*;
import java.io.*;

/**
 * @author Rakesh Sharad Navale.
 * 
 * @class FOLBC
 * Given the knowledge base and the query in a text file called "input.txt".
 * The first line of the input file contains the query. The second line contains an integer n specifying
 * the number of clauses in the knowledge base. The remaining lines contain the clauses in the
 * knowledge base, one per line. Each clause is written in one of the following forms:
 * 1) as an implication of the form 
 * 				(p1 & p2 & ... & pn => q)
 * whose premise is a conjunction of
 * atomic sentences and whose conclusion is a single atomic sentence.
 * 2) as a fact with a single atomic sentence: q
 * Each atomic sentence is a predicate applied to a certain number of arguments. Note that
 * negation is not used in this homework.

 * Output:
 * If the query sentence can be inferred from the knowledge base, your output should be "TRUE",
 * otherwise, "FALSE". Answer (TRUE/FALSE) will be made available in the file "output.txt". 
 * 
 * Note:
 * 1. & denotes the AND operator.
 * 2. => denotes the implication operator.
 * 3. No other operators besides & and => are used.
 * 4. Variables are denoted by a single lower case letter (For this homework, you can assume
 * that only variable x will be used. No other variables are used.)
 * 5. All predicates (such as HasFever) and constants (such as John) begin with uppercase
 * letters.
 * 6. You can assume that all predicates have at least one and at most two arguments.
 * 7. You can assume that there will be no more than 10 clauses in the knowledge base.
 *  
 *  
*/

class FOLBC
{
	public static int n; 					// Get number of clauses from Notepad
 	static String CheckAns = "";
	public static String ask; 
	public static String query; 				//Get query to search for FOL from Notepad 
	public static String tell="";				//Get KB from Notepad
	static String delimiter1 = "\\(";
	static String delimiter2 = ",";
	static String delimiter3 = "\\)";
	static String delimiter4 = "&";
	static String delimiter5 = "=>";
	public static ArrayList<String> Tasks;
	public static ArrayList<String> facts;
	public static ArrayList<String> clauses;
	public static ArrayList<String> pchild;
	public static ArrayList<String> Checked;
	public static ArrayList<String> fpred;
	public static ArrayList<String> cpred;	
	public static ArrayList<ArrayList<String>> fparam;	// Fact parameters
	public static ArrayList<ArrayList<String>> cparam;	// Clause parameter
	public static ArrayList<ArrayList<String>> pparam;		

	public static void main(String[] args) throws IOException 
	{	
		FileInputStream fis = null;
		BufferedReader reader = null; 		
		
		Tasks  = new ArrayList<String>();
	 	clauses  = new ArrayList<String>();
	 	pchild = new ArrayList<String>();
	 	Checked  = new ArrayList<String>();
	 	facts  = new ArrayList<String>();
	 	fpred  = new ArrayList<String>();
	 	cpred  = new ArrayList<String>();
	 	cparam = new ArrayList<ArrayList<String>>();
	 	pparam = new ArrayList<ArrayList<String>>();
	 	fparam = new ArrayList<ArrayList<String>>();
	
	    try 
	    {
	    	fis = new FileInputStream("input.txt");
			reader = new BufferedReader(new InputStreamReader(fis));
			
			query = reader.readLine();
	        n =Integer.valueOf(reader.readLine());
			tell = reader.readLine(); //get all the KB clauses
			tell = tell.concat(";");
			for (int i = 0; i < n-1; i++) {
				tell = tell.concat(reader.readLine());
				tell = tell.concat(";");
			}

			 String[] sentences = tell.split(";");
			 	for (int j=0;j<sentences.length;j++){
			 		if (!sentences[j].contains("=>")) 
			 			// add the facts
			 			facts.add(sentences[j]);
			 		else
			 			// add the premises	
			 			clauses.add(sentences[j]);
			 	}
//			 	System.out.println("Facts:-"+ facts);
//			 	System.out.println("Clauses:-"+ clauses);
//			 	System.out.println();
			 	
			 	
			 	for(int nextsentence=0;nextsentence<facts.size();nextsentence++)
			 	{
			 		ArrayList<String> temp4;
			 		temp4  = new ArrayList<String>();
			 		String temp2 = facts.get(nextsentence);
			 			 		
			 		temp2.replaceAll(delimiter1, delimiter2);
			 		temp2.replaceAll(delimiter3, delimiter2);
			 		String temp3[] = temp2.split("[\\(,)]");
			 		
			 		for(int e=0; e<temp3.length;e++)
			 		{
			 			if(!((temp3[e]).isEmpty()))
			 			temp4.add(temp3[e]);
			 			else
			 				break;
			 		}
			 		fparam.add(temp4);
			 	}
			ask = query;
			Tasks.add(ask);
			run();
		}
	    finally 
	    {
	    	try 
	    	{
	    		reader.close();
	    		fis.close();
   			}
	    	catch (IOException e) 
	    	{
	    		e.printStackTrace();
	    		return;
	    	} 
	    }
	}

//Method which calls the main BackChainInfer() method and returns output whether the query can be infer or not
	public static String run() throws IOException
	{
	 	if (BackChainInfer()) {
	 	// BackChainInfer method returned true so it can ans. query correctly with KB
	 		CheckAns = "Proved : ";
	 		for (int i=Checked.size()-1;i>=0;i--)
	 		{
	 				if (i==0)
	 					CheckAns += Checked.get(i);
	 				else	
	 					CheckAns += Checked.get(i)+", ";
	 		}
	 	}
	 	else {
	 		// BackChainInfer method returned False so it can ans. query correctly with KB
	 		CheckAns = "NOT Proved";
	 	}
	 	return CheckAns;		
	 }
	 
 //Implementation of FOL backward chaining algorithm
	public static boolean BackChainInfer() throws IOException
	{
	    BufferedWriter writer = null;
		writer = new BufferedWriter(new FileWriter("output.txt"));
	
	  	while(!Tasks.isEmpty())
	  	{
	 		String q = Tasks.remove(Tasks.size()-1);
	 		Checked.add(q);	 		 		
	 		if(!factFound(q))  // if this element is a fact then we dont need to go further
	 		{ 		
		 		ArrayList<String> p = new ArrayList<String>();
		 		for(int i=0;i<clauses.size();i++) // for each clause.... that contains the symbol as its conclusion
		 		{
		 			if (checkClause(clauses.get(i),q))
		 			{
		 					ArrayList<String> temp = getPremises(clauses.get(i));
		 					for(int j=0;j<temp.size();j++)
		 					{
		 						p.add(temp.get(j));
		 					}
		 			}						
		 		}
			 	// no symbols were generated and since it isnt a fact either then this sybmol and eventually ASK  cannot be implied by TELL
		 		if (p.size()==0)
		 		{
		 			try
		 			{
		 				writer.write("FALSE");
		 				writer.close();
		 			}
		 			catch (IOException ex) 
		 		    {} 		 			
		 			return false;
		 		}
		 		else
		 		{
		 			// there are symbols so check for previously processed ones and add to agenda
		 			for(int i=0;i<p.size();i++)
		 			{
		 				if (!Checked.contains(p.get(i)))
		 					Tasks.add(p.get(i));
		 			}
		 		}
	 		} 		
	  	}//while end
		try
		{
			//System.out.println("TRUE");
			writer.write("TRUE");
			writer.close();
		}
		catch (IOException ex) 
	    {} 
		return true;
	}
	 
	private static boolean factFound(String q) 
	{
		// TODO Auto-generated method stub	 
		String ptemp2 = q;
		ptemp2.replaceAll(delimiter1, delimiter2);
		ptemp2.replaceAll(delimiter3, delimiter2);
		ptemp2.replaceAll(delimiter4, delimiter2);
		ptemp2.replaceAll(delimiter5, delimiter2);
		String ptemp3[] = ptemp2.split("[\\(,)]");
		int yes =0;
		
		if(facts.contains(q)) // direct fact.. return true
			return true;
		
			for(int a =0; a<fparam.size();a++) {
				ArrayList<String> comparelist = fparam.get(a);
				if(comparelist.contains(ptemp3[0]))
				{yes =0;
					for(int e=0; e<ptemp3.length;e++) { 
						if(comparelist.get(e).equals(ptemp3[e]) || ptemp3[e].equals("x"))
						{
							yes++;
							if(ptemp3.length == yes)
								return true;

						}							
						else
							continue;
					}
				}
			}
			return false;
	}
	
//Methid that returns the conjuncts contained in a clause
	public static ArrayList<String> getPremises(String clause) {
		 //ArrayList<String> temp4 = new ArrayList<String>();	
		String premise = clause.split("=>")[0];
//			String conclusion = clause.split("=>")[1];
// 			get premise parameter and put it into conjucts
//			String ctemp2 = conclusion;
//			ctemp2.replaceAll(delimiter1, delimiter2);
//			ctemp2.replaceAll(delimiter3, delimiter2);
//			ctemp2.replaceAll(delimiter4, delimiter2);
//			ctemp2.replaceAll(delimiter5, delimiter2);
//			String ctemp3[] = ctemp2.split("[\\(,)]");
		
		ArrayList<String> temp = new ArrayList<String>();
		String[] conjuncts = premise.split("&");
		for(int i=0;i<conjuncts.length;i++)
		{
			if (!Tasks.contains(conjuncts[i]))
					temp.add(conjuncts[i]);
		}
		return temp;//return predicate
	}
	
	 
	 // method which checks if c appears in the conclusion of a given clause	
	 // input : clause, string to check in conclusion -> c
	 // output : true if c is in the conclusion of any clause
	
	public static boolean checkClause(String clause, String c)
	{
		// TODO Auto-generated method stub	 
		int yes =0;
		String conclusion = clause.split("=>")[1];
	
		String ptemp2 = conclusion;
		ptemp2.replaceAll(delimiter1, delimiter2);
		ptemp2.replaceAll(delimiter3, delimiter2);
		ptemp2.replaceAll(delimiter4, delimiter2);
		ptemp2.replaceAll(delimiter5, delimiter2);
		String ptemp3[] = ptemp2.split("[\\(,)]");
		
		String ptemp22 = c;
		ptemp22.replaceAll(delimiter1, delimiter2);
		ptemp22.replaceAll(delimiter3, delimiter2);
		ptemp22.replaceAll(delimiter4, delimiter2);
		ptemp22.replaceAll(delimiter5, delimiter2);
		String ptemp33[] = ptemp22.split("[\\(,)]");
	
		if(ptemp3[0].equals((ptemp33[0]))) {
				for(int e=0; e<ptemp3.length;e++) {
					if(ptemp3[e].equals(ptemp33[e]) || ptemp33[e].equals("x") || ptemp3[e].equals("x"))
						yes++;
					else
						continue;
				}
			}
		if(ptemp3.length == yes)
			return true;
		return false;
		}
}
