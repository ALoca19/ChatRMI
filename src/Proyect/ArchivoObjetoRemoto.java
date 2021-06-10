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
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author anita
 */
public class ArchivoObjetoRemoto extends UnicastRemoteObject implements ArchivoRMI{
    
    

    public ArchivoObjetoRemoto() throws RemoteException {
        super();
    }

    @Override
    public byte[] getFile(String name) throws RemoteException {
    
        //String PATH = "Cliente_"+name+"/";
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
