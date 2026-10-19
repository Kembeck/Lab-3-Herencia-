public class InstalacionElectricaCertificada extends Instalacion {

    private double valorMateriales;

    public InstalacionElectricaCertificada(
            String codigo, String cliente, String direccion,
            Zona zona, int horas, int minutos,
            int puntos, double valorMateriales) {

        super(codigo, cliente, direccion, zona, horas, minutos, puntos);

        if (valorMateriales <= 0 || !Double.isFinite(valorMateriales)) {
            throw new IllegalArgumentException(
                    "El valor de los materiales debe ser un número positivo.");
        }

        this.valorMateriales = valorMateriales;
    }

    @Override
    public double calcularCosto() {
        double costoInstalacion = super.calcularCosto();
        double garantia = valorMateriales * 0.05;

        return costoInstalacion + 120 + garantia;
    }

    @Override
    public int diasEstimados() {
        return super.diasEstimados() + 2;
    }

    @Override
    public int maximoVisitas() {
        return 2;
    }

    @Override
    public String categoria() {
        return "Instalación eléctrica certificada";
    }

    public double getValorMateriales() {
        return valorMateriales;
    }
}