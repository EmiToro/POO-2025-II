//Emiliano Angel Toro Rojas, RUT: 21.512.702-8, Carrera: Ingenieria en tecnologias de información.
//Valentina Castillo,Rut; 15.166.692-2, Carrera: Ingenieria en tecnologias de información.



package taller_1;

import java.io.*;
import java.util.*;

public class Main {
	//Creamos variables necesarias para las funciones que haremos en nuestro codigo.
    static String[] exp_id = new String[100];
    static String[] Desc = new String[100];
    static String[] nombre_metrica = new String[10];
    static double[][] valores_metrica = new double[100][10];
    static int[][] confusion = new int[100][4];
    static int cont_exp = 0;
    static int cont_metri = 0;

    public static void main(String[] args) throws Exception {

        cargar_arch_exp("experimentos.txt");
        cargar_arch_predi("predicciones.txt"); //Creamos funciones para abrir y leer archivos, para despues guardar los datos necesarios.
        cargar_arch_metricas("metricas.txt");
        calcular_metricas();

        Scanner salida = new Scanner(System.in);
        int opcion = 0;

        //Menu de seleccion inicial.
        do {
        	System.out.println("----------------------------");
            System.out.println("SISTEMA DE EVALUACIÓN DE MODELOS DE IA");
            System.out.println("1. Menú Administrador");
            System.out.println("2. Menú Usuario");
            System.out.println("3. Salir");
            System.out.print("Seleccione una opción: ");

            opcion = salida.nextInt();
            salida.nextLine();

            if (opcion == 1) {
            	System.out.println();
                mostrarMenuAdmin(salida);
            } else if (opcion == 2) {
            	System.out.println();
                mostrarMenuUsuario(salida);
            } else if (opcion == 3) {
            	System.out.println();
                System.out.println("Saliendo del sistema...");
                break;
            } else {
            	System.out.println();
                System.out.println("Opción no válida!");
            }

        } while (true);

        salida.close();
    }
    //Cargamos el archivo de los experimentos en su propia funcion.
    public static void cargar_arch_exp(String arch) throws Exception {
        Scanner lector = new Scanner(new File(arch));
        while (lector.hasNextLine()) {  //Ciclo para verificar si quedan mas lineas en el archivo.
            String linea = lector.nextLine();
            String[] partes = linea.split(";");
            exp_id[cont_exp] = partes[0];
            Desc[cont_exp] = partes[1];
            cont_exp++;
        }
        lector.close(); //En las 3 funciones para abrir y leer archivos siempre cerramos estos mismos con este codigo.
    }
    //Esta funcion cargara el archivo de ppredicciones.txt.
    public static void cargar_arch_predi(String arch) throws Exception {
        Scanner lector = new Scanner(new File(arch));
        while (lector.hasNextLine()) {
            String linea = lector.nextLine();
            String[] partes = linea.split(";"); //creamos la lista partes
            String id = partes[0];
            int valor_r = Integer.parseInt(partes[1]);
            int valor_teo = Integer.parseInt(partes[2]);

            int int_indice = buscar_indice(id);
            
            if (int_indice != -1) {
	            if (valor_r == 1 && valor_teo == 1) {
	            	confusion[int_indice][0]++;
	            }
	            else if (valor_r == 0 && valor_teo == 1) {
	            	confusion[int_indice][1]++;
	            }
	            else if (valor_r == 0 && valor_teo == 0) {
	            	confusion[int_indice][2]++;
	            }
	            else if (valor_r == 1 && valor_teo == 0) {
	            	confusion[int_indice][3]++;
	            }
	        } else if (int_indice == -1) {
	        	System.out.println("No se encontro ha encontrado el indice!");
	        }   //Si  buscar_indice regresa -1, usamos este print para avisar que no encontro el indice deseado.
        }
        lector.close();
    }
    //En esta funcion cargaremos el archivo metricas.txt.
    public static void cargar_arch_metricas(String arch) throws Exception {
        Scanner lector = new Scanner(new File(arch));
        while (lector.hasNextLine()) {
            String linea = lector.nextLine();
            nombre_metrica[cont_metri] = linea;
            cont_metri++;
        }
        lector.close();
    }
    //usamos esta funcion para buscar el indice para la funcion cargar_arch_predi, la cual nos ayudara a buscar la posicion en la que se encuentra la id
    public static int buscar_indice(String id) {
        for (int i = 0; i < cont_exp; i++) {
            if (exp_id[i].equals(id)) {
                return i;
            }
        }
        return -1; //si no encuentra el indice retornara un -1.
    }

    //calculamos metricas para cada experimento y las almacenamos.
    public static void calcular_metricas() throws Exception {
        for (int i = 0; i < cont_exp; i++) {
            int TP = confusion[i][0];
            int FP = confusion[i][1];
            int TN = confusion[i][2];
            int FN = confusion[i][3];
            int total = TP + FP + TN + FN;
            
            //Hacemos los calculos que se nos pidieron.
        	double Accuracy = (double) (TP + TN) / total;
			double Precision = (double) TP / (TP + FP);
		    double Recall = (double) TP / (TP + FN);
	        double F1_Score = (double) 2 * (Precision * Recall) / (Precision + Recall);
	       
	        //Guardamos en la matriz valores_metrica los datos que necesitamos.
            for (int j = 0; j < cont_metri; j++) {
                String nombre = nombre_metrica[j];
                if (nombre.equals("Accuracy")) {
                    valores_metrica[i][j] = Accuracy;
                } else if (nombre.equals("Precision")) {
                    valores_metrica[i][j] = Precision;
                } else if (nombre.equals("Recall")) {
                    valores_metrica[i][j] = Recall;
                } else if (nombre.equals("F1-Score")) {
                    valores_metrica[i][j] = F1_Score;
                }
            }
        }
    }
    
    //En esta funcion si se escogio Admin, entraremos en esta y mostraremos las opciones.
    public static void mostrarMenuAdmin(Scanner salida) throws Exception {
        int opcion = 0;
        do {
        	System.out.println("----------------------------");
            System.out.println("MENÚ ADMINISTRADOR");
            System.out.println("1. Ver matriz completa de métricas");
            System.out.println("2. Identificar experimento con mejor F1-Score");
            System.out.println("3. Calcular promedio global de cada métrica");
            System.out.println("4. Comparar dos experimentos lado a lado");
            System.out.println("5.Comparar verificaciones de docente con matriz de confusión generada");
            System.out.println("6. Volver al menú principal");
            System.out.print("Seleccione una opción: ");

            opcion = salida.nextInt();
            salida.nextLine();
            
            if (opcion == 1) {
            	System.out.println();
                mostrar_metricas();
            } else if (opcion == 2) {
            	System.out.println();
                mostrar_mejor_F1score();
            } else if (opcion == 3) {
            	System.out.println();
                mostrar_promedio_metri();
            } else if (opcion == 4) {
            	System.out.println();
                comparar_exp(salida);
            } else if (opcion == 5) {
            	System.out.println();
                comparar_verif("verificacion_docente_confusiones.csv");
            } else if (opcion == 6) {
                break;
            } else {
            	System.out.println();
                System.out.println("Opción no válida!"); //si la opcion no es valida, esto se printeara.
            }
        } while (opcion != 6);
    }
    //Si la opcion fue 1, esta funcion mostrara las metricas.
    public static void mostrar_metricas() {
        for (int i = 0; i < cont_exp; i++) {
            System.out.println("Experimento: " + exp_id[i]);
            for (int j = 0; j < cont_metri; j++) {
                System.out.println("  " + nombre_metrica[j] + ": " + valores_metrica[i][j]);
            }
            System.out.println("TP: " + confusion[i][0]);
            System.out.println("FP: " + confusion[i][1]);
            System.out.println("TN: " + confusion[i][2]);
            System.out.println("FN: " + confusion[i][3]);
            
        } System.out.println();
    }
    //Si la opcion fue 2, mostraremos el mejor F1-Score.
    public static void mostrar_mejor_F1score() {
        int indice_F1 = -1; //indice de F1-Score
        for (int j = 0; j < cont_metri; j++) {
            if (nombre_metrica[j].equals("F1-Score")) {
                indice_F1 = j;
            }
        } //Si no se encuentra F1-Score se regresara sin retornar un valor.
        if (indice_F1 == -1) {
            System.out.println("F1-Score no esta definida!");
            return;
        }
        double maxF1 = -1.0; //maxF1 es el maximo o mayor numero de F1-Score.
        int indice = -1;
        for (int i = 0; i < cont_exp; i++) {
            if (valores_metrica[i][indice_F1] > maxF1) {
                maxF1 = valores_metrica[i][indice_F1];
                indice = i; //Indice sin F1 es especificamente para las metricas.
            }
        } //Si no encuentra metricas calculadas este regresara.
        if (indice == -1) {
            System.out.println("No hay metricas calculadas!");
            return;
        }
        System.out.println("El experimento con mejor F1-Score es: " + exp_id[indice]);
        System.out.println("F1-Score: " + valores_metrica[indice][indice_F1]);
        System.out.println();
    }
    // Esta funcion la creamos para mostrar el promedio de la metrica, porsiacaso.
    public static void mostrar_promedio_metri() {
        for (int j = 0; j < cont_metri; j++) {
            double suma = 0.0;
            for (int i = 0; i < cont_exp; i++) {
                suma += valores_metrica[i][j];
            }
            if (cont_exp > 0) {
            double promedio = suma / cont_exp; //aqui calculamos el promedio de las metricas.
            System.out.println("El promedio de " + nombre_metrica[j] + "es: " + promedio);
            }
            
        } System.out.println();
    }
    // si opcion es 4 en esta funcion compararemos los experimentos
    public static void comparar_exp(Scanner salida) {
        mostrar_listas_exp();
        System.out.print("Ingrese el numero del primer experimento: ");
        int indice1 = 0; //indice1 es el indice del numero del primer experimento.
        
        indice1 = salida.nextInt() - 1; //le restamos uno porque si ponemos 1 necesitamos ver el indice en la matriz como 0.
        salida.nextLine();
  
        System.out.print("Ingrese el número del segundo experimento: ");
        int indice2 = 0; //indice2 es el indice del segundo experimento.
        
        indice2 = salida.nextInt() - 1;
        salida.nextLine();

     // Validamos los indices ingresados.
        if (indice1 < 0) {
            System.out.println("Dato no valido!!");
            return;
        }
        if (indice1 >= cont_exp) {
            System.out.println("Dato no valido!!");
            return;
        }
        if (indice2 < 0) {
            System.out.println("Dato no valido!!");
            return;
        }
        if (indice2 >= cont_exp) {
            System.out.println("Dato no valido!!");
            return;  
        }   
        //Empezamos a printear las comparaciones.
        System.out.println("Comparamos " + exp_id[indice1] + " y " + exp_id[indice2] + "!");
        for (int j = 0; j < cont_metri; j++) {
            System.out.println(nombre_metrica[j] + ": " + valores_metrica[indice1][j] + " vs " + valores_metrica[indice2][j]);
        }
        System.out.println("TP: " + confusion[indice1][0] + " vs " + confusion[indice2][0]);
        System.out.println("FP: " + confusion[indice1][1] + " vs " + confusion[indice2][1]);
        System.out.println("TN: " + confusion[indice1][2] + " vs " + confusion[indice2][2]);
        System.out.println("FN: " + confusion[indice1][3] + " vs " + confusion[indice2][3]);
        System.out.println();
    }
    //Aqui en esta funcion, vamos a realizar la comparación de verificacion de docente.
    public static void comparar_verif(String archivo) {//  lee el archivo.
        try {
            Scanner lector = new Scanner(new File(archivo));
            lector.nextLine(); // Salta encabezado.
            System.out.println("Comparación del archivo de verificacion de docente:");
            while (lector.hasNextLine()) {
                String linea = lector.nextLine();
                String[] partes = linea.split(",");
                String id = partes[0].trim(); //Identificador ID.
                //Les nombramos csv por el formato del archivo.
                int TP_csv = Integer.parseInt(partes[1]);//Verdaderos positivos.
                int FP_csv = Integer.parseInt(partes[2]);// Falsos positivos.
                int TN_csv = Integer.parseInt(partes[3]);//Verdaderos negativos.
                int FN_csv = Integer.parseInt(partes[4]);//
                int indice = buscar_indice(id); //int indice es el valor del indice de la id.
                if (indice != -1) {
                        if (TP_csv == confusion[indice][0] &&
                            FP_csv == confusion[indice][1] &&
                            TN_csv == confusion[indice][2] &&
                            FN_csv == confusion[indice][3]) {
                            System.out.println(id + ": CSV[" + TP_csv + "," + FP_csv + "," + TN_csv + "," + FN_csv +  "] vs Calculado[" + confusion[indice][0] + "," + confusion[indice][1] + "," + confusion[indice][2] + "," + confusion[indice][3] + "] Coinciden!. ");                   
                        } else {
                            System.out.println(id + ": CSV["  + TP_csv + "," + FP_csv + "," + TN_csv + "," + FN_csv +  "] vs Calculado[" + confusion[indice][0] + "," + confusion[indice][1] + "," + confusion[indice][2] + "," + confusion[indice][3] +  "] No Coinciden!.");
                        } //La razon por  la que agregamos si coinciden o no, es para verificar bien que los datos esten correctos.

                        }
            }
            lector.close();
          } catch (Exception e) {  //En caso de que haya un error en tiempo de ejecución.
            System.out.println("Error al comparar los datos del archivo CSV: " + e.getMessage());//Mensaje en caso de error.
        }
    }

    public static void mostrarMenuUsuario(Scanner salida) {//Aparece visiblemente el Menú de usuario y se pide que  se ingrese un valor.
        int opcionDos = 0; //le dimos valor opcionDos al el menu de admin tiene la primera opcion.
        do {
            System.out.println("----------------------------");
            System.out.println("=== MENÚ USUARIO ===");
            System.out.println("1. Ver lista de experimentos");
            System.out.println("2. Mostrar matriz de confusión de un experimento");
            System.out.println("3. Ver métricas de un experimento");
            System.out.println("4. Ver promedio de Accuracy de todos los modelos");
            System.out.println("5. Volver al menú principal");
            System.out.print("Seleccione una opción: ");

            opcionDos = salida.nextInt();
            salida.nextLine();

            //Opciones a escoger del menu de Usuario.
            if (opcionDos == 1){
                System.out.println();
                mostrar_listas_exp();
            }
            else if (opcionDos == 2){
                System.out.println();
                matriz_confusion_exp(salida);//Se pide la matriz confusión
            }
            else if (opcionDos == 3){
                System.out.println();
                metricas_exp(salida); //Se pide ver las metricas del experimento
            }
            else if (opcionDos == 4){
                System.out.println();
                accuracyPromedio();
            }
            else if (opcionDos == 5){ //Volver al menú principal.
                break;//Rompe el ciclo.
            }
            else{
                System.out.println();
                System.out.println("Opción no válida.");
            }
        }while (opcionDos != 5);
    }
    // Al seleccionar opcion 1 en esta funcion se mostraran las listas de experimentos.
    public static void mostrar_listas_exp(){ 
        System.out.println("Lista Experimentos!: ");
        for (int i = 0; i < cont_exp; i++) {//cantidad de experimentos = 4
            System.out.println((i + 1) + ". " + exp_id[i] + " - " + Desc[i]);
        }
    }
    //Al seleccionar la opción dos se muestra la matriz de confusión
    public static void matriz_confusion_exp(Scanner salida) {//Se pide ingresar un valor
        mostrar_listas_exp();
        System.out.print("Ingrese el número de experimento: ");
        int indice = 0;
        indice = salida.nextInt() - 1; //se le resta uno ya que es necesario tener el valor real del indice.
        salida.nextLine();
        //Se verifica si el indice ingresado es valido o no.
        if (indice < 0) {
            System.out.println("Índice inválido!!");
            return;
        }
        if (indice >= cont_exp) {
            System.out.println("Índice inválido!!");
            return;
        }//Se completan los valores de la matriz de de confusión.

        System.out.println("TP: " + confusion[indice][0]);
        System.out.println("FP: " + confusion[indice][1]);
        System.out.println("TN: " + confusion[indice][2]);
        System.out.println("FN: " + confusion[indice][3]);
        System.out.println();
    }
    //En esta funcion, al seleccionar la opcion 3, se mostraran las metricas del experimento.
    public static void metricas_exp(Scanner salida) {//Se pide ingresar el numero del experimento.
        mostrar_listas_exp();
        System.out.print("Ingrese el número de experimento: ");
        int indice = 0;
        indice = salida.nextInt() - 1;
        salida.nextLine();
        //Se verifica si el indice ingresado es valido o no.
        if (indice < 0) {
            System.out.println("Índice inválido!!");
            return;
        }
        if (indice >= cont_exp) {
            System.out.println("Índice inválido!!");
            return;
        }

        for (int j = 0; j < cont_metri; j++) {
            System.out.println(nombre_metrica[j] + ": " + valores_metrica[indice][j]);
        }

    }
    //Se calcula el promedio de Accuracy en esta funcion.
    public static void accuracyPromedio() {
        int indice_Accuracy = -1;
        for (int j = 0; j < cont_metri; j++) {
            if (nombre_metrica[j].equals("Accuracy")) {
                indice_Accuracy = j;
            }
        }
        if (indice_Accuracy == -1) {
            System.out.println("No está la métrica Accuracy.");
            return;
        }
        double suma = 0.0;
        for (int i = 0; i < cont_exp; i++) {
            suma += valores_metrica[i][indice_Accuracy];
        }
        double promedio = suma / cont_exp; //calculamos el promedio de Accuracy
        System.out.println("Promedio de Accuracy: " + promedio);
    }
}  