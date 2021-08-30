package com.ocf.derm;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Scanner;

import com.ocf.api.to.Edge;
import com.ocf.api.to.Feeder;
import com.ocf.api.to.Load;
import com.ocf.api.to.LoadAdmittanceConstant;
import com.ocf.api.to.LoadCurrentConstant;
import com.ocf.api.to.LoadImpedanceConstant;
import com.ocf.api.to.LoadPowerConstant;
import com.ocf.api.to.Network;
import com.ocf.api.to.Node;
import com.ocf.api.util.Complex;

/**
 * Classe responsavel pelo processamento atraves de aquivo de entrada simulando o INPUT do DERM
 * @author ablengini
 *
 */
public class ParserInputFile implements DataEntry{
	
	// Arquivo de entrada
	private File file;
	
	/**
	 * Construtor padr�o.
	 * @param file
	 */
	public ParserInputFile(File file)
	{
		this.file = file;
	}
	/**
	 * Realiza o Parser do arquivo de entrada imput.csv e monta o objeto que representa a Rede radia estuda.
	 * @param file
	 * @return Network : Objeto da Rede
	 */
	@SuppressWarnings("removal")
	public Network parser()
	{	
		// Grafo
		Network network = new Network(0);		
		try 
		{			
			// L� o arquivo.
			Scanner sc = new Scanner(file);
			
			// Formato do arquivo de entrada.
			MessageFormat message = new MessageFormat("{0};{1};{2};{3};{4};{5};{6};{7};{8};{9};{10};{11};{12};{13};{14};{15};{16};{17};{18};{19};{20};{21}");
		
			// ---------------------------------------------------------------------------	
			// EXEMPLO DO ARQUIVO DE ENTRADA, campos separado por (;)
			// Alimentador	Id Arco	Id No Origem	Id No Destino	Distancia (km)	Potencia Ativa	Potencia Reativa	Tensao Barra	R		X	 Estado	Zr	Zi	Ir	Ii	Yr	Yi Tipo Carga Limite Linha SupR Limite Linha InferiorR Limite Linha SupI Limite Linha InferiorI
			// A			1		0				2				1				0,0001			0,0001				0				0,02	0,06 NF							   G	      0,01			   -0,01   0 	-0,002			
			// A			2		1				2				1				0,02			0,01				0				0,02	0,06 NF						       C
			//-----------------------------------------------------------------------------	

			// Cabecalho
			String line = sc.nextLine();

			// Leitura do arquivo ate o final do arquivo.
			while (sc.hasNext()) 
			{
				Edge reference = null;
				ArrayList<Load> loads = new ArrayList<Load>();
				Load load;
				Complex impedance = null;
	
							
				// ---------------------------
				// INICIO DA LEITURA DO ARQUIVO
				// ---------------------------
			
				// le lina a lina do arquivo de entrada de dados
				line = sc.nextLine();
				
				// realiza o parse estabelecido
				Object[] objects = message.parse(line);

				// label do alimentador
				String lableFeeder = objects[0].toString();
				
				// identicacao do Arco
				String sedgeLabel= objects[1].toString();
				
				// identificacao do No origem
				String sSourceNode = objects[2].toString();
				
				// identificacao do No destino
				String sDestinationNode = objects[3].toString();
				
				// distancia entre os nos origem e destino
				String sLength = objects[4].toString();
				
				// P
				String siReal = objects[5].toString();
												
				// Q
				String siImaginary = objects[6].toString();		
				
				// limitVoltage
				String slimitVoltage = objects[7].toString();
				
				// R
				String sResistence = objects[8].toString();			

				// X
				String sReatance = objects[9].toString();	
				
				// estado do arco
				String sState = objects[10].toString();	
				
				// Zr
				String zR = objects[11].toString();
				
				// Zi
				String zI = objects[12].toString();		
				
				// Ir
				String iR = objects[13].toString();			

				// Ii
				String iI = objects[14].toString();	
				
				// Yr
				String yR = objects[15].toString();			

				// Yi
				String yI = objects[16].toString();	
					
				// Caracteristica do No destino (C = Carga ou G = Gera��o)
				String type = objects[17].toString();
				
				// Limite superior da linha parte Real
				String maxLimitR =  objects[18].toString();
				
				// Limite inferior da linha parte Real
				String minLimitR =  objects[19].toString();

				// Limite superior da linha parte Imag
				String maxLimitI =  objects[20].toString();
				
				// Limite inferior da linha parte Imag
				String minLimitI =  objects[21].toString();
				
				
				// ----------------------------------
				// FIM DA LEITURA DO ARQUIVO
				// ----------------------------------
				
				// Cargas de Potencia constante
				if (!siReal.equals("") && !siImaginary.equals("") && !siReal.equals("0") && !siImaginary.equals("0"))
				{
					Complex value = new Complex(siReal, siImaginary);				
					load = new LoadPowerConstant(value);
					loads.add(load);
				}
				
				// Cargas de Impedancia constante
				if (!zR.equals("") && !zI.equals("") && !zR.equals("0") && !zI.equals("0"))
				{
					Complex value = new Complex(zR, zI);
					load = new LoadImpedanceConstant(value);
					loads.add(load);
				}
				
				// Cargas de Corrente constante
				if (!iR.equals("") && !iI.equals("") && !iR.equals("0") && !iI.equals("0"))
				{
					Complex value = new Complex(iR, iI);
					load = new LoadCurrentConstant(value);
					loads.add(load);
				}
				
				// Cargas de Admitancia constante
				if (!yR.equals("") && !yI.equals(""))
				{
					Complex value = new Complex(yR, yI);
					load = new LoadAdmittanceConstant(value);
					loads.add(load);
				}
				
				// Buca o alimentador da rede
				Feeder feeder = network.getFeeder(lableFeeder);
				
				// Se n�o existe o aimentador
				if (feeder == null)
				{
					//Cria o alimentador
					feeder = new Feeder(lableFeeder);
				}				
				
				// Busca n�s no cache (origem)
				Node sourceNode = feeder.getNodeByCache(sSourceNode);
				
				// Busca n�s no cache (destino)
				Node destinationNode = feeder.getNodeByCache(sDestinationNode);
				
				// no origem nao exite cria
				if (sourceNode == null)
				{
					sourceNode = new Node(sSourceNode);					
				}				
						
				// no destino nao existe cria
				if (destinationNode == null)
				{
					// Cria no destino
					destinationNode = new Node(sourceNode, loads, sDestinationNode);
				}
				
				// atualiza as cargas do no destino do arco 
				// IMPORTANTE: Se no arquivo houver mais de uma carga para o mesmo no destino as cargas ser�o somadas.
				if (!loads.isEmpty())
				{
					destinationNode.setLoads(loads);
				}
				
				
				// Se o n� destino possui limite de tens�o
				if (slimitVoltage!=null && !slimitVoltage.equals("0") && !slimitVoltage.equals("0.0") && !slimitVoltage.equals("0,0") )
				{
					BigDecimal limiteVoltage =  new BigDecimal(slimitVoltage.replaceAll(",", "."));
					destinationNode.setMinLimitVoltage(limiteVoltage);
				}
				
				// Se a barra de origem for de gera��o seta no cache do alimentador
				if (type.equals("G"))
				{
					// seta o no origem como fonte do alimentador
					feeder.addGenerationNodes(sourceNode);					
				}
				else
				{
					// atualiza o cache para analise de redes em anel
					feeder.addDestionationNodeByCache(destinationNode);
				}
				
				// atualiza o tamanho do alimetador
				feeder.addSize(new Double(sLength.replace(',','.')));
				
				// atualiza o cache com no origem. 
				feeder.addNodeToCache(sourceNode);
				
				// atualiza o cache com no destino.
				feeder.addNodeToCache(destinationNode);
						
				// Conversao para double da distancia entre os dois nos
				BigDecimal lenght = new BigDecimal(sLength.replace(',', '.'));				
						
				// Conversao para double da resistencia
				BigDecimal resistence = new BigDecimal(sResistence.replace(',', '.'));
				
				// Conversao para double da reatancia
				BigDecimal reatance = new BigDecimal(sReatance.replace(',', '.'));
				
				// Conversao limite do arco 
				// Cria a impedancia do arco
				impedance = new Complex(resistence, reatance);
				
				// Se chave com estado normal
				if (sState.equals("NF"))
				{
					//obtem o arco de refernecia amontante
					reference = feeder.getEdgeReference(sourceNode);
				}
				
				// cria o arco
				Edge edge = new Edge(lenght, sourceNode, destinationNode, PowerFlowConstants.V1, impedance, sedgeLabel, reference, feeder);
			
				// gambi para o metodo forward
				feeder.addIndexEdge(destinationNode.getLabel(), Integer.parseInt(sedgeLabel));
				
				// Se o arco possui um limite maximo seta o valor Real ( n�o obrigatorio)
				if ((maxLimitR!= null) && !maxLimitR.equals(""))
				{					 
					 edge.setMaxLimitR(maxLimitR);
				}		
				
				
				// Se o arco possui um limite minimo seta o valor Real( n�o obrigatorio)
				if ((minLimitR!= null) && !minLimitR.equals(""))
				{
					 edge.setMinLimitR(minLimitR);
				}
				
				
				// Se o arco possui um limite maximo seta o valor Imaginario( n�o obrigatorio)
				if ((maxLimitI!= null) && !maxLimitI.equals(""))
				{
					 edge.setMaxLimitI(maxLimitI);
				}		
				
				
				// Se o arco possui um limite minimo seta o valor Imaginario( n�o obrigatorio)
				if ((minLimitI!= null) && !minLimitI.equals(""))
				{
					 edge.setMinLimitI(minLimitI);
				}
				
				
				// seta o estado do arco NF ou NA
				edge.setState(sState);
				
				// adciona o arco criando no alimentador
				feeder.addEdges(edge);
		
				// seta a tensao inicial do arco como a default
				feeder.setInitalVoltage(PowerFlowConstants.Vi);
				
				// Adiciona ou Atualiza o feeder
				network.addFeeder(lableFeeder, feeder);		
				
				// ---------------------------------------
				// Popula a Matriz Incidencia No x Arco
				// ---------------------------------------		
				// corrente que entra no n�
				network.setMi(destinationNode.getLabel(), edge.getLabel(), 1);
				
				// corrente que sai do n�
				network.setMi(sourceNode.getLabel(), edge.getLabel(), -1);		
				
			}
			sc.close();
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} 
		catch (ParseException e) 
		{
			e.printStackTrace();
		}		
		return network;
	}

}
