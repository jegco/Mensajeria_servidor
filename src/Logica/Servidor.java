/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Logica;

import Interfaces.Mensaje;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author j
 */
public class Servidor implements Interfaces.Interface_servidor{

    private Map<Integer,String> sesion_nombre=new HashMap<>();
    private Map<String,Integer> nombre_sesion=new HashMap<>();
    private Map<Integer,List<Integer>> contactos= new HashMap<>();
    private final Map<Integer,List<Mensaje>> buffer= new HashMap<Integer, List<Mensaje>>();
    
    @Override
    public int autenticar(String nombre) throws RemoteException {
        int sesion_usuario=getSesion();
        
        sesion_nombre.put(sesion_usuario, nombre);
        nombre_sesion.put(nombre,sesion_usuario);
        
        return sesion_usuario;
    }
    
    

    @Override
    public int agregar(String nombre, int sesion) throws RemoteException {
        if(!sesion_nombre.containsKey(sesion)){
            throw new RuntimeException("sesion invalida");
        }
        if(!nombre_sesion.containsKey(nombre)){
            throw new RuntimeException(nombre+" no esta conectado");
        }
        System.out.println(nombre_sesion.get(nombre));
        List<Integer> mis_contactos=contactos.get(sesion);
        
        if(mis_contactos==null){
            mis_contactos=new LinkedList<>();
            contactos.put(sesion,mis_contactos);
        }
        mis_contactos.add(nombre_sesion.get(nombre));
        
        return nombre_sesion.get(nombre);
    }

    @Override
    public void enviar(String mensaje, int sesion_destino, int sesion_fuente) throws RemoteException {
        if(!sesion_nombre.containsKey(sesion_destino)) throw new RuntimeException("sesion invalida");
        if(!sesion_nombre.containsKey(sesion_fuente)) throw new RuntimeException("contacto no esta conectado");
        if(!contactos.get(sesion_destino).contains(sesion_fuente)) throw new RuntimeException(sesion_nombre.get(sesion_fuente)+
                " no es parte de tus contactos");
        List<Mensaje> mensajes=buffer.get(sesion_destino);
        if(mensajes==null){
            mensajes=new LinkedList<>();
            buffer.put(sesion_destino, mensajes);
        }
        
        mensajes.add(new Mensaje(mensaje, sesion_nombre.get(sesion_fuente)));
        System.out.println("mensaje guardado: "+mensajes.get(0).getCuerpo()+" de: "+mensajes.get(0).getRemitente());
    }

    @Override
    public List<Mensaje> recibir(int sesion) throws RemoteException {
        if(!sesion_nombre.containsKey(sesion)) throw new RuntimeException("sesion invalida");
        
        return buffer.get(sesion);
    }
    private static int sesion=new Random().nextInt();
    
    public static int getSesion(){
        return ++sesion;
    }
    
    @Override
    public void limpiar_buffer(int sesion) throws RemoteException{
        if(!sesion_nombre.containsKey(sesion)) throw new RuntimeException("sesion invalida");
        buffer.get(sesion).clear();
    }
}
