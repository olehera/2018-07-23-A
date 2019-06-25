package it.polito.tdp.newufosightings.model;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

public class Evento implements Comparable<Evento> {
	
	public enum TipoEvento {
		AVVISTAMENTO,
		CESSATA_ALLERTA
	}
	
	private TipoEvento tipo;
	private LocalDateTime tempo;
	private Sighting avvistamento;
	private List<State> collaterali;
	
	public Evento(TipoEvento tipo, LocalDateTime tempo, Sighting avvistamento) {
		this.tipo = tipo;
		this.tempo = tempo;
		this.avvistamento = avvistamento;
		this.collaterali = new LinkedList<>();
	}
	
	public Evento(TipoEvento tipo, LocalDateTime tempo, Sighting avvistamento, List<State> collaterali) {
		this.tipo = tipo;
		this.tempo = tempo;
		this.avvistamento = avvistamento;
		this.collaterali = collaterali;
	}
	
	@Override
	public int compareTo(Evento ev) {
		return this.tempo.compareTo(ev.tempo);
	}

	public TipoEvento getTipo() {
		return tipo;
	}

	public LocalDateTime getTempo() {
		return tempo;
	}

	public Sighting getAvvistamento() {
		return avvistamento;
	}

	public List<State> getCollaterali() {
		return collaterali;
	}

}