package com.ocf.api.to;

import java.math.BigDecimal;

import com.ocf.api.util.Complex;

/**
 * Classe responsavel pelo encapsulamento dos atributos dos arcos.
 * 
 * @author ablengini
 *
 */
public class Edge {
	
	//identificacao do arco
	private String label;
	
	//arco referecia
	private Edge reference;
	
	// lenght
	private BigDecimal lenght;

	// Tensao do arco
	private Complex voltage;
	
	// Queda de tensao referente ao no anterior
	private Complex dropVoltage;

	// Impedancia Resistencia e Reatancia do arco
	private Complex impedance;
	
	// no origem
	private Node sourceNode;

	// no destino
	private Node destinationNode;
	
	// alimentador
	private Feeder feeder;
	
	// stado do arco NF = Normal fechado, NA= Normal aberto
	private String state;
	
	// Limite superior do arco
	private String maxLimitR;
	
	// Limite inferior do arco
	private String minLimitR;

	// Limite superior do arco
	private String maxLimitI;
	
	// Limite inferior do arco
	private String minLimitI;
	
	// Contrutor
	public Edge(BigDecimal lenght, Node source, Node destination, String label, Edge reference, Feeder feeder) {
		this.sourceNode = source;
		this.destinationNode = destination;
		this.label = label;
		this.reference = reference;		
		this.lenght = lenght;
		this.feeder = feeder;
	}
	
	// Contrutor
	public Edge(String label) {		
		this.label = label;
	}

	// Contrutor
	public Edge(BigDecimal lenght, Node source, Node destination, Complex voltage, Complex impedance, String label, Edge reference, Feeder feeder) {
		this.sourceNode = source;
		this.destinationNode = destination;
		this.label = label;
		this.reference = reference;		
		this.lenght = lenght;
		this.voltage = voltage;
		this.impedance = impedance;
		this.feeder = feeder;
		
	}

	public Complex getVoltage() {
		return voltage;
	}

	public void setVoltage(Complex voltage) {
		this.voltage = voltage;
	}
	
	public Feeder getFeeder() {
		return feeder;
	}

	public void setFeeder(Feeder feeder) {
		this.feeder = feeder;
	}
	
	public BigDecimal getLenght() {
		return lenght;
	}

	public void setLenght(BigDecimal lenght) {
		this.lenght = lenght;
	}
		
	public Complex getImpedance() {
		return impedance;
	}

	public void setImpedance(Complex impedance) {
		this.impedance = impedance;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Edge getReference() {
		return reference;
	}

	public void setReference(Edge reference) {
		this.reference = reference;
	}

	public void setDestinationNode(Node destinationNode) {
		this.destinationNode = destinationNode;
	}
	
	public Complex getDropVoltage() {
		return dropVoltage;
	}

	public void setDropVoltage(Complex dropVoltage) {
		this.dropVoltage = dropVoltage;
	}

	// retorna no origem do arco
	public Node getSourceNode() {
		return sourceNode;
	}

	// seta no origem do arco
	public void setSourceNode(Node sourceNode) {
		this.sourceNode = sourceNode;
	}

	// obtem o no destino do arco
	public Node getDestinationNode() {
		return destinationNode;
	}

	// seta o no destino do arco
	public void setDsestinationNode(Node destinationNode) {
		this.destinationNode = destinationNode;
	}
	
	// obtem o estado do arco
	public String getState() {
		return state;
	}

	// seta o estado do arco
	public void setState(String state) {
		this.state = state;
	}
	
	// seta o limite max do arco parte real
	public void setMaxLimitR(String value){
		this.maxLimitR = value;
	}
	
	// retorna o limite maximo para real
	public String getMaxLimitR()
	{
		return maxLimitR;
	}
	
	// seta o limite inferior do arco parte real
	public void setMinLimitR(String value){
		this.minLimitR = value;
	}
	
	// retorna o limite inferior parte real
	public String getMinLimitR()
	{
		return minLimitR;
	}	
	
	
	// seta o limite max do arco parte imaginaria
	public void setMaxLimitI(String value){
		this.maxLimitI = value;
	}
	
	// retorna o limite maximo parte imagin�ria
	public String getMaxLimitI()
	{
		return maxLimitI;
	}
	
	// seta o limite inferior do arco
	public void setMinLimitI(String value){
		this.minLimitI = value;
	}
	
	// retorna o limite inferior
	public String getMinLimitI()
	{
		return minLimitI;
	}	
	
	
	// retorna o label do arco
	public String toString()
	{
		String out =  label + "|" + this.getSourceNode().getLabel() + "-->" + this.getDestinationNode().getLabel();
		return out;
	}
}
