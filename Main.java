public class Main {

    public static void main(String[] args) {
        Controlador controlador = new Controlador();

        controlador.registrarCuadrilla(new Cuadrilla("C1", 8, false));
        controlador.registrarCuadrilla(new Cuadrilla("C2", 8, true));
        controlador.registrarCuadrilla(new Cuadrilla("C3", 6, false));

        controlador.registrarOrden(new Diagnostico(
                "D1", "Ana", "Zona 1",
                Zona.METROPOLITANA, 1, 0), false);

        controlador.registrarOrden(new Diagnostico(
                "D2", "Luis", "San Lucas",
                Zona.PERIFERICA, 1, 30), false);

        controlador.registrarOrden(new Instalacion(
                "I1", "Marta", "Zona 10",
                Zona.METROPOLITANA, 2, 30, 5), false);

        controlador.registrarOrden(new Instalacion(
                "I2", "Carlos", "Villa Canales",
                Zona.PERIFERICA, 1, 0, 4), false);

        controlador.registrarOrden(new InstalacionElectricaCertificada(
                "E1", "Pedro", "Zona 7",
                Zona.METROPOLITANA, 2, 30, 5, 2000), false);

        controlador.registrarOrden(new InstalacionElectricaCertificada(
                "E2", "Lucía", "San Lucas",
                Zona.PERIFERICA, 1, 0, 4, 1500), false);

        controlador.registrarOrden(new MantenimientoAireAcondicionado(
                "A1", "Sofía", "Zona 14",
                Zona.METROPOLITANA, 1, 30, 12000), false);

        controlador.registrarOrden(new MantenimientoAireAcondicionado(
                "A2", "Diego", "Zona 15",
                Zona.METROPOLITANA, 2, 0, 18000), false);

        Vista vista = new Vista(controlador);
        vista.mostrarMenu();
    }
}
