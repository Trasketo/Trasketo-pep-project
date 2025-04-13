package Service;

import DAO.AccountDAO;
import Exceptions.InvalidAccountException;
import Model.Account;


public class AccountService{

    private AccountDAO accountDAO;

    public AccountService(){
        this.accountDAO = new AccountDAO();
    }

    public Account register(Account account) throws InvalidAccountException{
        /**
        * Check if account critereas are met 
        * username is not blank
        * password is at least 4 characters long
        * and an Account with that username does not already exist
        */
        if(account.getUsername() == null|| account.getUsername().trim().isEmpty()){
            throw new InvalidAccountException("Username cannot be blank");
        }
        if(account.getPassword() == null || account.getPassword().length() < 4){
            throw new InvalidAccountException("Password must be at least 4 characters long");
        }
        if(!accountDAO.isAccountNameAvailabe(account.getUsername())){
            throw new InvalidAccountException("Username already exists");
        }
        return accountDAO.createNewUser(account);
    }

    public Account login(Account account) throws InvalidAccountException{
        if(accountDAO.isAccountNameAvailabe(account.getUsername())){
            throw new InvalidAccountException("Invalid User");
        }
        else if(accountDAO.confirmAccountCredentials(account.username, account.password) == null){
            throw new InvalidAccountException("Invalid Password");
        }
        return accountDAO.confirmAccountCredentials(account.username, account.password);
    }
}