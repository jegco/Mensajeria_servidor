/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Mensajeria_servidor;

import Interfaces.Interface_servidor;
import Interfaces.Util;
import Logica.Servidor;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author j
 */
public class Mensajeria_servidor {

    /**
     * @param args the command line arguments
     * @throws java.rmi.RemoteException
     */
    public static void main(String[] args) throws RemoteException, IOException, NotBoundException {
        Util.setCodebase(Interface_servidor.class);
        Servidor servidor=new Servidor();
        
        Interface_servidor remoto=(Interface_servidor)UnicastRemoteObject.exportObject(servidor, 8888);
        Registry registro=LocateRegistry.createRegistry(8888);
        registro.rebind("servidor mensajeria", remoto);
        System.out.println("entro al servidor");
        System.in.read();
        
        registro.unbind("servidor mensajeria");
        
        UnicastRemoteObject.unexportObject(servidor, true);
    }
    
}
