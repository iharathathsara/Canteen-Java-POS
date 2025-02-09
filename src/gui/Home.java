/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.regex.Pattern;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import log.Log;
import model.MySQL;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import org.apache.log4j.Logger;
import util.PasswordUtils;
import util.UserUtils;

/**
 *
 * @author Ihara
 */
public final class Home extends javax.swing.JFrame {

    /**
     * Creates new form Home
     */
    private Timer timer;
    public static String logged_user_id;
    public static String logged_user_type_id;
    DecimalFormat df = new DecimalFormat("0.00");
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    private static final Logger LOG = Log.getLogger(Home.class);

    public Home(String user_id) {
        initComponents();
        setExtendedState(MAXIMIZED_BOTH);
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/image/canteen.png")));
        this.logged_user_id = user_id;

        LOG.info("INFO:: run home");

        getLoggedUser();
        setDateTime();
        
        

//        LOG.debug("DEBUG:: debug test");
//        LOG.info("INFO:: debug test");
//        LOG.error("ERROR:: debug test");
//        LOG.warn("WARNING:: debug test");
//        LOG.fatal("FATAL:: debug test");
    }

    public void setDateTime() {
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Update the JLabel with the current date and time
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date now = new Date();
                jLabel70.setText(dateFormat.format(now));
            }
        });
        timer.start();
    }

    public void getLoggedUser() {
        try {
            ResultSet loggedUser_rs = MySQL.search("SELECT `user`.`fname`,`user`.`lname`,`user`.`email`,`user`.`mobile`,`user_type`.`id` AS `user_type_id`,`user_type`.`name` AS `user_type`,`user`.`line1`,`user`.`line2`,`user`.`city` FROM `user` INNER JOIN `user_type` ON `user`.`user_type_id`=`user_type`.`id` WHERE `user`.`id`=?", logged_user_id);
            loggedUser_rs.next();
            String loggedUserName = loggedUser_rs.getString("fname") + " " + loggedUser_rs.getString("lname") + " (" + loggedUser_rs.getString("user_type") + ")";
            loggedUserName = loggedUserName.toUpperCase();
            logged_user_type_id = loggedUser_rs.getString("user_type_id");
            jLabel69.setText(loggedUserName);
            
            LOG.info("INFO:: get logged user data success");
            
            if (logged_user_type_id.equals("4")) {
                jButton18.setEnabled(false);
                jButton23.setEnabled(false);
                jButton24.setEnabled(false);
                jButton29.setEnabled(false);
            }else if(logged_user_type_id.equals("5")){
                jButton18.setEnabled(false);
                jButton22.setEnabled(false);
                jButton23.setEnabled(false);
                jButton24.setEnabled(false);
                jButton29.setEnabled(false);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("ERROR:: get logged user data error");
        }
    }

////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////STOCK//////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
    public void loadStockPanel() {
        LOG.info("INFO:: load Stock");
        loadBrandComboStock();
        loadCategoryComboStock();
        loadStockTable();
        tableFontStock();
    }

    public void resetStockPanel() {
        LOG.info("INFO:: reset Stock");
        jComboBox7.setSelectedIndex(0);
        jComboBox8.setSelectedIndex(0);
        jComboBox9.setSelectedIndex(0);
        jTextField23.setText("");
        jTextField24.setText("");
        jTextField25.setText("");
        jTextField26.setText("");
        jDateChooser3.setDate(null);
        jDateChooser4.setDate(null);
        jDateChooser5.setDate(null);
        jDateChooser6.setDate(null);
        jLabel83.setText("0.00");
        jTable5.clearSelection();
        loadStockTable();
    }

    public void searchStock() {
        try {
            LOG.info("INFO:: search Stock");
            String category = "SELECT";
            if (jComboBox7.getSelectedItem() != null) {
                category = jComboBox7.getSelectedItem().toString();
            }

            String brand = "SELECT";
            if (jComboBox8.getSelectedItem() != null) {
                brand = jComboBox8.getSelectedItem().toString();
            }
//            String category =jComboBox7.getSelectedItem()toString();
//            String brand = jComboBox8.getSelectedItem().toString();
            String name = jTextField23.getText();
            String sp_min = jTextField24.getText();
            String sp_max = jTextField25.getText();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date d = new Date();
            String date = sdf.format(d);

            String mfd_fr = null;
            String mfd_to = null;
            String exd_fr = null;
            String exd_to = null;

            if (jDateChooser3.getDate() != null) {
                mfd_fr = sdf.format(jDateChooser3.getDate());
            }
            if (jDateChooser4.getDate() != null) {
                mfd_to = sdf.format(jDateChooser4.getDate());
            }
            if (jDateChooser5.getDate() != null) {
                exd_fr = sdf.format(jDateChooser5.getDate());
            }
            if (jDateChooser6.getDate() != null) {
                exd_to = sdf.format(jDateChooser6.getDate());
            }
            int sort = jComboBox9.getSelectedIndex();
            Vector queryVector = new Vector();
            if (category.equals("SELECT")) {
            } else {
                queryVector.add("`category`.`name`='" + category + "'");
            }
            if (brand.equals("SELECT")) {
            } else {
                queryVector.add("`brand`.`name`='" + brand + "'");
            }
            if (name.isEmpty()) {
            } else {
                queryVector.add("`product`.`title` LIKE '%" + name + "%'");
            }
            if (!sp_min.isEmpty()) {
                if (sp_max.isEmpty()) {
                    queryVector.add("`stock`.`selling_price` >= '" + sp_min + "'");
                } else {
                    queryVector.add("`stock`.`selling_price` >= '" + sp_min + "' AND `stock`.`selling_price` <= '" + sp_max + "'");
                }
            }

            if (!sp_max.isEmpty()) {
                if (sp_min.isEmpty()) {
                    queryVector.add("`stock`.`selling_price` <= '" + sp_max + "'");
                }
            }

            if (mfd_fr != null) {
                if (mfd_to == null) {
                    queryVector.add("`stock`.`mfd` >= '" + mfd_fr + "'");
                } else {
                    queryVector.add("`stock`.`mfd` >= '" + mfd_fr + "' AND `stock`.`mfd` <= '" + mfd_to + "'");
                }
            }

            if (mfd_to != null) {
                if (mfd_fr == null) {
                    queryVector.add("`stock`.`mfd` <= '" + mfd_to + "'");
                }
            }

            if (exd_fr != null) {
                queryVector.add("`stock`.`exd` >= '" + date + "'");
                if (exd_to == null) {
                    queryVector.add("`stock`.`exd` >= '" + exd_fr + "'");
                } else {
                    queryVector.add("`stock`.`exd` >= '" + exd_fr + "' AND `stock`.`exd` <= '" + exd_to + "'");
                }
            }

            if (exd_to != null) {
                if (exd_fr == null) {
                    queryVector.add("`stock`.`exd` <= '" + exd_to + "'");
                }
            }

            String wherequery = "WHERE `stock`.`quantity`>='0' ";

            for (int i = 0; i < queryVector.size(); i++) {
                wherequery += " AND ";
                wherequery += queryVector.get(i);
                wherequery += " ";
            }

            String sortquery;

            if (sort == 0) {
                sortquery = "`product`.`title` ASC";
            } else if (sort == 1) {
                sortquery = "`product`.`title` DESC";
            } else if (sort == 2) {
                sortquery = "`stock`.`selling_price` ASC";
            } else if (sort == 3) {
                sortquery = "`stock`.`selling_price` DESC";
            } else if (sort == 4) {
                sortquery = "`stock`.`quantity` ASC";
            } else if (sort == 5) {
                sortquery = "`stock`.`quantity` DESC";
            } else if (sort == 6) {
                sortquery = "`stock`.`exd` ASC";
            } else {
                sortquery = "`stock`.`exd` DESC";
            }

            ResultSet rs = MySQL.search("SELECT DISTINCT * FROM `stock` INNER JOIN `grn_item` ON `grn_item`.`stock_id`=`stock`.`id` INNER JOIN `product` ON `stock`.`product_id`=`product`.`id` INNER JOIN `brand` ON `product`.`brand_id`=`brand`.`id` INNER JOIN `category` ON `product`.`category_id`=`category`.`id`" + wherequery + " ORDER BY " + sortquery + "");
            DefaultTableModel dtm = (DefaultTableModel) jTable5.getModel();
            dtm.setRowCount(0);
            int rowCount = 1;
            while (rs.next()) {
                Vector v = new Vector();
                v.add(rowCount);
                rowCount += 1;
                v.add(rs.getString("stock.id"));
                v.add(rs.getString("product.id"));
                v.add(rs.getString("category.name"));
                v.add(rs.getString("brand.name"));
                v.add(rs.getString("product.title"));
                v.add(rs.getString("stock.quantity"));
                v.add(rs.getString("grn_item.buying_price"));
                v.add(rs.getString("stock.selling_price"));
                v.add(rs.getString("stock.mfd"));
                v.add(rs.getString("stock.exd"));
                dtm.addRow(v);
            }
            jTable5.setModel(dtm);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("ERROR:: Search Stock : " + e);
        }
    }

    public void tableFontStock() {
        Font f = new Font("Roboto", Font.PLAIN, 14);
        jTable5.getTableHeader().setFont(f);
    }

    public void loadStockTable() {
        try {
            LOG.info("INFO:: Load Stock Table");
            ResultSet rs = MySQL.search("SELECT DISTINCT `stock`.`id`,`product`.`id`,`category`.`name`,`brand`.`name`,`product`.`title`,`stock`.`quantity`,`grn_item`.`buying_price`,`stock`.`selling_price`,`stock`.`mfd`,`stock`.`exd` FROM `stock` INNER JOIN `grn_item` ON `grn_item`.`stock_id`=`stock`.`id` INNER JOIN `product` ON `stock`.`product_id`=`product`.`id` INNER JOIN `brand` ON `product`.`brand_id`=`brand`.`id` INNER JOIN `category` ON `product`.`category_id`=`category`.`id` WHERE `stock`.`quantity`>='0' ORDER BY `product`.`title` ASC");
            DefaultTableModel dtm = (DefaultTableModel) jTable5.getModel();
            dtm.setRowCount(0);
            int rowCount = 1;
            while (rs.next()) {
                Vector v1 = new Vector();
                v1.add(rowCount);
                rowCount += 1;
                v1.add(rs.getString("stock.id"));
                v1.add(rs.getString("product.id"));
                v1.add(rs.getString("category.name"));
                v1.add(rs.getString("brand.name"));
                v1.add(rs.getString("product.title"));
                v1.add(rs.getString("stock.quantity"));
                v1.add(rs.getString("grn_item.buying_price"));
                v1.add(rs.getString("stock.selling_price"));
                v1.add(rs.getString("stock.mfd"));
                v1.add(rs.getString("stock.exd"));
                dtm.addRow(v1);
            }
            jTable5.setModel(dtm);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("ERROR:: Load Stock Table " + e);
        }
    }

    public void loadBrandComboStock() {
        try {
            LOG.info("INFO:: Load Brand Combo Box Stock");
            ResultSet rs = MySQL.search("SELECT * FROM `brand`");
            Vector v3 = new Vector();
            v3.add("SELECT");
            while (rs.next()) {
                v3.add(rs.getString("name"));
            }
            DefaultComboBoxModel dcm = new DefaultComboBoxModel(v3);
            jComboBox8.setModel(dcm);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("ERROR:: Load Brand Combo Box Stock " + e);
        }
    }

    public void loadCategoryComboStock() {
        try {
            LOG.info("INFO:: Load Category Combo Box Stock");
            ResultSet rs = MySQL.search("SELECT * FROM `category`");
            Vector v3 = new Vector();
            v3.add("SELECT");
            while (rs.next()) {
                v3.add(rs.getString("name"));
            }
            DefaultComboBoxModel dcm = new DefaultComboBoxModel(v3);
            jComboBox7.setModel(dcm);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("ERROR:: Load Category Combo Box Stock " + e);
        }
    }

////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////STOCK//////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////GRN////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
    public void loadGRNPanel() {
        tableFontGRN();
        loadSupplierTableGRN();
        loadProductsTableGRN();
        loadBrandComboGRN();
        loadCategoryComboGRN();
        loadPaymentTypeComboGRN();
        jTextField22.setEditable(false);
    }

    public void resetFieldsGRN() {
        jTextField18.setText("");
        jTextField19.setText("");
        jTextField20.setText("");
        jTextField21.setText("");
        jComboBox2.setSelectedIndex(0);
        jComboBox3.setSelectedIndex(0);
        jDateChooser1.setDate(null);
        jDateChooser2.setDate(null);
        LOG.info("INFO:: Reset Fields GRN");
    }

    public void updateTotal() {
        double total = 0;

        for (int i = 0; i < jTable4.getRowCount(); i++) {
            String t = jTable4.getValueAt(i, 9).toString();
            total = total + Double.parseDouble(t);
        }
        jLabel63.setText(df.format(total));
    }

    public void searchProductsTableGRN() {
        try {
            LOG.info("INFO:: Search Products Table GRN");
            String name = jTextField18.getText();
            String category = jComboBox3.getSelectedItem().toString();
            String brand = jComboBox2.getSelectedItem().toString();

            String searchProductQuery = "SELECT `product`.`id`,`product`.`title`,`category`.`name` AS `category`,`brand`.`name` AS `brand` FROM `product` INNER JOIN `category` ON `product`.`category_id`=`category`.`id` INNER JOIN `brand` ON `product`.`brand_id` = `brand`.`id` WHERE `product`.`title` LIKE ? ";
            List<Object> params = new ArrayList<>();
            params.add("%" + name + "%");

            if (!category.equals("SELECT")) {
                searchProductQuery += " AND `category`.`name`=? ";
                params.add(category);
            }

            if (!brand.equals("SELECT")) {
                searchProductQuery += " AND `brand`.`name`=?";
                params.add(brand);
            }

            searchProductQuery += " ORDER BY `product`.`id` ASC ";

            ResultSet rs = MySQL.search(searchProductQuery, params.toArray());
            DefaultTableModel dtm = (DefaultTableModel) jTable3.getModel();
            dtm.setRowCount(0);
            while (rs.next()) {
                Vector v2 = new Vector();
                v2.add(rs.getString("id"));
                v2.add(rs.getString("title"));
                v2.add(rs.getString("brand"));
                v2.add(rs.getString("category"));
                dtm.addRow(v2);

            }
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("ERROR:: Search Products Table GRN " + e);

        }
    }

    public void loadPaymentTypeComboGRN() {
        try {
            LOG.info("INFO:: Load Payment Type ComboBox GRN");
            ResultSet rs = MySQL.search("SELECT * FROM `payment_type`");
            Vector v3 = new Vector();
            v3.add("SELECT");
            while (rs.next()) {
                v3.add(rs.getString("name"));
            }
            DefaultComboBoxModel dcm = new DefaultComboBoxModel(v3);
            jComboBox6.setModel(dcm);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("ERROR:: Load Payment Type ComboBox GRN " + e);
        }
    }

    public void loadBrandComboGRN() {
        try {
            LOG.info("INFO:: Load Brand ComboBox GRN");
            ResultSet rs = MySQL.search("SELECT * FROM `brand`");
            Vector v3 = new Vector();
            v3.add("SELECT");
            while (rs.next()) {
                v3.add(rs.getString("name"));
            }
            DefaultComboBoxModel dcm = new DefaultComboBoxModel(v3);
            jComboBox2.setModel(dcm);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("ERROR:: Load Brand ComboBox GRN " + e);
        }
    }

    public void loadCategoryComboGRN() {
        try {
            LOG.info("INFO:: Load Category ComboBox GRN");
            ResultSet rs = MySQL.search("SELECT * FROM `category`");
            Vector v3 = new Vector();
            v3.add("SELECT");
            while (rs.next()) {
                v3.add(rs.getString("name"));
            }
            DefaultComboBoxModel dcm = new DefaultComboBoxModel(v3);
            jComboBox3.setModel(dcm);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("ERROR:: Load Category ComboBox GRN " + e);
        }
    }

    public void loadProductsTableGRN() {
        try {
            LOG.info("INFO:: Load Products Table GRN");
            ResultSet rs = MySQL.search("SELECT `product`.`id`,`product`.`title`,`category`.`name` AS `category`,`brand`.`name` AS `brand` FROM `product` INNER JOIN `category` ON `product`.`category_id`=`category`.`id` INNER JOIN `brand` ON `product`.`brand_id` = `brand`.`id` ORDER BY `product`.`id` ASC");
            DefaultTableModel dtm = (DefaultTableModel) jTable3.getModel();
            dtm.setRowCount(0);
            while (rs.next()) {
                Vector v2 = new Vector();
                v2.add(rs.getString("id"));
                v2.add(rs.getString("title"));
                v2.add(rs.getString("brand"));
                v2.add(rs.getString("category"));
                dtm.addRow(v2);

            }
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("ERROR:: Load Products Table GRN " + e);
        }
    }

    public void loadSupplierTableGRN() {
        try {
            LOG.info("INFO:: Load Supplier Table GRN");
            ResultSet rs = MySQL.search("SELECT `supplier`.`id`,`supplier`.`name`,`supplier`.`mobile`, `company`.`name` AS `company` FROM `supplier` INNER JOIN `company_branch` ON `supplier`.`company_branch_id`=`company_branch`.`id` INNER JOIN `company` ON `company_branch`.`company_id`=`company`.`id`");
            DefaultTableModel dtm = (DefaultTableModel) jTable2.getModel();
            dtm.setRowCount(0);
            while (rs.next()) {
                Vector v2 = new Vector();
                v2.add(rs.getString("id"));
                v2.add(rs.getString("name"));
                v2.add(rs.getString("mobile"));
                v2.add(rs.getString("company"));
                dtm.addRow(v2);

            }
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("ERROR:: Load Supplier Table GRN " + e);
        }
    }

    public void loadSupplierTableGRN(String text) {
        try {
            LOG.info("INFO:: Search Supplier Table GRN");
            ResultSet rs = MySQL.search("SELECT `supplier`.`id`,`supplier`.`name`,`supplier`.`mobile`, `company`.`name` AS `company` FROM `supplier` INNER JOIN `company_branch` ON `supplier`.`company_branch_id`=`company_branch`.`id` INNER JOIN `company` ON `company_branch`.`company_id`=`company`.`id` WHERE `supplier`.`name` LIKE ? OR `company`.`name` LIKE ?", "%" + text + "%", "%" + text + "%");
            DefaultTableModel dtm = (DefaultTableModel) jTable2.getModel();
            dtm.setRowCount(0);
            while (rs.next()) {
                Vector v2 = new Vector();
                v2.add(rs.getString("id"));
                v2.add(rs.getString("name"));
                v2.add(rs.getString("mobile"));
                v2.add(rs.getString("company"));
                dtm.addRow(v2);

            }
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("ERROR:: Search Supplier Table GRN " + e);
        }
    }

    public void tableFontGRN() {
        Font f = new Font("Roboto", Font.PLAIN, 14);
        jTable2.getTableHeader().setFont(f);
        jTable3.getTableHeader().setFont(f);
        jTable4.getTableHeader().setFont(f);
    }

    public void calcBalanceInGRN() {
        LOG.info("INFO:: GRN Calculate Balance");
        String total = jLabel63.getText();
        String payment = jTextField22.getText();
        double balance = Double.parseDouble(payment) - Double.parseDouble(total);
        if (balance < 0) {
            jLabel67.setForeground(Color.RED);
        } else {
            jLabel67.setForeground(Color.GREEN);
        }
        jLabel67.setText(df.format(balance));
    }

////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////STOCK//////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
///////////////////////////////Profile//////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
    public void loadMyProfilePanel() {
        try {
            LOG.info("INFO:: My Profile load details");
            ResultSet rs = MySQL.search("SELECT `user`.`uniqid`,`user`.`fname`,`user`.`lname`,`user`.`email`,`user`.`mobile`,`user_type`.`name` AS `user_type`,`user`.`line1`,`user`.`line2`,`user`.`city`,`user`.`registered_date`,`user`.`updated_date` FROM `user` INNER JOIN `user_type` ON `user`.`user_type_id`=`user_type`.`id` WHERE `user`.`id`=?", logged_user_id);
            rs.next();
            jLabel34.setText(rs.getString("uniqid"));
            jLabel43.setText(rs.getString("registered_date"));
            jLabel44.setText(rs.getString("updated_date"));
            jTextField6.setText(rs.getString("fname"));
            jTextField12.setText(rs.getString("lname"));
            jTextField9.setText(rs.getString("email"));
            jTextField13.setText(rs.getString("mobile"));
            jTextField11.setText(rs.getString("user_type"));
            jTextField15.setText(rs.getString("line1"));
            jTextField17.setText(rs.getString("line2"));
            jTextField16.setText(rs.getString("city"));
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("ERROR:: My Profile load details " + e);
        }

    }

////////////////////////////////////////////////////////////////////////////////
///////////////////////////////Profile//////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
///////////////////////////////MANAGE USERS/////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
    public void loadUserManagePanel() {
        tableFontUM();
        loadUserTypesUM();
        loadUserTypesForSearchUM();
        loadUserStatusForSearchUM();
        loadUsersTableUM();
        jButton3.setEnabled(false);
        jButton5.setEnabled(true);
    }

    public void searchUserUM() {
        LOG.info("INFO:: User Manage search");
        String searchText = jTextField10.getText();
        String userType = jComboBox4.getSelectedItem().toString();
        String userStatus = jComboBox5.getSelectedItem().toString();

        String searchPattern = "%" + searchText + "%";

        String whereQuery = "WHERE (`user`.`uniqid` LIKE ? OR `user`.`fname` LIKE ? OR `user`.`lname` LIKE ? OR `user`.`mobile` LIKE ? OR `user`.`email` LIKE ?) ";
        List<Object> params = new ArrayList<>();
        params.add(searchPattern);
        params.add(searchPattern);
        params.add(searchPattern);
        params.add(searchPattern);
        params.add(searchPattern);

        if (!userType.equals("SELECT")) {
            whereQuery += " AND `user_type`.`name`=?";
            params.add(userType);
        }
        if (!userStatus.equals("SELECT")) {
            whereQuery += " AND `status`.`name`=?";
            params.add(userStatus);
        }

        try {
            ResultSet rs = MySQL.search("SELECT `user`.`id`,`user`.`uniqid`,`user`.`fname`,`user`.`lname`,`user`.`email`,`user`.`mobile`,`user_type`.`name` AS `user_type`,`user`.`line1`,`user`.`line2`,`user`.`city`,`status`.`name` AS `status`,`user`.`registered_date`,`user`.`updated_date` FROM `user` INNER JOIN `user_type` ON `user`.`user_type_id`=`user_type`.`id` INNER JOIN `status` ON `user`.`status_id` = `status`.`id` " + whereQuery + " ORDER BY `user`.`id` ASC", params.toArray());
            DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
            dtm.setRowCount(0);
            while (rs.next()) {
                Vector v2 = new Vector();
                v2.add(rs.getString("id"));
                v2.add(rs.getString("uniqid"));
                v2.add(rs.getString("fname") + " " + rs.getString("lname"));
                v2.add(rs.getString("email"));
                v2.add(rs.getString("mobile"));
                v2.add(rs.getString("user_type"));
                v2.add(rs.getString("line1") + ", " + rs.getString("line2") + ", " + rs.getString("city"));
                v2.add(rs.getString("status"));
                v2.add(rs.getString("registered_date"));
                v2.add(rs.getString("updated_date"));
                dtm.addRow(v2);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("ERROR:: User Manage search " + e);
        }

    }

    public void loadUsersTableUM() {
        try {
            LOG.info("INFO:: User Manage load Users Table");
            ResultSet rs = MySQL.search("SELECT `user`.`id`,`user`.`uniqid`,`user`.`fname`,`user`.`lname`,`user`.`email`,`user`.`mobile`,`user_type`.`name` AS `user_type`,`user`.`line1`,`user`.`line2`,`user`.`city`,`status`.`name` AS `status`,`user`.`registered_date`,`user`.`updated_date` FROM `user` INNER JOIN `user_type` ON `user`.`user_type_id`=`user_type`.`id` INNER JOIN `status` ON `user`.`status_id` = `status`.`id`  ORDER BY `user`.`id` ASC");
            DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
            dtm.setRowCount(0);
            while (rs.next()) {
                Vector v2 = new Vector();
                v2.add(rs.getString("id"));
                v2.add(rs.getString("uniqid"));
                v2.add(rs.getString("fname") + " " + rs.getString("lname"));
                v2.add(rs.getString("email"));
                v2.add(rs.getString("mobile"));
                v2.add(rs.getString("user_type"));
                v2.add(rs.getString("line1") + ", " + rs.getString("line2") + ", " + rs.getString("city"));
                v2.add(rs.getString("status"));
                v2.add(rs.getString("registered_date"));
                v2.add(rs.getString("updated_date"));
                dtm.addRow(v2);

            }
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("ERROR:: User Manage load Users Table " + e);
        }
    }

    public void clearTextUM() {
        jTextField1.setText("");
        jTextField2.setText("");
        jTextField3.setText("");
        jTextField4.setText("");
        jTextField8.setText("");
        jTextField5.setText("");
        jTextField7.setText("");
        jComboBox1.setSelectedIndex(0);

        jTable1.clearSelection();

        jButton3.setEnabled(false);
        jButton5.setEnabled(true);
        LOG.info("INFO:: User Manage clear");
    }

    public void tableFontUM() {
        Font f = new Font("Roboto", Font.PLAIN, 14);
        jTable1.getTableHeader().setFont(f);
    }

    public void loadUserTypesUM() {
        try {
            LOG.info("INFO:: User Manage load Users Types");
            ResultSet rs = MySQL.search("SELECT * FROM `user_type`");
            Vector v3 = new Vector();
            v3.add("SELECT");
            while (rs.next()) {
                v3.add(rs.getString("name"));
            }
            DefaultComboBoxModel dcm = new DefaultComboBoxModel(v3);
            jComboBox1.setModel(dcm);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("ERROR:: User Manage load Users Types " + e);
        }
    }

    public void loadUserTypesForSearchUM() {
        try {
            LOG.info("INFO:: User Manage load Users Types search Combobox");
            ResultSet rs = MySQL.search("SELECT * FROM `user_type`");
            Vector v3 = new Vector();
            v3.add("SELECT");
            while (rs.next()) {
                v3.add(rs.getString("name"));
            }
            DefaultComboBoxModel dcm = new DefaultComboBoxModel(v3);
            jComboBox4.setModel(dcm);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("ERROR:: User Manage load Users Types search Combobox " + e);
        }
    }

    public void loadUserStatusForSearchUM() {
        try {
            LOG.info("INFO:: User Manage load Users Status search Combobox");
            ResultSet rs = MySQL.search("SELECT * FROM `status`");
            Vector v3 = new Vector();
            v3.add("SELECT");
            while (rs.next()) {
                v3.add(rs.getString("name"));
            }
            DefaultComboBoxModel dcm = new DefaultComboBoxModel(v3);
            jComboBox5.setModel(dcm);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("ERROR:: User Manage load Users Status search Combobox " + e);
        }
    }

////////////////////////////////////////////////////////////////////////////////
///////////////////////////////MANAGE USERS/////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
///////////////////////////////INVOICE//////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
    public void loadInvoicePanel() {
        tableFontInvoice();
        loadCategoryComboInvoice();
        loadProductTableInvoice();
        loadPaymentTypeComboInvoice();
    }

    public void resetInvoice() {
        loadCategoryComboInvoice();
        loadProductTableInvoice();
        loadPaymentTypeComboInvoice();
        jTextField27.setText("");
        jLabel88.setText("00.00");
        jTextField28.setText("");
        jLabel92.setText("00.00");
        LOG.info("INFO:: Invoice reset panel");
    }

    public void calcBalanceInInvoice() {
        if (jTextField28.getText().isEmpty()) {
            jLabel92.setText("0.00");
            jLabel92.setForeground(Color.BLACK);
        } else {
            String total = jLabel88.getText();
            String payment = jTextField28.getText();
            double balance = Double.parseDouble(payment) - Double.parseDouble(total);
            if (balance < 0) {
                jLabel92.setForeground(Color.RED);
            } else {
                jLabel92.setForeground(Color.GREEN);
            }
            jLabel92.setText(df.format(balance));
        }
        LOG.info("INFO:: Invoice calculate balance");
    }

    public void loadPaymentTypeComboInvoice() {
        try {
            LOG.info("INFO:: Invoice load payment types");
            ResultSet rs = MySQL.search("SELECT * FROM `payment_type`");
            Vector v3 = new Vector();
//            v3.add("SELECT");
            while (rs.next()) {
                v3.add(rs.getString("name"));
            }
            DefaultComboBoxModel dcm = new DefaultComboBoxModel(v3);
            jComboBox11.setModel(dcm);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("ERROR:: Invoice load payment types " + e);
        }
    }

    public void tableFontInvoice() {
        Font f = new Font("Roboto", Font.PLAIN, 18);
        jTable6.getTableHeader().setFont(f);
        jTable7.getTableHeader().setFont(f);
    }

    public void loadCategoryComboInvoice() {
        try {
            LOG.info("INFO:: Invoice load category combo box");
            ResultSet rs = MySQL.search("SELECT * FROM `category`");
            Vector v3 = new Vector();
            v3.add("SELECT");
            while (rs.next()) {
                v3.add(rs.getString("name"));
            }
            DefaultComboBoxModel dcm = new DefaultComboBoxModel(v3);
            jComboBox10.setModel(dcm);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("ERROR:: Invoice load category combo box " + e);
        }
    }

    public void loadProductTableInvoice() {
        try {
            LOG.info("INFO:: Invoice load product table");
            ResultSet rs = MySQL.search("SELECT DISTINCT `stock`.`id`,`product`.`id`,`category`.`name`,`product`.`title`,`stock`.`quantity`,`stock`.`selling_price`,`stock`.`mfd`,`stock`.`exd` FROM `stock` INNER JOIN `product` ON `stock`.`product_id`=`product`.`id` INNER JOIN `category` ON `product`.`category_id`=`category`.`id` WHERE `stock`.`quantity`!='0' ORDER BY `product`.`title` ASC");
            DefaultTableModel dtm = (DefaultTableModel) jTable6.getModel();
            dtm.setRowCount(0);
            while (rs.next()) {
                Vector v1 = new Vector();
                v1.add(rs.getString("stock.id"));
                v1.add(rs.getString("product.title"));
                v1.add(rs.getString("category.name"));
                v1.add(rs.getString("stock.quantity"));
                v1.add(rs.getString("stock.selling_price"));
                v1.add(rs.getString("stock.mfd"));
                v1.add(rs.getString("stock.exd"));
                dtm.addRow(v1);
            }
            jTable6.setModel(dtm);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("ERROR:: Invoice load product table " + e);
        }
    }

    public void loadProductTableByCategoryInvoice() {
        try {
            String category = jComboBox10.getSelectedItem().toString();

            if (category.equals("SELECT")) {
                loadProductTableInvoice();
            } else {
                ResultSet rs = MySQL.search("SELECT DISTINCT `stock`.`id`,`product`.`id`,`category`.`name`,`product`.`title`,`stock`.`quantity`,`stock`.`selling_price`,`stock`.`mfd`,`stock`.`exd` FROM `stock` INNER JOIN `product` ON `stock`.`product_id`=`product`.`id` INNER JOIN `category` ON `product`.`category_id`=`category`.`id` WHERE `stock`.`quantity`!=? AND `category`.`name`=? ORDER BY `product`.`title` ASC", '0', category);
                DefaultTableModel dtm = (DefaultTableModel) jTable6.getModel();
                dtm.setRowCount(0);
                while (rs.next()) {
                    Vector v1 = new Vector();
                    v1.add(rs.getString("stock.id"));
                    v1.add(rs.getString("product.title"));
                    v1.add(rs.getString("category.name"));
                    v1.add(rs.getString("stock.quantity"));
                    v1.add(rs.getString("stock.selling_price"));
                    v1.add(rs.getString("stock.mfd"));
                    v1.add(rs.getString("stock.exd"));
                    dtm.addRow(v1);
                }
                jTable6.setModel(dtm);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateInvoiceTotal() {
        double total = 0;

        for (int i = 0; i < jTable7.getRowCount(); i++) {
            String t = jTable7.getValueAt(i, 4).toString();
            total = total + Double.parseDouble(t);
        }
        jLabel88.setText(df.format(total));
    }

////////////////////////////////////////////////////////////////////////////////
///////////////////////////////INVOICE//////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
///////////////////////////////CASH SUMMARY/////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
    public void loadCashSummaryPanel() {
        tableFontCashSummary();
        loadInviocesTableCashSummary();
    }

    public void tableFontCashSummary() {
        Font f = new Font("Roboto", Font.PLAIN, 14);
        jTable8.getTableHeader().setFont(f);
        jTable9.getTableHeader().setFont(f);
    }

    public void loadInviocesTableCashSummary() {
        try {
//            ResultSet rs = MySQL.search("SELECT `invoice`.`id` AS `invoice_id`, `invoice`.`date_time` AS `date_time`, `user`.`fname` AS `fname`, `user`.`lname` AS `lname`, `invoice_payment`.`payment` AS `payment`, `invoice_payment`.`balance` AS `balance`, SUM(`invoice_item`.`qty` * `stock`.`selling_price`) AS `total` FROM `invoice` JOIN `user` ON `invoice`.`user_id` = `user`.`id` JOIN `invoice_item` ON `invoice`.`id` = `invoice_item`.`invoice_id` JOIN `stock` ON `invoice_item`.`stock_id` = `stock`.`id` JOIN `invoice_payment` ON `invoice`.`id` = `invoice_payment`.`invoice_id` GROUP BY `invoice`.`id`, `invoice`.`date_time`, `user`.`fname`, `invoice_payment`.`payment`, `invoice_payment`.`balance` ORDER BY `invoice`.`date_time` DESC");
            ResultSet rs = MySQL.search("SELECT DISTINCT `invoice`.`id` AS `invoice_id`, `invoice`.`date_time` AS `date_time`, `user`.`fname` AS `fname`, `user`.`lname` AS `lname`, `invoice_payment`.`payment` AS `payment`, `invoice_payment`.`balance` AS `balance` FROM `invoice` INNER JOIN `user` ON `invoice`.`user_id` = `user`.`id` INNER JOIN `invoice_item` ON `invoice`.`id` = `invoice_item`.`invoice_id` INNER JOIN `invoice_payment` ON `invoice`.`id` = `invoice_payment`.`invoice_id` ORDER BY `invoice`.`date_time` DESC");
            DefaultTableModel dtm = (DefaultTableModel) jTable8.getModel();
            dtm.setRowCount(0);
            while (rs.next()) {
                Vector v1 = new Vector();
                v1.add(rs.getString("invoice_id"));
                v1.add(rs.getString("date_time"));
                v1.add(rs.getString("fname") + " " + rs.getString("lname"));
                v1.add(String.format("%.2f", rs.getDouble("payment")));
                v1.add(String.format("%.2f", rs.getDouble("balance")));
                v1.add(String.format("%.2f", rs.getDouble("payment") - rs.getDouble("balance")));
                dtm.addRow(v1);
            }
            jTable8.setModel(dtm);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadInviocesTableByDatesCashSummary() {
        try {
            String dateFrom = null;
            String dateTo = null;

            if (jDateChooser7.getDate() != null) {
                dateFrom = sdf.format(jDateChooser7.getDate());
            }
            if (jDateChooser8.getDate() != null) {
                dateTo = sdf.format(jDateChooser8.getDate());
            }

// Base query
            String query = "SELECT DISTINCT `invoice`.`id` AS `invoice_id`, `invoice`.`date_time` AS `date_time`, `user`.`fname` AS `fname`, `user`.`lname` AS `lname`, `invoice_payment`.`payment` AS `payment`, `invoice_payment`.`balance` AS `balance` FROM `invoice` INNER JOIN `user` ON `invoice`.`user_id` = `user`.`id` INNER JOIN `invoice_item` ON `invoice`.`id` = `invoice_item`.`invoice_id` INNER JOIN `invoice_payment` ON `invoice`.`id` = `invoice_payment`.`invoice_id`  ";

// Add conditions for date filters
            if (dateFrom != null && dateTo != null) {
                query += " WHERE `invoice`.`date_time` BETWEEN '" + dateFrom + " 00:00:00' AND '" + dateTo + " 23:59:59'";
            } else if (dateFrom != null) {
                query += " WHERE `invoice`.`date_time` >= '" + dateFrom + " 00:00:00'";
            } else if (dateTo != null) {
                query += " WHERE `invoice`.`date_time` <= '" + dateTo + " 23:59:59'";
            }

// Add grouping and ordering
            query += " ORDER BY `invoice`.`date_time` DESC";

// Execute query
            ResultSet rs = MySQL.search(query);
            DefaultTableModel dtm = (DefaultTableModel) jTable8.getModel();
            dtm.setRowCount(0);
            while (rs.next()) {
                Vector v1 = new Vector();
                v1.add(rs.getString("invoice_id"));
                v1.add(rs.getString("date_time"));
                v1.add(rs.getString("fname") + " " + rs.getString("lname"));
                v1.add(String.format("%.2f", rs.getDouble("payment")));
                v1.add(String.format("%.2f", rs.getDouble("balance")));
                v1.add(String.format("%.2f", rs.getDouble("payment") - rs.getDouble("balance")));
                dtm.addRow(v1);
            }
            jTable8.setModel(dtm);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

////////////////////////////////////////////////////////////////////////////////
///////////////////////////////CASH SUMMARY/////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
///////////////////////////////GRN HISTORY//////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
    public void loadGRNHistoryPanel() {
        tableFontGRNHistory();
        loadGRNsTableGRNHistory();
    }

    public void tableFontGRNHistory() {
        Font f = new Font("Roboto", Font.PLAIN, 14);
        jTable10.getTableHeader().setFont(f);
        jTable11.getTableHeader().setFont(f);
    }

    public void loadGRNsTableGRNHistory() {
        try {
            ResultSet rs = MySQL.search("SELECT `grn`.`id` AS `grn_id`, `grn`.`date_time` AS `date_time`, `user`.`fname` AS `fname`, `user`.`lname` AS `lname`, `grn_payment`.`payment` AS `payment`, `grn_payment`.`balance` AS `balance`, SUM(`grn_item`.`quantity` * `grn_item`.`buying_price`) AS `total` FROM `grn` JOIN `user` ON `grn`.`user_id` = `user`.`id` JOIN `grn_item` ON `grn`.`id` = `grn_item`.`grn_id` JOIN `stock` ON `grn_item`.`stock_id` = `stock`.`id` JOIN `grn_payment` ON `grn`.`id` = `grn_payment`.`grn_id` GROUP BY `grn`.`id`, `grn`.`date_time`, `user`.`fname`,`user`.`lname`, `grn_payment`.`payment`, `grn_payment`.`balance` ORDER BY `grn`.`date_time` DESC");
            DefaultTableModel dtm = (DefaultTableModel) jTable10.getModel();
            dtm.setRowCount(0);
            while (rs.next()) {
                Vector v1 = new Vector();
                v1.add(rs.getString("grn_id"));
                v1.add(rs.getString("date_time"));
                v1.add(rs.getString("fname") + " " + rs.getString("lname"));
                v1.add(String.format("%.2f", rs.getDouble("payment")));
                v1.add(String.format("%.2f", rs.getDouble("balance")));
                v1.add(String.format("%.2f", rs.getDouble("total")));
                dtm.addRow(v1);
            }
            jTable10.setModel(dtm);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadGRNsTableByDatesGRNHistory() {
        try {
            String dateFrom = null;
            String dateTo = null;

            if (jDateChooser9.getDate() != null) {
                dateFrom = sdf.format(jDateChooser9.getDate());
            }
            if (jDateChooser10.getDate() != null) {
                dateTo = sdf.format(jDateChooser10.getDate());
            }

            // Base query
            String query = "SELECT `grn`.`id` AS `grn_id`, `grn`.`date_time` AS `date_time`, "
                    + "`user`.`fname` AS `fname`, `user`.`lname` AS `lname`, "
                    + "`grn_payment`.`payment` AS `payment`, `grn_payment`.`balance` AS `balance`, "
                    + "SUM(`grn_item`.`quantity` * `grn_item`.`buying_price`) AS `total` "
                    + "FROM `grn` "
                    + "JOIN `user` ON `grn`.`user_id` = `user`.`id` "
                    + "JOIN `grn_item` ON `grn`.`id` = `grn_item`.`grn_id` "
                    + "JOIN `stock` ON `grn_item`.`stock_id` = `stock`.`id` "
                    + "JOIN `grn_payment` ON `grn`.`id` = `grn_payment`.`grn_id`";

            // Add date conditions
            if (dateFrom != null && dateTo != null) {
                query += " WHERE `grn`.`date_time` BETWEEN '" + dateFrom + " 00:00:00' AND '" + dateTo + " 23:59:59'";
            } else if (dateFrom != null) {
                query += " WHERE `grn`.`date_time` >= '" + dateFrom + " 00:00:00'";
            } else if (dateTo != null) {
                query += " WHERE `grn`.`date_time` <= '" + dateTo + " 23:59:59'";
            }

            // Add grouping and ordering
            query += " GROUP BY `grn`.`id`, `grn`.`date_time`, `user`.`fname`, `user`.`lname`, "
                    + "`grn_payment`.`payment`, `grn_payment`.`balance` "
                    + "ORDER BY `grn`.`date_time` DESC";

            // Execute query
            ResultSet rs = MySQL.search(query);
            DefaultTableModel dtm = (DefaultTableModel) jTable10.getModel();
            dtm.setRowCount(0);

            while (rs.next()) {
                Vector v1 = new Vector();
                v1.add(rs.getString("grn_id"));
                v1.add(rs.getString("date_time"));
                v1.add(rs.getString("fname") + " " + rs.getString("lname"));
                v1.add(String.format("%.2f", rs.getDouble("payment")));
                v1.add(String.format("%.2f", rs.getDouble("balance")));
                v1.add(String.format("%.2f", rs.getDouble("total")));
                dtm.addRow(v1);
            }

            jTable10.setModel(dtm);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

////////////////////////////////////////////////////////////////////////////////
///////////////////////////////GRN HISTORY//////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////SUMMARY////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
    public void loadSummaryPanel() {
        try {
            String dNow = sdf.format(new Date());
            ResultSet rs = MySQL.search("SELECT COUNT(`invoice`.`id`) AS `invoiceCount`, SUM(`invoice_payment`.`payment`) AS `payment`, SUM(`invoice_payment`.`balance`) AS balance FROM `invoice` INNER JOIN `invoice_payment` ON `invoice`.`id`=`invoice_payment`.`invoice_id` WHERE `invoice`.`date_time` LIKE ?", dNow + "%");
            if (rs.next()) {
                double paymentValue = rs.getDouble("payment");
                double balanceValue = rs.getDouble("balance");
                String invoiceCount = rs.getString("invoiceCount");

                double incomeValue = paymentValue - balanceValue;

                String formattedIncome = df.format(incomeValue);

                jLabel5.setText("Rs." + formattedIncome);
                jLabel7.setText(invoiceCount);
            }

            ResultSet total_rs = MySQL.search("SELECT COUNT(`invoice`.`id`) AS `invoiceCount`, SUM(`invoice_payment`.`payment`) AS `payment`, SUM(`invoice_payment`.`balance`) AS balance FROM `invoice` INNER JOIN `invoice_payment` ON `invoice`.`id`=`invoice_payment`.`invoice_id`");
            if (total_rs.next()) {
                double paymentValue = total_rs.getDouble("payment");
                double balanceValue = total_rs.getDouble("balance");
                double incomeValue = paymentValue - balanceValue;

                String formattedIncome = df.format(incomeValue);
                jLabel9.setText("Rs." + formattedIncome);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////SUMMARY////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel68 = new javax.swing.JLabel();
        jLabel69 = new javax.swing.JLabel();
        jLabel70 = new javax.swing.JLabel();
        jButton30 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        homejPanel18 = new javax.swing.JPanel();
        jPanel40 = new javax.swing.JPanel();
        jPanel39 = new javax.swing.JPanel();
        jButton18 = new javax.swing.JButton();
        jButton19 = new javax.swing.JButton();
        jButton20 = new javax.swing.JButton();
        jButton21 = new javax.swing.JButton();
        jButton22 = new javax.swing.JButton();
        jPanel11 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jButton23 = new javax.swing.JButton();
        jButton24 = new javax.swing.JButton();
        jButton29 = new javax.swing.JButton();
        userManagejPanel19 = new javax.swing.JPanel();
        jPanel18 = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel23 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel28 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jTextField8 = new javax.swing.JTextField();
        jButton12 = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jPanel19 = new javax.swing.JPanel();
        jLabel31 = new javax.swing.JLabel();
        jPanel20 = new javax.swing.JPanel();
        jLabel35 = new javax.swing.JLabel();
        jComboBox5 = new javax.swing.JComboBox<>();
        jPanel21 = new javax.swing.JPanel();
        jLabel36 = new javax.swing.JLabel();
        jTextField10 = new javax.swing.JTextField();
        jPanel22 = new javax.swing.JPanel();
        jLabel37 = new javax.swing.JLabel();
        jComboBox4 = new javax.swing.JComboBox<>();
        jButton6 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        grnjPanel23 = new javax.swing.JPanel();
        jPanel29 = new javax.swing.JPanel();
        jLabel49 = new javax.swing.JLabel();
        jTextField14 = new javax.swing.JTextField();
        jButton8 = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jPanel30 = new javax.swing.JPanel();
        jLabel54 = new javax.swing.JLabel();
        jTextField18 = new javax.swing.JTextField();
        jLabel55 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox<>();
        jLabel56 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox<>();
        jButton9 = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jPanel31 = new javax.swing.JPanel();
        jLabel57 = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jLabel58 = new javax.swing.JLabel();
        jDateChooser2 = new com.toedter.calendar.JDateChooser();
        jLabel59 = new javax.swing.JLabel();
        jTextField19 = new javax.swing.JTextField();
        jLabel60 = new javax.swing.JLabel();
        jTextField20 = new javax.swing.JTextField();
        jLabel61 = new javax.swing.JLabel();
        jTextField21 = new javax.swing.JTextField();
        jButton10 = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable();
        jPanel32 = new javax.swing.JPanel();
        jLabel62 = new javax.swing.JLabel();
        jLabel63 = new javax.swing.JLabel();
        jLabel64 = new javax.swing.JLabel();
        jComboBox6 = new javax.swing.JComboBox<>();
        jLabel65 = new javax.swing.JLabel();
        jTextField22 = new javax.swing.JTextField();
        jLabel66 = new javax.swing.JLabel();
        jLabel67 = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jButton11 = new javax.swing.JButton();
        jLabel93 = new javax.swing.JLabel();
        myProfilejPanel23 = new javax.swing.JPanel();
        jPanel28 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel23 = new javax.swing.JPanel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jPanel24 = new javax.swing.JPanel();
        jLabel38 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jLabel39 = new javax.swing.JLabel();
        jTextField9 = new javax.swing.JTextField();
        jLabel40 = new javax.swing.JLabel();
        jPasswordField1 = new javax.swing.JPasswordField();
        jLabel41 = new javax.swing.JLabel();
        jTextField11 = new javax.swing.JTextField();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        jPanel25 = new javax.swing.JPanel();
        jLabel46 = new javax.swing.JLabel();
        jTextField12 = new javax.swing.JTextField();
        jLabel47 = new javax.swing.JLabel();
        jTextField13 = new javax.swing.JTextField();
        jLabel48 = new javax.swing.JLabel();
        jPasswordField3 = new javax.swing.JPasswordField();
        jLabel50 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jPanel26 = new javax.swing.JPanel();
        jLabel51 = new javax.swing.JLabel();
        jTextField15 = new javax.swing.JTextField();
        jLabel52 = new javax.swing.JLabel();
        jTextField16 = new javax.swing.JTextField();
        jPanel27 = new javax.swing.JPanel();
        jLabel53 = new javax.swing.JLabel();
        jTextField17 = new javax.swing.JTextField();
        jButton7 = new javax.swing.JButton();
        stockjPanel33 = new javax.swing.JPanel();
        jPanel33 = new javax.swing.JPanel();
        jLabel71 = new javax.swing.JLabel();
        jComboBox7 = new javax.swing.JComboBox<>();
        jLabel72 = new javax.swing.JLabel();
        jComboBox8 = new javax.swing.JComboBox<>();
        jLabel73 = new javax.swing.JLabel();
        jTextField23 = new javax.swing.JTextField();
        jPanel35 = new javax.swing.JPanel();
        jLabel74 = new javax.swing.JLabel();
        jDateChooser3 = new com.toedter.calendar.JDateChooser();
        jLabel75 = new javax.swing.JLabel();
        jDateChooser4 = new com.toedter.calendar.JDateChooser();
        jLabel76 = new javax.swing.JLabel();
        jDateChooser5 = new com.toedter.calendar.JDateChooser();
        jLabel77 = new javax.swing.JLabel();
        jDateChooser6 = new com.toedter.calendar.JDateChooser();
        jLabel78 = new javax.swing.JLabel();
        jLabel79 = new javax.swing.JLabel();
        jTextField24 = new javax.swing.JTextField();
        jLabel80 = new javax.swing.JLabel();
        jTextField25 = new javax.swing.JTextField();
        jLabel81 = new javax.swing.JLabel();
        jComboBox9 = new javax.swing.JComboBox<>();
        jButton13 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jPanel34 = new javax.swing.JPanel();
        jLabel82 = new javax.swing.JLabel();
        jLabel83 = new javax.swing.JLabel();
        jLabel84 = new javax.swing.JLabel();
        jTextField26 = new javax.swing.JTextField();
        jButton15 = new javax.swing.JButton();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTable5 = new javax.swing.JTable();
        invoicejPanel36 = new javax.swing.JPanel();
        jPanel36 = new javax.swing.JPanel();
        jLabel85 = new javax.swing.JLabel();
        jComboBox10 = new javax.swing.JComboBox<>();
        jLabel86 = new javax.swing.JLabel();
        jTextField27 = new javax.swing.JTextField();
        jButton16 = new javax.swing.JButton();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTable6 = new javax.swing.JTable();
        jPanel37 = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        jTable7 = new javax.swing.JTable();
        jPanel38 = new javax.swing.JPanel();
        jLabel87 = new javax.swing.JLabel();
        jLabel88 = new javax.swing.JLabel();
        jLabel89 = new javax.swing.JLabel();
        jComboBox11 = new javax.swing.JComboBox<>();
        jLabel90 = new javax.swing.JLabel();
        jTextField28 = new javax.swing.JTextField();
        jCheckBox2 = new javax.swing.JCheckBox();
        jButton17 = new javax.swing.JButton();
        jLabel91 = new javax.swing.JLabel();
        jLabel92 = new javax.swing.JLabel();
        cashSummaryjPanel13 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jDateChooser7 = new com.toedter.calendar.JDateChooser();
        jLabel4 = new javax.swing.JLabel();
        jDateChooser8 = new com.toedter.calendar.JDateChooser();
        jButton25 = new javax.swing.JButton();
        jButton26 = new javax.swing.JButton();
        jScrollPane9 = new javax.swing.JScrollPane();
        jTable8 = new javax.swing.JTable();
        jScrollPane10 = new javax.swing.JScrollPane();
        jTable9 = new javax.swing.JTable();
        grnHistoryjPanel14 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jDateChooser9 = new com.toedter.calendar.JDateChooser();
        jLabel13 = new javax.swing.JLabel();
        jDateChooser10 = new com.toedter.calendar.JDateChooser();
        jButton27 = new javax.swing.JButton();
        jButton28 = new javax.swing.JButton();
        jScrollPane11 = new javax.swing.JScrollPane();
        jTable10 = new javax.swing.JTable();
        jScrollPane12 = new javax.swing.JScrollPane();
        jTable11 = new javax.swing.JTable();
        summaryjPanel4 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jButton31 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel2.setBackground(new java.awt.Color(40, 167, 69));

        jButton1.setBackground(new java.awt.Color(220, 53, 69));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/close_icon.png"))); // NOI18N
        jButton1.setToolTipText("Close");
        jButton1.setContentAreaFilled(false);
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.setOpaque(true);
        jButton1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jButton1FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jButton1FocusLost(evt);
            }
        });
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(0, 123, 255));
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/minimize_icon.png"))); // NOI18N
        jButton2.setToolTipText("Minimize");
        jButton2.setContentAreaFilled(false);
        jButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton2.setOpaque(true);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Roboto", 3, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(248, 249, 250));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/home_icon.png"))); // NOI18N
        jLabel1.setText(" Home");
        jLabel1.setToolTipText("Go To Home");
        jLabel1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel1MouseClicked(evt);
            }
        });

        jLabel68.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel68.setForeground(new java.awt.Color(242, 242, 242));
        jLabel68.setText("WelCome ! ");

        jLabel69.setFont(new java.awt.Font("Roboto", 1, 18)); // NOI18N
        jLabel69.setForeground(new java.awt.Color(242, 242, 242));
        jLabel69.setText("none");

        jLabel70.setFont(new java.awt.Font("Roboto", 2, 14)); // NOI18N
        jLabel70.setForeground(new java.awt.Color(242, 242, 242));
        jLabel70.setText("none");

        jButton30.setBackground(new java.awt.Color(220, 53, 69));
        jButton30.setFont(new java.awt.Font("Roboto", 1, 18)); // NOI18N
        jButton30.setForeground(new java.awt.Color(242, 242, 242));
        jButton30.setText("Log Out");
        jButton30.setContentAreaFilled(false);
        jButton30.setOpaque(true);
        jButton30.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton30ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel68)
                .addGap(18, 18, 18)
                .addComponent(jLabel69)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel70)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton30)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel68)
                .addComponent(jLabel69)
                .addComponent(jLabel70)
                .addComponent(jButton30))
            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel3.setBackground(new java.awt.Color(52, 58, 64));
        jPanel3.setLayout(new java.awt.CardLayout());

        homejPanel18.setBackground(new java.awt.Color(52, 58, 64));

        jPanel40.setBackground(new java.awt.Color(52, 58, 64));

        jPanel39.setBackground(new java.awt.Color(52, 58, 64));

        jButton18.setBackground(new java.awt.Color(40, 167, 69));
        jButton18.setFont(new java.awt.Font("Roboto", 1, 24)); // NOI18N
        jButton18.setText("GRN");
        jButton18.setContentAreaFilled(false);
        jButton18.setOpaque(true);
        jButton18.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton18MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton18MouseExited(evt);
            }
        });
        jButton18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton18ActionPerformed(evt);
            }
        });

        jButton19.setBackground(new java.awt.Color(0, 123, 255));
        jButton19.setFont(new java.awt.Font("Roboto", 1, 24)); // NOI18N
        jButton19.setText("Manage Users");
        jButton19.setContentAreaFilled(false);
        jButton19.setOpaque(true);
        jButton19.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton19MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton19MouseExited(evt);
            }
        });
        jButton19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton19ActionPerformed(evt);
            }
        });

        jButton20.setBackground(new java.awt.Color(255, 193, 7));
        jButton20.setFont(new java.awt.Font("Roboto", 1, 24)); // NOI18N
        jButton20.setText("My Profile");
        jButton20.setContentAreaFilled(false);
        jButton20.setOpaque(true);
        jButton20.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton20MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton20MouseExited(evt);
            }
        });
        jButton20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton20ActionPerformed(evt);
            }
        });

        jButton21.setBackground(new java.awt.Color(23, 162, 184));
        jButton21.setFont(new java.awt.Font("Roboto", 1, 24)); // NOI18N
        jButton21.setText("Manage Stock");
        jButton21.setContentAreaFilled(false);
        jButton21.setOpaque(true);
        jButton21.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton21MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton21MouseExited(evt);
            }
        });
        jButton21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton21ActionPerformed(evt);
            }
        });

        jButton22.setBackground(new java.awt.Color(108, 117, 125));
        jButton22.setFont(new java.awt.Font("Roboto", 1, 24)); // NOI18N
        jButton22.setText("Invoice");
        jButton22.setContentAreaFilled(false);
        jButton22.setOpaque(true);
        jButton22.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton22MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton22MouseExited(evt);
            }
        });
        jButton22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton22ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel39Layout = new javax.swing.GroupLayout(jPanel39);
        jPanel39.setLayout(jPanel39Layout);
        jPanel39Layout.setHorizontalGroup(
            jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel39Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel39Layout.setVerticalGroup(
            jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel39Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jButton19, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton18, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton20)
                    .addComponent(jButton21)
                    .addComponent(jButton22))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel39Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jButton18, jButton19, jButton20, jButton21, jButton22});

        javax.swing.GroupLayout jPanel40Layout = new javax.swing.GroupLayout(jPanel40);
        jPanel40.setLayout(jPanel40Layout);
        jPanel40Layout.setHorizontalGroup(
            jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel39, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel40Layout.setVerticalGroup(
            jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel39, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel11.setBackground(new java.awt.Color(52, 58, 64));
        jPanel11.setLayout(new javax.swing.BoxLayout(jPanel11, javax.swing.BoxLayout.LINE_AXIS));

        jPanel12.setBackground(new java.awt.Color(52, 58, 64));

        jButton23.setBackground(new java.awt.Color(253, 126, 20));
        jButton23.setFont(new java.awt.Font("Roboto", 1, 24)); // NOI18N
        jButton23.setText("Invoice History");
        jButton23.setContentAreaFilled(false);
        jButton23.setOpaque(true);
        jButton23.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton23MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton23MouseExited(evt);
            }
        });
        jButton23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton23ActionPerformed(evt);
            }
        });

        jButton24.setBackground(new java.awt.Color(0, 86, 179));
        jButton24.setFont(new java.awt.Font("Roboto", 1, 24)); // NOI18N
        jButton24.setText("GRN History");
        jButton24.setContentAreaFilled(false);
        jButton24.setOpaque(true);
        jButton24.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton24MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton24MouseExited(evt);
            }
        });
        jButton24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton24ActionPerformed(evt);
            }
        });

        jButton29.setBackground(new java.awt.Color(0, 168, 107));
        jButton29.setFont(new java.awt.Font("Roboto", 1, 24)); // NOI18N
        jButton29.setText("Summary");
        jButton29.setContentAreaFilled(false);
        jButton29.setOpaque(true);
        jButton29.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton29ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton23, javax.swing.GroupLayout.DEFAULT_SIZE, 462, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton24, javax.swing.GroupLayout.DEFAULT_SIZE, 462, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton29, javax.swing.GroupLayout.DEFAULT_SIZE, 464, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jButton29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton24, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton23, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel11.add(jPanel12);

        javax.swing.GroupLayout homejPanel18Layout = new javax.swing.GroupLayout(homejPanel18);
        homejPanel18.setLayout(homejPanel18Layout);
        homejPanel18Layout.setHorizontalGroup(
            homejPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel40, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        homejPanel18Layout.setVerticalGroup(
            homejPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(homejPanel18Layout.createSequentialGroup()
                .addGap(80, 80, 80)
                .addComponent(jPanel40, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel3.add(homejPanel18, "card2");

        userManagejPanel19.setBackground(new java.awt.Color(52, 58, 64));

        jPanel18.setBackground(new java.awt.Color(52, 58, 64));
        jPanel18.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel22.setFont(new java.awt.Font("Roboto", 1, 18)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(242, 242, 242));
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel22.setText("Add New Employee");
        jPanel18.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 420, -1));
        jPanel18.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 410, 10));

        jLabel23.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(242, 242, 242));
        jLabel23.setText("First Name");
        jPanel18.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 200, -1));

        jTextField1.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jTextField1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField1MouseClicked(evt);
            }
        });
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });
        jPanel18.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 200, -1));

        jTextField2.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jTextField2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField2MouseClicked(evt);
            }
        });
        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });
        jPanel18.add(jTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 90, 200, -1));

        jLabel24.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(242, 242, 242));
        jLabel24.setText("Last Name");
        jPanel18.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 70, 200, -1));

        jTextField3.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jTextField3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField3MouseClicked(evt);
            }
        });
        jTextField3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField3KeyTyped(evt);
            }
        });
        jPanel18.add(jTextField3, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 140, 200, -1));

        jLabel25.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(242, 242, 242));
        jLabel25.setText("Mobile");
        jPanel18.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 120, 200, -1));

        jTextField4.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jTextField4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField4MouseClicked(evt);
            }
        });
        jPanel18.add(jTextField4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, 200, -1));

        jLabel26.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(242, 242, 242));
        jLabel26.setText("Email");
        jPanel18.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, 200, -1));

        jLabel27.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(242, 242, 242));
        jLabel27.setText("User Type");
        jPanel18.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 170, 200, -1));

        jComboBox1.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel18.add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 190, 200, -1));

        jLabel28.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(242, 242, 242));
        jLabel28.setText("City");
        jPanel18.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 300, 200, -1));

        jTextField5.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jTextField5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField5MouseClicked(evt);
            }
        });
        jPanel18.add(jTextField5, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 270, 200, -1));

        jLabel29.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(242, 242, 242));
        jLabel29.setText("Address Line 1");
        jPanel18.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 250, 200, -1));

        jLabel30.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(242, 242, 242));
        jLabel30.setText("Address Line 2");
        jPanel18.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 250, 200, -1));

        jTextField7.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jTextField7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField7MouseClicked(evt);
            }
        });
        jPanel18.add(jTextField7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 320, 200, -1));

        jButton3.setBackground(new java.awt.Color(0, 123, 255));
        jButton3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton3.setForeground(new java.awt.Color(242, 242, 242));
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/user_check_icon.png"))); // NOI18N
        jButton3.setText("Update");
        jButton3.setContentAreaFilled(false);
        jButton3.setOpaque(true);
        jButton3.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jButton3FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jButton3FocusLost(evt);
            }
        });
        jButton3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton3MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton3MouseExited(evt);
            }
        });
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel18.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 400, 200, 40));

        jButton4.setBackground(new java.awt.Color(108, 117, 125));
        jButton4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton4.setForeground(new java.awt.Color(242, 242, 242));
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/eraser_icon.png"))); // NOI18N
        jButton4.setText("Clear");
        jButton4.setContentAreaFilled(false);
        jButton4.setOpaque(true);
        jButton4.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jButton4FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jButton4FocusLost(evt);
            }
        });
        jButton4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton4MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton4MouseExited(evt);
            }
        });
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel18.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 400, 200, 40));

        jButton5.setBackground(new java.awt.Color(40, 167, 69));
        jButton5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jButton5.setForeground(new java.awt.Color(242, 242, 242));
        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/user_plus_icon.png"))); // NOI18N
        jButton5.setText("Save");
        jButton5.setContentAreaFilled(false);
        jButton5.setOpaque(true);
        jButton5.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jButton5FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jButton5FocusLost(evt);
            }
        });
        jButton5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton5MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton5MouseExited(evt);
            }
        });
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel18.add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 450, 410, 40));

        jTextField8.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jTextField8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField8MouseClicked(evt);
            }
        });
        jPanel18.add(jTextField8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 270, 200, -1));

        jButton12.setBackground(new java.awt.Color(253, 126, 20));
        jButton12.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jButton12.setText("Change Status");
        jButton12.setContentAreaFilled(false);
        jButton12.setOpaque(true);
        jButton12.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton12MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton12MouseExited(evt);
            }
        });
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });
        jPanel18.add(jButton12, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 360, 410, -1));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(242, 242, 242));
        jLabel10.setText("user new password is his/her mobile number.");
        jPanel18.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 520, -1, -1));

        jPanel19.setBackground(new java.awt.Color(52, 58, 64));

        jLabel31.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(242, 242, 242));
        jLabel31.setText("Search User ~");

        jPanel20.setBackground(new java.awt.Color(52, 58, 64));

        jLabel35.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(242, 242, 242));
        jLabel35.setText("user status");

        jComboBox5.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jComboBox5.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox5ItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addComponent(jLabel35)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addComponent(jComboBox5, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addComponent(jLabel35)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 6, Short.MAX_VALUE))
        );

        jPanel21.setBackground(new java.awt.Color(52, 58, 64));

        jLabel36.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(242, 242, 242));
        jLabel36.setText("user ID/name/mobile/email");

        jTextField10.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jTextField10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField10MouseClicked(evt);
            }
        });
        jTextField10.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField10KeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addComponent(jLabel36)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(jTextField10)
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addComponent(jLabel36)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 6, Short.MAX_VALUE))
        );

        jPanel22.setBackground(new java.awt.Color(52, 58, 64));

        jLabel37.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel37.setForeground(new java.awt.Color(242, 242, 242));
        jLabel37.setText("user type");

        jComboBox4.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jComboBox4.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox4ItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addComponent(jLabel37)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addComponent(jComboBox4, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addComponent(jLabel37)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 6, Short.MAX_VALUE))
        );

        jButton6.setBackground(new java.awt.Color(0, 123, 255));
        jButton6.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        jButton6.setForeground(new java.awt.Color(242, 242, 242));
        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/reload_icon.png"))); // NOI18N
        jButton6.setText("Reload");
        jButton6.setContentAreaFilled(false);
        jButton6.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton6.setOpaque(true);
        jButton6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton6MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton6MouseExited(evt);
            }
        });
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel19Layout.createSequentialGroup()
                        .addComponent(jLabel31)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton6))
                    .addGroup(jPanel19Layout.createSequentialGroup()
                        .addComponent(jPanel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(10, 10, 10)
                        .addComponent(jPanel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(10, 10, 10)
                        .addComponent(jPanel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel31)
                    .addComponent(jButton6))
                .addGap(1, 1, 1)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jTable1.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "#", "User ID", "Name", "Email", "Mobile", "User Type", "Address", "Status", "Registered Date", "Updated"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setRowHeight(30);
        jTable1.setRowMargin(5);
        jTable1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jTable1.getTableHeader().setReorderingAllowed(false);
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setMaxWidth(50);
        }

        javax.swing.GroupLayout userManagejPanel19Layout = new javax.swing.GroupLayout(userManagejPanel19);
        userManagejPanel19.setLayout(userManagejPanel19Layout);
        userManagejPanel19Layout.setHorizontalGroup(
            userManagejPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(userManagejPanel19Layout.createSequentialGroup()
                .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, 430, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(userManagejPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(userManagejPanel19Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 976, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        userManagejPanel19Layout.setVerticalGroup(
            userManagejPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(userManagejPanel19Layout.createSequentialGroup()
                .addGroup(userManagejPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, 717, Short.MAX_VALUE)
                    .addGroup(userManagejPanel19Layout.createSequentialGroup()
                        .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                .addGap(1, 1, 1))
        );

        jPanel3.add(userManagejPanel19, "card3");

        grnjPanel23.setBackground(new java.awt.Color(52, 58, 64));

        jPanel29.setBackground(new java.awt.Color(52, 58, 64));
        jPanel29.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Select Supplier", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 12), new java.awt.Color(242, 242, 242))); // NOI18N
        jPanel29.setForeground(new java.awt.Color(242, 242, 242));

        jLabel49.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel49.setForeground(new java.awt.Color(242, 242, 242));
        jLabel49.setText("Supllier (name/company) : ");

        jTextField14.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jTextField14.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField14MouseClicked(evt);
            }
        });
        jTextField14.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField14KeyReleased(evt);
            }
        });

        jButton8.setBackground(new java.awt.Color(253, 126, 20));
        jButton8.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jButton8.setText("New Supplier");
        jButton8.setContentAreaFilled(false);
        jButton8.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton8.setOpaque(true);
        jButton8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton8MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton8MouseExited(evt);
            }
        });
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jTable2.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "#", "Name", "Mobile", "Company"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable2.setRowHeight(25);
        jTable2.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jTable2.getTableHeader().setReorderingAllowed(false);
        jScrollPane3.setViewportView(jTable2);
        if (jTable2.getColumnModel().getColumnCount() > 0) {
            jTable2.getColumnModel().getColumn(0).setMaxWidth(50);
        }

        javax.swing.GroupLayout jPanel29Layout = new javax.swing.GroupLayout(jPanel29);
        jPanel29.setLayout(jPanel29Layout);
        jPanel29Layout.setHorizontalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 692, Short.MAX_VALUE)
                    .addGroup(jPanel29Layout.createSequentialGroup()
                        .addComponent(jLabel49)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel29Layout.setVerticalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel49)
                    .addComponent(jTextField14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel30.setBackground(new java.awt.Color(52, 58, 64));
        jPanel30.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Select Product", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 12), new java.awt.Color(242, 242, 242))); // NOI18N

        jLabel54.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel54.setForeground(new java.awt.Color(242, 242, 242));
        jLabel54.setText("Product (Title) : ");

        jTextField18.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jTextField18.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField18MouseClicked(evt);
            }
        });
        jTextField18.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField18KeyReleased(evt);
            }
        });

        jLabel55.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel55.setForeground(new java.awt.Color(242, 242, 242));
        jLabel55.setText("Brand : ");

        jComboBox2.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jComboBox2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox2ItemStateChanged(evt);
            }
        });

        jLabel56.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel56.setForeground(new java.awt.Color(242, 242, 242));
        jLabel56.setText("Category :");

        jComboBox3.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jComboBox3.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox3ItemStateChanged(evt);
            }
        });

        jButton9.setBackground(new java.awt.Color(253, 126, 20));
        jButton9.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jButton9.setText("New Product");
        jButton9.setContentAreaFilled(false);
        jButton9.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton9.setOpaque(true);
        jButton9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton9MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton9MouseExited(evt);
            }
        });
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jTable3.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "#", "Title", "Brand", "Category"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable3.setRowHeight(25);
        jTable3.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jTable3.getTableHeader().setReorderingAllowed(false);
        jScrollPane4.setViewportView(jTable3);
        if (jTable3.getColumnModel().getColumnCount() > 0) {
            jTable3.getColumnModel().getColumn(0).setMaxWidth(50);
        }

        javax.swing.GroupLayout jPanel30Layout = new javax.swing.GroupLayout(jPanel30);
        jPanel30.setLayout(jPanel30Layout);
        jPanel30Layout.setHorizontalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel30Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 676, Short.MAX_VALUE)
                    .addGroup(jPanel30Layout.createSequentialGroup()
                        .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel54)
                            .addComponent(jLabel56))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField18)
                            .addComponent(jComboBox3, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel30Layout.createSequentialGroup()
                                .addComponent(jLabel55)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboBox2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jButton9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel30Layout.setVerticalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel30Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel54)
                    .addComponent(jTextField18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel55)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel56)
                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 185, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel31.setBackground(new java.awt.Color(52, 58, 64));

        jLabel57.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel57.setForeground(new java.awt.Color(242, 242, 242));
        jLabel57.setText("MFD : ");

        jDateChooser1.setDateFormatString("d.M.y");
        jDateChooser1.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N

        jLabel58.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel58.setForeground(new java.awt.Color(242, 242, 242));
        jLabel58.setText("EXD : ");

        jDateChooser2.setDateFormatString("d.M.y");
        jDateChooser2.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N

        jLabel59.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel59.setForeground(new java.awt.Color(242, 242, 242));
        jLabel59.setText("Buying Price : RS.");

        jTextField19.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jTextField19.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField19MouseClicked(evt);
            }
        });
        jTextField19.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField19KeyTyped(evt);
            }
        });

        jLabel60.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel60.setForeground(new java.awt.Color(242, 242, 242));
        jLabel60.setText("Selling Price : Rs.");

        jTextField20.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jTextField20.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField20MouseClicked(evt);
            }
        });
        jTextField20.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField20KeyTyped(evt);
            }
        });

        jLabel61.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel61.setForeground(new java.awt.Color(242, 242, 242));
        jLabel61.setText("Quantity : ");

        jTextField21.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jTextField21.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField21MouseClicked(evt);
            }
        });
        jTextField21.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField21KeyTyped(evt);
            }
        });

        jButton10.setBackground(new java.awt.Color(253, 126, 20));
        jButton10.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jButton10.setText("Add To GRN");
        jButton10.setContentAreaFilled(false);
        jButton10.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton10.setOpaque(true);
        jButton10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton10MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton10MouseExited(evt);
            }
        });
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jTable4.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jTable4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID ", "Title", "Brand", "Category", "Quantity", "MFD", "EXD", "Selling Price", "Buying Price", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable4.setRowHeight(25);
        jTable4.getTableHeader().setReorderingAllowed(false);
        jTable4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable4MouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(jTable4);
        if (jTable4.getColumnModel().getColumnCount() > 0) {
            jTable4.getColumnModel().getColumn(0).setMaxWidth(50);
        }

        javax.swing.GroupLayout jPanel31Layout = new javax.swing.GroupLayout(jPanel31);
        jPanel31.setLayout(jPanel31Layout);
        jPanel31Layout.setHorizontalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel31Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 1140, Short.MAX_VALUE)
                    .addGroup(jPanel31Layout.createSequentialGroup()
                        .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel57)
                            .addComponent(jLabel58))
                        .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel31Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jDateChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE))
                            .addGroup(jPanel31Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jDateChooser2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel60, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel59))
                        .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel31Layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(jTextField20, javax.swing.GroupLayout.PREFERRED_SIZE, 443, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel31Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextField19, javax.swing.GroupLayout.PREFERRED_SIZE, 443, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel31Layout.createSequentialGroup()
                                .addComponent(jLabel61)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField21))
                            .addComponent(jButton10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel31Layout.setVerticalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel31Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel31Layout.createSequentialGroup()
                        .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel57)
                            .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel59)
                                .addComponent(jTextField19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel58)
                            .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel60)
                                .addComponent(jTextField20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel31Layout.createSequentialGroup()
                        .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel61)
                            .addComponent(jTextField21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton10)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 364, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel32.setBackground(new java.awt.Color(52, 58, 64));

        jLabel62.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel62.setForeground(new java.awt.Color(242, 242, 242));
        jLabel62.setText("Total Amount");

        jLabel63.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel63.setForeground(new java.awt.Color(242, 242, 242));
        jLabel63.setText("00.00");

        jLabel64.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel64.setForeground(new java.awt.Color(242, 242, 242));
        jLabel64.setText("Payment Method");

        jComboBox6.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jComboBox6.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox6ItemStateChanged(evt);
            }
        });

        jLabel65.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel65.setForeground(new java.awt.Color(242, 242, 242));
        jLabel65.setText("Payment");

        jTextField22.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jTextField22.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField22MouseClicked(evt);
            }
        });
        jTextField22.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField22KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField22KeyTyped(evt);
            }
        });

        jLabel66.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel66.setForeground(new java.awt.Color(242, 242, 242));
        jLabel66.setText("Balance");

        jLabel67.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel67.setForeground(new java.awt.Color(242, 242, 242));
        jLabel67.setText("Rs.00.00");

        jCheckBox1.setBackground(new java.awt.Color(52, 58, 64));
        jCheckBox1.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jCheckBox1.setForeground(new java.awt.Color(242, 242, 242));
        jCheckBox1.setSelected(true);
        jCheckBox1.setText("Print GRN Bill");

        jButton11.setBackground(new java.awt.Color(40, 167, 69));
        jButton11.setFont(new java.awt.Font("Roboto", 1, 18)); // NOI18N
        jButton11.setText("Done");
        jButton11.setContentAreaFilled(false);
        jButton11.setOpaque(true);
        jButton11.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton11MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton11MouseExited(evt);
            }
        });
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jLabel93.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel93.setForeground(new java.awt.Color(242, 242, 242));
        jLabel93.setText("Rs. ");

        javax.swing.GroupLayout jPanel32Layout = new javax.swing.GroupLayout(jPanel32);
        jPanel32.setLayout(jPanel32Layout);
        jPanel32Layout.setHorizontalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel32Layout.createSequentialGroup()
                .addGroup(jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel32Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel62, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextField22, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel65, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel64, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jComboBox6, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel66, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel67, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jCheckBox1, javax.swing.GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel32Layout.createSequentialGroup()
                                .addComponent(jLabel93)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel63, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addComponent(jButton11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel32Layout.setVerticalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel32Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel62)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel63)
                    .addComponent(jLabel93))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel64)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel65)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel66)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel67)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jCheckBox1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 143, Short.MAX_VALUE)
                .addComponent(jButton11)
                .addGap(16, 16, 16))
        );

        javax.swing.GroupLayout grnjPanel23Layout = new javax.swing.GroupLayout(grnjPanel23);
        grnjPanel23.setLayout(grnjPanel23Layout);
        grnjPanel23Layout.setHorizontalGroup(
            grnjPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(grnjPanel23Layout.createSequentialGroup()
                .addComponent(jPanel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(jPanel30, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(grnjPanel23Layout.createSequentialGroup()
                .addComponent(jPanel31, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(jPanel32, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        grnjPanel23Layout.setVerticalGroup(
            grnjPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(grnjPanel23Layout.createSequentialGroup()
                .addGroup(grnjPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel30, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(grnjPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel31, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel32, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        jPanel3.add(grnjPanel23, "card4");

        myProfilejPanel23.setBackground(new java.awt.Color(52, 58, 64));

        jPanel28.setBackground(new java.awt.Color(52, 58, 64));

        jScrollPane2.setBackground(new java.awt.Color(52, 58, 64));
        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        jPanel23.setBackground(new java.awt.Color(52, 58, 64));

        jLabel32.setFont(new java.awt.Font("Roboto", 1, 24)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(242, 242, 242));
        jLabel32.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel32.setText("My Profile");

        jLabel33.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(242, 242, 242));
        jLabel33.setText("User ID : ");

        jLabel34.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel34.setForeground(new java.awt.Color(242, 242, 242));
        jLabel34.setText("None");

        jPanel24.setBackground(new java.awt.Color(52, 58, 64));

        jLabel38.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel38.setForeground(new java.awt.Color(242, 242, 242));
        jLabel38.setText("First Name");

        jTextField6.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jTextField6.setForeground(new java.awt.Color(52, 58, 64));
        jTextField6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField6MouseClicked(evt);
            }
        });

        jLabel39.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel39.setForeground(new java.awt.Color(242, 242, 242));
        jLabel39.setText("Email");

        jTextField9.setEditable(false);
        jTextField9.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jTextField9.setForeground(new java.awt.Color(52, 58, 64));
        jTextField9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField9MouseClicked(evt);
            }
        });

        jLabel40.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel40.setForeground(new java.awt.Color(242, 242, 242));
        jLabel40.setText("Password");

        jPasswordField1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jPasswordField1.setForeground(new java.awt.Color(52, 58, 64));
        jPasswordField1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPasswordField1MouseClicked(evt);
            }
        });
        jPasswordField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jPasswordField1ActionPerformed(evt);
            }
        });

        jLabel41.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel41.setForeground(new java.awt.Color(242, 242, 242));
        jLabel41.setText("User Type");

        jTextField11.setEditable(false);
        jTextField11.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jTextField11.setForeground(new java.awt.Color(52, 58, 64));
        jTextField11.setEnabled(false);

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField6)
                    .addComponent(jLabel38, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel39, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPasswordField1)
                    .addComponent(jTextField9)
                    .addComponent(jLabel41, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel24Layout.createSequentialGroup()
                        .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jTextField11))
                .addContainerGap())
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel38)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel39)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel40)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPasswordField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel41)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        jLabel42.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel42.setForeground(new java.awt.Color(242, 242, 242));
        jLabel42.setText("Registered Date : ");

        jLabel43.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel43.setForeground(new java.awt.Color(242, 242, 242));
        jLabel43.setText("None");

        jLabel44.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel44.setForeground(new java.awt.Color(242, 242, 242));
        jLabel44.setText("None");

        jLabel45.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel45.setForeground(new java.awt.Color(242, 242, 242));
        jLabel45.setText("Updated Date : ");

        jPanel25.setBackground(new java.awt.Color(52, 58, 64));

        jLabel46.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel46.setForeground(new java.awt.Color(242, 242, 242));
        jLabel46.setText("Last Name");

        jTextField12.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jTextField12.setForeground(new java.awt.Color(52, 58, 64));
        jTextField12.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField12MouseClicked(evt);
            }
        });

        jLabel47.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel47.setForeground(new java.awt.Color(242, 242, 242));
        jLabel47.setText("Mobile");

        jTextField13.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jTextField13.setForeground(new java.awt.Color(52, 58, 64));
        jTextField13.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField13MouseClicked(evt);
            }
        });
        jTextField13.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField13KeyTyped(evt);
            }
        });

        jLabel48.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel48.setForeground(new java.awt.Color(242, 242, 242));
        jLabel48.setText("Confirm Password");

        jPasswordField3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jPasswordField3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPasswordField3MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField12)
                    .addComponent(jLabel46, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel47, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextField13)
                    .addGroup(jPanel25Layout.createSequentialGroup()
                        .addComponent(jLabel48, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPasswordField3))
                .addContainerGap())
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel46)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel47)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel48)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPasswordField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(73, Short.MAX_VALUE))
        );

        jLabel50.setFont(new java.awt.Font("Roboto", 3, 14)); // NOI18N
        jLabel50.setForeground(new java.awt.Color(242, 242, 242));
        jLabel50.setText("Address Details~");

        jPanel26.setBackground(new java.awt.Color(52, 58, 64));

        jLabel51.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel51.setForeground(new java.awt.Color(242, 242, 242));
        jLabel51.setText("Address Line 1");

        jTextField15.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jTextField15.setForeground(new java.awt.Color(52, 58, 64));
        jTextField15.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField15MouseClicked(evt);
            }
        });

        jLabel52.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel52.setForeground(new java.awt.Color(242, 242, 242));
        jLabel52.setText("City");

        jTextField16.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jTextField16.setForeground(new java.awt.Color(52, 58, 64));
        jTextField16.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField16MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField15)
                    .addComponent(jLabel51, javax.swing.GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE)
                    .addComponent(jLabel52, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextField16))
                .addContainerGap())
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel51)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel52)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(10, Short.MAX_VALUE))
        );

        jPanel27.setBackground(new java.awt.Color(52, 58, 64));

        jLabel53.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel53.setForeground(new java.awt.Color(242, 242, 242));
        jLabel53.setText("Address Line 2");

        jTextField17.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jTextField17.setForeground(new java.awt.Color(52, 58, 64));
        jTextField17.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField17MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel27Layout = new javax.swing.GroupLayout(jPanel27);
        jPanel27.setLayout(jPanel27Layout);
        jPanel27Layout.setHorizontalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField17)
                    .addComponent(jLabel53, javax.swing.GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel27Layout.setVerticalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel53)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(70, Short.MAX_VALUE))
        );

        jButton7.setBackground(new java.awt.Color(40, 167, 69));
        jButton7.setFont(new java.awt.Font("Roboto", 1, 18)); // NOI18N
        jButton7.setForeground(new java.awt.Color(242, 242, 242));
        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/user_check_icon.png"))); // NOI18N
        jButton7.setText("Update Profile");
        jButton7.setContentAreaFilled(false);
        jButton7.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton7.setOpaque(true);
        jButton7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton7MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton7MouseExited(evt);
            }
        });
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel23Layout.createSequentialGroup()
                        .addComponent(jPanel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(40, 40, 40)
                        .addComponent(jPanel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel23Layout.createSequentialGroup()
                        .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel45, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel42, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel23Layout.createSequentialGroup()
                                .addGap(7, 7, 7)
                                .addComponent(jLabel43, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel23Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel44, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                .addGap(26, 26, 26))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel23Layout.createSequentialGroup()
                .addComponent(jLabel32, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel23Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel33)
                        .addGap(16, 16, 16)
                        .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 480, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel23Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel50)
                        .addGap(3, 3, 3)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 430, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel23Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jPanel26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40)
                        .addComponent(jPanel27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel23Layout.createSequentialGroup()
                        .addGap(188, 188, 188)
                        .addComponent(jButton7)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel32)
                .addGap(18, 18, 18)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel33)
                    .addComponent(jLabel34))
                .addGap(3, 3, 3)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel43)
                    .addComponent(jLabel42))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel45)
                    .addComponent(jLabel44))
                .addGap(7, 7, 7)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel50)
                    .addGroup(jPanel23Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(3, 3, 3)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton7)
                .addContainerGap())
        );

        jScrollPane2.setViewportView(jPanel23);

        javax.swing.GroupLayout jPanel28Layout = new javax.swing.GroupLayout(jPanel28);
        jPanel28.setLayout(jPanel28Layout);
        jPanel28Layout.setHorizontalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 566, Short.MAX_VALUE)
            .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 566, Short.MAX_VALUE))
        );
        jPanel28Layout.setVerticalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 611, Short.MAX_VALUE)
            .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel28Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 611, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        javax.swing.GroupLayout myProfilejPanel23Layout = new javax.swing.GroupLayout(myProfilejPanel23);
        myProfilejPanel23.setLayout(myProfilejPanel23Layout);
        myProfilejPanel23Layout.setHorizontalGroup(
            myProfilejPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, myProfilejPanel23Layout.createSequentialGroup()
                .addContainerGap(419, Short.MAX_VALUE)
                .addComponent(jPanel28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(427, Short.MAX_VALUE))
        );
        myProfilejPanel23Layout.setVerticalGroup(
            myProfilejPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(myProfilejPanel23Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel3.add(myProfilejPanel23, "card5");

        jPanel33.setBackground(new java.awt.Color(52, 58, 64));

        jLabel71.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel71.setForeground(new java.awt.Color(242, 242, 242));
        jLabel71.setText("Category :");

        jComboBox7.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jComboBox7.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox7ItemStateChanged(evt);
            }
        });

        jLabel72.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel72.setForeground(new java.awt.Color(242, 242, 242));
        jLabel72.setText("Brand :");

        jComboBox8.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jComboBox8.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox8ItemStateChanged(evt);
            }
        });

        jLabel73.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel73.setForeground(new java.awt.Color(242, 242, 242));
        jLabel73.setText("Name :");

        jTextField23.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jTextField23.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField23MouseClicked(evt);
            }
        });
        jTextField23.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField23KeyReleased(evt);
            }
        });

        jPanel35.setBackground(new java.awt.Color(52, 58, 64));

        jLabel74.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel74.setForeground(new java.awt.Color(242, 242, 242));
        jLabel74.setText("MFD :");

        jDateChooser3.setDateFormatString("d.M.y");
        jDateChooser3.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jDateChooser3.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDateChooser3PropertyChange(evt);
            }
        });

        jLabel75.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel75.setForeground(new java.awt.Color(242, 242, 242));
        jLabel75.setText("to");

        jDateChooser4.setDateFormatString("d.M.y");
        jDateChooser4.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jDateChooser4.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDateChooser4PropertyChange(evt);
            }
        });

        jLabel76.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel76.setForeground(new java.awt.Color(242, 242, 242));
        jLabel76.setText("EXD :");

        jDateChooser5.setDateFormatString("d.M.y");
        jDateChooser5.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jDateChooser5.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDateChooser5PropertyChange(evt);
            }
        });

        jLabel77.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel77.setForeground(new java.awt.Color(242, 242, 242));
        jLabel77.setText("to");

        jDateChooser6.setDateFormatString("d.M.y");
        jDateChooser6.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jDateChooser6.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDateChooser6PropertyChange(evt);
            }
        });

        jLabel78.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel78.setForeground(new java.awt.Color(242, 242, 242));
        jLabel78.setText("Selling Price :");

        jLabel79.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel79.setForeground(new java.awt.Color(242, 242, 242));
        jLabel79.setText("Min");

        jTextField24.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jTextField24.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField24MouseClicked(evt);
            }
        });
        jTextField24.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField24KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField24KeyTyped(evt);
            }
        });

        jLabel80.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel80.setForeground(new java.awt.Color(242, 242, 242));
        jLabel80.setText("Max");

        jTextField25.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jTextField25.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField25MouseClicked(evt);
            }
        });
        jTextField25.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField25KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField25KeyTyped(evt);
            }
        });

        jLabel81.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel81.setForeground(new java.awt.Color(242, 242, 242));
        jLabel81.setText("Sort By:");

        jComboBox9.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jComboBox9.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Name ASC", "Name DESC", "Price ASC", "Price DESC", "Quantity ASC", "Quantity DESC", "EXD ASC", "EXD DESC" }));
        jComboBox9.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox9ItemStateChanged(evt);
            }
        });

        jButton13.setBackground(new java.awt.Color(0, 123, 255));
        jButton13.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jButton13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/reload_icon.png"))); // NOI18N
        jButton13.setText("Reset");
        jButton13.setContentAreaFilled(false);
        jButton13.setOpaque(true);
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        jButton14.setBackground(new java.awt.Color(255, 193, 7));
        jButton14.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jButton14.setText("Print");
        jButton14.setContentAreaFilled(false);
        jButton14.setOpaque(true);
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel35Layout = new javax.swing.GroupLayout(jPanel35);
        jPanel35.setLayout(jPanel35Layout);
        jPanel35Layout.setHorizontalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel35Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel35Layout.createSequentialGroup()
                        .addComponent(jLabel74)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jDateChooser3, javax.swing.GroupLayout.DEFAULT_SIZE, 206, Short.MAX_VALUE))
                    .addGroup(jPanel35Layout.createSequentialGroup()
                        .addComponent(jLabel76)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jDateChooser5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel35Layout.createSequentialGroup()
                        .addComponent(jLabel75)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jDateChooser4, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE))
                    .addGroup(jPanel35Layout.createSequentialGroup()
                        .addComponent(jLabel77)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jDateChooser6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel78)
                    .addComponent(jLabel81))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel35Layout.createSequentialGroup()
                        .addComponent(jLabel79, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField24, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel80, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField25, javax.swing.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE))
                    .addComponent(jComboBox9, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton13, javax.swing.GroupLayout.DEFAULT_SIZE, 228, Short.MAX_VALUE)
                    .addComponent(jButton14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel35Layout.setVerticalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel35Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jDateChooser4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel75)
                    .addComponent(jDateChooser3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel74)
                    .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel78)
                        .addComponent(jLabel79)
                        .addComponent(jTextField24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel80)
                        .addComponent(jTextField25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton13)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel76)
                        .addComponent(jDateChooser5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel35Layout.createSequentialGroup()
                            .addGap(6, 6, 6)
                            .addComponent(jLabel77)))
                    .addComponent(jDateChooser6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel81)
                        .addComponent(jComboBox9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton14)))
                .addContainerGap(9, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel33Layout = new javax.swing.GroupLayout(jPanel33);
        jPanel33.setLayout(jPanel33Layout);
        jPanel33Layout.setHorizontalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel33Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel35, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel33Layout.createSequentialGroup()
                        .addComponent(jLabel71)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox7, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel72)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox8, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel73)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField23)))
                .addContainerGap())
        );
        jPanel33Layout.setVerticalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel33Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel71)
                    .addComponent(jComboBox7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel72)
                    .addComponent(jComboBox8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel73)
                    .addComponent(jTextField23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel35, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel34.setBackground(new java.awt.Color(52, 58, 64));

        jLabel82.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel82.setForeground(new java.awt.Color(242, 242, 242));
        jLabel82.setText("Selling Price : Rs.");

        jLabel83.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel83.setForeground(new java.awt.Color(242, 242, 242));
        jLabel83.setText("0.00");

        jLabel84.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel84.setForeground(new java.awt.Color(242, 242, 242));
        jLabel84.setText("New Price : Rs.");

        jTextField26.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jTextField26.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField26MouseClicked(evt);
            }
        });
        jTextField26.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField26KeyTyped(evt);
            }
        });

        jButton15.setBackground(new java.awt.Color(253, 126, 20));
        jButton15.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jButton15.setText("Update Price");
        jButton15.setContentAreaFilled(false);
        jButton15.setOpaque(true);
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel34Layout = new javax.swing.GroupLayout(jPanel34);
        jPanel34.setLayout(jPanel34Layout);
        jPanel34Layout.setHorizontalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel34Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel34Layout.createSequentialGroup()
                        .addComponent(jLabel82)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel83, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel34Layout.createSequentialGroup()
                        .addComponent(jLabel84)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField26, javax.swing.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel34Layout.setVerticalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel34Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel82)
                    .addComponent(jLabel83))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel84)
                    .addComponent(jTextField26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton15)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jScrollPane6.setBackground(new java.awt.Color(52, 58, 64));

        jTable5.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jTable5.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "#", "Stock ID", "Product ID", "Category", "Brand", "Name", "Quantity", "Buying Price", "Selling Price", "MFD", "EXD"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable5.setRowHeight(25);
        jTable5.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jTable5.getTableHeader().setReorderingAllowed(false);
        jTable5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable5MouseClicked(evt);
            }
        });
        jScrollPane6.setViewportView(jTable5);

        javax.swing.GroupLayout stockjPanel33Layout = new javax.swing.GroupLayout(stockjPanel33);
        stockjPanel33.setLayout(stockjPanel33Layout);
        stockjPanel33Layout.setHorizontalGroup(
            stockjPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(stockjPanel33Layout.createSequentialGroup()
                .addComponent(jPanel33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel34, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(stockjPanel33Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane6)
                .addContainerGap())
        );
        stockjPanel33Layout.setVerticalGroup(
            stockjPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(stockjPanel33Layout.createSequentialGroup()
                .addGroup(stockjPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel34, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 591, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel3.add(stockjPanel33, "card6");

        invoicejPanel36.setBackground(new java.awt.Color(52, 58, 64));

        jPanel36.setBackground(new java.awt.Color(52, 58, 64));
        jPanel36.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Select Product", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 12), new java.awt.Color(242, 242, 242))); // NOI18N

        jLabel85.setFont(new java.awt.Font("Roboto", 0, 24)); // NOI18N
        jLabel85.setForeground(new java.awt.Color(242, 242, 242));
        jLabel85.setText("Search category :");

        jComboBox10.setFont(new java.awt.Font("Roboto", 0, 24)); // NOI18N
        jComboBox10.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox10ItemStateChanged(evt);
            }
        });

        jLabel86.setFont(new java.awt.Font("Roboto", 0, 24)); // NOI18N
        jLabel86.setForeground(new java.awt.Color(242, 242, 242));
        jLabel86.setText("Quantity :");

        jTextField27.setFont(new java.awt.Font("Roboto", 0, 24)); // NOI18N
        jTextField27.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField27MouseClicked(evt);
            }
        });
        jTextField27.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField27KeyTyped(evt);
            }
        });

        jButton16.setBackground(new java.awt.Color(253, 126, 20));
        jButton16.setFont(new java.awt.Font("Roboto", 0, 24)); // NOI18N
        jButton16.setText("Add to Cart");
        jButton16.setContentAreaFilled(false);
        jButton16.setOpaque(true);
        jButton16.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton16MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton16MouseExited(evt);
            }
        });
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });

        jScrollPane7.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        jTable6.setFont(new java.awt.Font("Roboto", 0, 24)); // NOI18N
        jTable6.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Stock ID", "Product Name", "Category", "Quantity", "Selling Price (Rs)", "MFD", "EXD"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable6.setRowHeight(40);
        jTable6.getTableHeader().setReorderingAllowed(false);
        jScrollPane7.setViewportView(jTable6);

        javax.swing.GroupLayout jPanel36Layout = new javax.swing.GroupLayout(jPanel36);
        jPanel36.setLayout(jPanel36Layout);
        jPanel36Layout.setHorizontalGroup(
            jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel36Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane7)
                    .addGroup(jPanel36Layout.createSequentialGroup()
                        .addComponent(jLabel85)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jComboBox10, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel36Layout.createSequentialGroup()
                        .addComponent(jLabel86)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField27, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton16, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel36Layout.setVerticalGroup(
            jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel36Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel85)
                    .addComponent(jComboBox10, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel86)
                    .addComponent(jTextField27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton16))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane7)
                .addContainerGap())
        );

        jPanel37.setBackground(new java.awt.Color(52, 58, 64));
        jPanel37.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Cart", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 12), new java.awt.Color(242, 242, 242))); // NOI18N

        jScrollPane8.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        jTable7.setFont(new java.awt.Font("Roboto", 0, 24)); // NOI18N
        jTable7.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Stock ID", "Product Name", "Price", "Qty", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable7.setRowHeight(35);
        jTable7.getTableHeader().setReorderingAllowed(false);
        jTable7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable7MouseClicked(evt);
            }
        });
        jScrollPane8.setViewportView(jTable7);

        javax.swing.GroupLayout jPanel37Layout = new javax.swing.GroupLayout(jPanel37);
        jPanel37.setLayout(jPanel37Layout);
        jPanel37Layout.setHorizontalGroup(
            jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel37Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 758, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel37Layout.setVerticalGroup(
            jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel37Layout.createSequentialGroup()
                .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 324, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel38.setBackground(new java.awt.Color(52, 58, 64));

        jLabel87.setFont(new java.awt.Font("Roboto", 0, 24)); // NOI18N
        jLabel87.setForeground(new java.awt.Color(242, 242, 242));
        jLabel87.setText("Total Amount : Rs.");

        jLabel88.setFont(new java.awt.Font("Roboto", 0, 24)); // NOI18N
        jLabel88.setForeground(new java.awt.Color(242, 242, 242));
        jLabel88.setText("0.00");

        jLabel89.setFont(new java.awt.Font("Roboto", 0, 24)); // NOI18N
        jLabel89.setForeground(new java.awt.Color(242, 242, 242));
        jLabel89.setText("Payment Method");

        jComboBox11.setFont(new java.awt.Font("Roboto", 0, 24)); // NOI18N

        jLabel90.setFont(new java.awt.Font("Roboto", 0, 24)); // NOI18N
        jLabel90.setForeground(new java.awt.Color(242, 242, 242));
        jLabel90.setText("Payment :");

        jTextField28.setFont(new java.awt.Font("Roboto", 0, 24)); // NOI18N
        jTextField28.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField28MouseClicked(evt);
            }
        });
        jTextField28.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField28KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField28KeyTyped(evt);
            }
        });

        jCheckBox2.setBackground(new java.awt.Color(52, 58, 64));
        jCheckBox2.setFont(new java.awt.Font("Roboto", 0, 24)); // NOI18N
        jCheckBox2.setForeground(new java.awt.Color(242, 242, 242));
        jCheckBox2.setSelected(true);
        jCheckBox2.setText(" Print Invoice");

        jButton17.setBackground(new java.awt.Color(40, 167, 69));
        jButton17.setFont(new java.awt.Font("Roboto", 0, 36)); // NOI18N
        jButton17.setText("Print");
        jButton17.setContentAreaFilled(false);
        jButton17.setOpaque(true);
        jButton17.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton17MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton17MouseExited(evt);
            }
        });
        jButton17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton17ActionPerformed(evt);
            }
        });

        jLabel91.setFont(new java.awt.Font("Roboto", 0, 24)); // NOI18N
        jLabel91.setForeground(new java.awt.Color(242, 242, 242));
        jLabel91.setText("Balance : RS. ");

        jLabel92.setFont(new java.awt.Font("Roboto", 0, 24)); // NOI18N
        jLabel92.setForeground(new java.awt.Color(242, 242, 242));
        jLabel92.setText("00.00");

        javax.swing.GroupLayout jPanel38Layout = new javax.swing.GroupLayout(jPanel38);
        jPanel38.setLayout(jPanel38Layout);
        jPanel38Layout.setHorizontalGroup(
            jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel38Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel38Layout.createSequentialGroup()
                        .addComponent(jLabel90)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField28))
                    .addGroup(jPanel38Layout.createSequentialGroup()
                        .addComponent(jLabel89)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox11, 0, 576, Short.MAX_VALUE))
                    .addComponent(jButton17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel38Layout.createSequentialGroup()
                        .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel38Layout.createSequentialGroup()
                                .addComponent(jLabel87)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel88))
                            .addGroup(jPanel38Layout.createSequentialGroup()
                                .addComponent(jLabel91)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel92))
                            .addComponent(jCheckBox2))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel38Layout.setVerticalGroup(
            jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel38Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel87)
                    .addComponent(jLabel88))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel89)
                    .addComponent(jComboBox11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel90)
                    .addComponent(jTextField28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel91)
                    .addComponent(jLabel92))
                .addGap(18, 18, 18)
                .addComponent(jCheckBox2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton17)
                .addContainerGap(77, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout invoicejPanel36Layout = new javax.swing.GroupLayout(invoicejPanel36);
        invoicejPanel36.setLayout(invoicejPanel36Layout);
        invoicejPanel36Layout.setHorizontalGroup(
            invoicejPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(invoicejPanel36Layout.createSequentialGroup()
                .addComponent(jPanel36, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(invoicejPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel37, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel38, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        invoicejPanel36Layout.setVerticalGroup(
            invoicejPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(invoicejPanel36Layout.createSequentialGroup()
                .addGroup(invoicejPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(invoicejPanel36Layout.createSequentialGroup()
                        .addComponent(jPanel37, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel38, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel36, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel3.add(invoicejPanel36, "card7");

        cashSummaryjPanel13.setBackground(new java.awt.Color(52, 58, 64));

        jPanel13.setBackground(new java.awt.Color(52, 58, 64));
        jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Search ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Roboto", 0, 12), new java.awt.Color(242, 242, 242))); // NOI18N

        jLabel2.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(242, 242, 242));
        jLabel2.setText("Date From");

        jDateChooser7.setDateFormatString("d.M.y");
        jDateChooser7.setFont(new java.awt.Font("Roboto", 0, 24)); // NOI18N
        jDateChooser7.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDateChooser7PropertyChange(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(242, 242, 242));
        jLabel4.setText("Date To");

        jDateChooser8.setDateFormatString("d.M.y");
        jDateChooser8.setFont(new java.awt.Font("Roboto", 0, 24)); // NOI18N
        jDateChooser8.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDateChooser8PropertyChange(evt);
            }
        });

        jButton25.setBackground(new java.awt.Color(255, 193, 7));
        jButton25.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jButton25.setForeground(new java.awt.Color(242, 242, 242));
        jButton25.setText("Print Summary");
        jButton25.setContentAreaFilled(false);
        jButton25.setOpaque(true);
        jButton25.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton25MouseExited(evt);
            }
        });
        jButton25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton25ActionPerformed(evt);
            }
        });

        jButton26.setBackground(new java.awt.Color(0, 123, 255));
        jButton26.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jButton26.setForeground(new java.awt.Color(242, 242, 242));
        jButton26.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/reload_icon.png"))); // NOI18N
        jButton26.setText("Reset");
        jButton26.setContentAreaFilled(false);
        jButton26.setOpaque(true);
        jButton26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton26ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jDateChooser7, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jDateChooser8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton25, javax.swing.GroupLayout.PREFERRED_SIZE, 324, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton26, javax.swing.GroupLayout.DEFAULT_SIZE, 425, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel13Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jDateChooser7, jDateChooser8});

        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jButton25, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jDateChooser7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jDateChooser8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel13Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jDateChooser7, jDateChooser8});

        jTable8.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jTable8.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "Date Time", "User", "Payment", "Balance", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable8.setRowHeight(25);
        jTable8.getTableHeader().setReorderingAllowed(false);
        jTable8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable8MouseClicked(evt);
            }
        });
        jScrollPane9.setViewportView(jTable8);

        jScrollPane10.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N

        jTable9.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jTable9.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "Product", "Qty", "Unit Price", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable9.setRowHeight(25);
        jTable9.getTableHeader().setReorderingAllowed(false);
        jScrollPane10.setViewportView(jTable9);

        javax.swing.GroupLayout cashSummaryjPanel13Layout = new javax.swing.GroupLayout(cashSummaryjPanel13);
        cashSummaryjPanel13.setLayout(cashSummaryjPanel13Layout);
        cashSummaryjPanel13Layout.setHorizontalGroup(
            cashSummaryjPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cashSummaryjPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(cashSummaryjPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(cashSummaryjPanel13Layout.createSequentialGroup()
                        .addComponent(jScrollPane9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane10)))
                .addContainerGap())
        );
        cashSummaryjPanel13Layout.setVerticalGroup(
            cashSummaryjPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cashSummaryjPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(cashSummaryjPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 636, Short.MAX_VALUE)
                    .addComponent(jScrollPane10))
                .addContainerGap())
        );

        jPanel3.add(cashSummaryjPanel13, "card8");

        grnHistoryjPanel14.setBackground(new java.awt.Color(52, 58, 64));

        jPanel14.setBackground(new java.awt.Color(52, 58, 64));

        jLabel12.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(242, 242, 242));
        jLabel12.setText("Date From");

        jDateChooser9.setForeground(new java.awt.Color(242, 242, 242));
        jDateChooser9.setDateFormatString("d.M.y");
        jDateChooser9.setFont(new java.awt.Font("Roboto", 0, 24)); // NOI18N
        jDateChooser9.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDateChooser9PropertyChange(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(242, 242, 242));
        jLabel13.setText("Date To");

        jDateChooser10.setForeground(new java.awt.Color(242, 242, 242));
        jDateChooser10.setDateFormatString("d.M.y");
        jDateChooser10.setFont(new java.awt.Font("Roboto", 0, 24)); // NOI18N
        jDateChooser10.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDateChooser10PropertyChange(evt);
            }
        });

        jButton27.setBackground(new java.awt.Color(255, 193, 7));
        jButton27.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jButton27.setForeground(new java.awt.Color(242, 242, 242));
        jButton27.setText("Print Selected GRN Report");
        jButton27.setContentAreaFilled(false);
        jButton27.setOpaque(true);
        jButton27.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton27MouseExited(evt);
            }
        });
        jButton27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton27ActionPerformed(evt);
            }
        });

        jButton28.setBackground(new java.awt.Color(0, 123, 255));
        jButton28.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jButton28.setForeground(new java.awt.Color(242, 242, 242));
        jButton28.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/reload_icon.png"))); // NOI18N
        jButton28.setText("Reload");
        jButton28.setContentAreaFilled(false);
        jButton28.setOpaque(true);
        jButton28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton28ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jDateChooser9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jDateChooser10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jButton27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jDateChooser9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jDateChooser10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel14Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jDateChooser10, jDateChooser9});

        jTable10.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jTable10.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "Date Time", "User", "Payment", "Balance", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable10.setRowHeight(25);
        jTable10.getTableHeader().setReorderingAllowed(false);
        jTable10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable10MouseClicked(evt);
            }
        });
        jScrollPane11.setViewportView(jTable10);

        jTable11.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jTable11.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "Product", "Quantity", "Buying Price", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable11.setRowHeight(25);
        jTable11.getTableHeader().setReorderingAllowed(false);
        jScrollPane12.setViewportView(jTable11);

        javax.swing.GroupLayout grnHistoryjPanel14Layout = new javax.swing.GroupLayout(grnHistoryjPanel14);
        grnHistoryjPanel14.setLayout(grnHistoryjPanel14Layout);
        grnHistoryjPanel14Layout.setHorizontalGroup(
            grnHistoryjPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(grnHistoryjPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(grnHistoryjPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(grnHistoryjPanel14Layout.createSequentialGroup()
                        .addComponent(jScrollPane11, javax.swing.GroupLayout.DEFAULT_SIZE, 701, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane12, javax.swing.GroupLayout.DEFAULT_SIZE, 693, Short.MAX_VALUE)))
                .addContainerGap())
        );
        grnHistoryjPanel14Layout.setVerticalGroup(
            grnHistoryjPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(grnHistoryjPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(grnHistoryjPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane11, javax.swing.GroupLayout.DEFAULT_SIZE, 658, Short.MAX_VALUE)
                    .addComponent(jScrollPane12))
                .addContainerGap())
        );

        jPanel3.add(grnHistoryjPanel14, "card9");

        summaryjPanel4.setBackground(new java.awt.Color(52, 58, 64));

        jPanel4.setBackground(new java.awt.Color(52, 58, 64));
        jPanel4.setLayout(new javax.swing.BoxLayout(jPanel4, javax.swing.BoxLayout.LINE_AXIS));

        jPanel5.setBackground(new java.awt.Color(52, 58, 64));

        jPanel6.setBackground(new java.awt.Color(111, 66, 193));
        jPanel6.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        jLabel3.setFont(new java.awt.Font("Roboto", 1, 24)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Today Selling");

        jLabel5.setFont(new java.awt.Font("Roboto", 1, 36)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Rs.0.00");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap(61, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addComponent(jLabel5)
                .addGap(65, 65, 65))
        );

        jPanel7.setBackground(new java.awt.Color(0, 123, 255));
        jPanel7.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        jLabel6.setFont(new java.awt.Font("Roboto", 1, 24)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Today Invoices");

        jLabel7.setFont(new java.awt.Font("Roboto", 1, 36)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("0");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(61, 61, 61)
                .addComponent(jLabel6)
                .addGap(18, 18, 18)
                .addComponent(jLabel7)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel8.setBackground(new java.awt.Color(40, 167, 69));
        jPanel8.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        jLabel8.setFont(new java.awt.Font("Roboto", 1, 24)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Total Selling");

        jLabel9.setFont(new java.awt.Font("Roboto", 1, 36)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Rs.0.00");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 446, Short.MAX_VALUE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addComponent(jLabel8)
                .addGap(18, 18, 18)
                .addComponent(jLabel9)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel4.add(jPanel5);

        jPanel9.setBackground(new java.awt.Color(52, 58, 64));

        jPanel10.setBackground(new java.awt.Color(52, 58, 64));

        jButton31.setFont(new java.awt.Font("Roboto", 1, 18)); // NOI18N
        jButton31.setText("Database Backup");
        jButton31.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton31ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton31, javax.swing.GroupLayout.DEFAULT_SIZE, 288, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton31, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout summaryjPanel4Layout = new javax.swing.GroupLayout(summaryjPanel4);
        summaryjPanel4.setLayout(summaryjPanel4Layout);
        summaryjPanel4Layout.setHorizontalGroup(
            summaryjPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        summaryjPanel4Layout.setVerticalGroup(
            summaryjPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(summaryjPanel4Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(319, Short.MAX_VALUE))
        );

        jPanel3.add(summaryjPanel4, "card10");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jButton1FocusGained
        // TODO add your handling code here:
        jButton1.setBackground(new Color(200, 40, 50));
    }//GEN-LAST:event_jButton1FocusGained

    private void jButton1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jButton1FocusLost
        // TODO add your handling code here:
        jButton1.setBackground(new Color(220, 53, 69));
    }//GEN-LAST:event_jButton1FocusLost

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        this.setState(Home.ICONIFIED);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked
        // TODO add your handling code here:
        homejPanel18.setVisible(true);
        userManagejPanel19.setVisible(false);
        grnjPanel23.setVisible(false);
        myProfilejPanel23.setVisible(false);
        stockjPanel33.setVisible(false);
        invoicejPanel36.setVisible(false);
        cashSummaryjPanel13.setVisible(false);
        grnHistoryjPanel14.setVisible(false);
        summaryjPanel4.setVisible(false);

    }//GEN-LAST:event_jLabel1MouseClicked

    private void jButton3FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jButton3FocusGained
        // TODO add your handling code here:

    }//GEN-LAST:event_jButton3FocusGained

    private void jButton3FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jButton3FocusLost
        // TODO add your handling code here:

    }//GEN-LAST:event_jButton3FocusLost

    private void jButton4FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jButton4FocusGained
        // TODO add your handling code here:

    }//GEN-LAST:event_jButton4FocusGained

    private void jButton4FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jButton4FocusLost
        // TODO add your handling code here:

    }//GEN-LAST:event_jButton4FocusLost

    private void jButton5FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jButton5FocusGained
        // TODO add your handling code here:

    }//GEN-LAST:event_jButton5FocusGained

    private void jButton5FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jButton5FocusLost
        // TODO add your handling code here:

    }//GEN-LAST:event_jButton5FocusLost

    private void jButton3MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton3MouseEntered
        // TODO add your handling code here:
        jButton3.setBackground(new Color(0, 105, 220));
    }//GEN-LAST:event_jButton3MouseEntered

    private void jButton3MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton3MouseExited
        // TODO add your handling code here:
        jButton3.setBackground(new Color(0, 123, 255));
    }//GEN-LAST:event_jButton3MouseExited

    private void jButton4MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton4MouseEntered
        // TODO add your handling code here:
        jButton4.setBackground(new Color(88, 97, 105));
    }//GEN-LAST:event_jButton4MouseEntered

    private void jButton4MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton4MouseExited
        // TODO add your handling code here:
        jButton4.setBackground(new Color(108, 117, 125));
    }//GEN-LAST:event_jButton4MouseExited

    private void jButton5MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton5MouseEntered
        // TODO add your handling code here:
        jButton5.setBackground(new Color(25, 150, 50));
    }//GEN-LAST:event_jButton5MouseEntered

    private void jButton5MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton5MouseExited
        // TODO add your handling code here:
        jButton5.setBackground(new Color(40, 167, 69));
    }//GEN-LAST:event_jButton5MouseExited

    private void jButton6MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton6MouseEntered
        // TODO add your handling code here:
        jButton6.setBackground(new Color(0, 105, 220));
    }//GEN-LAST:event_jButton6MouseEntered

    private void jButton6MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton6MouseExited
        // TODO add your handling code here:
        jButton6.setBackground(new Color(0, 123, 255));
    }//GEN-LAST:event_jButton6MouseExited

    private void jTextField3KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField3KeyTyped
        // TODO add your handling code here:
        String mobile = jTextField3.getText();
        String text = mobile + evt.getKeyChar();

        if (text.length() == 1) {
            if (!text.equals("0")) {
                evt.consume();
            }
        } else if (text.length() == 2) {
            if (!text.equals("07")) {
                evt.consume();
            }
        } else if (text.length() == 3) {
            if (!Pattern.compile("07[01245678]").matcher(text).matches()) {
                evt.consume();
            }
        } else if (text.length() <= 10) {
            if (!Pattern.compile("07[1245678][0-9]+").matcher(text).matches()) {
                evt.consume();
            }
        } else {
            evt.consume();
        }

        if (!Pattern.compile("0?7?[01245678]?[0-9]{0,7}").matcher(text).matches()) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextField3KeyTyped

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        String fname = jTextField1.getText();
        String lname = jTextField2.getText();
        String email = jTextField4.getText();
        String mobile = jTextField3.getText();
        String utype = jComboBox1.getSelectedItem().toString();
        String line1 = jTextField8.getText();
        String line2 = jTextField5.getText();
        String city = jTextField7.getText();

        if (logged_user_type_id.equals("1") || logged_user_type_id.equals("2") || logged_user_type_id.equals("3")) {

            if (fname.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter fname", "Warning", JOptionPane.WARNING_MESSAGE);
            } else if (lname.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter lname", "Warning", JOptionPane.WARNING_MESSAGE);
            } else if (email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter email", "Warning", JOptionPane.WARNING_MESSAGE);
            } else if (!Pattern.compile("^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,6}$").matcher(email).matches()) {
                JOptionPane.showMessageDialog(this, "invalid email address", "Warning", JOptionPane.WARNING_MESSAGE);
            } else if (mobile.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter mobile", "Warning", JOptionPane.WARNING_MESSAGE);
            } else if (!Pattern.compile("07[01245678][0-9]{7}").matcher(mobile).matches()) {
                JOptionPane.showMessageDialog(this, "Invalid mobile number", "Warning", JOptionPane.WARNING_MESSAGE);
            } else if (utype.equals("SELECT")) {
                JOptionPane.showMessageDialog(this, "Please select user type", "Warning", JOptionPane.WARNING_MESSAGE);
            } else if (line1.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter address line 1", "Warning", JOptionPane.WARNING_MESSAGE);
            } else if (line2.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter address line 2", "Warning", JOptionPane.WARNING_MESSAGE);
            } else if (city.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter city", "Warning", JOptionPane.WARNING_MESSAGE);
            } else {
                try {
                    ResultSet users_rs = MySQL.search("SELECT * FROM `user` WHERE `mobile` = ? OR `email` = ?", mobile, email);
                    if (users_rs.next()) {
                        JOptionPane.showMessageDialog(this, "User email or mobile already exists. Please enter uniq email or mobile", "Warning", JOptionPane.WARNING_MESSAGE);
                    } else {
                        String securePassword = PasswordUtils.getSecurePassword(mobile);
                        String uniqueId = UserUtils.generateUniqueId();

                        ResultSet userType_rs = MySQL.search("SELECT * FROM `user_type` WHERE `name`=?", utype);
                        userType_rs.next();

                        String userTypeId = userType_rs.getString("id");

                        MySQL.iud("INSERT INTO `user` (`fname`,`lname`,`uniqid`,`mobile`,`email`,`password`,`line1`,`line2`,`city`,`user_type_id`) VALUES (?,?,?,?,?,?,?,?,?,?)",
                                fname, lname, uniqueId, mobile, email, securePassword, line1, line2, city, userTypeId);

                        JOptionPane.showMessageDialog(this, "Success", "Success", JOptionPane.INFORMATION_MESSAGE);

                        clearTextUM();
                        loadUsersTableUM();
                        LOG.info("INFO:: save new user iniq id = " + uniqueId + " , email = " + email);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    LOG.error("ERROR:: save new user reeor: " + e);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "You don't have permition to add new user", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        jTextField10.setText("");
        jComboBox4.setSelectedIndex(0);
        jComboBox5.setSelectedIndex(0);
        loadUsersTableUM();
        clearTextUM();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        clearTextUM();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jTextField10KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField10KeyReleased
        // TODO add your handling code here:
        searchUserUM();
    }//GEN-LAST:event_jTextField10KeyReleased

    private void jComboBox4ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox4ItemStateChanged
        // TODO add your handling code here:
        searchUserUM();
    }//GEN-LAST:event_jComboBox4ItemStateChanged

    private void jComboBox5ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox5ItemStateChanged
        // TODO add your handling code here:
        searchUserUM();
    }//GEN-LAST:event_jComboBox5ItemStateChanged

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        if (logged_user_type_id.equals("1") || logged_user_type_id.equals("2") || logged_user_type_id.equals("3")) {
            int r = jTable1.getSelectedRow();
            if (r == -1) {
                JOptionPane.showMessageDialog(this, "Please select a user", "Warning", JOptionPane.WARNING_MESSAGE);
            } else {
                String uid = jTable1.getValueAt(r, 0).toString();

                String fname = jTextField1.getText();
                String lname = jTextField2.getText();
                String email = jTextField4.getText();
                String mobile = jTextField3.getText();
                String utype = jComboBox1.getSelectedItem().toString();
                String line1 = jTextField8.getText();
                String line2 = jTextField5.getText();
                String city = jTextField7.getText();

                if (fname.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please enter fname", "Warning", JOptionPane.WARNING_MESSAGE);
                } else if (lname.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please enter lname", "Warning", JOptionPane.WARNING_MESSAGE);
                } else if (email.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please enter email", "Warning", JOptionPane.WARNING_MESSAGE);
                } else if (!Pattern.compile("^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,6}$").matcher(email).matches()) {
                    JOptionPane.showMessageDialog(this, "invalid email address", "Warning", JOptionPane.WARNING_MESSAGE);
                } else if (mobile.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please enter mobile", "Warning", JOptionPane.WARNING_MESSAGE);
                } else if (!Pattern.compile("07[01245678][0-9]{7}").matcher(mobile).matches()) {
                    JOptionPane.showMessageDialog(this, "Invalid mobile number", "Warning", JOptionPane.WARNING_MESSAGE);
                } else if (utype.equals("SELECT")) {
                    JOptionPane.showMessageDialog(this, "Please select user type", "Warning", JOptionPane.WARNING_MESSAGE);
                } else if (line1.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please enter address line 1", "Warning", JOptionPane.WARNING_MESSAGE);
                } else if (line2.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please enter address line 2", "Warning", JOptionPane.WARNING_MESSAGE);
                } else if (city.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please enter city", "Warning", JOptionPane.WARNING_MESSAGE);
                } else {
                    try {
                        ResultSet users_rs = MySQL.search("SELECT * FROM `user` WHERE `mobile` = ? OR `email` = ?", mobile, email);

                        int resultCount = 0;
                        while (users_rs.next()) {
                            resultCount++;
                        }
                        if (resultCount > 1) {
                            JOptionPane.showMessageDialog(this, "User email or mobile already exists. Please enter uniq email or mobile", "Warning", JOptionPane.WARNING_MESSAGE);
                        } else {

                            ResultSet userType_rs = MySQL.search("SELECT * FROM `user_type` WHERE `name`=?", utype);
                            userType_rs.next();
                            String userTypeId = userType_rs.getString("id");

                            MySQL.iud("UPDATE `user` SET `fname`=?,`lname`=?,`email`=?,`mobile`=?,`line1`=?,`line2`=?,`city`=?,`user_type_id`=? WHERE `id`=?", fname, lname, email, mobile, line1, line2, city, userTypeId, uid);
                            JOptionPane.showMessageDialog(this, "Update success", "Success", JOptionPane.INFORMATION_MESSAGE);

                            clearTextUM();
                            loadUsersTableUM();

                            LOG.info("INFO:: Update User Details uid= " + uid);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        LOG.error("ERROR:: User Update error= " + e);
                    }
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "You don't have permition to add new user", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:
        int r = jTable1.getSelectedRow();
        if (r == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            String uid = jTable1.getValueAt(r, 0).toString();
            try {
                ResultSet user_rs = MySQL.search("SELECT `user`.`fname`,`user`.`lname`,`user`.`email`,`user`.`mobile`,`user_type`.`name` AS `user_type`,`user`.`line1`,`user`.`line2`,`user`.`city` FROM `user` INNER JOIN `user_type` ON `user`.`user_type_id`=`user_type`.`id` WHERE `user`.`id`=?", uid);
                user_rs.next();

                String fname = user_rs.getString("fname");
                String lname = user_rs.getString("lname");
                String email = user_rs.getString("email");
                String mobile = user_rs.getString("mobile");
                String uType = user_rs.getString("user_type");
                String line1 = user_rs.getString("line1");
                String line2 = user_rs.getString("line2");
                String city = user_rs.getString("city");

                jTextField1.setText(fname);
                jTextField2.setText(lname);
                jTextField4.setText(email);
                jTextField3.setText(mobile);
                jComboBox1.setSelectedItem(uType);
                jTextField8.setText(line1);
                jTextField5.setText(line2);
                jTextField7.setText(city);

                jButton3.setEnabled(true);
                jButton5.setEnabled(false);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void jButton7MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton7MouseEntered
        // TODO add your handling code here:
        jButton7.setBackground(new Color(40, 167, 69));
    }//GEN-LAST:event_jButton7MouseEntered

    private void jButton7MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton7MouseExited
        // TODO add your handling code here:
        jButton7.setBackground(new Color(25, 150, 50));
    }//GEN-LAST:event_jButton7MouseExited

    private void jPasswordField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jPasswordField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jPasswordField1ActionPerformed

    private void jButton8MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton8MouseEntered
        // TODO add your handling code here:
        jButton8.setBackground(new Color(220, 100, 10));
    }//GEN-LAST:event_jButton8MouseEntered

    private void jButton8MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton8MouseExited
        // TODO add your handling code here:
        jButton8.setBackground(new Color(253, 126, 20));
    }//GEN-LAST:event_jButton8MouseExited

    private void jButton9MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton9MouseEntered
        // TODO add your handling code here:
        jButton9.setBackground(new Color(220, 100, 10));
    }//GEN-LAST:event_jButton9MouseEntered

    private void jButton9MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton9MouseExited
        // TODO add your handling code here:
        jButton9.setBackground(new Color(253, 126, 20));
    }//GEN-LAST:event_jButton9MouseExited

    private void jButton10MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton10MouseEntered
        // TODO add your handling code here:
        jButton10.setBackground(new Color(220, 100, 10));
    }//GEN-LAST:event_jButton10MouseEntered

    private void jButton10MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton10MouseExited
        // TODO add your handling code here:
        jButton10.setBackground(new Color(253, 126, 20));
    }//GEN-LAST:event_jButton10MouseExited

    private void jButton11MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton11MouseEntered
        // TODO add your handling code here:
        jButton11.setBackground(new Color(25, 150, 50));
    }//GEN-LAST:event_jButton11MouseEntered

    private void jButton11MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton11MouseExited
        // TODO add your handling code here:
        jButton11.setBackground(new Color(40, 167, 69));
    }//GEN-LAST:event_jButton11MouseExited

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
        AddNewSupplier addNewSupplier = new AddNewSupplier(this, true);
        addNewSupplier.setVisible(true);
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // TODO add your handling code here:
        AddNewProduct addNewProduct = new AddNewProduct(this, true);
        addNewProduct.setVisible(true);
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton12MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton12MouseEntered
        // TODO add your handling code here:
        jButton12.setBackground(new Color(220, 100, 10));
    }//GEN-LAST:event_jButton12MouseEntered

    private void jButton12MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton12MouseExited
        // TODO add your handling code here:
        jButton12.setBackground(new Color(253, 126, 20));
    }//GEN-LAST:event_jButton12MouseExited

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        // TODO add your handling code here:
        int r = jTable1.getSelectedRow();
        if (r == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            String userType = jTable1.getValueAt(r, 5).toString();
            String user_id = jTable1.getValueAt(r, 0).toString();
            if (userType.equalsIgnoreCase("Super Admin")) {
                JOptionPane.showMessageDialog(this, "Can't change super admin status", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                String currentStatus = jTable1.getValueAt(r, 7).toString();
                int status;
                if (currentStatus.equalsIgnoreCase("Active")) {
                    status = 2;
                } else {
                    status = 1;
                }
                if (logged_user_type_id.equals("3") && userType.equalsIgnoreCase("Admin")) {
                    JOptionPane.showMessageDialog(this, "Can't change admin status", "Error", JOptionPane.ERROR_MESSAGE);
                } else if (logged_user_type_id.equals("4") || logged_user_type_id.equals("5")) {
                    JOptionPane.showMessageDialog(this, "You don't have permition to change user's status", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    MySQL.iud("UPDATE `user` SET `status_id`=? WHERE `id`=?", status, user_id);
                    loadUsersTableUM();
                    LOG.info("INFO:: change User status, user_id = " + user_id);
                    JOptionPane.showMessageDialog(this, "User status updated", "Success", JOptionPane.INFORMATION_MESSAGE);
                }

            }
        }
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:

        String fname = jTextField6.getText();
        String lname = jTextField12.getText();
        String mobile = jTextField13.getText();
        char passwordArray[] = jPasswordField1.getPassword();
        String password = String.valueOf(passwordArray);
        char cpasswordArray[] = jPasswordField3.getPassword();
        String cpassword = String.valueOf(cpasswordArray);
        String line1 = jTextField15.getText();
        String line2 = jTextField17.getText();
        String city = jTextField16.getText();

        if (fname.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter first name", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (lname.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter last name", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (mobile.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter mobile number", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (!Pattern.compile("07[01245678][0-9]{7}").matcher(mobile).matches()) {
            JOptionPane.showMessageDialog(this, "Invalid mobile number", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (!password.equals(cpassword)) {
            JOptionPane.showMessageDialog(this, "Confirm password not matched!", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (line1.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter address line 1", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (line2.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter address line 2", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (city.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter city", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            try {
                ResultSet rs = MySQL.search("SELECT * FROM `user` WHERE `mobile` = ? AND `id`!=?", mobile, logged_user_id);
                if (rs.next()) {
                    JOptionPane.showMessageDialog(this, "mobile number already exists", "Warning", JOptionPane.WARNING_MESSAGE);
                } else {
                    if (password.isEmpty()) {
                        MySQL.iud("UPDATE `user` SET `fname` = ? , `lname` = ?, `mobile`=?, `line1`=?,`line2`=?,`city`=? WHERE `id`=?", fname, lname, mobile, line1, line2, city, logged_user_id);
                        loadMyProfilePanel();
                        JOptionPane.showMessageDialog(this, "Profile Updated", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        if (password.length() <= 6) {
                            JOptionPane.showMessageDialog(this, "Password should be greater than 6 charactors", "Warning", JOptionPane.WARNING_MESSAGE);
                        } else {
                            String securePassword = PasswordUtils.getSecurePassword(password);
                            MySQL.iud("UPDATE `user` SET `fname` = ? , `lname` = ?, `mobile`=?, `password`=?,`line1`=?,`line2`=?,`city`=? WHERE `id`=?", fname, lname, mobile, securePassword, line1, line2, city, logged_user_id);
                            loadMyProfilePanel();
                            LOG.info("INFO:: update my profile");
                            JOptionPane.showMessageDialog(this, "Profile Updated", "Success", JOptionPane.INFORMATION_MESSAGE);
                        }

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                LOG.error("ERROR:: update my profile error = " + e);
            }
        }


    }//GEN-LAST:event_jButton7ActionPerformed

    private void jTextField13KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField13KeyTyped
        // TODO add your handling code here:
        String mobile = jTextField13.getText();
        String text = mobile + evt.getKeyChar();

        if (text.length() == 1) {
            if (!text.equals("0")) {
                evt.consume();
            }
        } else if (text.length() == 2) {
            if (!text.equals("07")) {
                evt.consume();
            }
        } else if (text.length() == 3) {
            if (!Pattern.compile("07[01245678]").matcher(text).matches()) {
                evt.consume();
            }
        } else if (text.length() <= 10) {
            if (!Pattern.compile("07[1245678][0-9]+").matcher(text).matches()) {
                evt.consume();
            }
        } else {
            evt.consume();
        }

        if (!Pattern.compile("0?7?[01245678]?[0-9]{0,7}").matcher(text).matches()) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextField13KeyTyped

    private void jTextField14KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField14KeyReleased
        // TODO add your handling code here:
        String text = jTextField14.getText();
        loadSupplierTableGRN(text);
    }//GEN-LAST:event_jTextField14KeyReleased

    private void jTextField18KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField18KeyReleased
        // TODO add your handling code here:
        searchProductsTableGRN();
    }//GEN-LAST:event_jTextField18KeyReleased

    private void jComboBox3ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox3ItemStateChanged
        // TODO add your handling code here:
        searchProductsTableGRN();
    }//GEN-LAST:event_jComboBox3ItemStateChanged

    private void jComboBox2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox2ItemStateChanged
        // TODO add your handling code here:
        searchProductsTableGRN();
    }//GEN-LAST:event_jComboBox2ItemStateChanged

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        // TODO add your handling code here:
        int supplierRow = jTable2.getSelectedRow();
        int productRow = jTable3.getSelectedRow();
        Date mfd = jDateChooser1.getDate();
        Date exd = jDateChooser2.getDate();
        String buyingPrice = jTextField19.getText();
        String sellingPrice = jTextField20.getText();
        String qty = jTextField21.getText();

        if (supplierRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a supplier", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (productRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a product", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (mfd == null) {
            JOptionPane.showMessageDialog(this, "Please select MFD", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (mfd.after(new Date())) {
            JOptionPane.showMessageDialog(this, "Invalid MFD, The MFD should be a date before today.", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (exd == null) {
            JOptionPane.showMessageDialog(this, "Please select EXD", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (exd.before(new Date())) {
            JOptionPane.showMessageDialog(this, "Invalid EXD, The EXD should be one day after today.", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (!Pattern.compile("([1-9][0-9]*)|(([1-9][0-9]*)[.]([0]*[1-9][0-9]*))|([0][.]([0]*[1-9][0-9]*))").matcher(buyingPrice).matches()) {
            JOptionPane.showMessageDialog(this, "Invalid buying price", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (!Pattern.compile("([1-9][0-9]*)|(([1-9][0-9]*)[.]([0]*[1-9][0-9]*))|([0][.]([0]*[1-9][0-9]*))").matcher(sellingPrice).matches()) {
            JOptionPane.showMessageDialog(this, "Invalid selling price", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (Double.parseDouble(buyingPrice) >= Double.parseDouble(sellingPrice)) {
            JOptionPane.showMessageDialog(this, "Invalid buying and selling price", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (!Pattern.compile("[1-9][0-9]*").matcher(qty).matches()) {
            JOptionPane.showMessageDialog(this, "Please enter quantity", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            try {

                String sid = jTable2.getValueAt(supplierRow, 0).toString();
                String pid = jTable3.getValueAt(productRow, 0).toString();

                DefaultTableModel dtm = (DefaultTableModel) jTable4.getModel();
                boolean isFound = false;
                int x = -1;
                for (int i = 0; i < dtm.getRowCount(); i++) {
                    String id = jTable4.getValueAt(i, 0).toString();
                    if (id.equals(pid)) {
                        isFound = true;
                        x = i;
                        break;
                    }
                }
                if (isFound) {
                    int option = JOptionPane.showConfirmDialog(this, "This product is already added. Do you want to update?", "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
                    if (option == JOptionPane.YES_OPTION) {
                        int oldQty = Integer.parseInt(jTable4.getValueAt(x, 4).toString());
                        int finalQty = oldQty + Integer.parseInt(qty);
                        jTable4.setValueAt(String.valueOf(finalQty), x, 4);
                        jTable4.setValueAt(buyingPrice, x, 8);
                        double updatedItemtotal = finalQty * Double.parseDouble(buyingPrice);
                        jTable4.setValueAt(String.valueOf(updatedItemtotal), x, 9);
                        updateTotal();
                    }
                    resetFieldsGRN();
                } else {
                    Vector v = new Vector();

                    v.add(pid);
                    v.add(jTable3.getValueAt(jTable3.getSelectedRow(), 1));
                    v.add(jTable3.getValueAt(jTable3.getSelectedRow(), 2));
                    v.add(jTable3.getValueAt(jTable3.getSelectedRow(), 3));
                    v.add(qty);
                    v.add(sdf.format(mfd));
                    v.add(sdf.format(exd));
                    v.add(sellingPrice);
                    v.add(buyingPrice);

                    double itemtotal = Integer.parseInt(qty) * Double.parseDouble(buyingPrice);
                    v.add(df.format(itemtotal));
                    dtm.addRow(v);
                    updateTotal();
                    resetFieldsGRN();
                    JOptionPane.showMessageDialog(this, "Product add to GRN", "Success", JOptionPane.INFORMATION_MESSAGE);
                }
                jTable3.clearSelection();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jTextField19KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField19KeyTyped
        // TODO add your handling code here:
        String price = jTextField19.getText();
        String text = price + evt.getKeyChar();

        if (!Pattern.compile("(0|0[.]|0[.][1-9]*)|[1-9]|[1-9][0-9]*|[1-9][0-9]*[.]|[1-9][0-9]*[.][0-9]*").matcher(text).matches()) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextField19KeyTyped

    private void jTextField20KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField20KeyTyped
        // TODO add your handling code here:
        String price = jTextField20.getText();
        String text = price + evt.getKeyChar();

        if (!Pattern.compile("(0|0[.]|0[.][1-9]*)|[1-9]|[1-9][0-9]*|[1-9][0-9]*[.]|[1-9][0-9]*[.][0-9]*").matcher(text).matches()) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextField20KeyTyped

    private void jTextField21KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField21KeyTyped
        // TODO add your handling code here:
        String qty = jTextField21.getText();
        String text = qty + evt.getKeyChar();

        if (!Pattern.compile("[1-9][0-9]*").matcher(text).matches()) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextField21KeyTyped

    private void jTable4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable4MouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            int r = jTable4.getSelectedRow();
            if (r == -1) {
                JOptionPane.showMessageDialog(this, "Please select GRN item", "Warning", JOptionPane.WARNING_MESSAGE);
            } else {
                int option = JOptionPane.showConfirmDialog(this, "Do you want to remove product?", "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
                if (option == JOptionPane.YES_OPTION) {
                    DefaultTableModel dtm = (DefaultTableModel) jTable4.getModel();
                    dtm.removeRow(r);
                    updateTotal();

                    JOptionPane.showMessageDialog(this, "GRN item removed", "Success", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
    }//GEN-LAST:event_jTable4MouseClicked

    private void jComboBox6ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox6ItemStateChanged
        // TODO add your handling code here:
        String text = jComboBox6.getSelectedItem().toString();
        if (text.equals("SELECT")) {
            jTextField22.setEditable(false);
            jTextField22.setText("");
            jLabel67.setText("0.00");
            jLabel67.setForeground(Color.BLACK);
        } else {
            jTextField22.setEditable(true);
        }
    }//GEN-LAST:event_jComboBox6ItemStateChanged

    private void jTextField22KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField22KeyReleased
        // TODO add your handling code here:
        if (jTextField22.getText().isEmpty()) {
            jLabel67.setText("0.00");
            jLabel67.setForeground(Color.BLACK);
        } else {
            calcBalanceInGRN();

        }
    }//GEN-LAST:event_jTextField22KeyReleased

    private void jTextField22KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField22KeyTyped
        // TODO add your handling code here:
        String price = jTextField22.getText();
        String text = price + evt.getKeyChar();

        if (!Pattern.compile("(0|0[.]|0[.][1-9]*)|[1-9]|[1-9][0-9]*|[1-9][0-9]*[.]|[1-9][0-9]*[.][0-9]*").matcher(text).matches()) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextField22KeyTyped

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        // TODO add your handling code here:
        String paymentType = jComboBox6.getSelectedItem().toString();
        String payment = jTextField22.getText();

        if (jTable4.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Please add products", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (paymentType.equals("SELECT")) {
            JOptionPane.showMessageDialog(this, "Please payment method", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (!Pattern.compile("(0)|([1-9][0-9]*)|(([1-9][0-9]*)[.]([0]*[1-9][0-9]*))|([0][.]([0]*[1-9][0-9]*))").matcher(payment).matches()) {
            JOptionPane.showMessageDialog(this, "Invalid payment", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            // grn insert
            int supllierRow = jTable2.getSelectedRow();
            String sid = jTable2.getValueAt(supllierRow, 0).toString();
            long mTime = System.currentTimeMillis();
            String uniqueId = mTime + "-" + logged_user_id;
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dNow = sdf2.format(new Date());
            MySQL.iud("INSERT INTO `grn` (`supplier_id`,`date_time`,`user_id`,`unique_id`) VALUES (?,?,?,?)", sid, dNow, logged_user_id, uniqueId);
            // grn insert

            try {
                //grn payment insert
                ResultSet grn_rs = MySQL.search("SELECT * FROM `grn` WHERE `unique_id`=?", uniqueId);
                grn_rs.next();
                String id = grn_rs.getString("id");
                ResultSet payment_type_rs = MySQL.search("SELECT * FROM `payment_type` WHERE `name`=?", paymentType);
                payment_type_rs.next();
                String paymentTypeId = payment_type_rs.getString("id");
                String balance = jLabel67.getText();
                MySQL.iud("INSERT INTO `grn_payment` (`grn_id`,`payment_type_id`,`payment`,`balance`) VALUES (?,?,?,?)", id, paymentTypeId, payment, balance);
                //grn payment insert
                //GRN item insert &stock insert/update

                for (int i = 0; i < jTable4.getRowCount(); i++) {
                    String pid = jTable4.getValueAt(i, 0).toString();
                    String qty = jTable4.getValueAt(i, 4).toString();
                    String sellingPrice = jTable4.getValueAt(i, 7).toString();
                    String buyingPrice = jTable4.getValueAt(i, 8).toString();
                    String mfd = jTable4.getValueAt(i, 5).toString();
                    String exd = jTable4.getValueAt(i, 6).toString();

                    ResultSet stock_rs = MySQL.search("SELECT * FROM `stock` WHERE `product_id`=? AND `selling_price`=? AND `mfd`=? AND `exd`=?", pid, sellingPrice, mfd, exd);
                    {
                        String stock_id;
                        if (stock_rs.next()) {
                            // update
                            stock_id = stock_rs.getString("id");
                            String stock_qty = stock_rs.getString("quantity");
                            int updatedQty = Integer.parseInt(stock_qty) + Integer.parseInt(qty);
                            MySQL.iud("UPDATE `stock` SET `quantity` = ? WHERE `id`=?", updatedQty, stock_id);
                        } else {
                            //insert
                            MySQL.iud("INSERT INTO `stock` (`product_id`,`quantity`,`selling_price`,`mfd`,`exd`) VALUES (?,?,?,?,?)", pid, qty, sellingPrice, mfd, exd);
                            ResultSet rs4 = MySQL.search("SELECT * FROM `stock` WHERE `product_id`=? AND `selling_price`=? AND `mfd`=? AND `exd`=?", pid, sellingPrice, mfd, exd);
                            rs4.next();
                            stock_id = rs4.getString("id");
                        }
                        MySQL.iud("INSERT INTO `grn_item` (`quantity`,`buying_price`,`grn_id`,`stock_id`) VALUES (?,?,?,?)", qty, buyingPrice, id, stock_id);
                        LOG.info("INFO:: add new grn ");
                    }
                }

                if (jCheckBox1.isSelected()) {

//                    String path = "src//reports//canteen_grn.jasper";
                    InputStream path = getClass().getResourceAsStream("/reports/canteen_grn.jasper");

                    if (path == null) {
                        JOptionPane.showMessageDialog(this, "Report Not found", "Warning", JOptionPane.WARNING_MESSAGE);

                    } else {
                        String total = jLabel63.getText();

                        HashMap parameters = new HashMap();
                        parameters.put("Parameter1", jTable2.getValueAt(supllierRow, 1).toString());
                        parameters.put("Parameter2", jTable2.getValueAt(supllierRow, 3).toString());
                        parameters.put("Parameter3", jTable2.getValueAt(supllierRow, 2).toString());
                        parameters.put("Parameter4", uniqueId);
                        parameters.put("Parameter5", dNow);
                        parameters.put("Parameter6", total);
                        parameters.put("Parameter7", payment);
                        parameters.put("Parameter8", balance);

                        Vector v = new Vector();
                        for (int i = 0; i < jTable4.getRowCount(); i++) {
                            String pid = jTable4.getValueAt(i, 0).toString();
                            String pname = jTable4.getValueAt(i, 1).toString();
                            String brand = jTable4.getValueAt(i, 2).toString();
                            String qty = jTable4.getValueAt(i, 4).toString();
                            String buyingPrice = jTable4.getValueAt(i, 8).toString();
                            String mfd = jTable4.getValueAt(i, 5).toString();
                            String exd = jTable4.getValueAt(i, 6).toString();
                            String to = jTable4.getValueAt(i, 9).toString();

                            model.GRNBean g1 = new model.GRNBean(pid, pname, brand, qty, buyingPrice, mfd, exd, to);
                            v.add(g1);
                        }

                        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(v);

                        JasperPrint jp = JasperFillManager.fillReport(path, parameters, dataSource);
                        JasperViewer.viewReport(jp, false);

                        LOG.info("INFO:: print grn");
                    }

                }
                JOptionPane.showMessageDialog(this, "New GRN created", "Success", JOptionPane.INFORMATION_MESSAGE);

                resetFieldsGRN();
                loadSupplierTableGRN();
                loadProductsTableGRN();
                loadBrandComboGRN();
                loadCategoryComboGRN();
                loadPaymentTypeComboGRN();
                jTextField22.setEditable(false);
                jTextField22.setText("");
                jLabel63.setText("0.00");
                jLabel67.setText("0.00");
                jLabel67.setForeground(Color.BLACK);
                jCheckBox1.setSelected(false);
                DefaultTableModel dtm = (DefaultTableModel) jTable4.getModel();
                dtm.setRowCount(0);

            } catch (Exception e) {
                e.printStackTrace();
                LOG.error("ERROR:: add grn error : " + e);
            }
        }
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jTextField24KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField24KeyTyped
        // TODO add your handling code here:
        String price = jTextField24.getText();
        String text = price + evt.getKeyChar();

        if (!Pattern.compile("(0|0[.]|0[.][1-9]*)|[1-9]|[1-9][0-9]*|[1-9][0-9]*[.]|[1-9][0-9]*[.][0-9]*").matcher(text).matches()) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextField24KeyTyped

    private void jTextField25KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField25KeyTyped
        // TODO add your handling code here:
        String price = jTextField25.getText();
        String text = price + evt.getKeyChar();

        if (!Pattern.compile("(0|0[.]|0[.][1-9]*)|[1-9]|[1-9][0-9]*|[1-9][0-9]*[.]|[1-9][0-9]*[.][0-9]*").matcher(text).matches()) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextField25KeyTyped

    private void jTextField26KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField26KeyTyped
        // TODO add your handling code here:
        String price = jTextField26.getText();
        String text = price + evt.getKeyChar();

        if (!Pattern.compile("(0|0[.]|0[.][1-9]*)|[1-9]|[1-9][0-9]*|[1-9][0-9]*[.]|[1-9][0-9]*[.][0-9]*").matcher(text).matches()) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextField26KeyTyped

    private void jComboBox7ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox7ItemStateChanged
        // TODO add your handling code here:
        searchStock();
    }//GEN-LAST:event_jComboBox7ItemStateChanged

    private void jComboBox8ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox8ItemStateChanged
        // TODO add your handling code here:
        searchStock();
    }//GEN-LAST:event_jComboBox8ItemStateChanged

    private void jTextField23KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField23KeyReleased
        // TODO add your handling code here:
        searchStock();
    }//GEN-LAST:event_jTextField23KeyReleased

    private void jTextField24KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField24KeyReleased
        // TODO add your handling code here:
        searchStock();
    }//GEN-LAST:event_jTextField24KeyReleased

    private void jTextField25KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField25KeyReleased
        // TODO add your handling code here:
        searchStock();
    }//GEN-LAST:event_jTextField25KeyReleased

    private void jComboBox9ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox9ItemStateChanged
        // TODO add your handling code here:
        searchStock();
    }//GEN-LAST:event_jComboBox9ItemStateChanged

    private void jDateChooser3PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateChooser3PropertyChange
        // TODO add your handling code here:
        searchStock();
    }//GEN-LAST:event_jDateChooser3PropertyChange

    private void jDateChooser4PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateChooser4PropertyChange
        // TODO add your handling code here:
        searchStock();
    }//GEN-LAST:event_jDateChooser4PropertyChange

    private void jDateChooser5PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateChooser5PropertyChange
        // TODO add your handling code here:
        searchStock();
    }//GEN-LAST:event_jDateChooser5PropertyChange

    private void jDateChooser6PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateChooser6PropertyChange
        // TODO add your handling code here:
        searchStock();
    }//GEN-LAST:event_jDateChooser6PropertyChange

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        // TODO add your handling code here:
        resetStockPanel();
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jTable5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable5MouseClicked
        // TODO add your handling code here:
        int selectedRow = jTable5.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a Stock", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            String selling_price = jTable5.getValueAt(selectedRow, 8).toString();
            jLabel83.setText(selling_price);
        }
    }//GEN-LAST:event_jTable5MouseClicked

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        // TODO add your handling code here:
        if (logged_user_type_id.equals("1")||logged_user_type_id.equals("2")||logged_user_type_id.equals("3")){
        
        String sellingPrice = jLabel83.getText();
        String newPrice = jTextField26.getText();
        int selectedRow = jTable5.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a stock", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (!Pattern.compile("(0)|([1-9][0-9]*)|(([1-9][0-9]*)[.]([0]*[1-9][0-9]*))|([0][.]([0]*[1-9][0-9]*))").matcher(newPrice).matches()) {
            JOptionPane.showMessageDialog(this, "Invalid selling price", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            String stock_id = jTable5.getValueAt(selectedRow, 1).toString();
            if (Double.parseDouble(newPrice) <= Double.parseDouble(sellingPrice)) {
                int x = JOptionPane.showConfirmDialog(this, "New price <= Buying price. Do you want to continue?", "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

                if (x == JOptionPane.YES_OPTION) {
                    MySQL.iud("UPDATE `stock` SET `selling_price`='" + newPrice + "' WHERE `id`='" + stock_id + "'");
                    LOG.info("INFO:: update stock selling price>" + sellingPrice + " to>" + newPrice + ", stock_id=" + stock_id);
                    JOptionPane.showMessageDialog(this, "Selling Price Updated", "Success", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                MySQL.iud("UPDATE `stock` SET `selling_price`='" + newPrice + "' WHERE `id`='" + stock_id + "'");
                LOG.info("INFO:: update stock selling price>" + sellingPrice + " to>" + newPrice + ", stock_id=" + stock_id);
                JOptionPane.showMessageDialog(this, "Selling Price Updated", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
            resetStockPanel();
        }
        }else{
            JOptionPane.showMessageDialog(this, "You don't have permition to update selling price", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_jButton15ActionPerformed

    private void jTextField27KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField27KeyTyped
        // TODO add your handling code here:
        String qty = jTextField27.getText();
        String text = qty + evt.getKeyChar();

        if (!Pattern.compile("[1-9][0-9]*").matcher(text).matches()) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextField27KeyTyped

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
        // TODO add your handling code here:
        int selectedRow = jTable6.getSelectedRow();
        String qty = jTextField27.getText();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a product", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (!Pattern.compile("[1-9][0-9]*").matcher(qty).matches()) {
            JOptionPane.showMessageDialog(this, "Please enter quantity", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            try {
                String sid = jTable6.getValueAt(selectedRow, 0).toString();
                String pName = jTable6.getValueAt(selectedRow, 1).toString();
                String sp = jTable6.getValueAt(selectedRow, 4).toString();

//                ResultSet stock_rs = MySQL.search("SELECT * FROM `stock` WHERE `stock`.`id`=?", sid);
//                stock_rs.next();
//                String availableQty = stock_rs.getString("quantity");
//                if (Integer.parseInt(availableQty) < Integer.parseInt(qty)) {
//                    JOptionPane.showMessageDialog(this, "Quantity out of stock", "Warning", JOptionPane.WARNING_MESSAGE);
//                } else {
                    DefaultTableModel dtm = (DefaultTableModel) jTable7.getModel();
                    boolean isFound = false;
                    int x = -1;
                    for (int i = 0; i < dtm.getRowCount(); i++) {
                        String s = jTable7.getValueAt(i, 0).toString();
                        if (s.equals(sid)) {
                            isFound = true;
                            x = i;
                            break;
                        }
                    }
                    //add
                    if (isFound) {
                        int option = JOptionPane.showConfirmDialog(this, "This product is already added. Do you want to update?", "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
                        if (option == JOptionPane.YES_OPTION) {
                            int oldQty = Integer.parseInt(jTable7.getValueAt(x, 3).toString());
                            int finalQty = oldQty + Integer.parseInt(qty);
                            //check stock
//                            if (Integer.parseInt(availableQty) < finalQty) {
//                                JOptionPane.showMessageDialog(this, "Quantity out of stock", "Warning", JOptionPane.WARNING_MESSAGE);
//                            } else {
                                jTable7.setValueAt(String.valueOf(finalQty), x, 3);
                                double updatedItemtotal = finalQty * Double.parseDouble(sp);
                                jTable7.setValueAt(String.valueOf(updatedItemtotal), x, 4);
                                updateInvoiceTotal();
                                LOG.info("INFO:: invoice, update cart item");
//                            }
                            //check stock
                        }
//                        resetfields();
                    } else {
                        Vector v = new Vector();
                        v.add(sid);
                        v.add(pName);
                        v.add(sp);
                        v.add(qty);
                        double itemtotal = Integer.parseInt(qty) * Double.parseDouble(sp);
                        v.add(df.format(itemtotal));
                        dtm.addRow(v);
                        updateInvoiceTotal();
                        LOG.info("INFO:: invoice, add product to cart");
                        
                        JOptionPane.showMessageDialog(this, "Product add to Invoice", "Success", JOptionPane.INFORMATION_MESSAGE);
                    }
                    loadProductTableInvoice();
                    jTextField27.setText("");
                    jComboBox10.setSelectedIndex(0);
//                }
            } catch (Exception e) {
                e.printStackTrace();
                LOG.error("ERROR:: invoice, add to cart error" + e);
            }

        }
    }//GEN-LAST:event_jButton16ActionPerformed

    private void jComboBox10ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox10ItemStateChanged
        // TODO add your handling code here:
        loadProductTableByCategoryInvoice();
    }//GEN-LAST:event_jComboBox10ItemStateChanged

    private void jButton16MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton16MouseEntered
        // TODO add your handling code here:
        jButton16.setBackground(new Color(220, 100, 10));
    }//GEN-LAST:event_jButton16MouseEntered

    private void jButton16MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton16MouseExited
        // TODO add your handling code here:
        jButton16.setBackground(new Color(253, 126, 20));
    }//GEN-LAST:event_jButton16MouseExited

    private void jButton17MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton17MouseExited
        // TODO add your handling code here:
        jButton17.setBackground(new Color(40, 167, 69));
    }//GEN-LAST:event_jButton17MouseExited

    private void jButton17MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton17MouseEntered
        // TODO add your handling code here:
        jButton17.setBackground(new Color(25, 150, 50));
    }//GEN-LAST:event_jButton17MouseEntered

    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton17ActionPerformed
        // TODO add your handling code here:

        String paymentType = jComboBox11.getSelectedItem().toString();
        String payment = jTextField28.getText();
        if (jTable7.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Please add product", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (payment.equals("")) {
            JOptionPane.showMessageDialog(this, "Please enter payment", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (!Pattern.compile("(0)|([1-9][0-9]*)|(([1-9][0-9]*)[.]([0]*[1-9][0-9]*))|([0][.]([0]*[1-9][0-9]*))").matcher(payment).matches()) {
            JOptionPane.showMessageDialog(this, "Invalid payment", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            // invoice insert
            long mTime = System.currentTimeMillis();
            String uniqueId = mTime + "-" + logged_user_id;
//            String cid = jLabel5.getText();

            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dNow = sdf2.format(new Date());

            MySQL.iud("INSERT INTO `invoice` (`date_time`,`user_id`,`unique_id`) VALUES (?,?,?)", dNow, logged_user_id, uniqueId);
            // Invoice insert
            try {
                //Invoice payment insert
                ResultSet rs = MySQL.search("SELECT * FROM `invoice` WHERE `unique_id`=?", uniqueId);
                rs.next();
                String id = rs.getString("id");
                ResultSet rs2 = MySQL.search("SELECT * FROM `payment_type` WHERE `name`=?", paymentType);
                rs2.next();
                String paymentTypeId = rs2.getString("id");
                String balance = jLabel92.getText();
                MySQL.iud("INSERT INTO `invoice_payment` (`invoice_id`,`payment_type_id`,`payment`,`balance`) VALUES (?,?,?,?)", id, paymentTypeId, payment, balance);
                //invoice payment insert
                //invoice item insert &stock insert/update
                Vector v = new Vector();
                for (int i = 0; i < jTable7.getRowCount(); i++) {
                    String sid = jTable7.getValueAt(i, 0).toString();
                    String qty = jTable7.getValueAt(i, 3).toString();

                    String selling_price = jTable7.getValueAt(i, 2).toString();
                    String name = jTable7.getValueAt(i, 1).toString();
//                    String price = jTable7.getValueAt(i, 6).toString();
                    String total = jTable7.getValueAt(i, 4).toString();
                    ResultSet rs3 = MySQL.search("SELECT * FROM `stock` WHERE `id`=?", sid);
                    rs3.next();
                    String availableQty = rs3.getString("quantity");
                    int updateQty = Integer.parseInt(availableQty) - Integer.parseInt(qty);

                    model.InvoiceBean ib = new model.InvoiceBean(qty, name, total);
                    v.add(ib);

                    MySQL.iud("UPDATE `stock` SET `quantity` = ? WHERE `id`=?", updateQty, sid);
//                    MySQL.iud("INSERT INTO `invoice_item` (`qty`,`invoice_id`,`stock_id`) VALUES (?,?,?)", qty, id, sid);
                    MySQL.iud("INSERT INTO `invoice_item` (`qty`,`invoice_id`,`stock_id`,`selling_price`) VALUES (?,?,?,?)", qty, id, sid,selling_price);

                    JOptionPane.showMessageDialog(this, "New Invoice created", "Success", JOptionPane.INFORMATION_MESSAGE);
                    LOG.info("INFO:: add new invoice");
                }

                if (jCheckBox2.isSelected()) {
                    
//                    String path = "src//reports//canteen_invoice1.jasper";
                    InputStream path = getClass().getResourceAsStream("/reports/canteen_invoice1.jasper");

                    if (path == null) {
                        JOptionPane.showMessageDialog(this, "Report Not found", "Warning", JOptionPane.WARNING_MESSAGE);

                    } else {
                        HashMap parameters = new HashMap();
                        parameters.put("Parameter1", jLabel88.getText());

                        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(v);

                        JasperPrint jp = JasperFillManager.fillReport(path, parameters, dataSource);

                        boolean printJobCompleted = JasperPrintManager.printReport(jp, false); // false for printing without showing print dialog

                        if (!printJobCompleted) {
                            LOG.info("INFO:: invoice printing failed.");
                            JOptionPane.showMessageDialog(this, "Report printing failed", "Warning", JOptionPane.WARNING_MESSAGE);
//                            System.out.println("Report printing failed.");
                        } else {
                            LOG.info("INFO:: invoice printed.");
                            JOptionPane.showMessageDialog(this, "Report sent to printer.", "Success", JOptionPane.INFORMATION_MESSAGE);
//                            System.out.println("Report sent to printer.");
                        }
//                        JasperViewer.viewReport(jp, false);
                    }

                }
                

                //payment
                resetInvoice();
                //payment
                DefaultTableModel dtm = (DefaultTableModel) jTable7.getModel();
                dtm.setRowCount(0);
                //invoice item insert &stock insert/update
            } catch (Exception e) {
                e.printStackTrace();
                LOG.error("ERROR:: add new invoice=" + e);
            }
        }
    }//GEN-LAST:event_jButton17ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
        KeyBoard keyBoard = new KeyBoard(this, true, jTextField1);
        keyBoard.setVisible(true);
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jTextField28KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField28KeyTyped
        // TODO add your handling code here:
        String qty = jTextField28.getText();
        String text = qty + evt.getKeyChar();

        if (!Pattern.compile("[1-9][0-9]*").matcher(text).matches()) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextField28KeyTyped

    private void jTable7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable7MouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            int r = jTable7.getSelectedRow();
            if (r == -1) {
                JOptionPane.showMessageDialog(this, "Please select invoice item", "Warning", JOptionPane.WARNING_MESSAGE);
            } else {
                int option = JOptionPane.showConfirmDialog(this, "Do you want to remove product?", "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
                if (option == JOptionPane.YES_OPTION) {
                    DefaultTableModel dtm = (DefaultTableModel) jTable7.getModel();
                    dtm.removeRow(r);
                    updateInvoiceTotal();
                    //payment
                    jTextField28.setText("");
                    jTextField28.setEditable(false);
                    jLabel92.setText("0.00");
                    jComboBox11.setSelectedItem(0);
                    //payment

                    JOptionPane.showMessageDialog(this, "Invoice item removed", "Success", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
    }//GEN-LAST:event_jTable7MouseClicked

    private void jTextField28KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField28KeyReleased
        // TODO add your handling code here:
        calcBalanceInInvoice();
    }//GEN-LAST:event_jTextField28KeyReleased

    private void jButton21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton21ActionPerformed
        // TODO add your handling code here:
        stockjPanel33.setVisible(true);
        homejPanel18.setVisible(false);
        loadStockPanel();
    }//GEN-LAST:event_jButton21ActionPerformed

    private void jButton19MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton19MouseEntered
        // TODO add your handling code here:
        jButton19.setBackground(new Color(0, 105, 220));
    }//GEN-LAST:event_jButton19MouseEntered

    private void jButton19MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton19MouseExited
        // TODO add your handling code here:
        jButton19.setBackground(new Color(0, 123, 255));
    }//GEN-LAST:event_jButton19MouseExited

    private void jButton18MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton18MouseEntered
        // TODO add your handling code here:
        jButton18.setBackground(new Color(25, 150, 50));
    }//GEN-LAST:event_jButton18MouseEntered

    private void jButton18MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton18MouseExited
        // TODO add your handling code here:
        jButton18.setBackground(new Color(40, 167, 69));
    }//GEN-LAST:event_jButton18MouseExited

    private void jButton20MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton20MouseEntered
        // TODO add your handling code here:
        jButton20.setBackground(new Color(220, 170, 0));
    }//GEN-LAST:event_jButton20MouseEntered

    private void jButton20MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton20MouseExited
        // TODO add your handling code here:
        jButton20.setBackground(new Color(255, 193, 7));
    }//GEN-LAST:event_jButton20MouseExited

    private void jButton21MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton21MouseEntered
        // TODO add your handling code here:
        jButton21.setBackground(new Color(20, 140, 160));
    }//GEN-LAST:event_jButton21MouseEntered

    private void jButton21MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton21MouseExited
        // TODO add your handling code here:
        jButton21.setBackground(new Color(23, 162, 184));
    }//GEN-LAST:event_jButton21MouseExited

    private void jButton22MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton22MouseEntered
        // TODO add your handling code here:
        jButton22.setBackground(new Color(88, 97, 105));
    }//GEN-LAST:event_jButton22MouseEntered

    private void jButton22MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton22MouseExited
        // TODO add your handling code here:
        jButton22.setBackground(new Color(108, 117, 125));
    }//GEN-LAST:event_jButton22MouseExited

    private void jButton19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton19ActionPerformed
        // TODO add your handling code here:
        userManagejPanel19.setVisible(true);
        homejPanel18.setVisible(false);

        loadUserManagePanel();
    }//GEN-LAST:event_jButton19ActionPerformed

    private void jButton18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton18ActionPerformed
        // TODO add your handling code here:
        grnjPanel23.setVisible(true);
        homejPanel18.setVisible(false);
        loadGRNPanel();
    }//GEN-LAST:event_jButton18ActionPerformed

    private void jButton20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton20ActionPerformed
        // TODO add your handling code here:
        myProfilejPanel23.setVisible(true);
        homejPanel18.setVisible(false);

        loadMyProfilePanel();
    }//GEN-LAST:event_jButton20ActionPerformed

    private void jButton22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton22ActionPerformed
        // TODO add your handling code here:
        invoicejPanel36.setVisible(true);
        homejPanel18.setVisible(false);

        loadInvoicePanel();
    }//GEN-LAST:event_jButton22ActionPerformed

    private void jTextField1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField1MouseClicked
        // TODO add your handling code here:
        KeyBoard keyBoard = new KeyBoard(this, true, jTextField1);
        keyBoard.setVisible(true);
    }//GEN-LAST:event_jTextField1MouseClicked

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
        KeyBoard keyBoard = new KeyBoard(this, true, jTextField2);
        keyBoard.setVisible(true);
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jTextField2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField2MouseClicked
        // TODO add your handling code here:
        KeyBoard keyBoard = new KeyBoard(this, true, jTextField2);
        keyBoard.setVisible(true);
    }//GEN-LAST:event_jTextField2MouseClicked

    private void jTextField4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField4MouseClicked
        // TODO add your handling code here:
        KeyBoard keyBoard = new KeyBoard(this, true, jTextField4);
        keyBoard.setVisible(true);
    }//GEN-LAST:event_jTextField4MouseClicked

    private void jTextField3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField3MouseClicked
        // TODO add your handling code here:
        Numpad numpad = new Numpad(this, true, jTextField3);
        numpad.setVisible(true);
    }//GEN-LAST:event_jTextField3MouseClicked

    private void jTextField8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField8MouseClicked
        // TODO add your handling code here:
        KeyBoard keyBoard = new KeyBoard(this, true, jTextField8);
        keyBoard.setVisible(true);
    }//GEN-LAST:event_jTextField8MouseClicked

    private void jTextField5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField5MouseClicked
        // TODO add your handling code here:
        KeyBoard keyBoard = new KeyBoard(this, true, jTextField5);
        keyBoard.setVisible(true);
    }//GEN-LAST:event_jTextField5MouseClicked

    private void jTextField7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField7MouseClicked
        // TODO add your handling code here:
        KeyBoard keyBoard = new KeyBoard(this, true, jTextField7);
        keyBoard.setVisible(true);
    }//GEN-LAST:event_jTextField7MouseClicked

    private void jTextField10MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField10MouseClicked
        // TODO add your handling code here:
        KeyBoard keyBoard = new KeyBoard(this, true, jTextField10);
        keyBoard.setVisible(true);
    }//GEN-LAST:event_jTextField10MouseClicked

    private void jTextField14MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField14MouseClicked
        // TODO add your handling code here:
        KeyBoard keyBoard = new KeyBoard(this, true, jTextField14);
        keyBoard.setVisible(true);
    }//GEN-LAST:event_jTextField14MouseClicked

    private void jTextField18MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField18MouseClicked
        // TODO add your handling code here:
        KeyBoard keyBoard = new KeyBoard(this, true, jTextField18);
        keyBoard.setVisible(true);
    }//GEN-LAST:event_jTextField18MouseClicked

    private void jTextField19MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField19MouseClicked
        // TODO add your handling code here:
        Numpad numpad = new Numpad(this, true, jTextField19);
        numpad.setVisible(true);
    }//GEN-LAST:event_jTextField19MouseClicked

    private void jTextField20MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField20MouseClicked
        // TODO add your handling code here:
        Numpad numpad = new Numpad(this, true, jTextField20);
        numpad.setVisible(true);
    }//GEN-LAST:event_jTextField20MouseClicked

    private void jTextField21MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField21MouseClicked
        // TODO add your handling code here:
        Numpad numpad = new Numpad(this, true, jTextField21);
        numpad.setVisible(true);
    }//GEN-LAST:event_jTextField21MouseClicked

    private void jTextField22MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField22MouseClicked
        // TODO add your handling code here:
        Numpad numpad = new Numpad(this, true, jTextField22);
        numpad.setVisible(true);
    }//GEN-LAST:event_jTextField22MouseClicked

    private void jTextField6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField6MouseClicked
        // TODO add your handling code here:
        KeyBoard keyBoard = new KeyBoard(this, true, jTextField6);
        keyBoard.setVisible(true);
    }//GEN-LAST:event_jTextField6MouseClicked

    private void jTextField12MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField12MouseClicked
        // TODO add your handling code here:
        KeyBoard keyBoard = new KeyBoard(this, true, jTextField12);
        keyBoard.setVisible(true);
    }//GEN-LAST:event_jTextField12MouseClicked

    private void jTextField9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField9MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField9MouseClicked

    private void jTextField13MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField13MouseClicked
        // TODO add your handling code here:
        Numpad numpad = new Numpad(this, true, jTextField13);
        numpad.setVisible(true);
    }//GEN-LAST:event_jTextField13MouseClicked

    private void jPasswordField1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPasswordField1MouseClicked
        // TODO add your handling code here:
        KeyBoard keyBoard = new KeyBoard(this, true, jPasswordField1);
        keyBoard.setVisible(true);
    }//GEN-LAST:event_jPasswordField1MouseClicked

    private void jPasswordField3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPasswordField3MouseClicked
        // TODO add your handling code here:
        KeyBoard keyBoard = new KeyBoard(this, true, jPasswordField3);
        keyBoard.setVisible(true);
    }//GEN-LAST:event_jPasswordField3MouseClicked

    private void jTextField15MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField15MouseClicked
        // TODO add your handling code here:
        KeyBoard keyBoard = new KeyBoard(this, true, jTextField15);
        keyBoard.setVisible(true);
    }//GEN-LAST:event_jTextField15MouseClicked

    private void jTextField17MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField17MouseClicked
        // TODO add your handling code here:
        KeyBoard keyBoard = new KeyBoard(this, true, jTextField17);
        keyBoard.setVisible(true);
    }//GEN-LAST:event_jTextField17MouseClicked

    private void jTextField16MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField16MouseClicked
        // TODO add your handling code here:
        KeyBoard keyBoard = new KeyBoard(this, true, jTextField16);
        keyBoard.setVisible(true);
    }//GEN-LAST:event_jTextField16MouseClicked

    private void jTextField23MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField23MouseClicked
        // TODO add your handling code here:
        KeyBoard keyBoard = new KeyBoard(this, true, jTextField23);
        keyBoard.setVisible(true);
    }//GEN-LAST:event_jTextField23MouseClicked

    private void jTextField24MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField24MouseClicked
        // TODO add your handling code here:
        Numpad numpad = new Numpad(this, true, jTextField24);
        numpad.setVisible(true);
    }//GEN-LAST:event_jTextField24MouseClicked

    private void jTextField25MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField25MouseClicked
        // TODO add your handling code here:
        Numpad numpad = new Numpad(this, true, jTextField25);
        numpad.setVisible(true);
    }//GEN-LAST:event_jTextField25MouseClicked

    private void jTextField26MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField26MouseClicked
        // TODO add your handling code here:
        Numpad numpad = new Numpad(this, true, jTextField26);
        numpad.setVisible(true);
    }//GEN-LAST:event_jTextField26MouseClicked

    private void jTextField27MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField27MouseClicked
        // TODO add your handling code here:
        Numpad numpad = new Numpad(this, true, jTextField27);
        numpad.setVisible(true);
    }//GEN-LAST:event_jTextField27MouseClicked

    private void jTextField28MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField28MouseClicked
        // TODO add your handling code here:
        Numpad numpad = new Numpad(this, true, jTextField28);
        numpad.setVisible(true);
    }//GEN-LAST:event_jTextField28MouseClicked

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        // TODO add your handling code here:
        try {
//            String path = "src//reports//canteen_stock_report.jasper";
            InputStream path = getClass().getResourceAsStream("/reports/canteen_stock_report.jasper");

            if (path == null) {
                JOptionPane.showMessageDialog(this, "Report Not found", "Warning", JOptionPane.WARNING_MESSAGE);

            } else {
                Vector v = new Vector();
                for (int i = 0; i < jTable5.getRowCount(); i++) {
                    String id = jTable5.getValueAt(i, 0).toString();
                    String name = jTable5.getValueAt(i, 5).toString();
                    String category = jTable5.getValueAt(i, 3).toString();
                    String brand = jTable5.getValueAt(i, 4).toString();
                    String qty = jTable5.getValueAt(i, 6).toString();
                    String buying_price = jTable5.getValueAt(i, 7).toString();
                    String selling_price = jTable5.getValueAt(i, 8).toString();
                    String mfd = jTable5.getValueAt(i, 9).toString();
                    String exd = jTable5.getValueAt(i, 10).toString();

                    model.StockBean g1 = new model.StockBean(id, name, category, brand, qty, buying_price, selling_price, mfd, exd);
                    v.add(g1);
                }

                JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(v);

                JasperPrint jp = JasperFillManager.fillReport(path, null, dataSource);
                JasperViewer.viewReport(jp, false);

                LOG.info("INFO:: print stock ");
            }

        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("ERROR:: print stock error=" + e);
        }
    }//GEN-LAST:event_jButton14ActionPerformed

    private void jButton23MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton23MouseExited
        // TODO add your handling code here:
        jButton23.setBackground(new Color(253, 126, 20));
    }//GEN-LAST:event_jButton23MouseExited

    private void jButton23MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton23MouseEntered
        // TODO add your handling code here:
        jButton23.setBackground(new Color(220, 100, 10));
    }//GEN-LAST:event_jButton23MouseEntered

    private void jButton24MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton24MouseExited
        // TODO add your handling code here:
        jButton24.setBackground(new Color(0, 86, 179));
    }//GEN-LAST:event_jButton24MouseExited

    private void jButton24MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton24MouseEntered
        // TODO add your handling code here:
        jButton24.setBackground(new Color(0, 70, 140));
    }//GEN-LAST:event_jButton24MouseEntered

    private void jButton23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton23ActionPerformed
        // TODO add your handling code here:
        cashSummaryjPanel13.setVisible(true);
        homejPanel18.setVisible(false);
        loadCashSummaryPanel();
    }//GEN-LAST:event_jButton23ActionPerformed

    private void jTable8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable8MouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 1) {
            int r = jTable8.getSelectedRow();
            if (r == -1) {
                JOptionPane.showMessageDialog(this, "Please select a Invoice", "Warning", JOptionPane.WARNING_MESSAGE);
            } else {
                String invoice_id = jTable8.getValueAt(r, 0).toString();

                try {
                    ResultSet rs = MySQL.search("SELECT * FROM `invoice_item` INNER JOIN `stock` ON `invoice_item`.`stock_id`=`stock`.`id` INNER JOIN `product` ON `stock`.`product_id`=`product`.`id` WHERE `invoice_item`.`invoice_id`=?", invoice_id);
                    DefaultTableModel dtm = (DefaultTableModel) jTable9.getModel();
                    dtm.setRowCount(0);
                    while (rs.next()) {
                        Vector v1 = new Vector();
                        v1.add(rs.getString("invoice_item.id"));
                        v1.add(rs.getString("product.title"));
                        v1.add(rs.getString("invoice_item.qty"));
                        v1.add(String.format("%.2f", rs.getDouble("invoice_item.selling_price")));
                        double quantity = rs.getDouble("invoice_item.qty");
                        double sellingPrice = rs.getDouble("invoice_item.selling_price");
                        double totalPrice = quantity * sellingPrice;
                        v1.add(String.format("%.2f", totalPrice));
                        dtm.addRow(v1);
                    }
                    jTable9.setModel(dtm);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }//GEN-LAST:event_jTable8MouseClicked

    private void jButton26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton26ActionPerformed
        // TODO add your handling code here:
        loadInviocesTableCashSummary();
        jDateChooser7.setDate(null);
        jDateChooser8.setDate(null);
        DefaultTableModel dtm = (DefaultTableModel) jTable9.getModel();
        dtm.setRowCount(0);
    }//GEN-LAST:event_jButton26ActionPerformed

    private void jDateChooser8PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateChooser8PropertyChange
        // TODO add your handling code here:
        loadInviocesTableByDatesCashSummary();
    }//GEN-LAST:event_jDateChooser8PropertyChange

    private void jDateChooser7PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateChooser7PropertyChange
        // TODO add your handling code here:
        loadInviocesTableByDatesCashSummary();
    }//GEN-LAST:event_jDateChooser7PropertyChange

    private void jButton27MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton27MouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton27MouseExited

    private void jButton25MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton25MouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton25MouseExited

    private void jButton24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton24ActionPerformed
        // TODO add your handling code here:
        grnHistoryjPanel14.setVisible(true);
        homejPanel18.setVisible(false);
        loadGRNHistoryPanel();
    }//GEN-LAST:event_jButton24ActionPerformed

    private void jTable10MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable10MouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 1) {
            int r = jTable10.getSelectedRow();
            if (r == -1) {
                JOptionPane.showMessageDialog(this, "Please select a GRN", "Warning", JOptionPane.WARNING_MESSAGE);
            } else {
                String grn_id = jTable10.getValueAt(r, 0).toString();

                try {
                    ResultSet rs = MySQL.search("SELECT * FROM `grn_item` INNER JOIN `stock` ON `grn_item`.`stock_id`=`stock`.`id` INNER JOIN `product` ON `stock`.`product_id`=`product`.`id` WHERE `grn_item`.`grn_id`=?", grn_id);
                    DefaultTableModel dtm = (DefaultTableModel) jTable11.getModel();
                    dtm.setRowCount(0);
                    while (rs.next()) {
                        Vector v1 = new Vector();
                        v1.add(rs.getString("grn_item.id"));
                        v1.add(rs.getString("product.title"));
                        v1.add(rs.getString("grn_item.quantity"));
                        v1.add(String.format("%.2f", rs.getDouble("grn_item.buying_price")));
                        double quantity = rs.getDouble("grn_item.quantity");
                        double buyingPrice = rs.getDouble("grn_item.buying_price");
                        double totalPrice = quantity * buyingPrice;
                        v1.add(String.format("%.2f", totalPrice));
                        dtm.addRow(v1);
                    }
                    jTable11.setModel(dtm);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }//GEN-LAST:event_jTable10MouseClicked

    private void jButton28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton28ActionPerformed
        // TODO add your handling code here:
        loadGRNsTableGRNHistory();
        jDateChooser9.setDate(null);
        jDateChooser10.setDate(null);
        DefaultTableModel dtm = (DefaultTableModel) jTable11.getModel();
        dtm.setRowCount(0);
    }//GEN-LAST:event_jButton28ActionPerformed

    private void jDateChooser9PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateChooser9PropertyChange
        // TODO add your handling code here:
        loadGRNsTableByDatesGRNHistory();
    }//GEN-LAST:event_jDateChooser9PropertyChange

    private void jDateChooser10PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateChooser10PropertyChange
        // TODO add your handling code here:
        loadGRNsTableByDatesGRNHistory();
    }//GEN-LAST:event_jDateChooser10PropertyChange

    private void jButton27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton27ActionPerformed
        // TODO add your handling code here:

        int r = jTable10.getSelectedRow();
        if (r == -1) {
            JOptionPane.showMessageDialog(this, "Please select a GRN", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            String grn_id = jTable10.getValueAt(r, 0).toString();
            try {

                ResultSet rs = MySQL.search("SELECT * FROM `grn` INNER JOIN `grn_payment` ON `grn`.`id`=`grn_payment`.`grn_id` INNER JOIN `supplier` ON `grn`.`supplier_id`=`supplier`.`id` INNER JOIN `company_branch` ON `supplier`.`company_branch_id`=`company_branch`.`id` INNER JOIN `company` ON `company_branch`.`company_id`=`company`.`id` WHERE `grn`.`id`=?", grn_id);
                rs.next();

                ResultSet grnItems_rs = MySQL.search("SELECT * FROM `grn_item` INNER JOIN `stock` ON `grn_item`.`stock_id`=`stock`.`id` INNER JOIN `product` ON `stock`.`product_id`=`product`.`id` INNER JOIN `brand` ON `product`.`brand_id`=`brand`.`id` WHERE `grn_item`.`grn_id`=?", grn_id);

//                JOptionPane.showMessageDialog(this, "New GRN created, print grn", "Success", JOptionPane.INFORMATION_MESSAGE);
//                String path = "src//reports//canteen_grn.jasper";
                InputStream path = getClass().getResourceAsStream("/reports/canteen_grn.jasper");

                if (path == null) {
                    JOptionPane.showMessageDialog(this, "Report Not found", "Warning", JOptionPane.WARNING_MESSAGE);

                } else {
                    double total = rs.getDouble("grn_payment.payment") - rs.getDouble("grn_payment.balance");

                    HashMap parameters = new HashMap();
                    parameters.put("Parameter1", rs.getString("supplier.name"));
                    parameters.put("Parameter2", rs.getString("company.name"));
                    parameters.put("Parameter3", rs.getString("supplier.mobile"));
                    parameters.put("Parameter4", rs.getString("grn.unique_id"));
                    parameters.put("Parameter5", rs.getString("grn.date_time"));
                    parameters.put("Parameter6", String.format("%.2f", total));
                    parameters.put("Parameter7", String.format("%.2f", rs.getDouble("grn_payment.payment")));
                    parameters.put("Parameter8", String.format("%.2f", rs.getDouble("grn_payment.balance")));

                    Vector v = new Vector();
                    while (grnItems_rs.next()) {
                        String pid = grnItems_rs.getString("product.id");
                        String pname = grnItems_rs.getString("product.title");
                        String brand = grnItems_rs.getString("brand.name");
                        String qty = grnItems_rs.getString("grn_item.quantity");
                        String buyingPrice = String.format("%.2f", grnItems_rs.getDouble("grn_item.buying_price"));
                        String mfd = grnItems_rs.getString("stock.mfd");
                        String exd = grnItems_rs.getString("stock.exd");
                        double totalPrice = grnItems_rs.getDouble("grn_item.buying_price") * grnItems_rs.getInt("grn_item.quantity");
                        String to = String.format("%.2f", totalPrice);

                        model.GRNBean g1 = new model.GRNBean(pid, pname, brand, qty, buyingPrice, mfd, exd, to);
                        v.add(g1);
                    }

                    JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(v);

                    JasperPrint jp = JasperFillManager.fillReport(path, parameters, dataSource);
                    JasperViewer.viewReport(jp, false);

                    LOG.info("INFO:: grn history, print selected grn");
                }

            } catch (Exception e) {
                e.printStackTrace();
                LOG.error("ERROR:: grn history, print selected grn error : " + e);
            }

        }

    }//GEN-LAST:event_jButton27ActionPerformed

    private void jButton25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton25ActionPerformed
        // TODO add your handling code here:
        try {

            String dateFrom = null;
            String dateTo = null;

            if (jDateChooser7.getDate() != null) {
                dateFrom = sdf.format(jDateChooser7.getDate());
            }
            if (jDateChooser8.getDate() != null) {
                dateTo = sdf.format(jDateChooser8.getDate());
            }

            // Base query
            String details = "All Invoices Results";

            // Add date conditions
            if (dateFrom != null && dateTo != null) {
                details = "Invoices From " + dateFrom + " 00:00:00' To '" + dateTo + " 23:59:59'";
            } else if (dateFrom != null) {
                details = "Invoices From " + dateFrom + " 00:00:00'";
            } else if (dateTo != null) {
                details = "Invoices To " + dateTo + " 23:59:59'";
            }

//                JOptionPane.showMessageDialog(this, "New GRN created, print grn", "Success", JOptionPane.INFORMATION_MESSAGE);
//            String path = "src//reports//canteen_invoices.jasper";
            InputStream path = getClass().getResourceAsStream("/reports/canteen_invoices.jasper");

            if (path == null) {
                JOptionPane.showMessageDialog(this, "Report Not found", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            } else {
                double finalTotal = 0;

                Vector v = new Vector();
                for (int i = 0; i < jTable8.getRowCount(); i++) {
                    String id = jTable8.getValueAt(i, 0).toString();
                    String date_time = jTable8.getValueAt(i, 1).toString();
                    String user = jTable8.getValueAt(i, 2).toString();
                    String payment = jTable8.getValueAt(i, 3).toString();
                    String balance = jTable8.getValueAt(i, 4).toString();
                    String total = jTable8.getValueAt(i, 5).toString();

                    double totalValue = Double.parseDouble(total);
                    finalTotal += totalValue;

                    model.InvoicesReportBean g1 = new model.InvoicesReportBean(id, date_time, user, payment, balance, total);
                    v.add(g1);
                }

                HashMap parameters = new HashMap();
                parameters.put("Parameter1", details);
                parameters.put("Parameter2", String.format("%.2f", finalTotal));

                JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(v);

                JasperPrint jp = JasperFillManager.fillReport(path, parameters, dataSource);
                JasperViewer.viewReport(jp, false);

                LOG.info("INFO:: invoice history, print invoice list");
            }

        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("ERROR:: invoice history, print invoice list, error : " + e);
        }
    }//GEN-LAST:event_jButton25ActionPerformed

    private void jButton29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton29ActionPerformed
        // TODO add your handling code here:
        summaryjPanel4.setVisible(true);
        homejPanel18.setVisible(false);
        loadSummaryPanel();
    }//GEN-LAST:event_jButton29ActionPerformed

    private void jButton30ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton30ActionPerformed
        // TODO add your handling code here:
        Login login = new Login();
        login.setVisible(true);
        LOG.info("INFO:: log out success");
        this.dispose();
    }//GEN-LAST:event_jButton30ActionPerformed

    private void jButton31ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton31ActionPerformed
        // TODO add your handling code here:
        if(MySQL.backupDatabase()){
            JOptionPane.showMessageDialog(this, "Backup Saved", "Success", JOptionPane.INFORMATION_MESSAGE);
        }else{
            JOptionPane.showMessageDialog(this, "Backup save fail", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_jButton31ActionPerformed

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
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Home("").setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel cashSummaryjPanel13;
    private javax.swing.JPanel grnHistoryjPanel14;
    private javax.swing.JPanel grnjPanel23;
    private javax.swing.JPanel homejPanel18;
    private javax.swing.JPanel invoicejPanel36;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton19;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton20;
    private javax.swing.JButton jButton21;
    private javax.swing.JButton jButton22;
    private javax.swing.JButton jButton23;
    private javax.swing.JButton jButton24;
    private javax.swing.JButton jButton25;
    private javax.swing.JButton jButton26;
    private javax.swing.JButton jButton27;
    private javax.swing.JButton jButton28;
    private javax.swing.JButton jButton29;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton30;
    private javax.swing.JButton jButton31;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox10;
    private javax.swing.JComboBox<String> jComboBox11;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JComboBox<String> jComboBox4;
    private javax.swing.JComboBox<String> jComboBox5;
    private javax.swing.JComboBox<String> jComboBox6;
    private javax.swing.JComboBox<String> jComboBox7;
    private javax.swing.JComboBox<String> jComboBox8;
    private javax.swing.JComboBox<String> jComboBox9;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private com.toedter.calendar.JDateChooser jDateChooser10;
    private com.toedter.calendar.JDateChooser jDateChooser2;
    private com.toedter.calendar.JDateChooser jDateChooser3;
    private com.toedter.calendar.JDateChooser jDateChooser4;
    private com.toedter.calendar.JDateChooser jDateChooser5;
    private com.toedter.calendar.JDateChooser jDateChooser6;
    private com.toedter.calendar.JDateChooser jDateChooser7;
    private com.toedter.calendar.JDateChooser jDateChooser8;
    private com.toedter.calendar.JDateChooser jDateChooser9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JLabel jLabel71;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel73;
    private javax.swing.JLabel jLabel74;
    private javax.swing.JLabel jLabel75;
    private javax.swing.JLabel jLabel76;
    private javax.swing.JLabel jLabel77;
    private javax.swing.JLabel jLabel78;
    private javax.swing.JLabel jLabel79;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel80;
    private javax.swing.JLabel jLabel81;
    private javax.swing.JLabel jLabel82;
    private javax.swing.JLabel jLabel83;
    private javax.swing.JLabel jLabel84;
    private javax.swing.JLabel jLabel85;
    private javax.swing.JLabel jLabel86;
    private javax.swing.JLabel jLabel87;
    private javax.swing.JLabel jLabel88;
    private javax.swing.JLabel jLabel89;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel90;
    private javax.swing.JLabel jLabel91;
    private javax.swing.JLabel jLabel92;
    private javax.swing.JLabel jLabel93;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel32;
    private javax.swing.JPanel jPanel33;
    private javax.swing.JPanel jPanel34;
    private javax.swing.JPanel jPanel35;
    private javax.swing.JPanel jPanel36;
    private javax.swing.JPanel jPanel37;
    private javax.swing.JPanel jPanel38;
    private javax.swing.JPanel jPanel39;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel40;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JPasswordField jPasswordField3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable10;
    private javax.swing.JTable jTable11;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTable jTable4;
    private javax.swing.JTable jTable5;
    private javax.swing.JTable jTable6;
    private javax.swing.JTable jTable7;
    private javax.swing.JTable jTable8;
    private javax.swing.JTable jTable9;
    private javax.swing.JTextField jTextField1;
    public javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField11;
    private javax.swing.JTextField jTextField12;
    private javax.swing.JTextField jTextField13;
    public javax.swing.JTextField jTextField14;
    private javax.swing.JTextField jTextField15;
    private javax.swing.JTextField jTextField16;
    private javax.swing.JTextField jTextField17;
    public javax.swing.JTextField jTextField18;
    private javax.swing.JTextField jTextField19;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField20;
    private javax.swing.JTextField jTextField21;
    public javax.swing.JTextField jTextField22;
    private javax.swing.JTextField jTextField23;
    private javax.swing.JTextField jTextField24;
    private javax.swing.JTextField jTextField25;
    private javax.swing.JTextField jTextField26;
    private javax.swing.JTextField jTextField27;
    public javax.swing.JTextField jTextField28;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    private javax.swing.JPanel myProfilejPanel23;
    private javax.swing.JPanel stockjPanel33;
    private javax.swing.JPanel summaryjPanel4;
    private javax.swing.JPanel userManagejPanel19;
    // End of variables declaration//GEN-END:variables
}
