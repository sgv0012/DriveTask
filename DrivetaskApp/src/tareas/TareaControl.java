package tareas;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

//Maneja las tareas
public class TareaControl implements Job
{
	//Se ejecutarían las tareas aquí en caso de que fuera la hora real
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException 
	{
		System.out.println("Ejecutando tarea...");
	}
}
