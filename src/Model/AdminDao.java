/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Duong Minh Dat
 */
public class AdminDao {
    Connection con = MyConnection.getConnection();
    PreparedStatement ps ;
    Statement st;
    ResultSet rs;
    
    public int getMaxRowAdminTable(){
        int row = 0;
        
        try {
            st = con.createStatement();
            rs = st.executeQuery("select max(id) from admin");

            while (rs.next()) {
                row = rs.getInt(1);
            }
        } catch (Exception ex) {
            Logger.getLogger(AdminDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return row + 1;
    }
    public boolean isAdminNameExist(String username){
        try {
            ps = con.prepareStatement("select * from admin where username = ?");
            ps.setString(1, username);
            rs = ps.executeQuery();
            if(rs.next()){
                return true;
            }
        } catch (Exception ex) {
            Logger.getLogger(AdminDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }
    
    public boolean insert(Admin admin){
        String sql = "insert into admin (id, username, password , s_ques , ans) values (?,?,?,?,?)";
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, admin.getId());
            ps.setString(2, admin.getUsername());
            ps.setString(3, admin.getPassword());
            ps.setString(4, admin.getsQues());
            ps.setString(5, admin.getAns());
            
            
            return ps.executeUpdate() > 0 ;
            
        } catch (Exception ex) {
            return false;
        }
    }
    
}
