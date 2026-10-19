public class Instalacion extends OrdenDeServicio {

    private int puntos;

    public Instalacion(String codigo, String cliente, String direccion,
            Zona zona, int horas, int minutos, int puntos) {

        super(codigo, cliente, direccion, zona, horas, minutos);

        if (puntos <= 0) {
            throw new IllegalArgumentException(
                    "La cantidad de puntos debe ser mayor que cero.");
        }

        this.puntos = puntos;
    }

    @Override
    public double calcularCosto() {
        double tarifaBase;

        if (getZona() == Zona.METROPOLITANA) {
            tarifaBase = 100;
        } else {
            tarifaBase = 175;
        }

       
        long minutosPorPuntos = puntos * 45L;
        long minutosFacturables = getMinutosEstimados();

        if (minutosPorPuntos > minutosFacturables) {
            minutosFacturables = minutosPorPuntos;
        }

        
        long horasFacturables = minutosFacturables / 60;
         if (minutosFacturables % 60 != 0) {
        horasFacturables++;
         }

        return tarifaBase + horasFacturables * 90;
    }

    @Override
    public int diasEstimados() {
        if (getZona() == Zona.METROPOLITANA) {
            return 3;
        }

        return 5;
    }

    @Override
    public int maximoVisitas() {
        return 3;
    }

    @Override
    public String categoria() {
        return "Instalación";
    }

    public int getPuntos() {
        return puntos;
    }
}