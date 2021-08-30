package com.ocf.api.to;

import java.math.BigDecimal;

import com.ocf.api.util.Complex;

public class NetworkResult {
	
	private Complex[] currents =  null;

	private Complex[] voltages =  null;

	private Complex[] drooVoltages = null;

	// idetificador da iteracao ou estado da rede
	private int index;

	
	public void setCurrent(Integer idNode, Complex i) {
		currents[idNode] = i;
	}

	public void addCurrent(Integer idNode, Complex i) {
		Complex oldi = getCurrent(idNode);
		if (oldi == null) {
			currents [idNode] = i;
			oldi = new Complex();
		}
		Complex newi = i.add(oldi);
		currents[idNode] = newi;
	}

	public Complex getCurrent(Integer idNode) {
		return currents[idNode];
	}

	public void addVoltage(Integer idNode, Complex v) {
		voltages[idNode] = v;
	}

	public void addDropVoltage(Integer idNode, Complex v) {
		drooVoltages[idNode] = v;
	}

	public Complex getVoltage(Integer idNode) {
		return voltages[idNode];
	}

	public Complex getDropVoltage(Integer idNode) {
		return drooVoltages[idNode];
	}
	
	public Complex[] getVoltages()
	{
		return voltages;
	}

	public NetworkResult(int size, int index) {
		
		this.index = index;
		
		currents = new Complex[size];

		voltages = new Complex[size];

		drooVoltages = new Complex[size];
		
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	// ------------------------
	// Printa Matriz com as correntes
	// -----------------------
	@SuppressWarnings("deprecation")
	public void toStringCurrents() {

		System.out.println("------------------------------------------------");

		int index = 0;
		for (Complex i : currents) {
			
			if (i != null)
			{
				System.out.println("Arco	" + index + "	Ir	"
						+ i.getReal().setScale(6, BigDecimal.ROUND_CEILING).toEngineeringString().replace(".", ",")
						+ "	Ii	"
						+ i.getImaginary().setScale(6, BigDecimal.ROUND_CEILING).toEngineeringString().replace(".", ","));
				index++;
			}
			else
			{
				System.out.println("Arco	" + index + "	Ir	"
						+ "0,000000"
						+ "	Ii	"
						+ "0,00000");
				index++;
			}
		}

	}

	// ------------------------
	// Printa Matriz com a tensao e queda
	// -----------------------
	@SuppressWarnings("deprecation")
	public void toStringLowVoltage(Network network, String feeder) {

		System.out.println("--------------------------------------------------");

		Edge lowvolltage = network.getLowVoltage(feeder);

		int index = 0;
		for (Complex v : voltages) 
		{
			System.out.println("Arco	" + index + "	Vr	"
					+ v.getReal().setScale(6, BigDecimal.ROUND_CEILING).toEngineeringString().replace(".", ",")
					+ "	Vi	"
					+ v.getImaginary().setScale(6, BigDecimal.ROUND_CEILING).toEngineeringString().replace(".", ",")
					+ "	Modulo(pu)	"
					+ v.getModule().setScale(6, BigDecimal.ROUND_CEILING).toEngineeringString().replace(".", ","));

			if (lowvolltage == null) 
			{
				lowvolltage = new Edge(Integer.toString(index));
				lowvolltage.setVoltage(v);
			}

			// Calcula o arco com menor tens�o
			Complex vlow = lowvolltage.getVoltage();

			if (vlow.getModule().compareTo(v.getModule()) == 1) {
				lowvolltage.setVoltage(v);
				lowvolltage.setLabel(Integer.toString(index));
			}

			network.addLowVoltage(feeder, lowvolltage);
			
			index++;
		}

	}

	// ------------------------
	// Printa Matriz com a tensao
	// -----------------------
	@SuppressWarnings("deprecation")
	public void toStringVoltage() {

		System.out.println("--------------------------------------------------");

		int index = 0;
		for (Complex v : voltages) {

			
			System.out.println("Arco " + index + "	Vr	"
					+ v.getReal().setScale(6, BigDecimal.ROUND_CEILING).toEngineeringString().replace(".", ",")
					+ "	Vi	"
					+ v.getImaginary().setScale(6, BigDecimal.ROUND_CEILING).toEngineeringString().replace(".", ",")
					+ "	Modulo(pu)	"
					+ v.getModule().setScale(6, BigDecimal.ROUND_CEILING).toEngineeringString().replace(".", ","));

			index++;
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
	
	/**
	 * Convert para a estrutura mais nova de NetworkState
	 * @return
	 */
	public NetworkState convertToNetworkState()
	{
		NetworkState networkState =  new NetworkState();
		int index ;
		
		for (index = 0 ; index < currents.length; index++)
		{
			String sindex = Integer.toString(index);
			
			if (currents[index] != null)
			{
				networkState.addCurrent(sindex, currents[index]);
			}
			else
			{
				networkState.addCurrent(sindex, new Complex(new BigDecimal(0), new BigDecimal(0)));
			}
			
			if (voltages[index] != null)
			{
				networkState.addVoltage(sindex, voltages[index]);				
			}
			
			
		}
		
		return networkState;
	}

}
