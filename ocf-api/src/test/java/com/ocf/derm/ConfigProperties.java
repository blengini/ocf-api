
package com.ocf.derm;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;


/**
 * Classe respons�vel pelo acesso ao arquivo de configura��es.
 * 
 * @author Adolfo Blengini Neto
 * 
 * */
public class ConfigProperties
{
	private static ConfigProperties reference= null;	
	private Properties pr = null;
	
	public static final String CLASS_NAME =  "bd.driverName";
	public static final String URL = "input.bd";
	public static final String LOGIN =  "bd.login";
	public static final String PASSWORD =  "bd.password";
	public static final String URL_WS = "input.ws";

	//Tens�o da primeira itera��o
	public static final String VOLTAGE_V1 = "voltage.V1";
	
	//Tens�o na saida do alimentador
	public static final String VOLTAGE_FEEDER = "voltage.V0"; 

	//Toler�ncia do crit�rio de converg�ncia
	public static final String TOLERANCE = "iteration.tolerance";
			
	//Tipo da Fonte de Dados
	public static final String TYPE_INPUT = "input.file.type";
	
	//Endere�o quando fonte de dados = arquivo externo
	public static final String INPUT_FILE = "input.file";
	
	// Numero de motores
	public static final String ENGINES_AVAILABLE = "engine.available";

	

	/**
	 * ConfigPropertiesBO
	 * @return
	 */
	public static ConfigProperties getInstance()
	{
		if (reference == null)
		{
			reference = new ConfigProperties();
		}		
		return reference;
	}
	
	/**
	 * ConfigPropertiesBO
	 */
	private ConfigProperties()
	{
		pr = new Properties();
		try
		{
			pr.load(new FileInputStream("conf/flow.properties"));
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * ConfigPropertiesBO
	 */
	public  void reload()
	{
		pr = new Properties();
		try
		{
			pr.load(new FileInputStream("src/conf/flow.properties"));
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
		
		
	/**
	 * getDriverName
	 * @return
	 * @throws PropertieFileNotFoundException
	 */
	public String getDriverName()throws FileNotFoundException
	{
		if (pr.containsKey(CLASS_NAME))
		{
			return pr.getProperty(CLASS_NAME);
		}
		else
		{
			throw new FileNotFoundException(CLASS_NAME + "NotFound");
		}
		
		
	}
	
	/**
	 * getURL
	 * @return
	 * @throws PropertieFileNotFoundException
	 */
	public String getURL()throws FileNotFoundException
	{
		if (pr.containsKey(URL))
		{
			return pr.getProperty(URL);
		}
		else
		{
			throw new FileNotFoundException(URL + "NotFound");
		}
		
		
	}
	
	/**
	 * getURL
	 * @return
	 * @throws PropertieFileNotFoundException
	 */
	public String getURLWS()throws FileNotFoundException
	{
		if (pr.containsKey(URL_WS))
		{
			return pr.getProperty(URL_WS);
		}
		else
		{
			throw new FileNotFoundException(URL_WS + "NotFound");
		}
		
		
	}
	
	
	/**
	 * getLogin
	 * @return
	 * @throws PropertieFileNotFoundException
	 */
	public String getLogin()throws FileNotFoundException
	{
		if (pr.containsKey(LOGIN))
		{
			return pr.getProperty(LOGIN);
		}
		else
		{
			throw new FileNotFoundException(LOGIN + "NotFound");
		}	
		
	}
	
	
	/**
	 * getPassword
	 * @return
	 * @throws PropertieFileNotFoundException
	 */
	public String getPassword(String type)throws FileNotFoundException
	{
		if (pr.containsKey(PASSWORD))
		{
			return pr.getProperty(PASSWORD);
		}
		else
		{
			throw new FileNotFoundException(PASSWORD + "NotFound");
		}
	}
	
	/**
	 * getPassword
	 * @return
	 * @throws PropertieFileNotFoundException
	 */
	public String getVolatgeInitial()throws FileNotFoundException
	{
		if (pr.containsKey(VOLTAGE_V1))
		{
			return pr.getProperty(VOLTAGE_V1);
		}
		else
		{
			throw new FileNotFoundException(VOLTAGE_V1 + "NotFound");
		}
		
		
	}
	
	
	/**
	 * VOLTAGE_FEEDER
	 * @return
	 * @throws FileNotFoundException
	 */
	public String getVoltageFeeder()throws FileNotFoundException
	{
		if (pr.containsKey(VOLTAGE_FEEDER))
		{
			return pr.getProperty(VOLTAGE_FEEDER);
		}
		else
		{
			throw new FileNotFoundException(VOLTAGE_FEEDER + "NotFound");
		}
		
		
	}
	
	/**
	 * getTolerance
	 * @return
	 * @throws PropertieFileNotFoundException
	 */
	public String getTolerance()throws FileNotFoundException
	{
		if (pr.containsKey(TOLERANCE))
		{
			return pr.getProperty(TOLERANCE);
		}
		else
		{
			throw new FileNotFoundException(TOLERANCE + "NotFound");
		}
		
		
	}
	
	/**
	 * getInputType
	 * @return
	 * @throws PropertieFileNotFoundException
	 */
	public String getInputType()throws FileNotFoundException
	{
		if (pr.containsKey(TYPE_INPUT))
		{
			return pr.getProperty(TYPE_INPUT);
		}
		else
		{
			throw new FileNotFoundException(TYPE_INPUT + "NotFound");
		}
		
		
	}
	
	/**
	 * getEnginesAvailable
	 * @return
	 * @throws PropertieFileNotFoundException
	 */
	public String getEnginesAvailable()throws FileNotFoundException
	{
		if (pr.containsKey(ENGINES_AVAILABLE))
		{
			return pr.getProperty(ENGINES_AVAILABLE);
		}
		else
		{
			throw new FileNotFoundException(ENGINES_AVAILABLE + "NotFound");
		}
		
		
	}
	
	/**
	 * getInputType
	 * @return
	 * @throws PropertieFileNotFoundException
	 */
	public String getInputFile()throws FileNotFoundException
	{
		if (pr.containsKey(INPUT_FILE))
		{
			return pr.getProperty(INPUT_FILE);
		}
		else
		{
			throw new FileNotFoundException(INPUT_FILE + "NotFound");
		}
		
	}
	
	/**
	 * getEngine
	 * @return
	 * @throws PropertieFileNotFoundException
	 */
	public String getEngine(String type)throws FileNotFoundException
	{
		if (pr.containsKey("engine." + type))
		{
			return pr.getProperty("engine." + type);
		}
		else
		{
			throw new FileNotFoundException("engine." + type + "NotFound");
		}
	}
	
	
}
