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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.DocumentEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import kepegawaian.DlgCariDokter;
import kepegawaian.DlgCariPetugas;


/**
 *
 * @author perpustakaan
 */
public final class AsesmenAwalKeperawatanKemoterapi extends javax.swing.JDialog {
    private final DefaultTableModel tabMode,tabModeMasalah,tabModeDetailMasalah,tabModeRencana,tabModeDetailRencana;
    private Connection koneksi=koneksiDB.condb();
    private sekuel Sequel=new sekuel();
    private validasi Valid=new validasi();
    private PreparedStatement ps;
    private ResultSet rs;
    private int i=0,jml=0,index=0;
    private DlgCariPetugas petugas=new DlgCariPetugas(null,false);
    private DlgCariDokter dokter=new DlgCariDokter(null,false);
    private StringBuilder htmlContent;
    private String pilihan="";
    private boolean[] pilih; 
    private String[] kode,masalah;
    private String masalahkeperawatan="",finger=""; 
    private File file;
    private FileWriter fileWriter;
    private String iyem;
    private ObjectMapper mapper = new ObjectMapper();
    private JsonNode root;
    private JsonNode response;
    private FileReader myObj;
    
    /** Creates new form DlgRujuk
     * @param parent
     * @param modal */
    public AsesmenAwalKeperawatanKemoterapi(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        tabMode=new DefaultTableModel(null,new Object[]{
            "No.Rawat","No.RM","Nama Pasien","Tgl.Lahir","J.K.","NIP Pengkaji 1","Nama Pengkaji 1","NIP Pengkaji 2","Nama Pengkaji 2","Kode DPJP","Nama DPJP",
            "Tgl.Asuhan","Macam Kasus","Anamnesis","Cara Masuk","Asal Pasien","Keluhan Utama","Riwayat Keluhan","Riwayat Penyakit Keluarga",
            "Riwayat Penggunaan Obat","Resus","Kemoterapi Terdahulu","Kemoterapi Lainnya","Riwayat Ekstravasasi","Golongan Darah",
            "Tindakan Perawatan Lainnya","Hasil Lab","Keterangan Lab","BMP","Keterangan BMP","Tindakan Keperawatan","Riwayat GVHD","Kesadaran Mental","Keadaan Umum","GCS(E,V,M)","TD(mmHg)",
            "Nadi(x/menit)","RR(x/menit)","Suhu(°C)","SpO2(%)","BB(Kg)","TB(cm)","Kepala","Wajah","Leher","Kejang","Sensorik","Pulsasi","Sirkulasi","Denyut Nadi",
            "Retraksi","Pola Nafas","Suara Nafas","Batuk & Sekresi","Volume","Jenis Pernafasaan","Irama","Mulut","Lidah","Gigi","Tenggorokan","Abdomen","Peistatik Usus",
            "Anus","Sensorik","Penglihatan","Alat Bantu Penglihatan","Motorik","Pendengaran","Bicara","Otot","Kulit","Warna Kulit","Turgor","Resiko Decubitas",
            "Oedema","Pergerakan Sendi","Kekuatan Otot","Fraktur","Nyeri Sendi","Frekuensi BAB","x/","Konsistensi BAB","Warna BAB","Frekuensi BAK","x/","Warna BAK",
            "Lain-lain BAK","Lesi","Cairan Abnormal","Bau","Alat Bantu","Berpindah","Volume Genitalia","Provocative (P)","Quality (Q)","Regio (R)","Gangguan Tidur",
            "a. Aktifitas Sehari-hari","Severity (S)","c. Aktifitas","d. Alat Ambulasi","Time (T)","f. Ekstremitas Bawah","Pengobatan Alternatif Sebelumnya",
            "h. Kemampuan Koordinasi","i. Kesimpulan Gangguan Fungsi","Psikologis","Pencari Nafkah Utama","Kesediaan Menerima Informasi","Hubungan Anggota Keluarga",
            "e. Agama","Tinggal Dengan","g. Pekerjaan","h. Pembayaran","i. Nilai-nilai Kepercayaan","j. Bahasa Sehari-hari","k. Pendidikan Pasien","l. Pendidikan P.J.",
            "m. Edukasi Diberikan Kepada","Nyeri","Penyebab Nyeri","Kualitas Nyeri","Lokasi Ekstravasasi","Nyeri Menyebar","Skala Nyeri","Waktu / Durasi","Nyeri Hilang Bila",
            "Diberitahukan Pada Dokter","Skala Morse 1","N.M. 1","Skala Morse 2","N.M. 2","Skala Morse 3","N.M. 3","Skala Morse 4","N.M. 4","Skala Morse 5","N.M. 5",
            "Skala Morse 6","N.M. 6","T.M.","Skala Sydney 1","N.S. 1","Skala Sydney 2","N.S. 2","Skala Sydney 3","N.S. 3","Skala Sydney 4","N.S. 4",
            "Skala Sydney 5","N.S. 5","Skala Sydney 6","N.S. 6","Skala Sydney 7","N.S. 7","Skala Sydney 8","N.S. 8","Skala Sydney 9","N.S. 9","Skala Sydney 10","N.S. 10",
            "Skala Sydney 11","N.S. 11","T.S.","1. Apakah ada penurunan BB yang tidak diinginkan selama 6 bulan terakhir ?","Skor 1","2. Apakah asupan makan berkurang karena tidak nafsu makan ?",
            "Skor 2","Total Skor","Pasien dengan diagnosis khusus","Keterangan Diagnosa Khusus","Sudah dibaca dan diketahui oleh Dietisen","Jam Dibaca Dietisen",
            "Rencana Keperawatan Lainnya"
        }){
              @Override public boolean isCellEditable(int rowIndex, int colIndex){return false;}
        };
        tbObat.setModel(tabMode);

        //tbObat.setDefaultRenderer(Object.class, new WarnaTable(panelJudul.getBackground(),tbObat.getBackground()));
        tbObat.setPreferredScrollableViewportSize(new Dimension(500,500));
        tbObat.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 175; i++) {
            TableColumn column = tbObat.getColumnModel().getColumn(i);
            if(i==0){
                column.setPreferredWidth(105);
            }else if(i==1){
                column.setPreferredWidth(65);
            }else if(i==2){
                column.setPreferredWidth(160);
            }else if(i==3){
                column.setPreferredWidth(65);
            }else if(i==4){
                column.setPreferredWidth(25);
            }else if(i==5){
                column.setPreferredWidth(85);
            }else if(i==6){
                column.setPreferredWidth(150);
            }else if(i==7){
                column.setPreferredWidth(85);
            }else if(i==8){
                column.setPreferredWidth(150);
            }else if(i==9){
                column.setPreferredWidth(90);
            }else if(i==10){
                column.setPreferredWidth(150);
            }else if(i==11){
                column.setPreferredWidth(117);
            }else if(i==12){
                column.setPreferredWidth(78);
            }else if(i==13){
                column.setPreferredWidth(150);
            }else if(i==14){
                column.setPreferredWidth(110);
            }else if(i==15){
                column.setPreferredWidth(70);
            }else if(i==16){
                column.setPreferredWidth(220);
            }else if(i==17){
                column.setPreferredWidth(170);
            }else if(i==18){
                column.setPreferredWidth(170);
            }else if(i==19){
                column.setPreferredWidth(170);
            }else if(i==20){
                column.setPreferredWidth(150);
            }else if(i==21){
                column.setPreferredWidth(150);
            }else if(i==22){
                column.setPreferredWidth(125);
            }else if(i==23){
                column.setPreferredWidth(210);
            }else if(i==24){
                column.setPreferredWidth(130);
            }else if(i==25){
                column.setPreferredWidth(130);
            }else if(i==26){
                column.setPreferredWidth(48);
            }else if(i==27){
                column.setPreferredWidth(65);
            }else if(i==28){
                column.setPreferredWidth(44);
            }else if(i==29){
                column.setPreferredWidth(59);
            }else if(i==30){
                column.setPreferredWidth(61);
            }else if(i==31){
                column.setPreferredWidth(59);
            }else if(i==32){
                column.setPreferredWidth(120);
            }else if(i==33){
                column.setPreferredWidth(85);
            }else if(i==34){
                column.setPreferredWidth(64);
            }else if(i==35){
                column.setPreferredWidth(60);
            }else if(i==36){
                column.setPreferredWidth(74);
            }else if(i==37){
                column.setPreferredWidth(67);
            }else if(i==38){
                column.setPreferredWidth(52);
            }else if(i==39){
                column.setPreferredWidth(52);
            }else if(i==40){
                column.setPreferredWidth(44);
            }else if(i==41){
                column.setPreferredWidth(44);
            }else if(i==42){
                column.setPreferredWidth(150);
            }else if(i==43){
                column.setPreferredWidth(150);
            }else if(i==44){
                column.setPreferredWidth(106);
            }else if(i==45){
                column.setPreferredWidth(130);
            }else if(i==46){
                column.setPreferredWidth(65);
            }else if(i==47){
                column.setPreferredWidth(50);
            }else if(i==48){
                column.setPreferredWidth(130);
            }else if(i==49){
                column.setPreferredWidth(72);
            }else if(i==50){
                column.setPreferredWidth(54);
            }else if(i==51){
                column.setPreferredWidth(63);
            }else if(i==52){
                column.setPreferredWidth(69);
            }else if(i==53){
                column.setPreferredWidth(97);
            }else if(i==54){
                column.setPreferredWidth(75);
            }else if(i==55){
                column.setPreferredWidth(170);
            }else if(i==56){
                column.setPreferredWidth(70);
            }else if(i==57){
                column.setPreferredWidth(140);
            }else if(i==58){
                column.setPreferredWidth(140);
            }else if(i==59){
                column.setPreferredWidth(140);
            }else if(i==60){
                column.setPreferredWidth(140);
            }else if(i==61){
                column.setPreferredWidth(140);
            }else if(i==62){
                column.setPreferredWidth(111);
            }else if(i==63){
                column.setPreferredWidth(60);
            }else if(i==64){
                column.setPreferredWidth(60);
            }else if(i==65){
                column.setPreferredWidth(140);
            }else if(i==66){
                column.setPreferredWidth(119);
            }else if(i==67){
                column.setPreferredWidth(65);
            }else if(i==68){
                column.setPreferredWidth(74);
            }else if(i==69){
                column.setPreferredWidth(140);
            }else if(i==70){
                column.setPreferredWidth(41);
            }else if(i==71){
                column.setPreferredWidth(91);
            }else if(i==72){
                column.setPreferredWidth(66);
            }else if(i==73){
                column.setPreferredWidth(44);
            }else if(i==74){
                column.setPreferredWidth(159);
            }else if(i==75){
                column.setPreferredWidth(140);
            }else if(i==76){
                column.setPreferredWidth(94);
            }else if(i==77){
                column.setPreferredWidth(79);
            }else if(i==78){
                column.setPreferredWidth(140);
            }else if(i==79){
                column.setPreferredWidth(140);
            }else if(i==80){
                column.setPreferredWidth(79);
            }else if(i==81){
                column.setPreferredWidth(80);
            }else if(i==82){
                column.setPreferredWidth(85);
            }else if(i==83){
                column.setPreferredWidth(80);
            }else if(i==84){
                column.setPreferredWidth(79);
            }else if(i==85){
                column.setPreferredWidth(80);
            }else if(i==86){
                column.setPreferredWidth(80);
            }else if(i==87){
                column.setPreferredWidth(80);
            }else if(i==88){
                column.setPreferredWidth(103);
            }else if(i==89){
                column.setPreferredWidth(103);
            }else if(i==90){
                column.setPreferredWidth(103);
            }else if(i==91){
                column.setPreferredWidth(103);
            }else if(i==92){
                column.setPreferredWidth(103);
            }else if(i==93){
                column.setPreferredWidth(68);
            }else if(i==94){
                column.setPreferredWidth(90);
            }else if(i==95){
                column.setPreferredWidth(140);
            }else if(i==96){
                column.setPreferredWidth(65);
            }else if(i==97){
                column.setPreferredWidth(108);
            }else if(i==98){
                column.setPreferredWidth(120);
            }else if(i==99){
                column.setPreferredWidth(180);
            }else if(i==100){
                column.setPreferredWidth(67);
            }else if(i==101){
                column.setPreferredWidth(104);
            }else if(i==102){
                column.setPreferredWidth(140);
            }else if(i==103){
                column.setPreferredWidth(140);
            }else if(i==104){
                column.setPreferredWidth(170);
            }else if(i==105){
                column.setPreferredWidth(170);
            }else if(i==106){
                column.setPreferredWidth(161);
            }else if(i==107){
                column.setPreferredWidth(106);
            }else if(i==108){
                column.setPreferredWidth(250);
            }else if(i==109){
                column.setPreferredWidth(157);
            }else if(i==110){
                column.setPreferredWidth(105);
            }else if(i==111){
                column.setPreferredWidth(55);
            }else if(i==112){
                column.setPreferredWidth(140);
            }else if(i==113){
                column.setPreferredWidth(90);
            }else if(i==114){
                column.setPreferredWidth(90);
            }else if(i==115){
                column.setPreferredWidth(150);
            }else if(i==116){
                column.setPreferredWidth(110);
            }else if(i==117){
                column.setPreferredWidth(110);
            }else if(i==118){
                column.setPreferredWidth(95);
            }else if(i==119){
                column.setPreferredWidth(150);
            }else if(i==120){
                column.setPreferredWidth(80);
            }else if(i==121){
                column.setPreferredWidth(140);
            }else if(i==122){
                column.setPreferredWidth(140);
            }else if(i==123){
                column.setPreferredWidth(100);
            }else if(i==124){
                column.setPreferredWidth(85);
            }else if(i==125){
                column.setPreferredWidth(65);
            }else if(i==126){
                column.setPreferredWidth(80);
            }else if(i==127){
                column.setPreferredWidth(140);
            }else if(i==128){
                column.setPreferredWidth(140);
            }else if(i==129){
                column.setPreferredWidth(77);
            }else if(i==130){
                column.setPreferredWidth(40);
            }else if(i==131){
                column.setPreferredWidth(77);
            }else if(i==132){
                column.setPreferredWidth(40);
            }else if(i==133){
                column.setPreferredWidth(177);
            }else if(i==134){
                column.setPreferredWidth(40);
            }else if(i==135){
                column.setPreferredWidth(77);
            }else if(i==136){
                column.setPreferredWidth(40);
            }else if(i==137){
                column.setPreferredWidth(162);
            }else if(i==138){
                column.setPreferredWidth(40);
            }else if(i==139){
                column.setPreferredWidth(162);
            }else if(i==140){
                column.setPreferredWidth(40);
            }else if(i==141){
                column.setPreferredWidth(40);
            }else if(i==142){
                column.setPreferredWidth(82);
            }else if(i==143){
                column.setPreferredWidth(40);
            }else if(i==144){
                column.setPreferredWidth(82);
            }else if(i==145){
                column.setPreferredWidth(40);
            }else if(i==146){
                column.setPreferredWidth(82);
            }else if(i==147){
                column.setPreferredWidth(40);
            }else if(i==148){
                column.setPreferredWidth(82);
            }else if(i==149){
                column.setPreferredWidth(40);
            }else if(i==150){
                column.setPreferredWidth(82);
            }else if(i==151){
                column.setPreferredWidth(40);
            }else if(i==152){
                column.setPreferredWidth(82);
            }else if(i==153){
                column.setPreferredWidth(40);
            }else if(i==154){
                column.setPreferredWidth(82);
            }else if(i==155){
                column.setPreferredWidth(40);
            }else if(i==156){
                column.setPreferredWidth(82);
            }else if(i==157){
                column.setPreferredWidth(40);
            }else if(i==158){
                column.setPreferredWidth(82);
            }else if(i==159){
                column.setPreferredWidth(40);
            }else if(i==160){
                column.setPreferredWidth(86);
            }else if(i==161){
                column.setPreferredWidth(44);
            }else if(i==162){
                column.setPreferredWidth(86);
            }else if(i==163){
                column.setPreferredWidth(44);
            }else if(i==164){
                column.setPreferredWidth(40);
//            }else if(i==165){
//                column.setPreferredWidth(40);
            }else if(i==165){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==166){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==167){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==168){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==169){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==170){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==171){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==172){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==173){
                column.setMinWidth(0);
                column.setMaxWidth(0);
//                Akhir
            }else if(i==174){
                column.setPreferredWidth(200);
            }
        }
        tbObat.setDefaultRenderer(Object.class, new WarnaTable());
        
        TNoRw.setDocument(new batasInput((byte)17).getKata(TNoRw));
        KetAnamnesis.setDocument(new batasInput((byte)30).getKata(KetAnamnesis));
        RPS.setDocument(new batasInput((int)3000).getKata(RPS));
        RPD.setDocument(new batasInput((int)3000).getKata(RPD));
        RPK.setDocument(new batasInput((int)3000).getKata(RPK));
        RPO.setDocument(new batasInput((int)3000).getKata(RPO));
        RPembedahan.setDocument(new batasInput((int)1000).getKata(RPembedahan));
        RDirawatRS.setDocument(new batasInput((int)1000).getKata(RDirawatRS));
        RTranfusi.setDocument(new batasInput((int)1000).getKata(RTranfusi));
        Alergi.setDocument(new batasInput((int)1000).getKata(Alergi));
        KetSedangMenyusui.setDocument(new batasInput((int)100).getKata(KetSedangMenyusui));
        KebiasaanJumlahRokok.setDocument(new batasInput((int)100).getKata(KebiasaanJumlahRokok));
        KebiasaanJumlahAlkohol.setDocument(new batasInput((int)100).getKata(KebiasaanJumlahAlkohol));
        KesadaranMental.setDocument(new batasInput((int)100).getKata(KesadaranMental));
        GCS.setDocument(new batasInput((byte)10).getKata(GCS));
        TD.setDocument(new batasInput((byte)8).getKata(TD));
        Nadi.setDocument(new batasInput((byte)5).getKata(Nadi));
        RR.setDocument(new batasInput((byte)5).getKata(RR));
        Suhu.setDocument(new batasInput((byte)5).getKata(Suhu));
        SpO2.setDocument(new batasInput((byte)5).getKata(SpO2));
        BB.setDocument(new batasInput((byte)5).getKata(BB));
        TB.setDocument(new batasInput((byte)5).getKata(TB));
        KetSistemSarafKepala.setDocument(new batasInput((int)100).getKata(KetSistemSarafKepala));
        KetSistemSarafWajah.setDocument(new batasInput((int)100).getKata(KetSistemSarafWajah));
        KetSistemSarafKejang.setDocument(new batasInput((int)100).getKata(KetSistemSarafKejang));
        KetKardiovaskularSirkulasi.setDocument(new batasInput((int)100).getKata(KetKardiovaskularSirkulasi));
        KetRespirasiJenisPernafasan.setDocument(new batasInput((int)100).getKata(KetRespirasiJenisPernafasan));
        KetGastrointestinalMulut.setDocument(new batasInput((int)100).getKata(KetGastrointestinalMulut));
        KetGastrointestinalLidah.setDocument(new batasInput((int)100).getKata(KetGastrointestinalLidah));
        KetGastrointestinalGigi.setDocument(new batasInput((int)100).getKata(KetGastrointestinalGigi));
        KetGastrointestinalTenggorakan.setDocument(new batasInput((int)100).getKata(KetGastrointestinalTenggorakan));
        KetGastrointestinalAbdomen.setDocument(new batasInput((int)100).getKata(KetGastrointestinalAbdomen));
        KetNeurologiPenglihatan.setDocument(new batasInput((int)100).getKata(KetNeurologiPenglihatan));
        KetNeurologiBicara.setDocument(new batasInput((int)100).getKata(KetNeurologiBicara));
        KetMuskuloskletalOedema.setDocument(new batasInput((int)100).getKata(KetMuskuloskletalOedema));
        KetMuskuloskletalFraktur.setDocument(new batasInput((int)100).getKata(KetMuskuloskletalFraktur));
        KetMuskuloskletalNyeriSendi.setDocument(new batasInput((int)100).getKata(KetMuskuloskletalNyeriSendi));
        BAB.setDocument(new batasInput((byte)5).getKata(BAB));
        XBAB.setDocument(new batasInput((byte)10).getKata(XBAB));
        KBAB.setDocument(new batasInput((byte)30).getKata(KBAB));
        WBAB.setDocument(new batasInput((byte)30).getKata(WBAB));
        BAK.setDocument(new batasInput((byte)5).getKata(BAK));
        XBAK.setDocument(new batasInput((byte)10).getKata(XBAK));
        WBAK.setDocument(new batasInput((byte)30).getKata(WBAK));
        LBAK.setDocument(new batasInput((byte)30).getKata(LBAK));
        PolaNutrisiPorsi.setDocument(new batasInput((int)100).getKata(PolaNutrisiPorsi));
        PolaNutrisiFrekuensi.setDocument(new batasInput((int)100).getKata(PolaNutrisiFrekuensi));
        PolaNutrisiJenis.setDocument(new batasInput((int)100).getKata(PolaNutrisiJenis));
        PolaTidurLama.setDocument(new batasInput((int)100).getKata(PolaTidurLama));
        KeteranganBerjalan.setDocument(new batasInput((int)100).getKata(KeteranganBerjalan));
        KeteranganEkstrimitasAtas.setDocument(new batasInput((int)100).getKata(KeteranganEkstrimitasAtas));
        KeteranganEkstrimitasBawah.setDocument(new batasInput((int)100).getKata(KeteranganEkstrimitasBawah));
        KeteranganKemampuanMenggenggam.setDocument(new batasInput((int)100).getKata(KeteranganKemampuanMenggenggam));
        KeteranganKemampuanKoordinasi.setDocument(new batasInput((int)100).getKata(KeteranganKemampuanKoordinasi));
        KeteranganAdakahPerilaku.setDocument(new batasInput((int)100).getKata(KeteranganAdakahPerilaku));
        KeteranganTinggalDengan.setDocument(new batasInput((int)100).getKata(KeteranganTinggalDengan));
        KeteranganNilaiKepercayaan.setDocument(new batasInput((int)100).getKata(KeteranganNilaiKepercayaan));
        KeteranganEdukasiPsikologis.setDocument(new batasInput((int)100).getKata(KeteranganEdukasiPsikologis));
        KetProvokes.setDocument(new batasInput((int)100).getKata(KetProvokes));
        KetQuality.setDocument(new batasInput((int)100).getKata(KetQuality));
        Lokasi.setDocument(new batasInput((int)100).getKata(Lokasi));
        Durasi.setDocument(new batasInput((int)100).getKata(Durasi));
        KetNyeri.setDocument(new batasInput((int)100).getKata(KetNyeri));
        KetPadaDokter.setDocument(new batasInput((int)100).getKata(KetPadaDokter));
        KeteranganDiagnosaKhususGizi.setDocument(new batasInput((int)100).getKata(KeteranganDiagnosaKhususGizi));
        KeteranganDiketahuiDietisen.setDocument(new batasInput((int)100).getKata(KeteranganDiketahuiDietisen));
        Rencana.setDocument(new batasInput((int)3000).getKata(Rencana));
        
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
        
        petugas.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e) {}
            @Override
            public void windowClosed(WindowEvent e) {
                if(petugas.getTable().getSelectedRow()!= -1){ 
                    if(i==1){
                        KdPetugas.setText(petugas.getTable().getValueAt(petugas.getTable().getSelectedRow(),0).toString());
                        NmPetugas.setText(petugas.getTable().getValueAt(petugas.getTable().getSelectedRow(),1).toString());  
                    }else{
                        KdPetugas2.setText(petugas.getTable().getValueAt(petugas.getTable().getSelectedRow(),0).toString());
                        NmPetugas2.setText(petugas.getTable().getValueAt(petugas.getTable().getSelectedRow(),1).toString());  
                    }
                         
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
        
        dokter.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e) {}
            @Override
            public void windowClosed(WindowEvent e) {
                if(dokter.getTable().getSelectedRow()!= -1){ 
                    KdDPJP.setText(dokter.getTable().getValueAt(dokter.getTable().getSelectedRow(),0).toString());
                    NmDPJP.setText(dokter.getTable().getValueAt(dokter.getTable().getSelectedRow(),1).toString());  
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
        
        tabModeMasalah=new DefaultTableModel(null,new Object[]{
                "P","KODE","MASALAH KEPERAWATAN"
            }){
             @Override public boolean isCellEditable(int rowIndex, int colIndex){
                boolean a = false;
                if (colIndex==0) {
                    a=true;
                }
                return a;
             }
             Class[] types = new Class[] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Double.class
             };
             @Override
             public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
             }
        };
        tbMasalahKeperawatan.setModel(tabModeMasalah);

        //tbObat.setDefaultRenderer(Object.class, new WarnaTable(panelJudul.getBackground(),tbObat.getBackground()));
        tbMasalahKeperawatan.setPreferredScrollableViewportSize(new Dimension(500,500));
        tbMasalahKeperawatan.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        for (i = 0; i < 3; i++) {
            TableColumn column = tbMasalahKeperawatan.getColumnModel().getColumn(i);
            if(i==0){
                column.setPreferredWidth(20);
            }else if(i==1){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==2){
                column.setPreferredWidth(350);
            }
        }
        tbMasalahKeperawatan.setDefaultRenderer(Object.class, new WarnaTable());
        
        tabModeRencana=new DefaultTableModel(null,new Object[]{
                "P","KODE","RENCANA KEPERAWATAN"
            }){
             @Override public boolean isCellEditable(int rowIndex, int colIndex){
                boolean a = false;
                if (colIndex==0) {
                    a=true;
                }
                return a;
             }
             Class[] types = new Class[] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Double.class
             };
             @Override
             public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
             }
        };
        tbRencanaKeperawatan.setModel(tabModeRencana);

        //tbObat.setDefaultRenderer(Object.class, new WarnaTable(panelJudul.getBackground(),tbObat.getBackground()));
        tbRencanaKeperawatan.setPreferredScrollableViewportSize(new Dimension(500,500));
        tbRencanaKeperawatan.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        for (i = 0; i < 3; i++) {
            TableColumn column = tbRencanaKeperawatan.getColumnModel().getColumn(i);
            if(i==0){
                column.setPreferredWidth(20);
            }else if(i==1){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==2){
                column.setPreferredWidth(350);
            }
        }
        tbRencanaKeperawatan.setDefaultRenderer(Object.class, new WarnaTable());
        
        tabModeDetailMasalah=new DefaultTableModel(null,new Object[]{
                "Kode","Masalah Keperawatan"
            }){
              @Override public boolean isCellEditable(int rowIndex, int colIndex){return false;}
        };
        tbMasalahDetail.setModel(tabModeDetailMasalah);

        //tbObat.setDefaultRenderer(Object.class, new WarnaTable(panelJudul.getBackground(),tbObat.getBackground()));
        tbMasalahDetail.setPreferredScrollableViewportSize(new Dimension(500,500));
        tbMasalahDetail.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 2; i++) {
            TableColumn column = tbMasalahDetail.getColumnModel().getColumn(i);
            if(i==0){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==1){
                column.setPreferredWidth(420);
            }
        }
        tbMasalahDetail.setDefaultRenderer(Object.class, new WarnaTable());
        
        tabModeDetailRencana=new DefaultTableModel(null,new Object[]{
                "Kode","Rencana Keperawatan"
            }){
              @Override public boolean isCellEditable(int rowIndex, int colIndex){return false;}
        };
        tbRencanaDetail.setModel(tabModeDetailRencana);

        //tbObat.setDefaultRenderer(Object.class, new WarnaTable(panelJudul.getBackground(),tbObat.getBackground()));
        tbRencanaDetail.setPreferredScrollableViewportSize(new Dimension(500,500));
        tbRencanaDetail.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 2; i++) {
            TableColumn column = tbRencanaDetail.getColumnModel().getColumn(i);
            if(i==0){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==1){
                column.setPreferredWidth(420);
            }
        }
        tbRencanaDetail.setDefaultRenderer(Object.class, new WarnaTable());
        
        ChkAccor.setSelected(false);
        isMenu();
    }


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

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
        KdPetugas = new widget.TextBox();
        NmPetugas = new widget.TextBox();
        BtnPetugas = new widget.Button();
        jLabel8 = new widget.Label();
        TglLahir = new widget.TextBox();
        Jk = new widget.TextBox();
        jLabel10 = new widget.Label();
        label11 = new widget.Label();
        jLabel11 = new widget.Label();
        jLabel36 = new widget.Label();
        Anamnesis = new widget.ComboBox();
        TglAsuhan = new widget.Tanggal();
        NmPetugas2 = new widget.TextBox();
        BtnPetugas2 = new widget.Button();
        KdPetugas2 = new widget.TextBox();
        label15 = new widget.Label();
        label16 = new widget.Label();
        KdDPJP = new widget.TextBox();
        NmDPJP = new widget.TextBox();
        BtnDPJP = new widget.Button();
        TibadiRuang = new widget.ComboBox();
        jLabel37 = new widget.Label();
        CaraMasuk = new widget.ComboBox();
        jLabel38 = new widget.Label();
        jLabel94 = new widget.Label();
        jLabel9 = new widget.Label();
        Alergi = new widget.TextBox();
        scrollPane1 = new widget.ScrollPane();
        RPS = new widget.TextArea();
        jLabel30 = new widget.Label();
        scrollPane2 = new widget.ScrollPane();
        RPK = new widget.TextArea();
        jLabel31 = new widget.Label();
        scrollPane3 = new widget.ScrollPane();
        RPD = new widget.TextArea();
        jLabel32 = new widget.Label();
        scrollPane4 = new widget.ScrollPane();
        RPO = new widget.TextArea();
        jSeparator2 = new javax.swing.JSeparator();
        MacamKasus = new widget.ComboBox();
        jLabel41 = new widget.Label();
        KetAnamnesis = new widget.TextBox();
        RDirawatRS = new widget.TextBox();
        RPembedahan = new widget.TextBox();
        jLabel42 = new widget.Label();
        jLabel43 = new widget.Label();
        AlatBantuDipakai = new widget.ComboBox();
        SedangMenyusui = new widget.ComboBox();
        jLabel44 = new widget.Label();
        KetSedangMenyusui = new widget.TextBox();
        jLabel45 = new widget.Label();
        RTranfusi = new widget.TextBox();
        KebiasaanMerokok = new widget.ComboBox();
        KebiasaanJumlahRokok = new widget.TextBox();
        KebiasaanAlkohol = new widget.ComboBox();
        KebiasaanJumlahAlkohol = new widget.TextBox();
        jLabel127 = new widget.Label();
        KebiasaanNarkoba = new widget.ComboBox();
        jLabel128 = new widget.Label();
        jLabel129 = new widget.Label();
        OlahRaga = new widget.ComboBox();
        jLabel95 = new widget.Label();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel47 = new widget.Label();
        KesadaranMental = new widget.TextBox();
        jLabel130 = new widget.Label();
        KeadaanMentalUmum = new widget.ComboBox();
        jLabel28 = new widget.Label();
        GCS = new widget.TextBox();
        jLabel22 = new widget.Label();
        TD = new widget.TextBox();
        jLabel23 = new widget.Label();
        jLabel17 = new widget.Label();
        Nadi = new widget.TextBox();
        jLabel16 = new widget.Label();
        jLabel26 = new widget.Label();
        RR = new widget.TextBox();
        jLabel25 = new widget.Label();
        jLabel18 = new widget.Label();
        Suhu = new widget.TextBox();
        jLabel20 = new widget.Label();
        jLabel24 = new widget.Label();
        SpO2 = new widget.TextBox();
        jLabel29 = new widget.Label();
        jLabel12 = new widget.Label();
        BB = new widget.TextBox();
        jLabel13 = new widget.Label();
        jLabel15 = new widget.Label();
        TB = new widget.TextBox();
        jLabel48 = new widget.Label();
        jLabel27 = new widget.Label();
        jLabel131 = new widget.Label();
        SistemSarafKepala = new widget.ComboBox();
        KetSistemSarafKepala = new widget.TextBox();
        jLabel132 = new widget.Label();
        SistemSarafWajah = new widget.ComboBox();
        KetSistemSarafWajah = new widget.TextBox();
        jLabel133 = new widget.Label();
        SistemSarafLeher = new widget.ComboBox();
        jLabel134 = new widget.Label();
        SistemSarafSensorik = new widget.ComboBox();
        jLabel135 = new widget.Label();
        SistemSarafKejang = new widget.ComboBox();
        KetSistemSarafKejang = new widget.TextBox();
        jLabel33 = new widget.Label();
        jLabel136 = new widget.Label();
        KardiovaskularPulsasi = new widget.ComboBox();
        jLabel137 = new widget.Label();
        KardiovaskularSirkulasi = new widget.ComboBox();
        KetKardiovaskularSirkulasi = new widget.TextBox();
        jLabel138 = new widget.Label();
        KardiovaskularDenyutNadi = new widget.ComboBox();
        jLabel35 = new widget.Label();
        jLabel139 = new widget.Label();
        RespirasiRetraksi = new widget.ComboBox();
        RespirasiPolaNafas = new widget.ComboBox();
        jLabel140 = new widget.Label();
        jLabel141 = new widget.Label();
        RespirasiSuaraNafas = new widget.ComboBox();
        jLabel142 = new widget.Label();
        RespirasiIrama = new widget.ComboBox();
        jLabel143 = new widget.Label();
        RespirasiVolume = new widget.ComboBox();
        jLabel144 = new widget.Label();
        RespirasiJenisPernafasan = new widget.ComboBox();
        KetRespirasiJenisPernafasan = new widget.TextBox();
        jLabel145 = new widget.Label();
        RespirasiBatuk = new widget.ComboBox();
        jLabel49 = new widget.Label();
        jLabel146 = new widget.Label();
        GastrointestinalMulut = new widget.ComboBox();
        KetGastrointestinalMulut = new widget.TextBox();
        GastrointestinalGigi = new widget.ComboBox();
        jLabel148 = new widget.Label();
        KetGastrointestinalGigi = new widget.TextBox();
        jLabel149 = new widget.Label();
        GastrointestinalAnus = new widget.ComboBox();
        jLabel147 = new widget.Label();
        GastrointestinalLidah = new widget.ComboBox();
        KetGastrointestinalLidah = new widget.TextBox();
        jLabel150 = new widget.Label();
        GastrointestinalTenggorakan = new widget.ComboBox();
        KetGastrointestinalTenggorakan = new widget.TextBox();
        jLabel151 = new widget.Label();
        GastrointestinalAbdomen = new widget.ComboBox();
        KetGastrointestinalAbdomen = new widget.TextBox();
        jLabel152 = new widget.Label();
        GastrointestinalUsus = new widget.ComboBox();
        jLabel50 = new widget.Label();
        jLabel153 = new widget.Label();
        NeurologiSensorik = new widget.ComboBox();
        jLabel154 = new widget.Label();
        NeurologiMotorik = new widget.ComboBox();
        jLabel155 = new widget.Label();
        NeurologiOtot = new widget.ComboBox();
        jLabel156 = new widget.Label();
        NeurologiPenglihatan = new widget.ComboBox();
        KetNeurologiPenglihatan = new widget.TextBox();
        jLabel157 = new widget.Label();
        NeurologiAlatBantuPenglihatan = new widget.ComboBox();
        jLabel158 = new widget.Label();
        NeurologiPendengaran = new widget.ComboBox();
        jLabel159 = new widget.Label();
        NeurologiBicara = new widget.ComboBox();
        KetNeurologiBicara = new widget.TextBox();
        jLabel51 = new widget.Label();
        jLabel160 = new widget.Label();
        IntegumentKulit = new widget.ComboBox();
        jLabel161 = new widget.Label();
        IntegumentWarnaKulit = new widget.ComboBox();
        jLabel162 = new widget.Label();
        IntegumentTurgor = new widget.ComboBox();
        IntegumentDecubitus = new widget.ComboBox();
        jLabel163 = new widget.Label();
        jLabel52 = new widget.Label();
        jLabel164 = new widget.Label();
        MuskuloskletalOedema = new widget.ComboBox();
        KetMuskuloskletalOedema = new widget.TextBox();
        KetMuskuloskletalFraktur = new widget.TextBox();
        MuskuloskletalFraktur = new widget.ComboBox();
        jLabel165 = new widget.Label();
        jLabel166 = new widget.Label();
        MuskuloskletalNyeriSendi = new widget.ComboBox();
        KetMuskuloskletalNyeriSendi = new widget.TextBox();
        jLabel167 = new widget.Label();
        MuskuloskletalPegerakanSendi = new widget.ComboBox();
        MuskuloskletalKekuatanOtot = new widget.ComboBox();
        jLabel168 = new widget.Label();
        jLabel53 = new widget.Label();
        jLabel113 = new widget.Label();
        KBAB = new widget.TextBox();
        jLabel114 = new widget.Label();
        BAB = new widget.TextBox();
        jLabel115 = new widget.Label();
        XBAB = new widget.TextBox();
        jLabel116 = new widget.Label();
        WBAB = new widget.TextBox();
        jLabel117 = new widget.Label();
        BAK = new widget.TextBox();
        jLabel118 = new widget.Label();
        XBAK = new widget.TextBox();
        jLabel119 = new widget.Label();
        WBAK = new widget.TextBox();
        jLabel120 = new widget.Label();
        LBAK = new widget.TextBox();
        PolaAktifitasEliminasi = new widget.ComboBox();
        jLabel171 = new widget.Label();
        jLabel172 = new widget.Label();
        jLabel173 = new widget.Label();
        jLabel174 = new widget.Label();
        PolaAktifitasMandi = new widget.ComboBox();
        PolaAktifitasMakan = new widget.ComboBox();
        PolaAktifitasBerpakaian = new widget.ComboBox();
        PolaAktifitasBerpindah = new widget.ComboBox();
        jLabel55 = new widget.Label();
        jLabel121 = new widget.Label();
        PolaNutrisiPorsi = new widget.TextBox();
        jLabel122 = new widget.Label();
        jLabel123 = new widget.Label();
        PolaNutrisiFrekuensi = new widget.TextBox();
        jLabel177 = new widget.Label();
        PolaNutrisiJenis = new widget.TextBox();
        jLabel176 = new widget.Label();
        PolaTidurLama = new widget.TextBox();
        jLabel178 = new widget.Label();
        PolaTidurGangguan = new widget.ComboBox();
        jLabel179 = new widget.Label();
        AktifitasSehari2 = new widget.ComboBox();
        jLabel181 = new widget.Label();
        Berjalan = new widget.ComboBox();
        KeteranganBerjalan = new widget.TextBox();
        jLabel182 = new widget.Label();
        Aktifitas = new widget.ComboBox();
        jLabel183 = new widget.Label();
        AlatAmbulasi = new widget.ComboBox();
        jLabel184 = new widget.Label();
        EkstrimitasAtas = new widget.ComboBox();
        KeteranganEkstrimitasAtas = new widget.TextBox();
        jLabel185 = new widget.Label();
        EkstrimitasBawah = new widget.ComboBox();
        KeteranganEkstrimitasBawah = new widget.TextBox();
        jLabel186 = new widget.Label();
        KemampuanMenggenggam = new widget.ComboBox();
        KeteranganKemampuanMenggenggam = new widget.TextBox();
        jLabel187 = new widget.Label();
        KemampuanKoordinasi = new widget.ComboBox();
        KeteranganKemampuanKoordinasi = new widget.TextBox();
        jLabel188 = new widget.Label();
        KesimpulanGangguanFungsi = new widget.ComboBox();
        KondisiPsikologis = new widget.ComboBox();
        jLabel191 = new widget.Label();
        AdakahPerilaku = new widget.ComboBox();
        KeteranganAdakahPerilaku = new widget.TextBox();
        GangguanJiwa = new widget.ComboBox();
        jLabel193 = new widget.Label();
        HubunganAnggotaKeluarga = new widget.ComboBox();
        jLabel194 = new widget.Label();
        Agama = new widget.TextBox();
        TinggalDengan = new widget.ComboBox();
        KeteranganTinggalDengan = new widget.TextBox();
        jLabel196 = new widget.Label();
        PekerjaanPasien = new widget.TextBox();
        jLabel197 = new widget.Label();
        CaraBayar = new widget.TextBox();
        NilaiKepercayaan = new widget.ComboBox();
        KeteranganNilaiKepercayaan = new widget.TextBox();
        jLabel199 = new widget.Label();
        Bahasa = new widget.TextBox();
        PendidikanPasien = new widget.TextBox();
        jLabel201 = new widget.Label();
        PendidikanPJ = new widget.ComboBox();
        jLabel202 = new widget.Label();
        EdukasiPsikolgis = new widget.ComboBox();
        jSeparator8 = new javax.swing.JSeparator();
        KeteranganEdukasiPsikologis = new widget.TextBox();
        jLabel203 = new widget.Label();
        jSeparator9 = new javax.swing.JSeparator();
        PanelWall = new usu.widget.glass.PanelGlass();
        Nyeri = new widget.ComboBox();
        jLabel204 = new widget.Label();
        Provokes = new widget.ComboBox();
        KetProvokes = new widget.TextBox();
        jLabel205 = new widget.Label();
        Quality = new widget.ComboBox();
        KetQuality = new widget.TextBox();
        jLabel206 = new widget.Label();
        jLabel207 = new widget.Label();
        Lokasi = new widget.TextBox();
        jLabel208 = new widget.Label();
        Menyebar = new widget.ComboBox();
        jLabel209 = new widget.Label();
        SkalaNyeri = new widget.ComboBox();
        jLabel211 = new widget.Label();
        Durasi = new widget.TextBox();
        jLabel212 = new widget.Label();
        jLabel213 = new widget.Label();
        NyeriHilang = new widget.ComboBox();
        KetNyeri = new widget.TextBox();
        jLabel214 = new widget.Label();
        PadaDokter = new widget.ComboBox();
        jLabel215 = new widget.Label();
        KetPadaDokter = new widget.TextBox();
        jLabel57 = new widget.Label();
        jLabel217 = new widget.Label();
        SkalaResiko1 = new widget.ComboBox();
        jLabel218 = new widget.Label();
        NilaiResiko1 = new widget.TextBox();
        jLabel219 = new widget.Label();
        jLabel220 = new widget.Label();
        jLabel221 = new widget.Label();
        SkalaResiko2 = new widget.ComboBox();
        jLabel222 = new widget.Label();
        NilaiResiko2 = new widget.TextBox();
        jLabel223 = new widget.Label();
        jLabel224 = new widget.Label();
        SkalaResiko3 = new widget.ComboBox();
        jLabel225 = new widget.Label();
        NilaiResiko3 = new widget.TextBox();
        jLabel226 = new widget.Label();
        jLabel227 = new widget.Label();
        SkalaResiko4 = new widget.ComboBox();
        jLabel228 = new widget.Label();
        NilaiResiko4 = new widget.TextBox();
        jLabel229 = new widget.Label();
        jLabel230 = new widget.Label();
        SkalaResiko5 = new widget.ComboBox();
        jLabel231 = new widget.Label();
        NilaiResiko5 = new widget.TextBox();
        jLabel232 = new widget.Label();
        jLabel233 = new widget.Label();
        SkalaResiko6 = new widget.ComboBox();
        jLabel234 = new widget.Label();
        NilaiResiko6 = new widget.TextBox();
        jLabel235 = new widget.Label();
        NilaiResikoTotal = new widget.TextBox();
        TingkatResiko = new widget.Label();
        jLabel58 = new widget.Label();
        jLabel236 = new widget.Label();
        jLabel237 = new widget.Label();
        SkalaSydney1 = new widget.ComboBox();
        jLabel238 = new widget.Label();
        NilaiSydney1 = new widget.TextBox();
        jLabel239 = new widget.Label();
        jLabel240 = new widget.Label();
        SkalaSydney2 = new widget.ComboBox();
        jLabel241 = new widget.Label();
        NilaiSydney2 = new widget.TextBox();
        jLabel242 = new widget.Label();
        jLabel243 = new widget.Label();
        SkalaSydney3 = new widget.ComboBox();
        jLabel244 = new widget.Label();
        NilaiSydney3 = new widget.TextBox();
        jLabel245 = new widget.Label();
        jLabel246 = new widget.Label();
        SkalaSydney4 = new widget.ComboBox();
        jLabel247 = new widget.Label();
        NilaiSydney4 = new widget.TextBox();
        jLabel248 = new widget.Label();
        jLabel249 = new widget.Label();
        SkalaSydney5 = new widget.ComboBox();
        jLabel250 = new widget.Label();
        NilaiSydney5 = new widget.TextBox();
        jLabel251 = new widget.Label();
        jLabel252 = new widget.Label();
        SkalaSydney6 = new widget.ComboBox();
        jLabel253 = new widget.Label();
        NilaiSydney6 = new widget.TextBox();
        jLabel254 = new widget.Label();
        jLabel255 = new widget.Label();
        SkalaSydney7 = new widget.ComboBox();
        jLabel256 = new widget.Label();
        NilaiSydney7 = new widget.TextBox();
        jLabel258 = new widget.Label();
        jLabel259 = new widget.Label();
        SkalaSydney8 = new widget.ComboBox();
        jLabel260 = new widget.Label();
        NilaiSydney8 = new widget.TextBox();
        jLabel261 = new widget.Label();
        jLabel262 = new widget.Label();
        SkalaSydney9 = new widget.ComboBox();
        jLabel263 = new widget.Label();
        NilaiSydney9 = new widget.TextBox();
        jLabel264 = new widget.Label();
        jLabel265 = new widget.Label();
        SkalaSydney10 = new widget.ComboBox();
        jLabel266 = new widget.Label();
        NilaiSydney10 = new widget.TextBox();
        jLabel267 = new widget.Label();
        jLabel268 = new widget.Label();
        SkalaSydney11 = new widget.ComboBox();
        jLabel269 = new widget.Label();
        NilaiSydney11 = new widget.TextBox();
        NilaiSydneyTotal = new widget.TextBox();
        jLabel270 = new widget.Label();
        TingkatSydney = new widget.Label();
        jSeparator11 = new javax.swing.JSeparator();
        jLabel271 = new widget.Label();
        jLabel272 = new widget.Label();
        SkalaGizi1 = new widget.ComboBox();
        jLabel273 = new widget.Label();
        NilaiGizi1 = new widget.TextBox();
        jLabel274 = new widget.Label();
        SkalaGizi2 = new widget.ComboBox();
        jLabel275 = new widget.Label();
        NilaiGizi2 = new widget.TextBox();
        jLabel276 = new widget.Label();
        NilaiGiziTotal = new widget.TextBox();
        jLabel277 = new widget.Label();
        DiagnosaKhususGizi = new widget.ComboBox();
        KeteranganDiagnosaKhususGizi = new widget.TextBox();
        jLabel278 = new widget.Label();
        DiketahuiDietisen = new widget.ComboBox();
        jLabel279 = new widget.Label();
        KeteranganDiketahuiDietisen = new widget.TextBox();
        jSeparator12 = new javax.swing.JSeparator();
        Scroll6 = new widget.ScrollPane();
        tbMasalahKeperawatan = new widget.Table();
        TabRencanaKeperawatan = new javax.swing.JTabbedPane();
        panelBiasa1 = new widget.PanelBiasa();
        Scroll8 = new widget.ScrollPane();
        tbRencanaKeperawatan = new widget.Table();
        scrollPane5 = new widget.ScrollPane();
        Rencana = new widget.TextArea();
        BtnTambahMasalah = new widget.Button();
        BtnAllMasalah = new widget.Button();
        BtnCariMasalah = new widget.Button();
        TCariMasalah = new widget.TextBox();
        BtnTambahRencana = new widget.Button();
        BtnAllRencana = new widget.Button();
        BtnCariRencana = new widget.Button();
        label13 = new widget.Label();
        TCariRencana = new widget.TextBox();
        label12 = new widget.Label();
        jLabel283 = new widget.Label();
        jLabel284 = new widget.Label();
        jLabel285 = new widget.Label();
        jLabel286 = new widget.Label();
        jLabel287 = new widget.Label();
        jLabel288 = new widget.Label();
        jLabel257 = new widget.Label();
        jLabel124 = new widget.Label();
        jLabel126 = new widget.Label();
        jLabel54 = new widget.Label();
        jLabel169 = new widget.Label();
        jLabel216 = new widget.Label();
        PanelWall1 = new usu.widget.glass.PanelGlass();
        PanelWall2 = new usu.widget.glass.PanelGlass();
        jLabel170 = new widget.Label();
        jLabel175 = new widget.Label();
        jLabel46 = new widget.Label();
        jLabel56 = new widget.Label();
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

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        internalFrame1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "::[ Penilaian Awal Keperawatan Rawat Inap Kemoterapi ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 50, 50))); // NOI18N
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

        internalFrame1.add(panelGlass8, java.awt.BorderLayout.PAGE_END);

        TabRawat.setBackground(new java.awt.Color(254, 255, 254));
        TabRawat.setForeground(new java.awt.Color(50, 50, 50));
        TabRawat.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        TabRawat.setName("TabRawat"); // NOI18N
        TabRawat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TabRawatMouseClicked(evt);
            }
        });

        internalFrame2.setBorder(null);
        internalFrame2.setName("internalFrame2"); // NOI18N
        internalFrame2.setLayout(new java.awt.BorderLayout(1, 1));

        scrollInput.setName("scrollInput"); // NOI18N
        scrollInput.setPreferredSize(new java.awt.Dimension(102, 557));

        FormInput.setBackground(new java.awt.Color(255, 255, 255));
        FormInput.setBorder(null);
        FormInput.setName("FormInput"); // NOI18N
        FormInput.setPreferredSize(new java.awt.Dimension(870, 2500));
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

        label14.setText("Pengkaji 1 :");
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
        NmPetugas.setBounds(176, 40, 210, 23);

        BtnPetugas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnPetugas.setMnemonic('2');
        BtnPetugas.setToolTipText("Alt+2");
        BtnPetugas.setName("BtnPetugas"); // NOI18N
        BtnPetugas.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnPetugas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnPetugasActionPerformed(evt);
            }
        });
        BtnPetugas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnPetugasKeyPressed(evt);
            }
        });
        FormInput.add(BtnPetugas);
        BtnPetugas.setBounds(388, 40, 28, 23);

        jLabel8.setText("Tgl.Lahir :");
        jLabel8.setName("jLabel8"); // NOI18N
        FormInput.add(jLabel8);
        jLabel8.setBounds(580, 10, 60, 23);

        TglLahir.setEditable(false);
        TglLahir.setHighlighter(null);
        TglLahir.setName("TglLahir"); // NOI18N
        FormInput.add(TglLahir);
        TglLahir.setBounds(644, 10, 80, 23);

        Jk.setEditable(false);
        Jk.setHighlighter(null);
        Jk.setName("Jk"); // NOI18N
        FormInput.add(Jk);
        Jk.setBounds(774, 10, 80, 23);

        jLabel10.setText("No.Rawat :");
        jLabel10.setName("jLabel10"); // NOI18N
        FormInput.add(jLabel10);
        jLabel10.setBounds(0, 10, 70, 23);

        label11.setText("Tanggal :");
        label11.setName("label11"); // NOI18N
        label11.setPreferredSize(new java.awt.Dimension(70, 23));
        FormInput.add(label11);
        label11.setBounds(438, 70, 70, 23);

        jLabel11.setText("J.K. :");
        jLabel11.setName("jLabel11"); // NOI18N
        FormInput.add(jLabel11);
        jLabel11.setBounds(740, 10, 30, 23);

        jLabel36.setText("Anamnesis :");
        jLabel36.setName("jLabel36"); // NOI18N
        FormInput.add(jLabel36);
        jLabel36.setBounds(0, 100, 70, 23);

        Anamnesis.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Autoanamnesis", "Alloanamnesis" }));
        Anamnesis.setName("Anamnesis"); // NOI18N
        Anamnesis.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                AnamnesisKeyPressed(evt);
            }
        });
        FormInput.add(Anamnesis);
        Anamnesis.setBounds(74, 100, 130, 23);

        TglAsuhan.setForeground(new java.awt.Color(50, 70, 50));
        TglAsuhan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "23-10-2023 18:51:24" }));
        TglAsuhan.setDisplayFormat("dd-MM-yyyy HH:mm:ss");
        TglAsuhan.setName("TglAsuhan"); // NOI18N
        TglAsuhan.setOpaque(false);
        TglAsuhan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TglAsuhanKeyPressed(evt);
            }
        });
        FormInput.add(TglAsuhan);
        TglAsuhan.setBounds(512, 70, 135, 23);

        NmPetugas2.setEditable(false);
        NmPetugas2.setName("NmPetugas2"); // NOI18N
        NmPetugas2.setPreferredSize(new java.awt.Dimension(207, 23));
        FormInput.add(NmPetugas2);
        NmPetugas2.setBounds(614, 40, 210, 23);

        BtnPetugas2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnPetugas2.setMnemonic('2');
        BtnPetugas2.setToolTipText("Alt+2");
        BtnPetugas2.setName("BtnPetugas2"); // NOI18N
        BtnPetugas2.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnPetugas2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnPetugas2ActionPerformed(evt);
            }
        });
        BtnPetugas2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnPetugas2KeyPressed(evt);
            }
        });
        FormInput.add(BtnPetugas2);
        BtnPetugas2.setBounds(826, 40, 28, 23);

        KdPetugas2.setEditable(false);
        KdPetugas2.setName("KdPetugas2"); // NOI18N
        KdPetugas2.setPreferredSize(new java.awt.Dimension(80, 23));
        KdPetugas2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KdPetugas2KeyPressed(evt);
            }
        });
        FormInput.add(KdPetugas2);
        KdPetugas2.setBounds(512, 40, 100, 23);

        label15.setText("Pengkaji 2 :");
        label15.setName("label15"); // NOI18N
        label15.setPreferredSize(new java.awt.Dimension(70, 23));
        FormInput.add(label15);
        label15.setBounds(438, 40, 70, 23);

        label16.setText("DPJP :");
        label16.setName("label16"); // NOI18N
        label16.setPreferredSize(new java.awt.Dimension(70, 23));
        FormInput.add(label16);
        label16.setBounds(0, 70, 70, 23);

        KdDPJP.setEditable(false);
        KdDPJP.setName("KdDPJP"); // NOI18N
        KdDPJP.setPreferredSize(new java.awt.Dimension(80, 23));
        KdDPJP.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KdDPJPKeyPressed(evt);
            }
        });
        FormInput.add(KdDPJP);
        KdDPJP.setBounds(74, 70, 110, 23);

        NmDPJP.setEditable(false);
        NmDPJP.setName("NmDPJP"); // NOI18N
        NmDPJP.setPreferredSize(new java.awt.Dimension(207, 23));
        FormInput.add(NmDPJP);
        NmDPJP.setBounds(186, 70, 230, 23);

        BtnDPJP.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnDPJP.setMnemonic('2');
        BtnDPJP.setToolTipText("Alt+2");
        BtnDPJP.setName("BtnDPJP"); // NOI18N
        BtnDPJP.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnDPJP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnDPJPActionPerformed(evt);
            }
        });
        BtnDPJP.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnDPJPKeyPressed(evt);
            }
        });
        FormInput.add(BtnDPJP);
        BtnDPJP.setBounds(418, 70, 28, 23);

        TibadiRuang.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Jalan Tanpa Bantuan", "Kursi Roda", "Brankar" }));
        TibadiRuang.setName("TibadiRuang"); // NOI18N
        TibadiRuang.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TibadiRuangKeyPressed(evt);
            }
        });
        FormInput.add(TibadiRuang);
        TibadiRuang.setBounds(516, 100, 155, 23);

        jLabel37.setText("Cara Masuk :");
        jLabel37.setName("jLabel37"); // NOI18N
        FormInput.add(jLabel37);
        jLabel37.setBounds(392, 100, 120, 23);

        CaraMasuk.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Poliklinik", "IGD", "Rawat Inap", "ICU/HCU" }));
        CaraMasuk.setName("CaraMasuk"); // NOI18N
        CaraMasuk.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                CaraMasukKeyPressed(evt);
            }
        });
        FormInput.add(CaraMasuk);
        CaraMasuk.setBounds(759, 100, 95, 23);

        jLabel38.setText("Asal Pasien :");
        jLabel38.setName("jLabel38"); // NOI18N
        FormInput.add(jLabel38);
        jLabel38.setBounds(685, 100, 70, 23);

        jLabel94.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel94.setText("I. RIWAYAT KESEHATAN");
        jLabel94.setName("jLabel94"); // NOI18N
        FormInput.add(jLabel94);
        jLabel94.setBounds(10, 130, 180, 23);

        jLabel9.setText("Riwayat Penggunaan Obat :");
        jLabel9.setName("jLabel9"); // NOI18N
        FormInput.add(jLabel9);
        jLabel9.setBounds(450, 200, 150, 23);

        Alergi.setFocusTraversalPolicyProvider(true);
        Alergi.setName("Alergi"); // NOI18N
        Alergi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                AlergiKeyPressed(evt);
            }
        });
        FormInput.add(Alergi);
        Alergi.setBounds(270, 400, 190, 23);

        scrollPane1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        scrollPane1.setName("scrollPane1"); // NOI18N

        RPS.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        RPS.setColumns(20);
        RPS.setRows(5);
        RPS.setName("RPS"); // NOI18N
        RPS.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                RPSKeyPressed(evt);
            }
        });
        scrollPane1.setViewportView(RPS);

        FormInput.add(scrollPane1);
        scrollPane1.setBounds(179, 150, 280, 43);

        jLabel30.setText("Keluhan Utama :");
        jLabel30.setName("jLabel30"); // NOI18N
        FormInput.add(jLabel30);
        jLabel30.setBounds(0, 150, 175, 23);

        scrollPane2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        scrollPane2.setName("scrollPane2"); // NOI18N

        RPK.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        RPK.setColumns(20);
        RPK.setRows(5);
        RPK.setName("RPK"); // NOI18N
        RPK.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                RPKKeyPressed(evt);
            }
        });
        scrollPane2.setViewportView(RPK);

        FormInput.add(scrollPane2);
        scrollPane2.setBounds(179, 200, 280, 43);

        jLabel31.setText("Riwayat Penyakit Keluarga :");
        jLabel31.setName("jLabel31"); // NOI18N
        FormInput.add(jLabel31);
        jLabel31.setBounds(10, 200, 160, 23);

        scrollPane3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        scrollPane3.setName("scrollPane3"); // NOI18N

        RPD.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        RPD.setColumns(20);
        RPD.setRows(5);
        RPD.setName("RPD"); // NOI18N
        RPD.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                RPDKeyPressed(evt);
            }
        });
        scrollPane3.setViewportView(RPD);

        FormInput.add(scrollPane3);
        scrollPane3.setBounds(604, 150, 250, 43);

        jLabel32.setText("Riwayat Keluhan :");
        jLabel32.setName("jLabel32"); // NOI18N
        FormInput.add(jLabel32);
        jLabel32.setBounds(460, 150, 140, 23);

        scrollPane4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        scrollPane4.setName("scrollPane4"); // NOI18N

        RPO.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        RPO.setColumns(20);
        RPO.setRows(5);
        RPO.setName("RPO"); // NOI18N
        RPO.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                RPOKeyPressed(evt);
            }
        });
        scrollPane4.setViewportView(RPO);

        FormInput.add(scrollPane4);
        scrollPane4.setBounds(604, 200, 250, 43);

        jSeparator2.setBackground(new java.awt.Color(239, 244, 234));
        jSeparator2.setForeground(new java.awt.Color(239, 244, 234));
        jSeparator2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(239, 244, 234)));
        jSeparator2.setName("jSeparator2"); // NOI18N
        FormInput.add(jSeparator2);
        jSeparator2.setBounds(0, 130, 880, 1);

        MacamKasus.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Trauma", "Non Trauma" }));
        MacamKasus.setSelectedIndex(1);
        MacamKasus.setName("MacamKasus"); // NOI18N
        MacamKasus.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                MacamKasusKeyPressed(evt);
            }
        });
        FormInput.add(MacamKasus);
        MacamKasus.setBounds(742, 70, 112, 23);

        jLabel41.setText("Kasus :");
        jLabel41.setName("jLabel41"); // NOI18N
        FormInput.add(jLabel41);
        jLabel41.setBounds(658, 70, 80, 23);

        KetAnamnesis.setFocusTraversalPolicyProvider(true);
        KetAnamnesis.setName("KetAnamnesis"); // NOI18N
        KetAnamnesis.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KetAnamnesisKeyPressed(evt);
            }
        });
        FormInput.add(KetAnamnesis);
        KetAnamnesis.setBounds(208, 100, 175, 23);

        RDirawatRS.setFocusTraversalPolicyProvider(true);
        RDirawatRS.setName("RDirawatRS"); // NOI18N
        RDirawatRS.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                RDirawatRSKeyPressed(evt);
            }
        });
        FormInput.add(RDirawatRS);
        RDirawatRS.setBounds(270, 370, 190, 23);

        RPembedahan.setFocusTraversalPolicyProvider(true);
        RPembedahan.setName("RPembedahan"); // NOI18N
        RPembedahan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                RPembedahanKeyPressed(evt);
            }
        });
        FormInput.add(RPembedahan);
        RPembedahan.setBounds(180, 280, 280, 23);

        jLabel42.setText("Resus :");
        jLabel42.setName("jLabel42"); // NOI18N
        FormInput.add(jLabel42);
        jLabel42.setBounds(10, 280, 156, 23);

        jLabel43.setText("Kemoterapi Terdahulu :");
        jLabel43.setName("jLabel43"); // NOI18N
        FormInput.add(jLabel43);
        jLabel43.setBounds(0, 370, 168, 23);

        AlatBantuDipakai.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Induksi", "Konsolidasi", "Reinduksi", "Lainnya" }));
        AlatBantuDipakai.setName("AlatBantuDipakai"); // NOI18N
        AlatBantuDipakai.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                AlatBantuDipakaiKeyPressed(evt);
            }
        });
        FormInput.add(AlatBantuDipakai);
        AlatBantuDipakai.setBounds(180, 370, 80, 23);

        SedangMenyusui.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        SedangMenyusui.setName("SedangMenyusui"); // NOI18N
        SedangMenyusui.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SedangMenyusuiKeyPressed(evt);
            }
        });
        FormInput.add(SedangMenyusui);
        SedangMenyusui.setBounds(180, 340, 80, 23);

        jLabel44.setText("Hasil LAB :");
        jLabel44.setName("jLabel44"); // NOI18N
        FormInput.add(jLabel44);
        jLabel44.setBounds(50, 460, 120, 23);

        KetSedangMenyusui.setFocusTraversalPolicyProvider(true);
        KetSedangMenyusui.setName("KetSedangMenyusui"); // NOI18N
        KetSedangMenyusui.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KetSedangMenyusuiKeyPressed(evt);
            }
        });
        FormInput.add(KetSedangMenyusui);
        KetSedangMenyusui.setBounds(270, 340, 190, 23);

        jLabel45.setText("Golongan Darah :");
        jLabel45.setName("jLabel45"); // NOI18N
        FormInput.add(jLabel45);
        jLabel45.setBounds(0, 250, 170, 23);

        RTranfusi.setFocusTraversalPolicyProvider(true);
        RTranfusi.setName("RTranfusi"); // NOI18N
        RTranfusi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                RTranfusiKeyPressed(evt);
            }
        });
        FormInput.add(RTranfusi);
        RTranfusi.setBounds(180, 250, 280, 23);

        KebiasaanMerokok.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        KebiasaanMerokok.setName("KebiasaanMerokok"); // NOI18N
        KebiasaanMerokok.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KebiasaanMerokokKeyPressed(evt);
            }
        });
        FormInput.add(KebiasaanMerokok);
        KebiasaanMerokok.setBounds(180, 460, 80, 23);

        KebiasaanJumlahRokok.setFocusTraversalPolicyProvider(true);
        KebiasaanJumlahRokok.setName("KebiasaanJumlahRokok"); // NOI18N
        KebiasaanJumlahRokok.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KebiasaanJumlahRokokKeyPressed(evt);
            }
        });
        FormInput.add(KebiasaanJumlahRokok);
        KebiasaanJumlahRokok.setBounds(270, 460, 190, 23);

        KebiasaanAlkohol.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        KebiasaanAlkohol.setName("KebiasaanAlkohol"); // NOI18N
        KebiasaanAlkohol.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                KebiasaanAlkoholActionPerformed(evt);
            }
        });
        KebiasaanAlkohol.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KebiasaanAlkoholKeyPressed(evt);
            }
        });
        FormInput.add(KebiasaanAlkohol);
        KebiasaanAlkohol.setBounds(180, 430, 80, 23);

        KebiasaanJumlahAlkohol.setFocusTraversalPolicyProvider(true);
        KebiasaanJumlahAlkohol.setName("KebiasaanJumlahAlkohol"); // NOI18N
        KebiasaanJumlahAlkohol.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KebiasaanJumlahAlkoholKeyPressed(evt);
            }
        });
        FormInput.add(KebiasaanJumlahAlkohol);
        KebiasaanJumlahAlkohol.setBounds(270, 430, 190, 23);

        jLabel127.setText("Belakang");
        jLabel127.setName("jLabel127"); // NOI18N
        FormInput.add(jLabel127);
        jLabel127.setBounds(730, 470, 60, 23);

        KebiasaanNarkoba.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Induksi", "Transplantasi Sumsum Tulang", "Lainnya" }));
        KebiasaanNarkoba.setName("KebiasaanNarkoba"); // NOI18N
        KebiasaanNarkoba.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KebiasaanNarkobaKeyPressed(evt);
            }
        });
        FormInput.add(KebiasaanNarkoba);
        KebiasaanNarkoba.setBounds(180, 400, 80, 23);

        jLabel128.setText("Tindakan Perawatan :");
        jLabel128.setName("jLabel128"); // NOI18N
        FormInput.add(jLabel128);
        jLabel128.setBounds(50, 400, 120, 23);

        jLabel129.setText("Graft Versus Host Disease :");
        jLabel129.setName("jLabel129"); // NOI18N
        FormInput.add(jLabel129);
        jLabel129.setBounds(20, 490, 150, 23);

        OlahRaga.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        OlahRaga.setName("OlahRaga"); // NOI18N
        OlahRaga.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                OlahRagaKeyPressed(evt);
            }
        });
        FormInput.add(OlahRaga);
        OlahRaga.setBounds(180, 490, 80, 23);

        jLabel95.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel95.setText("II. PEMERIKSAAN FISIK");
        jLabel95.setName("jLabel95"); // NOI18N
        FormInput.add(jLabel95);
        jLabel95.setBounds(20, 530, 180, 23);

        jSeparator3.setBackground(new java.awt.Color(239, 244, 234));
        jSeparator3.setForeground(new java.awt.Color(239, 244, 234));
        jSeparator3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(239, 244, 234)));
        jSeparator3.setName("jSeparator3"); // NOI18N
        FormInput.add(jSeparator3);
        jSeparator3.setBounds(10, 530, 880, 1);

        jLabel47.setText("Kesadaran Mental :");
        jLabel47.setName("jLabel47"); // NOI18N
        FormInput.add(jLabel47);
        jLabel47.setBounds(10, 550, 138, 23);

        KesadaranMental.setFocusTraversalPolicyProvider(true);
        KesadaranMental.setName("KesadaranMental"); // NOI18N
        KesadaranMental.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KesadaranMentalKeyPressed(evt);
            }
        });
        FormInput.add(KesadaranMental);
        KesadaranMental.setBounds(150, 550, 175, 23);

        jLabel130.setText("Keadaan Umum :");
        jLabel130.setName("jLabel130"); // NOI18N
        FormInput.add(jLabel130);
        jLabel130.setBounds(350, 550, 90, 23);

        KeadaanMentalUmum.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Baik", "Sedang", "Buruk" }));
        KeadaanMentalUmum.setName("KeadaanMentalUmum"); // NOI18N
        KeadaanMentalUmum.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KeadaanMentalUmumKeyPressed(evt);
            }
        });
        FormInput.add(KeadaanMentalUmum);
        KeadaanMentalUmum.setBounds(440, 550, 90, 23);

        jLabel28.setText("GCS(E,V,M) :");
        jLabel28.setName("jLabel28"); // NOI18N
        FormInput.add(jLabel28);
        jLabel28.setBounds(560, 550, 70, 23);

        GCS.setFocusTraversalPolicyProvider(true);
        GCS.setName("GCS"); // NOI18N
        GCS.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                GCSKeyPressed(evt);
            }
        });
        FormInput.add(GCS);
        GCS.setBounds(630, 550, 75, 23);

        jLabel22.setText("TD :");
        jLabel22.setName("jLabel22"); // NOI18N
        FormInput.add(jLabel22);
        jLabel22.setBounds(730, 550, 30, 23);

        TD.setFocusTraversalPolicyProvider(true);
        TD.setName("TD"); // NOI18N
        TD.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TDKeyPressed(evt);
            }
        });
        FormInput.add(TD);
        TD.setBounds(760, 550, 65, 23);

        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel23.setText("mmHg");
        jLabel23.setName("jLabel23"); // NOI18N
        FormInput.add(jLabel23);
        jLabel23.setBounds(830, 550, 40, 23);

        jLabel17.setText("Nadi :");
        jLabel17.setName("jLabel17"); // NOI18N
        FormInput.add(jLabel17);
        jLabel17.setBounds(10, 580, 73, 23);

        Nadi.setFocusTraversalPolicyProvider(true);
        Nadi.setName("Nadi"); // NOI18N
        Nadi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                NadiKeyPressed(evt);
            }
        });
        FormInput.add(Nadi);
        Nadi.setBounds(80, 580, 50, 23);

        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel16.setText("x/menit");
        jLabel16.setName("jLabel16"); // NOI18N
        FormInput.add(jLabel16);
        jLabel16.setBounds(140, 580, 50, 23);

        jLabel26.setText("RR :");
        jLabel26.setName("jLabel26"); // NOI18N
        FormInput.add(jLabel26);
        jLabel26.setBounds(190, 580, 50, 23);

        RR.setFocusTraversalPolicyProvider(true);
        RR.setName("RR"); // NOI18N
        RR.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                RRKeyPressed(evt);
            }
        });
        FormInput.add(RR);
        RR.setBounds(240, 580, 50, 23);

        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel25.setText("x/menit");
        jLabel25.setName("jLabel25"); // NOI18N
        FormInput.add(jLabel25);
        jLabel25.setBounds(290, 580, 50, 23);

        jLabel18.setText("Suhu :");
        jLabel18.setName("jLabel18"); // NOI18N
        FormInput.add(jLabel18);
        jLabel18.setBounds(370, 580, 40, 23);

        Suhu.setFocusTraversalPolicyProvider(true);
        Suhu.setName("Suhu"); // NOI18N
        Suhu.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SuhuKeyPressed(evt);
            }
        });
        FormInput.add(Suhu);
        Suhu.setBounds(410, 580, 50, 23);

        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel20.setText("°C");
        jLabel20.setName("jLabel20"); // NOI18N
        FormInput.add(jLabel20);
        jLabel20.setBounds(460, 580, 30, 23);

        jLabel24.setText("SpO2 :");
        jLabel24.setName("jLabel24"); // NOI18N
        FormInput.add(jLabel24);
        jLabel24.setBounds(510, 580, 40, 23);

        SpO2.setFocusTraversalPolicyProvider(true);
        SpO2.setName("SpO2"); // NOI18N
        SpO2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SpO2KeyPressed(evt);
            }
        });
        FormInput.add(SpO2);
        SpO2.setBounds(550, 580, 50, 23);

        jLabel29.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel29.setText("%");
        jLabel29.setName("jLabel29"); // NOI18N
        FormInput.add(jLabel29);
        jLabel29.setBounds(600, 580, 30, 23);

        jLabel12.setText("BB :");
        jLabel12.setName("jLabel12"); // NOI18N
        FormInput.add(jLabel12);
        jLabel12.setBounds(640, 580, 30, 23);

        BB.setFocusTraversalPolicyProvider(true);
        BB.setName("BB"); // NOI18N
        BB.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BBKeyPressed(evt);
            }
        });
        FormInput.add(BB);
        BB.setBounds(670, 580, 50, 23);

        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel13.setText("Kg");
        jLabel13.setName("jLabel13"); // NOI18N
        FormInput.add(jLabel13);
        jLabel13.setBounds(730, 580, 30, 23);

        jLabel15.setText("TB :");
        jLabel15.setName("jLabel15"); // NOI18N
        FormInput.add(jLabel15);
        jLabel15.setBounds(760, 580, 30, 23);

        TB.setFocusTraversalPolicyProvider(true);
        TB.setName("TB"); // NOI18N
        TB.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TBKeyPressed(evt);
            }
        });
        FormInput.add(TB);
        TB.setBounds(790, 580, 50, 23);

        jLabel48.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel48.setText("cm");
        jLabel48.setName("jLabel48"); // NOI18N
        FormInput.add(jLabel48);
        jLabel48.setBounds(850, 580, 30, 23);

        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel27.setText("Sistem Susunan Saraf Pusat :");
        jLabel27.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel27.setName("jLabel27"); // NOI18N
        FormInput.add(jLabel27);
        jLabel27.setBounds(10, 610, 187, 23);

        jLabel131.setText("Kepala :");
        jLabel131.setName("jLabel131"); // NOI18N
        FormInput.add(jLabel131);
        jLabel131.setBounds(10, 630, 109, 23);

        SistemSarafKepala.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "TAK", "Hydrocephalus", "Hematoma", "Lain-lain" }));
        SistemSarafKepala.setName("SistemSarafKepala"); // NOI18N
        SistemSarafKepala.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SistemSarafKepalaKeyPressed(evt);
            }
        });
        FormInput.add(SistemSarafKepala);
        SistemSarafKepala.setBounds(120, 630, 103, 23);

        KetSistemSarafKepala.setFocusTraversalPolicyProvider(true);
        KetSistemSarafKepala.setName("KetSistemSarafKepala"); // NOI18N
        KetSistemSarafKepala.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KetSistemSarafKepalaKeyPressed(evt);
            }
        });
        FormInput.add(KetSistemSarafKepala);
        KetSistemSarafKepala.setBounds(250, 630, 184, 23);

        jLabel132.setText("Wajah :");
        jLabel132.setName("jLabel132"); // NOI18N
        FormInput.add(jLabel132);
        jLabel132.setBounds(440, 630, 80, 23);

        SistemSarafWajah.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "TAK", "Asimetris", "Kelainan Kongenital" }));
        SistemSarafWajah.setName("SistemSarafWajah"); // NOI18N
        SistemSarafWajah.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SistemSarafWajahKeyPressed(evt);
            }
        });
        FormInput.add(SistemSarafWajah);
        SistemSarafWajah.setBounds(520, 630, 150, 23);

        KetSistemSarafWajah.setFocusTraversalPolicyProvider(true);
        KetSistemSarafWajah.setName("KetSistemSarafWajah"); // NOI18N
        KetSistemSarafWajah.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KetSistemSarafWajahKeyPressed(evt);
            }
        });
        FormInput.add(KetSistemSarafWajah);
        KetSistemSarafWajah.setBounds(680, 630, 184, 23);

        jLabel133.setText("Leher :");
        jLabel133.setName("jLabel133"); // NOI18N
        FormInput.add(jLabel133);
        jLabel133.setBounds(10, 660, 109, 23);

        SistemSarafLeher.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "TAK", "Kaku Kuduk", "Pembesaran Thyroid", "Pembesaran KGB" }));
        SistemSarafLeher.setName("SistemSarafLeher"); // NOI18N
        SistemSarafLeher.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SistemSarafLeherKeyPressed(evt);
            }
        });
        FormInput.add(SistemSarafLeher);
        SistemSarafLeher.setBounds(120, 660, 100, 23);

        jLabel134.setText("Sensorik :");
        jLabel134.setName("jLabel134"); // NOI18N
        FormInput.add(jLabel134);
        jLabel134.setBounds(660, 660, 80, 23);

        SistemSarafSensorik.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "TAK", "Sakit Nyeri", "Rasa Kebas" }));
        SistemSarafSensorik.setName("SistemSarafSensorik"); // NOI18N
        SistemSarafSensorik.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SistemSarafSensorikKeyPressed(evt);
            }
        });
        FormInput.add(SistemSarafSensorik);
        SistemSarafSensorik.setBounds(740, 660, 120, 23);

        jLabel135.setText("Kejang :");
        jLabel135.setName("jLabel135"); // NOI18N
        FormInput.add(jLabel135);
        jLabel135.setBounds(250, 660, 60, 23);

        SistemSarafKejang.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "TAK", "Kuat", "Ada" }));
        SistemSarafKejang.setName("SistemSarafKejang"); // NOI18N
        SistemSarafKejang.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SistemSarafKejangKeyPressed(evt);
            }
        });
        FormInput.add(SistemSarafKejang);
        SistemSarafKejang.setBounds(320, 660, 110, 23);

        KetSistemSarafKejang.setFocusTraversalPolicyProvider(true);
        KetSistemSarafKejang.setName("KetSistemSarafKejang"); // NOI18N
        KetSistemSarafKejang.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KetSistemSarafKejangKeyPressed(evt);
            }
        });
        FormInput.add(KetSistemSarafKejang);
        KetSistemSarafKejang.setBounds(440, 660, 184, 23);

        jLabel33.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel33.setText("Kardiovaskuler :");
        jLabel33.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel33.setName("jLabel33"); // NOI18N
        FormInput.add(jLabel33);
        jLabel33.setBounds(10, 690, 122, 23);

        jLabel136.setText("Pulsasi :");
        jLabel136.setName("jLabel136"); // NOI18N
        FormInput.add(jLabel136);
        jLabel136.setBounds(10, 710, 109, 23);

        KardiovaskularPulsasi.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Kuat", "Lemah", "Lain-lain" }));
        KardiovaskularPulsasi.setName("KardiovaskularPulsasi"); // NOI18N
        KardiovaskularPulsasi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KardiovaskularPulsasiKeyPressed(evt);
            }
        });
        FormInput.add(KardiovaskularPulsasi);
        KardiovaskularPulsasi.setBounds(120, 710, 100, 23);

        jLabel137.setText("Sirkulasi :");
        jLabel137.setName("jLabel137"); // NOI18N
        FormInput.add(jLabel137);
        jLabel137.setBounds(250, 710, 60, 23);

        KardiovaskularSirkulasi.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Akral Hangat", "Akral Dingin", "Edema" }));
        KardiovaskularSirkulasi.setName("KardiovaskularSirkulasi"); // NOI18N
        KardiovaskularSirkulasi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KardiovaskularSirkulasiKeyPressed(evt);
            }
        });
        FormInput.add(KardiovaskularSirkulasi);
        KardiovaskularSirkulasi.setBounds(310, 710, 110, 23);

        KetKardiovaskularSirkulasi.setFocusTraversalPolicyProvider(true);
        KetKardiovaskularSirkulasi.setName("KetKardiovaskularSirkulasi"); // NOI18N
        KetKardiovaskularSirkulasi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KetKardiovaskularSirkulasiKeyPressed(evt);
            }
        });
        FormInput.add(KetKardiovaskularSirkulasi);
        KetKardiovaskularSirkulasi.setBounds(440, 710, 184, 23);

        jLabel138.setText("Denyut Nadi :");
        jLabel138.setName("jLabel138"); // NOI18N
        FormInput.add(jLabel138);
        jLabel138.setBounds(660, 710, 80, 23);

        KardiovaskularDenyutNadi.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Teratur", "Tidak Teratur" }));
        KardiovaskularDenyutNadi.setName("KardiovaskularDenyutNadi"); // NOI18N
        KardiovaskularDenyutNadi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KardiovaskularDenyutNadiKeyPressed(evt);
            }
        });
        FormInput.add(KardiovaskularDenyutNadi);
        KardiovaskularDenyutNadi.setBounds(740, 710, 120, 23);

        jLabel35.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel35.setText("Respirasi :");
        jLabel35.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel35.setName("jLabel35"); // NOI18N
        FormInput.add(jLabel35);
        jLabel35.setBounds(10, 740, 96, 23);

        jLabel139.setText("Retraksi :");
        jLabel139.setName("jLabel139"); // NOI18N
        FormInput.add(jLabel139);
        jLabel139.setBounds(10, 760, 109, 23);

        RespirasiRetraksi.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak Ada", "Ringan", "Berat" }));
        RespirasiRetraksi.setName("RespirasiRetraksi"); // NOI18N
        RespirasiRetraksi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                RespirasiRetraksiKeyPressed(evt);
            }
        });
        FormInput.add(RespirasiRetraksi);
        RespirasiRetraksi.setBounds(120, 760, 100, 23);

        RespirasiPolaNafas.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Normal", "Bradipnea", "Tachipnea" }));
        RespirasiPolaNafas.setName("RespirasiPolaNafas"); // NOI18N
        RespirasiPolaNafas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                RespirasiPolaNafasKeyPressed(evt);
            }
        });
        FormInput.add(RespirasiPolaNafas);
        RespirasiPolaNafas.setBounds(320, 760, 110, 23);

        jLabel140.setText("Pola Nafas :");
        jLabel140.setName("jLabel140"); // NOI18N
        FormInput.add(jLabel140);
        jLabel140.setBounds(230, 760, 80, 23);

        jLabel141.setText("Suara Nafas :");
        jLabel141.setName("jLabel141"); // NOI18N
        FormInput.add(jLabel141);
        jLabel141.setBounds(420, 760, 80, 23);

        RespirasiSuaraNafas.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Vesikuler", "Wheezing", "Rhonki" }));
        RespirasiSuaraNafas.setName("RespirasiSuaraNafas"); // NOI18N
        RespirasiSuaraNafas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                RespirasiSuaraNafasKeyPressed(evt);
            }
        });
        FormInput.add(RespirasiSuaraNafas);
        RespirasiSuaraNafas.setBounds(510, 760, 100, 23);

        jLabel142.setText("Irama :");
        jLabel142.setName("jLabel142"); // NOI18N
        FormInput.add(jLabel142);
        jLabel142.setBounds(680, 790, 60, 23);

        RespirasiIrama.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Teratur", "Tidak Teratur" }));
        RespirasiIrama.setName("RespirasiIrama"); // NOI18N
        RespirasiIrama.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                RespirasiIramaKeyPressed(evt);
            }
        });
        FormInput.add(RespirasiIrama);
        RespirasiIrama.setBounds(740, 790, 120, 23);

        jLabel143.setText("Volume :");
        jLabel143.setName("jLabel143"); // NOI18N
        FormInput.add(jLabel143);
        jLabel143.setBounds(10, 790, 109, 23);

        RespirasiVolume.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Normal", "Hiperventilasi", "Hipoventilasi" }));
        RespirasiVolume.setName("RespirasiVolume"); // NOI18N
        RespirasiVolume.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                RespirasiVolumeKeyPressed(evt);
            }
        });
        FormInput.add(RespirasiVolume);
        RespirasiVolume.setBounds(120, 790, 100, 23);

        jLabel144.setText("Jenis Pernafasaan :");
        jLabel144.setName("jLabel144"); // NOI18N
        FormInput.add(jLabel144);
        jLabel144.setBounds(240, 790, 120, 23);

        RespirasiJenisPernafasan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Pernafasan Dada", "Alat Bantu Pernafasaan" }));
        RespirasiJenisPernafasan.setName("RespirasiJenisPernafasan"); // NOI18N
        RespirasiJenisPernafasan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                RespirasiJenisPernafasanKeyPressed(evt);
            }
        });
        FormInput.add(RespirasiJenisPernafasan);
        RespirasiJenisPernafasan.setBounds(360, 790, 166, 23);

        KetRespirasiJenisPernafasan.setFocusTraversalPolicyProvider(true);
        KetRespirasiJenisPernafasan.setName("KetRespirasiJenisPernafasan"); // NOI18N
        KetRespirasiJenisPernafasan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KetRespirasiJenisPernafasanKeyPressed(evt);
            }
        });
        FormInput.add(KetRespirasiJenisPernafasan);
        KetRespirasiJenisPernafasan.setBounds(530, 790, 135, 23);

        jLabel145.setText("Batuk & Sekresi :");
        jLabel145.setName("jLabel145"); // NOI18N
        FormInput.add(jLabel145);
        jLabel145.setBounds(640, 760, 100, 23);

        RespirasiBatuk.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya : Produktif", "Ya : Non Produktif" }));
        RespirasiBatuk.setName("RespirasiBatuk"); // NOI18N
        RespirasiBatuk.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                RespirasiBatukKeyPressed(evt);
            }
        });
        FormInput.add(RespirasiBatuk);
        RespirasiBatuk.setBounds(740, 760, 120, 23);

        jLabel49.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel49.setText("Gastrointestinal :");
        jLabel49.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel49.setName("jLabel49"); // NOI18N
        FormInput.add(jLabel49);
        jLabel49.setBounds(10, 820, 129, 23);

        jLabel146.setText("Mulut :");
        jLabel146.setName("jLabel146"); // NOI18N
        FormInput.add(jLabel146);
        jLabel146.setBounds(0, 840, 109, 23);

        GastrointestinalMulut.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "TAK", "Stomatitis", "Mukosa Kering", "Bibir Pucat", "Lain-lain" }));
        GastrointestinalMulut.setName("GastrointestinalMulut"); // NOI18N
        GastrointestinalMulut.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                GastrointestinalMulutKeyPressed(evt);
            }
        });
        FormInput.add(GastrointestinalMulut);
        GastrointestinalMulut.setBounds(110, 840, 120, 23);

        KetGastrointestinalMulut.setFocusTraversalPolicyProvider(true);
        KetGastrointestinalMulut.setName("KetGastrointestinalMulut"); // NOI18N
        KetGastrointestinalMulut.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KetGastrointestinalMulutKeyPressed(evt);
            }
        });
        FormInput.add(KetGastrointestinalMulut);
        KetGastrointestinalMulut.setBounds(230, 840, 190, 23);

        GastrointestinalGigi.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "TAK", "Karies", "Goyang", "Lain-lain" }));
        GastrointestinalGigi.setName("GastrointestinalGigi"); // NOI18N
        GastrointestinalGigi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                GastrointestinalGigiKeyPressed(evt);
            }
        });
        FormInput.add(GastrointestinalGigi);
        GastrointestinalGigi.setBounds(110, 900, 95, 23);

        jLabel148.setText("Gigi :");
        jLabel148.setName("jLabel148"); // NOI18N
        FormInput.add(jLabel148);
        jLabel148.setBounds(0, 900, 109, 23);

        KetGastrointestinalGigi.setFocusTraversalPolicyProvider(true);
        KetGastrointestinalGigi.setName("KetGastrointestinalGigi"); // NOI18N
        KetGastrointestinalGigi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KetGastrointestinalGigiKeyPressed(evt);
            }
        });
        FormInput.add(KetGastrointestinalGigi);
        KetGastrointestinalGigi.setBounds(200, 900, 170, 23);

        jLabel149.setText("Anus :");
        jLabel149.setName("jLabel149"); // NOI18N
        FormInput.add(jLabel149);
        jLabel149.setBounds(690, 900, 50, 23);

        GastrointestinalAnus.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "TAK", "Atresia Ani" }));
        GastrointestinalAnus.setName("GastrointestinalAnus"); // NOI18N
        GastrointestinalAnus.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                GastrointestinalAnusKeyPressed(evt);
            }
        });
        FormInput.add(GastrointestinalAnus);
        GastrointestinalAnus.setBounds(740, 900, 108, 23);

        jLabel147.setText("Lidah :");
        jLabel147.setName("jLabel147"); // NOI18N
        FormInput.add(jLabel147);
        jLabel147.setBounds(0, 870, 109, 23);

        GastrointestinalLidah.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "TAK", "Kotor", "Gerak Asimetris", "Lain-lain" }));
        GastrointestinalLidah.setName("GastrointestinalLidah"); // NOI18N
        GastrointestinalLidah.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                GastrointestinalLidahKeyPressed(evt);
            }
        });
        FormInput.add(GastrointestinalLidah);
        GastrointestinalLidah.setBounds(110, 870, 130, 23);

        KetGastrointestinalLidah.setFocusTraversalPolicyProvider(true);
        KetGastrointestinalLidah.setName("KetGastrointestinalLidah"); // NOI18N
        KetGastrointestinalLidah.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KetGastrointestinalLidahKeyPressed(evt);
            }
        });
        FormInput.add(KetGastrointestinalLidah);
        KetGastrointestinalLidah.setBounds(240, 870, 190, 23);

        jLabel150.setText("Tenggorokan :");
        jLabel150.setName("jLabel150"); // NOI18N
        FormInput.add(jLabel150);
        jLabel150.setBounds(450, 840, 80, 23);

        GastrointestinalTenggorakan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "TAK", "Gangguan Menelan", "Sakit Menelan", "Lain-lain" }));
        GastrointestinalTenggorakan.setName("GastrointestinalTenggorakan"); // NOI18N
        GastrointestinalTenggorakan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                GastrointestinalTenggorakanKeyPressed(evt);
            }
        });
        FormInput.add(GastrointestinalTenggorakan);
        GastrointestinalTenggorakan.setBounds(530, 840, 150, 23);

        KetGastrointestinalTenggorakan.setFocusTraversalPolicyProvider(true);
        KetGastrointestinalTenggorakan.setName("KetGastrointestinalTenggorakan"); // NOI18N
        KetGastrointestinalTenggorakan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KetGastrointestinalTenggorakanKeyPressed(evt);
            }
        });
        FormInput.add(KetGastrointestinalTenggorakan);
        KetGastrointestinalTenggorakan.setBounds(680, 840, 164, 23);

        jLabel151.setText("Abdomen :");
        jLabel151.setName("jLabel151"); // NOI18N
        FormInput.add(jLabel151);
        jLabel151.setBounds(450, 870, 80, 23);

        GastrointestinalAbdomen.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Supel", "Asictes", "Tegang", "Nyeri Tekan/Lepas", "Lain-lain" }));
        GastrointestinalAbdomen.setName("GastrointestinalAbdomen"); // NOI18N
        GastrointestinalAbdomen.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                GastrointestinalAbdomenKeyPressed(evt);
            }
        });
        FormInput.add(GastrointestinalAbdomen);
        GastrointestinalAbdomen.setBounds(530, 870, 150, 23);

        KetGastrointestinalAbdomen.setFocusTraversalPolicyProvider(true);
        KetGastrointestinalAbdomen.setName("KetGastrointestinalAbdomen"); // NOI18N
        KetGastrointestinalAbdomen.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KetGastrointestinalAbdomenKeyPressed(evt);
            }
        });
        FormInput.add(KetGastrointestinalAbdomen);
        KetGastrointestinalAbdomen.setBounds(680, 870, 164, 23);

        jLabel152.setText("Peistatik Usus :");
        jLabel152.setName("jLabel152"); // NOI18N
        FormInput.add(jLabel152);
        jLabel152.setBounds(390, 900, 100, 23);

        GastrointestinalUsus.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "TAK", "Tidak Ada Bising Usus", "Hiperistaltik", "Hiperperistaltik" }));
        GastrointestinalUsus.setName("GastrointestinalUsus"); // NOI18N
        GastrointestinalUsus.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                GastrointestinalUsusKeyPressed(evt);
            }
        });
        FormInput.add(GastrointestinalUsus);
        GastrointestinalUsus.setBounds(490, 900, 164, 23);

        jLabel50.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel50.setText("Neurologi :");
        jLabel50.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel50.setName("jLabel50"); // NOI18N
        FormInput.add(jLabel50);
        jLabel50.setBounds(10, 930, 98, 23);

        jLabel153.setText("Sensorik :");
        jLabel153.setName("jLabel153"); // NOI18N
        FormInput.add(jLabel153);
        jLabel153.setBounds(0, 950, 109, 23);

        NeurologiSensorik.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "TAK", "Sakit Nyeri", "Rasa Kebas", "Lain-lain" }));
        NeurologiSensorik.setName("NeurologiSensorik"); // NOI18N
        NeurologiSensorik.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                NeurologiSensorikKeyPressed(evt);
            }
        });
        FormInput.add(NeurologiSensorik);
        NeurologiSensorik.setBounds(110, 950, 108, 23);

        jLabel154.setText("Motorik :");
        jLabel154.setName("jLabel154"); // NOI18N
        FormInput.add(jLabel154);
        jLabel154.setBounds(0, 980, 109, 23);

        NeurologiMotorik.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "TAK", "Hemiparese", "Tetraparese", "Tremor", "Lain-lain" }));
        NeurologiMotorik.setName("NeurologiMotorik"); // NOI18N
        NeurologiMotorik.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                NeurologiMotorikKeyPressed(evt);
            }
        });
        FormInput.add(NeurologiMotorik);
        NeurologiMotorik.setBounds(110, 980, 108, 23);

        jLabel155.setText("Otot :");
        jLabel155.setName("jLabel155"); // NOI18N
        FormInput.add(jLabel155);
        jLabel155.setBounds(720, 980, 40, 23);

        NeurologiOtot.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Kuat", "Lemah" }));
        NeurologiOtot.setName("NeurologiOtot"); // NOI18N
        NeurologiOtot.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                NeurologiOtotKeyPressed(evt);
            }
        });
        FormInput.add(NeurologiOtot);
        NeurologiOtot.setBounds(770, 980, 82, 23);

        jLabel156.setText("Penglihatan :");
        jLabel156.setName("jLabel156"); // NOI18N
        FormInput.add(jLabel156);
        jLabel156.setBounds(220, 950, 80, 23);

        NeurologiPenglihatan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "TAK", "Ada Kelainan" }));
        NeurologiPenglihatan.setName("NeurologiPenglihatan"); // NOI18N
        NeurologiPenglihatan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                NeurologiPenglihatanKeyPressed(evt);
            }
        });
        FormInput.add(NeurologiPenglihatan);
        NeurologiPenglihatan.setBounds(310, 950, 115, 23);

        KetNeurologiPenglihatan.setFocusTraversalPolicyProvider(true);
        KetNeurologiPenglihatan.setName("KetNeurologiPenglihatan"); // NOI18N
        KetNeurologiPenglihatan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KetNeurologiPenglihatanKeyPressed(evt);
            }
        });
        FormInput.add(KetNeurologiPenglihatan);
        KetNeurologiPenglihatan.setBounds(430, 950, 150, 23);

        jLabel157.setText("Alat Bantu Penglihatan :");
        jLabel157.setName("jLabel157"); // NOI18N
        FormInput.add(jLabel157);
        jLabel157.setBounds(580, 950, 140, 23);

        NeurologiAlatBantuPenglihatan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Kacamata", "Lensa Kontak" }));
        NeurologiAlatBantuPenglihatan.setName("NeurologiAlatBantuPenglihatan"); // NOI18N
        NeurologiAlatBantuPenglihatan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                NeurologiAlatBantuPenglihatanKeyPressed(evt);
            }
        });
        FormInput.add(NeurologiAlatBantuPenglihatan);
        NeurologiAlatBantuPenglihatan.setBounds(730, 950, 120, 23);

        jLabel158.setText("Pendengaran :");
        jLabel158.setName("jLabel158"); // NOI18N
        FormInput.add(jLabel158);
        jLabel158.setBounds(220, 980, 80, 23);

        NeurologiPendengaran.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "TAK", "Berdengung", "Nyeri", "Tuli", "Keluar Cairan", "Lain-lain" }));
        NeurologiPendengaran.setName("NeurologiPendengaran"); // NOI18N
        NeurologiPendengaran.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                NeurologiPendengaranKeyPressed(evt);
            }
        });
        FormInput.add(NeurologiPendengaran);
        NeurologiPendengaran.setBounds(310, 980, 117, 23);

        jLabel159.setText("Bicara :");
        jLabel159.setName("jLabel159"); // NOI18N
        FormInput.add(jLabel159);
        jLabel159.setBounds(430, 980, 50, 23);

        NeurologiBicara.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Jelas", "Tidak Jelas" }));
        NeurologiBicara.setName("NeurologiBicara"); // NOI18N
        NeurologiBicara.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                NeurologiBicaraKeyPressed(evt);
            }
        });
        FormInput.add(NeurologiBicara);
        NeurologiBicara.setBounds(480, 980, 105, 23);

        KetNeurologiBicara.setFocusTraversalPolicyProvider(true);
        KetNeurologiBicara.setName("KetNeurologiBicara"); // NOI18N
        KetNeurologiBicara.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KetNeurologiBicaraKeyPressed(evt);
            }
        });
        FormInput.add(KetNeurologiBicara);
        KetNeurologiBicara.setBounds(590, 980, 127, 23);

        jLabel51.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel51.setText("Integument :");
        jLabel51.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel51.setName("jLabel51"); // NOI18N
        FormInput.add(jLabel51);
        jLabel51.setBounds(10, 1010, 108, 23);

        jLabel160.setText("Kulit :");
        jLabel160.setName("jLabel160"); // NOI18N
        FormInput.add(jLabel160);
        jLabel160.setBounds(0, 1030, 109, 23);

        IntegumentKulit.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Normal", "Rash/Kemerahan", "Luka", "Memar", "Ptekie", "Bula" }));
        IntegumentKulit.setName("IntegumentKulit"); // NOI18N
        IntegumentKulit.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                IntegumentKulitKeyPressed(evt);
            }
        });
        FormInput.add(IntegumentKulit);
        IntegumentKulit.setBounds(110, 1030, 134, 23);

        jLabel161.setText("Warna Kulit :");
        jLabel161.setName("jLabel161"); // NOI18N
        FormInput.add(jLabel161);
        jLabel161.setBounds(240, 1030, 70, 23);

        IntegumentWarnaKulit.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Normal", "Pucat", "Sianosis", "Lain-lain" }));
        IntegumentWarnaKulit.setName("IntegumentWarnaKulit"); // NOI18N
        IntegumentWarnaKulit.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                IntegumentWarnaKulitKeyPressed(evt);
            }
        });
        FormInput.add(IntegumentWarnaKulit);
        IntegumentWarnaKulit.setBounds(320, 1030, 92, 23);

        jLabel162.setText("Turgor :");
        jLabel162.setName("jLabel162"); // NOI18N
        FormInput.add(jLabel162);
        jLabel162.setBounds(410, 1030, 48, 23);

        IntegumentTurgor.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Baik", "Sedang", "Buruk" }));
        IntegumentTurgor.setName("IntegumentTurgor"); // NOI18N
        IntegumentTurgor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                IntegumentTurgorKeyPressed(evt);
            }
        });
        FormInput.add(IntegumentTurgor);
        IntegumentTurgor.setBounds(470, 1030, 86, 23);

        IntegumentDecubitus.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak Ada", "Usia > 65 tahun", "Obesitas", "Imobilisasi", "Paraplegi/Vegetative State ", "Dirawat Di HCU", "Penyakit Kronis (DM, CHF, CKD)", "Inkontinentia Uri/Alvi" }));
        IntegumentDecubitus.setName("IntegumentDecubitus"); // NOI18N
        IntegumentDecubitus.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                IntegumentDecubitusKeyPressed(evt);
            }
        });
        FormInput.add(IntegumentDecubitus);
        IntegumentDecubitus.setBounds(650, 1030, 202, 23);

        jLabel163.setText("Resiko Decubi :");
        jLabel163.setName("jLabel163"); // NOI18N
        FormInput.add(jLabel163);
        jLabel163.setBounds(550, 1030, 90, 23);

        jLabel52.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel52.setText("Ekstremitas :");
        jLabel52.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel52.setName("jLabel52"); // NOI18N
        FormInput.add(jLabel52);
        jLabel52.setBounds(10, 1140, 122, 23);

        jLabel164.setText("Oedema :");
        jLabel164.setName("jLabel164"); // NOI18N
        FormInput.add(jLabel164);
        jLabel164.setBounds(0, 1080, 100, 23);

        MuskuloskletalOedema.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak Ada", "Ada" }));
        MuskuloskletalOedema.setName("MuskuloskletalOedema"); // NOI18N
        MuskuloskletalOedema.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                MuskuloskletalOedemaKeyPressed(evt);
            }
        });
        FormInput.add(MuskuloskletalOedema);
        MuskuloskletalOedema.setBounds(100, 1080, 100, 23);

        KetMuskuloskletalOedema.setFocusTraversalPolicyProvider(true);
        KetMuskuloskletalOedema.setName("KetMuskuloskletalOedema"); // NOI18N
        KetMuskuloskletalOedema.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KetMuskuloskletalOedemaKeyPressed(evt);
            }
        });
        FormInput.add(KetMuskuloskletalOedema);
        KetMuskuloskletalOedema.setBounds(210, 1080, 220, 23);

        KetMuskuloskletalFraktur.setFocusTraversalPolicyProvider(true);
        KetMuskuloskletalFraktur.setName("KetMuskuloskletalFraktur"); // NOI18N
        KetMuskuloskletalFraktur.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KetMuskuloskletalFrakturKeyPressed(evt);
            }
        });
        FormInput.add(KetMuskuloskletalFraktur);
        KetMuskuloskletalFraktur.setBounds(210, 1110, 220, 23);

        MuskuloskletalFraktur.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak Ada", "Ada" }));
        MuskuloskletalFraktur.setName("MuskuloskletalFraktur"); // NOI18N
        MuskuloskletalFraktur.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                MuskuloskletalFrakturKeyPressed(evt);
            }
        });
        FormInput.add(MuskuloskletalFraktur);
        MuskuloskletalFraktur.setBounds(100, 1110, 100, 23);

        jLabel165.setText("Lesi :");
        jLabel165.setName("jLabel165"); // NOI18N
        FormInput.add(jLabel165);
        jLabel165.setBounds(0, 1280, 109, 23);

        jLabel166.setText("Nyeri Sendi :");
        jLabel166.setName("jLabel166"); // NOI18N
        FormInput.add(jLabel166);
        jLabel166.setBounds(440, 1110, 80, 23);

        MuskuloskletalNyeriSendi.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak Ada", "Ada" }));
        MuskuloskletalNyeriSendi.setName("MuskuloskletalNyeriSendi"); // NOI18N
        MuskuloskletalNyeriSendi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                MuskuloskletalNyeriSendiKeyPressed(evt);
            }
        });
        FormInput.add(MuskuloskletalNyeriSendi);
        MuskuloskletalNyeriSendi.setBounds(520, 1110, 100, 23);

        KetMuskuloskletalNyeriSendi.setFocusTraversalPolicyProvider(true);
        KetMuskuloskletalNyeriSendi.setName("KetMuskuloskletalNyeriSendi"); // NOI18N
        KetMuskuloskletalNyeriSendi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KetMuskuloskletalNyeriSendiKeyPressed(evt);
            }
        });
        FormInput.add(KetMuskuloskletalNyeriSendi);
        KetMuskuloskletalNyeriSendi.setBounds(630, 1110, 220, 23);

        jLabel167.setText("Pergerakan Sendi :");
        jLabel167.setName("jLabel167"); // NOI18N
        FormInput.add(jLabel167);
        jLabel167.setBounds(450, 1080, 109, 23);

        MuskuloskletalPegerakanSendi.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Bebas", "Terbatas" }));
        MuskuloskletalPegerakanSendi.setName("MuskuloskletalPegerakanSendi"); // NOI18N
        MuskuloskletalPegerakanSendi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                MuskuloskletalPegerakanSendiKeyPressed(evt);
            }
        });
        FormInput.add(MuskuloskletalPegerakanSendi);
        MuskuloskletalPegerakanSendi.setBounds(560, 1080, 93, 23);

        MuskuloskletalKekuatanOtot.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Baik", "Lemah", "Tremor" }));
        MuskuloskletalKekuatanOtot.setName("MuskuloskletalKekuatanOtot"); // NOI18N
        MuskuloskletalKekuatanOtot.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                MuskuloskletalKekuatanOtotKeyPressed(evt);
            }
        });
        FormInput.add(MuskuloskletalKekuatanOtot);
        MuskuloskletalKekuatanOtot.setBounds(760, 1080, 85, 23);

        jLabel168.setText("Kekuatan Otot :");
        jLabel168.setName("jLabel168"); // NOI18N
        FormInput.add(jLabel168);
        jLabel168.setBounds(670, 1080, 90, 23);

        jLabel53.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel53.setText("Eliminasi :");
        jLabel53.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel53.setName("jLabel53"); // NOI18N
        FormInput.add(jLabel53);
        jLabel53.setBounds(10, 1190, 95, 23);

        jLabel113.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel113.setText("X/");
        jLabel113.setName("jLabel113"); // NOI18N
        FormInput.add(jLabel113);
        jLabel113.setBounds(220, 1210, 13, 23);

        KBAB.setHighlighter(null);
        KBAB.setName("KBAB"); // NOI18N
        KBAB.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KBABKeyPressed(evt);
            }
        });
        FormInput.add(KBAB);
        KBAB.setBounds(420, 1210, 175, 23);

        jLabel114.setText("Konsistensi :");
        jLabel114.setName("jLabel114"); // NOI18N
        FormInput.add(jLabel114);
        jLabel114.setBounds(340, 1210, 70, 23);

        BAB.setHighlighter(null);
        BAB.setName("BAB"); // NOI18N
        BAB.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BABKeyPressed(evt);
            }
        });
        FormInput.add(BAB);
        BAB.setBounds(170, 1210, 50, 23);

        jLabel115.setText("BAB : Frekuensi :");
        jLabel115.setName("jLabel115"); // NOI18N
        FormInput.add(jLabel115);
        jLabel115.setBounds(0, 1210, 166, 23);

        XBAB.setHighlighter(null);
        XBAB.setName("XBAB"); // NOI18N
        XBAB.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                XBABKeyPressed(evt);
            }
        });
        FormInput.add(XBAB);
        XBAB.setBounds(240, 1210, 75, 23);

        jLabel116.setText("Warna :");
        jLabel116.setName("jLabel116"); // NOI18N
        FormInput.add(jLabel116);
        jLabel116.setBounds(620, 1210, 55, 23);

        WBAB.setHighlighter(null);
        WBAB.setName("WBAB"); // NOI18N
        WBAB.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                WBABKeyPressed(evt);
            }
        });
        FormInput.add(WBAB);
        WBAB.setBounds(680, 1210, 175, 23);

        jLabel117.setText("BAK : Frekuensi :");
        jLabel117.setName("jLabel117"); // NOI18N
        FormInput.add(jLabel117);
        jLabel117.setBounds(0, 1240, 166, 23);

        BAK.setHighlighter(null);
        BAK.setName("BAK"); // NOI18N
        BAK.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BAKKeyPressed(evt);
            }
        });
        FormInput.add(BAK);
        BAK.setBounds(170, 1240, 50, 23);

        jLabel118.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel118.setText("X/");
        jLabel118.setName("jLabel118"); // NOI18N
        FormInput.add(jLabel118);
        jLabel118.setBounds(220, 1240, 13, 23);

        XBAK.setHighlighter(null);
        XBAK.setName("XBAK"); // NOI18N
        XBAK.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                XBAKKeyPressed(evt);
            }
        });
        FormInput.add(XBAK);
        XBAK.setBounds(240, 1240, 75, 23);

        jLabel119.setText("Warna :");
        jLabel119.setName("jLabel119"); // NOI18N
        FormInput.add(jLabel119);
        jLabel119.setBounds(340, 1240, 70, 23);

        WBAK.setHighlighter(null);
        WBAK.setName("WBAK"); // NOI18N
        WBAK.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                WBAKKeyPressed(evt);
            }
        });
        FormInput.add(WBAK);
        WBAK.setBounds(420, 1240, 175, 23);

        jLabel120.setText("Lain-lain :");
        jLabel120.setName("jLabel120"); // NOI18N
        FormInput.add(jLabel120);
        jLabel120.setBounds(620, 1240, 55, 23);

        LBAK.setHighlighter(null);
        LBAK.setName("LBAK"); // NOI18N
        LBAK.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                LBAKKeyPressed(evt);
            }
        });
        FormInput.add(LBAK);
        LBAK.setBounds(680, 1240, 175, 23);

        PolaAktifitasEliminasi.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak Ada", "Pacu Jantung", "Gigi Palsu", "Implant", "Penyangga Tubuh" }));
        PolaAktifitasEliminasi.setName("PolaAktifitasEliminasi"); // NOI18N
        PolaAktifitasEliminasi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PolaAktifitasEliminasiKeyPressed(evt);
            }
        });
        FormInput.add(PolaAktifitasEliminasi);
        PolaAktifitasEliminasi.setBounds(120, 1330, 100, 23);

        jLabel171.setText("Cairan Abnormal :");
        jLabel171.setName("jLabel171"); // NOI18N
        FormInput.add(jLabel171);
        jLabel171.setBounds(250, 1280, 100, 23);

        jLabel172.setText("Bau :");
        jLabel172.setName("jLabel172"); // NOI18N
        FormInput.add(jLabel172);
        jLabel172.setBounds(480, 1280, 80, 23);

        jLabel173.setText("Alat Bantu :");
        jLabel173.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel173.setName("jLabel173"); // NOI18N
        FormInput.add(jLabel173);
        jLabel173.setBounds(0, 1330, 109, 23);

        jLabel174.setText("Skrining Nyeri :");
        jLabel174.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel174.setName("jLabel174"); // NOI18N
        FormInput.add(jLabel174);
        jLabel174.setBounds(10, 1360, 100, 23);

        PolaAktifitasMandi.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        PolaAktifitasMandi.setName("PolaAktifitasMandi"); // NOI18N
        PolaAktifitasMandi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PolaAktifitasMandiKeyPressed(evt);
            }
        });
        FormInput.add(PolaAktifitasMandi);
        PolaAktifitasMandi.setBounds(120, 1280, 100, 23);

        PolaAktifitasMakan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak Ada", "Lendir", "Darah", "Pus" }));
        PolaAktifitasMakan.setName("PolaAktifitasMakan"); // NOI18N
        PolaAktifitasMakan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PolaAktifitasMakanKeyPressed(evt);
            }
        });
        FormInput.add(PolaAktifitasMakan);
        PolaAktifitasMakan.setBounds(360, 1280, 90, 23);

        PolaAktifitasBerpakaian.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        PolaAktifitasBerpakaian.setName("PolaAktifitasBerpakaian"); // NOI18N
        PolaAktifitasBerpakaian.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PolaAktifitasBerpakaianKeyPressed(evt);
            }
        });
        FormInput.add(PolaAktifitasBerpakaian);
        PolaAktifitasBerpakaian.setBounds(570, 1280, 90, 23);

        PolaAktifitasBerpindah.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        PolaAktifitasBerpindah.setName("PolaAktifitasBerpindah"); // NOI18N
        PolaAktifitasBerpindah.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PolaAktifitasBerpindahKeyPressed(evt);
            }
        });
        FormInput.add(PolaAktifitasBerpindah);
        PolaAktifitasBerpindah.setBounds(120, 1360, 100, 23);

        jLabel55.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel55.setText("Genitalia :");
        jLabel55.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel55.setName("jLabel55"); // NOI18N
        FormInput.add(jLabel55);
        jLabel55.setBounds(10, 1260, 128, 23);

        jLabel121.setText("Volume :");
        jLabel121.setName("jLabel121"); // NOI18N
        FormInput.add(jLabel121);
        jLabel121.setBounds(700, 1280, 73, 23);

        PolaNutrisiPorsi.setHighlighter(null);
        PolaNutrisiPorsi.setName("PolaNutrisiPorsi"); // NOI18N
        PolaNutrisiPorsi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PolaNutrisiPorsiKeyPressed(evt);
            }
        });
        FormInput.add(PolaNutrisiPorsi);
        PolaNutrisiPorsi.setBounds(780, 1280, 50, 23);

        jLabel122.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel122.setText("cc");
        jLabel122.setName("jLabel122"); // NOI18N
        FormInput.add(jLabel122);
        jLabel122.setBounds(840, 1280, 31, 23);

        jLabel123.setText("Time (T) :");
        jLabel123.setName("jLabel123"); // NOI18N
        FormInput.add(jLabel123);
        jLabel123.setBounds(330, 1420, 80, 23);

        PolaNutrisiFrekuensi.setHighlighter(null);
        PolaNutrisiFrekuensi.setName("PolaNutrisiFrekuensi"); // NOI18N
        PolaNutrisiFrekuensi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PolaNutrisiFrekuensiKeyPressed(evt);
            }
        });
        FormInput.add(PolaNutrisiFrekuensi);
        PolaNutrisiFrekuensi.setBounds(120, 1390, 160, 23);

        jLabel177.setText("Quality (Q) :");
        jLabel177.setName("jLabel177"); // NOI18N
        FormInput.add(jLabel177);
        jLabel177.setBounds(20, 1420, 92, 23);

        PolaNutrisiJenis.setHighlighter(null);
        PolaNutrisiJenis.setName("PolaNutrisiJenis"); // NOI18N
        PolaNutrisiJenis.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PolaNutrisiJenisKeyPressed(evt);
            }
        });
        FormInput.add(PolaNutrisiJenis);
        PolaNutrisiJenis.setBounds(120, 1420, 160, 23);

        jLabel176.setText("Regio (R) :");
        jLabel176.setName("jLabel176"); // NOI18N
        FormInput.add(jLabel176);
        jLabel176.setBounds(50, 1450, 60, 23);

        PolaTidurLama.setHighlighter(null);
        PolaTidurLama.setName("PolaTidurLama"); // NOI18N
        PolaTidurLama.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PolaTidurLamaKeyPressed(evt);
            }
        });
        FormInput.add(PolaTidurLama);
        PolaTidurLama.setBounds(120, 1450, 160, 23);

        jLabel178.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel178.setText(" jam/hari,");
        jLabel178.setName("jLabel178"); // NOI18N
        FormInput.add(jLabel178);
        jLabel178.setBounds(70, 3100, 51, 23);

        PolaTidurGangguan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak Ada Gangguan", "Insomnia" }));
        PolaTidurGangguan.setName("PolaTidurGangguan"); // NOI18N
        PolaTidurGangguan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PolaTidurGangguanKeyPressed(evt);
            }
        });
        FormInput.add(PolaTidurGangguan);
        PolaTidurGangguan.setBounds(120, 3100, 164, 23);

        jLabel179.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel179.setText("a. Kemampuan Aktifitas Sehari-hari");
        jLabel179.setName("jLabel179"); // NOI18N
        FormInput.add(jLabel179);
        jLabel179.setBounds(410, 3080, 180, 23);

        AktifitasSehari2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Mandiri", "Bantuan minimal", "Bantuan Sebagian", "Ketergantungan Total" }));
        AktifitasSehari2.setName("AktifitasSehari2"); // NOI18N
        AktifitasSehari2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                AktifitasSehari2KeyPressed(evt);
            }
        });
        FormInput.add(AktifitasSehari2);
        AktifitasSehari2.setBounds(600, 3080, 158, 23);

        jLabel181.setText("b. Berjalan :");
        jLabel181.setName("jLabel181"); // NOI18N
        FormInput.add(jLabel181);
        jLabel181.setBounds(760, 3080, 70, 23);

        Berjalan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "TAK", "Penurunan Kekuatan/ROM", "Paralisis", "Sering Jatuh", "Deformitas", "Hilang keseimbangan", "Riwayat Patah Tulang", "Lain-lain" }));
        Berjalan.setName("Berjalan"); // NOI18N
        Berjalan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BerjalanKeyPressed(evt);
            }
        });
        FormInput.add(Berjalan);
        Berjalan.setBounds(840, 3080, 178, 23);

        KeteranganBerjalan.setFocusTraversalPolicyProvider(true);
        KeteranganBerjalan.setName("KeteranganBerjalan"); // NOI18N
        KeteranganBerjalan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KeteranganBerjalanKeyPressed(evt);
            }
        });
        FormInput.add(KeteranganBerjalan);
        KeteranganBerjalan.setBounds(420, 1390, 200, 23);

        jLabel182.setText("Kiri Atas :");
        jLabel182.setName("jLabel182"); // NOI18N
        FormInput.add(jLabel182);
        jLabel182.setBounds(430, 1160, 90, 23);

        Aktifitas.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "5", "4", "3", "2", "1", "0" }));
        Aktifitas.setName("Aktifitas"); // NOI18N
        Aktifitas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                AktifitasKeyPressed(evt);
            }
        });
        FormInput.add(Aktifitas);
        Aktifitas.setBounds(530, 1160, 90, 23);

        jLabel183.setText("Kiri Bawah :");
        jLabel183.setName("jLabel183"); // NOI18N
        FormInput.add(jLabel183);
        jLabel183.setBounds(650, 1160, 100, 23);

        AlatAmbulasi.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "5", "4", "3", "2", "1", "0" }));
        AlatAmbulasi.setName("AlatAmbulasi"); // NOI18N
        AlatAmbulasi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                AlatAmbulasiKeyPressed(evt);
            }
        });
        FormInput.add(AlatAmbulasi);
        AlatAmbulasi.setBounds(760, 1160, 80, 23);

        jLabel184.setText("Kanan Atas :");
        jLabel184.setName("jLabel184"); // NOI18N
        FormInput.add(jLabel184);
        jLabel184.setBounds(0, 1160, 100, 23);

        EkstrimitasAtas.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "5", "4", "3", "2", "1", "0" }));
        EkstrimitasAtas.setName("EkstrimitasAtas"); // NOI18N
        EkstrimitasAtas.setPreferredSize(new java.awt.Dimension(80, 20));
        EkstrimitasAtas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                EkstrimitasAtasKeyPressed(evt);
            }
        });
        FormInput.add(EkstrimitasAtas);
        EkstrimitasAtas.setBounds(100, 1160, 100, 23);

        KeteranganEkstrimitasAtas.setFocusTraversalPolicyProvider(true);
        KeteranganEkstrimitasAtas.setName("KeteranganEkstrimitasAtas"); // NOI18N
        KeteranganEkstrimitasAtas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KeteranganEkstrimitasAtasKeyPressed(evt);
            }
        });
        FormInput.add(KeteranganEkstrimitasAtas);
        KeteranganEkstrimitasAtas.setBounds(420, 1420, 200, 23);

        jLabel185.setText("Kanan Bawah :");
        jLabel185.setName("jLabel185"); // NOI18N
        FormInput.add(jLabel185);
        jLabel185.setBounds(210, 1160, 110, 23);

        EkstrimitasBawah.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "5", "4", "3", "2", "1", "0" }));
        EkstrimitasBawah.setName("EkstrimitasBawah"); // NOI18N
        EkstrimitasBawah.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                EkstrimitasBawahKeyPressed(evt);
            }
        });
        FormInput.add(EkstrimitasBawah);
        EkstrimitasBawah.setBounds(320, 1160, 100, 23);

        KeteranganEkstrimitasBawah.setFocusTraversalPolicyProvider(true);
        KeteranganEkstrimitasBawah.setName("KeteranganEkstrimitasBawah"); // NOI18N
        KeteranganEkstrimitasBawah.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KeteranganEkstrimitasBawahKeyPressed(evt);
            }
        });
        FormInput.add(KeteranganEkstrimitasBawah);
        KeteranganEkstrimitasBawah.setBounds(360, 1330, 260, 23);

        jLabel186.setText("Pengobatan Alternatif Sebelumnya :");
        jLabel186.setName("jLabel186"); // NOI18N
        FormInput.add(jLabel186);
        jLabel186.setBounds(10, 1580, 180, 23);

        KemampuanMenggenggam.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        KemampuanMenggenggam.setName("KemampuanMenggenggam"); // NOI18N
        KemampuanMenggenggam.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KemampuanMenggenggamKeyPressed(evt);
            }
        });
        FormInput.add(KemampuanMenggenggam);
        KemampuanMenggenggam.setBounds(200, 1580, 110, 23);

        KeteranganKemampuanMenggenggam.setFocusTraversalPolicyProvider(true);
        KeteranganKemampuanMenggenggam.setName("KeteranganKemampuanMenggenggam"); // NOI18N
        KeteranganKemampuanMenggenggam.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KeteranganKemampuanMenggenggamKeyPressed(evt);
            }
        });
        FormInput.add(KeteranganKemampuanMenggenggam);
        KeteranganKemampuanMenggenggam.setBounds(320, 1580, 220, 23);

        jLabel187.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel187.setText("Prog. Bertentangan Keyakinan :");
        jLabel187.setName("jLabel187"); // NOI18N
        FormInput.add(jLabel187);
        jLabel187.setBounds(460, 1520, 160, 23);

        KemampuanKoordinasi.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        KemampuanKoordinasi.setName("KemampuanKoordinasi"); // NOI18N
        KemampuanKoordinasi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KemampuanKoordinasiKeyPressed(evt);
            }
        });
        FormInput.add(KemampuanKoordinasi);
        KemampuanKoordinasi.setBounds(620, 1520, 110, 23);

        KeteranganKemampuanKoordinasi.setFocusTraversalPolicyProvider(true);
        KeteranganKemampuanKoordinasi.setName("KeteranganKemampuanKoordinasi"); // NOI18N
        KeteranganKemampuanKoordinasi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KeteranganKemampuanKoordinasiKeyPressed(evt);
            }
        });
        FormInput.add(KeteranganKemampuanKoordinasi);
        KeteranganKemampuanKoordinasi.setBounds(730, 1520, 130, 23);

        jLabel188.setText("i. Kesimpulan Gangguan Fungsi :");
        jLabel188.setName("jLabel188"); // NOI18N
        FormInput.add(jLabel188);
        jLabel188.setBounds(70, 3130, 170, 23);

        KesimpulanGangguanFungsi.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak (Tidak Perlu Co DPJP)", "Ya (Co DPJP)" }));
        KesimpulanGangguanFungsi.setName("KesimpulanGangguanFungsi"); // NOI18N
        KesimpulanGangguanFungsi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KesimpulanGangguanFungsiKeyPressed(evt);
            }
        });
        FormInput.add(KesimpulanGangguanFungsi);
        KesimpulanGangguanFungsi.setBounds(240, 3130, 195, 23);

        KondisiPsikologis.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tenang", "Marah", "Sedih", "Lainnya", "Cemas", "Takut" }));
        KondisiPsikologis.setName("KondisiPsikologis"); // NOI18N
        KondisiPsikologis.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KondisiPsikologisKeyPressed(evt);
            }
        });
        FormInput.add(KondisiPsikologis);
        KondisiPsikologis.setBounds(80, 1520, 110, 23);

        jLabel191.setText("Pencari Nafkah Utama :");
        jLabel191.setName("jLabel191"); // NOI18N
        FormInput.add(jLabel191);
        jLabel191.setBounds(190, 1520, 120, 23);

        AdakahPerilaku.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        AdakahPerilaku.setName("AdakahPerilaku"); // NOI18N
        AdakahPerilaku.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                AdakahPerilakuKeyPressed(evt);
            }
        });
        FormInput.add(AdakahPerilaku);
        AdakahPerilaku.setBounds(320, 1520, 130, 23);

        KeteranganAdakahPerilaku.setFocusTraversalPolicyProvider(true);
        KeteranganAdakahPerilaku.setName("KeteranganAdakahPerilaku"); // NOI18N
        KeteranganAdakahPerilaku.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KeteranganAdakahPerilakuKeyPressed(evt);
            }
        });
        FormInput.add(KeteranganAdakahPerilaku);
        KeteranganAdakahPerilaku.setBounds(690, 3120, 202, 23);

        GangguanJiwa.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        GangguanJiwa.setName("GangguanJiwa"); // NOI18N
        GangguanJiwa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                GangguanJiwaKeyPressed(evt);
            }
        });
        FormInput.add(GangguanJiwa);
        GangguanJiwa.setBounds(780, 1550, 77, 23);

        jLabel193.setText("Hubungan Dengan Keluarga :");
        jLabel193.setName("jLabel193"); // NOI18N
        FormInput.add(jLabel193);
        jLabel193.setBounds(260, 1550, 150, 23);

        HubunganAnggotaKeluarga.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Harmonis", "Kurang Harmonis", "Tidak Harmonis", "Konflik Besar" }));
        HubunganAnggotaKeluarga.setName("HubunganAnggotaKeluarga"); // NOI18N
        HubunganAnggotaKeluarga.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                HubunganAnggotaKeluargaKeyPressed(evt);
            }
        });
        FormInput.add(HubunganAnggotaKeluarga);
        HubunganAnggotaKeluarga.setBounds(420, 1550, 120, 23);

        jLabel194.setText("e. Agama :");
        jLabel194.setName("jLabel194"); // NOI18N
        FormInput.add(jLabel194);
        jLabel194.setBounds(50, 3070, 60, 23);

        Agama.setEditable(false);
        Agama.setFocusTraversalPolicyProvider(true);
        Agama.setName("Agama"); // NOI18N
        FormInput.add(Agama);
        Agama.setBounds(120, 3070, 280, 23);

        TinggalDengan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Sendiri", "Orang Tua", "Suami/Istri", "Keluarga", "Lain-lain" }));
        TinggalDengan.setName("TinggalDengan"); // NOI18N
        TinggalDengan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TinggalDenganKeyPressed(evt);
            }
        });
        FormInput.add(TinggalDengan);
        TinggalDengan.setBounds(140, 1550, 110, 23);

        KeteranganTinggalDengan.setFocusTraversalPolicyProvider(true);
        KeteranganTinggalDengan.setName("KeteranganTinggalDengan"); // NOI18N
        KeteranganTinggalDengan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KeteranganTinggalDenganKeyPressed(evt);
            }
        });
        FormInput.add(KeteranganTinggalDengan);
        KeteranganTinggalDengan.setBounds(720, 1640, 137, 23);

        jLabel196.setText("g. Pekerjaan :");
        jLabel196.setName("jLabel196"); // NOI18N
        FormInput.add(jLabel196);
        jLabel196.setBounds(30, 3040, 83, 23);

        PekerjaanPasien.setEditable(false);
        PekerjaanPasien.setFocusTraversalPolicyProvider(true);
        PekerjaanPasien.setName("PekerjaanPasien"); // NOI18N
        FormInput.add(PekerjaanPasien);
        PekerjaanPasien.setBounds(110, 3040, 140, 23);

        jLabel197.setText("h. Pembayaran :");
        jLabel197.setName("jLabel197"); // NOI18N
        FormInput.add(jLabel197);
        jLabel197.setBounds(240, 3040, 90, 23);

        CaraBayar.setEditable(false);
        CaraBayar.setFocusTraversalPolicyProvider(true);
        CaraBayar.setName("CaraBayar"); // NOI18N
        FormInput.add(CaraBayar);
        CaraBayar.setBounds(330, 3040, 140, 23);

        NilaiKepercayaan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        NilaiKepercayaan.setName("NilaiKepercayaan"); // NOI18N
        NilaiKepercayaan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                NilaiKepercayaanKeyPressed(evt);
            }
        });
        FormInput.add(NilaiKepercayaan);
        NilaiKepercayaan.setBounds(270, 1610, 105, 23);

        KeteranganNilaiKepercayaan.setFocusTraversalPolicyProvider(true);
        KeteranganNilaiKepercayaan.setName("KeteranganNilaiKepercayaan"); // NOI18N
        KeteranganNilaiKepercayaan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KeteranganNilaiKepercayaanKeyPressed(evt);
            }
        });
        FormInput.add(KeteranganNilaiKepercayaan);
        KeteranganNilaiKepercayaan.setBounds(380, 1610, 160, 23);

        jLabel199.setText("Bahasa Sehari-hari :");
        jLabel199.setName("jLabel199"); // NOI18N
        FormInput.add(jLabel199);
        jLabel199.setBounds(620, 1580, 120, 23);

        Bahasa.setEditable(false);
        Bahasa.setFocusTraversalPolicyProvider(true);
        Bahasa.setName("Bahasa"); // NOI18N
        FormInput.add(Bahasa);
        Bahasa.setBounds(740, 1580, 120, 23);

        PendidikanPasien.setEditable(false);
        PendidikanPasien.setFocusTraversalPolicyProvider(true);
        PendidikanPasien.setName("PendidikanPasien"); // NOI18N
        FormInput.add(PendidikanPasien);
        PendidikanPasien.setBounds(160, 1640, 100, 23);

        jLabel201.setText("Kebutuhan Pemebelajaran Pasien :");
        jLabel201.setName("jLabel201"); // NOI18N
        FormInput.add(jLabel201);
        jLabel201.setBounds(430, 1640, 180, 23);

        PendidikanPJ.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Kemoterapi", "Radiasi", "Perawatan Luka", "Manajemen Nyeri", "Diet & Nutrisi", "Lain-Lain" }));
        PendidikanPJ.setName("PendidikanPJ"); // NOI18N
        PendidikanPJ.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PendidikanPJKeyPressed(evt);
            }
        });
        FormInput.add(PendidikanPJ);
        PendidikanPJ.setBounds(620, 1640, 90, 23);

        jLabel202.setText("Hambatan :");
        jLabel202.setName("jLabel202"); // NOI18N
        FormInput.add(jLabel202);
        jLabel202.setBounds(550, 1610, 60, 23);

        EdukasiPsikolgis.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Bahasa", "Pendengaran", "Hilang Memori", "Kognitif", "Penglihatan" }));
        EdukasiPsikolgis.setName("EdukasiPsikolgis"); // NOI18N
        EdukasiPsikolgis.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                EdukasiPsikolgisKeyPressed(evt);
            }
        });
        FormInput.add(EdukasiPsikolgis);
        EdukasiPsikolgis.setBounds(620, 1610, 95, 23);

        jSeparator8.setBackground(new java.awt.Color(239, 244, 234));
        jSeparator8.setForeground(new java.awt.Color(239, 244, 234));
        jSeparator8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(239, 244, 234)));
        jSeparator8.setName("jSeparator8"); // NOI18N
        FormInput.add(jSeparator8);
        jSeparator8.setBounds(0, 1670, 880, 1);

        KeteranganEdukasiPsikologis.setFocusTraversalPolicyProvider(true);
        KeteranganEdukasiPsikologis.setName("KeteranganEdukasiPsikologis"); // NOI18N
        KeteranganEdukasiPsikologis.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KeteranganEdukasiPsikologisKeyPressed(evt);
            }
        });
        FormInput.add(KeteranganEdukasiPsikologis);
        KeteranganEdukasiPsikologis.setBounds(720, 1610, 140, 23);

        jLabel203.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel203.setText("III. RIWAYAT PSIKOLOGIS – SOSIAL – EKONOMI – BUDAYA – SPIRITUAL");
        jLabel203.setName("jLabel203"); // NOI18N
        FormInput.add(jLabel203);
        jLabel203.setBounds(10, 1490, 420, 23);

        jSeparator9.setBackground(new java.awt.Color(239, 244, 234));
        jSeparator9.setForeground(new java.awt.Color(239, 244, 234));
        jSeparator9.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(239, 244, 234)));
        jSeparator9.setName("jSeparator9"); // NOI18N
        FormInput.add(jSeparator9);
        jSeparator9.setBounds(390, 3180, 1, 140);

        PanelWall.setBackground(new java.awt.Color(29, 29, 29));
        PanelWall.setBackgroundImage(new javax.swing.ImageIcon(getClass().getResource("/picture/nyeri.png"))); // NOI18N
        PanelWall.setBackgroundImageType(usu.widget.constan.BackgroundConstan.BACKGROUND_IMAGE_STRECT);
        PanelWall.setPreferredSize(new java.awt.Dimension(200, 200));
        PanelWall.setRound(false);
        PanelWall.setWarna(new java.awt.Color(110, 110, 110));
        PanelWall.setLayout(null);
        FormInput.add(PanelWall);
        PanelWall.setBounds(60, 3180, 320, 130);

        Nyeri.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak Ada Nyeri", "Nyeri Akut", "Nyeri Kronis" }));
        Nyeri.setName("Nyeri"); // NOI18N
        Nyeri.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                NyeriKeyPressed(evt);
            }
        });
        FormInput.add(Nyeri);
        Nyeri.setBounds(400, 3180, 130, 23);

        jLabel204.setText("Penyebab :");
        jLabel204.setName("jLabel204"); // NOI18N
        FormInput.add(jLabel204);
        jLabel204.setBounds(530, 3180, 60, 23);

        Provokes.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Proses Penyakit", "Benturan", "Lain-lain" }));
        Provokes.setName("Provokes"); // NOI18N
        Provokes.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ProvokesKeyPressed(evt);
            }
        });
        FormInput.add(Provokes);
        Provokes.setBounds(600, 3180, 130, 23);

        KetProvokes.setFocusTraversalPolicyProvider(true);
        KetProvokes.setName("KetProvokes"); // NOI18N
        KetProvokes.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KetProvokesKeyPressed(evt);
            }
        });
        FormInput.add(KetProvokes);
        KetProvokes.setBounds(730, 3180, 146, 23);

        jLabel205.setText("Kualitas :");
        jLabel205.setName("jLabel205"); // NOI18N
        FormInput.add(jLabel205);
        jLabel205.setBounds(390, 3210, 55, 23);

        Quality.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Seperti Tertusuk", "Berdenyut", "Teriris", "Tertindih", "Tertiban", "Lain-lain" }));
        Quality.setName("Quality"); // NOI18N
        Quality.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                QualityKeyPressed(evt);
            }
        });
        FormInput.add(Quality);
        Quality.setBounds(450, 3210, 140, 23);

        KetQuality.setFocusTraversalPolicyProvider(true);
        KetQuality.setName("KetQuality"); // NOI18N
        KetQuality.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KetQualityKeyPressed(evt);
            }
        });
        FormInput.add(KetQuality);
        KetQuality.setBounds(600, 3210, 280, 23);

        jLabel206.setText("Wilayah :");
        jLabel206.setName("jLabel206"); // NOI18N
        FormInput.add(jLabel206);
        jLabel206.setBounds(390, 3240, 55, 23);

        jLabel207.setText("Lokasi :");
        jLabel207.setName("jLabel207"); // NOI18N
        FormInput.add(jLabel207);
        jLabel207.setBounds(420, 3260, 60, 23);

        Lokasi.setFocusTraversalPolicyProvider(true);
        Lokasi.setName("Lokasi"); // NOI18N
        Lokasi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                LokasiKeyPressed(evt);
            }
        });
        FormInput.add(Lokasi);
        Lokasi.setBounds(180, 310, 280, 23);

        jLabel208.setText("Menyebar :");
        jLabel208.setName("jLabel208"); // NOI18N
        FormInput.add(jLabel208);
        jLabel208.setBounds(710, 3260, 79, 23);

        Menyebar.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        Menyebar.setName("Menyebar"); // NOI18N
        Menyebar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                MenyebarKeyPressed(evt);
            }
        });
        FormInput.add(Menyebar);
        Menyebar.setBounds(800, 3260, 80, 23);

        jLabel209.setText("Severity :");
        jLabel209.setName("jLabel209"); // NOI18N
        FormInput.add(jLabel209);
        jLabel209.setBounds(390, 3290, 55, 23);

        SkalaNyeri.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" }));
        SkalaNyeri.setName("SkalaNyeri"); // NOI18N
        SkalaNyeri.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SkalaNyeriKeyPressed(evt);
            }
        });
        FormInput.add(SkalaNyeri);
        SkalaNyeri.setBounds(220, 1360, 70, 23);

        jLabel211.setText("Waktu / Durasi :");
        jLabel211.setName("jLabel211"); // NOI18N
        FormInput.add(jLabel211);
        jLabel211.setBounds(650, 3290, 90, 23);

        Durasi.setFocusTraversalPolicyProvider(true);
        Durasi.setName("Durasi"); // NOI18N
        Durasi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DurasiKeyPressed(evt);
            }
        });
        FormInput.add(Durasi);
        Durasi.setBounds(740, 3290, 90, 23);

        jLabel212.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel212.setText("Menit");
        jLabel212.setName("jLabel212"); // NOI18N
        FormInput.add(jLabel212);
        jLabel212.setBounds(840, 3290, 35, 23);

        jLabel213.setText("Nyeri hilang bila :");
        jLabel213.setName("jLabel213"); // NOI18N
        FormInput.add(jLabel213);
        jLabel213.setBounds(20, 3320, 130, 23);

        NyeriHilang.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Istirahat", "Medengar Musik", "Minum Obat" }));
        NyeriHilang.setName("NyeriHilang"); // NOI18N
        NyeriHilang.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                NyeriHilangKeyPressed(evt);
            }
        });
        FormInput.add(NyeriHilang);
        NyeriHilang.setBounds(160, 3320, 130, 23);

        KetNyeri.setFocusTraversalPolicyProvider(true);
        KetNyeri.setName("KetNyeri"); // NOI18N
        KetNyeri.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KetNyeriKeyPressed(evt);
            }
        });
        FormInput.add(KetNyeri);
        KetNyeri.setBounds(290, 3320, 150, 23);

        jLabel214.setText("Diberitahukan pada dokter ?");
        jLabel214.setName("jLabel214"); // NOI18N
        FormInput.add(jLabel214);
        jLabel214.setBounds(500, 3320, 150, 23);

        PadaDokter.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        PadaDokter.setName("PadaDokter"); // NOI18N
        PadaDokter.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PadaDokterKeyPressed(evt);
            }
        });
        FormInput.add(PadaDokter);
        PadaDokter.setBounds(660, 3320, 80, 23);

        jLabel215.setText("Jam  :");
        jLabel215.setName("jLabel215"); // NOI18N
        FormInput.add(jLabel215);
        jLabel215.setBounds(740, 3320, 50, 23);

        KetPadaDokter.setFocusTraversalPolicyProvider(true);
        KetPadaDokter.setName("KetPadaDokter"); // NOI18N
        KetPadaDokter.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KetPadaDokterKeyPressed(evt);
            }
        });
        FormInput.add(KetPadaDokter);
        KetPadaDokter.setBounds(800, 3320, 80, 23);

        jLabel57.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel57.setText("Skala Morse :");
        jLabel57.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel57.setName("jLabel57"); // NOI18N
        FormInput.add(jLabel57);
        jLabel57.setBounds(40, 1670, 110, 23);

        jLabel217.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel217.setText("1. Riwayat Jatuh");
        jLabel217.setName("jLabel217"); // NOI18N
        FormInput.add(jLabel217);
        jLabel217.setBounds(60, 1690, 300, 23);

        SkalaResiko1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        SkalaResiko1.setName("SkalaResiko1"); // NOI18N
        SkalaResiko1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                SkalaResiko1ItemStateChanged(evt);
            }
        });
        SkalaResiko1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SkalaResiko1KeyPressed(evt);
            }
        });
        FormInput.add(SkalaResiko1);
        SkalaResiko1.setBounds(420, 1690, 280, 23);

        jLabel218.setText("Nilai :");
        jLabel218.setName("jLabel218"); // NOI18N
        FormInput.add(jLabel218);
        jLabel218.setBounds(710, 1690, 75, 23);

        NilaiResiko1.setEditable(false);
        NilaiResiko1.setFocusTraversalPolicyProvider(true);
        NilaiResiko1.setName("NilaiResiko1"); // NOI18N
        FormInput.add(NilaiResiko1);
        NilaiResiko1.setBounds(790, 1690, 60, 23);

        jLabel219.setText("Skala :");
        jLabel219.setName("jLabel219"); // NOI18N
        FormInput.add(jLabel219);
        jLabel219.setBounds(330, 1690, 80, 23);

        jLabel220.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel220.setText("2. Diagnosis Sekunder (≥ 2 Diagnosis Medis)");
        jLabel220.setName("jLabel220"); // NOI18N
        FormInput.add(jLabel220);
        jLabel220.setBounds(60, 1720, 300, 23);

        jLabel221.setText("Skala :");
        jLabel221.setName("jLabel221"); // NOI18N
        FormInput.add(jLabel221);
        jLabel221.setBounds(330, 1720, 80, 23);

        SkalaResiko2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        SkalaResiko2.setName("SkalaResiko2"); // NOI18N
        SkalaResiko2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                SkalaResiko2ItemStateChanged(evt);
            }
        });
        SkalaResiko2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SkalaResiko2KeyPressed(evt);
            }
        });
        FormInput.add(SkalaResiko2);
        SkalaResiko2.setBounds(420, 1720, 280, 23);

        jLabel222.setText("Nilai :");
        jLabel222.setName("jLabel222"); // NOI18N
        FormInput.add(jLabel222);
        jLabel222.setBounds(710, 1720, 75, 23);

        NilaiResiko2.setEditable(false);
        NilaiResiko2.setFocusTraversalPolicyProvider(true);
        NilaiResiko2.setName("NilaiResiko2"); // NOI18N
        FormInput.add(NilaiResiko2);
        NilaiResiko2.setBounds(790, 1720, 60, 23);

        jLabel223.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel223.setText("3. Alat Bantu");
        jLabel223.setName("jLabel223"); // NOI18N
        FormInput.add(jLabel223);
        jLabel223.setBounds(60, 1750, 300, 23);

        jLabel224.setText("Skala :");
        jLabel224.setName("jLabel224"); // NOI18N
        FormInput.add(jLabel224);
        jLabel224.setBounds(330, 1750, 80, 23);

        SkalaResiko3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Berpegangan pada benda sekitar", "Alat Penopang/Walker/Tongkat", "Bedrest/Dibantu perawat" }));
        SkalaResiko3.setName("SkalaResiko3"); // NOI18N
        SkalaResiko3.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                SkalaResiko3ItemStateChanged(evt);
            }
        });
        SkalaResiko3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SkalaResiko3KeyPressed(evt);
            }
        });
        FormInput.add(SkalaResiko3);
        SkalaResiko3.setBounds(420, 1750, 280, 23);

        jLabel225.setText("Nilai :");
        jLabel225.setName("jLabel225"); // NOI18N
        FormInput.add(jLabel225);
        jLabel225.setBounds(710, 1750, 75, 23);

        NilaiResiko3.setEditable(false);
        NilaiResiko3.setFocusTraversalPolicyProvider(true);
        NilaiResiko3.setName("NilaiResiko3"); // NOI18N
        FormInput.add(NilaiResiko3);
        NilaiResiko3.setBounds(790, 1750, 60, 23);

        jLabel226.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel226.setText("4. Terpasang Infuse");
        jLabel226.setName("jLabel226"); // NOI18N
        FormInput.add(jLabel226);
        jLabel226.setBounds(60, 1780, 300, 23);

        jLabel227.setText("Skala :");
        jLabel227.setName("jLabel227"); // NOI18N
        FormInput.add(jLabel227);
        jLabel227.setBounds(330, 1780, 80, 23);

        SkalaResiko4.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        SkalaResiko4.setName("SkalaResiko4"); // NOI18N
        SkalaResiko4.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                SkalaResiko4ItemStateChanged(evt);
            }
        });
        SkalaResiko4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SkalaResiko4KeyPressed(evt);
            }
        });
        FormInput.add(SkalaResiko4);
        SkalaResiko4.setBounds(420, 1780, 280, 23);

        jLabel228.setText("Nilai :");
        jLabel228.setName("jLabel228"); // NOI18N
        FormInput.add(jLabel228);
        jLabel228.setBounds(710, 1780, 75, 23);

        NilaiResiko4.setEditable(false);
        NilaiResiko4.setFocusTraversalPolicyProvider(true);
        NilaiResiko4.setName("NilaiResiko4"); // NOI18N
        FormInput.add(NilaiResiko4);
        NilaiResiko4.setBounds(790, 1780, 60, 23);

        jLabel229.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel229.setText("5. Gaya Berjalan");
        jLabel229.setName("jLabel229"); // NOI18N
        FormInput.add(jLabel229);
        jLabel229.setBounds(60, 1810, 300, 23);

        jLabel230.setText("Skala :");
        jLabel230.setName("jLabel230"); // NOI18N
        FormInput.add(jLabel230);
        jLabel230.setBounds(330, 1810, 80, 23);

        SkalaResiko5.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Normal/Tirah Baring/Imobilisasi", "Lemah", "Terganggu" }));
        SkalaResiko5.setName("SkalaResiko5"); // NOI18N
        SkalaResiko5.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                SkalaResiko5ItemStateChanged(evt);
            }
        });
        SkalaResiko5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SkalaResiko5KeyPressed(evt);
            }
        });
        FormInput.add(SkalaResiko5);
        SkalaResiko5.setBounds(420, 1810, 280, 23);

        jLabel231.setText("Nilai :");
        jLabel231.setName("jLabel231"); // NOI18N
        FormInput.add(jLabel231);
        jLabel231.setBounds(710, 1810, 75, 23);

        NilaiResiko5.setEditable(false);
        NilaiResiko5.setFocusTraversalPolicyProvider(true);
        NilaiResiko5.setName("NilaiResiko5"); // NOI18N
        FormInput.add(NilaiResiko5);
        NilaiResiko5.setBounds(790, 1810, 60, 23);

        jLabel232.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel232.setText("6. Status Mental");
        jLabel232.setName("jLabel232"); // NOI18N
        FormInput.add(jLabel232);
        jLabel232.setBounds(60, 1840, 300, 23);

        jLabel233.setText("Skala :");
        jLabel233.setName("jLabel233"); // NOI18N
        FormInput.add(jLabel233);
        jLabel233.setBounds(330, 1840, 80, 23);

        SkalaResiko6.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Sadar Akan Kemampuan Diri Sendiri", "Sering Lupa Akan Keterbatasan Yang Dimiliki" }));
        SkalaResiko6.setName("SkalaResiko6"); // NOI18N
        SkalaResiko6.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                SkalaResiko6ItemStateChanged(evt);
            }
        });
        SkalaResiko6.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SkalaResiko6KeyPressed(evt);
            }
        });
        FormInput.add(SkalaResiko6);
        SkalaResiko6.setBounds(420, 1840, 280, 23);

        jLabel234.setText("Nilai :");
        jLabel234.setName("jLabel234"); // NOI18N
        FormInput.add(jLabel234);
        jLabel234.setBounds(710, 1840, 75, 23);

        NilaiResiko6.setEditable(false);
        NilaiResiko6.setFocusTraversalPolicyProvider(true);
        NilaiResiko6.setName("NilaiResiko6"); // NOI18N
        FormInput.add(NilaiResiko6);
        NilaiResiko6.setBounds(790, 1840, 60, 23);

        jLabel235.setText("Total :");
        jLabel235.setName("jLabel235"); // NOI18N
        FormInput.add(jLabel235);
        jLabel235.setBounds(710, 1870, 75, 23);

        NilaiResikoTotal.setEditable(false);
        NilaiResikoTotal.setFocusTraversalPolicyProvider(true);
        NilaiResikoTotal.setName("NilaiResikoTotal"); // NOI18N
        FormInput.add(NilaiResikoTotal);
        NilaiResikoTotal.setBounds(790, 1870, 60, 23);

        TingkatResiko.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        TingkatResiko.setText("Tingkat Resiko : Risiko Rendah (0-24), Tindakan : Intervensi pencegahan risiko jatuh standar");
        TingkatResiko.setToolTipText("");
        TingkatResiko.setName("TingkatResiko"); // NOI18N
        FormInput.add(TingkatResiko);
        TingkatResiko.setBounds(60, 1870, 640, 23);

        jLabel58.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel58.setText("Edmonton Symptom Assessment Scale = ESAS");
        jLabel58.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel58.setName("jLabel58"); // NOI18N
        FormInput.add(jLabel58);
        jLabel58.setBounds(40, 1920, 270, 23);

        jLabel236.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel236.setText("1. Tidak Nyeri");
        jLabel236.setName("jLabel236"); // NOI18N
        FormInput.add(jLabel236);
        jLabel236.setBounds(60, 1940, 450, 23);

        jLabel237.setText("Skala :");
        jLabel237.setName("jLabel237"); // NOI18N
        FormInput.add(jLabel237);
        jLabel237.setBounds(520, 1940, 80, 23);

        SkalaSydney1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" }));
        SkalaSydney1.setName("SkalaSydney1"); // NOI18N
        SkalaSydney1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                SkalaSydney1ItemStateChanged(evt);
            }
        });
        SkalaSydney1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SkalaSydney1KeyPressed(evt);
            }
        });
        FormInput.add(SkalaSydney1);
        SkalaSydney1.setBounds(610, 1940, 90, 23);

        jLabel238.setText("Nilai :");
        jLabel238.setName("jLabel238"); // NOI18N
        FormInput.add(jLabel238);
        jLabel238.setBounds(710, 1940, 75, 23);

        NilaiSydney1.setEditable(false);
        NilaiSydney1.setFocusTraversalPolicyProvider(true);
        NilaiSydney1.setName("NilaiSydney1"); // NOI18N
        FormInput.add(NilaiSydney1);
        NilaiSydney1.setBounds(790, 1940, 60, 23);

        jLabel239.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel239.setText("2. Tidak Lelah");
        jLabel239.setName("jLabel239"); // NOI18N
        FormInput.add(jLabel239);
        jLabel239.setBounds(60, 1970, 450, 23);

        jLabel240.setText("Skala :");
        jLabel240.setName("jLabel240"); // NOI18N
        FormInput.add(jLabel240);
        jLabel240.setBounds(520, 1970, 80, 23);

        SkalaSydney2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" }));
        SkalaSydney2.setName("SkalaSydney2"); // NOI18N
        SkalaSydney2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                SkalaSydney2ItemStateChanged(evt);
            }
        });
        SkalaSydney2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SkalaSydney2KeyPressed(evt);
            }
        });
        FormInput.add(SkalaSydney2);
        SkalaSydney2.setBounds(610, 1970, 90, 23);

        jLabel241.setText("Nilai :");
        jLabel241.setName("jLabel241"); // NOI18N
        FormInput.add(jLabel241);
        jLabel241.setBounds(710, 1970, 75, 23);

        NilaiSydney2.setEditable(false);
        NilaiSydney2.setFocusTraversalPolicyProvider(true);
        NilaiSydney2.setName("NilaiSydney2"); // NOI18N
        FormInput.add(NilaiSydney2);
        NilaiSydney2.setBounds(790, 1970, 60, 23);

        jLabel242.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel242.setText("3. Tidak Mual");
        jLabel242.setName("jLabel242"); // NOI18N
        FormInput.add(jLabel242);
        jLabel242.setBounds(60, 2000, 450, 23);

        jLabel243.setText("Skala :");
        jLabel243.setName("jLabel243"); // NOI18N
        FormInput.add(jLabel243);
        jLabel243.setBounds(520, 2000, 80, 23);

        SkalaSydney3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" }));
        SkalaSydney3.setName("SkalaSydney3"); // NOI18N
        SkalaSydney3.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                SkalaSydney3ItemStateChanged(evt);
            }
        });
        SkalaSydney3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SkalaSydney3KeyPressed(evt);
            }
        });
        FormInput.add(SkalaSydney3);
        SkalaSydney3.setBounds(610, 2000, 90, 23);

        jLabel244.setText("Nilai :");
        jLabel244.setName("jLabel244"); // NOI18N
        FormInput.add(jLabel244);
        jLabel244.setBounds(710, 2000, 75, 23);

        NilaiSydney3.setEditable(false);
        NilaiSydney3.setFocusTraversalPolicyProvider(true);
        NilaiSydney3.setName("NilaiSydney3"); // NOI18N
        FormInput.add(NilaiSydney3);
        NilaiSydney3.setBounds(790, 2000, 60, 23);

        jLabel245.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel245.setText("4. Tidak Depresi");
        jLabel245.setName("jLabel245"); // NOI18N
        FormInput.add(jLabel245);
        jLabel245.setBounds(60, 2030, 450, 23);

        jLabel246.setText("Skala :");
        jLabel246.setName("jLabel246"); // NOI18N
        FormInput.add(jLabel246);
        jLabel246.setBounds(520, 2030, 80, 23);

        SkalaSydney4.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" }));
        SkalaSydney4.setName("SkalaSydney4"); // NOI18N
        SkalaSydney4.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                SkalaSydney4ItemStateChanged(evt);
            }
        });
        SkalaSydney4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SkalaSydney4KeyPressed(evt);
            }
        });
        FormInput.add(SkalaSydney4);
        SkalaSydney4.setBounds(610, 2030, 90, 23);

        jLabel247.setText("Nilai :");
        jLabel247.setName("jLabel247"); // NOI18N
        FormInput.add(jLabel247);
        jLabel247.setBounds(710, 2030, 75, 23);

        NilaiSydney4.setEditable(false);
        NilaiSydney4.setFocusTraversalPolicyProvider(true);
        NilaiSydney4.setName("NilaiSydney4"); // NOI18N
        FormInput.add(NilaiSydney4);
        NilaiSydney4.setBounds(790, 2030, 60, 23);

        jLabel248.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel248.setText("5. Tidak Cemas");
        jLabel248.setName("jLabel248"); // NOI18N
        FormInput.add(jLabel248);
        jLabel248.setBounds(60, 2060, 450, 23);

        jLabel249.setText("Skala :");
        jLabel249.setName("jLabel249"); // NOI18N
        FormInput.add(jLabel249);
        jLabel249.setBounds(520, 2060, 80, 23);

        SkalaSydney5.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" }));
        SkalaSydney5.setName("SkalaSydney5"); // NOI18N
        SkalaSydney5.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                SkalaSydney5ItemStateChanged(evt);
            }
        });
        SkalaSydney5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SkalaSydney5KeyPressed(evt);
            }
        });
        FormInput.add(SkalaSydney5);
        SkalaSydney5.setBounds(610, 2060, 90, 23);

        jLabel250.setText("Nilai :");
        jLabel250.setName("jLabel250"); // NOI18N
        FormInput.add(jLabel250);
        jLabel250.setBounds(710, 2060, 75, 23);

        NilaiSydney5.setEditable(false);
        NilaiSydney5.setFocusTraversalPolicyProvider(true);
        NilaiSydney5.setName("NilaiSydney5"); // NOI18N
        FormInput.add(NilaiSydney5);
        NilaiSydney5.setBounds(790, 2060, 60, 23);

        jLabel251.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel251.setText("6. Tidak Mengantuk");
        jLabel251.setName("jLabel251"); // NOI18N
        FormInput.add(jLabel251);
        jLabel251.setBounds(60, 2090, 450, 23);

        jLabel252.setText("Skala :");
        jLabel252.setName("jLabel252"); // NOI18N
        FormInput.add(jLabel252);
        jLabel252.setBounds(520, 2090, 80, 23);

        SkalaSydney6.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" }));
        SkalaSydney6.setName("SkalaSydney6"); // NOI18N
        SkalaSydney6.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                SkalaSydney6ItemStateChanged(evt);
            }
        });
        SkalaSydney6.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SkalaSydney6KeyPressed(evt);
            }
        });
        FormInput.add(SkalaSydney6);
        SkalaSydney6.setBounds(610, 2090, 90, 23);

        jLabel253.setText("Nilai :");
        jLabel253.setName("jLabel253"); // NOI18N
        FormInput.add(jLabel253);
        jLabel253.setBounds(710, 2090, 75, 23);

        NilaiSydney6.setEditable(false);
        NilaiSydney6.setFocusTraversalPolicyProvider(true);
        NilaiSydney6.setName("NilaiSydney6"); // NOI18N
        FormInput.add(NilaiSydney6);
        NilaiSydney6.setBounds(790, 2090, 60, 23);

        jLabel254.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel254.setText("7. Nafsu Makan Baik");
        jLabel254.setName("jLabel254"); // NOI18N
        FormInput.add(jLabel254);
        jLabel254.setBounds(60, 2120, 490, 23);

        jLabel255.setText("Skala :");
        jLabel255.setName("jLabel255"); // NOI18N
        FormInput.add(jLabel255);
        jLabel255.setBounds(520, 2120, 80, 23);

        SkalaSydney7.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" }));
        SkalaSydney7.setName("SkalaSydney7"); // NOI18N
        SkalaSydney7.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                SkalaSydney7ItemStateChanged(evt);
            }
        });
        SkalaSydney7.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SkalaSydney7KeyPressed(evt);
            }
        });
        FormInput.add(SkalaSydney7);
        SkalaSydney7.setBounds(610, 2120, 90, 23);

        jLabel256.setText("Nilai :");
        jLabel256.setName("jLabel256"); // NOI18N
        FormInput.add(jLabel256);
        jLabel256.setBounds(710, 2120, 75, 23);

        NilaiSydney7.setEditable(false);
        NilaiSydney7.setFocusTraversalPolicyProvider(true);
        NilaiSydney7.setName("NilaiSydney7"); // NOI18N
        FormInput.add(NilaiSydney7);
        NilaiSydney7.setBounds(790, 2120, 60, 23);

        jLabel258.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel258.setText("8. Merasa Sehat dan Bugar");
        jLabel258.setName("jLabel258"); // NOI18N
        FormInput.add(jLabel258);
        jLabel258.setBounds(60, 2150, 450, 23);

        jLabel259.setText("Skala :");
        jLabel259.setName("jLabel259"); // NOI18N
        FormInput.add(jLabel259);
        jLabel259.setBounds(520, 2150, 80, 23);

        SkalaSydney8.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" }));
        SkalaSydney8.setName("SkalaSydney8"); // NOI18N
        SkalaSydney8.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                SkalaSydney8ItemStateChanged(evt);
            }
        });
        SkalaSydney8.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SkalaSydney8KeyPressed(evt);
            }
        });
        FormInput.add(SkalaSydney8);
        SkalaSydney8.setBounds(610, 2150, 90, 23);

        jLabel260.setText("Nilai :");
        jLabel260.setName("jLabel260"); // NOI18N
        FormInput.add(jLabel260);
        jLabel260.setBounds(710, 2150, 75, 23);

        NilaiSydney8.setEditable(false);
        NilaiSydney8.setFocusTraversalPolicyProvider(true);
        NilaiSydney8.setName("NilaiSydney8"); // NOI18N
        FormInput.add(NilaiSydney8);
        NilaiSydney8.setBounds(790, 2150, 60, 23);

        jLabel261.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel261.setText("9. Tidak Sesak Nafas");
        jLabel261.setName("jLabel261"); // NOI18N
        FormInput.add(jLabel261);
        jLabel261.setBounds(60, 2180, 450, 23);

        jLabel262.setText("Skala :");
        jLabel262.setName("jLabel262"); // NOI18N
        FormInput.add(jLabel262);
        jLabel262.setBounds(520, 2180, 80, 23);

        SkalaSydney9.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" }));
        SkalaSydney9.setName("SkalaSydney9"); // NOI18N
        SkalaSydney9.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                SkalaSydney9ItemStateChanged(evt);
            }
        });
        SkalaSydney9.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SkalaSydney9KeyPressed(evt);
            }
        });
        FormInput.add(SkalaSydney9);
        SkalaSydney9.setBounds(610, 2180, 90, 23);

        jLabel263.setText("Nilai :");
        jLabel263.setName("jLabel263"); // NOI18N
        FormInput.add(jLabel263);
        jLabel263.setBounds(710, 2180, 75, 23);

        NilaiSydney9.setEditable(false);
        NilaiSydney9.setFocusTraversalPolicyProvider(true);
        NilaiSydney9.setName("NilaiSydney9"); // NOI18N
        FormInput.add(NilaiSydney9);
        NilaiSydney9.setBounds(790, 2180, 60, 23);

        jLabel264.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel264.setText("10. Tidak Ada Masalah");
        jLabel264.setName("jLabel264"); // NOI18N
        FormInput.add(jLabel264);
        jLabel264.setBounds(60, 2210, 450, 23);

        jLabel265.setText("Skala :");
        jLabel265.setName("jLabel265"); // NOI18N
        FormInput.add(jLabel265);
        jLabel265.setBounds(520, 2210, 80, 23);

        SkalaSydney10.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" }));
        SkalaSydney10.setName("SkalaSydney10"); // NOI18N
        SkalaSydney10.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                SkalaSydney10ItemStateChanged(evt);
            }
        });
        SkalaSydney10.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SkalaSydney10KeyPressed(evt);
            }
        });
        FormInput.add(SkalaSydney10);
        SkalaSydney10.setBounds(610, 2210, 90, 23);

        jLabel266.setText("Nilai :");
        jLabel266.setName("jLabel266"); // NOI18N
        FormInput.add(jLabel266);
        jLabel266.setBounds(710, 2210, 75, 23);

        NilaiSydney10.setEditable(false);
        NilaiSydney10.setFocusTraversalPolicyProvider(true);
        NilaiSydney10.setName("NilaiSydney10"); // NOI18N
        FormInput.add(NilaiSydney10);
        NilaiSydney10.setBounds(790, 2210, 60, 23);

        jLabel267.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel267.setText("11. Usia 70 Tahun Ke Atas");
        jLabel267.setName("jLabel267"); // NOI18N
        FormInput.add(jLabel267);
        jLabel267.setBounds(50, 2870, 450, 23);

        jLabel268.setText("Skala :");
        jLabel268.setName("jLabel268"); // NOI18N
        FormInput.add(jLabel268);
        jLabel268.setBounds(510, 2870, 80, 23);

        SkalaSydney11.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        SkalaSydney11.setName("SkalaSydney11"); // NOI18N
        SkalaSydney11.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                SkalaSydney11ItemStateChanged(evt);
            }
        });
        SkalaSydney11.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SkalaSydney11KeyPressed(evt);
            }
        });
        FormInput.add(SkalaSydney11);
        SkalaSydney11.setBounds(600, 2870, 90, 23);

        jLabel269.setText("Nilai :");
        jLabel269.setName("jLabel269"); // NOI18N
        FormInput.add(jLabel269);
        jLabel269.setBounds(700, 2870, 75, 23);

        NilaiSydney11.setEditable(false);
        NilaiSydney11.setFocusTraversalPolicyProvider(true);
        NilaiSydney11.setName("NilaiSydney11"); // NOI18N
        FormInput.add(NilaiSydney11);
        NilaiSydney11.setBounds(780, 2870, 60, 23);

        NilaiSydneyTotal.setEditable(false);
        NilaiSydneyTotal.setFocusTraversalPolicyProvider(true);
        NilaiSydneyTotal.setName("NilaiSydneyTotal"); // NOI18N
        FormInput.add(NilaiSydneyTotal);
        NilaiSydneyTotal.setBounds(790, 2240, 60, 23);

        jLabel270.setText("Total :");
        jLabel270.setName("jLabel270"); // NOI18N
        FormInput.add(jLabel270);
        jLabel270.setBounds(710, 2240, 75, 23);

        TingkatSydney.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        TingkatSydney.setText("Tingkat Resiko : Risiko Rendah (1-3), Tindakan : Intervensi pencegahan risiko jatuh standar");
        TingkatSydney.setName("TingkatSydney"); // NOI18N
        FormInput.add(TingkatSydney);
        TingkatSydney.setBounds(60, 2240, 650, 23);

        jSeparator11.setBackground(new java.awt.Color(239, 244, 234));
        jSeparator11.setForeground(new java.awt.Color(239, 244, 234));
        jSeparator11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(239, 244, 234)));
        jSeparator11.setName("jSeparator11"); // NOI18N
        FormInput.add(jSeparator11);
        jSeparator11.setBounds(0, 2450, 880, 1);

        jLabel271.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel271.setText("VIII. SKRINING GIZI");
        jLabel271.setName("jLabel271"); // NOI18N
        FormInput.add(jLabel271);
        jLabel271.setBounds(40, 2890, 380, 23);

        jLabel272.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel272.setText("1. Apakah ada penurunan BB yang tidak diinginkan selama 6 bulan terakhir ?");
        jLabel272.setName("jLabel272"); // NOI18N
        FormInput.add(jLabel272);
        jLabel272.setBounds(70, 2910, 380, 23);

        SkalaGizi1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak ada penurunan berat badan", "Tidak yakin/ tidak tahu/ terasa baju lebih longgar", "Ya 1-5 kg", "Ya 6-10 kg", "Ya 11-15 kg", "Ya > 15 kg" }));
        SkalaGizi1.setName("SkalaGizi1"); // NOI18N
        SkalaGizi1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                SkalaGizi1ItemStateChanged(evt);
            }
        });
        SkalaGizi1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SkalaGizi1KeyPressed(evt);
            }
        });
        FormInput.add(SkalaGizi1);
        SkalaGizi1.setBounds(450, 2910, 320, 23);

        jLabel273.setText("Skor :");
        jLabel273.setName("jLabel273"); // NOI18N
        FormInput.add(jLabel273);
        jLabel273.setBounds(780, 2910, 40, 23);

        NilaiGizi1.setEditable(false);
        NilaiGizi1.setFocusTraversalPolicyProvider(true);
        NilaiGizi1.setName("NilaiGizi1"); // NOI18N
        FormInput.add(NilaiGizi1);
        NilaiGizi1.setBounds(820, 2910, 60, 23);

        jLabel274.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel274.setText("2. Apakah asupan makan berkurang karena tidak nafsu makan ?");
        jLabel274.setName("jLabel274"); // NOI18N
        FormInput.add(jLabel274);
        jLabel274.setBounds(70, 2940, 380, 23);

        SkalaGizi2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        SkalaGizi2.setName("SkalaGizi2"); // NOI18N
        SkalaGizi2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                SkalaGizi2ItemStateChanged(evt);
            }
        });
        SkalaGizi2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SkalaGizi2KeyPressed(evt);
            }
        });
        FormInput.add(SkalaGizi2);
        SkalaGizi2.setBounds(450, 2940, 320, 23);

        jLabel275.setText("Skor :");
        jLabel275.setName("jLabel275"); // NOI18N
        FormInput.add(jLabel275);
        jLabel275.setBounds(780, 2940, 40, 23);

        NilaiGizi2.setEditable(false);
        NilaiGizi2.setFocusTraversalPolicyProvider(true);
        NilaiGizi2.setName("NilaiGizi2"); // NOI18N
        FormInput.add(NilaiGizi2);
        NilaiGizi2.setBounds(820, 2940, 60, 23);

        jLabel276.setText("Total Skor :");
        jLabel276.setName("jLabel276"); // NOI18N
        FormInput.add(jLabel276);
        jLabel276.setBounds(710, 2970, 110, 23);

        NilaiGiziTotal.setEditable(false);
        NilaiGiziTotal.setFocusTraversalPolicyProvider(true);
        NilaiGiziTotal.setName("NilaiGiziTotal"); // NOI18N
        FormInput.add(NilaiGiziTotal);
        NilaiGiziTotal.setBounds(820, 2970, 60, 23);

        jLabel277.setText("Pasien dengan diagnosis khusus : ");
        jLabel277.setName("jLabel277"); // NOI18N
        FormInput.add(jLabel277);
        jLabel277.setBounds(30, 3000, 206, 23);

        DiagnosaKhususGizi.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        DiagnosaKhususGizi.setName("DiagnosaKhususGizi"); // NOI18N
        DiagnosaKhususGizi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DiagnosaKhususGiziKeyPressed(evt);
            }
        });
        FormInput.add(DiagnosaKhususGizi);
        DiagnosaKhususGizi.setBounds(240, 3000, 80, 23);

        KeteranganDiagnosaKhususGizi.setFocusTraversalPolicyProvider(true);
        KeteranganDiagnosaKhususGizi.setName("KeteranganDiagnosaKhususGizi"); // NOI18N
        KeteranganDiagnosaKhususGizi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KeteranganDiagnosaKhususGiziKeyPressed(evt);
            }
        });
        FormInput.add(KeteranganDiagnosaKhususGizi);
        KeteranganDiagnosaKhususGizi.setBounds(330, 3000, 150, 23);

        jLabel278.setText("Sudah dibaca dan diketahui oleh Dietisen :");
        jLabel278.setName("jLabel278"); // NOI18N
        FormInput.add(jLabel278);
        jLabel278.setBounds(480, 3000, 220, 23);

        DiketahuiDietisen.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        DiketahuiDietisen.setName("DiketahuiDietisen"); // NOI18N
        DiketahuiDietisen.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DiketahuiDietisenKeyPressed(evt);
            }
        });
        FormInput.add(DiketahuiDietisen);
        DiketahuiDietisen.setBounds(700, 3000, 80, 23);

        jLabel279.setText("Jam  :");
        jLabel279.setName("jLabel279"); // NOI18N
        FormInput.add(jLabel279);
        jLabel279.setBounds(780, 3000, 40, 23);

        KeteranganDiketahuiDietisen.setFocusTraversalPolicyProvider(true);
        KeteranganDiketahuiDietisen.setName("KeteranganDiketahuiDietisen"); // NOI18N
        KeteranganDiketahuiDietisen.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KeteranganDiketahuiDietisenKeyPressed(evt);
            }
        });
        FormInput.add(KeteranganDiketahuiDietisen);
        KeteranganDiketahuiDietisen.setBounds(820, 3000, 60, 23);

        jSeparator12.setBackground(new java.awt.Color(239, 244, 234));
        jSeparator12.setForeground(new java.awt.Color(239, 244, 234));
        jSeparator12.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(239, 244, 234)));
        jSeparator12.setName("jSeparator12"); // NOI18N
        FormInput.add(jSeparator12);
        jSeparator12.setBounds(0, 2530, 880, 1);

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
        Scroll6.setBounds(30, 2270, 400, 143);

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

        TabRencanaKeperawatan.addTab("Rencana Keperawatan", panelBiasa1);

        scrollPane5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        scrollPane5.setName("scrollPane5"); // NOI18N

        Rencana.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        Rencana.setColumns(20);
        Rencana.setRows(5);
        Rencana.setName("Rencana"); // NOI18N
        Rencana.setOpaque(true);
        Rencana.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                RencanaKeyPressed(evt);
            }
        });
        scrollPane5.setViewportView(Rencana);

        TabRencanaKeperawatan.addTab("Rencana Keperawatan Lainnya", scrollPane5);

        FormInput.add(TabRencanaKeperawatan);
        TabRencanaKeperawatan.setBounds(460, 2270, 420, 143);

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
        BtnTambahMasalah.setBounds(390, 2420, 28, 23);

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
        BtnAllMasalah.setBounds(350, 2420, 28, 23);

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
        BtnCariMasalah.setBounds(320, 2420, 28, 23);

        TCariMasalah.setToolTipText("Alt+C");
        TCariMasalah.setName("TCariMasalah"); // NOI18N
        TCariMasalah.setPreferredSize(new java.awt.Dimension(140, 23));
        TCariMasalah.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TCariMasalahKeyPressed(evt);
            }
        });
        FormInput.add(TCariMasalah);
        TCariMasalah.setBounds(100, 2420, 215, 23);

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
        BtnTambahRencana.setBounds(830, 2420, 28, 23);

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
        BtnAllRencana.setBounds(800, 2420, 28, 23);

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
        BtnCariRencana.setBounds(770, 2420, 28, 23);

        label13.setText("Key Word :");
        label13.setName("label13"); // NOI18N
        label13.setPreferredSize(new java.awt.Dimension(60, 23));
        FormInput.add(label13);
        label13.setBounds(460, 2420, 60, 23);

        TCariRencana.setToolTipText("Alt+C");
        TCariRencana.setName("TCariRencana"); // NOI18N
        TCariRencana.setPreferredSize(new java.awt.Dimension(215, 23));
        TCariRencana.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TCariRencanaKeyPressed(evt);
            }
        });
        FormInput.add(TCariRencana);
        TCariRencana.setBounds(530, 2420, 235, 23);

        label12.setText("Key Word :");
        label12.setName("label12"); // NOI18N
        label12.setPreferredSize(new java.awt.Dimension(60, 23));
        FormInput.add(label12);
        label12.setBounds(40, 2420, 60, 23);

        jLabel283.setText(":");
        jLabel283.setName("jLabel283"); // NOI18N
        FormInput.add(jLabel283);
        jLabel283.setBounds(580, 3080, 10, 23);

        jLabel284.setText("Psikologis :");
        jLabel284.setName("jLabel284"); // NOI18N
        FormInput.add(jLabel284);
        jLabel284.setBounds(10, 1520, 60, 23);

        jLabel285.setText("Kesediaan Pasien Menerima Informasi :");
        jLabel285.setName("jLabel285"); // NOI18N
        FormInput.add(jLabel285);
        jLabel285.setBounds(560, 1550, 210, 23);

        jLabel286.setText("Tinggal Dengan :");
        jLabel286.setName("jLabel286"); // NOI18N
        FormInput.add(jLabel286);
        jLabel286.setBounds(0, 1550, 130, 23);

        jLabel287.setText("Prog. Pengobatan Bertentangan Dengan Budaya :");
        jLabel287.setName("jLabel287"); // NOI18N
        FormInput.add(jLabel287);
        jLabel287.setBounds(10, 1610, 250, 23);

        jLabel288.setText("Tingkat Pendidikan Pasien :");
        jLabel288.setName("jLabel288"); // NOI18N
        FormInput.add(jLabel288);
        jLabel288.setBounds(10, 1640, 140, 23);

        jLabel257.setText("Fraktur :");
        jLabel257.setName("jLabel257"); // NOI18N
        FormInput.add(jLabel257);
        jLabel257.setBounds(0, 1110, 100, 23);

        jLabel124.setText("Status Fungsional :");
        jLabel124.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel124.setName("jLabel124"); // NOI18N
        FormInput.add(jLabel124);
        jLabel124.setBounds(230, 1330, 120, 23);

        jLabel126.setText("Severity (S) :");
        jLabel126.setName("jLabel126"); // NOI18N
        FormInput.add(jLabel126);
        jLabel126.setBounds(330, 1390, 80, 23);

        jLabel54.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel54.setText("Muskuloskletal :");
        jLabel54.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel54.setName("jLabel54"); // NOI18N
        FormInput.add(jLabel54);
        jLabel54.setBounds(10, 1060, 122, 23);

        jLabel169.setText("Provocative (P) :");
        jLabel169.setName("jLabel169"); // NOI18N
        FormInput.add(jLabel169);
        jLabel169.setBounds(30, 1390, 80, 23);

        jLabel216.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel216.setText("VI. PENILAIAN TINGKAT NYERI");
        jLabel216.setName("jLabel216"); // NOI18N
        FormInput.add(jLabel216);
        jLabel216.setBounds(30, 3160, 380, 23);

        PanelWall1.setBackground(new java.awt.Color(255, 255, 255));
        PanelWall1.setBackgroundImage(new javax.swing.ImageIcon(getClass().getResource("/picture/tangankemo.png"))); // NOI18N
        PanelWall1.setBackgroundImageType(usu.widget.constan.BackgroundConstan.BACKGROUND_IMAGE_STRECT);
        PanelWall1.setOpaque(true);
        PanelWall1.setPreferredSize(new java.awt.Dimension(200, 200));
        PanelWall1.setRound(false);
        PanelWall1.setWarna(new java.awt.Color(110, 110, 110));
        PanelWall1.setLayout(null);
        FormInput.add(PanelWall1);
        PanelWall1.setBounds(530, 270, 110, 190);

        PanelWall2.setBackground(new java.awt.Color(255, 255, 255));
        PanelWall2.setBackgroundImage(new javax.swing.ImageIcon(getClass().getResource("/picture/tangan1kemo.png"))); // NOI18N
        PanelWall2.setBackgroundImageType(usu.widget.constan.BackgroundConstan.BACKGROUND_IMAGE_STRECT);
        PanelWall2.setOpaque(true);
        PanelWall2.setPreferredSize(new java.awt.Dimension(200, 200));
        PanelWall2.setRound(false);
        PanelWall2.setWarna(new java.awt.Color(110, 110, 110));
        PanelWall2.setLayout(null);
        FormInput.add(PanelWall2);
        PanelWall2.setBounds(720, 280, 130, 180);

        jLabel170.setText("Hasil BMP :");
        jLabel170.setName("jLabel170"); // NOI18N
        FormInput.add(jLabel170);
        jLabel170.setBounds(110, 430, 60, 23);

        jLabel175.setText("Depan");
        jLabel175.setName("jLabel175"); // NOI18N
        FormInput.add(jLabel175);
        jLabel175.setBounds(570, 470, 60, 23);

        jLabel46.setText("Lokasi Ekstravasasi :");
        jLabel46.setName("jLabel46"); // NOI18N
        FormInput.add(jLabel46);
        jLabel46.setBounds(50, 310, 120, 23);

        jLabel56.setText("Riwayat Ekstravasasi :");
        jLabel56.setName("jLabel56"); // NOI18N
        FormInput.add(jLabel56);
        jLabel56.setBounds(50, 340, 120, 23);

        scrollInput.setViewportView(FormInput);

        internalFrame2.add(scrollInput, java.awt.BorderLayout.CENTER);

        TabRawat.addTab("Input Penilaian", internalFrame2);

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

        jLabel19.setText("Tgl.Asuhan :");
        jLabel19.setName("jLabel19"); // NOI18N
        jLabel19.setPreferredSize(new java.awt.Dimension(70, 23));
        panelGlass9.add(jLabel19);

        DTPCari1.setForeground(new java.awt.Color(50, 70, 50));
        DTPCari1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "23-10-2023" }));
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
        DTPCari2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "23-10-2023" }));
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
        TCari.setPreferredSize(new java.awt.Dimension(195, 23));
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

        PanelAccor.setBackground(new java.awt.Color(255, 255, 255));
        PanelAccor.setName("PanelAccor"); // NOI18N
        PanelAccor.setPreferredSize(new java.awt.Dimension(470, 43));
        PanelAccor.setLayout(new java.awt.BorderLayout(1, 1));

        ChkAccor.setBackground(new java.awt.Color(255, 250, 248));
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
        DetailRencana.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DetailRencanaKeyPressed(evt);
            }
        });
        scrollPane6.setViewportView(DetailRencana);

        FormMasalahRencana.add(scrollPane6);

        PanelAccor.add(FormMasalahRencana, java.awt.BorderLayout.CENTER);

        internalFrame3.add(PanelAccor, java.awt.BorderLayout.EAST);

        TabRawat.addTab("Data Penilaian", internalFrame3);

        internalFrame1.add(TabRawat, java.awt.BorderLayout.CENTER);

        getContentPane().add(internalFrame1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BtnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSimpanActionPerformed
        if(TNoRM.getText().trim().equals("")){
            Valid.textKosong(TNoRw,"Nama Pasien");
        }else if(KdPetugas.getText().trim().equals("")||NmPetugas.getText().trim().equals("")){
            Valid.textKosong(BtnPetugas,"Pengkaji 1");
        }else if(KdPetugas2.getText().trim().equals("")||NmPetugas2.getText().trim().equals("")){
            Valid.textKosong(BtnPetugas2,"Pegkaji 2");
        }else if(KdDPJP.getText().trim().equals("")||NmDPJP.getText().trim().equals("")){
            Valid.textKosong(BtnDPJP,"DPJP");
        
        }else{
            if(Sequel.menyimpantf("penilaian_awal_keperawatan_ranap_kemoterapi","?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?","No.Rawat",193,new String[]{
                    TNoRw.getText(),Valid.SetTgl(TglAsuhan.getSelectedItem()+"")+" "+TglAsuhan.getSelectedItem().toString().substring(11,19),Anamnesis.getSelectedItem().toString(),KetAnamnesis.getText(),TibadiRuang.getSelectedItem().toString(),MacamKasus.getSelectedItem().toString(), 
                    CaraMasuk.getSelectedItem().toString(),RPS.getText(),RPD.getText(),RPK.getText(),RPO.getText(),RPembedahan.getText(),RDirawatRS.getText(),AlatBantuDipakai.getSelectedItem().toString(),SedangMenyusui.getSelectedItem().toString(),KetSedangMenyusui.getText(),RTranfusi.getText(), 
                    Alergi.getText(),KebiasaanMerokok.getSelectedItem().toString(),KebiasaanJumlahRokok.getText(),KebiasaanAlkohol.getSelectedItem().toString(),KebiasaanJumlahAlkohol.getText(),KebiasaanNarkoba.getSelectedItem().toString(),OlahRaga.getSelectedItem().toString(),KesadaranMental.getText(), 
                    KeadaanMentalUmum.getSelectedItem().toString(),GCS.getText(),TD.getText(),Nadi.getText(),RR.getText(),Suhu.getText(),SpO2.getText(),BB.getText(),TB.getText(),SistemSarafKepala.getSelectedItem().toString(),KetSistemSarafKepala.getText(),SistemSarafWajah.getSelectedItem().toString(), 
                    KetSistemSarafWajah.getText(),SistemSarafLeher.getSelectedItem().toString(),SistemSarafKejang.getSelectedItem().toString(),KetSistemSarafKejang.getText(),SistemSarafSensorik.getSelectedItem().toString(),KardiovaskularDenyutNadi.getSelectedItem().toString(),KardiovaskularSirkulasi.getSelectedItem().toString(), 
                    KetKardiovaskularSirkulasi.getText(),KardiovaskularPulsasi.getSelectedItem().toString(),RespirasiPolaNafas.getSelectedItem().toString(),RespirasiRetraksi.getSelectedItem().toString(),RespirasiSuaraNafas.getSelectedItem().toString(),RespirasiVolume.getSelectedItem().toString(),
                    RespirasiJenisPernafasan.getSelectedItem().toString(),KetRespirasiJenisPernafasan.getText(),RespirasiIrama.getSelectedItem().toString(),RespirasiBatuk.getSelectedItem().toString(),GastrointestinalMulut.getSelectedItem().toString(),KetGastrointestinalMulut.getText(),
                    GastrointestinalGigi.getSelectedItem().toString(),KetGastrointestinalGigi.getText(),GastrointestinalLidah.getSelectedItem().toString(),KetGastrointestinalLidah.getText(),GastrointestinalTenggorakan.getSelectedItem().toString(),KetGastrointestinalTenggorakan.getText(), 
                    GastrointestinalAbdomen.getSelectedItem().toString(),KetGastrointestinalAbdomen.getText(),GastrointestinalUsus.getSelectedItem().toString(),GastrointestinalAnus.getSelectedItem().toString(),NeurologiPenglihatan.getSelectedItem().toString(),KetNeurologiPenglihatan.getText(), 
                    NeurologiAlatBantuPenglihatan.getSelectedItem().toString(),NeurologiPendengaran.getSelectedItem().toString(),NeurologiBicara.getSelectedItem().toString(),KetNeurologiBicara.getText(),NeurologiSensorik.getSelectedItem().toString(),NeurologiMotorik.getSelectedItem().toString(), 
                    NeurologiOtot.getSelectedItem().toString(),IntegumentWarnaKulit.getSelectedItem().toString(),IntegumentTurgor.getSelectedItem().toString(),IntegumentKulit.getSelectedItem().toString(),IntegumentDecubitus.getSelectedItem().toString(),MuskuloskletalPegerakanSendi.getSelectedItem().toString(), 
                    MuskuloskletalKekuatanOtot.getSelectedItem().toString(),MuskuloskletalNyeriSendi.getSelectedItem().toString(),KetMuskuloskletalNyeriSendi.getText(),MuskuloskletalOedema.getSelectedItem().toString(),KetMuskuloskletalOedema.getText(),MuskuloskletalFraktur.getSelectedItem().toString(), 
                    KetMuskuloskletalFraktur.getText(),BAB.getText(),XBAB.getText(),KBAB.getText(),WBAB.getText(),BAK.getText(),XBAK.getText(),WBAK.getText(),LBAK.getText(),PolaAktifitasMakan.getSelectedItem().toString(),PolaAktifitasMandi.getSelectedItem().toString(),PolaAktifitasEliminasi.getSelectedItem().toString(), 
                    PolaAktifitasBerpakaian.getSelectedItem().toString(),PolaAktifitasBerpindah.getSelectedItem().toString(),PolaNutrisiFrekuensi.getText(),PolaNutrisiJenis.getText(),PolaNutrisiPorsi.getText(),PolaTidurLama.getText(),PolaTidurGangguan.getSelectedItem().toString(),AktifitasSehari2.getSelectedItem().toString(), 
                    Aktifitas.getSelectedItem().toString(),Berjalan.getSelectedItem().toString(),KeteranganBerjalan.getText(),AlatAmbulasi.getSelectedItem().toString(),EkstrimitasAtas.getSelectedItem().toString(),KeteranganEkstrimitasAtas.getText(),EkstrimitasBawah.getSelectedItem().toString(),
                    KeteranganEkstrimitasBawah.getText(),KemampuanMenggenggam.getSelectedItem().toString(),KeteranganKemampuanMenggenggam.getText(),KemampuanKoordinasi.getSelectedItem().toString(),KeteranganKemampuanKoordinasi.getText(),KesimpulanGangguanFungsi.getSelectedItem().toString(),
                    KondisiPsikologis.getSelectedItem().toString(),GangguanJiwa.getSelectedItem().toString(),AdakahPerilaku.getSelectedItem().toString(),KeteranganAdakahPerilaku.getText(),HubunganAnggotaKeluarga.getSelectedItem().toString(),TinggalDengan.getSelectedItem().toString(),KeteranganTinggalDengan.getText(),
                    NilaiKepercayaan.getSelectedItem().toString(),KeteranganNilaiKepercayaan.getText(),PendidikanPJ.getSelectedItem().toString(),EdukasiPsikolgis.getSelectedItem().toString(),KeteranganEdukasiPsikologis.getText(),Nyeri.getSelectedItem().toString(),Provokes.getSelectedItem().toString(),KetProvokes.getText(), 
                    Quality.getSelectedItem().toString(),KetQuality.getText(),Lokasi.getText(),Menyebar.getSelectedItem().toString(),SkalaNyeri.getSelectedItem().toString(),Durasi.getText(),NyeriHilang.getSelectedItem().toString(),KetNyeri.getText(),PadaDokter.getSelectedItem().toString(), 
                    KetPadaDokter.getText(),SkalaResiko1.getSelectedItem().toString(),NilaiResiko1.getText(),SkalaResiko2.getSelectedItem().toString(),NilaiResiko2.getText(),SkalaResiko3.getSelectedItem().toString(),NilaiResiko3.getText(),SkalaResiko4.getSelectedItem().toString(),NilaiResiko4.getText(), 
                    SkalaResiko5.getSelectedItem().toString(),NilaiResiko5.getText(),SkalaResiko6.getSelectedItem().toString(),NilaiResiko6.getText(),NilaiResikoTotal.getText(),SkalaSydney1.getSelectedItem().toString(),NilaiSydney1.getText(),SkalaSydney2.getSelectedItem().toString(),NilaiSydney2.getText(),
                    SkalaSydney3.getSelectedItem().toString(),NilaiSydney3.getText(),SkalaSydney4.getSelectedItem().toString(),NilaiSydney4.getText(),SkalaSydney5.getSelectedItem().toString(),NilaiSydney5.getText(),SkalaSydney6.getSelectedItem().toString(),NilaiSydney6.getText(), 
                    SkalaSydney7.getSelectedItem().toString(),NilaiSydney7.getText(),SkalaSydney8.getSelectedItem().toString(),NilaiSydney8.getText(),SkalaSydney9.getSelectedItem().toString(),NilaiSydney9.getText(),SkalaSydney10.getSelectedItem().toString(),NilaiSydney10.getText(), 
                    SkalaSydney11.getSelectedItem().toString(),NilaiSydney11.getText(),NilaiSydneyTotal.getText(),SkalaGizi1.getSelectedItem().toString(),NilaiGizi1.getText(),SkalaGizi2.getSelectedItem().toString(),NilaiGizi2.getText(),NilaiGiziTotal.getText(),DiagnosaKhususGizi.getSelectedItem().toString(),
                    KeteranganDiagnosaKhususGizi.getText(),DiketahuiDietisen.getSelectedItem().toString(),KeteranganDiketahuiDietisen.getText(),Rencana.getText(),KdPetugas.getText(),KdPetugas2.getText(),KdDPJP.getText()
                })==true){
                    for (i = 0; i < tbMasalahKeperawatan.getRowCount(); i++) {
                        if(tbMasalahKeperawatan.getValueAt(i,0).toString().equals("true")){
                            Sequel.menyimpan2("penilaian_awal_keperawatan_ranap_masalah","?,?",2,new String[]{TNoRw.getText(),tbMasalahKeperawatan.getValueAt(i,1).toString()});
                        }
                    }
                    for (i = 0; i < tbRencanaKeperawatan.getRowCount(); i++) {
                        if(tbRencanaKeperawatan.getValueAt(i,0).toString().equals("true")){
                            Sequel.menyimpan2("penilaian_awal_keperawatan_ranap_rencana","?,?",2,new String[]{TNoRw.getText(),tbRencanaKeperawatan.getValueAt(i,1).toString()});
                        }
                    }
                    emptTeks();
            }
        }
}//GEN-LAST:event_BtnSimpanActionPerformed

    private void BtnSimpanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnSimpanKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnSimpanActionPerformed(null);
        }else{
            Valid.pindah(evt,Rencana,BtnBatal);
        }
}//GEN-LAST:event_BtnSimpanKeyPressed

    private void BtnBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnBatalActionPerformed
        emptTeks();
}//GEN-LAST:event_BtnBatalActionPerformed

    private void BtnBatalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnBatalKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            emptTeks();
        }else{
            Valid.pindah(evt, BtnSimpan, BtnHapus);
        }
}//GEN-LAST:event_BtnBatalKeyPressed

    private void BtnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnHapusActionPerformed
        if(tbObat.getSelectedRow()>-1){
            if(akses.getkode().equals("Admin Utama")){
                hapus();
            }else{
                if(KdPetugas.getText().equals(tbObat.getValueAt(tbObat.getSelectedRow(),5).toString())){
                    hapus();
                }else{
                    JOptionPane.showMessageDialog(null,"Hanya bisa dihapus oleh petugas yang bersangkutan..!!");
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
        }else if(KdPetugas.getText().trim().equals("")||NmPetugas.getText().trim().equals("")){
            Valid.textKosong(BtnPetugas,"Pengkaji 1");
        }else if(KdPetugas2.getText().trim().equals("")||NmPetugas2.getText().trim().equals("")){
            Valid.textKosong(BtnPetugas2,"Pegkaji 2");
        }else if(KdDPJP.getText().trim().equals("")||NmDPJP.getText().trim().equals("")){
            Valid.textKosong(BtnDPJP,"DPJP");
        
        }else{
            if(tbObat.getSelectedRow()>-1){
                if(akses.getkode().equals("Admin Utama")){
                    ganti();
                }else{
                    if(KdPetugas.getText().equals(tbObat.getValueAt(tbObat.getSelectedRow(),5).toString())){
                        ganti();
                    }else{
                        JOptionPane.showMessageDialog(null,"Hanya bisa diganti oleh petugas yang bersangkutan..!!");
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
                File g = new File("file2.css");            
                BufferedWriter bg = new BufferedWriter(new FileWriter(g));
                bg.write(
                        ".isi td{border-right: 1px solid #e2e7dd;font: 11px tahoma;height:12px;border-bottom: 1px solid #e2e7dd;background: #ffffff;color:#323232;}"+
                        ".isi2 td{font: 11px tahoma;height:12px;background: #ffffff;color:#323232;}"+                    
                        ".isi3 td{border-right: 1px solid #e2e7dd;font: 11px tahoma;height:12px;border-top: 1px solid #e2e7dd;background: #ffffff;color:#323232;}"+
                        ".isi4 td{font: 11px tahoma;height:12px;border-top: 1px solid #e2e7dd;background: #ffffff;color:#323232;}"
                );
                bg.close();

                File f;            
                BufferedWriter bw; 
                
                ps=koneksi.prepareStatement(
                    "select penilaian_awal_keperawatan_ranap_kemoterapi.no_rawat,penilaian_awal_keperawatan_ranap_kemoterapi.tanggal,penilaian_awal_keperawatan_ranap_kemoterapi.informasi,penilaian_awal_keperawatan_ranap_kemoterapi.ket_informasi,penilaian_awal_keperawatan_ranap_kemoterapi.tiba_diruang_rawat,"+
                    "penilaian_awal_keperawatan_ranap_kemoterapi.kasus_trauma,penilaian_awal_keperawatan_ranap_kemoterapi.cara_masuk,penilaian_awal_keperawatan_ranap_kemoterapi.rps,penilaian_awal_keperawatan_ranap_kemoterapi.rpd,penilaian_awal_keperawatan_ranap_kemoterapi.rpk,penilaian_awal_keperawatan_ranap_kemoterapi.rpo,"+
                    "penilaian_awal_keperawatan_ranap_kemoterapi.riwayat_pembedahan,penilaian_awal_keperawatan_ranap_kemoterapi.riwayat_dirawat_dirs,penilaian_awal_keperawatan_ranap_kemoterapi.alat_bantu_dipakai,penilaian_awal_keperawatan_ranap_kemoterapi.riwayat_kehamilan,"+
                    "penilaian_awal_keperawatan_ranap_kemoterapi.riwayat_kehamilan_perkiraan,penilaian_awal_keperawatan_ranap_kemoterapi.riwayat_tranfusi,penilaian_awal_keperawatan_ranap_kemoterapi.riwayat_alergi,penilaian_awal_keperawatan_ranap_kemoterapi.riwayat_merokok,"+
                    "penilaian_awal_keperawatan_ranap_kemoterapi.riwayat_merokok_jumlah,penilaian_awal_keperawatan_ranap_kemoterapi.riwayat_alkohol,penilaian_awal_keperawatan_ranap_kemoterapi.riwayat_alkohol_jumlah,penilaian_awal_keperawatan_ranap_kemoterapi.riwayat_narkoba,"+
                    "penilaian_awal_keperawatan_ranap_kemoterapi.riwayat_olahraga,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_mental,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_keadaan_umum,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_gcs,"+
                    "penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_td,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_nadi,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_rr,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_suhu,"+
                    "penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_spo2,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_bb,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_tb,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_susunan_kepala,"+
                    "penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_susunan_kepala_keterangan,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_susunan_wajah,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_susunan_wajah_keterangan,"+
                    "penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_susunan_leher,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_susunan_kejang,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_susunan_kejang_keterangan,"+
                    "penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_susunan_sensorik,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_kardiovaskuler_denyut_nadi,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_kardiovaskuler_sirkulasi,"+
                    "penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_kardiovaskuler_sirkulasi_keterangan,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_kardiovaskuler_pulsasi,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_respirasi_pola_nafas,"+
                    "penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_respirasi_retraksi,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_respirasi_suara_nafas,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_respirasi_volume_pernafasan,"+
                    "penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_respirasi_jenis_pernafasan,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_respirasi_jenis_pernafasan_keterangan,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_respirasi_irama_nafas,"+
                    "penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_respirasi_batuk,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_gastrointestinal_mulut,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_gastrointestinal_mulut_keterangan,"+
                    "penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_gastrointestinal_gigi,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_gastrointestinal_gigi_keterangan,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_gastrointestinal_lidah,"+
                    "penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_gastrointestinal_lidah_keterangan,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_gastrointestinal_tenggorokan,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_gastrointestinal_tenggorokan_keterangan,"+
                    "penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_gastrointestinal_abdomen,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_gastrointestinal_abdomen_keterangan,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_gastrointestinal_peistatik_usus,"+
                    "penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_gastrointestinal_anus,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_neurologi_pengelihatan,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_neurologi_pengelihatan_keterangan,"+
                    "penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_neurologi_alat_bantu_penglihatan,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_neurologi_pendengaran,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_neurologi_bicara,"+
                    "penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_neurologi_bicara_keterangan,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_neurologi_sensorik,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_neurologi_motorik,"+
                    "penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_neurologi_kekuatan_otot,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_integument_warnakulit,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_integument_turgor,"+
                    "penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_integument_kulit,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_integument_dekubitas,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_muskuloskletal_pergerakan_sendi,"+
                    "penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_muskuloskletal_kekauatan_otot,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_muskuloskletal_nyeri_sendi,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_muskuloskletal_nyeri_sendi_keterangan,"+
                    "penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_muskuloskletal_oedema,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_muskuloskletal_oedema_keterangan,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_muskuloskletal_fraktur,"+
                    "penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_muskuloskletal_fraktur_keterangan,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_eliminasi_bab_frekuensi_jumlah,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_eliminasi_bab_frekuensi_durasi,"+
                    "penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_eliminasi_bab_konsistensi,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_eliminasi_bab_warna,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_eliminasi_bak_frekuensi_jumlah,"+
                    "penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_eliminasi_bak_frekuensi_durasi,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_eliminasi_bak_warna,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_eliminasi_bak_lainlain,"+
                    "penilaian_awal_keperawatan_ranap_kemoterapi.pola_aktifitas_makanminum,penilaian_awal_keperawatan_ranap_kemoterapi.pola_aktifitas_mandi,penilaian_awal_keperawatan_ranap_kemoterapi.pola_aktifitas_eliminasi,penilaian_awal_keperawatan_ranap_kemoterapi.pola_aktifitas_berpakaian,"+
                    "penilaian_awal_keperawatan_ranap_kemoterapi.pola_aktifitas_berpindah,penilaian_awal_keperawatan_ranap_kemoterapi.pola_nutrisi_frekuesi_makan,penilaian_awal_keperawatan_ranap_kemoterapi.pola_nutrisi_jenis_makanan,penilaian_awal_keperawatan_ranap_kemoterapi.pola_nutrisi_porsi_makan,"+
                    "penilaian_awal_keperawatan_ranap_kemoterapi.pola_tidur_lama_tidur,penilaian_awal_keperawatan_ranap_kemoterapi.pola_tidur_gangguan,penilaian_awal_keperawatan_ranap_kemoterapi.pengkajian_fungsi_kemampuan_sehari,penilaian_awal_keperawatan_ranap_kemoterapi.pengkajian_fungsi_aktifitas,"+
                    "penilaian_awal_keperawatan_ranap_kemoterapi.pengkajian_fungsi_berjalan,penilaian_awal_keperawatan_ranap_kemoterapi.pengkajian_fungsi_berjalan_keterangan,penilaian_awal_keperawatan_ranap_kemoterapi.pengkajian_fungsi_ambulasi,"+
                    "penilaian_awal_keperawatan_ranap_kemoterapi.pengkajian_fungsi_ekstrimitas_atas,penilaian_awal_keperawatan_ranap_kemoterapi.pengkajian_fungsi_ekstrimitas_atas_keterangan,penilaian_awal_keperawatan_ranap_kemoterapi.pengkajian_fungsi_ekstrimitas_bawah,"+
                    "penilaian_awal_keperawatan_ranap_kemoterapi.pengkajian_fungsi_ekstrimitas_bawah_keterangan,penilaian_awal_keperawatan_ranap_kemoterapi.pengkajian_fungsi_menggenggam,penilaian_awal_keperawatan_ranap_kemoterapi.pengkajian_fungsi_menggenggam_keterangan,"+
                    "penilaian_awal_keperawatan_ranap_kemoterapi.pengkajian_fungsi_koordinasi,penilaian_awal_keperawatan_ranap_kemoterapi.pengkajian_fungsi_koordinasi_keterangan,penilaian_awal_keperawatan_ranap_kemoterapi.pengkajian_fungsi_kesimpulan,"+
                    "penilaian_awal_keperawatan_ranap_kemoterapi.riwayat_psiko_kondisi_psiko,penilaian_awal_keperawatan_ranap_kemoterapi.riwayat_psiko_gangguan_jiwa,penilaian_awal_keperawatan_ranap_kemoterapi.riwayat_psiko_perilaku,"+
                    "penilaian_awal_keperawatan_ranap_kemoterapi.riwayat_psiko_perilaku_keterangan,penilaian_awal_keperawatan_ranap_kemoterapi.riwayat_psiko_hubungan_keluarga,penilaian_awal_keperawatan_ranap_kemoterapi.riwayat_psiko_tinggal,"+
                    "penilaian_awal_keperawatan_ranap_kemoterapi.riwayat_psiko_tinggal_keterangan,penilaian_awal_keperawatan_ranap_kemoterapi.riwayat_psiko_nilai_kepercayaan,penilaian_awal_keperawatan_ranap_kemoterapi.riwayat_psiko_nilai_kepercayaan_keterangan,"+
                    "penilaian_awal_keperawatan_ranap_kemoterapi.riwayat_psiko_pendidikan_pj,penilaian_awal_keperawatan_ranap_kemoterapi.riwayat_psiko_edukasi_diberikan,penilaian_awal_keperawatan_ranap_kemoterapi.riwayat_psiko_edukasi_diberikan_keterangan,"+
                    "penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_nyeri,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_nyeri_penyebab,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_nyeri_ket_penyebab,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_nyeri_kualitas,"+
                    "penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_nyeri_ket_kualitas,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_nyeri_lokasi,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_nyeri_menyebar,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_nyeri_skala,"+
                    "penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_nyeri_waktu,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_nyeri_hilang,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_nyeri_ket_hilang,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_nyeri_diberitahukan_dokter,"+
                    "penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_nyeri_jam_diberitahukan_dokter,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhmorse_skala1,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhmorse_nilai1,"+
                    "penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhmorse_skala2,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhmorse_nilai2,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhmorse_skala3,"+
                    "penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhmorse_nilai3,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhmorse_skala4,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhmorse_nilai4,"+
                    "penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhmorse_skala5,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhmorse_nilai5,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhmorse_skala6,"+
                    "penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhmorse_nilai6,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhmorse_totalnilai,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhsydney_skala1,"+
                    "penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhsydney_nilai1,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhsydney_skala2,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhsydney_nilai2,"+
                    "penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhsydney_skala3,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhsydney_nilai3,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhsydney_skala4,"+
                    "penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhsydney_nilai4,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhsydney_skala5,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhsydney_nilai5,"+
                    "penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhsydney_skala6,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhsydney_nilai6,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhsydney_skala7,"+
                    "penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhsydney_nilai7,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhsydney_skala8,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhsydney_nilai8,"+
                    "penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhsydney_skala9,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhsydney_nilai9,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhsydney_skala10,"+
                    "penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhsydney_nilai10,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhsydney_skala11,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhsydney_nilai11,"+
                    "penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhsydney_totalnilai,penilaian_awal_keperawatan_ranap_kemoterapi.skrining_gizi1,penilaian_awal_keperawatan_ranap_kemoterapi.nilai_gizi1,penilaian_awal_keperawatan_ranap_kemoterapi.skrining_gizi2,"+
                    "penilaian_awal_keperawatan_ranap_kemoterapi.nilai_gizi2,penilaian_awal_keperawatan_ranap_kemoterapi.nilai_total_gizi,penilaian_awal_keperawatan_ranap_kemoterapi.skrining_gizi_diagnosa_khusus,penilaian_awal_keperawatan_ranap_kemoterapi.skrining_gizi_ket_diagnosa_khusus,"+
                    "penilaian_awal_keperawatan_ranap_kemoterapi.skrining_gizi_diketahui_dietisen,penilaian_awal_keperawatan_ranap_kemoterapi.skrining_gizi_jam_diketahui_dietisen,penilaian_awal_keperawatan_ranap_kemoterapi.rencana,penilaian_awal_keperawatan_ranap_kemoterapi.nip1,"+
                    "penilaian_awal_keperawatan_ranap_kemoterapi.nip2,penilaian_awal_keperawatan_ranap_kemoterapi.kd_dokter,pasien.tgl_lahir,pasien.jk,pengkaji1.nama as pengkaji1,pengkaji2.nama as pengkaji2,dokter.nm_dokter,"+
                    "reg_periksa.no_rkm_medis,pasien.nm_pasien,pasien.agama,pasien.pekerjaan,pasien.pnd,penjab.png_jawab,bahasa_pasien.nama_bahasa "+
                    "from reg_periksa inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                    "inner join penilaian_awal_keperawatan_ranap_kemoterapi on reg_periksa.no_rawat=penilaian_awal_keperawatan_ranap_kemoterapi.no_rawat "+
                    "inner join petugas as pengkaji1 on penilaian_awal_keperawatan_ranap_kemoterapi.nip1=pengkaji1.nip "+
                    "inner join petugas as pengkaji2 on penilaian_awal_keperawatan_ranap_kemoterapi.nip2=pengkaji2.nip "+
                    "inner join dokter on penilaian_awal_keperawatan_ranap_kemoterapi.kd_dokter=dokter.kd_dokter "+
                    "inner join bahasa_pasien on bahasa_pasien.id=pasien.bahasa_pasien "+
                    "inner join penjab on penjab.kd_pj=reg_periksa.kd_pj where "+
                    "penilaian_awal_keperawatan_ranap_kemoterapi.tanggal between ? and ? "+
                    (TCari.getText().trim().equals("")?"":"and (reg_periksa.no_rawat like ? or pasien.no_rkm_medis like ? or pasien.nm_pasien like ? or penilaian_awal_keperawatan_ranap_kemoterapi.nip1 like ? or "+
                    "pengkaji1.nama like ? or penilaian_awal_keperawatan_ranap_kemoterapi.kd_dokter like ? or dokter.nm_dokter like ?)")+
                    " order by penilaian_awal_keperawatan_ranap_kemoterapi.tanggal");

                try {
                    ps.setString(1,Valid.SetTgl(DTPCari1.getSelectedItem()+"")+" 00:00:00");
                    ps.setString(2,Valid.SetTgl(DTPCari2.getSelectedItem()+"")+" 23:59:59");
                    if(!TCari.getText().equals("")){
                        ps.setString(3,"%"+TCari.getText()+"%");
                        ps.setString(4,"%"+TCari.getText()+"%");
                        ps.setString(5,"%"+TCari.getText()+"%");
                        ps.setString(6,"%"+TCari.getText()+"%");
                        ps.setString(7,"%"+TCari.getText()+"%");
                        ps.setString(8,"%"+TCari.getText()+"%");
                        ps.setString(9,"%"+TCari.getText()+"%");
                    }   
                    rs=ps.executeQuery();
                    pilihan = (String)JOptionPane.showInputDialog(null,"Silahkan pilih laporan..!","Pilihan Cetak",JOptionPane.QUESTION_MESSAGE,null,new Object[]{"Laporan 1 (HTML)","Laporan 2 (WPS)","Laporan 3 (CSV)"},"Laporan 1 (HTML)");
                    switch (pilihan) {
                        case "Laporan 1 (HTML)":
                                htmlContent = new StringBuilder();
                                htmlContent.append(                             
                                    "<tr class='isi'>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='105px'>No.Rawat</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>No.RM</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Nama Pasien</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Tgl.Lahir</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='25px'>J.K.</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>NIP Pengkaji 1</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Nama Pengkaji 1</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>NIP Pengkaji 2</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Nama Pengkaji 2</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Kode DPJP</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Nama DPJP</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='117px'>Tgl.Asuhan</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='78px'>Macam Kasus</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Anamnesis</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='110px'>Tiba Di Ruang Rawat</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Cara Masuk</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='220px'>Riwayat Penyakit Saat Ini</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Riwayat Penyakit Dahulu</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Riwayat Penyakit Keluarga</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Riwayat Penggunaan Obat</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Riwayat Pembedahan</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Riwayat Dirawat Di RS</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Alat Bantu Yang Dipakai</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='210px'>Dalam Keadaan Hamil/Sedang Menyusui</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Riwayat Transfusi Darah</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Riwayat Alergi</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='48px'>Merokok</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Batang/Hari</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='44px'>Alkohol</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='59px'>Gelas/Hari</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='61px'>Obat Tidur</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='59px'>Olah Raga</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Kesadaran Mental</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Keadaan Umum</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='64px'>GCS(E,V,M)</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='60px'>TD(mmHg)</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='74px'>Nadi(x/menit)</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='67px'>RR(x/menit)</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='52px'>Suhu(°C)</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='52px'>SpO2(%)</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='44px'>BB(Kg)</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='44px'>TB(cm)</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Kepala</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Wajah</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='106px'>Leher</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Kejang</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Sensorik</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='50px'>Pulsasi</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Sirkulasi</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='72px'>Denyut Nadi</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='54px'>Retraksi</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='63px'>Pola Nafas</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='69px'>Suara Nafas</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='97px'>Batuk & Sekresi</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='75px'>Volume</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Jenis Pernafasaan</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Irama</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Mulut</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Lidah</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Gigi</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Tenggorokan</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Abdomen</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Peistatik Usus</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Anus</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Sensorik</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Penglihatan</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Alat Bantu Penglihatan</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Motorik</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Pendengaran</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Bicara</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Otot</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Kulit</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Warna Kulit</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Turgor</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Resiko Decubitas</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Oedema</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Pergerakan Sendi</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Kekuatan Otot</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Fraktur</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Nyeri Sendi</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Frekuensi BAB</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>x/</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Konsistensi BAB</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Warna BAB</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Frekuensi BAK</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>x/</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Warna BAK</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Lain-lain BAK</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Mandi</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Makan/Minum</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Berpakaian</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Eliminasi</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Berpindah</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Porsi Makan</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Frekuensi Makan</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Jenis Makanan</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Lama Tidur</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Gangguan Tidur</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>a. Aktifitas Sehari-hari</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>b. Berjalan</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>c. Aktifitas</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>d. Alat Ambulasi</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>e. Ekstremitas Atas</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>f. Ekstremitas Bawah</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>g. Kemampuan Menggenggam</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>h. Kemampuan Koordinasi</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>i. Kesimpulan Gangguan Fungsi</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>a. Kondisi Psikologis</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>b. Adakah Perilaku</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>c. Gangguan Jiwa di Masa Lalu</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>d. Hubungan Pasien</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>e. Agama</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>f. Tinggal Dengan</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>g. Pekerjaan</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>h. Pembayaran</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>i. Nilai-nilai Kepercayaan</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>j. Bahasa Sehari-hari</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>k. Pendidikan Pasien</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>l. Pendidikan P.J.</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>m. Edukasi Diberikan Kepada</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Nyeri</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Penyebab Nyeri</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Kualitas Nyeri</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Lokasi Nyeri</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Nyeri Menyebar</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Skala Nyeri</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Waktu / Durasi</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Nyeri Hilang Bila</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Diberitahukan Pada Dokter</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Skala Morse 1</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>N.M. 1</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Skala Morse 2</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>N.M. 2</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Skala Morse 3</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>N.M. 3</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Skala Morse 4</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>N.M. 4</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Skala Morse 5</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>N.M. 5</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Skala Morse 6</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>N.M. 6</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>T.M.</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Skala Sydney 1</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>N.S. 1</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Skala Sydney 2</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>N.S. 2</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Skala Sydney 3</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>N.S. 3</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Skala Sydney 4</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>N.S. 4</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Skala Sydney 5</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>N.S. 5</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Skala Sydney 6</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>N.S. 6</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Skala Sydney 7</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>N.S. 7</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Skala Sydney 8</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>N.S. 8</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Skala Sydney 9</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>N.S. 9</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Skala Sydney 10</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>N.S. 10</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Skala Sydney 11</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>N.S. 11</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>T.S.</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>1. Apakah ada penurunan BB yang tidak diinginkan selama 6 bulan terakhir ?</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Skor 1</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>2. Apakah asupan makan berkurang karena tidak nafsu makan ?</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Skor 2</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Total Skor</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Pasien dengan diagnosis khusus</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Keterangan Diagnosa Khusus</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Sudah dibaca dan diketahui oleh Dietisen</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Jam Dibaca Dietisen</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Rencana Keperawatan Lainnya</td>"+
                                    "</tr>"
                                );
                                while(rs.next()){
                                    htmlContent.append(
                                        "<tr class='isi'>"+
                                            "<td valign='top'>"+rs.getString("no_rawat")+"</td>"+
                                            "<td valign='top'>"+rs.getString("no_rkm_medis")+"</td>"+
                                            "<td valign='top'>"+rs.getString("nm_pasien")+"</td>"+
                                            "<td valign='top'>"+rs.getString("tgl_lahir")+"</td>"+
                                            "<td valign='top'>"+rs.getString("jk")+"</td>"+
                                            "<td valign='top'>"+rs.getString("nip1")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pengkaji1")+"</td>"+
                                            "<td valign='top'>"+rs.getString("nip2")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pengkaji2")+"</td>"+
                                            "<td valign='top'>"+rs.getString("kd_dokter")+"</td>"+
                                            "<td valign='top'>"+rs.getString("nm_dokter")+"</td>"+
                                            "<td valign='top'>"+rs.getString("tanggal")+"</td>"+
                                            "<td valign='top'>"+rs.getString("kasus_trauma")+"</td>"+
                                            "<td valign='top'>"+rs.getString("informasi")+", "+rs.getString("ket_informasi")+"</td>"+
                                            "<td valign='top'>"+rs.getString("tiba_diruang_rawat")+"</td>"+
                                            "<td valign='top'>"+rs.getString("cara_masuk")+"</td>"+
                                            "<td valign='top'>"+rs.getString("rps")+"</td>"+
                                            "<td valign='top'>"+rs.getString("rpd")+"</td>"+
                                            "<td valign='top'>"+rs.getString("rpk")+"</td>"+
                                            "<td valign='top'>"+rs.getString("rpo")+"</td>"+
                                            "<td valign='top'>"+rs.getString("riwayat_pembedahan")+"</td>"+
                                            "<td valign='top'>"+rs.getString("riwayat_dirawat_dirs")+"</td>"+
                                            "<td valign='top'>"+rs.getString("alat_bantu_dipakai")+"</td>"+
                                            "<td valign='top'>"+rs.getString("riwayat_kehamilan")+", "+rs.getString("riwayat_kehamilan_perkiraan")+"</td>"+
                                            "<td valign='top'>"+rs.getString("riwayat_tranfusi")+"</td>"+
                                            "<td valign='top'>"+rs.getString("riwayat_alergi")+"</td>"+
                                            "<td valign='top'>"+rs.getString("riwayat_merokok")+"</td>"+
                                            "<td valign='top'>"+rs.getString("riwayat_merokok_jumlah")+"</td>"+
                                            "<td valign='top'>"+rs.getString("riwayat_alkohol")+"</td>"+
                                            "<td valign='top'>"+rs.getString("riwayat_alkohol_jumlah")+"</td>"+
                                            "<td valign='top'>"+rs.getString("riwayat_narkoba")+"</td>"+
                                            "<td valign='top'>"+rs.getString("riwayat_olahraga")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_mental")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_keadaan_umum")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_gcs")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_td")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_nadi")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_rr")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_suhu")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_spo2")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_bb")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_tb")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_susunan_kepala")+", "+rs.getString("pemeriksaan_susunan_kepala_keterangan")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_susunan_wajah")+", "+rs.getString("pemeriksaan_susunan_wajah_keterangan")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_susunan_leher")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_susunan_kejang")+", "+rs.getString("pemeriksaan_susunan_kejang_keterangan")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_susunan_sensorik")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_kardiovaskuler_pulsasi")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_kardiovaskuler_sirkulasi")+", "+rs.getString("pemeriksaan_kardiovaskuler_sirkulasi_keterangan")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_kardiovaskuler_denyut_nadi")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_respirasi_retraksi")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_respirasi_pola_nafas")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_respirasi_suara_nafas")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_respirasi_batuk")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_respirasi_volume_pernafasan")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_respirasi_jenis_pernafasan")+", "+rs.getString("pemeriksaan_respirasi_jenis_pernafasan_keterangan")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_respirasi_irama_nafas")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_gastrointestinal_mulut")+", "+rs.getString("pemeriksaan_gastrointestinal_mulut_keterangan")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_gastrointestinal_lidah")+", "+rs.getString("pemeriksaan_gastrointestinal_lidah_keterangan")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_gastrointestinal_gigi")+", "+rs.getString("pemeriksaan_gastrointestinal_gigi_keterangan")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_gastrointestinal_tenggorokan")+", "+rs.getString("pemeriksaan_gastrointestinal_tenggorokan_keterangan")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_gastrointestinal_abdomen")+", "+rs.getString("pemeriksaan_gastrointestinal_abdomen_keterangan")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_gastrointestinal_peistatik_usus")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_gastrointestinal_anus")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_neurologi_sensorik")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_neurologi_pengelihatan")+", "+rs.getString("pemeriksaan_neurologi_pengelihatan_keterangan")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_neurologi_alat_bantu_penglihatan")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_neurologi_motorik")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_neurologi_pendengaran")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_neurologi_bicara")+", "+rs.getString("pemeriksaan_neurologi_bicara_keterangan")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_neurologi_kekuatan_otot")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_integument_kulit")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_integument_warnakulit")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_integument_turgor")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_integument_dekubitas")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_muskuloskletal_oedema")+", "+rs.getString("pemeriksaan_muskuloskletal_oedema_keterangan")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_muskuloskletal_pergerakan_sendi")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_muskuloskletal_kekauatan_otot")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_muskuloskletal_fraktur")+", "+rs.getString("pemeriksaan_muskuloskletal_fraktur_keterangan")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_muskuloskletal_nyeri_sendi")+", "+rs.getString("pemeriksaan_muskuloskletal_nyeri_sendi_keterangan")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_eliminasi_bab_frekuensi_jumlah")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_eliminasi_bab_frekuensi_durasi")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_eliminasi_bab_konsistensi")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_eliminasi_bab_warna")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_eliminasi_bak_frekuensi_jumlah")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_eliminasi_bak_frekuensi_durasi")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_eliminasi_bak_warna")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_eliminasi_bak_lainlain")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pola_aktifitas_mandi")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pola_aktifitas_makanminum")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pola_aktifitas_berpakaian")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pola_aktifitas_eliminasi")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pola_aktifitas_berpindah")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pola_nutrisi_porsi_makan")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pola_nutrisi_frekuesi_makan")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pola_nutrisi_jenis_makanan")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pola_tidur_lama_tidur")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pola_tidur_gangguan")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pengkajian_fungsi_kemampuan_sehari")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pengkajian_fungsi_berjalan")+", "+rs.getString("pengkajian_fungsi_berjalan_keterangan")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pengkajian_fungsi_aktifitas")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pengkajian_fungsi_ambulasi")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pengkajian_fungsi_ekstrimitas_atas")+", "+rs.getString("pengkajian_fungsi_ekstrimitas_atas_keterangan")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pengkajian_fungsi_ekstrimitas_bawah")+", "+rs.getString("pengkajian_fungsi_ekstrimitas_bawah_keterangan")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pengkajian_fungsi_menggenggam")+", "+rs.getString("pengkajian_fungsi_menggenggam_keterangan")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pengkajian_fungsi_koordinasi")+", "+rs.getString("pengkajian_fungsi_koordinasi_keterangan")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pengkajian_fungsi_kesimpulan")+"</td>"+
                                            "<td valign='top'>"+rs.getString("riwayat_psiko_kondisi_psiko")+"</td>"+
                                            "<td valign='top'>"+rs.getString("riwayat_psiko_perilaku")+", "+rs.getString("riwayat_psiko_perilaku_keterangan")+"</td>"+
                                            "<td valign='top'>"+rs.getString("riwayat_psiko_gangguan_jiwa")+"</td>"+
                                            "<td valign='top'>"+rs.getString("riwayat_psiko_hubungan_keluarga")+"</td>"+
                                            "<td valign='top'>"+rs.getString("agama")+"</td>"+
                                            "<td valign='top'>"+rs.getString("riwayat_psiko_tinggal")+", "+rs.getString("riwayat_psiko_tinggal_keterangan")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pekerjaan")+"</td>"+
                                            "<td valign='top'>"+rs.getString("png_jawab")+"</td>"+
                                            "<td valign='top'>"+rs.getString("riwayat_psiko_nilai_kepercayaan")+", "+rs.getString("riwayat_psiko_nilai_kepercayaan_keterangan")+"</td>"+
                                            "<td valign='top'>"+rs.getString("nama_bahasa")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pnd")+"</td>"+
                                            "<td valign='top'>"+rs.getString("riwayat_psiko_pendidikan_pj")+"</td>"+
                                            "<td valign='top'>"+rs.getString("riwayat_psiko_edukasi_diberikan")+", "+rs.getString("riwayat_psiko_edukasi_diberikan_keterangan")+"</td>"+
                                            "<td valign='top'>"+rs.getString("penilaian_nyeri")+"</td>"+
                                            "<td valign='top'>"+rs.getString("penilaian_nyeri_penyebab")+", "+rs.getString("penilaian_nyeri_ket_penyebab")+"</td>"+
                                            "<td valign='top'>"+rs.getString("penilaian_nyeri_kualitas")+", "+rs.getString("penilaian_nyeri_ket_kualitas")+"</td>"+
                                            "<td valign='top'>"+rs.getString("penilaian_nyeri_lokasi")+"</td>"+
                                            "<td valign='top'>"+rs.getString("penilaian_nyeri_menyebar")+"</td>"+
                                            "<td valign='top'>"+rs.getString("penilaian_nyeri_skala")+"</td>"+
                                            "<td valign='top'>"+rs.getString("penilaian_nyeri_waktu")+"</td>"+
                                            "<td valign='top'>"+rs.getString("penilaian_nyeri_hilang")+", "+rs.getString("penilaian_nyeri_ket_hilang")+"</td>"+
                                            "<td valign='top'>"+rs.getString("penilaian_nyeri_diberitahukan_dokter")+", "+rs.getString("penilaian_nyeri_jam_diberitahukan_dokter")+"</td>"+
                                            "<td valign='top'>"+rs.getString("penilaian_jatuhmorse_skala1")+"</td>"+
                                            "<td valign='top'>"+rs.getString("penilaian_jatuhmorse_nilai1")+"</td>"+
                                            "<td valign='top'>"+rs.getString("penilaian_jatuhmorse_skala2")+"</td>"+
                                            "<td valign='top'>"+rs.getString("penilaian_jatuhmorse_nilai2")+"</td>"+
                                            "<td valign='top'>"+rs.getString("penilaian_jatuhmorse_skala3")+"</td>"+
                                            "<td valign='top'>"+rs.getString("penilaian_jatuhmorse_nilai3")+"</td>"+
                                            "<td valign='top'>"+rs.getString("penilaian_jatuhmorse_skala4")+"</td>"+
                                            "<td valign='top'>"+rs.getString("penilaian_jatuhmorse_nilai4")+"</td>"+
                                            "<td valign='top'>"+rs.getString("penilaian_jatuhmorse_skala5")+"</td>"+
                                            "<td valign='top'>"+rs.getString("penilaian_jatuhmorse_nilai5")+"</td>"+
                                            "<td valign='top'>"+rs.getString("penilaian_jatuhmorse_skala6")+"</td>"+
                                            "<td valign='top'>"+rs.getString("penilaian_jatuhmorse_nilai6")+"</td>"+
                                            "<td valign='top'>"+rs.getString("penilaian_jatuhmorse_totalnilai")+"</td>"+
                                            "<td valign='top'>"+rs.getString("penilaian_jatuhsydney_skala1")+"</td>"+
                                            "<td valign='top'>"+rs.getString("penilaian_jatuhsydney_nilai1")+"</td>"+
                                            "<td valign='top'>"+rs.getString("penilaian_jatuhsydney_skala2")+"</td>"+
                                            "<td valign='top'>"+rs.getString("penilaian_jatuhsydney_nilai2")+"</td>"+
                                            "<td valign='top'>"+rs.getString("penilaian_jatuhsydney_skala3")+"</td>"+
                                            "<td valign='top'>"+rs.getString("penilaian_jatuhsydney_nilai3")+"</td>"+
                                            "<td valign='top'>"+rs.getString("penilaian_jatuhsydney_skala4")+"</td>"+
                                            "<td valign='top'>"+rs.getString("penilaian_jatuhsydney_nilai4")+"</td>"+
                                            "<td valign='top'>"+rs.getString("penilaian_jatuhsydney_skala5")+"</td>"+
                                            "<td valign='top'>"+rs.getString("penilaian_jatuhsydney_nilai5")+"</td>"+
                                            "<td valign='top'>"+rs.getString("penilaian_jatuhsydney_skala6")+"</td>"+
                                            "<td valign='top'>"+rs.getString("penilaian_jatuhsydney_nilai6")+"</td>"+
                                            "<td valign='top'>"+rs.getString("penilaian_jatuhsydney_skala7")+"</td>"+
                                            "<td valign='top'>"+rs.getString("penilaian_jatuhsydney_nilai7")+"</td>"+
                                            "<td valign='top'>"+rs.getString("penilaian_jatuhsydney_skala8")+"</td>"+
                                            "<td valign='top'>"+rs.getString("penilaian_jatuhsydney_nilai8")+"</td>"+
                                            "<td valign='top'>"+rs.getString("penilaian_jatuhsydney_skala9")+"</td>"+
                                            "<td valign='top'>"+rs.getString("penilaian_jatuhsydney_nilai9")+"</td>"+
                                            "<td valign='top'>"+rs.getString("penilaian_jatuhsydney_skala10")+"</td>"+
                                            "<td valign='top'>"+rs.getString("penilaian_jatuhsydney_nilai10")+"</td>"+
                                            "<td valign='top'>"+rs.getString("penilaian_jatuhsydney_skala11")+"</td>"+
                                            "<td valign='top'>"+rs.getString("penilaian_jatuhsydney_nilai11")+"</td>"+
                                            "<td valign='top'>"+rs.getString("penilaian_jatuhsydney_totalnilai")+"</td>"+
                                            "<td valign='top'>"+rs.getString("skrining_gizi1")+"</td>"+
                                            "<td valign='top'>"+rs.getString("nilai_gizi1")+"</td>"+
                                            "<td valign='top'>"+rs.getString("skrining_gizi2")+"</td>"+
                                            "<td valign='top'>"+rs.getString("nilai_gizi2")+"</td>"+
                                            "<td valign='top'>"+rs.getString("nilai_total_gizi")+"</td>"+
                                            "<td valign='top'>"+rs.getString("skrining_gizi_diagnosa_khusus")+"</td>"+
                                            "<td valign='top'>"+rs.getString("skrining_gizi_ket_diagnosa_khusus")+"</td>"+
                                            "<td valign='top'>"+rs.getString("skrining_gizi_diketahui_dietisen")+"</td>"+
                                            "<td valign='top'>"+rs.getString("skrining_gizi_jam_diketahui_dietisen")+"</td>"+
                                            "<td valign='top'>"+rs.getString("rencana")+"</td>"+
                                        "</tr>"
                                    );
                                }
                                f = new File("RMPenilaianAwalKeperawatanRanap.html");            
                                bw = new BufferedWriter(new FileWriter(f));            
                                bw.write("<html>"+
                                            "<head><link href=\"file2.css\" rel=\"stylesheet\" type=\"text/css\" /></head>"+
                                            "<body>"+
                                                "<table width='18500px' border='0' align='center' cellpadding='3px' cellspacing='0' class='tbl_form'>"+
                                                    htmlContent.toString()+
                                                "</table>"+
                                            "</body>"+                   
                                         "</html>"
                                );

                                bw.close();                         
                                Desktop.getDesktop().browse(f.toURI());
                            break;
                        case "Laporan 2 (WPS)":
                                htmlContent = new StringBuilder();
                                htmlContent.append(                             
                                    "<tr class='isi'>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='105px'>No.Rawat</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>No.RM</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Nama Pasien</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Tgl.Lahir</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='25px'>J.K.</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>NIP Pengkaji 1</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Nama Pengkaji 1</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>NIP Pengkaji 2</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Nama Pengkaji 2</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Kode DPJP</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Nama DPJP</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='117px'>Tgl.Asuhan</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='78px'>Macam Kasus</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Anamnesis</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='110px'>Tiba Di Ruang Rawat</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Cara Masuk</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='220px'>Riwayat Penyakit Saat Ini</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Riwayat Penyakit Dahulu</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Riwayat Penyakit Keluarga</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Riwayat Penggunaan Obat</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Riwayat Pembedahan</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Riwayat Dirawat Di RS</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Alat Bantu Yang Dipakai</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='210px'>Dalam Keadaan Hamil/Sedang Menyusui</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Riwayat Transfusi Darah</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Riwayat Alergi</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='48px'>Merokok</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Batang/Hari</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='44px'>Alkohol</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='59px'>Gelas/Hari</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='61px'>Obat Tidur</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='59px'>Olah Raga</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Kesadaran Mental</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Keadaan Umum</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='64px'>GCS(E,V,M)</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='60px'>TD(mmHg)</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='74px'>Nadi(x/menit)</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='67px'>RR(x/menit)</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='52px'>Suhu(°C)</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='52px'>SpO2(%)</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='44px'>BB(Kg)</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='44px'>TB(cm)</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Kepala</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Wajah</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='106px'>Leher</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Kejang</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Sensorik</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='50px'>Pulsasi</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Sirkulasi</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='72px'>Denyut Nadi</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='54px'>Retraksi</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='63px'>Pola Nafas</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='69px'>Suara Nafas</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='97px'>Batuk & Sekresi</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='75px'>Volume</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Jenis Pernafasaan</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Irama</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Mulut</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Lidah</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Gigi</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Tenggorokan</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Abdomen</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Peistatik Usus</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Anus</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Sensorik</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Penglihatan</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Alat Bantu Penglihatan</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Motorik</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Pendengaran</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Bicara</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Otot</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Kulit</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Warna Kulit</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Turgor</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Resiko Decubitas</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Oedema</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Pergerakan Sendi</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Kekuatan Otot</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Fraktur</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Nyeri Sendi</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Frekuensi BAB</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>x/</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Konsistensi BAB</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Warna BAB</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Frekuensi BAK</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>x/</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Warna BAK</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Lain-lain BAK</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Mandi</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Makan/Minum</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Berpakaian</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Eliminasi</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Berpindah</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Porsi Makan</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Frekuensi Makan</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Jenis Makanan</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Lama Tidur</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Gangguan Tidur</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>a. Aktifitas Sehari-hari</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>b. Berjalan</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>c. Aktifitas</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>d. Alat Ambulasi</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>e. Ekstremitas Atas</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>f. Ekstremitas Bawah</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>g. Kemampuan Menggenggam</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>h. Kemampuan Koordinasi</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>i. Kesimpulan Gangguan Fungsi</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>a. Kondisi Psikologis</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>b. Adakah Perilaku</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>c. Gangguan Jiwa di Masa Lalu</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>d. Hubungan Pasien</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>e. Agama</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>f. Tinggal Dengan</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>g. Pekerjaan</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>h. Pembayaran</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>i. Nilai-nilai Kepercayaan</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>j. Bahasa Sehari-hari</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>k. Pendidikan Pasien</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>l. Pendidikan P.J.</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>m. Edukasi Diberikan Kepada</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Nyeri</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Penyebab Nyeri</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Kualitas Nyeri</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Lokasi Nyeri</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Nyeri Menyebar</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Skala Nyeri</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Waktu / Durasi</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Nyeri Hilang Bila</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Diberitahukan Pada Dokter</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Skala Morse 1</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>N.M. 1</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Skala Morse 2</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>N.M. 2</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Skala Morse 3</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>N.M. 3</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Skala Morse 4</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>N.M. 4</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Skala Morse 5</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>N.M. 5</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Skala Morse 6</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>N.M. 6</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>T.M.</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Skala Sydney 1</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>N.S. 1</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Skala Sydney 2</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>N.S. 2</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Skala Sydney 3</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>N.S. 3</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Skala Sydney 4</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>N.S. 4</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Skala Sydney 5</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>N.S. 5</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Skala Sydney 6</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>N.S. 6</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Skala Sydney 7</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>N.S. 7</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Skala Sydney 8</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>N.S. 8</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Skala Sydney 9</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>N.S. 9</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Skala Sydney 10</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>N.S. 10</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Skala Sydney 11</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>N.S. 11</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>T.S.</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>1. Apakah ada penurunan BB yang tidak diinginkan selama 6 bulan terakhir ?</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Skor 1</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>2. Apakah asupan makan berkurang karena tidak nafsu makan ?</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Skor 2</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Total Skor</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Pasien dengan diagnosis khusus</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Keterangan Diagnosa Khusus</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Sudah dibaca dan diketahui oleh Dietisen</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Jam Dibaca Dietisen</td>"+
                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Rencana Keperawatan Lainnya</td>"+
                                    "</tr>"
                                );
                                while(rs.next()){
                                    htmlContent.append(
                                        "<tr class='isi'>"+
                                            "<td valign='top'>"+rs.getString("no_rawat")+"</td>"+
                                            "<td valign='top'>"+rs.getString("no_rkm_medis")+"</td>"+
                                            "<td valign='top'>"+rs.getString("nm_pasien")+"</td>"+
                                            "<td valign='top'>"+rs.getString("tgl_lahir")+"</td>"+
                                            "<td valign='top'>"+rs.getString("jk")+"</td>"+
                                            "<td valign='top'>"+rs.getString("nip1")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pengkaji1")+"</td>"+
                                            "<td valign='top'>"+rs.getString("nip2")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pengkaji2")+"</td>"+
                                            "<td valign='top'>"+rs.getString("kd_dokter")+"</td>"+
                                            "<td valign='top'>"+rs.getString("nm_dokter")+"</td>"+
                                            "<td valign='top'>"+rs.getString("tanggal")+"</td>"+
                                            "<td valign='top'>"+rs.getString("kasus_trauma")+"</td>"+
                                            "<td valign='top'>"+rs.getString("informasi")+", "+rs.getString("ket_informasi")+"</td>"+
                                            "<td valign='top'>"+rs.getString("tiba_diruang_rawat")+"</td>"+
                                            "<td valign='top'>"+rs.getString("cara_masuk")+"</td>"+
                                            "<td valign='top'>"+rs.getString("rps")+"</td>"+
                                            "<td valign='top'>"+rs.getString("rpd")+"</td>"+
                                            "<td valign='top'>"+rs.getString("rpk")+"</td>"+
                                            "<td valign='top'>"+rs.getString("rpo")+"</td>"+
                                            "<td valign='top'>"+rs.getString("riwayat_pembedahan")+"</td>"+
                                            "<td valign='top'>"+rs.getString("riwayat_dirawat_dirs")+"</td>"+
                                            "<td valign='top'>"+rs.getString("alat_bantu_dipakai")+"</td>"+
                                            "<td valign='top'>"+rs.getString("riwayat_kehamilan")+", "+rs.getString("riwayat_kehamilan_perkiraan")+"</td>"+
                                            "<td valign='top'>"+rs.getString("riwayat_tranfusi")+"</td>"+
                                            "<td valign='top'>"+rs.getString("riwayat_alergi")+"</td>"+
                                            "<td valign='top'>"+rs.getString("riwayat_merokok")+"</td>"+
                                            "<td valign='top'>"+rs.getString("riwayat_merokok_jumlah")+"</td>"+
                                            "<td valign='top'>"+rs.getString("riwayat_alkohol")+"</td>"+
                                            "<td valign='top'>"+rs.getString("riwayat_alkohol_jumlah")+"</td>"+
                                            "<td valign='top'>"+rs.getString("riwayat_narkoba")+"</td>"+
                                            "<td valign='top'>"+rs.getString("riwayat_olahraga")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_mental")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_keadaan_umum")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_gcs")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_td")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_nadi")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_rr")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_suhu")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_spo2")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_bb")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_tb")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_susunan_kepala")+", "+rs.getString("pemeriksaan_susunan_kepala_keterangan")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_susunan_wajah")+", "+rs.getString("pemeriksaan_susunan_wajah_keterangan")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_susunan_leher")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_susunan_kejang")+", "+rs.getString("pemeriksaan_susunan_kejang_keterangan")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_susunan_sensorik")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_kardiovaskuler_pulsasi")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_kardiovaskuler_sirkulasi")+", "+rs.getString("pemeriksaan_kardiovaskuler_sirkulasi_keterangan")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_kardiovaskuler_denyut_nadi")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_respirasi_retraksi")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_respirasi_pola_nafas")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_respirasi_suara_nafas")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_respirasi_batuk")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_respirasi_volume_pernafasan")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_respirasi_jenis_pernafasan")+", "+rs.getString("pemeriksaan_respirasi_jenis_pernafasan_keterangan")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_respirasi_irama_nafas")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_gastrointestinal_mulut")+", "+rs.getString("pemeriksaan_gastrointestinal_mulut_keterangan")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_gastrointestinal_lidah")+", "+rs.getString("pemeriksaan_gastrointestinal_lidah_keterangan")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_gastrointestinal_gigi")+", "+rs.getString("pemeriksaan_gastrointestinal_gigi_keterangan")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_gastrointestinal_tenggorokan")+", "+rs.getString("pemeriksaan_gastrointestinal_tenggorokan_keterangan")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_gastrointestinal_abdomen")+", "+rs.getString("pemeriksaan_gastrointestinal_abdomen_keterangan")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_gastrointestinal_peistatik_usus")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_gastrointestinal_anus")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_neurologi_sensorik")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_neurologi_pengelihatan")+", "+rs.getString("pemeriksaan_neurologi_pengelihatan_keterangan")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_neurologi_alat_bantu_penglihatan")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_neurologi_motorik")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_neurologi_pendengaran")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_neurologi_bicara")+", "+rs.getString("pemeriksaan_neurologi_bicara_keterangan")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_neurologi_kekuatan_otot")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_integument_kulit")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_integument_warnakulit")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_integument_turgor")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_integument_dekubitas")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_muskuloskletal_oedema")+", "+rs.getString("pemeriksaan_muskuloskletal_oedema_keterangan")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_muskuloskletal_pergerakan_sendi")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_muskuloskletal_kekauatan_otot")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_muskuloskletal_fraktur")+", "+rs.getString("pemeriksaan_muskuloskletal_fraktur_keterangan")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_muskuloskletal_nyeri_sendi")+", "+rs.getString("pemeriksaan_muskuloskletal_nyeri_sendi_keterangan")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_eliminasi_bab_frekuensi_jumlah")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_eliminasi_bab_frekuensi_durasi")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_eliminasi_bab_konsistensi")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_eliminasi_bab_warna")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_eliminasi_bak_frekuensi_jumlah")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_eliminasi_bak_frekuensi_durasi")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_eliminasi_bak_warna")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pemeriksaan_eliminasi_bak_lainlain")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pola_aktifitas_mandi")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pola_aktifitas_makanminum")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pola_aktifitas_berpakaian")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pola_aktifitas_eliminasi")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pola_aktifitas_berpindah")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pola_nutrisi_porsi_makan")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pola_nutrisi_frekuesi_makan")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pola_nutrisi_jenis_makanan")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pola_tidur_lama_tidur")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pola_tidur_gangguan")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pengkajian_fungsi_kemampuan_sehari")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pengkajian_fungsi_berjalan")+", "+rs.getString("pengkajian_fungsi_berjalan_keterangan")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pengkajian_fungsi_aktifitas")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pengkajian_fungsi_ambulasi")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pengkajian_fungsi_ekstrimitas_atas")+", "+rs.getString("pengkajian_fungsi_ekstrimitas_atas_keterangan")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pengkajian_fungsi_ekstrimitas_bawah")+", "+rs.getString("pengkajian_fungsi_ekstrimitas_bawah_keterangan")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pengkajian_fungsi_menggenggam")+", "+rs.getString("pengkajian_fungsi_menggenggam_keterangan")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pengkajian_fungsi_koordinasi")+", "+rs.getString("pengkajian_fungsi_koordinasi_keterangan")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pengkajian_fungsi_kesimpulan")+"</td>"+
                                            "<td valign='top'>"+rs.getString("riwayat_psiko_kondisi_psiko")+"</td>"+
                                            "<td valign='top'>"+rs.getString("riwayat_psiko_perilaku")+", "+rs.getString("riwayat_psiko_perilaku_keterangan")+"</td>"+
                                            "<td valign='top'>"+rs.getString("riwayat_psiko_gangguan_jiwa")+"</td>"+
                                            "<td valign='top'>"+rs.getString("riwayat_psiko_hubungan_keluarga")+"</td>"+
                                            "<td valign='top'>"+rs.getString("agama")+"</td>"+
                                            "<td valign='top'>"+rs.getString("riwayat_psiko_tinggal")+", "+rs.getString("riwayat_psiko_tinggal_keterangan")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pekerjaan")+"</td>"+
                                            "<td valign='top'>"+rs.getString("png_jawab")+"</td>"+
                                            "<td valign='top'>"+rs.getString("riwayat_psiko_nilai_kepercayaan")+", "+rs.getString("riwayat_psiko_nilai_kepercayaan_keterangan")+"</td>"+
                                            "<td valign='top'>"+rs.getString("nama_bahasa")+"</td>"+
                                            "<td valign='top'>"+rs.getString("pnd")+"</td>"+
                                            "<td valign='top'>"+rs.getString("riwayat_psiko_pendidikan_pj")+"</td>"+
                                            "<td valign='top'>"+rs.getString("riwayat_psiko_edukasi_diberikan")+", "+rs.getString("riwayat_psiko_edukasi_diberikan_keterangan")+"</td>"+
                                            "<td valign='top'>"+rs.getString("penilaian_nyeri")+"</td>"+
                                            "<td valign='top'>"+rs.getString("penilaian_nyeri_penyebab")+", "+rs.getString("penilaian_nyeri_ket_penyebab")+"</td>"+
                                            "<td valign='top'>"+rs.getString("penilaian_nyeri_kualitas")+", "+rs.getString("penilaian_nyeri_ket_kualitas")+"</td>"+
                                            "<td valign='top'>"+rs.getString("penilaian_nyeri_lokasi")+"</td>"+
                                            "<td valign='top'>"+rs.getString("penilaian_nyeri_menyebar")+"</td>"+
                                            "<td valign='top'>"+rs.getString("penilaian_nyeri_skala")+"</td>"+
                                            "<td valign='top'>"+rs.getString("penilaian_nyeri_waktu")+"</td>"+
                                            "<td valign='top'>"+rs.getString("penilaian_nyeri_hilang")+", "+rs.getString("penilaian_nyeri_ket_hilang")+"</td>"+
                                            "<td valign='top'>"+rs.getString("penilaian_nyeri_diberitahukan_dokter")+", "+rs.getString("penilaian_nyeri_jam_diberitahukan_dokter")+"</td>"+
                                            "<td valign='top'>"+rs.getString("penilaian_jatuhmorse_skala1")+"</td>"+
                                            "<td valign='top'>"+rs.getString("penilaian_jatuhmorse_nilai1")+"</td>"+
                                            "<td valign='top'>"+rs.getString("penilaian_jatuhmorse_skala2")+"</td>"+
                                            "<td valign='top'>"+rs.getString("penilaian_jatuhmorse_nilai2")+"</td>"+
                                            "<td valign='top'>"+rs.getString("penilaian_jatuhmorse_skala3")+"</td>"+
                                            "<td valign='top'>"+rs.getString("penilaian_jatuhmorse_nilai3")+"</td>"+
                                            "<td valign='top'>"+rs.getString("penilaian_jatuhmorse_skala4")+"</td>"+
                                            "<td valign='top'>"+rs.getString("penilaian_jatuhmorse_nilai4")+"</td>"+
                                            "<td valign='top'>"+rs.getString("penilaian_jatuhmorse_skala5")+"</td>"+
                                            "<td valign='top'>"+rs.getString("penilaian_jatuhmorse_nilai5")+"</td>"+
                                            "<td valign='top'>"+rs.getString("penilaian_jatuhmorse_skala6")+"</td>"+
                                            "<td valign='top'>"+rs.getString("penilaian_jatuhmorse_nilai6")+"</td>"+
                                            "<td valign='top'>"+rs.getString("penilaian_jatuhmorse_totalnilai")+"</td>"+
                                            "<td valign='top'>"+rs.getString("penilaian_jatuhsydney_skala1")+"</td>"+
                                            "<td valign='top'>"+rs.getString("penilaian_jatuhsydney_nilai1")+"</td>"+
                                            "<td valign='top'>"+rs.getString("penilaian_jatuhsydney_skala2")+"</td>"+
                                            "<td valign='top'>"+rs.getString("penilaian_jatuhsydney_nilai2")+"</td>"+
                                            "<td valign='top'>"+rs.getString("penilaian_jatuhsydney_skala3")+"</td>"+
                                            "<td valign='top'>"+rs.getString("penilaian_jatuhsydney_nilai3")+"</td>"+
                                            "<td valign='top'>"+rs.getString("penilaian_jatuhsydney_skala4")+"</td>"+
                                            "<td valign='top'>"+rs.getString("penilaian_jatuhsydney_nilai4")+"</td>"+
                                            "<td valign='top'>"+rs.getString("penilaian_jatuhsydney_skala5")+"</td>"+
                                            "<td valign='top'>"+rs.getString("penilaian_jatuhsydney_nilai5")+"</td>"+
                                            "<td valign='top'>"+rs.getString("penilaian_jatuhsydney_skala6")+"</td>"+
                                            "<td valign='top'>"+rs.getString("penilaian_jatuhsydney_nilai6")+"</td>"+
                                            "<td valign='top'>"+rs.getString("penilaian_jatuhsydney_skala7")+"</td>"+
                                            "<td valign='top'>"+rs.getString("penilaian_jatuhsydney_nilai7")+"</td>"+
                                            "<td valign='top'>"+rs.getString("penilaian_jatuhsydney_skala8")+"</td>"+
                                            "<td valign='top'>"+rs.getString("penilaian_jatuhsydney_nilai8")+"</td>"+
                                            "<td valign='top'>"+rs.getString("penilaian_jatuhsydney_skala9")+"</td>"+
                                            "<td valign='top'>"+rs.getString("penilaian_jatuhsydney_nilai9")+"</td>"+
                                            "<td valign='top'>"+rs.getString("penilaian_jatuhsydney_skala10")+"</td>"+
                                            "<td valign='top'>"+rs.getString("penilaian_jatuhsydney_nilai10")+"</td>"+
                                            "<td valign='top'>"+rs.getString("penilaian_jatuhsydney_skala11")+"</td>"+
                                            "<td valign='top'>"+rs.getString("penilaian_jatuhsydney_nilai11")+"</td>"+
                                            "<td valign='top'>"+rs.getString("penilaian_jatuhsydney_totalnilai")+"</td>"+
                                            "<td valign='top'>"+rs.getString("skrining_gizi1")+"</td>"+
                                            "<td valign='top'>"+rs.getString("nilai_gizi1")+"</td>"+
                                            "<td valign='top'>"+rs.getString("skrining_gizi2")+"</td>"+
                                            "<td valign='top'>"+rs.getString("nilai_gizi2")+"</td>"+
                                            "<td valign='top'>"+rs.getString("nilai_total_gizi")+"</td>"+
                                            "<td valign='top'>"+rs.getString("skrining_gizi_diagnosa_khusus")+"</td>"+
                                            "<td valign='top'>"+rs.getString("skrining_gizi_ket_diagnosa_khusus")+"</td>"+
                                            "<td valign='top'>"+rs.getString("skrining_gizi_diketahui_dietisen")+"</td>"+
                                            "<td valign='top'>"+rs.getString("skrining_gizi_jam_diketahui_dietisen")+"</td>"+
                                            "<td valign='top'>"+rs.getString("rencana")+"</td>"+
                                        "</tr>"
                                    );
                                }
                                f = new File("RMPenilaianAwalKeperawatanRanap.wps");            
                                bw = new BufferedWriter(new FileWriter(f));            
                                bw.write("<html>"+
                                            "<head><link href=\"file2.css\" rel=\"stylesheet\" type=\"text/css\" /></head>"+
                                            "<body>"+
                                                "<table width='18500px' border='0' align='center' cellpadding='3px' cellspacing='0' class='tbl_form'>"+
                                                    htmlContent.toString()+
                                                "</table>"+
                                            "</body>"+                   
                                         "</html>"
                                );

                                bw.close();                         
                                Desktop.getDesktop().browse(f.toURI());
                            break;
                        case "Laporan 3 (CSV)":
                                htmlContent = new StringBuilder();
                                htmlContent.append(                             
                                    "\"No.Rawat\";\"No.RM\";\"Nama Pasien\";\"Tgl.Lahir\";\"J.K.\";\"NIP Pengkaji 1\";\"Nama Pengkaji 1\";\"NIP Pengkaji 2\";\"Nama Pengkaji 2\";\"Kode DPJP\";\"Nama DPJP\";\"Tgl.Asuhan\";\"Macam Kasus\";\"Anamnesis\";\"Tiba Di Ruang Rawat\";\"Cara Masuk\";\"Riwayat Penyakit Saat Ini\";\"Riwayat Penyakit Dahulu\";\"Riwayat Penyakit Keluarga\";\"Riwayat Penggunaan Obat\";\"Riwayat Pembedahan\";\"Riwayat Dirawat Di RS\";\"Alat Bantu Yang Dipakai\";\"Dalam Keadaan Hamil/Sedang Menyusui\";\"Riwayat Transfusi Darah\";\"Riwayat Alergi\";\"Merokok\";\"Batang/Hari\";\"Alkohol\";\"Gelas/Hari\";\"Obat Tidur\";\"Olah Raga\";\"Kesadaran Mental\";\"Keadaan Umum\";\"GCS(E,V,M)\";\"TD(mmHg)\";\"Nadi(x/menit)\";\"RR(x/menit)\";\"Suhu(°C)\";\"SpO2(%)\";\"BB(Kg)\";\"TB(cm)\";\"Kepala\";\"Wajah\";\"Leher\";\"Kejang\";\"Sensorik\";\"Pulsasi\";\"Sirkulasi\";\"Denyut Nadi\";\"Retraksi\";\"Pola Nafas\";\"Suara Nafas\";\"Batuk & Sekresi\";\"Volume\";\"Jenis Pernafasaan\";\"Irama\";\"Mulut\";\"Lidah\";\"Gigi\";\"Tenggorokan\";\"Abdomen\";\"Peistatik Usus\";\"Anus\";\"Sensorik\";\"Penglihatan\";\"Alat Bantu Penglihatan\";\"Motorik\";\"Pendengaran\";\"Bicara\";\"Otot\";\"Kulit\";\"Warna Kulit\";\"Turgor\";\"Resiko Decubitas\";\"Oedema\";\"Pergerakan Sendi\";\"Kekuatan Otot\";\"Fraktur\";\"Nyeri Sendi\";\"Frekuensi BAB\";\"x/\";\"Konsistensi BAB\";\"Warna BAB\";\"Frekuensi BAK\";\"x/\";\"Warna BAK\";\"Lain-lain BAK\";\"Mandi\";\"Makan/Minum\";\"Berpakaian\";\"Eliminasi\";\"Berpindah\";\"Porsi Makan\";\"Frekuensi Makan\";\"Jenis Makanan\";\"Lama Tidur\";\"Gangguan Tidur\";\"a. Aktifitas Sehari-hari\";\"b. Berjalan\";\"c. Aktifitas\";\"d. Alat Ambulasi\";\"e. Ekstremitas Atas\";\"f. Ekstremitas Bawah\";\"g. Kemampuan Menggenggam\";\"h. Kemampuan Koordinasi\";\"i. Kesimpulan Gangguan Fungsi\";\"a. Kondisi Psikologis\";\"b. Adakah Perilaku\";\"c. Gangguan Jiwa di Masa Lalu\";\"d. Hubungan Pasien\";\"e. Agama\";\"f. Tinggal Dengan\";\"g. Pekerjaan\";\"h. Pembayaran\";\"i. Nilai-nilai Kepercayaan\";\"j. Bahasa Sehari-hari\";\"k. Pendidikan Pasien\";\"l. Pendidikan P.J.\";\"m. Edukasi Diberikan Kepada\";\"Nyeri\";\"Penyebab Nyeri\";\"Kualitas Nyeri\";\"Lokasi Nyeri\";\"Nyeri Menyebar\";\"Skala Nyeri\";\"Waktu / Durasi\";\"Nyeri Hilang Bila\";\"Diberitahukan Pada Dokter\";\"Skala Morse 1\";\"N.M. 1\";\"Skala Morse 2\";\"N.M. 2\";\"Skala Morse 3\";\"N.M. 3\";\"Skala Morse 4\";\"N.M. 4\";\"Skala Morse 5\";\"N.M. 5\";\"Skala Morse 6\";\"N.M. 6\";\"T.M.\";\"Skala Sydney 1\";\"N.S. 1\";\"Skala Sydney 2\";\"N.S. 2\";\"Skala Sydney 3\";\"N.S. 3\";\"Skala Sydney 4\";\"N.S. 4\";\"Skala Sydney 5\";\"N.S. 5\";\"Skala Sydney 6\";\"N.S. 6\";\"Skala Sydney 7\";\"N.S. 7\";\"Skala Sydney 8\";\"N.S. 8\";\"Skala Sydney 9\";\"N.S. 9\";\"Skala Sydney 10\";\"N.S. 10\";\"Skala Sydney 11\";\"N.S. 11\";\"T.S.\";\"1. Apakah ada penurunan BB yang tidak diinginkan selama 6 bulan terakhir ?\";\"Skor 1\";\"2. Apakah asupan makan berkurang karena tidak nafsu makan ?\";\"Skor 2\";\"Total Skor\";\"Pasien dengan diagnosis khusus\";\"Keterangan Diagnosa Khusus\";\"Sudah dibaca dan diketahui oleh Dietisen\";\"Jam Dibaca Dietisen\";\"Rencana Keperawatan Lainnya\"\n"
                                ); 
                                while(rs.next()){
                                    htmlContent.append(
                                        "\""+rs.getString("no_rawat")+"\";\""+rs.getString("no_rkm_medis")+"\";\""+rs.getString("nm_pasien")+"\";\""+rs.getString("tgl_lahir")+"\";\""+rs.getString("jk")+"\";\""+rs.getString("nip1")+"\";\""+rs.getString("pengkaji1")+"\";\""+rs.getString("nip2")+"\";\""+rs.getString("pengkaji2")+"\";\""+rs.getString("kd_dokter")+"\";\""+rs.getString("nm_dokter")+"\";\""+rs.getString("tanggal")+"\";\""+rs.getString("kasus_trauma")+"\";\""+rs.getString("informasi")+", "+rs.getString("ket_informasi")+"\";\""+rs.getString("tiba_diruang_rawat")+"\";\""+rs.getString("cara_masuk")+"\";\""+rs.getString("rps")+"\";\""+rs.getString("rpd")+"\";\""+rs.getString("rpk")+"\";\""+rs.getString("rpo")+"\";\""+rs.getString("riwayat_pembedahan")+"\";\""+rs.getString("riwayat_dirawat_dirs")+"\";\""+rs.getString("alat_bantu_dipakai")+"\";\""+rs.getString("riwayat_kehamilan")+", "+rs.getString("riwayat_kehamilan_perkiraan")+"\";\""+rs.getString("riwayat_tranfusi")+"\";\""+rs.getString("riwayat_alergi")+"\";\""+rs.getString("riwayat_merokok")+"\";\""+rs.getString("riwayat_merokok_jumlah")+"\";\""+rs.getString("riwayat_alkohol")+"\";\""+rs.getString("riwayat_alkohol_jumlah")+"\";\""+rs.getString("riwayat_narkoba")+"\";\""+rs.getString("riwayat_olahraga")+"\";\""+rs.getString("pemeriksaan_mental")+"\";\""+rs.getString("pemeriksaan_keadaan_umum")+"\";\""+rs.getString("pemeriksaan_gcs")+"\";\""+rs.getString("pemeriksaan_td")+"\";\""+rs.getString("pemeriksaan_nadi")+"\";\""+rs.getString("pemeriksaan_rr")+"\";\""+rs.getString("pemeriksaan_suhu")+"\";\""+rs.getString("pemeriksaan_spo2")+"\";\""+rs.getString("pemeriksaan_bb")+"\";\""+rs.getString("pemeriksaan_tb")+"\";\""+rs.getString("pemeriksaan_susunan_kepala")+", "+rs.getString("pemeriksaan_susunan_kepala_keterangan")+"\";\""+rs.getString("pemeriksaan_susunan_wajah")+", "+rs.getString("pemeriksaan_susunan_wajah_keterangan")+"\";\""+rs.getString("pemeriksaan_susunan_leher")+"\";\""+rs.getString("pemeriksaan_susunan_kejang")+", "+rs.getString("pemeriksaan_susunan_kejang_keterangan")+"\";\""+rs.getString("pemeriksaan_susunan_sensorik")+"\";\""+rs.getString("pemeriksaan_kardiovaskuler_pulsasi")+"\";\""+rs.getString("pemeriksaan_kardiovaskuler_sirkulasi")+", "+rs.getString("pemeriksaan_kardiovaskuler_sirkulasi_keterangan")+"\";\""+rs.getString("pemeriksaan_kardiovaskuler_denyut_nadi")+"\";\""+rs.getString("pemeriksaan_respirasi_retraksi")+"\";\""+rs.getString("pemeriksaan_respirasi_pola_nafas")+"\";\""+rs.getString("pemeriksaan_respirasi_suara_nafas")+"\";\""+rs.getString("pemeriksaan_respirasi_batuk")+"\";\""+rs.getString("pemeriksaan_respirasi_volume_pernafasan")+"\";\""+rs.getString("pemeriksaan_respirasi_jenis_pernafasan")+", "+rs.getString("pemeriksaan_respirasi_jenis_pernafasan_keterangan")+"\";\""+rs.getString("pemeriksaan_respirasi_irama_nafas")+"\";\""+rs.getString("pemeriksaan_gastrointestinal_mulut")+", "+rs.getString("pemeriksaan_gastrointestinal_mulut_keterangan")+"\";\""+rs.getString("pemeriksaan_gastrointestinal_lidah")+", "+rs.getString("pemeriksaan_gastrointestinal_lidah_keterangan")+"\";\""+rs.getString("pemeriksaan_gastrointestinal_gigi")+", "+rs.getString("pemeriksaan_gastrointestinal_gigi_keterangan")+"\";\""+rs.getString("pemeriksaan_gastrointestinal_tenggorokan")+", "+rs.getString("pemeriksaan_gastrointestinal_tenggorokan_keterangan")+"\";\""+rs.getString("pemeriksaan_gastrointestinal_abdomen")+", "+rs.getString("pemeriksaan_gastrointestinal_abdomen_keterangan")+"\";\""+rs.getString("pemeriksaan_gastrointestinal_peistatik_usus")+"\";\""+rs.getString("pemeriksaan_gastrointestinal_anus")+"\";\""+rs.getString("pemeriksaan_neurologi_sensorik")+"\";\""+rs.getString("pemeriksaan_neurologi_pengelihatan")+", "+rs.getString("pemeriksaan_neurologi_pengelihatan_keterangan")+"\";\""+rs.getString("pemeriksaan_neurologi_alat_bantu_penglihatan")+"\";\""+rs.getString("pemeriksaan_neurologi_motorik")+"\";\""+rs.getString("pemeriksaan_neurologi_pendengaran")+"\";\""+rs.getString("pemeriksaan_neurologi_bicara")+", "+rs.getString("pemeriksaan_neurologi_bicara_keterangan")+"\";\""+rs.getString("pemeriksaan_neurologi_kekuatan_otot")+"\";\""+rs.getString("pemeriksaan_integument_kulit")+"\";\""+rs.getString("pemeriksaan_integument_warnakulit")+"\";\""+rs.getString("pemeriksaan_integument_turgor")+"\";\""+rs.getString("pemeriksaan_integument_dekubitas")+"\";\""+rs.getString("pemeriksaan_muskuloskletal_oedema")+", "+rs.getString("pemeriksaan_muskuloskletal_oedema_keterangan")+"\";\""+rs.getString("pemeriksaan_muskuloskletal_pergerakan_sendi")+"\";\""+rs.getString("pemeriksaan_muskuloskletal_kekauatan_otot")+"\";\""+rs.getString("pemeriksaan_muskuloskletal_fraktur")+", "+rs.getString("pemeriksaan_muskuloskletal_fraktur_keterangan")+"\";\""+rs.getString("pemeriksaan_muskuloskletal_nyeri_sendi")+", "+rs.getString("pemeriksaan_muskuloskletal_nyeri_sendi_keterangan")+"\";\""+rs.getString("pemeriksaan_eliminasi_bab_frekuensi_jumlah")+"\";\""+rs.getString("pemeriksaan_eliminasi_bab_frekuensi_durasi")+"\";\""+rs.getString("pemeriksaan_eliminasi_bab_konsistensi")+"\";\""+rs.getString("pemeriksaan_eliminasi_bab_warna")+"\";\""+rs.getString("pemeriksaan_eliminasi_bak_frekuensi_jumlah")+"\";\""+rs.getString("pemeriksaan_eliminasi_bak_frekuensi_durasi")+"\";\""+rs.getString("pemeriksaan_eliminasi_bak_warna")+"\";\""+rs.getString("pemeriksaan_eliminasi_bak_lainlain")+"\";\""+rs.getString("pola_aktifitas_mandi")+"\";\""+rs.getString("pola_aktifitas_makanminum")+"\";\""+rs.getString("pola_aktifitas_berpakaian")+"\";\""+rs.getString("pola_aktifitas_eliminasi")+"\";\""+rs.getString("pola_aktifitas_berpindah")+"\";\""+rs.getString("pola_nutrisi_porsi_makan")+"\";\""+rs.getString("pola_nutrisi_frekuesi_makan")+"\";\""+rs.getString("pola_nutrisi_jenis_makanan")+"\";\""+rs.getString("pola_tidur_lama_tidur")+"\";\""+rs.getString("pola_tidur_gangguan")+"\";\""+rs.getString("pengkajian_fungsi_kemampuan_sehari")+"\";\""+rs.getString("pengkajian_fungsi_berjalan")+", "+rs.getString("pengkajian_fungsi_berjalan_keterangan")+"\";\""+rs.getString("pengkajian_fungsi_aktifitas")+"\";\""+rs.getString("pengkajian_fungsi_ambulasi")+"\";\""+rs.getString("pengkajian_fungsi_ekstrimitas_atas")+", "+rs.getString("pengkajian_fungsi_ekstrimitas_atas_keterangan")+"\";\""+rs.getString("pengkajian_fungsi_ekstrimitas_bawah")+", "+rs.getString("pengkajian_fungsi_ekstrimitas_bawah_keterangan")+"\";\""+rs.getString("pengkajian_fungsi_menggenggam")+", "+rs.getString("pengkajian_fungsi_menggenggam_keterangan")+"\";\""+rs.getString("pengkajian_fungsi_koordinasi")+", "+rs.getString("pengkajian_fungsi_koordinasi_keterangan")+"\";\""+rs.getString("pengkajian_fungsi_kesimpulan")+"\";\""+rs.getString("riwayat_psiko_kondisi_psiko")+"\";\""+rs.getString("riwayat_psiko_perilaku")+", "+rs.getString("riwayat_psiko_perilaku_keterangan")+"\";\""+rs.getString("riwayat_psiko_gangguan_jiwa")+"\";\""+rs.getString("riwayat_psiko_hubungan_keluarga")+"\";\""+rs.getString("agama")+"\";\""+rs.getString("riwayat_psiko_tinggal")+", "+rs.getString("riwayat_psiko_tinggal_keterangan")+"\";\""+rs.getString("pekerjaan")+"\";\""+rs.getString("png_jawab")+"\";\""+rs.getString("riwayat_psiko_nilai_kepercayaan")+", "+rs.getString("riwayat_psiko_nilai_kepercayaan_keterangan")+"\";\""+rs.getString("nama_bahasa")+"\";\""+rs.getString("pnd")+"\";\""+rs.getString("riwayat_psiko_pendidikan_pj")+"\";\""+rs.getString("riwayat_psiko_edukasi_diberikan")+", "+rs.getString("riwayat_psiko_edukasi_diberikan_keterangan")+"\";\""+rs.getString("penilaian_nyeri")+"\";\""+rs.getString("penilaian_nyeri_penyebab")+", "+rs.getString("penilaian_nyeri_ket_penyebab")+"\";\""+rs.getString("penilaian_nyeri_kualitas")+", "+rs.getString("penilaian_nyeri_ket_kualitas")+"\";\""+rs.getString("penilaian_nyeri_lokasi")+"\";\""+rs.getString("penilaian_nyeri_menyebar")+"\";\""+rs.getString("penilaian_nyeri_skala")+"\";\""+rs.getString("penilaian_nyeri_waktu")+"\";\""+rs.getString("penilaian_nyeri_hilang")+", "+rs.getString("penilaian_nyeri_ket_hilang")+"\";\""+rs.getString("penilaian_nyeri_diberitahukan_dokter")+", "+rs.getString("penilaian_nyeri_jam_diberitahukan_dokter")+"\";\""+rs.getString("penilaian_jatuhmorse_skala1")+"\";\""+rs.getString("penilaian_jatuhmorse_nilai1")+"\";\""+rs.getString("penilaian_jatuhmorse_skala2")+"\";\""+rs.getString("penilaian_jatuhmorse_nilai2")+"\";\""+rs.getString("penilaian_jatuhmorse_skala3")+"\";\""+rs.getString("penilaian_jatuhmorse_nilai3")+"\";\""+rs.getString("penilaian_jatuhmorse_skala4")+"\";\""+rs.getString("penilaian_jatuhmorse_nilai4")+"\";\""+rs.getString("penilaian_jatuhmorse_skala5")+"\";\""+rs.getString("penilaian_jatuhmorse_nilai5")+"\";\""+rs.getString("penilaian_jatuhmorse_skala6")+"\";\""+rs.getString("penilaian_jatuhmorse_nilai6")+"\";\""+rs.getString("penilaian_jatuhmorse_totalnilai")+"\";\""+rs.getString("penilaian_jatuhsydney_skala1")+"\";\""+rs.getString("penilaian_jatuhsydney_nilai1")+"\";\""+rs.getString("penilaian_jatuhsydney_skala2")+"\";\""+rs.getString("penilaian_jatuhsydney_nilai2")+"\";\""+rs.getString("penilaian_jatuhsydney_skala3")+"\";\""+rs.getString("penilaian_jatuhsydney_nilai3")+"\";\""+rs.getString("penilaian_jatuhsydney_skala4")+"\";\""+rs.getString("penilaian_jatuhsydney_nilai4")+"\";\""+rs.getString("penilaian_jatuhsydney_skala5")+"\";\""+rs.getString("penilaian_jatuhsydney_nilai5")+"\";\""+rs.getString("penilaian_jatuhsydney_skala6")+"\";\""+rs.getString("penilaian_jatuhsydney_nilai6")+"\";\""+rs.getString("penilaian_jatuhsydney_skala7")+"\";\""+rs.getString("penilaian_jatuhsydney_nilai7")+"\";\""+rs.getString("penilaian_jatuhsydney_skala8")+"\";\""+rs.getString("penilaian_jatuhsydney_nilai8")+"\";\""+rs.getString("penilaian_jatuhsydney_skala9")+"\";\""+rs.getString("penilaian_jatuhsydney_nilai9")+"\";\""+rs.getString("penilaian_jatuhsydney_skala10")+"\";\""+rs.getString("penilaian_jatuhsydney_nilai10")+"\";\""+rs.getString("penilaian_jatuhsydney_skala11")+"\";\""+rs.getString("penilaian_jatuhsydney_nilai11")+"\";\""+rs.getString("penilaian_jatuhsydney_totalnilai")+"\";\""+rs.getString("skrining_gizi1")+"\";\""+rs.getString("nilai_gizi1")+"\";\""+rs.getString("skrining_gizi2")+"\";\""+rs.getString("nilai_gizi2")+"\";\""+rs.getString("nilai_total_gizi")+"\";\""+rs.getString("skrining_gizi_diagnosa_khusus")+"\";\""+rs.getString("skrining_gizi_ket_diagnosa_khusus")+"\";\""+rs.getString("skrining_gizi_diketahui_dietisen")+"\";\""+rs.getString("skrining_gizi_jam_diketahui_dietisen")+"\";\""+rs.getString("rencana")+"\"\n"
                                    );
                                }
                                f = new File("RMPenilaianAwalKeperawatanRanap.csv");            
                                bw = new BufferedWriter(new FileWriter(f));            
                                bw.write(htmlContent.toString());

                                bw.close();                         
                                Desktop.getDesktop().browse(f.toURI());
                            break; 
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
                ChkAccor.setSelected(true);
                isMenu();
                getMasalah();
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
                    ChkAccor.setSelected(true);
                    isMenu();
                    getMasalah();
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

    private void TabRawatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TabRawatMouseClicked
        if(TabRawat.getSelectedIndex()==1){
            tampil();
        }
    }//GEN-LAST:event_TabRawatMouseClicked

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        try {
            if(Valid.daysOld("./cache/masalahkeperawatan.iyem")<8){
                tampilMasalah2();
            }else{
                tampilMasalah();
            }
        } catch (Exception e) {
        }
        
        try {
            if(Valid.daysOld("./cache/rencanakeperawatan.iyem")>=7){
                tampilRencana();
            }
        } catch (Exception e) {
        }
    }//GEN-LAST:event_formWindowOpened

    private void ChkAccorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChkAccorActionPerformed
        if(tbObat.getSelectedRow()!= -1){
            isMenu();
        }else{
            ChkAccor.setSelected(false);
            JOptionPane.showMessageDialog(null,"Maaf, silahkan pilih data yang mau ditampilkan...!!!!");
        }
    }//GEN-LAST:event_ChkAccorActionPerformed

    private void BtnPrint1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnPrint1ActionPerformed
        if(tbObat.getSelectedRow()>-1){
            Map<String, Object> param = new HashMap<>();    
            param.put("namars",akses.getnamars());
            param.put("alamatrs",akses.getalamatrs());
            param.put("kotars",akses.getkabupatenrs());
            param.put("propinsirs",akses.getpropinsirs());
            param.put("kontakrs",akses.getkontakrs());
            param.put("emailrs",akses.getemailrs());          
            param.put("logo",Sequel.cariGambar("select setting.logo from setting")); 
            param.put("nyeri",Sequel.cariGambar("select gambar.nyeri from gambar")); 
            finger=Sequel.cariIsi("select sha1(sidikjari.sidikjari) from sidikjari inner join pegawai on pegawai.id=sidikjari.id where pegawai.nik=?",tbObat.getValueAt(tbObat.getSelectedRow(),5).toString());
            param.put("finger","Dikeluarkan di "+akses.getnamars()+", Kabupaten/Kota "+akses.getkabupatenrs()+"\nDitandatangani secara elektronik oleh "+tbObat.getValueAt(tbObat.getSelectedRow(),6).toString()+"\nID "+(finger.equals("")?tbObat.getValueAt(tbObat.getSelectedRow(),5).toString():finger)+"\n"+Valid.SetTgl3(tbObat.getValueAt(tbObat.getSelectedRow(),11).toString())); 
            
            try {
                masalahkeperawatan="";
                ps=koneksi.prepareStatement(
                    "select master_masalah_keperawatan.kode_masalah,master_masalah_keperawatan.nama_masalah from master_masalah_keperawatan "+
                    "inner join penilaian_awal_keperawatan_ranap_masalah on penilaian_awal_keperawatan_ranap_masalah.kode_masalah=master_masalah_keperawatan.kode_masalah "+
                    "where penilaian_awal_keperawatan_ranap_masalah.no_rawat=? order by penilaian_awal_keperawatan_ranap_masalah.kode_masalah");
                try {
                    ps.setString(1,tbObat.getValueAt(tbObat.getSelectedRow(),0).toString());
                    rs=ps.executeQuery();
                    while(rs.next()){
                        masalahkeperawatan=rs.getString("nama_masalah")+", "+masalahkeperawatan;
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
            param.put("masalah",masalahkeperawatan);  
            try {
                masalahkeperawatan="";
                ps=koneksi.prepareStatement(
                    "select master_rencana_keperawatan.kode_rencana,master_rencana_keperawatan.rencana_keperawatan from master_rencana_keperawatan "+
                    "inner join penilaian_awal_keperawatan_ranap_rencana on penilaian_awal_keperawatan_ranap_rencana.kode_rencana=master_rencana_keperawatan.kode_rencana "+
                    "where penilaian_awal_keperawatan_ranap_rencana.no_rawat=? order by penilaian_awal_keperawatan_ranap_rencana.kode_rencana");
                try {
                    ps.setString(1,tbObat.getValueAt(tbObat.getSelectedRow(),0).toString());
                    rs=ps.executeQuery();
                    while(rs.next()){
                        masalahkeperawatan=rs.getString("rencana_keperawatan")+", "+masalahkeperawatan;
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
            param.put("rencana",masalahkeperawatan); 
            Valid.MyReportqry("rptCetakPenilaianAwalKeperawatanRanap.jasper","report","::[ Laporan Penilaian Awal Keperawatan Rawat Inap ]::",
                "select penilaian_awal_keperawatan_ranap_kemoterapi.no_rawat,penilaian_awal_keperawatan_ranap_kemoterapi.tanggal,penilaian_awal_keperawatan_ranap_kemoterapi.informasi,penilaian_awal_keperawatan_ranap_kemoterapi.ket_informasi,penilaian_awal_keperawatan_ranap_kemoterapi.tiba_diruang_rawat,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.kasus_trauma,penilaian_awal_keperawatan_ranap_kemoterapi.cara_masuk,penilaian_awal_keperawatan_ranap_kemoterapi.rps,penilaian_awal_keperawatan_ranap_kemoterapi.rpd,penilaian_awal_keperawatan_ranap_kemoterapi.rpk,penilaian_awal_keperawatan_ranap_kemoterapi.rpo,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.riwayat_pembedahan,penilaian_awal_keperawatan_ranap_kemoterapi.riwayat_dirawat_dirs,penilaian_awal_keperawatan_ranap_kemoterapi.alat_bantu_dipakai,penilaian_awal_keperawatan_ranap_kemoterapi.riwayat_kehamilan,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.riwayat_kehamilan_perkiraan,penilaian_awal_keperawatan_ranap_kemoterapi.riwayat_tranfusi,penilaian_awal_keperawatan_ranap_kemoterapi.riwayat_alergi,penilaian_awal_keperawatan_ranap_kemoterapi.riwayat_merokok,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.riwayat_merokok_jumlah,penilaian_awal_keperawatan_ranap_kemoterapi.riwayat_alkohol,penilaian_awal_keperawatan_ranap_kemoterapi.riwayat_alkohol_jumlah,penilaian_awal_keperawatan_ranap_kemoterapi.riwayat_narkoba,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.riwayat_olahraga,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_mental,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_keadaan_umum,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_gcs,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_td,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_nadi,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_rr,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_suhu,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_spo2,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_bb,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_tb,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_susunan_kepala,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_susunan_kepala_keterangan,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_susunan_wajah,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_susunan_wajah_keterangan,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_susunan_leher,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_susunan_kejang,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_susunan_kejang_keterangan,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_susunan_sensorik,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_kardiovaskuler_denyut_nadi,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_kardiovaskuler_sirkulasi,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_kardiovaskuler_sirkulasi_keterangan,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_kardiovaskuler_pulsasi,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_respirasi_pola_nafas,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_respirasi_retraksi,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_respirasi_suara_nafas,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_respirasi_volume_pernafasan,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_respirasi_jenis_pernafasan,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_respirasi_jenis_pernafasan_keterangan,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_respirasi_irama_nafas,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_respirasi_batuk,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_gastrointestinal_mulut,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_gastrointestinal_mulut_keterangan,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_gastrointestinal_gigi,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_gastrointestinal_gigi_keterangan,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_gastrointestinal_lidah,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_gastrointestinal_lidah_keterangan,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_gastrointestinal_tenggorokan,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_gastrointestinal_tenggorokan_keterangan,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_gastrointestinal_abdomen,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_gastrointestinal_abdomen_keterangan,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_gastrointestinal_peistatik_usus,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_gastrointestinal_anus,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_neurologi_pengelihatan,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_neurologi_pengelihatan_keterangan,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_neurologi_alat_bantu_penglihatan,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_neurologi_pendengaran,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_neurologi_bicara,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_neurologi_bicara_keterangan,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_neurologi_sensorik,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_neurologi_motorik,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_neurologi_kekuatan_otot,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_integument_warnakulit,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_integument_turgor,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_integument_kulit,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_integument_dekubitas,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_muskuloskletal_pergerakan_sendi,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_muskuloskletal_kekauatan_otot,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_muskuloskletal_nyeri_sendi,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_muskuloskletal_nyeri_sendi_keterangan,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_muskuloskletal_oedema,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_muskuloskletal_oedema_keterangan,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_muskuloskletal_fraktur,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_muskuloskletal_fraktur_keterangan,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_eliminasi_bab_frekuensi_jumlah,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_eliminasi_bab_frekuensi_durasi,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_eliminasi_bab_konsistensi,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_eliminasi_bab_warna,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_eliminasi_bak_frekuensi_jumlah,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_eliminasi_bak_frekuensi_durasi,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_eliminasi_bak_warna,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_eliminasi_bak_lainlain,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.pola_aktifitas_makanminum,penilaian_awal_keperawatan_ranap_kemoterapi.pola_aktifitas_mandi,penilaian_awal_keperawatan_ranap_kemoterapi.pola_aktifitas_eliminasi,penilaian_awal_keperawatan_ranap_kemoterapi.pola_aktifitas_berpakaian,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.pola_aktifitas_berpindah,penilaian_awal_keperawatan_ranap_kemoterapi.pola_nutrisi_frekuesi_makan,penilaian_awal_keperawatan_ranap_kemoterapi.pola_nutrisi_jenis_makanan,penilaian_awal_keperawatan_ranap_kemoterapi.pola_nutrisi_porsi_makan,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.pola_tidur_lama_tidur,penilaian_awal_keperawatan_ranap_kemoterapi.pola_tidur_gangguan,penilaian_awal_keperawatan_ranap_kemoterapi.pengkajian_fungsi_kemampuan_sehari,penilaian_awal_keperawatan_ranap_kemoterapi.pengkajian_fungsi_aktifitas,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.pengkajian_fungsi_berjalan,penilaian_awal_keperawatan_ranap_kemoterapi.pengkajian_fungsi_berjalan_keterangan,penilaian_awal_keperawatan_ranap_kemoterapi.pengkajian_fungsi_ambulasi,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.pengkajian_fungsi_ekstrimitas_atas,penilaian_awal_keperawatan_ranap_kemoterapi.pengkajian_fungsi_ekstrimitas_atas_keterangan,penilaian_awal_keperawatan_ranap_kemoterapi.pengkajian_fungsi_ekstrimitas_bawah,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.pengkajian_fungsi_ekstrimitas_bawah_keterangan,penilaian_awal_keperawatan_ranap_kemoterapi.pengkajian_fungsi_menggenggam,penilaian_awal_keperawatan_ranap_kemoterapi.pengkajian_fungsi_menggenggam_keterangan,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.pengkajian_fungsi_koordinasi,penilaian_awal_keperawatan_ranap_kemoterapi.pengkajian_fungsi_koordinasi_keterangan,penilaian_awal_keperawatan_ranap_kemoterapi.pengkajian_fungsi_kesimpulan,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.riwayat_psiko_kondisi_psiko,penilaian_awal_keperawatan_ranap_kemoterapi.riwayat_psiko_gangguan_jiwa,penilaian_awal_keperawatan_ranap_kemoterapi.riwayat_psiko_perilaku,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.riwayat_psiko_perilaku_keterangan,penilaian_awal_keperawatan_ranap_kemoterapi.riwayat_psiko_hubungan_keluarga,penilaian_awal_keperawatan_ranap_kemoterapi.riwayat_psiko_tinggal,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.riwayat_psiko_tinggal_keterangan,penilaian_awal_keperawatan_ranap_kemoterapi.riwayat_psiko_nilai_kepercayaan,penilaian_awal_keperawatan_ranap_kemoterapi.riwayat_psiko_nilai_kepercayaan_keterangan,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.riwayat_psiko_pendidikan_pj,penilaian_awal_keperawatan_ranap_kemoterapi.riwayat_psiko_edukasi_diberikan,penilaian_awal_keperawatan_ranap_kemoterapi.riwayat_psiko_edukasi_diberikan_keterangan,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_nyeri,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_nyeri_penyebab,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_nyeri_ket_penyebab,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_nyeri_kualitas,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_nyeri_ket_kualitas,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_nyeri_lokasi,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_nyeri_menyebar,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_nyeri_skala,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_nyeri_waktu,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_nyeri_hilang,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_nyeri_ket_hilang,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_nyeri_diberitahukan_dokter,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_nyeri_jam_diberitahukan_dokter,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhmorse_skala1,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhmorse_nilai1,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhmorse_skala2,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhmorse_nilai2,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhmorse_skala3,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhmorse_nilai3,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhmorse_skala4,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhmorse_nilai4,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhmorse_skala5,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhmorse_nilai5,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhmorse_skala6,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhmorse_nilai6,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhmorse_totalnilai,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhsydney_skala1,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhsydney_nilai1,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhsydney_skala2,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhsydney_nilai2,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhsydney_skala3,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhsydney_nilai3,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhsydney_skala4,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhsydney_nilai4,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhsydney_skala5,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhsydney_nilai5,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhsydney_skala6,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhsydney_nilai6,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhsydney_skala7,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhsydney_nilai7,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhsydney_skala8,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhsydney_nilai8,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhsydney_skala9,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhsydney_nilai9,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhsydney_skala10,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhsydney_nilai10,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhsydney_skala11,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhsydney_nilai11,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhsydney_totalnilai,penilaian_awal_keperawatan_ranap_kemoterapi.skrining_gizi1,penilaian_awal_keperawatan_ranap_kemoterapi.nilai_gizi1,penilaian_awal_keperawatan_ranap_kemoterapi.skrining_gizi2,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.nilai_gizi2,penilaian_awal_keperawatan_ranap_kemoterapi.nilai_total_gizi,penilaian_awal_keperawatan_ranap_kemoterapi.skrining_gizi_diagnosa_khusus,penilaian_awal_keperawatan_ranap_kemoterapi.skrining_gizi_ket_diagnosa_khusus,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.skrining_gizi_diketahui_dietisen,penilaian_awal_keperawatan_ranap_kemoterapi.skrining_gizi_jam_diketahui_dietisen,penilaian_awal_keperawatan_ranap_kemoterapi.rencana,penilaian_awal_keperawatan_ranap_kemoterapi.nip1,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.nip2,penilaian_awal_keperawatan_ranap_kemoterapi.kd_dokter,pasien.tgl_lahir,pasien.jk,pengkaji1.nama as pengkaji1,pengkaji2.nama as pengkaji2,dokter.nm_dokter,"+
                "reg_periksa.no_rkm_medis,pasien.nm_pasien,pasien.agama,pasien.pekerjaan,pasien.pnd,penjab.png_jawab,bahasa_pasien.nama_bahasa "+
                "from reg_periksa inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                "inner join penilaian_awal_keperawatan_ranap_kemoterapi on reg_periksa.no_rawat=penilaian_awal_keperawatan_ranap_kemoterapi.no_rawat "+
                "inner join petugas as pengkaji1 on penilaian_awal_keperawatan_ranap_kemoterapi.nip1=pengkaji1.nip "+
                "inner join petugas as pengkaji2 on penilaian_awal_keperawatan_ranap_kemoterapi.nip2=pengkaji2.nip "+
                "inner join dokter on penilaian_awal_keperawatan_ranap_kemoterapi.kd_dokter=dokter.kd_dokter "+
                "inner join bahasa_pasien on bahasa_pasien.id=pasien.bahasa_pasien "+
                "inner join penjab on penjab.kd_pj=reg_periksa.kd_pj where reg_periksa.no_rawat='"+tbObat.getValueAt(tbObat.getSelectedRow(),0).toString()+"'",param);
            
            Valid.MyReportqry("rptCetakPenilaianAwalKeperawatanRanap2.jasper","report","::[ Laporan Penilaian Awal Keperawatan Rawat Inap ]::",
                "select penilaian_awal_keperawatan_ranap_kemoterapi.no_rawat,penilaian_awal_keperawatan_ranap_kemoterapi.tanggal,penilaian_awal_keperawatan_ranap_kemoterapi.informasi,penilaian_awal_keperawatan_ranap_kemoterapi.ket_informasi,penilaian_awal_keperawatan_ranap_kemoterapi.tiba_diruang_rawat,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.kasus_trauma,penilaian_awal_keperawatan_ranap_kemoterapi.cara_masuk,penilaian_awal_keperawatan_ranap_kemoterapi.rps,penilaian_awal_keperawatan_ranap_kemoterapi.rpd,penilaian_awal_keperawatan_ranap_kemoterapi.rpk,penilaian_awal_keperawatan_ranap_kemoterapi.rpo,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.riwayat_pembedahan,penilaian_awal_keperawatan_ranap_kemoterapi.riwayat_dirawat_dirs,penilaian_awal_keperawatan_ranap_kemoterapi.alat_bantu_dipakai,penilaian_awal_keperawatan_ranap_kemoterapi.riwayat_kehamilan,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.riwayat_kehamilan_perkiraan,penilaian_awal_keperawatan_ranap_kemoterapi.riwayat_tranfusi,penilaian_awal_keperawatan_ranap_kemoterapi.riwayat_alergi,penilaian_awal_keperawatan_ranap_kemoterapi.riwayat_merokok,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.riwayat_merokok_jumlah,penilaian_awal_keperawatan_ranap_kemoterapi.riwayat_alkohol,penilaian_awal_keperawatan_ranap_kemoterapi.riwayat_alkohol_jumlah,penilaian_awal_keperawatan_ranap_kemoterapi.riwayat_narkoba,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.riwayat_olahraga,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_mental,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_keadaan_umum,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_gcs,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_td,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_nadi,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_rr,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_suhu,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_spo2,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_bb,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_tb,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_susunan_kepala,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_susunan_kepala_keterangan,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_susunan_wajah,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_susunan_wajah_keterangan,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_susunan_leher,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_susunan_kejang,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_susunan_kejang_keterangan,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_susunan_sensorik,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_kardiovaskuler_denyut_nadi,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_kardiovaskuler_sirkulasi,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_kardiovaskuler_sirkulasi_keterangan,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_kardiovaskuler_pulsasi,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_respirasi_pola_nafas,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_respirasi_retraksi,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_respirasi_suara_nafas,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_respirasi_volume_pernafasan,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_respirasi_jenis_pernafasan,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_respirasi_jenis_pernafasan_keterangan,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_respirasi_irama_nafas,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_respirasi_batuk,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_gastrointestinal_mulut,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_gastrointestinal_mulut_keterangan,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_gastrointestinal_gigi,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_gastrointestinal_gigi_keterangan,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_gastrointestinal_lidah,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_gastrointestinal_lidah_keterangan,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_gastrointestinal_tenggorokan,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_gastrointestinal_tenggorokan_keterangan,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_gastrointestinal_abdomen,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_gastrointestinal_abdomen_keterangan,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_gastrointestinal_peistatik_usus,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_gastrointestinal_anus,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_neurologi_pengelihatan,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_neurologi_pengelihatan_keterangan,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_neurologi_alat_bantu_penglihatan,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_neurologi_pendengaran,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_neurologi_bicara,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_neurologi_bicara_keterangan,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_neurologi_sensorik,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_neurologi_motorik,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_neurologi_kekuatan_otot,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_integument_warnakulit,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_integument_turgor,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_integument_kulit,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_integument_dekubitas,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_muskuloskletal_pergerakan_sendi,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_muskuloskletal_kekauatan_otot,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_muskuloskletal_nyeri_sendi,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_muskuloskletal_nyeri_sendi_keterangan,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_muskuloskletal_oedema,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_muskuloskletal_oedema_keterangan,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_muskuloskletal_fraktur,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_muskuloskletal_fraktur_keterangan,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_eliminasi_bab_frekuensi_jumlah,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_eliminasi_bab_frekuensi_durasi,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_eliminasi_bab_konsistensi,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_eliminasi_bab_warna,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_eliminasi_bak_frekuensi_jumlah,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_eliminasi_bak_frekuensi_durasi,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_eliminasi_bak_warna,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_eliminasi_bak_lainlain,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.pola_aktifitas_makanminum,penilaian_awal_keperawatan_ranap_kemoterapi.pola_aktifitas_mandi,penilaian_awal_keperawatan_ranap_kemoterapi.pola_aktifitas_eliminasi,penilaian_awal_keperawatan_ranap_kemoterapi.pola_aktifitas_berpakaian,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.pola_aktifitas_berpindah,penilaian_awal_keperawatan_ranap_kemoterapi.pola_nutrisi_frekuesi_makan,penilaian_awal_keperawatan_ranap_kemoterapi.pola_nutrisi_jenis_makanan,penilaian_awal_keperawatan_ranap_kemoterapi.pola_nutrisi_porsi_makan,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.pola_tidur_lama_tidur,penilaian_awal_keperawatan_ranap_kemoterapi.pola_tidur_gangguan,penilaian_awal_keperawatan_ranap_kemoterapi.pengkajian_fungsi_kemampuan_sehari,penilaian_awal_keperawatan_ranap_kemoterapi.pengkajian_fungsi_aktifitas,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.pengkajian_fungsi_berjalan,penilaian_awal_keperawatan_ranap_kemoterapi.pengkajian_fungsi_berjalan_keterangan,penilaian_awal_keperawatan_ranap_kemoterapi.pengkajian_fungsi_ambulasi,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.pengkajian_fungsi_ekstrimitas_atas,penilaian_awal_keperawatan_ranap_kemoterapi.pengkajian_fungsi_ekstrimitas_atas_keterangan,penilaian_awal_keperawatan_ranap_kemoterapi.pengkajian_fungsi_ekstrimitas_bawah,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.pengkajian_fungsi_ekstrimitas_bawah_keterangan,penilaian_awal_keperawatan_ranap_kemoterapi.pengkajian_fungsi_menggenggam,penilaian_awal_keperawatan_ranap_kemoterapi.pengkajian_fungsi_menggenggam_keterangan,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.pengkajian_fungsi_koordinasi,penilaian_awal_keperawatan_ranap_kemoterapi.pengkajian_fungsi_koordinasi_keterangan,penilaian_awal_keperawatan_ranap_kemoterapi.pengkajian_fungsi_kesimpulan,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.riwayat_psiko_kondisi_psiko,penilaian_awal_keperawatan_ranap_kemoterapi.riwayat_psiko_gangguan_jiwa,penilaian_awal_keperawatan_ranap_kemoterapi.riwayat_psiko_perilaku,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.riwayat_psiko_perilaku_keterangan,penilaian_awal_keperawatan_ranap_kemoterapi.riwayat_psiko_hubungan_keluarga,penilaian_awal_keperawatan_ranap_kemoterapi.riwayat_psiko_tinggal,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.riwayat_psiko_tinggal_keterangan,penilaian_awal_keperawatan_ranap_kemoterapi.riwayat_psiko_nilai_kepercayaan,penilaian_awal_keperawatan_ranap_kemoterapi.riwayat_psiko_nilai_kepercayaan_keterangan,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.riwayat_psiko_pendidikan_pj,penilaian_awal_keperawatan_ranap_kemoterapi.riwayat_psiko_edukasi_diberikan,penilaian_awal_keperawatan_ranap_kemoterapi.riwayat_psiko_edukasi_diberikan_keterangan,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_nyeri,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_nyeri_penyebab,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_nyeri_ket_penyebab,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_nyeri_kualitas,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_nyeri_ket_kualitas,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_nyeri_lokasi,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_nyeri_menyebar,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_nyeri_skala,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_nyeri_waktu,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_nyeri_hilang,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_nyeri_ket_hilang,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_nyeri_diberitahukan_dokter,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_nyeri_jam_diberitahukan_dokter,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhmorse_skala1,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhmorse_nilai1,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhmorse_skala2,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhmorse_nilai2,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhmorse_skala3,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhmorse_nilai3,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhmorse_skala4,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhmorse_nilai4,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhmorse_skala5,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhmorse_nilai5,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhmorse_skala6,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhmorse_nilai6,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhmorse_totalnilai,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhsydney_skala1,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhsydney_nilai1,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhsydney_skala2,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhsydney_nilai2,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhsydney_skala3,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhsydney_nilai3,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhsydney_skala4,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhsydney_nilai4,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhsydney_skala5,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhsydney_nilai5,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhsydney_skala6,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhsydney_nilai6,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhsydney_skala7,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhsydney_nilai7,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhsydney_skala8,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhsydney_nilai8,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhsydney_skala9,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhsydney_nilai9,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhsydney_skala10,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhsydney_nilai10,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhsydney_skala11,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhsydney_nilai11,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhsydney_totalnilai,penilaian_awal_keperawatan_ranap_kemoterapi.skrining_gizi1,penilaian_awal_keperawatan_ranap_kemoterapi.nilai_gizi1,penilaian_awal_keperawatan_ranap_kemoterapi.skrining_gizi2,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.nilai_gizi2,penilaian_awal_keperawatan_ranap_kemoterapi.nilai_total_gizi,penilaian_awal_keperawatan_ranap_kemoterapi.skrining_gizi_diagnosa_khusus,penilaian_awal_keperawatan_ranap_kemoterapi.skrining_gizi_ket_diagnosa_khusus,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.skrining_gizi_diketahui_dietisen,penilaian_awal_keperawatan_ranap_kemoterapi.skrining_gizi_jam_diketahui_dietisen,penilaian_awal_keperawatan_ranap_kemoterapi.rencana,penilaian_awal_keperawatan_ranap_kemoterapi.nip1,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.nip2,penilaian_awal_keperawatan_ranap_kemoterapi.kd_dokter,pasien.tgl_lahir,pasien.jk,pengkaji1.nama as pengkaji1,pengkaji2.nama as pengkaji2,dokter.nm_dokter,"+
                "reg_periksa.no_rkm_medis,pasien.nm_pasien,pasien.agama,pasien.pekerjaan,pasien.pnd,penjab.png_jawab,bahasa_pasien.nama_bahasa "+
                "from reg_periksa inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                "inner join penilaian_awal_keperawatan_ranap_kemoterapi on reg_periksa.no_rawat=penilaian_awal_keperawatan_ranap_kemoterapi.no_rawat "+
                "inner join petugas as pengkaji1 on penilaian_awal_keperawatan_ranap_kemoterapi.nip1=pengkaji1.nip "+
                "inner join petugas as pengkaji2 on penilaian_awal_keperawatan_ranap_kemoterapi.nip2=pengkaji2.nip "+
                "inner join dokter on penilaian_awal_keperawatan_ranap_kemoterapi.kd_dokter=dokter.kd_dokter "+
                "inner join bahasa_pasien on bahasa_pasien.id=pasien.bahasa_pasien "+
                "inner join penjab on penjab.kd_pj=reg_periksa.kd_pj where reg_periksa.no_rawat='"+tbObat.getValueAt(tbObat.getSelectedRow(),0).toString()+"'",param);
        }else{
            JOptionPane.showMessageDialog(null,"Maaf, silahkan pilih data terlebih dahulu..!!!!");
        }  
    }//GEN-LAST:event_BtnPrint1ActionPerformed

    private void TglAsuhanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TglAsuhanKeyPressed
        Valid.pindah(evt,BtnDPJP,MacamKasus);
    }//GEN-LAST:event_TglAsuhanKeyPressed

    private void AnamnesisKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_AnamnesisKeyPressed
        Valid.pindah(evt,MacamKasus,KetAnamnesis);
    }//GEN-LAST:event_AnamnesisKeyPressed

    private void BtnPetugasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnPetugasKeyPressed
        Valid.pindah(evt,BtnSimpan,BtnPetugas2);
    }//GEN-LAST:event_BtnPetugasKeyPressed

    private void BtnPetugasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnPetugasActionPerformed
        i=1;
        petugas.isCek();
        petugas.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
        petugas.setLocationRelativeTo(internalFrame1);
        petugas.setAlwaysOnTop(false);
        petugas.setVisible(true);
    }//GEN-LAST:event_BtnPetugasActionPerformed

    private void KdPetugasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KdPetugasKeyPressed

    }//GEN-LAST:event_KdPetugasKeyPressed

    private void TNoRwKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TNoRwKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            isRawat();
        }else{
            Valid.pindah(evt,TCari,BtnPetugas);
        }
    }//GEN-LAST:event_TNoRwKeyPressed

    private void BtnPetugas2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnPetugas2ActionPerformed
        i=2;
        petugas.isCek();
        petugas.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
        petugas.setLocationRelativeTo(internalFrame1);
        petugas.setAlwaysOnTop(false);
        petugas.setVisible(true);
    }//GEN-LAST:event_BtnPetugas2ActionPerformed

    private void BtnPetugas2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnPetugas2KeyPressed
        Valid.pindah(evt,BtnPetugas,BtnDPJP);
    }//GEN-LAST:event_BtnPetugas2KeyPressed

    private void KdPetugas2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KdPetugas2KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_KdPetugas2KeyPressed

    private void KdDPJPKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KdDPJPKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_KdDPJPKeyPressed

    private void BtnDPJPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnDPJPActionPerformed
        dokter.isCek();
        dokter.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
        dokter.setLocationRelativeTo(internalFrame1);
        dokter.setAlwaysOnTop(false);
        dokter.setVisible(true);
    }//GEN-LAST:event_BtnDPJPActionPerformed

    private void BtnDPJPKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnDPJPKeyPressed
        Valid.pindah(evt,BtnPetugas2,MacamKasus);
    }//GEN-LAST:event_BtnDPJPKeyPressed

    private void TibadiRuangKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TibadiRuangKeyPressed
        Valid.pindah(evt,KetAnamnesis,CaraMasuk);
    }//GEN-LAST:event_TibadiRuangKeyPressed

    private void CaraMasukKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_CaraMasukKeyPressed
        Valid.pindah(evt,TibadiRuang,RPS);
    }//GEN-LAST:event_CaraMasukKeyPressed

    private void AlergiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_AlergiKeyPressed
        Valid.pindah(evt,RTranfusi,KebiasaanMerokok);
    }//GEN-LAST:event_AlergiKeyPressed

    private void RPSKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_RPSKeyPressed
        Valid.pindah2(evt,CaraMasuk,RPD);
    }//GEN-LAST:event_RPSKeyPressed

    private void RPKKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_RPKKeyPressed
        Valid.pindah2(evt,RPD,RPO);
    }//GEN-LAST:event_RPKKeyPressed

    private void RPDKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_RPDKeyPressed
        Valid.pindah2(evt,RPS,RPK);
    }//GEN-LAST:event_RPDKeyPressed

    private void RPOKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_RPOKeyPressed
        Valid.pindah2(evt,RPK,RPembedahan);
    }//GEN-LAST:event_RPOKeyPressed

    private void DetailRencanaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DetailRencanaKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_DetailRencanaKeyPressed

    private void MacamKasusKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_MacamKasusKeyPressed
        Valid.pindah(evt,BtnDPJP,Anamnesis);
    }//GEN-LAST:event_MacamKasusKeyPressed

    private void KetAnamnesisKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KetAnamnesisKeyPressed
        Valid.pindah(evt,Anamnesis,TibadiRuang);
    }//GEN-LAST:event_KetAnamnesisKeyPressed

    private void RDirawatRSKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_RDirawatRSKeyPressed
        Valid.pindah(evt,RPembedahan,AlatBantuDipakai);
    }//GEN-LAST:event_RDirawatRSKeyPressed

    private void RPembedahanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_RPembedahanKeyPressed
        Valid.pindah(evt,RPO,RDirawatRS);
    }//GEN-LAST:event_RPembedahanKeyPressed

    private void AlatBantuDipakaiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_AlatBantuDipakaiKeyPressed
        Valid.pindah(evt,RDirawatRS,SedangMenyusui);
    }//GEN-LAST:event_AlatBantuDipakaiKeyPressed

    private void SedangMenyusuiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SedangMenyusuiKeyPressed
        Valid.pindah(evt,AlatBantuDipakai,KetSedangMenyusui);
    }//GEN-LAST:event_SedangMenyusuiKeyPressed

    private void KetSedangMenyusuiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KetSedangMenyusuiKeyPressed
        Valid.pindah(evt,SedangMenyusui,RTranfusi);
    }//GEN-LAST:event_KetSedangMenyusuiKeyPressed

    private void RTranfusiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_RTranfusiKeyPressed
        Valid.pindah(evt,KetSedangMenyusui,Alergi);
    }//GEN-LAST:event_RTranfusiKeyPressed

    private void KebiasaanMerokokKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KebiasaanMerokokKeyPressed
        Valid.pindah(evt,Alergi,KebiasaanJumlahRokok);
    }//GEN-LAST:event_KebiasaanMerokokKeyPressed

    private void KebiasaanJumlahRokokKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KebiasaanJumlahRokokKeyPressed
        Valid.pindah(evt,KebiasaanMerokok,KebiasaanAlkohol);
    }//GEN-LAST:event_KebiasaanJumlahRokokKeyPressed

    private void KebiasaanAlkoholKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KebiasaanAlkoholKeyPressed
        Valid.pindah(evt,KebiasaanJumlahRokok,KebiasaanJumlahAlkohol);
    }//GEN-LAST:event_KebiasaanAlkoholKeyPressed

    private void KebiasaanJumlahAlkoholKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KebiasaanJumlahAlkoholKeyPressed
        Valid.pindah(evt,KebiasaanAlkohol,KebiasaanNarkoba);
    }//GEN-LAST:event_KebiasaanJumlahAlkoholKeyPressed

    private void KebiasaanNarkobaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KebiasaanNarkobaKeyPressed
        Valid.pindah(evt,KebiasaanJumlahAlkohol,OlahRaga);
    }//GEN-LAST:event_KebiasaanNarkobaKeyPressed

    private void OlahRagaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_OlahRagaKeyPressed
        Valid.pindah(evt,KebiasaanNarkoba,KesadaranMental);
    }//GEN-LAST:event_OlahRagaKeyPressed

    private void KesadaranMentalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KesadaranMentalKeyPressed
        Valid.pindah(evt,OlahRaga,KeadaanMentalUmum);
    }//GEN-LAST:event_KesadaranMentalKeyPressed

    private void KeadaanMentalUmumKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KeadaanMentalUmumKeyPressed
        Valid.pindah(evt,KesadaranMental,GCS);
    }//GEN-LAST:event_KeadaanMentalUmumKeyPressed

    private void GCSKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_GCSKeyPressed
        Valid.pindah(evt,KeadaanMentalUmum,TD);
    }//GEN-LAST:event_GCSKeyPressed

    private void TDKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TDKeyPressed
        Valid.pindah(evt,GCS,Nadi);
    }//GEN-LAST:event_TDKeyPressed

    private void NadiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_NadiKeyPressed
        Valid.pindah(evt,TD,RR);
    }//GEN-LAST:event_NadiKeyPressed

    private void RRKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_RRKeyPressed
        Valid.pindah(evt,Nadi,Suhu);
    }//GEN-LAST:event_RRKeyPressed

    private void SuhuKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SuhuKeyPressed
        Valid.pindah(evt,RR,SpO2);
    }//GEN-LAST:event_SuhuKeyPressed

    private void SpO2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SpO2KeyPressed
        Valid.pindah(evt,Suhu,BB);
    }//GEN-LAST:event_SpO2KeyPressed

    private void BBKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BBKeyPressed
        Valid.pindah(evt,SpO2,TB);
    }//GEN-LAST:event_BBKeyPressed

    private void TBKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TBKeyPressed
        Valid.pindah(evt,BB,SistemSarafKepala);
    }//GEN-LAST:event_TBKeyPressed

    private void SistemSarafKepalaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SistemSarafKepalaKeyPressed
        Valid.pindah(evt,TB,KetSistemSarafKepala);
    }//GEN-LAST:event_SistemSarafKepalaKeyPressed

    private void KetSistemSarafKepalaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KetSistemSarafKepalaKeyPressed
        Valid.pindah(evt,SistemSarafKepala,SistemSarafWajah);
    }//GEN-LAST:event_KetSistemSarafKepalaKeyPressed

    private void SistemSarafWajahKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SistemSarafWajahKeyPressed
        Valid.pindah(evt,KetSistemSarafKepala,KetSistemSarafWajah);
    }//GEN-LAST:event_SistemSarafWajahKeyPressed

    private void KetSistemSarafWajahKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KetSistemSarafWajahKeyPressed
        Valid.pindah(evt,SistemSarafWajah,SistemSarafLeher);
    }//GEN-LAST:event_KetSistemSarafWajahKeyPressed

    private void SistemSarafLeherKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SistemSarafLeherKeyPressed
        Valid.pindah(evt,KetSistemSarafWajah,SistemSarafKejang);
    }//GEN-LAST:event_SistemSarafLeherKeyPressed

    private void SistemSarafSensorikKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SistemSarafSensorikKeyPressed
        Valid.pindah(evt,KetSistemSarafKejang,KardiovaskularPulsasi);
    }//GEN-LAST:event_SistemSarafSensorikKeyPressed

    private void SistemSarafKejangKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SistemSarafKejangKeyPressed
        Valid.pindah(evt,SistemSarafLeher,KetSistemSarafKejang);
    }//GEN-LAST:event_SistemSarafKejangKeyPressed

    private void KetSistemSarafKejangKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KetSistemSarafKejangKeyPressed
        Valid.pindah(evt,SistemSarafKejang,SistemSarafSensorik);
    }//GEN-LAST:event_KetSistemSarafKejangKeyPressed

    private void KardiovaskularPulsasiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KardiovaskularPulsasiKeyPressed
        Valid.pindah(evt,SistemSarafSensorik,KardiovaskularSirkulasi);
    }//GEN-LAST:event_KardiovaskularPulsasiKeyPressed

    private void KardiovaskularSirkulasiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KardiovaskularSirkulasiKeyPressed
        Valid.pindah(evt,KardiovaskularPulsasi,KetKardiovaskularSirkulasi);
    }//GEN-LAST:event_KardiovaskularSirkulasiKeyPressed

    private void KetKardiovaskularSirkulasiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KetKardiovaskularSirkulasiKeyPressed
        Valid.pindah(evt,KardiovaskularSirkulasi,KardiovaskularDenyutNadi);
    }//GEN-LAST:event_KetKardiovaskularSirkulasiKeyPressed

    private void KardiovaskularDenyutNadiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KardiovaskularDenyutNadiKeyPressed
        Valid.pindah(evt,KetKardiovaskularSirkulasi,RespirasiRetraksi);
    }//GEN-LAST:event_KardiovaskularDenyutNadiKeyPressed

    private void RespirasiRetraksiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_RespirasiRetraksiKeyPressed
        Valid.pindah(evt,KardiovaskularDenyutNadi,RespirasiPolaNafas);
    }//GEN-LAST:event_RespirasiRetraksiKeyPressed

    private void RespirasiPolaNafasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_RespirasiPolaNafasKeyPressed
        Valid.pindah(evt,RespirasiRetraksi,RespirasiSuaraNafas);
    }//GEN-LAST:event_RespirasiPolaNafasKeyPressed

    private void RespirasiSuaraNafasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_RespirasiSuaraNafasKeyPressed
        Valid.pindah(evt,RespirasiPolaNafas,RespirasiBatuk);
    }//GEN-LAST:event_RespirasiSuaraNafasKeyPressed

    private void RespirasiIramaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_RespirasiIramaKeyPressed
        Valid.pindah(evt,KetRespirasiJenisPernafasan,GastrointestinalMulut);
    }//GEN-LAST:event_RespirasiIramaKeyPressed

    private void RespirasiVolumeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_RespirasiVolumeKeyPressed
        Valid.pindah(evt,RespirasiBatuk,RespirasiJenisPernafasan);
    }//GEN-LAST:event_RespirasiVolumeKeyPressed

    private void RespirasiJenisPernafasanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_RespirasiJenisPernafasanKeyPressed
        Valid.pindah(evt,RespirasiVolume,KetRespirasiJenisPernafasan);
    }//GEN-LAST:event_RespirasiJenisPernafasanKeyPressed

    private void KetRespirasiJenisPernafasanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KetRespirasiJenisPernafasanKeyPressed
        Valid.pindah(evt,RespirasiJenisPernafasan,RespirasiIrama);
    }//GEN-LAST:event_KetRespirasiJenisPernafasanKeyPressed

    private void RespirasiBatukKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_RespirasiBatukKeyPressed
        Valid.pindah(evt,RespirasiSuaraNafas,RespirasiVolume);
    }//GEN-LAST:event_RespirasiBatukKeyPressed

    private void GastrointestinalMulutKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_GastrointestinalMulutKeyPressed
        Valid.pindah(evt,RespirasiIrama,KetGastrointestinalMulut);
    }//GEN-LAST:event_GastrointestinalMulutKeyPressed

    private void KetGastrointestinalGigiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KetGastrointestinalGigiKeyPressed
        Valid.pindah(evt,GastrointestinalGigi,GastrointestinalTenggorakan);
    }//GEN-LAST:event_KetGastrointestinalGigiKeyPressed

    private void GastrointestinalAnusKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_GastrointestinalAnusKeyPressed
        Valid.pindah(evt,GastrointestinalUsus,NeurologiSensorik);
    }//GEN-LAST:event_GastrointestinalAnusKeyPressed

    private void GastrointestinalGigiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_GastrointestinalGigiKeyPressed
        Valid.pindah(evt,KetGastrointestinalLidah,KetGastrointestinalGigi);
    }//GEN-LAST:event_GastrointestinalGigiKeyPressed

    private void KetGastrointestinalMulutKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KetGastrointestinalMulutKeyPressed
        Valid.pindah(evt,GastrointestinalMulut,GastrointestinalLidah);
    }//GEN-LAST:event_KetGastrointestinalMulutKeyPressed

    private void GastrointestinalLidahKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_GastrointestinalLidahKeyPressed
        Valid.pindah(evt,KetGastrointestinalMulut,KetGastrointestinalLidah);
    }//GEN-LAST:event_GastrointestinalLidahKeyPressed

    private void KetGastrointestinalLidahKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KetGastrointestinalLidahKeyPressed
        Valid.pindah(evt,GastrointestinalLidah,GastrointestinalGigi);
    }//GEN-LAST:event_KetGastrointestinalLidahKeyPressed

    private void GastrointestinalTenggorakanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_GastrointestinalTenggorakanKeyPressed
        Valid.pindah(evt,KetGastrointestinalGigi,KetGastrointestinalTenggorakan);
    }//GEN-LAST:event_GastrointestinalTenggorakanKeyPressed

    private void KetGastrointestinalTenggorakanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KetGastrointestinalTenggorakanKeyPressed
        Valid.pindah(evt,GastrointestinalTenggorakan,GastrointestinalAbdomen);
    }//GEN-LAST:event_KetGastrointestinalTenggorakanKeyPressed

    private void GastrointestinalAbdomenKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_GastrointestinalAbdomenKeyPressed
        Valid.pindah(evt,KetGastrointestinalTenggorakan,KetGastrointestinalAbdomen);
    }//GEN-LAST:event_GastrointestinalAbdomenKeyPressed

    private void KetGastrointestinalAbdomenKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KetGastrointestinalAbdomenKeyPressed
        Valid.pindah(evt,GastrointestinalAbdomen,GastrointestinalUsus);
    }//GEN-LAST:event_KetGastrointestinalAbdomenKeyPressed

    private void GastrointestinalUsusKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_GastrointestinalUsusKeyPressed
        Valid.pindah(evt,KetGastrointestinalGigi,GastrointestinalAnus);
    }//GEN-LAST:event_GastrointestinalUsusKeyPressed

    private void NeurologiSensorikKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_NeurologiSensorikKeyPressed
        Valid.pindah(evt,GastrointestinalAnus,NeurologiPenglihatan);
    }//GEN-LAST:event_NeurologiSensorikKeyPressed

    private void NeurologiMotorikKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_NeurologiMotorikKeyPressed
        Valid.pindah(evt,NeurologiAlatBantuPenglihatan,NeurologiPendengaran);
    }//GEN-LAST:event_NeurologiMotorikKeyPressed

    private void NeurologiOtotKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_NeurologiOtotKeyPressed
        Valid.pindah(evt,KetNeurologiBicara,IntegumentKulit);
    }//GEN-LAST:event_NeurologiOtotKeyPressed

    private void NeurologiPenglihatanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_NeurologiPenglihatanKeyPressed
        Valid.pindah(evt,NeurologiSensorik,KetNeurologiPenglihatan);
    }//GEN-LAST:event_NeurologiPenglihatanKeyPressed

    private void KetNeurologiPenglihatanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KetNeurologiPenglihatanKeyPressed
        Valid.pindah(evt,NeurologiPenglihatan,NeurologiAlatBantuPenglihatan);
    }//GEN-LAST:event_KetNeurologiPenglihatanKeyPressed

    private void NeurologiAlatBantuPenglihatanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_NeurologiAlatBantuPenglihatanKeyPressed
        Valid.pindah(evt,KetNeurologiPenglihatan,NeurologiMotorik);
    }//GEN-LAST:event_NeurologiAlatBantuPenglihatanKeyPressed

    private void NeurologiPendengaranKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_NeurologiPendengaranKeyPressed
        Valid.pindah(evt,NeurologiMotorik,NeurologiBicara);
    }//GEN-LAST:event_NeurologiPendengaranKeyPressed

    private void NeurologiBicaraKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_NeurologiBicaraKeyPressed
        Valid.pindah(evt,NeurologiPendengaran,KetNeurologiBicara);
    }//GEN-LAST:event_NeurologiBicaraKeyPressed

    private void KetNeurologiBicaraKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KetNeurologiBicaraKeyPressed
        Valid.pindah(evt,NeurologiBicara,NeurologiOtot);
    }//GEN-LAST:event_KetNeurologiBicaraKeyPressed

    private void IntegumentKulitKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_IntegumentKulitKeyPressed
        Valid.pindah(evt,NeurologiOtot,IntegumentWarnaKulit);
    }//GEN-LAST:event_IntegumentKulitKeyPressed

    private void IntegumentWarnaKulitKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_IntegumentWarnaKulitKeyPressed
        Valid.pindah(evt,IntegumentKulit,IntegumentTurgor);
    }//GEN-LAST:event_IntegumentWarnaKulitKeyPressed

    private void IntegumentTurgorKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_IntegumentTurgorKeyPressed
        Valid.pindah(evt,IntegumentWarnaKulit,IntegumentDecubitus);
    }//GEN-LAST:event_IntegumentTurgorKeyPressed

    private void IntegumentDecubitusKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_IntegumentDecubitusKeyPressed
        Valid.pindah(evt,IntegumentTurgor,MuskuloskletalOedema);
    }//GEN-LAST:event_IntegumentDecubitusKeyPressed

    private void MuskuloskletalOedemaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_MuskuloskletalOedemaKeyPressed
        Valid.pindah(evt,IntegumentDecubitus,KetMuskuloskletalOedema);
    }//GEN-LAST:event_MuskuloskletalOedemaKeyPressed

    private void KetMuskuloskletalOedemaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KetMuskuloskletalOedemaKeyPressed
        Valid.pindah(evt,MuskuloskletalOedema,MuskuloskletalPegerakanSendi);
    }//GEN-LAST:event_KetMuskuloskletalOedemaKeyPressed

    private void KetMuskuloskletalFrakturKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KetMuskuloskletalFrakturKeyPressed
        Valid.pindah(evt,MuskuloskletalFraktur,MuskuloskletalNyeriSendi);
    }//GEN-LAST:event_KetMuskuloskletalFrakturKeyPressed

    private void MuskuloskletalFrakturKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_MuskuloskletalFrakturKeyPressed
        Valid.pindah(evt,MuskuloskletalKekuatanOtot,KetMuskuloskletalFraktur);
    }//GEN-LAST:event_MuskuloskletalFrakturKeyPressed

    private void MuskuloskletalNyeriSendiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_MuskuloskletalNyeriSendiKeyPressed
        Valid.pindah(evt,KetMuskuloskletalFraktur,KetMuskuloskletalNyeriSendi);
    }//GEN-LAST:event_MuskuloskletalNyeriSendiKeyPressed

    private void KetMuskuloskletalNyeriSendiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KetMuskuloskletalNyeriSendiKeyPressed
        Valid.pindah(evt,KetMuskuloskletalNyeriSendi,BAB);
    }//GEN-LAST:event_KetMuskuloskletalNyeriSendiKeyPressed

    private void MuskuloskletalPegerakanSendiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_MuskuloskletalPegerakanSendiKeyPressed
        Valid.pindah(evt,KetMuskuloskletalOedema,MuskuloskletalKekuatanOtot);
    }//GEN-LAST:event_MuskuloskletalPegerakanSendiKeyPressed

    private void MuskuloskletalKekuatanOtotKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_MuskuloskletalKekuatanOtotKeyPressed
        Valid.pindah(evt,MuskuloskletalPegerakanSendi,MuskuloskletalFraktur);
    }//GEN-LAST:event_MuskuloskletalKekuatanOtotKeyPressed

    private void KBABKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KBABKeyPressed
        Valid.pindah(evt,XBAB,WBAB);
    }//GEN-LAST:event_KBABKeyPressed

    private void BABKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BABKeyPressed
        Valid.pindah(evt,KetMuskuloskletalNyeriSendi,XBAB);
    }//GEN-LAST:event_BABKeyPressed

    private void XBABKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_XBABKeyPressed
        Valid.pindah(evt,BAB,KBAB);
    }//GEN-LAST:event_XBABKeyPressed

    private void WBABKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_WBABKeyPressed
        Valid.pindah(evt,KBAB,BAK);
    }//GEN-LAST:event_WBABKeyPressed

    private void BAKKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BAKKeyPressed
        Valid.pindah(evt,WBAB,XBAK);
    }//GEN-LAST:event_BAKKeyPressed

    private void XBAKKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_XBAKKeyPressed
        Valid.pindah(evt,BAK,WBAK);
    }//GEN-LAST:event_XBAKKeyPressed

    private void WBAKKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_WBAKKeyPressed
        Valid.pindah(evt,XBAK,LBAK);
    }//GEN-LAST:event_WBAKKeyPressed

    private void LBAKKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_LBAKKeyPressed
        Valid.pindah(evt,WBAK,PolaAktifitasMandi);
    }//GEN-LAST:event_LBAKKeyPressed

    private void PolaAktifitasEliminasiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PolaAktifitasEliminasiKeyPressed
        Valid.pindah(evt,PolaAktifitasBerpakaian,PolaAktifitasBerpindah);
    }//GEN-LAST:event_PolaAktifitasEliminasiKeyPressed

    private void PolaAktifitasMandiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PolaAktifitasMandiKeyPressed
        Valid.pindah(evt,LBAK,PolaAktifitasMakan);
    }//GEN-LAST:event_PolaAktifitasMandiKeyPressed

    private void PolaAktifitasMakanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PolaAktifitasMakanKeyPressed
        Valid.pindah(evt,PolaAktifitasMandi,PolaAktifitasBerpakaian);
    }//GEN-LAST:event_PolaAktifitasMakanKeyPressed

    private void PolaAktifitasBerpakaianKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PolaAktifitasBerpakaianKeyPressed
        Valid.pindah(evt,PolaAktifitasMakan,PolaAktifitasEliminasi);
    }//GEN-LAST:event_PolaAktifitasBerpakaianKeyPressed

    private void PolaAktifitasBerpindahKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PolaAktifitasBerpindahKeyPressed
        Valid.pindah(evt,PolaAktifitasEliminasi,PolaNutrisiPorsi);
    }//GEN-LAST:event_PolaAktifitasBerpindahKeyPressed

    private void PolaNutrisiPorsiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PolaNutrisiPorsiKeyPressed
        Valid.pindah(evt,PolaAktifitasBerpindah,PolaNutrisiFrekuensi);
    }//GEN-LAST:event_PolaNutrisiPorsiKeyPressed

    private void PolaNutrisiFrekuensiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PolaNutrisiFrekuensiKeyPressed
        Valid.pindah(evt,PolaNutrisiPorsi,PolaNutrisiJenis);
    }//GEN-LAST:event_PolaNutrisiFrekuensiKeyPressed

    private void PolaNutrisiJenisKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PolaNutrisiJenisKeyPressed
        Valid.pindah(evt,PolaNutrisiFrekuensi,PolaTidurLama);
    }//GEN-LAST:event_PolaNutrisiJenisKeyPressed

    private void PolaTidurLamaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PolaTidurLamaKeyPressed
        Valid.pindah(evt,PolaNutrisiJenis,PolaTidurGangguan);
    }//GEN-LAST:event_PolaTidurLamaKeyPressed

    private void PolaTidurGangguanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PolaTidurGangguanKeyPressed
        Valid.pindah(evt,PolaTidurLama,AktifitasSehari2);
    }//GEN-LAST:event_PolaTidurGangguanKeyPressed

    private void AktifitasSehari2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_AktifitasSehari2KeyPressed
        Valid.pindah(evt,PolaTidurGangguan,Berjalan);
    }//GEN-LAST:event_AktifitasSehari2KeyPressed

    private void BerjalanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BerjalanKeyPressed
        Valid.pindah(evt,AktifitasSehari2,KeteranganBerjalan);
    }//GEN-LAST:event_BerjalanKeyPressed

    private void KeteranganBerjalanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KeteranganBerjalanKeyPressed
        Valid.pindah(evt,Berjalan,Aktifitas);
    }//GEN-LAST:event_KeteranganBerjalanKeyPressed

    private void AktifitasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_AktifitasKeyPressed
        Valid.pindah(evt,KeteranganBerjalan,AlatAmbulasi);
    }//GEN-LAST:event_AktifitasKeyPressed

    private void AlatAmbulasiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_AlatAmbulasiKeyPressed
        Valid.pindah(evt,Aktifitas,EkstrimitasAtas);
    }//GEN-LAST:event_AlatAmbulasiKeyPressed

    private void EkstrimitasAtasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_EkstrimitasAtasKeyPressed
        Valid.pindah(evt,AlatAmbulasi,KeteranganEkstrimitasAtas);
    }//GEN-LAST:event_EkstrimitasAtasKeyPressed

    private void KeteranganEkstrimitasAtasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KeteranganEkstrimitasAtasKeyPressed
        Valid.pindah(evt,EkstrimitasAtas,EkstrimitasBawah);
    }//GEN-LAST:event_KeteranganEkstrimitasAtasKeyPressed

    private void EkstrimitasBawahKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_EkstrimitasBawahKeyPressed
        Valid.pindah(evt,KeteranganEkstrimitasAtas,KeteranganEkstrimitasBawah);
    }//GEN-LAST:event_EkstrimitasBawahKeyPressed

    private void KeteranganEkstrimitasBawahKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KeteranganEkstrimitasBawahKeyPressed
        Valid.pindah(evt,EkstrimitasBawah,KemampuanMenggenggam);
    }//GEN-LAST:event_KeteranganEkstrimitasBawahKeyPressed

    private void KemampuanMenggenggamKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KemampuanMenggenggamKeyPressed
        Valid.pindah(evt,KeteranganEkstrimitasBawah,KeteranganKemampuanMenggenggam);
    }//GEN-LAST:event_KemampuanMenggenggamKeyPressed

    private void KeteranganKemampuanMenggenggamKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KeteranganKemampuanMenggenggamKeyPressed
        Valid.pindah(evt,KemampuanMenggenggam,KemampuanKoordinasi);
    }//GEN-LAST:event_KeteranganKemampuanMenggenggamKeyPressed

    private void KemampuanKoordinasiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KemampuanKoordinasiKeyPressed
        Valid.pindah(evt,KeteranganKemampuanMenggenggam,KeteranganKemampuanKoordinasi);
    }//GEN-LAST:event_KemampuanKoordinasiKeyPressed

    private void KeteranganKemampuanKoordinasiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KeteranganKemampuanKoordinasiKeyPressed
        Valid.pindah(evt,KemampuanKoordinasi,KesimpulanGangguanFungsi);
    }//GEN-LAST:event_KeteranganKemampuanKoordinasiKeyPressed

    private void KesimpulanGangguanFungsiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KesimpulanGangguanFungsiKeyPressed
        Valid.pindah(evt,KeteranganKemampuanKoordinasi,KondisiPsikologis);
    }//GEN-LAST:event_KesimpulanGangguanFungsiKeyPressed

    private void KondisiPsikologisKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KondisiPsikologisKeyPressed
        Valid.pindah(evt,KesimpulanGangguanFungsi,AdakahPerilaku);
    }//GEN-LAST:event_KondisiPsikologisKeyPressed

    private void AdakahPerilakuKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_AdakahPerilakuKeyPressed
        Valid.pindah(evt,KondisiPsikologis,KeteranganAdakahPerilaku);
    }//GEN-LAST:event_AdakahPerilakuKeyPressed

    private void KeteranganAdakahPerilakuKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KeteranganAdakahPerilakuKeyPressed
        Valid.pindah(evt,AdakahPerilaku,GangguanJiwa);
    }//GEN-LAST:event_KeteranganAdakahPerilakuKeyPressed

    private void GangguanJiwaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_GangguanJiwaKeyPressed
        Valid.pindah(evt,KeteranganAdakahPerilaku,HubunganAnggotaKeluarga);
    }//GEN-LAST:event_GangguanJiwaKeyPressed

    private void HubunganAnggotaKeluargaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_HubunganAnggotaKeluargaKeyPressed
        Valid.pindah(evt,GangguanJiwa,TinggalDengan);
    }//GEN-LAST:event_HubunganAnggotaKeluargaKeyPressed

    private void TinggalDenganKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TinggalDenganKeyPressed
        Valid.pindah(evt,HubunganAnggotaKeluarga,KeteranganTinggalDengan);
    }//GEN-LAST:event_TinggalDenganKeyPressed

    private void KeteranganTinggalDenganKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KeteranganTinggalDenganKeyPressed
        Valid.pindah(evt,TinggalDengan,NilaiKepercayaan);
    }//GEN-LAST:event_KeteranganTinggalDenganKeyPressed

    private void NilaiKepercayaanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_NilaiKepercayaanKeyPressed
        Valid.pindah(evt,KeteranganTinggalDengan,KeteranganNilaiKepercayaan);
    }//GEN-LAST:event_NilaiKepercayaanKeyPressed

    private void KeteranganNilaiKepercayaanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KeteranganNilaiKepercayaanKeyPressed
        Valid.pindah(evt,NilaiKepercayaan,PendidikanPJ);
    }//GEN-LAST:event_KeteranganNilaiKepercayaanKeyPressed

    private void PendidikanPJKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PendidikanPJKeyPressed
        Valid.pindah(evt,KeteranganNilaiKepercayaan,EdukasiPsikolgis);
    }//GEN-LAST:event_PendidikanPJKeyPressed

    private void EdukasiPsikolgisKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_EdukasiPsikolgisKeyPressed
        Valid.pindah(evt,PendidikanPJ,KeteranganEdukasiPsikologis);
    }//GEN-LAST:event_EdukasiPsikolgisKeyPressed

    private void KeteranganEdukasiPsikologisKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KeteranganEdukasiPsikologisKeyPressed
        Valid.pindah(evt,EdukasiPsikolgis,Nyeri);
    }//GEN-LAST:event_KeteranganEdukasiPsikologisKeyPressed

    private void NyeriKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_NyeriKeyPressed
        Valid.pindah(evt,KeteranganEdukasiPsikologis,Provokes);
    }//GEN-LAST:event_NyeriKeyPressed

    private void ProvokesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ProvokesKeyPressed
        Valid.pindah(evt,Nyeri,KetProvokes);
    }//GEN-LAST:event_ProvokesKeyPressed

    private void KetProvokesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KetProvokesKeyPressed
        Valid.pindah(evt,Provokes,Quality);
    }//GEN-LAST:event_KetProvokesKeyPressed

    private void QualityKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_QualityKeyPressed
        Valid.pindah(evt,KetProvokes,KetQuality);
    }//GEN-LAST:event_QualityKeyPressed

    private void KetQualityKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KetQualityKeyPressed
        Valid.pindah(evt,Quality,Lokasi);
    }//GEN-LAST:event_KetQualityKeyPressed

    private void LokasiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_LokasiKeyPressed
        Valid.pindah(evt,KetQuality,Menyebar);
    }//GEN-LAST:event_LokasiKeyPressed

    private void MenyebarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_MenyebarKeyPressed
        Valid.pindah(evt,Lokasi,SkalaNyeri);
    }//GEN-LAST:event_MenyebarKeyPressed

    private void SkalaNyeriKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SkalaNyeriKeyPressed
        Valid.pindah(evt,Menyebar,Durasi);
    }//GEN-LAST:event_SkalaNyeriKeyPressed

    private void DurasiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DurasiKeyPressed
        Valid.pindah(evt,SkalaNyeri,NyeriHilang);
    }//GEN-LAST:event_DurasiKeyPressed

    private void NyeriHilangKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_NyeriHilangKeyPressed
        Valid.pindah(evt,Durasi,KetNyeri);
    }//GEN-LAST:event_NyeriHilangKeyPressed

    private void KetNyeriKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KetNyeriKeyPressed
        Valid.pindah(evt,NyeriHilang,PadaDokter);
    }//GEN-LAST:event_KetNyeriKeyPressed

    private void PadaDokterKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PadaDokterKeyPressed
        Valid.pindah(evt,KetNyeri,KetPadaDokter);
    }//GEN-LAST:event_PadaDokterKeyPressed

    private void KetPadaDokterKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KetPadaDokterKeyPressed
        Valid.pindah(evt,PadaDokter,SkalaResiko1);
    }//GEN-LAST:event_KetPadaDokterKeyPressed

    private void SkalaResiko1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_SkalaResiko1ItemStateChanged
        if(SkalaResiko1.getSelectedIndex()==0){
            NilaiResiko1.setText("0");
        }else{
            NilaiResiko1.setText("25");
        }
        isTotalResikoJatuh();
    }//GEN-LAST:event_SkalaResiko1ItemStateChanged

    private void SkalaResiko1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SkalaResiko1KeyPressed
        Valid.pindah(evt,KetPadaDokter,SkalaResiko2);
    }//GEN-LAST:event_SkalaResiko1KeyPressed

    private void SkalaResiko2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_SkalaResiko2ItemStateChanged
        if(SkalaResiko2.getSelectedIndex()==0){
            NilaiResiko2.setText("0");
        }else{
            NilaiResiko2.setText("15");
        }
        isTotalResikoJatuh();
    }//GEN-LAST:event_SkalaResiko2ItemStateChanged

    private void SkalaResiko2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SkalaResiko2KeyPressed
        Valid.pindah(evt,SkalaResiko1,SkalaResiko3);
    }//GEN-LAST:event_SkalaResiko2KeyPressed

    private void SkalaResiko3ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_SkalaResiko3ItemStateChanged
        if(SkalaResiko3.getSelectedIndex()==0){
            NilaiResiko3.setText("0");
        }else if(SkalaResiko3.getSelectedIndex()==1){
            NilaiResiko3.setText("15");
        }else{
            NilaiResiko3.setText("30");
        }
        isTotalResikoJatuh();
    }//GEN-LAST:event_SkalaResiko3ItemStateChanged

    private void SkalaResiko3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SkalaResiko3KeyPressed
        Valid.pindah(evt,SkalaResiko2,SkalaResiko4);
    }//GEN-LAST:event_SkalaResiko3KeyPressed

    private void SkalaResiko4ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_SkalaResiko4ItemStateChanged
        if(SkalaResiko4.getSelectedIndex()==0){
            NilaiResiko4.setText("0");
        }else{
            NilaiResiko4.setText("20");
        }
        isTotalResikoJatuh();
    }//GEN-LAST:event_SkalaResiko4ItemStateChanged

    private void SkalaResiko4KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SkalaResiko4KeyPressed
        Valid.pindah(evt,SkalaResiko3,SkalaResiko5);
    }//GEN-LAST:event_SkalaResiko4KeyPressed

    private void SkalaResiko5ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_SkalaResiko5ItemStateChanged
        if(SkalaResiko5.getSelectedIndex()==0){
            NilaiResiko5.setText("0");
        }else if(SkalaResiko5.getSelectedIndex()==1){
            NilaiResiko5.setText("10");
        }else{
            NilaiResiko5.setText("20");
        }
        isTotalResikoJatuh();
    }//GEN-LAST:event_SkalaResiko5ItemStateChanged

    private void SkalaResiko5KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SkalaResiko5KeyPressed
        Valid.pindah(evt,SkalaResiko4,SkalaResiko6);
    }//GEN-LAST:event_SkalaResiko5KeyPressed

    private void SkalaResiko6ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_SkalaResiko6ItemStateChanged
        if(SkalaResiko6.getSelectedIndex()==0){
            NilaiResiko6.setText("0");
        }else{
            NilaiResiko6.setText("15");
        }
        isTotalResikoJatuh();
    }//GEN-LAST:event_SkalaResiko6ItemStateChanged

    private void SkalaResiko6KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SkalaResiko6KeyPressed
        Valid.pindah(evt,SkalaResiko5,SkalaSydney1);
    }//GEN-LAST:event_SkalaResiko6KeyPressed

    private void SkalaSydney1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_SkalaSydney1ItemStateChanged
        if(SkalaSydney1.getSelectedIndex()==0){
            NilaiSydney1.setText("0");
        }else if(SkalaSydney1.getSelectedIndex()==1){
            NilaiSydney1.setText("1");
             }else if(SkalaSydney1.getSelectedIndex()==2){
            NilaiSydney1.setText("2");
             }else if(SkalaSydney1.getSelectedIndex()==3){
            NilaiSydney1.setText("3");
             }else if(SkalaSydney1.getSelectedIndex()==4){
            NilaiSydney1.setText("4");
             }else if(SkalaSydney1.getSelectedIndex()==5){
            NilaiSydney1.setText("5");
             }else if(SkalaSydney1.getSelectedIndex()==6){
            NilaiSydney1.setText("6");
             }else if(SkalaSydney1.getSelectedIndex()==7){
            NilaiSydney1.setText("7");
             }else if(SkalaSydney1.getSelectedIndex()==8){
            NilaiSydney1.setText("8");
             }else if(SkalaSydney1.getSelectedIndex()==9){
            NilaiSydney1.setText("9");
             }else if(SkalaSydney1.getSelectedIndex()==10){
            NilaiSydney1.setText("10");

        }
        isTotalResikoSydney();
    }//GEN-LAST:event_SkalaSydney1ItemStateChanged

    private void SkalaSydney1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SkalaSydney1KeyPressed
        Valid.pindah(evt,SkalaResiko6,SkalaSydney2);
    }//GEN-LAST:event_SkalaSydney1KeyPressed

    private void SkalaSydney2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_SkalaSydney2ItemStateChanged
       if(SkalaSydney2.getSelectedIndex()==0){
            NilaiSydney2.setText("0");
        }else if(SkalaSydney2.getSelectedIndex()==1){
            NilaiSydney2.setText("1");
             }else if(SkalaSydney2.getSelectedIndex()==2){
            NilaiSydney2.setText("2");
             }else if(SkalaSydney2.getSelectedIndex()==3){
            NilaiSydney2.setText("3");
             }else if(SkalaSydney2.getSelectedIndex()==4){
            NilaiSydney2.setText("4");
             }else if(SkalaSydney2.getSelectedIndex()==5){
            NilaiSydney2.setText("5");
             }else if(SkalaSydney2.getSelectedIndex()==6){
            NilaiSydney2.setText("6");
             }else if(SkalaSydney2.getSelectedIndex()==7){
            NilaiSydney2.setText("7");
             }else if(SkalaSydney2.getSelectedIndex()==8){
            NilaiSydney2.setText("8");
             }else if(SkalaSydney2.getSelectedIndex()==9){
            NilaiSydney2.setText("9");
             }else if(SkalaSydney2.getSelectedIndex()==10){
            NilaiSydney2.setText("10");

        }
        isTotalResikoSydney();
    }//GEN-LAST:event_SkalaSydney2ItemStateChanged

    private void SkalaSydney2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SkalaSydney2KeyPressed
        Valid.pindah(evt,SkalaSydney1,SkalaSydney3);
    }//GEN-LAST:event_SkalaSydney2KeyPressed

    private void SkalaSydney3ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_SkalaSydney3ItemStateChanged
        if(SkalaSydney3.getSelectedIndex()==0){
            NilaiSydney3.setText("0");
        }else if(SkalaSydney3.getSelectedIndex()==1){
            NilaiSydney3.setText("1");
             }else if(SkalaSydney3.getSelectedIndex()==2){
            NilaiSydney3.setText("2");
             }else if(SkalaSydney3.getSelectedIndex()==3){
            NilaiSydney3.setText("3");
             }else if(SkalaSydney3.getSelectedIndex()==4){
            NilaiSydney3.setText("4");
             }else if(SkalaSydney3.getSelectedIndex()==5){
            NilaiSydney3.setText("5");
             }else if(SkalaSydney3.getSelectedIndex()==6){
            NilaiSydney3.setText("6");
             }else if(SkalaSydney3.getSelectedIndex()==7){
            NilaiSydney3.setText("7");
             }else if(SkalaSydney3.getSelectedIndex()==8){
            NilaiSydney3.setText("8");
             }else if(SkalaSydney3.getSelectedIndex()==9){
            NilaiSydney3.setText("9");
             }else if(SkalaSydney3.getSelectedIndex()==10){
            NilaiSydney3.setText("10");

        }
        isTotalResikoSydney();
    }//GEN-LAST:event_SkalaSydney3ItemStateChanged

    private void SkalaSydney3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SkalaSydney3KeyPressed
        Valid.pindah(evt,SkalaSydney2,SkalaSydney4);
    }//GEN-LAST:event_SkalaSydney3KeyPressed

    private void SkalaSydney4ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_SkalaSydney4ItemStateChanged
       if(SkalaSydney4.getSelectedIndex()==0){
            NilaiSydney4.setText("0");
        }else if(SkalaSydney4.getSelectedIndex()==1){
            NilaiSydney4.setText("1");
             }else if(SkalaSydney4.getSelectedIndex()==2){
            NilaiSydney4.setText("2");
             }else if(SkalaSydney4.getSelectedIndex()==3){
            NilaiSydney4.setText("3");
             }else if(SkalaSydney4.getSelectedIndex()==4){
            NilaiSydney4.setText("4");
             }else if(SkalaSydney4.getSelectedIndex()==5){
            NilaiSydney4.setText("5");
             }else if(SkalaSydney4.getSelectedIndex()==6){
            NilaiSydney4.setText("6");
             }else if(SkalaSydney4.getSelectedIndex()==7){
            NilaiSydney4.setText("7");
             }else if(SkalaSydney4.getSelectedIndex()==8){
            NilaiSydney4.setText("8");
             }else if(SkalaSydney4.getSelectedIndex()==9){
            NilaiSydney4.setText("9");
             }else if(SkalaSydney4.getSelectedIndex()==10){
            NilaiSydney4.setText("10");

        }
        isTotalResikoSydney();
    }//GEN-LAST:event_SkalaSydney4ItemStateChanged

    private void SkalaSydney4KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SkalaSydney4KeyPressed
        Valid.pindah(evt,SkalaSydney3,SkalaSydney5);
    }//GEN-LAST:event_SkalaSydney4KeyPressed

    private void SkalaSydney5ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_SkalaSydney5ItemStateChanged
        if(SkalaSydney5.getSelectedIndex()==0){
            NilaiSydney5.setText("0");
        }else if(SkalaSydney5.getSelectedIndex()==1){
            NilaiSydney5.setText("1");
             }else if(SkalaSydney5.getSelectedIndex()==2){
            NilaiSydney5.setText("2");
             }else if(SkalaSydney5.getSelectedIndex()==3){
            NilaiSydney5.setText("3");
             }else if(SkalaSydney5.getSelectedIndex()==4){
            NilaiSydney5.setText("4");
             }else if(SkalaSydney5.getSelectedIndex()==5){
            NilaiSydney5.setText("5");
             }else if(SkalaSydney5.getSelectedIndex()==6){
            NilaiSydney5.setText("6");
             }else if(SkalaSydney5.getSelectedIndex()==7){
            NilaiSydney5.setText("7");
             }else if(SkalaSydney5.getSelectedIndex()==8){
            NilaiSydney5.setText("8");
             }else if(SkalaSydney5.getSelectedIndex()==9){
            NilaiSydney5.setText("9");
             }else if(SkalaSydney5.getSelectedIndex()==10){
            NilaiSydney5.setText("10");

        }
        isTotalResikoSydney();
    }//GEN-LAST:event_SkalaSydney5ItemStateChanged

    private void SkalaSydney5KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SkalaSydney5KeyPressed
        Valid.pindah(evt,SkalaSydney4,SkalaSydney6);
    }//GEN-LAST:event_SkalaSydney5KeyPressed

    private void SkalaSydney6ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_SkalaSydney6ItemStateChanged
        if(SkalaSydney6.getSelectedIndex()==0){
            NilaiSydney6.setText("0");
        }else if(SkalaSydney6.getSelectedIndex()==1){
            NilaiSydney6.setText("1");
             }else if(SkalaSydney6.getSelectedIndex()==2){
            NilaiSydney6.setText("2");
             }else if(SkalaSydney6.getSelectedIndex()==3){
            NilaiSydney6.setText("3");
             }else if(SkalaSydney6.getSelectedIndex()==4){
            NilaiSydney6.setText("4");
             }else if(SkalaSydney6.getSelectedIndex()==5){
            NilaiSydney6.setText("5");
             }else if(SkalaSydney6.getSelectedIndex()==6){
            NilaiSydney6.setText("6");
             }else if(SkalaSydney6.getSelectedIndex()==7){
            NilaiSydney6.setText("7");
             }else if(SkalaSydney6.getSelectedIndex()==8){
            NilaiSydney6.setText("8");
             }else if(SkalaSydney6.getSelectedIndex()==9){
            NilaiSydney6.setText("9");
             }else if(SkalaSydney6.getSelectedIndex()==10){
            NilaiSydney6.setText("10");

        }
        isTotalResikoSydney();
    }//GEN-LAST:event_SkalaSydney6ItemStateChanged

    private void SkalaSydney6KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SkalaSydney6KeyPressed
        Valid.pindah(evt,SkalaSydney5,SkalaSydney7);
    }//GEN-LAST:event_SkalaSydney6KeyPressed

    private void SkalaSydney7ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_SkalaSydney7ItemStateChanged
        if(SkalaSydney7.getSelectedIndex()==0){
            NilaiSydney7.setText("0");
        }else if(SkalaSydney7.getSelectedIndex()==1){
            NilaiSydney7.setText("1");
             }else if(SkalaSydney7.getSelectedIndex()==2){
            NilaiSydney7.setText("2");
             }else if(SkalaSydney7.getSelectedIndex()==3){
            NilaiSydney7.setText("3");
             }else if(SkalaSydney7.getSelectedIndex()==4){
            NilaiSydney7.setText("4");
             }else if(SkalaSydney7.getSelectedIndex()==5){
            NilaiSydney7.setText("5");
             }else if(SkalaSydney7.getSelectedIndex()==6){
            NilaiSydney7.setText("6");
             }else if(SkalaSydney7.getSelectedIndex()==7){
            NilaiSydney7.setText("7");
             }else if(SkalaSydney7.getSelectedIndex()==8){
            NilaiSydney7.setText("8");
             }else if(SkalaSydney7.getSelectedIndex()==9){
            NilaiSydney7.setText("9");
             }else if(SkalaSydney7.getSelectedIndex()==10){
            NilaiSydney7.setText("10");

        }
        isTotalResikoSydney();
    }//GEN-LAST:event_SkalaSydney7ItemStateChanged

    private void SkalaSydney7KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SkalaSydney7KeyPressed
        Valid.pindah(evt,SkalaSydney6,SkalaSydney8);
    }//GEN-LAST:event_SkalaSydney7KeyPressed

    private void SkalaSydney8ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_SkalaSydney8ItemStateChanged
        if(SkalaSydney8.getSelectedIndex()==0){
            NilaiSydney8.setText("0");
        }else if(SkalaSydney8.getSelectedIndex()==1){
            NilaiSydney8.setText("1");
             }else if(SkalaSydney8.getSelectedIndex()==2){
            NilaiSydney8.setText("2");
             }else if(SkalaSydney8.getSelectedIndex()==3){
            NilaiSydney8.setText("3");
             }else if(SkalaSydney8.getSelectedIndex()==4){
            NilaiSydney8.setText("4");
             }else if(SkalaSydney8.getSelectedIndex()==5){
            NilaiSydney8.setText("5");
             }else if(SkalaSydney8.getSelectedIndex()==6){
            NilaiSydney8.setText("6");
             }else if(SkalaSydney8.getSelectedIndex()==7){
            NilaiSydney8.setText("7");
             }else if(SkalaSydney8.getSelectedIndex()==8){
            NilaiSydney8.setText("8");
             }else if(SkalaSydney8.getSelectedIndex()==9){
            NilaiSydney8.setText("9");
             }else if(SkalaSydney8.getSelectedIndex()==10){
            NilaiSydney8.setText("10");

        }
        isTotalResikoSydney();
    }//GEN-LAST:event_SkalaSydney8ItemStateChanged

    private void SkalaSydney8KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SkalaSydney8KeyPressed
        Valid.pindah(evt,SkalaSydney7,SkalaSydney9);
    }//GEN-LAST:event_SkalaSydney8KeyPressed

    private void SkalaSydney9ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_SkalaSydney9ItemStateChanged
        if(SkalaSydney9.getSelectedIndex()==0){
            NilaiSydney9.setText("0");
        }else if(SkalaSydney9.getSelectedIndex()==1){
            NilaiSydney9.setText("1");
             }else if(SkalaSydney9.getSelectedIndex()==2){
            NilaiSydney9.setText("2");
             }else if(SkalaSydney9.getSelectedIndex()==3){
            NilaiSydney9.setText("3");
             }else if(SkalaSydney9.getSelectedIndex()==4){
            NilaiSydney9.setText("4");
             }else if(SkalaSydney9.getSelectedIndex()==5){
            NilaiSydney9.setText("5");
             }else if(SkalaSydney9.getSelectedIndex()==6){
            NilaiSydney9.setText("6");
             }else if(SkalaSydney9.getSelectedIndex()==7){
            NilaiSydney9.setText("7");
             }else if(SkalaSydney9.getSelectedIndex()==8){
            NilaiSydney9.setText("8");
             }else if(SkalaSydney9.getSelectedIndex()==9){
            NilaiSydney9.setText("9");
             }else if(SkalaSydney9.getSelectedIndex()==10){
            NilaiSydney9.setText("10");

        }
        isTotalResikoSydney();
    }//GEN-LAST:event_SkalaSydney9ItemStateChanged

    private void SkalaSydney9KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SkalaSydney9KeyPressed
        Valid.pindah(evt,SkalaSydney8,SkalaSydney10);
    }//GEN-LAST:event_SkalaSydney9KeyPressed

    private void SkalaSydney10ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_SkalaSydney10ItemStateChanged
        if(SkalaSydney10.getSelectedIndex()==0){
            NilaiSydney10.setText("0");
        }else if(SkalaSydney10.getSelectedIndex()==1){
            NilaiSydney10.setText("1");
             }else if(SkalaSydney10.getSelectedIndex()==2){
            NilaiSydney10.setText("2");
             }else if(SkalaSydney10.getSelectedIndex()==3){
            NilaiSydney10.setText("3");
             }else if(SkalaSydney10.getSelectedIndex()==4){
            NilaiSydney10.setText("4");
             }else if(SkalaSydney10.getSelectedIndex()==5){
            NilaiSydney10.setText("5");
             }else if(SkalaSydney10.getSelectedIndex()==6){
            NilaiSydney10.setText("6");
             }else if(SkalaSydney10.getSelectedIndex()==7){
            NilaiSydney10.setText("7");
             }else if(SkalaSydney10.getSelectedIndex()==8){
            NilaiSydney10.setText("8");
             }else if(SkalaSydney10.getSelectedIndex()==9){
            NilaiSydney10.setText("9");
             }else if(SkalaSydney10.getSelectedIndex()==10){
            NilaiSydney10.setText("10");

        }
        isTotalResikoSydney();
    }//GEN-LAST:event_SkalaSydney10ItemStateChanged

    private void SkalaSydney10KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SkalaSydney10KeyPressed
        Valid.pindah(evt,SkalaSydney9,SkalaSydney11);
    }//GEN-LAST:event_SkalaSydney10KeyPressed

    private void SkalaSydney11ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_SkalaSydney11ItemStateChanged
        if(SkalaSydney11.getSelectedIndex()==0){
            NilaiSydney11.setText("0");
        }else if(SkalaSydney11.getSelectedIndex()==1){
            NilaiSydney11.setText("1");
             }else if(SkalaSydney11.getSelectedIndex()==2){
            NilaiSydney11.setText("2");
             }else if(SkalaSydney11.getSelectedIndex()==3){
            NilaiSydney11.setText("3");
             }else if(SkalaSydney11.getSelectedIndex()==4){
            NilaiSydney11.setText("4");
             }else if(SkalaSydney11.getSelectedIndex()==5){
            NilaiSydney11.setText("5");
             }else if(SkalaSydney11.getSelectedIndex()==6){
            NilaiSydney11.setText("6");
             }else if(SkalaSydney11.getSelectedIndex()==7){
            NilaiSydney11.setText("7");
             }else if(SkalaSydney11.getSelectedIndex()==8){
            NilaiSydney11.setText("8");
             }else if(SkalaSydney11.getSelectedIndex()==9){
            NilaiSydney11.setText("9");
             }else if(SkalaSydney11.getSelectedIndex()==10){
            NilaiSydney11.setText("10");

        }
        isTotalResikoSydney();
    }//GEN-LAST:event_SkalaSydney11ItemStateChanged

    private void SkalaSydney11KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SkalaSydney11KeyPressed
        Valid.pindah(evt,SkalaSydney10,SkalaGizi1);
    }//GEN-LAST:event_SkalaSydney11KeyPressed

    private void SkalaGizi1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_SkalaGizi1ItemStateChanged
        if(SkalaGizi1.getSelectedIndex()==0){
            NilaiGizi1.setText("0");
        }else if(SkalaGizi1.getSelectedIndex()==1){
            NilaiGizi1.setText("2");
        }else if(SkalaGizi1.getSelectedIndex()==2){
            NilaiGizi1.setText("1");
        }else if(SkalaGizi1.getSelectedIndex()==3){
            NilaiGizi1.setText("2");
        }else if(SkalaGizi1.getSelectedIndex()==4){
            NilaiGizi1.setText("3");
        }else if(SkalaGizi1.getSelectedIndex()==5){
            NilaiGizi1.setText("4");
        }
        isTotalGizi();
    }//GEN-LAST:event_SkalaGizi1ItemStateChanged

    private void SkalaGizi1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SkalaGizi1KeyPressed
        Valid.pindah(evt,SkalaSydney11,SkalaGizi2);
    }//GEN-LAST:event_SkalaGizi1KeyPressed

    private void SkalaGizi2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_SkalaGizi2ItemStateChanged
        if(SkalaGizi2.getSelectedIndex()==0){
            NilaiGizi2.setText("0");
        }else if(SkalaGizi2.getSelectedIndex()==1){
            NilaiGizi2.setText("1");
        }
        isTotalGizi();
    }//GEN-LAST:event_SkalaGizi2ItemStateChanged

    private void SkalaGizi2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SkalaGizi2KeyPressed
        Valid.pindah(evt,SkalaGizi1,DiagnosaKhususGizi);
    }//GEN-LAST:event_SkalaGizi2KeyPressed

    private void DiagnosaKhususGiziKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DiagnosaKhususGiziKeyPressed
        Valid.pindah(evt,SkalaGizi2,KeteranganDiagnosaKhususGizi);
    }//GEN-LAST:event_DiagnosaKhususGiziKeyPressed

    private void KeteranganDiagnosaKhususGiziKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KeteranganDiagnosaKhususGiziKeyPressed
        Valid.pindah(evt,DiagnosaKhususGizi,DiketahuiDietisen);
    }//GEN-LAST:event_KeteranganDiagnosaKhususGiziKeyPressed

    private void DiketahuiDietisenKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DiketahuiDietisenKeyPressed
        Valid.pindah(evt,KeteranganDiagnosaKhususGizi,KeteranganDiketahuiDietisen);
    }//GEN-LAST:event_DiketahuiDietisenKeyPressed

    private void KeteranganDiketahuiDietisenKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KeteranganDiketahuiDietisenKeyPressed
        Valid.pindah(evt,DiketahuiDietisen,TCariMasalah);
    }//GEN-LAST:event_KeteranganDiketahuiDietisenKeyPressed

    private void tbMasalahKeperawatanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbMasalahKeperawatanMouseClicked
        if(tabModeMasalah.getRowCount()!=0){
            try {
                tampilRencana2();
            } catch (java.lang.NullPointerException e) {
            }
        }
    }//GEN-LAST:event_tbMasalahKeperawatanMouseClicked

    private void tbMasalahKeperawatanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbMasalahKeperawatanKeyPressed
        if(tabModeMasalah.getRowCount()!=0){
            if(evt.getKeyCode()==KeyEvent.VK_SHIFT){
                TCariMasalah.setText("");
                TCariMasalah.requestFocus();
            }
        }
    }//GEN-LAST:event_tbMasalahKeperawatanKeyPressed

    private void tbMasalahKeperawatanKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbMasalahKeperawatanKeyReleased
        if(tabModeMasalah.getRowCount()!=0){
            if((evt.getKeyCode()==KeyEvent.VK_ENTER)||(evt.getKeyCode()==KeyEvent.VK_UP)||(evt.getKeyCode()==KeyEvent.VK_DOWN)){
                try {
                    tampilRencana2();
                } catch (java.lang.NullPointerException e) {
                }
            }
        }
    }//GEN-LAST:event_tbMasalahKeperawatanKeyReleased

    private void RencanaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_RencanaKeyPressed
        Valid.pindah2(evt,TCariMasalah,BtnSimpan);
    }//GEN-LAST:event_RencanaKeyPressed

    private void BtnTambahMasalahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnTambahMasalahActionPerformed
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        MasterMasalahKeperawatan form=new MasterMasalahKeperawatan(null,false);
        form.isCek();
        form.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
        form.setLocationRelativeTo(internalFrame1);
        form.setVisible(true);
        this.setCursor(Cursor.getDefaultCursor());
    }//GEN-LAST:event_BtnTambahMasalahActionPerformed

    private void BtnAllMasalahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAllMasalahActionPerformed
        TCari.setText("");
        tampilMasalah();
    }//GEN-LAST:event_BtnAllMasalahActionPerformed

    private void BtnAllMasalahKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnAllMasalahKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnAllMasalahActionPerformed(null);
        }else{
            Valid.pindah(evt, BtnCariMasalah, TCariMasalah);
        }
    }//GEN-LAST:event_BtnAllMasalahKeyPressed

    private void BtnCariMasalahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCariMasalahActionPerformed
        tampilMasalah2();
    }//GEN-LAST:event_BtnCariMasalahActionPerformed

    private void BtnCariMasalahKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnCariMasalahKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            tampilMasalah2();
        }else if((evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN)||(evt.getKeyCode()==KeyEvent.VK_TAB)){
            Rencana.requestFocus();
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_UP){
            KeteranganDiketahuiDietisen.requestFocus();
        }
    }//GEN-LAST:event_BtnCariMasalahKeyPressed

    private void TCariMasalahKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TCariMasalahKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            tampilMasalah2();
        }else if((evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN)||(evt.getKeyCode()==KeyEvent.VK_TAB)){
            Rencana.requestFocus();
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_UP){
            KeteranganDiketahuiDietisen.requestFocus();
        }
    }//GEN-LAST:event_TCariMasalahKeyPressed

    private void BtnTambahRencanaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnTambahRencanaActionPerformed
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        MasterRencanaKeperawatan form=new MasterRencanaKeperawatan(null,false);
        form.isCek();
        form.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
        form.setLocationRelativeTo(internalFrame1);
        form.setVisible(true);
        this.setCursor(Cursor.getDefaultCursor());
    }//GEN-LAST:event_BtnTambahRencanaActionPerformed

    private void BtnAllRencanaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAllRencanaActionPerformed
        TCariRencana.setText("");
        tampilRencana();
        tampilRencana2();
    }//GEN-LAST:event_BtnAllRencanaActionPerformed

    private void BtnAllRencanaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnAllRencanaKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnAllRencanaActionPerformed(null);
        }else{
            Valid.pindah(evt, BtnCariRencana, TCariRencana);
        }
    }//GEN-LAST:event_BtnAllRencanaKeyPressed

    private void BtnCariRencanaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCariRencanaActionPerformed
        tampilRencana2();
    }//GEN-LAST:event_BtnCariRencanaActionPerformed

    private void BtnCariRencanaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnCariRencanaKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            tampilRencana2();
        }else if((evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN)||(evt.getKeyCode()==KeyEvent.VK_TAB)){
            BtnSimpan.requestFocus();
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_UP){
            TCariRencana.requestFocus();
        }
    }//GEN-LAST:event_BtnCariRencanaKeyPressed

    private void TCariRencanaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TCariRencanaKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            tampilRencana2();
        }else if((evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN)||(evt.getKeyCode()==KeyEvent.VK_TAB)){
            BtnCariRencana.requestFocus();
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_UP){
            TCariMasalah.requestFocus();
        }
    }//GEN-LAST:event_TCariRencanaKeyPressed

    private void KebiasaanAlkoholActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_KebiasaanAlkoholActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_KebiasaanAlkoholActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            AsesmenAwalKeperawatanKemoterapi dialog = new AsesmenAwalKeperawatanKemoterapi(new javax.swing.JFrame(), true);
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
    private widget.ComboBox AdakahPerilaku;
    private widget.TextBox Agama;
    private widget.ComboBox Aktifitas;
    private widget.ComboBox AktifitasSehari2;
    private widget.ComboBox AlatAmbulasi;
    private widget.ComboBox AlatBantuDipakai;
    private widget.TextBox Alergi;
    private widget.ComboBox Anamnesis;
    private widget.TextBox BAB;
    private widget.TextBox BAK;
    private widget.TextBox BB;
    private widget.TextBox Bahasa;
    private widget.ComboBox Berjalan;
    private widget.Button BtnAll;
    private widget.Button BtnAllMasalah;
    private widget.Button BtnAllRencana;
    private widget.Button BtnBatal;
    private widget.Button BtnCari;
    private widget.Button BtnCariMasalah;
    private widget.Button BtnCariRencana;
    private widget.Button BtnDPJP;
    private widget.Button BtnEdit;
    private widget.Button BtnHapus;
    private widget.Button BtnKeluar;
    private widget.Button BtnPetugas;
    private widget.Button BtnPetugas2;
    private widget.Button BtnPrint;
    private widget.Button BtnPrint1;
    private widget.Button BtnSimpan;
    private widget.Button BtnTambahMasalah;
    private widget.Button BtnTambahRencana;
    private widget.TextBox CaraBayar;
    private widget.ComboBox CaraMasuk;
    private widget.CekBox ChkAccor;
    private widget.Tanggal DTPCari1;
    private widget.Tanggal DTPCari2;
    private widget.TextArea DetailRencana;
    private widget.ComboBox DiagnosaKhususGizi;
    private widget.ComboBox DiketahuiDietisen;
    private widget.TextBox Durasi;
    private widget.ComboBox EdukasiPsikolgis;
    private widget.ComboBox EkstrimitasAtas;
    private widget.ComboBox EkstrimitasBawah;
    private widget.PanelBiasa FormInput;
    private widget.PanelBiasa FormMasalahRencana;
    private widget.PanelBiasa FormMenu;
    private widget.TextBox GCS;
    private widget.ComboBox GangguanJiwa;
    private widget.ComboBox GastrointestinalAbdomen;
    private widget.ComboBox GastrointestinalAnus;
    private widget.ComboBox GastrointestinalGigi;
    private widget.ComboBox GastrointestinalLidah;
    private widget.ComboBox GastrointestinalMulut;
    private widget.ComboBox GastrointestinalTenggorakan;
    private widget.ComboBox GastrointestinalUsus;
    private widget.ComboBox HubunganAnggotaKeluarga;
    private widget.ComboBox IntegumentDecubitus;
    private widget.ComboBox IntegumentKulit;
    private widget.ComboBox IntegumentTurgor;
    private widget.ComboBox IntegumentWarnaKulit;
    private widget.TextBox Jk;
    private widget.TextBox KBAB;
    private widget.ComboBox KardiovaskularDenyutNadi;
    private widget.ComboBox KardiovaskularPulsasi;
    private widget.ComboBox KardiovaskularSirkulasi;
    private widget.TextBox KdDPJP;
    private widget.TextBox KdPetugas;
    private widget.TextBox KdPetugas2;
    private widget.ComboBox KeadaanMentalUmum;
    private widget.ComboBox KebiasaanAlkohol;
    private widget.TextBox KebiasaanJumlahAlkohol;
    private widget.TextBox KebiasaanJumlahRokok;
    private widget.ComboBox KebiasaanMerokok;
    private widget.ComboBox KebiasaanNarkoba;
    private widget.ComboBox KemampuanKoordinasi;
    private widget.ComboBox KemampuanMenggenggam;
    private widget.TextBox KesadaranMental;
    private widget.ComboBox KesimpulanGangguanFungsi;
    private widget.TextBox KetAnamnesis;
    private widget.TextBox KetGastrointestinalAbdomen;
    private widget.TextBox KetGastrointestinalGigi;
    private widget.TextBox KetGastrointestinalLidah;
    private widget.TextBox KetGastrointestinalMulut;
    private widget.TextBox KetGastrointestinalTenggorakan;
    private widget.TextBox KetKardiovaskularSirkulasi;
    private widget.TextBox KetMuskuloskletalFraktur;
    private widget.TextBox KetMuskuloskletalNyeriSendi;
    private widget.TextBox KetMuskuloskletalOedema;
    private widget.TextBox KetNeurologiBicara;
    private widget.TextBox KetNeurologiPenglihatan;
    private widget.TextBox KetNyeri;
    private widget.TextBox KetPadaDokter;
    private widget.TextBox KetProvokes;
    private widget.TextBox KetQuality;
    private widget.TextBox KetRespirasiJenisPernafasan;
    private widget.TextBox KetSedangMenyusui;
    private widget.TextBox KetSistemSarafKejang;
    private widget.TextBox KetSistemSarafKepala;
    private widget.TextBox KetSistemSarafWajah;
    private widget.TextBox KeteranganAdakahPerilaku;
    private widget.TextBox KeteranganBerjalan;
    private widget.TextBox KeteranganDiagnosaKhususGizi;
    private widget.TextBox KeteranganDiketahuiDietisen;
    private widget.TextBox KeteranganEdukasiPsikologis;
    private widget.TextBox KeteranganEkstrimitasAtas;
    private widget.TextBox KeteranganEkstrimitasBawah;
    private widget.TextBox KeteranganKemampuanKoordinasi;
    private widget.TextBox KeteranganKemampuanMenggenggam;
    private widget.TextBox KeteranganNilaiKepercayaan;
    private widget.TextBox KeteranganTinggalDengan;
    private widget.ComboBox KondisiPsikologis;
    private widget.TextBox LBAK;
    private widget.Label LCount;
    private widget.TextBox Lokasi;
    private widget.ComboBox MacamKasus;
    private widget.ComboBox Menyebar;
    private widget.ComboBox MuskuloskletalFraktur;
    private widget.ComboBox MuskuloskletalKekuatanOtot;
    private widget.ComboBox MuskuloskletalNyeriSendi;
    private widget.ComboBox MuskuloskletalOedema;
    private widget.ComboBox MuskuloskletalPegerakanSendi;
    private widget.TextBox Nadi;
    private widget.ComboBox NeurologiAlatBantuPenglihatan;
    private widget.ComboBox NeurologiBicara;
    private widget.ComboBox NeurologiMotorik;
    private widget.ComboBox NeurologiOtot;
    private widget.ComboBox NeurologiPendengaran;
    private widget.ComboBox NeurologiPenglihatan;
    private widget.ComboBox NeurologiSensorik;
    private widget.TextBox NilaiGizi1;
    private widget.TextBox NilaiGizi2;
    private widget.TextBox NilaiGiziTotal;
    private widget.ComboBox NilaiKepercayaan;
    private widget.TextBox NilaiResiko1;
    private widget.TextBox NilaiResiko2;
    private widget.TextBox NilaiResiko3;
    private widget.TextBox NilaiResiko4;
    private widget.TextBox NilaiResiko5;
    private widget.TextBox NilaiResiko6;
    private widget.TextBox NilaiResikoTotal;
    private widget.TextBox NilaiSydney1;
    private widget.TextBox NilaiSydney10;
    private widget.TextBox NilaiSydney11;
    private widget.TextBox NilaiSydney2;
    private widget.TextBox NilaiSydney3;
    private widget.TextBox NilaiSydney4;
    private widget.TextBox NilaiSydney5;
    private widget.TextBox NilaiSydney6;
    private widget.TextBox NilaiSydney7;
    private widget.TextBox NilaiSydney8;
    private widget.TextBox NilaiSydney9;
    private widget.TextBox NilaiSydneyTotal;
    private widget.TextBox NmDPJP;
    private widget.TextBox NmPetugas;
    private widget.TextBox NmPetugas2;
    private widget.ComboBox Nyeri;
    private widget.ComboBox NyeriHilang;
    private widget.ComboBox OlahRaga;
    private widget.ComboBox PadaDokter;
    private widget.PanelBiasa PanelAccor;
    private usu.widget.glass.PanelGlass PanelWall;
    private usu.widget.glass.PanelGlass PanelWall1;
    private usu.widget.glass.PanelGlass PanelWall2;
    private widget.TextBox PekerjaanPasien;
    private widget.ComboBox PendidikanPJ;
    private widget.TextBox PendidikanPasien;
    private widget.ComboBox PolaAktifitasBerpakaian;
    private widget.ComboBox PolaAktifitasBerpindah;
    private widget.ComboBox PolaAktifitasEliminasi;
    private widget.ComboBox PolaAktifitasMakan;
    private widget.ComboBox PolaAktifitasMandi;
    private widget.TextBox PolaNutrisiFrekuensi;
    private widget.TextBox PolaNutrisiJenis;
    private widget.TextBox PolaNutrisiPorsi;
    private widget.ComboBox PolaTidurGangguan;
    private widget.TextBox PolaTidurLama;
    private widget.ComboBox Provokes;
    private widget.ComboBox Quality;
    private widget.TextBox RDirawatRS;
    private widget.TextArea RPD;
    private widget.TextArea RPK;
    private widget.TextArea RPO;
    private widget.TextArea RPS;
    private widget.TextBox RPembedahan;
    private widget.TextBox RR;
    private widget.TextBox RTranfusi;
    private widget.TextArea Rencana;
    private widget.ComboBox RespirasiBatuk;
    private widget.ComboBox RespirasiIrama;
    private widget.ComboBox RespirasiJenisPernafasan;
    private widget.ComboBox RespirasiPolaNafas;
    private widget.ComboBox RespirasiRetraksi;
    private widget.ComboBox RespirasiSuaraNafas;
    private widget.ComboBox RespirasiVolume;
    private widget.ScrollPane Scroll;
    private widget.ScrollPane Scroll6;
    private widget.ScrollPane Scroll7;
    private widget.ScrollPane Scroll8;
    private widget.ScrollPane Scroll9;
    private widget.ComboBox SedangMenyusui;
    private widget.ComboBox SistemSarafKejang;
    private widget.ComboBox SistemSarafKepala;
    private widget.ComboBox SistemSarafLeher;
    private widget.ComboBox SistemSarafSensorik;
    private widget.ComboBox SistemSarafWajah;
    private widget.ComboBox SkalaGizi1;
    private widget.ComboBox SkalaGizi2;
    private widget.ComboBox SkalaNyeri;
    private widget.ComboBox SkalaResiko1;
    private widget.ComboBox SkalaResiko2;
    private widget.ComboBox SkalaResiko3;
    private widget.ComboBox SkalaResiko4;
    private widget.ComboBox SkalaResiko5;
    private widget.ComboBox SkalaResiko6;
    private widget.ComboBox SkalaSydney1;
    private widget.ComboBox SkalaSydney10;
    private widget.ComboBox SkalaSydney11;
    private widget.ComboBox SkalaSydney2;
    private widget.ComboBox SkalaSydney3;
    private widget.ComboBox SkalaSydney4;
    private widget.ComboBox SkalaSydney5;
    private widget.ComboBox SkalaSydney6;
    private widget.ComboBox SkalaSydney7;
    private widget.ComboBox SkalaSydney8;
    private widget.ComboBox SkalaSydney9;
    private widget.TextBox SpO2;
    private widget.TextBox Suhu;
    private widget.TextBox TB;
    private widget.TextBox TCari;
    private widget.TextBox TCariMasalah;
    private widget.TextBox TCariRencana;
    private widget.TextBox TD;
    private widget.TextBox TNoRM;
    private widget.TextBox TNoRM1;
    private widget.TextBox TNoRw;
    private widget.TextBox TPasien;
    private widget.TextBox TPasien1;
    private javax.swing.JTabbedPane TabRawat;
    private javax.swing.JTabbedPane TabRencanaKeperawatan;
    private widget.Tanggal TglAsuhan;
    private widget.TextBox TglLahir;
    private widget.ComboBox TibadiRuang;
    private widget.ComboBox TinggalDengan;
    private widget.Label TingkatResiko;
    private widget.Label TingkatSydney;
    private widget.TextBox WBAB;
    private widget.TextBox WBAK;
    private widget.TextBox XBAB;
    private widget.TextBox XBAK;
    private widget.InternalFrame internalFrame1;
    private widget.InternalFrame internalFrame2;
    private widget.InternalFrame internalFrame3;
    private widget.Label jLabel10;
    private widget.Label jLabel11;
    private widget.Label jLabel113;
    private widget.Label jLabel114;
    private widget.Label jLabel115;
    private widget.Label jLabel116;
    private widget.Label jLabel117;
    private widget.Label jLabel118;
    private widget.Label jLabel119;
    private widget.Label jLabel12;
    private widget.Label jLabel120;
    private widget.Label jLabel121;
    private widget.Label jLabel122;
    private widget.Label jLabel123;
    private widget.Label jLabel124;
    private widget.Label jLabel126;
    private widget.Label jLabel127;
    private widget.Label jLabel128;
    private widget.Label jLabel129;
    private widget.Label jLabel13;
    private widget.Label jLabel130;
    private widget.Label jLabel131;
    private widget.Label jLabel132;
    private widget.Label jLabel133;
    private widget.Label jLabel134;
    private widget.Label jLabel135;
    private widget.Label jLabel136;
    private widget.Label jLabel137;
    private widget.Label jLabel138;
    private widget.Label jLabel139;
    private widget.Label jLabel140;
    private widget.Label jLabel141;
    private widget.Label jLabel142;
    private widget.Label jLabel143;
    private widget.Label jLabel144;
    private widget.Label jLabel145;
    private widget.Label jLabel146;
    private widget.Label jLabel147;
    private widget.Label jLabel148;
    private widget.Label jLabel149;
    private widget.Label jLabel15;
    private widget.Label jLabel150;
    private widget.Label jLabel151;
    private widget.Label jLabel152;
    private widget.Label jLabel153;
    private widget.Label jLabel154;
    private widget.Label jLabel155;
    private widget.Label jLabel156;
    private widget.Label jLabel157;
    private widget.Label jLabel158;
    private widget.Label jLabel159;
    private widget.Label jLabel16;
    private widget.Label jLabel160;
    private widget.Label jLabel161;
    private widget.Label jLabel162;
    private widget.Label jLabel163;
    private widget.Label jLabel164;
    private widget.Label jLabel165;
    private widget.Label jLabel166;
    private widget.Label jLabel167;
    private widget.Label jLabel168;
    private widget.Label jLabel169;
    private widget.Label jLabel17;
    private widget.Label jLabel170;
    private widget.Label jLabel171;
    private widget.Label jLabel172;
    private widget.Label jLabel173;
    private widget.Label jLabel174;
    private widget.Label jLabel175;
    private widget.Label jLabel176;
    private widget.Label jLabel177;
    private widget.Label jLabel178;
    private widget.Label jLabel179;
    private widget.Label jLabel18;
    private widget.Label jLabel181;
    private widget.Label jLabel182;
    private widget.Label jLabel183;
    private widget.Label jLabel184;
    private widget.Label jLabel185;
    private widget.Label jLabel186;
    private widget.Label jLabel187;
    private widget.Label jLabel188;
    private widget.Label jLabel19;
    private widget.Label jLabel191;
    private widget.Label jLabel193;
    private widget.Label jLabel194;
    private widget.Label jLabel196;
    private widget.Label jLabel197;
    private widget.Label jLabel199;
    private widget.Label jLabel20;
    private widget.Label jLabel201;
    private widget.Label jLabel202;
    private widget.Label jLabel203;
    private widget.Label jLabel204;
    private widget.Label jLabel205;
    private widget.Label jLabel206;
    private widget.Label jLabel207;
    private widget.Label jLabel208;
    private widget.Label jLabel209;
    private widget.Label jLabel21;
    private widget.Label jLabel211;
    private widget.Label jLabel212;
    private widget.Label jLabel213;
    private widget.Label jLabel214;
    private widget.Label jLabel215;
    private widget.Label jLabel216;
    private widget.Label jLabel217;
    private widget.Label jLabel218;
    private widget.Label jLabel219;
    private widget.Label jLabel22;
    private widget.Label jLabel220;
    private widget.Label jLabel221;
    private widget.Label jLabel222;
    private widget.Label jLabel223;
    private widget.Label jLabel224;
    private widget.Label jLabel225;
    private widget.Label jLabel226;
    private widget.Label jLabel227;
    private widget.Label jLabel228;
    private widget.Label jLabel229;
    private widget.Label jLabel23;
    private widget.Label jLabel230;
    private widget.Label jLabel231;
    private widget.Label jLabel232;
    private widget.Label jLabel233;
    private widget.Label jLabel234;
    private widget.Label jLabel235;
    private widget.Label jLabel236;
    private widget.Label jLabel237;
    private widget.Label jLabel238;
    private widget.Label jLabel239;
    private widget.Label jLabel24;
    private widget.Label jLabel240;
    private widget.Label jLabel241;
    private widget.Label jLabel242;
    private widget.Label jLabel243;
    private widget.Label jLabel244;
    private widget.Label jLabel245;
    private widget.Label jLabel246;
    private widget.Label jLabel247;
    private widget.Label jLabel248;
    private widget.Label jLabel249;
    private widget.Label jLabel25;
    private widget.Label jLabel250;
    private widget.Label jLabel251;
    private widget.Label jLabel252;
    private widget.Label jLabel253;
    private widget.Label jLabel254;
    private widget.Label jLabel255;
    private widget.Label jLabel256;
    private widget.Label jLabel257;
    private widget.Label jLabel258;
    private widget.Label jLabel259;
    private widget.Label jLabel26;
    private widget.Label jLabel260;
    private widget.Label jLabel261;
    private widget.Label jLabel262;
    private widget.Label jLabel263;
    private widget.Label jLabel264;
    private widget.Label jLabel265;
    private widget.Label jLabel266;
    private widget.Label jLabel267;
    private widget.Label jLabel268;
    private widget.Label jLabel269;
    private widget.Label jLabel27;
    private widget.Label jLabel270;
    private widget.Label jLabel271;
    private widget.Label jLabel272;
    private widget.Label jLabel273;
    private widget.Label jLabel274;
    private widget.Label jLabel275;
    private widget.Label jLabel276;
    private widget.Label jLabel277;
    private widget.Label jLabel278;
    private widget.Label jLabel279;
    private widget.Label jLabel28;
    private widget.Label jLabel283;
    private widget.Label jLabel284;
    private widget.Label jLabel285;
    private widget.Label jLabel286;
    private widget.Label jLabel287;
    private widget.Label jLabel288;
    private widget.Label jLabel29;
    private widget.Label jLabel30;
    private widget.Label jLabel31;
    private widget.Label jLabel32;
    private widget.Label jLabel33;
    private widget.Label jLabel34;
    private widget.Label jLabel35;
    private widget.Label jLabel36;
    private widget.Label jLabel37;
    private widget.Label jLabel38;
    private widget.Label jLabel41;
    private widget.Label jLabel42;
    private widget.Label jLabel43;
    private widget.Label jLabel44;
    private widget.Label jLabel45;
    private widget.Label jLabel46;
    private widget.Label jLabel47;
    private widget.Label jLabel48;
    private widget.Label jLabel49;
    private widget.Label jLabel50;
    private widget.Label jLabel51;
    private widget.Label jLabel52;
    private widget.Label jLabel53;
    private widget.Label jLabel54;
    private widget.Label jLabel55;
    private widget.Label jLabel56;
    private widget.Label jLabel57;
    private widget.Label jLabel58;
    private widget.Label jLabel6;
    private widget.Label jLabel7;
    private widget.Label jLabel8;
    private widget.Label jLabel9;
    private widget.Label jLabel94;
    private widget.Label jLabel95;
    private javax.swing.JSeparator jSeparator11;
    private javax.swing.JSeparator jSeparator12;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private widget.Label label11;
    private widget.Label label12;
    private widget.Label label13;
    private widget.Label label14;
    private widget.Label label15;
    private widget.Label label16;
    private widget.PanelBiasa panelBiasa1;
    private widget.panelisi panelGlass8;
    private widget.panelisi panelGlass9;
    private widget.ScrollPane scrollInput;
    private widget.ScrollPane scrollPane1;
    private widget.ScrollPane scrollPane2;
    private widget.ScrollPane scrollPane3;
    private widget.ScrollPane scrollPane4;
    private widget.ScrollPane scrollPane5;
    private widget.ScrollPane scrollPane6;
    private widget.Table tbMasalahDetail;
    private widget.Table tbMasalahKeperawatan;
    private widget.Table tbObat;
    private widget.Table tbRencanaDetail;
    private widget.Table tbRencanaKeperawatan;
    // End of variables declaration//GEN-END:variables

    public void tampil() {
        Valid.tabelKosong(tabMode);
        try{
            ps=koneksi.prepareStatement(
                "select penilaian_awal_keperawatan_ranap_kemoterapi.no_rawat,penilaian_awal_keperawatan_ranap_kemoterapi.tanggal,penilaian_awal_keperawatan_ranap_kemoterapi.informasi,penilaian_awal_keperawatan_ranap_kemoterapi.ket_informasi,penilaian_awal_keperawatan_ranap_kemoterapi.tiba_diruang_rawat,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.kasus_trauma,penilaian_awal_keperawatan_ranap_kemoterapi.cara_masuk,penilaian_awal_keperawatan_ranap_kemoterapi.rps,penilaian_awal_keperawatan_ranap_kemoterapi.rpd,penilaian_awal_keperawatan_ranap_kemoterapi.rpk,penilaian_awal_keperawatan_ranap_kemoterapi.rpo,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.riwayat_pembedahan,penilaian_awal_keperawatan_ranap_kemoterapi.riwayat_dirawat_dirs,penilaian_awal_keperawatan_ranap_kemoterapi.alat_bantu_dipakai,penilaian_awal_keperawatan_ranap_kemoterapi.riwayat_kehamilan,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.riwayat_kehamilan_perkiraan,penilaian_awal_keperawatan_ranap_kemoterapi.riwayat_tranfusi,penilaian_awal_keperawatan_ranap_kemoterapi.riwayat_alergi,penilaian_awal_keperawatan_ranap_kemoterapi.riwayat_merokok,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.riwayat_merokok_jumlah,penilaian_awal_keperawatan_ranap_kemoterapi.riwayat_alkohol,penilaian_awal_keperawatan_ranap_kemoterapi.riwayat_alkohol_jumlah,penilaian_awal_keperawatan_ranap_kemoterapi.riwayat_narkoba,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.riwayat_olahraga,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_mental,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_keadaan_umum,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_gcs,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_td,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_nadi,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_rr,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_suhu,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_spo2,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_bb,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_tb,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_susunan_kepala,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_susunan_kepala_keterangan,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_susunan_wajah,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_susunan_wajah_keterangan,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_susunan_leher,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_susunan_kejang,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_susunan_kejang_keterangan,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_susunan_sensorik,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_kardiovaskuler_denyut_nadi,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_kardiovaskuler_sirkulasi,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_kardiovaskuler_sirkulasi_keterangan,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_kardiovaskuler_pulsasi,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_respirasi_pola_nafas,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_respirasi_retraksi,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_respirasi_suara_nafas,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_respirasi_volume_pernafasan,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_respirasi_jenis_pernafasan,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_respirasi_jenis_pernafasan_keterangan,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_respirasi_irama_nafas,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_respirasi_batuk,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_gastrointestinal_mulut,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_gastrointestinal_mulut_keterangan,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_gastrointestinal_gigi,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_gastrointestinal_gigi_keterangan,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_gastrointestinal_lidah,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_gastrointestinal_lidah_keterangan,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_gastrointestinal_tenggorokan,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_gastrointestinal_tenggorokan_keterangan,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_gastrointestinal_abdomen,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_gastrointestinal_abdomen_keterangan,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_gastrointestinal_peistatik_usus,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_gastrointestinal_anus,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_neurologi_pengelihatan,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_neurologi_pengelihatan_keterangan,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_neurologi_alat_bantu_penglihatan,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_neurologi_pendengaran,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_neurologi_bicara,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_neurologi_bicara_keterangan,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_neurologi_sensorik,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_neurologi_motorik,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_neurologi_kekuatan_otot,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_integument_warnakulit,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_integument_turgor,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_integument_kulit,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_integument_dekubitas,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_muskuloskletal_pergerakan_sendi,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_muskuloskletal_kekauatan_otot,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_muskuloskletal_nyeri_sendi,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_muskuloskletal_nyeri_sendi_keterangan,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_muskuloskletal_oedema,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_muskuloskletal_oedema_keterangan,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_muskuloskletal_fraktur,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_muskuloskletal_fraktur_keterangan,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_eliminasi_bab_frekuensi_jumlah,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_eliminasi_bab_frekuensi_durasi,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_eliminasi_bab_konsistensi,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_eliminasi_bab_warna,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_eliminasi_bak_frekuensi_jumlah,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_eliminasi_bak_frekuensi_durasi,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_eliminasi_bak_warna,penilaian_awal_keperawatan_ranap_kemoterapi.pemeriksaan_eliminasi_bak_lainlain,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.pola_aktifitas_makanminum,penilaian_awal_keperawatan_ranap_kemoterapi.pola_aktifitas_mandi,penilaian_awal_keperawatan_ranap_kemoterapi.pola_aktifitas_eliminasi,penilaian_awal_keperawatan_ranap_kemoterapi.pola_aktifitas_berpakaian,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.pola_aktifitas_berpindah,penilaian_awal_keperawatan_ranap_kemoterapi.pola_nutrisi_frekuesi_makan,penilaian_awal_keperawatan_ranap_kemoterapi.pola_nutrisi_jenis_makanan,penilaian_awal_keperawatan_ranap_kemoterapi.pola_nutrisi_porsi_makan,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.pola_tidur_lama_tidur,penilaian_awal_keperawatan_ranap_kemoterapi.pola_tidur_gangguan,penilaian_awal_keperawatan_ranap_kemoterapi.pengkajian_fungsi_kemampuan_sehari,penilaian_awal_keperawatan_ranap_kemoterapi.pengkajian_fungsi_aktifitas,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.pengkajian_fungsi_berjalan,penilaian_awal_keperawatan_ranap_kemoterapi.pengkajian_fungsi_berjalan_keterangan,penilaian_awal_keperawatan_ranap_kemoterapi.pengkajian_fungsi_ambulasi,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.pengkajian_fungsi_ekstrimitas_atas,penilaian_awal_keperawatan_ranap_kemoterapi.pengkajian_fungsi_ekstrimitas_atas_keterangan,penilaian_awal_keperawatan_ranap_kemoterapi.pengkajian_fungsi_ekstrimitas_bawah,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.pengkajian_fungsi_ekstrimitas_bawah_keterangan,penilaian_awal_keperawatan_ranap_kemoterapi.pengkajian_fungsi_menggenggam,penilaian_awal_keperawatan_ranap_kemoterapi.pengkajian_fungsi_menggenggam_keterangan,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.pengkajian_fungsi_koordinasi,penilaian_awal_keperawatan_ranap_kemoterapi.pengkajian_fungsi_koordinasi_keterangan,penilaian_awal_keperawatan_ranap_kemoterapi.pengkajian_fungsi_kesimpulan,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.riwayat_psiko_kondisi_psiko,penilaian_awal_keperawatan_ranap_kemoterapi.riwayat_psiko_gangguan_jiwa,penilaian_awal_keperawatan_ranap_kemoterapi.riwayat_psiko_perilaku,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.riwayat_psiko_perilaku_keterangan,penilaian_awal_keperawatan_ranap_kemoterapi.riwayat_psiko_hubungan_keluarga,penilaian_awal_keperawatan_ranap_kemoterapi.riwayat_psiko_tinggal,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.riwayat_psiko_tinggal_keterangan,penilaian_awal_keperawatan_ranap_kemoterapi.riwayat_psiko_nilai_kepercayaan,penilaian_awal_keperawatan_ranap_kemoterapi.riwayat_psiko_nilai_kepercayaan_keterangan,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.riwayat_psiko_pendidikan_pj,penilaian_awal_keperawatan_ranap_kemoterapi.riwayat_psiko_edukasi_diberikan,penilaian_awal_keperawatan_ranap_kemoterapi.riwayat_psiko_edukasi_diberikan_keterangan,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_nyeri,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_nyeri_penyebab,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_nyeri_ket_penyebab,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_nyeri_kualitas,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_nyeri_ket_kualitas,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_nyeri_lokasi,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_nyeri_menyebar,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_nyeri_skala,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_nyeri_waktu,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_nyeri_hilang,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_nyeri_ket_hilang,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_nyeri_diberitahukan_dokter,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_nyeri_jam_diberitahukan_dokter,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhmorse_skala1,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhmorse_nilai1,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhmorse_skala2,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhmorse_nilai2,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhmorse_skala3,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhmorse_nilai3,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhmorse_skala4,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhmorse_nilai4,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhmorse_skala5,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhmorse_nilai5,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhmorse_skala6,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhmorse_nilai6,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhmorse_totalnilai,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhsydney_skala1,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhsydney_nilai1,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhsydney_skala2,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhsydney_nilai2,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhsydney_skala3,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhsydney_nilai3,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhsydney_skala4,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhsydney_nilai4,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhsydney_skala5,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhsydney_nilai5,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhsydney_skala6,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhsydney_nilai6,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhsydney_skala7,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhsydney_nilai7,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhsydney_skala8,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhsydney_nilai8,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhsydney_skala9,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhsydney_nilai9,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhsydney_skala10,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhsydney_nilai10,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhsydney_skala11,penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhsydney_nilai11,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.penilaian_jatuhsydney_totalnilai,penilaian_awal_keperawatan_ranap_kemoterapi.skrining_gizi1,penilaian_awal_keperawatan_ranap_kemoterapi.nilai_gizi1,penilaian_awal_keperawatan_ranap_kemoterapi.skrining_gizi2,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.nilai_gizi2,penilaian_awal_keperawatan_ranap_kemoterapi.nilai_total_gizi,penilaian_awal_keperawatan_ranap_kemoterapi.skrining_gizi_diagnosa_khusus,penilaian_awal_keperawatan_ranap_kemoterapi.skrining_gizi_ket_diagnosa_khusus,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.skrining_gizi_diketahui_dietisen,penilaian_awal_keperawatan_ranap_kemoterapi.skrining_gizi_jam_diketahui_dietisen,penilaian_awal_keperawatan_ranap_kemoterapi.rencana,penilaian_awal_keperawatan_ranap_kemoterapi.nip1,"+
                "penilaian_awal_keperawatan_ranap_kemoterapi.nip2,penilaian_awal_keperawatan_ranap_kemoterapi.kd_dokter,pasien.tgl_lahir,pasien.jk,pengkaji1.nama as pengkaji1,pengkaji2.nama as pengkaji2,dokter.nm_dokter,"+
                "reg_periksa.no_rkm_medis,pasien.nm_pasien,pasien.agama,pasien.pekerjaan,pasien.pnd,penjab.png_jawab,bahasa_pasien.nama_bahasa "+
                "from reg_periksa inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                "inner join penilaian_awal_keperawatan_ranap_kemoterapi on reg_periksa.no_rawat=penilaian_awal_keperawatan_ranap_kemoterapi.no_rawat "+
                "inner join petugas as pengkaji1 on penilaian_awal_keperawatan_ranap_kemoterapi.nip1=pengkaji1.nip "+
                "inner join petugas as pengkaji2 on penilaian_awal_keperawatan_ranap_kemoterapi.nip2=pengkaji2.nip "+
                "inner join dokter on penilaian_awal_keperawatan_ranap_kemoterapi.kd_dokter=dokter.kd_dokter "+
                "inner join bahasa_pasien on bahasa_pasien.id=pasien.bahasa_pasien "+
                "inner join penjab on penjab.kd_pj=reg_periksa.kd_pj where "+
                "penilaian_awal_keperawatan_ranap_kemoterapi.tanggal between ? and ? "+
                (TCari.getText().trim().equals("")?"":"and (reg_periksa.no_rawat like ? or pasien.no_rkm_medis like ? or pasien.nm_pasien like ? or penilaian_awal_keperawatan_ranap_kemoterapi.nip1 like ? or "+
                "pengkaji1.nama like ? or penilaian_awal_keperawatan_ranap_kemoterapi.kd_dokter like ? or dokter.nm_dokter like ?)")+
                " order by penilaian_awal_keperawatan_ranap_kemoterapi.tanggal");
            
            try {
                ps.setString(1,Valid.SetTgl(DTPCari1.getSelectedItem()+"")+" 00:00:00");
                ps.setString(2,Valid.SetTgl(DTPCari2.getSelectedItem()+"")+" 23:59:59");
                if(!TCari.getText().equals("")){
                    ps.setString(3,"%"+TCari.getText()+"%");
                    ps.setString(4,"%"+TCari.getText()+"%");
                    ps.setString(5,"%"+TCari.getText()+"%");
                    ps.setString(6,"%"+TCari.getText()+"%");
                    ps.setString(7,"%"+TCari.getText()+"%");
                    ps.setString(8,"%"+TCari.getText()+"%");
                    ps.setString(9,"%"+TCari.getText()+"%");
                }   
                rs=ps.executeQuery();
                while(rs.next()){
                    tabMode.addRow(new String[]{
                        rs.getString("no_rawat"),rs.getString("no_rkm_medis"),rs.getString("nm_pasien"),rs.getString("tgl_lahir"),rs.getString("jk"),rs.getString("nip1"),rs.getString("pengkaji1"),rs.getString("nip2"),rs.getString("pengkaji2"),
                        rs.getString("kd_dokter"),rs.getString("nm_dokter"),rs.getString("tanggal"),rs.getString("kasus_trauma"),rs.getString("informasi")+", "+rs.getString("ket_informasi"),rs.getString("tiba_diruang_rawat"),rs.getString("cara_masuk"),
                        rs.getString("rps"),rs.getString("rpd"),rs.getString("rpk"),rs.getString("rpo"),rs.getString("riwayat_pembedahan"),rs.getString("alat_bantu_dipakai"),rs.getString("riwayat_dirawat_dirs"),
                        rs.getString("riwayat_kehamilan")+", "+rs.getString("riwayat_kehamilan_perkiraan"),rs.getString("riwayat_tranfusi"),rs.getString("riwayat_alergi"),rs.getString("riwayat_merokok"),rs.getString("riwayat_merokok_jumlah"),
                        rs.getString("riwayat_alkohol"),rs.getString("riwayat_alkohol_jumlah"),rs.getString("riwayat_narkoba"),rs.getString("riwayat_olahraga"),rs.getString("pemeriksaan_mental"),rs.getString("pemeriksaan_keadaan_umum"),
                        rs.getString("pemeriksaan_gcs"),rs.getString("pemeriksaan_td"),rs.getString("pemeriksaan_nadi"),rs.getString("pemeriksaan_rr"),rs.getString("pemeriksaan_suhu"),rs.getString("pemeriksaan_spo2"),rs.getString("pemeriksaan_bb"),
                        rs.getString("pemeriksaan_tb"),rs.getString("pemeriksaan_susunan_kepala")+", "+rs.getString("pemeriksaan_susunan_kepala_keterangan"),rs.getString("pemeriksaan_susunan_wajah")+", "+rs.getString("pemeriksaan_susunan_wajah_keterangan"),
                        rs.getString("pemeriksaan_susunan_leher"),rs.getString("pemeriksaan_susunan_kejang")+", "+rs.getString("pemeriksaan_susunan_kejang_keterangan"),rs.getString("pemeriksaan_susunan_sensorik"),rs.getString("pemeriksaan_kardiovaskuler_pulsasi"),
                        rs.getString("pemeriksaan_kardiovaskuler_sirkulasi")+", "+rs.getString("pemeriksaan_kardiovaskuler_sirkulasi_keterangan"),rs.getString("pemeriksaan_kardiovaskuler_denyut_nadi"),rs.getString("pemeriksaan_respirasi_retraksi"),
                        rs.getString("pemeriksaan_respirasi_pola_nafas"),rs.getString("pemeriksaan_respirasi_suara_nafas"),rs.getString("pemeriksaan_respirasi_batuk"),rs.getString("pemeriksaan_respirasi_volume_pernafasan"),
                        rs.getString("pemeriksaan_respirasi_jenis_pernafasan")+", "+rs.getString("pemeriksaan_respirasi_jenis_pernafasan_keterangan"),rs.getString("pemeriksaan_respirasi_irama_nafas"),
                        rs.getString("pemeriksaan_gastrointestinal_mulut")+", "+rs.getString("pemeriksaan_gastrointestinal_mulut_keterangan"),rs.getString("pemeriksaan_gastrointestinal_lidah")+", "+rs.getString("pemeriksaan_gastrointestinal_lidah_keterangan"),
                        rs.getString("pemeriksaan_gastrointestinal_gigi")+", "+rs.getString("pemeriksaan_gastrointestinal_gigi_keterangan"),rs.getString("pemeriksaan_gastrointestinal_tenggorokan")+", "+rs.getString("pemeriksaan_gastrointestinal_tenggorokan_keterangan"),
                        rs.getString("pemeriksaan_gastrointestinal_abdomen")+", "+rs.getString("pemeriksaan_gastrointestinal_abdomen_keterangan"),rs.getString("pemeriksaan_gastrointestinal_peistatik_usus"),rs.getString("pemeriksaan_gastrointestinal_anus"),
                        rs.getString("pemeriksaan_neurologi_sensorik"),rs.getString("pemeriksaan_neurologi_pengelihatan")+", "+rs.getString("pemeriksaan_neurologi_pengelihatan_keterangan"),rs.getString("pemeriksaan_neurologi_alat_bantu_penglihatan"),
                        rs.getString("pemeriksaan_neurologi_motorik"),rs.getString("pemeriksaan_neurologi_pendengaran"),rs.getString("pemeriksaan_neurologi_bicara")+", "+rs.getString("pemeriksaan_neurologi_bicara_keterangan"),
                        rs.getString("pemeriksaan_neurologi_kekuatan_otot"),rs.getString("pemeriksaan_integument_kulit"),rs.getString("pemeriksaan_integument_warnakulit"),rs.getString("pemeriksaan_integument_turgor"),
                        rs.getString("pemeriksaan_integument_dekubitas"),rs.getString("pemeriksaan_muskuloskletal_oedema")+", "+rs.getString("pemeriksaan_muskuloskletal_oedema_keterangan"),rs.getString("pemeriksaan_muskuloskletal_pergerakan_sendi"),
                        rs.getString("pemeriksaan_muskuloskletal_kekauatan_otot"),rs.getString("pemeriksaan_muskuloskletal_fraktur")+", "+rs.getString("pemeriksaan_muskuloskletal_fraktur_keterangan"),
                        rs.getString("pemeriksaan_muskuloskletal_nyeri_sendi")+", "+rs.getString("pemeriksaan_muskuloskletal_nyeri_sendi_keterangan"),rs.getString("pemeriksaan_eliminasi_bab_frekuensi_jumlah"),
                        rs.getString("pemeriksaan_eliminasi_bab_frekuensi_durasi"),rs.getString("pemeriksaan_eliminasi_bab_konsistensi"),rs.getString("pemeriksaan_eliminasi_bab_warna"),rs.getString("pemeriksaan_eliminasi_bak_frekuensi_jumlah"),
                        rs.getString("pemeriksaan_eliminasi_bak_frekuensi_durasi"),rs.getString("pemeriksaan_eliminasi_bak_warna"),rs.getString("pemeriksaan_eliminasi_bak_lainlain"),rs.getString("pola_aktifitas_mandi"),
                        rs.getString("pola_aktifitas_makanminum"),rs.getString("pola_aktifitas_berpakaian"),rs.getString("pola_aktifitas_eliminasi"),rs.getString("pola_aktifitas_berpindah"),rs.getString("pola_nutrisi_porsi_makan"),
                        rs.getString("pola_nutrisi_frekuesi_makan"),rs.getString("pola_nutrisi_jenis_makanan"),rs.getString("pola_tidur_lama_tidur"),rs.getString("pola_tidur_gangguan"),rs.getString("pengkajian_fungsi_kemampuan_sehari"),
//                        rs.getString("pengkajian_fungsi_berjalan")+", "+
                        rs.getString("pengkajian_fungsi_berjalan_keterangan"),rs.getString("pengkajian_fungsi_aktifitas"),rs.getString("pengkajian_fungsi_ambulasi"),
//                        rs.getString("pengkajian_fungsi_ekstrimitas_atas")+", "+
                        rs.getString("pengkajian_fungsi_ekstrimitas_atas_keterangan"),rs.getString("pengkajian_fungsi_ekstrimitas_bawah")+", "+rs.getString("pengkajian_fungsi_ekstrimitas_bawah_keterangan"),
                        rs.getString("pengkajian_fungsi_menggenggam")+", "+rs.getString("pengkajian_fungsi_menggenggam_keterangan"),rs.getString("pengkajian_fungsi_koordinasi")+", "+rs.getString("pengkajian_fungsi_koordinasi_keterangan"),
                        rs.getString("pengkajian_fungsi_kesimpulan"),rs.getString("riwayat_psiko_kondisi_psiko"),rs.getString("riwayat_psiko_perilaku")+", "+rs.getString("riwayat_psiko_perilaku_keterangan"),rs.getString("riwayat_psiko_gangguan_jiwa"),
                        rs.getString("riwayat_psiko_hubungan_keluarga"),rs.getString("agama"),rs.getString("riwayat_psiko_tinggal")+", "+rs.getString("riwayat_psiko_tinggal_keterangan"),rs.getString("pekerjaan"),rs.getString("png_jawab"),
                        rs.getString("riwayat_psiko_nilai_kepercayaan")+", "+rs.getString("riwayat_psiko_nilai_kepercayaan_keterangan"),rs.getString("nama_bahasa"),rs.getString("pnd"),rs.getString("riwayat_psiko_pendidikan_pj"),
                        rs.getString("riwayat_psiko_edukasi_diberikan")+", "+rs.getString("riwayat_psiko_edukasi_diberikan_keterangan"),rs.getString("penilaian_nyeri"),rs.getString("penilaian_nyeri_penyebab")+", "+rs.getString("penilaian_nyeri_ket_penyebab"),
                        rs.getString("penilaian_nyeri_kualitas")+", "+rs.getString("penilaian_nyeri_ket_kualitas"),rs.getString("penilaian_nyeri_lokasi"),rs.getString("penilaian_nyeri_menyebar"),rs.getString("penilaian_nyeri_skala"),
                        rs.getString("penilaian_nyeri_waktu"),rs.getString("penilaian_nyeri_hilang")+", "+rs.getString("penilaian_nyeri_ket_hilang"),rs.getString("penilaian_nyeri_diberitahukan_dokter")+", "+rs.getString("penilaian_nyeri_jam_diberitahukan_dokter"),
                        rs.getString("penilaian_jatuhmorse_skala1"),rs.getString("penilaian_jatuhmorse_nilai1"),rs.getString("penilaian_jatuhmorse_skala2"),rs.getString("penilaian_jatuhmorse_nilai2"),
                        rs.getString("penilaian_jatuhmorse_skala3"),rs.getString("penilaian_jatuhmorse_nilai3"),rs.getString("penilaian_jatuhmorse_skala4"),rs.getString("penilaian_jatuhmorse_nilai4"),
                        rs.getString("penilaian_jatuhmorse_skala5"),rs.getString("penilaian_jatuhmorse_nilai5"),rs.getString("penilaian_jatuhmorse_skala6"),rs.getString("penilaian_jatuhmorse_nilai6"),
                        rs.getString("penilaian_jatuhmorse_totalnilai"),rs.getString("penilaian_jatuhsydney_skala1"),rs.getString("penilaian_jatuhsydney_nilai1"),rs.getString("penilaian_jatuhsydney_skala2"),
                        rs.getString("penilaian_jatuhsydney_nilai2"),rs.getString("penilaian_jatuhsydney_skala3"),rs.getString("penilaian_jatuhsydney_nilai3"),rs.getString("penilaian_jatuhsydney_skala4"),rs.getString("penilaian_jatuhsydney_nilai4"),
                        rs.getString("penilaian_jatuhsydney_skala5"),rs.getString("penilaian_jatuhsydney_nilai5"),rs.getString("penilaian_jatuhsydney_skala6"),rs.getString("penilaian_jatuhsydney_nilai6"),rs.getString("penilaian_jatuhsydney_skala7"),
                        rs.getString("penilaian_jatuhsydney_nilai7"),rs.getString("penilaian_jatuhsydney_skala8"),rs.getString("penilaian_jatuhsydney_nilai8"),rs.getString("penilaian_jatuhsydney_skala9"),rs.getString("penilaian_jatuhsydney_nilai9"),
                        rs.getString("penilaian_jatuhsydney_skala10"),rs.getString("penilaian_jatuhsydney_nilai10"),rs.getString("penilaian_jatuhsydney_skala11"),rs.getString("penilaian_jatuhsydney_nilai11"),
                        rs.getString("penilaian_jatuhsydney_totalnilai"),rs.getString("skrining_gizi1"),rs.getString("nilai_gizi1"),rs.getString("skrining_gizi2"),rs.getString("nilai_gizi2"),rs.getString("nilai_total_gizi"),
                        rs.getString("skrining_gizi_diagnosa_khusus"),rs.getString("skrining_gizi_ket_diagnosa_khusus"),rs.getString("skrining_gizi_diketahui_dietisen"),rs.getString("skrining_gizi_jam_diketahui_dietisen"),
                        rs.getString("rencana")
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
        TglAsuhan.setDate(new Date());
        Anamnesis.setSelectedIndex(0);
        KetAnamnesis.setText("");
        TibadiRuang.setSelectedIndex(0);
        CaraMasuk.setSelectedIndex(0);
        MacamKasus.setSelectedIndex(0);
        RPS.setText("");
        RPD.setText("");
        RPK.setText("");
        RPO.setText("");
        RPembedahan.setText("");
        RDirawatRS.setText("");
        AlatBantuDipakai.setSelectedIndex(0);
        SedangMenyusui.setSelectedIndex(0);
        KetSedangMenyusui.setText("");
        RTranfusi.setText("");
        Alergi.setText("");
        KebiasaanMerokok.setSelectedIndex(0);
        KebiasaanJumlahRokok.setText("");
        KebiasaanAlkohol.setSelectedIndex(0);
        KebiasaanJumlahAlkohol.setText("");
        KebiasaanNarkoba.setSelectedIndex(0);
        OlahRaga.setSelectedIndex(0);
        KesadaranMental.setText("");
        KeadaanMentalUmum.setSelectedIndex(0);
        GCS.setText("");
        TD.setText("");
        Nadi.setText("");
        RR.setText("");
        Suhu.setText("");
        SpO2.setText("");
        BB.setText("");
        TB.setText("");
        SistemSarafKepala.setSelectedIndex(0);
        KetSistemSarafKepala.setText("");
        SistemSarafWajah.setSelectedIndex(0);
        KetSistemSarafWajah.setText("");
        SistemSarafLeher.setSelectedIndex(0);
        SistemSarafKejang.setSelectedIndex(0);
        KetSistemSarafKejang.setText("");
        SistemSarafSensorik.setSelectedIndex(0);
        KardiovaskularPulsasi.setSelectedIndex(0);
        KardiovaskularSirkulasi.setSelectedIndex(0);
        KetKardiovaskularSirkulasi.setText("");
        KardiovaskularDenyutNadi.setSelectedIndex(0);
        RespirasiRetraksi.setSelectedIndex(0);
        RespirasiPolaNafas.setSelectedIndex(0);
        RespirasiSuaraNafas.setSelectedIndex(0);
        RespirasiBatuk.setSelectedIndex(0);
        RespirasiVolume.setSelectedIndex(0);
        RespirasiJenisPernafasan.setSelectedIndex(0);
        KetRespirasiJenisPernafasan.setText("");
        RespirasiIrama.setSelectedIndex(0);
        GastrointestinalMulut.setSelectedIndex(0);
        KetGastrointestinalMulut.setText("");
        GastrointestinalLidah.setSelectedIndex(0);
        KetGastrointestinalLidah.setText("");
        GastrointestinalGigi.setSelectedIndex(0);
        KetGastrointestinalGigi.setText("");
        GastrointestinalTenggorakan.setSelectedIndex(0);
        KetGastrointestinalTenggorakan.setText("");
        GastrointestinalAbdomen.setSelectedIndex(0);
        KetGastrointestinalAbdomen.setText("");
        GastrointestinalUsus.setSelectedIndex(0);
        GastrointestinalAnus.setSelectedIndex(0);
        NeurologiSensorik.setSelectedIndex(0);
        NeurologiPenglihatan.setSelectedIndex(0);
        KetNeurologiPenglihatan.setText("");
        NeurologiAlatBantuPenglihatan.setSelectedIndex(0);
        NeurologiMotorik.setSelectedIndex(0);
        NeurologiPendengaran.setSelectedIndex(0);
        NeurologiBicara.setSelectedIndex(0);
        KetNeurologiBicara.setText("");
        NeurologiOtot.setSelectedIndex(0);
        IntegumentKulit.setSelectedIndex(0);
        IntegumentWarnaKulit.setSelectedIndex(0);
        IntegumentTurgor.setSelectedIndex(0);
        IntegumentDecubitus.setSelectedIndex(0);
        MuskuloskletalOedema.setSelectedIndex(0);
        KetMuskuloskletalOedema.setText("");
        MuskuloskletalPegerakanSendi.setSelectedIndex(0);
        MuskuloskletalKekuatanOtot.setSelectedIndex(0);
        MuskuloskletalFraktur.setSelectedIndex(0);
        KetMuskuloskletalFraktur.setText("");
        MuskuloskletalNyeriSendi.setSelectedIndex(0);
        KetMuskuloskletalNyeriSendi.setText("");
        BAB.setText("");
        XBAB.setText("");
        KBAB.setText("");
        WBAB.setText("");
        BAK.setText("");
        XBAK.setText("");
        WBAK.setText("");
        LBAK.setText("");
        PolaAktifitasMandi.setSelectedIndex(0);
        PolaAktifitasMakan.setSelectedIndex(0);
        PolaAktifitasBerpakaian.setSelectedIndex(0);
        PolaAktifitasEliminasi.setSelectedIndex(0);
        PolaAktifitasBerpindah.setSelectedIndex(0);
        PolaNutrisiPorsi.setText("");
        PolaNutrisiFrekuensi.setText("");
        PolaNutrisiJenis.setText("");
        PolaTidurLama.setText("");
        PolaTidurGangguan.setSelectedIndex(0);
        AktifitasSehari2.setSelectedIndex(0);
        Berjalan.setSelectedIndex(0);
        KeteranganBerjalan.setText("");
        Aktifitas.setSelectedIndex(0);
        AlatAmbulasi.setSelectedIndex(0);
        EkstrimitasAtas.setSelectedIndex(0);
        KeteranganEkstrimitasAtas.setText("");
        EkstrimitasBawah.setSelectedIndex(0);
        KeteranganEkstrimitasBawah.setText("");
        KemampuanMenggenggam.setSelectedIndex(0);
        KeteranganKemampuanMenggenggam.setText("");
        KemampuanKoordinasi.setSelectedIndex(0);
        KeteranganKemampuanKoordinasi.setText("");
        KesimpulanGangguanFungsi.setSelectedIndex(0);
        KondisiPsikologis.setSelectedIndex(0);
        AdakahPerilaku.setSelectedIndex(0);
        KeteranganAdakahPerilaku.setText("");
        GangguanJiwa.setSelectedIndex(0);
        HubunganAnggotaKeluarga.setSelectedIndex(0);
        TinggalDengan.setSelectedIndex(0);
        KeteranganTinggalDengan.setText("");
        NilaiKepercayaan.setSelectedIndex(0);
        KeteranganNilaiKepercayaan.setText("");
        PendidikanPJ.setSelectedIndex(0);
        EdukasiPsikolgis.setSelectedIndex(0);
        KeteranganEdukasiPsikologis.setText("");
        Nyeri.setSelectedIndex(0);
        Provokes.setSelectedIndex(0);
        KetProvokes.setText("");
        Quality.setSelectedIndex(0);
        KetQuality.setText("");
        Lokasi.setText("");
        Menyebar.setSelectedIndex(0);
        SkalaNyeri.setSelectedIndex(0);
        Durasi.setText("");
        NyeriHilang.setSelectedIndex(0);
        KetNyeri.setText("");
        PadaDokter.setSelectedIndex(0);
        KetPadaDokter.setText("");
        SkalaResiko1.setSelectedIndex(0);
        NilaiResiko1.setText("0");
        SkalaResiko2.setSelectedIndex(0);
        NilaiResiko2.setText("0");
        SkalaResiko3.setSelectedIndex(0);
        NilaiResiko3.setText("0");
        SkalaResiko4.setSelectedIndex(0);
        NilaiResiko4.setText("0");
        SkalaResiko5.setSelectedIndex(0);
        NilaiResiko5.setText("0");
        SkalaResiko6.setSelectedIndex(0);
        NilaiResiko6.setText("0");
        NilaiResikoTotal.setText("0");
        TingkatResiko.setText("Tingkat Resiko : Risiko Rendah (0-24), Tindakan : Intervensi pencegahan risiko jatuh standar");
        SkalaSydney1.setSelectedIndex(0);
        NilaiSydney1.setText("0");
        SkalaSydney2.setSelectedIndex(0);
        NilaiSydney2.setText("0");
        SkalaSydney3.setSelectedIndex(0);
        NilaiSydney3.setText("0");
        SkalaSydney4.setSelectedIndex(0);
        NilaiSydney4.setText("0");
        SkalaSydney5.setSelectedIndex(0);
        NilaiSydney5.setText("0");
        SkalaSydney6.setSelectedIndex(0);
        NilaiSydney6.setText("0");
        SkalaSydney7.setSelectedIndex(0);
        NilaiSydney7.setText("0");
        SkalaSydney8.setSelectedIndex(0);
        NilaiSydney8.setText("0");
        SkalaSydney9.setSelectedIndex(0);
        NilaiSydney9.setText("0");
        SkalaSydney10.setSelectedIndex(0);
        NilaiSydney10.setText("0");
        SkalaSydney11.setSelectedIndex(0);
        NilaiSydney11.setText("0");
        NilaiSydneyTotal.setText("0");
        TingkatSydney.setText("Tingkat Resiko : Risiko Rendah (1-3), Tindakan : Intervensi pencegahan risiko jatuh standar");
        SkalaGizi1.setSelectedIndex(0);
        NilaiGizi1.setText("0");
        SkalaGizi2.setSelectedIndex(0);
        NilaiGizi2.setText("0");
        NilaiGiziTotal.setText("0");
        DiagnosaKhususGizi.setSelectedIndex(0);
        KeteranganDiagnosaKhususGizi.setText("");
        DiketahuiDietisen.setSelectedIndex(0);
        KeteranganDiketahuiDietisen.setText("");
        Rencana.setText("");
        for (i = 0; i < tabModeMasalah.getRowCount(); i++) {
            tabModeMasalah.setValueAt(false,i,0);
        }
        Valid.tabelKosong(tabModeRencana);
        TabRawat.setSelectedIndex(0);
        MacamKasus.requestFocus();
    } 

    private void getData() {
        if(tbObat.getSelectedRow()!= -1){
            TNoRw.setText(tbObat.getValueAt(tbObat.getSelectedRow(),0).toString()); 
            TNoRM.setText(tbObat.getValueAt(tbObat.getSelectedRow(),1).toString());
            TPasien.setText(tbObat.getValueAt(tbObat.getSelectedRow(),2).toString()); 
            TglLahir.setText(tbObat.getValueAt(tbObat.getSelectedRow(),3).toString()); 
            Jk.setText(tbObat.getValueAt(tbObat.getSelectedRow(),4).toString()); 
            KdPetugas2.setText(tbObat.getValueAt(tbObat.getSelectedRow(),7).toString()); 
            NmPetugas2.setText(tbObat.getValueAt(tbObat.getSelectedRow(),8).toString()); 
            KdDPJP.setText(tbObat.getValueAt(tbObat.getSelectedRow(),9).toString()); 
            NmDPJP.setText(tbObat.getValueAt(tbObat.getSelectedRow(),10).toString()); 
            MacamKasus.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),12).toString()); 
            if(tbObat.getValueAt(tbObat.getSelectedRow(),13).toString().contains("Autoanamnesis")){
                Anamnesis.setSelectedItem("Autoanamnesis");
            }else{
                Anamnesis.setSelectedItem("Alloanamnesis");
            }
            KetAnamnesis.setText(tbObat.getValueAt(tbObat.getSelectedRow(),13).toString().replaceAll(Anamnesis.getSelectedItem().toString()+", ",""));
            TibadiRuang.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),14).toString()); 
            CaraMasuk.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),15).toString()); 
            RPS.setText(tbObat.getValueAt(tbObat.getSelectedRow(),16).toString()); 
            RPD.setText(tbObat.getValueAt(tbObat.getSelectedRow(),17).toString()); 
            RPK.setText(tbObat.getValueAt(tbObat.getSelectedRow(),18).toString()); 
            RPO.setText(tbObat.getValueAt(tbObat.getSelectedRow(),19).toString()); 
            RPembedahan.setText(tbObat.getValueAt(tbObat.getSelectedRow(),20).toString()); 
            RDirawatRS.setText(tbObat.getValueAt(tbObat.getSelectedRow(),22).toString()); 
            AlatBantuDipakai.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),21).toString()); 
            if(tbObat.getValueAt(tbObat.getSelectedRow(),23).toString().contains("Ya")){
                SedangMenyusui.setSelectedItem("Ya");
            }else{
                SedangMenyusui.setSelectedItem("Tidak");
            }
            KetSedangMenyusui.setText(tbObat.getValueAt(tbObat.getSelectedRow(),23).toString().replaceAll(SedangMenyusui.getSelectedItem().toString()+", ",""));
            RTranfusi.setText(tbObat.getValueAt(tbObat.getSelectedRow(),24).toString()); 
            Alergi.setText(tbObat.getValueAt(tbObat.getSelectedRow(),25).toString()); 
            KebiasaanMerokok.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),26).toString());  
            KebiasaanJumlahRokok.setText(tbObat.getValueAt(tbObat.getSelectedRow(),27).toString()); 
            KebiasaanAlkohol.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),28).toString());  
            KebiasaanJumlahAlkohol.setText(tbObat.getValueAt(tbObat.getSelectedRow(),29).toString()); 
            KebiasaanNarkoba.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),30).toString());  
            OlahRaga.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),31).toString());  
            KesadaranMental.setText(tbObat.getValueAt(tbObat.getSelectedRow(),32).toString());  
            KeadaanMentalUmum.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),33).toString());   
            GCS.setText(tbObat.getValueAt(tbObat.getSelectedRow(),34).toString());  
            TD.setText(tbObat.getValueAt(tbObat.getSelectedRow(),35).toString());  
            Nadi.setText(tbObat.getValueAt(tbObat.getSelectedRow(),36).toString());  
            RR.setText(tbObat.getValueAt(tbObat.getSelectedRow(),37).toString());  
            Suhu.setText(tbObat.getValueAt(tbObat.getSelectedRow(),38).toString());  
            SpO2.setText(tbObat.getValueAt(tbObat.getSelectedRow(),39).toString());  
            BB.setText(tbObat.getValueAt(tbObat.getSelectedRow(),40).toString());  
            TB.setText(tbObat.getValueAt(tbObat.getSelectedRow(),41).toString());  
            if(tbObat.getValueAt(tbObat.getSelectedRow(),42).toString().contains("TAK")){
                SistemSarafKepala.setSelectedItem("TAK");
            }else if(tbObat.getValueAt(tbObat.getSelectedRow(),42).toString().contains("Hydrocephalus")){
                SistemSarafKepala.setSelectedItem("Hydrocephalus");
            }else if(tbObat.getValueAt(tbObat.getSelectedRow(),42).toString().contains("Hematoma")){
                SistemSarafKepala.setSelectedItem("Hematoma");
            }else{
                SistemSarafKepala.setSelectedItem("Lain-lain");
            }
            KetSistemSarafKepala.setText(tbObat.getValueAt(tbObat.getSelectedRow(),42).toString().replaceAll(SistemSarafKepala.getSelectedItem().toString()+", ",""));
            if(tbObat.getValueAt(tbObat.getSelectedRow(),43).toString().contains("TAK")){
                SistemSarafWajah.setSelectedItem("TAK");
            }else if(tbObat.getValueAt(tbObat.getSelectedRow(),43).toString().contains("Asimetris")){
                SistemSarafWajah.setSelectedItem("Asimetris");
            }else{
                SistemSarafWajah.setSelectedItem("Kelainan Kongenital");
            }
            KetSistemSarafWajah.setText(tbObat.getValueAt(tbObat.getSelectedRow(),43).toString().replaceAll(SistemSarafWajah.getSelectedItem().toString()+", ",""));
            SistemSarafLeher.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),44).toString());  
            if(tbObat.getValueAt(tbObat.getSelectedRow(),45).toString().contains("TAK")){
                SistemSarafKejang.setSelectedItem("TAK");
            }else if(tbObat.getValueAt(tbObat.getSelectedRow(),45).toString().contains("Kuat")){
                SistemSarafKejang.setSelectedItem("Kuat");
            }else{
                SistemSarafKejang.setSelectedItem("Ada");
            }
            KetSistemSarafKejang.setText(tbObat.getValueAt(tbObat.getSelectedRow(),45).toString().replaceAll(SistemSarafKejang.getSelectedItem().toString()+", ",""));
            SistemSarafSensorik.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),46).toString());
            KardiovaskularPulsasi.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),47).toString());
            if(tbObat.getValueAt(tbObat.getSelectedRow(),48).toString().contains("Akral Hangat")){
                KardiovaskularSirkulasi.setSelectedItem("Akral Hangat");
            }else if(tbObat.getValueAt(tbObat.getSelectedRow(),48).toString().contains("Akral Dingin")){
                KardiovaskularSirkulasi.setSelectedItem("Akral Dingin");
            }else{
                KardiovaskularSirkulasi.setSelectedItem("Edema");
            }
            KetKardiovaskularSirkulasi.setText(tbObat.getValueAt(tbObat.getSelectedRow(),48).toString().replaceAll(KardiovaskularSirkulasi.getSelectedItem().toString()+", ",""));
            KardiovaskularDenyutNadi.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),49).toString());
            RespirasiRetraksi.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),50).toString());
            RespirasiPolaNafas.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),51).toString());
            RespirasiSuaraNafas.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),52).toString());
            RespirasiBatuk.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),53).toString());
            RespirasiVolume.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),54).toString());
            if(tbObat.getValueAt(tbObat.getSelectedRow(),55).toString().contains("Pernafasan Dada")){
                RespirasiJenisPernafasan.setSelectedItem("Pernafasan Dada");
            }else{
                RespirasiJenisPernafasan.setSelectedItem("Alat Bantu Pernafasaan");
            }
            KetRespirasiJenisPernafasan.setText(tbObat.getValueAt(tbObat.getSelectedRow(),55).toString().replaceAll(RespirasiJenisPernafasan.getSelectedItem().toString()+", ",""));
            RespirasiIrama.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),56).toString());
            if(tbObat.getValueAt(tbObat.getSelectedRow(),57).toString().contains("TAK")){
                GastrointestinalMulut.setSelectedItem("TAK");
            }else if(tbObat.getValueAt(tbObat.getSelectedRow(),57).toString().contains("Stomatitis")){
                GastrointestinalMulut.setSelectedItem("Stomatitis");
            }else if(tbObat.getValueAt(tbObat.getSelectedRow(),57).toString().contains("Mukosa Kering")){
                GastrointestinalMulut.setSelectedItem("Mukosa Kering");
            }else if(tbObat.getValueAt(tbObat.getSelectedRow(),57).toString().contains("Bibir Pucat")){
                GastrointestinalMulut.setSelectedItem("Bibir Pucat");
            }else{
                GastrointestinalMulut.setSelectedItem("Lain-lain");
            }
            KetGastrointestinalMulut.setText(tbObat.getValueAt(tbObat.getSelectedRow(),57).toString().replaceAll(GastrointestinalMulut.getSelectedItem().toString()+", ",""));
            if(tbObat.getValueAt(tbObat.getSelectedRow(),58).toString().contains("TAK")){
                GastrointestinalLidah.setSelectedItem("TAK");
            }else if(tbObat.getValueAt(tbObat.getSelectedRow(),58).toString().contains("Kotor")){
                GastrointestinalLidah.setSelectedItem("Kotor");
            }else if(tbObat.getValueAt(tbObat.getSelectedRow(),58).toString().contains("Gerak Asimetris")){
                GastrointestinalLidah.setSelectedItem("Gerak Asimetris");
            }else{
                GastrointestinalLidah.setSelectedItem("Lain-lain");
            }
            KetGastrointestinalLidah.setText(tbObat.getValueAt(tbObat.getSelectedRow(),58).toString().replaceAll(GastrointestinalLidah.getSelectedItem().toString()+", ",""));
            if(tbObat.getValueAt(tbObat.getSelectedRow(),59).toString().contains("TAK")){
                GastrointestinalGigi.setSelectedItem("TAK");
            }else if(tbObat.getValueAt(tbObat.getSelectedRow(),59).toString().contains("Karies")){
                GastrointestinalGigi.setSelectedItem("Karies");
            }else if(tbObat.getValueAt(tbObat.getSelectedRow(),59).toString().contains("Goyang")){
                GastrointestinalGigi.setSelectedItem("Goyang");
            }else{
                GastrointestinalGigi.setSelectedItem("Lain-lain");
            }
            KetGastrointestinalGigi.setText(tbObat.getValueAt(tbObat.getSelectedRow(),59).toString().replaceAll(GastrointestinalGigi.getSelectedItem().toString()+", ",""));
            if(tbObat.getValueAt(tbObat.getSelectedRow(),60).toString().contains("TAK")){
                GastrointestinalTenggorakan.setSelectedItem("TAK");
            }else if(tbObat.getValueAt(tbObat.getSelectedRow(),60).toString().contains("Gangguan Menelan")){
                GastrointestinalTenggorakan.setSelectedItem("Gangguan Menelan");
            }else if(tbObat.getValueAt(tbObat.getSelectedRow(),60).toString().contains("Sakit Menelan")){
                GastrointestinalTenggorakan.setSelectedItem("Sakit Menelan");
            }else{
                GastrointestinalTenggorakan.setSelectedItem("Lain-lain");
            }
            KetGastrointestinalTenggorakan.setText(tbObat.getValueAt(tbObat.getSelectedRow(),60).toString().replaceAll(GastrointestinalTenggorakan.getSelectedItem().toString()+", ",""));
            if(tbObat.getValueAt(tbObat.getSelectedRow(),61).toString().contains("Supel")){
                GastrointestinalAbdomen.setSelectedItem("Supel");
            }else if(tbObat.getValueAt(tbObat.getSelectedRow(),61).toString().contains("Asictes")){
                GastrointestinalAbdomen.setSelectedItem("Asictes");
            }else if(tbObat.getValueAt(tbObat.getSelectedRow(),61).toString().contains("Tegang")){
                GastrointestinalAbdomen.setSelectedItem("Tegang");
            }else if(tbObat.getValueAt(tbObat.getSelectedRow(),61).toString().contains("Nyeri Tekan/Lepas")){
                GastrointestinalAbdomen.setSelectedItem("Nyeri Tekan/Lepas");
            }else{
                GastrointestinalAbdomen.setSelectedItem("Lain-lain");
            }
            KetGastrointestinalAbdomen.setText(tbObat.getValueAt(tbObat.getSelectedRow(),61).toString().replaceAll(GastrointestinalAbdomen.getSelectedItem().toString()+", ",""));
            GastrointestinalUsus.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),62).toString());
            GastrointestinalAnus.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),63).toString());
            NeurologiSensorik.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),64).toString());
            if(tbObat.getValueAt(tbObat.getSelectedRow(),65).toString().contains("TAK")){
                NeurologiPenglihatan.setSelectedItem("TAK");
            }else{
                NeurologiPenglihatan.setSelectedItem("Ada Kelainan");
            }
            KetNeurologiPenglihatan.setText(tbObat.getValueAt(tbObat.getSelectedRow(),65).toString().replaceAll(NeurologiPenglihatan.getSelectedItem().toString()+", ",""));
            NeurologiAlatBantuPenglihatan.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),66).toString());
            NeurologiMotorik.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),67).toString());
            NeurologiPendengaran.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),68).toString());
            if(tbObat.getValueAt(tbObat.getSelectedRow(),69).toString().contains("Jelas")){
                NeurologiBicara.setSelectedItem("Jelas");
            }else{
                NeurologiBicara.setSelectedItem("Tidak Jelas");
            }
            KetNeurologiBicara.setText(tbObat.getValueAt(tbObat.getSelectedRow(),69).toString().replaceAll(NeurologiBicara.getSelectedItem().toString()+", ",""));
            NeurologiOtot.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),70).toString());
            IntegumentKulit.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),71).toString());
            IntegumentWarnaKulit.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),72).toString());
            IntegumentTurgor.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),73).toString());
            IntegumentDecubitus.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),74).toString());
            if(tbObat.getValueAt(tbObat.getSelectedRow(),75).toString().contains("Tidak Ada")){
                MuskuloskletalOedema.setSelectedItem("Tidak Ada");
            }else{
                MuskuloskletalOedema.setSelectedItem("Ada");
            }
            KetMuskuloskletalOedema.setText(tbObat.getValueAt(tbObat.getSelectedRow(),75).toString().replaceAll(MuskuloskletalOedema.getSelectedItem().toString()+", ",""));
            MuskuloskletalPegerakanSendi.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),76).toString());
            MuskuloskletalKekuatanOtot.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),77).toString());
            if(tbObat.getValueAt(tbObat.getSelectedRow(),78).toString().contains("Tidak Ada")){
                MuskuloskletalFraktur.setSelectedItem("Tidak Ada");
            }else{
                MuskuloskletalFraktur.setSelectedItem("Ada");
            }
            KetMuskuloskletalFraktur.setText(tbObat.getValueAt(tbObat.getSelectedRow(),78).toString().replaceAll(MuskuloskletalFraktur.getSelectedItem().toString()+", ",""));
            if(tbObat.getValueAt(tbObat.getSelectedRow(),79).toString().contains("Tidak Ada")){
                MuskuloskletalNyeriSendi.setSelectedItem("Tidak Ada");
            }else{
                MuskuloskletalNyeriSendi.setSelectedItem("Ada");
            }
            KetMuskuloskletalNyeriSendi.setText(tbObat.getValueAt(tbObat.getSelectedRow(),79).toString().replaceAll(MuskuloskletalNyeriSendi.getSelectedItem().toString()+", ",""));
            BAB.setText(tbObat.getValueAt(tbObat.getSelectedRow(),80).toString());
            XBAB.setText(tbObat.getValueAt(tbObat.getSelectedRow(),81).toString());
            KBAB.setText(tbObat.getValueAt(tbObat.getSelectedRow(),82).toString());
            WBAB.setText(tbObat.getValueAt(tbObat.getSelectedRow(),83).toString());
            BAK.setText(tbObat.getValueAt(tbObat.getSelectedRow(),84).toString());
            XBAK.setText(tbObat.getValueAt(tbObat.getSelectedRow(),85).toString());
            WBAK.setText(tbObat.getValueAt(tbObat.getSelectedRow(),86).toString());
            LBAK.setText(tbObat.getValueAt(tbObat.getSelectedRow(),87).toString());
            PolaAktifitasMandi.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),88).toString());
            PolaAktifitasMakan.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),89).toString());
            PolaAktifitasBerpakaian.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),90).toString());
            PolaAktifitasEliminasi.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),91).toString());
            PolaAktifitasBerpindah.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),92).toString());
            PolaNutrisiPorsi.setText(tbObat.getValueAt(tbObat.getSelectedRow(),93).toString());
            PolaNutrisiFrekuensi.setText(tbObat.getValueAt(tbObat.getSelectedRow(),94).toString());
            PolaNutrisiJenis.setText(tbObat.getValueAt(tbObat.getSelectedRow(),95).toString());
            PolaTidurLama.setText(tbObat.getValueAt(tbObat.getSelectedRow(),96).toString());
            PolaTidurGangguan.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),97).toString());
            AktifitasSehari2.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),98).toString());
//            if(tbObat.getValueAt(tbObat.getSelectedRow(),99).toString().contains("TAK")){
//                Berjalan.setSelectedItem("TAK");
//            }else if(tbObat.getValueAt(tbObat.getSelectedRow(),99).toString().contains("Penurunan Kekuatan/ROM")){
//                Berjalan.setSelectedItem("Penurunan Kekuatan/ROM");
//            }else if(tbObat.getValueAt(tbObat.getSelectedRow(),99).toString().contains("Paralisis")){
//                Berjalan.setSelectedItem("Paralisis");
//            }else if(tbObat.getValueAt(tbObat.getSelectedRow(),99).toString().contains("Sering Jatuh")){
//                Berjalan.setSelectedItem("Sering Jatuh");
//            }else if(tbObat.getValueAt(tbObat.getSelectedRow(),99).toString().contains("Deformitas")){
//                Berjalan.setSelectedItem("Deformitas");
//            }else if(tbObat.getValueAt(tbObat.getSelectedRow(),99).toString().contains("Hilang keseimbangan")){
//                Berjalan.setSelectedItem("Hilang keseimbangan");
//            }else if(tbObat.getValueAt(tbObat.getSelectedRow(),99).toString().contains("Riwayat Patah Tulang")){
//                Berjalan.setSelectedItem("Riwayat Patah Tulang");
//            }else{
//                Berjalan.setSelectedItem("Lain-lain");
//            }
            KeteranganBerjalan.setText(tbObat.getValueAt(tbObat.getSelectedRow(),99).toString().replaceAll(Berjalan.getSelectedItem().toString()+", ",""));
            Aktifitas.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),100).toString());
            AlatAmbulasi.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),101).toString());
            if(tbObat.getValueAt(tbObat.getSelectedRow(),102).toString().contains("TAK")){
                EkstrimitasAtas.setSelectedItem("TAK");
            }else if(tbObat.getValueAt(tbObat.getSelectedRow(),102).toString().contains("Lemah")){
                EkstrimitasAtas.setSelectedItem("Lemah");
            }else if(tbObat.getValueAt(tbObat.getSelectedRow(),102).toString().contains("Oedema")){
                EkstrimitasAtas.setSelectedItem("Oedema");
            }else if(tbObat.getValueAt(tbObat.getSelectedRow(),102).toString().contains("Tidak Simetris")){
                EkstrimitasAtas.setSelectedItem("Tidak Simetris");
            }else{
                EkstrimitasAtas.setSelectedItem("Lain-lain");
            }
            KeteranganEkstrimitasAtas.setText(tbObat.getValueAt(tbObat.getSelectedRow(),102).toString().replaceAll(EkstrimitasAtas.getSelectedItem().toString()+", ",""));
            if(tbObat.getValueAt(tbObat.getSelectedRow(),103).toString().contains("TAK")){
                EkstrimitasBawah.setSelectedItem("TAK");
            }else if(tbObat.getValueAt(tbObat.getSelectedRow(),103).toString().contains("Varises")){
                EkstrimitasBawah.setSelectedItem("Varises");
            }else if(tbObat.getValueAt(tbObat.getSelectedRow(),103).toString().contains("Oedema")){
                EkstrimitasBawah.setSelectedItem("Oedema");
            }else if(tbObat.getValueAt(tbObat.getSelectedRow(),103).toString().contains("Tidak Simetris")){
                EkstrimitasBawah.setSelectedItem("Tidak Simetris");
            }else{
                EkstrimitasBawah.setSelectedItem("Lain-lain");
            }
            KeteranganEkstrimitasBawah.setText(tbObat.getValueAt(tbObat.getSelectedRow(),103).toString().replaceAll(EkstrimitasBawah.getSelectedItem().toString()+", ",""));
            if(tbObat.getValueAt(tbObat.getSelectedRow(),104).toString().contains("Tidak Ada Kesulitan")){
                KemampuanMenggenggam.setSelectedItem("Tidak Ada Kesulitan");
            }else if(tbObat.getValueAt(tbObat.getSelectedRow(),104).toString().contains("Terakhir")){
                KemampuanMenggenggam.setSelectedItem("Terakhir");
            }else{
                KemampuanMenggenggam.setSelectedItem("Lain-lain");
            }
            KeteranganKemampuanMenggenggam.setText(tbObat.getValueAt(tbObat.getSelectedRow(),104).toString().replaceAll(KemampuanMenggenggam.getSelectedItem().toString()+", ",""));
            if(tbObat.getValueAt(tbObat.getSelectedRow(),105).toString().contains("Tidak Ada Kesulitan")){
                KemampuanKoordinasi.setSelectedItem("Tidak Ada Kesulitan");
            }else{
                KemampuanKoordinasi.setSelectedItem("Ada Masalah");
            }
            KeteranganKemampuanKoordinasi.setText(tbObat.getValueAt(tbObat.getSelectedRow(),105).toString().replaceAll(KemampuanKoordinasi.getSelectedItem().toString()+", ",""));
            KesimpulanGangguanFungsi.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),106).toString());
            KondisiPsikologis.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),107).toString());
            if(tbObat.getValueAt(tbObat.getSelectedRow(),108).toString().contains("Tidak Ada Masalah")){
                AdakahPerilaku.setSelectedItem("Tidak Ada Masalah");
            }else if(tbObat.getValueAt(tbObat.getSelectedRow(),108).toString().contains("Perilaku Kekerasan")){
                AdakahPerilaku.setSelectedItem("Perilaku Kekerasan");
            }else if(tbObat.getValueAt(tbObat.getSelectedRow(),108).toString().contains("Gangguan Efek")){
                AdakahPerilaku.setSelectedItem("Gangguan Efek");
            }else if(tbObat.getValueAt(tbObat.getSelectedRow(),108).toString().contains("Gangguan Memori")){
                AdakahPerilaku.setSelectedItem("Gangguan Memori");
            }else if(tbObat.getValueAt(tbObat.getSelectedRow(),108).toString().contains("Halusinasi")){
                AdakahPerilaku.setSelectedItem("Halusinasi");
            }else if(tbObat.getValueAt(tbObat.getSelectedRow(),108).toString().contains("Kecenderungan Percobaan Bunuh Diri")){
                AdakahPerilaku.setSelectedItem("Kecenderungan Percobaan Bunuh Diri");
            }else{
                AdakahPerilaku.setSelectedItem("Lain-lain");
            }
            KeteranganAdakahPerilaku.setText(tbObat.getValueAt(tbObat.getSelectedRow(),108).toString().replaceAll(AdakahPerilaku.getSelectedItem().toString()+", ",""));
            GangguanJiwa.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),109).toString());
            HubunganAnggotaKeluarga.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),110).toString());
            Agama.setText(tbObat.getValueAt(tbObat.getSelectedRow(),111).toString());
            if(tbObat.getValueAt(tbObat.getSelectedRow(),112).toString().contains("Sendiri")){
                TinggalDengan.setSelectedItem("Sendiri");
            }else if(tbObat.getValueAt(tbObat.getSelectedRow(),112).toString().contains("Orang Tua")){
                TinggalDengan.setSelectedItem("Orang Tua");
            }else if(tbObat.getValueAt(tbObat.getSelectedRow(),112).toString().contains("Suami/Istri")){
                TinggalDengan.setSelectedItem("Suami/Istri");
            }else if(tbObat.getValueAt(tbObat.getSelectedRow(),112).toString().contains("Keluarga")){
                TinggalDengan.setSelectedItem("Keluarga");
            }else{
                TinggalDengan.setSelectedItem("Lain-lain");
            }
            KeteranganTinggalDengan.setText(tbObat.getValueAt(tbObat.getSelectedRow(),112).toString().replaceAll(TinggalDengan.getSelectedItem().toString()+", ",""));
            PekerjaanPasien.setText(tbObat.getValueAt(tbObat.getSelectedRow(),113).toString());
            CaraBayar.setText(tbObat.getValueAt(tbObat.getSelectedRow(),114).toString());
            if(tbObat.getValueAt(tbObat.getSelectedRow(),115).toString().contains("Tidak Ada")){
                NilaiKepercayaan.setSelectedItem("Tidak Ada");
            }else{
                NilaiKepercayaan.setSelectedItem("Ada");
            }
            KeteranganNilaiKepercayaan.setText(tbObat.getValueAt(tbObat.getSelectedRow(),115).toString().replaceAll(NilaiKepercayaan.getSelectedItem().toString()+", ",""));
            Bahasa.setText(tbObat.getValueAt(tbObat.getSelectedRow(),116).toString());
            PendidikanPasien.setText(tbObat.getValueAt(tbObat.getSelectedRow(),117).toString());
            PendidikanPJ.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),118).toString());
            if(tbObat.getValueAt(tbObat.getSelectedRow(),119).toString().contains("Pasien")){
                EdukasiPsikolgis.setSelectedItem("Pasien");
            }else{
                EdukasiPsikolgis.setSelectedItem("Keluarga");
            }
            KeteranganEdukasiPsikologis.setText(tbObat.getValueAt(tbObat.getSelectedRow(),119).toString().replaceAll(EdukasiPsikolgis.getSelectedItem().toString()+", ",""));
            Nyeri.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),120).toString());
            if(tbObat.getValueAt(tbObat.getSelectedRow(),121).toString().contains("Proses Penyakit")){
                Provokes.setSelectedItem("Proses Penyakit");
            }else if(tbObat.getValueAt(tbObat.getSelectedRow(),121).toString().contains("Benturan")){
                Provokes.setSelectedItem("Benturan");
            }else{
                Provokes.setSelectedItem("Lain-lain");
            }
            KetProvokes.setText(tbObat.getValueAt(tbObat.getSelectedRow(),121).toString().replaceAll(Provokes.getSelectedItem().toString()+", ",""));
            if(tbObat.getValueAt(tbObat.getSelectedRow(),122).toString().contains("Seperti Tertusuk")){
                Quality.setSelectedItem("Seperti Tertusuk");
            }else if(tbObat.getValueAt(tbObat.getSelectedRow(),122).toString().contains("Berdenyut")){
                Quality.setSelectedItem("Berdenyut");
            }else if(tbObat.getValueAt(tbObat.getSelectedRow(),122).toString().contains("Teriris")){
                Quality.setSelectedItem("Teriris");
            }else if(tbObat.getValueAt(tbObat.getSelectedRow(),122).toString().contains("Tertindih")){
                Quality.setSelectedItem("Tertindih");
            }else if(tbObat.getValueAt(tbObat.getSelectedRow(),122).toString().contains("Tertiban")){
                Quality.setSelectedItem("Tertiban");
            }else{
                Quality.setSelectedItem("Lain-lain");
            }
            KetQuality.setText(tbObat.getValueAt(tbObat.getSelectedRow(),122).toString().replaceAll(Quality.getSelectedItem().toString()+", ",""));
            Lokasi.setText(tbObat.getValueAt(tbObat.getSelectedRow(),123).toString());
            Menyebar.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),124).toString());
            SkalaNyeri.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),125).toString());
            Durasi.setText(tbObat.getValueAt(tbObat.getSelectedRow(),126).toString());
            if(tbObat.getValueAt(tbObat.getSelectedRow(),127).toString().contains("Istirahat")){
                NyeriHilang.setSelectedItem("Istirahat");
            }else if(tbObat.getValueAt(tbObat.getSelectedRow(),127).toString().contains("Medengar Musik")){
                NyeriHilang.setSelectedItem("Medengar Musik");
            }else{
                NyeriHilang.setSelectedItem("Minum Obat");
            }
            KetNyeri.setText(tbObat.getValueAt(tbObat.getSelectedRow(),127).toString().replaceAll(NyeriHilang.getSelectedItem().toString()+", ",""));
            if(tbObat.getValueAt(tbObat.getSelectedRow(),128).toString().contains("Ya")){
                PadaDokter.setSelectedItem("Ya");
            }else{
                PadaDokter.setSelectedItem("Tidak");
            }
            KetPadaDokter.setText(tbObat.getValueAt(tbObat.getSelectedRow(),128).toString().replaceAll(PadaDokter.getSelectedItem().toString()+", ",""));
            SkalaResiko1.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),129).toString());
            NilaiResiko1.setText(tbObat.getValueAt(tbObat.getSelectedRow(),130).toString());
            SkalaResiko2.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),131).toString());
            NilaiResiko2.setText(tbObat.getValueAt(tbObat.getSelectedRow(),132).toString());
            SkalaResiko3.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),133).toString());
            NilaiResiko3.setText(tbObat.getValueAt(tbObat.getSelectedRow(),134).toString());
            SkalaResiko4.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),135).toString());
            NilaiResiko4.setText(tbObat.getValueAt(tbObat.getSelectedRow(),136).toString());
            SkalaResiko5.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),137).toString());
            NilaiResiko5.setText(tbObat.getValueAt(tbObat.getSelectedRow(),138).toString());
            SkalaResiko6.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),139).toString());
            NilaiResiko6.setText(tbObat.getValueAt(tbObat.getSelectedRow(),140).toString());
            NilaiResikoTotal.setText(tbObat.getValueAt(tbObat.getSelectedRow(),141).toString());
            isTotalResikoJatuh();
            SkalaSydney1.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),142).toString());
            NilaiSydney1.setText(tbObat.getValueAt(tbObat.getSelectedRow(),143).toString());
            SkalaSydney2.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),144).toString());
            NilaiSydney2.setText(tbObat.getValueAt(tbObat.getSelectedRow(),145).toString());
            SkalaSydney3.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),146).toString());
            NilaiSydney3.setText(tbObat.getValueAt(tbObat.getSelectedRow(),147).toString());
            SkalaSydney4.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),148).toString());
            NilaiSydney4.setText(tbObat.getValueAt(tbObat.getSelectedRow(),149).toString());
            SkalaSydney5.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),150).toString());
            NilaiSydney5.setText(tbObat.getValueAt(tbObat.getSelectedRow(),151).toString());
            SkalaSydney6.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),152).toString());
            NilaiSydney6.setText(tbObat.getValueAt(tbObat.getSelectedRow(),153).toString());
            SkalaSydney7.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),154).toString());
            NilaiSydney7.setText(tbObat.getValueAt(tbObat.getSelectedRow(),155).toString());
            SkalaSydney8.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),156).toString());
            NilaiSydney8.setText(tbObat.getValueAt(tbObat.getSelectedRow(),157).toString());
            SkalaSydney9.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),158).toString());
            NilaiSydney9.setText(tbObat.getValueAt(tbObat.getSelectedRow(),159).toString());
            SkalaSydney10.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),160).toString());
            NilaiSydney10.setText(tbObat.getValueAt(tbObat.getSelectedRow(),161).toString());
            SkalaSydney11.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),162).toString());
            NilaiSydney11.setText(tbObat.getValueAt(tbObat.getSelectedRow(),163).toString());
            NilaiSydneyTotal.setText(tbObat.getValueAt(tbObat.getSelectedRow(),164).toString());
            isTotalResikoSydney();
            SkalaGizi1.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),165).toString());
            NilaiGizi1.setText(tbObat.getValueAt(tbObat.getSelectedRow(),166).toString());
            SkalaGizi2.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),167).toString());
            NilaiGizi2.setText(tbObat.getValueAt(tbObat.getSelectedRow(),168).toString());
            NilaiGiziTotal.setText(tbObat.getValueAt(tbObat.getSelectedRow(),169).toString());
            DiagnosaKhususGizi.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),170).toString());
            KeteranganDiagnosaKhususGizi.setText(tbObat.getValueAt(tbObat.getSelectedRow(),171).toString());
            DiketahuiDietisen.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),172).toString());
            KeteranganDiketahuiDietisen.setText(tbObat.getValueAt(tbObat.getSelectedRow(),173).toString());
            Rencana.setText(tbObat.getValueAt(tbObat.getSelectedRow(),174).toString());
            
            try {
                Valid.tabelKosong(tabModeMasalah);
                
                ps=koneksi.prepareStatement(
                        "select master_masalah_keperawatan.kode_masalah,master_masalah_keperawatan.nama_masalah from master_masalah_keperawatan "+
                        "inner join penilaian_awal_keperawatan_ranap_masalah on penilaian_awal_keperawatan_ranap_masalah.kode_masalah=master_masalah_keperawatan.kode_masalah "+
                        "where penilaian_awal_keperawatan_ranap_masalah.no_rawat=? order by penilaian_awal_keperawatan_ranap_masalah.kode_masalah");
                try {
                    ps.setString(1,tbObat.getValueAt(tbObat.getSelectedRow(),0).toString());
                    rs=ps.executeQuery();
                    while(rs.next()){
                        tabModeMasalah.addRow(new Object[]{true,rs.getString(1),rs.getString(2)});
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
            
            try {
                Valid.tabelKosong(tabModeRencana);
                
                ps=koneksi.prepareStatement(
                        "select master_rencana_keperawatan.kode_rencana,master_rencana_keperawatan.rencana_keperawatan from master_rencana_keperawatan "+
                        "inner join penilaian_awal_keperawatan_ranap_rencana on penilaian_awal_keperawatan_ranap_rencana.kode_rencana=master_rencana_keperawatan.kode_rencana "+
                        "where penilaian_awal_keperawatan_ranap_rencana.no_rawat=? order by penilaian_awal_keperawatan_ranap_rencana.kode_rencana");
                try {
                    ps.setString(1,tbObat.getValueAt(tbObat.getSelectedRow(),0).toString());
                    rs=ps.executeQuery();
                    while(rs.next()){
                        tabModeRencana.addRow(new Object[]{true,rs.getString(1),rs.getString(2)});
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
            
            Valid.SetTgl2(TglAsuhan,tbObat.getValueAt(tbObat.getSelectedRow(),11).toString());
        }
    }

    private void isRawat() {
        try {
            ps=koneksi.prepareStatement(
                    "select pasien.nm_pasien, if(pasien.jk='L','Laki-Laki','Perempuan') as jk,pasien.tgl_lahir,pasien.agama,"+
                    "bahasa_pasien.nama_bahasa,pasien.pnd,pasien.pekerjaan "+
                    "from pasien inner join bahasa_pasien on bahasa_pasien.id=pasien.bahasa_pasien "+
                    "where pasien.no_rkm_medis=?");
            try {
                ps.setString(1,TNoRM.getText());
                rs=ps.executeQuery();
                if(rs.next()){
                    TPasien.setText(rs.getString("nm_pasien"));
                    Jk.setText(rs.getString("jk"));
                    TglLahir.setText(rs.getString("tgl_lahir"));
                    Agama.setText(rs.getString("agama"));
                    Bahasa.setText(rs.getString("nama_bahasa"));
                    PendidikanPasien.setText(rs.getString("pnd"));
                    PekerjaanPasien.setText(rs.getString("pekerjaan"));
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
    
    public void setNoRm(String norwt,Date tgl2,String carabayar,String norm) {
        TNoRw.setText(norwt);
        TNoRM.setText(norm);
        TCari.setText(norwt);
        Sequel.cariIsi("select reg_periksa.tgl_registrasi from reg_periksa where reg_periksa.no_rawat='"+norwt+"'", DTPCari1);
        CaraBayar.setText(carabayar);
        DTPCari2.setDate(tgl2);    
        isRawat(); 
        isPasien();
    }
    
    private void isPasien() {
        Sequel.cariIsi("select pasien.nm_pasien from pasien where pasien.no_rkm_medis='"+TNoRM.getText()+"' ",TPasien);
        Sequel.cariIsi("select date_format(pasien.tgl_lahir,'%d-%m-%Y') from pasien where pasien.no_rkm_medis=? ",TglLahir,TNoRM.getText());
    }
    
    
    
    public void isCek(){
        BtnSimpan.setEnabled(akses.getpenilaian_awal_keperawatan_ranap());
        BtnHapus.setEnabled(akses.getpenilaian_awal_keperawatan_ranap());
        BtnEdit.setEnabled(akses.getpenilaian_awal_keperawatan_ranap());
        BtnEdit.setEnabled(akses.getpenilaian_awal_keperawatan_ranap()); 
        BtnTambahMasalah.setEnabled(akses.getmaster_masalah_keperawatan()); 
        BtnTambahRencana.setEnabled(akses.getmaster_rencana_keperawatan()); 
        if(akses.getjml2()>=1){
            KdPetugas.setEditable(false);
            BtnPetugas.setEnabled(false);
            KdPetugas.setText(akses.getkode());
            Sequel.cariIsi("select petugas.nama from petugas where petugas.nip=?", NmPetugas,KdPetugas.getText());
            if(NmPetugas.getText().equals("")){
                KdPetugas.setText("");
                JOptionPane.showMessageDialog(null,"User login bukan petugas...!!");
            }
        }            
    }

    public void setTampil(){
       TabRawat.setSelectedIndex(1);
       tampil();
    }
    
    private void isMenu(){
        if(ChkAccor.isSelected()==true){
            ChkAccor.setVisible(false);
            PanelAccor.setPreferredSize(new Dimension(470,HEIGHT));
            FormMenu.setVisible(true);  
            FormMasalahRencana.setVisible(true);
            ChkAccor.setVisible(true);
        }else if(ChkAccor.isSelected()==false){    
            ChkAccor.setVisible(false);
            PanelAccor.setPreferredSize(new Dimension(15,HEIGHT));
            FormMenu.setVisible(false);  
            FormMasalahRencana.setVisible(false);  
            ChkAccor.setVisible(true);
        }
    }

    private void getMasalah() {
        if(tbObat.getSelectedRow()!= -1){
            TNoRM1.setText(tbObat.getValueAt(tbObat.getSelectedRow(),1).toString());
            TPasien1.setText(tbObat.getValueAt(tbObat.getSelectedRow(),2).toString());
            DetailRencana.setText(tbObat.getValueAt(tbObat.getSelectedRow(),174).toString());
            
            try {
                Valid.tabelKosong(tabModeDetailMasalah);
                ps=koneksi.prepareStatement(
                        "select master_masalah_keperawatan.kode_masalah,master_masalah_keperawatan.nama_masalah from master_masalah_keperawatan "+
                        "inner join penilaian_awal_keperawatan_ranap_masalah on penilaian_awal_keperawatan_ranap_masalah.kode_masalah=master_masalah_keperawatan.kode_masalah "+
                        "where penilaian_awal_keperawatan_ranap_masalah.no_rawat=? order by penilaian_awal_keperawatan_ranap_masalah.kode_masalah");
                try {
                    ps.setString(1,tbObat.getValueAt(tbObat.getSelectedRow(),0).toString());
                    rs=ps.executeQuery();
                    while(rs.next()){
                        tabModeDetailMasalah.addRow(new Object[]{rs.getString(1),rs.getString(2)});
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
            
            try {
                Valid.tabelKosong(tabModeDetailRencana);
                ps=koneksi.prepareStatement(
                        "select master_rencana_keperawatan.kode_rencana,master_rencana_keperawatan.rencana_keperawatan from master_rencana_keperawatan "+
                        "inner join penilaian_awal_keperawatan_ranap_rencana on penilaian_awal_keperawatan_ranap_rencana.kode_rencana=master_rencana_keperawatan.kode_rencana "+
                        "where penilaian_awal_keperawatan_ranap_rencana.no_rawat=? order by penilaian_awal_keperawatan_ranap_rencana.kode_rencana");
                try {
                    ps.setString(1,tbObat.getValueAt(tbObat.getSelectedRow(),0).toString());
                    rs=ps.executeQuery();
                    while(rs.next()){
                        tabModeDetailRencana.addRow(new Object[]{rs.getString(1),rs.getString(2)});
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
    }
   
    

    private void hapus() {
        if(Sequel.queryu2tf("delete from penilaian_awal_keperawatan_ranap_kemoterapi where no_rawat=?",1,new String[]{
            tbObat.getValueAt(tbObat.getSelectedRow(),0).toString()
        })==true){
            TNoRM1.setText("");
            TPasien1.setText("");
            ChkAccor.setSelected(false);
            isMenu();
            tampil();
        }else{
            JOptionPane.showMessageDialog(null,"Gagal menghapus..!!");
        }
    }

    private void ganti() {
        if(Sequel.mengedittf("penilaian_awal_keperawatan_ranap_kemoterapi","no_rawat=?","no_rawat=?,tanggal=?,informasi=?,ket_informasi=?,tiba_diruang_rawat=?,kasus_trauma=?,cara_masuk=?,rps=?,rpd=?,rpk=?,rpo=?,riwayat_pembedahan=?,riwayat_dirawat_dirs=?,alat_bantu_dipakai=?,riwayat_kehamilan=?,riwayat_kehamilan_perkiraan=?,riwayat_tranfusi=?,riwayat_alergi=?,riwayat_merokok=?,riwayat_merokok_jumlah=?,riwayat_alkohol=?,riwayat_alkohol_jumlah=?,riwayat_narkoba=?,riwayat_olahraga=?,pemeriksaan_mental=?,pemeriksaan_keadaan_umum=?,pemeriksaan_gcs=?,pemeriksaan_td=?,pemeriksaan_nadi=?,pemeriksaan_rr=?,pemeriksaan_suhu=?,pemeriksaan_spo2=?,pemeriksaan_bb=?,pemeriksaan_tb=?,pemeriksaan_susunan_kepala=?,pemeriksaan_susunan_kepala_keterangan=?,pemeriksaan_susunan_wajah=?,pemeriksaan_susunan_wajah_keterangan=?,pemeriksaan_susunan_leher=?,pemeriksaan_susunan_kejang=?,pemeriksaan_susunan_kejang_keterangan=?,pemeriksaan_susunan_sensorik=?,pemeriksaan_kardiovaskuler_denyut_nadi=?,pemeriksaan_kardiovaskuler_sirkulasi=?,pemeriksaan_kardiovaskuler_sirkulasi_keterangan=?,pemeriksaan_kardiovaskuler_pulsasi=?,pemeriksaan_respirasi_pola_nafas=?,pemeriksaan_respirasi_retraksi=?,pemeriksaan_respirasi_suara_nafas=?,pemeriksaan_respirasi_volume_pernafasan=?,pemeriksaan_respirasi_jenis_pernafasan=?,pemeriksaan_respirasi_jenis_pernafasan_keterangan=?,pemeriksaan_respirasi_irama_nafas=?,pemeriksaan_respirasi_batuk=?,pemeriksaan_gastrointestinal_mulut=?,pemeriksaan_gastrointestinal_mulut_keterangan=?,pemeriksaan_gastrointestinal_gigi=?,pemeriksaan_gastrointestinal_gigi_keterangan=?,pemeriksaan_gastrointestinal_lidah=?,pemeriksaan_gastrointestinal_lidah_keterangan=?,pemeriksaan_gastrointestinal_tenggorokan=?,pemeriksaan_gastrointestinal_tenggorokan_keterangan=?,pemeriksaan_gastrointestinal_abdomen=?,pemeriksaan_gastrointestinal_abdomen_keterangan=?,pemeriksaan_gastrointestinal_peistatik_usus=?,pemeriksaan_gastrointestinal_anus=?,pemeriksaan_neurologi_pengelihatan=?,pemeriksaan_neurologi_pengelihatan_keterangan=?,pemeriksaan_neurologi_alat_bantu_penglihatan=?,pemeriksaan_neurologi_pendengaran=?,pemeriksaan_neurologi_bicara=?,pemeriksaan_neurologi_bicara_keterangan=?,pemeriksaan_neurologi_sensorik=?,pemeriksaan_neurologi_motorik=?,pemeriksaan_neurologi_kekuatan_otot=?,pemeriksaan_integument_warnakulit=?,pemeriksaan_integument_turgor=?,pemeriksaan_integument_kulit=?,pemeriksaan_integument_dekubitas=?,pemeriksaan_muskuloskletal_pergerakan_sendi=?,pemeriksaan_muskuloskletal_kekauatan_otot=?,pemeriksaan_muskuloskletal_nyeri_sendi=?,pemeriksaan_muskuloskletal_nyeri_sendi_keterangan=?,pemeriksaan_muskuloskletal_oedema=?,pemeriksaan_muskuloskletal_oedema_keterangan=?,pemeriksaan_muskuloskletal_fraktur=?,pemeriksaan_muskuloskletal_fraktur_keterangan=?,pemeriksaan_eliminasi_bab_frekuensi_jumlah=?,pemeriksaan_eliminasi_bab_frekuensi_durasi=?,pemeriksaan_eliminasi_bab_konsistensi=?,pemeriksaan_eliminasi_bab_warna=?,pemeriksaan_eliminasi_bak_frekuensi_jumlah=?,pemeriksaan_eliminasi_bak_frekuensi_durasi=?,pemeriksaan_eliminasi_bak_warna=?,pemeriksaan_eliminasi_bak_lainlain=?,pola_aktifitas_makanminum=?,pola_aktifitas_mandi=?,pola_aktifitas_eliminasi=?,pola_aktifitas_berpakaian=?,pola_aktifitas_berpindah=?,pola_nutrisi_frekuesi_makan=?,pola_nutrisi_jenis_makanan=?,pola_nutrisi_porsi_makan=?,pola_tidur_lama_tidur=?,pola_tidur_gangguan=?,pengkajian_fungsi_kemampuan_sehari=?,pengkajian_fungsi_aktifitas=?,pengkajian_fungsi_berjalan=?,pengkajian_fungsi_berjalan_keterangan=?,pengkajian_fungsi_ambulasi=?,pengkajian_fungsi_ekstrimitas_atas=?,pengkajian_fungsi_ekstrimitas_atas_keterangan=?,pengkajian_fungsi_ekstrimitas_bawah=?,pengkajian_fungsi_ekstrimitas_bawah_keterangan=?,pengkajian_fungsi_menggenggam=?,pengkajian_fungsi_menggenggam_keterangan=?,pengkajian_fungsi_koordinasi=?,pengkajian_fungsi_koordinasi_keterangan=?,pengkajian_fungsi_kesimpulan=?,riwayat_psiko_kondisi_psiko=?,riwayat_psiko_gangguan_jiwa=?,riwayat_psiko_perilaku=?,riwayat_psiko_perilaku_keterangan=?,riwayat_psiko_hubungan_keluarga=?,riwayat_psiko_tinggal=?,riwayat_psiko_tinggal_keterangan=?,riwayat_psiko_nilai_kepercayaan=?,riwayat_psiko_nilai_kepercayaan_keterangan=?,riwayat_psiko_pendidikan_pj=?,riwayat_psiko_edukasi_diberikan=?,riwayat_psiko_edukasi_diberikan_keterangan=?,penilaian_nyeri=?,penilaian_nyeri_penyebab=?,penilaian_nyeri_ket_penyebab=?,penilaian_nyeri_kualitas=?,penilaian_nyeri_ket_kualitas=?,penilaian_nyeri_lokasi=?,penilaian_nyeri_menyebar=?,penilaian_nyeri_skala=?,penilaian_nyeri_waktu=?,penilaian_nyeri_hilang=?,penilaian_nyeri_ket_hilang=?,penilaian_nyeri_diberitahukan_dokter=?,penilaian_nyeri_jam_diberitahukan_dokter=?,penilaian_jatuhmorse_skala1=?,penilaian_jatuhmorse_nilai1=?,penilaian_jatuhmorse_skala2=?,penilaian_jatuhmorse_nilai2=?,penilaian_jatuhmorse_skala3=?,penilaian_jatuhmorse_nilai3=?,penilaian_jatuhmorse_skala4=?,penilaian_jatuhmorse_nilai4=?,penilaian_jatuhmorse_skala5=?,penilaian_jatuhmorse_nilai5=?,penilaian_jatuhmorse_skala6=?,penilaian_jatuhmorse_nilai6=?,penilaian_jatuhmorse_totalnilai=?,penilaian_jatuhsydney_skala1=?,penilaian_jatuhsydney_nilai1=?,penilaian_jatuhsydney_skala2=?,penilaian_jatuhsydney_nilai2=?,penilaian_jatuhsydney_skala3=?,penilaian_jatuhsydney_nilai3=?,penilaian_jatuhsydney_skala4=?,penilaian_jatuhsydney_nilai4=?,penilaian_jatuhsydney_skala5=?,penilaian_jatuhsydney_nilai5=?,penilaian_jatuhsydney_skala6=?,penilaian_jatuhsydney_nilai6=?,penilaian_jatuhsydney_skala7=?,penilaian_jatuhsydney_nilai7=?,penilaian_jatuhsydney_skala8=?,penilaian_jatuhsydney_nilai8=?,penilaian_jatuhsydney_skala9=?,penilaian_jatuhsydney_nilai9=?,penilaian_jatuhsydney_skala10=?,penilaian_jatuhsydney_nilai10=?,penilaian_jatuhsydney_skala11=?,penilaian_jatuhsydney_nilai11=?,penilaian_jatuhsydney_totalnilai=?,skrining_gizi1=?,nilai_gizi1=?,skrining_gizi2=?,nilai_gizi2=?,nilai_total_gizi=?,skrining_gizi_diagnosa_khusus=?,skrining_gizi_ket_diagnosa_khusus=?,skrining_gizi_diketahui_dietisen=?,skrining_gizi_jam_diketahui_dietisen=?,rencana=?,nip1=?,nip2=?,kd_dokter=?",194,new String[]{
                TNoRw.getText(),Valid.SetTgl(TglAsuhan.getSelectedItem()+"")+" "+TglAsuhan.getSelectedItem().toString().substring(11,19),Anamnesis.getSelectedItem().toString(),KetAnamnesis.getText(),TibadiRuang.getSelectedItem().toString(),MacamKasus.getSelectedItem().toString(), 
                    CaraMasuk.getSelectedItem().toString(),RPS.getText(),RPD.getText(),RPK.getText(),RPO.getText(),RPembedahan.getText(),RDirawatRS.getText(),AlatBantuDipakai.getSelectedItem().toString(),SedangMenyusui.getSelectedItem().toString(),KetSedangMenyusui.getText(),RTranfusi.getText(), 
                    Alergi.getText(),KebiasaanMerokok.getSelectedItem().toString(),KebiasaanJumlahRokok.getText(),KebiasaanAlkohol.getSelectedItem().toString(),KebiasaanJumlahAlkohol.getText(),KebiasaanNarkoba.getSelectedItem().toString(),OlahRaga.getSelectedItem().toString(),KesadaranMental.getText(), 
                    KeadaanMentalUmum.getSelectedItem().toString(),GCS.getText(),TD.getText(),Nadi.getText(),RR.getText(),Suhu.getText(),SpO2.getText(),BB.getText(),TB.getText(),SistemSarafKepala.getSelectedItem().toString(),KetSistemSarafKepala.getText(),SistemSarafWajah.getSelectedItem().toString(), 
                    KetSistemSarafWajah.getText(),SistemSarafLeher.getSelectedItem().toString(),SistemSarafKejang.getSelectedItem().toString(),KetSistemSarafKejang.getText(),SistemSarafSensorik.getSelectedItem().toString(),KardiovaskularDenyutNadi.getSelectedItem().toString(),KardiovaskularSirkulasi.getSelectedItem().toString(), 
                    KetKardiovaskularSirkulasi.getText(),KardiovaskularPulsasi.getSelectedItem().toString(),RespirasiPolaNafas.getSelectedItem().toString(),RespirasiRetraksi.getSelectedItem().toString(),RespirasiSuaraNafas.getSelectedItem().toString(),RespirasiVolume.getSelectedItem().toString(),
                    RespirasiJenisPernafasan.getSelectedItem().toString(),KetRespirasiJenisPernafasan.getText(),RespirasiIrama.getSelectedItem().toString(),RespirasiBatuk.getSelectedItem().toString(),GastrointestinalMulut.getSelectedItem().toString(),KetGastrointestinalMulut.getText(),
                    GastrointestinalGigi.getSelectedItem().toString(),KetGastrointestinalGigi.getText(),GastrointestinalLidah.getSelectedItem().toString(),KetGastrointestinalLidah.getText(),GastrointestinalTenggorakan.getSelectedItem().toString(),KetGastrointestinalTenggorakan.getText(), 
                    GastrointestinalAbdomen.getSelectedItem().toString(),KetGastrointestinalAbdomen.getText(),GastrointestinalUsus.getSelectedItem().toString(),GastrointestinalAnus.getSelectedItem().toString(),NeurologiPenglihatan.getSelectedItem().toString(),KetNeurologiPenglihatan.getText(), 
                    NeurologiAlatBantuPenglihatan.getSelectedItem().toString(),NeurologiPendengaran.getSelectedItem().toString(),NeurologiBicara.getSelectedItem().toString(),KetNeurologiBicara.getText(),NeurologiSensorik.getSelectedItem().toString(),NeurologiMotorik.getSelectedItem().toString(), 
                    NeurologiOtot.getSelectedItem().toString(),IntegumentWarnaKulit.getSelectedItem().toString(),IntegumentTurgor.getSelectedItem().toString(),IntegumentKulit.getSelectedItem().toString(),IntegumentDecubitus.getSelectedItem().toString(),MuskuloskletalPegerakanSendi.getSelectedItem().toString(), 
                    MuskuloskletalKekuatanOtot.getSelectedItem().toString(),MuskuloskletalNyeriSendi.getSelectedItem().toString(),KetMuskuloskletalNyeriSendi.getText(),MuskuloskletalOedema.getSelectedItem().toString(),KetMuskuloskletalOedema.getText(),MuskuloskletalFraktur.getSelectedItem().toString(), 
                    KetMuskuloskletalFraktur.getText(),BAB.getText(),XBAB.getText(),KBAB.getText(),WBAB.getText(),BAK.getText(),XBAK.getText(),WBAK.getText(),LBAK.getText(),PolaAktifitasMakan.getSelectedItem().toString(),PolaAktifitasMandi.getSelectedItem().toString(),PolaAktifitasEliminasi.getSelectedItem().toString(), 
                    PolaAktifitasBerpakaian.getSelectedItem().toString(),PolaAktifitasBerpindah.getSelectedItem().toString(),PolaNutrisiFrekuensi.getText(),PolaNutrisiJenis.getText(),PolaNutrisiPorsi.getText(),PolaTidurLama.getText(),PolaTidurGangguan.getSelectedItem().toString(),AktifitasSehari2.getSelectedItem().toString(), 
                    Aktifitas.getSelectedItem().toString(),Berjalan.getSelectedItem().toString(),KeteranganBerjalan.getText(),AlatAmbulasi.getSelectedItem().toString(),EkstrimitasAtas.getSelectedItem().toString(),KeteranganEkstrimitasAtas.getText(),EkstrimitasBawah.getSelectedItem().toString(),
                    KeteranganEkstrimitasBawah.getText(),KemampuanMenggenggam.getSelectedItem().toString(),KeteranganKemampuanMenggenggam.getText(),KemampuanKoordinasi.getSelectedItem().toString(),KeteranganKemampuanKoordinasi.getText(),KesimpulanGangguanFungsi.getSelectedItem().toString(),
                    KondisiPsikologis.getSelectedItem().toString(),GangguanJiwa.getSelectedItem().toString(),AdakahPerilaku.getSelectedItem().toString(),KeteranganAdakahPerilaku.getText(),HubunganAnggotaKeluarga.getSelectedItem().toString(),TinggalDengan.getSelectedItem().toString(),KeteranganTinggalDengan.getText(),
                    NilaiKepercayaan.getSelectedItem().toString(),KeteranganNilaiKepercayaan.getText(),PendidikanPJ.getSelectedItem().toString(),EdukasiPsikolgis.getSelectedItem().toString(),KeteranganEdukasiPsikologis.getText(),Nyeri.getSelectedItem().toString(),Provokes.getSelectedItem().toString(),KetProvokes.getText(), 
                    Quality.getSelectedItem().toString(),KetQuality.getText(),Lokasi.getText(),Menyebar.getSelectedItem().toString(),SkalaNyeri.getSelectedItem().toString(),Durasi.getText(),NyeriHilang.getSelectedItem().toString(),KetNyeri.getText(),PadaDokter.getSelectedItem().toString(), 
                    KetPadaDokter.getText(),SkalaResiko1.getSelectedItem().toString(),NilaiResiko1.getText(),SkalaResiko2.getSelectedItem().toString(),NilaiResiko2.getText(),SkalaResiko3.getSelectedItem().toString(),NilaiResiko3.getText(),SkalaResiko4.getSelectedItem().toString(),NilaiResiko4.getText(), 
                    SkalaResiko5.getSelectedItem().toString(),NilaiResiko5.getText(),SkalaResiko6.getSelectedItem().toString(),NilaiResiko6.getText(),NilaiResikoTotal.getText(),SkalaSydney1.getSelectedItem().toString(),NilaiSydney1.getText(),SkalaSydney2.getSelectedItem().toString(),NilaiSydney2.getText(),
                    SkalaSydney3.getSelectedItem().toString(),NilaiSydney3.getText(),SkalaSydney4.getSelectedItem().toString(),NilaiSydney4.getText(),SkalaSydney5.getSelectedItem().toString(),NilaiSydney5.getText(),SkalaSydney6.getSelectedItem().toString(),NilaiSydney6.getText(), 
                    SkalaSydney7.getSelectedItem().toString(),NilaiSydney7.getText(),SkalaSydney8.getSelectedItem().toString(),NilaiSydney8.getText(),SkalaSydney9.getSelectedItem().toString(),NilaiSydney9.getText(),SkalaSydney10.getSelectedItem().toString(),NilaiSydney10.getText(), 
                    SkalaSydney11.getSelectedItem().toString(),NilaiSydney11.getText(),NilaiSydneyTotal.getText(),SkalaGizi1.getSelectedItem().toString(),NilaiGizi1.getText(),SkalaGizi2.getSelectedItem().toString(),NilaiGizi2.getText(),NilaiGiziTotal.getText(),DiagnosaKhususGizi.getSelectedItem().toString(),
                    KeteranganDiagnosaKhususGizi.getText(),DiketahuiDietisen.getSelectedItem().toString(),KeteranganDiketahuiDietisen.getText(),Rencana.getText(),KdPetugas.getText(),KdPetugas2.getText(),KdDPJP.getText(),tbObat.getValueAt(tbObat.getSelectedRow(),0).toString()
             })==true){
                Sequel.meghapus("penilaian_awal_keperawatan_ranap_masalah","no_rawat",tbObat.getValueAt(tbObat.getSelectedRow(),0).toString());
                for (i = 0; i < tbMasalahKeperawatan.getRowCount(); i++) {
                    if(tbMasalahKeperawatan.getValueAt(i,0).toString().equals("true")){
                        Sequel.menyimpan2("penilaian_awal_keperawatan_ranap_masalah","?,?",2,new String[]{TNoRw.getText(),tbMasalahKeperawatan.getValueAt(i,1).toString()});
                    }
                }
                Sequel.meghapus("penilaian_awal_keperawatan_ranap_rencana","no_rawat",tbObat.getValueAt(tbObat.getSelectedRow(),0).toString());
                for (i = 0; i < tbRencanaKeperawatan.getRowCount(); i++) {
                    if(tbRencanaKeperawatan.getValueAt(i,0).toString().equals("true")){
                        Sequel.menyimpan2("penilaian_awal_keperawatan_ranap_rencana","?,?",2,new String[]{TNoRw.getText(),tbRencanaKeperawatan.getValueAt(i,1).toString()});
                    }
                }
                getMasalah();
                tampil();
                DetailRencana.setText(Rencana.getText());
                emptTeks();
                TabRawat.setSelectedIndex(1);
        }
    }
    
    private void isTotalResikoJatuh(){
        try {
            NilaiResikoTotal.setText((Integer.parseInt(NilaiResiko1.getText())+Integer.parseInt(NilaiResiko2.getText())+Integer.parseInt(NilaiResiko3.getText())+Integer.parseInt(NilaiResiko4.getText())+Integer.parseInt(NilaiResiko5.getText())+Integer.parseInt(NilaiResiko6.getText()))+"");
            if(Integer.parseInt(NilaiResikoTotal.getText())<25){
                TingkatResiko.setText("Tingkat Resiko : Risiko Rendah (0-24), Tindakan : Intervensi pencegahan risiko jatuh standar");
            }else if(Integer.parseInt(NilaiResikoTotal.getText())<45){
                TingkatResiko.setText("Tingkat Resiko : Risiko Sedang (25-44), Tindakan : Intervensi pencegahan risiko jatuh standar");
            }else if(Integer.parseInt(NilaiResikoTotal.getText())>=45){
                TingkatResiko.setText("Tingkat Resiko : Risiko Tinggi (> 45), Tindakan : Intervensi pencegahan risiko jatuh standar dan Intervensi risiko jatuh tinggi");
            }
        } catch (Exception e) {
            NilaiResikoTotal.setText("0");
            TingkatResiko.setText("Tingkat Resiko : Risiko Rendah (0-24), Tindakan : Intervensi pencegahan risiko jatuh standar");
        }
    }
    
    private void isTotalGizi(){
        try {
            NilaiGiziTotal.setText((Integer.parseInt(NilaiGizi1.getText())+Integer.parseInt(NilaiGizi2.getText()))+"");
        } catch (Exception e) {
            NilaiGiziTotal.setText("0");
        }
    }
    
    private void tampilMasalah() {
        try{
            Valid.tabelKosong(tabModeMasalah);
            file=new File("./cache/masalahkeperawatan.iyem");
            file.createNewFile();
            fileWriter = new FileWriter(file);
            iyem="";
            ps=koneksi.prepareStatement("select * from master_masalah_keperawatan order by master_masalah_keperawatan.kode_masalah");
            try {
                rs=ps.executeQuery();
                while(rs.next()){
                    tabModeMasalah.addRow(new Object[]{false,rs.getString(1),rs.getString(2)});
                    iyem=iyem+"{\"KodeMasalah\":\""+rs.getString(1)+"\",\"NamaMasalah\":\""+rs.getString(2)+"\"},";
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
            fileWriter.write("{\"masalahkeperawatan\":["+iyem.substring(0,iyem.length()-1)+"]}");
            fileWriter.flush();
            fileWriter.close();
            iyem=null;
        }catch(Exception e){
            System.out.println("Notifikasi : "+e);
        }
    }
    
    private void tampilMasalah2() {
        try{
            jml=0;
            for(i=0;i<tbMasalahKeperawatan.getRowCount();i++){
                if(tbMasalahKeperawatan.getValueAt(i,0).toString().equals("true")){
                    jml++;
                }
            }

            pilih=null;
            pilih=new boolean[jml]; 
            kode=null;
            kode=new String[jml];
            masalah=null;
            masalah=new String[jml];

            index=0;        
            for(i=0;i<tbMasalahKeperawatan.getRowCount();i++){
                if(tbMasalahKeperawatan.getValueAt(i,0).toString().equals("true")){
                    pilih[index]=true;
                    kode[index]=tbMasalahKeperawatan.getValueAt(i,1).toString();
                    masalah[index]=tbMasalahKeperawatan.getValueAt(i,2).toString();
                    index++;
                }
            } 

            Valid.tabelKosong(tabModeMasalah);

            for(i=0;i<jml;i++){
                tabModeMasalah.addRow(new Object[] {
                    pilih[i],kode[i],masalah[i]
                });
            }
            
            myObj = new FileReader("./cache/masalahkeperawatan.iyem");
            root = mapper.readTree(myObj);
            response = root.path("masalahkeperawatan");
            if(response.isArray()){
                for(JsonNode list:response){
                    if(list.path("KodeMasalah").asText().toLowerCase().contains(TCariMasalah.getText().toLowerCase())||list.path("NamaMasalah").asText().toLowerCase().contains(TCariMasalah.getText().toLowerCase())){
                        tabModeMasalah.addRow(new Object[]{
                            false,list.path("KodeMasalah").asText(),list.path("NamaMasalah").asText()
                        });                    
                    }
                }
            }
            myObj.close();
        }catch(Exception e){
            System.out.println("Notifikasi : "+e);
        }
    }
    
    private void tampilRencana() {
        try{
            file=new File("./cache/rencanakeperawatan.iyem");
            file.createNewFile();
            fileWriter = new FileWriter(file);
            iyem="";
            ps=koneksi.prepareStatement("select * from master_rencana_keperawatan order by master_rencana_keperawatan.kode_rencana");
            try {
                rs=ps.executeQuery();
                while(rs.next()){
                    iyem=iyem+"{\"KodeMasalah\":\""+rs.getString(1)+"\",\"KodeRencana\":\""+rs.getString(2)+"\",\"NamaRencana\":\""+rs.getString(3)+"\"},";
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
            fileWriter.write("{\"rencanakeperawatan\":["+iyem.substring(0,iyem.length()-1)+"]}");
            fileWriter.flush();
            fileWriter.close();
            iyem=null;
        }catch(Exception e){
            System.out.println("Notifikasi : "+e);
        }
    }
    
    private void tampilRencana2() {
        try{
            jml=0;
            for(i=0;i<tbRencanaKeperawatan.getRowCount();i++){
                if(tbRencanaKeperawatan.getValueAt(i,0).toString().equals("true")){
                    jml++;
                }
            }

            pilih=null;
            pilih=new boolean[jml]; 
            kode=null;
            kode=new String[jml];
            masalah=null;
            masalah=new String[jml];

            index=0;        
            for(i=0;i<tbRencanaKeperawatan.getRowCount();i++){
                if(tbRencanaKeperawatan.getValueAt(i,0).toString().equals("true")){
                    pilih[index]=true;
                    kode[index]=tbRencanaKeperawatan.getValueAt(i,1).toString();
                    masalah[index]=tbRencanaKeperawatan.getValueAt(i,2).toString();
                    index++;
                }
            } 

            Valid.tabelKosong(tabModeRencana);

            for(i=0;i<jml;i++){
                tabModeRencana.addRow(new Object[] {
                    pilih[i],kode[i],masalah[i]
                });
            }

            myObj = new FileReader("./cache/rencanakeperawatan.iyem");
            root = mapper.readTree(myObj);
            response = root.path("rencanakeperawatan");
            if(response.isArray()){
                for(i=0;i<tbMasalahKeperawatan.getRowCount();i++){
                    if(tbMasalahKeperawatan.getValueAt(i,0).toString().equals("true")){
                        for(JsonNode list:response){
                            if(list.path("KodeMasalah").asText().toLowerCase().equals(tbMasalahKeperawatan.getValueAt(i,1).toString())&&
                                    list.path("NamaRencana").asText().toLowerCase().contains(TCariRencana.getText().toLowerCase())){
                                tabModeRencana.addRow(new Object[]{
                                    false,list.path("KodeRencana").asText(),list.path("NamaRencana").asText()
                                });                    
                            }
                        }
                    }
                }
            }
            myObj.close();
        }catch(Exception e){
            System.out.println("Notifikasi : "+e);
        }
    }

    private void isTotalResikoSydney() {
        try {
            NilaiSydneyTotal.setText((Integer.parseInt(NilaiSydney1.getText())+Integer.parseInt(NilaiSydney2.getText())+Integer.parseInt(NilaiSydney3.getText())+Integer.parseInt(NilaiSydney4.getText())+Integer.parseInt(NilaiSydney5.getText())+Integer.parseInt(NilaiSydney6.getText())+Integer.parseInt(NilaiSydney7.getText())+Integer.parseInt(NilaiSydney8.getText())+Integer.parseInt(NilaiSydney9.getText())+Integer.parseInt(NilaiSydney10.getText()))+"");
            if(Integer.parseInt(NilaiSydneyTotal.getText())<3){
                TingkatSydney.setText("Ringan");
            }else if(Integer.parseInt(NilaiSydneyTotal.getText())==4){
                TingkatSydney.setText("Sedang");
            }else if(Integer.parseInt(NilaiSydneyTotal.getText())==5){
                TingkatSydney.setText("Sedang");
            }else if(Integer.parseInt(NilaiSydneyTotal.getText())==6){
                TingkatSydney.setText("Sedang");
            }else if(Integer.parseInt(NilaiSydneyTotal.getText())>=7){
                TingkatSydney.setText("Berat");
            }
        } catch (Exception e) {
            NilaiSydneyTotal.setText("0");
            TingkatSydney.setText("Ringan");
        }
    }
}
