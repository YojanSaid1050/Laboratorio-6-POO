package Pruebas;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import logica.Cuenta;

public class Tests {
	private Cuenta cuenta;
	
	@BeforeEach
	public void Setup() {
		this.cuenta = new Cuenta (1, 1000, null, null);
	}
	
	@Test
	public void ConsignarTest() {
	cuenta.consignar(500);
	assertEquals(1500,cuenta.getSaldo());
	}
	
	@Test
	public void retirarTest() throws Exception {
		cuenta.retirar(500);
			assertEquals(500, cuenta.getSaldo());
		}
	}