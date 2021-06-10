/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Proyect;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 *
 * @author anita
 */
public class Servidor {
    
        public String name;
	public ClienteRMI client;
	
	//constructor
	public Servidor(String name, ClienteRMI client){
		this.name = name;
		this.client = client;
	}

	
	//getters and setters
	public String getName(){
		return name;
	}
        
	public ClienteRMI getClient(){
		return client;
	}
}
