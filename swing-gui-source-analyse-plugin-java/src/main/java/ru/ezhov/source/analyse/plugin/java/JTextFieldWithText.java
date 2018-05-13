/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.ezhov.source.analyse.plugin.java;

import javax.swing.*;
import java.awt.*;

/**
 * @author RRNDeonisiusEZH
 */
public class JTextFieldWithText extends JTextField {

	private String text;

	public JTextFieldWithText(String text) {
		this.text = text;
		setToolTipText(text);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g); //To change body of generated methods, choose Tools | Templates.
		Graphics2D graphics2D = (Graphics2D) g;
		if (getText().length() == 0) {
			graphics2D.setPaint(Color.GRAY);
			graphics2D.drawString(text, 8, 15);
		}
	}
}
