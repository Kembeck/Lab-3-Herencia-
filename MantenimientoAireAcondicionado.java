public class MantenimientoAireAcondicionado extends OrdenDeServicio {

    private int btu;

    public MantenimientoAireAcondicionado(
            String codigo, String cliente, String direccion,
            Zona zona, int horas, int minutos, int btu) {

        super(codigo, cliente, direccion, zona, horas, minutos);

        if (zona != Zona.METROPOLITANA) {
            throw new IllegalArgumentException(
                    "Este servicio solo se realiza en zona metropolitana.");
        }

        if (btu < 9000 || btu > 60000) {
            throw new IllegalArgumentException(
                    "La capacidad debe estar entre 9000 y 60000 BTU.");
        }

        this.btu = btu;
    }

    @Override
    public double calcularCosto() {
        return 100 + getHorasEstimadas() * 110 + 80;
    }

    @Override
    public int diasEstimados() {
        return 2;
    }

    @Override
    public int maximoVisitas() {
        return 1;
    }

    @Override
    public String categoria() {
        return "Mantenimiento de aire acondicionado";
    }

    @Override
    public boolean requiereCertificacion() {
        return true;
    }

    public int getBtu() {
        return btu;
    }
}