package com.ocf.derm;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;

import com.ocf.api.to.Edge;
import com.ocf.api.to.Feeder;
import com.ocf.api.to.Network;
import com.ocf.api.to.Node;



/**
 * Classe respons�vel pela an�lise das caracteristicas da rede.
 * 
 * @date   14/07/2016
 * @author Adolfo Blengini Neto
 * @author Marcius Fabius Henriques de Carvallho.
 * @version 1.0
 * 
 */
public class NetworkBO {
	
	
	/**
	 * Caracteristias dos Alimentadores
	 * @param feeders : Identifica��o dos Alimentadores selecionandos na entrada de dados.
	 * @param network : Rede completa.
	 */
	public void printFeedersInfo(SortedSet<String> feeders, Network network)
	{
				
		for (String feederLabel : feeders)
		{
			Feeder feeder = network.getFeeder(feederLabel);
			
			// ------------------
			// Caracateristicas do alimentador
			// -----------------
			
			System.out.println("Alimentador --> " + feeder.getLabel());
			System.out.println("Numero de Arcos --> " + feeder.getEdges().size());
			System.out.println("Numero de Barras --> " + feeder.getSizeNodes());
			System.out.println("Numero de Fontes --> " + feeder.getGenerationNodesSize());
			System.out.println("Alimentador possui anel --> " + feeder.isMesh());
		
		}
	}
	
	/**
	 * Caracteristica dos alimentador selecionado.
	 * 
	 * @param feeder: Alimentador Selecionado.
	 */
	public void printFeederInfo(Feeder feeder)
	{
		// ------------------
		// get Edges
		// -----------------
		List<Edge> edges = feeder.getEdges();
		

		System.out.println("Alimentador --> " + feeder.getLabel());
		System.out.println("Numero de Arcos --> " + feeder.getEdges().size());
		System.out.println("Numero de Barras --> " + feeder.getSizeNodes());
		System.out.println("Numero de Fontes --> " + feeder.getGenerationNodesSize());
		System.out.println("Alimentador possui anel ? --> " + feeder.isMesh());
				
		for (Edge edge: edges)
		{
			System.out.println("Arco --> " + edge.toString());			
		}	
		
	}
	
	/**
	 * Constroi em memoria a rede ou grafo de referencia 
	 * @return
	 */
	public Network buildNetwork(ConfigProperties conf)
	{
		
		Network network =  null;
		
		// Arquivo de entrada de dados , padr�o cadastro dos trechos
		File file;
		try 
		{	
			// Busca o caminho do arquivo de entrada
			file = new File(conf.getInputFile());
			
			// Cria o DAO
			ParserInputFile parser = new ParserInputFile(file);
			
			// Le os dados do arquivo de entrada e monta a rede no objeto Network
			network = parser.parser();	
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}	
		
		return network;
	}
	
	/**
	 * busca os nos que possuem mais de um arco chegando;
	 * 
	 * @return Todos os ramos do alimentor.
	 */
	private List<Node> findBranchsMesh(Feeder feeder)
	{	
		// Nos que fecham o laco
		List<Node>  nodes =  new ArrayList<>();
		
		Set<Entry<String, Integer>> values = feeder.getDestionationNodeByCacheEntrySet();
		
		for (Entry<String, Integer> value : values)
		{
			// No possui mais de  um arco chegando (ANEL!)
			if (value.getValue() > 1)
			{
				String label = value.getKey();
				Node node = feeder.getDestinationNode(label);
				nodes.add(node);
			}
		}
		
		return nodes;
	}
	
	/**
	 * Verifica se a rede est� me malha
	 * 
	 * @return
	 */
	public boolean isMesh(Feeder feeder)
	{		
		List<Node> nodes = findBranchsMesh(feeder);
		
		if (nodes.size() > 0 )
		{
			return true;
		}
		else
		{
			return false;
		}	
	}
}
