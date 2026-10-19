import java.util.ArrayList;

public class Controlador {

    private ArrayList<OrdenDeServicio> ordenes = new ArrayList<>();
    private ArrayList<Cuadrilla> cuadrillas = new ArrayList<>();
    private ArrayList<Jornada> jornadas = new ArrayList<>();

    private ArrayList<OrdenDeServicio> registrosCobrados = new ArrayList<>();
    private ArrayList<OrdenDeServicio> visitasCobradas = new ArrayList<>();

   
    public void registrarOrden(OrdenDeServicio nueva, boolean cobrar) {
        if (nueva.getCodigo().trim().equals("")) {
            throw new IllegalArgumentException("Ingrese el código.");
        }

        for (OrdenDeServicio orden : ordenes) {
            if (orden.getCodigo().equals(nueva.getCodigo())) {
                throw new IllegalArgumentException("Código repetido.");
            }
        }

        if (nueva.getEstado() != EstadoOrden.PENDIENTE
                || nueva.getVisitasRealizadas() != 0) {
            throw new IllegalArgumentException("La orden debe ser nueva.");
        }

        ordenes.add(nueva);

        if (cobrar) {
            registrosCobrados.add(nueva);
        }
    }

    public void registrarCuadrilla(Cuadrilla nueva) {
        for (Cuadrilla cuadrilla : cuadrillas) {
            if (cuadrilla.getCodigo().equals(nueva.getCodigo())) {
                throw new IllegalArgumentException("Código repetido.");
            }
        }

        cuadrillas.add(nueva);
    }

    public Jornada crearJornada(Cuadrilla cuadrilla) {
        if (!cuadrillas.contains(cuadrilla)) {
            throw new IllegalArgumentException("La cuadrilla no existe.");
        }

        for (Jornada jornada : jornadas) {
            if (jornada.getCuadrilla() == cuadrilla
                    && !jornada.getEstado().equals("Cerrada")) {
                throw new IllegalStateException(
                        "La cuadrilla tiene una jornada sin cerrar.");
            }
        }

        Jornada nueva = new Jornada(cuadrilla);
        jornadas.add(nueva);
        return nueva;
    }

    public void asignarOrden(Jornada jornada, OrdenDeServicio orden) {
        validarDatos(jornada, orden);

        for (Jornada otra : jornadas) {
            if (otra.tieneAsignada(orden)) {
                throw new IllegalStateException(
                        "La orden ya está asignada.");
            }
        }

        jornada.asignarOrden(orden);
    }

    public void retirarOrden(Jornada jornada, OrdenDeServicio orden) {
        validarDatos(jornada, orden);
        jornada.retirarOrden(orden);
    }

    public void iniciarJornada(Jornada jornada) {
        if (!jornadas.contains(jornada)) {
            throw new IllegalArgumentException("La jornada no existe.");
        }

        jornada.iniciar();

        for (OrdenDeServicio orden : jornada.getOrdenes()) {
            if (orden.getVisitasRealizadas() > 0) {
                visitasCobradas.add(orden);
            }
        }
    }

    public void registrarResultado(Jornada jornada,
            OrdenDeServicio orden, boolean completada) {

        validarDatos(jornada, orden);
        jornada.registrarResultado(orden, completada);
    }

    private void validarDatos(Jornada jornada, OrdenDeServicio orden) {
        if (!jornadas.contains(jornada) || !ordenes.contains(orden)) {
            throw new IllegalArgumentException(
                    "La jornada o la orden no existe.");
        }
    }

    public int contarOrdenes(String categoria, EstadoOrden estado) {
        int cantidad = 0;

        for (OrdenDeServicio orden : ordenes) {
            if (orden.categoria().equals(categoria)
                    && orden.getEstado() == estado) {
                cantidad++;
            }
        }

        return cantidad;
    }

    public double ingresoRegistro() {
        double total = 0;

        for (OrdenDeServicio orden : registrosCobrados) {
            total += orden.calcularCosto();
        }

        return total;
    }

    public double ingresoVisitas() {
        return visitasCobradas.size() * 40.0;
    }

    public double ingresoTotal() {
        return ingresoRegistro() + ingresoVisitas();
    }

    public double ingresoCategoria(String categoria) {
        double total = 0;

        for (OrdenDeServicio orden : registrosCobrados) {
            if (orden.categoria().equals(categoria)) {
                total += orden.calcularCosto();
            }
        }

        for (OrdenDeServicio orden : visitasCobradas) {
            if (orden.categoria().equals(categoria)) {
                total += 40;
            }
        }

        return total;
    }

    public ArrayList<OrdenDeServicio> getOrdenes() {
        return new ArrayList<>(ordenes);
    }

    public ArrayList<Cuadrilla> getCuadrillas() {
        return new ArrayList<>(cuadrillas);
    }

    public ArrayList<Jornada> getJornadas() {
        return new ArrayList<>(jornadas);
    }
}