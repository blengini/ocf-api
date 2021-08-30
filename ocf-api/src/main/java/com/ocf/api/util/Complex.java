package com.ocf.api.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Classe de representacao de um numero complexo com operacoes de soma e
 * subtracao
 * 
 * @author ablengini
 *
 * */
public class Complex {

	// parte real
	private BigDecimal real;

	// parte imaginaria
	private BigDecimal imaginary;

	// Inicializa com zeros as duas partes
	public Complex() {
		this(new BigDecimal(0.0),new BigDecimal(0.0));
	}

	// Inicializa parte real e parte imaginaria com zero
	public Complex(BigDecimal r) {
		this(r, new BigDecimal(0.0));
	}

	// Inicializa parte real e imaginaria
	public Complex(BigDecimal r, BigDecimal i) {
		real = r;
		imaginary = i;
	}

	// Inicializa parte real e imaginaria com Strings
	public Complex(String r, String i) {

		if (r != null && !r.equals("")) {
			r = r.replace(',', '.');
			real = new BigDecimal(r);
		}
		if (i != null && !i.equals("")) {
			i =  i.replace(',', '.');
			imaginary = new BigDecimal(i);
		}
	}

	// retorna parte real
	public BigDecimal getReal() {
		return real;
	}

	// retirna parte imaginaria
	public BigDecimal getImaginary() {
		return imaginary;
	}

	// Soma dois numeros complexos
	public Complex add(Complex right) {
		return new Complex(real.add(right.real) , imaginary.add(right.imaginary));
	}

	// Subtrai dois numberos complexos
	public Complex subtract(Complex right) {
		return new Complex(real.subtract(right.real), imaginary.subtract(right.imaginary));
	}

	// Retorna a representa��o String do numero complexo
	public String toString() {
		return "(" + real + ", " + imaginary + ")";
	}
	
	// multiplicacao
	public Complex multiply(Complex rigth)
	{
		// parte real
		BigDecimal r = (real.multiply(rigth.getReal())).subtract(imaginary.multiply(rigth.getImaginary()));
		
		// parte imaginaria		
		BigDecimal i  = rigth.getReal().multiply(imaginary).add(real.multiply(rigth.imaginary));
		
		return new Complex(r,i);
	}
	
	
	// divis�o
	public Complex div(Complex right)
	{
		BigDecimal r =  null;
		BigDecimal i =  null;
		
		// parte real
		if (imaginary.compareTo(new BigDecimal(0.0)) < 0  && right.getImaginary().compareTo(new BigDecimal(0.0)) < 0)
		{
			r = (real.multiply(right.getReal())).subtract(imaginary.multiply(right.getImaginary()));
			i  = right.getReal().multiply(imaginary).add(real.multiply(right.imaginary));
		}
		else
		{
			r = (real.multiply(right.getReal())).add(imaginary.multiply(right.getImaginary()));
			i  = right.getReal().multiply(imaginary).subtract(real.multiply(right.imaginary));
		}
	
		
		// denominador
		BigDecimal denominador = right.getReal().multiply(right.getReal()).add(right.getImaginary().multiply(right.getImaginary()));
		
		return new Complex(r.divide(denominador, 8, RoundingMode.CEILING), i.divide(denominador, 8, RoundingMode.CEILING));		
	}
	
	// retorna o conjugado
	public Complex getConjugado()
	{
		return new Complex(real, imaginary.multiply(new BigDecimal(-1)));
	}
	
	// retorna o modulo
	public BigDecimal getModule()
	{		
		BigDecimal r = real.pow(2);
		BigDecimal i = imaginary.pow(2);				
		BigDecimal result = r.add(i);		
		double dresult = Math.pow(result.doubleValue(), 0.5);		
		return new BigDecimal(dresult);
	}
	
	public static void main(String[] args)
	{
		Complex a =  new Complex(new BigDecimal(0.997282), new BigDecimal(-0.001432));
		BigDecimal module = a.getModule();
		module.toString();
	}
}
