package horario;
import tareas.*;
import visual.Ventana;

import org.quartz.Scheduler;

import static org.quartz.SimpleScheduleBuilder.*;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

public class Horario {
	private String[] dias= {"lunes","martes","miercoles","jueves","viernes","sabado", "domingo"};
	private String[] horas=crearHoras();
	private Ventana ventana;
	private Scheduler scheduler;
	
	public Horario()
	{	
		this.crear();
	}
	
	/*
	Crea las horas para los bloques del horario
	*/
	
	public Scheduler getScheduler()
	{
		return scheduler;
	}
	
	public String[] crearHoras() 
	{
		String[] horas=new String[25];
		
		//Añade los números del 0 al 24
		for(int i=0; i<=24; i++)
		{
			horas[i]=String.valueOf(i)+":00";
		}
		
		return horas;
	}
	
	public String[] getDias() {
		return dias;
	}

	public void setDias(String[] dias) {
		this.dias = dias;
	}

	public String[] getHoras() {
		return horas;
	}

	public void setHoras(String[] horas) {
		this.horas = horas;
	}

	//Te da parte de la hora de la hora
	public int getHora(String horaTexto)
	{
		String[] partes=horaTexto.split(":");
		String horaTotal=partes[0];
		
		int hora=Integer.parseInt(horaTotal);
		
		return hora;
	}
	
	//Te da la parte de los minutos de la hora
	public int getMinutos(String horaTexto)
	{
		String[] partes=horaTexto.split(":");
		String minutosTotal=partes[1];
		
		int minutos=Integer.parseInt(minutosTotal);
		
		return minutos;
	}
	
	//Da la hora del calendario elegida en la tarea
	public Date getHoraCal(String hora)
	{
		Calendar cal=Calendar.getInstance();
		int horaCal=getHora(hora);
		int minutosCal=getMinutos(hora);
		
		//Como ejemplo
		cal.set(Calendar.YEAR,2000);
		cal.set(Calendar.MONTH, Calendar.JANUARY);
		cal.set(Calendar.DAY_OF_MONTH,1);
		
		cal.set(Calendar.HOUR_OF_DAY,horaCal);
		cal.set(Calendar.MINUTE,minutosCal);
		Date horaFinal=cal.getTime();
		
		return horaFinal;
	}
	
	//Da la tarea (job)
	public JobDetail getJob(String nombre, String diaSemana)
	{
		JobDetail tarea=JobBuilder.newJob(TareaControl.class)
			.withIdentity(nombre, "tareas")
			.usingJobData("diaSemana",diaSemana)
			.build();
		
		return tarea;
	}
	
	//Da la acción en que se ejecuta (trigger)
	public Trigger getTrigger(String nombre, Date horaInicial, Date horaFinal)
	{
		Trigger accion=TriggerBuilder.newTrigger()
			.withIdentity(nombre+"_accion", "acciones")
			.startAt(horaInicial)
			.endAt(horaFinal)
			.withSchedule(SimpleScheduleBuilder.simpleSchedule()
					.withIntervalInSeconds(1)
					.repeatForever())
			.build();
	
		return accion;
	}
	
	//Crea una tarea
	public void crearTarea(String nombre, String diaSemana, String horaInicio, String horaFin)
	{
		//Hora de inicio
		Date horaInicial=getHoraCal(horaInicio);
		
		//Hora de fin
		Date horaFinal=getHoraCal(horaFin);
		
		//Tarea
		JobDetail tarea=getJob(nombre,diaSemana);
		
		//Acción de la tarea
		Trigger accion=getTrigger(nombre,horaInicial,horaFinal);
		
		try {
			scheduler.scheduleJob(tarea, accion);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		
		ventana.mostrarTarea(tarea);
	}
	
	//Quita una tarea
	public void quitarTarea(String nombre)
	{
		JobKey jobKey=new JobKey(nombre, "tareas");
		try {
			scheduler.deleteJob(jobKey);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
	
	//Crea el horario
	public void crear()
	{
		try {
			scheduler=StdSchedulerFactory.getDefaultScheduler();
			scheduler.start();
			
			ventana=new Ventana(this);
			
			ventana.getVentana().addWindowListener(new WindowAdapter()
			{
				@Override
				public void windowClosing(WindowEvent e)
				{
					try {
						scheduler.shutdown();
					} catch (SchedulerException ex) {
						ex.printStackTrace();
					}
				}
			});
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
}
