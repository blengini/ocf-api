package com.ocf.api.engine;

import java.util.SortedSet;

import com.ocf.api.to.Network;
import com.ocf.api.to.NetworkState;

/**
 * Interface padr�o para os motores implementados.
 * 
 * @date   06/10/2016
 * @author Adolfo Blengini Neto
 * @author Marcius Fabius Henriques de Carvallho.
 * @author Lia Mota
 * @version 1.0
 *
 */
public interface Engines {
	
	/**
	 * M�todo principal do motor de calculo.
	 * 
	 * @param feeder label dos alimentadores selecionados pelo usu�rio.
	 * @param network rede completa
	 * @return 
	 */
	public NetworkState execute(SortedSet<String> feeders, Network network);
	
	/**
	 * M�todo respons�vel por apresentar os resultados
	 */
	public void printResults();

}
