package main;

import javax.swing.JLabel;

import horario.Horario;
import visual.Ventana;

public class Main {
	 
	/*
	 Esta aplicación sirve para crear tareas, eliminarlas y condicionarlas.
	 + -> Crear
	 x -> Eliminar
	 * -> Condicionar
	 
	 Hay que poner un nombre distinto para cada tarea
	 
	 Condicionar consiste en que puedes elegir dos tareas y el nombre de una condición,ç
	 que se añade al desplegable de la primera.
	 La segunda tarea solo aparecerá cuando hayas elegido esa condición en el desplegable
	
	 Extra:
	 Si eliminas una tarea con otra condicionada, solo se elimina la primera,
	 debe ser deseleccionada la tarea para que se elimine
	 
	 Si deseleccionas una condición de una tarea y esta tiene activada otra,
	 la tercera se mantiene, debe ser desactivada la tercera antes para que se desactive
	 */
	
	//Crea el horario, que se autoejecuta
	public static void main(String[] args)
	{
		Horario horario=new Horario();
	}	
}
