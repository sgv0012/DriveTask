package visual;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import horario.Horario;

public class VentanaTarea {
	private JFrame ventana;
	private JPanel panel;
	private JPanel ventanaTarea;
	private String nombreTarea;
	private String diaSemana;
	private String horaInicio;
	private String horaFin;
	//Identifica a cada componente
	private int compID;
	private Horario horario;
	
	public VentanaTarea(JFrame ventana, JPanel panel, Horario horario)
	{
		this.ventana=ventana;
		this.panel=panel;
		this.horario=horario;
		
		compID=1;
		
		ventanaTarea=new JPanel();
		crearVentanaTarea();
	}
	
	public JPanel getVentanaTarea()
	{
		return this.ventanaTarea;
	}
	
	//Crea textos para la ventana
	public void crearTextoVentana(int dist, String texto)
	{
		JLabel tarea=new JLabel();
		tarea.setBounds(40,dist,ventanaTarea.getWidth()-40*2,30);
		tarea.setText(texto);
		
		ventanaTarea.add(tarea);
	}
	
	//Crea inputs para la ventana
	public void crearInputVentana(int dist)
	{	
		JTextField tarea=new JTextField();
		tarea.setBounds(40,dist,ventanaTarea.getWidth()-40*2,30);
		tarea.setBorder(new LineBorder(Color.RED,4));
		//Asigno un identificador para poder acceder a él
		//con un ID autoincrementador
		tarea.setName(String.valueOf(compID));
		compID++;
		
		ventanaTarea.add(tarea);
	}
	
	//Asigna las propiedades de la ventana de tareas
	public void ventanaTareaPropiedades()
	{
		ventanaTarea=new JPanel();
		ventanaTarea.setLayout(null);
		ventanaTarea.setBackground(Color.WHITE);
		ventanaTarea.setOpaque(true);
		ventanaTarea.setBounds(ventana.getWidth()/2-500/2+100,100,500,300);
		Border ventanaBorde=BorderFactory.createLineBorder(Color.BLACK,4);
		ventanaTarea.setBorder(ventanaBorde);
	}
	
	public void ventanaTareaElems()
	{
		//Texto de escribir tarea
		crearTextoVentana(10,"Nombre de tarea");
		
		//Input para tarea
		crearInputVentana(40);
		
		//Texto de escoger día de la semana
		crearTextoVentana(70,"Día de la semana");
				
		//Input para día de la semana
		crearInputVentana(100);
		
		//Texto de hora de inicio
		crearTextoVentana(130,"Hora inicial (hh:mm)");
				
		//Input para hora de fin
		crearInputVentana(160);
		
		//Texto de hora de fin
		crearTextoVentana(190,"Hora final (hh:mm)");
		
		//Input para hora de fin
		crearInputVentana(220);
	}
	
	//En la ventana de tareas, el botón para enviar todo
	public void ventanaTareaBoton()
	{
		JButton botonAsignarTarea=new JButton("+");
		botonAsignarTarea.setBounds(ventanaTarea.getWidth()/2-20/2,255,40,40);
		botonAsignarTarea.setFont(new Font("Arial", Font.BOLD, 10));
		botonAsignarTarea.setFocusPainted(false);
		botonAsignarTarea.setBackground(Color.RED);
		botonAsignarTarea.setForeground(Color.WHITE);
		botonAsignarTarea.addActionListener(e->
		{
			//Asigna a las variables los datos de la ventana de tarea
			Component[] comps=ventanaTarea.getComponents();
			for(Component comp:comps)
			{
				if(comp instanceof JTextField)
				{
					String compID=comp.getName();
					
					//Según el ID del input hay una variable u otra
					switch(compID) 
					{
						case "1":
							nombreTarea=((JTextField)(comp)).getText();
							break;
						case "2":
							diaSemana=((JTextField)(comp)).getText();
							break;
						case "3":
							horaInicio=((JTextField)(comp)).getText();
							break;
						case "4":
							horaFin=((JTextField)(comp)).getText();
							break;
					}	
				}
			}
			
			horario.crearTarea(nombreTarea,diaSemana,horaInicio,horaFin);
			ventanaTarea.setVisible(false);
		});
		
		ventanaTarea.add(botonAsignarTarea);
	}
	
	//Crea la ventana para crear tareas
	public void crearVentanaTarea()
	{	
		ventanaTareaPropiedades();
		ventanaTareaElems();
		ventanaTareaBoton();
		
		//Carga la ventana con todo
		panel.add(ventanaTarea);
		ventanaTarea.setVisible(false);
		panel.revalidate();
		panel.repaint();
	}
}
