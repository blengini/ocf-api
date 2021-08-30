package com.ocf.api.to;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * Classe que representa o grafo ou a rede 
 * @author ablengini
 *
 */
public class Network {
	
	// Todos os alimentadores que compoe a rede ou grafo
	private Map<String, Feeder> feeders = new HashMap<String, Feeder>(); 
	
	// Arcos com menor tensao em todos os alimentadores
	private LinkedHashMap<String, Edge> lowVoltages = new LinkedHashMap<>();
	
	// matriz incidencia
	public double[][] mI;
		
	// vers�o da rede
	private int version;
	
	public  final int SIZE_NODES = 404;
	public Network()
	{
		//cria instancia matriz incidencia
		mI = new double[SIZE_NODES][SIZE_NODES];
	}
	

	/**
	 * Adiciona um alimentado no grafo
	 * @param edge
	 */
	public void addFeeder(String key, Feeder value)
	{
		feeders.put(key, value);
	}
	
	/**
	 * Contrutor com a versao da rede, por definicao 0 corresponde a versao inical do arquivo
	 * @param version
	 */
	public Network(int version)
	{
		this.version = version;
		
		//cria instancia matriz incidencia
		this.mI = new double[SIZE_NODES][SIZE_NODES];
	}	
	
	public Feeder getFeeder(String key)
	{
		return feeders.get(key);
	}
	
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}	
	
	public Map<String, Feeder> getFeeders() {
		return feeders;
	}

	public void addLowVoltage(String edString, Edge edge)
	{
		lowVoltages.put(edString, edge);
	}
	
	public Edge getLowVoltage(String idEdge)
	{
		return lowVoltages.get(idEdge);
	}	
	
	public LinkedHashMap<String, Edge> getLowVoltages() {
		return lowVoltages;
	}

	public void setLowVoltages(LinkedHashMap<String, Edge> lowVoltages) {
		this.lowVoltages = lowVoltages;
	}

	public void setMi(String nodeLabel, String edgeLabel, int value)
	{		
		int a =  Integer.parseInt(nodeLabel);
		int b = Integer.parseInt(edgeLabel);
		mI[a][b] = value;
	}
	
}
