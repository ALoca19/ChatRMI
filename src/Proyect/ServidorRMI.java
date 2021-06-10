/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Proyect;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.RemoteRef;

/**
 *
 * @author anita
 */
public interface ServidorRMI extends Remote{

    public void cargarChat(String userName, String chatMessage)throws RemoteException;
    public void registroServidor(String[] details)throws RemoteException;

    //
    public void pasarArchivosAUsuarios(String nombreArchivo, String usuario) throws RemoteException;
    public void mandarPrivadoInbox(int[] privado, String privateMessage) throws RemoteException;
    public void mandarPrivadoInboxArchivo(int[] privado, String privateMessage, String Archivo) throws RemoteException;
    public void salirDelChat(String nombre) throws RemoteException;
    public boolean validarNombreUsuario(String nombreARegistrar) throws RemoteException;
}
