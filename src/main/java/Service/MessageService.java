package Service;

import java.util.List;

import DAO.AccountDAO;
import DAO.MessageDAO;
import Exceptions.InvalidMessageException;
import Model.Message;

public class MessageService {
    MessageDAO messageDAO;
    AccountDAO accountDAO;

    public MessageService(){
        this.messageDAO = new MessageDAO();
        this.accountDAO = new AccountDAO();
    }

    public Message submitNewMessage(Message message)throws InvalidMessageException{

        if(message.message_text.isBlank()){
            throw new InvalidMessageException("The message is blank");
        }
        else if(message.message_text.length() > 255){
            throw new InvalidMessageException("The message has more than 255 characters");
        }
        else if(!accountDAO.isRealUser(message.posted_by)){
            throw new InvalidMessageException("The message does not have a valid user");
        }        
        return messageDAO.postMessage(message);
    }

    public List<Message> getAllMessages(){
        return messageDAO.getAllMessages();
    }

    public Message getMessageById(int id)throws InvalidMessageException{        
        return messageDAO.getMessageById(id);     
    }
    
    public Message deleteMessageById(int id) throws InvalidMessageException{
        return messageDAO.deleteMessageById(id);
    }

    public Message updateMessage(int id, Message newMessage)throws InvalidMessageException{
        if(newMessage.message_text.isBlank()){
            throw new InvalidMessageException("Message is blank");
        }        
        if(newMessage.message_text.length() > 255){
            throw new InvalidMessageException("The message has more than 255 characters");
        }
        messageDAO.getMessageById(id);
        return messageDAO.updateMessage(id,newMessage);     
    }

    public List<Message> retrieveAllMessagesFromAccount(int account_id){
        return messageDAO.retrieveAllMessagesFromAccount(account_id);
    }
}
