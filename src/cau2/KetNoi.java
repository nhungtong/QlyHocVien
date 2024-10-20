/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cau2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author DELL
 */
public class KetNoi {
     private Connection con;

    public KetNoi() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");// nap trinh dieu khien
        this.con = DriverManager.getConnection("jdbc:mysql://localhost:3306/qlhocvien", "root", ""); // thuc hien ket noi
    }

    public ResultSet GetData(String tbName) throws SQLException {
        ResultSet kq = null;
        Statement statement = this.con.createStatement();
        String sql = "select * from " + tbName; 
        kq = statement.executeQuery(sql);
        return kq;
    }
    public void Themhv(String maHv, String tenHv, String queQuan, Float diemThi) throws SQLException {
        String sqlCheck = "SELECT COUNT(*) FROM hocvien WHERE maHv = ?";
        try (PreparedStatement stmtCheck = this.con.prepareStatement(sqlCheck)) {
            stmtCheck.setString(1, maHv);
            ResultSet rs = stmtCheck.executeQuery();
            rs.next(); 

            int rowCount = rs.getInt(1); // Lấy giá trị của cột COUNT(*)

            // Nếu rowCount > 0, có nghĩa là học viên đã tồn tại
            if (rowCount > 0) {
                JOptionPane.showMessageDialog(null, "Thêm không thành công, mã học viên đã tồn tại", "Lỗi", JOptionPane.ERROR_MESSAGE);
            } else {
                // Nếu rowCount <= 0, có nghĩa là học viên chưa tồn tại, thêm mới
                String sqlInsert = "INSERT INTO hocvien (maHv, tenHv, queQuan, diemThi) VALUES (?, ?, ?, ?)";
                try (PreparedStatement stmtInsert = this.con.prepareStatement(sqlInsert)) {
                    stmtInsert.setString(1, maHv);
                    stmtInsert.setString(2, tenHv);
                    stmtInsert.setString(3, queQuan);
                    stmtInsert.setFloat(4, diemThi);
                    int rowsAffected = stmtInsert.executeUpdate();

                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(null, "Thêm thành công", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Thêm không thành công", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(KetNoi.class.getName()).log(Level.SEVERE, "Xảy ra ngoại lệ SQL", ex);
        }

    }

    public void Suahv(String maHv, String tenHv, String queQuan, float diemThi) throws SQLException {
        String sql1 = "UPDATE hocvien SET tenHv = ?, queQuan = ?, diemThi = ? WHERE maHv = ?";
        try (PreparedStatement sta = this.con.prepareStatement(sql1)) {
            sta.setString(1, tenHv);
            sta.setString(2, queQuan);
            sta.setFloat(3, diemThi);
            sta.setString(4, maHv);
            sta.executeUpdate();
            int rowsUpdated = sta.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(null, "Sửa thành công", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Học viên không tồn tại", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            Logger.getLogger(KetNoi.class.getName()).log(Level.SEVERE, "Xảy ra ngoại lệ SQL", ex);
        }
    }
    public void Deletehv(String maHv) throws SQLException {
        String sql = "DELETE FROM hocvien WHERE maHv = ?";
        try (PreparedStatement sta = this.con.prepareStatement(sql)) {
            sta.setString(1, maHv);
            sta.executeUpdate();
            JOptionPane.showMessageDialog(null, "Xóa thành công", "Thành công", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            Logger.getLogger(KetNoi.class.getName()).log(Level.SEVERE, "Xảy ra ngoại lệ SQL", ex);
        }
    }
    public ResultSet timkiemtheoten(String hoten) throws SQLException
    {
        ResultSet resultSet = null;
        String sql = "SELECT * FROM hocvien WHERE tenHv LIKE ?";
        PreparedStatement preparedStatement = con.prepareStatement(sql);
        preparedStatement.setString(1, "%" + hoten + "%");
        resultSet = preparedStatement.executeQuery();
        return resultSet;
    }
    public void Close() throws SQLException {
        if (this.con != null)
            this.con.close();
    } 
}
