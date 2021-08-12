package com.cn;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.*;

import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

import javax.swing.JLabel;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;

public class View {

	private JFrame frame;
	private SpellChecker spellChecker;
	private Map<Integer, Integer> map;
	private Highlighter highlighter;
	private List<String> show;
	private Integer i;
	JTextArea suggestionsArea;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					View window = new View();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public View() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		spellChecker = new SpellChecker();
		map = new HashMap<>();
		show = new ArrayList<>();
		i = 0;
		frame = new JFrame();
		frame.setBounds(100, 100, 920, 653);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JPanel topPane = new JPanel();
		topPane.setBounds(10, 10, 886, 489);
		frame.getContentPane().add(topPane);
		topPane.setLayout(null);
		
	    //the input pane
		JTextArea inputPane = new JTextArea();
		inputPane.setLineWrap(true);
		inputPane.setWrapStyleWord(true);
		JScrollPane inPutScrollPane = new JScrollPane(inputPane);
		inPutScrollPane.setBounds(10, 10, 310, 469);
		topPane.add(inPutScrollPane);
		
	    //the output pane after detection
		JTextArea outputPane = new JTextArea();
		outputPane.setLineWrap(true);
		outputPane.setWrapStyleWord(true);
		outputPane.setEnabled(false);
		outputPane.setDisabledTextColor(Color.BLACK);
		JScrollPane outPutScrollPane = new JScrollPane(outputPane);
		outPutScrollPane.setBounds(566, 10, 310, 469);
		topPane.add(outPutScrollPane);
		highlighter	= outputPane.getHighlighter();
		
	    // the button it starts to detect grammar
		JButton checkBotton = new JButton("Check Button");
		checkBotton.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 25));
		checkBotton.setBounds(341, 87, 197, 49);
		topPane.add(checkBotton);
		// the check button to bind the trigger 
		checkBotton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				i = 0;
				String inputString = inputPane.getText();

				String[] strings = inputString.trim().split(" ");
				for(String s : strings){
					String start = null;
					try {
						start = spellChecker.start(s);
					} catch (IOException ex) {
						ex.printStackTrace();
					}
					if(start != null){
						show.add(start);
						int i = inputString.indexOf(s);
						map.put(i, s.length());
					}
				}
				
			   //output in the output pane when get the out data
				if(map.isEmpty()) {
					suggestionsArea.setText("There is no spelling mistakes!");
					outputPane.setText(inputString);
				}
				else {
					Set<Integer> integers = map.keySet();
					outputPane.setText(inputString);
					for(Integer i : integers){
						try {
							highlighter.addHighlight(i, i+map.get(i), new DefaultHighlighter.DefaultHighlightPainter(Color.RED));
						} catch (BadLocationException ex) {
							ex.printStackTrace();
						}
					}
					if(show.isEmpty()){
						suggestionsArea.setText("There is no spelling mistakes!");
					}else {
						suggestionsArea.setText(show.get(0));
					}
				}
			}
		});
		
		
	    // the list to detect the algorithm
		JComboBox selection = new JComboBox();
		selection.setModel(new DefaultComboBoxModel(new String[] {"algorithm"}));
		selection.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 20));
		selection.setBounds(341, 193, 204, 49);
		topPane.add(selection);
		
		JLabel generateLabel = new JLabel("Generate Suggestions");
		generateLabel.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 18));
		generateLabel.setBounds(341, 362, 204, 49);
		topPane.add(generateLabel);
		
		JPanel bottomPane = new JPanel();
		bottomPane.setBounds(10, 509, 886, 97);
		frame.getContentPane().add(bottomPane);
		bottomPane.setLayout(null);
		
	    // the area to show suggestion
		suggestionsArea = new JTextArea();
		suggestionsArea.setLineWrap(true);
		suggestionsArea.setWrapStyleWord(true);
		suggestionsArea.setEnabled(false);
		suggestionsArea.setDisabledTextColor(Color.BLACK);
		JScrollPane suggestionScrollPane = new JScrollPane(suggestionsArea);
		suggestionScrollPane.setBounds(10, 10, 866, 77);
		bottomPane.add(suggestionScrollPane);
		
	    // the next button to change suggestions
		JButton next = new JButton("NextSuggestion");
		next.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 25));
		next.setBounds(330, 421, 226, 49);

		next.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				++i;
				if(i < show.size()){
					suggestionsArea.setText(show.get(i));
				}else{
					show.clear();
					map.clear();
				}
			}
		});
		topPane.add(next);
		
	}
	
}
