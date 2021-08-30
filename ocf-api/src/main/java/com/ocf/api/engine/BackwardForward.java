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
 * Classe responsavel pela execucao do metodo de 
 * fluxo de carga Backforward
 *
 * @date   21/09/2015
 * @author Adolfo Blengini Neto
 * @author Marcius Fabius Henriques de Carvallho.
 * @author Alexandre Mota.
 * @version 1.0
 *
 */
public class BackwardForward implements Engines {

	// Iteracoes ou Estado da Rede
	private NetworkResult iteration;
	

	@Override
	public NetworkState execute(SortedSet<String> feeders, Network network) 
	{		
		System.out.println("***********************************************************");
		System.out.println("     			Metodo Backforward ");
		System.out.println("***********************************************************");
		
		int indexIteration = 1;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault());
		BigDecimal convergent = null;
		NetworkState state = null;
		
		// Tensao atribuida na primeira iteracao
		BigDecimal drop = PowerFlowConstants.V1.getReal();
		
		
		for (String key : feeders) 
		{
			indexIteration = 1;
			
			// Obtem o alimentador para calculo
			Feeder feeder = network.getFeeder(key);

			// Tensao inicial do alimentadore primario
			convergent = feeder.getInitalVoltage().getReal();
			String begin = sdf.format(new Date());
			
			if(PowerFlowConstants.DEBUG)
			{
				System.out.println("Inicio BFS --> " + begin);
			}
			
			
			// ---------------------------
			// Enquanto DeltaVx+1 - DeltaV > Tolerancia
			// ---------------------------			
			while (convergent.compareTo(PowerFlowConstants.TOLERANCE) >= 1) 
			{
				convergent = drop;
				iteration = new NetworkResult(feeder.getEdges().size(), indexIteration);
				
				// --------------------
				// Calcular as correntes amontandes da rede, a cada rede 
				// gerada uma simulacao
				// -------------------
				backward(feeder);

				// -------------------
				// Gera as correntes em cada ramo
				// -------------------
				drop = forward(feeder);

				// ----------------------
				// Numero das iteracoes
				// ---------------------
				indexIteration = indexIteration + 1;

				// --------------------
				// DeltaV - DeltaV-1
				// --------------------
				convergent = convergent.subtract(drop).abs();

				if(PowerFlowConstants.DEBUG)
				{		
					System.out.println(" Iterac�es     = " + (indexIteration - 1) );
					
//					// print correntes
//					iteration.toStringCurrents();
//	
//					// print tensoes
//					iteration.toStringLowVoltage(network, feeder.getLabel());
				}
			}
			
			state = iteration.convertToNetworkState();
			state.setConvergence(convergent.doubleValue());
			state.setIndex(indexIteration - 1);

			if(PowerFlowConstants.DEBUG)
			{
				String end = sdf.format(new Date());
				System.out.println("Fim: BFS --> " + end);
			}
			
			System.out.println(" ----------------------------");
			System.out.println(" Alimentador   = " + key);
			System.out.println(" Itera��es     = " + (indexIteration - 1));
			System.out.println(" Converg�ncia  = " + convergent);
			System.out.println("----------------------------");
			
			// print correntes
			iteration.toStringCurrents();

			// print tensoes
			iteration.toStringLowVoltage(network, feeder.getLabel());

			
			
			drop = PowerFlowConstants.V1.getReal();
			convergent = feeder.getInitalVoltage().getReal();			
		}		
		
		// print as menores tensoes
		iteration.toStringLowVoltage(network);		
		
		return state;
	}
	
	/**
	 * Calculo das correntes ativas e reativas.
	 * 
	 * @param netwotk
	 *            grafo
	 */
	private void backward(Feeder feeder) 
	{
		// tensao arbitrada na iteracao
		Complex v = null;

		for (Edge edge : feeder.getEdges()) 
		{

			// Set o ha carga no no , este no � uma folha da rede
			if (edge != null && edge.getDestinationNode().getLoads().size() > 0) 
			{
			
				// recupera a tensao inicial arbitrata
				if (edge.getVoltage() != null) 
				{
					v = edge.getVoltage();
				}
				else 
				{
					v = PowerFlowConstants.V1;
				}

				// seta a nova tensao no barra destino
				edge.setVoltage(v);

				// obtem o no destino do arco
				Node  destinationNode = edge.getDestinationNode();

				// obtem a nova corrente da barra
				Complex i = destinationNode.getInstantCurrent(v);
			
				// atualiza a corrente na iteracao
				iteration.addCurrent(Integer.parseInt(edge.getLabel()), i);

				// percorre os elementos amontantes do no e monta o laco
				while (edge.getReference() != null) 
				{
					// obtem o arco amontante
					edge = edge.getReference();

					// obtem no destino
					destinationNode = edge.getDestinationNode();

					// atualiza a corrente na iteracao
					iteration.addCurrent(Integer.parseInt(edge.getLabel()), i);

					// atualiza o destino no arco.
					edge.setDestinationNode(destinationNode);
				}
			}
		}
	}

	/**
	 * Calculo da Tensao em todos arcos da rede
	 * 
	 * @param v0
	 *            - tensao incial do alimentador
	 * @param network
	 *            - grafo da rede para calculo
	 * @return - media da queda de tensao em todos os arcos da rede
	 */
	private BigDecimal forward(Feeder feeder) 
	{
		BigDecimal deltaVk = new BigDecimal(0);
		Complex v0 = feeder.getInitalVoltage();
		int index = 0;

		// U = i * Z
		// V = V0 - U

		for (Edge edge : feeder.getEdges()) 
		{
			// primeiro trecho utiliza-se a tensao referencia do alimentador
			if (edge.getReference() != null) 
			{	
				v0 = edge.getReference().getVoltage();
			}
			else
			{
				v0 = PowerFlowConstants.Vi;
			}

			// obtem a corrente acumulada no no
			Complex i = iteration.getCurrent(Integer.parseInt(edge.getLabel()));

			// N�o h� corrente no n�
			if (i ==  null)
			{
				i = new Complex(new BigDecimal(0), new BigDecimal(0));
			}		
			
			// obtem a impedacia r , x
			Complex z = edge.getImpedance();

			// tensao no techo
			Complex u = i.multiply(z);

			// tensao no origem - no destino
			Complex v = v0.subtract(u);

			// queda de tensao
			Complex drop = v0.subtract(v);

			// atualiza a tensa no arco
			edge.setVoltage(v);

			// seta a tensao no arco
			iteration.addVoltage(Integer.parseInt(edge.getLabel()), v);

			// seta a queda de tensao
			iteration.addDropVoltage(Integer.parseInt(edge.getLabel()), drop);

			// armazena a soama das tensoes do trecho
			deltaVk = deltaVk.add(v.getReal());

			index = index + 1;

		}

		// Media das tensoes
		BigDecimal returnVk = deltaVk.divide(new BigDecimal(index), 6,
				RoundingMode.CEILING);
		
		return returnVk;
	}

	@Override
	public void printResults() {
		// TODO Auto-generated method stub
		
	}
}
