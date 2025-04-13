package DAO;

import Model.Account;
import Util.ConnectionUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Exceptions.InvalidAccountException;



public class AccountDAO {
    
    public AccountDAO(){

    }

    public Account createNewUser(Account account)throws InvalidAccountException{

        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "INSERT INTO Account (username,password) VALUES (?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, account.username);
            preparedStatement.setString(2,account.password);
           
            preparedStatement.executeUpdate();
            ResultSet pkResultSet = preparedStatement.getGeneratedKeys();
            if(pkResultSet.next()){
                int generated_account_id = (int) pkResultSet.getLong(1);
                return new Account(generated_account_id, account.username, account.password); 
            } 
                        
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }     
        throw new InvalidAccountException ("Failed to Register New Account");
    }

    public boolean isAccountNameAvailabe(String name)
    {
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "Select * FROM Account WHERE username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            
            ResultSet rs = preparedStatement.executeQuery();
            return !rs.next();               
            
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return false;
    }
    public Account confirmAccountCredentials(String name, String password)
    {
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "Select * FROM Account WHERE username = ? AND password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, password);            

            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                return new Account(rs.getInt("account_id"),name,password);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public boolean isRealUser(int account_id){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "Select * FROM Account WHERE account_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, account_id);         

            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                return true;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return false;
    }
}
