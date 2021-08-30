/**
 * 
 */
package com.ocf.api.to;

import java.math.BigDecimal;

import com.ocf.api.util.Complex;

/**
 * @author ablengini
 *
 */
public class LoadAdmittanceConstant extends Load {

	/**
	 * 
	 */
	public LoadAdmittanceConstant(Complex value) {
		super.setValue(value);
	}
	

	@Override
	public Complex getInstantCurrent(Complex v) {
	
		Complex i = new Complex(new BigDecimal(0), new BigDecimal(0));

		// set ha carga no no
		if (getValue() != null) {

			// i = V*Y
			i = v.multiply(getValue());
		}
		
		return i;
	}

}
