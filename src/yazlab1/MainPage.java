/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yazlab1;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

/**
 *
 * @author MehmetFirat
 */
public class MainPage extends javax.swing.JFrame {

    /**
     * Creates new form MainPage
     */
    ArrayList<String[]> isbnAndRatings = new ArrayList<String[]>();
    static double mainRowSize = 10.0;
    JTable recoveryMainTable;

    public MainPage() throws SQLException {
        initComponents();
        String refreshPath = "img/refresh.png";
        String homePath = "img/home.png";
        String logOffPath = "img/logoff.png";
        ImageIcon icon = new ImageIcon(refreshPath);
        refreshlbl.setIcon(icon);
        icon = new ImageIcon(homePath);
        homelbl.setIcon(icon);
        icon = new ImageIcon(logOffPath);
        logofflbl.setIcon(icon);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);

        best10Books();
        popular10Books();
        new5Books();
        MyItemListener actionListener = new MyItemListener();
        pagecb.addItemListener(actionListener);
        search();
        recommendedBooks();
        RecommendTable.setLayout(new BorderLayout());
        jScrollPane2.setViewportView(RecommendTable);
        jScrollPane5.setVisible(false);
    }


    public void search() {
        searchtf.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    try {
                        recommendlbl.setText("Search results for '" + searchtf.getText() + "'.");
                        DefaultTableModel model = (DefaultTableModel) MainTable.getModel();
                        model.setNumRows(0);
                        pagecb.removeAllItems();
                        YazLab1 yazlab = new YazLab1();
                        Connection conn = DriverManager.getConnection(yazlab.getHost(), yazlab.getUName(), yazlab.getUPass());
                        Statement stmt = conn.createStatement();
                        String query = "SELECT count(*) FROM bx_books "
                                + "WHERE book_title LIKE '%" + searchtf.getText() + "%' OR book_author LIKE '%" + searchtf.getText() + "%'";
                        ResultSet rs = stmt.executeQuery(query);
                        rs.next();
                        int rowCounter = rs.getInt("count(*)");
                        System.out.println("Sum of book rows: " + rowCounter);
                        double sumPage = Math.ceil(rowCounter / mainRowSize);
                        rs.close();
                        conn.close();
                        for (int i = 1; i <= sumPage; i++) {
                            pagecb.addItem(Integer.toString(i));
                        }
                        jScrollPane2.setViewportView(MainTable);
                        jScrollPane5.setVisible(false);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(MainPage.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (SQLException ex) {
                        Logger.getLogger(MainPage.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });

        MainTable.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent mouseEvent) {
                JTable table = (JTable) mouseEvent.getSource();
                Point point = mouseEvent.getPoint();
                int row = table.rowAtPoint(point);
                if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    String selectedIsbn = MainTable.getValueAt(row, 0).toString();
                    setVisible(false);
                    try {
                        new BookDetails(selectedIsbn).setVisible(true);
                    } catch (SQLException ex) {
                        Logger.getLogger(MainPage.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(MainPage.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(MainPage.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
    }

    class MyItemListener implements ItemListener {

        // This method is called only if a new item has been selected.
        public void itemStateChanged(ItemEvent evt) {
            JComboBox cb = (JComboBox) evt.getSource();
            Object item = evt.getItem();
            if (evt.getStateChange() == ItemEvent.SELECTED) {
                try {
                    // Item was just selected
                    DefaultTableModel model = (DefaultTableModel) MainTable.getModel();
                    model.setNumRows(0);
                    YazLab1 yazlab = new YazLab1();
                    Connection conn = DriverManager.getConnection(yazlab.getHost(), yazlab.getUName(), yazlab.getUPass());
                    Statement stmt = conn.createStatement();
                    String query = "SELECT isbn,book_title,book_author,image_url_s FROM bx_books "
                            + "WHERE book_title LIKE '%" + searchtf.getText() + "%' OR book_author LIKE '%" + searchtf.getText() + "%' "
                            + "LIMIT " + (int) ((Integer.parseInt(pagecb.getSelectedItem().toString()) - 1) * mainRowSize) + "," + (int) mainRowSize;
                    System.out.println(query);
                    ResultSet rs = stmt.executeQuery(query);

                    while (rs.next()) {
                        URL url;
                        try {
                            url = new URL(rs.getString("image_url_s"));
                            BufferedImage img = ImageIO.read(url);
                            ImageIcon icon = new ImageIcon(img);
                            JLabel lbl = new JLabel();
                            lbl.setIcon(icon);
                            model.addRow(new Object[]{rs.getString("isbn"), rs.getString("book_title"), rs.getString("book_author"), lbl});
                        } catch (MalformedURLException ex) {
                            Logger.getLogger(MainPage.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IOException ex) {
                            Logger.getLogger(MainPage.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        MainTable.getColumn("Title").setCellRenderer(new WordWrapCellRenderer());
                        MainTable.getColumn("Image").setCellRenderer(new LabelRenderer());
                        MainTable.getColumnModel().getColumn(0).setMinWidth(0);
                        MainTable.getColumnModel().getColumn(0).setMaxWidth(0);
                        MainTable.getColumnModel().getColumn(1).setMinWidth(200);
                        MainTable.getColumnModel().getColumn(2).setMinWidth(170);
                    }
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(MainPage.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(MainPage.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else if (evt.getStateChange() == ItemEvent.DESELECTED) {
                // Item is no longer selected
            }
        }
    }

    public void recommendedBooks() throws SQLException {
        try {
            BookRecommender recommend = new BookRecommender();
            ArrayList<Integer> booksToRecommend = new ArrayList<Integer>();
            for (int i = 0;i < recommend.getResultsOfCosineValues().size(); i++) {
                for (int j = 0; j < recommend.getData().length; j++) {
                    if (recommend.getResultsOfCosineValues().get(i).user_id == recommend.getData()[j].user_id) {
                        for (int k = 0; k < recommend.getData()[j].book_id.size(); k++) {
                            booksToRecommend.add(recommend.getData()[j].book_id.get(k));
                        }
                    }
                }
            }
            System.out.println("recommend size: " + booksToRecommend.size());
            for (int i = 0; i < booksToRecommend.size(); i++) {
                System.out.println("Book to recommend: " + booksToRecommend.get(i));
            }
            YazLab1 yazlab = new YazLab1();
            Connection conn = DriverManager.getConnection(yazlab.getHost(), yazlab.getUName(), yazlab.getUPass());
            Statement stmt = conn.createStatement();
            String query = "SELECT book_id FROM bx_books";
            ResultSet rs = stmt.executeQuery(query);
            Statement stmt2 = conn.createStatement();
            DefaultTableModel model = (DefaultTableModel) RecommendTable.getModel();
            int bookCounter = 0;
            while (rs.next()) {
                int bookIdValue = rs.getInt("book_id");
                if (booksToRecommend.contains(bookIdValue) && bookCounter < 20) {
                    String imageQuery = String.format("SELECT isbn,book_title,book_author,image_url_s FROM bx_books "
                            + "WHERE book_id = '%s'", bookIdValue);

//                    System.out.println(imageQuery);
                    ResultSet rs2 = stmt2.executeQuery(imageQuery);
                    rs2.next();
                    URL url;
                    try {
                        url = new URL(rs2.getString("image_url_s"));
                        BufferedImage img = ImageIO.read(url);
                        ImageIcon icon = new ImageIcon(img);
                        JLabel lbl = new JLabel();
                        lbl.setIcon(icon);
                        model.addRow(new Object[]{rs2.getString("isbn"), rs2.getString("book_title"), rs2.getString("book_author"), lbl});
                    } catch (MalformedURLException ex) {
                        Logger.getLogger(MainPage.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(MainPage.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    RecommendTable.getColumn("Title").setCellRenderer(new WordWrapCellRenderer());
                    RecommendTable.getColumn("Image").setCellRenderer(new LabelRenderer());
                    RecommendTable.getColumnModel().getColumn(0).setMinWidth(0);
                    RecommendTable.getColumnModel().getColumn(0).setMaxWidth(0);
                    rs2.close();
                    bookCounter++;
                }
            }
            rs.close();
            conn.close();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MainPage.class.getName()).log(Level.SEVERE, null, ex);
        }

        RecommendTable.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent mouseEvent) {
                JTable table = (JTable) mouseEvent.getSource();
                Point point = mouseEvent.getPoint();
                int row = table.rowAtPoint(point);
                if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    String selectedIsbn = RecommendTable.getValueAt(row, 0).toString();
                    setVisible(false);
                    try {
                        new BookDetails(selectedIsbn).setVisible(true);
                    } catch (SQLException ex) {
                        Logger.getLogger(MainPage.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(MainPage.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(MainPage.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
    }

    public void new5Books() {
        try {
            YazLab1 yazlab = new YazLab1();
            Connection conn = DriverManager.getConnection(yazlab.getHost(), yazlab.getUName(), yazlab.getUPass());
            Statement stmt = conn.createStatement();
            String query = "SELECT isbn FROM bx_last_added_books";
            ResultSet rs = stmt.executeQuery(query);
            Statement stmt2 = conn.createStatement();
            DefaultTableModel model = (DefaultTableModel) NewBooksTable.getModel();
            while (rs.next()) {
                String isbnValue = rs.getString("isbn");
                String imageQuery = String.format("SELECT book_title,image_url_s FROM bx_books "
                        + "WHERE isbn = '%s'", isbnValue);
                System.out.println(imageQuery);
                ResultSet rs2 = stmt2.executeQuery(imageQuery);
                rs2.next();
                URL url;
                try {
                    url = new URL(rs2.getString("image_url_s"));
                    BufferedImage img = ImageIO.read(url);
                    ImageIcon icon = new ImageIcon(img);
                    JLabel lbl = new JLabel();
                    lbl.setIcon(icon);
                    model.addRow(new Object[]{isbnValue, rs2.getString("book_title"), lbl});
                } catch (MalformedURLException ex) {
                    Logger.getLogger(MainPage.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(MainPage.class.getName()).log(Level.SEVERE, null, ex);
                }
                NewBooksTable.getColumn("Title").setCellRenderer(new WordWrapCellRenderer());
                NewBooksTable.getColumn("Image").setCellRenderer(new LabelRenderer());
                NewBooksTable.getColumnModel().getColumn(0).setMinWidth(0);
                NewBooksTable.getColumnModel().getColumn(0).setMaxWidth(0);
                rs2.close();
            }
            rs.close();
            conn.close();

            NewBooksTable.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent mouseEvent) {
                    JTable table = (JTable) mouseEvent.getSource();
                    Point point = mouseEvent.getPoint();
                    int row = table.rowAtPoint(point);
                    if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
                        String selectedIsbn = NewBooksTable.getValueAt(row, 0).toString();
                        setVisible(false);
                        try {
                            new BookDetails(selectedIsbn).setVisible(true);
                        } catch (SQLException ex) {
                            Logger.getLogger(MainPage.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (ClassNotFoundException ex) {
                            Logger.getLogger(MainPage.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IOException ex) {
                            Logger.getLogger(MainPage.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            });
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MainPage.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(MainPage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void popular10Books() {
        try {
            YazLab1 yazlab = new YazLab1();
            Connection conn = DriverManager.getConnection(yazlab.getHost(), yazlab.getUName(), yazlab.getUPass());
            Statement stmt = conn.createStatement();
            String query = "SELECT isbn,count(*) FROM bx_book_ratings "
                    + "GROUP BY isbn ORDER BY count(*) DESC LIMIT 10";
            ResultSet rs = stmt.executeQuery(query);
            Statement stmt2 = conn.createStatement();
            DefaultTableModel model = (DefaultTableModel) PopularBooksTable.getModel();
            while (rs.next()) {
                String isbnValue = rs.getString("isbn");
                int bookRatingValue = rs.getInt("count(*)");
                String imageQuery = String.format("SELECT book_title,image_url_s FROM bx_books "
                        + "WHERE isbn = '%s'", isbnValue);
                System.out.println(imageQuery);
                ResultSet rs2 = stmt2.executeQuery(imageQuery);
                rs2.next();
                URL url;
                try {
                    url = new URL(rs2.getString("image_url_s"));
                    BufferedImage img = ImageIO.read(url);
                    ImageIcon icon = new ImageIcon(img);
                    JLabel lbl = new JLabel();
                    lbl.setIcon(icon);
                    model.addRow(new Object[]{isbnValue, rs2.getString("book_title"), lbl, bookRatingValue});
                } catch (MalformedURLException ex) {
                    Logger.getLogger(MainPage.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(MainPage.class.getName()).log(Level.SEVERE, null, ex);
                }

                PopularBooksTable.getColumn("Title").setCellRenderer(new WordWrapCellRenderer());
                PopularBooksTable.getColumn("Image").setCellRenderer(new LabelRenderer());
                PopularBooksTable.getColumnModel().getColumn(0).setMinWidth(0);
                PopularBooksTable.getColumnModel().getColumn(0).setMaxWidth(0);
                PopularBooksTable.getColumnModel().getColumn(1).setMinWidth(100);
                PopularBooksTable.getColumnModel().getColumn(3).setMaxWidth(50);
                rs2.close();
            }
            rs.close();
            conn.close();

            PopularBooksTable.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent mouseEvent) {
                    JTable table = (JTable) mouseEvent.getSource();
                    Point point = mouseEvent.getPoint();
                    int row = table.rowAtPoint(point);
                    if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
                        String selectedIsbn = PopularBooksTable.getValueAt(row, 0).toString();
                        setVisible(false);
                        try {
                            new BookDetails(selectedIsbn).setVisible(true);
                        } catch (SQLException ex) {
                            Logger.getLogger(MainPage.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (ClassNotFoundException ex) {
                            Logger.getLogger(MainPage.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IOException ex) {
                            Logger.getLogger(MainPage.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            });
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MainPage.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(MainPage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void best10Books() {
        try {
            YazLab1 yazlab = new YazLab1();
            Connection conn = DriverManager.getConnection(yazlab.getHost(), yazlab.getUName(), yazlab.getUPass());
            Statement stmt = conn.createStatement();
            String query = "SELECT isbn,avg(book_rating),count(*) FROM bx_book_ratings "
                    + "GROUP BY isbn ORDER BY avg(book_rating) DESC, count(*) DESC LIMIT 10";
            ResultSet rs = stmt.executeQuery(query);
            Statement stmt2 = conn.createStatement();
            DefaultTableModel model = (DefaultTableModel) BestBooksTable.getModel();
            while (rs.next()) {
                String isbnValue = rs.getString("isbn");
                int bookRatingValue = rs.getInt("avg(book_rating)");
                String imageQuery = String.format("SELECT book_title,image_url_s FROM bx_books "
                        + "WHERE isbn = '%s'", isbnValue);
                System.out.println(imageQuery);
                ResultSet rs2 = stmt2.executeQuery(imageQuery);
                rs2.next();
                URL url;
                try {
                    url = new URL(rs2.getString("image_url_s"));
                    BufferedImage img = ImageIO.read(url);
                    ImageIcon icon = new ImageIcon(img);
                    JLabel lbl = new JLabel();
                    lbl.setIcon(icon);
                    model.addRow(new Object[]{isbnValue, rs2.getString("book_title"), lbl, bookRatingValue});
                } catch (MalformedURLException ex) {
                    Logger.getLogger(MainPage.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(MainPage.class.getName()).log(Level.SEVERE, null, ex);
                }
                BestBooksTable.getColumn("Title").setCellRenderer(new WordWrapCellRenderer());
                BestBooksTable.getColumn("Image").setCellRenderer(new LabelRenderer());
                BestBooksTable.getColumnModel().getColumn(0).setMinWidth(0);
                BestBooksTable.getColumnModel().getColumn(0).setMaxWidth(0);
                BestBooksTable.getColumnModel().getColumn(1).setMinWidth(110);
                BestBooksTable.getColumnModel().getColumn(3).setMaxWidth(50);
                rs2.close();
            }
            rs.close();
            conn.close();

            BestBooksTable.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent mouseEvent) {
                    JTable table = (JTable) mouseEvent.getSource();
                    Point point = mouseEvent.getPoint();
                    int row = table.rowAtPoint(point);
                    if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
                        String selectedIsbn = BestBooksTable.getValueAt(row, 0).toString();
                        setVisible(false);
                        try {
                            new BookDetails(selectedIsbn).setVisible(true);
                        } catch (SQLException ex) {
                            Logger.getLogger(MainPage.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (ClassNotFoundException ex) {
                            Logger.getLogger(MainPage.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IOException ex) {
                            Logger.getLogger(MainPage.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            });
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MainPage.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(MainPage.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    class LabelRenderer extends JTextArea implements TableCellRenderer {

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            TableColumn tc = BestBooksTable.getColumn("Image");
            tc.setMinWidth(50);
            BestBooksTable.setRowHeight(100);
            PopularBooksTable.setRowHeight(100);
            NewBooksTable.setRowHeight(100);
            MainTable.setRowHeight(100);
            RecommendTable.setRowHeight(100);
            return (Component) value;
        }
    }

    static class WordWrapCellRenderer extends JTextArea implements TableCellRenderer {

        WordWrapCellRenderer() {
            setLineWrap(true);
            setWrapStyleWord(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText(value.toString());
            return this;
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        searchtf = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        BestBooksTable = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        MainTable = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        PopularBooksTable = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        NewBooksTable = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        recommendlbl = new javax.swing.JLabel();
        pagecb = new javax.swing.JComboBox<>();
        refreshlbl = new javax.swing.JLabel();
        homelbl = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        RecommendTable = new javax.swing.JTable();
        logofflbl = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        searchtf.setText("Search for the book you wanna read...");
        searchtf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchtfActionPerformed(evt);
            }
        });

        BestBooksTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ISBN", "Title", "Image", "Rating"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.String.class, java.lang.Object.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(BestBooksTable);

        MainTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ISBN", "Title", "Author", "Image"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(MainTable);

        PopularBooksTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ISBN", "Title", "Image", "Vote"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(PopularBooksTable);

        NewBooksTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ISBN", "Title", "Image"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane4.setViewportView(NewBooksTable);

        jLabel1.setText("Best 10 Books:");

        jLabel2.setText("Popular 10 Books:");

        jLabel3.setText("Last Added 5 Books:");

        recommendlbl.setText("Recommended Books For You:");

        pagecb.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Sayfa" }));

        refreshlbl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                refreshlblMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                refreshlblMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                refreshlblMouseExited(evt);
            }
        });

        homelbl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                homelblMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                homelblMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                homelblMouseExited(evt);
            }
        });

        RecommendTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ISBN", "Title", "Author", "Image"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane5.setViewportView(RecommendTable);

        logofflbl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                logofflblMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                logofflblMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                logofflblMouseExited(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(recommendlbl)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(searchtf)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 566, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(pagecb, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(209, 209, 209)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                .addGap(10, 10, 10))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 159, Short.MAX_VALUE)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(refreshlbl, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(homelbl, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(logofflbl, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchtf, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(refreshlbl, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(homelbl, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(logofflbl, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(recommendlbl)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 480, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(pagecb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 5, Short.MAX_VALUE)))
                .addContainerGap())
        );

        getAccessibleContext().setAccessibleName("frame1");
        getAccessibleContext().setAccessibleDescription("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void searchtfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchtfActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchtfActionPerformed

    private void refreshlblMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_refreshlblMouseClicked
        // TODO add your handling code here:
        dispose();
        new Loading().setVisible(true);
    }//GEN-LAST:event_refreshlblMouseClicked

    private void refreshlblMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_refreshlblMouseEntered
        // TODO add your handling code here:
        refreshlbl.setOpaque(true);
        refreshlbl.setBackground(new Color(253, 255, 229));
    }//GEN-LAST:event_refreshlblMouseEntered

    private void refreshlblMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_refreshlblMouseExited
        // TODO add your handling code here:
        refreshlbl.setOpaque(false);
        refreshlbl.setBackground(new Color(240, 240, 240));
    }//GEN-LAST:event_refreshlblMouseExited

    private void homelblMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_homelblMouseEntered
        // TODO add your handling code here:
        homelbl.setOpaque(true);
        homelbl.setBackground(new Color(253, 255, 229));
    }//GEN-LAST:event_homelblMouseEntered

    private void homelblMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_homelblMouseExited
        // TODO add your handling code here:
        homelbl.setOpaque(false);
        homelbl.setBackground(new Color(240, 240, 240));
    }//GEN-LAST:event_homelblMouseExited

    private void homelblMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_homelblMouseClicked
        // TODO add your handling code here:
        RecommendTable.setLayout(new BorderLayout());
        jScrollPane2.setViewportView(RecommendTable);
        jScrollPane5.setVisible(false);
        recommendlbl.setText("Recommended Books For You:");
    }//GEN-LAST:event_homelblMouseClicked

    private void logofflblMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logofflblMouseClicked
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_logofflblMouseClicked

    private void logofflblMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logofflblMouseEntered
        // TODO add your handling code here:
        logofflbl.setOpaque(true);
        logofflbl.setBackground(new Color(253, 255, 229));
    }//GEN-LAST:event_logofflblMouseEntered

    private void logofflblMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logofflblMouseExited
        // TODO add your handling code here:
        logofflbl.setOpaque(false);
        logofflbl.setBackground(new Color(240, 240, 240));
    }//GEN-LAST:event_logofflblMouseExited

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
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new MainPage().setVisible(true);
                } catch (SQLException ex) {
                    Logger.getLogger(MainPage.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    javax.swing.JTable BestBooksTable;
    javax.swing.JTable MainTable;
    javax.swing.JTable NewBooksTable;
    javax.swing.JTable PopularBooksTable;
    javax.swing.JTable RecommendTable;
    javax.swing.JLabel homelbl;
    javax.swing.JLabel jLabel1;
    javax.swing.JLabel jLabel2;
    javax.swing.JLabel jLabel3;
    javax.swing.JScrollPane jScrollPane1;
    javax.swing.JScrollPane jScrollPane2;
    javax.swing.JScrollPane jScrollPane3;
    javax.swing.JScrollPane jScrollPane4;
    javax.swing.JScrollPane jScrollPane5;
    javax.swing.JLabel logofflbl;
    javax.swing.JComboBox<String> pagecb;
    javax.swing.JLabel recommendlbl;
    javax.swing.JLabel refreshlbl;
    javax.swing.JTextField searchtf;
    // End of variables declaration//GEN-END:variables
}
