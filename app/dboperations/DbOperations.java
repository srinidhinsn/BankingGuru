package dboperations;

import vo.Customer;
import vo.TransactionDetails;

import java.sql.*;
import java.util.*;

public class DbOperations {
    public static void main(String args[]){
        String jdbcUrl = "jdbc:postgresql://localhost:5432/postgres";
        String username = "postgres";
        String password = "admin";

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            // Step 1 - Load driver
            // Class.forName("org.postgresql.Driver"); // Class.forName() is not needed since JDBC 4.0

            // Step 2 - Open connection
            conn = DriverManager.getConnection(jdbcUrl, username, password);

            // Step 3 - Execute statement
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT version()");

            // Step 4 - Get result
            if (rs.next()) {
                System.out.println(rs.getString(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {

                // Step 5 Close connection
                if (stmt != null) {
                    stmt.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
    }
}

    public Connection getConnection(){
        String jdbcUrl = "jdbc:postgresql://localhost:5432/postgres";
        String username = "postgres";
        String password = "admin";

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            // Step 1 - Load driver
            // Class.forName("org.postgresql.Driver"); // Class.forName() is not needed since JDBC 4.0

            // Step 2 - Open connection
            conn = DriverManager.getConnection(jdbcUrl, username, password);

            // Step 3 - Execute statement
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT version()");

            // Step 4 - Get result
            if (rs.next()) {
                System.out.println(rs.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public List<Map<String, Object>> getBanks(String query, Map<String, String> params){
        Connection con = getConnection();
        try {
            StringBuffer queryBuffer = null;
            Statement stat = con.createStatement();
            if (null != params && !params.isEmpty()){
                queryBuffer = new StringBuffer(" where ");
                Set<String> keys = params.keySet();
                Iterator<String> i = keys.iterator();
                while(i.hasNext()){
                    String key = i.next();
                    queryBuffer.append(key).append("=").append("'").append(
                            params.get(key)).append("'");
                    if (i.hasNext()){
                        queryBuffer.append(" and ");
                    }
                }
            }
            if (null!= queryBuffer){
                query = query + queryBuffer.toString();
            }
            ResultSet rs = stat.executeQuery(query);
            return resultSetToArrayList(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Map<String, Object>> resultSetToArrayList(ResultSet rs) throws SQLException{
        ResultSetMetaData md = rs.getMetaData();
        int columns = md.getColumnCount();
        ArrayList list = new ArrayList(50);
        while (rs.next()){
            LinkedHashMap<String, Object> row = new LinkedHashMap<String, Object>(columns);
            for(int i=1; i<=columns; ++i){
                row.put(md.getColumnName(i),rs.getObject(i));
            }
            list.add(row);
        }

        return list;
    }


    public Customer authenticateCustomer(String username, String password, String bankname) {
        Customer customer = null;
        Connection con = getConnection();
        try {
            StringBuffer queryBuffer = null;
            PreparedStatement statement = con.prepareStatement(DBConstants.authenticateCustomer);
            statement.setString(1,username);
            statement.setString(2,password);
            statement.setString(3,bankname);
            statement.execute();
            ResultSet rs = statement.getResultSet();
            while(rs.next()){
                customer = new Customer();
                customer.setFirstName(rs.getString("firstname"));
                customer.setLastName(rs.getString("lastname"));
                customer.setAccountNo(rs.getString("accountno"));
                customer.setIfsccode(rs.getString("ifsccode"));
                customer.setBankname(rs.getString("bankname"));
                customer.setUserid(rs.getString("userid"));
                customer.setAdharno(rs.getString("adharno"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customer;
    }

    public Customer getCustomer(String accountNo, String ifsccode) {
        Customer customer = null;
        Connection con = getConnection();
        try {
            StringBuffer queryBuffer = null;
            PreparedStatement statement = con.prepareStatement(DBConstants.getCustomer);
            statement.setString(1,accountNo);
            statement.setString(2,ifsccode);
            statement.execute();
            ResultSet rs = statement.getResultSet();
            while(rs.next()){
                customer = new Customer();
                customer.setFirstName(rs.getString("firstname"));
                customer.setLastName(rs.getString("lastname"));
                customer.setAccountNo(rs.getString("accountno"));
                customer.setIfsccode(rs.getString("ifsccode"));
                customer.setBankname(rs.getString("bankname"));
                customer.setUserid(rs.getString("userid"));
                customer.setAdharno(rs.getString("adharno"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customer;
    }

    public List<TransactionDetails> getTransactions (Customer customer){
        List<TransactionDetails> transactionDetailsList = new ArrayList<>();
        Connection con = getConnection();
        try{
            PreparedStatement statement = con.prepareStatement(DBConstants.getTransactionDetails);
            statement.setString(1,customer.getAccountNo());
            statement.setString(2,customer.getIfsccode());
            statement.execute();
            ResultSet rs = statement.getResultSet();
            while (rs.next()){
                TransactionDetails td = new TransactionDetails();
                td.setAccountNo(rs.getString("accountno"));
                td.setAmount(rs.getDouble("amount"));
                td.setBalance(rs.getDouble("balance"));
                td.setDateTime(rs.getTimestamp ("datetime"));
                td.setFrom(rs.getString("from"));
                td.setMaterial(rs.getString("material"));
                td.setType(rs.getString("type"));
                td.setIfsccode(rs.getString("ifsccode"));
                transactionDetailsList.add(td);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return transactionDetailsList;
    }


    public List<TransactionDetails> getAdharLinks (Customer customer){
        List<TransactionDetails> transactionDetailsList = new ArrayList<>();
        Connection con = getConnection();
        try{
            PreparedStatement statement = con.prepareStatement(DBConstants.getAdharLinks);
            statement.setString(1,customer.getAdharno());
            statement.execute();
            ResultSet rs = statement.getResultSet();
            while (rs.next()){
                TransactionDetails td = new TransactionDetails();
                td.setAccountNo(rs.getString("accountno"));
                td.setFrom(rs.getString("branch"));
                td.setType(rs.getString("name"));
                td.setIfsccode(rs.getString("ifsc"));
                transactionDetailsList.add(td);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return transactionDetailsList;
    }


    public void insertTransaction(TransactionDetails td) {

        Connection con = getConnection();
        try {
            PreparedStatement statement = con.prepareStatement(DBConstants.insertTransactionDetails);
            statement.setString(1, td.getAccountNo());
            statement.setString(2, td.getType());
            statement.setString(3, td.getMaterial());
            statement.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            statement.setString(5, td.getFrom());
            statement.setString(6, td.getIfsccode());
            statement.setDouble(7, td.getAmount());
            statement.setDouble(8, td.getBalance());
            statement.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<TransactionDetails> getTransactionChart (Customer customer){
        List<TransactionDetails> transactionDetailsList = new ArrayList<>();
        Connection con = getConnection();
        try{
            PreparedStatement statement = con.prepareStatement(DBConstants.getTransactionChart);
            statement.setString(1,customer.getAccountNo());
            statement.setString(2,customer.getIfsccode());
            statement.execute();
            ResultSet rs = statement.getResultSet();
            TransactionDetails td = new TransactionDetails();
            while (rs.next()){
                if ("Credit".equalsIgnoreCase(rs.getString("type"))){
                    td.setType(rs.getString("mon"));
                    td.setAmount(rs.getDouble("sum"));
                } else {
                    td.setMaterial(rs.getString("mon"));
                    td.setBalance(rs.getDouble("sum"));
                }
            }
            transactionDetailsList.add(td);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return transactionDetailsList;
    }
}
