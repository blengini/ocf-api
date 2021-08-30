package com.ocf.api.to;

import java.math.BigDecimal;

import com.ocf.api.util.Complex;

/**
 * Classe responsavel pela representacao de uma carga para potencia constante P
 * 
 * @author ablengini
 *
 */
public class LoadPowerConstant extends Load {

	public LoadPowerConstant(Complex value) {
		super.setValue(value);
	}
	
	@Override
	public Complex getInstantCurrent(Complex v)
	{		
		Complex i = new Complex(new BigDecimal(0),new BigDecimal(0));
		
		// set ha carga no no
		if (getValue()!= null)
		{
			// i* = S/V		
			i = getValue().div(v);
		}
		return i.getConjugado();
	}
}
