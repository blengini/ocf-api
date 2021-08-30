package com.ocf.api.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ocf.api.to.Edge;
import com.ocf.api.to.Feeder;


@RestController
@RequestMapping("/feederInfo")
public class FeederInfoController {

	
	@GetMapping
	public String getAlive()
	{
		return "Hi i'm alive!";
	}
	
	
	@PostMapping
	public String execute(@RequestAttribute Feeder feeder)
	{
			

		Map<String, String> obj =  new HashMap<String, String>();
		
		// ------------------ // get Edges // ----------------- 
		List<Edge> edges = feeder.getEdges();
		  
		obj.put("Feeder",feeder.getLabel()); 
		obj.put("Branches",Integer.toString(feeder.getEdges().size())); 
		obj.put("Nodes", Integer.toString(feeder.getSizeNodes()));
		obj.put("Sources",Integer.toString(feeder.getGenerationNodesSize()));
		obj.put("Meshed",Boolean.toString(feeder.isMesh()));
		  
		System.out.println("Alimentador --> " + feeder.getLabel());
		System.out.println("Numero de Arcos --> " + feeder.getEdges().size());
		System.out.println("Numero de Barras --> " + feeder.getSizeNodes());
		System.out.println("Numero de Fontes --> " +
		feeder.getGenerationNodesSize());
		System.out.println("Alimentador possui anel ? --> " + feeder.isMesh());
		  
		for (Edge edge: edges) { System.out.println("Arco --> " + edge.toString()); }
		 
		
		String jsonText = JSONValue.toJSONString(obj); 
		System.out.print(jsonText);
		return jsonText;
	}
}
