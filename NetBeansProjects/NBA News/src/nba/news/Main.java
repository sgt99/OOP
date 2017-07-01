/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nba.news;


import crawler.CrawlerGetVideos;
import crawler.CrawlerGetNoticias;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.List;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Stream;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import org.w3c.dom.css.RGBColor;
import crawler.CrawlerGetFotos;
import crawler.CrawlerInfoPessoais;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;
import java.net.URISyntaxException;
import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import org.json.JSONException;
/**
 *
 * @author victor
 */
public class Main extends javax.swing.JFrame {
    
    private int year;
    private String actualCategory;
    private  Map<String,Integer> lines;
    private  Map<String, String[]> columns;
    private int xMouse;
    private int yMouse;
    
  
    /**
     * Creates new form Main
     */
    public Main() {
        settingEnvironment();
        initComponents();
        loading.setVisible(false);
        menu_home.setBackground(new Color(220,4,2));
        menu_home.setForeground(new Color(255,255,255));
        this.year = 2017;
        this.actualCategory = "APG";
        loadStatistics(this.year, this.actualCategory);
        this.jTable1.getTableHeader().setDefaultRenderer(new HeaderColor());
        slideShow();
        try{
            getNews();
            getVideos();
        }catch(MalformedURLException e){
            
        }
        
    }
    
    public void slideShow(){
        LinkedList<String> bgs = new LinkedList();
        bgs.add("https://img.bleacherreport.net/cms/media/image/c1/70/d4/2d/e59c/4175/ac18/a3468cbf7041/crop_exact_3-2.jpg?h=650&w=850&q=70&crop_x=center&crop_y=top");
        bgs.add("https://img.bleacherreport.net/img/images/photos/003/681/592/hi-res-7fc0b8fe92797e3918503ae39cd37e7f_crop_north.jpg?h=650&w=850&q=70&crop_x=center&crop_y=top");
        bgs.add("https://img.bleacherreport.net/img/images/photos/003/681/587/hi-res-e0ac49ac0529bc0fca4843c44ee48316_crop_north.jpg?h=650&w=850&q=70&crop_x=center&crop_y=top");
        bgs.add("https://img.bleacherreport.net/cms/media/image/3b/36/bf/62/0975/435c/bccf/12df79dbef11/crop_exact_AP_920926153835.jpg?h=650&w=850&q=70&crop_x=center&crop_y=top");
        bgs.add("https://img.bleacherreport.net/cms/media/image/06/1c/f9/d4/e87b/491b/9c49/d666b864b974/crop_exact_CP3.jpg?h=650&w=850&q=70&crop_x=center&crop_y=top");
        bgs.add("https://img.bleacherreport.net/img/slides/photos/004/252/694/hi-res-fe8e397346e5f7d801c514f0b4adcfb5_crop_exact.jpg?h=650&w=850&q=70&crop_x=center&crop_y=top");
        int x = 0;
        Timer tm = new Timer(15000, new ActionListener() {
            @Override
             public void actionPerformed(ActionEvent e){
                int randomNum = 0 + (int)(Math.random() * bgs.size()); 
                  try{
                    URL url = new URL(bgs.get(randomNum));
                    ImageIcon newIco = new ImageIcon((new ImageIcon(url).getImage()));
                    bg_main.setIcon(newIco);
                  }catch(MalformedURLException err){
                      
                  }
//                x++;
//                if(x >= bgs.size())
//                x = 0;
             }
        });
        tm.start();
    }
   
    public void getNews() throws MalformedURLException{
       CrawlerGetNoticias listNews = new CrawlerGetNoticias();
        listNews.getNoticias();
        LinkedList<String> titles = listNews.getTitulos();
        LinkedList<String> thumbs = listNews.getThumb();
        LinkedList<String> links = listNews.getLinks();
        
        if(titles.size() > 0){
            JLabel newsThumbs[] = new JLabel[titles.size()];
            JLabel newsTitles[] = new JLabel[titles.size()];
            for(int i = 0; i < titles.size(); i++){
                newsThumbs[i] = new JLabel();
                newsTitles[i] = new JLabel();
                
                 try{
                    String link = links.get(i);
                    URL url = new URL(thumbs.get(i).replace("h=40", "h=170").replace("w=60", "w=220"));
                    ImageIcon newIco = new ImageIcon((new ImageIcon(url).getImage()));
                    newsThumbs[i].setIcon(newIco);
                    newsThumbs[i].setBounds(i * 220 + i * 30 + 20, 60, 220, 170);
                    newsThumbs[i].setVisible(true);
                    newsThumbs[i].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    newsThumbs[i].addMouseListener(new MouseAdapter(){
                        @Override
                        public void mouseClicked(MouseEvent e){
                            try{
                            Desktop.getDesktop().browse(new URI(link));
                            }catch(URISyntaxException err){

                            }catch(IOException err){

                            }
                        }
                    });
                    String title = titles.get(i).length() > 40 ? titles.get(i).substring(0, 40) + "..." : titles.get(0);
                    newsTitles[i].setText(title);
                    newsTitles[i].setBounds(i * 220 + i * 30 + 20, 230, 220, 20);
                    newsTitles[i].setForeground(new Color(255, 255, 255));
                    newsTitles[i].setVisible(true);
                    newsTitles[i].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    newsTitles[i].addMouseListener(new MouseAdapter(){
                    @Override
                    public void mouseClicked(MouseEvent e){
                        try{
                        Desktop.getDesktop().browse(new URI(link));
                        }catch(URISyntaxException err){
                            
                        }catch(IOException err){
                            
                        }
                    }
                });
                    container_main.add(newsThumbs[i]);
                    container_main.add(newsTitles[i]);
                    }catch(MalformedURLException e){

                    }
        
            }
        }
         
   }
    
    
     public void getVideos() throws MalformedURLException{
        CrawlerGetVideos videos = new CrawlerGetVideos();
        videos.GetVideo(4);
        LinkedList<String> titles = videos.getTitles();
        LinkedList<String> thumbs = videos.getThumbs();
        LinkedList<String> links = videos.getLinks();
        
        if(titles.size() > 0){
            JLabel videosThumbs[] = new JLabel[titles.size()];
            JLabel videosTitles[] = new JLabel[titles.size()];
            for(int i = 0; i < titles.size(); i++){
                videosThumbs[i] = new JLabel();
                videosTitles[i] = new JLabel();
                
                 try{
                    String link = links.get(i);
                    URL url = new URL(thumbs.get(i));
                    ImageIcon newIco = new ImageIcon((new ImageIcon(url).getImage()));
                    videosThumbs[i].setIcon(newIco);
                    videosThumbs[i].setBounds(i * 220 + i * 30 + 20, 290, 220, 170);
                    videosThumbs[i].setVisible(true);
                    videosThumbs[i].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    videosThumbs[i].addMouseListener(new MouseAdapter(){
                        @Override
                        public void mouseClicked(MouseEvent e){
                            try{
                            Desktop.getDesktop().browse(new URI(link));
                            }catch(URISyntaxException err){

                            }catch(IOException err){

                            }
                        }
                    });
                    String title = titles.get(i).length() > 25 ? titles.get(i).substring(0, 25) + "..." : titles.get(0);
                    videosTitles[i].setText(title);
                    videosTitles[i].setBounds(i * 220 + i * 30 + 20, 430, 220, 20);
                    videosTitles[i].setForeground(new Color(255, 255, 255));
                    videosTitles[i].setVisible(true);
                    videosTitles[i].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    videosTitles[i].addMouseListener(new MouseAdapter(){
                    @Override
                    public void mouseClicked(MouseEvent e){
                        try{
                        Desktop.getDesktop().browse(new URI(link));
                        }catch(URISyntaxException err){
                            
                        }catch(IOException err){
                            
                        }
                    }
                });
                    container_main.add(videosThumbs[i]);
                    container_main.add(videosTitles[i]);
                    }catch(MalformedURLException e){

                    }
        
            }
        }
         
   }

    
    public void settingEnvironment(){
       
        this.lines = new HashMap<String, Integer>();
        this.lines.put("PTS", 11);
        this.lines.put("RPG", 11);
        this.lines.put("FG%", 14);
        this.lines.put("FT%", 9);
        this.lines.put("3P%", 14);
        this.lines.put("APG", 10);
        this.lines.put("STPG", 12);
        this.lines.put("BLKPG", 9); 
        this.lines.put("PFPG", 10); 
        this.lines.put("MPG", 5);
        this.lines.put("DBLDBL", 11);
        
        
        // Columns
        this.columns = new HashMap<String, String[]>();
        String[] pts = {"RK", "PLAYER", "TEAM", "GP", "MPG", "PTS", "FGM-FGA", "FG%", "3PM-3PA", "3P%", "FTM-FTA", "FT%"};
        this.columns.put("PTS", pts);
        
        String[] rpg = {"RK", "PLAYER", "TEAM", "GP", "MPG", "OFF", "ORPG", "DEF", "DRPG", "REB", "RPG", "RP48"};
        this.columns.put("RPG", rpg);
        
        String[] fg = {"RK", "PLAYER", "TEAM", "GP", "PPG", "FGM", "FGA", "FGM", "FGA", "FG%", "2PM", "2PA", "2P%", "PPS", "FG%"};
        this.columns.put("FG%", fg);
        
        String[] ft = {"RK", "PLAYER", "TEAM", "GP", "PPG", "FTM", "FTA", "FTM", "FTA", "FT%"};
        this.columns.put("FT%", ft);
        
        String[] trp = {"RK", "PLAYER", "TEAM", "GP", "PPG", "3PM", "3PA", "3PM", "3PA", "3P%", "2PM", "2PA", "2P%", "PPS", "FG%"};
        this.columns.put("3P%", trp);
        
        String[] apg = {"RK", "PLAYER", "TEAM", "GP", "MPG", "AST", "APG", "TO", "TOPG", "AP48M", "AST/TO"};
        this.columns.put("APG", apg);
        
        String[] stpg = {"RK", "PLAYER", "TEAM", "GP", "MPG", "STL", "STPG", "STP48M", "TO", "TOPG", "PF", "ST/TO", "ST/PF"};
        this.columns.put("STPG", stpg);
        
        String[] blkpg = {"RK", "PLAYER", "TEAM", "GP", "MPG", "BLK", "PF", "BLKPG", "BLKP48M", "BLK/ṔF"};
        this.columns.put("BLKPG", blkpg);
        
        String[] pfpg = {"RK", "PLAYER", "TEAM", "GP", "MPG", "PF", "PFPG", "FLAG", "TECH", "EJECT"};
        this.columns.put("PFPG", pfpg);
        
        String[] mpg = {"RK", "PLAYER", "TEAM", "GP", "MIN", "MPG"};
        this.columns.put("MPG", mpg);
        
        String[] dbldbl = {"RK", "PLAYER", "TEAM", "GP", "MPG", "PPG", "RPG", "APG", "STPG", "BLKPG", "DBLDBL", "TRIDBL"};
        this.columns.put("DBLDBL", dbldbl);
        
        
    }
  
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel9 = new javax.swing.JPanel();
        minWindow = new javax.swing.JLabel();
        closeWindow = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        menu_home = new javax.swing.JLabel();
        menu_statis = new javax.swing.JLabel();
        menu_teams = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        search = new javax.swing.JTextField();
        jPanel10 = new javax.swing.JPanel();
        Main = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        container_main = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        bg_main = new javax.swing.JLabel();
        Statistics = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        apg = new javax.swing.JLabel();
        dd = new javax.swing.JLabel();
        fg = new javax.swing.JLabel();
        rpg = new javax.swing.JLabel();
        blkpg = new javax.swing.JLabel();
        stpg = new javax.swing.JLabel();
        ft = new javax.swing.JLabel();
        pfpg = new javax.swing.JLabel();
        mpg = new javax.swing.JLabel();
        pts = new javax.swing.JLabel();
        trp = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        y2016 = new javax.swing.JLabel();
        y2014 = new javax.swing.JLabel();
        y2015 = new javax.swing.JLabel();
        y2011 = new javax.swing.JLabel();
        y2013 = new javax.swing.JLabel();
        y2012 = new javax.swing.JLabel();
        y2010 = new javax.swing.JLabel();
        y2004 = new javax.swing.JLabel();
        y2009 = new javax.swing.JLabel();
        y2008 = new javax.swing.JLabel();
        y2007 = new javax.swing.JLabel();
        y2006 = new javax.swing.JLabel();
        y2005 = new javax.swing.JLabel();
        y2003 = new javax.swing.JLabel();
        y2002 = new javax.swing.JLabel();
        y2017 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        Search = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        result_search = new javax.swing.JLabel();
        loading = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        container_search = new javax.swing.JPanel();
        Teams = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel13 = new javax.swing.JPanel();
        result_search1 = new javax.swing.JLabel();
        result_search2 = new javax.swing.JLabel();
        leste_team = new javax.swing.JPanel();
        oeste_team = new javax.swing.JPanel();
        TeamProfile = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        nameTeam = new javax.swing.JLabel();
        picTeam = new javax.swing.JLabel();
        win_loses = new javax.swing.JLabel();
        jPanel19 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        pts_num = new javax.swing.JLabel();
        jLabel62 = new javax.swing.JLabel();
        ass_num = new javax.swing.JLabel();
        jLabel64 = new javax.swing.JLabel();
        rbt_num = new javax.swing.JLabel();
        jLabel66 = new javax.swing.JLabel();
        lvd_num = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        result_search3 = new javax.swing.JLabel();
        result_search4 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel17 = new javax.swing.JPanel();
        jLabel38 = new javax.swing.JLabel();
        pic_1 = new javax.swing.JLabel();
        name_1 = new javax.swing.JLabel();
        num_1 = new javax.swing.JLabel();
        pic_2 = new javax.swing.JLabel();
        name_2 = new javax.swing.JLabel();
        num_2 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        pic_3 = new javax.swing.JLabel();
        name_3 = new javax.swing.JLabel();
        num_3 = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        pos_1 = new javax.swing.JLabel();
        pos_2 = new javax.swing.JLabel();
        pos_3 = new javax.swing.JLabel();
        jPanel18 = new javax.swing.JPanel();
        name_4 = new javax.swing.JLabel();
        pic_4 = new javax.swing.JLabel();
        num_4 = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        jSeparator5 = new javax.swing.JSeparator();
        pic_5 = new javax.swing.JLabel();
        num_5 = new javax.swing.JLabel();
        name_5 = new javax.swing.JLabel();
        pic_6 = new javax.swing.JLabel();
        num_6 = new javax.swing.JLabel();
        name_6 = new javax.swing.JLabel();
        jLabel59 = new javax.swing.JLabel();
        jLabel60 = new javax.swing.JLabel();
        jLabel61 = new javax.swing.JLabel();
        pos_4 = new javax.swing.JLabel();
        pos_5 = new javax.swing.JLabel();
        pos_6 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        teamRoster = new javax.swing.JTable();
        jPanel14 = new javax.swing.JPanel();
        playerProfile = new javax.swing.JPanel();
        jPanel20 = new javax.swing.JPanel();
        namePlayer = new javax.swing.JLabel();
        picPlayer = new javax.swing.JLabel();
        subTitle = new javax.swing.JLabel();
        playerBorn = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        playerDrafted = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        playerCollege = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        playerExp = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        jPanel22 = new javax.swing.JPanel();
        result_search6 = new javax.swing.JLabel();
        jPanel25 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        playerStatis_2 = new javax.swing.JTable();
        jScrollPane5 = new javax.swing.JScrollPane();
        playerStatis_1 = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        playerStatis_3 = new javax.swing.JTable();
        result_search7 = new javax.swing.JLabel();
        container_photos = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel9.setBackground(new java.awt.Color(14, 14, 14));
        jPanel9.setBorder(null);
        jPanel9.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                jPanel9MouseDragged(evt);
            }
        });
        jPanel9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jPanel9MousePressed(evt);
            }
        });
        jPanel9.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        minWindow.setFont(new java.awt.Font("Ubuntu", 1, 14)); // NOI18N
        minWindow.setForeground(new java.awt.Color(254, 254, 254));
        minWindow.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        minWindow.setText("_");
        minWindow.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                minWindowMouseClicked(evt);
            }
        });
        jPanel9.add(minWindow, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 0, 21, 20));

        closeWindow.setFont(new java.awt.Font("Ubuntu", 1, 14)); // NOI18N
        closeWindow.setForeground(new java.awt.Color(254, 254, 254));
        closeWindow.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        closeWindow.setText("X");
        closeWindow.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                closeWindowMouseClicked(evt);
            }
        });
        jPanel9.add(closeWindow, new org.netbeans.lib.awtextra.AbsoluteConstraints(977, 0, 21, 20));

        getContentPane().add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1000, 20));

        jPanel3.setBackground(new java.awt.Color(9, 9, 9));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        menu_home.setBackground(new java.awt.Color(9, 9, 9));
        menu_home.setFont(new java.awt.Font("Ubuntu Light", 0, 16)); // NOI18N
        menu_home.setForeground(new java.awt.Color(254, 254, 254));
        menu_home.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        menu_home.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/home.png"))); // NOI18N
        menu_home.setText("INÍCIO");
        menu_home.setIconTextGap(5);
        menu_home.setOpaque(true);
        menu_home.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                menu_homeMouseClicked(evt);
            }
        });
        jPanel3.add(menu_home, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 160, 150, 40));

        menu_statis.setBackground(new java.awt.Color(9, 9, 9));
        menu_statis.setFont(new java.awt.Font("Ubuntu Light", 0, 16)); // NOI18N
        menu_statis.setForeground(new java.awt.Color(254, 254, 254));
        menu_statis.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        menu_statis.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/bar-graph.png"))); // NOI18N
        menu_statis.setText("ESTATÍSTICAS");
        menu_statis.setOpaque(true);
        menu_statis.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                menu_statisMouseClicked(evt);
            }
        });
        jPanel3.add(menu_statis, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 200, 150, 40));

        menu_teams.setBackground(new java.awt.Color(9, 9, 9));
        menu_teams.setFont(new java.awt.Font("Ubuntu Light", 0, 16)); // NOI18N
        menu_teams.setForeground(new java.awt.Color(254, 254, 254));
        menu_teams.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        menu_teams.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/baseball.png"))); // NOI18N
        menu_teams.setText("TIMES");
        menu_teams.setOpaque(true);
        menu_teams.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                menu_teamsMouseClicked(evt);
            }
        });
        jPanel3.add(menu_teams, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 240, 150, 40));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/images/logo.png"))); // NOI18N
        jPanel3.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 20, 150, 700));

        jPanel4.setBackground(new java.awt.Color(254, 254, 254));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setIcon(new javax.swing.ImageIcon("/home/victor/NetBeansProjects/NBA News/src/icons/search.png")); // NOI18N
        jPanel4.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, -1, -1));
        jPanel4.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 34, 322, -1));

        search.setBackground(new java.awt.Color(254, 254, 254));
        search.setFont(new java.awt.Font("Ubuntu Light", 0, 18)); // NOI18N
        search.setForeground(new java.awt.Color(66, 66, 66));
        search.setBorder(null);
        search.setDisabledTextColor(new java.awt.Color(254, 254, 254));
        search.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchActionPerformed(evt);
            }
        });
        search.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                searchKeyPressed(evt);
            }
        });
        jPanel4.add(search, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 0, 330, 40));

        getContentPane().add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 20, 850, 40));

        jPanel10.setBackground(new java.awt.Color(208, 208, 208));
        jPanel10.setLayout(new java.awt.CardLayout());

        jPanel5.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jPanel5.setLayout(null);

        container_main.setBackground(new Color(0, 0, 0, 200));

        jPanel2.setBackground(new java.awt.Color(255, 9, 0));
        jPanel2.setPreferredSize(new java.awt.Dimension(780, 3));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 3, Short.MAX_VALUE)
        );

        jLabel10.setFont(new java.awt.Font("Ubuntu Light", 0, 24)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(254, 254, 254));
        jLabel10.setText("NOTÍCIAS");

        jLabel39.setFont(new java.awt.Font("Ubuntu Light", 0, 24)); // NOI18N
        jLabel39.setForeground(new java.awt.Color(254, 254, 254));
        jLabel39.setText("VÍDEOS");

        jPanel12.setBackground(new java.awt.Color(255, 9, 0));
        jPanel12.setPreferredSize(new java.awt.Dimension(780, 3));

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 3, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout container_mainLayout = new javax.swing.GroupLayout(container_main);
        container_main.setLayout(container_mainLayout);
        container_mainLayout.setHorizontalGroup(
            container_mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(container_mainLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(container_mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(container_mainLayout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addGap(16, 666, Short.MAX_VALUE))
                    .addGroup(container_mainLayout.createSequentialGroup()
                        .addComponent(jLabel39)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        container_mainLayout.setVerticalGroup(
            container_mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(container_mainLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 220, Short.MAX_VALUE)
                .addComponent(jLabel39)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(162, 162, 162))
        );

        jPanel5.add(container_main);
        container_main.setBounds(30, 180, 780, 470);

        bg_main.setIcon(new javax.swing.JLabel() {
            public javax.swing.Icon getIcon() {
                try {
                    return new javax.swing.ImageIcon(
                        new java.net.URL("https://img.bleacherreport.net/img/images/photos/003/660/534/hi-res-4f47dce60af97f363f2c03f015ddfc23_crop_north.jpg?h=670&w=850&q=70&crop_x=center&crop_y=top")
                    );
                } catch (java.net.MalformedURLException e) {
                }
                return null;
            }
        }.getIcon());
        jPanel5.add(bg_main);
        bg_main.setBounds(0, 0, 850, 670);

        javax.swing.GroupLayout MainLayout = new javax.swing.GroupLayout(Main);
        Main.setLayout(MainLayout);
        MainLayout.setHorizontalGroup(
            MainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, MainLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 901, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        MainLayout.setVerticalGroup(
            MainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, 660, Short.MAX_VALUE)
        );

        jPanel10.add(Main, "main");

        Statistics.setBackground(new java.awt.Color(241, 242, 255));
        Statistics.setMaximumSize(new java.awt.Dimension(880, 666));
        Statistics.setMinimumSize(new java.awt.Dimension(880, 666));
        Statistics.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTable1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jTable1.setFont(new java.awt.Font("Ubuntu Light", 0, 15)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "RK", "PLAYER", "TEAM", "GP", "MPG", "PTS", "FGM-FGA", "FG%", "3PM-3PA", "3P%", "FTM-FTA", "FT%", "Título 13", "Título 14", "Título 15"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, true, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setGridColor(new java.awt.Color(254, 254, 254));
        jTable1.setRowHeight(20);
        jTable1.setRowMargin(0);
        jTable1.setSelectionBackground(new java.awt.Color(50, 50, 50));
        jTable1.setSelectionForeground(new java.awt.Color(254, 254, 254));
        jTable1.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setResizable(false);
            jTable1.getColumnModel().getColumn(1).setResizable(false);
            jTable1.getColumnModel().getColumn(2).setResizable(false);
            jTable1.getColumnModel().getColumn(3).setResizable(false);
            jTable1.getColumnModel().getColumn(4).setResizable(false);
            jTable1.getColumnModel().getColumn(5).setResizable(false);
            jTable1.getColumnModel().getColumn(6).setResizable(false);
            jTable1.getColumnModel().getColumn(7).setResizable(false);
            jTable1.getColumnModel().getColumn(8).setResizable(false);
            jTable1.getColumnModel().getColumn(9).setResizable(false);
            jTable1.getColumnModel().getColumn(10).setResizable(false);
            jTable1.getColumnModel().getColumn(11).setResizable(false);
        }

        Statistics.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(116, 201, 690, 460));

        jPanel1.setBackground(new java.awt.Color(38, 38, 38));
        jPanel1.setBorder(null);

        apg.setBackground(new java.awt.Color(220, 4, 2));
        apg.setFont(new java.awt.Font("Ubuntu Light", 0, 18)); // NOI18N
        apg.setForeground(new java.awt.Color(255, 255, 255));
        apg.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        apg.setText("APG");
        apg.setToolTipText("");
        apg.setOpaque(true);
        apg.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                apgMouseClicked(evt);
            }
        });

        dd.setBackground(new java.awt.Color(220, 4, 2));
        dd.setFont(new java.awt.Font("Ubuntu Light", 0, 18)); // NOI18N
        dd.setForeground(new java.awt.Color(255, 255, 255));
        dd.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        dd.setText("DD");
        dd.setToolTipText("");
        dd.setOpaque(true);
        dd.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ddMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                ddMouseEntered(evt);
            }
        });

        fg.setBackground(new java.awt.Color(220, 4, 2));
        fg.setFont(new java.awt.Font("Ubuntu Light", 0, 18)); // NOI18N
        fg.setForeground(new java.awt.Color(255, 255, 255));
        fg.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        fg.setText("FG%");
        fg.setToolTipText("");
        fg.setOpaque(true);
        fg.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                fgMouseClicked(evt);
            }
        });

        rpg.setBackground(new java.awt.Color(220, 4, 2));
        rpg.setFont(new java.awt.Font("Ubuntu Light", 0, 18)); // NOI18N
        rpg.setForeground(new java.awt.Color(255, 255, 255));
        rpg.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rpg.setText("RPG");
        rpg.setToolTipText("");
        rpg.setOpaque(true);
        rpg.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                rpgMouseClicked(evt);
            }
        });

        blkpg.setBackground(new java.awt.Color(220, 4, 2));
        blkpg.setFont(new java.awt.Font("Ubuntu Light", 0, 18)); // NOI18N
        blkpg.setForeground(new java.awt.Color(255, 255, 255));
        blkpg.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        blkpg.setText("BLKPG");
        blkpg.setToolTipText("");
        blkpg.setOpaque(true);
        blkpg.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                blkpgMouseClicked(evt);
            }
        });

        stpg.setBackground(new java.awt.Color(220, 4, 2));
        stpg.setFont(new java.awt.Font("Ubuntu Light", 0, 18)); // NOI18N
        stpg.setForeground(new java.awt.Color(255, 255, 255));
        stpg.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        stpg.setText("STPG");
        stpg.setToolTipText("");
        stpg.setOpaque(true);
        stpg.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                stpgMouseClicked(evt);
            }
        });

        ft.setBackground(new java.awt.Color(220, 4, 2));
        ft.setFont(new java.awt.Font("Ubuntu Light", 0, 18)); // NOI18N
        ft.setForeground(new java.awt.Color(255, 255, 255));
        ft.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ft.setText("FT%");
        ft.setToolTipText("");
        ft.setOpaque(true);
        ft.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ftMouseClicked(evt);
            }
        });

        pfpg.setBackground(new java.awt.Color(220, 4, 2));
        pfpg.setFont(new java.awt.Font("Ubuntu Light", 0, 18)); // NOI18N
        pfpg.setForeground(new java.awt.Color(255, 255, 255));
        pfpg.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        pfpg.setText("PFPG");
        pfpg.setToolTipText("");
        pfpg.setOpaque(true);
        pfpg.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pfpgMouseClicked(evt);
            }
        });

        mpg.setBackground(new java.awt.Color(220, 4, 2));
        mpg.setFont(new java.awt.Font("Ubuntu Light", 0, 18)); // NOI18N
        mpg.setForeground(new java.awt.Color(255, 255, 255));
        mpg.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        mpg.setText("MPG");
        mpg.setToolTipText("");
        mpg.setOpaque(true);
        mpg.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mpgMouseClicked(evt);
            }
        });

        pts.setBackground(new java.awt.Color(220, 4, 2));
        pts.setFont(new java.awt.Font("Ubuntu Light", 0, 18)); // NOI18N
        pts.setForeground(new java.awt.Color(255, 255, 255));
        pts.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        pts.setText("PTS");
        pts.setToolTipText("");
        pts.setOpaque(true);
        pts.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ptsMouseClicked(evt);
            }
        });

        trp.setBackground(new java.awt.Color(220, 4, 2));
        trp.setFont(new java.awt.Font("Ubuntu Light", 0, 18)); // NOI18N
        trp.setForeground(new java.awt.Color(255, 255, 255));
        trp.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        trp.setText("3P%");
        trp.setToolTipText("");
        trp.setOpaque(true);
        trp.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                trpMouseClicked(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Ubuntu Light", 0, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(207, 207, 207));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("ESTATÍSTICAS DOS JOGADORES");
        jLabel4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel6.setFont(new java.awt.Font("Ubuntu Light", 0, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(207, 207, 207));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("CATEGORIAS");
        jLabel6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(apg, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fg, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rpg, javax.swing.GroupLayout.DEFAULT_SIZE, 67, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(blkpg, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(stpg, javax.swing.GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE)
                .addGap(5, 5, 5)
                .addComponent(ft, javax.swing.GroupLayout.DEFAULT_SIZE, 68, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pfpg, javax.swing.GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(mpg, javax.swing.GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pts, javax.swing.GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(trp, javax.swing.GroupLayout.DEFAULT_SIZE, 69, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dd, javax.swing.GroupLayout.DEFAULT_SIZE, 63, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 370, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 784, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 122, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dd, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fg, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rpg, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(blkpg, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(stpg, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ft, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pfpg, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(mpg, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pts, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(trp, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(apg, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                    .addContainerGap(126, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(41, 41, 41)))
        );

        jLabel4.getAccessibleContext().setAccessibleDescription("");

        Statistics.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 1, 808, 200));

        jPanel7.setBackground(new java.awt.Color(38, 38, 38));
        jPanel7.setBorder(null);

        y2016.setBackground(new java.awt.Color(220, 4, 2));
        y2016.setFont(new java.awt.Font("Ubuntu Light", 0, 18)); // NOI18N
        y2016.setForeground(new java.awt.Color(255, 255, 255));
        y2016.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        y2016.setText("2016");
        y2016.setToolTipText("");
        y2016.setOpaque(true);
        y2016.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                y2016MouseClicked(evt);
            }
        });

        y2014.setBackground(new java.awt.Color(220, 4, 2));
        y2014.setFont(new java.awt.Font("Ubuntu Light", 0, 18)); // NOI18N
        y2014.setForeground(new java.awt.Color(255, 255, 255));
        y2014.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        y2014.setText("2014");
        y2014.setToolTipText("");
        y2014.setOpaque(true);
        y2014.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                y2014MouseClicked(evt);
            }
        });

        y2015.setBackground(new java.awt.Color(220, 4, 2));
        y2015.setFont(new java.awt.Font("Ubuntu Light", 0, 18)); // NOI18N
        y2015.setForeground(new java.awt.Color(255, 255, 255));
        y2015.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        y2015.setText("2015");
        y2015.setToolTipText("");
        y2015.setOpaque(true);
        y2015.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                y2015MouseClicked(evt);
            }
        });

        y2011.setBackground(new java.awt.Color(220, 4, 2));
        y2011.setFont(new java.awt.Font("Ubuntu Light", 0, 18)); // NOI18N
        y2011.setForeground(new java.awt.Color(255, 255, 255));
        y2011.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        y2011.setText("2011");
        y2011.setToolTipText("");
        y2011.setOpaque(true);
        y2011.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                y2011MouseClicked(evt);
            }
        });

        y2013.setBackground(new java.awt.Color(220, 4, 2));
        y2013.setFont(new java.awt.Font("Ubuntu Light", 0, 18)); // NOI18N
        y2013.setForeground(new java.awt.Color(255, 255, 255));
        y2013.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        y2013.setText("2013");
        y2013.setToolTipText("");
        y2013.setOpaque(true);
        y2013.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                y2013MouseClicked(evt);
            }
        });

        y2012.setBackground(new java.awt.Color(220, 4, 2));
        y2012.setFont(new java.awt.Font("Ubuntu Light", 0, 18)); // NOI18N
        y2012.setForeground(new java.awt.Color(255, 255, 255));
        y2012.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        y2012.setText("2012");
        y2012.setToolTipText("");
        y2012.setOpaque(true);
        y2012.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                y2012MouseClicked(evt);
            }
        });

        y2010.setBackground(new java.awt.Color(220, 4, 2));
        y2010.setFont(new java.awt.Font("Ubuntu Light", 0, 18)); // NOI18N
        y2010.setForeground(new java.awt.Color(255, 255, 255));
        y2010.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        y2010.setText("2010");
        y2010.setToolTipText("");
        y2010.setOpaque(true);
        y2010.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                y2010MouseClicked(evt);
            }
        });

        y2004.setBackground(new java.awt.Color(220, 4, 2));
        y2004.setFont(new java.awt.Font("Ubuntu Light", 0, 18)); // NOI18N
        y2004.setForeground(new java.awt.Color(255, 255, 255));
        y2004.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        y2004.setText("2004");
        y2004.setToolTipText("");
        y2004.setOpaque(true);
        y2004.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                y2004MouseClicked(evt);
            }
        });

        y2009.setBackground(new java.awt.Color(220, 4, 2));
        y2009.setFont(new java.awt.Font("Ubuntu Light", 0, 18)); // NOI18N
        y2009.setForeground(new java.awt.Color(255, 255, 255));
        y2009.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        y2009.setText("2009");
        y2009.setToolTipText("");
        y2009.setOpaque(true);
        y2009.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                y2009MouseClicked(evt);
            }
        });

        y2008.setBackground(new java.awt.Color(220, 4, 2));
        y2008.setFont(new java.awt.Font("Ubuntu Light", 0, 18)); // NOI18N
        y2008.setForeground(new java.awt.Color(255, 255, 255));
        y2008.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        y2008.setText("2008");
        y2008.setToolTipText("");
        y2008.setOpaque(true);
        y2008.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                y2008MouseClicked(evt);
            }
        });

        y2007.setBackground(new java.awt.Color(220, 4, 2));
        y2007.setFont(new java.awt.Font("Ubuntu Light", 0, 18)); // NOI18N
        y2007.setForeground(new java.awt.Color(255, 255, 255));
        y2007.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        y2007.setText("2007");
        y2007.setToolTipText("");
        y2007.setOpaque(true);
        y2007.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                y2007MouseClicked(evt);
            }
        });

        y2006.setBackground(new java.awt.Color(220, 4, 2));
        y2006.setFont(new java.awt.Font("Ubuntu Light", 0, 18)); // NOI18N
        y2006.setForeground(new java.awt.Color(255, 255, 255));
        y2006.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        y2006.setText("2006");
        y2006.setToolTipText("");
        y2006.setOpaque(true);
        y2006.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                y2006MouseClicked(evt);
            }
        });

        y2005.setBackground(new java.awt.Color(220, 4, 2));
        y2005.setFont(new java.awt.Font("Ubuntu Light", 0, 18)); // NOI18N
        y2005.setForeground(new java.awt.Color(255, 255, 255));
        y2005.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        y2005.setText("2005");
        y2005.setToolTipText("");
        y2005.setOpaque(true);
        y2005.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                y2005MouseClicked(evt);
            }
        });

        y2003.setBackground(new java.awt.Color(220, 4, 2));
        y2003.setFont(new java.awt.Font("Ubuntu Light", 0, 18)); // NOI18N
        y2003.setForeground(new java.awt.Color(255, 255, 255));
        y2003.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        y2003.setText("2003");
        y2003.setToolTipText("");
        y2003.setOpaque(true);
        y2003.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                y2003MouseClicked(evt);
            }
        });

        y2002.setBackground(new java.awt.Color(220, 4, 2));
        y2002.setFont(new java.awt.Font("Ubuntu Light", 0, 18)); // NOI18N
        y2002.setForeground(new java.awt.Color(255, 255, 255));
        y2002.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        y2002.setText("2002");
        y2002.setToolTipText("");
        y2002.setOpaque(true);
        y2002.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                y2002MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                y2002MouseEntered(evt);
            }
        });

        y2017.setBackground(new java.awt.Color(220, 4, 2));
        y2017.setFont(new java.awt.Font("Ubuntu Light", 0, 18)); // NOI18N
        y2017.setForeground(new java.awt.Color(255, 255, 255));
        y2017.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        y2017.setText("2017");
        y2017.setToolTipText("");
        y2017.setOpaque(true);
        y2017.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                y2017MouseClicked(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Ubuntu Light", 0, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(207, 207, 207));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("ANOS");
        jLabel5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(y2016, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(y2014, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(y2015, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(y2011, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(y2013, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(y2012, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(y2010, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(y2004, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(y2009, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(y2008, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(y2007, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(y2006, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(y2005, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(y2003, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(y2002, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(y2017, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(y2002)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(y2003)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(y2004)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(y2005)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(y2006)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(y2007)
                .addGap(7, 7, 7)
                .addComponent(y2008)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(y2009)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(y2010)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(y2011)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(y2012)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(y2013)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(y2014)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(y2015)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(y2016)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(y2017)
                .addContainerGap(33, Short.MAX_VALUE))
        );

        Statistics.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 200, -1, -1));

        jPanel10.add(Statistics, "statistics");

        Search.setBackground(new java.awt.Color(241, 242, 255));
        Search.setMaximumSize(new java.awt.Dimension(880, 666));
        Search.setMinimumSize(new java.awt.Dimension(880, 666));
        Search.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel8.setBackground(new java.awt.Color(38, 38, 38));
        jPanel8.setBorder(null);

        jLabel7.setFont(new java.awt.Font("Ubuntu Light", 0, 24)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(207, 207, 207));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel7.setText("PESQUSA DE JOGADORES");
        jLabel7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        result_search.setFont(new java.awt.Font("Ubuntu Light", 0, 18)); // NOI18N
        result_search.setForeground(new java.awt.Color(207, 207, 207));
        result_search.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        result_search.setText("CATEGORIAS");
        result_search.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        loading.setForeground(new java.awt.Color(255, 255, 255));
        loading.setText("Loading");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 308, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(result_search, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(39, 39, 39)
                        .addComponent(loading, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(383, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(result_search, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(loading, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        Search.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 1, 850, 140));

        container_search.setBackground(new java.awt.Color(254, 254, 254));
        container_search.setBorder(null);

        javax.swing.GroupLayout container_searchLayout = new javax.swing.GroupLayout(container_search);
        container_search.setLayout(container_searchLayout);
        container_searchLayout.setHorizontalGroup(
            container_searchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 835, Short.MAX_VALUE)
        );
        container_searchLayout.setVerticalGroup(
            container_searchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 690, Short.MAX_VALUE)
        );

        jScrollPane7.setViewportView(container_search);

        Search.add(jScrollPane7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 140, 850, 530));

        jPanel10.add(Search, "search");

        Teams.setBackground(new java.awt.Color(241, 242, 255));
        Teams.setMaximumSize(new java.awt.Dimension(880, 666));
        Teams.setMinimumSize(new java.awt.Dimension(880, 666));
        Teams.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel11.setBackground(new java.awt.Color(38, 38, 38));
        jPanel11.setBorder(null);

        jLabel8.setFont(new java.awt.Font("Ubuntu Light", 0, 24)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(207, 207, 207));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel8.setText("TIMES");
        jLabel8.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 308, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(530, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(85, Short.MAX_VALUE))
        );

        Teams.add(jPanel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 1, 850, 130));

        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane2.setAlignmentY(2.0F);

        jPanel13.setBackground(new java.awt.Color(254, 254, 254));
        jPanel13.setBorder(null);
        jPanel13.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        result_search1.setFont(new java.awt.Font("Ubuntu Light", 0, 18)); // NOI18N
        result_search1.setForeground(new java.awt.Color(66, 66, 66));
        result_search1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        result_search1.setText("LESTE");
        result_search1.setToolTipText("");
        result_search1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel13.add(result_search1, new org.netbeans.lib.awtextra.AbsoluteConstraints(442, 0, 390, 33));

        result_search2.setFont(new java.awt.Font("Ubuntu Light", 0, 18)); // NOI18N
        result_search2.setForeground(new java.awt.Color(66, 66, 66));
        result_search2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        result_search2.setText("OESTE");
        result_search2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel13.add(result_search2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 383, 33));

        leste_team.setBackground(new java.awt.Color(232, 232, 232));
        leste_team.setLayout(null);
        jPanel13.add(leste_team, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 40, 380, 840));

        oeste_team.setBackground(new java.awt.Color(232, 232, 232));
        oeste_team.setLayout(null);
        jPanel13.add(oeste_team, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 380, 840));

        jScrollPane2.setViewportView(jPanel13);

        Teams.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 130, 850, 540));

        jPanel10.add(Teams, "team");

        TeamProfile.setBackground(new java.awt.Color(241, 242, 255));
        TeamProfile.setMaximumSize(new java.awt.Dimension(880, 666));
        TeamProfile.setMinimumSize(new java.awt.Dimension(880, 666));
        TeamProfile.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel15.setBackground(new java.awt.Color(38, 38, 38));
        jPanel15.setBorder(null);

        nameTeam.setFont(new java.awt.Font("Ubuntu Light", 0, 24)); // NOI18N
        nameTeam.setForeground(new java.awt.Color(254, 254, 254));
        nameTeam.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        nameTeam.setText("ADS");
        nameTeam.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        win_loses.setForeground(new java.awt.Color(254, 254, 254));
        win_loses.setText("32 - 45");

        jPanel19.setBackground(new java.awt.Color(38, 38, 38));

        jLabel9.setForeground(new java.awt.Color(254, 254, 254));
        jLabel9.setText("Assistência");

        pts_num.setFont(new java.awt.Font("Ubuntu", 1, 24)); // NOI18N
        pts_num.setForeground(new java.awt.Color(181, 181, 181));
        pts_num.setText("12");

        jLabel62.setForeground(new java.awt.Color(254, 254, 254));
        jLabel62.setText("Pontos");

        ass_num.setFont(new java.awt.Font("Ubuntu", 1, 24)); // NOI18N
        ass_num.setForeground(new java.awt.Color(181, 181, 181));
        ass_num.setText("12");

        jLabel64.setForeground(new java.awt.Color(254, 254, 254));
        jLabel64.setText("Rebotes");

        rbt_num.setFont(new java.awt.Font("Ubuntu", 1, 24)); // NOI18N
        rbt_num.setForeground(new java.awt.Color(181, 181, 181));
        rbt_num.setText("12");

        jLabel66.setForeground(new java.awt.Color(254, 254, 254));
        jLabel66.setText("Pontos levados");

        lvd_num.setFont(new java.awt.Font("Ubuntu", 1, 24)); // NOI18N
        lvd_num.setForeground(new java.awt.Color(181, 181, 181));
        lvd_num.setText("12");

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel19Layout.createSequentialGroup()
                        .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pts_num)
                            .addComponent(jLabel62))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(ass_num, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addGroup(jPanel19Layout.createSequentialGroup()
                        .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(rbt_num)
                            .addComponent(jLabel64))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 62, Short.MAX_VALUE)
                        .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel66, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lvd_num, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addContainerGap())
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel62))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pts_num)
                    .addComponent(ass_num))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel64)
                    .addComponent(jLabel66))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rbt_num)
                    .addComponent(lvd_num))
                .addGap(24, 24, 24))
        );

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(picTeam, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(nameTeam, javax.swing.GroupLayout.PREFERRED_SIZE, 308, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(win_loses))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(58, Short.MAX_VALUE))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(picTeam, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel15Layout.createSequentialGroup()
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel15Layout.createSequentialGroup()
                                .addComponent(nameTeam, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(win_loses))
                            .addComponent(jPanel19, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 8, Short.MAX_VALUE)))
                .addContainerGap())
        );

        nameTeam.getAccessibleContext().setAccessibleName("");

        TeamProfile.add(jPanel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 1, 850, 200));

        jPanel16.setBackground(new java.awt.Color(254, 254, 254));
        jPanel16.setBorder(null);

        result_search3.setFont(new java.awt.Font("Ubuntu Light", 0, 18)); // NOI18N
        result_search3.setForeground(new java.awt.Color(66, 66, 66));
        result_search3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        result_search3.setText("LÍDERES DO TIME");
        result_search3.setToolTipText("");
        result_search3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        result_search4.setFont(new java.awt.Font("Ubuntu Light", 0, 18)); // NOI18N
        result_search4.setForeground(new java.awt.Color(66, 66, 66));
        result_search4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        result_search4.setText("ELENCO");
        result_search4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel38.setText("Pontos por jogo");

        name_1.setText("Nome do jogador");

        num_1.setFont(new java.awt.Font("Ubuntu", 1, 24)); // NOI18N
        num_1.setText("92");

        name_2.setText("Nome do jogador");

        num_2.setFont(new java.awt.Font("Ubuntu", 1, 24)); // NOI18N
        num_2.setText("92");

        jLabel45.setText("Assistência por jogo");

        name_3.setText("Nome do jogador");

        num_3.setFont(new java.awt.Font("Ubuntu", 1, 24)); // NOI18N
        num_3.setText("92");

        jLabel49.setText("Aproveitamento");

        pos_1.setText("jLabel10");

        pos_2.setText("jLabel10");

        pos_3.setText("jLabel10");

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator2)
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addComponent(pic_1, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(name_1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel17Layout.createSequentialGroup()
                                .addComponent(num_1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(pos_1))))
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addComponent(pic_2, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(name_2, javax.swing.GroupLayout.DEFAULT_SIZE, 141, Short.MAX_VALUE)
                            .addGroup(jPanel17Layout.createSequentialGroup()
                                .addComponent(num_2, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(pos_2))))
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addComponent(pic_3, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(name_3, javax.swing.GroupLayout.DEFAULT_SIZE, 141, Short.MAX_VALUE)
                            .addGroup(jPanel17Layout.createSequentialGroup()
                                .addComponent(num_3, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(pos_3))))
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel38)
                            .addComponent(jLabel45)
                            .addComponent(jLabel49))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jSeparator3, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel38)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addComponent(name_1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(num_1)
                            .addComponent(pos_1)))
                    .addComponent(pic_1, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 4, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel45)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pic_2, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addComponent(name_2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(num_2)
                            .addComponent(pos_2))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel49)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pic_3, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addComponent(name_3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(num_3)
                            .addComponent(pos_3))))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Offensive", jPanel17);

        name_4.setText("Nome do jogador");

        num_4.setFont(new java.awt.Font("Ubuntu", 1, 24)); // NOI18N
        num_4.setText("92");

        num_5.setFont(new java.awt.Font("Ubuntu", 1, 24)); // NOI18N
        num_5.setText("92");

        name_5.setText("Nome do jogador");

        num_6.setFont(new java.awt.Font("Ubuntu", 1, 24)); // NOI18N
        num_6.setText("92");

        name_6.setText("Nome do jogador");

        jLabel59.setText("Rebotes por jogo");

        jLabel60.setText("Roubo de bola por jogo");

        jLabel61.setText("Tocos por jogo");

        pos_4.setText("jLabel10");

        pos_5.setText("jLabel10");

        pos_6.setText("jLabel10");

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel18Layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(jSeparator4))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel18Layout.createSequentialGroup()
                            .addGap(12, 12, 12)
                            .addComponent(pic_4, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel18Layout.createSequentialGroup()
                                    .addGap(4, 4, 4)
                                    .addComponent(num_4, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(44, 44, 44)
                                    .addComponent(pos_4))
                                .addGroup(jPanel18Layout.createSequentialGroup()
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(name_4, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel18Layout.createSequentialGroup()
                            .addGap(12, 12, 12)
                            .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel59)
                                .addComponent(jLabel60)
                                .addGroup(jPanel18Layout.createSequentialGroup()
                                    .addComponent(pic_5, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(6, 6, 6)
                                    .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(name_5, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(jPanel18Layout.createSequentialGroup()
                                            .addComponent(num_5, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(42, 42, 42)
                                            .addComponent(pos_5))))
                                .addGroup(jPanel18Layout.createSequentialGroup()
                                    .addComponent(pic_6, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(6, 6, 6)
                                    .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(name_6, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(jPanel18Layout.createSequentialGroup()
                                            .addComponent(num_6, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(44, 44, 44)
                                            .addComponent(pos_6))
                                        .addComponent(jLabel61)))))))
                .addGap(212, 212, 212))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel59)
                .addGap(6, 6, 6)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pic_4, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addComponent(name_4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(num_4, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pos_4))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 6, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel60)
                .addGap(6, 6, 6)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pic_5, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addComponent(name_5)
                        .addGap(6, 6, 6)
                        .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(num_5, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pos_5))))
                .addGap(8, 8, 8)
                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 6, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel61)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pic_6, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addComponent(name_6)
                        .addGap(6, 6, 6)
                        .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(num_6, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pos_6)))))
        );

        jTabbedPane1.addTab("Deffensive", jPanel18);

        teamRoster.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "null", "null", "null", "null"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(teamRoster);
        if (teamRoster.getColumnModel().getColumnCount() > 0) {
            teamRoster.getColumnModel().getColumn(0).setResizable(false);
            teamRoster.getColumnModel().getColumn(1).setResizable(false);
            teamRoster.getColumnModel().getColumn(2).setResizable(false);
            teamRoster.getColumnModel().getColumn(3).setResizable(false);
            teamRoster.getColumnModel().getColumn(4).setResizable(false);
            teamRoster.getColumnModel().getColumn(5).setResizable(false);
            teamRoster.getColumnModel().getColumn(5).setHeaderValue("null");
            teamRoster.getColumnModel().getColumn(6).setResizable(false);
            teamRoster.getColumnModel().getColumn(6).setHeaderValue("null");
            teamRoster.getColumnModel().getColumn(7).setResizable(false);
            teamRoster.getColumnModel().getColumn(7).setHeaderValue("null");
        }

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(result_search4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addGap(325, 325, 325)
                        .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                        .addGap(0, 9, Short.MAX_VALUE)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 585, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(result_search3, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(result_search4, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(result_search3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(299, 299, 299)
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(178, Short.MAX_VALUE))
        );

        TeamProfile.add(jPanel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 200, 850, -1));

        jPanel10.add(TeamProfile, "teamProfile");

        playerProfile.setBackground(new java.awt.Color(241, 242, 255));
        playerProfile.setMaximumSize(new java.awt.Dimension(880, 666));
        playerProfile.setMinimumSize(new java.awt.Dimension(880, 666));
        playerProfile.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel20.setBackground(new java.awt.Color(38, 38, 38));
        jPanel20.setBorder(null);

        namePlayer.setFont(new java.awt.Font("Ubuntu Light", 0, 24)); // NOI18N
        namePlayer.setForeground(new java.awt.Color(254, 254, 254));
        namePlayer.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        namePlayer.setText("ADS");
        namePlayer.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        picPlayer.setToolTipText("");

        subTitle.setForeground(new java.awt.Color(254, 254, 254));
        subTitle.setText("32 - 45");

        playerBorn.setForeground(new java.awt.Color(205, 205, 205));

        jLabel40.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        jLabel40.setForeground(new java.awt.Color(205, 205, 205));
        jLabel40.setText("Nascimento");

        playerDrafted.setForeground(new java.awt.Color(205, 205, 205));

        jLabel42.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        jLabel42.setForeground(new java.awt.Color(205, 205, 205));
        jLabel42.setText("Drafted");

        playerCollege.setForeground(new java.awt.Color(205, 205, 205));

        jLabel44.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        jLabel44.setForeground(new java.awt.Color(205, 205, 205));
        jLabel44.setText("Faculdade");

        playerExp.setForeground(new java.awt.Color(205, 205, 205));

        jLabel47.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        jLabel47.setForeground(new java.awt.Color(205, 205, 205));
        jLabel47.setText("Experiência");

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(picPlayer, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel20Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel20Layout.createSequentialGroup()
                                .addComponent(jLabel47)
                                .addGap(33, 33, 33)
                                .addComponent(playerExp, javax.swing.GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE))
                            .addGroup(jPanel20Layout.createSequentialGroup()
                                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel20Layout.createSequentialGroup()
                                        .addComponent(jLabel42)
                                        .addGap(59, 59, 59))
                                    .addGroup(jPanel20Layout.createSequentialGroup()
                                        .addComponent(jLabel44)
                                        .addGap(42, 42, 42)))
                                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(playerBorn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(playerDrafted, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(playerCollege, javax.swing.GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE))))
                        .addGap(245, 245, 245))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel20Layout.createSequentialGroup()
                        .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(subTitle, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 491, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(namePlayer, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 342, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel40, javax.swing.GroupLayout.Alignment.LEADING))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(picPlayer, javax.swing.GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE)
                    .addGroup(jPanel20Layout.createSequentialGroup()
                        .addComponent(namePlayer, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(subTitle)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel40)
                            .addComponent(playerBorn))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel42)
                            .addComponent(playerDrafted))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel44)
                            .addComponent(playerCollege))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel47)
                            .addComponent(playerExp))))
                .addContainerGap())
        );

        playerProfile.add(jPanel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 1, 850, 200));

        jPanel22.setBackground(new java.awt.Color(254, 254, 254));
        jPanel22.setBorder(null);

        result_search6.setFont(new java.awt.Font("Ubuntu Light", 0, 18)); // NOI18N
        result_search6.setForeground(new java.awt.Color(66, 66, 66));
        result_search6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        result_search6.setText("FOTOS");
        result_search6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        playerStatis_2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "GP3PM-3PA", "3P%", "PPGFTM-FTA", "FT%", "RPG"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane6.setViewportView(playerStatis_2);
        if (playerStatis_2.getColumnModel().getColumnCount() > 0) {
            playerStatis_2.getColumnModel().getColumn(0).setResizable(false);
            playerStatis_2.getColumnModel().getColumn(1).setResizable(false);
            playerStatis_2.getColumnModel().getColumn(2).setResizable(false);
            playerStatis_2.getColumnModel().getColumn(3).setResizable(false);
            playerStatis_2.getColumnModel().getColumn(4).setResizable(false);
        }
        playerStatis_2.getAccessibleContext().setAccessibleName("playerStatis_2");

        playerStatis_1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "GP", "MPG", "PPG", "FGM-FGA", "FG%"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane5.setViewportView(playerStatis_1);
        if (playerStatis_1.getColumnModel().getColumnCount() > 0) {
            playerStatis_1.getColumnModel().getColumn(0).setResizable(false);
            playerStatis_1.getColumnModel().getColumn(1).setResizable(false);
            playerStatis_1.getColumnModel().getColumn(2).setResizable(false);
            playerStatis_1.getColumnModel().getColumn(3).setResizable(false);
            playerStatis_1.getColumnModel().getColumn(4).setResizable(false);
        }
        playerStatis_1.getAccessibleContext().setAccessibleName("playerStatis_1");

        playerStatis_3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "APG", "BLKPG", "STLṔG", "PFPG", "TOPG"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane4.setViewportView(playerStatis_3);
        if (playerStatis_3.getColumnModel().getColumnCount() > 0) {
            playerStatis_3.getColumnModel().getColumn(0).setResizable(false);
            playerStatis_3.getColumnModel().getColumn(1).setResizable(false);
            playerStatis_3.getColumnModel().getColumn(2).setResizable(false);
            playerStatis_3.getColumnModel().getColumn(3).setResizable(false);
            playerStatis_3.getColumnModel().getColumn(4).setResizable(false);
        }
        playerStatis_3.getAccessibleContext().setAccessibleName("playerStatis_3");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 251, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        result_search7.setFont(new java.awt.Font("Ubuntu Light", 0, 18)); // NOI18N
        result_search7.setForeground(new java.awt.Color(66, 66, 66));
        result_search7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        result_search7.setText("ESTATÍSTICAS DO JOGADOR");
        result_search7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        container_photos.setBackground(new java.awt.Color(254, 254, 254));
        container_photos.setBorder(null);

        javax.swing.GroupLayout container_photosLayout = new javax.swing.GroupLayout(container_photos);
        container_photos.setLayout(container_photosLayout);
        container_photosLayout.setHorizontalGroup(
            container_photosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        container_photosLayout.setVerticalGroup(
            container_photosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 278, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(result_search6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel22Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jPanel25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(container_photos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel22Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(result_search7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(result_search6, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(container_photos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(186, 186, 186)
                .addComponent(jPanel25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(216, Short.MAX_VALUE))
            .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel22Layout.createSequentialGroup()
                    .addGap(10, 10, 10)
                    .addComponent(result_search7, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(905, Short.MAX_VALUE)))
        );

        playerProfile.add(jPanel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 200, 850, -1));

        jPanel10.add(playerProfile, "playerProfile");

        getContentPane().add(jPanel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 60, 850, 660));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void menu_homeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menu_homeMouseClicked
        // TODO add your handling code here:
        CardLayout cl = (CardLayout) jPanel10.getLayout();
        cl.show(jPanel10, "main");
        
        menu_home.setBackground(new Color(220,4,2));
        menu_home.setForeground(new Color(255,255,255));
        
        menu_teams.setBackground(new Color(9,9,9));
        menu_teams.setForeground(new Color(255,255,255));
        
        menu_statis.setBackground(new Color(9,9,9));
        menu_statis.setForeground(new Color(255,255,255));
    }//GEN-LAST:event_menu_homeMouseClicked

    private void menu_statisMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menu_statisMouseClicked
        // TODO add your handling code here:
        CardLayout cl = (CardLayout) jPanel10.getLayout();
        cl.show(jPanel10, "statistics");
        
        menu_home.setBackground(new Color(9,9,9));
        menu_home.setForeground(new Color(255,255,255));
        
        menu_teams.setBackground(new Color(9,9,9));
        menu_teams.setForeground(new Color(255,255,255));
        
        menu_statis.setBackground(new Color(220,4,2));
        menu_statis.setForeground(new Color(255,255,255));
    }//GEN-LAST:event_menu_statisMouseClicked

    private void y2002MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_y2002MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_y2002MouseEntered

    private void y2002MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_y2002MouseClicked
        // TODO add your handling code here:
        this.year = 2002;
        loadStatistics(this.year, this.actualCategory);
    }//GEN-LAST:event_y2002MouseClicked

    private void apgMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_apgMouseClicked
        // TODO add your handling code here:
        this.actualCategory = "APG";
        loadStatistics(this.year, "APG");
    }//GEN-LAST:event_apgMouseClicked

    private void fgMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fgMouseClicked
        // TODO add your handling code here:
        this.actualCategory = "FG%";
        loadStatistics(this.year, "FG%");
    }//GEN-LAST:event_fgMouseClicked

    private void rpgMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rpgMouseClicked
        // TODO add your handling code here:
        this.actualCategory = "RPG";
        loadStatistics(this.year, "RPG");
    }//GEN-LAST:event_rpgMouseClicked

    private void blkpgMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_blkpgMouseClicked
        // TODO add your handling code here:
        this.actualCategory = "BLKPG";
        loadStatistics(this.year, "BLKPG");
    }//GEN-LAST:event_blkpgMouseClicked

    private void stpgMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_stpgMouseClicked
        // TODO add your handling code here:
        this.actualCategory = "STPG";
        loadStatistics(this.year, "STPG");
    }//GEN-LAST:event_stpgMouseClicked

    private void ftMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ftMouseClicked
        // TODO add your handling code here:
        this.actualCategory = "FT%";
        loadStatistics(this.year, "FT%");
    }//GEN-LAST:event_ftMouseClicked

    private void pfpgMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pfpgMouseClicked
        // TODO add your handling code here:
        this.actualCategory = "PFPG";
        loadStatistics(this.year, "PFPG");
    }//GEN-LAST:event_pfpgMouseClicked

    private void mpgMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mpgMouseClicked
        // TODO add your handling code here:
        this.actualCategory = "MPG";
        loadStatistics(this.year, "MPG");
    }//GEN-LAST:event_mpgMouseClicked

    private void ptsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ptsMouseClicked
        // TODO add your handling code here:
        this.actualCategory = "PTS";
        loadStatistics(this.year, "PTS");
    }//GEN-LAST:event_ptsMouseClicked

    private void trpMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_trpMouseClicked
        // TODO add your handling code here:
        this.actualCategory = "3P%";
        loadStatistics(this.year, "3P%");
    }//GEN-LAST:event_trpMouseClicked

    private void ddMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ddMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_ddMouseEntered

    private void ddMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ddMouseClicked
        // TODO add your handling code here:
        this.actualCategory = "DBLDBL";
        loadStatistics(this.year, "DBLDBL");
    }//GEN-LAST:event_ddMouseClicked

    private void y2003MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_y2003MouseClicked
        // TODO add your handling code here:
        this.year = 2003;
        loadStatistics(this.year, this.actualCategory);
    }//GEN-LAST:event_y2003MouseClicked

    private void y2004MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_y2004MouseClicked
        // TODO add your handling code here:
        this.year = 2004;
        loadStatistics(this.year, this.actualCategory);
    }//GEN-LAST:event_y2004MouseClicked

    private void y2005MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_y2005MouseClicked
        // TODO add your handling code here:
        this.year = 2005;
        loadStatistics(this.year, this.actualCategory);
    }//GEN-LAST:event_y2005MouseClicked

    private void y2006MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_y2006MouseClicked
        // TODO add your handling code here:
        this.year = 2006;
        loadStatistics(this.year, this.actualCategory);
    }//GEN-LAST:event_y2006MouseClicked

    private void y2007MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_y2007MouseClicked
        // TODO add your handling code here:
        this.year = 2007;
        loadStatistics(this.year, this.actualCategory);
    }//GEN-LAST:event_y2007MouseClicked

    private void y2008MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_y2008MouseClicked
        // TODO add your handling code here:
        this.year = 2008;
        loadStatistics(this.year, this.actualCategory);
    }//GEN-LAST:event_y2008MouseClicked

    private void y2009MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_y2009MouseClicked
        // TODO add your handling code here:
        this.year = 2009;
        loadStatistics(this.year, this.actualCategory);
    }//GEN-LAST:event_y2009MouseClicked

    private void y2010MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_y2010MouseClicked
        // TODO add your handling code here:
        this.year = 2010;
        loadStatistics(this.year, this.actualCategory);
    }//GEN-LAST:event_y2010MouseClicked

    private void y2011MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_y2011MouseClicked
        // TODO add your handling code here:
        this.year = 2011;
        loadStatistics(this.year, this.actualCategory);
    }//GEN-LAST:event_y2011MouseClicked

    private void y2012MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_y2012MouseClicked
        // TODO add your handling code here:
        this.year = 2012;
        loadStatistics(this.year, this.actualCategory);
    }//GEN-LAST:event_y2012MouseClicked

    private void y2013MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_y2013MouseClicked
        // TODO add your handling code here:
        this.year = 2013;
        loadStatistics(this.year, this.actualCategory);
    }//GEN-LAST:event_y2013MouseClicked

    private void y2014MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_y2014MouseClicked
        // TODO add your handling code here:
        this.year = 2014;
        loadStatistics(this.year, this.actualCategory);
    }//GEN-LAST:event_y2014MouseClicked

    private void y2015MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_y2015MouseClicked
        // TODO add your handling code here:
        this.year = 2015;
        loadStatistics(this.year, this.actualCategory);
    }//GEN-LAST:event_y2015MouseClicked

    private void y2016MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_y2016MouseClicked
        // TODO add your handling code here:
        this.year = 2016;
        loadStatistics(this.year, this.actualCategory);
    }//GEN-LAST:event_y2016MouseClicked

    private void y2017MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_y2017MouseClicked
        // TODO add your handling code here:
        this.year = 2017;
        loadStatistics(this.year, this.actualCategory);
    }//GEN-LAST:event_y2017MouseClicked

    private void searchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchActionPerformed

    private void searchKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode()== KeyEvent.VK_ENTER){
            String text = search.getText();
            
            CardLayout cl = (CardLayout) jPanel10.getLayout();
            cl.show(jPanel10, "search");
            container_search.removeAll();


            try{
                File homedir = new File(System.getProperty("user.home"));

                File fileToRead = new File(homedir, "/NetBeansProjects/NBA News/src/sources/players.txt");
                BufferedReader br = new BufferedReader(new FileReader(fileToRead));
                String line = br.readLine();
                
                LinkedList<String> players = new LinkedList();
                
            while (line != null) {
   
                if(line.contains(text)){
                    players.add(line);
                    line = br.readLine();
                    players.add(line);
                    line = br.readLine();
                    players.add(line);
                }
                
    
                line = br.readLine();
            }
            
            if(players.size() > 0){
                JLabel pictures_players[] = new JLabel[players.size() / 3];
                JLabel names_players[] = new JLabel[players.size() / 3];
                
                result_search.setText(players.size() / 3 + " resultados encontrado(s)");

                for(int i = 0; i < players.size(); i++){
                   pictures_players[i / 3] = new JLabel();
                    names_players[i / 3] = new JLabel();
                try{
                String picture = players.get(i + 1);
                String name = players.get(i);
                String id = players.get(i + 2);
                
                URL url = new URL(picture);
                ImageIcon ico = new ImageIcon(url);
                Image image = ico.getImage(); // transform it 
                Dimension imgSize = new Dimension(ico.getIconWidth(), ico.getIconHeight());
                Dimension boundary = new Dimension(60, 60);
                Dimension newSize = getScaledDimension(imgSize, boundary);
                Image newimg = image.getScaledInstance(newSize.width, newSize.height,  java.awt.Image.SCALE_SMOOTH);
                ImageIcon newIco = new ImageIcon(newimg);

                pictures_players[i / 3].setIcon(newIco);
                names_players[i / 3].setText(name);
                
                pictures_players[i / 3].setVisible(true);
                names_players[i / 3].setVisible(true);
                
                pictures_players[i / 3].setBounds(10, (i / 3) * 70 + 10, 70, 70);
                names_players[i / 3].setBounds(75, (i / 3) * 70 + 10, 200, 20);
                names_players[i / 3].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                pictures_players[i / 3].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                
                names_players[i / 3].addMouseListener(new MouseAdapter(){
                    public void mouseClicked(MouseEvent e){
                        CardLayout cl = (CardLayout) jPanel10.getLayout();
                        cl.show(jPanel10, "playerProfile");
                        loadPlayerProfile(name, id, picture);
                    }
                });
                
                pictures_players[i / 3].addMouseListener(new MouseAdapter(){
                    public void mouseClicked(MouseEvent e){
                        CardLayout cl = (CardLayout) jPanel10.getLayout();
                        cl.show(jPanel10, "playerProfile");
                        loadPlayerProfile(name, id, picture);
                    }
                });

                
                container_search.add(pictures_players[i / 3]);
                container_search.add(names_players[i / 3]);
                
                i += 2;
                }catch(MalformedURLException e){
                    
                }
                
                }
                
            }else{
                result_search.setText("Jogador não encontrado");
            }
            
           br.close();
         
        }catch(FileNotFoundException e){
            System.out.println(e);
        } catch(IOException e){
            System.out.println(e);
        }
            
        }
    }//GEN-LAST:event_searchKeyPressed

    private void menu_teamsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menu_teamsMouseClicked
        // TODO add your handling code here:
        CardLayout cl = (CardLayout) jPanel10.getLayout();
        cl.show(jPanel10, "team");
        loadTeams();
        
        menu_home.setBackground(new Color(9,9,9));
        menu_home.setForeground(new Color(255,255,255));
        
        menu_teams.setBackground(new Color(220,4,2));
        menu_teams.setForeground(new Color(255,255,255));
        
        menu_statis.setBackground(new Color(9,9,9));
        menu_statis.setForeground(new Color(255,255,255));
    }//GEN-LAST:event_menu_teamsMouseClicked

    private void closeWindowMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_closeWindowMouseClicked
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_closeWindowMouseClicked

    private void minWindowMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_minWindowMouseClicked
        // TODO add your handling code here:
        this.setState(this.ICONIFIED);
    }//GEN-LAST:event_minWindowMouseClicked

    private void jPanel9MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel9MouseDragged
        // TODO add your handling code here:
        int x = evt.getXOnScreen();
        int y = evt.getYOnScreen();
        
        this.setLocation(x - xMouse, y - yMouse);
    }//GEN-LAST:event_jPanel9MouseDragged

    private void jPanel9MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel9MousePressed
        // TODO add your handling code here:
        xMouse = evt.getX();
        yMouse = evt.getY();
    }//GEN-LAST:event_jPanel9MousePressed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Main().setVisible(true);
            }
        });
        
        
        
    }
    
    public void loadPlayerProfile(String name, String id, String picture){

        CrawlerInfoPessoais infop = new CrawlerInfoPessoais();
        LinkedList<String> list = new LinkedList();
        LinkedList<String> playerStatis = new LinkedList();
        list = infop.getDadosPessoais("http://www.espn.com/nba/player/_/id/"+ id + "/" + name.replace(" ", "-").toLowerCase());
        
        try{
        URL url = new URL(picture);
        ImageIcon ico = new ImageIcon(url);
        Image image = ico.getImage(); // transform it 
        Dimension imgSize = new Dimension(ico.getIconWidth(), ico.getIconHeight());
        Dimension boundary = new Dimension(200, 170);
        Dimension newSize = getScaledDimension(imgSize, boundary);
        Image newimg = image.getScaledInstance(newSize.width, newSize.height,  java.awt.Image.SCALE_SMOOTH);
        ImageIcon newIco = new ImageIcon(newimg);
        picPlayer.setIcon(newIco);
        }catch(MalformedURLException e){
            
        }
       
        namePlayer.setText(list.get(0));
        subTitle.setText(list.get(1));
        playerBorn.setText(list.get(2));
        playerDrafted.setText(list.get(3));
        playerCollege.setText(list.get(4));
        playerExp.setText(list.get(5));
        
        playerStatis = infop.iterarColunasImpares("http://www.espn.com/nba/player/_/id/"+ id + "/" + name.replace(" ", "-").toLowerCase());
        
        DefaultTableModel playerStatisTable_1 = (DefaultTableModel) playerStatis_1.getModel();
        DefaultTableModel playerStatisTable_2 = (DefaultTableModel) playerStatis_2.getModel();
        DefaultTableModel playerStatisTable_3 = (DefaultTableModel) playerStatis_3.getModel();
        
        String dados[] = new String[5];
        int count = 0;

        for(int i = 0; i < playerStatis.size(); i++){
            count = i % 5;
            dados[count] = playerStatis.get(i);
            if(i == 4){
                playerStatisTable_1.addRow(dados);
                dados = new String[5];
            }else if(i == 9){
                playerStatisTable_2.addRow(dados);
                dados = new String[5];
            }else if(i == 14){
                playerStatisTable_3.addRow(dados);
                dados = new String[5];
            }
        }
        
        
        // Get photos
        try{
        CrawlerGetFotos getF = new CrawlerGetFotos();
        
        LinkedList<String> photos = new LinkedList();
        photos = getF.getPictures(list.get(0), 8);
        
      
        if(photos.size() > 0){
            container_photos.removeAll();
            JLabel jphotos[] = new JLabel[photos.size()];
            for(int i = 0; i < photos.size(); i++) {
                  jphotos[i] = new JLabel();

         try{
         String link = photos.get(i);
         URL url = new URL(photos.get(i));
         
         ImageIcon ico = new ImageIcon(url);
         Image image = ico.getImage(); // transform it 
         Dimension imgSize = new Dimension(ico.getIconWidth(), ico.getIconHeight());
         Dimension boundary = new Dimension(200, 200);
         Dimension newSize = getScaledDimension(imgSize, boundary);
         Image newimg = image.getScaledInstance(newSize.width, newSize.height,  java.awt.Image.SCALE_SMOOTH);
         ImageIcon newIco = new ImageIcon(newimg);
         jphotos[i].setIcon(newIco);
         jphotos[i].setMinimumSize(boundary);
         jphotos[i].setMaximumSize(boundary);
         
         jphotos[i].setVisible(true);
         if(i <= 3)
         jphotos[i].setBounds(i * 200, 0, 200, 200);
         else if(i >= 4 && i <=7)
         jphotos[i].setBounds((i % 4) * 200, 130, 200, 200);
         jphotos[i].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
         jphotos[i].addMouseListener(new MouseAdapter(){
                    @Override
                    public void mouseClicked(MouseEvent e){
                        try{
                        Desktop.getDesktop().browse(new URI(link));
                        }catch(URISyntaxException err){
                            
                        }catch(IOException err){
                            
                        }
                    }
                });
         container_photos.add(jphotos[i]);
         
         }catch(MalformedURLException e){
  System.out.println(photos.get(i));
         }
        }
        }

        
           
        }catch(JSONException e){
            System.out.println("ERROR JSON");
        }
        
        
    }
    
    public void loadTeams(){
        
        LinkedList<String> oesteTeams = new LinkedList();
        LinkedList<String> lesteTeams = new LinkedList();
        
        oesteTeams.add("Dallas Mavericks");
        oesteTeams.add("Denver Nuggets");
        oesteTeams.add("Golden State Warriors");
        oesteTeams.add("Houston Rockets");
        oesteTeams.add("LA Clippers");
        oesteTeams.add("Los Angeles Lakers");
        oesteTeams.add("Memphis Grizzlies");
        oesteTeams.add("Minnesota Timberwolves");
        oesteTeams.add("New Orleans Pelicans");
        oesteTeams.add("Oklahoma City Thunder");
        oesteTeams.add("Phoenix Suns");
        oesteTeams.add("Portland Trail Blazers");
        oesteTeams.add("Sacramento Kings");
        oesteTeams.add("San Antonio Spurs");
        oesteTeams.add("Utah Jazz");
        
        // Times do leste
        lesteTeams.add("Atlanta Hawks");
        lesteTeams.add("Boston Celtics");
        lesteTeams.add("Brooklyn Nets");
        lesteTeams.add("Chicago Bulls");
        lesteTeams.add("Cleveland Cavaliers");
        lesteTeams.add("Detroit Pistons");
        lesteTeams.add("Indiana Pacers");
        lesteTeams.add("Miami Heat");
        lesteTeams.add("Milwaukee Bucks");
        lesteTeams.add("New Orleans Pelicans");
        lesteTeams.add("New York Knicks");
        lesteTeams.add("Orlando Magic");
        lesteTeams.add("Philadelphia 76ers");
        lesteTeams.add("Toronto Raptors");
        lesteTeams.add("Washington Wizards");
        
        JLabel oesteThumbs[] = new JLabel[oesteTeams.size()];
        JLabel oesteTitles[] = new JLabel[oesteTeams.size()];
        JLabel lesteThumbs[] = new JLabel[lesteTeams.size()];
        JLabel lesteTitles[] = new JLabel[lesteTeams.size()];
        
        for(int i = 0; i < oesteTeams.size(); i++){
            oesteThumbs[i] = new JLabel();
            oesteTitles[i] = new JLabel();
            
            String name = oesteTeams.get(i);
            ImageIcon image = new ImageIcon(getClass().getResource("/assets/images/teams/oeste/"+ oesteTeams.get(i) +".png"));
            oesteThumbs[i].setIcon(image);
            oesteThumbs[i].setBounds(10, i * 80 + 10, 80, 80);
            oesteThumbs[i].setVisible(true);
            oesteThumbs[i].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            oesteThumbs[i].addMouseListener(new MouseAdapter(){
                    @Override
                    public void mouseClicked(MouseEvent e){
                        CardLayout cl = (CardLayout) jPanel10.getLayout();
                        cl.show(jPanel10, "teamProfile");
                        loadTeamProfile("oeste", name);
                    }
                });
            oesteTitles[i].setText(name);
            oesteTitles[i].setBounds(100, i * 80 + 10, 200, 20);
            oesteTitles[i].setVisible(true);
            oesteTitles[i].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            oesteTitles[i].addMouseListener(new MouseAdapter(){
                    @Override
                    public void mouseClicked(MouseEvent e){
                       CardLayout cl = (CardLayout) jPanel10.getLayout();
                        cl.show(jPanel10, "teamProfile");
                        loadTeamProfile("oeste", name);
                    }
                });
            oeste_team.add(oesteThumbs[i]);
            oeste_team.add(oesteTitles[i]);
        }
        
        
        // LADO LESTE
        for(int i = 0; i < lesteTeams.size(); i++){
            lesteThumbs[i] = new JLabel();
            lesteTitles[i] = new JLabel();
            
            String name = lesteTeams.get(i);
            ImageIcon image = new ImageIcon(getClass().getResource("/assets/images/teams/leste/"+ lesteTeams.get(i) +".png"));
            lesteThumbs[i].setIcon(image);
            lesteThumbs[i].setBounds(10, i * 80 + 10, 80, 80);
            lesteThumbs[i].setVisible(true);
            lesteThumbs[i].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            lesteThumbs[i].addMouseListener(new MouseAdapter(){
                    @Override
                    public void mouseClicked(MouseEvent e){
                        CardLayout cl = (CardLayout) jPanel10.getLayout();
                        cl.show(jPanel10, "teamProfile");
                        loadTeamProfile("leste", name);
                    }
                });
            lesteTitles[i].setText(name);
            lesteTitles[i].setBounds(100, i * 80 + 10, 200, 20);
            lesteTitles[i].setVisible(true);
            lesteTitles[i].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            lesteTitles[i].addMouseListener(new MouseAdapter(){
                    @Override
                    public void mouseClicked(MouseEvent e){
                       CardLayout cl = (CardLayout) jPanel10.getLayout();
                        cl.show(jPanel10, "teamProfile");
                        loadTeamProfile("leste", name);
                    }
                });
            leste_team.add(lesteThumbs[i]);
            leste_team.add(lesteTitles[i]);
        }
        
        
    }
    
    public static Dimension getScaledDimension(Dimension imgSize,Dimension boundary) {

    int original_width = imgSize.width;
    int original_height = imgSize.height;
    int bound_width = boundary.width;
    int bound_height = boundary.height;
    int new_width = original_width;
    int new_height = original_height;

    // first check if we need to scale width
    if (original_width > bound_width) {
        //scale width to fit
        new_width = bound_width;
        //scale height to maintain aspect ratio
        new_height = (new_width * original_height) / original_width;
    }

    // then check if we need to scale even with the new height
    if (new_height > bound_height) {
        //scale height to fit instead
        new_height = bound_height;
        //scale width to maintain aspect ratio
        new_width = (new_height * original_width) / original_height;
    }
    // upscale if original is smaller
    if (original_width < bound_width) {
        //scale width to fit
        new_width = bound_width;
        //scale height to maintain aspect ratio
        new_height = (new_width * original_height) / original_width;
    }

    return new Dimension(new_width, new_height);
}
    
    public void loadTeamProfile(String side, String teamName){
        String columns[] = {"NAME", "NO.", "POS", "AGE", "HT", "WT", "COLLEGE", "2016-2017 SALARY"};
        DefaultTableModel tableStatistics = (DefaultTableModel) teamRoster.getModel();
        tableStatistics.setColumnIdentifiers(columns);
        
        
        try{
            File homedir = new File(System.getProperty("user.home"));
            
            URL resource = getClass().getResource("/sources/"+ side +"/"+ teamName +".txt");
            File fileToRead = new File(resource.toURI());
            BufferedReader br = new BufferedReader(new FileReader(fileToRead));
            String line = br.readLine();

            nameTeam.setText(line);
            line = br.readLine();
            URL url = new URL(line);
            ImageIcon ico = new ImageIcon(url);
            Image newimg = ico.getImage().getScaledInstance(200, 200,  java.awt.Image.SCALE_SMOOTH);
            ImageIcon newIco = new ImageIcon(newimg);
            picTeam.setIcon(newIco);
            line = br.readLine();
            win_loses.setText(line);
            line = br.readLine();
            String[] teamStaticSplited = line.split(" ");
            pts_num.setText(teamStaticSplited[0]);
            ass_num.setText(teamStaticSplited[1]);
            rbt_num.setText(teamStaticSplited[2]);
            lvd_num.setText(teamStaticSplited[3]);
            line = br.readLine();
            
            String[] splited = line.split(" ");
            name_1.setText(splited[0] + " " + splited[1]);
            pos_1.setText(splited[2]);
            num_1.setText(splited[3]);
            line = br.readLine();
            pic_1.setIcon(new ImageIcon(new ImageIcon(new URL(line)).getImage().getScaledInstance(50, 50,  java.awt.Image.SCALE_SMOOTH)));
            line = br.readLine();
            
            splited = line.split(" ");
            name_2.setText(splited[0] + " " + splited[1]);
            pos_2.setText(splited[2]);
            num_2.setText(splited[3]);
            line = br.readLine();
            pic_2.setIcon(new ImageIcon(new ImageIcon(new URL(line)).getImage().getScaledInstance(50, 50,  java.awt.Image.SCALE_SMOOTH)));
            line = br.readLine();
            
            splited = line.split(" ");
            name_3.setText(splited[0] + " " + splited[1]);
            pos_3.setText(splited[2]);
            num_3.setText(splited[3]);
            line = br.readLine();
            pic_3.setIcon(new ImageIcon(new ImageIcon(new URL(line)).getImage().getScaledInstance(50, 50,  java.awt.Image.SCALE_SMOOTH)));
            line = br.readLine();
            
            splited = line.split(" ");
            name_4.setText(splited[0] + " " + splited[1]);
            pos_4.setText(splited[2]);
            num_4.setText(splited[3]);
            line = br.readLine();
            pic_4.setIcon(new ImageIcon(new ImageIcon(new URL(line)).getImage().getScaledInstance(50, 50,  java.awt.Image.SCALE_SMOOTH)));
            line = br.readLine();
            
            splited = line.split(" ");
            name_5.setText(splited[0] + " " + splited[1]);
            pos_5.setText(splited[2]);
            num_5.setText(splited[3]);
            line = br.readLine();
            pic_5.setIcon(new ImageIcon(new ImageIcon(new URL(line)).getImage().getScaledInstance(50, 50,  java.awt.Image.SCALE_SMOOTH)));
            line = br.readLine();
            
            splited = line.split(" ");
            name_6.setText(splited[0] + " " + splited[1]);
            pos_6.setText(splited[2]);
            num_6.setText(splited[3]);
            line = br.readLine();
            pic_6.setIcon(new ImageIcon(new ImageIcon(new URL(line)).getImage().getScaledInstance(50, 50,  java.awt.Image.SCALE_SMOOTH)));
            line = br.readLine();
            
            int count = 0, i = 0;
            String dados[] = new String[8];
            while (line != null) {
                
                if(i != 2){
                                     
                if(count < 8){
                    dados[count] = line;
                }
               
                if(count == 7){
                      tableStatistics.addRow(dados);
                      count = 0;
                      i = 0;
                }else
                    count++;
                }
                if(count != 0)
                    i++;
                    
                
                line = br.readLine();
            }
           br.close();
         
        }catch(FileNotFoundException e){
            System.out.println(e);
        }catch(IOException e){
            System.out.println(e);
        }catch(URISyntaxException e){
            System.out.println(e);
        }
    }
    
    public void loadStatistics(int year, String type){
        
        setButtons(year, type);
        
        DefaultTableModel tableStatistics = (DefaultTableModel) jTable1.getModel();
        tableStatistics.setColumnCount(this.lines.get(type) + 1);
        tableStatistics.setNumRows(0);
        tableStatistics.setColumnIdentifiers(this.columns.get(type));

        try{
            URL resource = getClass().getResource("/sources/"+ year +"/"+ type +".txt");
            File fileToRead = new File(resource.toURI());
            BufferedReader br = new BufferedReader(new FileReader(fileToRead));
            String line = br.readLine();
            int count = 0;
            int rk = 1;
            String[] dados = new String[this.lines.get(type) + 1];
            
            while (line != null) {
                if(count == 0){
                    dados[count] = String.valueOf(rk);
                    count++;
                }
                
                if(count <= this.lines.get(type)){
                    dados[count] = line;
                }
                if(count == this.lines.get(type)){
                      tableStatistics.addRow(dados);
                      count = 0;
                      rk++;
                      dados[count] = String.valueOf(rk);
                }
                
                count++;
                
                line = br.readLine();
            }
           br.close();
         
        }catch(FileNotFoundException e){
            System.out.println(e);
        }catch(IOException e){
            System.out.println(e);
        }catch(URISyntaxException e){
            System.out.println(e);
        }
    }
    
    public void setButtons(int year, String type){

        
        this.apg.setBackground(new Color(220,4,2));
        this.apg.setForeground(Color.white);
        this.rpg.setBackground(new Color(220,4,2));
        this.rpg.setForeground(Color.white);
        this.dd.setBackground(new Color(220,4,2));
        this.dd.setForeground(Color.white);
        this.fg.setBackground(new Color(220,4,2));
        this.fg.setForeground(Color.white);
        this.ft.setBackground(new Color(220,4,2));
        this.ft.setForeground(Color.white);
        this.mpg.setBackground(new Color(220,4,2));
        this.mpg.setForeground(Color.white);
        this.pfpg.setBackground(new Color(220,4,2));
        this.pfpg.setForeground(Color.white);
        this.stpg.setBackground(new Color(220,4,2));
        this.stpg.setForeground(Color.white);
        this.trp.setBackground(new Color(220,4,2));
        this.trp.setForeground(Color.white);
        this.blkpg.setBackground(new Color(220,4,2));
        this.blkpg.setForeground(Color.white);
        this.pts.setBackground(new Color(220,4,2));
        this.pts.setForeground(Color.white);

        
        switch(type){
            case "APG":
                this.apg.setBackground(Color.white);
                this.apg.setForeground(Color.black);
                break;
            case "FG%":
                this.fg.setBackground(Color.white);
                this.fg.setForeground(Color.black);
                break;
            case "FT%":
                this.ft.setBackground(Color.white);
                this.ft.setForeground(Color.black);
                break;
            case "3P%":
                this.trp.setBackground(Color.white);
                this.trp.setForeground(Color.black);
                break;
            case "PTS":
                this.pts.setBackground(Color.white);
                this.pts.setForeground(Color.black);
                break;
            case "MPG":
                this.mpg.setBackground(Color.white);
                this.mpg.setForeground(Color.black);
                break;
            case "BLKPG":
                this.blkpg.setBackground(Color.white);
                this.blkpg.setForeground(Color.black);
                break;
            case "DBLDBL":
                this.dd.setBackground(Color.white);
                this.dd.setForeground(Color.black);
                break;  
            case "STPG":
                this.stpg.setBackground(Color.white);
                this.stpg.setForeground(Color.black);
                break;  
            case "PFPG":
                this.pfpg.setBackground(Color.white);
                this.pfpg.setForeground(Color.black);
                break;  
            case "RPG":
                this.rpg.setBackground(Color.white);
                this.rpg.setForeground(Color.black);
                break;      
                        
        }
        
        
        // Years
        
        this.y2002.setBackground(new Color(220,4,2));
        this.y2002.setForeground(Color.white);
        this.y2003.setBackground(new Color(220,4,2));
        this.y2003.setForeground(Color.white);
        this.y2004.setBackground(new Color(220,4,2));
        this.y2004.setForeground(Color.white);
        this.y2005.setBackground(new Color(220,4,2));
        this.y2005.setForeground(Color.white);
        this.y2006.setBackground(new Color(220,4,2));
        this.y2006.setForeground(Color.white);
        this.y2007.setBackground(new Color(220,4,2));
        this.y2007.setForeground(Color.white);
        this.y2008.setBackground(new Color(220,4,2));
        this.y2008.setForeground(Color.white);
        this.y2009.setBackground(new Color(220,4,2));
        this.y2009.setForeground(Color.white);
        this.y2010.setBackground(new Color(220,4,2));
        this.y2010.setForeground(Color.white);
        this.y2011.setBackground(new Color(220,4,2));
        this.y2011.setForeground(Color.white);
        this.y2012.setBackground(new Color(220,4,2));
        this.y2012.setForeground(Color.white);
        this.y2013.setBackground(new Color(220,4,2));
        this.y2013.setForeground(Color.white);
        this.y2014.setBackground(new Color(220,4,2));
        this.y2014.setForeground(Color.white);
        this.y2015.setBackground(new Color(220,4,2));
        this.y2015.setForeground(Color.white);
        this.y2016.setBackground(new Color(220,4,2));
        this.y2016.setForeground(Color.white);
        this.y2017.setBackground(new Color(220,4,2));
        this.y2017.setForeground(Color.white);

        
        switch(year){
            case 2002:
                this.y2002.setBackground(Color.white);
                this.y2002.setForeground(Color.black);
                break;
            case 2003:
                this.y2003.setBackground(Color.white);
                this.y2003.setForeground(Color.black);
                break;
            case 2004:
                this.y2004.setBackground(Color.white);
                this.y2004.setForeground(Color.black);
                break;
            case 2005:
                this.y2005.setBackground(Color.white);
                this.y2005.setForeground(Color.black);
                break;
            case 2006:
                this.y2006.setBackground(Color.white);
                this.y2006.setForeground(Color.black);
                break;
            case 2007:
                this.y2007.setBackground(Color.white);
                this.y2007.setForeground(Color.black);
                break;
            case 2008:
                this.y2008.setBackground(Color.white);
                this.y2008.setForeground(Color.black);
                break;
            case 2009:
                this.y2009.setBackground(Color.white);
                this.y2009.setForeground(Color.black);
                break;   
            case 2010:
                this.y2010.setBackground(Color.white);
                this.y2010.setForeground(Color.black);
                break; 
            case 2011:
                this.y2011.setBackground(Color.white);
                this.y2011.setForeground(Color.black);
                break; 
            case 2012:
                this.y2012.setBackground(Color.white);
                this.y2012.setForeground(Color.black);
                break; 
            case 2013:
                this.y2013.setBackground(Color.white);
                this.y2013.setForeground(Color.black);
                break; 
            case 2014:
                this.y2014.setBackground(Color.white);
                this.y2014.setForeground(Color.black);
                break;  
            case 2015:
                this.y2015.setBackground(Color.white);
                this.y2015.setForeground(Color.black);
                break;  
            case 2016:
                this.y2016.setBackground(Color.white);
                this.y2016.setForeground(Color.black);
                break;  
            case 2017:
                this.y2017.setBackground(Color.white);
                this.y2017.setForeground(Color.black);
                break;                       
        }
        
    }
        
       
   static public class HeaderColor extends DefaultTableCellRenderer{
            public HeaderColor(){
                setOpaque(true);
                
            }
            
            public Component getTableCellRendererComponent(JTable jTable, Object value, boolean selected, boolean focused, int row, int column){
                super.getTableCellRendererComponent(jTable, value, selected, focused, row, column);
                setBackground(new Color(255, 255, 255));
                return this;
            }
        }



  

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Main;
    private javax.swing.JPanel Search;
    private javax.swing.JPanel Statistics;
    private javax.swing.JPanel TeamProfile;
    private javax.swing.JPanel Teams;
    private javax.swing.JLabel apg;
    private javax.swing.JLabel ass_num;
    private javax.swing.JLabel bg_main;
    private javax.swing.JLabel blkpg;
    private javax.swing.JLabel closeWindow;
    private javax.swing.JPanel container_main;
    private javax.swing.JPanel container_photos;
    private javax.swing.JPanel container_search;
    private javax.swing.JLabel dd;
    private javax.swing.JLabel fg;
    private javax.swing.JLabel ft;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JPanel leste_team;
    private javax.swing.JLabel loading;
    private javax.swing.JLabel lvd_num;
    private javax.swing.JLabel menu_home;
    private javax.swing.JLabel menu_statis;
    private javax.swing.JLabel menu_teams;
    private javax.swing.JLabel minWindow;
    private javax.swing.JLabel mpg;
    private javax.swing.JLabel namePlayer;
    private javax.swing.JLabel nameTeam;
    private javax.swing.JLabel name_1;
    private javax.swing.JLabel name_2;
    private javax.swing.JLabel name_3;
    private javax.swing.JLabel name_4;
    private javax.swing.JLabel name_5;
    private javax.swing.JLabel name_6;
    private javax.swing.JLabel num_1;
    private javax.swing.JLabel num_2;
    private javax.swing.JLabel num_3;
    private javax.swing.JLabel num_4;
    private javax.swing.JLabel num_5;
    private javax.swing.JLabel num_6;
    private javax.swing.JPanel oeste_team;
    private javax.swing.JLabel pfpg;
    private javax.swing.JLabel picPlayer;
    private javax.swing.JLabel picTeam;
    private javax.swing.JLabel pic_1;
    private javax.swing.JLabel pic_2;
    private javax.swing.JLabel pic_3;
    private javax.swing.JLabel pic_4;
    private javax.swing.JLabel pic_5;
    private javax.swing.JLabel pic_6;
    private javax.swing.JLabel playerBorn;
    private javax.swing.JLabel playerCollege;
    private javax.swing.JLabel playerDrafted;
    private javax.swing.JLabel playerExp;
    private javax.swing.JPanel playerProfile;
    private javax.swing.JTable playerStatis_1;
    private javax.swing.JTable playerStatis_2;
    private javax.swing.JTable playerStatis_3;
    private javax.swing.JLabel pos_1;
    private javax.swing.JLabel pos_2;
    private javax.swing.JLabel pos_3;
    private javax.swing.JLabel pos_4;
    private javax.swing.JLabel pos_5;
    private javax.swing.JLabel pos_6;
    private javax.swing.JLabel pts;
    private javax.swing.JLabel pts_num;
    private javax.swing.JLabel rbt_num;
    private javax.swing.JLabel result_search;
    private javax.swing.JLabel result_search1;
    private javax.swing.JLabel result_search2;
    private javax.swing.JLabel result_search3;
    private javax.swing.JLabel result_search4;
    private javax.swing.JLabel result_search6;
    private javax.swing.JLabel result_search7;
    private javax.swing.JLabel rpg;
    private javax.swing.JTextField search;
    private javax.swing.JLabel stpg;
    private javax.swing.JLabel subTitle;
    private javax.swing.JTable teamRoster;
    private javax.swing.JLabel trp;
    private javax.swing.JLabel win_loses;
    private javax.swing.JLabel y2002;
    private javax.swing.JLabel y2003;
    private javax.swing.JLabel y2004;
    private javax.swing.JLabel y2005;
    private javax.swing.JLabel y2006;
    private javax.swing.JLabel y2007;
    private javax.swing.JLabel y2008;
    private javax.swing.JLabel y2009;
    private javax.swing.JLabel y2010;
    private javax.swing.JLabel y2011;
    private javax.swing.JLabel y2012;
    private javax.swing.JLabel y2013;
    private javax.swing.JLabel y2014;
    private javax.swing.JLabel y2015;
    private javax.swing.JLabel y2016;
    private javax.swing.JLabel y2017;
    // End of variables declaration//GEN-END:variables
}





