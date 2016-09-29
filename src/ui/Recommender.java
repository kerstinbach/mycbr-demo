package ui;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import core.CBREngine;
import de.dfki.mycbr.core.DefaultCaseBase;
import de.dfki.mycbr.core.Project;
import de.dfki.mycbr.core.casebase.Instance;
import de.dfki.mycbr.core.model.AttributeDesc;
import de.dfki.mycbr.core.model.Concept;
import de.dfki.mycbr.core.model.FloatDesc;
import de.dfki.mycbr.core.model.SymbolDesc;
import de.dfki.mycbr.core.retrieval.Retrieval;
import de.dfki.mycbr.core.retrieval.Retrieval.RetrievalMethod;
import de.dfki.mycbr.core.similarity.AmalgamationFct;
import de.dfki.mycbr.core.similarity.Similarity;
import de.dfki.mycbr.core.util.Pair;

public class Recommender {

	public CBREngine engine;
	public Project rec;
	public DefaultCaseBase cb;
	public Concept myConcept;

	public void loadengine () {

		engine = new CBREngine();	
		rec = engine.createProjectFromPRJ();
		// create case bases and assign the case bases that will be used for submitting a query 
		cb = (DefaultCaseBase)rec.getCaseBases().get(engine.getCaseBase());
		// create a concept and get the main concept of the project; 
		myConcept = rec.getConceptByID(engine.getConceptName());	
	}

	public String solveOuery(String color, Float price, Float mileage, Integer numberofcases) {

		String answer="";		
		// create a new retrieval
		Retrieval ret = new Retrieval(myConcept, cb);
		// specify the retrieval method
		ret.setRetrievalMethod(RetrievalMethod.RETRIEVE_SORTED);		
		// create a query instance
		Instance query = ret.getQueryInstance();		
		// Insert values into the query: Symbolic Description
		SymbolDesc colorDesc = (SymbolDesc) myConcept.getAllAttributeDescs().get("Color");
		query.addAttribute(colorDesc,colorDesc.getAttribute(color));

		// Insert values into the query: Float Description
		FloatDesc priceDesc = (FloatDesc) myConcept.getAllAttributeDescs().get("Price");
		try {
			query.addAttribute(priceDesc,priceDesc.getAttribute(price));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Insert values into the query: Float Description
		FloatDesc mileageDesc = (FloatDesc) myConcept.getAllAttributeDescs().get("Mileage");
		try {
			query.addAttribute(mileageDesc,mileageDesc.getAttribute(mileage));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// perform retrieval
		ret.start();	
		// get the retrieval result
		List <Pair<Instance, Similarity>> result = ret.getResult(); 
		// get the case name	
		if(result.size()>0){

			// get the best case's name
			String casename = result.get(0).getFirst().getName();	
			// get the similarity value
			Double sim = result.get(0).getSecond().getValue();		
			answer = "I found "+casename+" with a similarity of "+sim+" as the best match.";
			answer = answer+"The "+numberofcases+" best cases shown in a table: <br /> <br /> <table border=\"1\">";	
			ArrayList<Hashtable<String, String>> liste = new ArrayList<Hashtable<String, String>>();	
			// if more case results are requested than we have in our case base at all:
			if(numberofcases>=cb.getCases().size()){numberofcases = cb.getCases().size();}

			for(int i = 0; i<numberofcases; i++){

				liste.add(getAttributes(result.get(i), rec.getConceptByID(engine.getConceptName())));
				System.out.println("liste "+liste.get(i).toString());
				answer=answer+"<tr><td>"+result.get(i).getFirst().getName()+"</td><td>"+liste.get(i).toString()+"</td></tr>";
			}

			answer= answer+"</table>";		
		}	
		else{System.out.println("Retrieval Result is empty");}

		return answer;
	}
	/**
	 * This method delivers a Hashtable which contains the Attributs names (Attributes of the case) combined with their respective values.
	 * @author weber,koehler,namuth
	 * @param r = An Instance.
	 * @param concept = A Concept
	 * @return List = List containing the Attributes of a case with their values.
	 */
	public static Hashtable<String, String> getAttributes(Pair<Instance, Similarity> r, Concept concept) {

		Hashtable<String, String> table = new Hashtable<String, String>();
		ArrayList<String> cats = getCategories(r);
		// Add the similarity of the case
		table.put("Sim", String.valueOf(r.getSecond().getValue()));
		for (String cat : cats) {
			// Add the Attribute name and its value into the Hashtable
			table.put(cat, r.getFirst().getAttForDesc(concept.getAllAttributeDescs().get(cat)).getValueAsString());
		}
		return table;
	}
	/**
	 * This Method generates an ArrayList, which contains all Categories of aa Concept.
	 * @author weber,koehler,namuth
	 * @param r  =  An Instance.
	 * @return List = List containing the Attributes names.
	 */
	public static ArrayList<String> getCategories(Pair<Instance, Similarity> r) {

		ArrayList<String> cats = new ArrayList<String>();

		// Read all Attributes of a Concept
		Set<AttributeDesc> catlist = r.getFirst().getAttributes().keySet();

		for (AttributeDesc cat : catlist) {
			if (cat != null) {
				// Add the String literals for each Attribute into the ArrayList
				cats.add(cat.getName());
			}
		}
		return cats;
	}

	public String displayAmalgamationFunctions() {

		ArrayList <String> amalgam = new ArrayList<String>(); 
		String listoffunctions="Currently available Amalgamationfunctions: <br /> <br />";		 
		AmalgamationFct current = myConcept.getActiveAmalgamFct();		 
		System.out.println("Amalgamation Function is used = "+current.getName());		 
		List<AmalgamationFct> liste = myConcept.getAvailableAmalgamFcts();

		for (int i = 0; i < liste.size(); i++){
			System.out.println(liste.get(i).getName());
			listoffunctions=listoffunctions+liste.get(i).getName()+"<br />";			 
		}

		listoffunctions=listoffunctions+(" <br /> <br /> Currently selected Amalgamationfunction: "+current.getName()+"\n");
		listoffunctions=listoffunctions+(" <br /> <br /> Please type the name of the Amalgamationfunction to use in the " +
				" Field \"Amalgamationfunction\" it will be automatically used during the next retrieval");
		System.out.println(listoffunctions);	 
		return listoffunctions;		 
	}	 
}