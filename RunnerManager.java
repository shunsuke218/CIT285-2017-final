// Created by Shunsuke Haga
// Inherited the script wrote by IntelliJ IDEA (JCIAN)

//package finalproject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.logging.Handler;
import java.text.NumberFormat;
import java.text.DecimalFormat;

public class RunnerManager extends JFrame {
	/* Variable Declaration */
    private static Logger log = Logger.getLogger(RunnerManager.class.getName());
	
    private RunnerDAO runnerDAO;

    private JList runnerList;
    private DefaultListModel runnerListModel;

    private JButton saveButton;

    private ArrayList<JTextField> editableTextFields;
    private ArrayList<JTextField> allTextFields;


	/* Constructor */
    public RunnerManager() {
        super("Runner Manager");
		System.out.println(RunnerManager.class.getName());

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
        setSize(400, 400);

        //the ID field does not get shown, but it gets used by this class
        JTextField idField = new JTextField("-1");
		JTextField NameField = addLabelAndTextField("Name", 100, true, centerPanel);
        JTextField GenderField = addLabelAndTextField("Gender", 10, true, centerPanel);
        JTextField PlaceOverallField = addLabelAndTextField("PlaceOverall", 10, true, centerPanel);
        JTextField PlacecGenderField = addLabelAndTextField("PlacecGender", 10, true, centerPanel);
        JTextField PlacecDivisionField = addLabelAndTextField("PlacecDivision", 10, true, centerPanel);
        JTextField CountryField = addLabelAndTextField("Country", 10, true, centerPanel);
        JTextField LocationField = addLabelAndTextField("Location", 10, true, centerPanel);
        JTextField BibField = addLabelAndTextField("Bib", 10, true, centerPanel);
        JTextField DivField = addLabelAndTextField("Div", 10, true, centerPanel);
        JTextField AgeField = addLabelAndTextField("Age", 10, true, centerPanel);
        JTextField HalfField = addLabelAndTextField("Half", 10, true, centerPanel);
        JTextField FinishField = addLabelAndTextField("Finish", 10, true, centerPanel);

        JTextField PacePerMileField = addLabelAndTextField("PacePerMile", 100, false, centerPanel);
        JTextField PacePerKiloField = addLabelAndTextField("PacePerKilo", 100, false, centerPanel);
        JTextField MilesPerHourField = addLabelAndTextField("MilesPerHour", 100, false, centerPanel);
        JTextField KiloPerHourField = addLabelAndTextField("KiloPerHour", 100, false, centerPanel);
		JTextField SubThreeField = addLabelAndTextField("Is sub-3?", 100, false, centerPanel);
        add(centerPanel, BorderLayout.CENTER);

		/* EAST PANEL */
        JPanel eastPanel = new JPanel(new GridLayout(2, 1));

        runnerListModel = new DefaultListModel();
        runnerList = new JList(runnerListModel);
        runnerList.setLayoutOrientation(JList.VERTICAL_WRAP);
        runnerList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        try {
            refreshRunnerList();
        } catch (Exception e) {
            handleFatalException(e);
        }
        eastPanel.add(runnerList);

        JPanel eastPanelBottom = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridy = 0;
        JButton viewButton = new JButton("View");
        viewButton.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent event) {
                        viewRunner();
                    }
                }
        );
        eastPanelBottom.add(viewButton, constraints);

        constraints.gridy = 1;
        JButton editButton = new JButton("Edit");
        editButton.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent event) {
                        editRunner();
                    }
                }
        );
        eastPanelBottom.add(editButton, constraints);

        constraints.gridy = 2;
        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent event) {
                        deleteRunner();
                    }
                }
        );
        eastPanelBottom.add(deleteButton, constraints);
        eastPanel.add(eastPanelBottom);
        add(eastPanel, BorderLayout.EAST);


		/* SOUTH PANEL */
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new GridLayout(1, 2));

        JButton newButton = new JButton("New");
        newButton.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent event) {
                        clearTextFields();
                        setFieldsEditable(true);
                        saveButton.setEnabled(true);
                    }
                }
        );
        southPanel.add(newButton);

        saveButton = new JButton("Save");
        saveButton.setEnabled(false);
        saveButton.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent event) {
                        try {
                            saveRunner();
                        } catch (Exception e) {
                            handleFatalException(e);
                        }
                    }
                }
        );
        southPanel.add(saveButton);
        add(southPanel, BorderLayout.SOUTH);

        //show the UI
        setVisible(true);
    }

	/* Methods */
    private void deleteRunner() {
        Runner runner = getSelectedRunner();
        if (runner != null) {
            int result = JOptionPane.showOptionDialog(this, "Are you sure you want to delete " + runner + "?", "Confirm Delete",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);

            if (result == JOptionPane.YES_OPTION) {
                try {
                    runnerDAO.deleteRunner(runner);
                    refreshRunnerList();
                } catch (Exception e) {
                    handleFatalException(e);
                }

                if (Integer.parseInt(idField.getText()) == runner.getId()) {
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

    private Runner getSelectedRunner() {
        int selectedIndex = runnerList.getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(null, "Please select an runner from the list.", "ERROR", JOptionPane.PLAIN_MESSAGE);
            return null;
        }

        return (Runner) runnerListModel.getElementAt(selectedIndex);
    }

    private void saveRunner() throws Exception {
        Runner runner = populateRunnerFromFields();
        if (runner != null) {
            //if the ID is -1, this is a new runner - otherwise it's an update
            if (runner.getId() != -1) {
                runnerDAO.updateRunner(runner);
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
            log.fine("Adding runner to list: " + runner);
            runnerListModel.addElement(runner);
        }
    }

    private void setFieldsEditable(boolean b) {
        for (JTextField textField : editableTextFields) {
            textField.setEditable(b);
        }
    }

    protected void clearTextFields() {
        for (JTextField textField : allTextFields) {
            textField.setText("");
        }

        idField.setText("-1");
    }

    private void handleFatalException(Exception e) {
        JOptionPane.showMessageDialog(null, e.getMessage(), "ERROR", JOptionPane.PLAIN_MESSAGE);
        System.exit(1);
    }

    private Runner populateRunnerFromFields() {
        try {
            Runner runner = new Runner();
            runner.setId(Integer.parseInt(idField.getText()));
            runner.setName(nameField.getText());
            runner.setAddress(addressField.getText());
            runner.setHours(getDoubleValue(hoursField.getText(), "Hours"));
            runner.setRate(getDoubleValue(rateField.getText(), "Rate"));

            String sex = sexField.getText();
            if (sex.length() > 0) {
                runner.setSex(sex.charAt(0));
            } else {
                runner.setSex(' ');
            }

            runner.setAge((int) getDoubleValue(ageField.getText(), "Age"));
            runner.setActive(Boolean.valueOf(activeField.getText()));

            return runner;
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(null, nfe.getMessage(), "ERROR", JOptionPane.PLAIN_MESSAGE);
            return null;
        }
    }

    private void populateFieldsFromRunner(Runner runner) {
        NumberFormat dollarsFormat = new DecimalFormat("$0.00");
        NumberFormat hoursFormat = new DecimalFormat("0.00");

        idField.setText(String.valueOf(runner.getId()));
        nameField.setText(runner.getName());
        addressField.setText(runner.getAddress());
        hoursField.setText(hoursFormat.format(runner.getHours()));
        rateField.setText(hoursFormat.format(runner.getRate()));
        sexField.setText(String.valueOf(runner.getSex()));
        ageField.setText(String.valueOf(runner.getAge()));
        activeField.setText(String.valueOf(runner.isActive()));
        grossPayField.setText(dollarsFormat.format(runner.calculateGrossPay()));
        federalTaxField.setText(dollarsFormat.format(runner.calculateFederalTax()));
        stateTaxField.setText(dollarsFormat.format(runner.calculateStateTax()));
        netPayField.setText(dollarsFormat.format(runner.calculateNetPay()));
    }

    private double getDoubleValue(String input, String fieldName) {
        try {
            return Double.valueOf(input);
        } catch (NumberFormatException e) {
            throw new NumberFormatException(fieldName + " must contain a numeric value!");
        }
    }

    private JTextField addLabelAndTextField(String label, int fieldLength, boolean textFieldIsEditable, JPanel panel) {
        panel.add(new JLabel(label));

        JTextField textField = new JTextField(fieldLength);
        textField.setEditable(false);
        panel.add(textField);

        if (textFieldIsEditable)
            editableTextFields.add(textField);

        allTextFields.add(textField);

        return textField;
    }

	/* Main function */
    public static void main(String[] args) {
        Level enabledLoggingLevel = Level.FINEST;

        Logger.getLogger("finalproject").setLevel(enabledLoggingLevel);

        Logger rootLogger = Logger.getLogger("");
        Handler[] handlers = rootLogger.getHandlers();
        for (Handler handler : handlers) {
            handler.setLevel(enabledLoggingLevel);
        }
        new RunnerManager();
    }
}
