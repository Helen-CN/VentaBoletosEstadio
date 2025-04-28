package model;

/**
 * Clase que representa un boleto de un evento en el estadio.
 */
public class Boleto {
    private String id;
    private String categoria;
    private double precio;
    private String asiento;
    private boolean vendido;

    /**
     * Constructor para crear un boleto nuevo.
     *
     * @param id        Identificador único del boleto.
     * @param categoria Categoría del boleto (VIP, Preferencial, General).
     * @param precio    Precio del boleto.
     * @param asiento   Asiento asignado (ejemplo: F1C2).
     */
    public Boleto(String id, String categoria, double precio, String asiento) {
        this.id = id;
        this.categoria = categoria;
        this.precio = precio;
        this.asiento = asiento;
        this.vendido = false;
    }

    public String getId() {
        return id;
    }

    public String getCategoria() {
        return categoria;
    }

    public double getPrecio() {
        return precio;
    }

    public String getAsiento() {
        return asiento;
    }

    public boolean isVendido() {
        return vendido;
    }

    public void setVendido(boolean vendido) {
        this.vendido = vendido;
    }

    @Override
    public String toString() {
        return "Boleto{" +
                "ID='" + id + '\'' +
                ", Categoría='" + categoria + '\'' +
                ", Precio=" + precio +
                ", Asiento='" + asiento + '\'' +
                ", Vendido=" + vendido +
                '}';
    }
}
