package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

//CODE SMELLS
//Duplicated Code: "saldo = 0" (duplicado en la declaracion y el constructor). CORREGIDO
//Duplicated Code: getMovimientos().stream().filter(movimiento -> movimiento.isDeposito() (la idea de filtrar segun el tipo de movimiento se repite bastante) CORREGIDO
//Large Class: class Cuenta (se podrian abstraer las responsabilidades del manejo de los movimientos a una clase "registroMovimientos") CORREGIDO
//Divergent Change: modificar el saldo (1 atributo, 2 metodos) agregar movimientos (1 atributo 2 metodos) CORREGIDO
//setSaldo y setMovimientos innecesario: poder setear el saldo puede traer conflictos ya que solo se deberia modificar por extracciones o depositos CORREGIDO
//Duplicated Code: excepciones. CORREGIDO
//Primitive obsession: se utilizan tipos de datos primitivos.
//Type Test: "isDeposito" (estamos preguntandole el tipo al movimiento cuando podrian ser objetos polimorficos)



public class Cuenta {

  private double saldo = 0;
  private RegistroMovimientos registroMovimientos = new RegistroMovimientos();

  public Cuenta() {}

  public Cuenta(double montoInicial) {
    saldo = montoInicial;
  }

  public void poner(double cuanto) {
    validarDeposito(cuanto);
    registroMovimientos.agregarMovimiento(new Movimiento(LocalDate.now(), cuanto, true));
    sumarSaldo(cuanto);
  }

  public void sacar(double cuanto) {
    validarExtraccion(cuanto);
    registroMovimientos.agregarMovimiento(new Movimiento(LocalDate.now(), cuanto, false));
    restarSaldo(cuanto);
  }

  public RegistroMovimientos getRegistroMovimientos(){
    return registroMovimientos;
  }

  public void sumarSaldo(double cuanto) {
    saldo += cuanto;
  }

  public void restarSaldo(double cuanto) {
    saldo -= cuanto;
  }

  public double getSaldo() {
    return saldo;
  }

  public void validarMontoNegativo(double cuanto) {
    if (cuanto <= 0) {
      throw new MontoNegativoException(cuanto + ": el monto a ingresar debe ser un valor positivo");
    }
  }

  public void validarDeposito(double cuanto) {
    validarMontoNegativo(cuanto);
    if (registroMovimientos.getDepositos().stream().count() >= 3) {
      throw new MaximaCantidadDepositosException("Ya excedio los " + 3 + " depositos diarios");
    }
  }

  public void validarExtraccion(double cuanto) {
    validarMontoNegativo(cuanto);
    if (getSaldo() - cuanto < 0) {
      throw new SaldoMenorException("No puede sacar mas de " + getSaldo() + " $");
    }
    double limite = registroMovimientos.limiteA(LocalDate.now());
    if (cuanto > limite) {
      throw new MaximoExtraccionDiarioException("No puede extraer mas de $ " + 1000
          + " diarios, límite: " + limite);
    }
  }

}
