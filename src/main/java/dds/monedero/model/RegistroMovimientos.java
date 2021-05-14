package dds.monedero.model;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class RegistroMovimientos {
 private List<Movimiento> movimientos= new ArrayList<>();

 public RegistroMovimientos(){}

 public void setMovimientos(List<Movimiento> movimientos) {
  this.movimientos.addAll(movimientos);
 }

 public List<Movimiento> getMovimientos() {
   return movimientos;
 }

 public void agregarMovimiento(Movimiento movimiento) {
  this.movimientos.add(movimiento);
 }

 public double getMontoExtraidoA(LocalDate fecha) {
  return getMovimientos().stream()
      .filter(movimiento -> movimiento.fueExtraido(fecha))
      .mapToDouble(Movimiento::getMonto)
      .sum();
 }

 public List<Movimiento> getDepositos() {
  return getMovimientos().stream().filter(movimiento -> movimiento.isDeposito()).collect(Collectors.toList());
 }

 public double limiteA(LocalDate fecha) {
    return 1000 - getMontoExtraidoA(fecha);
 }

}
