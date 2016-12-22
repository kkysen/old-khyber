package sen.khyber.apcs.swing;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class TemperatureConverter extends JFrame implements ActionListener {
    
    private final Container pane;
    
    private final JButton convertToCelsiusButton;
    private final JButton convertToFahrenheitButton;
    
    private final JTextField celsius;
    private final JTextField fahrenheit;
    
    private final JLabel celsiusLabel;
    private final JLabel fahrenheitLabel;
    
    public TemperatureConverter() {
        setTitle("Temperature Converter");
        setSize(600, 600);
        setLocation(100, 100);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        pane = getContentPane();
        pane.setLayout(new FlowLayout());
        
        convertToCelsiusButton = new JButton("Convert to Celsius");
        convertToFahrenheitButton = new JButton("Convert to Fahrenheit");
        
        celsius = new JTextField(10);
        fahrenheit = new JTextField(10);
        
        celsiusLabel = new JLabel("Celsius");
        fahrenheitLabel = new JLabel("Fahrenheit");
        
        pane.add(celsiusLabel);
        pane.add(fahrenheitLabel);
        
        pane.add(celsius);
        pane.add(fahrenheit);
        
        pane.add(convertToFahrenheitButton);
        pane.add(convertToCelsiusButton);
    }
    
    public static double CtoF(final double t) {
        return t * 9 / 5 + 32;
    }
    
    public static double FtoC(final double t) {
        return (t - 32) * 5 / 9;
    }
    
    @Override
    public void actionPerformed(final ActionEvent e) {
        final String cmd = e.getActionCommand();
        System.out.println("cmd: " + cmd);
    }
    
    public static void main(final String[] args) {
        final TemperatureConverter g = new TemperatureConverter();
        g.setVisible(true);
    }
    
}
