package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import de.dfki.mycbr.core.similarity.AmalgamationFct;

public class GUI {

	public JPanel       Inframe, Outframe, Buttonframe, Labelframe, Fieldframe;
	public JTextField   InputPrice, InputColor, InputMileage, InputNumberofcases, InputAmalgam;
	public JEditorPane  Output,Info;
	public JLabel       InputlabelPrice, InputlabelColor, InputlabelMileage, InputlabelNumberofcases, InputlabelAmalgam, OutputlabelAmalgam;
	public JButton      SubmitQuery;
	public JScrollPane  Scroll;

	public Recommender remy;


	public GUI() {                                               // Constructor for an Instance of SimpleGUI

		InputPrice = new JTextField(10);	
		InputlabelPrice = new JLabel("Price : Please Enter like: 1000.50");

		InputColor = new JTextField(10);	
		InputlabelColor = new JLabel("Color : Please enter like: White");

		InputMileage = new JTextField(10);	
		InputlabelMileage = new JLabel("Mileage: Please enter like: 7050.00");

		InputNumberofcases = new JTextField(10);	
		InputlabelNumberofcases = new JLabel("Number of cases to tretrieve:");

		InputAmalgam = new JTextField(10);	
		InputlabelAmalgam = new JLabel("Amalgamationfunction to use:");

		Output = new JEditorPane("text/html","<b>Initial text</b>");
		Output.setEditable(false);                 // no one should be able to write in the display	
		Output.setToolTipText("<html>Your dialog with the machine.</html>");  

		Scroll = new JScrollPane(Output);          // Scrollpane = "A Frame with Scrollbars										                
		Scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		Scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		Scroll.setBorder(BorderFactory.createTitledBorder("Conversation:"));
		Scroll.getViewport().setPreferredSize(new Dimension(450,200));

		SubmitQuery = new JButton("Submit Query");
		SubmitQuery.setToolTipText("Press me to process the Query.");
		

		Inframe = new JPanel();                                  // Frame for the Inputelements		
		Inframe.setLayout(new GridLayout(5,2));
		
		
		Inframe.add(InputlabelPrice);                            // adding the elements to the JPanels
		Inframe.add(InputPrice);
		Inframe.add(InputlabelColor); 
		Inframe.add(InputColor);
		Inframe.add(InputlabelMileage);
		Inframe.add(InputMileage);			
		Inframe.add(InputlabelNumberofcases);
		Inframe.add(InputNumberofcases);
		Inframe.add(InputlabelAmalgam);
		Inframe.add(InputAmalgam);
		

		Outframe = new JPanel();                            // Frame for the Outputelements			
		Outframe.setSize(new Dimension(300,250));
		Outframe.add(Scroll);

		JFrame Main = new JFrame("myCBR little recommender demo");   // our main frame
		
		/*try {
			Main.setContentPane(new JLabel(new ImageIcon(ImageIO.read(new File("test.jpg")))));
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		Main.getContentPane().setLayout(new BorderLayout());
		//Main.setSize(new Dimension(400,600));
		Main.getContentPane().add(Inframe, BorderLayout.NORTH);
		Main.add(SubmitQuery, BorderLayout.SOUTH);
		Main.getContentPane().add(Outframe, BorderLayout.CENTER);			

		remy = new Recommender();
		remy.loadengine();
		Output.setText(remy.displayAmalgamationFunctions());
		InputAmalgam.setText(remy.myConcept.getActiveAmalgamFct().getName());

		Main.addWindowListener(new WindowAdapter() {        // implementing listeners we need

			public void windowClosing(WindowEvent e) {      // What to do if Window is closed

				System.exit(0);                             
			}
		});

		SubmitQuery.addActionListener(new ActionListener() { // Action if: Button is clicked
			public void actionPerformed(ActionEvent e) {

				CheckforAmalgamSelection();			
				String recomendation = remy.solveOuery(InputColor.getText(),
						Float.valueOf(InputPrice.getText()),
						Float.valueOf(InputMileage.getText()), 
						Integer.valueOf(InputNumberofcases.getText())
						);
				Output.setText(recomendation);
			}
		});

		InputPrice.addActionListener(new ActionListener() {      // Action if "Enter" is pressed
			public void actionPerformed(ActionEvent e) {    // while "Input" has the Focus

				InputPrice.setText("");
			}
		});
		Main.pack();
		Main.setSize(500,400);
		Main.setVisible(true);                               
	}                                                        // Constructor done

	public void setmyinfobox(String inputstring) {

		try {
			Info.setPage(inputstring);
		}
		catch (IOException e) {
			Info.setContentType("text/html");
			Info.setText("<html>Could not load"+inputstring);
		} 
		System.out.println("Done setting the Tooltiptext");
	}

	public void CheckforAmalgamSelection(){

		List<AmalgamationFct> liste = remy.myConcept.getAvailableAmalgamFcts();

		for (int i = 0; i < liste.size(); i++){

			if  ((liste.get(i).getName()).equals(InputAmalgam.getText())) {

				remy.myConcept.setActiveAmalgamFct(liste.get(i));
			}		 
		}			
	}

	public static void main(String[] args) {                 // main Method (starts when
		
		GUI mygui = new GUI();		                              
	}
}




