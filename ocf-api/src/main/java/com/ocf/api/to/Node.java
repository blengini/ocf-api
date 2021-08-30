package com.ocf.api.to;

import java.math.BigDecimal;
import java.util.List;

import com.ocf.api.util.Complex;

/**
 * Classe responsavel pelo encapsulamento dos atributos dos nos.
 * @author ablengini
 *
 */
public class Node {

	// label ou desrica do no
	private String label;	
	
	// Cargas do no
	private List<Load> loads;	
	
	// corrente do n�
	private Complex current = new Complex(new BigDecimal(0), new BigDecimal(0));
	
	// queda de tensao no n�
	private BigDecimal dropVoltage;
	
	// limite minimo de tens�o
	private BigDecimal minLimitVoltage;
		
	// no pai ou no de referencia
	private Node referenceNode;	
	

	//------------------------------
	// Opera��es do No
	//------------------------------


	// seta o label do no
	public String getLabel() {
		return label;
	}

	// obtem o limite minimo de tens�o do no ou barra
	public BigDecimal getMinLimitVoltage() {
		return minLimitVoltage;
	}

	// seta o limite minimo de tensao
	public void setMinLimitVoltage(BigDecimal minLimitVoltage) {
		this.minLimitVoltage = minLimitVoltage;
	}

	// obtem o label do no
	public void setLabel(String label) {
		this.label = label;
	}

	// obtem a corrente acumulada ajusante do no.
	public Complex getTotalCurrent()
	{
		return current;
	}
	
	// seta a carga do no
	public void addLoad(Load load) {
		this.loads.add(load);
	}


	//obtem a queda de tensao
	public BigDecimal getDropVoltage() {
		return dropVoltage;
	}
	
	// seta a tensao no no
	public void setDropVoltage(BigDecimal dropVoltage) {
		this.dropVoltage = dropVoltage;
	}
	
	// Construtor customizado com identificacao e no de referencia
	public Node(String label){
		this.label = label;
	}		
	
	// Construtor customizado com identificacao e no de referencia
	public Node(Node reference, String label){	
		this.referenceNode = reference;
		this.label= label;
	}	
	
	// Construtor customizado com identificacao , no de referencia e carga
	public Node(Node reference, List<Load> loads, String label){	
		this.referenceNode = reference;
		this.loads = loads;
		this.label = label;
	}	

	//obtem as cargas do no
	public List<Load> getLoads() {
		return loads;
	}
	
	//seta as cargas do no (se j� houver cargas carregadas no n� as cargas ser�o somadas.
	public void setLoads(List<Load> ploads) {
		
		this.loads = ploads;
	}
	
	//obtem a corrente total do no
	public Complex getInstantCurrent(Complex v)
	{
		Complex iTotal = new Complex(new BigDecimal(0), new BigDecimal(0));
		
		//Soma a corrente obtida em todas as cargas
		if (this.loads !=  null)
		{
			for (Load load: this.loads)
			{
				Complex i = load.getInstantCurrent(v);
				iTotal = iTotal.add(i);
			}
		}
		return iTotal;
	}	

	// obtem o no de referencia ou no pai
	public Node getReferenceNode() {
		return referenceNode;
	}

	// seta o no de referencia ou no pai
	public void setReferenceNode(Node referenceNode) {
		this.referenceNode = referenceNode;
	}	
	
	// retorna o label do arco
	public String toString()
	{
		String out =  label + "|" + this.getLabel();
		return out;
	}
}
