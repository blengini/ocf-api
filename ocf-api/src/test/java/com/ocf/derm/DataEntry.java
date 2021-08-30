package com.ocf.derm;

import com.ocf.api.to.Network;

/**
 * Interface padr�o para acesso aos dados.
 * @author Adolfo
 *
 */
public interface DataEntry {
	
	/**
	 * Opera��o respons�vel pela leitura dos dados de entrada.
	 * @return Rede de distribui��o.
	 */
	public Network parser();

}
