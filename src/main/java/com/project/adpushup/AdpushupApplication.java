package com.project.adpushup;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.project.adpushup.reader.DataFileReader;
@EnableAutoConfiguration
@SpringBootApplication
public class AdpushupApplication {
	@Autowired
	DataFileReader dataFileReader;
	public static void main(String[] args) {
		SpringApplication.run(AdpushupApplication.class, args);
		//DataFileReader dataFileReader=new DataFileReader();
		
	}
	
	@PostConstruct
	public void init() 
	{
		try {
			dataFileReader.readFiles();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
