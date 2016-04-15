package Leitor;
import java.awt.event.*; 

import com.jogamp.opengl.DebugGL2;


import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.gl2.GLUT;

import jogamp.opengl.*;
import jogamp.opengl.glu.*;
import jogamp.opengl.util.*;
import OBJLoader.*;


	
	/**
	 * Classe Renderer, junto com a classe ExemploJava, mostra um exemplo de 
	 * como trabalhar com Java e OpenGL utilizando a API JOGL. É aberta uma 
	 * janela na qual é desenhada uma casinha. É possível fazer 
	 * zoom in e zoom out usando o mouse, e mover a posição do observador 
	 * virtual com as teclas de setas, HOME e END.
	 * 
	 * @author Marcelo Cohen
	 * @version 1.0
	 */



	public class Renderer extends MouseAdapter implements GLEventListener, KeyListener
	{
		// Atributos
		private GL gl;
		private GLU glu;
		private GLUT glut;
		private GLAutoDrawable glDrawable;
		private double angle, fAspect;
		private float rotX, rotY, obsZ;
	  	private OBJModel model;
		private String name;
		private float maxSize;

	  	private static final float MAX_SIZE = 1.0f;  // for a model's dimension

		/**
		 * Construtor da classe Renderer que não recebe parâmetros.
		 */
		public Renderer(String name)
		{
			// Especifica o ângulo da projeção perspectiva  
			angle=60;
			// Posição do observador virtual
			rotX = 5;
			rotY = 0;
			obsZ = 2; 
			// Inicializa o valor para correção de aspecto   
			fAspect = 1;    
			this.name = name;
			this.maxSize = MAX_SIZE;
		}

		/**
		 * Método definido na interface GLEventListener e chamado pelo objeto no qual será feito o desenho
		 * logo após a inicialização do contexto OpenGL. 
		 */    
		public void init(GLAutoDrawable drawable)
		{
			glDrawable = drawable;
			gl = drawable.getGL();
			// glu = drawable.getGLU();       
			glu = new GLU();
			glut = new GLUT();

			drawable.setGL(new DebugGL2((GL2) gl));        

			// Carrega o modelo
			model = new OBJModel(name, maxSize, gl, true);

			gl.glClearColor(0.0f, 0.0f, 0.7f, 1.0f);

			// Habilita GL_COLOR_MATERIAL, que significa alterar a cor do material
			// quando a cor corrente é alterada via glColor...
			gl.glEnable(GL.GL_COLOR_MATERIAL);
			gl.glColorMaterial(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT_AND_DIFFUSE);

			// Habilita iluminação
			gl.glEnable(GL.GL_LIGHTING);

			// Habilita luz 0
			gl.glEnable(GL.GL_LIGHT0);

			// Habilita Z-Buffer
			gl.glEnable(GL.GL_DEPTH_TEST);

			// Habilita tonalização Gouraud
			gl.glShadeModel(GL.GL_SMOOTH);         
	    
			gl.glTexEnvi(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_MODULATE);

			// Especifica sistema de coordenadas de projeção
			gl.glMatrixMode(GL.GL_PROJECTION);
			// Inicializa sistema de coordenadas de projeção
			gl.glLoadIdentity();
			// Especifica a projeção perspectiva(angulo,aspecto,zMin,zMax)
			glu.gluPerspective(angle, fAspect, 0.2, 800);
			// Volta para Modelview
			gl.glMatrixMode(GL.GL_MODELVIEW);

		}

		/**
		 * Método definido na interface GLEventListener e chamado pelo objeto no qual será feito o desenho
		 * para começar a fazer o desenho OpenGL pelo cliente.
		 */  
		public void display(GLAutoDrawable drawable)
		{
			gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT );
			//gl.glLoadIdentity();    

			defineIluminacao();
			especificaParametrosVisualizacao();

			gl.glDisable(GL.GL_COLOR_MATERIAL);
			
	    	model.drawList(gl);      // draw the model
	    	
	    	gl.glEnable(GL.GL_COLOR_MATERIAL);
			
		}

		/**
		 * Método definido na interface GLEventListener e chamado pelo objeto no qual será feito o desenho
		 * depois que a janela foi redimensionada.
		 */  
		public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height)
		{
			gl.glViewport(0, 0, width, height);
			fAspect = (float)width/(float)height;      
		}

		/**
		 * Método definido na interface GLEventListener e chamado pelo objeto no qual será feito o desenho
		 * quando o modo de exibição ou o dispositivo de exibição associado foi alterado.
		 */  
		public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) { }

		/**
		 * Método responsável pela especificação dos parâmetros de iluminação.
		 */     
		public void defineIluminacao ()
		{    
			float luzAmbiente[]={0.3f, 0.3f, 0.3f, 1.0f}; 
			float luzDifusa[]={0.7f, 0.7f, 0.7f, 1.0f};  
			float luzEspecular[]={0.8f, 0.8f, 0.8f, 1.0f};
			float posicaoLuz[]={100.0f, 200.0f, 100.0f, 1.0f};  
			float direcaoLuz[]={-10.0f, -20.0f, -10.0f, 1.0f};

			// Capacidade de brilho do material
			float especularidade[]={1.0f, 1.0f, 1.0f, 1.0f};
			int especMaterial = 60;

			// Define a reflectância do material 
			gl.glMaterialfv(GL.GL_FRONT, GL.GL_SPECULAR, especularidade, 0);
			// Define a concentração do brilho
			gl.glMateriali(GL.GL_FRONT, GL.GL_SHININESS, especMaterial);

			// Ativa o uso da luz ambiente 
			gl.glLightModelfv(GL.GL_LIGHT_MODEL_AMBIENT, luzAmbiente, 0);

			// Define os parâmetros da luz de número 0
			gl.glLightfv(GL.GL_LIGHT0, GL.GL_AMBIENT, luzAmbiente, 0); 
			gl.glLightfv(GL.GL_LIGHT0, GL.GL_DIFFUSE, luzDifusa, 0 );
			gl.glLightfv(GL.GL_LIGHT0, GL.GL_SPECULAR, luzEspecular, 0);
			gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, posicaoLuz, 0 );   
			gl.glLightfv(GL.GL_LIGHT0, GL.GL_SPOT_DIRECTION, direcaoLuz, 0 ); 
		}

		/**
		 * Método usado para especificar a posição do observador virtual.
		 */    
		public void posicionaObservador()
		{
			// Especifica sistema de coordenadas do modelo
			gl.glMatrixMode(GL.GL_MODELVIEW);
			// Inicializa sistema de coordenadas do modelo  
			gl.glLoadIdentity();
			// Especifica posição do observador e do alvo   
			gl.glTranslatef(0.0f, 0.0f, -obsZ);    
			gl.glRotatef(rotX, 1.0f, 0.0f, 0.0f);  
			gl.glRotatef(rotY, 0.0f, 1.0f, 0.0f);
		}
	   
		/**
		 * Método usado para especificar o volume de visualização.
		 */    
		public void especificaParametrosVisualizacao()
		{
			// Especifica sistema de coordenadas de projeção
			gl.glMatrixMode(GL.GL_PROJECTION);
			// Inicializa sistema de coordenadas de projeção
			gl.glLoadIdentity();
			// Especifica a projeção perspectiva(angulo,aspecto,zMin,zMax)
			glu.gluPerspective(angle, fAspect, 0.2, 800);
					
			posicionaObservador();
		}

		/**
		 * Método da classe MouseAdapter que está sendo sobrescrito para gerenciar os 
		 * eventos de clique de mouse, de maneira que sejá feito zoom in e zoom out.
		 */  
		public void mouseClicked(MouseEvent e)
		{
			if (e.getButton() == MouseEvent.BUTTON1) // Zoom in
			{
				if (angle >= 12) 
					angle -= 2;
			}
			else if (e.getButton() == MouseEvent.BUTTON3) // Zoom out
			{
				if (angle <= 72) 
					angle += 2;
			}
			glDrawable.display();
		}

		/**
		 * Método definido na interface KeyListener que está sendo implementado para, 
		 * de acordo com as teclas pressionadas, permitir mover a posição do observador
		 * virtual.
		 */        
		public void keyPressed(KeyEvent e)
		{
			switch (e.getKeyCode())
			{
				case KeyEvent.VK_RIGHT:		rotY++;   
											break;
				case KeyEvent.VK_LEFT:		rotY--;   
											break;     
				case KeyEvent.VK_UP:		rotX++;   
											break;                              
				case KeyEvent.VK_DOWN:		rotX--;   
											break;                               
				case KeyEvent.VK_HOME:		obsZ++;   
											break;                                  
				case KeyEvent.VK_END:		obsZ--;   
											break;  
				case KeyEvent.VK_ESCAPE:	System.exit(0);
											break;
			}  
			glDrawable.display();
		}

		/**
		 * Método definido na interface KeyListener.
		 */      
		public void keyTyped(KeyEvent e) { }

		/**
		 * Método definido na interface KeyListener.
		 */       
		public void keyReleased(KeyEvent e) { }

		

		@Override
		public void dispose(GLAutoDrawable arg0) {
			// TODO Auto-generated method stub
			
		}

					
		}
	}



