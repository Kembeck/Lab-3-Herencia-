import java.util.ArrayList;

public class Jornada {

    private Cuadrilla cuadrilla;
    private String estado;
    private ArrayList<OrdenDeServicio> ordenes;
    private ArrayList<OrdenDeServicio> resultadosPendientes;

    public Jornada(Cuadrilla cuadrilla) {
        if (cuadrilla == null) {
            throw new IllegalArgumentException("Seleccione una cuadrilla.");
        }

        this.cuadrilla = cuadrilla;
        this.estado = "En preparación";
        this.ordenes = new ArrayList<>();
        this.resultadosPendientes = new ArrayList<>();
    }

    public void asignarOrden(OrdenDeServicio orden) {
        if (!estado.equals("En preparación")) {
            throw new IllegalStateException(
                    "La jornada debe estar en preparación.");
        }

        if (orden.getEstado() != EstadoOrden.PENDIENTE) {
            throw new IllegalStateException(
                    "La orden debe estar pendiente.");
        }

        if (ordenes.contains(orden)) {
            throw new IllegalStateException("La orden ya está asignada.");
        }

        if (orden.requiereCertificacion()
                && !cuadrilla.tieneCertificacion()) {
            throw new IllegalStateException(
                    "La cuadrilla necesita certificación en refrigeración.");
        }

        long total = minutosAsignados() + orden.getMinutosEstimados();

        if (total > cuadrilla.getCapacidadHoras() * 60) {
            throw new IllegalStateException(
                    "Se supera la capacidad de la cuadrilla.");
        }

        ordenes.add(orden);
    }

    public void retirarOrden(OrdenDeServicio orden) {
        if (!estado.equals("En preparación")) {
            throw new IllegalStateException(
                    "Solo puede retirar órdenes antes de iniciar.");
        }

        if (!ordenes.contains(orden)) {
            throw new IllegalArgumentException(
                    "La orden no pertenece a esta jornada.");
        }

        ordenes.remove(orden);
    }

    public void iniciar() {
        if (!estado.equals("En preparación")) {
            throw new IllegalStateException(
                    "La jornada debe estar en preparación.");
        }

        if (ordenes.isEmpty()) {
            throw new IllegalStateException("La jornada está vacía.");
        }

        for (OrdenDeServicio orden : ordenes) {
            if (orden.getEstado() != EstadoOrden.PENDIENTE) {
                throw new IllegalStateException(
                        "Todas las órdenes deben estar pendientes.");
            }
        }

        for (OrdenDeServicio orden : ordenes) {
            orden.iniciarAtencion();
            resultadosPendientes.add(orden);
        }

        estado = "Iniciada";
    }

    public void registrarResultado(OrdenDeServicio orden,
            boolean completada) {

        if (!estado.equals("Iniciada")) {
            throw new IllegalStateException(
                    "La jornada debe estar iniciada.");
        }

        if (!resultadosPendientes.contains(orden)) {
            throw new IllegalArgumentException(
                    "La orden no pertenece a esta jornada o ya tiene resultado.");
        }

        orden.registrarResultado(completada);
        resultadosPendientes.remove(orden);

        if (resultadosPendientes.isEmpty()) {
            estado = "Cerrada";
        }
    }

    private long minutosAsignados() {
        long total = 0;

        for (OrdenDeServicio orden : ordenes) {
            total += orden.getMinutosEstimados();
        }

        return total;
    }

    public double getHorasAsignadas() {
        return minutosAsignados() / 60.0;
    }

    public double getCapacidadDisponible() {
        return cuadrilla.getCapacidadHoras() - getHorasAsignadas();
    }

    public boolean tieneAsignada(OrdenDeServicio orden) {
        if (estado.equals("En preparación")) {
            return ordenes.contains(orden);
        }

        return resultadosPendientes.contains(orden);
    }

    public Cuadrilla getCuadrilla() {
        return cuadrilla;
    }

    public String getEstado() {
        return estado;
    }

    public ArrayList<OrdenDeServicio> getOrdenes() {
        return new ArrayList<>(ordenes);
    }
}