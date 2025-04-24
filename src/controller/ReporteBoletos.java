package controller;

import model.Boleto;
import java.util.LinkedList;

public class ReporteBoletos {

    public static void mostrarVendidos(String categoria, LinkedList<Boleto> vendidos) 
    {
        System.out.println("Boletos vendidos (" + categoria + "):");
        for (Boleto b : vendidos) 
        {
            System.out.println(b);
        }
    }
}
