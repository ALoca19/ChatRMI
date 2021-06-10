/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Proyect;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.MulticastSocket;
import java.rmi.ConnectException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import Proyect.ServidorRMI;

/**
 *
 * @author anita
 */
public class ClienteObjetoRemoto extends UnicastRemoteObject implements ClienteRMI{

    private static final long serialVersionUID = 7468891722773409712L;
    Chat chatGUI;
    private String hostName = "localhost";
    private String serviceName = "GroupChatService";
    private String clientServiceName;
    private String name;
    protected ServidorRMI serverIF;
    protected boolean connectionProblem = false;
        
    public ClienteObjetoRemoto(Chat aChatGUI, String userName) throws RemoteException {
        super();
        this.chatGUI = aChatGUI;
	this.name = userName;
	this.clientServiceName = "ClientListenService_" + userName;
    }

    	public void iniciarCliente() throws RemoteException {		
		String[] details = {name, hostName, clientServiceName};	

		try {
			Naming.rebind("rmi://" + hostName + "/" + clientServiceName, this);
			serverIF = ( ServidorRMI )Naming.lookup("rmi://" + hostName + "/" + serviceName);	
		} 
		catch (ConnectException  e) {
			JOptionPane.showMessageDialog(
					chatGUI.areaChat, "El servidor ah sido invalidado\nIntentelo mas tarde",
					"Problemas de Conexion", JOptionPane.ERROR_MESSAGE);
			connectionProblem = true;
			e.printStackTrace();
		}
		catch(NotBoundException | MalformedURLException me){
			connectionProblem = true;
			me.printStackTrace();
		}
		if(!connectionProblem){
			registroConServidor(details);
		}	
		System.out.println("Cliente Activo...\n");
	}
        
        public void registroConServidor(String[] details) {		
		try{
			serverIF.registroServidor(details);	
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}



    @Override
    public void obtenerUsuarios() throws RemoteException {
        
        System.out.println("Hola");
    }

    @Override
    public void mensajeServidor(String message) throws RemoteException {
        System.out.println( message );
	chatGUI.areaChat.append( message );
	chatGUI.areaChat.setCaretPosition(chatGUI.areaChat.getDocument().getLength());
	
    }

    @Override
    public void cargarLista(String[] currentUsers) throws RemoteException {
        if(currentUsers.length < 2){
			chatGUI.btnInbox.setEnabled(false);
                        chatGUI.btnBuscarArchivo.setEnabled(false);
		}
        
        chatGUI.modeloLista.clear();
	chatGUI.actualizarListaUsuarios(currentUsers);

        

    }

    @Override
    public byte[] getFile(String name) throws RemoteException {
    
        String PATH = "ServidorArchivos/";
        
        byte[] filebytes = null;
        String path = PATH + name;
        FileInputStream fileInput = null;
        BufferedInputStream bufferedInput = null;
        try {
            File file = new File(path);
            fileInput = new FileInputStream(file);
            bufferedInput = new BufferedInputStream(fileInput);
            filebytes = new byte[(int) file.length()];
            bufferedInput.read(filebytes, 0, filebytes.length);

        } catch (FileNotFoundException e) {
//            throw e;
            return null;

        } catch (IOException ex) {
//            throw ex;
            return null;
        }
        return filebytes;
    }

 


    
    
}
