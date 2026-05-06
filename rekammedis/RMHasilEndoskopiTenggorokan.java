/*
 * By Mas Elkhanza
 */


package rekammedis;

import bridging.ApiOrthanc;
import bridging.OrthancDICOM;
import com.fasterxml.jackson.databind.JsonNode;
import fungsi.WarnaTable;
import fungsi.batasInput;
import fungsi.koneksiDB;
import fungsi.sekuel;
import fungsi.validasi;
import fungsi.akses;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.DocumentEvent;
import javax.swing.event.HyperlinkEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import kepegawaian.DlgCariDokter;


/**
 *
 * @author perpustakaan
 */
public final class RMHasilEndoskopiTenggorokan extends javax.swing.JDialog {
    private final DefaultTableModel tabMode,tabModeDicom;
    private Connection koneksi=koneksiDB.condb();
    private sekuel Sequel=new sekuel();
    private validasi Valid=new validasi();
    private PreparedStatement ps;
    private ResultSet rs;
    private int i=0;
    private DlgCariDokter dokter=new DlgCariDokter(null,false);
    private StringBuilder htmlContent;
    private String finger="";
    private JsonNode root;
    
    /** Creates new form DlgRujuk
     * @param parent
     * @param modal */
    public RMHasilEndoskopiTenggorokan(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        tabMode=new DefaultTableModel(null,new Object[]{
            "No.Rawat","No.RM","Nama Pasien","Tgl.Lahir","Kode Dokter","Nama Dokter","Tanggal","Diagnose",
            "Arcus Faring","Tonsil","Benda Asing","Dinding Faring","Epiglotis","Plika Vocalis","Plika Ventrikularis","Kesimpulan"
        }){
              @Override public boolean isCellEditable(int rowIndex, int colIndex){return false;}
        };
        
        tbObat.setModel(tabMode);
        tbObat.setPreferredScrollableViewportSize(new Dimension(500,500));
        tbObat.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 16; i++) {
            TableColumn column = tbObat.getColumnModel().getColumn(i);
            if(i==0){
                column.setPreferredWidth(105);
            }else if(i==1){
                column.setPreferredWidth(70);
            }else if(i==2){
                column.setPreferredWidth(150);
            }else if(i==3){
                column.setPreferredWidth(65);
            }else if(i==4){
                column.setPreferredWidth(80);
            }else if(i==5){
                column.setPreferredWidth(150);
            }else if(i==6){
                column.setPreferredWidth(115);
            }else if(i==7){
                column.setPreferredWidth(150);
            }else if(i==8){
                column.setPreferredWidth(150);
            }else if(i==9){
                column.setPreferredWidth(130);
            }else if(i==10){
                column.setPreferredWidth(150);
            }else if(i==11){
                column.setPreferredWidth(100);
            }else if(i==12){
                column.setPreferredWidth(100);
            }else if(i==13){
                column.setPreferredWidth(100);
            }else if(i==14){
                column.setPreferredWidth(100);
            }else if(i==15){
                column.setPreferredWidth(100);
            }else if(i==16){
                column.setPreferredWidth(200);
            }
        }
        tbObat.setDefaultRenderer(Object.class, new WarnaTable());
        
        tabModeDicom=new DefaultTableModel(null,new Object[]{
            "UUID Pasien","ID Studies","ID Series"}){
             @Override public boolean isCellEditable(int rowIndex, int colIndex){return false;}
        };
//        tbListDicom.setModel(tabModeDicom);
//        tbListDicom.setPreferredScrollableViewportSize(new Dimension(500,500));
//        tbListDicom.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

//        for (i = 0; i < 3; i++) {
//            TableColumn column = tbListDicom.getColumnModel().getColumn(i);
//            if(i==0){
//                column.setPreferredWidth(100);
//            }else if(i==1){
//                column.setPreferredWidth(270);
//            }else if(i==2){
//                column.setPreferredWidth(270);
//            }
//        }
//        tbListDicom.setDefaultRenderer(Object.class, new WarnaTable());
//        
        TNoRw.setDocument(new batasInput((byte)17).getKata(TNoRw));
      
        Diagnosis.setDocument(new batasInput((int)50).getKata(Diagnosis));
        
//        Plasenta.setDocument(new batasInput((int)50).getKata(Plasenta));
      
        Kesimpulan.setDocument(new batasInput((int)200).getKata(Kesimpulan));
        TCari.setDocument(new batasInput((int)100).getKata(TCari));
        
        if(koneksiDB.CARICEPAT().equals("aktif")){
            TCari.getDocument().addDocumentListener(new javax.swing.event.DocumentListener(){
                @Override
                public void insertUpdate(DocumentEvent e) {
                    if(TCari.getText().length()>2){
                        tampil();
                    }
                }
                @Override
                public void removeUpdate(DocumentEvent e) {
                    if(TCari.getText().length()>2){
                        tampil();
                    }
                }
                @Override
                public void changedUpdate(DocumentEvent e) {
                    if(TCari.getText().length()>2){
                        tampil();
                    }
                }
            });
        }
        
        dokter.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e) {}
            @Override
            public void windowClosed(WindowEvent e) {
                if(dokter.getTable().getSelectedRow()!= -1){
                    KdDokter.setText(dokter.getTable().getValueAt(dokter.getTable().getSelectedRow(),0).toString());
                    NmDokter.setText(dokter.getTable().getValueAt(dokter.getTable().getSelectedRow(),1).toString());
                    KdDokter.requestFocus();
                }
            }
            @Override
            public void windowIconified(WindowEvent e) {}
            @Override
            public void windowDeiconified(WindowEvent e) {}
            @Override
            public void windowActivated(WindowEvent e) {}
            @Override
            public void windowDeactivated(WindowEvent e) {}
        });
        
//        ChkAccor.setSelected(false);
//        isPhoto();
        
        HTMLEditorKit kit = new HTMLEditorKit();
        LoadHTML.setEditable(true);
        LoadHTML.setEditorKit(kit);
//        LoadHTML2.setEditable(false);
//        LoadHTML2.addHyperlinkListener(e -> {
//            if (HyperlinkEvent.EventType.ACTIVATED.equals(e.getEventType())) {
//              Desktop desktop = Desktop.getDesktop();
//              try {
//                desktop.browse(e.getURL().toURI());
//              } catch (Exception ex) {
//                ex.printStackTrace();
//              }
//            }
//        });
//        LoadHTML2.setEditorKit(kit);
        StyleSheet styleSheet = kit.getStyleSheet();
        styleSheet.addRule(
                ".isi td{border-right: 1px solid #e2e7dd;font: 8.5px tahoma;height:12px;border-bottom: 1px solid #e2e7dd;background: #ffffff;color:#323232;}"+
                ".isi2 td{font: 8.5px tahoma;border:none;height:12px;background: #ffffff;color:#323232;}"+
                ".isi3 td{border-right: 1px solid #e2e7dd;font: 8.5px tahoma;height:12px;border-top: 1px solid #e2e7dd;background: #ffffff;color:#323232;}"+
                ".isi4 td{font: 11px tahoma;height:12px;border-top: 1px solid #e2e7dd;background: #ffffff;color:#323232;}"+
                ".isi5 td{font: 8.5px tahoma;border:none;height:12px;background: #ffffff;color:#AA0000;}"+
                ".isi6 td{font: 8.5px tahoma;border:none;height:12px;background: #ffffff;color:#FF0000;}"+
                ".isi7 td{font: 8.5px tahoma;border:none;height:12px;background: #ffffff;color:#C8C800;}"+
                ".isi8 td{font: 8.5px tahoma;border:none;height:12px;background: #ffffff;color:#00AA00;}"+
                ".isi9 td{font: 8.5px tahoma;border:none;height:12px;background: #ffffff;color:#969696;}"
        );
        Document doc = kit.createDefaultDocument();
        LoadHTML.setDocument(doc);
//        LoadHTML2.setDocument(doc);
    }


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        LoadHTML = new widget.editorpane();
        jPopupMenu1 = new javax.swing.JPopupMenu();
        MnPenilaianMedis = new javax.swing.JMenuItem();
        internalFrame1 = new widget.InternalFrame();
        panelGlass8 = new widget.panelisi();
        BtnSimpan = new widget.Button();
        BtnBatal = new widget.Button();
        BtnHapus = new widget.Button();
        BtnEdit = new widget.Button();
        BtnPrint = new widget.Button();
        BtnAll = new widget.Button();
        BtnKeluar = new widget.Button();
        TabRawat = new javax.swing.JTabbedPane();
        internalFrame2 = new widget.InternalFrame();
        scrollInput = new widget.ScrollPane();
        FormInput = new widget.PanelBiasa();
        TNoRw = new widget.TextBox();
        TPasien = new widget.TextBox();
        TNoRM = new widget.TextBox();
        label14 = new widget.Label();
        KdDokter = new widget.TextBox();
        NmDokter = new widget.TextBox();
        BtnDokter = new widget.Button();
        jLabel10 = new widget.Label();
        jSeparator1 = new javax.swing.JSeparator();
        label11 = new widget.Label();
        Tanggal = new widget.Tanggal();
        jLabel41 = new widget.Label();
        Arcus_faring = new widget.ComboBox();
        jLabel42 = new widget.Label();
        Tonsil = new widget.ComboBox();
        jLabel43 = new widget.Label();
        BendaAsing = new widget.ComboBox();
        jLabel44 = new widget.Label();
        Dinding_faring = new widget.ComboBox();
        jLabel46 = new widget.Label();
        Epiglotis = new widget.ComboBox();
        jLabel47 = new widget.Label();
        Plika_vocalis = new widget.ComboBox();
        jLabel48 = new widget.Label();
        Plika_ventrikularis = new widget.ComboBox();
        jLabel33 = new widget.Label();
        Diagnosis = new widget.TextBox();
        jLabel50 = new widget.Label();
        scrollPane17 = new widget.ScrollPane();
        Kesimpulan = new widget.TextArea();
        TglLahir = new widget.TextBox();
        jLabel8 = new widget.Label();
        internalFrame3 = new widget.InternalFrame();
        Scroll = new widget.ScrollPane();
        tbObat = new widget.Table();
        panelGlass9 = new widget.panelisi();
        jLabel19 = new widget.Label();
        DTPCari1 = new widget.Tanggal();
        jLabel21 = new widget.Label();
        DTPCari2 = new widget.Tanggal();
        jLabel6 = new widget.Label();
        TCari = new widget.TextBox();
        BtnCari = new widget.Button();
        jLabel7 = new widget.Label();
        LCount = new widget.Label();

        LoadHTML.setBorder(null);
        LoadHTML.setName("LoadHTML"); // NOI18N

        jPopupMenu1.setName("jPopupMenu1"); // NOI18N

        MnPenilaianMedis.setBackground(new java.awt.Color(255, 255, 254));
        MnPenilaianMedis.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        MnPenilaianMedis.setForeground(new java.awt.Color(50, 50, 50));
        MnPenilaianMedis.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        MnPenilaianMedis.setText("Formulir Hasil Endoskopi Tenggorokan");
        MnPenilaianMedis.setName("MnPenilaianMedis"); // NOI18N
        MnPenilaianMedis.setPreferredSize(new java.awt.Dimension(220, 26));
        MnPenilaianMedis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MnPenilaianMedisActionPerformed(evt);
            }
        });
        jPopupMenu1.add(MnPenilaianMedis);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);

        internalFrame1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "::[ Hasil Endoskopi Tenggorokan ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 50, 50))); // NOI18N
        internalFrame1.setFont(new java.awt.Font("Tahoma", 2, 12)); // NOI18N
        internalFrame1.setName("internalFrame1"); // NOI18N
        internalFrame1.setPreferredSize(new java.awt.Dimension(467, 500));
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

        internalFrame1.add(panelGlass8, java.awt.BorderLayout.PAGE_END);

        TabRawat.setBackground(new java.awt.Color(254, 255, 254));
        TabRawat.setForeground(new java.awt.Color(50, 50, 50));
        TabRawat.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        TabRawat.setName("TabRawat"); // NOI18N
        TabRawat.setPreferredSize(new java.awt.Dimension(457, 480));
        TabRawat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TabRawatMouseClicked(evt);
            }
        });

        internalFrame2.setBorder(null);
        internalFrame2.setName("internalFrame2"); // NOI18N
        internalFrame2.setPreferredSize(new java.awt.Dimension(102, 480));
        internalFrame2.setLayout(new java.awt.BorderLayout(1, 1));

        scrollInput.setName("scrollInput"); // NOI18N
        scrollInput.setPreferredSize(new java.awt.Dimension(102, 557));

        FormInput.setBackground(new java.awt.Color(255, 255, 255));
        FormInput.setBorder(null);
        FormInput.setName("FormInput"); // NOI18N
        FormInput.setPreferredSize(new java.awt.Dimension(750, 363));
        FormInput.setLayout(null);

        TNoRw.setHighlighter(null);
        TNoRw.setName("TNoRw"); // NOI18N
        TNoRw.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TNoRwKeyPressed(evt);
            }
        });
        FormInput.add(TNoRw);
        TNoRw.setBounds(74, 10, 131, 23);

        TPasien.setEditable(false);
        TPasien.setHighlighter(null);
        TPasien.setName("TPasien"); // NOI18N
        FormInput.add(TPasien);
        TPasien.setBounds(309, 10, 260, 23);

        TNoRM.setEditable(false);
        TNoRM.setHighlighter(null);
        TNoRM.setName("TNoRM"); // NOI18N
        FormInput.add(TNoRM);
        TNoRM.setBounds(207, 10, 100, 23);

        label14.setText("Dokter :");
        label14.setName("label14"); // NOI18N
        label14.setPreferredSize(new java.awt.Dimension(70, 23));
        FormInput.add(label14);
        label14.setBounds(0, 40, 70, 23);

        KdDokter.setEditable(false);
        KdDokter.setName("KdDokter"); // NOI18N
        KdDokter.setPreferredSize(new java.awt.Dimension(80, 23));
        FormInput.add(KdDokter);
        KdDokter.setBounds(74, 40, 110, 23);

        NmDokter.setEditable(false);
        NmDokter.setName("NmDokter"); // NOI18N
        NmDokter.setPreferredSize(new java.awt.Dimension(207, 23));
        FormInput.add(NmDokter);
        NmDokter.setBounds(186, 40, 295, 23);

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
        BtnDokter.setBounds(484, 40, 28, 23);

        jLabel10.setText("No.Rawat :");
        jLabel10.setName("jLabel10"); // NOI18N
        FormInput.add(jLabel10);
        jLabel10.setBounds(0, 10, 70, 23);

        jSeparator1.setBackground(new java.awt.Color(239, 244, 234));
        jSeparator1.setForeground(new java.awt.Color(239, 244, 234));
        jSeparator1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(239, 244, 234)));
        jSeparator1.setName("jSeparator1"); // NOI18N
        FormInput.add(jSeparator1);
        jSeparator1.setBounds(0, 70, 750, 1);

        label11.setText("Tanggal :");
        label11.setName("label11"); // NOI18N
        label11.setPreferredSize(new java.awt.Dimension(70, 23));
        FormInput.add(label11);
        label11.setBounds(538, 40, 52, 23);

        Tanggal.setForeground(new java.awt.Color(50, 70, 50));
        Tanggal.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "15-11-2023 09:08:24" }));
        Tanggal.setDisplayFormat("dd-MM-yyyy HH:mm:ss");
        Tanggal.setName("Tanggal"); // NOI18N
        Tanggal.setOpaque(false);
        Tanggal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TanggalKeyPressed(evt);
            }
        });
        FormInput.add(Tanggal);
        Tanggal.setBounds(594, 40, 140, 23);

        jLabel41.setText("Arcus Faring :");
        jLabel41.setName("jLabel41"); // NOI18N
        FormInput.add(jLabel41);
        jLabel41.setBounds(0, 120, 150, 23);

        Arcus_faring.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Simetris", "Asimetris" }));
        Arcus_faring.setName("Arcus_faring"); // NOI18N
        Arcus_faring.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Arcus_faringActionPerformed(evt);
            }
        });
        Arcus_faring.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Arcus_faringKeyPressed(evt);
            }
        });
        FormInput.add(Arcus_faring);
        Arcus_faring.setBounds(160, 120, 150, 23);

        jLabel42.setText("Tonsil :");
        jLabel42.setName("jLabel42"); // NOI18N
        FormInput.add(jLabel42);
        jLabel42.setBounds(0, 150, 150, 23);

        Tonsil.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "T0/T0", "T1/T1", "T2/T2", "T3/T3", "T4/T4" }));
        Tonsil.setName("Tonsil"); // NOI18N
        Tonsil.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TonsilActionPerformed(evt);
            }
        });
        Tonsil.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TonsilKeyPressed(evt);
            }
        });
        FormInput.add(Tonsil);
        Tonsil.setBounds(160, 150, 150, 23);

        jLabel43.setText("Benda Asing :");
        jLabel43.setName("jLabel43"); // NOI18N
        FormInput.add(jLabel43);
        jLabel43.setBounds(0, 180, 150, 23);

        BendaAsing.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ada", "Tidak Ada" }));
        BendaAsing.setName("BendaAsing"); // NOI18N
        BendaAsing.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BendaAsingActionPerformed(evt);
            }
        });
        BendaAsing.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BendaAsingKeyPressed(evt);
            }
        });
        FormInput.add(BendaAsing);
        BendaAsing.setBounds(160, 180, 150, 23);

        jLabel44.setText("Dinding Faring :");
        jLabel44.setName("jLabel44"); // NOI18N
        FormInput.add(jLabel44);
        jLabel44.setBounds(0, 210, 150, 23);

        Dinding_faring.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Normal", "Hiperemis", "Bulging" }));
        Dinding_faring.setName("Dinding_faring"); // NOI18N
        Dinding_faring.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Dinding_faringActionPerformed(evt);
            }
        });
        Dinding_faring.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Dinding_faringKeyPressed(evt);
            }
        });
        FormInput.add(Dinding_faring);
        Dinding_faring.setBounds(160, 210, 150, 23);

        jLabel46.setText("Epiglotis :");
        jLabel46.setName("jLabel46"); // NOI18N
        FormInput.add(jLabel46);
        jLabel46.setBounds(350, 120, 150, 23);

        Epiglotis.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Normal", "Oedem" }));
        Epiglotis.setName("Epiglotis"); // NOI18N
        Epiglotis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EpiglotisActionPerformed(evt);
            }
        });
        Epiglotis.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                EpiglotisKeyPressed(evt);
            }
        });
        FormInput.add(Epiglotis);
        Epiglotis.setBounds(510, 120, 150, 23);

        jLabel47.setText("Plika Vocalis :");
        jLabel47.setName("jLabel47"); // NOI18N
        FormInput.add(jLabel47);
        jLabel47.setBounds(350, 150, 150, 23);

        Plika_vocalis.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Normal", "Oedem" }));
        Plika_vocalis.setName("Plika_vocalis"); // NOI18N
        Plika_vocalis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Plika_vocalisActionPerformed(evt);
            }
        });
        Plika_vocalis.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Plika_vocalisKeyPressed(evt);
            }
        });
        FormInput.add(Plika_vocalis);
        Plika_vocalis.setBounds(510, 150, 150, 23);

        jLabel48.setText("Plika Ventrikularis :");
        jLabel48.setName("jLabel48"); // NOI18N
        FormInput.add(jLabel48);
        jLabel48.setBounds(350, 180, 150, 23);

        Plika_ventrikularis.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Normal", "Oedem" }));
        Plika_ventrikularis.setName("Plika_ventrikularis"); // NOI18N
        Plika_ventrikularis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Plika_ventrikularisActionPerformed(evt);
            }
        });
        Plika_ventrikularis.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Plika_ventrikularisKeyPressed(evt);
            }
        });
        FormInput.add(Plika_ventrikularis);
        Plika_ventrikularis.setBounds(510, 180, 150, 23);

        jLabel33.setText("Diagnose :");
        jLabel33.setName("jLabel33"); // NOI18N
        FormInput.add(jLabel33);
        jLabel33.setBounds(0, 90, 150, 23);

        Diagnosis.setFocusTraversalPolicyProvider(true);
        Diagnosis.setName("Diagnosis"); // NOI18N
        Diagnosis.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DiagnosisKeyPressed(evt);
            }
        });
        FormInput.add(Diagnosis);
        Diagnosis.setBounds(160, 90, 550, 23);

        jLabel50.setText("Kesimpulan :");
        jLabel50.setName("jLabel50"); // NOI18N
        FormInput.add(jLabel50);
        jLabel50.setBounds(0, 240, 150, 23);

        scrollPane17.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        scrollPane17.setName("scrollPane17"); // NOI18N

        Kesimpulan.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        Kesimpulan.setColumns(20);
        Kesimpulan.setRows(5);
        Kesimpulan.setName("Kesimpulan"); // NOI18N
        Kesimpulan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KesimpulanKeyPressed(evt);
            }
        });
        scrollPane17.setViewportView(Kesimpulan);

        FormInput.add(scrollPane17);
        scrollPane17.setBounds(160, 240, 540, 63);

        TglLahir.setEditable(false);
        TglLahir.setHighlighter(null);
        TglLahir.setName("TglLahir"); // NOI18N
        FormInput.add(TglLahir);
        TglLahir.setBounds(644, 10, 80, 23);

        jLabel8.setText("Tgl.Lahir :");
        jLabel8.setName("jLabel8"); // NOI18N
        FormInput.add(jLabel8);
        jLabel8.setBounds(580, 10, 60, 23);

        scrollInput.setViewportView(FormInput);

        internalFrame2.add(scrollInput, java.awt.BorderLayout.CENTER);

        TabRawat.addTab("Input Hasil Endoskopi Tenggorokan", internalFrame2);

        internalFrame3.setBorder(null);
        internalFrame3.setName("internalFrame3"); // NOI18N
        internalFrame3.setLayout(new java.awt.BorderLayout(1, 1));

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

        internalFrame3.add(Scroll, java.awt.BorderLayout.CENTER);

        panelGlass9.setName("panelGlass9"); // NOI18N
        panelGlass9.setPreferredSize(new java.awt.Dimension(44, 44));
        panelGlass9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));

        jLabel19.setText("Tanggal :");
        jLabel19.setName("jLabel19"); // NOI18N
        jLabel19.setPreferredSize(new java.awt.Dimension(60, 23));
        panelGlass9.add(jLabel19);

        DTPCari1.setForeground(new java.awt.Color(50, 70, 50));
        DTPCari1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "15-11-2023" }));
        DTPCari1.setDisplayFormat("dd-MM-yyyy");
        DTPCari1.setName("DTPCari1"); // NOI18N
        DTPCari1.setOpaque(false);
        DTPCari1.setPreferredSize(new java.awt.Dimension(90, 23));
        panelGlass9.add(DTPCari1);

        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel21.setText("s.d.");
        jLabel21.setName("jLabel21"); // NOI18N
        jLabel21.setPreferredSize(new java.awt.Dimension(23, 23));
        panelGlass9.add(jLabel21);

        DTPCari2.setForeground(new java.awt.Color(50, 70, 50));
        DTPCari2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "15-11-2023" }));
        DTPCari2.setDisplayFormat("dd-MM-yyyy");
        DTPCari2.setName("DTPCari2"); // NOI18N
        DTPCari2.setOpaque(false);
        DTPCari2.setPreferredSize(new java.awt.Dimension(90, 23));
        panelGlass9.add(DTPCari2);

        jLabel6.setText("Key Word :");
        jLabel6.setName("jLabel6"); // NOI18N
        jLabel6.setPreferredSize(new java.awt.Dimension(80, 23));
        panelGlass9.add(jLabel6);

        TCari.setName("TCari"); // NOI18N
        TCari.setPreferredSize(new java.awt.Dimension(205, 23));
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

        jLabel7.setText("Record :");
        jLabel7.setName("jLabel7"); // NOI18N
        jLabel7.setPreferredSize(new java.awt.Dimension(60, 23));
        panelGlass9.add(jLabel7);

        LCount.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        LCount.setText("0");
        LCount.setName("LCount"); // NOI18N
        LCount.setPreferredSize(new java.awt.Dimension(70, 23));
        panelGlass9.add(LCount);

        internalFrame3.add(panelGlass9, java.awt.BorderLayout.PAGE_END);

        TabRawat.addTab("Data Hasil Endoskopi Tenggorokan", internalFrame3);

        internalFrame1.add(TabRawat, java.awt.BorderLayout.CENTER);

        getContentPane().add(internalFrame1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void TNoRwKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TNoRwKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            isRawat();
        }else{            
            Valid.pindah(evt,TCari,BtnDokter);
        }
}//GEN-LAST:event_TNoRwKeyPressed

    private void BtnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSimpanActionPerformed
        if(TNoRM.getText().trim().equals("")){
            Valid.textKosong(TNoRw,"Nama Pasien");
        }else if(NmDokter.getText().trim().equals("")){
            Valid.textKosong(BtnDokter,"Dokter");
        }else if(Diagnosis.getText().trim().equals("")){
            Valid.textKosong(Diagnosis,"Diagnosis");
        }else{
            if(Sequel.menyimpantf("hasil_endoskopi_tenggorokan","?,?,?,?,?,?,?,?,?,?,?,?","No.Rawat",12,new String[]{
                    TNoRw.getText(),Valid.SetTgl(Tanggal.getSelectedItem()+"")+" "+Tanggal.getSelectedItem().toString().substring(11,19),KdDokter.getText(),
                    Diagnosis.getText(),Arcus_faring.getSelectedItem().toString(),Tonsil.getSelectedItem().toString(),
                    BendaAsing.getSelectedItem().toString(),Dinding_faring.getSelectedItem().toString(),
                    Epiglotis.getSelectedItem().toString(),Plika_vocalis.getSelectedItem().toString(),Plika_ventrikularis.getSelectedItem().toString(),Kesimpulan.getText()
                   
                })==true){
                    JOptionPane.showMessageDialog(null,"Data Berhasil Disimpan..!!");
//                    TabRawat.getSelectedIndex()
                    emptTeks();
            }
        }    
}//GEN-LAST:event_BtnSimpanActionPerformed

    private void BtnSimpanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnSimpanKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnSimpanActionPerformed(null);
        }else{
            //Valid.pindah(evt,TerapiPreOp,BtnBatal);
        }
}//GEN-LAST:event_BtnSimpanKeyPressed

    private void BtnBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnBatalActionPerformed
        emptTeks();
}//GEN-LAST:event_BtnBatalActionPerformed

    private void BtnBatalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnBatalKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            emptTeks();
        }else{Valid.pindah(evt, BtnSimpan, BtnHapus);}
}//GEN-LAST:event_BtnBatalKeyPressed

    private void BtnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnHapusActionPerformed
        if(tbObat.getSelectedRow()>-1){
            if(akses.getkode().equals("Admin Utama")){
                hapus();
            }else{
                if(KdDokter.getText().equals(tbObat.getValueAt(tbObat.getSelectedRow(),4).toString())){
                    hapus();
                }else{
                    JOptionPane.showMessageDialog(null,"Hanya bisa dihapus oleh dokter yang bersangkutan..!!");
                }
            }
        }else{
            JOptionPane.showMessageDialog(rootPane,"Silahkan anda pilih data terlebih dahulu..!!");
        }              
            
}//GEN-LAST:event_BtnHapusActionPerformed

    private void BtnHapusKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnHapusKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnHapusActionPerformed(null);
        }else{
            Valid.pindah(evt, BtnBatal, BtnEdit);
        }
}//GEN-LAST:event_BtnHapusKeyPressed

    private void BtnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnEditActionPerformed
       if(TNoRM.getText().trim().equals("")){
            Valid.textKosong(TNoRw,"Nama Pasien");
        }else if(NmDokter.getText().trim().equals("")){
            Valid.textKosong(BtnDokter,"Dokter");
        }else if(Kesimpulan.getText().trim().equals("")){
            Valid.textKosong(Kesimpulan,"Kesimpulan");
        }else{
            if(tbObat.getSelectedRow()>-1){
                if(akses.getkode().equals("Admin Utama")){
                    ganti();
                }else{
                    if(KdDokter.getText().equals(tbObat.getValueAt(tbObat.getSelectedRow(),4).toString())){
                        ganti();
                    }else{
                        JOptionPane.showMessageDialog(null,"Hanya bisa diganti oleh dokter yang bersangkutan..!!");
                    }
                }
            }else{
                JOptionPane.showMessageDialog(rootPane,"Silahkan anda pilih data terlebih dahulu..!!");
            }
        }
}//GEN-LAST:event_BtnEditActionPerformed

    private void BtnEditKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnEditKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnEditActionPerformed(null);
        }else{
            Valid.pindah(evt, BtnHapus, BtnPrint);
        }
}//GEN-LAST:event_BtnEditKeyPressed

    private void BtnKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnKeluarActionPerformed
        dispose();
}//GEN-LAST:event_BtnKeluarActionPerformed

    private void BtnKeluarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnKeluarKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnKeluarActionPerformed(null);
        }else{Valid.pindah(evt,BtnEdit,TCari);}
}//GEN-LAST:event_BtnKeluarKeyPressed

    private void BtnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnPrintActionPerformed
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        if(tabMode.getRowCount()==0){
            JOptionPane.showMessageDialog(null,"Maaf, data sudah habis. Tidak ada data yang bisa anda print...!!!!");
            BtnBatal.requestFocus();
        }else if(tabMode.getRowCount()!=0){
            try{
                if(TCari.getText().trim().equals("")){
                    ps=koneksi.prepareStatement(
                            "select reg_periksa.no_rawat,pasien.no_rkm_medis,pasien.nm_pasien,pasien.tgl_lahir,hasil_pemeriksaan_usg.tanggal,"+
                            "hasil_pemeriksaan_usg.kd_dokter,dokter.nm_dokter,hasil_pemeriksaan_usg.keluhan,hasil_pemeriksaan_usg.paritas_g,"+
                            "hasil_pemeriksaan_usg.paritas_p,hasil_pemeriksaan_usg.kantong_gestasi,hasil_pemeriksaan_usg.ukuran_bokongkepala,"+
                            "hasil_pemeriksaan_usg.paritas_a,hasil_pemeriksaan_usg.diameter_biparietal,hasil_pemeriksaan_usg.panjang_femur,"+
                            "hasil_pemeriksaan_usg.lingkar_abdomen,hasil_pemeriksaan_usg.tafsiran_berat_janin,hasil_pemeriksaan_usg.usia_kehamilan,"+
                            "hasil_pemeriksaan_usg.plasenta_berimplatansi,hasil_pemeriksaan_usg.derajat_maturitas,hasil_pemeriksaan_usg.jumlah_air_ketuban,"+
                            "hasil_pemeriksaan_usg.indek_cairan_ketuban,hasil_pemeriksaan_usg.kelainan_kongenital,hasil_pemeriksaan_usg.peluang_sex,"+
                            "hasil_pemeriksaan_usg.kesimpulan from reg_periksa inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                            "inner join hasil_pemeriksaan_usg on reg_periksa.no_rawat=hasil_pemeriksaan_usg.no_rawat "+
                            "inner join dokter on hasil_pemeriksaan_usg.kd_dokter=dokter.kd_dokter where "+
                            "hasil_pemeriksaan_usg.tanggal between ? and ? order by hasil_pemeriksaan_usg.tanggal");
                }else{
                    ps=koneksi.prepareStatement(
                            "select reg_periksa.no_rawat,pasien.no_rkm_medis,pasien.nm_pasien,pasien.tgl_lahir,hasil_pemeriksaan_usg.tanggal,"+
                            "hasil_pemeriksaan_usg.kd_dokter,dokter.nm_dokter,hasil_pemeriksaan_usg.keluhan,hasil_pemeriksaan_usg.paritas_g,"+
                            "hasil_pemeriksaan_usg.paritas_p,hasil_pemeriksaan_usg.kantong_gestasi,hasil_pemeriksaan_usg.ukuran_bokongkepala,"+
                            "hasil_pemeriksaan_usg.paritas_a,hasil_pemeriksaan_usg.diameter_biparietal,hasil_pemeriksaan_usg.panjang_femur,"+
                            "hasil_pemeriksaan_usg.lingkar_abdomen,hasil_pemeriksaan_usg.tafsiran_berat_janin,hasil_pemeriksaan_usg.usia_kehamilan,"+
                            "hasil_pemeriksaan_usg.plasenta_berimplatansi,hasil_pemeriksaan_usg.derajat_maturitas,hasil_pemeriksaan_usg.jumlah_air_ketuban,"+
                            "hasil_pemeriksaan_usg.indek_cairan_ketuban,hasil_pemeriksaan_usg.kelainan_kongenital,hasil_pemeriksaan_usg.peluang_sex,"+
                            "hasil_pemeriksaan_usg.kesimpulan from reg_periksa inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                            "inner join hasil_pemeriksaan_usg on reg_periksa.no_rawat=hasil_pemeriksaan_usg.no_rawat "+
                            "inner join dokter on hasil_pemeriksaan_usg.kd_dokter=dokter.kd_dokter where "+
                            "hasil_pemeriksaan_usg.tanggal between ? and ? and (reg_periksa.no_rawat like ? or pasien.no_rkm_medis like ? or pasien.nm_pasien like ? or "+
                            "hasil_pemeriksaan_usg.kd_dokter like ? or dokter.nm_dokter like ?) order by hasil_pemeriksaan_usg.tanggal");
                }

                try {
                    if(TCari.getText().trim().equals("")){
                        ps.setString(1,Valid.SetTgl(DTPCari1.getSelectedItem()+"")+" 00:00:00");
                        ps.setString(2,Valid.SetTgl(DTPCari2.getSelectedItem()+"")+" 23:59:59");
                    }else{
                        ps.setString(1,Valid.SetTgl(DTPCari1.getSelectedItem()+"")+" 00:00:00");
                        ps.setString(2,Valid.SetTgl(DTPCari2.getSelectedItem()+"")+" 23:59:59");
                        ps.setString(3,"%"+TCari.getText()+"%");
                        ps.setString(4,"%"+TCari.getText()+"%");
                        ps.setString(5,"%"+TCari.getText()+"%");
                        ps.setString(6,"%"+TCari.getText()+"%");
                        ps.setString(7,"%"+TCari.getText()+"%");
                    } 
                    rs=ps.executeQuery();
                    htmlContent = new StringBuilder();
                    htmlContent.append(                             
                        "<tr class='isi'>"+
                            "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>No.Rawat</b></td>"+
                            "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>No.RM</b></td>"+
                            "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>Nama Pasien</b></td>"+
                            "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>Tgl.Lahir</b></td>"+
                            "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>Kode Dokter</b></td>"+
                            "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>Nama Dokter</b></td>"+
                            "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>Tanggal</b></td>"+
                            "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>Keluhan</b></td>"+
                            "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>G</b></td>"+
                            "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>P</b></td>"+
                            "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>A</b></td>"+
                            "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>GS</b></td>"+
                            "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>CRL</b></td>"+
                            "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>DBP</b></td>"+
                            "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>FL</b></td>"+
                            "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>AC</b></td>"+
                            "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>TBJ</b></td>"+
                            "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>Usia Kehamilan</b></td>"+
                            "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>Plasenta Berimplatansi</b></td>"+
                            "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>Derajat Maturitas</b></td>"+
                            "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>Air Ketuban</b></td>"+
                            "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>Peluang Sex</b></td>"+
                            "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>Indeks Cairan Ketuban (ICK)</b></td>"+
                            "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>Kelainan Kongenital Mayor</b></td>"+
                            "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>Kesimpulan</b></td>"+
                        "</tr>"
                    );
                    while(rs.next()){
                        htmlContent.append(
                            "<tr class='isi'>"+
                               "<td valign='top'>"+rs.getString("no_rawat")+"</td>"+
                               "<td valign='top'>"+rs.getString("no_rkm_medis")+"</td>"+
                               "<td valign='top'>"+rs.getString("nm_pasien")+"</td>"+
                               "<td valign='top'>"+rs.getString("tgl_lahir")+"</td>"+
                               "<td valign='top'>"+rs.getString("kd_dokter")+"</td>"+
                               "<td valign='top'>"+rs.getString("nm_dokter")+"</td>"+
                               "<td valign='top'>"+rs.getString("tanggal")+"</td>"+
                               "<td valign='top'>"+rs.getString("keluhan")+"</td>"+
                               "<td valign='top'>"+rs.getString("paritas_g")+"</td>"+
                               "<td valign='top'>"+rs.getString("paritas_p")+"</td>"+
                               "<td valign='top'>"+rs.getString("paritas_a")+"</td>"+
                               "<td valign='top'>"+rs.getString("kantong_gestasi")+"</td>"+
                               "<td valign='top'>"+rs.getString("ukuran_bokongkepala")+"</td>"+
                               "<td valign='top'>"+rs.getString("diameter_biparietal")+"</td>"+
                               "<td valign='top'>"+rs.getString("panjang_femur")+"</td>"+
                               "<td valign='top'>"+rs.getString("lingkar_abdomen")+"</td>"+
                               "<td valign='top'>"+rs.getString("tafsiran_berat_janin")+"</td>"+
                               "<td valign='top'>"+rs.getString("usia_kehamilan")+"</td>"+
                               "<td valign='top'>"+rs.getString("plasenta_berimplatansi")+"</td>"+
                               "<td valign='top'>"+rs.getString("derajat_maturitas")+"</td>"+
                               "<td valign='top'>"+rs.getString("jumlah_air_ketuban")+"</td>"+
                               "<td valign='top'>"+rs.getString("peluang_sex")+"</td>"+
                               "<td valign='top'>"+rs.getString("indek_cairan_ketuban")+"</td>"+
                               "<td valign='top'>"+rs.getString("kelainan_kongenital")+"</td>"+
                               "<td valign='top'>"+rs.getString("kesimpulan")+"</td>"+
                            "</tr>");
                    }
                    LoadHTML.setText(
                        "<html>"+
                          "<table width='2100px' border='0' align='center' cellpadding='1px' cellspacing='0' class='tbl_form'>"+
                           htmlContent.toString()+
                          "</table>"+
                        "</html>"
                    );

                    File g = new File("file2.css");            
                    BufferedWriter bg = new BufferedWriter(new FileWriter(g));
                    bg.write(
                        ".isi td{border-right: 1px solid #e2e7dd;font: 8.5px tahoma;height:12px;border-bottom: 1px solid #e2e7dd;background: #ffffff;color:#323232;}"+
                        ".isi2 td{font: 8.5px tahoma;border:none;height:12px;background: #ffffff;color:#323232;}"+
                        ".isi3 td{border-right: 1px solid #e2e7dd;font: 8.5px tahoma;height:12px;border-top: 1px solid #e2e7dd;background: #ffffff;color:#323232;}"+
                        ".isi4 td{font: 11px tahoma;height:12px;border-top: 1px solid #e2e7dd;background: #ffffff;color:#323232;}"+
                        ".isi5 td{font: 8.5px tahoma;border:none;height:12px;background: #ffffff;color:#AA0000;}"+
                        ".isi6 td{font: 8.5px tahoma;border:none;height:12px;background: #ffffff;color:#FF0000;}"+
                        ".isi7 td{font: 8.5px tahoma;border:none;height:12px;background: #ffffff;color:#C8C800;}"+
                        ".isi8 td{font: 8.5px tahoma;border:none;height:12px;background: #ffffff;color:#00AA00;}"+
                        ".isi9 td{font: 8.5px tahoma;border:none;height:12px;background: #ffffff;color:#969696;}"
                    );
                    bg.close();

                    File f = new File("DataHasilPemeriksaanUSG.html");            
                    BufferedWriter bw = new BufferedWriter(new FileWriter(f));            
                    bw.write(LoadHTML.getText().replaceAll("<head>","<head>"+
                                "<link href=\"file2.css\" rel=\"stylesheet\" type=\"text/css\" />"+
                                "<table width='2100px' border='0' align='center' cellpadding='3px' cellspacing='0' class='tbl_form'>"+
                                    "<tr class='isi2'>"+
                                        "<td valign='top' align='center'>"+
                                            "<font size='4' face='Tahoma'>"+akses.getnamars()+"</font><br>"+
                                            akses.getalamatrs()+", "+akses.getkabupatenrs()+", "+akses.getpropinsirs()+"<br>"+
                                            akses.getkontakrs()+", E-mail : "+akses.getemailrs()+"<br><br>"+
                                            "<font size='2' face='Tahoma'>DATA PENILAIAN PRE OPERASI<br><br></font>"+        
                                        "</td>"+
                                   "</tr>"+
                                "</table>")
                    );
                    bw.close();                         
                    Desktop.getDesktop().browse(f.toURI());
                } catch (Exception e) {
                    System.out.println("Notif : "+e);
                } finally{
                    if(rs!=null){
                        rs.close();
                    }
                    if(ps!=null){
                        ps.close();
                    }
                }

            }catch(Exception e){
                System.out.println("Notifikasi : "+e);
            }
        }
        this.setCursor(Cursor.getDefaultCursor());
}//GEN-LAST:event_BtnPrintActionPerformed

    private void BtnPrintKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnPrintKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnPrintActionPerformed(null);
        }else{
            Valid.pindah(evt, BtnEdit, BtnKeluar);
        }
}//GEN-LAST:event_BtnPrintKeyPressed

    private void TCariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TCariKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            BtnCariActionPerformed(null);
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            BtnCari.requestFocus();
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_UP){
            BtnKeluar.requestFocus();
        }
}//GEN-LAST:event_TCariKeyPressed

    private void BtnCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCariActionPerformed
        tampil();
}//GEN-LAST:event_BtnCariActionPerformed

    private void BtnCariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnCariKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnCariActionPerformed(null);
        }else{
            Valid.pindah(evt, TCari, BtnAll);
        }
}//GEN-LAST:event_BtnCariKeyPressed

    private void BtnAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAllActionPerformed
        TCari.setText("");
        tampil();
}//GEN-LAST:event_BtnAllActionPerformed

    private void BtnAllKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnAllKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            TCari.setText("");
            tampil();
        }else{
            Valid.pindah(evt, BtnCari, TPasien);
        }
}//GEN-LAST:event_BtnAllKeyPressed

    private void tbObatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbObatMouseClicked
        if(tabMode.getRowCount()!=0){
            try {
//                isPhoto();
//                panggilPhoto();
                getData();
//                tampilOrthanc();
            } catch (java.lang.NullPointerException e) {
            }
            if((evt.getClickCount()==2)&&(tbObat.getSelectedColumn()==0)){
                TabRawat.setSelectedIndex(0);
            }
        }
}//GEN-LAST:event_tbObatMouseClicked

    private void tbObatKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbObatKeyPressed
        if(tabMode.getRowCount()!=0){
            if((evt.getKeyCode()==KeyEvent.VK_ENTER)||(evt.getKeyCode()==KeyEvent.VK_UP)||(evt.getKeyCode()==KeyEvent.VK_DOWN)){
                try {
                    getData();
                } catch (java.lang.NullPointerException e) {
                }
            }else if(evt.getKeyCode()==KeyEvent.VK_SPACE){
                try {
                    getData();
                    TabRawat.setSelectedIndex(0);
                } catch (java.lang.NullPointerException e) {
                }
            }
        }
}//GEN-LAST:event_tbObatKeyPressed

    private void BtnDokterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnDokterActionPerformed
        dokter.isCek();
        dokter.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
        dokter.setLocationRelativeTo(internalFrame1);
        dokter.setAlwaysOnTop(false);
        dokter.setVisible(true);
    }//GEN-LAST:event_BtnDokterActionPerformed

    private void BtnDokterKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnDokterKeyPressed
        //Valid.pindah(evt,Edukasi,Hubungan);
    }//GEN-LAST:event_BtnDokterKeyPressed

    private void TabRawatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TabRawatMouseClicked
        if(TabRawat.getSelectedIndex()==1){
            tampil();
        }
    }//GEN-LAST:event_TabRawatMouseClicked

    private void TanggalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TanggalKeyPressed
        //Valid.pindah(evt,Edukasi,Anamnesis);
    }//GEN-LAST:event_TanggalKeyPressed

    private void MnPenilaianMedisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MnPenilaianMedisActionPerformed
        if(tbObat.getSelectedRow()>-1){
            Map<String, Object> param = new HashMap<>();
            param.put("namars",akses.getnamars());
            param.put("alamatrs",akses.getalamatrs());
            param.put("kotars",akses.getkabupatenrs());
            param.put("propinsirs",akses.getpropinsirs());
            param.put("kontakrs",akses.getkontakrs());
            param.put("emailrs",akses.getemailrs());          
            param.put("logo",Sequel.cariGambar("select setting.logo from setting")); 
            finger=Sequel.cariIsi("select sha1(sidikjari.sidikjari) from sidikjari inner join pegawai on pegawai.id=sidikjari.id where pegawai.nik=?",tbObat.getValueAt(tbObat.getSelectedRow(),4).toString());
            param.put("finger","Dikeluarkan di "+akses.getnamars()+", Kabupaten/Kota "+akses.getkabupatenrs()+"\nDitandatangani secara elektronik oleh "+tbObat.getValueAt(tbObat.getSelectedRow(),5).toString()+"\nID "+(finger.equals("")?tbObat.getValueAt(tbObat.getSelectedRow(),4).toString():finger)+"\n"+Valid.SetTgl3(tbObat.getValueAt(tbObat.getSelectedRow(),6).toString())); 
            
            Valid.MyReportqry("rptCetakHasilPemeriksaanEndosTenggorokan.jasper","report","::[ Formulir Hasil Endoskopi Tenggorokan ]::",
                "select reg_periksa.no_rawat,pasien.no_rkm_medis,pasien.nm_pasien,pasien.tgl_lahir,hasil_endoskopi_tenggorokan.tanggal,"+
                "hasil_endoskopi_tenggorokan.kd_dokter,dokter.nm_dokter,hasil_endoskopi_tenggorokan.diagnosis,"+
                "hasil_endoskopi_tenggorokan.arcus_faring,hasil_endoskopi_tenggorokan.tonsil,hasil_endoskopi_tenggorokan.benda_asing,"+
                "hasil_endoskopi_tenggorokan.dinding_faring,hasil_endoskopi_tenggorokan.epiglotis,hasil_endoskopi_tenggorokan.plika_vocalis,"+
                "hasil_endoskopi_tenggorokan.plika_ventrikularis,"+
                "hasil_endoskopi_tenggorokan.kesimpulan from reg_periksa inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                "inner join hasil_endoskopi_tenggorokan on reg_periksa.no_rawat=hasil_endoskopi_tenggorokan.no_rawat "+
                "inner join dokter on hasil_endoskopi_tenggorokan.kd_dokter=dokter.kd_dokter where hasil_endoskopi_tenggorokan.no_rawat='"+tbObat.getValueAt(tbObat.getSelectedRow(),0).toString()+"'",param);
        }
    }//GEN-LAST:event_MnPenilaianMedisActionPerformed

    private void Janin21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Janin21ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Janin21ActionPerformed

    private void Janin21KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Janin21KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_Janin21KeyPressed

    private void Janin31ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Janin31ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Janin31ActionPerformed

    private void Janin31KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Janin31KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_Janin31KeyPressed

    private void Jani2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Jani2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Jani2ActionPerformed

    private void Jani2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Jani2KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_Jani2KeyPressed

    private void Janin5KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Janin4KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_Janin4KeyPressed

    private void Janin5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Janin4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Janin4ActionPerformed

    private void Arcus_faringActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Arcus_faringActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Arcus_faringActionPerformed

    private void Arcus_faringKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Arcus_faringKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_Arcus_faringKeyPressed

    private void TonsilActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TonsilActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TonsilActionPerformed

    private void TonsilKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TonsilKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_TonsilKeyPressed

    private void BendaAsingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BendaAsingActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_BendaAsingActionPerformed

    private void BendaAsingKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BendaAsingKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BendaAsingKeyPressed

    private void Dinding_faringActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Dinding_faringActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Dinding_faringActionPerformed

    private void Dinding_faringKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Dinding_faringKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_Dinding_faringKeyPressed

    private void EpiglotisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EpiglotisActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_EpiglotisActionPerformed

    private void EpiglotisKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_EpiglotisKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_EpiglotisKeyPressed

    private void Plika_vocalisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Plika_vocalisActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Plika_vocalisActionPerformed

    private void Plika_vocalisKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Plika_vocalisKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_Plika_vocalisKeyPressed

    private void Plika_ventrikularisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Plika_ventrikularisActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Plika_ventrikularisActionPerformed

    private void Plika_ventrikularisKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Plika_ventrikularisKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_Plika_ventrikularisKeyPressed

    private void DiagnosisKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DiagnosisKeyPressed
//        Valid.pindah(evt,Regio,Bsa);
    }//GEN-LAST:event_DiagnosisKeyPressed

    private void KesimpulanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KesimpulanKeyPressed
//        Valid.pindah2(evt,Kista,BtnSimpan);
    }//GEN-LAST:event_KesimpulanKeyPressed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            RMHasilEndoskopiTenggorokan dialog = new RMHasilEndoskopiTenggorokan(new javax.swing.JFrame(), true);
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
    private widget.ComboBox Arcus_faring;
    private widget.ComboBox BendaAsing;
    private widget.Button BtnAll;
    private widget.Button BtnBatal;
    private widget.Button BtnCari;
    private widget.Button BtnDokter;
    private widget.Button BtnEdit;
    private widget.Button BtnHapus;
    private widget.Button BtnKeluar;
    private widget.Button BtnPrint;
    private widget.Button BtnSimpan;
    private widget.Tanggal DTPCari1;
    private widget.Tanggal DTPCari2;
    private widget.TextBox Diagnosis;
    private widget.ComboBox Dinding_faring;
    private widget.ComboBox Epiglotis;
    private widget.PanelBiasa FormInput;
    private widget.TextBox KdDokter;
    private widget.TextArea Kesimpulan;
    private widget.Label LCount;
    private widget.editorpane LoadHTML;
    private javax.swing.JMenuItem MnPenilaianMedis;
    private widget.TextBox NmDokter;
    private widget.ComboBox Plika_ventrikularis;
    private widget.ComboBox Plika_vocalis;
    private widget.ScrollPane Scroll;
    private widget.TextBox TCari;
    private widget.TextBox TNoRM;
    private widget.TextBox TNoRw;
    private widget.TextBox TPasien;
    private javax.swing.JTabbedPane TabRawat;
    private widget.Tanggal Tanggal;
    private widget.TextBox TglLahir;
    private widget.ComboBox Tonsil;
    private widget.InternalFrame internalFrame1;
    private widget.InternalFrame internalFrame2;
    private widget.InternalFrame internalFrame3;
    private widget.Label jLabel10;
    private widget.Label jLabel19;
    private widget.Label jLabel21;
    private widget.Label jLabel33;
    private widget.Label jLabel41;
    private widget.Label jLabel42;
    private widget.Label jLabel43;
    private widget.Label jLabel44;
    private widget.Label jLabel46;
    private widget.Label jLabel47;
    private widget.Label jLabel48;
    private widget.Label jLabel50;
    private widget.Label jLabel6;
    private widget.Label jLabel7;
    private widget.Label jLabel8;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JSeparator jSeparator1;
    private widget.Label label11;
    private widget.Label label14;
    private widget.panelisi panelGlass8;
    private widget.panelisi panelGlass9;
    private widget.ScrollPane scrollInput;
    private widget.ScrollPane scrollPane17;
    private widget.Table tbObat;
    // End of variables declaration//GEN-END:variables

    public void tampil() {
        Valid.tabelKosong(tabMode);
        try{
            if(TCari.getText().trim().equals("")){
                ps=koneksi.prepareStatement(
                     "select reg_periksa.no_rawat,pasien.no_rkm_medis,pasien.nm_pasien,pasien.tgl_lahir,hasil_endoskopi_tenggorokan.tanggal,"+
                        "hasil_endoskopi_tenggorokan.kd_dokter,dokter.nm_dokter,hasil_endoskopi_tenggorokan.diagnosis,"+
                        "hasil_endoskopi_tenggorokan.arcus_faring,hasil_endoskopi_tenggorokan.tonsil,hasil_endoskopi_tenggorokan.benda_asing,"+
                        "hasil_endoskopi_tenggorokan.dinding_faring,hasil_endoskopi_tenggorokan.epiglotis,hasil_endoskopi_tenggorokan.plika_vocalis,"+
                        "hasil_endoskopi_tenggorokan.plika_ventrikularis,"+
                        "hasil_endoskopi_tenggorokan.kesimpulan from reg_periksa inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                        "inner join hasil_endoskopi_tenggorokan on reg_periksa.no_rawat=hasil_endoskopi_tenggorokan.no_rawat "+
                        "inner join dokter on hasil_endoskopi_tenggorokan.kd_dokter=dokter.kd_dokter where "+
                        "hasil_endoskopi_tenggorokan.tanggal between ? and ? order by hasil_endoskopi_tenggorokan.tanggal");
            }else{
                ps=koneksi.prepareStatement(
                        "select reg_periksa.no_rawat,pasien.no_rkm_medis,pasien.nm_pasien,pasien.tgl_lahir,hasil_endoskopi_tenggorokan.tanggal,"+
                      "hasil_endoskopi_tenggorokan.kd_dokter,dokter.nm_dokter,hasil_endoskopi_tenggorokan.diagnosis,"+
                        "hasil_endoskopi_tenggorokan.arcus_faring,hasil_endoskopi_tenggorokan.tonsil,hasil_endoskopi_tenggorokan.benda_asing,"+
                        "hasil_endoskopi_tenggorokan.dinding_faring,hasil_endoskopi_tenggorokan.epiglotis,hasil_endoskopi_tenggorokan.plika_vocalis,"+
                        "hasil_endoskopi_tenggorokan.plika_ventrikularis,"+
                        "hasil_endoskopi_tenggorokan.kesimpulan from reg_periksa inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                        "inner join hasil_endoskopi_tenggorokan on reg_periksa.no_rawat=hasil_endoskopi_tenggorokan.no_rawat "+
                        "inner join dokter on hasil_endoskopi_tenggorokan.kd_dokter=dokter.kd_dokter where "+
                        "hasil_endoskopi_tenggorokan.tanggal between ? and ? and (reg_periksa.no_rawat like ? or pasien.no_rkm_medis like ? or pasien.nm_pasien like ? or "+
                        "hasil_endoskopi_tenggorokan.kd_dokter like ? or dokter.nm_dokter like ?) order by hasil_endoskopi_tenggorokan.tanggal");
            }
                
            try {
                if(TCari.getText().trim().equals("")){
                    ps.setString(1,Valid.SetTgl(DTPCari1.getSelectedItem()+"")+" 00:00:00");
                    ps.setString(2,Valid.SetTgl(DTPCari2.getSelectedItem()+"")+" 23:59:59");
                }else{
                    ps.setString(1,Valid.SetTgl(DTPCari1.getSelectedItem()+"")+" 00:00:00");
                    ps.setString(2,Valid.SetTgl(DTPCari2.getSelectedItem()+"")+" 23:59:59");
                    ps.setString(3,"%"+TCari.getText()+"%");
                    ps.setString(4,"%"+TCari.getText()+"%");
                    ps.setString(5,"%"+TCari.getText()+"%");
                    ps.setString(6,"%"+TCari.getText()+"%");
                    ps.setString(7,"%"+TCari.getText()+"%");
                }   
                rs=ps.executeQuery();
                while(rs.next()){
                    tabMode.addRow(new String[]{
                        rs.getString("no_rawat"),rs.getString("no_rkm_medis"),rs.getString("nm_pasien"),rs.getString("tgl_lahir"),rs.getString("kd_dokter"),rs.getString("nm_dokter"),rs.getString("tanggal"),
                        rs.getString("diagnosis"),rs.getString("arcus_faring"),rs.getString("tonsil"),rs.getString("benda_asing"),rs.getString("dinding_faring"),
                        rs.getString("epiglotis"),rs.getString("plika_vocalis"),rs.getString("plika_ventrikularis"),
                        rs.getString("kesimpulan")
                    });
                }
            } catch (Exception e) {
                System.out.println("Notif : "+e);
            } finally{
                if(rs!=null){
                    rs.close();
                }
                if(ps!=null){
                    ps.close();
                }
            }
            
        }catch(Exception e){
            System.out.println("Notifikasi : "+e);
        }
        LCount.setText(""+tabMode.getRowCount());
    }

    public void emptTeks() {
       
        Diagnosis.setText("");
        Kesimpulan.setText("");
        Tanggal.setDate(new Date());
        TabRawat.setSelectedIndex(0);
        Diagnosis.requestFocus();
    } 

    private void getData() {
        if(tbObat.getSelectedRow()!= -1){
            TNoRw.setText(tbObat.getValueAt(tbObat.getSelectedRow(),0).toString()); 
            TNoRM.setText(tbObat.getValueAt(tbObat.getSelectedRow(),1).toString());
            TPasien.setText(tbObat.getValueAt(tbObat.getSelectedRow(),2).toString());
            TglLahir.setText(tbObat.getValueAt(tbObat.getSelectedRow(),3).toString());
            Valid.SetTgl2(Tanggal,tbObat.getValueAt(tbObat.getSelectedRow(),6).toString());
            Diagnosis.setText(tbObat.getValueAt(tbObat.getSelectedRow(),7).toString());
            Arcus_faring.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),8).toString());
            Tonsil.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),9).toString());
            BendaAsing.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),10).toString());
            Dinding_faring.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),11).toString());
            Epiglotis.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),12).toString());
            Plika_vocalis.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),13).toString());
            Plika_ventrikularis.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),14).toString());
            Kesimpulan.setText(tbObat.getValueAt(tbObat.getSelectedRow(),15).toString());
        }
    }

    private void isRawat() {
        try {
            ps=koneksi.prepareStatement(
                    "select reg_periksa.no_rkm_medis,pasien.nm_pasien, pasien.tgl_lahir,reg_periksa.tgl_registrasi "+
                    "from reg_periksa inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                    "where reg_periksa.no_rawat=?");
            try {
                ps.setString(1,TNoRw.getText());
                rs=ps.executeQuery();
                if(rs.next()){
                    TNoRM.setText(rs.getString("no_rkm_medis"));
                    DTPCari1.setDate(rs.getDate("tgl_registrasi"));
                    TPasien.setText(rs.getString("nm_pasien"));
                    TglLahir.setText(rs.getString("tgl_lahir"));
                }
            } catch (Exception e) {
                System.out.println("Notif : "+e);
            } finally{
                if(rs!=null){
                    rs.close();
                }
                if(ps!=null){
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notif : "+e);
        }
    }
 
    public void setNoRm(String norwt,Date tgl2) {
        TNoRw.setText(norwt);
        TCari.setText(norwt);
        DTPCari2.setDate(tgl2);    
        isRawat(); 
    }
    
    public void isCek(){
        BtnSimpan.setEnabled(akses.gethasil_pemeriksaan_usg());
        BtnHapus.setEnabled(akses.gethasil_pemeriksaan_usg());
        BtnEdit.setEnabled(akses.gethasil_pemeriksaan_usg());
        BtnEdit.setEnabled(akses.gethasil_pemeriksaan_usg());
        if(akses.getjml2()>=1){
            KdDokter.setEditable(false);
            BtnDokter.setEnabled(false);
            KdDokter.setText(akses.getkode());
            NmDokter.setText(dokter.tampil3(KdDokter.getText()));
            if(NmDokter.getText().equals("")){
                KdDokter.setText("");
                JOptionPane.showMessageDialog(null,"User login bukan Dokter...!!");
            }
        }            
    }
    
    public void setTampil(){
       TabRawat.setSelectedIndex(1);
    }

    private void hapus() {
        if(Sequel.queryu2tf("delete from hasil_endoskopi_tenggorokan where no_rawat=?",1,new String[]{
            tbObat.getValueAt(tbObat.getSelectedRow(),0).toString()
        })==true){
            JOptionPane.showMessageDialog(null,"Berhasil Menghapus..!!");
            tabMode.removeRow(tbObat.getSelectedRow());
            LCount.setText(""+tabMode.getRowCount());
            TabRawat.setSelectedIndex(1);
        }else{
            JOptionPane.showMessageDialog(null,"Gagal Menghapus..!!");
        }
    }

    private void ganti() {
          if(Sequel.mengedittf("hasil_endoskopi_tenggorokan","no_rawat=?","no_rawat=?,tanggal=?,kd_dokter=?,diagnosis=?,arcus_faring=?,tonsil=?,benda_asing=?,"+
                "dinding_faring=?,epiglotis=?,plika_vocalis=?,plika_ventrikularis=?,kesimpulan=?",13,new String[]{
                TNoRw.getText(),Valid.SetTgl(Tanggal.getSelectedItem()+"")+" "+Tanggal.getSelectedItem().toString().substring(11,19),KdDokter.getText(),
                Diagnosis.getText(),Arcus_faring.getSelectedItem().toString(),Tonsil.getSelectedItem().toString(),
                BendaAsing.getSelectedItem().toString(),Dinding_faring.getSelectedItem().toString(),Epiglotis.getSelectedItem().toString(),
                Plika_vocalis.getSelectedItem().toString(),Plika_ventrikularis.getSelectedItem().toString(),Kesimpulan.getText(),tbObat.getValueAt(tbObat.getSelectedRow(),0).toString()
            })==true){
              JOptionPane.showMessageDialog(null,"Data Berhasil Diganti");
               tampil();
               emptTeks();
               TabRawat.setSelectedIndex(1);
        }
    }
    
//    private void isPhoto(){
//        if(ChkAccor.isSelected()==true){
//            ChkAccor.setVisible(false);
//            PanelAccor.setPreferredSize(new Dimension(530,HEIGHT));
//            TabData.setVisible(true);  
//            ChkAccor.setVisible(true);
//        }else if(ChkAccor.isSelected()==false){    
//            ChkAccor.setVisible(false);
//            PanelAccor.setPreferredSize(new Dimension(15,HEIGHT));
//            TabData.setVisible(false);  
//            ChkAccor.setVisible(true);
//        }
//    }

//    private void panggilPhoto() {
//        if(FormPhoto.isVisible()==true){
//            try {
//                ps=koneksi.prepareStatement("select hasil_pemeriksaan_usg_gambar.photo from hasil_pemeriksaan_usg_gambar where hasil_pemeriksaan_usg_gambar.no_rawat=?");
//                try {
//                    ps.setString(1,tbObat.getValueAt(tbObat.getSelectedRow(),0).toString());
//                    rs=ps.executeQuery();
//                    if(rs.next()){
//                        if(rs.getString("photo").equals("")||rs.getString("photo").equals("-")){
//                            LoadHTML2.setText("<html><body><center><br><br><font face='tahoma' size='2' color='#434343'>Kosong</font></center></body></html>");
//                        }else{
//                            LoadHTML2.setText("<html><body><center><a href='http://"+koneksiDB.HOSTHYBRIDWEB()+":"+koneksiDB.PORTWEB()+"/"+koneksiDB.HYBRIDWEB()+"/hasilpemeriksaanusg/"+rs.getString("photo")+"'><img src='http://"+koneksiDB.HOSTHYBRIDWEB()+":"+koneksiDB.PORTWEB()+"/"+koneksiDB.HYBRIDWEB()+"/hasilpemeriksaanusg/"+rs.getString("photo")+"' alt='photo' width='550' height='550'/></a></center></body></html>");
//                        }  
//                    }else{
//                        LoadHTML2.setText("<html><body><center><br><br><font face='tahoma' size='2' color='#434343'>Kosong</font></center></body></html>");
//                    }
//                } catch (Exception e) {
//                    System.out.println("Notif : "+e);
//                } finally{
//                    if(rs!=null){
//                        rs.close();
//                    }
//                    if(ps!=null){
//                        ps.close();
//                    }
//                }
//            } catch (Exception e) {
//                System.out.println("Notif : "+e);
//            }
//        }
//    }
    
//    private void tampilOrthanc() {
//        if(TabData.isVisible()==true){
//            if(tbObat.getSelectedRow()!= -1){
//                 if(TabData.getSelectedIndex()==1){
//                     try {
//                         Valid.tabelKosong(tabModeDicom);
//                         ApiOrthanc orthanc=new ApiOrthanc();
//                         root=orthanc.AmbilSeries(tbObat.getValueAt(tbObat.getSelectedRow(),1).toString(),Valid.SetTgl(DTPCari1.getSelectedItem()+"").replaceAll("-",""),Valid.SetTgl(DTPCari2.getSelectedItem()+"").replaceAll("-",""));
//                         for(JsonNode list:root){
//                             for(JsonNode sublist:list.path("Series")){
//                                  tabModeDicom.addRow(new Object[]{
//                                       list.path("PatientMainDicomTags").path("PatientID").asText(),list.path("ID").asText(),sublist.asText()
//                                  });   
//                             }        
//                         }
//                     } catch (Exception e) {
//                         System.out.println("Notif : "+e);
//                     }
//                 }
//            }
//        }
//    }
}
