public class Cuadrilla {

    private String codigo;
    private double capacidadHoras;
    private boolean certificada;

    public Cuadrilla(String codigo, double capacidadHoras,
            boolean certificada) {

        if (codigo.trim().equals("")) {
            throw new IllegalArgumentException("Ingrese el código.");
        }

        if (capacidadHoras <= 0 || !Double.isFinite(capacidadHoras)) {
            throw new IllegalArgumentException(
                    "La capacidad debe ser un número positivo.");
        }

        this.codigo = codigo;
        this.capacidadHoras = capacidadHoras;
        this.certificada = certificada;
    }

    public String getCodigo() {
        return codigo;
    }

    public double getCapacidadHoras() {
        return capacidadHoras;
    }

    public boolean tieneCertificacion() {
        return certificada;
    }
}