package visual;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;		

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;

import horario.Horario;

public class Ventana 
{
	private JFrame ventana;
	private JPanel panel;
	private JScrollPane panelScroll;
	private VentanaTarea ventanaT;
	private JPanel ventanaTarea;
	private JPanel ventanaQuitarTarea;
	private JPanel ventanaCondTarea;
	private Horario horario;
	private LinkedList<LinkedList<Object>> relCondicion;
	
	public Ventana(Horario horario)
	{
		this.horario=horario;
		
		crearVentana();
		crearPanel();
		anadirTarea();
		quitarTarea();
		condTarea();
		crearVentanaQuitarTarea();
		crearVentanaCondTarea();
		crearHorario(horario.getDias(), horario.getHoras());
		
		ventanaT=new VentanaTarea(ventana, panel, horario);
		ventanaTarea=ventanaT.getVentanaTarea();
		panel.add(ventanaTarea);
		panel.add(ventanaQuitarTarea);
		ventana.setVisible(true);
		ventanaQuitarTarea.setVisible(false);
		relCondicion=new LinkedList<LinkedList<Object>>();
	}
	
	public JFrame getVentana()
	{
		return this.ventana;
	}
	
	//Creación de la ventana
	public void crearVentana()
	{
		ventana = new JFrame("ventana");
		ventana.setSize(1000,4000);
		ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	//Scroll vertical
	public void crearPanel()
	{
		panel=new JPanel();
		panel.setLayout(null);
		panel.setPreferredSize(new java.awt.Dimension(ventana.getWidth(),ventana.getHeight()));
		panelScroll=new JScrollPane(panel);
		panelScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		ventana.setContentPane(panelScroll);
		panel.setBackground(new Color(74, 72, 72));
		panelScroll.getViewport().setBackground(new Color(74, 72, 72));
	}
	
	//Crea la ventana de quitar tarea
	public void crearVentanaQuitarTarea()
	{
		ventanaQuitarTarea=new JPanel();
		ventanaQuitarTarea.setLayout(null);
		ventanaQuitarTarea.setBackground(Color.WHITE);
		ventanaQuitarTarea.setFont(new Font("Arial", Font.BOLD, 10));
		ventanaQuitarTarea.setOpaque(true);
		ventanaQuitarTarea.setBounds(ventana.getWidth()/2-300/2+100,100,300,200);
		Border ventanaBorde=BorderFactory.createLineBorder(Color.BLACK,4);
		ventanaQuitarTarea.setBorder(ventanaBorde);
		
		//Preguntas y respuestas sobre datos de tarea
		
		JLabel pregunta=new JLabel();
		pregunta.setText("Nombre de la tarea a eliminar");
		pregunta.setBounds(40,40,ventanaQuitarTarea.getWidth()-40*2,30);
		ventanaQuitarTarea.add(pregunta);
		
		JTextField respuesta=new JTextField();
		respuesta.setBounds(40,80,ventanaQuitarTarea.getWidth()-40*2,30);
		respuesta.setBorder(new LineBorder(Color.RED,4));
		ventanaQuitarTarea.add(respuesta);
		
		//Botón de eliminar tarea
		JButton botonEliminar=new JButton("x");
		botonEliminar.setBounds(ventanaQuitarTarea.getWidth()/2-20/2,130,40,40);
		botonEliminar.setFont(new Font("Arial", Font.BOLD, 10));
		botonEliminar.setFocusPainted(false);
		botonEliminar.setBackground(Color.RED);
		botonEliminar.setForeground(Color.WHITE);
		botonEliminar.addActionListener(e->
		{
			String nombre=respuesta.getText();
			horario.quitarTarea(nombre);
			
			//Busca la tarea del panel que coincida para eliminar
			for(Component comp:panel.getComponents())
			{
				if(comp instanceof JPanel)
				{
					JLabel label=(JLabel)((JPanel)comp).getComponents()[0];
					
					if(label.getText().equals(respuesta.getText()))
					{
						panel.remove(comp);
						panel.revalidate();
						panel.repaint();
					}
				}
			}
			
			ventanaQuitarTarea.setVisible(false);
		});
		
		ventanaQuitarTarea.add(botonEliminar);
	}
	
	public void crearPregunta(String texto, int num)
	{
		//Tarea a seleccionar
		JLabel tarea=new JLabel();
		tarea.setText(texto);
		tarea.setBounds(40,num,ventanaCondTarea.getWidth()-40*2,30);
		ventanaCondTarea.add(tarea);
	}
	
	public JTextField crearRespuesta(int num)
	{
		//Respuesta de tarea a seleccionar
		JTextField respuesta=new JTextField();
		respuesta.setBounds(40,num,ventanaCondTarea.getWidth()-40*2,30);
		respuesta.setBorder(new LineBorder(Color.RED,4));
		ventanaCondTarea.add(respuesta);
		
		return respuesta;
	}
	
	//Ventana para explicar lo que quieres condicionar
	public void crearVentanaCondTarea()
	{
		ventanaCondTarea=new JPanel();
		ventanaCondTarea.setLayout(null);
		ventanaCondTarea.setBackground(Color.WHITE);
		ventanaCondTarea.setFont(new Font("Arial", Font.BOLD, 10));
		ventanaCondTarea.setOpaque(true);
		ventanaCondTarea.setBounds(ventana.getWidth()/2-300/2+100,100,400,400);
		Border ventanaBorde=BorderFactory.createLineBorder(Color.BLACK,4);
		ventanaCondTarea.setBorder(ventanaBorde);
		panel.add(ventanaCondTarea);
		
		crearPregunta("Nombre de la tarea a seleccionar",40);
		JTextField respuesta1=crearRespuesta(80);
		crearPregunta("Nombre de la tarea a condicionar",120);
		JTextField respuesta2 = crearRespuesta(160);
		crearPregunta("Condición de la tarea seleccionada",200);
		JTextField respuesta3=crearRespuesta(240);
		
		//Botón de añadir condición
		JButton botonCond=new JButton("*");
		botonCond.setBounds(ventanaCondTarea.getWidth()/2-20/2,300,40,40);
		botonCond.setFont(new Font("Arial", Font.BOLD, 15));
		botonCond.setFocusPainted(false);
		botonCond.setBackground(Color.RED);
		botonCond.setForeground(Color.WHITE);
		botonCond.addActionListener(e->
		{
			JPanel tareaSel1=null;
			JPanel tareaSel2=null;
			
			//Busca la tarea donde está el desplegable
			for(Component comp:panel.getComponents())
			{
				if(comp instanceof JPanel && comp.getBackground().equals(Color.RED))
				{
					JLabel texto=(JLabel)((JPanel)comp).getComponents()[0];
					JComboBox<String> desplegable=(JComboBox<String>)((JPanel)comp).getComponents()[1];
					if(texto.getText().equals(respuesta1.getText()))
					{
						tareaSel1=(JPanel)comp;
						
						//Añade la opción al desplegable
						desplegable.addItem(respuesta3.getText());
					}
					
					if(texto.getText().equals(respuesta2.getText()))
					{
						tareaSel2=(JPanel)(comp);
					}
				}
			}
			
			//Añade un nuevo elemento a la lista de tareas asociadas con la condición
			JPanel[] tareas= {tareaSel1,tareaSel2};
			LinkedList<Object> relTareas=new LinkedList<>();
			relTareas.add(tareas);
			relTareas.add(respuesta3.getText());
			relCondicion.add(relTareas);
			ventanaCondTarea.setVisible(false);
			
			//La tarea condicionada es invisible hasta que se elija la condición asociada
			tareaSel2.setVisible(false);
		});
		
		ventanaCondTarea.add(botonCond);
		ventanaCondTarea.setVisible(false);
	}
	
	//Botón de añadir tarea
	public void anadirTarea()
	{
		JButton botonCrearTarea=new JButton("+");
		botonCrearTarea.setBounds(50,40,40,40);
		botonCrearTarea.setFont(new Font("Arial", Font.BOLD, 15));
		botonCrearTarea.setFocusPainted(false);
		botonCrearTarea.setBackground(Color.RED);
		botonCrearTarea.setForeground(Color.WHITE);
		botonCrearTarea.setBorder(new LineBorder((Color.BLACK),5));
		panel.add(botonCrearTarea);
		
		botonCrearTarea.addActionListener(e->
		{
			ventanaTarea.setVisible(true);
		});
	}
	
	//Botón de quitar tarea
	public void quitarTarea()
	{
		JButton botonQuitarTarea=new JButton("x");
		botonQuitarTarea.setBounds(10,40,40,40);
		botonQuitarTarea.setFont(new Font("Arial", Font.BOLD, 15));
		botonQuitarTarea.setFocusPainted(false);
		botonQuitarTarea.setBackground(Color.RED);
		botonQuitarTarea.setForeground(Color.WHITE);
		botonQuitarTarea.setBorder(new LineBorder((Color.BLACK),5));
		panel.add(botonQuitarTarea);
		
		botonQuitarTarea.addActionListener(e->
		{
			ventanaQuitarTarea.setVisible(true);
		});
	}
	
	//Condiciones de la tarea
	public void condTarea()
	{
		JButton condTarea=new JButton("*");
		condTarea.setBounds(50,0,40,40);
		condTarea.setFont(new Font("Arial", Font.BOLD, 30));
		condTarea.setFocusPainted(false);
		condTarea.setBackground(Color.RED);
		condTarea.setForeground(Color.WHITE);
		condTarea.setBorder(new LineBorder((Color.BLACK),5));
		panel.add(condTarea);
		
		condTarea.addActionListener(e->
		{
			ventanaCondTarea.setVisible(true);
		});
	}
	
	//Crea a partir de los strings los labels
	public JLabel[] crearLabels(String[] textos)
	{
		JLabel[] labels=new JLabel[textos.length];
		
		//Añade según el texto de cada texto cada label
		JLabel label;
		for(int i=0;i<textos.length;i++)
		{
			label=new JLabel();
			label.setText(textos[i]);
			labels[i]=label;
		}
		
		return labels;
	}
	
	//Crea la ventana con todo su interior
	public void crearHorario(String[] dias, String[] horas)
	{	
		JLabel[] diasLabels=crearLabels(dias);
		JLabel[] horasLabels=crearLabels(horas);
		
		//Dibuja los días
		for(int i=0;i<diasLabels.length;i++)
		{
			JLabel label=diasLabels[i];
			label.setBounds(i*150+100,40,150,50);
			label.setBorder(new LineBorder(Color.RED,4));
			label.setFont(new Font("Arial",Font.BOLD,15));			
			label.setBackground(Color.WHITE);
			label.setOpaque(true);
			label.setHorizontalAlignment(JLabel.CENTER);
			panel.add(label);
		}
		
		//Dibuja las horas
		for(int i=0;i<horasLabels.length;i++)
		{
			JLabel label=horasLabels[i];
			label.setBounds(50,i*150+87,50,150);
			label.setBorder(new LineBorder(Color.RED,4));
			label.setFont(new Font("Arial",Font.BOLD,15));
			label.setBackground(Color.WHITE);
			label.setOpaque(true);
			label.setHorizontalAlignment(JLabel.CENTER);
			panel.add(label);
			panel.revalidate();
			panel.repaint();
		}
	}
	
	//Te da las coordenadas de la tarea
	public int[] getTareaCoords(Date horaInicio, Date horaFin, String diaSemana)
	{
		Calendar cal1=Calendar.getInstance();
		cal1.setTime(horaInicio);
		Calendar cal2=Calendar.getInstance();
		cal2.setTime(horaFin);

		int inicioHora=cal1.get(Calendar.HOUR_OF_DAY);
		int inicioMinutos=cal1.get(Calendar.MINUTE);
		int finHora=cal2.get(Calendar.HOUR_OF_DAY);
		int finMinutos=cal2.get(Calendar.MINUTE);
		
		List<String> diasSemana=Arrays.asList(horario.getDias());
		
		//Calcula donde situar la tarea según el horario
		int x=100+diasSemana.indexOf(diaSemana)*150;
		int y=inicioHora*150+80
			 +4*inicioHora
			 +(int)(((double)inicioMinutos/60.00)*150);
		int width=150;
		int height=finHora*150+80
				   +4*inicioHora+4
				   +(int)(((double)finMinutos/60.00)*150)-y;
		
		return new int[] {x,y,width,height};
	}
	
	//Crea el panel de la tarea
	public void crearTareaPanel(String nombre, int x,int y,int width,int height)
	{
		JPanel tarea=new JPanel();
		tarea.setBackground(Color.RED);
		tarea.setBounds(x,y,width,height);
		tarea.setOpaque(true);
		tarea.setBorder(new LineBorder(Color.WHITE,4));
		JLabel textoTarea=new JLabel();
		textoTarea.setBounds(tarea.getWidth()/2-10/2,10,10,10);
		textoTarea.setText(nombre);
		textoTarea.setForeground(Color.WHITE);
		textoTarea.setFont(new Font("Arial",Font.BOLD,15));
		tarea.add(textoTarea);
		panel.add(tarea);
		
		//Crea el desplegable
		ArrayList<String> opciones=new ArrayList<>();
		DefaultComboBoxModel<String> desplModelo=new DefaultComboBoxModel<>();
		JComboBox<String> desplegable=new JComboBox<>(desplModelo);
		desplegable.setBounds(40,200,200,30);
		desplegable.setBackground(Color.WHITE);
		//Se añade "cond" al desplegable como opción por defecto
		desplegable.addItem("cond");
		
		desplegable.addActionListener(e->
		{
			//Se elige una opción del desplegable
			String condicion=(String)desplegable.getSelectedItem();
			
			for(LinkedList<Object> rel:relCondicion)
			{
				JPanel[] tareas=(JPanel[])(rel.get(0));
				JPanel tarea1=(JPanel)(tareas[0]);
				JPanel tarea2=(JPanel)(tareas[1]);
				String cond=(String)rel.get(1);

				//Si la tarea tiene misma condición que la primera, se activa
				if(tarea.equals(tarea1) && condicion.equals(cond))
				{
					tarea2.setVisible(true);
				}
				
				//Toda tarea que no tenga misma condición se desactiva
				else if (tarea.equals(tarea1) && !condicion.equals(cond))
				{
					tarea2.setVisible(false);
				}
			}
		});
		
		tarea.add(desplegable);
		
		panel.revalidate();
		panel.repaint();
	}
	
	//Crea la tarea en la ventana
	public void crearTarea(String nombre, String diaSemana, Date horaInicio, Date horaFin)
	{
		
		int[] tareaCoords=getTareaCoords(horaInicio,horaFin,diaSemana);
		int x=tareaCoords[0];
		int y=tareaCoords[1];
		int width=tareaCoords[2];
		int height=tareaCoords[3];
		
		crearTareaPanel(nombre,x,y,width,height);
	}
	
	//Crea la tarea nueva en el horario
	public void mostrarTarea(JobDetail tarea)
	{
		Scheduler scheduler=horario.getScheduler();
		JobDataMap dataMap=tarea.getJobDataMap();
		JobKey jobKey=tarea.getKey();
		String nombre=jobKey.getName();
		String diaSemana=dataMap.getString("diaSemana");
		
		try {
			List<? extends Trigger> triggers=scheduler.getTriggersOfJob(jobKey);
			
			//Por cada vez que se ejecuta la tarea en cada tiempo se dibuja
			//en la ventana
			for(Trigger trigger:triggers)
			{
				Date horaInicio=trigger.getStartTime();
				Date horaFin=trigger.getEndTime();
				crearTarea(nombre,diaSemana,horaInicio,horaFin);
			}
			
		} catch (SchedulerException e) {
			e.printStackTrace();
		}	
	}
}
