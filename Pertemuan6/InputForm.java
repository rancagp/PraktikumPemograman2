package Pertemuan6;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class InputForm extends JFrame {
    private JTextField textField;
    private JTextArea textArea;
    private JRadioButton radioButton1, radioButton2;
    private JCheckBox checkBox1, checkBox2;
    private JComboBox<String> comboBox;
    private JList<String> list;
    private DefaultTableModel tableModel;
    private JTable dataTable;

    // Menambahkan JSlider dan JSpinner
    private JSlider jSlider;
    private JSpinner jSpinner;

    public InputForm() {
        setTitle("Form Input");
        setSize(800, 600); 
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());
        
        // Menambahkan menu bar
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Menu");
        
        // Menu Item untuk membuka tabel
        JMenuItem menuItemTable = new JMenuItem("Data Inputan Form");
        menuItemTable.addActionListener(e -> showTableWindow());
        menu.add(menuItemTable);
        
        // Menambahkan item menu Exit
        JMenuItem menuItemExit = new JMenuItem("Exit");
        menuItemExit.addActionListener(e -> System.exit(0)); // Menutup aplikasi
        menu.add(menuItemExit);
        
        menuBar.add(menu);
        setJMenuBar(menuBar);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); 

        // Panel untuk input data
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("FORM PENDAFTARAN UKM"));
        GridBagConstraints inputGbc = new GridBagConstraints();
        inputGbc.insets = new Insets(5, 5, 5, 5); 
        inputGbc.fill = GridBagConstraints.BOTH;

        // JTextField
        inputGbc.gridx = 0;
        inputGbc.gridy = 0;
        inputPanel.add(new JLabel("Nama:"), inputGbc);

        inputGbc.gridx = 1;
        inputGbc.weightx = 1.0; 
        textField = new JTextField();
        inputPanel.add(textField, inputGbc);

        // JTextArea
        inputGbc.gridx = 0;
        inputGbc.gridy = 1;
        inputPanel.add(new JLabel("Alamat:"), inputGbc);

        inputGbc.gridx = 1;
        inputGbc.weighty = 1.0; 
        textArea = new JTextArea(5, 20); 
        textArea.setLineWrap(true); 
        textArea.setWrapStyleWord(true); 
        JScrollPane textAreaScrollPane = new JScrollPane(textArea);
        inputPanel.add(textAreaScrollPane, inputGbc);

        // JRadioButton
        inputGbc.gridx = 0;
        inputGbc.gridy = 2;
        inputPanel.add(new JLabel("Jenis Kelamin:"), inputGbc);

        inputGbc.gridx = 1;
        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        radioButton1 = new JRadioButton("Laki-Laki");
        radioButton2 = new JRadioButton("Perempuan");
        ButtonGroup group = new ButtonGroup();
        group.add(radioButton1);
        group.add(radioButton2);
        radioPanel.add(radioButton1);
        radioPanel.add(radioButton2);
        inputPanel.add(radioPanel, inputGbc);

        // JCheckBox
        inputGbc.gridx = 0;
        inputGbc.gridy = 3;
        inputPanel.add(new JLabel("Pengalaman:"), inputGbc);

        inputGbc.gridx = 1;
        JPanel checkPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        checkBox1 = new JCheckBox("Tidak Memiliki Pengalaman");
        checkBox2 = new JCheckBox("Memiliki Pengalaman");
        checkPanel.add(checkBox1);
        checkPanel.add(checkBox2);
        inputPanel.add(checkPanel, inputGbc);

        // JComboBox
        inputGbc.gridx = 0;
        inputGbc.gridy = 4;
        inputPanel.add(new JLabel("Fakultas:"), inputGbc);

        inputGbc.gridx = 1;
        comboBox = new JComboBox<>(new String[]{"Teknik", "Hukum", "FISS", "FEB", "FISIP", "FKIP"});
        inputPanel.add(comboBox, inputGbc);

        // JList untuk unit
        inputGbc.gridx = 0;
        inputGbc.gridy = 5;
        inputPanel.add(new JLabel("Unit:"), inputGbc);

        inputGbc.gridx = 1;
        inputGbc.weighty = 1.0; 
        list = new JList<>(new String[]{"Paduan Suara & Musik", "Takre", "Teater & Sastra", "Fotografi", "Kesenian Daerah Sunda"});
        JScrollPane listScrollPane = new JScrollPane(list);
        inputPanel.add(listScrollPane, inputGbc);

        // Menambahkan JSlider
        inputGbc.gridx = 0;
        inputGbc.gridy = 6;
        inputPanel.add(new JLabel("Tingkat Keterampilan:"), inputGbc);

        inputGbc.gridx = 1;
        jSlider = new JSlider(0, 100, 50);
        jSlider.setMajorTickSpacing(20);
        jSlider.setMinorTickSpacing(5);
        jSlider.setPaintTicks(true);
        jSlider.setPaintLabels(true);
        inputPanel.add(jSlider, inputGbc);

        // Menambahkan JSpinner
        inputGbc.gridx = 0;
        inputGbc.gridy = 7;
        inputPanel.add(new JLabel("Jumlah Pengalaman (Tahun):"), inputGbc);

        inputGbc.gridx = 1;
        SpinnerModel model = new SpinnerNumberModel(0, 0, 50, 1);
        jSpinner = new JSpinner(model);
        inputPanel.add(jSpinner, inputGbc);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; 
        add(inputPanel, gbc);

        // Table Model untuk data input
        String[] columns = {"Nama", "Alamat", "Jenis Kelamin", "Pengalaman", "Fakultas", "Unit", "Tingkat Keterampilan", "Jumlah Pengalaman"};
        tableModel = new DefaultTableModel(columns, 0);

        // Panel untuk tombol Submit dan Reset
        JPanel buttonPanel = new JPanel();
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> submitData());
        buttonPanel.add(submitButton);

        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> resetForm());
        buttonPanel.add(resetButton);

        gbc.gridx = 0;
        gbc.gridy = 1; 
        gbc.anchor = GridBagConstraints.CENTER;
        add(buttonPanel, gbc);
    }

    private void submitData() {
        String text = textField.getText();
        String area = textArea.getText();
        String radio = radioButton1.isSelected() ? "Laki-Laki" : "Perempuan";
        String checks = (checkBox1.isSelected() ? "Tidak Memiliki Pengalaman" : "") + 
                        (checkBox2.isSelected() ? " Memiliki Pengalaman" : "");
        String combo = (String) comboBox.getSelectedItem();
        String listSelection = list.getSelectedValue();
        int skillLevel = jSlider.getValue(); 
        int experienceYears = (Integer) jSpinner.getValue(); 

        tableModel.addRow(new Object[]{text, area, radio, checks, combo, listSelection, skillLevel, experienceYears});
    }

    private void resetForm() {
        textField.setText("");
        textArea.setText("");
        radioButton1.setSelected(false);
        radioButton2.setSelected(false);
        checkBox1.setSelected(false);
        checkBox2.setSelected(false);
        comboBox.setSelectedIndex(0);
        list.clearSelection();
        jSlider.setValue(50); 
        jSpinner.setValue(0);
    }

    private void showTableWindow() {
        JFrame tableFrame = new JFrame("Tabel Hasil Input");
        tableFrame.setSize(800, 400);
        tableFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        dataTable = new JTable(tableModel);
        dataTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); 
        JScrollPane scrollPane = new JScrollPane(dataTable);
        tableFrame.add(scrollPane, BorderLayout.CENTER);

        // Panel untuk tombol Delete di dalam tabel
        JPanel deleteButtonPanel = new JPanel();
        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> deleteSelectedRow());
        deleteButtonPanel.add(deleteButton);
        
        tableFrame.add(deleteButtonPanel, BorderLayout.SOUTH);

        tableFrame.setVisible(true);
    }

    private void deleteSelectedRow() {
        int selectedRow = dataTable.getSelectedRow();
        if (selectedRow != -1) {
            tableModel.removeRow(selectedRow);
        } else {
            JOptionPane.showMessageDialog(this, "Silakan pilih baris yang ingin dihapus.", "Peringatan", JOptionPane.WARNING_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            InputForm form = new InputForm();
            form.setVisible(true);
        });
    }
}
