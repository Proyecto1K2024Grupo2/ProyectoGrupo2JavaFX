package com.iesochoa.grupo2.proyectogrupo2javafx.Model;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        int opc;
        do {
            System.out.println("""
                === MENÚ PRINCIPAL ===
                1. Gestionar ANIMALES
                2. Gestionar CLIENTES
                3. Gestionar TRATAMIENTOS iu
                4. Gestionar CITAS iu
                5. Gestionar HISTORIAL N/A
                6. Gestionar VETERINARIOS
                7. Gestionar CIRUJANOS
                8. Gestionar CUIDADORES
                9. Gestionar RECEPCIONISTAS
                10. Gestionar CENTROS
                11. Gestionar SALAS
                12. SALIR
                """);

            System.out.print("Selecciona una opción: ");
            while (!sc.hasNextInt()) {
                System.out.println("Por favor, ingresa un número válido.");
                sc.next();
            }
            opc = sc.nextInt();

            switch (opc) {
                case 1 -> Animal.mostrarMenu();
                case 2 -> Cliente.mostrarMenu();
                case 3 -> Tratamiento.mostrarMenu();
                case 4 -> Cita.mostrarMenu();
                case 5 -> Historial.mostrarMenu();
                case 6 -> Veterinario.mostrarMenu();
                case 7 -> Cirujano.mostrarMenu();
                case 8 -> Cuidador.mostrarMenu();
                case 9 -> Recepcionista.mostrarMenu();
                case 10 -> Centro.mostrarMenu();
                case 11 -> Sala.mostrarMenu();
                case 12 -> System.out.println("Saliendo del programa...");
                default -> System.out.println("Opción no válida. Inténtalo de nuevo.");
            }

        } while (opc != 12);
    }
}
