package controller;

/**
 * Clase utilitaria para visualizar los asientos disponibles y ocupados
 * en una matriz correspondiente a una categoría específica.
 */
public class VisualizadorAsientos {

    /**
     * Muestra en consola el mapa de asientos de una categoría.
     *
     * @param categoria Nombre de la categoría (VIP, Preferencial, General).
     * @param matriz Matriz de asientos, donde true = ocupado y false = disponible.
     */
    public static void mostrarDisponibles(String categoria, boolean[][] matriz) {
        System.out.println("\n========================================");
        System.out.println("🎟️  Mapa de Asientos - Categoría: " + categoria);
        System.out.println("========================================");

        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                System.out.print(matriz[i][j] ? " [X] " : " [O] ");
            }
            System.out.println("  ← Fila " + (i + 1));
        }

        System.out.println("\n[O] = Disponible | [X] = Ocupado");
        System.out.println("========================================\n");
    }
}
