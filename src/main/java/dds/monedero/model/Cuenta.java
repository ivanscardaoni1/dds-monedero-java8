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
//Duplicated Code: getMovimientos().stream().filter(movimiento -> movimiento.isDeposito() (la idea de filtrar segun el tipo de movimiento se repite bastante)
//Large Class: class Cuenta (se podrian abstraer las responsabilidades del manejo de los movimientos a una clase "registroMovimientos") CORREGIDO
//Type Test: "isDeposito" (estamos preguntandole el tipo al movimiento cuando podrian ser objetos polimorficos)
//Divergent Change: modificar el saldo (1 atributo, 2 metodos) agregar movimientos (1 atributo 2 metodos)
//setSaldo y setMovimientos innecesario: poder setear el saldo puede traer conflictos ya que solo se deberia modificar por extracciones o depositos
//Duplicated Code: excepciones.
//Primitive obsession: se utilizan tipos de datos primitivos.



public class Cuenta {

  private double saldo = 0;
  private RegistroMovimientos registroMovimientos = new RegistroMovimientos();

  public Cuenta() {}

  public Cuenta(double montoInicial) {
    saldo = montoInicial;
  }

  public void setMovimientos(List<Movimiento> movimientos) {
    this.registroMovimientos.setMovimientos(movimientos);
  }

  public void poner(double cuanto) {
    if (cuanto <= 0) {
      throw new MontoNegativoException(cuanto + ": el monto a ingresar debe ser un valor positivo");
    }

    if (registroMovimientos.getMovimientos().stream().filter(movimiento -> movimiento.isDeposito()).count() >= 3) {
      throw new MaximaCantidadDepositosException("Ya excedio los " + 3 + " depositos diarios");
    }

    new Movimiento(LocalDate.now(), cuanto, true).agregateA(this);
  }

  public void sacar(double cuanto) {
    if (cuanto <= 0) {
      throw new MontoNegativoException(cuanto + ": el monto a ingresar debe ser un valor positivo");
    }
    if (getSaldo() - cuanto < 0) {
      throw new SaldoMenorException("No puede sacar mas de " + getSaldo() + " $");
    }

    double montoExtraidoHoy = registroMovimientos.getMontoExtraidoA(LocalDate.now());
    double limite = 1000 - montoExtraidoHoy;
    if (cuanto > limite) {
      throw new MaximoExtraccionDiarioException("No puede extraer mas de $ " + 1000
          + " diarios, límite: " + limite);
    }
    new Movimiento(LocalDate.now(), cuanto, false).agregateA(this);
  }

  public RegistroMovimientos getRegistroMovimientos(){
    return registroMovimientos;
  }

  public double getSaldo() {
    return saldo;
  }

  public void setSaldo(double saldo) {
    this.saldo = saldo;
  }

}
