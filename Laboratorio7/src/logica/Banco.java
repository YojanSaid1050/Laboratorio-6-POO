package logica;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

import persistencia.ArchivoPlano;


public class Banco {
	private ArrayList<Cuenta> cuentas;
	private HashMap<Integer, Cliente> clientes;
	
	public ArrayList<Cuenta> getCuentas() {
		return cuentas;
	}

	public HashMap<Integer, Cliente> getClientes() {
		return clientes;
	}

	public Banco() {
		this.cuentas = new ArrayList<Cuenta>();
		this.clientes = new HashMap<Integer, Cliente>();
	}

	public void crearCliente(int id , String nombre, String apellido) throws Exception{
		if(this.clientes.containsKey(id)){
			throw new Exception("cliente repetido");
		}else {
			Cliente cliente = new Cliente(id, nombre, apellido);
			this.clientes.put(id, cliente);
		}
	}
	public void crearCuenta(int numero, int saldo, String tipo, int idCliente) throws Exception {
		if (!this.clientes.containsKey(idCliente)) {
			throw new Exception ("Cliente No existente");
		}else if(this.exiteCuenta(numero)){
			throw new Exception ("La cuenta ya existe");
		} else if(this.existeTipoCuenta(idCliente, tipo)) {
			throw new Exception ("El cliente ya tiene una cuenta de tipo : " + tipo);
		}else {
			Cuenta cuenta = new Cuenta(numero, saldo, tipo, this.clientes.get(idCliente));
			this.clientes.get(idCliente).getCuentas().put(numero, cuenta);
			this.cuentas.add(cuenta);
		}
	}
	
	private boolean existeTipoCuenta(int idCliente, String tipo) {
		for(Integer numero : this.clientes.get(idCliente).getCuentas().keySet()) {
			if(this.clientes.get(idCliente).getCuentas().get(numero).getTipo().equals(tipo)) {
				return true;
			}
		}
		return false;
	}

	public boolean exiteCuenta(int numero) {
		for(Cuenta cuenta : this.cuentas) {
			if(cuenta.getNumero() == numero) {
				return true;
			}
		}
		return false;
	}

	public void almacenar() {
		ArrayList<String> lineasClientes = new ArrayList<String>();
		for(Integer id : this.clientes.keySet()) {
			Cliente cliente = this.clientes.get(id);
			lineasClientes.add(cliente.getId() + ";" + cliente.getNombre() + ";" + cliente.getApellido());
		}
		ArchivoPlano.almacenar("clientes.csv", lineasClientes);
		//TODO almacenar cuentas
		
		ArrayList<String> lineasCuentas = new ArrayList<String>();
	    for(Cuenta cuenta : this.cuentas) {
	        lineasCuentas.add(cuenta.getNumero() + ";" + cuenta.getSaldo() + ";" + cuenta.getTipo() + ";" + cuenta.getCliente().getId());
	    }
	    ArchivoPlano.almacenar("cuentas.csv", lineasCuentas);
	}
	
	public void cargar() {
		ArrayList<String> lineasClientes = ArchivoPlano.cargar("clientes.csv");
		for(String linea : lineasClientes) {
			String datos[] = linea.split(";");
			Cliente c = new Cliente(Integer.parseInt(datos[0]), datos[1], datos[2]);
			this.clientes.put(Integer.parseInt(datos[0]), c);
		}
		//TODO cargar cuentas
		ArrayList<String> lineasCuentas = ArchivoPlano.cargar("cuentas.csv");
	    for(String linea : lineasCuentas) {
	        String datos[] = linea.split(";");
	        int numero = Integer.parseInt(datos[0]);
	        int saldo = Integer.parseInt(datos[1]);
	        String tipo = datos[2];
	        int idCliente = Integer.parseInt(datos[3]);
	        Cliente propietario = this.clientes.get(idCliente);
	        if (propietario != null) {
	            try {
	                this.crearCuenta(numero, saldo, tipo, idCliente);
	            } catch (Exception e) {
	                System.out.println("Error al cargar cuenta: " + e.getMessage());
	            }
	        } else {
	            System.out.println("Cliente no encontrado para la cuenta con número: " + numero);
	        }
	    }
	}
	
}	