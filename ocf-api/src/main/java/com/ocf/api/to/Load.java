package com.ocf.api.to;

import java.math.BigDecimal;

import com.ocf.api.util.Complex;

/**
 * Super Classe responsavel pela representacao de cargas, dos tipos de potencia
 * constante, corrente constante e impedancia constante.
 * 
 * @author ablengini
 *
 */
public abstract class Load {
	private Complex value;

	public Complex getValue() {
		return value;
	}

	public void setValue(Complex value) {
		this.value = value;
	}

	public Complex getInstantCurrent(Complex v) {

		// Por default a carga � de potencia constante.

		Complex i = new Complex(new BigDecimal(0), new BigDecimal(0));

		// set ha carga no no
		if (getValue() != null) {
			// i* = S/V
			i = getValue().div(v);
		}
		return i.getConjugado();
	}
}
