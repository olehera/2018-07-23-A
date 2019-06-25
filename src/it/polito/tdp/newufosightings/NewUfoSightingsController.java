package it.polito.tdp.newufosightings;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

import it.polito.tdp.newufosightings.model.Model;
import it.polito.tdp.newufosightings.model.State;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class NewUfoSightingsController {

	private Model model;
	
	public void setModel(Model model) {
		this.model = model;
	}
	
	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;

	@FXML // URL location of the FXML file that was given to the FXMLLoader
	private URL location;

	@FXML // fx:id="txtResult"
	private TextArea txtResult; // Value injected by FXMLLoader

	@FXML // fx:id="txtAnno"
	private TextField txtAnno; // Value injected by FXMLLoader

	@FXML // fx:id="btnSelezionaAnno"
	private Button btnSelezionaAnno; // Value injected by FXMLLoader

	@FXML // fx:id="cmbBoxForma"
	private ComboBox<String> cmbBoxForma; // Value injected by FXMLLoader

	@FXML // fx:id="btnCreaGrafo"
	private Button btnCreaGrafo; // Value injected by FXMLLoader

	@FXML // fx:id="txtT1"
	private TextField txtT1; // Value injected by FXMLLoader

	@FXML // fx:id="txtAlfa"
	private TextField txtAlfa; // Value injected by FXMLLoader

	@FXML // fx:id="btnSimula"
	private Button btnSimula; // Value injected by FXMLLoader

	@FXML
	void doCreaGrafo(ActionEvent event) {
		String forma = cmbBoxForma.getValue();
		
		if ( forma == null ) {
			txtResult.setText("Devi selezionare una forma!");
    		return ;
		}
		
		model.creaGrafo(forma);
		
		for (State s: model.listaVertici())
			txtResult.appendText(s.getName()+" -> "+model.sommaPesi(s)+"\n");
	}

	@FXML
	void doSelezionaAnno(ActionEvent event) {
		int anno = 0;
		
		try {
    		anno = Integer.parseInt(txtAnno.getText().trim());
    	} catch(NullPointerException npe) {
    		txtResult.setText("Inserisci un numero intero come anno!");
    		txtAnno.clear();
    		return ;
    	}
		
		if (anno < 1910 || anno > 2014) {
			txtResult.setText("Inserisci un anno tra il 1910 ed il 2014, estremi inclusi.");
    		txtAnno.clear();
    		return ;
		}
		
		cmbBoxForma.getItems().clear();
		cmbBoxForma.getItems().addAll(model.listaShape(anno));
	}

	@FXML
	void doSimula(ActionEvent event) {
		int alfa = 0;
		
		try {
    		alfa = Integer.parseInt(txtAlfa.getText().trim());
    	} catch(NullPointerException npe) {
    		txtResult.setText("Inserisci un numero intero come alfa!");
    		txtAlfa.clear();
    		return ;
    	}
		
		if (alfa < 0 || alfa > 100) {
			txtResult.setText("Inserisci alfa nel range (0-100)");
			txtAlfa.clear();
    		return ;
		}
		
		int giorni = 0;
		
		try {
    		giorni = Integer.parseInt(txtT1.getText().trim());
    	} catch(NullPointerException npe) {
    		txtResult.setText("Inserisci un numero intero come giorni!");
    		txtT1.clear();
    		return ;
    	}
		
		if (giorni < 1 || giorni > 364) {
			txtResult.setText("Inserisci un numero di giorni maggiore di 0 e minore di 365");
			txtT1.clear();
    		return ;
		}
		
		Map<State, Double> mappa = model.simula(giorni, alfa);
		
		txtResult.appendText("\n\n\nSimulazione:\n");
		for (State s: mappa.keySet())
			txtResult.appendText(s.getName()+" -> "+mappa.get(s)+"\n");
			
	}

	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize() {
		assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
		assert txtAnno != null : "fx:id=\"txtAnno\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
		assert btnSelezionaAnno != null : "fx:id=\"btnSelezionaAnno\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
		assert cmbBoxForma != null : "fx:id=\"cmbBoxForma\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
		assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
		assert txtT1 != null : "fx:id=\"txtT1\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
		assert txtAlfa != null : "fx:id=\"txtAlfa\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
		assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
	}

}