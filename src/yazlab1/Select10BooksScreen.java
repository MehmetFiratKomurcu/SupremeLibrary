/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yazlab1;

import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author MehmetFirat
 */
public class Select10BooksScreen extends javax.swing.JFrame {

    ArrayList<String[]> bookAndStar = new ArrayList<String[]>();

    /**
     * Creates new form Select10BooksScreen
     */
    public Select10BooksScreen() {
        initComponents();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);

    }
    String username = "";
    String password = "";
    String location = "";
    int age = 1;

    public Select10BooksScreen(String username, String password, String location, int age) {
        initComponents();
        this.username = username;
        this.password = password;
        this.location = location;
        this.age = age;
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
        jPopupMenu2 = new javax.swing.JPopupMenu();
        jComboBox1 = new javax.swing.JComboBox<>();
        booktf = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        Select10BooksTable = new javax.swing.JTable();
        bookSearchbtn = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        LogInScreenBtn = new javax.swing.JButton();
        starComboBox = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jLabel1.setText("Please star at least 10 books you have read.");

        jLabel2.setText("Search");

        Select10BooksTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "isbn", "book_title", "book_author", "year_of_publication", "publisher"
            }
        ));
        jScrollPane2.setViewportView(Select10BooksTable);

        bookSearchbtn.setText("Submit");
        bookSearchbtn.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                bookSearchbtnMouseMoved(evt);
            }
        });
        bookSearchbtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bookSearchbtnMouseClicked(evt);
            }
        });
        bookSearchbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bookSearchbtnActionPerformed(evt);
            }
        });

        jButton1.setText("submit");
        jButton1.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                jButton1MouseMoved(evt);
            }
        });
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        LogInScreenBtn.setText("Login Screen");
        LogInScreenBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LogInScreenBtnActionPerformed(evt);
            }
        });

        starComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" }));
        starComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                starComboBoxActionPerformed(evt);
            }
        });

        jLabel3.setText("Star");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(59, 59, 59)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(43, 43, 43)
                        .addComponent(booktf, javax.swing.GroupLayout.PREFERRED_SIZE, 419, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bookSearchbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 264, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(24, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(LogInScreenBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addGap(65, 65, 65)
                .addComponent(starComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(121, 121, 121)
                .addComponent(jButton1)
                .addGap(53, 53, 53))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(booktf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(bookSearchbtn))
                .addGap(24, 24, 24)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(LogInScreenBtn)
                    .addComponent(starComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bookSearchbtnMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bookSearchbtnMouseMoved
        // TODO add your handling code here:
        if (booktf.getText().equals("")) {
            bookSearchbtn.setEnabled(false);
        } else {
            bookSearchbtn.setEnabled(true);
        }
    }//GEN-LAST:event_bookSearchbtnMouseMoved

    private void bookSearchbtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bookSearchbtnMouseClicked
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel) Select10BooksTable.getModel();
        model.setRowCount(0);
        String bookSearch = booktf.getText();
        try {
            YazLab1 yazlab = new YazLab1();
            Connection conn = DriverManager.getConnection(yazlab.getHost(), yazlab.getUName(), yazlab.getUPass());
            Statement stmt = conn.createStatement();
            String bookQuery = "SELECT isbn,book_title,book_author,year_of_publication,publisher"
                    + " FROM bx_books WHERE book_title LIKE '%"
                    + bookSearch + "%'";
            //System.out.println(bookQuery);
            ResultSet rs = stmt.executeQuery(bookQuery);

            while (rs.next()) {
                model.addRow(new Object[]{rs.getString("isbn"), rs.getString("book_title"),
                    rs.getString("book_author"), rs.getString("year_of_publication"),
                    rs.getString("publisher")});
                System.out.println(rs.getString("isbn"));
            }

            rs.close();
            conn.close();
        } catch (HeadlessException | ClassNotFoundException | SQLException e) {
            System.out.println(e.getMessage());
        }

    }//GEN-LAST:event_bookSearchbtnMouseClicked

    private void bookSearchbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bookSearchbtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_bookSearchbtnActionPerformed

    private void jButton1MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseMoved
        // TODO add your handling code here:


    }//GEN-LAST:event_jButton1MouseMoved

    private void starComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_starComboBoxActionPerformed
        // TODO add your handling code here:
        String starValue = starComboBox.getSelectedItem().toString();
        String[] bookStarPair = new String[2];
        int selectedRow = Select10BooksTable.getSelectedRow();
        bookStarPair[0] = (String) Select10BooksTable.getValueAt(selectedRow, 0);
        bookStarPair[1] = starValue;
        if (!bookAndStar.contains(bookStarPair)) {
            bookAndStar.add(bookStarPair);
        }

    }//GEN-LAST:event_starComboBoxActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            // TODO add your handling code here:
            YazLab1 yazlab = new YazLab1();
            Random generator = new Random();
            Connection conn = DriverManager.getConnection(yazlab.getHost(), yazlab.getUName(), yazlab.getUPass());
            Statement stmt = conn.createStatement();
            Statement stmt2 = conn.createStatement();
            if (Select10BooksTable.getSelectedRow() == -1) {
                JOptionPane.showMessageDialog(null, "You haven't selected any book");
            } else {
                if (bookAndStar.size() < 10) {
                    JOptionPane.showMessageDialog(null, "You have to select 10 books at least");
                } else {
                    String insertquery = String.format("INSERT INTO bx_users(user_id,location,age,username,password) "
                            + "VALUES('%d','%s', '%s','%s', '%s')", generator.nextInt(), location, age, username, password);
                    stmt.executeUpdate(insertquery);
                    String getIdUsersql = String.format("SELECT user_id FROM bx_users where username = '%s'",
                            username);
                    ResultSet useridsql = stmt.executeQuery(getIdUsersql);
                    useridsql.next();
                    //System.out.println(useridsql.getString("user_id"));
                    //String insertquery2 = String.format("INSERT INTO bx_users(user_id,location, age) "
                    //                    + "VALUES('%s','%s', '%s')", useridsql.getString("user_id"), locationtf.getText(), agetf.getText());
                    //stmt.executeUpdate(insertquery2);
                    //System.out.println(bookAndStar.get(1).toString());
                    for (String[] pairValues : bookAndStar) {
                        String insertQuery2 = String.format("INSERT INTO bx_book_ratings (user_id,isbn,book_rating)"
                                + "VALUES('%d','%s','%d')", Integer.parseInt(useridsql.getString("user_id")),
                                pairValues[0], Integer.parseInt(pairValues[1]));
                        System.out.println(insertQuery2);
                        stmt2.addBatch(insertQuery2);
                    }
                    stmt2.executeBatch();
                    JOptionPane.showMessageDialog(null, "Congrulations, You have been successfully registered");
                    dispose();
                    new LogInScreen().setVisible(true);
                }
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Select10BooksScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void LogInScreenBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LogInScreenBtnActionPerformed
        // TODO add your handling code here:
        this.setVisible(false);
        new LogInScreen().setVisible(true);
    }//GEN-LAST:event_LogInScreenBtnActionPerformed

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
            java.util.logging.Logger.getLogger(Select10BooksScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Select10BooksScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Select10BooksScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Select10BooksScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Select10BooksScreen().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton LogInScreenBtn;
    private javax.swing.JTable Select10BooksTable;
    private javax.swing.JButton bookSearchbtn;
    private javax.swing.JTextField booktf;
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JPopupMenu jPopupMenu2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JComboBox<String> starComboBox;
    // End of variables declaration//GEN-END:variables
}
