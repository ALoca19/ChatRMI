/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Proyect;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.RemoteRef;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author anita
 */
public class ServidorObjetoRemoto extends UnicastRemoteObject implements ServidorRMI{

    
    private Vector<Servidor> servidores;
    private static final long serialVersionUID = 1L;
    Chat chatGUI;
	
	//Constructor
	public ServidorObjetoRemoto() throws RemoteException {
		super();
		servidores = new Vector<Servidor>(10, 1);
	}
        
        public static void main(String[] args) {
		
                try{
                java.rmi.registry.LocateRegistry.createRegistry(1099);
		String hostName = "localhost";
		String serviceName = "GroupChatService";
		
		if(args.length == 2){
			hostName = args[0];
			serviceName = args[1];
		}
		
		
			ServidorRMI hello = new ServidorObjetoRemoto();
			Naming.rebind("rmi://" + hostName + "/" + serviceName, hello);
			System.out.println("El servidor esta activo...");
		}
		catch(Exception e){
			System.out.println("El servidor tiene problemas para iniciar");
		}	
	}
        
    public void mandarATodos(String newMessage){	
		for(Servidor c : servidores){
			try {
                            
                            c.getClient().mensajeServidor(newMessage);
				
			} 
                        catch (RemoteException e) {
				e.printStackTrace();
			}
		}	
	}
    
    
    public boolean buscarUsuario(String usuario)
    {
        String[] currentUsers = obtenerLista();	
        for(int i=0; i<servidores.size(); i++)
        {
            if(usuario.equals(servidores.get(i).getName()))
            {
                return true;
            }
        }
        
        return false;
    }
    
    private void cargarListaUsuarios() {
		String[] currentUsers = obtenerLista();	
		for(Servidor c : servidores){
			try {
				c.getClient().cargarLista(currentUsers);
			} 
			catch (RemoteException e) {
				e.printStackTrace();
			}
		}	
	}
    
    public String[] obtenerLista(){
		String[] allUsers = new String[servidores.size()];
		for(int i = 0; i< allUsers.length; i++){
			allUsers[i] = servidores.elementAt(i).getName();
		}
		return allUsers;
	}
   
			
    @Override
    public void cargarChat(String userName, String chatMessage) throws RemoteException {
            String message =  userName + " : " + chatMessage + "\n";
		mandarATodos(message);
    }


    @Override
    public void registroServidor(String[] details) throws RemoteException {
                try{
                   
                    
                        ClienteRMI nextClient = ( ClienteRMI )Naming.lookup("rmi://" + details[1] + "/" + details[2]);
			
			servidores.addElement(new Servidor(details[0], nextClient));
			
			nextClient.mensajeServidor("[Servidor] : Hello! " + details[0] + " bienvenido al chat.\n");
			
			mandarATodos("[Servidor] : " + details[0] + " se ha unido al grupo.\n");
			
                        cargarListaUsuarios();
                    
					
		}
		catch(RemoteException | MalformedURLException | NotBoundException e){
			e.printStackTrace();
		}
    }

    ////////
    @Override
    public void pasarArchivosAUsuarios(String nombreArchivo, String usuario) {
        
        try {
            String mensaje = usuario+" : "+" Mando el archivo -> "+nombreArchivo+"\n";
            mandarATodos(mensaje);
            String[] usuariosConectados=obtenerLista();

            ClienteRMI fileManager = ( ClienteRMI )Naming.lookup("rmi://localhost/ClientListenService_" + usuario);
            byte[]  fileBytes = fileManager.getFile(nombreArchivo);
            
            for(int i=0; i<usuariosConectados.length; i++)
            {
                if (fileBytes != null) {

                File file = new File("Clientes/Cliente_"+servidores.elementAt(i).getName());

                //Verificar o crear el archivo
                if(!file.exists())
                {
                    if(file.mkdirs())
                    {
                        //se crea el archivo
                    }
                }
                
                file = new File("Clientes/Cliente_"+servidores.elementAt(i).getName()+"/"+nombreArchivo);
                FileOutputStream fileOutput = new FileOutputStream(file);
                BufferedOutputStream bufferedOutput = new BufferedOutputStream(fileOutput);
                bufferedOutput.write(fileBytes, 0, fileBytes.length);
                bufferedOutput.flush();
                fileOutput.close();
                bufferedOutput.close();
                }
                else
                {
                    mensaje="Error al mandar el archivo: "+nombreArchivo+" al usuario "+servidores.elementAt(i).getName();
                    mandarATodos(mensaje);
                }
                
            }
            

        } catch (NotBoundException ex) {
            Logger.getLogger(ServidorObjetoRemoto.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(ServidorObjetoRemoto.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            Logger.getLogger(ServidorObjetoRemoto.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ServidorObjetoRemoto.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ServidorObjetoRemoto.class.getName()).log(Level.SEVERE, null, ex);
        }
        

    
    
    }

    @Override
    public void mandarPrivadoInbox(int[] privado, String privateMessage) throws RemoteException {
    
        Servidor pc;
	for(int i : privado){
            pc= servidores.elementAt(i);
            pc.getClient().mensajeServidor(privateMessage);
	}
        
    }

    @Override
    public void salirDelChat(String nombre) throws RemoteException {
        
        for(Servidor c : servidores){
			if(c.getName().equals(nombre)){
//				System.out.println(line + nombre + " abandono la sesion");
//				System.out.println(new Date(System.currentTimeMillis()));
				servidores.remove(c);
				break;
			}
		}		
		if(!servidores.isEmpty()){
			cargarListaUsuarios();
		}
    
    }

    @Override
    public boolean validarNombreUsuario(String nombreARegistrar) throws RemoteException {
            
        String[] usuariosUsados = obtenerLista();//
        int aux=0;
        
        
       
        for(int i=0; i<usuariosUsados.length; i++)
       {
           if(nombreARegistrar.equals(servidores.elementAt(i).getName()))
           {
               System.out.println("Usuario Existente");
               aux++;
           }
       }
        
        if(aux>=2)
        {
            JOptionPane.showMessageDialog(null, "Usuario Existente\n Favor de introducir otro nombre de usuario");
            return true;
        }
        else
        {
            return false;
        }
        
    
    }

    @Override
    public void mandarPrivadoInboxArchivo(int[] privado, String privateMessage, String Archivo) throws RemoteException {
    
        Servidor pc;
	
        
          try {
            
          
for(int i : privado){
    
            pc= servidores.elementAt(i);
            pc.getClient().mensajeServidor(privateMessage);
	
            ClienteRMI fileManager = ( ClienteRMI )Naming.lookup("rmi://localhost/ClientListenService_" + servidores.elementAt(i).getName());
            byte[]  fileBytes = fileManager.getFile(Archivo);
            
            
                if (fileBytes != null) {

                File file = new File("Clientes/Cliente_"+servidores.elementAt(i).getName());

                //Verificar o crear el archivo
                if(!file.exists())
                {
                    if(file.mkdirs())
                    {
                        //se crea el archivo
                    }
                }
                
                file = new File("Clientes/Cliente_"+servidores.elementAt(i).getName()+"/"+Archivo);
                FileOutputStream fileOutput = new FileOutputStream(file);
                BufferedOutputStream bufferedOutput = new BufferedOutputStream(fileOutput);
                bufferedOutput.write(fileBytes, 0, fileBytes.length);
                bufferedOutput.flush();
                fileOutput.close();
                bufferedOutput.close();
                }
                else
                {
                    mandarATodos("Error al mandar el archivo: "+Archivo+" al usuario "+servidores.elementAt(i).getName());
                }
                
          }
            

        } catch (NotBoundException ex) {
            Logger.getLogger(ServidorObjetoRemoto.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(ServidorObjetoRemoto.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            Logger.getLogger(ServidorObjetoRemoto.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ServidorObjetoRemoto.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ServidorObjetoRemoto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    

    
}
