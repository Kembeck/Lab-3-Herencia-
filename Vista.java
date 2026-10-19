import java.util.ArrayList;
import java.util.Scanner;
import java.text.DecimalFormat;

public class Vista {

    private Controlador controlador;
    private Scanner lector = new Scanner(System.in);
    private DecimalFormat formato = new DecimalFormat("0.00");

    public Vista(Controlador controlador) {
        this.controlador = controlador;
    }

    public void mostrarMenu() {
        int opcion = -1;

        while (opcion != 0) {
            System.out.println("\n--- TecniHogar  ---");
            System.out.println("1. Cotizar");
            System.out.println("2. Registrar orden");
            System.out.println("3. Registrar cuadrilla");
            System.out.println("4. Crear jornada");
            System.out.println("5. Asignar orden");
            System.out.println("6. Retirar orden");
            System.out.println("7. Iniciar jornada");
            System.out.println("8. Registrar resultado");
            System.out.println("9. Consultar órdenes");
            System.out.println("10. Consultar cuadrillas");
            System.out.println("11. Reportes");
            System.out.println("0. Salir");

            try {
                opcion = leerEntero("Opción: ");

                switch (opcion) {
                    case 1:
                        cotizar();
                        break;

                    case 2:
                        controlador.registrarOrden(pedirOrden(false), true);
                        break;

                    case 3:
                        registrarCuadrilla();
                        break;

                    case 4:
                        controlador.crearJornada(elegirCuadrilla());
                        break;

                    case 5:
                        controlador.asignarOrden(
                                elegirJornada(), elegirOrden());
                        break;

                    case 6:
                        controlador.retirarOrden(
                                elegirJornada(), elegirOrden());
                        break;

                    case 7:
                        controlador.iniciarJornada(elegirJornada());
                        break;

                    case 8:
                        registrarResultado();
                        break;

                    case 9:
                        mostrarOrdenes();
                        break;

                    case 10:
                        mostrarCuadrillas();
                        break;

                    case 11:
                        mostrarReportes();
                        break;

                    case 0:
                        System.out.println("Programa finalizado.");
                        break;

                    default:
                        System.out.println("Opción inválida.");
                }

                if (opcion >= 2 && opcion <= 8) {
                    System.out.println("Operación realizada.");
                }

            } catch (NumberFormatException e) {
                System.out.println("Ingrese un número válido.");
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private OrdenDeServicio pedirOrden(boolean cotizacion) {
        System.out.println("\n1. Diagnóstico");
        System.out.println("2. Instalación");
        System.out.println("3. Instalación eléctrica certificada");
        System.out.println("4. Mantenimiento de aire acondicionado");

        int tipo = leerEntero("Servicio: ");

        if (tipo < 1 || tipo > 4) {
            throw new IllegalArgumentException("Servicio inválido.");
        }

        String codigo = "COTIZACION";
        String cliente = "Sin registrar";
        String direccion = "Sin registrar";

        if (!cotizacion) {
            codigo = leerTexto("Código: ");
            cliente = leerTexto("Cliente: ");
            direccion = leerTexto("Dirección: ");
        }

        System.out.println("1. Metropolitana");
        System.out.println("2. Periférica");

        int opcionZona = leerEntero("Zona: ");
        Zona zona;

        if (opcionZona == 1) {
            zona = Zona.METROPOLITANA;
        } else if (opcionZona == 2) {
            zona = Zona.PERIFERICA;
        } else {
            throw new IllegalArgumentException("Zona inválida.");
        }

        int horas = leerEntero("Horas estimadas: ");
        int minutos = leerEntero("Minutos estimados: ");

        switch (tipo) {
            case 1:
                return new Diagnostico(
                        codigo, cliente, direccion, zona, horas, minutos);

            case 2:
                int puntos = leerEntero("Cantidad de puntos: ");

                return new Instalacion(
                        codigo, cliente, direccion,
                        zona, horas, minutos, puntos);

            case 3:
                int puntosElectricos = leerEntero("Cantidad de puntos: ");
                double materiales = leerDecimal("Valor de materiales: Q");

                return new InstalacionElectricaCertificada(
                        codigo, cliente, direccion, zona,
                        horas, minutos, puntosElectricos, materiales);

            default:
                int btu = leerEntero("Capacidad en BTU: ");

                return new MantenimientoAireAcondicionado(
                        codigo, cliente, direccion,
                        zona, horas, minutos, btu);
        }
    }

    private void cotizar() {
        OrdenDeServicio orden = pedirOrden(true);

        System.out.println("Costo: Q"
                + formato.format(orden.calcularCosto()));

        System.out.println("Trabajo estimado: "
                + orden.getHorasEstimadas() + " horas");

        System.out.println("Atención estimada: "
                + orden.diasEstimados() + " días hábiles");
    }

    private void registrarCuadrilla() {
        String codigo = leerTexto("Código: ");
        double capacidad = leerDecimal("Capacidad de horas por jornada: ");

        int respuesta = leerEntero(
                "¿Certificada en refrigeración? 1. Sí / 2. No: ");

        if (respuesta != 1 && respuesta != 2) {
            throw new IllegalArgumentException("Seleccione 1 o 2.");
        }

        boolean certificada = respuesta == 1;

        Cuadrilla cuadrilla = new Cuadrilla(
                codigo, capacidad, certificada);

        controlador.registrarCuadrilla(cuadrilla);
    }

    private void registrarResultado() {
        Jornada jornada = elegirJornada();
        OrdenDeServicio orden = elegirOrden();

        int resultado = leerEntero("1. Completada / 2. Visita fallida: ");

        if (resultado != 1 && resultado != 2) {
            throw new IllegalArgumentException("Seleccione 1 o 2.");
        }

        controlador.registrarResultado(jornada, orden, resultado == 1);
    }

    private OrdenDeServicio elegirOrden() {
        String codigo = leerTexto("Código de la orden: ");

        for (OrdenDeServicio orden : controlador.getOrdenes()) {
            if (orden.getCodigo().equals(codigo)) {
                return orden;
            }
        }

        throw new IllegalArgumentException("La orden no existe.");
    }

    private Cuadrilla elegirCuadrilla() {
        String codigo = leerTexto("Código de la cuadrilla: ");

        for (Cuadrilla cuadrilla : controlador.getCuadrillas()) {
            if (cuadrilla.getCodigo().equals(codigo)) {
                return cuadrilla;
            }
        }

        throw new IllegalArgumentException("La cuadrilla no existe.");
    }

    private Jornada elegirJornada() {
        ArrayList<Jornada> jornadas = controlador.getJornadas();

        if (jornadas.isEmpty()) {
            throw new IllegalStateException("No hay jornadas.");
        }

        for (int i = 0; i < jornadas.size(); i++) {
            Jornada jornada = jornadas.get(i);

            System.out.println((i + 1) + ". Cuadrilla "
                    + jornada.getCuadrilla().getCodigo()
                    + " - " + jornada.getEstado());
        }

        int numero = leerEntero("Seleccione una jornada: ");

        if (numero < 1 || numero > jornadas.size()) {
            throw new IllegalArgumentException("Selección inválida.");
        }

        return jornadas.get(numero - 1);
    }

    private void mostrarOrdenes() {
        for (OrdenDeServicio orden : controlador.getOrdenes()) {
            System.out.println("\nCódigo: " + orden.getCodigo());
            System.out.println("Cliente: " + orden.getCliente());
            System.out.println("Dirección: " + orden.getDireccion());
            System.out.println("Zona: " + orden.getZona());
            System.out.println("Servicio: " + orden.categoria());

            System.out.println("Horas estimadas: "
                    + orden.getHorasEstimadas());

            System.out.println("Estado: " + orden.getEstado());
            System.out.println("Visitas: " + orden.getVisitasRealizadas());
        }
    }

    private void mostrarCuadrillas() {
        for (Cuadrilla cuadrilla : controlador.getCuadrillas()) {
            System.out.println("\nCódigo: " + cuadrilla.getCodigo());

            System.out.println("Capacidad: "
                    + cuadrilla.getCapacidadHoras() + " horas");

            System.out.println("Certificada en refrigeración: "
                    + cuadrilla.tieneCertificacion());
        }
    }

    private void mostrarReportes() {
        ArrayList<String> categorias = new ArrayList<>();

        for (OrdenDeServicio orden : controlador.getOrdenes()) {
            if (!categorias.contains(orden.categoria())) {
                categorias.add(orden.categoria());
            }
        }

        System.out.println("\n--- ÓRDENES POR CATEGORÍA Y ESTADO ---");

        for (String categoria : categorias) {
            System.out.println("\n" + categoria);

            for (EstadoOrden estado : EstadoOrden.values()) {
                System.out.println(estado + ": "
                        + controlador.contarOrdenes(categoria, estado));
            }
        }

        System.out.println("\n--- INGRESOS ---");

        System.out.println("Por registro: Q"
                + formato.format(controlador.ingresoRegistro()));

        System.out.println("Por visitas adicionales: Q"
                + formato.format(controlador.ingresoVisitas()));

        System.out.println("Total: Q"
                + formato.format(controlador.ingresoTotal()));

        for (String categoria : categorias) {
            System.out.println(categoria + ": Q"
                    + formato.format(
                            controlador.ingresoCategoria(categoria)));
        }

        System.out.println("\n--- JORNADAS SIN CERRAR ---");

        for (Jornada jornada : controlador.getJornadas()) {
            if (!jornada.getEstado().equals("Cerrada")) {
                System.out.println("\nCuadrilla: "
                        + jornada.getCuadrilla().getCodigo());

                System.out.println("Estado: " + jornada.getEstado());

                System.out.println("Horas asignadas: "
                        + jornada.getHorasAsignadas());

                System.out.println("Capacidad disponible: "
                        + jornada.getCapacidadDisponible() + " horas");
            }
        }
    }

    private String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return lector.nextLine().trim();
    }

    private int leerEntero(String mensaje) {
        return Integer.parseInt(leerTexto(mensaje));
    }

    private double leerDecimal(String mensaje) {
        return Double.parseDouble(leerTexto(mensaje));
    }
}