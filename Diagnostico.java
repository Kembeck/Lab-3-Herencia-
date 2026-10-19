public class Diagnostico extends OrdenDeServicio {

    public Diagnostico(String codigo, String cliente, String direccion,
            Zona zona, int horas, int minutos) {

        super(codigo, cliente, direccion, zona, horas, minutos);

        if (getMinutosEstimados() > 90) {
            throw new IllegalArgumentException(
                    "El diagnóstico no puede superar 1 hora y 30 minutos.");
        }
    }

    @Override
    public double calcularCosto() {
        if (getZona() == Zona.METROPOLITANA) {
            return 150;
        }

        return 225;
    }

    @Override
    public int diasEstimados() {
        if (getZona() == Zona.METROPOLITANA) {
            return 1;
        }

        return 2;
    }

    @Override
    public int maximoVisitas() {
        return 3;
    }

    @Override
    public String categoria() {
        return "Diagnóstico";
    }
}