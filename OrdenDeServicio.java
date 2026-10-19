public abstract class OrdenDeServicio {

    private String codigo;
    private String cliente;
    private String direccion;
    private Zona zona;
    private long minutosEstimados;
    private EstadoOrden estado;
    private int visitasRealizadas;

    protected OrdenDeServicio(String codigo, String cliente,
            String direccion, Zona zona, int horas, int minutos) {

        if (codigo.equals("") || cliente.equals("") || direccion.equals("")) {
            throw new IllegalArgumentException("Complete todos los datos.");
        }

        if (zona == null) {
            throw new IllegalArgumentException("Seleccione una zona.");
        }

        if (horas < 0 || minutos < 0 || minutos > 59) {
            throw new IllegalArgumentException(
                    "Las horas deben ser positivas o cero y los minutos entre 0 y 59.");
        }

        if (horas == 0 && minutos == 0) {
            throw new IllegalArgumentException(
                    "El tiempo estimado debe ser mayor que cero.");
        }

        this.codigo = codigo;
        this.cliente = cliente;
        this.direccion = direccion;
        this.zona = zona;
        this.minutosEstimados = (long) horas * 60 + minutos;
        this.estado = EstadoOrden.PENDIENTE;
        this.visitasRealizadas = 0;
    }

   
    public abstract double calcularCosto();

    public abstract int diasEstimados();

    public abstract int maximoVisitas();

    public abstract String categoria();

    public boolean requiereCertificacion() {
        return false;
    }

    public void iniciarAtencion() {
        if (estado != EstadoOrden.PENDIENTE) {
            throw new IllegalStateException(
                    "La orden debe estar pendiente.");
        }

        estado = EstadoOrden.EN_ATENCION;
    }

    public void registrarResultado(boolean completada) {
        if (estado != EstadoOrden.EN_ATENCION) {
            throw new IllegalStateException(
                    "La orden debe estar en atención.");
        }

        visitasRealizadas++;

        if (completada) {
            estado = EstadoOrden.COMPLETADA;
        } else if (visitasRealizadas >= maximoVisitas()) {
            estado = EstadoOrden.CANCELADA;
        } else {
            estado = EstadoOrden.PENDIENTE;
        }
    }

    public double getHorasEstimadas() {
        return minutosEstimados / 60.0;
    }

    public long getMinutosEstimados() {
        return minutosEstimados;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getCliente() {
        return cliente;
    }

    public String getDireccion() {
        return direccion;
    }

    public Zona getZona() {
        return zona;
    }

    public EstadoOrden getEstado() {
        return estado;
    }

    public int getVisitasRealizadas() {
        return visitasRealizadas;
    }
}