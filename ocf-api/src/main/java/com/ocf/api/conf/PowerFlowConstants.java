package com.ocf.api.conf;

import java.math.BigDecimal;

import com.ocf.api.util.Complex;


/**
 * Interface responsavel pelas constantes pelo calculo de fluxo.
 * 
 * @author ablengini
 *
 */
public interface PowerFlowConstants {

	// Tolerancia para convergencia da iteracao
	public static final BigDecimal TOLERANCE = new BigDecimal("0.00001");
	
	// Tensao na saida do alimentador 
	public static final Complex Vi = new Complex(new BigDecimal("1.00"), new BigDecimal(0));
	
	// Tensao atribuida na primeira itera��o 
	public static final Complex V1 = new Complex(new BigDecimal("0.999"), new BigDecimal(0));
	
	// Configura��o do LOG
	public static final boolean DEBUG = true;
}
