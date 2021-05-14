package dds.monedero.model;

import java.time.LocalDate;

//CODE SMELLS
// Misplaced method: "agregateA" (va en cuenta o en un objeto que lleve un registro de los movimientos, al que la cuenta conoce)
// Long method: "agregateA" (agregar un movimiento y cambiarle el saldo a la cuenta no deberia estar en un metodo que se llama agregar, este metodo solo deberia agregar el movimiento)
// Metodo innecesario: "fueDepositado" y "fueExtraido"
// Type test: "isDeposito" y "isExtraccion" (estamos preguntando a un objeto por su tipo, claramente podrian ser clases polimorficas)

public class Movimiento {
  private LocalDate fecha;
  //En ningún lenguaje de programación usen jamás doubles para modelar dinero en el mundo real
  //siempre usen numeros de precision arbitraria, como BigDecimal en Java y similares
  private double monto;
  private boolean esDeposito;

  public Movimiento(LocalDate fecha, double monto, boolean esDeposito) {
    this.fecha = fecha;
    this.monto = monto;
    this.esDeposito = esDeposito;
  }

  public double getMonto() {
    return monto;
  }

  public LocalDate getFecha() {
    return fecha;
  }

  public boolean fueDepositado(LocalDate fecha) {
    return isDeposito() && esDeLaFecha(fecha);
  }

  public boolean fueExtraido(LocalDate fecha) {
    return isExtraccion() && esDeLaFecha(fecha);
  }

  public boolean esDeLaFecha(LocalDate fecha) {
    return this.fecha.equals(fecha);
  }

  public boolean isDeposito() {
    return esDeposito;
  }

  public boolean isExtraccion() {
    return !esDeposito;
  }

  public void agregateA(Cuenta cuenta) {
    cuenta.setSaldo(calcularValor(cuenta));
    cuenta.getRegistroMovimientos().agregarMovimiento(this);
  }

  public double calcularValor(Cuenta cuenta) {
    if (esDeposito) {
      return cuenta.getSaldo() + getMonto();
    } else {
      return cuenta.getSaldo() - getMonto();
    }
  }
}
