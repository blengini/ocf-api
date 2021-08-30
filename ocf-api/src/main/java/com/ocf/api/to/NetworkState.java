package com.ocf.api.to;

import java.math.BigDecimal;
import java.util.LinkedHashMap;

import com.ocf.api.util.Complex;

/**
 * Classe responsavel pela itera��o, armazenado as correntes, tensoes , quedas
 * de tensao e numero da iteracao
 * 
 * @author ablengini
 *
 */
public class NetworkState {

	

	private LinkedHashMap<String, Complex> currents = new LinkedHashMap<>();

	private LinkedHashMap<String, Complex> voltages = new LinkedHashMap<>();

	private LinkedHashMap<String, Complex> drooVoltages = new LinkedHashMap<>();

	// idetificador da iteracao ou estado da rede
	private int index;

	//tempo de execu��o
	String time = "";
	
	//converg�ncia
	Double convergence;

	public void setCurrent(String idNode, Complex i) {
		currents.put(idNode, i);
	}

	public void addCurrent(String idNode, Complex i) {
		Complex oldi = getCurrent(idNode);
		if (oldi == null) {
			currents.put(idNode, i);
			oldi = new Complex();
		}
		Complex newi = i.add(oldi);
		currents.put(idNode, newi);
	}

	public Complex getCurrent(String idNode) {
		return currents.get(idNode);
	}

	public void addVoltage(String idNode, Complex v) {
		voltages.put(idNode, v);
	}

	public void addDropVoltage(String idNode, Complex v) {
		drooVoltages.put(idNode, v);
	}

	public Complex getVoltage(String idNode) {
		return voltages.get(idNode);
	}

	public Complex getDropVoltage(String idNode) {
		return drooVoltages.get(idNode);
	}

	public NetworkState(int index) {
		this.index = index;
	}

	public NetworkState() {

	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public Double getConvergence() {
		return convergence;
	}

	public void setConvergence(Double convergence) {
		this.convergence = convergence;
	}
	
	// ------------------------
	// Printa Matriz com as correntes
	// -----------------------
	@SuppressWarnings("deprecation")
	public void toStringCurrents() {

		System.out.println("------------------------------------------------");

		for (String key : currents.keySet()) {
			Complex i = currents.get(key);

			System.out.println("Arco	" + key + "	Ir	"
					+ i.getReal().setScale(6, BigDecimal.ROUND_CEILING).toEngineeringString().replace(".", ",")
					+ "	Ii	"
					+ i.getImaginary().setScale(6, BigDecimal.ROUND_CEILING).toEngineeringString().replace(".", ","));

		}

	}

	// ------------------------
	// Printa Matriz com a tensao e queda
	// -----------------------
	@SuppressWarnings("deprecation")
	public void toStringLowVoltage(Network network, String feeder) {

		System.out.println("--------------------------------------------------");

		Edge lowvolltage = network.getLowVoltage(feeder);

		for (String key : voltages.keySet()) {

			Complex v = voltages.get(key);

			System.out.println("Arco	" + key + "	Vr	"
					+ v.getReal().setScale(6, BigDecimal.ROUND_CEILING).toEngineeringString().replace(".", ",")
					+ "	Vi	"
					+ v.getImaginary().setScale(6, BigDecimal.ROUND_CEILING).toEngineeringString().replace(".", ",")
					+ "	Modulo(pu)	"
					+ v.getModule().setScale(6, BigDecimal.ROUND_CEILING).toEngineeringString().replace(".", ","));

			if (lowvolltage == null) {
				lowvolltage = new Edge(key);
				lowvolltage.setVoltage(v);
			}

			// Calcula o arco com menor tens�o
			Complex vlow = lowvolltage.getVoltage();

			if (vlow.getModule().compareTo(v.getModule()) == 1) {
				lowvolltage.setVoltage(v);
				lowvolltage.setLabel(key);
			}

			network.addLowVoltage(feeder, lowvolltage);
		}

	}

	// ------------------------
	// Printa Matriz com a tensao
	// -----------------------
	@SuppressWarnings("deprecation")
	public void toStringVoltage() {

		System.out.println("--------------------------------------------------");

		for (String key : voltages.keySet()) {

			Complex v = voltages.get(key);

			System.out.println("Arco " + key + "	Vr	"
					+ v.getReal().setScale(6, BigDecimal.ROUND_CEILING).toEngineeringString().replace(".", ",")
					+ "	Vi	"
					+ v.getImaginary().setScale(6, BigDecimal.ROUND_CEILING).toEngineeringString().replace(".", ",")
					+ "	Modulo(pu)	"
					+ v.getModule().setScale(6, BigDecimal.ROUND_CEILING).toEngineeringString().replace(".", ","));

		}

	}

	// ------------------------
	// Printa Matriz com a tensao e queda
	// -----------------------
	@SuppressWarnings("deprecation")
	public void toStringLowVoltage(Network network) {
		System.out.println("--------------------------------------------------");
		System.out.println("");
		System.out.println("      RESULTADO MENOR TENS�O NAS BARRAS");
		System.out.println("");
		
		for (String key : network.getLowVoltages().keySet()) 
		{			
			System.out.println(" Alimentador: " + key);
			System.out.println(" Arco com menor tens�o: " + network.getLowVoltage(key).getLabel());
			System.out.println(" M�dulo da tens�o:" + network.getLowVoltage(key).getVoltage().getModule().setScale(6, BigDecimal.ROUND_CEILING));
							
		}
		System.out.println("");
		System.out.println("--------------------------------------------------");		
	}
	
	public int getCurrentsEdges()
	{
		return currents.size();
	}
}
