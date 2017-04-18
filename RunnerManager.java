// Created by Shunsuke Haga
// Inherited the script wrote by IntelliJ IDEA (JCIAN)

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.*;//ConsoleHandler;
import java.util.Random;
/*
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
*/
//import java.text.NumberFormat;
//import java.text.DecimalFormat;

public class RunnerManager extends JFrame {
	/* Variable Declaration */
    static final Logger logger = Logger.getLogger(RunnerManager.class.getName());
	static final String file = "CIT-285_Shaga_final.log";

	
    private RunnerDAO runnerDAO;

    private JList runnerList;
    private DefaultListModel runnerListModel;

    private JButton saveButton;

    private ArrayList<JTextField> editableTextFields;
    private ArrayList<JTextField> allTextFields;

	private JTextField idField;
	private JTextField NameField;
	private JTextField GenderField;
	private JTextField PlaceOverallField;
	private JTextField PlaceGenderField;
	private JTextField PlaceDivisionField;
	private JTextField CountryField;
	private JTextField LocationField;
	private JTextField BibField;
	private JTextField DivField;
	private JComboBox DivBox;
	private JTextField AgeField;
	private JTextField HalfField;
	private JTextField FinishField;

	private JTextField PacePerMileField;
	private JTextField PacePerKiloField;
	private JTextField MilesPerHourField;
	private JTextField KiloPerHourField;
	//private JTextField SubThreeField;
	private JCheckBox SubThreeCheckBox;
	private String[] labelStrings = { "16-19", "18-24", "20-24", "25-29", "30-34", "35-39", "40-44", "45-49", "50-54", "55-59", "60-64", "65-69" };
	

	
	////////////////////////////////////////////////////
	// CONSTRUCTOR/LAUNCH GUI
	////////////////////////////////////////////////////
    public RunnerManager() {
        super("Runner Manager");
        try {
            runnerDAO = new RunnerDAO();
        } catch (Exception e) {
            handleFatalException(e);
        }
        editableTextFields = new ArrayList<JTextField>();
        allTextFields = new ArrayList<JTextField>();

		/* CENTER PANEL */
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(17, 2));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(600, 400);

        //the ID field does not get shown, but it gets used by this class
        idField = new JTextField("-1");
		NameField = addLabelAndTextField("Name", 100, true, centerPanel);
        GenderField = addLabelAndTextField("Gender", 10, true, centerPanel);
        PlaceOverallField = addLabelAndTextField("PlaceOverall", 10, true, centerPanel);
        PlaceGenderField = addLabelAndTextField("PlaceGender", 10, true, centerPanel);
        PlaceDivisionField = addLabelAndTextField("PlaceDivision", 10, true, centerPanel);
        CountryField = addLabelAndTextField("Country", 10, true, centerPanel);
        LocationField = addLabelAndTextField("Location", 10, true, centerPanel);
        BibField = addLabelAndTextField("Bib", 10, true, centerPanel);
        //DivField = addLabelAndTextField("Div", 10, true, centerPanel);
		DivBox = addLabelAndComboBox("Div", centerPanel);
		AgeField = addLabelAndTextField("Age", 10, true, centerPanel);
        HalfField = addLabelAndTextField("Half", 10, true, centerPanel);
        FinishField = addLabelAndTextField("Finish", 10, true, centerPanel);
        PacePerMileField = addLabelAndTextField("PacePerMile", 100, false, centerPanel);
        PacePerKiloField = addLabelAndTextField("PacePerKilo", 100, false, centerPanel);
        MilesPerHourField = addLabelAndTextField("MilesPerHour", 100, false, centerPanel);
        KiloPerHourField = addLabelAndTextField("KiloPerHour", 100, false, centerPanel);
		//SubThreeField = addLabelAndTextField("Is sub-3?", 100, false, centerPanel);
		SubThreeCheckBox = addLabelAndCheckBox("Is sub-3?", false, centerPanel);

        add(centerPanel, BorderLayout.CENTER);
		System.out.println("CENTER is ready");

		/* EAST PANEL */
        JPanel eastPanel = new JPanel(new GridLayout(2, 1));
		JScrollPane scrollPane = new JScrollPane();
        runnerListModel = new DefaultListModel();
        runnerList = new JList(runnerListModel);
		runnerList.setLayoutOrientation(JList.VERTICAL);
        runnerList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        try {
            refreshRunnerList();
        } catch (Exception e) {
            handleFatalException(e);
        }
		scrollPane.setViewportView(runnerList);
        eastPanel.add(scrollPane);
		// Bottom of East Panel
        JPanel eastPanelBottom = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridy = 0;
        JButton viewButton = new JButton("View");
        viewButton.addActionListener(new ActionListener()
			{ public void actionPerformed(ActionEvent event) { viewRunner(); }});
        eastPanelBottom.add(viewButton, constraints);

		/*
        constraints.gridy = 1;
        JButton editButton = new JButton("Edit");
        editButton.addActionListener(new ActionListener()
			{ public void actionPerformed(ActionEvent event) { editRunner(); }});
        eastPanelBottom.add(editButton, constraints);

        constraints.gridy = 2;
        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new ActionListener()
			{ public void actionPerformed(ActionEvent event) { deleteRunner(); }});
        eastPanelBottom.add(deleteButton, constraints);
		*/
		
        constraints.gridy = 1;
        JButton randomButton = new JButton("Random");
        randomButton.addActionListener(new ActionListener()
			{ public void actionPerformed(ActionEvent event) { randomRunner(); }});
        eastPanelBottom.add(randomButton, constraints);

        constraints.gridy = 2;
        JButton meButton = new JButton("Me!");
        meButton.addActionListener(new ActionListener()
			{ public void actionPerformed(ActionEvent event) { findMe(); }});
        eastPanelBottom.add(meButton, constraints);

        eastPanel.add(eastPanelBottom);
        add(eastPanel, BorderLayout.EAST);
		System.out.println("EAST is ready");

		/* SOUTH PANEL */
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new GridLayout(1, 2));

        JButton newButton = new JButton("New");
        newButton.addActionListener(new ActionListener()
			{ public void actionPerformed(ActionEvent event)
				{
					clearTextFields();
					setFieldsEditable(true);
					saveButton.setEnabled(true);
				}
			});
        //southPanel.add(newButton);

        saveButton = new JButton("Save");
        saveButton.setEnabled(false);
        saveButton.addActionListener( new ActionListener()
			{ public void actionPerformed(ActionEvent event)
				{
					try {
						saveRunner();
					} catch (Exception e) {
						handleFatalException(e);
					}
				}
			});
        //southPanel.add(saveButton);

        add(southPanel, BorderLayout.SOUTH);

		System.out.println("GUI is ready");
        //show the UI
        setVisible(true);
    }



	////////////////////////////////////////////////////
	// METHOD BLOCK
	////////////////////////////////////////////////////
	
	// when creates a text with a field
    private JTextField addLabelAndTextField(String label, int fieldLength, boolean textFieldIsEditable, JPanel panel) {
        panel.add(new JLabel(label));
        JTextField textField = new JTextField(fieldLength);
        textField.setEditable(false);
        panel.add(textField);
        if (textFieldIsEditable)
            editableTextFields.add(textField);
        return textField;
    }
	// when creates a checkbox with a field
    private JCheckBox addLabelAndCheckBox(String label, boolean checkBoxIsEditable, JPanel panel) {
		JCheckBox checkBox = new JCheckBox(label);
        //checkBox.setEnable(false);
        panel.add(checkBox);
        return checkBox;
    }

    private JComboBox addLabelAndComboBox(String label, JPanel panel) {
		panel.add(new JLabel(label));
		JComboBox comboBox = new JComboBox(labelStrings);
		comboBox.setSelectedIndex(0);
        panel.add(comboBox);
        return comboBox;
    }

	// Manipulate text fields
    private void setFieldsEditable(boolean b) {
        for (JTextField textField : editableTextFields) {
            textField.setEditable(b);
        }
    }

    protected void clearTextFields() {
        for (JTextField textField : allTextFields) {
            textField.setText("");
        }
		SubThreeCheckBox.setSelected(false);
        idField.setText("-1");
    }

    private void populateFieldsFromRunner(Runner runner) {
		GenderField.setText(String.valueOf(runner.getGender()));
		PlaceOverallField.setText(String.valueOf(runner.getPlaceOverall()));
		PlaceGenderField.setText(String.valueOf(runner.getPlaceGender()));
		PlaceDivisionField.setText(String.valueOf(runner.getPlaceDivision()));
		NameField.setText(runner.getName());
		CountryField.setText(runner.getCountry());
		LocationField.setText(runner.getLocation());
		BibField.setText(String.valueOf(runner.getBib()));
		//DivField.setText(runner.getDiv());
		DivBox.setSelectedItem(String.valueOf(runner.getDiv()));
		AgeField.setText(String.valueOf(runner.getAge()));
		HalfField.setText(runner.getHalfAsString().substring(1));
		FinishField.setText(runner.getFinishAsString().substring(1));

		PacePerMileField.setText(String.valueOf(runner.getPacePerMileAsString()));
		PacePerKiloField.setText(String.valueOf(runner.getPacePerKiloAsString()));
		MilesPerHourField.setText(runner.getMilesPerHourAsString());
		KiloPerHourField.setText(runner.getKiloPerHourAsString());
		//SubThreeField.setText(String.valueOf(runner.getIsSubThree()));
		SubThreeCheckBox.setSelected(runner.getIsSubThree());
    }
    private Runner populateRunnerFromFields() {
        try {
			char Gender = GenderField.getText().charAt(0);
			int PlaceOverall = Integer.valueOf(PlaceOverallField.getText());
			int PlaceGender = Integer.valueOf(PlaceGenderField.getText());
			int PlaceDivision = Integer.valueOf(PlaceDivisionField.getText());
			String Name = NameField.getText();
			String Country = CountryField.getText();
			String Location = LocationField.getText();
			int Bib = Integer.valueOf(BibField.getText());
			String Div = DivBox.getSelectedItem().toString();
			//String Div = DivField.getText();
			int Age = Integer.valueOf(AgeField.getText());
			String Half = HalfField.getText();
			String Finish = FinishField.getText();
			if (!Name.contains(" "))
				throw new Exception("First name and last name should be separated by space.");
			String FirstName = Name.split(" ", 0)[0];String LastName = Name.split(" ", 0)[1];
			if (Gender != 'M' && Gender != 'F')
				throw new Exception("Gender have to be either 'M' or 'F'!");
			if (PlaceOverall < 1 || PlaceOverall > 50000 )
				throw new Exception("PlaceOverAll should be 1 ~ 50000");
			if (PlaceGender < 1 || PlaceGender > 50000 )
				throw new Exception("PlaceGender should be 1 ~ 50000");
			if (PlaceDivision < 1 || PlaceDivision > 50000 )
				throw new Exception("PlaceDivision should be 1 ~ 50000");
			if (!Country.matches("[A-Z]{3}") && !Country.isEmpty())
				throw new Exception("Country should be 3 CAPS (like USA) or none.");
			if (Bib < 1 || Bib > 50000 )
				throw new Exception("Bib should be 1 ~ 50000");
			if (Age < 19 || Age > 100 )
				throw new Exception("Age should be 19 ~ 100");
			if (!Half.matches("\\d{1}:\\d{2}:\\d{2}"))
				throw new Exception("Half should be in the form of '1:23:45'.");
			if (!Finish.matches("\\d{1}:\\d{2}:\\d{2}"))
				throw new Exception("Finish should be in the form of '1:23:45'.");
            Runner runner = new RunnerBuilder()
				.Gender(Gender)
				.PlaceOverall(PlaceOverall)
				.PlaceGender(PlaceGender)
				.PlaceDivision(PlaceDivision)
				.LastName(LastName)
				.FirstName(FirstName)
				.Country(Country)
				.Location(Location)
				.Bib(Bib)
				.Div(Div)
				.Age(Age)
				.Half(Half)
				.Finish(Finish)
				.buildRunner();
            return runner;
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(null, nfe.getMessage(), "ERROR", JOptionPane.PLAIN_MESSAGE);
            return null;
        } catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "ERROR", JOptionPane.PLAIN_MESSAGE);
		}
		return null;
    }

	// Runner related methods
	private Runner getSelectedRunner() {
        int selectedIndex = runnerList.getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(null, "Please select an runner from the list.", "ERROR", JOptionPane.PLAIN_MESSAGE);
            return null;
        }
        return (Runner) runnerListModel.getElementAt(selectedIndex);
    }

	private void findMe() {
		runnerList.setSelectedIndex(4343);
		viewRunner();
    }

    private void randomRunner() {
		int maxsize = runnerListModel.getSize();
		runnerList.setSelectedIndex(new Random().nextInt(maxsize));
		viewRunner();
    }
	
    private void deleteRunner() {
        Runner runner = getSelectedRunner();
        if (runner != null) {
            int result = JOptionPane
				.showOptionDialog
				(this, "Are you sure you want to delete " + runner + "?", "Confirm Delete",
				 JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
            if (result == JOptionPane.YES_OPTION) {
                try {
                    runnerDAO.deleteRunner(runner);
                    refreshRunnerList();
                } catch (Exception e) {
                    handleFatalException(e);
                }
                if (Integer.parseInt(idField.getText()) == runner.getPlaceOverall()) {
                    clearTextFields();
                    setFieldsEditable(false);
                    saveButton.setEnabled(false);
                }
            }
        }
    }

    private void editRunner() {
        Runner runner = getSelectedRunner();
        if (runner != null) {
            clearTextFields();
            setFieldsEditable(true);
            populateFieldsFromRunner(runner);
            saveButton.setEnabled(true);
        }
    }

    private void viewRunner() {
        Runner runner = getSelectedRunner();
        if (runner != null) {
            clearTextFields();
            setFieldsEditable(false);
            populateFieldsFromRunner(runner);
            saveButton.setEnabled(false);
        }
    }

    private void saveRunner() throws Exception {
        Runner runner = populateRunnerFromFields();
        if (runner != null) {
            //if the ID is -1, this is a new runner - otherwise it's an update
            if (runner.getPlaceOverall() != 49999) {
                runnerDAO.editRunner(runner);
            } else {
                runnerDAO.addRunner(runner);
            }
            clearTextFields();
            setFieldsEditable(false);
            refreshRunnerList();
            saveButton.setEnabled(false);
        }
    }

    private void refreshRunnerList() throws Exception {
        List<Runner> runners = runnerDAO.getAllRunners();
		runnerListModel.clear();
        for (Runner runner : runners) {
            logger.fine("Adding runner to list: " + runner);
            runnerListModel.addElement(runner);
        }
    }


	// Handles error
	private void handleFatalException(Exception e) {
        JOptionPane.showMessageDialog(null, e.getMessage(), "ERROR", JOptionPane.PLAIN_MESSAGE);
        System.exit(1);
    }

	////////////////////////////////////////////////////
	// MAIN FUNCTION
	////////////////////////////////////////////////////
    public static void main(String[] args) {
		try {
		FileHandler fileHandler = new FileHandler(file, false);
		fileHandler.setFormatter(new SimpleFormatter());
		logger.addHandler(fileHandler);
		logger.setLevel(Level.FINEST);
 
		ConsoleHandler consoleHandler = new ConsoleHandler();
		consoleHandler.setLevel(Level.CONFIG);      
		logger.addHandler(consoleHandler);
 
		logger.setUseParentHandlers(false);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			logger.log(Level.FINER, "ERROR!", e);
		}
	  //Logger.getLogger("finalproject").setLevel(Level.FINEST);
		
		/*
        Logger rootLogger = Logger.getLogger("");
        Handler[] handlers = rootLogger.getHandlers();
        for (Handler handler : handlers) {
            handler.setLevel(enabledLoggingLevel);
        }
		*/
        new RunnerManager();
    }
}
