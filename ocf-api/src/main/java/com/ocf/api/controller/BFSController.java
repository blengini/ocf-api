package com.ocf.api.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ocf.api.to.Feeder;


@RestController
@RequestMapping("/bfs")
public class BFSController 
{
		
	@GetMapping
	public String getAlive()
	{
		return "Hi DERM i'm alive!";
	}
	
	
	@PostMapping
	public String execute(@RequestAttribute Feeder feeder)
	{
		System.out.println(feeder);
		return null;
	}
}
