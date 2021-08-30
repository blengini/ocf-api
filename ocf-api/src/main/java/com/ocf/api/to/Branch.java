package com.ocf.api.to;


import java.util.LinkedList;
import java.util.List;

import com.ocf.api.util.Complex;

/**
 * Classe respons�vel pelos dados dos la�os ou ramos do alimentador
 * @author ablengini
 *
 */
public class Branch {

	// Nome do Laco
	private String label;
		
	// Todos Arcos que compoe o grafo, rede completa
	private List<Edge> edges = new LinkedList<Edge>(); 
	
	// Arco com a menor tens�o
	private Edge lowVoltage;
	
	// Tens�o iniciao V0 do alimentador
	private Complex initalVoltage;
	
	/**
	 * Construtor com todos os parametros.
	 * @param id
	 * @param label
	 */
	public Branch(String label)
	{
		this.label =  label;
	}	
	
	public Complex getInitalVoltage() {
		return initalVoltage;
	}

	public void setInitalVoltage(Complex initalVoltage) {
		this.initalVoltage = initalVoltage;
	}

	public Edge getLowVoltage() {
		return lowVoltage;
	}

	public void setLowVoltage(Edge lowVoltage) {
		this.lowVoltage = lowVoltage;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	/**
	 * Adiciona um arco no grafo
	 * @param edge
	 */
	public void addEdges(Edge value)
	{
		edges.add(value);
	}
	
	public Edge getEdge(int key)
	{
		return edges.get(key);
	}	
	
	public List<Edge>getEdges() {
		return edges;
	}			
}
