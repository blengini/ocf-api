package com.ocf.api.to;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import com.ocf.api.util.Complex;

/**
 * Classe responsavel pelos dados dos alimentadores
 * @author Adolfo
 */
public class Feeder {

	// nome do alimentador ou label
	private String label;
		
	// Todos Arcos que compoe o grafo, rede completa
	private List<Edge> edges = new LinkedList<Edge>(); 
	
	// Arco com a menor tensao.
	private Edge lowVoltage;
	
	// Tens�o iniciao V0 do alimentador.
	private Complex initalVoltage;
		
	// Lista de todos os ramos do alimentador.
	private List<Branch> branchs = new ArrayList<>();
	
	// Armazena todos os nos do alimentador.
	private HashMap<String , Node> cacheNodes = new HashMap<>();	
	
	// Armazena a quantidade arcos de chegada no no destiono.
	private HashMap<String, Integer> cacheDestinationNodes = new HashMap<>();
	
	// Armazena os nos de geracao (fontes)
	private HashMap<String, Node> cacheGenerationNodes = new HashMap<String, Node>();
	
	// Armazena o indice o arco para o no destino
	private HashMap<String, Integer> cacheEdgesDestinationNodes =  new HashMap<>();
	
	// Alimentador estao em anel;
	private boolean isMesh = false; 
	
	// Tamanho em KM do alimentador
	private double size = 0;
	
	/**
	 * Construtor com todos os parametros.
	 * @param id
	 * @param label
	 */
	public Feeder(String label)
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
	
	public void setEdges(List<Edge> list)
	{
		this.edges = list;
	}

	// Obtem o arco acima do no desejado
	public Edge getEdgeReference(Node sourceNode) {
			
		Edge edgeReference = null;
		for (Edge to: edges)
		{
			if (to.getDestinationNode().getLabel().equals(sourceNode.getLabel()))
			{
				edgeReference = to;
				break;
			}			
		}
		
		return edgeReference;
	}
	
	// Obtem os arcos abaixo do no desejado
	public List<Edge> getEdgesForward(Node sourceNode) {

		List<Edge> edgeReferences = new LinkedList<>();

		for (Edge to : edges) {
			if (to.getSourceNode().getLabel().equals(sourceNode.getLabel())) {
				edgeReferences.add(to);
			}
		}
		return edgeReferences;
	}
	
	// Obtem o destino 
	public Node getDestinationNode(String label) {
			
		Node node = null;
		
		for (Edge to: edges)
		{
			// encontramos o no destino do arco
			if (to.getDestinationNode().getLabel().equals(label))
			{			
				node = to.getDestinationNode();
				break;
			}			
		}		
		return node;
	}
	
	/**
	 * busca os nos que possuem mais de um arco chegando;
	 * 
	 * @return Todos os ramos do alimentor.
	 */
	private List<Node> findBranchsMesh()
	{	
		// Nos que fecham o laco
		List<Node>  nodes =  new ArrayList<>();
		
		for (Entry<String, Integer> value : cacheDestinationNodes.entrySet())
		{
			// No possui mais de  um arco chegando (ANEL!)
			if (value.getValue() > 1)
			{
				String label = value.getKey();
				Node node = cacheNodes.get(label);
				nodes.add(node);
			}
		}
		
		//  nodes > 1 indica que esta rede possui anel
		return nodes; 
	}
	
	
	
	/**
	 * busca todos os elementos que compoe a equacao entre o no folha e o troco
	 * do grafo.
	 * 
	 * @return Todos os ramos do alimentor.
	 */
	private List<Branch> findBranchs()
	{	
		List<Branch> localBranchs =  new ArrayList<>();
		HashMap<String, Node> destinationNodes = new HashMap<>();
		
		// Percorre todos os arcos da rede
		for (Edge edge: this.getEdges())
		{	
			// Se ha carga no no destino do arco, este no � uma folha da rede
			if (edge != null && edge.getDestinationNode().getLoads().size() > 0) 
			{	
				// Verifica se h� mais de um arco com a mesma carga
				if (!destinationNodes.containsKey(edge.getDestinationNode().getLabel()))
				{					
					Branch branch = new Branch(edge.getDestinationNode().getLabel());
					branch.addEdges(edge);
					Edge newEdge = edge.getReference();
					destinationNodes.put(edge.getDestinationNode().getLabel(), edge.getDestinationNode());
					
					// percorre os elementos amontantes do no e monta o laco
					while (newEdge != null) 
					{		
						branch.addEdges(newEdge.getReference());
						newEdge = newEdge.getReference();
					}
				
					localBranchs.add(branch);
				}
			}
		}	
	
		return localBranchs; 
	}
	
	/**
	 * retorna todos os elementos que compoe a equacao entre o no folha e o troco
	 * do grafo.
	 * 
	 * @return Todos os ramos do alimentor.
	 */
	public List<Branch> getBranchs()
	{	
		if (branchs.size() == 0)
		{
			//Lacos abertos carga x terra (redes radiais)
			branchs.addAll(findBranchs());		
		
		}
		return branchs; 
	}
	
	/**
	 * Obtem todos os arcos que estao chegando no n� destino desejado.
	 * 
	 * (Obs: Este m�todo deve ser melhorado)
	 * @param label
	 * @return
	 */
	public List<Edge> getEdgesByDestinationNodes(String label)
	{
		List<Edge> myEdges = new ArrayList<>();
				
		for (Edge edge: edges)
		{
			if (edge.getDestinationNode().getLabel().equals(label))
			{
				myEdges.add(edge);
			}
		}
		return myEdges;
	}
	
	/**
	 * Obtem todos os nos referencias dos arcos que chegam no n�.
	 * 
	 * (Obs: Este m�todo deve ser melhorado)
	 * @param label
	 * @return
	 */
	public List<Node> getNodesByDestinationNodes(String label)
	{
		List<Node> nodes =  new ArrayList<Node>();
		
		for (Edge edge: edges)
		{
			if (edge.getDestinationNode().getLabel().equals(label))
			{
				nodes.add(edge.getSourceNode());
			}
		}
		
		return nodes;
	}
	
	
	
	/**
	 * Obtem todos os arcos que estao saindo do no origem
	 * 
	 * (Obs: Este m�todo deve ser melhorado)
	 * @param label
	 * @return
	 */
	public List<Edge> getEdgesBySourceNodes(String label)
	{
		List<Edge> myEdges = new ArrayList<>();
				
		for (Edge edge: edges)
		{
			if (edge.getSourceNode().getLabel().equals(label))
			{
				myEdges.add(edge);
			}
		}
		return myEdges;
	}
	
	/**
	 * Retorna o no armazenado no em cache
	 * @param label
	 * @return
	 */
	public Node getNodeByCache(String label)
	{
		return cacheNodes.get(label);
	}
	
	/**
	 * Adiciona o n� no cache
	 * @param label
	 * @return
	 */
	public void addNodeToCache(Node node)
	{
		cacheNodes.put(node.getLabel(), node);
	}
	

	/**
	 *
	 * Adiciona o n� no cache (Somente para redes radiais)
	 * @param label
	 * @return
	 */
	public void addIndexEdge(String nodeDestinationLabel, int labelEdege)
	{
		if (!cacheEdgesDestinationNodes.containsKey(nodeDestinationLabel))
		{
			cacheEdgesDestinationNodes.put(nodeDestinationLabel, labelEdege);
		}
	}
	
	
	/**
	 * @param destinationNodeLable retorna o indice do arco para o n� destino 
	 * somente para redes radias.
	 * @return
	 */
	public int getLabelEdgeByDestinationNode(String destinationNodeLable)
	{
		return cacheEdgesDestinationNodes.get(destinationNodeLable);
	}

	
	/**
	 * Retorna a quantidade de n�s da rede
	 * @return
	 */
	public int getSizeNodes()
	{
		return cacheNodes.size();
	}
	
	/**
	 * Retorna a quantidade de arcos de chegada no n�
	 * destino.
	 * @return quantidade arcos que chegam no n�.
	 */
	public int getDestionationNodeByCache(Node destination)
	{
		return cacheDestinationNodes.get(destination.getLabel());
	}
	
	/**
	 * Retorna todo o map do cache
	 * destino.
	 * @return quantidade arcos que chegam no n�.
	 */
	public Set<Entry<String,Integer>>  getDestionationNodeByCacheEntrySet()
	{
		return cacheDestinationNodes.entrySet();
	}
	
	/**
	 * Adiciona o  quantidade de arcos de chegada no n�
	 * destino.
	 * @return quantidade arcos que chegam no n�.
	 */
	public void addDestionationNodeByCache(Node destination)
	{
		int sizeNode = 0;
		
		// ------------------
		//Verifica se o n� � um n� de gera��o
		// ------------------
	
		if(cacheDestinationNodes.get(destination.getLabel()) != null)
		{		
			sizeNode =  cacheDestinationNodes.get(destination.getLabel());		
		}
		sizeNode = sizeNode + 1;
		cacheDestinationNodes.put(destination.getLabel(), sizeNode);
	
	}
	
	/**
	 * Verifica se a rede est� me malha
	 * 
	 * @return
	 */
	public boolean isMesh()
	{		
		List<Node> nodes = findBranchsMesh();
		
		if (nodes.size() > 0 )
		{
			isMesh = true;
		}
		else
		{
			isMesh = false;
		}
		
		return isMesh;		
	}
	
	/**
	 * Adiciona os n�s de gera��o
	 * @param node
	 */
	public void addGenerationNodes(Node node)
	{
		cacheGenerationNodes.put(node.getLabel(), node);
	}
	
	/**
	 * Retorna a quantidade de n�s de gera��o
	 * @return
	 */
	public int getGenerationNodesSize()
	{
		return cacheGenerationNodes.size();
	}
	
	/**
	 * Retorna os n�s de gera��o
	 * @return
	 */
	public Collection<Node> getGenerationNodes()
	{
		return cacheGenerationNodes.values();
	}
	
	/**
	 * verifica se o n� � de gera��o
	 * @return
	 */
	public boolean checkGenerationNode(Node node)
	{
		return cacheGenerationNodes.containsKey(node.getLabel());
	}
	
	/**
	 * Adiciona o trecho no tamanho do alimentador
	 * @param edgeSize
	 */
	public void addSize(double edgeSize)
	{
		size= size + edgeSize;
	}
	
	/**
	 * Retorna o tamanho do alimentor em KM.
	 * 
	 * @return
	 */
	public double getFeederSize()
	{
		return size;
	}
}
