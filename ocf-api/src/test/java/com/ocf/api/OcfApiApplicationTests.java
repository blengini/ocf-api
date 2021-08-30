package com.ocf.api;

import java.io.FileNotFoundException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.ocf.api.to.Feeder;
import com.ocf.api.to.Network;
import com.ocf.derm.ConfigProperties;
import com.ocf.derm.NetworkBO;



@SpringBootTest
@AutoConfigureMockMvc
class OcfApiApplicationTests {
	
    @Autowired
    MockMvc mockMvc;
    

	@Test
	void contextLoads() throws Exception 
	{
		
		///Map<String, Feeder> obj=new HashMap<String, Feeder>();   
		//obj.put("A", feeder);		
		//String jsonText = JSONValue.toJSONString(obj); 
		//System.out.print(jsonText);
		ConfigProperties conf = ConfigProperties.getInstance();			
		
		try 
		{			
			System.out.println("--> Tipo de Fonte de dados configurada :" + conf.getInputType());
			System.out.println("--> Quantidade de Metodos disponiveis : " + conf.getEnginesAvailable());
			System.out.println("--> Tolerancia de convergencia : " + conf.getTolerance());
			System.out.println("--> Tensao saida do alimentador (pu) : " + conf.getVoltageFeeder());
			System.out.println("--> Tensao primeira iteracao (pu) : " + conf.getVolatgeInitial());
			
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
	
			
		Network network = new NetworkBO().buildNetwork(conf);		
		Feeder feederA = network.getFeeder("A");
		
		ResultActions resultActions = mockMvc.perform(
				MockMvcRequestBuilders.post("/bfs")
					.requestAttr("feeder", feederA)
			);
			resultActions.
				andExpect(MockMvcResultMatchers.status().isOk());
			System.out
				.println("response:" + resultActions.andReturn().getResponse().getContentAsString());				
	}
}
