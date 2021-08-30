package com.ocf.api.engine;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.SortedSet;

import com.ocf.api.conf.PowerFlowConstants;
import com.ocf.api.to.Edge;
import com.ocf.api.to.Feeder;
import com.ocf.api.to.Network;
import com.ocf.api.to.NetworkResult;
import com.ocf.api.to.NetworkState;
import com.ocf.api.to.Node;
import com.ocf.api.util.Complex;

/**
 * Classe responsavel pela execu��o do m�todo de 
 * fluxo de carga Forward
 *
 * @date   14/07/2016
 * @author Adolfo Blengini Neto
 * @author Marcius Fabius Henriques de Carvallho.
 * @version 1.0
 *
 */
public class Forward implements Engines{

	// Iteracoes ou Estado da Rede
	private NetworkResult iteration;
	Node lcbv[][];
	int lenLine[];   
	Complex impedance[];
	Complex voltage[];
	
	@Override
	public NetworkState execute(SortedSet<String> feeders, Network network) {
	
		int indexIteration = 1;		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault());
		
		System.out.println("---------------------------------");
		System.out.println("     M�todo Forward ");
		System.out.println("---------------------------------");
	
		// Tens�o atribuida na primeira iteracao
		BigDecimal drop = PowerFlowConstants.V1.getReal();
		
		for (String key : feeders) 
		{
			// obtem o alimentador para calculo
			Feeder feeder = network.getFeeder(key);
			
			lcbv =  new Node[feeder.getEdges().size()][feeder.getEdges().size() + 1];
			impedance= new Complex[feeder.getEdges().size()];
			lenLine =  new int[feeder.getEdges().size()];
			voltage = new Complex[feeder.getEdges().size()];
			
			// Tens�o inicial do alimentadore primario
			BigDecimal convergent = feeder.getInitalVoltage().getReal();
										
			// --------------------
			// Inicializa o vetor das tens�es(barras)
			// -------------------
			for (int index = 0; index  < feeder.getEdges().size(); index++)
			{
				voltage[index] =  PowerFlowConstants.V1;
			}
			
			String begin = sdf.format(new Date());
			System.out.println("Inicio FW --> " + begin);
			
			// --------------------
			// Criar a matriz LCBV
			// -------------------
			createMatrixLCBV(feeder);			
			
			// ---------------------------
			// Enquanto DeltaVx+1 - DeltaV > Tolerancia
			// ---------------------------									
			while (convergent.compareTo(PowerFlowConstants.TOLERANCE) >= 1) 
			{
				iteration = new NetworkResult(feeder.getEdges().size(), indexIteration);
								
				convergent = drop;
			
				// -------------------
				// Calcular as correntes injetadas nas barras e as tens��es.
				// -------------------
				drop = forwardV(feeder);

				// ----------------------
				// Numero das iteracoes
				// ---------------------
				indexIteration = indexIteration + 1;

				// --------------------
				// DeltaV - DeltaV-1
				// --------------------
				convergent = convergent.subtract(drop).abs();
				
				System.out.println(" Iterac�es     = " + (indexIteration - 1));

				// print correntes
				//iteration.toStringCurrents();

				// print tensoes
				//iteration.toStringVoltage();				
			}
			String end = sdf.format(new Date());
			System.out.println("Fim: FW --> " + end);
			
			// print correntes
			iteration.toStringCurrents();

			// print tensoes
			iteration.toStringVoltage();
			
			System.out.println(" ----------------------------");
			System.out.println(" Alimentador   = " + key);
			System.out.println(" Iterac�es     = " + (indexIteration - 1));
			System.out.println(" Convergencia  = " + convergent);
			System.out.println("----------------------------");

			drop = PowerFlowConstants.V1.getReal();
			convergent = feeder.getInitalVoltage().getReal();
			indexIteration = 1;
		}
		
		return iteration.convertToNetworkState();
	}
	
	/**
	 * Calculo das correntes ativas e reativas.
	 * 
	 * @param netwotk
	 *            grafo
	 */
	private void createMatrixLCBV(Feeder feeder) {
	
		for (Edge edge : feeder.getEdges()) {
			
			// Set o ha carga no no , este no � uma folha da rede
			if (edge != null && edge.getDestinationNode().getLoads().size() > 0) {

				// obtem o no destino do arco
				Node  referenceNode = edge.getDestinationNode();
				int iedge = Integer.parseInt(edge.getLabel());
				int inode = Integer.parseInt(referenceNode.getLabel());				
				lcbv[iedge][inode] = referenceNode;			
				lenLine[iedge] = 1;
				
				// percorre os elementos amontantes do no e monta o laco
				while (edge.getReference() != null) {

					// obtem o arco amontante
					edge = edge.getReference();
					iedge = Integer.parseInt(edge.getLabel());
					
					lcbv[iedge][inode] = referenceNode;
					lenLine[iedge] = lenLine[iedge] + 1;
				}
			}
		}
	}

	/**
	 * Calculo da Tens�o em todos arcos da rede
	 * 
	 * @param v0
	 *            - tens�o incial do alimentador
	 * @param network
	 *            - grafo da rede para calculo
	 * @return - media da queda de tensao em todos os arcos da rede
	 */
	private BigDecimal forwardV(Feeder feeder) {
		
		BigDecimal deltaVk = new BigDecimal(0);
		int index = 0;
		
		// U = i * Z
		// V = V0 - U		
		// ----------------------------------
		// Percorre arco por arco
		//-----------------------------------
		for (Edge edge : feeder.getEdges()) 
		{
						
			// obtem a impedacia r , x
			Complex z = edge.getImpedance();
			Complex vx = null;
			Complex iArco = new Complex(new BigDecimal(0.0), new BigDecimal(0.0)); 
			int iedge = Integer.parseInt(edge.getLabel());
			
			// ---------------------------------------------
			// Calculo da Corrente em cada n� da rede
			//----------------------------------------------		
			
			for (int k =0; k <= feeder.getEdges().size(); k++)
			{
				// recupera a barra na matriz lcbv
				Node node = lcbv[iedge][k];				
				
				if (node != null)
				{
					// gambi
					int indexEdge = feeder.getLabelEdgeByDestinationNode(node.getLabel());
					
					vx = voltage[indexEdge];
					
					// Calcula a corrente no n�
					Complex	i = node.getInstantCurrent(vx);
					
					// Calcula a corrente no arco
					iArco = iArco.add(i);					
				}
			}
			
			// recupera o arco amontante
			Edge reference = edge.getReference();
						
			if (reference != null)
			{	
				// recupera a tensao do arco amontante
				vx = reference.getVoltage();
			}
			else
			{
				vx = PowerFlowConstants.Vi;
			}
			
			// tensao no techo
			Complex u = iArco.multiply(z);

			// tensao no origem - no destino
			Complex v = vx.subtract(u);
			
			// atualiza a tensa no arco
			edge.setVoltage(v);

			// armazena a soama das tensoes do trecho
			deltaVk = deltaVk.add(v.getReal());
			
			// atualiza a corrente na iteracao
			iteration.addCurrent(Integer.parseInt(edge.getLabel()), iArco);
			
			// seta a tensao na barra destino do arco
			iteration.addVoltage(Integer.parseInt(edge.getLabel()), v);

			// seta a queda de tensao
			iteration.addDropVoltage(Integer.parseInt(edge.getLabel()), u);
			
			index = index + 1;

		}

		// ---------------------
		// atualizar o vetor das tens�es
		// ---------------------
		voltage = iteration.getVoltages();
		
		// Media das tensoes
		BigDecimal returnVk = deltaVk.divide(new BigDecimal(index), 6,
				RoundingMode.CEILING);
		return returnVk;
	}

	@Override
	public void printResults() {
			
	}

}
