package core;

import de.dfki.mycbr.core.Project;
import de.dfki.mycbr.core.io.CSVImporter;
import de.dfki.mycbr.core.model.Concept;

public class CBREngine {

	// set path to myCBR projects	
	private static String data_path = System.getProperty("user.dir") + "/project/";
	/* project specific: NewExampleProject*/
	// name of the project file
	private static String projectName = "NewExampleProject.prj";
	// name of the central concept 
	private static String conceptName = "Car";
	// name of the csv containing the instances
	private static String csv = "cars_casebase.csv";
	// set the separators that are used in the csv file
	private static String columnseparator = ";";
	private static String multiplevalueseparator = ",";
	// name of the case base that should be used; the default name in myCBR is CB_csvImport
	private static String casebase = "CarsCB";
	// Getter for the Project meta data
	public static String getCaseBase() {
		return casebase;
	}

	public static void setCasebase(String casebase) {
		CBREngine.casebase = casebase;
	}

	public static String getProjectName() {
		return projectName;
	}	

	public static void setProjectName(String projectName) {
		CBREngine.projectName = projectName;
	}

	public static String getConceptName() {
		return conceptName;
	}

	public static void setConceptName(String conceptName) {
		CBREngine.conceptName = conceptName;
	}

	public static String getCsv() {
		return csv;
	}

	public static void setCsv(String csv) {
		CBREngine.csv = csv;
	}

	/**
	 * This methods creates a myCBR project and loads the project from a .prj file
	 */	
	public Project createProjectFromPRJ(){

		System.out.println("Trying to load prj file with : "+data_path+ " "+projectName+" "+conceptName+" "); 

		Project project = null;

		try{

			project = new Project(data_path + projectName);

			// Sehr wichtig hier das Warten einzubauen, sonst gibts leere 
			// Retrieval Results, weil die Faelle noch nicht geladen sind wenn das 
			// Erste Retrieval laueft		
			while (project.isImporting()){
				Thread.sleep(1000);
				System.out.print(".");
			}		
			System.out.print("\n");	//console pretty print			
		}
		catch(Exception ex){

			System.out.println("Error when loading the project");
		}		
		return project;		
	}	


	/**
	 * This methods creates an EMPTY myCBR project.
	 * The specification of the project's location and according file names has to be
	 * done at the beginning of this class.
	 * @return Project instance containing model, sims and cases (if available)
	 */
	public Project createemptyCBRProject(){

		Project project = null;
		try {
			// load new project
			project = new Project(data_path+projectName);
			// create a concept and get the main concept of the project; 
			// the name has to be specified at the beginning of this class
			while (project.isImporting()){
				Thread.sleep(1000);
				System.out.print(".");
			}
			System.out.print("\n");	//console pretty print
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return project;
	}
}