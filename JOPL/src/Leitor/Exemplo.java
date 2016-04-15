package Leitor;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class Exemplo {
	
	public static void main(String[] args){
	JFrame janela = new JFrame("Desenho de um torus 3D com Iluminação");
	janela.setBounds(50,100,500,500);
	janela.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	BorderLayout layout =new BorderLayout();
	Container caixa= janela.getContentPane();
	caixa.setLayout(layout);
}
}