package Leitor;
/**
 * Classe ExemploObj, junto com a classe Renderer, mostra um exemplo de 
 * como trabalhar com Java e OpenGL utilizando a API JOGL. � aberta uma 
 * janela na qual � desenhado um objeto carregado de um arquivo .obj,
 * com ilumina��o e texturas, se houver. � poss�vel fazer zoom in e
 * zoom out usando o mouse, e mover a posi��o do observador 
 * virtual com as teclas de setas, HOME e END.
 * 
 * @author Marcelo Cohen, Isabel Harb Manssour
 * @version 1.0
 */

import javax.swing.*;
import java.awt.*; 
import java.awt.event.*; 
import javax.media.opengl.*;

public class ExemploObj
{
	private Renderer renderer;

	/**
	 * Construtor da classe ExemploObj que n�o recebe par�metros. Cria uma janela e insere  
	 * um componente canvas OpenGL.
	 */
	public ExemploObj()
	{
		// Cria janela
		JFrame janela = new JFrame("Leitura de Objetos");   
		janela.setBounds(50,100,500,500); 
		janela.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		BorderLayout layout = new BorderLayout(); 
		Container caixa=janela.getContentPane();
		caixa.setLayout(layout); 

		// Cria um objeto GLCapabilities para especificar o n�mero de bits 
		// por pixel para RGBA
		GLCapabilities c = new GLCapabilities();
		c.setRedBits(8);
		c.setBlueBits(8);
		c.setGreenBits(8);
		c.setAlphaBits(8); 

		// Pede que o usu�rio digite o nome do arquivo obj.
		String name;
		name = JOptionPane.showInputDialog(null, "Digite o nome do arquivo (sem colocar extens�o):");
				
		
		// Cria o objeto que ir� gerenciar os eventos
		Renderer renderer = new Renderer(name);

		// Cria um canvas, adiciona na janela, e especifica o objeto "ouvinte" 
		// para os eventos Gl, de mouse e teclado
		// GLCanvas canvas = GLDrawableFactory.getFactory().createGLCanvas(c);
		GLCanvas canvas = new GLCanvas(c);
		janela.add(canvas,BorderLayout.CENTER);
		canvas.addGLEventListener(renderer);        
		canvas.addMouseListener(renderer);
		canvas.addKeyListener(renderer);
		janela.setVisible(true);
		canvas.requestFocus();
	}

	/**
	 * M�todo main que apenas cria um objeto ExemploObj
	 */
	public static void main(String args[])
	{
		ExemploObj eo = new ExemploObj();
	}
}
