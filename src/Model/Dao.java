package Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Dao {

    Connection con = MyConnection.getConnection();
    PreparedStatement ps;
    Statement st;
    ResultSet rs;

    public int insertProduct1(Product p) {
        int rowsAffected = 0;
        PreparedStatement ps = null;
        Connection con = null;

        try {
            // Kết nối tới cơ sở dữ liệu
            con = MyConnection.getConnection();

            // SQL Query
            String sql = "INSERT INTO product(name, price, image) VALUES (?, ?, ?)";
            System.out.println(sql);

            // Create PreparedStatement object
            ps = con.prepareStatement(sql);
            ps.setString(1, p.getName());
            ps.setDouble(2, p.getPrice());
            ps.setBytes(3, p.getImage());

            // Execute query  
            rowsAffected = ps.executeUpdate();

            // Check result
            if (rowsAffected > 0) {
                System.out.println("You executed query: " + sql);
                System.out.println("Product inserted successfully");
            } else {
                System.out.println("You executed query: " + sql);
                System.out.println("No rows affected");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Đảm bảo đóng các tài nguyên đúng cách
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    MyConnection.closeConnection(con);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return rowsAffected;
    }

    public ArrayList<Product> getAllProduct() {
        ArrayList<Product> danhsachHoa = new ArrayList<>();
        PreparedStatement ps = null;
        Connection con = null;

        try {
            // Kết nối cơ sở dữ liệu
            con = MyConnection.getConnection();
            ps = con.prepareStatement("SELECT * FROM product");
            rs = ps.executeQuery();

            while (rs.next()) {
                Product p = new Product();
                p.setId(rs.getInt("id"));
                p.setName(rs.getString("name"));
                p.setPrice(rs.getDouble("price"));
                p.setImage(rs.getBytes("image"));
                danhsachHoa.add(p);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Đảm bảo đóng các tài nguyên đúng cách
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    MyConnection.closeConnection(con);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return danhsachHoa;
    }

    public void getallProduct(JTable table) {
        String sql = "SELECT * FROM product ORDER BY id DESC";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = MyConnection.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.setRowCount(0); // Clear existing rows

            while (rs.next()) {
                Object[] row = new Object[4];
                row[0] = rs.getInt("id");
                row[1] = rs.getString("name");
                row[2] = rs.getDouble("price");
                row[3] = rs.getBytes("image"); // Store image bytes

                model.addRow(row);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    MyConnection.closeConnection(con);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean update(Product product) {
        String sql = "UPDATE product SET name = ?, price = ? WHERE id = ?";
        PreparedStatement ps = null;
        Connection con = null;

        try {
            con = MyConnection.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, product.getName());
            ps.setDouble(2, product.getPrice());
            ps.setInt(3, product.getId());
            return ps.executeUpdate() > 0;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    MyConnection.closeConnection(con);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean delete(Product product) {
        try {
            ps = con.prepareStatement("delete from product where id = ? ");
            ps.setInt(1, product.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            return false;
        }

    }

    public int getMaxRowAOrderTable() {
        int row = 0;

        try {
            st = con.createStatement();
            rs = st.executeQuery("select max(cid) from cart");

            while (rs.next()) {
                row = rs.getInt(1);
            }
        } catch (Exception ex) {
            Logger.getLogger(Dao.class.getName()).log(Level.SEVERE, null, ex);
        }

        return row + 1;
    }

    public boolean isProductExist(int cid, int pid) {
        try {
            ps = con.prepareStatement("select * from cart where cid = ? and pid = ?");
            ps.setInt(1, cid);
            ps.setInt(2, pid);
            rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (Exception ex) {
            Logger.getLogger(Dao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean insertCart(Cart cart) {
        String sql = "insert into cart (cid,pid,pName,qty,price,total) values (?,?,?,?,?,?)";
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, cart.getId());
            ps.setInt(2, cart.getPid());
            ps.setString(3, cart.getpName());
            ps.setInt(4, cart.getQty());
            ps.setDouble(5, cart.getPrice());
            ps.setDouble(6, cart.getTotal());
            return ps.executeUpdate() > 0;
        } catch (Exception ex) {
            return false;
        }
    }

    public int getMaxRowAPaymentTable() {
        int row = 0;

        try {
            st = con.createStatement();
            rs = st.executeQuery("select max(pid) from payment");

            while (rs.next()) {
                row = rs.getInt(1);
            }
        } catch (Exception ex) {
            Logger.getLogger(Dao.class.getName()).log(Level.SEVERE, null, ex);
        }

        return row + 1;
    }

    public int getMaxRowACartTable() {
        int row = 0;

        try {
            st = con.createStatement();
            rs = st.executeQuery("select max(cid) from cart");

            while (rs.next()) {
                row = rs.getInt(1);
            }
        } catch (Exception ex) {
            Logger.getLogger(Dao.class.getName()).log(Level.SEVERE, null, ex);
        }

        return row;
    }

    public double subTotal() {
        double subTotal = 0.0;
        int cid = getMaxRowACartTable();

        try {
            st = con.createStatement();
            rs = st.executeQuery("select sum(total) as total from cart where cid = '" + cid + "'");

            if (rs.next()) {
                subTotal = rs.getDouble(1);
            }
        } catch (Exception ex) {
            Logger.getLogger(Dao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return subTotal;
    }

//    public void getProductsFromCart(JTable table) {
//        int cid = getMaxRowACartTable();
//        String sql = "SELECT * FROM cart where cid = ?";
//        try {
//            ps = con.prepareStatement(sql);
//            ps.setInt(1, cid);
//            rs = ps.executeQuery();
//
//            DefaultTableModel model = (DefaultTableModel) table.getModel();
//
//            Object[] row;
//
//            while (rs.next()) {
//                row = new Object[6];
//                row[0] = rs.getInt(1);
//                row[1] = rs.getInt(2);
//                row[2] = rs.getString(3);
//                row[3] = rs.getInt(4);
//                row[4] = rs.getDouble(5);
//                row[5] = rs.getDouble(6);
//                model.addRow(row);
//            }
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }
    public void getProductsFromCart(JTable table) {
        int cid = getMaxRowACartTable();
        String sql = "SELECT * FROM cart where cid = ?";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = MyConnection.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, cid);
            rs = ps.executeQuery();

            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.setRowCount(0); // Clear existing rows

            while (rs.next()) {
                Object[] row = new Object[6];
                row[0] = rs.getInt(1);
                row[1] = rs.getInt(2);
                row[2] = rs.getString(3);
                row[3] = rs.getInt(4);
                row[4] = rs.getDouble(5);
                row[5] = rs.getDouble(6);
                model.addRow(row);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    MyConnection.closeConnection(con);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean Payment(Payment payment) {
        String sql = "insert into payment (pid,cName,proid,pName,total,pdate) values (?,?,?,?,?,?)";
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, payment.getpId());
            ps.setString(2, payment.getcName());
            ps.setString(3, payment.getProId());
            ps.setString(4, payment.getProName());
            ps.setDouble(5, payment.getTotal());
            ps.setString(6, payment.getDate());
            return ps.executeUpdate() > 0;
        } catch (Exception ex) {
            return false;
        }
    }

    public boolean deleteCart(int cid) {
        try {
            ps = con.prepareStatement("delete from cart where cid = ? ");
            ps.setInt(1, cid);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            return false;
        }

    }

    public ArrayList<Payment> getPaymentDetails() {
        ArrayList<Payment> PaymentList = new ArrayList<>();
        PreparedStatement ps = null;
        Connection con = null;

        try {
            // Kết nối cơ sở dữ liệu
            con = MyConnection.getConnection();
            ps = con.prepareStatement("SELECT * FROM payment");
            rs = ps.executeQuery();

            while (rs.next()) {
                Payment p = new Payment();
                p.setpId(rs.getInt("pid"));
                p.setcName(rs.getString("cName"));
                p.setProId(rs.getString("proid"));
                p.setProName(rs.getString("pName"));
                p.setTotal(rs.getDouble("total"));

                PaymentList.add(p);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Đảm bảo đóng các tài nguyên đúng cách
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    MyConnection.closeConnection(con);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return PaymentList;
    }

    public int totalProduct() {
        int total = 0;
        try {
            st = con.createStatement();
            rs = st.executeQuery("SELECT COUNT(*) AS 'total' FROM product");

            if (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (Exception ex) {
            Logger.getLogger(Dao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return total;
    }

    public double todayRevenue(String date) {
        double total = 0.0;
        try {
            st = con.createStatement();
            rs = st.executeQuery("SELECT SUM(total) AS 'total' FROM payment where pdate '" + date + "'");

            if (rs.next()) {
                total = rs.getDouble(1);
            }
        } catch (Exception ex) {
            Logger.getLogger(Dao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return total;
    }

    public double todayRevenue() {
        double total = 0.0;
        try {
            st = con.createStatement();
            rs = st.executeQuery("SELECT SUM(total) AS 'total' FROM payment ");

            if (rs.next()) {
                total = rs.getDouble(1);
            }
        } catch (Exception ex) {
            Logger.getLogger(Dao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return total;
    }
}
