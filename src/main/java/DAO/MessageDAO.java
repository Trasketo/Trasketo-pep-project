package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Exceptions.InvalidMessageException;
import Model.Message;
import Util.ConnectionUtil;

public class MessageDAO {
    
    public MessageDAO(){

    }

    public Message postMessage(Message message)throws InvalidMessageException{
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "INSERT INTO Message (posted_by,message_text,time_posted_epoch) VALUES (?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, message.posted_by);
            preparedStatement.setString(2, message.message_text);
            preparedStatement.setLong(3, message.time_posted_epoch);                       

            preparedStatement.executeUpdate();
            ResultSet pkResultSet = preparedStatement.getGeneratedKeys();
            if(pkResultSet.next()){
                int message_id = (int)pkResultSet.getLong("message_id");
                return new Message(message_id,message.posted_by,message.message_text,message.time_posted_epoch);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        throw new InvalidMessageException("Failed to post Message");
    }

    public List<Message> getAllMessages(){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try{
            String sql = "SELECT * FROM Message";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message message = new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch"));
                messages.add(message);    
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }
    
    public Message getMessageById(int id)throws InvalidMessageException{
        Connection connection = ConnectionUtil.getConnection();       
        try{
            String sql = "SELECT * FROM Message WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                return new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch"));
                   
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        throw new InvalidMessageException("No message Found");
    }

    public Message deleteMessageById(int id)throws InvalidMessageException{
        Connection connection = ConnectionUtil.getConnection();         
        try{
            Message deletedMessage = getMessageById(id);
            String sql = "DELETE FROM Message WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            return deletedMessage;           
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        throw new InvalidMessageException("0 Message deleted");
    }

    public Message updateMessage (int id, Message newMessage){
        Connection connection = ConnectionUtil.getConnection();         
        try{            
            String sql = "UPDATE Message SET message_text = ? where message_id = ? ";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, newMessage.message_text);
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();
            return getMessageById(id);
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        throw new InvalidMessageException("0 Message updated");
    }

    public List<Message> retrieveAllMessagesFromAccount(int account_id){
        Connection connection = ConnectionUtil.getConnection();       
        try{
            String sql = "SELECT * FROM Message WHERE posted_by = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, account_id);
            ResultSet rs = preparedStatement.executeQuery();
            List<Message> messages = new ArrayList<>();
            while(rs.next()){
                Message message = new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch"));
                   messages.add(message);
            }
            return messages;
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        throw new InvalidMessageException("No message Found");
    }
}