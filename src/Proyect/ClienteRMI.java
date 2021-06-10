/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Proyect;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author anita
 */
public interface ClienteRMI extends Remote{
    

    public void obtenerUsuarios() throws RemoteException;
    public void mensajeServidor(String message) throws RemoteException;
    public void cargarLista(String[] currentUsers) throws RemoteException;
    

//
    byte[] getFile(String name) throws RemoteException;


}
