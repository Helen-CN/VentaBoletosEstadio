package controller;

public class VisualizadorAsientos 
{

    public static void mostrarDisponibles(String categoria, boolean[][] matriz) 
    {
        System.out.println("Mapa de asientos (" + categoria + "):");

        for (int i = 0; i < matriz.length; i++) 
        {
            for (int j = 0; j < matriz[i].length; j++) 
            {
                System.out.print(matriz[i][j] ? " X " : " O ");
            }
            System.out.println(" ← Fila " + (i + 1));
        }

        System.out.println("O = Libre | X = Ocupado");
    }
}
