package it.polito.tdp.newufosightings.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.newufosightings.db.NewUfoSightingsDAO;

public class Model {

	private Graph<State, DefaultWeightedEdge> grafo;
	private NewUfoSightingsDAO dao;
	private List<String> shape;
	private Map<String, State> stati;
	private int anno;
	private String forma;
	private Simulatore sim;
	
	public Model() {
		dao = new NewUfoSightingsDAO(); 
		stati = new HashMap<>();
		shape = new LinkedList<>();
		dao.loadAllStates(stati);
	}
	
	public List<String> listaShape(int anno) {
		this.anno = anno;
		
		if ( shape.isEmpty() )
			shape = dao.loadAllShape(anno);
		
		return shape;
	}
	
	public void creaGrafo(String forma) {
		this.forma = forma;
		
		grafo = new SimpleWeightedGraph<State, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(grafo, stati.values());
		
		for (Confinanti c: dao.confinanti(anno, forma))
			if (!grafo.containsEdge(stati.get(c.getState1()), stati.get(c.getState2())))
				Graphs.addEdge(grafo, stati.get(c.getState1()), stati.get(c.getState2()), c.getPeso());
		
	}
	
	public Set<State> listaVertici() {
		return grafo.vertexSet();
	}
	
	public double sommaPesi(State stato) {
		double somma = 0;
		
		for ( State s: Graphs.neighborListOf(grafo, stato) )
			somma += grafo.getEdgeWeight(grafo.getEdge(stato, s));
		
		return somma;
	}
	
	public Map<State, Double> simula(int giorni, int alfa) {
		
		sim = new Simulatore();
		sim.init(giorni, alfa, dao.loadAllSightings(anno, forma), grafo, stati);
		sim.run();
		
		return sim.getDefcon();
	}
	
}