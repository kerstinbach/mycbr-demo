package core;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.dfki.mycbr.core.DefaultCaseBase;
import de.dfki.mycbr.core.Project;
import de.dfki.mycbr.core.casebase.Attribute;
import de.dfki.mycbr.core.casebase.Instance;
import de.dfki.mycbr.core.model.AttributeDesc;
import de.dfki.mycbr.core.model.Concept;
import de.dfki.mycbr.core.model.SymbolDesc;
import de.dfki.mycbr.core.retrieval.Retrieval;
import de.dfki.mycbr.core.retrieval.Retrieval.RetrievalMethod;
import de.dfki.mycbr.core.similarity.AmalgamationFct;
import de.dfki.mycbr.core.similarity.Similarity;
import de.dfki.mycbr.core.util.Pair;

public class Test {

	public static CBREngine engine;
	public static Project rec;
	public static DefaultCaseBase cb;
	public static Concept myConcept;
	

	public static void main(String[] args) {
		
		engine = new CBREngine();	
		rec = engine.createProjectFromPRJ();
		// create case bases and assign the case bases that will be used for submitting a query 
		cb = (DefaultCaseBase)rec.getCaseBases().get(CBREngine.getCaseBase());
		// create a concept and get the main concept of the project; 
		myConcept = rec.getConceptByID(CBREngine.getConceptName());	

		// list cases
		System.out.println("cases = "+ cb.getCases().toString());	
		 
	   // remove cases
	//	cb.removeCase("Car #5");
		
		// list new cases
		System.out.println("cases = "+ cb.getCases().toString());					
		
		// save project
		rec.save();
		
		// just a retrieval
    	Retrieval r = new Retrieval(myConcept, cb);
		LinkedList<Double> results;

		try {
			Instance query = r.getQueryInstance();

			Instance caze = myConcept.getInstance("Car #3");

			for (Map.Entry<AttributeDesc, Attribute> e : caze.getAttributes()
					.entrySet()) {
				query.addAttribute(e.getKey(), e.getValue());
			}

		
		r.setRetrievalMethod(RetrievalMethod.RETRIEVE_SORTED);
		r.start();
		List<Pair<Instance, Similarity>> result = r.getResult(); 
		printResult(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static LinkedList<Double> printResult(List<Pair<Instance, Similarity>> result) {
		LinkedList<Double> sims = new LinkedList<Double>();
		for (Pair<Instance, Similarity> r : result) {
			System.out.println("\nSimilarity: " + r.getSecond().getValue()
					+ " to Instance: " + r.getFirst().getName());
			sims.add(r.getSecond().getValue());
		}
		return sims;
	}
}
