package com.ocf.api.to;

import java.math.BigDecimal;

import com.ocf.api.util.Complex;

public class LoadImpedanceConstant extends Load {

	public LoadImpedanceConstant(Complex value) {
		super.setValue(value);
	}

	@Override
	public Complex getInstantCurrent(Complex v) {

		Complex i = new Complex(new BigDecimal(0), new BigDecimal(0));

		// set ha carga no no
		if (getValue() != null) {			
			// i = V/Z
			i = v.div(getValue());
		}
		
		return i;
	}

}
