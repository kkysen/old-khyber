package sen.khyber.apcs.swing;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class TemperatureConverterGUI extends JFrame implements ActionListener {
    
    private static final long serialVersionUID = -4338952373525015735L;

    private final Container pane = getContentPane();
    
    private final JPanel labels = new JPanel();
    private final JLabel celsiusLabel;
    private final JLabel fahrenheitLabel;
    
    private final JPanel temperatures = new JPanel();
    private final JTextField celsius;
    private final JTextField fahrenheit;
    
    private final JPanel converterButtons = new JPanel();
    private final JButton convertToCelsiusButton;
    private final JButton convertToFahrenheitButton;
    
    private final List<JPanel> panels = new ArrayList<>();
    
    private void setupFrame() {
        setTitle("Temperature Converter");
        setSize(600, 600);
        setLocation(100, 100);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    
    private void setupPane() {
        final LayoutManager layout;
        layout = new BoxLayout(pane, BoxLayout.Y_AXIS);
        //layout = new FlowLayout(FlowLayout.CENTER);
        pane.setLayout(layout);
    }
    
    private void setupPanels() {
        panels.add(labels);
        panels.add(temperatures);
        panels.add(converterButtons);
        for (final JPanel panel : panels) {
            panel.setLayout(new FlowLayout(FlowLayout.CENTER));
            pane.add(panel);
        }
    }
    
    public TemperatureConverterGUI() {
        setupFrame();
        setupPane();
        
        celsiusLabel = new JLabel("Celsius");
        fahrenheitLabel = new JLabel("Fahrenheit");
        labels.add(celsiusLabel);
        labels.add(fahrenheitLabel);
        
        celsius = new JTextField(10);
        fahrenheit = new JTextField(10);
        temperatures.add(celsius);
        temperatures.add(fahrenheit);
        
        convertToFahrenheitButton = new JButton("Convert to Fahrenheit");
        convertToFahrenheitButton.addActionListener(this);
        convertToCelsiusButton = new JButton("Convert to Celsius");
        convertToCelsiusButton.addActionListener(this);
        converterButtons.add(convertToFahrenheitButton);
        converterButtons.add(convertToCelsiusButton);
        
        setupPanels();
        
        pane.add(new JLabel("Swing sucks - I can't even make a simple newline!"));
    }
    
    public static double celsiusToFahrenheit(final double t) {
        return t * 9 / 5 + 32;
    }
    
    public static double fahrenheitToCelsius(final double t) {
        return (t - 32) * 5 / 9;
    }
    
    private void convert(final Function<Double, Double> converter, final JTextField originalText, final JTextField convertedText) {
        final String temperatureString = originalText.getText();
        if (temperatureString.isEmpty()) {
            return;
        }
        double temperature = 0;
        try {
            temperature = Double.parseDouble(originalText.getText());
        } catch (final NumberFormatException e) {
            return;
        }
        convertedText.setText(converter.apply(temperature).toString());
    }
    
    @Override
    public void actionPerformed(final ActionEvent e) {
        switch (e.getActionCommand()) {
            case "Convert to Fahrenheit":
                convert(TemperatureConverterGUI::celsiusToFahrenheit, celsius, fahrenheit);
                break;
            case "Convert to Celsius":
                convert(TemperatureConverterGUI::fahrenheitToCelsius, fahrenheit, celsius);
                break;
        }
    }
    
    public static void main(final String[] args) {
        final TemperatureConverterGUI g = new TemperatureConverterGUI();
        g.setVisible(true);
    }
    
}
