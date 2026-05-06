/*
 * Kontribusi dari dr. Salim Mulyana
 */
package rekammedis;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fungsi.WarnaTable;
import fungsi.batasInput;
import fungsi.koneksiDB;
import fungsi.sekuel;
import fungsi.validasi;
import fungsi.akses;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import kepegawaian.DlgCariDokter;
import kepegawaian.DlgCariPetugas;

/**
 *
 * @author perpustakaan
 */
public final class RMAsesmenUlangHemodialisis extends javax.swing.JDialog {

    private final DefaultTableModel tabMode, tabMode2, tabMode3, tabMode4, tabModeMasalah, tabModeDetailMasalah, tabModeRencana, tabModeDetailRencana, tabModePenyulit, tabModeDetailPenyulit;
    private Connection koneksi = koneksiDB.condb();
    private DlgCariDokter dokter = new DlgCariDokter(null, false);
    private sekuel Sequel = new sekuel();
    private validasi Valid = new validasi();
    private PreparedStatement ps, ps2;
    private ResultSet rs, rs2;
    private int i = 0, jml = 0, index = 0, pilihan = 0;
    private DlgCariPetugas petugas = new DlgCariPetugas(null, false);
    private boolean[] pilih;
    private String[] kode, masalah;
    private String masalahkeperawatan = "", finger = "";
    private StringBuilder htmlContent;
    private File file;
    private FileWriter fileWriter;
    private String iyem;
    private ObjectMapper mapper = new ObjectMapper();
    private JsonNode root;
    private JsonNode response;
    private FileReader myObj;
    private String TANGGALMUNDUR = "yes";

    /**
     * Creates new form DlgRujuk
     *
     * @param parent
     * @param modal
     */
    public RMAsesmenUlangHemodialisis(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

        tabMode = new DefaultTableModel(null, new Object[]{
            "No.Rawat", "No.RM", "Nama Pasien", "JK", "Tgl.Lahir", "Kode Perawat", "Nama Perawat", "Tgl.Asuhan", "Informasi",
            "Diagnosa Medis", "Terapi HD Ke", "Tipe Dializer", "Riwayat Alergi", "Pasien Merasa Nyeri", "Intensitas Nyeri", "Lokasi Nyeri", "Sejak Kapan", "Sifat Nyeri",
            "Sifat Nyeri Keterangan", "Kualitas Nyeri", "Keterangan Kualitas Nyeri", "Faktor Pemberat", "Keterangan Faktor Pemberat", "Faktor Peringan", "Keterangan Faktor Peringan",
            "Efek Nyeri", "Keterangan Efek Nyeri", "Keadaan umum", "Keterangan Keadaan Umum", "Tekanan Darah", "Nadi", "Frekuensi Nadi", "Respirasi", "Frekuensi Respirasi",
            "Konjungtiva", "Keterangan Konjungtiva", "Ektremitas", "BB Kering", "BB Pre HD", "BB HD yang lalu", "BB PostHD", "Akses Vaskular", "Jalur Akses", "Keterangan Jalur Akses",
            "Cara Berjalan", "Alat Bantu", "Saat Duduk", "Intervensi Resiko Jatuh", "Intervensi Kolaborasi"
        }) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false;
            }
        };
        tbObat.setModel(tabMode);

        tbObat.setPreferredScrollableViewportSize(new Dimension(500, 500));
        tbObat.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

// Atur lebar kolom
        int[] columnWidths = {105, 65, 160, 90, 50, 65, 120, 90, 35, 40, 35, 40, 35, 35, 35, 35, 35, 35,
            90, 90, 100, 90, 90, 100, 100, 100, 100, 90, 120, 100, 60, 60, 60, 80, 80, 80, 80, 80,
            80, 80, 100, 90, 90, 100, 90, 90, 90, 90, 90};

        for (int i = 0; i < columnWidths.length; i++) {
            tbObat.getColumnModel().getColumn(i).setPreferredWidth(columnWidths[i]);
        }

        tbObat.setDefaultRenderer(Object.class, new WarnaTable());

        tabMode2 = new DefaultTableModel(null, new Object[]{
            "No.Rawat", "No.RM", "Nama Pasien", "J.K.", "Tanggal Lahir", "Tgl.Asuhan",
            "Resep HD", "HD", "QB", "QD", "UF Goal", "Heparinisasi", "Keterangan Heparinisasi", "Dialisat", "Kode Dokter", "Nama Dokter"
        }) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false;
            }
        };
        tbObat1.setModel(tabMode2);

        tbObat1.setPreferredScrollableViewportSize(new Dimension(500, 500));
        tbObat1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

// Atur lebar kolom
        int[] columnWidths2 = {105, 65, 160, 50, 100, 90, 120, 90, 60, 50, 50, 80, 90, 120, 80, 100};

        for (int i = 0; i < columnWidths2.length; i++) {
            tbObat1.getColumnModel().getColumn(i).setPreferredWidth(columnWidths2[i]);
        }

        tbObat1.setDefaultRenderer(Object.class, new WarnaTable());

        tabMode3 = new DefaultTableModel(null, new Object[]{
            "No.Rawat", "No.RM", "Nama Pasien", "Tgl.Lahir", "J.K.",
            "Tanggal Perawatan", "Jam Perawatan", "Observasi", "QB", "UF Rate", "TD", "Nadi", "Suhu",
            "Respirasi", "VP", "AP", "TMP", "Keterangan Lain", "Kode Perawat", "Nama Perawat"
        }) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false;
            }
        };
        tbObat2.setModel(tabMode3);

        tbObat2.setPreferredScrollableViewportSize(new Dimension(500, 500));
        tbObat2.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

// Atur lebar kolom
        int[] columnWidths3 = {105, 65, 160, 50, 65, 120, 90, 120, 60, 60, 60, 60, 60, 60, 60, 60, 60, 120, 100, 120};

        for (int i = 0; i < columnWidths3.length; i++) {
            tbObat2.getColumnModel().getColumn(i).setPreferredWidth(columnWidths3[i]);
        }

        tbObat2.setDefaultRenderer(Object.class, new WarnaTable());

        tabMode4 = new DefaultTableModel(null, new Object[]{
            "No.Rawat", "No.RM", "Nama Pasien", "J.K.", "Tgl.Lahir",
            "Tanggal Asuhan", "Evaluasi", "Kode Perawat", "Nama Perawat"
        }) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false;
            }
        };
        tbObat3.setModel(tabMode4);

        tbObat3.setPreferredScrollableViewportSize(new Dimension(500, 500));
        tbObat3.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

// Atur lebar kolom
        int[] columnWidths4 = {105, 65, 160, 90, 65, 120, 90, 100, 120};

        for (int i = 0; i < columnWidths4.length; i++) {
            tbObat3.getColumnModel().getColumn(i).setPreferredWidth(columnWidths4[i]);
        }

        tbObat3.setDefaultRenderer(Object.class, new WarnaTable());

        tabModeMasalah = new DefaultTableModel(null, new Object[]{
            "P", "KODE", "MASALAH KEPERAWATAN"
        }) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                boolean a = false;
                if (colIndex == 0) {
                    a = true;
                }
                return a;
            }
            Class[] types = new Class[]{
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Double.class
            };

            @Override
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }
        };
        tbMasalahKeperawatan.setModel(tabModeMasalah);

        //tbObat.setDefaultRenderer(Object.class, new WarnaTable(panelJudul.getBackground(),tbObat.getBackground()));
        tbMasalahKeperawatan.setPreferredScrollableViewportSize(new Dimension(500, 500));
        tbMasalahKeperawatan.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 3; i++) {
            TableColumn column = tbMasalahKeperawatan.getColumnModel().getColumn(i);
            if (i == 0) {
                column.setPreferredWidth(20);
            } else if (i == 1) {
                column.setMinWidth(0);
                column.setMaxWidth(0);
            } else if (i == 2) {
                column.setPreferredWidth(350);
            }
        }
        tbMasalahKeperawatan.setDefaultRenderer(Object.class, new WarnaTable());

        tabModePenyulit = new DefaultTableModel(null, new Object[]{
            "P", "KODE", "PENYULIT"
        }) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                boolean a = false;
                if (colIndex == 0) {
                    a = true;
                }
                return a;
            }
            Class[] types = new Class[]{
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Double.class
            };

            @Override
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }
        };
        tbPenyulit.setModel(tabModePenyulit);

        //tbObat.setDefaultRenderer(Object.class, new WarnaTable(panelJudul.getBackground(),tbObat.getBackground()));
        tbPenyulit.setPreferredScrollableViewportSize(new Dimension(500, 500));
        tbPenyulit.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 3; i++) {
            TableColumn column = tbPenyulit.getColumnModel().getColumn(i);
            if (i == 0) {
                column.setPreferredWidth(20);
            } else if (i == 1) {
                column.setMinWidth(0);
                column.setMaxWidth(0);
            } else if (i == 2) {
                column.setPreferredWidth(350);
            }
        }
        tbPenyulit.setDefaultRenderer(Object.class, new WarnaTable());

        tabModeRencana = new DefaultTableModel(null, new Object[]{
            "P", "KODE", "RENCANA KEPERAWATAN"
        }) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                boolean a = false;
                if (colIndex == 0) {
                    a = true;
                }
                return a;
            }
            Class[] types = new Class[]{
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Double.class
            };

            @Override
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }
        };
        tbRencanaKeperawatan.setModel(tabModeRencana);

        //tbObat.setDefaultRenderer(Object.class, new WarnaTable(panelJudul.getBackground(),tbObat.getBackground()));
        tbRencanaKeperawatan.setPreferredScrollableViewportSize(new Dimension(500, 500));
        tbRencanaKeperawatan.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 3; i++) {
            TableColumn column = tbRencanaKeperawatan.getColumnModel().getColumn(i);
            if (i == 0) {
                column.setPreferredWidth(20);
            } else if (i == 1) {
                column.setMinWidth(0);
                column.setMaxWidth(0);
            } else if (i == 2) {
                column.setPreferredWidth(350);
            }
        }
        tbRencanaKeperawatan.setDefaultRenderer(Object.class, new WarnaTable());

        tabModeDetailMasalah = new DefaultTableModel(null, new Object[]{
            "Kode", "Masalah Keperawatan"
        }) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false;
            }
        };
        tbMasalahDetail.setModel(tabModeDetailMasalah);

        //tbObat.setDefaultRenderer(Object.class, new WarnaTable(panelJudul.getBackground(),tbObat.getBackground()));
        tbMasalahDetail.setPreferredScrollableViewportSize(new Dimension(500, 500));
        tbMasalahDetail.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 2; i++) {
            TableColumn column = tbMasalahDetail.getColumnModel().getColumn(i);
            if (i == 0) {
                column.setMinWidth(0);
                column.setMaxWidth(0);
            } else if (i == 1) {
                column.setPreferredWidth(420);
            }
        }
        tbMasalahDetail.setDefaultRenderer(Object.class, new WarnaTable());

        tabModeDetailPenyulit = new DefaultTableModel(null, new Object[]{
            "Kode", "Penyulit HD"
        }) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false;
            }
        };

        /*    tbPenyulitDetail.setModel(tabModeDetailPenyulit);

        //tbObat.setDefaultRenderer(Object.class, new WarnaTable(panelJudul.getBackground(),tbObat.getBackground()));
        tbPenyulitDetail.setPreferredScrollableViewportSize(new Dimension(500, 500));
        tbPenyulitDetail.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 2; i++) {
            TableColumn column = tbPenyulit.getColumnModel().getColumn(i);
            if (i == 0) {
                column.setMinWidth(0);
                column.setMaxWidth(0);
            } else if (i == 1) {
                column.setPreferredWidth(420);
            }
        }
        tbPenyulitDetail.setDefaultRenderer(Object.class, new WarnaTable()); */
        tabModeDetailRencana = new DefaultTableModel(null, new Object[]{
            "Kode", "Rencana Keperawatan"
        }) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false;
            }
        };
        tbRencanaDetail.setModel(tabModeDetailRencana);

        //tbObat.setDefaultRenderer(Object.class, new WarnaTable(panelJudul.getBackground(),tbObat.getBackground()));
        tbRencanaDetail.setPreferredScrollableViewportSize(new Dimension(500, 500));
        tbRencanaDetail.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 2; i++) {
            TableColumn column = tbRencanaDetail.getColumnModel().getColumn(i);
            if (i == 0) {
                column.setMinWidth(0);
                column.setMaxWidth(0);
            } else if (i == 1) {
                column.setPreferredWidth(420);
            }
        }
        tbRencanaDetail.setDefaultRenderer(Object.class, new WarnaTable());

        TNoRw.setDocument(new batasInput((byte) 17).getKata(TNoRw));
        KualitasNyeriKet.setDocument(new batasInput((byte) 100).getKata(KualitasNyeriKet));
        FaktorPemberatKet.setDocument(new batasInput((byte) 100).getKata(FaktorPemberatKet));
        FaktoPeringanKet.setDocument(new batasInput((byte) 100).getKata(FaktoPeringanKet));
        Dializer.setDocument(new batasInput((byte) 100).getKata(Dializer));
        SifatNyeriKet.setDocument(new batasInput((byte) 100).getKata(SifatNyeriKet));
        EfekNyeriKet.setDocument(new batasInput((byte) 100).getKata(EfekNyeriKet));
        DiagnosaMedis.setDocument(new batasInput((byte) 100).getKata(DiagnosaMedis));
        TerapiKe.setDocument(new batasInput((byte) 10).getKata(TerapiKe));
        Evaluasi.setDocument(new batasInput((int) 150).getKata(Evaluasi));
        KdDokter.setDocument(new batasInput((int) 100).getKata(KdDokter));
        Uf.setDocument(new batasInput((int) 100).getKata(Uf));
        NmDokter.setDocument(new batasInput((int) 100).getKata(NmDokter));
        Alergi.setDocument(new batasInput((int) 100).getKata(Alergi));
        LokasiNyeri.setDocument(new batasInput((int) 100).getKata(LokasiNyeri));
        Kapan.setDocument(new batasInput((int) 100).getKata(Kapan));
        BbPostHD.setDocument(new batasInput((int) 50).getKata(BbPostHD));
        KuKet.setDocument(new batasInput((int) 100).getKata(KuKet));
        RespirasiKet.setDocument(new batasInput((int) 100).getKata(RespirasiKet));
        JalurKet.setDocument(new batasInput((int) 100).getKata(JalurKet));
        BbKering.setDocument(new batasInput((int) 15).getKata(BbKering));
        Qb.setDocument(new batasInput((int) 15).getKata(Qb));
        BbPreHD.setDocument(new batasInput((int) 50).getKata(BbPreHD));
        Qd.setDocument(new batasInput((int) 40).getKata(Qd));
        IntervensiKolaborasi.setDocument(new batasInput((int) 200).getKata(IntervensiKolaborasi));

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

            TCariMasalah.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    if (TCariMasalah.getText().length() > 2) {
                        tampilMasalah2();
                    }
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    if (TCariMasalah.getText().length() > 2) {
                        tampilMasalah2();
                    }
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    if (TCariMasalah.getText().length() > 2) {
                        tampilMasalah2();
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

        petugas.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
            }

            @Override
            public void windowClosing(WindowEvent e) {
            }

            @Override
            public void windowClosed(WindowEvent e) {
                if (petugas.getTable().getSelectedRow() != -1) {
                    if (pilihan == 1) {
                        KdPetugas.setText(petugas.getTable().getValueAt(petugas.getTable().getSelectedRow(), 0).toString());
                        NmPetugas.setText(petugas.getTable().getValueAt(petugas.getTable().getSelectedRow(), 1).toString());
                    } else if (pilihan == 2) {
                        NIP.setText(petugas.getTable().getValueAt(petugas.getTable().getSelectedRow(), 0).toString());
                        NamaPetugas.setText(petugas.getTable().getValueAt(petugas.getTable().getSelectedRow(), 1).toString());
                    } else if (pilihan == 3) {
                        NIP1.setText(petugas.getTable().getValueAt(petugas.getTable().getSelectedRow(), 0).toString());
                        NamaPetugas1.setText(petugas.getTable().getValueAt(petugas.getTable().getSelectedRow(), 1).toString());
                    }

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

        EfekNyeriKet.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                isBMI();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                isBMI();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                isBMI();
            }
        });

        DiagnosaMedis.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                isBMI();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                isBMI();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                isBMI();
            }
        });

        jam();

        HTMLEditorKit kit = new HTMLEditorKit();
        LoadHTML.setEditable(true);
        LoadHTML.setEditorKit(kit);
        StyleSheet styleSheet = kit.getStyleSheet();
        styleSheet.addRule(
                ".isi td{border-right: 1px solid #e2e7dd;font: 8.5px tahoma;height:12px;border-bottom: 1px solid #e2e7dd;background: #ffffff;color:#323232;}"
                + ".isi2 td{font: 8.5px tahoma;border:none;height:12px;background: #ffffff;color:#323232;}"
                + ".isi3 td{border-right: 1px solid #e2e7dd;font: 8.5px tahoma;height:12px;border-top: 1px solid #e2e7dd;background: #ffffff;color:#323232;}"
                + ".isi4 td{font: 11px tahoma;height:12px;border-top: 1px solid #e2e7dd;background: #ffffff;color:#323232;}"
                + ".isi5 td{font: 8.5px tahoma;border:none;height:12px;background: #ffffff;color:#AA0000;}"
                + ".isi6 td{font: 8.5px tahoma;border:none;height:12px;background: #ffffff;color:#FF0000;}"
                + ".isi7 td{font: 8.5px tahoma;border:none;height:12px;background: #ffffff;color:#C8C800;}"
                + ".isi8 td{font: 8.5px tahoma;border:none;height:12px;background: #ffffff;color:#00AA00;}"
                + ".isi9 td{font: 8.5px tahoma;border:none;height:12px;background: #ffffff;color:#969696;}"
        );
        Document doc = kit.createDefaultDocument();
        LoadHTML.setDocument(doc);

        try {
            TANGGALMUNDUR = koneksiDB.TANGGALMUNDUR();
        } catch (Exception e) {
            TANGGALMUNDUR = "yes";
        }

        ChkAccor.setSelected(false);
        isMenu();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        LoadHTML = new widget.editorpane();
        TanggalRegistrasi = new widget.TextBox();
        internalFrame1 = new widget.InternalFrame();
        panelGlass8 = new widget.panelisi();
        BtnSimpan = new widget.Button();
        BtnBatal = new widget.Button();
        BtnHapus = new widget.Button();
        BtnEdit = new widget.Button();
        BtnPrint = new widget.Button();
        BtnAll = new widget.Button();
        BtnKeluar = new widget.Button();
        jLabel6 = new widget.Label();
        TCari = new widget.TextBox();
        BtnCari = new widget.Button();
        jLabel19 = new widget.Label();
        DTPCari1 = new widget.Tanggal();
        jLabel21 = new widget.Label();
        DTPCari2 = new widget.Tanggal();
        jLabel7 = new widget.Label();
        LCount = new widget.Label();
        TabRawat = new javax.swing.JTabbedPane();
        internalFrame2 = new widget.InternalFrame();
        scrollInput = new widget.ScrollPane();
        FormInput = new widget.PanelBiasa();
        label14 = new widget.Label();
        KdPetugas = new widget.TextBox();
        NmPetugas = new widget.TextBox();
        BtnDokter = new widget.Button();
        EfekNyeriKet = new widget.TextBox();
        DiagnosaMedis = new widget.TextBox();
        jLabel15 = new widget.Label();
        FaktorPemberatKet = new widget.TextBox();
        jLabel18 = new widget.Label();
        Dializer = new widget.TextBox();
        KualitasNyeriKet = new widget.TextBox();
        jLabel25 = new widget.Label();
        FaktoPeringanKet = new widget.TextBox();
        jLabel14 = new widget.Label();
        TerapiKe = new widget.TextBox();
        jLabel36 = new widget.Label();
        jLabel37 = new widget.Label();
        Alergi = new widget.TextBox();
        jLabel43 = new widget.Label();
        jLabel50 = new widget.Label();
        jLabel52 = new widget.Label();
        Informasi = new widget.ComboBox();
        jLabel30 = new widget.Label();
        jLabel31 = new widget.Label();
        IntensitasNyeri = new widget.ComboBox();
        LokasiNyeri = new widget.TextBox();
        jLabel54 = new widget.Label();
        NyeriPasien = new widget.ComboBox();
        Kapan = new widget.TextBox();
        jLabel55 = new widget.Label();
        FaktorPemberat = new widget.ComboBox();
        jLabel57 = new widget.Label();
        SifatNyeri = new widget.ComboBox();
        KuKet = new widget.TextBox();
        jLabel58 = new widget.Label();
        FaktorPeringan = new widget.ComboBox();
        jLabel59 = new widget.Label();
        EfekNyeri = new widget.ComboBox();
        RespirasiKet = new widget.TextBox();
        jLabel60 = new widget.Label();
        Respirasi = new widget.ComboBox();
        jLabel61 = new widget.Label();
        Nadi = new widget.ComboBox();
        JalurKet = new widget.TextBox();
        jLabel65 = new widget.Label();
        jLabel66 = new widget.Label();
        Jalur = new widget.ComboBox();
        Ku = new widget.ComboBox();
        Konjungtiva = new widget.ComboBox();
        AksesVaskular = new widget.ComboBox();
        jLabel68 = new widget.Label();
        BJM1 = new widget.ComboBox();
        BbKering = new widget.TextBox();
        jLabel70 = new widget.Label();
        ATS1 = new widget.ComboBox();
        Ektremitas = new widget.ComboBox();
        jLabel73 = new widget.Label();
        jLabel75 = new widget.Label();
        IntervensiJatuh = new widget.ComboBox();
        BbPreHD = new widget.TextBox();
        jLabel83 = new widget.Label();
        MSA1 = new widget.ComboBox();
        jLabel84 = new widget.Label();
        jLabel87 = new widget.Label();
        BbLalu = new widget.TextBox();
        SifatNyeriKet = new widget.TextBox();
        jLabel51 = new widget.Label();
        KonjungtivaKet = new widget.TextBox();
        jLabel62 = new widget.Label();
        jLabel95 = new widget.Label();
        KualitasNyeri = new widget.ComboBox();
        BbPostHD = new widget.TextBox();
        jLabel63 = new widget.Label();
        jSeparator6 = new javax.swing.JSeparator();
        jSeparator8 = new javax.swing.JSeparator();
        PanelWall = new usu.widget.glass.PanelGlass();
        jLabel71 = new widget.Label();
        NadiKet = new widget.TextBox();
        jLabel76 = new widget.Label();
        jLabel77 = new widget.Label();
        Td = new widget.TextBox();
        TabRencanaKeperawatan = new javax.swing.JTabbedPane();
        panelBiasa1 = new widget.PanelBiasa();
        Scroll8 = new widget.ScrollPane();
        tbRencanaKeperawatan = new widget.Table();
        scrollPane5 = new widget.ScrollPane();
        IntervensiKolaborasi = new widget.TextArea();
        jLabel78 = new widget.Label();
        jLabel53 = new widget.Label();
        jLabel72 = new widget.Label();
        jLabel79 = new widget.Label();
        jLabel90 = new widget.Label();
        jLabel91 = new widget.Label();
        jLabel93 = new widget.Label();
        HasilJatuh = new widget.ComboBox();
        jLabel94 = new widget.Label();
        label12 = new widget.Label();
        TCariMasalah = new widget.TextBox();
        BtnCariMasalah = new widget.Button();
        BtnAllMasalah = new widget.Button();
        BtnTambahMasalah = new widget.Button();
        label13 = new widget.Label();
        TCariRencana = new widget.TextBox();
        BtnCariRencana = new widget.Button();
        BtnAllRencana = new widget.Button();
        BtnTambahRencana = new widget.Button();
        Scroll6 = new widget.ScrollPane();
        tbMasalahKeperawatan = new widget.Table();
        internalFrame3 = new widget.InternalFrame();
        Scroll = new widget.ScrollPane();
        tbObat = new widget.Table();
        panelGlass9 = new widget.panelisi();
        PanelAccor = new widget.PanelBiasa();
        ChkAccor = new widget.CekBox();
        FormMenu = new widget.PanelBiasa();
        jLabel34 = new widget.Label();
        TNoRM1 = new widget.TextBox();
        TPasien1 = new widget.TextBox();
        BtnPrint1 = new widget.Button();
        FormMasalahRencana = new widget.PanelBiasa();
        Scroll7 = new widget.ScrollPane();
        tbMasalahDetail = new widget.Table();
        Scroll9 = new widget.ScrollPane();
        tbRencanaDetail = new widget.Table();
        scrollPane6 = new widget.ScrollPane();
        DetailRencana = new widget.TextArea();
        internalFrame4 = new widget.InternalFrame();
        Scroll1 = new widget.ScrollPane();
        tbObat1 = new widget.Table();
        PanelInput = new javax.swing.JPanel();
        ChkInput = new widget.CekBox();
        FormInput1 = new widget.PanelBiasa();
        jLabel26 = new widget.Label();
        jLabel69 = new widget.Label();
        Dialisat = new widget.ComboBox();
        Heparinisasi = new widget.ComboBox();
        Qb = new widget.TextBox();
        jLabel81 = new widget.Label();
        jLabel82 = new widget.Label();
        jLabel85 = new widget.Label();
        resepHd = new widget.ComboBox();
        jLabel86 = new widget.Label();
        jLabel88 = new widget.Label();
        Qd = new widget.TextBox();
        jLabel89 = new widget.Label();
        jLabel92 = new widget.Label();
        Hd = new widget.TextBox();
        Uf = new widget.TextBox();
        jLabel96 = new widget.Label();
        jLabel97 = new widget.Label();
        Heparinisasi_Ket = new widget.TextBox();
        jLabel98 = new widget.Label();
        KdDokter = new widget.TextBox();
        NmDokter = new widget.TextBox();
        BtnDokter1 = new widget.Button();
        internalFrame5 = new widget.InternalFrame();
        Scroll2 = new widget.ScrollPane();
        tbObat2 = new widget.Table();
        PanelInput1 = new javax.swing.JPanel();
        ChkInput1 = new widget.CekBox();
        FormInput2 = new widget.PanelBiasa();
        jLabel27 = new widget.Label();
        Qb2 = new widget.TextBox();
        jLabel100 = new widget.Label();
        jLabel102 = new widget.Label();
        Uf2 = new widget.TextBox();
        jLabel104 = new widget.Label();
        jLabel105 = new widget.Label();
        Observasi = new widget.TextBox();
        Td2 = new widget.TextBox();
        jLabel108 = new widget.Label();
        jLabel110 = new widget.Label();
        NIP = new widget.TextBox();
        NamaPetugas = new widget.TextBox();
        jLabel109 = new widget.Label();
        Nadi2 = new widget.TextBox();
        jLabel111 = new widget.Label();
        jLabel112 = new widget.Label();
        Suhu = new widget.TextBox();
        jLabel113 = new widget.Label();
        jLabel101 = new widget.Label();
        Respirasi2 = new widget.TextBox();
        jLabel114 = new widget.Label();
        Vp = new widget.TextBox();
        jLabel115 = new widget.Label();
        Ap = new widget.TextBox();
        Tmp = new widget.TextBox();
        jLabel117 = new widget.Label();
        jLabel119 = new widget.Label();
        Ket_Lain = new widget.TextBox();
        jLabel121 = new widget.Label();
        jLabel16 = new widget.Label();
        Tanggal = new widget.Tanggal();
        Jam = new widget.ComboBox();
        Menit = new widget.ComboBox();
        Detik = new widget.ComboBox();
        ChkKejadian = new widget.CekBox();
        btnPetugas = new widget.Button();
        internalFrame6 = new widget.InternalFrame();
        Scroll3 = new widget.ScrollPane();
        tbObat3 = new widget.Table();
        PanelInput2 = new javax.swing.JPanel();
        ChkInput2 = new widget.CekBox();
        FormInput3 = new widget.PanelBiasa();
        jLabel28 = new widget.Label();
        jLabel138 = new widget.Label();
        Evaluasi = new widget.TextBox();
        jLabel80 = new widget.Label();
        jLabel116 = new widget.Label();
        NIP1 = new widget.TextBox();
        NamaPetugas1 = new widget.TextBox();
        btnPetugas1 = new widget.Button();
        Scroll10 = new widget.ScrollPane();
        tbPenyulit = new widget.Table();
        label15 = new widget.Label();
        TCariPenyulit = new widget.TextBox();
        BtnCariMasalah1 = new widget.Button();
        BtnAllMasalah1 = new widget.Button();
        BtnTambahMasalah1 = new widget.Button();
        panelGlass10 = new widget.panelisi();
        jLabel10 = new widget.Label();
        TNoRw = new widget.TextBox();
        TNoRM = new widget.TextBox();
        TPasien = new widget.TextBox();
        jLabel8 = new widget.Label();
        TglLahir = new widget.TextBox();
        jLabel11 = new widget.Label();
        Jk = new widget.TextBox();
        label11 = new widget.Label();
        TglAsuhan = new widget.Tanggal();

        LoadHTML.setBorder(null);
        LoadHTML.setName("LoadHTML"); // NOI18N

        TanggalRegistrasi.setHighlighter(null);
        TanggalRegistrasi.setName("TanggalRegistrasi"); // NOI18N

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        internalFrame1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "::[ Asesmen Ulang Hemodialisis ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 50, 50))); // NOI18N
        internalFrame1.setFont(new java.awt.Font("Tahoma", 2, 12)); // NOI18N
        internalFrame1.setName("internalFrame1"); // NOI18N
        internalFrame1.setLayout(new java.awt.BorderLayout(1, 1));

        panelGlass8.setName("panelGlass8"); // NOI18N
        panelGlass8.setPreferredSize(new java.awt.Dimension(44, 54));
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

        BtnAll.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/Search-16x16.png"))); // NOI18N
        BtnAll.setMnemonic('M');
        BtnAll.setText("Semua");
        BtnAll.setToolTipText("Alt+M");
        BtnAll.setName("BtnAll"); // NOI18N
        BtnAll.setPreferredSize(new java.awt.Dimension(100, 30));
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
        panelGlass8.add(BtnAll);

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

        jLabel6.setText("Key Word :");
        jLabel6.setName("jLabel6"); // NOI18N
        jLabel6.setPreferredSize(new java.awt.Dimension(80, 23));
        panelGlass8.add(jLabel6);

        TCari.setName("TCari"); // NOI18N
        TCari.setPreferredSize(new java.awt.Dimension(195, 23));
        TCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TCariKeyPressed(evt);
            }
        });
        panelGlass8.add(TCari);

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
        panelGlass8.add(BtnCari);

        jLabel19.setText("Tgl.Asuhan :");
        jLabel19.setName("jLabel19"); // NOI18N
        jLabel19.setPreferredSize(new java.awt.Dimension(70, 23));
        panelGlass8.add(jLabel19);

        DTPCari1.setForeground(new java.awt.Color(50, 70, 50));
        DTPCari1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "21-01-2025" }));
        DTPCari1.setDisplayFormat("dd-MM-yyyy");
        DTPCari1.setName("DTPCari1"); // NOI18N
        DTPCari1.setOpaque(false);
        DTPCari1.setPreferredSize(new java.awt.Dimension(90, 23));
        panelGlass8.add(DTPCari1);

        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel21.setText("s.d.");
        jLabel21.setName("jLabel21"); // NOI18N
        jLabel21.setPreferredSize(new java.awt.Dimension(23, 23));
        panelGlass8.add(jLabel21);

        DTPCari2.setForeground(new java.awt.Color(50, 70, 50));
        DTPCari2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "21-01-2025" }));
        DTPCari2.setDisplayFormat("dd-MM-yyyy");
        DTPCari2.setName("DTPCari2"); // NOI18N
        DTPCari2.setOpaque(false);
        DTPCari2.setPreferredSize(new java.awt.Dimension(90, 23));
        panelGlass8.add(DTPCari2);

        jLabel7.setText("Record :");
        jLabel7.setName("jLabel7"); // NOI18N
        jLabel7.setPreferredSize(new java.awt.Dimension(60, 23));
        panelGlass8.add(jLabel7);

        LCount.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        LCount.setText("0");
        LCount.setName("LCount"); // NOI18N
        LCount.setPreferredSize(new java.awt.Dimension(70, 23));
        panelGlass8.add(LCount);

        internalFrame1.add(panelGlass8, java.awt.BorderLayout.PAGE_END);

        TabRawat.setBackground(new java.awt.Color(254, 255, 254));
        TabRawat.setForeground(new java.awt.Color(50, 50, 50));
        TabRawat.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        TabRawat.setName("TabRawat"); // NOI18N

        internalFrame2.setBorder(null);
        internalFrame2.setName("internalFrame2"); // NOI18N
        internalFrame2.setLayout(new java.awt.BorderLayout(1, 1));

        scrollInput.setName("scrollInput"); // NOI18N
        scrollInput.setPreferredSize(new java.awt.Dimension(102, 557));

        FormInput.setBackground(new java.awt.Color(255, 255, 255));
        FormInput.setBorder(null);
        FormInput.setName("FormInput"); // NOI18N
        FormInput.setPreferredSize(new java.awt.Dimension(870, 1193));
        FormInput.setLayout(null);

        label14.setText("Perawat:");
        label14.setName("label14"); // NOI18N
        label14.setPreferredSize(new java.awt.Dimension(70, 23));
        FormInput.add(label14);
        label14.setBounds(0, 40, 70, 23);

        KdPetugas.setEditable(false);
        KdPetugas.setName("KdPetugas"); // NOI18N
        KdPetugas.setPreferredSize(new java.awt.Dimension(80, 23));
        KdPetugas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KdPetugasKeyPressed(evt);
            }
        });
        FormInput.add(KdPetugas);
        KdPetugas.setBounds(74, 40, 100, 23);

        NmPetugas.setEditable(false);
        NmPetugas.setName("NmPetugas"); // NOI18N
        NmPetugas.setPreferredSize(new java.awt.Dimension(207, 23));
        FormInput.add(NmPetugas);
        NmPetugas.setBounds(176, 40, 180, 23);

        BtnDokter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnDokter.setMnemonic('2');
        BtnDokter.setToolTipText("Alt+2");
        BtnDokter.setName("BtnDokter"); // NOI18N
        BtnDokter.setPreferredSize(new java.awt.Dimension(28, 23));
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
        BtnDokter.setBounds(358, 40, 28, 23);

        EfekNyeriKet.setFocusTraversalPolicyProvider(true);
        EfekNyeriKet.setName("EfekNyeriKet"); // NOI18N
        EfekNyeriKet.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                EfekNyeriKetKeyPressed(evt);
            }
        });
        FormInput.add(EfekNyeriKet);
        EfekNyeriKet.setBounds(590, 390, 270, 23);

        DiagnosaMedis.setFocusTraversalPolicyProvider(true);
        DiagnosaMedis.setName("DiagnosaMedis"); // NOI18N
        DiagnosaMedis.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DiagnosaMedisKeyPressed(evt);
            }
        });
        FormInput.add(DiagnosaMedis);
        DiagnosaMedis.setBounds(90, 70, 280, 23);

        jLabel15.setText("Diagnosa Medis:");
        jLabel15.setName("jLabel15"); // NOI18N
        FormInput.add(jLabel15);
        jLabel15.setBounds(0, 70, 90, 23);

        FaktorPemberatKet.setFocusTraversalPolicyProvider(true);
        FaktorPemberatKet.setName("FaktorPemberatKet"); // NOI18N
        FaktorPemberatKet.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                FaktorPemberatKetKeyPressed(evt);
            }
        });
        FormInput.add(FaktorPemberatKet);
        FaktorPemberatKet.setBounds(640, 360, 220, 23);

        jLabel18.setText("Tipe Dializer:");
        jLabel18.setName("jLabel18"); // NOI18N
        FormInput.add(jLabel18);
        jLabel18.setBounds(520, 70, 80, 23);

        Dializer.setFocusTraversalPolicyProvider(true);
        Dializer.setName("Dializer"); // NOI18N
        Dializer.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DializerKeyPressed(evt);
            }
        });
        FormInput.add(Dializer);
        Dializer.setBounds(610, 70, 250, 23);

        KualitasNyeriKet.setFocusTraversalPolicyProvider(true);
        KualitasNyeriKet.setName("KualitasNyeriKet"); // NOI18N
        KualitasNyeriKet.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KualitasNyeriKetKeyPressed(evt);
            }
        });
        FormInput.add(KualitasNyeriKet);
        KualitasNyeriKet.setBounds(210, 360, 200, 23);

        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel25.setText("x/menit");
        jLabel25.setName("jLabel25"); // NOI18N
        FormInput.add(jLabel25);
        jLabel25.setBounds(2260, 100, 50, 23);

        FaktoPeringanKet.setFocusTraversalPolicyProvider(true);
        FaktoPeringanKet.setName("FaktoPeringanKet"); // NOI18N
        FaktoPeringanKet.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                FaktoPeringanKetKeyPressed(evt);
            }
        });
        FormInput.add(FaktoPeringanKet);
        FaktoPeringanKet.setBounds(210, 390, 200, 23);

        jLabel14.setText("Terapi HD Ke:");
        jLabel14.setName("jLabel14"); // NOI18N
        FormInput.add(jLabel14);
        jLabel14.setBounds(370, 70, 80, 23);

        TerapiKe.setFocusTraversalPolicyProvider(true);
        TerapiKe.setName("TerapiKe"); // NOI18N
        TerapiKe.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TerapiKeKeyPressed(evt);
            }
        });
        FormInput.add(TerapiKe);
        TerapiKe.setBounds(460, 70, 60, 23);

        jLabel36.setText("Informasi didapat dari :");
        jLabel36.setName("jLabel36"); // NOI18N
        FormInput.add(jLabel36);
        jLabel36.setBounds(592, 40, 130, 23);

        jLabel37.setText("Riwayat Alergi :");
        jLabel37.setName("jLabel37"); // NOI18N
        FormInput.add(jLabel37);
        jLabel37.setBounds(-80, 100, 170, 23);

        Alergi.setFocusTraversalPolicyProvider(true);
        Alergi.setName("Alergi"); // NOI18N
        Alergi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                AlergiKeyPressed(evt);
            }
        });
        FormInput.add(Alergi);
        Alergi.setBounds(90, 100, 280, 23);

        jLabel43.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel43.setText("Kg");
        jLabel43.setName("jLabel43"); // NOI18N
        FormInput.add(jLabel43);
        jLabel43.setBounds(580, 510, 20, 23);

        jLabel50.setText("Pasien merasa nyeri:");
        jLabel50.setName("jLabel50"); // NOI18N
        FormInput.add(jLabel50);
        jLabel50.setBounds(380, 100, 110, 23);

        jLabel52.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel52.setText("INTERVENSI RISIKO JATUH");
        jLabel52.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel52.setName("jLabel52"); // NOI18N
        FormInput.add(jLabel52);
        jLabel52.setBounds(10, 700, 380, 23);

        Informasi.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Autoanamnesis", "Alloanamnesis" }));
        Informasi.setName("Informasi"); // NOI18N
        Informasi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                InformasiKeyPressed(evt);
            }
        });
        FormInput.add(Informasi);
        Informasi.setBounds(726, 40, 128, 23);

        jLabel30.setText("Lokasi Nyeri:");
        jLabel30.setName("jLabel30"); // NOI18N
        FormInput.add(jLabel30);
        jLabel30.setBounds(10, 330, 70, 20);

        jLabel31.setText("Sejak Kapan:");
        jLabel31.setName("jLabel31"); // NOI18N
        FormInput.add(jLabel31);
        jLabel31.setBounds(250, 330, 70, 23);

        IntensitasNyeri.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak nyeri (0)", "Nyeri ringan (1-3)", "Nyeri sedang (4-6)", "Nyeri berat (7-9)", "Nyeri sangat berat (10)" }));
        IntensitasNyeri.setName("IntensitasNyeri"); // NOI18N
        IntensitasNyeri.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                IntensitasNyeriKeyPressed(evt);
            }
        });
        FormInput.add(IntensitasNyeri);
        IntensitasNyeri.setBounds(710, 100, 150, 23);

        LokasiNyeri.setFocusTraversalPolicyProvider(true);
        LokasiNyeri.setName("LokasiNyeri"); // NOI18N
        LokasiNyeri.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                LokasiNyeriKeyPressed(evt);
            }
        });
        FormInput.add(LokasiNyeri);
        LokasiNyeri.setBounds(90, 330, 160, 23);

        jLabel54.setText("Faktor Peringan:");
        jLabel54.setName("jLabel54"); // NOI18N
        FormInput.add(jLabel54);
        jLabel54.setBounds(0, 390, 90, 23);

        NyeriPasien.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        NyeriPasien.setName("NyeriPasien"); // NOI18N
        NyeriPasien.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                NyeriPasienKeyPressed(evt);
            }
        });
        FormInput.add(NyeriPasien);
        NyeriPasien.setBounds(500, 100, 90, 23);

        Kapan.setFocusTraversalPolicyProvider(true);
        Kapan.setName("Kapan"); // NOI18N
        Kapan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KapanKeyPressed(evt);
            }
        });
        FormInput.add(Kapan);
        Kapan.setBounds(330, 330, 180, 23);

        jLabel55.setText("Intensitas Nyeri:");
        jLabel55.setName("jLabel55"); // NOI18N
        FormInput.add(jLabel55);
        jLabel55.setBounds(600, 100, 100, 23);

        FaktorPemberat.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Cahaya", "Gerakan", "Berbaring" }));
        FaktorPemberat.setName("FaktorPemberat"); // NOI18N
        FaktorPemberat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                FaktorPemberatKeyPressed(evt);
            }
        });
        FormInput.add(FaktorPemberat);
        FaktorPemberat.setBounds(520, 360, 110, 23);

        jLabel57.setText("Faktor Pemberat:");
        jLabel57.setName("jLabel57"); // NOI18N
        FormInput.add(jLabel57);
        jLabel57.setBounds(420, 360, 90, 23);

        SifatNyeri.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Terus menerus", "Hilang timbul", "Lainnya" }));
        SifatNyeri.setName("SifatNyeri"); // NOI18N
        SifatNyeri.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SifatNyeriKeyPressed(evt);
            }
        });
        FormInput.add(SifatNyeri);
        SifatNyeri.setBounds(610, 330, 110, 23);

        KuKet.setFocusTraversalPolicyProvider(true);
        KuKet.setName("KuKet"); // NOI18N
        KuKet.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KuKetKeyPressed(evt);
            }
        });
        FormInput.add(KuKet);
        KuKet.setBounds(180, 450, 140, 23);

        jLabel58.setText("Nadi:");
        jLabel58.setName("jLabel58"); // NOI18N
        FormInput.add(jLabel58);
        jLabel58.setBounds(530, 450, 40, 23);

        FaktorPeringan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Makan", "Sunyi", "Dingin", "Panas" }));
        FaktorPeringan.setName("FaktorPeringan"); // NOI18N
        FaktorPeringan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                FaktorPeringanKeyPressed(evt);
            }
        });
        FormInput.add(FaktorPeringan);
        FaktorPeringan.setBounds(90, 390, 110, 23);

        jLabel59.setText("mmHg");
        jLabel59.setName("jLabel59"); // NOI18N
        FormInput.add(jLabel59);
        jLabel59.setBounds(480, 450, 40, 23);

        EfekNyeri.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Mual Muntah", "Tidur", "Nafsu makan", "Aktivitas" }));
        EfekNyeri.setName("EfekNyeri"); // NOI18N
        EfekNyeri.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                EfekNyeriKeyPressed(evt);
            }
        });
        FormInput.add(EfekNyeri);
        EfekNyeri.setBounds(470, 390, 110, 23);

        RespirasiKet.setFocusTraversalPolicyProvider(true);
        RespirasiKet.setName("RespirasiKet"); // NOI18N
        RespirasiKet.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                RespirasiKetKeyPressed(evt);
            }
        });
        FormInput.add(RespirasiKet);
        RespirasiKet.setBounds(300, 480, 50, 23);

        jLabel60.setText("Efek Nyeri:");
        jLabel60.setName("jLabel60"); // NOI18N
        FormInput.add(jLabel60);
        jLabel60.setBounds(410, 390, 60, 23);

        Respirasi.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Edema Paru/Ronchi", "Kusmaul", "Dispneu" }));
        Respirasi.setName("Respirasi"); // NOI18N
        Respirasi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                RespirasiKeyPressed(evt);
            }
        });
        FormInput.add(Respirasi);
        Respirasi.setBounds(90, 480, 130, 23);

        jLabel61.setText("Respirasi:");
        jLabel61.setName("jLabel61"); // NOI18N
        FormInput.add(jLabel61);
        jLabel61.setBounds(10, 480, 71, 23);

        Nadi.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Regular", "Irregular" }));
        Nadi.setName("Nadi"); // NOI18N
        Nadi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                NadiKeyPressed(evt);
            }
        });
        FormInput.add(Nadi);
        Nadi.setBounds(580, 450, 110, 23);

        JalurKet.setFocusTraversalPolicyProvider(true);
        JalurKet.setName("JalurKet"); // NOI18N
        JalurKet.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JalurKetKeyPressed(evt);
            }
        });
        FormInput.add(JalurKet);
        JalurKet.setBounds(380, 540, 260, 23);

        jLabel65.setText("Keadaan Umum:");
        jLabel65.setName("jLabel65"); // NOI18N
        FormInput.add(jLabel65);
        jLabel65.setBounds(10, 450, 80, 23);

        jLabel66.setText("Konjungctiva:");
        jLabel66.setName("jLabel66"); // NOI18N
        FormInput.add(jLabel66);
        jLabel66.setBounds(390, 480, 80, 23);

        Jalur.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Subclavicula", "Jugular", "Femoral", "Lainnya" }));
        Jalur.setName("Jalur"); // NOI18N
        Jalur.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JalurKeyPressed(evt);
            }
        });
        FormInput.add(Jalur);
        Jalur.setBounds(260, 540, 110, 23);

        Ku.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Baik", "Sedang", "Buruk", "Lain-lain" }));
        Ku.setName("Ku"); // NOI18N
        Ku.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KuKeyPressed(evt);
            }
        });
        FormInput.add(Ku);
        Ku.setBounds(90, 450, 80, 23);

        Konjungtiva.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Anemis", "Tidak anemis", "Dispneu", "Lain-lain" }));
        Konjungtiva.setName("Konjungtiva"); // NOI18N
        Konjungtiva.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KonjungtivaKeyPressed(evt);
            }
        });
        FormInput.add(Konjungtiva);
        Konjungtiva.setBounds(470, 480, 100, 23);

        AksesVaskular.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "AV fistula", "HD kateter", " " }));
        AksesVaskular.setName("AksesVaskular"); // NOI18N
        AksesVaskular.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                AksesVaskularKeyPressed(evt);
            }
        });
        FormInput.add(AksesVaskular);
        AksesVaskular.setBounds(90, 540, 160, 23);

        jLabel68.setText("Akses Vaskular:");
        jLabel68.setName("jLabel68"); // NOI18N
        FormInput.add(jLabel68);
        jLabel68.setBounds(2, 540, 80, 23);

        BJM1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        BJM1.setName("BJM1"); // NOI18N
        BJM1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                BJM1ItemStateChanged(evt);
            }
        });
        BJM1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BJM1KeyPressed(evt);
            }
        });
        FormInput.add(BJM1);
        BJM1.setBounds(770, 610, 120, 23);

        BbKering.setFocusTraversalPolicyProvider(true);
        BbKering.setName("BbKering"); // NOI18N
        BbKering.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BbKeringKeyPressed(evt);
            }
        });
        FormInput.add(BbKering);
        BbKering.setBounds(366, 510, 60, 23);

        jLabel70.setText("Ekstremitas:");
        jLabel70.setName("jLabel70"); // NOI18N
        FormInput.add(jLabel70);
        jLabel70.setBounds(20, 510, 70, 23);

        ATS1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Tidak Yakin", "Ya, 1-5 Kg", "Ya, 6-10 Kg", "Ya, 11-15 Kg", "Ya, >15 Kg" }));
        ATS1.setName("ATS1"); // NOI18N
        ATS1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ATS1ItemStateChanged(evt);
            }
        });
        ATS1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ATS1KeyPressed(evt);
            }
        });
        FormInput.add(ATS1);
        ATS1.setBounds(290, 610, 100, 23);

        Ektremitas.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak Edema/ Tidak Dehidarasi", "Dehidrasi", "Edeme anasarka", "Pucat dan dingin" }));
        Ektremitas.setName("Ektremitas"); // NOI18N
        Ektremitas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                EktremitasKeyPressed(evt);
            }
        });
        FormInput.add(Ektremitas);
        Ektremitas.setBounds(90, 510, 200, 23);

        jLabel73.setText("Kg");
        jLabel73.setName("jLabel73"); // NOI18N
        FormInput.add(jLabel73);
        jLabel73.setBounds(430, 510, 20, 23);

        jLabel75.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel75.setText("BB HD yang lalu:");
        jLabel75.setName("jLabel75"); // NOI18N
        FormInput.add(jLabel75);
        jLabel75.setBounds(600, 510, 90, 23);

        IntervensiJatuh.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak ada tindakan", "Edukasi", "Pasang stiker kuning dan edukasi" }));
        IntervensiJatuh.setName("IntervensiJatuh"); // NOI18N
        IntervensiJatuh.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                IntervensiJatuhKeyPressed(evt);
            }
        });
        FormInput.add(IntervensiJatuh);
        IntervensiJatuh.setBounds(40, 720, 340, 23);

        BbPreHD.setFocusTraversalPolicyProvider(true);
        BbPreHD.setName("BbPreHD"); // NOI18N
        BbPreHD.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BbPreHDKeyPressed(evt);
            }
        });
        FormInput.add(BbPreHD);
        BbPreHD.setBounds(510, 510, 70, 23);

        jLabel83.setText("BB Pre HD:");
        jLabel83.setName("jLabel83"); // NOI18N
        FormInput.add(jLabel83);
        jLabel83.setBounds(450, 510, 60, 23);

        MSA1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        MSA1.setName("MSA1"); // NOI18N
        MSA1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                MSA1KeyPressed(evt);
            }
        });
        FormInput.add(MSA1);
        MSA1.setBounds(580, 640, 80, 23);

        jLabel84.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel84.setText("Kg");
        jLabel84.setName("jLabel84"); // NOI18N
        FormInput.add(jLabel84);
        jLabel84.setBounds(910, 510, 20, 23);

        jLabel87.setText("BB Post HD:");
        jLabel87.setName("jLabel87"); // NOI18N
        FormInput.add(jLabel87);
        jLabel87.setBounds(770, 510, 60, 23);

        BbLalu.setEditable(false);
        BbLalu.setFocusTraversalPolicyProvider(true);
        BbLalu.setName("BbLalu"); // NOI18N
        BbLalu.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BbLaluKeyPressed(evt);
            }
        });
        FormInput.add(BbLalu);
        BbLalu.setBounds(690, 510, 80, 23);

        SifatNyeriKet.setFocusTraversalPolicyProvider(true);
        SifatNyeriKet.setName("SifatNyeriKet"); // NOI18N
        SifatNyeriKet.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SifatNyeriKetKeyPressed(evt);
            }
        });
        FormInput.add(SifatNyeriKet);
        SifatNyeriKet.setBounds(730, 330, 130, 23);

        jLabel51.setText("x/menit");
        jLabel51.setName("jLabel51"); // NOI18N
        FormInput.add(jLabel51);
        jLabel51.setBounds(810, 450, 40, 23);

        KonjungtivaKet.setEditable(false);
        KonjungtivaKet.setFocusTraversalPolicyProvider(true);
        KonjungtivaKet.setName("KonjungtivaKet"); // NOI18N
        KonjungtivaKet.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KonjungtivaKetKeyPressed(evt);
            }
        });
        FormInput.add(KonjungtivaKet);
        KonjungtivaKet.setBounds(580, 480, 280, 23);

        jLabel62.setText("Sifat Nyeri:");
        jLabel62.setName("jLabel62"); // NOI18N
        FormInput.add(jLabel62);
        jLabel62.setBounds(520, 330, 80, 23);

        jLabel95.setText("Kualitas Nyeri:");
        jLabel95.setName("jLabel95"); // NOI18N
        FormInput.add(jLabel95);
        jLabel95.setBounds(10, 360, 80, 23);

        KualitasNyeri.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tumpul", "Tajam", "Tertekan", "Terbakar" }));
        KualitasNyeri.setName("KualitasNyeri"); // NOI18N
        KualitasNyeri.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KualitasNyeriKeyPressed(evt);
            }
        });
        FormInput.add(KualitasNyeri);
        KualitasNyeri.setBounds(90, 360, 110, 23);

        BbPostHD.setFocusTraversalPolicyProvider(true);
        BbPostHD.setName("BbPostHD"); // NOI18N
        BbPostHD.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BbPostHDKeyPressed(evt);
            }
        });
        FormInput.add(BbPostHD);
        BbPostHD.setBounds(840, 510, 60, 23);

        jLabel63.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel63.setText("PEMERIKSAAN FISIK");
        jLabel63.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel63.setName("jLabel63"); // NOI18N
        FormInput.add(jLabel63);
        jLabel63.setBounds(10, 430, 380, 23);

        jSeparator6.setBackground(new java.awt.Color(239, 244, 234));
        jSeparator6.setForeground(new java.awt.Color(239, 244, 234));
        jSeparator6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(239, 244, 234)));
        jSeparator6.setName("jSeparator6"); // NOI18N
        FormInput.add(jSeparator6);
        jSeparator6.setBounds(0, 421, 880, 3);

        jSeparator8.setBackground(new java.awt.Color(239, 244, 234));
        jSeparator8.setForeground(new java.awt.Color(239, 244, 234));
        jSeparator8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(239, 244, 234)));
        jSeparator8.setName("jSeparator8"); // NOI18N
        FormInput.add(jSeparator8);
        jSeparator8.setBounds(0, 751, 880, 3);

        PanelWall.setBackground(new java.awt.Color(29, 29, 29));
        PanelWall.setBackgroundImage(new javax.swing.ImageIcon(getClass().getResource("/picture/nyeri-kh.png"))); // NOI18N
        PanelWall.setBackgroundImageType(usu.widget.constan.BackgroundConstan.BACKGROUND_IMAGE_STRECT);
        PanelWall.setPreferredSize(new java.awt.Dimension(200, 200));
        PanelWall.setRound(false);
        PanelWall.setWarna(new java.awt.Color(110, 110, 110));
        PanelWall.setLayout(null);
        FormInput.add(PanelWall);
        PanelWall.setBounds(20, 130, 840, 180);

        jLabel71.setText("BB kering:");
        jLabel71.setName("jLabel71"); // NOI18N
        FormInput.add(jLabel71);
        jLabel71.setBounds(300, 510, 60, 23);

        NadiKet.setFocusTraversalPolicyProvider(true);
        NadiKet.setName("NadiKet"); // NOI18N
        FormInput.add(NadiKet);
        NadiKet.setBounds(750, 450, 60, 23);

        jLabel76.setText("Frekuensi:");
        jLabel76.setName("jLabel76"); // NOI18N
        FormInput.add(jLabel76);
        jLabel76.setBounds(690, 450, 60, 23);

        jLabel77.setText("Tekanan Darah:");
        jLabel77.setName("jLabel77"); // NOI18N
        FormInput.add(jLabel77);
        jLabel77.setBounds(320, 450, 82, 23);

        Td.setFocusTraversalPolicyProvider(true);
        Td.setName("Td"); // NOI18N
        FormInput.add(Td);
        Td.setBounds(410, 450, 70, 23);

        TabRencanaKeperawatan.setBackground(new java.awt.Color(255, 255, 254));
        TabRencanaKeperawatan.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        TabRencanaKeperawatan.setForeground(new java.awt.Color(50, 50, 50));
        TabRencanaKeperawatan.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        TabRencanaKeperawatan.setName("TabRencanaKeperawatan"); // NOI18N

        panelBiasa1.setName("panelBiasa1"); // NOI18N
        panelBiasa1.setLayout(new java.awt.BorderLayout());

        Scroll8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 253)));
        Scroll8.setName("Scroll8"); // NOI18N
        Scroll8.setOpaque(true);

        tbRencanaKeperawatan.setName("tbRencanaKeperawatan"); // NOI18N
        Scroll8.setViewportView(tbRencanaKeperawatan);

        panelBiasa1.add(Scroll8, java.awt.BorderLayout.CENTER);

        TabRencanaKeperawatan.addTab("Intervensi Keperawatan", panelBiasa1);

        scrollPane5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        scrollPane5.setName("scrollPane5"); // NOI18N

        IntervensiKolaborasi.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        IntervensiKolaborasi.setColumns(20);
        IntervensiKolaborasi.setRows(5);
        IntervensiKolaborasi.setName("IntervensiKolaborasi"); // NOI18N
        IntervensiKolaborasi.setOpaque(true);
        IntervensiKolaborasi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                IntervensiKolaborasiKeyPressed(evt);
            }
        });
        scrollPane5.setViewportView(IntervensiKolaborasi);

        TabRencanaKeperawatan.addTab("Intervensi Kolaborasi", scrollPane5);

        FormInput.add(TabRencanaKeperawatan);
        TabRencanaKeperawatan.setBounds(430, 760, 420, 143);

        jLabel78.setText("Frekuensi:");
        jLabel78.setName("jLabel78"); // NOI18N
        FormInput.add(jLabel78);
        jLabel78.setBounds(230, 480, 60, 23);

        jLabel53.setText("x/menit");
        jLabel53.setName("jLabel53"); // NOI18N
        FormInput.add(jLabel53);
        jLabel53.setBounds(350, 480, 40, 23);

        jLabel72.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel72.setText("PENILAIAN RESIKO JATUH (Get Up and Go Test)");
        jLabel72.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel72.setName("jLabel72"); // NOI18N
        FormInput.add(jLabel72);
        jLabel72.setBounds(10, 570, 380, 23);

        jLabel79.setText("a. Cara Berjalan :");
        jLabel79.setName("jLabel79"); // NOI18N
        FormInput.add(jLabel79);
        jLabel79.setBounds(0, 590, 126, 23);

        jLabel90.setText("1. Tidak seimbang / sempoyongan / limbung :");
        jLabel90.setName("jLabel90"); // NOI18N
        FormInput.add(jLabel90);
        jLabel90.setBounds(30, 610, 250, 23);

        jLabel91.setText("b. Menopang saat akan duduk, tampak memegang pinggiran kursi atau meja / benda lain sebagai penopang :");
        jLabel91.setName("jLabel91"); // NOI18N
        FormInput.add(jLabel91);
        jLabel91.setBounds(0, 640, 571, 23);

        jLabel93.setText("Hasil :");
        jLabel93.setName("jLabel93"); // NOI18N
        FormInput.add(jLabel93);
        jLabel93.setBounds(0, 670, 72, 23);

        HasilJatuh.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak beresiko (tidak ditemukan a dan b)", "Resiko rendah (ditemukan a/b)", "Resiko tinggi (ditemukan a dan b)" }));
        HasilJatuh.setName("HasilJatuh"); // NOI18N
        HasilJatuh.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                HasilJatuhKeyPressed(evt);
            }
        });
        FormInput.add(HasilJatuh);
        HasilJatuh.setBounds(80, 670, 293, 23);

        jLabel94.setText("2. Jalan dengan menggunakan alat bantu (kruk, tripot, kursi roda, orang lain) :");
        jLabel94.setName("jLabel94"); // NOI18N
        FormInput.add(jLabel94);
        jLabel94.setBounds(370, 610, 400, 23);

        label12.setText("Key Word :");
        label12.setName("label12"); // NOI18N
        label12.setPreferredSize(new java.awt.Dimension(60, 23));
        FormInput.add(label12);
        label12.setBounds(10, 950, 60, 23);

        TCariMasalah.setToolTipText("Alt+C");
        TCariMasalah.setName("TCariMasalah"); // NOI18N
        TCariMasalah.setPreferredSize(new java.awt.Dimension(140, 23));
        TCariMasalah.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TCariMasalahKeyPressed(evt);
            }
        });
        FormInput.add(TCariMasalah);
        TCariMasalah.setBounds(70, 950, 215, 23);

        BtnCariMasalah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/accept.png"))); // NOI18N
        BtnCariMasalah.setMnemonic('1');
        BtnCariMasalah.setToolTipText("Alt+1");
        BtnCariMasalah.setName("BtnCariMasalah"); // NOI18N
        BtnCariMasalah.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnCariMasalah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCariMasalahActionPerformed(evt);
            }
        });
        BtnCariMasalah.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnCariMasalahKeyPressed(evt);
            }
        });
        FormInput.add(BtnCariMasalah);
        BtnCariMasalah.setBounds(290, 950, 28, 23);

        BtnAllMasalah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/Search-16x16.png"))); // NOI18N
        BtnAllMasalah.setMnemonic('2');
        BtnAllMasalah.setToolTipText("2Alt+2");
        BtnAllMasalah.setName("BtnAllMasalah"); // NOI18N
        BtnAllMasalah.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnAllMasalah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnAllMasalahActionPerformed(evt);
            }
        });
        BtnAllMasalah.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnAllMasalahKeyPressed(evt);
            }
        });
        FormInput.add(BtnAllMasalah);
        BtnAllMasalah.setBounds(320, 950, 28, 23);

        BtnTambahMasalah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/plus_16.png"))); // NOI18N
        BtnTambahMasalah.setMnemonic('3');
        BtnTambahMasalah.setToolTipText("Alt+3");
        BtnTambahMasalah.setName("BtnTambahMasalah"); // NOI18N
        BtnTambahMasalah.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnTambahMasalah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnTambahMasalahActionPerformed(evt);
            }
        });
        FormInput.add(BtnTambahMasalah);
        BtnTambahMasalah.setBounds(350, 950, 28, 23);

        label13.setText("Key Word :");
        label13.setName("label13"); // NOI18N
        label13.setPreferredSize(new java.awt.Dimension(60, 23));
        FormInput.add(label13);
        label13.setBounds(430, 950, 60, 23);

        TCariRencana.setToolTipText("Alt+C");
        TCariRencana.setName("TCariRencana"); // NOI18N
        TCariRencana.setPreferredSize(new java.awt.Dimension(215, 23));
        TCariRencana.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TCariRencanaKeyPressed(evt);
            }
        });
        FormInput.add(TCariRencana);
        TCariRencana.setBounds(490, 950, 235, 23);

        BtnCariRencana.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/accept.png"))); // NOI18N
        BtnCariRencana.setMnemonic('1');
        BtnCariRencana.setToolTipText("Alt+1");
        BtnCariRencana.setName("BtnCariRencana"); // NOI18N
        BtnCariRencana.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnCariRencana.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCariRencanaActionPerformed(evt);
            }
        });
        BtnCariRencana.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnCariRencanaKeyPressed(evt);
            }
        });
        FormInput.add(BtnCariRencana);
        BtnCariRencana.setBounds(730, 950, 28, 23);

        BtnAllRencana.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/Search-16x16.png"))); // NOI18N
        BtnAllRencana.setMnemonic('2');
        BtnAllRencana.setToolTipText("2Alt+2");
        BtnAllRencana.setName("BtnAllRencana"); // NOI18N
        BtnAllRencana.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnAllRencana.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnAllRencanaActionPerformed(evt);
            }
        });
        BtnAllRencana.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnAllRencanaKeyPressed(evt);
            }
        });
        FormInput.add(BtnAllRencana);
        BtnAllRencana.setBounds(770, 950, 28, 23);

        BtnTambahRencana.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/plus_16.png"))); // NOI18N
        BtnTambahRencana.setMnemonic('3');
        BtnTambahRencana.setToolTipText("Alt+3");
        BtnTambahRencana.setName("BtnTambahRencana"); // NOI18N
        BtnTambahRencana.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnTambahRencana.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnTambahRencanaActionPerformed(evt);
            }
        });
        FormInput.add(BtnTambahRencana);
        BtnTambahRencana.setBounds(800, 950, 28, 23);

        Scroll6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 253)));
        Scroll6.setName("Scroll6"); // NOI18N
        Scroll6.setOpaque(true);

        tbMasalahKeperawatan.setName("tbMasalahKeperawatan"); // NOI18N
        tbMasalahKeperawatan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbMasalahKeperawatanMouseClicked(evt);
            }
        });
        tbMasalahKeperawatan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbMasalahKeperawatanKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tbMasalahKeperawatanKeyReleased(evt);
            }
        });
        Scroll6.setViewportView(tbMasalahKeperawatan);

        FormInput.add(Scroll6);
        Scroll6.setBounds(30, 770, 380, 143);

        scrollInput.setViewportView(FormInput);

        internalFrame2.add(scrollInput, java.awt.BorderLayout.CENTER);

        TabRawat.addTab("Input Penilaian Keperawatan", internalFrame2);

        internalFrame3.setBorder(null);
        internalFrame3.setName("internalFrame3"); // NOI18N
        internalFrame3.setLayout(new java.awt.BorderLayout(1, 1));

        Scroll.setName("Scroll"); // NOI18N
        Scroll.setOpaque(true);
        Scroll.setPreferredSize(new java.awt.Dimension(452, 200));

        tbObat.setToolTipText("Silahkan klik untuk memilih data yang mau diedit ataupun dihapus");
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

        internalFrame3.add(Scroll, java.awt.BorderLayout.CENTER);

        panelGlass9.setName("panelGlass9"); // NOI18N
        panelGlass9.setPreferredSize(new java.awt.Dimension(44, 44));
        panelGlass9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));
        internalFrame3.add(panelGlass9, java.awt.BorderLayout.PAGE_END);

        PanelAccor.setBackground(new java.awt.Color(255, 255, 255));
        PanelAccor.setName("PanelAccor"); // NOI18N
        PanelAccor.setPreferredSize(new java.awt.Dimension(470, 43));
        PanelAccor.setLayout(new java.awt.BorderLayout(1, 1));

        ChkAccor.setBackground(new java.awt.Color(255, 250, 250));
        ChkAccor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/kiri.png"))); // NOI18N
        ChkAccor.setSelected(true);
        ChkAccor.setFocusable(false);
        ChkAccor.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ChkAccor.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        ChkAccor.setName("ChkAccor"); // NOI18N
        ChkAccor.setPreferredSize(new java.awt.Dimension(15, 20));
        ChkAccor.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/kiri.png"))); // NOI18N
        ChkAccor.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/kanan.png"))); // NOI18N
        ChkAccor.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/kanan.png"))); // NOI18N
        ChkAccor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ChkAccorActionPerformed(evt);
            }
        });
        PanelAccor.add(ChkAccor, java.awt.BorderLayout.WEST);

        FormMenu.setBackground(new java.awt.Color(255, 255, 255));
        FormMenu.setBorder(null);
        FormMenu.setName("FormMenu"); // NOI18N
        FormMenu.setPreferredSize(new java.awt.Dimension(115, 43));
        FormMenu.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 4, 9));

        jLabel34.setText("Pasien :");
        jLabel34.setName("jLabel34"); // NOI18N
        jLabel34.setPreferredSize(new java.awt.Dimension(55, 23));
        FormMenu.add(jLabel34);

        TNoRM1.setEditable(false);
        TNoRM1.setHighlighter(null);
        TNoRM1.setName("TNoRM1"); // NOI18N
        TNoRM1.setPreferredSize(new java.awt.Dimension(100, 23));
        FormMenu.add(TNoRM1);

        TPasien1.setEditable(false);
        TPasien1.setBackground(new java.awt.Color(245, 250, 240));
        TPasien1.setHighlighter(null);
        TPasien1.setName("TPasien1"); // NOI18N
        TPasien1.setPreferredSize(new java.awt.Dimension(250, 23));
        FormMenu.add(TPasien1);

        BtnPrint1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/item (copy).png"))); // NOI18N
        BtnPrint1.setMnemonic('T');
        BtnPrint1.setToolTipText("Alt+T");
        BtnPrint1.setName("BtnPrint1"); // NOI18N
        BtnPrint1.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnPrint1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnPrint1ActionPerformed(evt);
            }
        });
        FormMenu.add(BtnPrint1);

        PanelAccor.add(FormMenu, java.awt.BorderLayout.NORTH);

        FormMasalahRencana.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 254)));
        FormMasalahRencana.setName("FormMasalahRencana"); // NOI18N
        FormMasalahRencana.setLayout(new java.awt.GridLayout(3, 0, 1, 1));

        Scroll7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 254)));
        Scroll7.setName("Scroll7"); // NOI18N
        Scroll7.setOpaque(true);

        tbMasalahDetail.setName("tbMasalahDetail"); // NOI18N
        Scroll7.setViewportView(tbMasalahDetail);

        FormMasalahRencana.add(Scroll7);

        Scroll9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 254)));
        Scroll9.setName("Scroll9"); // NOI18N
        Scroll9.setOpaque(true);

        tbRencanaDetail.setName("tbRencanaDetail"); // NOI18N
        Scroll9.setViewportView(tbRencanaDetail);

        FormMasalahRencana.add(Scroll9);

        scrollPane6.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 254)), "Rencana Keperawatan Lainnya :", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 50, 50))); // NOI18N
        scrollPane6.setName("scrollPane6"); // NOI18N

        DetailRencana.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1));
        DetailRencana.setColumns(20);
        DetailRencana.setRows(5);
        DetailRencana.setName("DetailRencana"); // NOI18N
        scrollPane6.setViewportView(DetailRencana);

        FormMasalahRencana.add(scrollPane6);

        PanelAccor.add(FormMasalahRencana, java.awt.BorderLayout.CENTER);

        internalFrame3.add(PanelAccor, java.awt.BorderLayout.EAST);

        TabRawat.addTab("Data Penilaian Keperawatan", internalFrame3);

        internalFrame4.setBorder(null);
        internalFrame4.setName("internalFrame4"); // NOI18N
        internalFrame4.setLayout(new java.awt.BorderLayout(1, 1));

        Scroll1.setName("Scroll1"); // NOI18N
        Scroll1.setOpaque(true);
        Scroll1.setPreferredSize(new java.awt.Dimension(452, 200));

        tbObat1.setToolTipText("Silahkan klik untuk memilih data yang mau diedit ataupun dihapus");
        tbObat1.setName("tbObat1"); // NOI18N
        tbObat1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbObat1MouseClicked(evt);
            }
        });
        tbObat1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbObat1KeyPressed(evt);
            }
        });
        Scroll1.setViewportView(tbObat1);

        internalFrame4.add(Scroll1, java.awt.BorderLayout.CENTER);

        PanelInput.setName("PanelInput"); // NOI18N
        PanelInput.setOpaque(false);
        PanelInput.setPreferredSize(new java.awt.Dimension(192, 150));
        PanelInput.setLayout(new java.awt.BorderLayout(1, 1));

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

        FormInput1.setBackground(new java.awt.Color(255, 255, 255));
        FormInput1.setBorder(null);
        FormInput1.setName("FormInput1"); // NOI18N
        FormInput1.setPreferredSize(new java.awt.Dimension(870, 200));
        FormInput1.setLayout(null);

        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel26.setText("x/menit");
        jLabel26.setName("jLabel26"); // NOI18N
        FormInput1.add(jLabel26);
        jLabel26.setBounds(2260, 100, 50, 23);

        jLabel69.setText("QD:");
        jLabel69.setName("jLabel69"); // NOI18N
        FormInput1.add(jLabel69);
        jLabel69.setBounds(450, 10, 30, 23);

        Dialisat.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Bikarbonat", "Konduktifity", "Temperatur" }));
        Dialisat.setName("Dialisat"); // NOI18N
        Dialisat.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                DialisatItemStateChanged(evt);
            }
        });
        Dialisat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DialisatKeyPressed(evt);
            }
        });
        FormInput1.add(Dialisat);
        Dialisat.setBounds(430, 60, 100, 23);

        Heparinisasi.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Dosis Sirkulasi", "Dosis awal", "Dosis maintenance continue", "Dosis maintanance intermitten", "LMWH", "Tanpa heparin, penyebab", "Program bilas NaCl 0,9%" }));
        Heparinisasi.setName("Heparinisasi"); // NOI18N
        Heparinisasi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                HeparinisasiKeyPressed(evt);
            }
        });
        FormInput1.add(Heparinisasi);
        Heparinisasi.setBounds(10, 60, 130, 23);

        Qb.setFocusTraversalPolicyProvider(true);
        Qb.setName("Qb"); // NOI18N
        Qb.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                QbKeyPressed(evt);
            }
        });
        FormInput1.add(Qb);
        Qb.setBounds(330, 10, 70, 23);

        jLabel81.setText("jam");
        jLabel81.setName("jLabel81"); // NOI18N
        FormInput1.add(jLabel81);
        jLabel81.setBounds(250, 10, 30, 23);

        jLabel82.setText("QB:");
        jLabel82.setName("jLabel82"); // NOI18N
        FormInput1.add(jLabel82);
        jLabel82.setBounds(290, 10, 30, 23);

        jLabel85.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel85.setText("Heparinisasi:");
        jLabel85.setName("jLabel85"); // NOI18N
        FormInput1.add(jLabel85);
        jLabel85.setBounds(10, 40, 70, 23);

        resepHd.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Inisiasi", "Akut", "Rutin", "Pre Op", "SLED" }));
        resepHd.setName("resepHd"); // NOI18N
        resepHd.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                resepHdKeyPressed(evt);
            }
        });
        FormInput1.add(resepHd);
        resepHd.setBounds(70, 10, 80, 23);

        jLabel86.setText("ml/mnt");
        jLabel86.setName("jLabel86"); // NOI18N
        FormInput1.add(jLabel86);
        jLabel86.setBounds(560, 10, 50, 23);

        jLabel88.setText("HD:");
        jLabel88.setName("jLabel88"); // NOI18N
        FormInput1.add(jLabel88);
        jLabel88.setBounds(160, 10, 30, 23);

        Qd.setFocusTraversalPolicyProvider(true);
        Qd.setName("Qd"); // NOI18N
        Qd.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                QdKeyPressed(evt);
            }
        });
        FormInput1.add(Qd);
        Qd.setBounds(490, 10, 70, 23);

        jLabel89.setText("UF Goal:");
        jLabel89.setName("jLabel89"); // NOI18N
        FormInput1.add(jLabel89);
        jLabel89.setBounds(610, 10, 60, 23);

        jLabel92.setText("ml/mnt");
        jLabel92.setName("jLabel92"); // NOI18N
        FormInput1.add(jLabel92);
        jLabel92.setBounds(410, 10, 40, 23);

        Hd.setFocusTraversalPolicyProvider(true);
        Hd.setName("Hd"); // NOI18N
        Hd.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                HdKeyPressed(evt);
            }
        });
        FormInput1.add(Hd);
        Hd.setBounds(200, 10, 50, 23);

        Uf.setFocusTraversalPolicyProvider(true);
        Uf.setName("Uf"); // NOI18N
        Uf.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                UfKeyPressed(evt);
            }
        });
        FormInput1.add(Uf);
        Uf.setBounds(670, 10, 70, 23);

        jLabel96.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel96.setText("Resep HD");
        jLabel96.setName("jLabel96"); // NOI18N
        FormInput1.add(jLabel96);
        jLabel96.setBounds(10, 10, 60, 23);

        jLabel97.setText("Dialisat:");
        jLabel97.setName("jLabel97"); // NOI18N
        FormInput1.add(jLabel97);
        jLabel97.setBounds(370, 60, 50, 23);

        Heparinisasi_Ket.setFocusTraversalPolicyProvider(true);
        Heparinisasi_Ket.setName("Heparinisasi_Ket"); // NOI18N
        Heparinisasi_Ket.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Heparinisasi_KetKeyPressed(evt);
            }
        });
        FormInput1.add(Heparinisasi_Ket);
        Heparinisasi_Ket.setBounds(150, 60, 210, 23);

        jLabel98.setText("Dokter:");
        jLabel98.setName("jLabel98"); // NOI18N
        FormInput1.add(jLabel98);
        jLabel98.setBounds(10, 90, 40, 23);

        KdDokter.setFocusTraversalPolicyProvider(true);
        KdDokter.setName("KdDokter"); // NOI18N
        KdDokter.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KdDokterKeyPressed(evt);
            }
        });
        FormInput1.add(KdDokter);
        KdDokter.setBounds(60, 90, 80, 23);

        NmDokter.setFocusTraversalPolicyProvider(true);
        NmDokter.setName("NmDokter"); // NOI18N
        NmDokter.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                NmDokterKeyPressed(evt);
            }
        });
        FormInput1.add(NmDokter);
        NmDokter.setBounds(150, 90, 210, 23);

        BtnDokter1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnDokter1.setMnemonic('2');
        BtnDokter1.setToolTipText("Alt+2");
        BtnDokter1.setName("BtnDokter1"); // NOI18N
        BtnDokter1.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnDokter1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnDokter1ActionPerformed(evt);
            }
        });
        BtnDokter1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnDokter1KeyPressed(evt);
            }
        });
        FormInput1.add(BtnDokter1);
        BtnDokter1.setBounds(360, 90, 28, 23);

        PanelInput.add(FormInput1, java.awt.BorderLayout.PAGE_START);

        internalFrame4.add(PanelInput, java.awt.BorderLayout.PAGE_START);

        TabRawat.addTab("Instruksi Medik", internalFrame4);

        internalFrame5.setBorder(null);
        internalFrame5.setName("internalFrame5"); // NOI18N
        internalFrame5.setLayout(new java.awt.BorderLayout(1, 1));

        Scroll2.setName("Scroll2"); // NOI18N
        Scroll2.setOpaque(true);
        Scroll2.setPreferredSize(new java.awt.Dimension(452, 200));

        tbObat2.setToolTipText("Silahkan klik untuk memilih data yang mau diedit ataupun dihapus");
        tbObat2.setName("tbObat2"); // NOI18N
        tbObat2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbObat2MouseClicked(evt);
            }
        });
        tbObat2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbObat2KeyPressed(evt);
            }
        });
        Scroll2.setViewportView(tbObat2);

        internalFrame5.add(Scroll2, java.awt.BorderLayout.CENTER);

        PanelInput1.setName("PanelInput1"); // NOI18N
        PanelInput1.setOpaque(false);
        PanelInput1.setPreferredSize(new java.awt.Dimension(192, 170));
        PanelInput1.setLayout(new java.awt.BorderLayout(1, 1));

        ChkInput1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/143.png"))); // NOI18N
        ChkInput1.setMnemonic('I');
        ChkInput1.setText(".: Input Data");
        ChkInput1.setToolTipText("Alt+I");
        ChkInput1.setBorderPainted(true);
        ChkInput1.setBorderPaintedFlat(true);
        ChkInput1.setFocusable(false);
        ChkInput1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ChkInput1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        ChkInput1.setName("ChkInput1"); // NOI18N
        ChkInput1.setPreferredSize(new java.awt.Dimension(192, 20));
        ChkInput1.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/143.png"))); // NOI18N
        ChkInput1.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/145.png"))); // NOI18N
        ChkInput1.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/145.png"))); // NOI18N
        ChkInput1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ChkInput1ActionPerformed(evt);
            }
        });
        PanelInput1.add(ChkInput1, java.awt.BorderLayout.PAGE_END);

        FormInput2.setBackground(new java.awt.Color(255, 255, 255));
        FormInput2.setBorder(null);
        FormInput2.setName("FormInput2"); // NOI18N
        FormInput2.setPreferredSize(new java.awt.Dimension(870, 1193));
        FormInput2.setLayout(null);

        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel27.setText("x/menit");
        jLabel27.setName("jLabel27"); // NOI18N
        FormInput2.add(jLabel27);
        jLabel27.setBounds(2260, 100, 50, 23);

        Qb2.setFocusTraversalPolicyProvider(true);
        Qb2.setName("Qb2"); // NOI18N
        Qb2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Qb2KeyPressed(evt);
            }
        });
        FormInput2.add(Qb2);
        Qb2.setBounds(180, 50, 70, 23);

        jLabel100.setText("QB:");
        jLabel100.setName("jLabel100"); // NOI18N
        FormInput2.add(jLabel100);
        jLabel100.setBounds(140, 50, 30, 23);

        jLabel102.setText("mmHg");
        jLabel102.setName("jLabel102"); // NOI18N
        FormInput2.add(jLabel102);
        jLabel102.setBounds(510, 50, 50, 23);

        Uf2.setFocusTraversalPolicyProvider(true);
        Uf2.setName("Uf2"); // NOI18N
        Uf2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Uf2KeyPressed(evt);
            }
        });
        FormInput2.add(Uf2);
        Uf2.setBounds(330, 50, 70, 23);

        jLabel104.setText("TD:");
        jLabel104.setName("jLabel104"); // NOI18N
        FormInput2.add(jLabel104);
        jLabel104.setBounds(410, 50, 30, 23);

        jLabel105.setText("UF Rate(ml):");
        jLabel105.setName("jLabel105"); // NOI18N
        FormInput2.add(jLabel105);
        jLabel105.setBounds(260, 50, 70, 23);

        Observasi.setFocusTraversalPolicyProvider(true);
        Observasi.setName("Observasi"); // NOI18N
        Observasi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ObservasiKeyPressed(evt);
            }
        });
        FormInput2.add(Observasi);
        Observasi.setBounds(80, 50, 60, 23);

        Td2.setFocusTraversalPolicyProvider(true);
        Td2.setName("Td2"); // NOI18N
        Td2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Td2KeyPressed(evt);
            }
        });
        FormInput2.add(Td2);
        Td2.setBounds(440, 50, 70, 23);

        jLabel108.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel108.setText("Observasi:");
        jLabel108.setName("jLabel108"); // NOI18N
        FormInput2.add(jLabel108);
        jLabel108.setBounds(20, 50, 60, 23);

        jLabel110.setText("Perawat:");
        jLabel110.setName("jLabel110"); // NOI18N
        FormInput2.add(jLabel110);
        jLabel110.setBounds(160, 110, 60, 23);

        NIP.setFocusTraversalPolicyProvider(true);
        NIP.setName("NIP"); // NOI18N
        NIP.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                NIPKeyPressed(evt);
            }
        });
        FormInput2.add(NIP);
        NIP.setBounds(230, 110, 80, 23);

        NamaPetugas.setFocusTraversalPolicyProvider(true);
        NamaPetugas.setName("NamaPetugas"); // NOI18N
        NamaPetugas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                NamaPetugasKeyPressed(evt);
            }
        });
        FormInput2.add(NamaPetugas);
        NamaPetugas.setBounds(320, 110, 210, 23);

        jLabel109.setText("Nadi:");
        jLabel109.setName("jLabel109"); // NOI18N
        FormInput2.add(jLabel109);
        jLabel109.setBounds(560, 50, 30, 23);

        Nadi2.setFocusTraversalPolicyProvider(true);
        Nadi2.setName("Nadi2"); // NOI18N
        Nadi2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Nadi2KeyPressed(evt);
            }
        });
        FormInput2.add(Nadi2);
        Nadi2.setBounds(590, 50, 70, 23);

        jLabel111.setText("x/mnt");
        jLabel111.setName("jLabel111"); // NOI18N
        FormInput2.add(jLabel111);
        jLabel111.setBounds(660, 50, 40, 23);

        jLabel112.setText("Suhu:");
        jLabel112.setName("jLabel112"); // NOI18N
        FormInput2.add(jLabel112);
        jLabel112.setBounds(30, 80, 30, 23);

        Suhu.setFocusTraversalPolicyProvider(true);
        Suhu.setName("Suhu"); // NOI18N
        Suhu.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SuhuKeyPressed(evt);
            }
        });
        FormInput2.add(Suhu);
        Suhu.setBounds(60, 80, 70, 23);

        jLabel113.setText("C");
        jLabel113.setName("jLabel113"); // NOI18N
        FormInput2.add(jLabel113);
        jLabel113.setBounds(140, 80, 20, 23);

        jLabel101.setText("Respirasi");
        jLabel101.setName("jLabel101"); // NOI18N
        FormInput2.add(jLabel101);
        jLabel101.setBounds(170, 80, 50, 23);

        Respirasi2.setFocusTraversalPolicyProvider(true);
        Respirasi2.setName("Respirasi2"); // NOI18N
        Respirasi2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Respirasi2KeyPressed(evt);
            }
        });
        FormInput2.add(Respirasi2);
        Respirasi2.setBounds(230, 80, 50, 23);

        jLabel114.setText("x/mnt");
        jLabel114.setName("jLabel114"); // NOI18N
        FormInput2.add(jLabel114);
        jLabel114.setBounds(280, 80, 30, 23);

        Vp.setFocusTraversalPolicyProvider(true);
        Vp.setName("Vp"); // NOI18N
        Vp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                VpKeyPressed(evt);
            }
        });
        FormInput2.add(Vp);
        Vp.setBounds(360, 80, 70, 23);

        jLabel115.setText("AP:");
        jLabel115.setName("jLabel115"); // NOI18N
        FormInput2.add(jLabel115);
        jLabel115.setBounds(430, 80, 30, 23);

        Ap.setFocusTraversalPolicyProvider(true);
        Ap.setName("Ap"); // NOI18N
        Ap.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ApKeyPressed(evt);
            }
        });
        FormInput2.add(Ap);
        Ap.setBounds(470, 80, 70, 23);

        Tmp.setFocusTraversalPolicyProvider(true);
        Tmp.setName("Tmp"); // NOI18N
        Tmp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TmpKeyPressed(evt);
            }
        });
        FormInput2.add(Tmp);
        Tmp.setBounds(600, 80, 70, 23);

        jLabel117.setText("TMP:");
        jLabel117.setName("jLabel117"); // NOI18N
        FormInput2.add(jLabel117);
        jLabel117.setBounds(560, 80, 30, 23);

        jLabel119.setText("Keterangan Lain:");
        jLabel119.setName("jLabel119"); // NOI18N
        FormInput2.add(jLabel119);
        jLabel119.setBounds(-20, 110, 90, 23);

        Ket_Lain.setFocusTraversalPolicyProvider(true);
        Ket_Lain.setName("Ket_Lain"); // NOI18N
        Ket_Lain.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Ket_LainKeyPressed(evt);
            }
        });
        FormInput2.add(Ket_Lain);
        Ket_Lain.setBounds(80, 110, 70, 23);

        jLabel121.setText("VP:");
        jLabel121.setName("jLabel121"); // NOI18N
        FormInput2.add(jLabel121);
        jLabel121.setBounds(330, 80, 30, 23);

        jLabel16.setText("Tanggal :");
        jLabel16.setName("jLabel16"); // NOI18N
        jLabel16.setVerifyInputWhenFocusTarget(false);
        FormInput2.add(jLabel16);
        jLabel16.setBounds(0, 10, 80, 23);

        Tanggal.setForeground(new java.awt.Color(50, 70, 50));
        Tanggal.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "21-01-2025" }));
        Tanggal.setDisplayFormat("dd-MM-yyyy");
        Tanggal.setName("Tanggal"); // NOI18N
        Tanggal.setOpaque(false);
        Tanggal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TanggalKeyPressed(evt);
            }
        });
        FormInput2.add(Tanggal);
        Tanggal.setBounds(80, 10, 90, 23);

        Jam.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23" }));
        Jam.setName("Jam"); // NOI18N
        Jam.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JamKeyPressed(evt);
            }
        });
        FormInput2.add(Jam);
        Jam.setBounds(170, 10, 62, 23);

        Menit.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));
        Menit.setName("Menit"); // NOI18N
        Menit.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                MenitKeyPressed(evt);
            }
        });
        FormInput2.add(Menit);
        Menit.setBounds(240, 10, 62, 23);

        Detik.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));
        Detik.setName("Detik"); // NOI18N
        Detik.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DetikKeyPressed(evt);
            }
        });
        FormInput2.add(Detik);
        Detik.setBounds(300, 10, 62, 23);

        ChkKejadian.setBorder(null);
        ChkKejadian.setSelected(true);
        ChkKejadian.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        ChkKejadian.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ChkKejadian.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        ChkKejadian.setName("ChkKejadian"); // NOI18N
        FormInput2.add(ChkKejadian);
        ChkKejadian.setBounds(370, 10, 23, 23);

        btnPetugas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnPetugas.setMnemonic('2');
        btnPetugas.setToolTipText("ALt+2");
        btnPetugas.setName("btnPetugas"); // NOI18N
        btnPetugas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPetugasActionPerformed(evt);
            }
        });
        btnPetugas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnPetugasKeyPressed(evt);
            }
        });
        FormInput2.add(btnPetugas);
        btnPetugas.setBounds(530, 110, 28, 23);

        PanelInput1.add(FormInput2, java.awt.BorderLayout.PAGE_START);

        internalFrame5.add(PanelInput1, java.awt.BorderLayout.PAGE_START);

        TabRawat.addTab("Observasi Keperawatan", internalFrame5);

        internalFrame6.setBorder(null);
        internalFrame6.setName("internalFrame6"); // NOI18N
        internalFrame6.setLayout(new java.awt.BorderLayout(1, 1));

        Scroll3.setName("Scroll3"); // NOI18N
        Scroll3.setOpaque(true);
        Scroll3.setPreferredSize(new java.awt.Dimension(452, 200));

        tbObat3.setToolTipText("Silahkan klik untuk memilih data yang mau diedit ataupun dihapus");
        tbObat3.setName("tbObat3"); // NOI18N
        tbObat3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbObat3MouseClicked(evt);
            }
        });
        tbObat3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbObat3KeyPressed(evt);
            }
        });
        Scroll3.setViewportView(tbObat3);

        internalFrame6.add(Scroll3, java.awt.BorderLayout.CENTER);

        PanelInput2.setName("PanelInput2"); // NOI18N
        PanelInput2.setOpaque(false);
        PanelInput2.setPreferredSize(new java.awt.Dimension(192, 320));
        PanelInput2.setLayout(new java.awt.BorderLayout(1, 1));

        ChkInput2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/143.png"))); // NOI18N
        ChkInput2.setMnemonic('I');
        ChkInput2.setText(".: Input Data");
        ChkInput2.setToolTipText("Alt+I");
        ChkInput2.setBorderPainted(true);
        ChkInput2.setBorderPaintedFlat(true);
        ChkInput2.setFocusable(false);
        ChkInput2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ChkInput2.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        ChkInput2.setName("ChkInput2"); // NOI18N
        ChkInput2.setPreferredSize(new java.awt.Dimension(192, 20));
        ChkInput2.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/143.png"))); // NOI18N
        ChkInput2.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/145.png"))); // NOI18N
        ChkInput2.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/145.png"))); // NOI18N
        ChkInput2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ChkInput2ActionPerformed(evt);
            }
        });
        PanelInput2.add(ChkInput2, java.awt.BorderLayout.PAGE_END);

        FormInput3.setBackground(new java.awt.Color(255, 255, 255));
        FormInput3.setBorder(null);
        FormInput3.setName("FormInput3"); // NOI18N
        FormInput3.setPreferredSize(new java.awt.Dimension(870, 1193));
        FormInput3.setLayout(null);

        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel28.setText("x/menit");
        jLabel28.setName("jLabel28"); // NOI18N
        FormInput3.add(jLabel28);
        jLabel28.setBounds(2260, 100, 50, 23);

        jLabel138.setText("Evaluasi:");
        jLabel138.setName("jLabel138"); // NOI18N
        FormInput3.add(jLabel138);
        jLabel138.setBounds(0, 240, 80, 23);

        Evaluasi.setFocusTraversalPolicyProvider(true);
        Evaluasi.setName("Evaluasi"); // NOI18N
        Evaluasi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                EvaluasiKeyPressed(evt);
            }
        });
        FormInput3.add(Evaluasi);
        Evaluasi.setBounds(90, 240, 470, 23);

        jLabel80.setText("PENYULIT");
        jLabel80.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel80.setName("jLabel80"); // NOI18N
        FormInput3.add(jLabel80);
        jLabel80.setBounds(20, 10, 80, 23);

        jLabel116.setText("Perawat:");
        jLabel116.setName("jLabel116"); // NOI18N
        FormInput3.add(jLabel116);
        jLabel116.setBounds(20, 270, 60, 23);

        NIP1.setFocusTraversalPolicyProvider(true);
        NIP1.setName("NIP1"); // NOI18N
        NIP1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                NIP1KeyPressed(evt);
            }
        });
        FormInput3.add(NIP1);
        NIP1.setBounds(90, 270, 80, 23);

        NamaPetugas1.setFocusTraversalPolicyProvider(true);
        NamaPetugas1.setName("NamaPetugas1"); // NOI18N
        NamaPetugas1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                NamaPetugas1KeyPressed(evt);
            }
        });
        FormInput3.add(NamaPetugas1);
        NamaPetugas1.setBounds(180, 270, 210, 23);

        btnPetugas1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnPetugas1.setMnemonic('2');
        btnPetugas1.setToolTipText("ALt+2");
        btnPetugas1.setName("btnPetugas1"); // NOI18N
        btnPetugas1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPetugas1ActionPerformed(evt);
            }
        });
        btnPetugas1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnPetugas1KeyPressed(evt);
            }
        });
        FormInput3.add(btnPetugas1);
        btnPetugas1.setBounds(390, 270, 28, 23);

        Scroll10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 253)));
        Scroll10.setName("Scroll10"); // NOI18N
        Scroll10.setOpaque(true);

        tbPenyulit.setName("tbPenyulit"); // NOI18N
        tbPenyulit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbPenyulitMouseClicked(evt);
            }
        });
        tbPenyulit.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbPenyulitKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tbPenyulitKeyReleased(evt);
            }
        });
        Scroll10.setViewportView(tbPenyulit);

        FormInput3.add(Scroll10);
        Scroll10.setBounds(40, 40, 400, 143);

        label15.setText("Key Word :");
        label15.setName("label15"); // NOI18N
        label15.setPreferredSize(new java.awt.Dimension(60, 23));
        FormInput3.add(label15);
        label15.setBounds(30, 210, 60, 23);

        TCariPenyulit.setToolTipText("Alt+C");
        TCariPenyulit.setName("TCariPenyulit"); // NOI18N
        TCariPenyulit.setPreferredSize(new java.awt.Dimension(140, 23));
        TCariPenyulit.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TCariPenyulitKeyPressed(evt);
            }
        });
        FormInput3.add(TCariPenyulit);
        TCariPenyulit.setBounds(90, 210, 215, 23);

        BtnCariMasalah1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/accept.png"))); // NOI18N
        BtnCariMasalah1.setMnemonic('1');
        BtnCariMasalah1.setToolTipText("Alt+1");
        BtnCariMasalah1.setName("BtnCariMasalah1"); // NOI18N
        BtnCariMasalah1.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnCariMasalah1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCariMasalah1ActionPerformed(evt);
            }
        });
        BtnCariMasalah1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnCariMasalah1KeyPressed(evt);
            }
        });
        FormInput3.add(BtnCariMasalah1);
        BtnCariMasalah1.setBounds(310, 210, 28, 23);

        BtnAllMasalah1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/Search-16x16.png"))); // NOI18N
        BtnAllMasalah1.setMnemonic('2');
        BtnAllMasalah1.setToolTipText("2Alt+2");
        BtnAllMasalah1.setName("BtnAllMasalah1"); // NOI18N
        BtnAllMasalah1.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnAllMasalah1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnAllMasalah1ActionPerformed(evt);
            }
        });
        BtnAllMasalah1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnAllMasalah1KeyPressed(evt);
            }
        });
        FormInput3.add(BtnAllMasalah1);
        BtnAllMasalah1.setBounds(340, 210, 28, 23);

        BtnTambahMasalah1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/plus_16.png"))); // NOI18N
        BtnTambahMasalah1.setMnemonic('3');
        BtnTambahMasalah1.setToolTipText("Alt+3");
        BtnTambahMasalah1.setName("BtnTambahMasalah1"); // NOI18N
        BtnTambahMasalah1.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnTambahMasalah1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnTambahMasalah1ActionPerformed(evt);
            }
        });
        FormInput3.add(BtnTambahMasalah1);
        BtnTambahMasalah1.setBounds(370, 210, 28, 23);

        PanelInput2.add(FormInput3, java.awt.BorderLayout.PAGE_START);

        internalFrame6.add(PanelInput2, java.awt.BorderLayout.PAGE_START);

        TabRawat.addTab("Penyulit dan Evaluasi", internalFrame6);

        internalFrame1.add(TabRawat, java.awt.BorderLayout.CENTER);

        panelGlass10.setName("panelGlass10"); // NOI18N
        panelGlass10.setPreferredSize(new java.awt.Dimension(44, 54));
        panelGlass10.setLayout(null);

        jLabel10.setText("No.Rawat :");
        jLabel10.setName("jLabel10"); // NOI18N
        panelGlass10.add(jLabel10);
        jLabel10.setBounds(10, 10, 54, 14);

        TNoRw.setHighlighter(null);
        TNoRw.setName("TNoRw"); // NOI18N
        TNoRw.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TNoRwKeyPressed(evt);
            }
        });
        panelGlass10.add(TNoRw);
        TNoRw.setBounds(70, 10, 50, 24);

        TNoRM.setEditable(false);
        TNoRM.setHighlighter(null);
        TNoRM.setName("TNoRM"); // NOI18N
        panelGlass10.add(TNoRM);
        TNoRM.setBounds(130, 10, 64, 24);

        TPasien.setEditable(false);
        TPasien.setHighlighter(null);
        TPasien.setName("TPasien"); // NOI18N
        panelGlass10.add(TPasien);
        TPasien.setBounds(200, 10, 100, 24);

        jLabel8.setText("Tgl.Lahir :");
        jLabel8.setName("jLabel8"); // NOI18N
        panelGlass10.add(jLabel8);
        jLabel8.setBounds(310, 10, 49, 14);

        TglLahir.setEditable(false);
        TglLahir.setHighlighter(null);
        TglLahir.setName("TglLahir"); // NOI18N
        panelGlass10.add(TglLahir);
        TglLahir.setBounds(360, 10, 64, 24);

        jLabel11.setText("J.K. :");
        jLabel11.setName("jLabel11"); // NOI18N
        panelGlass10.add(jLabel11);
        jLabel11.setBounds(430, 10, 24, 14);

        Jk.setEditable(false);
        Jk.setHighlighter(null);
        Jk.setName("Jk"); // NOI18N
        panelGlass10.add(Jk);
        Jk.setBounds(470, 10, 64, 24);

        label11.setText("Tanggal :");
        label11.setName("label11"); // NOI18N
        label11.setPreferredSize(new java.awt.Dimension(70, 23));
        panelGlass10.add(label11);
        label11.setBounds(560, 10, 70, 23);

        TglAsuhan.setForeground(new java.awt.Color(50, 70, 50));
        TglAsuhan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "21-01-2025 07:08:20" }));
        TglAsuhan.setDisplayFormat("dd-MM-yyyy HH:mm:ss");
        TglAsuhan.setName("TglAsuhan"); // NOI18N
        TglAsuhan.setOpaque(false);
        TglAsuhan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TglAsuhanKeyPressed(evt);
            }
        });
        panelGlass10.add(TglAsuhan);
        TglAsuhan.setBounds(640, 10, 143, 24);

        internalFrame1.add(panelGlass10, java.awt.BorderLayout.PAGE_START);

        getContentPane().add(internalFrame1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void TNoRwKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TNoRwKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_PAGE_DOWN) {
            isRawat();
        } else {
            Valid.pindah(evt, TCari, BtnDokter);
        }
}//GEN-LAST:event_TNoRwKeyPressed

    private void BtnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSimpanActionPerformed
        switch (TabRawat.getSelectedIndex()) {
            case 0:
                if (TNoRM.getText().trim().equals("")) {
                    Valid.textKosong(TNoRw, "Nama Pasien");
                } else if (DiagnosaMedis.getText().trim().equals("")) {
                    Valid.textKosong(DiagnosaMedis, "Diagnosa Medis");
                } else if (KdPetugas.getText().trim().equals("")) {
                    Valid.textKosong(KdPetugas, "Nama Perawat");
                } else {
                    if (akses.getkode().equals("Admin Utama")) {
                        simpan();
                    } else {
                        if (TanggalRegistrasi.getText().equals("")) {
                            TanggalRegistrasi.setText(Sequel.cariIsi("select concat(reg_periksa.tgl_registrasi,' ',reg_periksa.jam_reg) from reg_periksa where reg_periksa.no_rawat=?", TNoRw.getText()));
                        }
                        if (Sequel.cekTanggalRegistrasi(TanggalRegistrasi.getText(), Valid.SetTgl(TglAsuhan.getSelectedItem() + "") + " " + TglAsuhan.getSelectedItem().toString().substring(11, 19)) == true) {
                            simpan();
                        }
                    }
                }
                break;
            case 2:
                if (TNoRM.getText().trim().equals("")) {
                    Valid.textKosong(TNoRw, "Nama Pasien");
                } else if (Hd.getText().trim().equals("")) {
                    Valid.textKosong(Hd, "HD");
                } else if (Qb.getText().trim().equals("")) {
                    Valid.textKosong(Qb, "QB");
                } else if (KdDokter.getText().trim().equals("")) {
                    Valid.textKosong(KdDokter, "Nama Dokter");
                } else {
                    if (akses.getkode().equals("Admin Utama")) {
                        simpan2();
                    } else {
                        if (TanggalRegistrasi.getText().equals("")) {
                            TanggalRegistrasi.setText(Sequel.cariIsi("select concat(reg_periksa.tgl_registrasi,' ',reg_periksa.jam_reg) from reg_periksa where reg_periksa.no_rawat=?", TNoRw.getText()));
                        }
                        if (Sequel.cekTanggalRegistrasi(TanggalRegistrasi.getText(), Valid.SetTgl(TglAsuhan.getSelectedItem() + "") + " " + TglAsuhan.getSelectedItem().toString().substring(11, 19)) == true) {
                            simpan2();
                        }
                    }
                }
                break;
            case 3:
                if (TNoRM.getText().trim().equals("")) {
                    Valid.textKosong(TNoRw, "Nama Pasien");
                } else if (Observasi.getText().trim().equals("")) {
                    Valid.textKosong(Observasi, "Observasi");
                } else if (Qb2.getText().trim().equals("")) {
                    Valid.textKosong(Qb2, "Qb2");
                } else if (NIP.getText().trim().equals("")) {
                    Valid.textKosong(NIP, "Nama Dokter");
                } else {
                    if (akses.getkode().equals("Admin Utama")) {
                        simpan3();
                    } else {
                        if (TanggalRegistrasi.getText().equals("")) {
                            TanggalRegistrasi.setText(Sequel.cariIsi("select concat(reg_periksa.tgl_registrasi,' ',reg_periksa.jam_reg) from reg_periksa where reg_periksa.no_rawat=?", TNoRw.getText()));
                        }
                        if (Sequel.cekTanggalRegistrasi(TanggalRegistrasi.getText(), Valid.SetTgl(TglAsuhan.getSelectedItem() + "") + " " + TglAsuhan.getSelectedItem().toString().substring(11, 19)) == true) {
                            simpan3();
                        }
                    }
                }
                break;
            case 4:
                if (TNoRM.getText().trim().equals("")) {
                    Valid.textKosong(TNoRw, "Nama Pasien");
                } else if (Evaluasi.getText().trim().equals("")) {
                    Valid.textKosong(Evaluasi, "Evaluasi");
                } else if (NIP1.getText().trim().equals("")) {
                    Valid.textKosong(NIP1, "Perawat");
                } else {
                    if (akses.getkode().equals("Admin Utama")) {
                        simpan4();
                    } else {
                        if (TanggalRegistrasi.getText().equals("")) {
                            TanggalRegistrasi.setText(Sequel.cariIsi("select concat(reg_periksa.tgl_registrasi,' ',reg_periksa.jam_reg) from reg_periksa where reg_periksa.no_rawat=?", TNoRw.getText()));
                        }
                        if (Sequel.cekTanggalRegistrasi(TanggalRegistrasi.getText(), Valid.SetTgl(TglAsuhan.getSelectedItem() + "") + " " + TglAsuhan.getSelectedItem().toString().substring(11, 19)) == true) {
                            simpan4();
                        }
                    }
                }
                break;
        }


}//GEN-LAST:event_BtnSimpanActionPerformed

    private void BtnSimpanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnSimpanKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_SPACE) {
            BtnSimpanActionPerformed(null);
        } else {
            Valid.pindah(evt, IntervensiKolaborasi, BtnBatal);
        }
}//GEN-LAST:event_BtnSimpanKeyPressed

    private void BtnBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnBatalActionPerformed
        emptTeks();
}//GEN-LAST:event_BtnBatalActionPerformed

    private void BtnBatalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnBatalKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_SPACE) {
            emptTeks();
        } else {
            Valid.pindah(evt, BtnSimpan, BtnHapus);
        }
}//GEN-LAST:event_BtnBatalKeyPressed

    private void BtnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnHapusActionPerformed
        switch (TabRawat.getSelectedIndex()) {
            case 0:
                if (tbObat.getSelectedRow() > -1) {
                    if (akses.getkode().equals("Admin Utama")) {
                        hapus();
                    } else {
                        if (KdPetugas.getText().equals(tbObat.getValueAt(tbObat.getSelectedRow(), 5).toString())) {
                            if (Sequel.cekTanggal48jam(tbObat.getValueAt(tbObat.getSelectedRow(), 7).toString(), Sequel.ambiltanggalsekarang()) == true) {
                                hapus();
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Hanya bisa dihapus oleh petugas yang bersangkutan..!!");
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(rootPane, "Silahkan anda pilih data terlebih dahulu..!!");
                }
                break;
            case 2:
                if (tbObat1.getSelectedRow() > -1) {
                    if (akses.getkode().equals("Admin Utama")) {
                        hapus();
                    } else {
                        if (KdDokter.getText().equals(tbObat1.getValueAt(tbObat1.getSelectedRow(), 13).toString())) {
                            if (Sequel.cekTanggal48jam(tbObat1.getValueAt(tbObat1.getSelectedRow(), 4).toString(), Sequel.ambiltanggalsekarang()) == true) {
                                hapus();
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Hanya bisa dihapus oleh petugas yang bersangkutan..!!");
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(rootPane, "Silahkan anda pilih data terlebih dahulu..!!");
                }
                break;
            case 3:
                if (tbObat2.getSelectedRow() > -1) {
                    if (akses.getkode().equals("Admin Utama")) {
                        hapus();
                    } else {
                        if (NIP.getText().equals(tbObat2.getValueAt(tbObat2.getSelectedRow(), 18).toString())) {
                            if (Sequel.cekTanggal48jam(tbObat2.getValueAt(tbObat2.getSelectedRow(), 5).toString(), Sequel.ambiltanggalsekarang()) == true) {
                                hapus();
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Hanya bisa dihapus oleh petugas yang bersangkutan..!!");
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(rootPane, "Silahkan anda pilih data terlebih dahulu..!!");
                }
                break;
            case 4:
                if (tbObat3.getSelectedRow() > -1) {
                    if (akses.getkode().equals("Admin Utama")) {
                        hapus();
                    } else {
                        if (KdDokter.getText().equals(tbObat3.getValueAt(tbObat3.getSelectedRow(), 7).toString())) {
                            if (Sequel.cekTanggal48jam(tbObat3.getValueAt(tbObat3.getSelectedRow(), 5).toString(), Sequel.ambiltanggalsekarang()) == true) {
                                hapus();
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Hanya bisa dihapus oleh petugas yang bersangkutan..!!");
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(rootPane, "Silahkan anda pilih data terlebih dahulu..!!");
                }
                break;
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
        switch (TabRawat.getSelectedIndex()) {
            case 0:
                if (TNoRM.getText().trim().equals("")) {
                    Valid.textKosong(TNoRw, "Nama Pasien");
                } else if (DiagnosaMedis.getText().trim().equals("")) {
                    Valid.textKosong(DiagnosaMedis, "Diagnosa Medis");
                } else if (KdPetugas.getText().trim().equals("")) {
                    Valid.textKosong(KdPetugas, "Nama Perawat");
                } else {
                    if (tbObat.getSelectedRow() > -1) {
                        if (akses.getkode().equals("Admin Utama")) {
                            ganti();
                        } else {
                            if (KdPetugas.getText().equals(tbObat.getValueAt(tbObat.getSelectedRow(), 63).toString())) {
                                if (Sequel.cekTanggal48jam(tbObat.getValueAt(tbObat.getSelectedRow(), 8).toString(), Sequel.ambiltanggalsekarang()) == true) {
                                    if (TanggalRegistrasi.getText().equals("")) {
                                        TanggalRegistrasi.setText(Sequel.cariIsi("select concat(reg_periksa.tgl_registrasi,' ',reg_periksa.jam_reg) from reg_periksa where reg_periksa.no_rawat=?", TNoRw.getText()));
                                    }
                                    if (Sequel.cekTanggalRegistrasi(TanggalRegistrasi.getText(), Valid.SetTgl(TglAsuhan.getSelectedItem() + "") + " " + TglAsuhan.getSelectedItem().toString().substring(11, 19)) == true) {
                                        ganti();
                                    }
                                }
                            } else {
                                JOptionPane.showMessageDialog(null, "Hanya bisa diganti oleh petugas yang bersangkutan..!!");
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(rootPane, "Silahkan anda pilih data terlebih dahulu..!!");
                    }
                }
                break;
            case 2:
                if (TNoRM.getText().trim().equals("")) {
                    Valid.textKosong(TNoRw, "Nama Pasien");
                } else if (Hd.getText().trim().equals("")) {
                    Valid.textKosong(Hd, "HD");
                } else if (Qb.getText().trim().equals("")) {
                    Valid.textKosong(Qb, "QB");
                } else if (KdDokter.getText().trim().equals("")) {
                    Valid.textKosong(KdDokter, "Nama Dokter");
                } else {
                    if (tbObat1.getSelectedRow() > -1) {
                        if (akses.getkode().equals("Admin Utama")) {
                            ganti();
                        } else {
                            if (KdDokter.getText().equals(tbObat1.getValueAt(tbObat1.getSelectedRow(), 14).toString())) {
                                if (Sequel.cekTanggal48jam(tbObat1.getValueAt(tbObat1.getSelectedRow(), 5).toString(), Sequel.ambiltanggalsekarang()) == true) {
                                    if (TanggalRegistrasi.getText().equals("")) {
                                        TanggalRegistrasi.setText(Sequel.cariIsi("select concat(reg_periksa.tgl_registrasi,' ',reg_periksa.jam_reg) from reg_periksa where reg_periksa.no_rawat=?", TNoRw.getText()));
                                    }
                                    if (Sequel.cekTanggalRegistrasi(TanggalRegistrasi.getText(), Valid.SetTgl(TglAsuhan.getSelectedItem() + "") + " " + TglAsuhan.getSelectedItem().toString().substring(11, 19)) == true) {
                                        ganti();
                                    }
                                }
                            } else {
                                JOptionPane.showMessageDialog(null, "Hanya bisa diganti oleh petugas yang bersangkutan..!!");
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(rootPane, "Silahkan anda pilih data terlebih dahulu..!!");
                    }
                }
                break;
            case 3:
                break;
            case 4:
                break;
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
            try {
                if (TCari.getText().equals("")) {
                    ps = koneksi.prepareStatement(
                            "select reg_periksa.no_rawat,pasien.no_rkm_medis,pasien.nm_pasien,if(pasien.jk='L','Laki-Laki','Perempuan') as jk,pasien.tgl_lahir,pasien.agama,bahasa_pasien.nama_bahasa,cacat_fisik.nama_cacat,asesmen_ulang_hemodialisa.tanggal,"
                            + "asesmen_ulang_hemodialisa.informasi,asesmen_ulang_hemodialisa.td,asesmen_ulang_hemodialisa.nadi,asesmen_ulang_hemodialisa.rr,asesmen_ulang_hemodialisa.suhu,asesmen_ulang_hemodialisa.bb,asesmen_ulang_hemodialisa.tb,"
                            + "asesmen_ulang_hemodialisa.nadi,asesmen_ulang_hemodialisa.rr,asesmen_ulang_hemodialisa.suhu,asesmen_ulang_hemodialisa.gcs,asesmen_ulang_hemodialisa.bb,asesmen_ulang_hemodialisa.tb,asesmen_ulang_hemodialisa.bmi,asesmen_ulang_hemodialisa.keluhan_utama,"
                            + "asesmen_ulang_hemodialisa.rpd,asesmen_ulang_hemodialisa.rpk,asesmen_ulang_hemodialisa.rpo,asesmen_ulang_hemodialisa.alergi,asesmen_ulang_hemodialisa.alat_bantu,asesmen_ulang_hemodialisa.ket_bantu,asesmen_ulang_hemodialisa.prothesa,"
                            + "asesmen_ulang_hemodialisa.ket_pro,asesmen_ulang_hemodialisa.adl,asesmen_ulang_hemodialisa.status_psiko,asesmen_ulang_hemodialisa.ket_psiko,asesmen_ulang_hemodialisa.hub_keluarga,asesmen_ulang_hemodialisa.tinggal_dengan,"
                            + "asesmen_ulang_hemodialisa.ket_tinggal,asesmen_ulang_hemodialisa.ekonomi,asesmen_ulang_hemodialisa.edukasi,asesmen_ulang_hemodialisa.ket_edukasi,asesmen_ulang_hemodialisa.berjalan_a,asesmen_ulang_hemodialisa.berjalan_b,"
                            + "asesmen_ulang_hemodialisa.berjalan_c,asesmen_ulang_hemodialisa.hasil,asesmen_ulang_hemodialisa.lapor,asesmen_ulang_hemodialisa.ket_lapor,asesmen_ulang_hemodialisa.sg1,asesmen_ulang_hemodialisa.nilai1,asesmen_ulang_hemodialisa.sg2,asesmen_ulang_hemodialisa.nilai2,"
                            + "asesmen_ulang_hemodialisa.total_hasil,asesmen_ulang_hemodialisa.nyeri,asesmen_ulang_hemodialisa.provokes,asesmen_ulang_hemodialisa.ket_provokes,asesmen_ulang_hemodialisa.quality,asesmen_ulang_hemodialisa.ket_quality,asesmen_ulang_hemodialisa.lokasi,asesmen_ulang_hemodialisa.menyebar,"
                            + "asesmen_ulang_hemodialisa.skala_nyeri,asesmen_ulang_hemodialisa.durasi,asesmen_ulang_hemodialisa.nyeri_hilang,asesmen_ulang_hemodialisa.ket_nyeri,asesmen_ulang_hemodialisa.pada_dokter,asesmen_ulang_hemodialisa.ket_dokter,asesmen_ulang_hemodialisa.rencana,"
                            + "asesmen_ulang_hemodialisa.nip,petugas.nama,asesmen_ulang_hemodialisa.budaya,asesmen_ulang_hemodialisa.ket_budaya "
                            + "from reg_periksa inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "
                            + "inner join asesmen_ulang_hemodialisa on reg_periksa.no_rawat=asesmen_ulang_hemodialisa.no_rawat "
                            + "inner join petugas on asesmen_ulang_hemodialisa.nip=petugas.nip "
                            + "inner join bahasa_pasien on bahasa_pasien.id=pasien.bahasa_pasien "
                            + "inner join cacat_fisik on cacat_fisik.id=pasien.cacat_fisik where "
                            + "asesmen_ulang_hemodialisa.tanggal between ? and ? order by asesmen_ulang_hemodialisa.tanggal");
                } else {
                    ps = koneksi.prepareStatement(
                            "select reg_periksa.no_rawat,pasien.no_rkm_medis,pasien.nm_pasien,if(pasien.jk='L','Laki-Laki','Perempuan') as jk,pasien.tgl_lahir,pasien.agama,bahasa_pasien.nama_bahasa,cacat_fisik.nama_cacat,asesmen_ulang_hemodialisa.tanggal,"
                            + "asesmen_ulang_hemodialisa.informasi,asesmen_ulang_hemodialisa.td,asesmen_ulang_hemodialisa.nadi,asesmen_ulang_hemodialisa.rr,asesmen_ulang_hemodialisa.suhu,asesmen_ulang_hemodialisa.bb,asesmen_ulang_hemodialisa.tb,"
                            + "asesmen_ulang_hemodialisa.nadi,asesmen_ulang_hemodialisa.rr,asesmen_ulang_hemodialisa.suhu,asesmen_ulang_hemodialisa.gcs,asesmen_ulang_hemodialisa.bb,asesmen_ulang_hemodialisa.tb,asesmen_ulang_hemodialisa.bmi,asesmen_ulang_hemodialisa.keluhan_utama,"
                            + "asesmen_ulang_hemodialisa.rpd,asesmen_ulang_hemodialisa.rpk,asesmen_ulang_hemodialisa.rpo,asesmen_ulang_hemodialisa.alergi,asesmen_ulang_hemodialisa.alat_bantu,asesmen_ulang_hemodialisa.ket_bantu,asesmen_ulang_hemodialisa.prothesa,"
                            + "asesmen_ulang_hemodialisa.ket_pro,asesmen_ulang_hemodialisa.adl,asesmen_ulang_hemodialisa.status_psiko,asesmen_ulang_hemodialisa.ket_psiko,asesmen_ulang_hemodialisa.hub_keluarga,asesmen_ulang_hemodialisa.tinggal_dengan,"
                            + "asesmen_ulang_hemodialisa.ket_tinggal,asesmen_ulang_hemodialisa.ekonomi,asesmen_ulang_hemodialisa.edukasi,asesmen_ulang_hemodialisa.ket_edukasi,asesmen_ulang_hemodialisa.berjalan_a,asesmen_ulang_hemodialisa.berjalan_b,"
                            + "asesmen_ulang_hemodialisa.berjalan_c,asesmen_ulang_hemodialisa.hasil,asesmen_ulang_hemodialisa.lapor,asesmen_ulang_hemodialisa.ket_lapor,asesmen_ulang_hemodialisa.sg1,asesmen_ulang_hemodialisa.nilai1,asesmen_ulang_hemodialisa.sg2,asesmen_ulang_hemodialisa.nilai2,"
                            + "asesmen_ulang_hemodialisa.total_hasil,asesmen_ulang_hemodialisa.nyeri,asesmen_ulang_hemodialisa.provokes,asesmen_ulang_hemodialisa.ket_provokes,asesmen_ulang_hemodialisa.quality,asesmen_ulang_hemodialisa.ket_quality,asesmen_ulang_hemodialisa.lokasi,asesmen_ulang_hemodialisa.menyebar,"
                            + "asesmen_ulang_hemodialisa.skala_nyeri,asesmen_ulang_hemodialisa.durasi,asesmen_ulang_hemodialisa.nyeri_hilang,asesmen_ulang_hemodialisa.ket_nyeri,asesmen_ulang_hemodialisa.pada_dokter,asesmen_ulang_hemodialisa.ket_dokter,asesmen_ulang_hemodialisa.rencana,"
                            + "asesmen_ulang_hemodialisa.nip,petugas.nama,asesmen_ulang_hemodialisa.budaya,asesmen_ulang_hemodialisa.ket_budaya "
                            + "from reg_periksa inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "
                            + "inner join asesmen_ulang_hemodialisa on reg_periksa.no_rawat=asesmen_ulang_hemodialisa.no_rawat "
                            + "inner join petugas on asesmen_ulang_hemodialisa.nip=petugas.nip "
                            + "inner join bahasa_pasien on bahasa_pasien.id=pasien.bahasa_pasien "
                            + "inner join cacat_fisik on cacat_fisik.id=pasien.cacat_fisik where "
                            + "asesmen_ulang_hemodialisa.tanggal between ? and ? and reg_periksa.no_rawat like ? or "
                            + "asesmen_ulang_hemodialisa.tanggal between ? and ? and pasien.no_rkm_medis like ? or "
                            + "asesmen_ulang_hemodialisa.tanggal between ? and ? and pasien.nm_pasien like ? or "
                            + "asesmen_ulang_hemodialisa.tanggal between ? and ? and asesmen_ulang_hemodialisa.nip like ? or "
                            + "asesmen_ulang_hemodialisa.tanggal between ? and ? and petugas.nama like ? order by asesmen_ulang_hemodialisa.tanggal");
                }

                try {
                    if (TCari.getText().equals("")) {
                        ps.setString(1, Valid.SetTgl(DTPCari1.getSelectedItem() + "") + " 00:00:00");
                        ps.setString(2, Valid.SetTgl(DTPCari2.getSelectedItem() + "") + " 23:59:59");
                    } else {
                        ps.setString(1, Valid.SetTgl(DTPCari1.getSelectedItem() + "") + " 00:00:00");
                        ps.setString(2, Valid.SetTgl(DTPCari2.getSelectedItem() + "") + " 23:59:59");
                        ps.setString(3, "%" + TCari.getText() + "%");
                        ps.setString(4, Valid.SetTgl(DTPCari1.getSelectedItem() + "") + " 00:00:00");
                        ps.setString(5, Valid.SetTgl(DTPCari2.getSelectedItem() + "") + " 23:59:59");
                        ps.setString(6, "%" + TCari.getText() + "%");
                        ps.setString(7, Valid.SetTgl(DTPCari1.getSelectedItem() + "") + " 00:00:00");
                        ps.setString(8, Valid.SetTgl(DTPCari2.getSelectedItem() + "") + " 23:59:59");
                        ps.setString(9, "%" + TCari.getText() + "%");
                        ps.setString(10, Valid.SetTgl(DTPCari1.getSelectedItem() + "") + " 00:00:00");
                        ps.setString(11, Valid.SetTgl(DTPCari2.getSelectedItem() + "") + " 23:59:59");
                        ps.setString(12, "%" + TCari.getText() + "%");
                        ps.setString(13, Valid.SetTgl(DTPCari1.getSelectedItem() + "") + " 00:00:00");
                        ps.setString(14, Valid.SetTgl(DTPCari2.getSelectedItem() + "") + " 23:59:59");
                        ps.setString(15, "%" + TCari.getText() + "%");
                    }
                    rs = ps.executeQuery();
                    htmlContent = new StringBuilder();
                    htmlContent.append(
                            "<tr class='isi'>"
                            + "<td valign='middle' bgcolor='#FFFAFA' align='center' width='9%'><b>PASIEN & PETUGAS</b></td>"
                            + "<td valign='middle' bgcolor='#FFFAFA' align='center' width='5%'><b>I. KEADAAN UMUM</b></td>"
                            + "<td valign='middle' bgcolor='#FFFAFA' align='center' width='5%'><b>II. STATUS NUTRISI</b></td>"
                            + "<td valign='middle' bgcolor='#FFFAFA' align='center' width='13%'><b>III. RIWAYAT KESEHATAN</b></td>"
                            + "<td valign='middle' bgcolor='#FFFAFA' align='center' width='8%'><b>IV. FUNGSIONAL</b></td>"
                            + "<td valign='middle' bgcolor='#FFFAFA' align='center' width='16%'><b>V. RIWAYAT PSIKO-SOSIAL SPIRITUAL DAN BUDAYA</b></td>"
                            + "<td valign='middle' bgcolor='#FFFAFA' align='center' width='16%'><b>VI. PENILAIAN RESIKO JATUH</b></td>"
                            + "<td valign='middle' bgcolor='#FFFAFA' align='center' width='11%'><b>VII. SKRINING GIZI</b></td>"
                            + "<td valign='middle' bgcolor='#FFFAFA' align='center' width='11%'><b>VIII. PENILAIAN TINGKAT NYERI</b></td>"
                            + "<td valign='middle' bgcolor='#FFFAFA' align='center' width='6%'><b>MASALAH & RENCANA KEPERAWATAN</b></td>"
                            + "</tr>"
                    );
                    while (rs.next()) {
                        masalahkeperawatan = "";
                        ps2 = koneksi.prepareStatement(
                                "select master_masalah_keperawatan.kode_masalah,master_masalah_keperawatan.nama_masalah from master_masalah_keperawatan "
                                + "inner join asesmen_ulang_hemodialisa_masalah on asesmen_ulang_hemodialisa_masalah.kode_masalah=master_masalah_keperawatan.kode_masalah "
                                + "where asesmen_ulang_hemodialisa_masalah.no_rawat=? order by kode_masalah");
                        try {
                            ps2.setString(1, rs.getString("no_rawat"));
                            rs2 = ps2.executeQuery();
                            while (rs2.next()) {
                                masalahkeperawatan = rs2.getString("nama_masalah") + ", " + masalahkeperawatan;
                            }
                        } catch (Exception e) {
                            System.out.println("Notif : " + e);
                        } finally {
                            if (rs2 != null) {
                                rs2.close();
                            }
                            if (ps2 != null) {
                                ps2.close();
                            }
                        }
                        htmlContent.append(
                                "<tr class='isi'>"
                                + "<td valign='top' cellpadding='0' cellspacing='0'>"
                                + "<table width='100%' border='0' cellpadding='0' cellspacing='0'align='center'>"
                                + "<tr class='isi2'>"
                                + "<td width='32%' valign='top'>No.Rawat</td><td valign='top'>:&nbsp;</td><td width='67%' valign='top'>" + rs.getString("no_rawat") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='32%' valign='top'>No.R.M.</td><td valign='top'>:&nbsp;</td><td width='67%' valign='top'>" + rs.getString("no_rkm_medis") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='32%' valign='top'>Nama Pasien</td><td valign='top'>:&nbsp;</td><td width='67%' valign='top'>" + rs.getString("nm_pasien") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='32%' valign='top'>J.K.</td><td valign='top'>:&nbsp;</td><td width='67%' valign='top'>" + rs.getString("jk") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='32%' valign='top'>Agama</td><td valign='top'>:&nbsp;</td><td width='67%' valign='top'>" + rs.getString("agama") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='32%' valign='top'>Bahasa</td><td valign='top'>:&nbsp;</td><td width='67%' valign='top'>" + rs.getString("nama_bahasa") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='32%' valign='top'>Tgl.Lahir</td><td valign='top'>:&nbsp;</td><td width='67%' valign='top'>" + rs.getString("nama_cacat") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='32%' valign='top'>Cacat Fisik</td><td valign='top'>:&nbsp;</td><td width='67%' valign='top'>" + rs.getString("tgl_lahir") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='32%' valign='top'>Petugas</td><td valign='top'>:&nbsp;</td><td width='67%' valign='top'>" + rs.getString("nip") + " " + rs.getString("nama") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='32%' valign='top'>Tgl.Asuhan</td><td valign='top'>:&nbsp;</td><td width='67%' valign='top'>" + rs.getString("tanggal") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='32%' valign='top'>Informasi</td><td valign='top'>:&nbsp;</td><td width='67%' valign='top'>" + rs.getString("informasi") + "</td>"
                                + "</tr>"
                                + "</table>"
                                + "</td>"
                                + "<td valign='top' cellpadding='0' cellspacing='0'>"
                                + "<table width='100%' border='0' cellpadding='0' cellspacing='0'align='center'>"
                                + "<tr class='isi2'>"
                                + "<td width='34%' valign='top'>TD</td><td valign='top'>:&nbsp;</td><td width='65%' valign='top'>" + rs.getString("td") + "mmHg</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='34%' valign='top'>Nadi</td><td valign='top'>:&nbsp;</td><td width='65%' valign='top'>" + rs.getString("nadi") + "x/menit</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='34%' valign='top'>RR</td><td valign='top'>:&nbsp;</td><td width='65%' valign='top'>" + rs.getString("rr") + "x/menit</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='34%' valign='top'>Suhu</td><td valign='top'>:&nbsp;</td><td width='65%' valign='top'>" + rs.getString("suhu") + "°C</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='34%' valign='top'>GCS</td><td valign='top'>:&nbsp;</td><td width='65%' valign='top'>" + rs.getString("gcs") + "</td>"
                                + "</tr>"
                                + "</table>"
                                + "</td>"
                                + "<td valign='top' cellpadding='0' cellspacing='0'>"
                                + "<table width='100%' border='0' cellpadding='0' cellspacing='0'align='center'>"
                                + "<tr class='isi2'>"
                                + "<td width='34%' valign='top'>BB</td><td valign='top'>:&nbsp;</td><td width='65%' valign='top'>" + rs.getString("bb") + "Kg</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='34%' valign='top'>TB</td><td valign='top'>:&nbsp;</td><td width='65%' valign='top'>" + rs.getString("tb") + "cm</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='34%' valign='top'>BMI</td><td valign='top'>:&nbsp;</td><td width='65%' valign='top'>" + rs.getString("bmi") + "Kg/m²</td>"
                                + "</tr>"
                                + "</table>"
                                + "</td>"
                                + "<td valign='top' cellpadding='0' cellspacing='0'>"
                                + "<table width='100%' border='0' cellpadding='0' cellspacing='0'align='center'>"
                                + "<tr class='isi2'>"
                                + "<td width='32%' valign='top'>Keluhan Utama</td><td valign='top'>:&nbsp;</td><td width='67%' valign='top'>" + rs.getString("keluhan_utama") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='32%' valign='top'>RPD</td><td valign='top'>:&nbsp;</td><td width='67%' valign='top'>" + rs.getString("rpd") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='32%' valign='top'>RPK</td><td valign='top'>:&nbsp;</td><td width='67%' valign='top'>" + rs.getString("rpk") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='32%' valign='top'>RPO</td><td valign='top'>:&nbsp;</td><td width='67%' valign='top'>" + rs.getString("rpo") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='32%' valign='top'>Alergi</td><td valign='top'>:&nbsp;</td><td width='67%' valign='top'>" + rs.getString("alergi") + "</td>"
                                + "</tr>"
                                + "</table>"
                                + "</td>"
                                + "<td valign='top' cellpadding='0' cellspacing='0'>"
                                + "<table width='100%' border='0' cellpadding='0' cellspacing='0'align='center'>"
                                + "<tr class='isi2'>"
                                + "<td width='44%' valign='top'>Alat Bantu</td><td valign='top'>:&nbsp;</td><td width='55%' valign='top'>" + rs.getString("alat_bantu") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='44%' valign='top'>Ket. Alat Bantu</td><td valign='top'>:&nbsp;</td><td width='55%' valign='top'>" + rs.getString("ket_bantu") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='44%' valign='top'>Prothesa</td><td valign='top'>:&nbsp;</td><td width='55%' valign='top'>" + rs.getString("prothesa") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='44%' valign='top'>Ket. Prothesa</td><td valign='top'>:&nbsp;</td><td width='55%' valign='top'>" + rs.getString("ket_pro") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='44%' valign='top'>ADL</td><td valign='top'>:&nbsp;</td><td width='55%' valign='top'>" + rs.getString("adl") + "</td>"
                                + "</tr>"
                                + "</table>"
                                + "</td>"
                                + "<td valign='top' cellpadding='0' cellspacing='0'>"
                                + "<table width='100%' border='0' cellpadding='0' cellspacing='0'align='center'>"
                                + "<tr class='isi2'>"
                                + "<td width='64%' valign='top'>Status Psikologis</td><td valign='top'>:&nbsp;</td><td width='35%' valign='top'>" + rs.getString("status_psiko") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='64%' valign='top'>Ket. Psikologi</td><td valign='top'>:&nbsp;</td><td width='35%' valign='top'>" + rs.getString("ket_psiko") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='64%' valign='top'>Hubungan pasien dengan anggota keluarga</td><td valign='top'>:&nbsp;</td><td width='35%' valign='top'>" + rs.getString("hub_keluarga") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='64%' valign='top'>Tinggal dengan</td><td valign='top'>:&nbsp;</td><td width='35%' valign='top'>" + rs.getString("tinggal_dengan") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='64%' valign='top'>Ket. Tinggal</td><td valign='top'>:&nbsp;</td><td width='35%' valign='top'>" + rs.getString("ket_tinggal") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='64%' valign='top'>Ekonomi</td><td valign='top'>:&nbsp;</td><td width='35%' valign='top'>" + rs.getString("ekonomi") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='64%' valign='top'>Kepercayaan / Budaya / Nilai-nilai khusus yang perlu diperhatikan</td><td valign='top'>:&nbsp;</td><td width='35%' valign='top'>" + rs.getString("budaya") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='64%' valign='top'>Ket. Budaya</td><td valign='top'>:&nbsp;</td><td width='35%' valign='top'>" + rs.getString("ket_budaya") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='64%' valign='top'>Edukasi diberikan kepada </td><td valign='top'>:&nbsp;</td><td width='35%' valign='top'>" + rs.getString("edukasi") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='64%' valign='top'>Ket. Edukasi</td><td valign='top'>:&nbsp;</td><td width='35%' valign='top'>" + rs.getString("ket_edukasi") + "</td>"
                                + "</tr>"
                                + "</table>"
                                + "</td>"
                                + "<td valign='top' cellpadding='0' cellspacing='0'>"
                                + "<table width='100%' border='0' cellpadding='0' cellspacing='0'align='center'>"
                                + "<tr class='isi2'>"
                                + "<td width='64%' valign='top'>Tidak seimbang/sempoyongan/limbung</td><td valign='top'>:&nbsp;</td><td width='35%' valign='top'>" + rs.getString("berjalan_a") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='64%' valign='top'>Jalan dengan menggunakan alat bantu (kruk, tripot, kursi roda, orang lain)</td><td valign='top'>:&nbsp;</td><td width='35%' valign='top'>" + rs.getString("berjalan_b") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='64%' valign='top'>Menopang saat akan duduk, tampak memegang pinggiran kursi atau meja/benda lain sebagai penopang</td><td valign='top'>:&nbsp;</td><td width='35%' valign='top'>" + rs.getString("berjalan_c") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='64%' valign='top'>Hasil</td><td valign='top'>:&nbsp;</td><td width='35%' valign='top'>" + rs.getString("hasil") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='64%' valign='top'>Dilaporan ke dokter?</td><td valign='top'>:&nbsp;</td><td width='35%' valign='top'>" + rs.getString("lapor") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='64%' valign='top'>Jam Lapor</td><td valign='top'>:&nbsp;</td><td width='35%' valign='top'>" + rs.getString("ket_lapor") + "</td>"
                                + "</tr>"
                                + "</table>"
                                + "</td>"
                                + "<td valign='top' cellpadding='0' cellspacing='0'>"
                                + "<table width='100%' border='0' cellpadding='0' cellspacing='0'align='center'>"
                                + "<tr class='isi2'>"
                                + "<td width='64%' valign='top'>Apakah ada penurunan berat badanyang tidak diinginkan selama enam bulan terakhir?</td><td valign='top'>:&nbsp;</td><td width='35%' valign='top'>" + rs.getString("sg1") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='64%' valign='top'>Apakah nafsu makan berkurang karena tidak nafsu makan?</td><td valign='top'>:&nbsp;</td><td width='35%' valign='top'>" + rs.getString("sg2") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='64%' valign='top'>Nilai 1</td><td valign='top'>:&nbsp;</td><td width='35%' valign='top'>" + rs.getString("nilai1") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='64%' valign='top'>Nilai 2</td><td valign='top'>:&nbsp;</td><td width='35%' valign='top'>" + rs.getString("nilai2") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='64%' valign='top'>Total Skor</td><td valign='top'>:&nbsp;</td><td width='35%' valign='top'>" + rs.getString("total_hasil") + "</td>"
                                + "</tr>"
                                + "</table>"
                                + "</td>"
                                + "<td valign='top' cellpadding='0' cellspacing='0'>"
                                + "<table width='100%' border='0' cellpadding='0' cellspacing='0'align='center'>"
                                + "<tr class='isi2'>"
                                + "<td width='44%' valign='top'>Tingkat Nyeri</td><td valign='top'>:&nbsp;</td><td width='55%' valign='top'>" + rs.getString("nyeri") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='44%' valign='top'>Provokes</td><td valign='top'>:&nbsp;</td><td width='55%' valign='top'>" + rs.getString("provokes") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='44%' valign='top'>Ket. Provokes</td><td valign='top'>:&nbsp;</td><td width='55%' valign='top'>" + rs.getString("ket_provokes") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='44%' valign='top'>Kualitas</td><td valign='top'>:&nbsp;</td><td width='55%' valign='top'>" + rs.getString("quality") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='44%' valign='top'>Ket. Kualitas</td><td valign='top'>:&nbsp;</td><td width='55%' valign='top'>" + rs.getString("ket_quality") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='44%' valign='top'>Lokas</td><td valign='top'>:&nbsp;</td><td width='55%' valign='top'>" + rs.getString("lokasi") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='44%' valign='top'>Menyebar</td><td valign='top'>:&nbsp;</td><td width='55%' valign='top'>" + rs.getString("menyebar") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='44%' valign='top'>Skala Nyeri</td><td valign='top'>:&nbsp;</td><td width='55%' valign='top'>" + rs.getString("skala_nyeri") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='44%' valign='top'>Durasi</td><td valign='top'>:&nbsp;</td><td width='55%' valign='top'>" + rs.getString("durasi") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='44%' valign='top'>Nyeri Hilang</td><td valign='top'>:&nbsp;</td><td width='55%' valign='top'>" + rs.getString("nyeri_hilang") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='44%' valign='top'>Ket. Hilang Nyeri</td><td valign='top'>:&nbsp;</td><td width='55%' valign='top'>" + rs.getString("ket_nyeri") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='44%' valign='top'>Lapor Ke Dokter</td><td valign='top'>:&nbsp;</td><td width='55%' valign='top'>" + rs.getString("pada_dokter") + "</td>"
                                + "</tr>"
                                + "<tr class='isi2'>"
                                + "<td width='44%' valign='top'>Jam Lapor</td><td valign='top'>:&nbsp;</td><td width='55%' valign='top'>" + rs.getString("ket_dokter") + "</td>"
                                + "</tr>"
                                + "</table>"
                                + "</td>"
                                + "<td valign='top' cellpadding='0' cellspacing='0'>"
                                + "Masalah Keperawatan : " + masalahkeperawatan + "<br><br>"
                                + "Rencana Keperawatan : " + rs.getString("rencana")
                                + "</td>"
                                + "</tr>"
                        );
                    }
                    LoadHTML.setText(
                            "<html>"
                            + "<table width='1800px' border='0' align='center' cellpadding='1px' cellspacing='0' class='tbl_form'>"
                            + htmlContent.toString()
                            + "</table>"
                            + "</html>"
                    );

                    File g = new File("file2.css");
                    BufferedWriter bg = new BufferedWriter(new FileWriter(g));
                    bg.write(
                            ".isi td{border-right: 1px solid #e2e7dd;font: 8.5px tahoma;height:12px;border-bottom: 1px solid #e2e7dd;background: #ffffff;color:#323232;}"
                            + ".isi2 td{font: 8.5px tahoma;border:none;height:12px;background: #ffffff;color:#323232;}"
                            + ".isi3 td{border-right: 1px solid #e2e7dd;font: 8.5px tahoma;height:12px;border-top: 1px solid #e2e7dd;background: #ffffff;color:#323232;}"
                            + ".isi4 td{font: 11px tahoma;height:12px;border-top: 1px solid #e2e7dd;background: #ffffff;color:#323232;}"
                            + ".isi5 td{font: 8.5px tahoma;border:none;height:12px;background: #ffffff;color:#AA0000;}"
                            + ".isi6 td{font: 8.5px tahoma;border:none;height:12px;background: #ffffff;color:#FF0000;}"
                            + ".isi7 td{font: 8.5px tahoma;border:none;height:12px;background: #ffffff;color:#C8C800;}"
                            + ".isi8 td{font: 8.5px tahoma;border:none;height:12px;background: #ffffff;color:#00AA00;}"
                            + ".isi9 td{font: 8.5px tahoma;border:none;height:12px;background: #ffffff;color:#969696;}"
                    );
                    bg.close();

                    File f = new File("DataPenilaianAwalKeperawatanRalan.html");
                    BufferedWriter bw = new BufferedWriter(new FileWriter(f));
                    bw.write(LoadHTML.getText().replaceAll("<head>", "<head>"
                            + "<link href=\"file2.css\" rel=\"stylesheet\" type=\"text/css\" />"
                            + "<table width='1800px' border='0' align='center' cellpadding='3px' cellspacing='0' class='tbl_form'>"
                            + "<tr class='isi2'>"
                            + "<td valign='top' align='center'>"
                            + "<font size='4' face='Tahoma'>" + akses.getnamars() + "</font><br>"
                            + akses.getalamatrs() + ", " + akses.getkabupatenrs() + ", " + akses.getpropinsirs() + "<br>"
                            + akses.getkontakrs() + ", E-mail : " + akses.getemailrs() + "<br><br>"
                            + "<font size='2' face='Tahoma'>DATA PENILAIAN AWAL KEPERAWATAN RAWAT JALAN<br><br></font>"
                            + "</td>"
                            + "</tr>"
                            + "</table>")
                    );
                    bw.close();
                    Desktop.getDesktop().browse(f.toURI());
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
                System.out.println("Notifikasi : " + e);
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
        switch (TabRawat.getSelectedIndex()) {
            case 1:
                tampil();
                break;
            case 2:
                tampil2();
                break;
            case 3:
                tampil3();
                break;
            case 4:
                tampil4();
                break;
        }

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
            TCari.setText("");
            tampil();
        } else {
            Valid.pindah(evt, BtnCari, TPasien);
        }
}//GEN-LAST:event_BtnAllKeyPressed

    private void tbObatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbObatMouseClicked
        if (tabMode.getRowCount() != 0) {
            try {
                ChkAccor.setSelected(true);
                isMenu();
                getMasalah();
            } catch (java.lang.NullPointerException e) {
            }
            if ((evt.getClickCount() == 2) && (tbObat.getSelectedColumn() == 0)) {
                TabRawat.setSelectedIndex(0);
            }
        }
}//GEN-LAST:event_tbObatMouseClicked

    private void tbObatKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbObatKeyPressed
        if (tabMode.getRowCount() != 0) {
            if ((evt.getKeyCode() == KeyEvent.VK_ENTER) || (evt.getKeyCode() == KeyEvent.VK_UP) || (evt.getKeyCode() == KeyEvent.VK_DOWN)) {
                try {
                    ChkAccor.setSelected(true);
                    isMenu();
                    getMasalah();
                } catch (java.lang.NullPointerException e) {
                }
            } else if (evt.getKeyCode() == KeyEvent.VK_SPACE) {
                try {
                    getData();
                    TabRawat.setSelectedIndex(0);
                } catch (java.lang.NullPointerException e) {
                }
            }
        }
}//GEN-LAST:event_tbObatKeyPressed

    private void KdPetugasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KdPetugasKeyPressed

    }//GEN-LAST:event_KdPetugasKeyPressed

    private void BtnDokterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnDokterActionPerformed
        pilihan = 1;
        petugas.isCek();
        petugas.setSize(internalFrame1.getWidth() - 20, internalFrame1.getHeight() - 20);
        petugas.setLocationRelativeTo(internalFrame1);
        petugas.setAlwaysOnTop(false);
        petugas.setVisible(true);
    }//GEN-LAST:event_BtnDokterActionPerformed

    private void BtnDokterKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnDokterKeyPressed
        Valid.pindah(evt, IntervensiKolaborasi, Informasi);
    }//GEN-LAST:event_BtnDokterKeyPressed

    private void EfekNyeriKetKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_EfekNyeriKetKeyPressed
        Valid.pindah(evt, SifatNyeriKet, DiagnosaMedis);
    }//GEN-LAST:event_EfekNyeriKetKeyPressed

    private void DiagnosaMedisKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DiagnosaMedisKeyPressed
        Valid.pindah(evt, EfekNyeriKet, TerapiKe);
    }//GEN-LAST:event_DiagnosaMedisKeyPressed

    private void FaktorPemberatKetKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_FaktorPemberatKetKeyPressed
        Valid.pindah(evt, KualitasNyeriKet, FaktoPeringanKet);
    }//GEN-LAST:event_FaktorPemberatKetKeyPressed

    private void DializerKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DializerKeyPressed
        Valid.pindah(evt, FaktoPeringanKet, SifatNyeriKet);
    }//GEN-LAST:event_DializerKeyPressed

    private void KualitasNyeriKetKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KualitasNyeriKetKeyPressed
        Valid.pindah(evt, Informasi, FaktorPemberatKet);
    }//GEN-LAST:event_KualitasNyeriKetKeyPressed

    private void FaktoPeringanKetKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_FaktoPeringanKetKeyPressed
        Valid.pindah(evt, FaktorPemberatKet, Dializer);
    }//GEN-LAST:event_FaktoPeringanKetKeyPressed

    private void TerapiKeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TerapiKeKeyPressed
        Valid.pindah(evt, DiagnosaMedis, Evaluasi);
    }//GEN-LAST:event_TerapiKeKeyPressed

    private void AlergiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_AlergiKeyPressed
        Valid.pindah(evt, NmDokter, IntensitasNyeri);
    }//GEN-LAST:event_AlergiKeyPressed

    private void InformasiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_InformasiKeyPressed
        Valid.pindah(evt, TglAsuhan, KualitasNyeriKet);
    }//GEN-LAST:event_InformasiKeyPressed

    private void IntensitasNyeriKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_IntensitasNyeriKeyPressed
        Valid.pindah(evt, Alergi, LokasiNyeri);
    }//GEN-LAST:event_IntensitasNyeriKeyPressed

    private void LokasiNyeriKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_LokasiNyeriKeyPressed
        Valid.pindah(evt, IntensitasNyeri, NyeriPasien);
    }//GEN-LAST:event_LokasiNyeriKeyPressed

    private void NyeriPasienKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_NyeriPasienKeyPressed
        Valid.pindah(evt, LokasiNyeri, Kapan);
    }//GEN-LAST:event_NyeriPasienKeyPressed

    private void KapanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KapanKeyPressed
        Valid.pindah(evt, NyeriPasien, FaktorPemberat);
    }//GEN-LAST:event_KapanKeyPressed

    private void FaktorPemberatKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_FaktorPemberatKeyPressed
        Valid.pindah(evt, Kapan, SifatNyeri);
    }//GEN-LAST:event_FaktorPemberatKeyPressed

    private void SifatNyeriKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SifatNyeriKeyPressed
        Valid.pindah(evt, FaktorPemberat, KuKet);
    }//GEN-LAST:event_SifatNyeriKeyPressed

    private void KuKetKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KuKetKeyPressed
        Valid.pindah(evt, SifatNyeri, FaktorPeringan);
    }//GEN-LAST:event_KuKetKeyPressed

    private void FaktorPeringanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_FaktorPeringanKeyPressed
        Valid.pindah(evt, KuKet, EfekNyeri);
    }//GEN-LAST:event_FaktorPeringanKeyPressed

    private void EfekNyeriKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_EfekNyeriKeyPressed
        Valid.pindah(evt, FaktorPeringan, RespirasiKet);
    }//GEN-LAST:event_EfekNyeriKeyPressed

    private void RespirasiKetKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_RespirasiKetKeyPressed
        Valid.pindah(evt, EfekNyeri, Respirasi);
    }//GEN-LAST:event_RespirasiKetKeyPressed

    private void RespirasiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_RespirasiKeyPressed
        Valid.pindah(evt, RespirasiKet, KualitasNyeri);
    }//GEN-LAST:event_RespirasiKeyPressed

    private void NadiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_NadiKeyPressed
        Valid.pindah(evt, BbPostHD, JalurKet);
    }//GEN-LAST:event_NadiKeyPressed

    private void JalurKetKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_JalurKetKeyPressed
        Valid.pindah(evt, Nadi, Ku);
    }//GEN-LAST:event_JalurKetKeyPressed

    private void JalurKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_JalurKeyPressed
        Valid.pindah(evt, AksesVaskular, BbKering);
    }//GEN-LAST:event_JalurKeyPressed

    private void KuKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KuKeyPressed
        Valid.pindah(evt, JalurKet, Konjungtiva);
    }//GEN-LAST:event_KuKeyPressed

    private void KonjungtivaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KonjungtivaKeyPressed
        Valid.pindah(evt, Ku, Ektremitas);
    }//GEN-LAST:event_KonjungtivaKeyPressed

    private void AksesVaskularKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_AksesVaskularKeyPressed
        Valid.pindah(evt, Ektremitas, Jalur);
    }//GEN-LAST:event_AksesVaskularKeyPressed

    private void BJM1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BJM1KeyPressed
        Valid.pindah(evt, Dialisat, Detik);
    }//GEN-LAST:event_BJM1KeyPressed

    private void BbKeringKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BbKeringKeyPressed
        Valid.pindah(evt, Jalur, ATS1);
    }//GEN-LAST:event_BbKeringKeyPressed

    private void ATS1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ATS1KeyPressed
        Valid.pindah(evt, BbKering, Dialisat);
    }//GEN-LAST:event_ATS1KeyPressed

    private void DialisatKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DialisatKeyPressed
        Valid.pindah(evt, ATS1, BJM1);
    }//GEN-LAST:event_DialisatKeyPressed

    private void EktremitasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_EktremitasKeyPressed
        Valid.pindah(evt, Konjungtiva, AksesVaskular);
    }//GEN-LAST:event_EktremitasKeyPressed

    private void HeparinisasiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_HeparinisasiKeyPressed
        //    Valid.pindah(evt, HasilJatuh, KetProvokes);
    }//GEN-LAST:event_HeparinisasiKeyPressed

    private void IntervensiJatuhKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_IntervensiJatuhKeyPressed
        //   Valid.pindah(evt, KetProvokes, Qb);
    }//GEN-LAST:event_IntervensiJatuhKeyPressed

    private void QbKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_QbKeyPressed
        Valid.pindah(evt, IntervensiJatuh, BbPreHD);
    }//GEN-LAST:event_QbKeyPressed

    private void BbPreHDKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BbPreHDKeyPressed
        Valid.pindah(evt, Qb, MSA1);
    }//GEN-LAST:event_BbPreHDKeyPressed

    private void MSA1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_MSA1KeyPressed
        Valid.pindah(evt, BbPreHD, resepHd);
    }//GEN-LAST:event_MSA1KeyPressed

    private void resepHdKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_resepHdKeyPressed
        //   Valid.pindah(evt, MSA1, Durasi);
    }//GEN-LAST:event_resepHdKeyPressed

    private void QdKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_QdKeyPressed
        //    Valid.pindah(evt, NyeriHilang, PadaDokter);
    }//GEN-LAST:event_QdKeyPressed

    private void IntervensiKolaborasiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_IntervensiKolaborasiKeyPressed
        Valid.pindah2(evt, TCariMasalah, BtnSimpan);
    }//GEN-LAST:event_IntervensiKolaborasiKeyPressed

    private void BbLaluKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BbLaluKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BbLaluKeyPressed

    private void TglAsuhanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TglAsuhanKeyPressed
        Valid.pindah(evt, IntervensiKolaborasi, Informasi);
    }//GEN-LAST:event_TglAsuhanKeyPressed

    private void SifatNyeriKetKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SifatNyeriKetKeyPressed
        Valid.pindah(evt, Dializer, EfekNyeriKet);
    }//GEN-LAST:event_SifatNyeriKetKeyPressed

    private void KonjungtivaKetKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KonjungtivaKetKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_KonjungtivaKetKeyPressed

    private void KualitasNyeriKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KualitasNyeriKeyPressed
        Valid.pindah(evt, Respirasi, BbPostHD);
    }//GEN-LAST:event_KualitasNyeriKeyPressed

    private void BbPostHDKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BbPostHDKeyPressed
        Valid.pindah(evt, KualitasNyeri, Nadi);
    }//GEN-LAST:event_BbPostHDKeyPressed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        try {
            if (Valid.daysOld("./cache/masalahkeperawatan.iyem") < 30) {
                tampilMasalah2();
            } else {
                tampilMasalah();
            }
        } catch (Exception e) {
        }
    }//GEN-LAST:event_formWindowOpened

    private void ATS1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ATS1ItemStateChanged
        Dialisat.setSelectedIndex(ATS1.getSelectedIndex());
        BbLalu.setText("" + (Integer.parseInt(Dialisat.getSelectedItem().toString()) + Integer.parseInt(Detik.getSelectedItem().toString())));
    }//GEN-LAST:event_ATS1ItemStateChanged

    private void DialisatItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_DialisatItemStateChanged
        BbLalu.setText("" + (Integer.parseInt(Dialisat.getSelectedItem().toString()) + Integer.parseInt(Detik.getSelectedItem().toString())));
    }//GEN-LAST:event_DialisatItemStateChanged

    private void BJM1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_BJM1ItemStateChanged
        Detik.setSelectedIndex(BJM1.getSelectedIndex());
        BbLalu.setText("" + (Integer.parseInt(Dialisat.getSelectedItem().toString()) + Integer.parseInt(Detik.getSelectedItem().toString())));
    }//GEN-LAST:event_BJM1ItemStateChanged

    private void ChkAccorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChkAccorActionPerformed
        if (tbObat.getSelectedRow() != -1) {
            isMenu();
        } else {
            ChkAccor.setSelected(false);
            JOptionPane.showMessageDialog(null, "Maaf, silahkan pilih data yang mau ditampilkan...!!!!");
        }
    }//GEN-LAST:event_ChkAccorActionPerformed

    private void BtnPrint1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnPrint1ActionPerformed
        if (tbObat.getSelectedRow() > -1) {
            Map<String, Object> param = new HashMap<>();
            param.put("namars", akses.getnamars());
            param.put("alamatrs", akses.getalamatrs());
            param.put("kotars", akses.getkabupatenrs());
            param.put("propinsirs", akses.getpropinsirs());
            param.put("kontakrs", akses.getkontakrs());
            param.put("emailrs", akses.getemailrs());
            param.put("logo", Sequel.cariGambar("select setting.logo from setting"));
            param.put("nyeri", Sequel.cariGambar("select gambar.nyeri from gambar"));
            finger = Sequel.cariIsi("select sha1(sidikjari.sidikjari) from sidikjari inner join pegawai on pegawai.id=sidikjari.id where pegawai.nik=?", tbObat.getValueAt(tbObat.getSelectedRow(), 63).toString());
            param.put("finger", "Dikeluarkan di " + akses.getnamars() + ", Kabupaten/Kota " + akses.getkabupatenrs() + "\nDitandatangani secara elektronik oleh " + tbObat.getValueAt(tbObat.getSelectedRow(), 64).toString() + "\nID " + (finger.equals("") ? tbObat.getValueAt(tbObat.getSelectedRow(), 63).toString() : finger) + "\n" + Valid.SetTgl3(tbObat.getValueAt(tbObat.getSelectedRow(), 8).toString()));
            try {
                masalahkeperawatan = "";
                ps2 = koneksi.prepareStatement(
                        "select master_masalah_keperawatan.kode_masalah,master_masalah_keperawatan.nama_masalah from master_masalah_keperawatan "
                        + "inner join asesmen_ulang_hemodialisa_masalah on asesmen_ulang_hemodialisa_masalah.kode_masalah=master_masalah_keperawatan.kode_masalah "
                        + "where asesmen_ulang_hemodialisa_masalah.no_rawat=? order by asesmen_ulang_hemodialisa_masalah.kode_masalah");
                try {
                    ps2.setString(1, tbObat.getValueAt(tbObat.getSelectedRow(), 0).toString());
                    rs2 = ps2.executeQuery();
                    while (rs2.next()) {
                        masalahkeperawatan = rs2.getString("nama_masalah") + ", " + masalahkeperawatan;
                    }
                } catch (Exception e) {
                    System.out.println("Notif : " + e);
                } finally {
                    if (rs2 != null) {
                        rs2.close();
                    }
                    if (ps2 != null) {
                        ps2.close();
                    }
                }
            } catch (Exception e) {
                System.out.println("Notif : " + e);
            }
            param.put("masalah", masalahkeperawatan);
            try {
                masalahkeperawatan = "";
                ps2 = koneksi.prepareStatement(
                        "select master_rencana_keperawatan.kode_rencana,master_rencana_keperawatan.rencana_keperawatan from master_rencana_keperawatan "
                        + "inner join asesmen_ulang_hemodialisa_rencana on asesmen_ulang_hemodialisa_rencana.kode_rencana=master_rencana_keperawatan.kode_rencana "
                        + "where asesmen_ulang_hemodialisa_rencana.no_rawat=? order by asesmen_ulang_hemodialisa_rencana.kode_rencana");
                try {
                    ps2.setString(1, tbObat.getValueAt(tbObat.getSelectedRow(), 0).toString());
                    rs2 = ps2.executeQuery();
                    while (rs2.next()) {
                        masalahkeperawatan = rs2.getString("rencana_keperawatan") + ", " + masalahkeperawatan;
                    }
                } catch (Exception e) {
                    System.out.println("Notif : " + e);
                } finally {
                    if (rs2 != null) {
                        rs2.close();
                    }
                    if (ps2 != null) {
                        ps2.close();
                    }
                }
            } catch (Exception e) {
                System.out.println("Notif : " + e);
            }
            param.put("rencana", masalahkeperawatan);
            Valid.MyReportqry("rptCetakAsesmenUlangHemodialisa.jasper", "report", "::[ Laporan Penilaian Awal Keperawatan Ralan ]::",
                    "SELECT reg_periksa.no_rawat, reg_periksa.no_rkm_medis, pasien.nm_pasien, pasien.jk, pasien.tgl_lahir, pasien.agama, pasien.cacat_fisik, pasien.bahasa_pasien, "
                    + "asesmen_ulang_hemodialisa.nip, asesmen_ulang_hemodialisa.tanggal, asesmen_ulang_hemodialisa.informasi, asesmen_ulang_hemodialisa.diagnosa, "
                    + "asesmen_ulang_hemodialisa.terapike, asesmen_ulang_hemodialisa.dializer, asesmen_ulang_hemodialisa.alergi, asesmen_ulang_hemodialisa.penilaian_nyeri, "
                    + "asesmen_ulang_hemodialisa.intensitas_nyeri, asesmen_ulang_hemodialisa.lokasi_nyeri, asesmen_ulang_hemodialisa.kapan, "
                    + "asesmen_ulang_hemodialisa.sifat_nyeri, asesmen_ulang_hemodialisa.sifat_nyeriket, asesmen_ulang_hemodialisa.kualitas_nyeri, "
                    + "asesmen_ulang_hemodialisa.kualitas_nyeriket, asesmen_ulang_hemodialisa.pemberat, asesmen_ulang_hemodialisa.pemberat_ket, "
                    + "asesmen_ulang_hemodialisa.peringan, asesmen_ulang_hemodialisa.peringanket, asesmen_ulang_hemodialisa.efek_nyeri, "
                    + "asesmen_ulang_hemodialisa.efek_nyeriket, asesmen_ulang_hemodialisa.ku, asesmen_ulang_hemodialisa.ku_ket, "
                    + "asesmen_ulang_hemodialisa.td, asesmen_ulang_hemodialisa.nadi, asesmen_ulang_hemodialisa.frekuensi_nadi, asesmen_ulang_hemodialisa.respirasi, "
                    + "asesmen_ulang_hemodialisa.rr, asesmen_ulang_hemodialisa.konjungtiva, asesmen_ulang_hemodialisa.konjungtivaket, asesmen_ulang_hemodialisa.ekstremitas, "
                    + "asesmen_ulang_hemodialisa.bb_kering, asesmen_ulang_hemodialisa.bb_prehd, asesmen_ulang_hemodialisa.bb_lalu, asesmen_ulang_hemodialisa.bb_posthd, "
                    + "asesmen_ulang_hemodialisa.akses, asesmen_ulang_hemodialisa.akses_jalur, asesmen_ulang_hemodialisa.aksesket, asesmen_ulang_hemodialisa.berjalan_a, "
                    + "asesmen_ulang_hemodialisa.berjalan_b, asesmen_ulang_hemodialisa.berjalan_c, asesmen_ulang_hemodialisa.hasil, asesmen_ulang_hemodialisa.intervensi_jatuh, "
                    + "asesmen_ulang_hemodialisa.intervensi_kolaborasi, petugas.nama, cacat_fisik.nama_cacat, bahasa_pasien.nama_bahasa FROM reg_periksa INNER JOIN pasien "
                    + "ON reg_periksa.no_rkm_medis = pasien.no_rkm_medis INNER JOIN asesmen_ulang_hemodialisa ON reg_periksa.no_rawat = asesmen_ulang_hemodialisa.no_rawat "
                    + "INNER JOIN petugas ON asesmen_ulang_hemodialisa.nip = petugas.nip INNER JOIN cacat_fisik ON pasien.cacat_fisik = cacat_fisik.id INNER JOIN bahasa_pasien "
                    + "ON pasien.bahasa_pasien = bahasa_pasien.id where reg_periksa.no_rawat='" + tbObat.getValueAt(tbObat.getSelectedRow(), 0).toString() + "'", param);
        } else {
            JOptionPane.showMessageDialog(null, "Maaf, silahkan pilih data terlebih dahulu..!!!!");
        }
    }//GEN-LAST:event_BtnPrint1ActionPerformed

    private void tbMasalahKeperawatanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbMasalahKeperawatanKeyPressed
        if (tabModeMasalah.getRowCount() != 0) {
            if (evt.getKeyCode() == KeyEvent.VK_SHIFT) {
                TCariMasalah.setText("");
                TCariMasalah.requestFocus();
            }
        }
    }//GEN-LAST:event_tbMasalahKeperawatanKeyPressed

    private void tbMasalahKeperawatanKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbMasalahKeperawatanKeyReleased
        if (tabModeMasalah.getRowCount() != 0) {
            if ((evt.getKeyCode() == KeyEvent.VK_ENTER) || (evt.getKeyCode() == KeyEvent.VK_UP) || (evt.getKeyCode() == KeyEvent.VK_DOWN)) {
                try {
                    tampilRencana2();
                } catch (java.lang.NullPointerException e) {
                }
            }
        }
    }//GEN-LAST:event_tbMasalahKeperawatanKeyReleased

    private void tbMasalahKeperawatanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbMasalahKeperawatanMouseClicked
        if (tabModeMasalah.getRowCount() != 0) {
            try {
                tampilRencana2();
            } catch (java.lang.NullPointerException e) {
            }
        }
    }//GEN-LAST:event_tbMasalahKeperawatanMouseClicked

    private void HasilJatuhKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_HasilJatuhKeyPressed
        Valid.pindah(evt, Ektremitas, Jalur);
    }//GEN-LAST:event_HasilJatuhKeyPressed

    private void HdKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_HdKeyPressed
        Valid.pindah(evt, Jalur, ATS1);
    }//GEN-LAST:event_HdKeyPressed

    private void UfKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_UfKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_UfKeyPressed

    private void Heparinisasi_KetKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Heparinisasi_KetKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_Heparinisasi_KetKeyPressed

    private void KdDokterKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KdDokterKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_KdDokterKeyPressed

    private void NmDokterKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_NmDokterKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_NmDokterKeyPressed

    private void Qb2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Qb2KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_Qb2KeyPressed

    private void Uf2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Uf2KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_Uf2KeyPressed

    private void ObservasiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ObservasiKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_ObservasiKeyPressed

    private void Td2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Td2KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_Td2KeyPressed

    private void NIPKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_NIPKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_NIPKeyPressed

    private void NamaPetugasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_NamaPetugasKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_NamaPetugasKeyPressed

    private void Nadi2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Nadi2KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_Nadi2KeyPressed

    private void SuhuKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SuhuKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_SuhuKeyPressed

    private void Respirasi2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Respirasi2KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_Respirasi2KeyPressed

    private void VpKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_VpKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_VpKeyPressed

    private void ApKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ApKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_ApKeyPressed

    private void TmpKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TmpKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_TmpKeyPressed

    private void Ket_LainKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Ket_LainKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_Ket_LainKeyPressed

    private void EvaluasiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_EvaluasiKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_EvaluasiKeyPressed

    private void TCariMasalahKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TCariMasalahKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            tampilMasalah2();
        } else if ((evt.getKeyCode() == KeyEvent.VK_PAGE_DOWN) || (evt.getKeyCode() == KeyEvent.VK_TAB)) {
            IntervensiKolaborasi.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_PAGE_UP) {
            //  KetDokter.requestFocus();
        }
    }//GEN-LAST:event_TCariMasalahKeyPressed

    private void BtnCariMasalahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCariMasalahActionPerformed
        tampilMasalah2();
    }//GEN-LAST:event_BtnCariMasalahActionPerformed

    private void BtnCariMasalahKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnCariMasalahKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            tampilMasalah2();
        } else if ((evt.getKeyCode() == KeyEvent.VK_PAGE_DOWN) || (evt.getKeyCode() == KeyEvent.VK_TAB)) {
            IntervensiKolaborasi.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_PAGE_UP) {
            //   KetDokter.requestFocus();
        }
    }//GEN-LAST:event_BtnCariMasalahKeyPressed

    private void BtnAllMasalahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAllMasalahActionPerformed
        TCari.setText("");
        tampilMasalah();
    }//GEN-LAST:event_BtnAllMasalahActionPerformed

    private void BtnAllMasalahKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnAllMasalahKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_SPACE) {
            BtnAllMasalahActionPerformed(null);
        } else {
            Valid.pindah(evt, BtnCariMasalah, TCariMasalah);
        }
    }//GEN-LAST:event_BtnAllMasalahKeyPressed

    private void BtnTambahMasalahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnTambahMasalahActionPerformed
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        MasterMasalahKeperawatan form = new MasterMasalahKeperawatan(null, false);
        form.isCek();
        form.setSize(internalFrame1.getWidth() - 20, internalFrame1.getHeight() - 20);
        form.setLocationRelativeTo(internalFrame1);
        form.setVisible(true);
        this.setCursor(Cursor.getDefaultCursor());
    }//GEN-LAST:event_BtnTambahMasalahActionPerformed

    private void TCariRencanaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TCariRencanaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            tampilRencana2();
        } else if ((evt.getKeyCode() == KeyEvent.VK_PAGE_DOWN) || (evt.getKeyCode() == KeyEvent.VK_TAB)) {
            BtnCariRencana.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_PAGE_UP) {
            TCariMasalah.requestFocus();
        }
    }//GEN-LAST:event_TCariRencanaKeyPressed

    private void BtnCariRencanaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCariRencanaActionPerformed
        tampilRencana2();
    }//GEN-LAST:event_BtnCariRencanaActionPerformed

    private void BtnCariRencanaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnCariRencanaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            tampilRencana2();
        } else if ((evt.getKeyCode() == KeyEvent.VK_PAGE_DOWN) || (evt.getKeyCode() == KeyEvent.VK_TAB)) {
            BtnSimpan.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_PAGE_UP) {
            TCariRencana.requestFocus();
        }
    }//GEN-LAST:event_BtnCariRencanaKeyPressed

    private void BtnAllRencanaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAllRencanaActionPerformed
        TCariRencana.setText("");
        tampilRencana();
        tampilRencana2();
    }//GEN-LAST:event_BtnAllRencanaActionPerformed

    private void BtnAllRencanaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnAllRencanaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_SPACE) {
            BtnAllRencanaActionPerformed(null);
        } else {
            Valid.pindah(evt, BtnCariRencana, TCariRencana);
        }
    }//GEN-LAST:event_BtnAllRencanaKeyPressed

    private void BtnTambahRencanaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnTambahRencanaActionPerformed
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        MasterRencanaKeperawatan form = new MasterRencanaKeperawatan(null, false);
        form.isCek();
        form.setSize(internalFrame1.getWidth() - 20, internalFrame1.getHeight() - 20);
        form.setLocationRelativeTo(internalFrame1);
        form.setVisible(true);
        this.setCursor(Cursor.getDefaultCursor());
    }//GEN-LAST:event_BtnTambahRencanaActionPerformed

    private void BtnDokter1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnDokter1ActionPerformed
        dokter.isCek();
        dokter.setSize(internalFrame1.getWidth() - 20, internalFrame1.getHeight() - 20);
        dokter.setLocationRelativeTo(internalFrame1);
        dokter.setAlwaysOnTop(false);
        dokter.setVisible(true);
    }//GEN-LAST:event_BtnDokter1ActionPerformed

    private void BtnDokter1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnDokter1KeyPressed
        Valid.pindah(evt, TCari, BtnSimpan);
    }//GEN-LAST:event_BtnDokter1KeyPressed

    private void TanggalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TanggalKeyPressed
        Valid.pindah(evt, TCari, Jam);
    }//GEN-LAST:event_TanggalKeyPressed

    private void JamKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_JamKeyPressed
        Valid.pindah(evt, Tanggal, Menit);
    }//GEN-LAST:event_JamKeyPressed

    private void MenitKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_MenitKeyPressed
        Valid.pindah(evt, Jam, Detik);
    }//GEN-LAST:event_MenitKeyPressed

    private void DetikKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DetikKeyPressed
        Valid.pindah(evt, Menit, btnPetugas);
    }//GEN-LAST:event_DetikKeyPressed

    private void btnPetugasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPetugasActionPerformed
        pilihan = 2;
        petugas.emptTeks();
        petugas.isCek();
        petugas.setSize(internalFrame1.getWidth() - 20, internalFrame1.getHeight() - 20);
        petugas.setLocationRelativeTo(internalFrame1);
        petugas.setVisible(true);
    }//GEN-LAST:event_btnPetugasActionPerformed

    private void btnPetugasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnPetugasKeyPressed
        //  Valid.pindah(evt,Detik,GCS);
    }//GEN-LAST:event_btnPetugasKeyPressed

    private void NIP1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_NIP1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_NIP1KeyPressed

    private void NamaPetugas1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_NamaPetugas1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_NamaPetugas1KeyPressed

    private void btnPetugas1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPetugas1ActionPerformed
        pilihan = 3;
        petugas.emptTeks();
        petugas.isCek();
        petugas.setSize(internalFrame1.getWidth() - 20, internalFrame1.getHeight() - 20);
        petugas.setLocationRelativeTo(internalFrame1);
        petugas.setVisible(true);
    }//GEN-LAST:event_btnPetugas1ActionPerformed

    private void btnPetugas1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnPetugas1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnPetugas1KeyPressed

    private void tbObat1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbObat1MouseClicked
        if (tabMode2.getRowCount() != 0) {
            try {
                getData2();
            } catch (java.lang.NullPointerException e) {
            }
        }
    }//GEN-LAST:event_tbObat1MouseClicked

    private void tbObat1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbObat1KeyPressed
        if (tabMode.getRowCount() != 0) {
            if ((evt.getKeyCode() == KeyEvent.VK_ENTER) || (evt.getKeyCode() == KeyEvent.VK_UP) || (evt.getKeyCode() == KeyEvent.VK_DOWN)) {
                try {
                    getData();
                } catch (java.lang.NullPointerException e) {
                }
            }
        }
    }//GEN-LAST:event_tbObat1KeyPressed

    private void tbObat2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbObat2MouseClicked
        if (tabMode.getRowCount() != 0) {
            try {
                getData();
            } catch (java.lang.NullPointerException e) {
            }
        }
    }//GEN-LAST:event_tbObat2MouseClicked

    private void tbObat2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbObat2KeyPressed
        if (tabMode.getRowCount() != 0) {
            if ((evt.getKeyCode() == KeyEvent.VK_ENTER) || (evt.getKeyCode() == KeyEvent.VK_UP) || (evt.getKeyCode() == KeyEvent.VK_DOWN)) {
                try {
                    getData();
                } catch (java.lang.NullPointerException e) {
                }
            }
        }
    }//GEN-LAST:event_tbObat2KeyPressed

    private void tbObat3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbObat3MouseClicked
        if (tabMode.getRowCount() != 0) {
            try {
                getData();
            } catch (java.lang.NullPointerException e) {
            }
        }
    }//GEN-LAST:event_tbObat3MouseClicked

    private void tbObat3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbObat3KeyPressed
        if (tabMode.getRowCount() != 0) {
            if ((evt.getKeyCode() == KeyEvent.VK_ENTER) || (evt.getKeyCode() == KeyEvent.VK_UP) || (evt.getKeyCode() == KeyEvent.VK_DOWN)) {
                try {
                    getData();
                } catch (java.lang.NullPointerException e) {
                }
            }
        }
    }//GEN-LAST:event_tbObat3KeyPressed

    private void ChkInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChkInputActionPerformed
        isForm();
    }//GEN-LAST:event_ChkInputActionPerformed

    private void ChkInput1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChkInput1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ChkInput1ActionPerformed

    private void ChkInput2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChkInput2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ChkInput2ActionPerformed

    private void tbPenyulitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbPenyulitMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tbPenyulitMouseClicked

    private void tbPenyulitKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbPenyulitKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tbPenyulitKeyPressed

    private void tbPenyulitKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbPenyulitKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_tbPenyulitKeyReleased

    private void TCariPenyulitKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TCariPenyulitKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_TCariPenyulitKeyPressed

    private void BtnCariMasalah1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCariMasalah1ActionPerformed
        tampilPenyulit2();
    }//GEN-LAST:event_BtnCariMasalah1ActionPerformed

    private void BtnCariMasalah1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnCariMasalah1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnCariMasalah1KeyPressed

    private void BtnAllMasalah1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAllMasalah1ActionPerformed
        tampilPenyulit();
    }//GEN-LAST:event_BtnAllMasalah1ActionPerformed

    private void BtnAllMasalah1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnAllMasalah1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnAllMasalah1KeyPressed

    private void BtnTambahMasalah1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnTambahMasalah1ActionPerformed
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        MasterPenyulitHD form = new MasterPenyulitHD(null, false);
        form.isCek();
        form.setSize(internalFrame1.getWidth() - 20, internalFrame1.getHeight() - 20);
        form.setLocationRelativeTo(internalFrame1);
        form.setVisible(true);
        this.setCursor(Cursor.getDefaultCursor());
    }//GEN-LAST:event_BtnTambahMasalah1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            RMAsesmenUlangHemodialisis dialog = new RMAsesmenUlangHemodialisis(new javax.swing.JFrame(), true);
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
    private widget.ComboBox ATS1;
    private widget.ComboBox AksesVaskular;
    private widget.TextBox Alergi;
    private widget.TextBox Ap;
    private widget.ComboBox BJM1;
    private widget.TextBox BbKering;
    private widget.TextBox BbLalu;
    private widget.TextBox BbPostHD;
    private widget.TextBox BbPreHD;
    private widget.Button BtnAll;
    private widget.Button BtnAllMasalah;
    private widget.Button BtnAllMasalah1;
    private widget.Button BtnAllRencana;
    private widget.Button BtnBatal;
    private widget.Button BtnCari;
    private widget.Button BtnCariMasalah;
    private widget.Button BtnCariMasalah1;
    private widget.Button BtnCariRencana;
    private widget.Button BtnDokter;
    private widget.Button BtnDokter1;
    private widget.Button BtnEdit;
    private widget.Button BtnHapus;
    private widget.Button BtnKeluar;
    private widget.Button BtnPrint;
    private widget.Button BtnPrint1;
    private widget.Button BtnSimpan;
    private widget.Button BtnTambahMasalah;
    private widget.Button BtnTambahMasalah1;
    private widget.Button BtnTambahRencana;
    private widget.CekBox ChkAccor;
    private widget.CekBox ChkInput;
    private widget.CekBox ChkInput1;
    private widget.CekBox ChkInput2;
    private widget.CekBox ChkKejadian;
    private widget.Tanggal DTPCari1;
    private widget.Tanggal DTPCari2;
    private widget.TextArea DetailRencana;
    private widget.ComboBox Detik;
    private widget.TextBox DiagnosaMedis;
    private widget.ComboBox Dialisat;
    private widget.TextBox Dializer;
    private widget.ComboBox EfekNyeri;
    private widget.TextBox EfekNyeriKet;
    private widget.ComboBox Ektremitas;
    private widget.TextBox Evaluasi;
    private widget.TextBox FaktoPeringanKet;
    private widget.ComboBox FaktorPemberat;
    private widget.TextBox FaktorPemberatKet;
    private widget.ComboBox FaktorPeringan;
    private widget.PanelBiasa FormInput;
    private widget.PanelBiasa FormInput1;
    private widget.PanelBiasa FormInput2;
    private widget.PanelBiasa FormInput3;
    private widget.PanelBiasa FormMasalahRencana;
    private widget.PanelBiasa FormMenu;
    private widget.ComboBox HasilJatuh;
    private widget.TextBox Hd;
    private widget.ComboBox Heparinisasi;
    private widget.TextBox Heparinisasi_Ket;
    private widget.ComboBox Informasi;
    private widget.ComboBox IntensitasNyeri;
    private widget.ComboBox IntervensiJatuh;
    private widget.TextArea IntervensiKolaborasi;
    private widget.ComboBox Jalur;
    private widget.TextBox JalurKet;
    private widget.ComboBox Jam;
    private widget.TextBox Jk;
    private widget.TextBox Kapan;
    private widget.TextBox KdDokter;
    private widget.TextBox KdPetugas;
    private widget.TextBox Ket_Lain;
    private widget.ComboBox Konjungtiva;
    private widget.TextBox KonjungtivaKet;
    private widget.ComboBox Ku;
    private widget.TextBox KuKet;
    private widget.ComboBox KualitasNyeri;
    private widget.TextBox KualitasNyeriKet;
    private widget.Label LCount;
    private widget.editorpane LoadHTML;
    private widget.TextBox LokasiNyeri;
    private widget.ComboBox MSA1;
    private widget.ComboBox Menit;
    private widget.TextBox NIP;
    private widget.TextBox NIP1;
    private widget.ComboBox Nadi;
    private widget.TextBox Nadi2;
    private widget.TextBox NadiKet;
    private widget.TextBox NamaPetugas;
    private widget.TextBox NamaPetugas1;
    private widget.TextBox NmDokter;
    private widget.TextBox NmPetugas;
    private widget.ComboBox NyeriPasien;
    private widget.TextBox Observasi;
    private widget.PanelBiasa PanelAccor;
    private javax.swing.JPanel PanelInput;
    private javax.swing.JPanel PanelInput1;
    private javax.swing.JPanel PanelInput2;
    private usu.widget.glass.PanelGlass PanelWall;
    private widget.TextBox Qb;
    private widget.TextBox Qb2;
    private widget.TextBox Qd;
    private widget.ComboBox Respirasi;
    private widget.TextBox Respirasi2;
    private widget.TextBox RespirasiKet;
    private widget.ScrollPane Scroll;
    private widget.ScrollPane Scroll1;
    private widget.ScrollPane Scroll10;
    private widget.ScrollPane Scroll2;
    private widget.ScrollPane Scroll3;
    private widget.ScrollPane Scroll6;
    private widget.ScrollPane Scroll7;
    private widget.ScrollPane Scroll8;
    private widget.ScrollPane Scroll9;
    private widget.ComboBox SifatNyeri;
    private widget.TextBox SifatNyeriKet;
    private widget.TextBox Suhu;
    private widget.TextBox TCari;
    private widget.TextBox TCariMasalah;
    private widget.TextBox TCariPenyulit;
    private widget.TextBox TCariRencana;
    private widget.TextBox TNoRM;
    private widget.TextBox TNoRM1;
    private widget.TextBox TNoRw;
    private widget.TextBox TPasien;
    private widget.TextBox TPasien1;
    private javax.swing.JTabbedPane TabRawat;
    private javax.swing.JTabbedPane TabRencanaKeperawatan;
    private widget.Tanggal Tanggal;
    private widget.TextBox TanggalRegistrasi;
    private widget.TextBox Td;
    private widget.TextBox Td2;
    private widget.TextBox TerapiKe;
    private widget.Tanggal TglAsuhan;
    private widget.TextBox TglLahir;
    private widget.TextBox Tmp;
    private widget.TextBox Uf;
    private widget.TextBox Uf2;
    private widget.TextBox Vp;
    private widget.Button btnPetugas;
    private widget.Button btnPetugas1;
    private widget.InternalFrame internalFrame1;
    private widget.InternalFrame internalFrame2;
    private widget.InternalFrame internalFrame3;
    private widget.InternalFrame internalFrame4;
    private widget.InternalFrame internalFrame5;
    private widget.InternalFrame internalFrame6;
    private widget.Label jLabel10;
    private widget.Label jLabel100;
    private widget.Label jLabel101;
    private widget.Label jLabel102;
    private widget.Label jLabel104;
    private widget.Label jLabel105;
    private widget.Label jLabel108;
    private widget.Label jLabel109;
    private widget.Label jLabel11;
    private widget.Label jLabel110;
    private widget.Label jLabel111;
    private widget.Label jLabel112;
    private widget.Label jLabel113;
    private widget.Label jLabel114;
    private widget.Label jLabel115;
    private widget.Label jLabel116;
    private widget.Label jLabel117;
    private widget.Label jLabel119;
    private widget.Label jLabel121;
    private widget.Label jLabel138;
    private widget.Label jLabel14;
    private widget.Label jLabel15;
    private widget.Label jLabel16;
    private widget.Label jLabel18;
    private widget.Label jLabel19;
    private widget.Label jLabel21;
    private widget.Label jLabel25;
    private widget.Label jLabel26;
    private widget.Label jLabel27;
    private widget.Label jLabel28;
    private widget.Label jLabel30;
    private widget.Label jLabel31;
    private widget.Label jLabel34;
    private widget.Label jLabel36;
    private widget.Label jLabel37;
    private widget.Label jLabel43;
    private widget.Label jLabel50;
    private widget.Label jLabel51;
    private widget.Label jLabel52;
    private widget.Label jLabel53;
    private widget.Label jLabel54;
    private widget.Label jLabel55;
    private widget.Label jLabel57;
    private widget.Label jLabel58;
    private widget.Label jLabel59;
    private widget.Label jLabel6;
    private widget.Label jLabel60;
    private widget.Label jLabel61;
    private widget.Label jLabel62;
    private widget.Label jLabel63;
    private widget.Label jLabel65;
    private widget.Label jLabel66;
    private widget.Label jLabel68;
    private widget.Label jLabel69;
    private widget.Label jLabel7;
    private widget.Label jLabel70;
    private widget.Label jLabel71;
    private widget.Label jLabel72;
    private widget.Label jLabel73;
    private widget.Label jLabel75;
    private widget.Label jLabel76;
    private widget.Label jLabel77;
    private widget.Label jLabel78;
    private widget.Label jLabel79;
    private widget.Label jLabel8;
    private widget.Label jLabel80;
    private widget.Label jLabel81;
    private widget.Label jLabel82;
    private widget.Label jLabel83;
    private widget.Label jLabel84;
    private widget.Label jLabel85;
    private widget.Label jLabel86;
    private widget.Label jLabel87;
    private widget.Label jLabel88;
    private widget.Label jLabel89;
    private widget.Label jLabel90;
    private widget.Label jLabel91;
    private widget.Label jLabel92;
    private widget.Label jLabel93;
    private widget.Label jLabel94;
    private widget.Label jLabel95;
    private widget.Label jLabel96;
    private widget.Label jLabel97;
    private widget.Label jLabel98;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator8;
    private widget.Label label11;
    private widget.Label label12;
    private widget.Label label13;
    private widget.Label label14;
    private widget.Label label15;
    private widget.PanelBiasa panelBiasa1;
    private widget.panelisi panelGlass10;
    private widget.panelisi panelGlass8;
    private widget.panelisi panelGlass9;
    private widget.ComboBox resepHd;
    private widget.ScrollPane scrollInput;
    private widget.ScrollPane scrollPane5;
    private widget.ScrollPane scrollPane6;
    private widget.Table tbMasalahDetail;
    private widget.Table tbMasalahKeperawatan;
    private widget.Table tbObat;
    private widget.Table tbObat1;
    private widget.Table tbObat2;
    private widget.Table tbObat3;
    private widget.Table tbPenyulit;
    private widget.Table tbRencanaDetail;
    private widget.Table tbRencanaKeperawatan;
    // End of variables declaration//GEN-END:variables

    private void tampil() {
        Valid.tabelKosong(tabMode);
        try {
            if (TCari.getText().equals("")) {
                ps = koneksi.prepareStatement(
                        "SELECT reg_periksa.no_rawat, reg_periksa.no_rkm_medis, pasien.nm_pasien, pasien.jk, pasien.tgl_lahir, pasien.agama, pasien.cacat_fisik, pasien.bahasa_pasien, "
                        + "asesmen_ulang_hemodialisa.nip, asesmen_ulang_hemodialisa.tanggal, asesmen_ulang_hemodialisa.informasi, asesmen_ulang_hemodialisa.diagnosa, "
                        + "asesmen_ulang_hemodialisa.terapike, asesmen_ulang_hemodialisa.dializer, asesmen_ulang_hemodialisa.alergi, asesmen_ulang_hemodialisa.penilaian_nyeri, "
                        + "asesmen_ulang_hemodialisa.intensitas_nyeri, asesmen_ulang_hemodialisa.lokasi_nyeri, asesmen_ulang_hemodialisa.kapan, asesmen_ulang_hemodialisa.sifat_nyeri, "
                        + "asesmen_ulang_hemodialisa.sifat_nyeriket, asesmen_ulang_hemodialisa.kualitas_nyeri, asesmen_ulang_hemodialisa.kualitas_nyeriket, asesmen_ulang_hemodialisa.pemberat, "
                        + "asesmen_ulang_hemodialisa.pemberat_ket, asesmen_ulang_hemodialisa.peringan, asesmen_ulang_hemodialisa.peringanket, asesmen_ulang_hemodialisa.efek_nyeri, "
                        + "asesmen_ulang_hemodialisa.efek_nyeriket, asesmen_ulang_hemodialisa.ku, asesmen_ulang_hemodialisa.ku_ket, asesmen_ulang_hemodialisa.td, asesmen_ulang_hemodialisa.nadi, "
                        + "asesmen_ulang_hemodialisa.frekuensi_nadi, asesmen_ulang_hemodialisa.respirasi, asesmen_ulang_hemodialisa.rr, asesmen_ulang_hemodialisa.konjungtiva, "
                        + "asesmen_ulang_hemodialisa.konjungtivaket, asesmen_ulang_hemodialisa.ekstremitas, asesmen_ulang_hemodialisa.bb_kering, asesmen_ulang_hemodialisa.bb_prehd, "
                        + "asesmen_ulang_hemodialisa.bb_lalu, asesmen_ulang_hemodialisa.bb_posthd, asesmen_ulang_hemodialisa.akses, asesmen_ulang_hemodialisa.akses_jalur, "
                        + "asesmen_ulang_hemodialisa.aksesket, asesmen_ulang_hemodialisa.berjalan_a, asesmen_ulang_hemodialisa.berjalan_b, asesmen_ulang_hemodialisa.berjalan_c, "
                        + "asesmen_ulang_hemodialisa.hasil, asesmen_ulang_hemodialisa.intervensi_jatuh, asesmen_ulang_hemodialisa.intervensi_kolaborasi, petugas.nama, "
                        + "cacat_fisik.nama_cacat, bahasa_pasien.nama_bahasa FROM reg_periksa INNER JOIN pasien ON reg_periksa.no_rkm_medis = pasien.no_rkm_medis "
                        + "INNER JOIN asesmen_ulang_hemodialisa ON reg_periksa.no_rawat = asesmen_ulang_hemodialisa.no_rawat INNER JOIN petugas ON asesmen_ulang_hemodialisa.nip = "
                        + "petugas.nip INNER JOIN cacat_fisik ON pasien.cacat_fisik = cacat_fisik.id INNER JOIN bahasa_pasien ON pasien.bahasa_pasien = bahasa_pasien.id where "
                        + "asesmen_ulang_hemodialisa.tanggal between ? and ? order by asesmen_ulang_hemodialisa.tanggal");
            } else {
                ps = koneksi.prepareStatement(
                        "SELECT reg_periksa.no_rawat, reg_periksa.no_rkm_medis, pasien.nm_pasien, pasien.jk, pasien.tgl_lahir, pasien.agama, pasien.cacat_fisik, pasien.bahasa_pasien, "
                        + "asesmen_ulang_hemodialisa.nip, asesmen_ulang_hemodialisa.tanggal, asesmen_ulang_hemodialisa.informasi, asesmen_ulang_hemodialisa.diagnosa, "
                        + "asesmen_ulang_hemodialisa.terapike, asesmen_ulang_hemodialisa.dializer, asesmen_ulang_hemodialisa.alergi, asesmen_ulang_hemodialisa.penilaian_nyeri, "
                        + "asesmen_ulang_hemodialisa.intensitas_nyeri, asesmen_ulang_hemodialisa.lokasi_nyeri, asesmen_ulang_hemodialisa.kapan, asesmen_ulang_hemodialisa.sifat_nyeri, "
                        + "asesmen_ulang_hemodialisa.sifat_nyeriket, asesmen_ulang_hemodialisa.kualitas_nyeri, asesmen_ulang_hemodialisa.kualitas_nyeriket, asesmen_ulang_hemodialisa.pemberat, "
                        + "asesmen_ulang_hemodialisa.pemberat_ket, asesmen_ulang_hemodialisa.peringan, asesmen_ulang_hemodialisa.peringanket, asesmen_ulang_hemodialisa.efek_nyeri, "
                        + "asesmen_ulang_hemodialisa.efek_nyeriket, asesmen_ulang_hemodialisa.ku, asesmen_ulang_hemodialisa.ku_ket, asesmen_ulang_hemodialisa.td, asesmen_ulang_hemodialisa.nadi, "
                        + "asesmen_ulang_hemodialisa.frekuensi_nadi, asesmen_ulang_hemodialisa.respirasi, asesmen_ulang_hemodialisa.rr, asesmen_ulang_hemodialisa.konjungtiva, "
                        + "asesmen_ulang_hemodialisa.konjungtivaket, asesmen_ulang_hemodialisa.ekstremitas, asesmen_ulang_hemodialisa.bb_kering, asesmen_ulang_hemodialisa.bb_prehd, "
                        + "asesmen_ulang_hemodialisa.bb_lalu, asesmen_ulang_hemodialisa.bb_posthd, asesmen_ulang_hemodialisa.akses, asesmen_ulang_hemodialisa.akses_jalur, "
                        + "asesmen_ulang_hemodialisa.aksesket, asesmen_ulang_hemodialisa.berjalan_a, asesmen_ulang_hemodialisa.berjalan_b, asesmen_ulang_hemodialisa.berjalan_c, "
                        + "asesmen_ulang_hemodialisa.hasil, asesmen_ulang_hemodialisa.intervensi_jatuh, asesmen_ulang_hemodialisa.intervensi_kolaborasi, petugas.nama, "
                        + "cacat_fisik.nama_cacat, bahasa_pasien.nama_bahasa FROM reg_periksa INNER JOIN pasien ON reg_periksa.no_rkm_medis = pasien.no_rkm_medis "
                        + "INNER JOIN asesmen_ulang_hemodialisa ON reg_periksa.no_rawat = asesmen_ulang_hemodialisa.no_rawat INNER JOIN petugas ON asesmen_ulang_hemodialisa.nip = "
                        + "petugas.nip INNER JOIN cacat_fisik ON pasien.cacat_fisik = cacat_fisik.id INNER JOIN bahasa_pasien ON pasien.bahasa_pasien = bahasa_pasien.id where "
                        + "asesmen_ulang_hemodialisa.tanggal between ? and ? and "
                        + "(reg_periksa.no_rawat like ? or pasien.no_rkm_medis like ? or pasien.nm_pasien like ? or "
                        + "asesmen_ulang_hemodialisa.nip like ? or petugas.nama like ?) "
                        + "order by asesmen_ulang_hemodialisa.tanggal");
            }

            try {
                if (TCari.getText().equals("")) {
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
                    /*
                        "No.Rawat", "No.RM", "Nama Pasien", "Tgl.Lahir", "Kode Perawat", "Nama Perawat", "Tgl.Asuhan", "Informasi",
            "Diagnosa Medis", "Terapi HD Ke", "Tipe Dializer", "Riwayat Alergi", "Pasien Merasa Nyeri", "Intensitas Nyeri", "Lokasi Nyeri", "Sejak Kapan", "Sifat Nyeri",
            "Sifat Nyeri Keterangan", "Kualitas Nyeri", "Keterangan Kualitas Nyeri", "Faktor Pemberat", "Keterangan Faktor Pemberat", "Faktor Peringan", "Keterangan Faktor Peringan",
            "Efek Nyeri", "Keterangan Efek Nyeri", "Keadaan umum", "Keterangan Keadaan Umum", "Tekanan Darah", "Nadi", "Frekuensi Nadi", "Respirasi", "Frekuensi Respirasi",
            "Konjungtiva", "Keterangan Konjungtiva", "Ektremitas", "BB Kering", "BB Pre HD", "BB HD yang lalu", "BB PostHD", "Akses Vaskular", "Jalur Akses", "Keterangan Jalur Akses",
            "Cara Berjalan", "Alat Bantu", "Saat Duduk", "Intervensi Resiko Jatuh", "Intervensi Kolaborasi"
                     */

                    tabMode.addRow(new String[]{rs.getString("no_rawat"), rs.getString("no_rkm_medis"), rs.getString("nm_pasien"),
                        rs.getString("jk"), rs.getString("tgl_lahir"), rs.getString("nip"), rs.getString("nama"), rs.getString("tanggal"),
                        rs.getString("informasi"), rs.getString("diagnosa"), rs.getString("terapike"), rs.getString("dializer"),
                        rs.getString("alergi"), rs.getString("penilaian_nyeri"), rs.getString("intensitas_nyeri"), rs.getString("lokasi_nyeri"),
                        rs.getString("kapan"), rs.getString("sifat_nyeri"), rs.getString("sifat_nyeriket"), rs.getString("kualitas_nyeri"),
                        rs.getString("kualitas_nyeriket"), rs.getString("pemberat"), rs.getString("pemberat_ket"), rs.getString("peringan"),
                        rs.getString("peringanket"), rs.getString("efek_nyeri"), rs.getString("efek_nyeriket"), rs.getString("ku"),
                        rs.getString("ku_ket"), rs.getString("td"), rs.getString("nadi"), rs.getString("frekuensi_nadi"),
                        rs.getString("respirasi"), rs.getString("rr"), rs.getString("konjungtiva"), rs.getString("konjungtivaket"),
                        rs.getString("ekstremitas"), rs.getString("bb_kering"), rs.getString("bb_prehd"), rs.getString("bb_lalu"), rs.getString("bb_posthd"),
                        rs.getString("akses"), rs.getString("akses_jalur"), rs.getString("aksesket"), rs.getString("berjalan_a"),
                        rs.getString("berjalan_b"), rs.getString("berjalan_c"), rs.getString("hasil"), rs.getString("intervensi_jatuh"),
                        rs.getString("intervensi_kolaborasi")});

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
            System.out.println("Notifikasi : " + e);
        }
        LCount.setText("" + tabMode.getRowCount());
    }

    private void tampil2() {
        Valid.tabelKosong(tabMode2);
        try {
            if (TCari.getText().equals("")) {
                ps = koneksi.prepareStatement(
                        "SELECT reg_periksa.no_rawat, reg_periksa.no_rkm_medis, pasien.nm_pasien, pasien.tgl_lahir, pasien.jk, asesmen_ulang_hemodialisa_medik.tanggal, "
                        + "asesmen_ulang_hemodialisa_medik.resephd, asesmen_ulang_hemodialisa_medik.hd, asesmen_ulang_hemodialisa_medik.qb, asesmen_ulang_hemodialisa_medik.qd, "
                        + "asesmen_ulang_hemodialisa_medik.uf, asesmen_ulang_hemodialisa_medik.heparinisasi, asesmen_ulang_hemodialisa_medik.heparinisasi_ket, "
                        + "asesmen_ulang_hemodialisa_medik.dialisat, asesmen_ulang_hemodialisa_medik.nip, dokter.nm_dokter FROM reg_periksa INNER JOIN pasien ON "
                        + "reg_periksa.no_rkm_medis = pasien.no_rkm_medis INNER JOIN asesmen_ulang_hemodialisa_medik ON reg_periksa.no_rawat = "
                        + "asesmen_ulang_hemodialisa_medik.no_rawat INNER JOIN dokter ON asesmen_ulang_hemodialisa_medik.nip = dokter.kd_dokter where "
                        + "asesmen_ulang_hemodialisa_medik.tanggal between ? and ? order by asesmen_ulang_hemodialisa_medik.tanggal");
            } else {
                ps = koneksi.prepareStatement(
                        "SELECT reg_periksa.no_rawat, reg_periksa.no_rkm_medis, pasien.nm_pasien, pasien.tgl_lahir, pasien.jk, asesmen_ulang_hemodialisa_medik.tanggal, "
                        + "asesmen_ulang_hemodialisa_medik.resephd, asesmen_ulang_hemodialisa_medik.hd, asesmen_ulang_hemodialisa_medik.qb, asesmen_ulang_hemodialisa_medik.qd, "
                        + "asesmen_ulang_hemodialisa_medik.uf, asesmen_ulang_hemodialisa_medik.heparinisasi, asesmen_ulang_hemodialisa_medik.heparinisasi_ket, "
                        + "asesmen_ulang_hemodialisa_medik.dialisat, asesmen_ulang_hemodialisa_medik.nip, dokter.nm_dokter FROM reg_periksa INNER JOIN pasien ON "
                        + "reg_periksa.no_rkm_medis = pasien.no_rkm_medis INNER JOIN asesmen_ulang_hemodialisa_medik ON reg_periksa.no_rawat = "
                        + "asesmen_ulang_hemodialisa_medik.no_rawat INNER JOIN dokter ON asesmen_ulang_hemodialisa_medik.nip = dokter.kd_dokter where "
                        + "asesmen_ulang_hemodialisa_medik.tanggal between ? and ? and "
                        + "(reg_periksa.no_rawat like ? or pasien.no_rkm_medis like ? or pasien.nm_pasien like ? or "
                        + "asesmen_ulang_hemodialisa_medik.nip like ? or dokter.nm_dokter like ?) "
                        + "order by asesmen_ulang_hemodialisa_medik.tanggal");
            }

            try {
                if (TCari.getText().equals("")) {
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
                    /*
                    "No.Rawat", "No.RM", "Nama Pasien", "J.K.", "Tgl.Lahir", "Tgl.Asuhan",
            "Resep HD", "HD", "QB", "QD", "UF Goal", "Heparinisasi", "Keterangan Heparinisasi", "Dialisat", "Kode Dokter", "Nama Dokter"
                     */

                    tabMode2.addRow(new String[]{rs.getString("no_rawat"), rs.getString("no_rkm_medis"), rs.getString("nm_pasien"),
                        rs.getString("jk"), rs.getString("tgl_lahir"), rs.getString("tanggal"), rs.getString("resephd"),
                        rs.getString("hd"), rs.getString("qb"), rs.getString("qd"), rs.getString("uf"), rs.getString("heparinisasi"),
                        rs.getString("heparinisasi_ket"), rs.getString("dialisat"), rs.getString("nip"), rs.getString("nm_dokter")});

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
            System.out.println("Notifikasi : " + e);
        }
        LCount.setText("" + tabMode2.getRowCount());
    }

    private void tampil3() {
        Valid.tabelKosong(tabMode3);
        try {
            if (TCari.getText().equals("")) {
                ps = koneksi.prepareStatement(
                        "SELECT reg_periksa.no_rawat, reg_periksa.no_rkm_medis, pasien.nm_pasien, pasien.jk,pasien.tgl_lahir, asesmen_ulang_hemodialisa_observasi.tgl_perawatan, "
                        + "asesmen_ulang_hemodialisa_observasi.jam_rawat, asesmen_ulang_hemodialisa_observasi.observasi, asesmen_ulang_hemodialisa_observasi.qb, "
                        + "asesmen_ulang_hemodialisa_observasi.uf, asesmen_ulang_hemodialisa_observasi.td, asesmen_ulang_hemodialisa_observasi.nadi, "
                        + "asesmen_ulang_hemodialisa_observasi.suhu, asesmen_ulang_hemodialisa_observasi.respirasi, asesmen_ulang_hemodialisa_observasi.vp, "
                        + "asesmen_ulang_hemodialisa_observasi.ap, asesmen_ulang_hemodialisa_observasi.tmp, asesmen_ulang_hemodialisa_observasi.keterangan, "
                        + "asesmen_ulang_hemodialisa_observasi.nip, petugas.nama FROM reg_periksa INNER JOIN pasien ON reg_periksa.no_rkm_medis = pasien.no_rkm_medis "
                        + "INNER JOIN asesmen_ulang_hemodialisa_observasi ON reg_periksa.no_rawat = asesmen_ulang_hemodialisa_observasi.no_rawat INNER JOIN petugas ON "
                        + "asesmen_ulang_hemodialisa_observasi.nip = petugas.nip where "
                        + "asesmen_ulang_hemodialisa_observasi.tgl_perawatan between ? and ? order by asesmen_ulang_hemodialisa_observasi.tgl_perawatan");
            } else {
                ps = koneksi.prepareStatement(
                        "SELECT reg_periksa.no_rawat, reg_periksa.no_rkm_medis, pasien.nm_pasien, pasien.jk, pasien.tgl_lahir,asesmen_ulang_hemodialisa_observasi.tgl_perawatan, "
                        + "asesmen_ulang_hemodialisa_observasi.jam_rawat, asesmen_ulang_hemodialisa_observasi.observasi, asesmen_ulang_hemodialisa_observasi.qb, "
                        + "asesmen_ulang_hemodialisa_observasi.uf, asesmen_ulang_hemodialisa_observasi.td, asesmen_ulang_hemodialisa_observasi.nadi, "
                        + "asesmen_ulang_hemodialisa_observasi.suhu, asesmen_ulang_hemodialisa_observasi.respirasi, asesmen_ulang_hemodialisa_observasi.vp, "
                        + "asesmen_ulang_hemodialisa_observasi.ap, asesmen_ulang_hemodialisa_observasi.tmp, asesmen_ulang_hemodialisa_observasi.keterangan, "
                        + "asesmen_ulang_hemodialisa_observasi.nip, petugas.nama FROM reg_periksa INNER JOIN pasien ON reg_periksa.no_rkm_medis = pasien.no_rkm_medis "
                        + "INNER JOIN asesmen_ulang_hemodialisa_observasi ON reg_periksa.no_rawat = asesmen_ulang_hemodialisa_observasi.no_rawat INNER JOIN petugas ON "
                        + "asesmen_ulang_hemodialisa_observasi.nip = petugas.nip where "
                        + "asesmen_ulang_hemodialisa_observasi.tgl_perawatan between ? and ? and "
                        + "(reg_periksa.no_rawat like ? or pasien.no_rkm_medis like ? or pasien.nm_pasien like ? or "
                        + "asesmen_ulang_hemodialisa_observasi.nip like ? or petugas.nama like ?) "
                        + "order by asesmen_ulang_hemodialisa_observasi.tgl_perawatan");
            }

            try {
                if (TCari.getText().equals("")) {
                    ps.setString(1, Valid.SetTgl(DTPCari1.getSelectedItem() + ""));
                    ps.setString(2, Valid.SetTgl(DTPCari2.getSelectedItem() + ""));
                } else {
                    ps.setString(1, Valid.SetTgl(DTPCari1.getSelectedItem() + ""));
                    ps.setString(2, Valid.SetTgl(DTPCari2.getSelectedItem() + ""));
                    ps.setString(3, "%" + TCari.getText() + "%");
                    ps.setString(4, "%" + TCari.getText() + "%");
                    ps.setString(5, "%" + TCari.getText() + "%");
                    ps.setString(6, "%" + TCari.getText() + "%");
                    ps.setString(7, "%" + TCari.getText() + "%");
                }
                rs = ps.executeQuery();
                while (rs.next()) {
                    /*
        "No.Rawat", "No.RM", "Nama Pasien", "J.K.","Tgl.Lahir",
            "Tanggal Perawatan", "Jam Perawatan", "Observasi", "QB", "UF Rate", "TD", "Nadi", "Suhu",
            "Respirasi", "VP", "AP", "TMP", "Keterangan Lain", "Kode Perawat", "Nama Perawat"
                     */

                    tabMode3.addRow(new String[]{rs.getString("no_rawat"), rs.getString("no_rkm_medis"), rs.getString("nm_pasien"),
                        rs.getString("jk"), rs.getString("tgl_lahir"), rs.getString("tgl_perawatan"), rs.getString("jam_rawat"),
                        rs.getString("observasi"), rs.getString("qb"), rs.getString("uf"), rs.getString("td"), rs.getString("nadi"),
                        rs.getString("suhu"), rs.getString("respirasi"), rs.getString("vp"), rs.getString("ap"), rs.getString("tmp"),
                        rs.getString("keterangan"), rs.getString("nip"), rs.getString("nama")});

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
            System.out.println("Notifikasi : " + e);
        }
        LCount.setText("" + tabMode3.getRowCount());

    }

    private void tampil4() {

    }

    public void emptTeks() {
        switch (TabRawat.getSelectedIndex()) {
            case 0:
                TglAsuhan.setDate(new Date());
                Informasi.setSelectedIndex(0);
                KualitasNyeriKet.setText("");
                FaktorPemberatKet.setText("");
                FaktoPeringanKet.setText("");
                Dializer.setText("");
                SifatNyeriKet.setText("");
                EfekNyeriKet.setText("");
                DiagnosaMedis.setText("");
                TerapiKe.setText("");
                Evaluasi.setText("");
                Uf.setText("");
                KdDokter.setText("");
                NmDokter.setText("");
                Alergi.setText("");
                IntensitasNyeri.setSelectedIndex(0);
                LokasiNyeri.setText("");
                NyeriPasien.setSelectedIndex(0);
                Kapan.setText("");
                FaktorPemberat.setSelectedIndex(0);
                SifatNyeri.setSelectedIndex(0);
                KuKet.setText("");
                FaktorPeringan.setSelectedIndex(0);
                EfekNyeri.setSelectedIndex(0);
                RespirasiKet.setText("");
                Respirasi.setSelectedIndex(0);
                KualitasNyeri.setSelectedIndex(0);
                BbPostHD.setText("");
                Nadi.setSelectedIndex(0);
                JalurKet.setText("");
                Ku.setSelectedIndex(0);
                Konjungtiva.setSelectedIndex(0);
                Ektremitas.setSelectedIndex(0);
                AksesVaskular.setSelectedIndex(0);
                Jalur.setSelectedIndex(0);
                BbKering.setText("");
                ATS1.setSelectedIndex(0);
                Dialisat.setSelectedIndex(0);
                BJM1.setSelectedIndex(0);
                Detik.setSelectedIndex(0);
                BbLalu.setText("0");
                HasilJatuh.setSelectedIndex(0);
                Heparinisasi.setSelectedIndex(0);
                IntervensiJatuh.setSelectedIndex(0);
                Qb.setText("");
                BbPreHD.setText("");
                MSA1.setSelectedIndex(0);
                resepHd.setSelectedIndex(0);
                Menit.setSelectedIndex(0);
                Qd.setText("");
                IntervensiKolaborasi.setText("");
                for (i = 0; i < tabModeMasalah.getRowCount(); i++) {
                    tabModeMasalah.setValueAt(false, i, 0);
                }
                Valid.tabelKosong(tabModeRencana);
                TabRawat.setSelectedIndex(0);
                Informasi.requestFocus();
                break;
            case 2:
                TNoRw.setText("");
                TNoRM.setText("");
                TPasien.setText("");
                TglLahir.setText("");
                Jk.setText("");
                resepHd.setSelectedIndex(0);
                Hd.setText("");
                Qb.setText("");
                Qd.setText("");
                Uf.setText("");
                Heparinisasi.setSelectedIndex(0);
                Heparinisasi_Ket.setText("");
                Dialisat.setSelectedIndex(0);
                KdDokter.setText("");
                NmDokter.setText("");
                break;
            case 3:

                Observasi.setText("");
                Qb2.setText("");
                Uf2.setText("");
                Td2.setText("");
                Nadi2.setText("");
                Suhu.setText("");
                Respirasi2.setText("");
                Vp.setText("");
                Ap.setText("");
                Tmp.setText("");
                Ket_Lain.setText("");
                NIP.setText(iyem);
                NamaPetugas.setText("");
                break;
            case 4:
                TNoRw.setText("");
                TNoRM.setText("");
                TPasien.setText("");
                TglLahir.setText("");
                Jk.setText("");
                Evaluasi.setText("");
                NIP1.setText("");
                NamaPetugas1.setText("");
                break;
        }

    }

    private void getData() {
        if (tbObat.getSelectedRow() != -1) {
            TNoRw.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 0).toString());
            TNoRM.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 1).toString());
            TPasien.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 2).toString());
            TglLahir.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 3).toString());
            Jk.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 4).toString());
            KdPetugas.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 5).toString());
            NmPetugas.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 6).toString());
            //     TglAsuhan.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 7).toString());
            Informasi.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 8).toString());
            DiagnosaMedis.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 9).toString());
            TerapiKe.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 10).toString());
            Dializer.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 11).toString());
            Alergi.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 12).toString());
            NyeriPasien.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 13).toString());
            IntensitasNyeri.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 14).toString());
            LokasiNyeri.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 15).toString());
            Kapan.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 16).toString());
            SifatNyeri.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 17).toString());
            SifatNyeriKet.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 18).toString());
            KualitasNyeri.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 19).toString());
            KualitasNyeriKet.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 20).toString());
            FaktorPemberat.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 21).toString());
            FaktorPemberatKet.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 22).toString());
            FaktorPeringan.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 23).toString());
            FaktoPeringanKet.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 24).toString());
            EfekNyeri.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 25).toString());
            EfekNyeriKet.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 26).toString());
            Ku.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 27).toString());
            KuKet.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 28).toString());
            Td.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 29).toString());
            Nadi.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 30).toString());
            NadiKet.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 31).toString());
            Respirasi.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 32).toString());
            RespirasiKet.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 33).toString());
            Konjungtiva.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 34).toString());
            KonjungtivaKet.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 35).toString());
            Ektremitas.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 36).toString());
            BbKering.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 37).toString());
            BbPreHD.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 38).toString());
            BbLalu.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 39).toString());
            BbPostHD.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 40).toString());
            AksesVaskular.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 41).toString());
            Jalur.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 42).toString());
            JalurKet.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 43).toString());
            ATS1.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 44).toString());
            BJM1.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 45).toString());
            MSA1.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 46).toString());
            HasilJatuh.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 47).toString());
            IntervensiJatuh.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 48).toString());
            IntervensiKolaborasi.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 49).toString());
            Valid.tabelKosong(tabModeMasalah);
            Valid.tabelKosong(tabModeRencana);
            for (i = 0; i < tbMasalahDetail.getRowCount(); i++) {
                tabModeMasalah.addRow(new Object[]{
                    true, tbMasalahDetail.getValueAt(i, 0).toString(), tbMasalahDetail.getValueAt(i, 1).toString()
                });
            }
            for (i = 0; i < tbRencanaDetail.getRowCount(); i++) {
                tabModeRencana.addRow(new Object[]{
                    true, tbRencanaDetail.getValueAt(i, 0).toString(), tbRencanaDetail.getValueAt(i, 1).toString()
                });
            }
            Valid.SetTgl2(TglAsuhan, tbObat.getValueAt(tbObat.getSelectedRow(), 7).toString());
        }
    }

    private void getData2() {
        if (tbObat1.getSelectedRow() != -1) {
            /*
            "No.Rawat", "No.RM", "Nama Pasien", "J.K.", "Tgl.Asuhan",
            "Resep HD", "HD", "QB", "QD", "UF Goal", "Heparinisasi", "Keterangan Heparinisasi", "Dialisat", "Kode Dokter", "Nama Dokter"
             */

            TNoRw.setText(tbObat1.getValueAt(tbObat1.getSelectedRow(), 0).toString());
            TNoRM.setText(tbObat1.getValueAt(tbObat1.getSelectedRow(), 1).toString());
            TPasien.setText(tbObat1.getValueAt(tbObat1.getSelectedRow(), 2).toString());
            TglLahir.setText(tbObat1.getValueAt(tbObat1.getSelectedRow(), 3).toString());
            Jk.setText(tbObat1.getValueAt(tbObat1.getSelectedRow(), 4).toString());
            resepHd.setSelectedItem(tbObat1.getValueAt(tbObat1.getSelectedRow(), 6).toString());
            Hd.setText(tbObat1.getValueAt(tbObat1.getSelectedRow(), 7).toString());
            Qb.setText(tbObat1.getValueAt(tbObat1.getSelectedRow(), 8).toString());
            Qd.setText(tbObat1.getValueAt(tbObat1.getSelectedRow(), 9).toString());
            Uf.setText(tbObat1.getValueAt(tbObat1.getSelectedRow(), 10).toString());
            Heparinisasi.setSelectedItem(tbObat1.getValueAt(tbObat1.getSelectedRow(), 11).toString());
            Heparinisasi_Ket.setText(tbObat1.getValueAt(tbObat1.getSelectedRow(), 12).toString());
            Dialisat.setSelectedItem(tbObat1.getValueAt(tbObat1.getSelectedRow(), 13).toString());
            KdPetugas.setText(tbObat1.getValueAt(tbObat1.getSelectedRow(), 14).toString());
            NmPetugas.setText(tbObat1.getValueAt(tbObat1.getSelectedRow(), 15).toString());
            Valid.SetTgl2(TglAsuhan, tbObat1.getValueAt(tbObat1.getSelectedRow(), 5).toString());
        }
    }

    private void isRawat() {
        try {
            ps = koneksi.prepareStatement(
                    "select reg_periksa.no_rkm_medis,pasien.nm_pasien, if(pasien.jk='L','Laki-Laki','Perempuan') as jk,"
                    + "pasien.tgl_lahir,pasien.agama,bahasa_pasien.nama_bahasa,cacat_fisik.nama_cacat,reg_periksa.tgl_registrasi,"
                    + "reg_periksa.jam_reg from reg_periksa inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "
                    + "inner join bahasa_pasien on bahasa_pasien.id=pasien.bahasa_pasien "
                    + "inner join cacat_fisik on cacat_fisik.id=pasien.cacat_fisik "
                    + "where reg_periksa.no_rawat=?");
            try {
                ps.setString(1, TNoRw.getText());
                rs = ps.executeQuery();
                if (rs.next()) {
                    TNoRM.setText(rs.getString("no_rkm_medis"));
                    TPasien.setText(rs.getString("nm_pasien"));
                    DTPCari1.setDate(rs.getDate("tgl_registrasi"));
                    Jk.setText(rs.getString("jk"));
                    TglLahir.setText(rs.getString("tgl_lahir"));
//                    Td.setText(rs.getString("agama"));
//                    NadiKet.setText(rs.getString("nama_bahasa"));
//                    KonjungtivaKet.setText(rs.getString("nama_cacat"));
                    TanggalRegistrasi.setText(rs.getString("tgl_registrasi") + " " + rs.getString("jam_reg"));
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
    }

    public void isCek() {
        BtnSimpan.setEnabled(akses.gethemodialisa());
        BtnHapus.setEnabled(akses.gethemodialisa());
        BtnEdit.setEnabled(akses.gethemodialisa());
        BtnEdit.setEnabled(akses.gethemodialisa());
        BtnTambahMasalah.setEnabled(akses.getmaster_masalah_keperawatan());
        BtnTambahRencana.setEnabled(akses.getmaster_rencana_keperawatan());
        if (akses.getjml2() >= 1) {
            KdPetugas.setEditable(false);
            BtnDokter.setEnabled(false);
            KdPetugas.setText(akses.getkode());
            NmPetugas.setText(petugas.tampil3(KdPetugas.getText()));
            if (NmPetugas.getText().equals("")) {
                KdPetugas.setText("");
                JOptionPane.showMessageDialog(null, "User login bukan petugas...!!");
            }
        }

        if (TANGGALMUNDUR.equals("no")) {
            if (!akses.getkode().equals("Admin Utama")) {
                TglAsuhan.setEditable(false);
                TglAsuhan.setEnabled(false);
            }
        }
    }

    public void setTampil() {
        TabRawat.setSelectedIndex(1);
    }

    private void tampilMasalah() {
        try {
            Valid.tabelKosong(tabModeMasalah);
            file = new File("./cache/masalahkeperawatan.iyem");
            file.createNewFile();
            fileWriter = new FileWriter(file);
            iyem = "";
            ps = koneksi.prepareStatement("select * from master_masalah_keperawatan order by master_masalah_keperawatan.kode_masalah");
            try {
                rs = ps.executeQuery();
                while (rs.next()) {
                    tabModeMasalah.addRow(new Object[]{false, rs.getString(1), rs.getString(2)});
                    iyem = iyem + "{\"KodeMasalah\":\"" + rs.getString(1) + "\",\"NamaMasalah\":\"" + rs.getString(2) + "\"},";
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
            fileWriter.write("{\"masalahkeperawatan\":[" + iyem.substring(0, iyem.length() - 1) + "]}");
            fileWriter.flush();
            fileWriter.close();
            iyem = null;
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }
    }

    private void tampilMasalah2() {
        try {
            jml = 0;
            for (i = 0; i < tbMasalahKeperawatan.getRowCount(); i++) {
                if (tbMasalahKeperawatan.getValueAt(i, 0).toString().equals("true")) {
                    jml++;
                }
            }

            pilih = null;
            pilih = new boolean[jml];
            kode = null;
            kode = new String[jml];
            masalah = null;
            masalah = new String[jml];

            index = 0;
            for (i = 0; i < tbMasalahKeperawatan.getRowCount(); i++) {
                if (tbMasalahKeperawatan.getValueAt(i, 0).toString().equals("true")) {
                    pilih[index] = true;
                    kode[index] = tbMasalahKeperawatan.getValueAt(i, 1).toString();
                    masalah[index] = tbMasalahKeperawatan.getValueAt(i, 2).toString();
                    index++;
                }
            }

            Valid.tabelKosong(tabModeMasalah);

            for (i = 0; i < jml; i++) {
                tabModeMasalah.addRow(new Object[]{
                    pilih[i], kode[i], masalah[i]
                });
            }

            myObj = new FileReader("./cache/masalahkeperawatan.iyem");
            root = mapper.readTree(myObj);
            response = root.path("masalahkeperawatan");
            if (response.isArray()) {
                for (JsonNode list : response) {
                    if (list.path("KodeMasalah").asText().toLowerCase().contains(TCariMasalah.getText().toLowerCase()) || list.path("NamaMasalah").asText().toLowerCase().contains(TCariMasalah.getText().toLowerCase())) {
                        tabModeMasalah.addRow(new Object[]{
                            false, list.path("KodeMasalah").asText(), list.path("NamaMasalah").asText()
                        });
                    }
                }
            }
            myObj.close();
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }
    }

    private void tampilPenyulit() {
        try {
            Valid.tabelKosong(tabModePenyulit);
            file = new File("./cache/penyulithd.iyem");
            file.createNewFile();
            fileWriter = new FileWriter(file);
            iyem = "";
            ps = koneksi.prepareStatement("select * from master_penyulit_hd order by master_penyulit_hd.kode_penyulit");
            try {
                rs = ps.executeQuery();
                while (rs.next()) {
                    tabModePenyulit.addRow(new Object[]{false, rs.getString(1), rs.getString(2)});
                    iyem = iyem + "{\"KodePenyulit\":\"" + rs.getString(1) + "\",\"NamaPenyulit\":\"" + rs.getString(2) + "\"},";
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
            fileWriter.write("{\"penyulithd\":[" + iyem.substring(0, iyem.length() - 1) + "]}");
            fileWriter.flush();
            fileWriter.close();
            iyem = null;
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }
    }

    private void tampilPenyulit2() {
        try {
            jml = 0;
            for (i = 0; i < tbPenyulit.getRowCount(); i++) {
                if (tbPenyulit.getValueAt(i, 0).toString().equals("true")) {
                    jml++;
                }
            }

            pilih = null;
            pilih = new boolean[jml];
            kode = null;
            kode = new String[jml];
            masalah = null;
            masalah = new String[jml];

            index = 0;
            for (i = 0; i < tbPenyulit.getRowCount(); i++) {
                if (tbPenyulit.getValueAt(i, 0).toString().equals("true")) {
                    pilih[index] = true;
                    kode[index] = tbPenyulit.getValueAt(i, 1).toString();
                    masalah[index] = tbPenyulit.getValueAt(i, 2).toString();
                    index++;
                }
            }

            Valid.tabelKosong(tabModePenyulit);

            for (i = 0; i < jml; i++) {
                tabModePenyulit.addRow(new Object[]{
                    pilih[i], kode[i], masalah[i]
                });
            }

            myObj = new FileReader("./cache/penyulithd.iyem");
            root = mapper.readTree(myObj);
            response = root.path("penyulithd");
            if (response.isArray()) {
                for (JsonNode list : response) {
                    if (list.path("KodePenyulit").asText().toLowerCase().contains(TCariPenyulit.getText().toLowerCase()) || list.path("NamaPenyulit").asText().toLowerCase().contains(TCariPenyulit.getText().toLowerCase())) {
                        tabModePenyulit.addRow(new Object[]{
                            false, list.path("KodePenyulit").asText(), list.path("NamaPenyulit").asText()
                        });
                    }
                }
            }
            myObj.close();
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }
    }

    private void tampilRencana() {
        try {
            file = new File("./cache/rencanakeperawatan.iyem");
            file.createNewFile();
            fileWriter = new FileWriter(file);
            iyem = "";
            ps = koneksi.prepareStatement("select * from master_rencana_keperawatan order by master_rencana_keperawatan.kode_rencana");
            try {
                rs = ps.executeQuery();
                while (rs.next()) {
                    iyem = iyem + "{\"KodeMasalah\":\"" + rs.getString(1) + "\",\"KodeRencana\":\"" + rs.getString(2) + "\",\"NamaRencana\":\"" + rs.getString(3) + "\"},";
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
            fileWriter.write("{\"rencanakeperawatan\":[" + iyem.substring(0, iyem.length() - 1) + "]}");
            fileWriter.flush();
            fileWriter.close();
            iyem = null;
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }
    }

    private void tampilRencana2() {
        try {
            jml = 0;
            for (i = 0; i < tbRencanaKeperawatan.getRowCount(); i++) {
                if (tbRencanaKeperawatan.getValueAt(i, 0).toString().equals("true")) {
                    jml++;
                }
            }

            pilih = null;
            pilih = new boolean[jml];
            kode = null;
            kode = new String[jml];
            masalah = null;
            masalah = new String[jml];

            index = 0;
            for (i = 0; i < tbRencanaKeperawatan.getRowCount(); i++) {
                if (tbRencanaKeperawatan.getValueAt(i, 0).toString().equals("true")) {
                    pilih[index] = true;
                    kode[index] = tbRencanaKeperawatan.getValueAt(i, 1).toString();
                    masalah[index] = tbRencanaKeperawatan.getValueAt(i, 2).toString();
                    index++;
                }
            }

            Valid.tabelKosong(tabModeRencana);

            for (i = 0; i < jml; i++) {
                tabModeRencana.addRow(new Object[]{
                    pilih[i], kode[i], masalah[i]
                });
            }

            myObj = new FileReader("./cache/rencanakeperawatan.iyem");
            root = mapper.readTree(myObj);
            response = root.path("rencanakeperawatan");
            if (response.isArray()) {
                for (i = 0; i < tbMasalahKeperawatan.getRowCount(); i++) {
                    if (tbMasalahKeperawatan.getValueAt(i, 0).toString().equals("true")) {
                        for (JsonNode list : response) {
                            if (list.path("KodeMasalah").asText().toLowerCase().equals(tbMasalahKeperawatan.getValueAt(i, 1).toString())
                                    && list.path("NamaRencana").asText().toLowerCase().contains(TCariRencana.getText().toLowerCase())) {
                                tabModeRencana.addRow(new Object[]{
                                    false, list.path("KodeRencana").asText(), list.path("NamaRencana").asText()
                                });
                            }
                        }
                    }
                }
            }
            myObj.close();
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }
    }

    private void isMenu() {
        if (ChkAccor.isSelected() == true) {
            ChkAccor.setVisible(false);
            PanelAccor.setPreferredSize(new Dimension(470, HEIGHT));
            FormMenu.setVisible(true);
            FormMasalahRencana.setVisible(true);
            ChkAccor.setVisible(true);
        } else if (ChkAccor.isSelected() == false) {
            ChkAccor.setVisible(false);
            PanelAccor.setPreferredSize(new Dimension(15, HEIGHT));
            FormMenu.setVisible(false);
            FormMasalahRencana.setVisible(false);
            ChkAccor.setVisible(true);
        }
    }

    private void getMasalah() {
        if (tbObat.getSelectedRow() != -1) {
            TNoRM1.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 1).toString());
            TPasien1.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 2).toString());
            DetailRencana.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 62).toString());
            try {
                Valid.tabelKosong(tabModeDetailMasalah);
                ps = koneksi.prepareStatement(
                        "select master_masalah_keperawatan.kode_masalah,master_masalah_keperawatan.nama_masalah from master_masalah_keperawatan "
                        + "inner join asesmen_ulang_hemodialisa_masalah on asesmen_ulang_hemodialisa_masalah.kode_masalah=master_masalah_keperawatan.kode_masalah "
                        + "where asesmen_ulang_hemodialisa_masalah.no_rawat=? order by asesmen_ulang_hemodialisa_masalah.kode_masalah");
                try {
                    ps.setString(1, tbObat.getValueAt(tbObat.getSelectedRow(), 0).toString());
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        tabModeDetailMasalah.addRow(new Object[]{rs.getString(1), rs.getString(2)});
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

            try {
                Valid.tabelKosong(tabModeDetailRencana);
                ps = koneksi.prepareStatement(
                        "select master_rencana_keperawatan.kode_rencana,master_rencana_keperawatan.rencana_keperawatan from master_rencana_keperawatan "
                        + "inner join asesmen_ulang_hemodialisa_rencana on asesmen_ulang_hemodialisa_rencana.kode_rencana=master_rencana_keperawatan.kode_rencana "
                        + "where asesmen_ulang_hemodialisa_rencana.no_rawat=? order by asesmen_ulang_hemodialisa_rencana.kode_rencana");
                try {
                    ps.setString(1, tbObat.getValueAt(tbObat.getSelectedRow(), 0).toString());
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        tabModeDetailRencana.addRow(new Object[]{rs.getString(1), rs.getString(2)});
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
    }

    private void isBMI() {
        if ((!DiagnosaMedis.getText().equals("")) && (!EfekNyeriKet.getText().equals(""))) {
            try {
                TerapiKe.setText(Valid.SetAngka8(Valid.SetAngka(EfekNyeriKet.getText()) / ((Valid.SetAngka(DiagnosaMedis.getText()) / 100) * (Valid.SetAngka(DiagnosaMedis.getText()) / 100)), 1) + "");
            } catch (Exception e) {
                TerapiKe.setText("");
            }
        }
    }

    private void hapus() {
        switch (TabRawat.getSelectedIndex()) {
            case 0:
                if (Sequel.queryu2tf("delete from asesmen_ulang_hemodialisa where no_rawat=?", 1, new String[]{
                    tbObat.getValueAt(tbObat.getSelectedRow(), 0).toString()
                }) == true) {
                    TNoRM1.setText("");
                    TPasien1.setText("");
                    Valid.tabelKosong(tabModeDetailMasalah);
                    Valid.tabelKosong(tabModeDetailRencana);
                    ChkAccor.setSelected(false);
                    isMenu();
                    tabMode.removeRow(tbObat.getSelectedRow());
                    LCount.setText("" + tabMode.getRowCount());
                } else {
                    JOptionPane.showMessageDialog(null, "Gagal menghapus..!!");
                }
                break;
            case 2:
                if (Sequel.queryu2tf("delete from asesmen_ulang_hemodialisa_medik where no_rawat=?", 1, new String[]{
                    tbObat1.getValueAt(tbObat1.getSelectedRow(), 0).toString()
                }) == true) {
                    Hd.setText("");
                    Qb.setText("");
                    Qd.setText("");
                    Uf.setText("");
                    Heparinisasi_Ket.setText("");
                    tabMode2.removeRow(tbObat1.getSelectedRow());
                    LCount.setText("" + tabMode2.getRowCount());
                } else {
                    JOptionPane.showMessageDialog(null, "Gagal menghapus..!!");
                }
                break;
            case 3:
                if (Sequel.queryu2tf("delete from asesmen_ulang_hemodialisa_observasi where no_rawat=? and tgl_perawatan = ? and jam_rawat = ?", 3, new String[]{
                    tbObat2.getValueAt(tbObat2.getSelectedRow(), 0).toString(), tbObat2.getValueAt(tbObat2.getSelectedRow(), 5).toString(), tbObat2.getValueAt(tbObat2.getSelectedRow(), 6).toString()
                }) == true) {
                    Observasi.setText("");
                    Qb2.setText("");
                    Uf2.setText("");
                    Td2.setText("");
                    Nadi2.setText("");
                    Suhu.setText("");
                    Respirasi2.setText("");
                    Vp.setText("");
                    Ap.setText("");
                    Tmp.setText("");
                    Ket_Lain.setText("");
                    tabMode3.removeRow(tbObat2.getSelectedRow());
                    LCount.setText("" + tabMode3.getRowCount());
                } else {
                    JOptionPane.showMessageDialog(null, "Gagal menghapus..!!");
                }
                break;
            case 4:
                if (Sequel.queryu2tf("delete from aasesmen_ulang_hemodialisa_evaluasi where no_rawat=?", 1, new String[]{
                    tbObat1.getValueAt(tbObat1.getSelectedRow(), 0).toString()
                }) == true) {
                    Evaluasi.setText("");
                    NIP1.setText("");
                    NamaPetugas1.setText("");
                    tabMode2.removeRow(tbObat1.getSelectedRow());
                    LCount.setText("" + tabMode2.getRowCount());
                } else {
                    JOptionPane.showMessageDialog(null, "Gagal menghapus..!!");
                }
                break;
        }
    }

    private void ganti() {
        switch (TabRawat.getSelectedIndex()) {
            case 0:
                if (Sequel.mengedittf("asesmen_ulang_hemodialisa", "no_rawat=?", "no_rawat=?,nip=?,tanggal=?,informasi=?,diagnosa=?,terapike=?,dializer=?,alergi=?,penilaian_nyeri=?,intensitas_nyeri=?,lokasi_nyeri=?,kapan=?,sifat_nyeri=?,"
                        + "sifat_nyeriket=?,kualitas_nyeri=?,kualitas_nyeriket=?,pemberat=?,pemberat_ket=?,peringan=?,peringanket=?,efek_nyeri=?,efek_nyeriket=?,ku=?,ku_ket=?,td=?,nadi=?,frekuensi_nadi=?,respirasi=?,rr=?,konjungtiva=?,konjungtivaket=?,ekstremitas=?,"
                        + "bb_kering=?,bb_prehd=?,bb_lalu=?,bb_posthd=?,akses=?,akses_jalur=?,aksesket=?,berjalan_a=?,berjalan_b=?,berjalan_c=?,hasil=?,intervensi_jatuh =?,intervensi_kolaborasi=?", 46, new String[]{
                            TNoRw.getText(), KdPetugas.getText(), Valid.SetTgl(TglAsuhan.getSelectedItem() + "") + " " + TglAsuhan.getSelectedItem().toString().substring(11, 19), Informasi.getSelectedItem().toString(), DiagnosaMedis.getText(),
                            TerapiKe.getText(), Dializer.getText(), Alergi.getText(), NyeriPasien.getSelectedItem().toString(), IntensitasNyeri.getSelectedItem().toString(), LokasiNyeri.getText(), Kapan.getText(),
                            SifatNyeri.getSelectedItem().toString(), SifatNyeriKet.getText(), KualitasNyeri.getSelectedItem().toString(), KualitasNyeriKet.getText(), FaktorPemberat.getSelectedItem().toString(),
                            FaktorPemberatKet.getText(), FaktorPeringan.getSelectedItem().toString(), FaktoPeringanKet.getText(), EfekNyeri.getSelectedItem().toString(), EfekNyeriKet.getText(), Ku.getSelectedItem().toString(),
                            KuKet.getText(), Td.getText(), Nadi.getSelectedItem().toString(), NadiKet.getText(), Respirasi.getSelectedItem().toString(), RespirasiKet.getText(), Konjungtiva.getSelectedItem().toString(),
                            KonjungtivaKet.getText(), Ektremitas.getSelectedItem().toString(), BbKering.getText(), BbPreHD.getText(), BbLalu.getText(), BbPostHD.getText(), AksesVaskular.getSelectedItem().toString(),
                            Jalur.getSelectedItem().toString(), JalurKet.getText(), ATS1.getSelectedItem().toString(), BJM1.getSelectedItem().toString(), MSA1.getSelectedItem().toString(), HasilJatuh.getSelectedItem().toString(),
                            IntervensiJatuh.getSelectedItem().toString(), IntervensiKolaborasi.getText(), KdPetugas.getText(), tbObat.getValueAt(tbObat.getSelectedRow(), 0).toString()
                        }) == true) {
                    tbObat.setValueAt(TNoRw.getText(), tbObat.getSelectedRow(), 0);
                    tbObat.setValueAt(TNoRM.getText(), tbObat.getSelectedRow(), 1);
                    tbObat.setValueAt(TPasien.getText(), tbObat.getSelectedRow(), 2);
                    tbObat.setValueAt(TglLahir.getText(), tbObat.getSelectedRow(), 3);
                    tbObat.setValueAt(Jk.getText(), tbObat.getSelectedRow(), 4);
                    tbObat.setValueAt(KdPetugas.getText(), tbObat.getSelectedRow(), 5);
                    tbObat.setValueAt(NmPetugas.getText(), tbObat.getSelectedRow(), 6);
                    tbObat.setValueAt(Valid.SetTgl(TglAsuhan.getSelectedItem() + "") + " " + TglAsuhan.getSelectedItem().toString().substring(11, 19), tbObat.getSelectedRow(), 7);
                    tbObat.setValueAt(Informasi.getSelectedItem().toString(), tbObat.getSelectedRow(), 8);
                    tbObat.setValueAt(DiagnosaMedis.getText(), tbObat.getSelectedRow(), 9);
                    tbObat.setValueAt(TerapiKe.getText(), tbObat.getSelectedRow(), 10);
                    tbObat.setValueAt(Dializer.getText(), tbObat.getSelectedRow(), 11);
                    tbObat.setValueAt(Alergi.getText(), tbObat.getSelectedRow(), 12);
                    tbObat.setValueAt(NyeriPasien.getSelectedItem().toString(), tbObat.getSelectedRow(), 13);
                    tbObat.setValueAt(IntensitasNyeri.getSelectedItem().toString(), tbObat.getSelectedRow(), 14);
                    tbObat.setValueAt(LokasiNyeri.getText(), tbObat.getSelectedRow(), 15);
                    tbObat.setValueAt(Kapan.getText(), tbObat.getSelectedRow(), 16);
                    tbObat.setValueAt(SifatNyeri.getSelectedItem().toString(), tbObat.getSelectedRow(), 17);
                    tbObat.setValueAt(SifatNyeriKet.getText(), tbObat.getSelectedRow(), 18);
                    tbObat.setValueAt(KualitasNyeri.getSelectedItem().toString(), tbObat.getSelectedRow(), 19);
                    tbObat.setValueAt(KualitasNyeriKet.getText(), tbObat.getSelectedRow(), 20);
                    tbObat.setValueAt(FaktorPemberat.getSelectedItem().toString(), tbObat.getSelectedRow(), 21);
                    tbObat.setValueAt(FaktorPemberatKet.getText(), tbObat.getSelectedRow(), 22);
                    tbObat.setValueAt(FaktorPeringan.getSelectedItem().toString(), tbObat.getSelectedRow(), 23);
                    tbObat.setValueAt(FaktoPeringanKet.getText(), tbObat.getSelectedRow(), 24);
                    tbObat.setValueAt(EfekNyeri.getSelectedItem().toString(), tbObat.getSelectedRow(), 25);
                    tbObat.setValueAt(EfekNyeriKet.getText(), tbObat.getSelectedRow(), 26);
                    tbObat.setValueAt(Ku.getSelectedItem().toString(), tbObat.getSelectedRow(), 27);
                    tbObat.setValueAt(KuKet.getText(), tbObat.getSelectedRow(), 28);
                    tbObat.setValueAt(Td.getText(), tbObat.getSelectedRow(), 29);
                    tbObat.setValueAt(Nadi.getSelectedItem().toString(), tbObat.getSelectedRow(), 30);
                    tbObat.setValueAt(NadiKet.getText(), tbObat.getSelectedRow(), 31);
                    tbObat.setValueAt(Respirasi.getSelectedItem().toString(), tbObat.getSelectedRow(), 32);
                    tbObat.setValueAt(RespirasiKet.getText(), tbObat.getSelectedRow(), 33);
                    tbObat.setValueAt(Konjungtiva.getSelectedItem().toString(), tbObat.getSelectedRow(), 34);
                    tbObat.setValueAt(KonjungtivaKet.getText(), tbObat.getSelectedRow(), 35);
                    tbObat.setValueAt(Ektremitas.getSelectedItem().toString(), tbObat.getSelectedRow(), 36);
                    tbObat.setValueAt(BbKering.getText(), tbObat.getSelectedRow(), 37);
                    tbObat.setValueAt(BbPreHD.getText(), tbObat.getSelectedRow(), 38);
                    tbObat.setValueAt(BbLalu.getText(), tbObat.getSelectedRow(), 39);
                    tbObat.setValueAt(BbPostHD.getText(), tbObat.getSelectedRow(), 40);
                    tbObat.setValueAt(AksesVaskular.getSelectedItem().toString(), tbObat.getSelectedRow(), 41);
                    tbObat.setValueAt(Jalur.getSelectedItem().toString(), tbObat.getSelectedRow(), 42);
                    tbObat.setValueAt(JalurKet.getText(), tbObat.getSelectedRow(), 43);
                    tbObat.setValueAt(ATS1.getSelectedItem().toString(), tbObat.getSelectedRow(), 44);
                    tbObat.setValueAt(BJM1.getSelectedItem().toString(), tbObat.getSelectedRow(), 45);
                    tbObat.setValueAt(MSA1.getSelectedItem().toString(), tbObat.getSelectedRow(), 46);
                    tbObat.setValueAt(HasilJatuh.getSelectedItem().toString(), tbObat.getSelectedRow(), 47);
                    tbObat.setValueAt(IntervensiJatuh.getSelectedItem().toString(), tbObat.getSelectedRow(), 48);
                    tbObat.setValueAt(IntervensiKolaborasi.getText(), tbObat.getSelectedRow(), 49);
                    Sequel.meghapus("asesmen_ulang_hemodialisa_masalah", "no_rawat", tbObat.getValueAt(tbObat.getSelectedRow(), 0).toString());
                    Sequel.meghapus("asesmen_ulang_hemodialisa_rencana", "no_rawat", tbObat.getValueAt(tbObat.getSelectedRow(), 0).toString());
                    Valid.tabelKosong(tabModeDetailMasalah);
                    Valid.tabelKosong(tabModeDetailRencana);
                    for (i = 0; i < tbMasalahKeperawatan.getRowCount(); i++) {
                        if (tbMasalahKeperawatan.getValueAt(i, 0).toString().equals("true")) {
                            if (Sequel.menyimpantf2("asesmen_ulang_hemodialisa_masalah", "?,?", 2, new String[]{TNoRw.getText(), tbMasalahKeperawatan.getValueAt(i, 1).toString()}) == true) {
                                tabModeDetailMasalah.addRow(new String[]{
                                    tbMasalahKeperawatan.getValueAt(i, 1).toString(), tbMasalahKeperawatan.getValueAt(i, 2).toString()
                                });
                            }
                        }
                    }
                    for (i = 0; i < tbRencanaKeperawatan.getRowCount(); i++) {
                        if (tbRencanaKeperawatan.getValueAt(i, 0).toString().equals("true")) {
                            if (Sequel.menyimpantf2("asesmen_ulang_hemodialisa_rencana", "?,?", 2, new String[]{TNoRw.getText(), tbRencanaKeperawatan.getValueAt(i, 1).toString()}) == true) {
                                tabModeDetailRencana.addRow(new String[]{
                                    tbRencanaKeperawatan.getValueAt(i, 1).toString(), tbRencanaKeperawatan.getValueAt(i, 2).toString()
                                });
                            }
                        }
                    }
                    DetailRencana.setText(IntervensiKolaborasi.getText());
                    TNoRM1.setText(TNoRM.getText());
                    TPasien1.setText(TPasien.getText());
                    emptTeks();;
                    TabRawat.setSelectedIndex(1);
                }
                break;
            case 2:
                /*
                            "No.Rawat", "No.RM", "Nama Pasien", "J.K.", "Tanggal Lahir", "Tgl.Asuhan",
            "Resep HD", "HD", "QB", "QD", "UF Goal", "Heparinisasi", "Keterangan Heparinisasi", "Dialisat", "Kode Dokter", "Nama Dokter"
                 */
                if (Sequel.mengedittf("asesmen_ulang_hemodialisa_medik", "no_rawat=?", "no_rawat=?,tanggal=?,resephd=?,hd=?,qb=?,qd=?,uf=?,heparinisasi=?,heparinisasi_ket=?,dialisat=?,nip=?", 12, new String[]{
                    TNoRw.getText(), Valid.SetTgl(TglAsuhan.getSelectedItem() + "") + " " + TglAsuhan.getSelectedItem().toString().substring(11, 19), resepHd.getSelectedItem().toString(),
                    Hd.getText(), Qb.getText(), Qd.getText(), Uf.getText(), Heparinisasi.getSelectedItem().toString(), Heparinisasi_Ket.getText(), Dialisat.getSelectedItem().toString(), KdDokter.getText(), tbObat1.getValueAt(tbObat1.getSelectedRow(), 0).toString()
                }) == true) {
                    tbObat1.setValueAt(TNoRw.getText(), tbObat1.getSelectedRow(), 0);
                    tbObat1.setValueAt(TNoRM.getText(), tbObat1.getSelectedRow(), 1);
                    tbObat1.setValueAt(TPasien.getText(), tbObat1.getSelectedRow(), 2);
                    tbObat1.setValueAt(TglLahir.getText(), tbObat1.getSelectedRow(), 3);
                    tbObat1.setValueAt(Jk.getText(), tbObat1.getSelectedRow(), 4);
                    tbObat1.setValueAt(Valid.SetTgl(TglAsuhan.getSelectedItem() + "") + " " + TglAsuhan.getSelectedItem().toString().substring(11, 19), tbObat1.getSelectedRow(), 5);
                    tbObat1.setValueAt(resepHd.getSelectedItem().toString(), tbObat1.getSelectedRow(), 6);
                    tbObat1.setValueAt(Hd.getText(), tbObat1.getSelectedRow(), 7);
                    tbObat1.setValueAt(Qb.getText(), tbObat1.getSelectedRow(), 8);
                    tbObat1.setValueAt(Qd.getText(), tbObat1.getSelectedRow(), 9);
                    tbObat1.setValueAt(Uf.getText(), tbObat1.getSelectedRow(), 10);
                    tbObat1.setValueAt(Heparinisasi.getSelectedItem().toString(), tbObat1.getSelectedRow(), 11);
                    tbObat1.setValueAt(Heparinisasi_Ket.getText(), tbObat1.getSelectedRow(), 12);
                    tbObat1.setValueAt(Dialisat.getSelectedItem().toString(), tbObat1.getSelectedRow(), 13);
                    tbObat1.setValueAt(KdDokter.getText(), tbObat1.getSelectedRow(), 14);
                    tbObat1.setValueAt(NmDokter.getText(), tbObat1.getSelectedRow(), 15);
                    emptTeks();;
                    TabRawat.setSelectedIndex(2);
                }

                break;
            case 3:
                break;
            case 4:
                break;
        }

    }

    private void simpan() {
        if (Sequel.menyimpantf("asesmen_ulang_hemodialisa", "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?", "No.Rawat", 45, new String[]{
            TNoRw.getText(), KdPetugas.getText(), Valid.SetTgl(TglAsuhan.getSelectedItem() + "") + " " + TglAsuhan.getSelectedItem().toString().substring(11, 19), Informasi.getSelectedItem().toString(), DiagnosaMedis.getText(),
            TerapiKe.getText(), Dializer.getText(), Alergi.getText(), NyeriPasien.getSelectedItem().toString(), IntensitasNyeri.getSelectedItem().toString(), LokasiNyeri.getText(), Kapan.getText(),
            SifatNyeri.getSelectedItem().toString(), SifatNyeriKet.getText(), KualitasNyeri.getSelectedItem().toString(), KualitasNyeriKet.getText(), FaktorPemberat.getSelectedItem().toString(),
            FaktorPemberatKet.getText(), FaktorPeringan.getSelectedItem().toString(), FaktoPeringanKet.getText(), EfekNyeri.getSelectedItem().toString(), EfekNyeriKet.getText(), Ku.getSelectedItem().toString(),
            KuKet.getText(), Td.getText(), Nadi.getSelectedItem().toString(), NadiKet.getText(), Respirasi.getSelectedItem().toString(), RespirasiKet.getText(), Konjungtiva.getSelectedItem().toString(),
            KonjungtivaKet.getText(), Ektremitas.getSelectedItem().toString(), BbKering.getText(), BbPreHD.getText(), BbLalu.getText(), BbPostHD.getText(), AksesVaskular.getSelectedItem().toString(),
            Jalur.getSelectedItem().toString(), JalurKet.getText(), ATS1.getSelectedItem().toString(), BJM1.getSelectedItem().toString(), MSA1.getSelectedItem().toString(), HasilJatuh.getSelectedItem().toString(),
            IntervensiJatuh.getSelectedItem().toString(), IntervensiKolaborasi.getText()
        }) == true) {
            tabMode.addRow(new String[]{
                TNoRw.getText(), TNoRM.getText(), TPasien.getText(), TglLahir.getText(), Jk.getText(), KdPetugas.getText(), NmPetugas.getText(), Valid.SetTgl(TglAsuhan.getSelectedItem() + "") + " " + TglAsuhan.getSelectedItem().toString().substring(11, 19), Informasi.getSelectedItem().toString(), DiagnosaMedis.getText(),
                TerapiKe.getText(), Dializer.getText(), Alergi.getText(), NyeriPasien.getSelectedItem().toString(), IntensitasNyeri.getSelectedItem().toString(), LokasiNyeri.getText(), Kapan.getText(),
                SifatNyeri.getSelectedItem().toString(), SifatNyeriKet.getText(), KualitasNyeri.getSelectedItem().toString(), KualitasNyeriKet.getText(), FaktorPemberat.getSelectedItem().toString(),
                FaktorPemberatKet.getText(), FaktorPeringan.getSelectedItem().toString(), FaktoPeringanKet.getText(), EfekNyeri.getSelectedItem().toString(), EfekNyeriKet.getText(), Ku.getSelectedItem().toString(),
                KuKet.getText(), Td.getText(), Nadi.getSelectedItem().toString(), NadiKet.getText(), Respirasi.getSelectedItem().toString(), RespirasiKet.getText(), Konjungtiva.getSelectedItem().toString(),
                KonjungtivaKet.getText(), Ektremitas.getSelectedItem().toString(), BbKering.getText(), BbPreHD.getText(), BbLalu.getText(), BbPostHD.getText(), AksesVaskular.getSelectedItem().toString(),
                Jalur.getSelectedItem().toString(), JalurKet.getText(), ATS1.getSelectedItem().toString(), BJM1.getSelectedItem().toString(), MSA1.getSelectedItem().toString(), HasilJatuh.getSelectedItem().toString(),
                IntervensiJatuh.getSelectedItem().toString(), IntervensiKolaborasi.getText()
            });
            LCount.setText("" + tabMode.getRowCount());
            Valid.tabelKosong(tabModeDetailMasalah);
            Valid.tabelKosong(tabModeDetailRencana);
            for (i = 0; i < tbMasalahKeperawatan.getRowCount(); i++) {
                if (tbMasalahKeperawatan.getValueAt(i, 0).toString().equals("true")) {
                    if (Sequel.menyimpantf2("asesmen_ulang_hemodialisa_masalah", "?,?", 2, new String[]{TNoRw.getText(), tbMasalahKeperawatan.getValueAt(i, 1).toString()}) == true) {
                        tabModeDetailMasalah.addRow(new String[]{
                            tbMasalahKeperawatan.getValueAt(i, 1).toString(), tbMasalahKeperawatan.getValueAt(i, 2).toString()
                        });
                    }
                }
            }
            for (i = 0; i < tbRencanaKeperawatan.getRowCount(); i++) {
                if (tbRencanaKeperawatan.getValueAt(i, 0).toString().equals("true")) {
                    if (Sequel.menyimpantf2("asesmen_ulang_hemodialisa_rencana", "?,?", 2, new String[]{TNoRw.getText(), tbRencanaKeperawatan.getValueAt(i, 1).toString()}) == true) {
                        tabModeDetailRencana.addRow(new String[]{
                            tbRencanaKeperawatan.getValueAt(i, 1).toString(), tbRencanaKeperawatan.getValueAt(i, 2).toString()
                        });
                    }
                }
            }
            DetailRencana.setText(IntervensiKolaborasi.getText());
            TNoRM1.setText(TNoRM.getText());
            TPasien1.setText(TPasien.getText());
            emptTeks();
        }
    }

    private void simpan2() {
        if (Sequel.menyimpantf("asesmen_ulang_hemodialisa_medik", "?,?,?,?,?,?,?,?,?,?,?", "No.Rawat", 11, new String[]{
            TNoRw.getText(), Valid.SetTgl(TglAsuhan.getSelectedItem() + "") + " " + TglAsuhan.getSelectedItem().toString().substring(11, 19), resepHd.getSelectedItem().toString(),
            Hd.getText(), Qb.getText(), Qd.getText(), Uf.getText(), Heparinisasi.getSelectedItem().toString(), Heparinisasi_Ket.getText(), Dialisat.getSelectedItem().toString(), KdDokter.getText()
        }) == true) {
            tabMode2.addRow(new String[]{
                TNoRw.getText(), TNoRM.getText(), TPasien.getText(), TglLahir.getText(), Jk.getText(), Valid.SetTgl(TglAsuhan.getSelectedItem() + "") + " " + TglAsuhan.getSelectedItem().toString().substring(11, 19), resepHd.getSelectedItem().toString(),
                Hd.getText(), Qb.getText(), Qd.getText(), Uf.getText(), Heparinisasi.getSelectedItem().toString(), Heparinisasi_Ket.getText(), Dialisat.getSelectedItem().toString(), KdDokter.getText(), NmDokter.getText()
            });
            LCount.setText("" + tabMode.getRowCount());
            emptTeks();
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

    private void simpan3() {
        if (Sequel.menyimpantf("asesmen_ulang_hemodialisa_observasi", "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?", "Data", 15, new String[]{
            TNoRw.getText(), Valid.SetTgl(Tanggal.getSelectedItem() + ""), Jam.getSelectedItem() + ":" + Menit.getSelectedItem() + ":" + Detik.getSelectedItem(),
            Observasi.getText(), Qb2.getText(), Uf2.getText(), Td2.getText(), Nadi2.getText(), Suhu.getText(), Respirasi2.getText(), Vp.getText(), Ap.getText(),
            Tmp.getText(), Ket_Lain.getText(), NIP.getText()

        }) == true) {
            tabMode3.addRow(new String[]{
                TNoRw.getText(), TNoRM.getText(), TPasien.getText(), Valid.SetTgl(Tanggal.getSelectedItem() + ""), Jam.getSelectedItem() + ":" + Menit.getSelectedItem() + ":" + Detik.getSelectedItem(),
                Observasi.getText(), Qb2.getText(), Uf2.getText(), Td2.getText(), Nadi2.getText(), Suhu.getText(), Respirasi2.getText(), Vp.getText(), Ap.getText(),
                Tmp.getText(), Ket_Lain.getText(), NIP.getText(), NamaPetugas.getText()
            });
            LCount.setText("" + tabMode3.getRowCount());
            emptTeks();
        }
    }

    private void simpan4() {
        if (Sequel.menyimpantf("asesmen_ulang_hemodialisa_evaluasi", "?,?,?,?,?", "Data", 5, new String[]{
            TNoRw.getText(), Valid.SetTgl(Tanggal.getSelectedItem() + ""), Jam.getSelectedItem() + ":" + Menit.getSelectedItem() + ":" + Detik.getSelectedItem(),
            Evaluasi.getText(), NIP1.getText()
        }) == true) {
            tabMode.addRow(new String[]{
                TNoRw.getText(), TNoRM.getText(), TPasien.getText(), Valid.SetTgl(Tanggal.getSelectedItem() + ""), Jam.getSelectedItem() + ":" + Menit.getSelectedItem() + ":" + Detik.getSelectedItem(),
                Evaluasi.getText(), NIP1.getText(), NamaPetugas1.getText()
            });
            LCount.setText("" + tabMode.getRowCount());

            Valid.tabelKosong(tabModeDetailPenyulit);

            for (i = 0; i < tbPenyulit.getRowCount(); i++) {
                if (tbPenyulit.getValueAt(i, 0).toString().equals("true")) {
                    if (Sequel.menyimpantf2("asesmen_ulang_hemodialisa_penyulit", "?,?", 2, new String[]{TNoRw.getText(), tbPenyulit.getValueAt(i, 1).toString()}) == true) {
                        tabModeDetailPenyulit.addRow(new String[]{
                            tbPenyulit.getValueAt(i, 1).toString(), tbPenyulit.getValueAt(i, 2).toString()
                        });
                    }
                }
            }

            DetailRencana.setText(IntervensiKolaborasi.getText());
            TNoRM1.setText(TNoRM.getText());
            TPasien1.setText(TPasien.getText());

            emptTeks();
        }

    }

    private void isForm() {
        if (ChkInput.isSelected() == true) {
            ChkInput.setVisible(false);
            PanelInput.setPreferredSize(new Dimension(WIDTH, 124));
            FormInput.setVisible(true);
            ChkInput.setVisible(true);
        } else if (ChkInput.isSelected() == false) {
            ChkInput.setVisible(false);
            PanelInput.setPreferredSize(new Dimension(WIDTH, 20));
            FormInput.setVisible(false);
            ChkInput.setVisible(true);
        }
    }
}
