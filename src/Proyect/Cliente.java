/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Proyect;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 *
 * @author anita
 */
public class Cliente {
    
    public static void main(String[] args) throws RemoteException, NotBoundException {
        
        
     ClienteObjetoRemoto chatClient = null;
//        //Buscar el registro del objeto remotor en el servidor
//        Registry reg = LocateRegistry.getRegistry("127.0.0.1",1099);
//        
//        //definir una referencia en el cliente para el objeto remoto
//        ClienteRMI objr;
//        
//        objr = (ClienteRMI) reg.lookup("OBJ1");
        
        InicioUsuario calcu = new InicioUsuario(chatClient); //Inicializamos los componentes de la calculadora
        calcu.setVisible(true);
    }

}
