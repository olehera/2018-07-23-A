package it.polito.tdp.newufosightings.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.newufosightings.model.Evento.TipoEvento;

public class Simulatore {

	private PriorityQueue<Evento> queue;
	private int giorni;
	private int alfa;
	private Map<State, Double> mappa;
	private Graph<State, DefaultWeightedEdge> grafo;
	private Map<String, State> stati;
	private Random rand;
	
	public void init(int giorni, int alfa, List<Sighting> avvistamenti, Graph<State, DefaultWeightedEdge> grafo, Map<String, State> stati) {
		
		queue = new PriorityQueue<>();
		this.giorni = giorni;
		this.alfa = alfa;
		this.mappa = new HashMap<>();
		this.grafo = grafo;
		this.stati = stati;
		rand = new Random();
		
		for (State s: stati.values())
			mappa.put(s, 5.0);
				
		for (Sighting e: avvistamenti) 
			queue.add(new Evento(TipoEvento.AVVISTAMENTO, e.getDatetime(), e));
		
	}

	public void run() {
		
		while ( !queue.isEmpty() ) {     
			Evento ev = queue.poll();
			
			switch (ev.getTipo()) {
			
			case AVVISTAMENTO:
				
				State stato = stati.get(ev.getAvvistamento().getState());
				
				decrementa(stato, 1);
				
				List<State> collaterali = new ArrayList<>();
				for (State s: Graphs.neighborListOf(grafo, stato))
					if (rand.nextInt(101) <= alfa) {
						decrementa(s, 0.5);
						collaterali.add(s);
					}
						
				
				queue.add(new Evento(TipoEvento.CESSATA_ALLERTA, ev.getAvvistamento().getDatetime().plusDays(giorni), ev.getAvvistamento(), collaterali));
				
				break;
				
			case CESSATA_ALLERTA:
				
				State stat = stati.get(ev.getAvvistamento().getState());
				
				aumenta(stat, 1);
				
				for (State s: ev.getCollaterali())
					aumenta(s, 0.5);
					
				break;
			}
		}
		
	}
	
	private void aumenta(State stato, double val) {
		double attuale = mappa.get(stato);
		mappa.remove(stato);
		
		if (val == 1 && attuale <= 4)
			mappa.put(stato, attuale+val);
		else if (val == 0.5 && attuale <= 4.5)
			mappa.put(stato, attuale+val);
		else 
			mappa.put(stato, attuale);
	}
	
	private void decrementa(State stato, double val) {
		double attuale = mappa.get(stato);
		mappa.remove(stato);
		
		if (val == 1 && attuale >= 2)
			mappa.put(stato, attuale-val);
		else if (val == 0.5 && attuale >= 1.5)
			mappa.put(stato, attuale-val);
		else 
			mappa.put(stato, attuale);
	}
	
	public Map<State, Double> getDefcon() {
		return mappa;
	}
	
}