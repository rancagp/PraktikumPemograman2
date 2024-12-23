package Main;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ComicManagementApp extends JFrame {
    private JTextField tfNama, tfStudio, tfPenerbit, tfRilis, tfGenre, tfHarga;
    private JTable table;
    private DefaultTableModel tableModel;

    public ComicManagementApp() {
        setTitle("Comic Management");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        // Panel Input
        JPanel inputPanel = new JPanel(new GridLayout(6, 2));
        inputPanel.add(new JLabel("Nama Komik:"));
        tfNama = new JTextField();
        inputPanel.add(tfNama);

        inputPanel.add(new JLabel("Studio:"));
        tfStudio = new JTextField();
        inputPanel.add(tfStudio);

        inputPanel.add(new JLabel("Penerbit:"));
        tfPenerbit = new JTextField();
        inputPanel.add(tfPenerbit);

        inputPanel.add(new JLabel("Rilis:"));
        tfRilis = new JTextField();
        inputPanel.add(tfRilis);

        inputPanel.add(new JLabel("Genre:"));
        tfGenre = new JTextField();
        inputPanel.add(tfGenre);

        inputPanel.add(new JLabel("Harga:"));
        tfHarga = new JTextField();
        inputPanel.add(tfHarga);

        add(inputPanel, BorderLayout.NORTH);

        // Tabel
        tableModel = new DefaultTableModel(new String[]{"ID", "Nama Komik", "Studio", "Penerbit", "Rilis", "Genre", "Harga"}, 0);
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Panel Tombol
        JPanel buttonPanel = new JPanel();
        JButton btnAdd = new JButton("Add Comic");
        JButton btnUpdate = new JButton("Update Comic");
        JButton btnDelete = new JButton("Delete Comic");
        JButton btnRefresh = new JButton("Refresh");

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnRefresh);

        add(buttonPanel, BorderLayout.SOUTH);

        // Tombol Add
        btnAdd.addActionListener(e -> {
            String nama = tfNama.getText();
            String studio = tfStudio.getText();
            String penerbit = tfPenerbit.getText();
            int rilis = Integer.parseInt(tfRilis.getText());
            String genre = tfGenre.getText();
            double harga = Double.parseDouble(tfHarga.getText());

            try (Connection conn = DatabaseConnection.getConnection()) {
                String query = "INSERT INTO comics (title, author, publisher, release_year, genre, price) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, nama);
                stmt.setString(2, studio);
                stmt.setString(3, penerbit);
                stmt.setInt(4, rilis);
                stmt.setString(5, genre);
                stmt.setDouble(6, harga);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Data berhasil ditambahkan!");
                refreshTable();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        // Tombol Update
        btnUpdate.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int id = (int) tableModel.getValueAt(selectedRow, 0);
                String nama = tfNama.getText();
                String studio = tfStudio.getText();
                String penerbit = tfPenerbit.getText();

                // Mengambil input dan menghapus spasi kosong di awal dan akhir
                String rilisText = tfRilis.getText().trim();
                int rilis = -1; // nilai default
                try {
                    // Pastikan input tidak kosong dan bisa diparse
                    if (!rilisText.isEmpty()) {
                        rilis = Integer.parseInt(rilisText);
                    } else {
                        throw new NumberFormatException(); // throw exception jika input kosong
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Tahun rilis harus berupa angka!");
                    return;
                }

                // Memeriksa genre dan harga
                String genre = tfGenre.getText();
                String hargaText = tfHarga.getText().trim();
                double harga = -1.0; // nilai default
                try {
                    if (!hargaText.isEmpty()) {
                        harga = Double.parseDouble(hargaText);
                    } else {
                        throw new NumberFormatException();
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Harga harus berupa angka!");
                    return;
                }

                // Mengecek jika ada field yang kosong
                if (nama.isEmpty() || studio.isEmpty() || penerbit.isEmpty() || genre.isEmpty() || rilisText.isEmpty() || hargaText.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Semua kolom harus diisi!");
                    return;
                }

                try (Connection conn = DatabaseConnection.getConnection()) {
                    String query = "UPDATE comics SET title = ?, author = ?, publisher = ?, release_year = ?, genre = ?, price = ? WHERE id = ?";
                    PreparedStatement stmt = conn.prepareStatement(query);
                    stmt.setString(1, nama);
                    stmt.setString(2, studio);
                    stmt.setString(3, penerbit);
                    stmt.setInt(4, rilis);
                    stmt.setString(5, genre);
                    stmt.setDouble(6, harga);
                    stmt.setInt(7, id);
                    stmt.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Data berhasil diperbarui!");
                    refreshTable();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Gagal memperbarui data!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Silakan pilih baris yang ingin diperbarui.");
            }
        });


        // Tombol Delete
        btnDelete.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int id = (int) tableModel.getValueAt(selectedRow, 0);

                try (Connection conn = DatabaseConnection.getConnection()) {
                    String query = "DELETE FROM comics WHERE id = ?";
                    PreparedStatement stmt = conn.prepareStatement(query);
                    stmt.setInt(1, id);
                    stmt.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Data berhasil dihapus!");
                    refreshTable();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Gagal menghapus data!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Silakan pilih baris yang ingin dihapus.");
            }
        });

        // Tombol Refresh
        btnRefresh.addActionListener(e -> refreshTable());

        setVisible(true);
    }

    private void refreshTable() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM comics";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            tableModel.setRowCount(0);  // Menghapus baris tabel lama
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("publisher"),
                        rs.getInt("release_year"),
                        rs.getString("genre"),
                        rs.getDouble("price")
                });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new ComicManagementApp();
    }
}

