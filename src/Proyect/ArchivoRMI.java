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
public interface ArchivoRMI extends Remote{
    
    byte[] getFile(String name) throws RemoteException;
}
