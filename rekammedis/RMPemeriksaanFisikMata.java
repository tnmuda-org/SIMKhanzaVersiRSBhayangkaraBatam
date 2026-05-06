/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rekammedis;

import fungsi.WarnaTable;
import fungsi.batasInput;
import fungsi.koneksiDB;
import fungsi.sekuel;
import fungsi.validasi;
import fungsi.akses;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import kepegawaian.DlgCariDokter;
import kepegawaian.DlgCariPetugas;

/**
 *
 * @author perpustakaan
 */
public final class RMPemeriksaanFisikMata extends javax.swing.JDialog {

    private final DefaultTableModel tabMode;
    private DlgCariDokter dokter = new DlgCariDokter(null, false);
    private Connection koneksi = koneksiDB.condb();
    private sekuel Sequel = new sekuel();
    private validasi Valid = new validasi();
    private PreparedStatement ps;
    private ResultSet rs;
    private int i = 0;

    /**
     * Creates new form DlgRujuk
     *
     * @param parent
     * @param modal
     */
    public RMPemeriksaanFisikMata(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setLocation(8, 1);
        setSize(628, 674);

        tabMode = new DefaultTableModel(null, new Object[]{
            "No.Rawat", "No.R.M.", "Nama Pasien", "Tgl.Lahir", "Tanggal", "Jam", "NIP", "Nama Dokter",
            "Kepala", "Mata", "Melihat Jauh Kanan", "Melihat Jauh Kiri", "Melihat Dekat Kanan",
            "Melihat Dekat Kiri", "Melihat Warna", "Kelopak Mata dan Selaput Mata", "Kornea dan Pupil",
            "Sklera", "Fundus", "Gerakan Mata", "Kesimpulan", "Saran"
        }) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false;
            }
        };
        tbObat.setModel(tabMode);
        tbObat.setPreferredScrollableViewportSize(new Dimension(500, 500));
        tbObat.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

// Lebar masing-masing kolom
        int[] columnWidths = {
            105, 65, 160, 90, 100, 65, 65, 120,
            250, 90, 160, 160, 160, 160, 160, 160,
            160, 160, 160, 160, 160, 160
        };

// Atur lebar kolom secara efisien
        for (int i = 0; i < columnWidths.length; i++) {
            TableColumn column = tbObat.getColumnModel().getColumn(i);
            column.setPreferredWidth(columnWidths[i]);
        }

// Set renderer warna tabel (gunakan sesuai kebutuhan Anda)
        tbObat.setDefaultRenderer(Object.class, new WarnaTable());

        TNoRw.setDocument(new batasInput((byte) 17).getKata(TNoRw));
        KdDokter.setDocument(new batasInput((byte) 20).getKata(KdDokter));
        Kesimpulan.setDocument(new batasInput((int) 2000).getKata(Kesimpulan));
        TCari.setDocument(new batasInput((int) 100).getKata(TCari));

        if (koneksiDB.CARICEPAT().equals("aktif")) {
            TCari.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    if (TCari.getText().length() > 2) {
                        tampil();
                    }
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    if (TCari.getText().length() > 2) {
                        tampil();
                    }
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    if (TCari.getText().length() > 2) {
                        tampil();
                    }
                }
            });
        }

        dokter.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
            }

            @Override
            public void windowClosing(WindowEvent e) {
            }

            @Override
            public void windowClosed(WindowEvent e) {
                if (dokter.getTable().getSelectedRow() != -1) {
                    KdDokter.setText(dokter.getTable().getValueAt(dokter.getTable().getSelectedRow(), 0).toString());
                    NmDokter.setText(dokter.getTable().getValueAt(dokter.getTable().getSelectedRow(), 1).toString());
                    KdDokter.requestFocus();
                }
            }

            @Override
            public void windowIconified(WindowEvent e) {
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
            }

            @Override
            public void windowActivated(WindowEvent e) {
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
            }
        });

        ChkInput.setSelected(false);
        isForm();
        jam();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        MnCatatanKeperawatanRawatJalan = new javax.swing.JMenuItem();
        JK = new widget.TextBox();
        Umur = new widget.TextBox();
        internalFrame1 = new widget.InternalFrame();
        Scroll = new widget.ScrollPane();
        tbObat = new widget.Table();
        jPanel3 = new javax.swing.JPanel();
        panelGlass8 = new widget.panelisi();
        BtnSimpan = new widget.Button();
        BtnBatal = new widget.Button();
        BtnHapus = new widget.Button();
        BtnEdit = new widget.Button();
        BtnPrint = new widget.Button();
        jLabel7 = new widget.Label();
        LCount = new widget.Label();
        BtnKeluar = new widget.Button();
        panelGlass9 = new widget.panelisi();
        jLabel19 = new widget.Label();
        DTPCari1 = new widget.Tanggal();
        jLabel21 = new widget.Label();
        DTPCari2 = new widget.Tanggal();
        jLabel6 = new widget.Label();
        TCari = new widget.TextBox();
        BtnCari = new widget.Button();
        BtnAll = new widget.Button();
        PanelInput = new javax.swing.JPanel();
        FormInput = new widget.PanelBiasa();
        jLabel4 = new widget.Label();
        TNoRw = new widget.TextBox();
        TPasien = new widget.TextBox();
        Tanggal = new widget.Tanggal();
        TNoRM = new widget.TextBox();
        jLabel16 = new widget.Label();
        Jam = new widget.ComboBox();
        Menit = new widget.ComboBox();
        Detik = new widget.ComboBox();
        ChkKejadian = new widget.CekBox();
        jLabel18 = new widget.Label();
        KdDokter = new widget.TextBox();
        NmDokter = new widget.TextBox();
        BtnDokter = new widget.Button();
        jLabel8 = new widget.Label();
        TglLahir = new widget.TextBox();
        jLabel12 = new widget.Label();
        scrollPane2 = new widget.ScrollPane();
        Kesimpulan = new widget.TextArea();
        jLabel13 = new widget.Label();
        scrollPane3 = new widget.ScrollPane();
        Saran = new widget.TextArea();
        jLabel9 = new widget.Label();
        Kepala = new widget.TextBox();
        jLabel10 = new widget.Label();
        Mata = new widget.TextBox();
        jLabel11 = new widget.Label();
        jLabel14 = new widget.Label();
        JauhKanan = new widget.TextBox();
        jLabel15 = new widget.Label();
        JauhKiri = new widget.TextBox();
        jLabel17 = new widget.Label();
        DekatKanan = new widget.TextBox();
        jLabel20 = new widget.Label();
        jLabel22 = new widget.Label();
        DekatKiri = new widget.TextBox();
        jLabel23 = new widget.Label();
        Warna = new widget.ComboBox();
        jLabel24 = new widget.Label();
        Kelopak = new widget.ComboBox();
        jLabel25 = new widget.Label();
        Kornea = new widget.ComboBox();
        jLabel26 = new widget.Label();
        Sklera = new widget.ComboBox();
        jLabel27 = new widget.Label();
        Fundus = new widget.ComboBox();
        jLabel28 = new widget.Label();
        Gerakan = new widget.ComboBox();
        ChkInput = new widget.CekBox();

        jPopupMenu1.setName("jPopupMenu1"); // NOI18N

        MnCatatanKeperawatanRawatJalan.setBackground(new java.awt.Color(255, 255, 254));
        MnCatatanKeperawatanRawatJalan.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        MnCatatanKeperawatanRawatJalan.setForeground(new java.awt.Color(50, 50, 50));
        MnCatatanKeperawatanRawatJalan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        MnCatatanKeperawatanRawatJalan.setText("Formulir Catatan Keperawatan Rawat Jalan");
        MnCatatanKeperawatanRawatJalan.setName("MnCatatanKeperawatanRawatJalan"); // NOI18N
        MnCatatanKeperawatanRawatJalan.setPreferredSize(new java.awt.Dimension(260, 26));
        MnCatatanKeperawatanRawatJalan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MnCatatanKeperawatanRawatJalanActionPerformed(evt);
            }
        });
        jPopupMenu1.add(MnCatatanKeperawatanRawatJalan);

        JK.setHighlighter(null);
        JK.setName("JK"); // NOI18N

        Umur.setHighlighter(null);
        Umur.setName("Umur"); // NOI18N

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);

        internalFrame1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "::[ Pemeriksaaan Fisik Mata ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 50, 50))); // NOI18N
        internalFrame1.setFont(new java.awt.Font("Tahoma", 2, 12)); // NOI18N
        internalFrame1.setName("internalFrame1"); // NOI18N
        internalFrame1.setLayout(new java.awt.BorderLayout(1, 1));

        Scroll.setName("Scroll"); // NOI18N
        Scroll.setOpaque(true);
        Scroll.setPreferredSize(new java.awt.Dimension(452, 200));

        tbObat.setToolTipText("Silahkan klik untuk memilih data yang mau diedit ataupun dihapus");
        tbObat.setComponentPopupMenu(jPopupMenu1);
        tbObat.setName("tbObat"); // NOI18N
        tbObat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbObatMouseClicked(evt);
            }
        });
        tbObat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbObatKeyPressed(evt);
            }
        });
        Scroll.setViewportView(tbObat);

        internalFrame1.add(Scroll, java.awt.BorderLayout.CENTER);

        jPanel3.setName("jPanel3"); // NOI18N
        jPanel3.setOpaque(false);
        jPanel3.setPreferredSize(new java.awt.Dimension(44, 100));
        jPanel3.setLayout(new java.awt.BorderLayout(1, 1));

        panelGlass8.setName("panelGlass8"); // NOI18N
        panelGlass8.setPreferredSize(new java.awt.Dimension(44, 44));
        panelGlass8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));

        BtnSimpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/save-16x16.png"))); // NOI18N
        BtnSimpan.setMnemonic('S');
        BtnSimpan.setText("Simpan");
        BtnSimpan.setToolTipText("Alt+S");
        BtnSimpan.setName("BtnSimpan"); // NOI18N
        BtnSimpan.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnSimpanActionPerformed(evt);
            }
        });
        BtnSimpan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnSimpanKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnSimpan);

        BtnBatal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/Cancel-2-16x16.png"))); // NOI18N
        BtnBatal.setMnemonic('B');
        BtnBatal.setText("Baru");
        BtnBatal.setToolTipText("Alt+B");
        BtnBatal.setName("BtnBatal"); // NOI18N
        BtnBatal.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnBatal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnBatalActionPerformed(evt);
            }
        });
        BtnBatal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnBatalKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnBatal);

        BtnHapus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/stop_f2.png"))); // NOI18N
        BtnHapus.setMnemonic('H');
        BtnHapus.setText("Hapus");
        BtnHapus.setToolTipText("Alt+H");
        BtnHapus.setName("BtnHapus"); // NOI18N
        BtnHapus.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnHapusActionPerformed(evt);
            }
        });
        BtnHapus.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnHapusKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnHapus);

        BtnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/inventaris.png"))); // NOI18N
        BtnEdit.setMnemonic('G');
        BtnEdit.setText("Ganti");
        BtnEdit.setToolTipText("Alt+G");
        BtnEdit.setName("BtnEdit"); // NOI18N
        BtnEdit.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnEditActionPerformed(evt);
            }
        });
        BtnEdit.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnEditKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnEdit);

        BtnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/b_print.png"))); // NOI18N
        BtnPrint.setMnemonic('T');
        BtnPrint.setText("Cetak");
        BtnPrint.setToolTipText("Alt+T");
        BtnPrint.setName("BtnPrint"); // NOI18N
        BtnPrint.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnPrintActionPerformed(evt);
            }
        });
        BtnPrint.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnPrintKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnPrint);

        jLabel7.setText("Record :");
        jLabel7.setName("jLabel7"); // NOI18N
        jLabel7.setPreferredSize(new java.awt.Dimension(80, 23));
        panelGlass8.add(jLabel7);

        LCount.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        LCount.setText("0");
        LCount.setName("LCount"); // NOI18N
        LCount.setPreferredSize(new java.awt.Dimension(70, 23));
        panelGlass8.add(LCount);

        BtnKeluar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/exit.png"))); // NOI18N
        BtnKeluar.setMnemonic('K');
        BtnKeluar.setText("Keluar");
        BtnKeluar.setToolTipText("Alt+K");
        BtnKeluar.setName("BtnKeluar"); // NOI18N
        BtnKeluar.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnKeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnKeluarActionPerformed(evt);
            }
        });
        BtnKeluar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnKeluarKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnKeluar);

        jPanel3.add(panelGlass8, java.awt.BorderLayout.CENTER);

        panelGlass9.setName("panelGlass9"); // NOI18N
        panelGlass9.setPreferredSize(new java.awt.Dimension(44, 44));
        panelGlass9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));

        jLabel19.setText("Tanggal :");
        jLabel19.setName("jLabel19"); // NOI18N
        jLabel19.setPreferredSize(new java.awt.Dimension(60, 23));
        panelGlass9.add(jLabel19);

        DTPCari1.setForeground(new java.awt.Color(50, 70, 50));
        DTPCari1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "20-11-2024" }));
        DTPCari1.setDisplayFormat("dd-MM-yyyy");
        DTPCari1.setName("DTPCari1"); // NOI18N
        DTPCari1.setOpaque(false);
        DTPCari1.setPreferredSize(new java.awt.Dimension(95, 23));
        panelGlass9.add(DTPCari1);

        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel21.setText("s.d.");
        jLabel21.setName("jLabel21"); // NOI18N
        jLabel21.setPreferredSize(new java.awt.Dimension(23, 23));
        panelGlass9.add(jLabel21);

        DTPCari2.setForeground(new java.awt.Color(50, 70, 50));
        DTPCari2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "20-11-2024" }));
        DTPCari2.setDisplayFormat("dd-MM-yyyy");
        DTPCari2.setName("DTPCari2"); // NOI18N
        DTPCari2.setOpaque(false);
        DTPCari2.setPreferredSize(new java.awt.Dimension(95, 23));
        panelGlass9.add(DTPCari2);

        jLabel6.setText("Key Word :");
        jLabel6.setName("jLabel6"); // NOI18N
        jLabel6.setPreferredSize(new java.awt.Dimension(90, 23));
        panelGlass9.add(jLabel6);

        TCari.setName("TCari"); // NOI18N
        TCari.setPreferredSize(new java.awt.Dimension(310, 23));
        TCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TCariKeyPressed(evt);
            }
        });
        panelGlass9.add(TCari);

        BtnCari.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/accept.png"))); // NOI18N
        BtnCari.setMnemonic('3');
        BtnCari.setToolTipText("Alt+3");
        BtnCari.setName("BtnCari"); // NOI18N
        BtnCari.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCariActionPerformed(evt);
            }
        });
        BtnCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnCariKeyPressed(evt);
            }
        });
        panelGlass9.add(BtnCari);

        BtnAll.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/Search-16x16.png"))); // NOI18N
        BtnAll.setMnemonic('M');
        BtnAll.setToolTipText("Alt+M");
        BtnAll.setName("BtnAll"); // NOI18N
        BtnAll.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnAllActionPerformed(evt);
            }
        });
        BtnAll.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnAllKeyPressed(evt);
            }
        });
        panelGlass9.add(BtnAll);

        jPanel3.add(panelGlass9, java.awt.BorderLayout.PAGE_START);

        internalFrame1.add(jPanel3, java.awt.BorderLayout.PAGE_END);

        PanelInput.setName("PanelInput"); // NOI18N
        PanelInput.setOpaque(false);
        PanelInput.setPreferredSize(new java.awt.Dimension(192, 320));
        PanelInput.setLayout(new java.awt.BorderLayout(1, 1));

        FormInput.setBackground(new java.awt.Color(250, 255, 245));
        FormInput.setName("FormInput"); // NOI18N
        FormInput.setPreferredSize(new java.awt.Dimension(100, 225));
        FormInput.setLayout(null);

        jLabel4.setText("No.Rawat :");
        jLabel4.setName("jLabel4"); // NOI18N
        FormInput.add(jLabel4);
        jLabel4.setBounds(0, 10, 70, 23);

        TNoRw.setHighlighter(null);
        TNoRw.setName("TNoRw"); // NOI18N
        TNoRw.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TNoRwKeyPressed(evt);
            }
        });
        FormInput.add(TNoRw);
        TNoRw.setBounds(74, 10, 136, 23);

        TPasien.setEditable(false);
        TPasien.setHighlighter(null);
        TPasien.setName("TPasien"); // NOI18N
        TPasien.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TPasienKeyPressed(evt);
            }
        });
        FormInput.add(TPasien);
        TPasien.setBounds(326, 10, 295, 23);

        Tanggal.setForeground(new java.awt.Color(50, 70, 50));
        Tanggal.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "20-11-2024" }));
        Tanggal.setDisplayFormat("dd-MM-yyyy");
        Tanggal.setName("Tanggal"); // NOI18N
        Tanggal.setOpaque(false);
        Tanggal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TanggalKeyPressed(evt);
            }
        });
        FormInput.add(Tanggal);
        Tanggal.setBounds(74, 40, 90, 23);

        TNoRM.setEditable(false);
        TNoRM.setHighlighter(null);
        TNoRM.setName("TNoRM"); // NOI18N
        TNoRM.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TNoRMKeyPressed(evt);
            }
        });
        FormInput.add(TNoRM);
        TNoRM.setBounds(212, 10, 112, 23);

        jLabel16.setText("Tanggal :");
        jLabel16.setName("jLabel16"); // NOI18N
        jLabel16.setVerifyInputWhenFocusTarget(false);
        FormInput.add(jLabel16);
        jLabel16.setBounds(0, 40, 70, 23);

        Jam.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23" }));
        Jam.setName("Jam"); // NOI18N
        Jam.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JamKeyPressed(evt);
            }
        });
        FormInput.add(Jam);
        Jam.setBounds(168, 40, 62, 23);

        Menit.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));
        Menit.setName("Menit"); // NOI18N
        Menit.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                MenitKeyPressed(evt);
            }
        });
        FormInput.add(Menit);
        Menit.setBounds(233, 40, 62, 23);

        Detik.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));
        Detik.setName("Detik"); // NOI18N
        Detik.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DetikKeyPressed(evt);
            }
        });
        FormInput.add(Detik);
        Detik.setBounds(298, 40, 62, 23);

        ChkKejadian.setBorder(null);
        ChkKejadian.setSelected(true);
        ChkKejadian.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        ChkKejadian.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ChkKejadian.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        ChkKejadian.setName("ChkKejadian"); // NOI18N
        FormInput.add(ChkKejadian);
        ChkKejadian.setBounds(363, 40, 23, 23);

        jLabel18.setText("Dokter:");
        jLabel18.setName("jLabel18"); // NOI18N
        FormInput.add(jLabel18);
        jLabel18.setBounds(390, 40, 70, 23);

        KdDokter.setEditable(false);
        KdDokter.setHighlighter(null);
        KdDokter.setName("KdDokter"); // NOI18N
        KdDokter.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KdDokterKeyPressed(evt);
            }
        });
        FormInput.add(KdDokter);
        KdDokter.setBounds(464, 40, 104, 23);

        NmDokter.setEditable(false);
        NmDokter.setName("NmDokter"); // NOI18N
        FormInput.add(NmDokter);
        NmDokter.setBounds(570, 40, 187, 23);

        BtnDokter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnDokter.setMnemonic('2');
        BtnDokter.setToolTipText("ALt+2");
        BtnDokter.setName("BtnDokter"); // NOI18N
        BtnDokter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnDokterActionPerformed(evt);
            }
        });
        BtnDokter.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnDokterKeyPressed(evt);
            }
        });
        FormInput.add(BtnDokter);
        BtnDokter.setBounds(761, 40, 28, 23);

        jLabel8.setText("Tgl.Lahir :");
        jLabel8.setName("jLabel8"); // NOI18N
        FormInput.add(jLabel8);
        jLabel8.setBounds(625, 10, 60, 23);

        TglLahir.setHighlighter(null);
        TglLahir.setName("TglLahir"); // NOI18N
        FormInput.add(TglLahir);
        TglLahir.setBounds(689, 10, 100, 23);

        jLabel12.setText("Kesimpulan:");
        jLabel12.setName("jLabel12"); // NOI18N
        FormInput.add(jLabel12);
        jLabel12.setBounds(0, 200, 70, 23);

        scrollPane2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        scrollPane2.setName("scrollPane2"); // NOI18N

        Kesimpulan.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        Kesimpulan.setColumns(20);
        Kesimpulan.setRows(5);
        Kesimpulan.setName("Kesimpulan"); // NOI18N
        Kesimpulan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KesimpulanKeyPressed(evt);
            }
        });
        scrollPane2.setViewportView(Kesimpulan);

        FormInput.add(scrollPane2);
        scrollPane2.setBounds(70, 200, 715, 40);

        jLabel13.setText("Saran:");
        jLabel13.setName("jLabel13"); // NOI18N
        FormInput.add(jLabel13);
        jLabel13.setBounds(0, 250, 70, 23);

        scrollPane3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        scrollPane3.setName("scrollPane3"); // NOI18N

        Saran.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        Saran.setColumns(20);
        Saran.setRows(5);
        Saran.setName("Saran"); // NOI18N
        Saran.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SaranKeyPressed(evt);
            }
        });
        scrollPane3.setViewportView(Saran);

        FormInput.add(scrollPane3);
        scrollPane3.setBounds(70, 250, 715, 40);

        jLabel9.setText("Melihat Warna:");
        jLabel9.setName("jLabel9"); // NOI18N
        FormInput.add(jLabel9);
        jLabel9.setBounds(0, 140, 80, 23);

        Kepala.setHighlighter(null);
        Kepala.setName("Kepala"); // NOI18N
        FormInput.add(Kepala);
        Kepala.setBounds(70, 80, 290, 23);

        jLabel10.setText("Mata:");
        jLabel10.setName("jLabel10"); // NOI18N
        FormInput.add(jLabel10);
        jLabel10.setBounds(410, 80, 60, 23);

        Mata.setHighlighter(null);
        Mata.setName("Mata"); // NOI18N
        FormInput.add(Mata);
        Mata.setBounds(470, 80, 290, 23);

        jLabel11.setText("Kepala:");
        jLabel11.setName("jLabel11"); // NOI18N
        FormInput.add(jLabel11);
        jLabel11.setBounds(10, 80, 60, 23);

        jLabel14.setText("Kanan:");
        jLabel14.setName("jLabel14"); // NOI18N
        FormInput.add(jLabel14);
        jLabel14.setBounds(70, 110, 40, 23);

        JauhKanan.setHighlighter(null);
        JauhKanan.setName("JauhKanan"); // NOI18N
        FormInput.add(JauhKanan);
        JauhKanan.setBounds(110, 110, 100, 23);

        jLabel15.setText("Kiri:");
        jLabel15.setName("jLabel15"); // NOI18N
        FormInput.add(jLabel15);
        jLabel15.setBounds(220, 110, 30, 23);

        JauhKiri.setHighlighter(null);
        JauhKiri.setName("JauhKiri"); // NOI18N
        FormInput.add(JauhKiri);
        JauhKiri.setBounds(250, 110, 110, 23);

        jLabel17.setText("Melihat Dekat:");
        jLabel17.setName("jLabel17"); // NOI18N
        FormInput.add(jLabel17);
        jLabel17.setBounds(360, 110, 80, 23);

        DekatKanan.setHighlighter(null);
        DekatKanan.setName("DekatKanan"); // NOI18N
        FormInput.add(DekatKanan);
        DekatKanan.setBounds(480, 110, 80, 23);

        jLabel20.setText("Kanan:");
        jLabel20.setName("jLabel20"); // NOI18N
        FormInput.add(jLabel20);
        jLabel20.setBounds(420, 110, 60, 23);

        jLabel22.setText("Kiri:");
        jLabel22.setName("jLabel22"); // NOI18N
        FormInput.add(jLabel22);
        jLabel22.setBounds(580, 110, 50, 23);

        DekatKiri.setHighlighter(null);
        DekatKiri.setName("DekatKiri"); // NOI18N
        FormInput.add(DekatKiri);
        DekatKiri.setBounds(630, 110, 90, 23);

        jLabel23.setText("Melihat Jauh:");
        jLabel23.setName("jLabel23"); // NOI18N
        FormInput.add(jLabel23);
        jLabel23.setBounds(-10, 110, 80, 23);

        Warna.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Normal", "Tidak Normal" }));
        Warna.setName("Warna"); // NOI18N
        FormInput.add(Warna);
        Warna.setBounds(90, 140, 110, 20);

        jLabel24.setText("Kelopak mata dan selaput mata:");
        jLabel24.setName("jLabel24"); // NOI18N
        FormInput.add(jLabel24);
        jLabel24.setBounds(220, 140, 170, 23);

        Kelopak.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Normal", "Tidak Normal" }));
        Kelopak.setName("Kelopak"); // NOI18N
        FormInput.add(Kelopak);
        Kelopak.setBounds(390, 140, 110, 20);

        jLabel25.setText("Kornea dan Pupil:");
        jLabel25.setName("jLabel25"); // NOI18N
        FormInput.add(jLabel25);
        jLabel25.setBounds(510, 140, 100, 23);

        Kornea.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Normal", "Tidak Normal" }));
        Kornea.setName("Kornea"); // NOI18N
        FormInput.add(Kornea);
        Kornea.setBounds(630, 140, 110, 20);

        jLabel26.setText("Sklera:");
        jLabel26.setName("jLabel26"); // NOI18N
        FormInput.add(jLabel26);
        jLabel26.setBounds(0, 170, 80, 23);

        Sklera.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Normal", "Tidak Normal" }));
        Sklera.setName("Sklera"); // NOI18N
        FormInput.add(Sklera);
        Sklera.setBounds(90, 170, 110, 20);

        jLabel27.setText("Fundus:");
        jLabel27.setName("jLabel27"); // NOI18N
        FormInput.add(jLabel27);
        jLabel27.setBounds(200, 170, 80, 23);

        Fundus.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Normal", "Tidak Normal" }));
        Fundus.setName("Fundus"); // NOI18N
        FormInput.add(Fundus);
        Fundus.setBounds(290, 170, 110, 20);

        jLabel28.setText("Gerakan Mata:");
        jLabel28.setName("jLabel28"); // NOI18N
        FormInput.add(jLabel28);
        jLabel28.setBounds(410, 170, 80, 23);

        Gerakan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Normal", "Tidak Normal" }));
        Gerakan.setName("Gerakan"); // NOI18N
        FormInput.add(Gerakan);
        Gerakan.setBounds(500, 170, 110, 20);

        PanelInput.add(FormInput, java.awt.BorderLayout.CENTER);

        ChkInput.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/143.png"))); // NOI18N
        ChkInput.setMnemonic('I');
        ChkInput.setText(".: Input Data");
        ChkInput.setToolTipText("Alt+I");
        ChkInput.setBorderPainted(true);
        ChkInput.setBorderPaintedFlat(true);
        ChkInput.setFocusable(false);
        ChkInput.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ChkInput.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        ChkInput.setName("ChkInput"); // NOI18N
        ChkInput.setPreferredSize(new java.awt.Dimension(192, 20));
        ChkInput.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/143.png"))); // NOI18N
        ChkInput.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/145.png"))); // NOI18N
        ChkInput.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/145.png"))); // NOI18N
        ChkInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ChkInputActionPerformed(evt);
            }
        });
        PanelInput.add(ChkInput, java.awt.BorderLayout.PAGE_END);

        internalFrame1.add(PanelInput, java.awt.BorderLayout.PAGE_START);

        getContentPane().add(internalFrame1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void TNoRwKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TNoRwKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_PAGE_DOWN) {
            isRawat();
        } else {
            Valid.pindah(evt, TCari, Tanggal);
        }
}//GEN-LAST:event_TNoRwKeyPressed

    private void TPasienKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TPasienKeyPressed
        Valid.pindah(evt, TCari, BtnSimpan);
}//GEN-LAST:event_TPasienKeyPressed

    private void BtnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSimpanActionPerformed
        if (TNoRw.getText().trim().equals("") || TPasien.getText().trim().equals("")) {
            Valid.textKosong(TNoRw, "pasien");
        } else if (Kepala.getText().trim().equals("")) {
            Valid.textKosong(Kepala, "Kepala");
        } else if (Mata.getText().trim().equals("")) {
            Valid.textKosong(Mata, "Mata");
        } else if (JauhKanan.getText().trim().equals("")) {
            Valid.textKosong(JauhKanan, "Jauh  Kanan");
        } else if (JauhKiri.getText().trim().equals("")) {
            Valid.textKosong(JauhKiri, "Jauh Kiri");
        } else if (DekatKanan.getText().trim().equals("")) {
            Valid.textKosong(DekatKanan, "Dekat Kanan");
        } else if (DekatKiri.getText().trim().equals("")) {
            Valid.textKosong(DekatKiri, "Dekat Kiri");
        } else if (Kesimpulan.getText().trim().equals("")) {
            Valid.textKosong(Kesimpulan, "Kesimpulan");
        } else if (Saran.getText().trim().equals("")) {
            Valid.textKosong(Saran, "Saran");
        } else if (KdDokter.getText().trim().equals("") || NmDokter.getText().trim().equals("")) {
            Valid.textKosong(KdDokter, "Petugas");
        } else {
            if (Sequel.menyimpantf("pemeriksaan_fisik_mata", "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?", "Data", 18, new String[]{
                TNoRw.getText(), Valid.SetTgl(Tanggal.getSelectedItem() + ""), Jam.getSelectedItem() + ":" + Menit.getSelectedItem() + ":" + Detik.getSelectedItem(), KdDokter.getText(),
                Kepala.getText(), Mata.getText(), JauhKanan.getText(), JauhKiri.getText(), DekatKanan.getText(), DekatKiri.getText(), Warna.getSelectedItem().toString(),
                Kelopak.getSelectedItem().toString(), Kornea.getSelectedItem().toString(), Sklera.getSelectedItem().toString(), Fundus.getSelectedItem().toString(), Gerakan.getSelectedItem().toString(),
                Kesimpulan.getText(), Saran.getText()
            }) == true) {
                tabMode.addRow(new String[]{
                    TNoRw.getText(), TNoRM.getText(), TPasien.getText(), TglLahir.getText(), Valid.SetTgl(Tanggal.getSelectedItem() + ""), Jam.getSelectedItem() + ":" + Menit.getSelectedItem() + ":" + Detik.getSelectedItem(), KdDokter.getText(),
                    NmDokter.getText(), Kepala.getText(), Mata.getText(), JauhKanan.getText(), JauhKiri.getText(), DekatKanan.getText(), DekatKiri.getText(), Warna.getSelectedItem().toString(),
                    Kelopak.getSelectedItem().toString(), Kornea.getSelectedItem().toString(), Sklera.getSelectedItem().toString(), Fundus.getSelectedItem().toString(), Gerakan.getSelectedItem().toString(),
                    Kesimpulan.getText(), Saran.getText()
                });
                LCount.setText("" + tabMode.getRowCount());
                emptTeks();
            }
        }
}//GEN-LAST:event_BtnSimpanActionPerformed

    private void BtnSimpanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnSimpanKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_SPACE) {
            BtnSimpanActionPerformed(null);
        } else {
            Valid.pindah(evt, Kesimpulan, BtnBatal);
        }
}//GEN-LAST:event_BtnSimpanKeyPressed

    private void BtnBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnBatalActionPerformed
        emptTeks();
        ChkInput.setSelected(true);
        isForm();
}//GEN-LAST:event_BtnBatalActionPerformed

    private void BtnBatalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnBatalKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_SPACE) {
            emptTeks();
        } else {
            Valid.pindah(evt, BtnSimpan, BtnHapus);
        }
}//GEN-LAST:event_BtnBatalKeyPressed

    private void BtnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnHapusActionPerformed
        if (tbObat.getSelectedRow() > -1) {
            if (akses.getkode().equals("Admin Utama")) {
                hapus();
            } else {
                if (KdDokter.getText().equals(tbObat.getValueAt(tbObat.getSelectedRow(), 9).toString())) {
                    hapus();
                } else {
                    JOptionPane.showMessageDialog(null, "Hanya bisa dihapus oleh petugas yang bersangkutan..!!");
                }
            }
        } else {
            JOptionPane.showMessageDialog(rootPane, "Silahkan anda pilih data terlebih dahulu..!!");
        }
}//GEN-LAST:event_BtnHapusActionPerformed

    private void BtnHapusKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnHapusKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_SPACE) {
            BtnHapusActionPerformed(null);
        } else {
            Valid.pindah(evt, BtnBatal, BtnEdit);
        }
}//GEN-LAST:event_BtnHapusKeyPressed

    private void BtnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnEditActionPerformed
        if (TNoRw.getText().trim().equals("") || TPasien.getText().trim().equals("")) {
            Valid.textKosong(TNoRw, "pasien");
        } else if (Kepala.getText().trim().equals("")) {
            Valid.textKosong(Kepala, "Kepala");
        } else if (Mata.getText().trim().equals("")) {
            Valid.textKosong(Mata, "Mata");
        } else if (JauhKanan.getText().trim().equals("")) {
            Valid.textKosong(JauhKanan, "Jauh  Kanan");
        } else if (JauhKiri.getText().trim().equals("")) {
            Valid.textKosong(JauhKiri, "Jauh Kiri");
        } else if (DekatKanan.getText().trim().equals("")) {
            Valid.textKosong(DekatKanan, "Dekat Kanan");
        } else if (DekatKiri.getText().trim().equals("")) {
            Valid.textKosong(DekatKiri, "Dekat Kiri");
        } else if (Kesimpulan.getText().trim().equals("")) {
            Valid.textKosong(Kesimpulan, "Kesimpulan");
        } else if (Saran.getText().trim().equals("")) {
            Valid.textKosong(Saran, "Saran");
        } else if (KdDokter.getText().trim().equals("") || NmDokter.getText().trim().equals("")) {
            Valid.textKosong(KdDokter, "Petugas");
        } else {
            if (tbObat.getSelectedRow() > -1) {
                if (akses.getkode().equals("Admin Utama")) {
                    ganti();
                } else {
                    if (KdDokter.getText().equals(tbObat.getValueAt(tbObat.getSelectedRow(), 9).toString())) {
                        ganti();
                    } else {
                        JOptionPane.showMessageDialog(null, "Hanya bisa diganti oleh petugas yang bersangkutan..!!");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(rootPane, "Silahkan anda pilih data terlebih dahulu..!!");
            }
        }
}//GEN-LAST:event_BtnEditActionPerformed

    private void BtnEditKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnEditKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_SPACE) {
            BtnEditActionPerformed(null);
        } else {
            Valid.pindah(evt, BtnHapus, BtnPrint);
        }
}//GEN-LAST:event_BtnEditKeyPressed

    private void BtnKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnKeluarActionPerformed
        dokter.dispose();
        dispose();
}//GEN-LAST:event_BtnKeluarActionPerformed

    private void BtnKeluarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnKeluarKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_SPACE) {
            BtnKeluarActionPerformed(null);
        } else {
            Valid.pindah(evt, BtnEdit, TCari);
        }
}//GEN-LAST:event_BtnKeluarKeyPressed

    private void BtnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnPrintActionPerformed
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        if (tabMode.getRowCount() == 0) {
            JOptionPane.showMessageDialog(null, "Maaf, data sudah habis. Tidak ada data yang bisa anda print...!!!!");
            BtnBatal.requestFocus();
        } else if (tabMode.getRowCount() != 0) {
            Map<String, Object> param = new HashMap<>();
            param.put("namars", akses.getnamars());
            param.put("alamatrs", akses.getalamatrs());
            param.put("kotars", akses.getkabupatenrs());
            param.put("propinsirs", akses.getpropinsirs());
            param.put("kontakrs", akses.getkontakrs());
            param.put("emailrs", akses.getemailrs());
            param.put("logo", Sequel.cariGambar("select setting.logo from setting"));

            if (TCari.getText().trim().equals("")) {
                Valid.MyReportqry("rptDataCatatanKeperawatanRalan.jasper", "report", "::[ Data Catatan Keperawatan Rawat Jalan ]::",
                        "select reg_periksa.no_rawat,pasien.no_rkm_medis,pasien.nm_pasien,reg_periksa.umurdaftar,reg_periksa.sttsumur,"
                        + "pasien.jk,pasien.tgl_lahir,pemeriksaan_fisik_mata.tanggal,pemeriksaan_fisik_mata.jam,pemeriksaan_fisik_mata.uraian,"
                        + "pemeriksaan_fisik_mata.nip,petugas.nama from pemeriksaan_fisik_mata inner join reg_periksa on pemeriksaan_fisik_mata.no_rawat=reg_periksa.no_rawat "
                        + "inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "
                        + "inner join petugas on pemeriksaan_fisik_mata.nip=petugas.nip where "
                        + "pemeriksaan_fisik_mata.tanggal between '" + Valid.SetTgl(DTPCari1.getSelectedItem() + "") + "' and '" + Valid.SetTgl(DTPCari2.getSelectedItem() + "") + "' order by pemeriksaan_fisik_mata.tanggal", param);
            } else {
                Valid.MyReportqry("rptDataCatatanKeperawatanRalan.jasper", "report", "::[ Data Catatan Keperawatan Rawat Jalan ]::",
                        "select reg_periksa.no_rawat,pasien.no_rkm_medis,pasien.nm_pasien,reg_periksa.umurdaftar,reg_periksa.sttsumur,"
                        + "pasien.jk,pasien.tgl_lahir,pemeriksaan_fisik_mata.tanggal,pemeriksaan_fisik_mata.jam,pemeriksaan_fisik_mata.uraian,"
                        + "pemeriksaan_fisik_mata.nip,petugas.nama from pemeriksaan_fisik_mata inner join reg_periksa on pemeriksaan_fisik_mata.no_rawat=reg_periksa.no_rawat "
                        + "inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "
                        + "inner join petugas on pemeriksaan_fisik_mata.nip=petugas.nip where "
                        + "pemeriksaan_fisik_mata.tanggal between '" + Valid.SetTgl(DTPCari1.getSelectedItem() + "") + "' and '" + Valid.SetTgl(DTPCari2.getSelectedItem() + "") + "' and "
                        + "(reg_periksa.no_rawat like '%" + TCari.getText().trim() + "%' or pasien.no_rkm_medis like '%" + TCari.getText().trim() + "%' or "
                        + "pasien.nm_pasien like '%" + TCari.getText().trim() + "%' or pemeriksaan_fisik_mata.nip like '%" + TCari.getText().trim() + "%' or petugas.nama like '%" + TCari.getText().trim() + "%') "
                        + "order by pemeriksaan_fisik_mata.tanggal ", param);
            }
        }
        this.setCursor(Cursor.getDefaultCursor());
}//GEN-LAST:event_BtnPrintActionPerformed

    private void BtnPrintKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnPrintKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_SPACE) {
            BtnPrintActionPerformed(null);
        } else {
            Valid.pindah(evt, BtnEdit, BtnKeluar);
        }
}//GEN-LAST:event_BtnPrintKeyPressed

    private void TCariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TCariKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            BtnCariActionPerformed(null);
        } else if (evt.getKeyCode() == KeyEvent.VK_PAGE_DOWN) {
            BtnCari.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_PAGE_UP) {
            BtnKeluar.requestFocus();
        }
}//GEN-LAST:event_TCariKeyPressed

    private void BtnCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCariActionPerformed
        tampil();
}//GEN-LAST:event_BtnCariActionPerformed

    private void BtnCariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnCariKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_SPACE) {
            BtnCariActionPerformed(null);
        } else {
            Valid.pindah(evt, TCari, BtnAll);
        }
}//GEN-LAST:event_BtnCariKeyPressed

    private void BtnAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAllActionPerformed
        TCari.setText("");
        tampil();
}//GEN-LAST:event_BtnAllActionPerformed

    private void BtnAllKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnAllKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_SPACE) {
            tampil();
            TCari.setText("");
        } else {
            Valid.pindah(evt, BtnCari, TPasien);
        }
}//GEN-LAST:event_BtnAllKeyPressed

    private void TanggalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TanggalKeyPressed
        Valid.pindah(evt, TCari, Jam);
}//GEN-LAST:event_TanggalKeyPressed

    private void TNoRMKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TNoRMKeyPressed
        // Valid.pindah(evt, TNm, BtnSimpan);
}//GEN-LAST:event_TNoRMKeyPressed

    private void tbObatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbObatMouseClicked
        if (tabMode.getRowCount() != 0) {
            try {
                getData();
            } catch (java.lang.NullPointerException e) {
            }
        }
}//GEN-LAST:event_tbObatMouseClicked

    private void tbObatKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbObatKeyPressed
        if (tabMode.getRowCount() != 0) {
            if ((evt.getKeyCode() == KeyEvent.VK_ENTER) || (evt.getKeyCode() == KeyEvent.VK_UP) || (evt.getKeyCode() == KeyEvent.VK_DOWN)) {
                try {
                    getData();
                } catch (java.lang.NullPointerException e) {
                }
            }
        }
}//GEN-LAST:event_tbObatKeyPressed

    private void ChkInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChkInputActionPerformed
        isForm();
    }//GEN-LAST:event_ChkInputActionPerformed

    private void JamKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_JamKeyPressed
        Valid.pindah(evt, Tanggal, Menit);
    }//GEN-LAST:event_JamKeyPressed

    private void MenitKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_MenitKeyPressed
        Valid.pindah(evt, Jam, Detik);
    }//GEN-LAST:event_MenitKeyPressed

    private void DetikKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DetikKeyPressed
        Valid.pindah(evt, Menit, BtnDokter);
    }//GEN-LAST:event_DetikKeyPressed

    private void KdDokterKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KdDokterKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_PAGE_DOWN) {
            NmDokter.setText(dokter.tampil3(KdDokter.getText()));
        } else if (evt.getKeyCode() == KeyEvent.VK_PAGE_UP) {
            Detik.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            Kesimpulan.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_UP) {
            BtnDokterActionPerformed(null);
        }
    }//GEN-LAST:event_KdDokterKeyPressed

    private void BtnDokterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnDokterActionPerformed
        dokter.isCek();
        dokter.setSize(internalFrame1.getWidth() - 20, internalFrame1.getHeight() - 20);
        dokter.setLocationRelativeTo(internalFrame1);
        dokter.setAlwaysOnTop(false);
        dokter.setVisible(true);
    }//GEN-LAST:event_BtnDokterActionPerformed

    private void BtnDokterKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnDokterKeyPressed
        Valid.pindah(evt, Detik, Kesimpulan);
    }//GEN-LAST:event_BtnDokterKeyPressed

    private void MnCatatanKeperawatanRawatJalanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MnCatatanKeperawatanRawatJalanActionPerformed
        if (tbObat.getSelectedRow() > -1) {
            Map<String, Object> param = new HashMap<>();
            param.put("namars", akses.getnamars());
            param.put("alamatrs", akses.getalamatrs());
            param.put("kotars", akses.getkabupatenrs());
            param.put("propinsirs", akses.getpropinsirs());
            param.put("kontakrs", akses.getkontakrs());
            param.put("emailrs", akses.getemailrs());
            param.put("dpjp", Sequel.cariIsi("select dokter.nm_dokter from reg_periksa inner join dokter on reg_periksa.kd_dokter=dokter.kd_dokter where reg_periksa.no_rawat=?", tbObat.getValueAt(tbObat.getSelectedRow(), 0).toString()));
            param.put("logo", Sequel.cariGambar("select setting.logo from setting"));
            Valid.MyReportqry("rptFormulirCatatanKeperawatanRalan.jasper", "report", "::[ Formulir Catatan Keperawatan Rawat Jalan ]::",
                    "select reg_periksa.no_rawat,pasien.no_rkm_medis,pasien.nm_pasien,reg_periksa.umurdaftar,reg_periksa.sttsumur,reg_periksa.tgl_registrasi,reg_periksa.jam_reg,"
                    + "pasien.jk,pasien.tgl_lahir,pemeriksaan_fisik_mata.tanggal,pemeriksaan_fisik_mata.jam,pemeriksaan_fisik_mata.uraian,"
                    + "petugas.nama from pemeriksaan_fisik_mata inner join reg_periksa on pemeriksaan_fisik_mata.no_rawat=reg_periksa.no_rawat "
                    + "inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "
                    + "inner join petugas on pemeriksaan_fisik_mata.nip=petugas.nip where reg_periksa.no_rawat='" + tbObat.getValueAt(tbObat.getSelectedRow(), 0).toString() + "'", param);
        }
    }//GEN-LAST:event_MnCatatanKeperawatanRawatJalanActionPerformed

    private void KesimpulanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KesimpulanKeyPressed
        Valid.pindah2(evt, KdDokter, BtnSimpan);
    }//GEN-LAST:event_KesimpulanKeyPressed

    private void SaranKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SaranKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_SaranKeyPressed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            RMPemeriksaanFisikMata dialog = new RMPemeriksaanFisikMata(new javax.swing.JFrame(), true);
            dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    System.exit(0);
                }
            });
            dialog.setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private widget.Button BtnAll;
    private widget.Button BtnBatal;
    private widget.Button BtnCari;
    private widget.Button BtnDokter;
    private widget.Button BtnEdit;
    private widget.Button BtnHapus;
    private widget.Button BtnKeluar;
    private widget.Button BtnPrint;
    private widget.Button BtnSimpan;
    private widget.CekBox ChkInput;
    private widget.CekBox ChkKejadian;
    private widget.Tanggal DTPCari1;
    private widget.Tanggal DTPCari2;
    private widget.TextBox DekatKanan;
    private widget.TextBox DekatKiri;
    private widget.ComboBox Detik;
    private widget.PanelBiasa FormInput;
    private widget.ComboBox Fundus;
    private widget.ComboBox Gerakan;
    private widget.TextBox JK;
    private widget.ComboBox Jam;
    private widget.TextBox JauhKanan;
    private widget.TextBox JauhKiri;
    private widget.TextBox KdDokter;
    private widget.ComboBox Kelopak;
    private widget.TextBox Kepala;
    private widget.TextArea Kesimpulan;
    private widget.ComboBox Kornea;
    private widget.Label LCount;
    private widget.TextBox Mata;
    private widget.ComboBox Menit;
    private javax.swing.JMenuItem MnCatatanKeperawatanRawatJalan;
    private widget.TextBox NmDokter;
    private javax.swing.JPanel PanelInput;
    private widget.TextArea Saran;
    private widget.ScrollPane Scroll;
    private widget.ComboBox Sklera;
    private widget.TextBox TCari;
    private widget.TextBox TNoRM;
    private widget.TextBox TNoRw;
    private widget.TextBox TPasien;
    private widget.Tanggal Tanggal;
    private widget.TextBox TglLahir;
    private widget.TextBox Umur;
    private widget.ComboBox Warna;
    private widget.InternalFrame internalFrame1;
    private widget.Label jLabel10;
    private widget.Label jLabel11;
    private widget.Label jLabel12;
    private widget.Label jLabel13;
    private widget.Label jLabel14;
    private widget.Label jLabel15;
    private widget.Label jLabel16;
    private widget.Label jLabel17;
    private widget.Label jLabel18;
    private widget.Label jLabel19;
    private widget.Label jLabel20;
    private widget.Label jLabel21;
    private widget.Label jLabel22;
    private widget.Label jLabel23;
    private widget.Label jLabel24;
    private widget.Label jLabel25;
    private widget.Label jLabel26;
    private widget.Label jLabel27;
    private widget.Label jLabel28;
    private widget.Label jLabel4;
    private widget.Label jLabel6;
    private widget.Label jLabel7;
    private widget.Label jLabel8;
    private widget.Label jLabel9;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPopupMenu jPopupMenu1;
    private widget.panelisi panelGlass8;
    private widget.panelisi panelGlass9;
    private widget.ScrollPane scrollPane2;
    private widget.ScrollPane scrollPane3;
    private widget.Table tbObat;
    // End of variables declaration//GEN-END:variables

    public void tampil() {
        Valid.tabelKosong(tabMode);
        try {
            if (TCari.getText().toString().trim().equals("")) {
                ps = koneksi.prepareStatement(
                        "SELECT reg_periksa.no_rawat, reg_periksa.umurdaftar, reg_periksa.sttsumur,"
                        + "pasien.no_rkm_medis, pasien.nm_pasien, pasien.jk, pasien.tgl_lahir,"
                        + "pemeriksaan_fisik_mata.tanggal, pemeriksaan_fisik_mata.jam, pemeriksaan_fisik_mata.nip,"
                        + "pemeriksaan_fisik_mata.kepala, pemeriksaan_fisik_mata.mata, pemeriksaan_fisik_mata.jauh_kanan,"
                        + "pemeriksaan_fisik_mata.jauh_kiri, pemeriksaan_fisik_mata.dekat_kanan, pemeriksaan_fisik_mata.dekat_kiri,"
                        + "pemeriksaan_fisik_mata.warna, pemeriksaan_fisik_mata.kelopak, pemeriksaan_fisik_mata.kornea,"
                        + "pemeriksaan_fisik_mata.sklera, pemeriksaan_fisik_mata.fundus, pemeriksaan_fisik_mata.gerakan,"
                        + "pemeriksaan_fisik_mata.kesimpulan, pemeriksaan_fisik_mata.saran, dokter.nm_dokter "
                        + "FROM reg_periksa INNER JOIN pasien ON reg_periksa.no_rkm_medis = pasien.no_rkm_medis "
                        + "INNER JOIN pemeriksaan_fisik_mata ON reg_periksa.no_rawat = pemeriksaan_fisik_mata.no_rawat "
                        + "INNER JOIN dokter ON pemeriksaan_fisik_mata.nip = dokter.kd_dokter where "
                        + "pemeriksaan_fisik_mata.tanggal between ? and ? order by pemeriksaan_fisik_mata.tanggal");
            } else {
                ps = koneksi.prepareStatement(
                        "SELECT reg_periksa.no_rawat, reg_periksa.umurdaftar, reg_periksa.sttsumur,"
                        + "pasien.no_rkm_medis, pasien.nm_pasien, pasien.jk, pasien.tgl_lahir,"
                        + "pemeriksaan_fisik_mata.tanggal, pemeriksaan_fisik_mata.jam, pemeriksaan_fisik_mata.nip,"
                        + "pemeriksaan_fisik_mata.kepala, pemeriksaan_fisik_mata.mata, pemeriksaan_fisik_mata.jauh_kanan,"
                        + "pemeriksaan_fisik_mata.jauh_kiri, pemeriksaan_fisik_mata.dekat_kanan, pemeriksaan_fisik_mata.dekat_kiri,"
                        + "pemeriksaan_fisik_mata.warna, pemeriksaan_fisik_mata.kelopak, pemeriksaan_fisik_mata.kornea,"
                        + "pemeriksaan_fisik_mata.sklera, pemeriksaan_fisik_mata.fundus, pemeriksaan_fisik_mata.gerakan,"
                        + "pemeriksaan_fisik_mata.kesimpulan, pemeriksaan_fisik_mata.saran, dokter.nm_dokter "
                        + "FROM reg_periksa INNER JOIN pasien ON reg_periksa.no_rkm_medis = pasien.no_rkm_medis "
                        + "INNER JOIN pemeriksaan_fisik_mata ON reg_periksa.no_rawat = pemeriksaan_fisik_mata.no_rawat "
                        + "INNER JOIN dokter ON pemeriksaan_fisik_mata.nip = dokter.kd_dokter where "
                        + "pemeriksaan_fisik_mata.tanggal between ? and ? and (reg_periksa.no_rawat like ? or pasien.no_rkm_medis like ? or pasien.nm_pasien like ? or pemeriksaan_fisik_mata.nip like ? or dokter.nm_dokter like ?) "
                        + "order by pemeriksaan_fisik_mata.tanggal ");
            }

            try {
                if (TCari.getText().toString().trim().equals("")) {
                    ps.setString(1, Valid.SetTgl(DTPCari1.getSelectedItem() + "") + " 00:00:00");
                    ps.setString(2, Valid.SetTgl(DTPCari2.getSelectedItem() + "") + " 23:59:59");
                } else {
                    ps.setString(1, Valid.SetTgl(DTPCari1.getSelectedItem() + "") + " 00:00:00");
                    ps.setString(2, Valid.SetTgl(DTPCari2.getSelectedItem() + "") + " 23:59:59");
                    ps.setString(3, "%" + TCari.getText() + "%");
                    ps.setString(4, "%" + TCari.getText() + "%");
                    ps.setString(5, "%" + TCari.getText() + "%");
                    ps.setString(6, "%" + TCari.getText() + "%");
                    ps.setString(7, "%" + TCari.getText() + "%");
                }

                rs = ps.executeQuery();
                while (rs.next()) {
                    tabMode.addRow(new String[]{
                        rs.getString("no_rawat"), rs.getString("no_rkm_medis"), rs.getString("nm_pasien"),
                        rs.getString("tgl_lahir"), rs.getString("tanggal"), rs.getString("jam"),
                        rs.getString("nip"), rs.getString("nm_dokter"), rs.getString("kepala"), rs.getString("mata"),
                        rs.getString("jauh_kanan"), rs.getString("jauh_kiri"), rs.getString("dekat_kanan"),
                        rs.getString("dekat_kiri"), rs.getString("warna"), rs.getString("kelopak"),
                        rs.getString("kornea"), rs.getString("sklera"), rs.getString("fundus"),
                        rs.getString("gerakan"), rs.getString("kesimpulan"), rs.getString("saran"),});
                }
            } catch (Exception e) {
                System.out.println("Notif : " + e);
            } finally {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            }
        } catch (SQLException e) {
            System.out.println("Notifikasi : " + e);
        }
        LCount.setText("" + tabMode.getRowCount());
    }

    public void emptTeks() {
        TNoRw.setText("");
        TNoRM.setText("");
        TPasien.setText("");
        TglLahir.setText("");
        KdDokter.setText("");
        NmDokter.setText("");
        Kepala.setText("");
        Mata.setText("");
        JauhKanan.setText("");
        JauhKiri.setText("");
        DekatKanan.setText("");
        DekatKiri.setText("");
        Warna.setSelectedIndex(0);
        Kelopak.setSelectedIndex(0);
        Kornea.setSelectedIndex(0);
        Sklera.setSelectedIndex(0);
        Fundus.setSelectedIndex(0);
        Gerakan.setSelectedIndex(0);
        Saran.setText("");
        Tanggal.setDate(new Date());
        Kesimpulan.requestFocus();
    }

    private void getData() {
        if (tbObat.getSelectedRow() != -1) {
            TNoRw.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 0).toString());
            TNoRM.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 1).toString());
            TPasien.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 2).toString());
            TglLahir.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 3).toString());
            Jam.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 5).toString().substring(0, 2));
            Menit.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 5).toString().substring(3, 5));
            Detik.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 5).toString().substring(6, 8));
            KdDokter.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 6).toString());
            NmDokter.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 7).toString());
            Kepala.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 8).toString());
            Mata.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 9).toString());
            JauhKanan.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 10).toString());
            JauhKiri.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 11).toString());
            DekatKanan.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 12).toString());
            DekatKiri.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 13).toString());
            Warna.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 14).toString());
            Kelopak.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 15).toString());
            Kornea.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 16).toString());
            Sklera.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 17).toString());
            Fundus.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 18).toString());
            Gerakan.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 19).toString());
            Kesimpulan.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 20).toString());
            Saran.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 21).toString());            
            Valid.SetTgl(Tanggal, tbObat.getValueAt(tbObat.getSelectedRow(), 4).toString());
        }
    }

    private void isRawat() {
        try {
            ps = koneksi.prepareStatement(
                    "select reg_periksa.no_rkm_medis,pasien.nm_pasien,pasien.jk,pasien.tgl_lahir,reg_periksa.tgl_registrasi,reg_periksa.umurdaftar,"
                    + "reg_periksa.sttsumur from reg_periksa inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis where reg_periksa.no_rawat=?");
            try {
                ps.setString(1, TNoRw.getText());
                rs = ps.executeQuery();
                if (rs.next()) {
                    TNoRM.setText(rs.getString("no_rkm_medis"));
                    DTPCari1.setDate(rs.getDate("tgl_registrasi"));
                    TPasien.setText(rs.getString("nm_pasien"));
                    JK.setText(rs.getString("jk"));
                    Umur.setText(rs.getString("umurdaftar") + " " + rs.getString("sttsumur"));
                    TglLahir.setText(rs.getString("tgl_lahir"));
                }
            } catch (Exception e) {
                System.out.println("Notif : " + e);
            } finally {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notif : " + e);
        }
    }

    public void setNoRm(String norwt, Date tgl2) {
        TNoRw.setText(norwt);
        TCari.setText(norwt);
        DTPCari2.setDate(tgl2);
        isRawat();
        ChkInput.setSelected(true);
        isForm();
    }

    private void isForm() {
        if (ChkInput.isSelected() == true) {
            ChkInput.setVisible(false);
            PanelInput.setPreferredSize(new Dimension(WIDTH, 320));
            FormInput.setVisible(true);
            ChkInput.setVisible(true);
        } else if (ChkInput.isSelected() == false) {
            ChkInput.setVisible(false);
            PanelInput.setPreferredSize(new Dimension(WIDTH, 20));
            FormInput.setVisible(false);
            ChkInput.setVisible(true);
        }
    }

    public void isCek() {
        BtnSimpan.setEnabled(akses.getpenilaian_awal_medis_ralan_mata());
        BtnHapus.setEnabled(akses.getpenilaian_awal_medis_ralan_mata());
        BtnEdit.setEnabled(akses.getpenilaian_awal_medis_ralan_mata());
        BtnPrint.setEnabled(akses.getpenilaian_awal_medis_ralan_mata());
        if (akses.getjml2() >= 1) {
            KdDokter.setEditable(false);
            BtnDokter.setEnabled(false);
            KdDokter.setText(akses.getkode());
            NmDokter.setText(dokter.tampil3(KdDokter.getText()));
            if (NmDokter.getText().equals("")) {
                KdDokter.setText("");
                JOptionPane.showMessageDialog(null, "User login bukan Dokter...!!");
            }
        }
    }

    private void jam() {
        ActionListener taskPerformer = new ActionListener() {
            private int nilai_jam;
            private int nilai_menit;
            private int nilai_detik;

            public void actionPerformed(ActionEvent e) {
                String nol_jam = "";
                String nol_menit = "";
                String nol_detik = "";

                Date now = Calendar.getInstance().getTime();

                // Mengambil nilaj JAM, MENIT, dan DETIK Sekarang
                if (ChkKejadian.isSelected() == true) {
                    nilai_jam = now.getHours();
                    nilai_menit = now.getMinutes();
                    nilai_detik = now.getSeconds();
                } else if (ChkKejadian.isSelected() == false) {
                    nilai_jam = Jam.getSelectedIndex();
                    nilai_menit = Menit.getSelectedIndex();
                    nilai_detik = Detik.getSelectedIndex();
                }

                // Jika nilai JAM lebih kecil dari 10 (hanya 1 digit)
                if (nilai_jam <= 9) {
                    // Tambahkan "0" didepannya
                    nol_jam = "0";
                }
                // Jika nilai MENIT lebih kecil dari 10 (hanya 1 digit)
                if (nilai_menit <= 9) {
                    // Tambahkan "0" didepannya
                    nol_menit = "0";
                }
                // Jika nilai DETIK lebih kecil dari 10 (hanya 1 digit)
                if (nilai_detik <= 9) {
                    // Tambahkan "0" didepannya
                    nol_detik = "0";
                }
                // Membuat String JAM, MENIT, DETIK
                String jam = nol_jam + Integer.toString(nilai_jam);
                String menit = nol_menit + Integer.toString(nilai_menit);
                String detik = nol_detik + Integer.toString(nilai_detik);
                // Menampilkan pada Layar
                //tampil_jam.setText("  " + jam + " : " + menit + " : " + detik + "  ");
                Jam.setSelectedItem(jam);
                Menit.setSelectedItem(menit);
                Detik.setSelectedItem(detik);
            }
        };
        // Timer
        new Timer(1000, taskPerformer).start();
    }

    private void ganti() {
        /*
        "No.Rawat", "No.R.M.", "Nama Pasien", "Tgl.Lahir", "Tanggal", "Jam", "NIP", "Nama Dokter",
            "Kepala", "Mata", "Melihat Jauh Kanan", "Melihat Jauh Kiri", "Melihat Dekat Kanan",
            "Melihat Dekat Kiri", "Melihat Warna", "Kelopak Mata dan Selaput Mata", "Kornea dan Pupil",
            "Sklera", "Fundus", "Gerakan Mata", "Kesimpulan", "Saran"
         */

        if (Sequel.mengedittf("pemeriksaan_fisik_mata", "tanggal=? and jam=? and no_rawat=?", "no_rawat=?,tanggal=?,jam=?,nip=?,kepala=?,mata=?,jauh_kanan=?,jauh_kiri=?,"
                + "dekat_kanan=?,dekat_kiri=?,warna=?,kelopak=?,kornea=?,sklera=?,fundus=?,gerakan=?,kesimpulan=?,saran=?", 21, new String[]{
                    TNoRw.getText(), Valid.SetTgl(Tanggal.getSelectedItem() + ""), Jam.getSelectedItem() + ":" + Menit.getSelectedItem() + ":" + Detik.getSelectedItem(), KdDokter.getText(),
                    Kepala.getText(), Mata.getText(), JauhKanan.getText(), JauhKiri.getText(), DekatKanan.getText(), DekatKiri.getText(), Warna.getSelectedItem().toString(),
                    Kelopak.getSelectedItem().toString(), Kornea.getSelectedItem().toString(), Sklera.getSelectedItem().toString(), Fundus.getSelectedItem().toString(), Gerakan.getSelectedItem().toString(),
                    Kesimpulan.getText(), Saran.getText(), tbObat.getValueAt(tbObat.getSelectedRow(), 4).toString(), tbObat.getValueAt(tbObat.getSelectedRow(), 5).toString(),
                    tbObat.getValueAt(tbObat.getSelectedRow(), 0).toString()
                }) == true) {
            tbObat.setValueAt(TNoRw.getText(), tbObat.getSelectedRow(), 0);
            tbObat.setValueAt(TNoRM.getText(), tbObat.getSelectedRow(), 1);
            tbObat.setValueAt(TPasien.getText(), tbObat.getSelectedRow(), 2);
            tbObat.setValueAt(Umur.getText(), tbObat.getSelectedRow(), 3);
            tbObat.setValueAt(JK.getText(), tbObat.getSelectedRow(), 4);
            tbObat.setValueAt(TglLahir.getText(), tbObat.getSelectedRow(), 5);
            tbObat.setValueAt(Valid.SetTgl(Tanggal.getSelectedItem() + ""), tbObat.getSelectedRow(), 6);
            tbObat.setValueAt(Jam.getSelectedItem() + ":" + Menit.getSelectedItem() + ":" + Detik.getSelectedItem(), tbObat.getSelectedRow(), 7);
            tbObat.setValueAt(Kesimpulan.getText(), tbObat.getSelectedRow(), 8);
            tbObat.setValueAt(KdDokter.getText(), tbObat.getSelectedRow(), 9);
            tbObat.setValueAt(NmDokter.getText(), tbObat.getSelectedRow(), 10);
            emptTeks();
        }
    }

    private void hapus() {
        if (Sequel.queryu2tf("delete from pemeriksaan_fisik_mata where tanggal=? and jam=? and no_rawat=?", 3, new String[]{
            tbObat.getValueAt(tbObat.getSelectedRow(), 6).toString(), tbObat.getValueAt(tbObat.getSelectedRow(), 7).toString(), tbObat.getValueAt(tbObat.getSelectedRow(), 0).toString()
        }) == true) {
            tabMode.removeRow(tbObat.getSelectedRow());
            LCount.setText("" + tabMode.getRowCount());
            emptTeks();
        } else {
            JOptionPane.showMessageDialog(null, "Gagal menghapus..!!");
        }
    }
}
