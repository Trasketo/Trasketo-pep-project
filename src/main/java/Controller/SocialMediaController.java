package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.io.JsonEOFException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Exceptions.InvalidAccountException;
import Exceptions.InvalidMessageException;
import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController(){
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        //app.get("example-endpoint", this::exampleHandler);
        app.post("/register", this::registerHandler);       
        app.post("/login",this::loginHandler);
        app.post("/messages", this::newMessageHandler);
        app.get("/messages",this::getAllMessagesHandler);
        app.get("/messages/{message_id}",this::getMessageByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageByIdHandler);
        app.patch("/messages/{message_id}",this::updateMessageByIdHandler);    
        app.get("/accounts/{account_id}/messages",this::retrieveMessageFromUser);          
        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param ctx The Javalin Context object manages information about both the HTTP request and response.
     */
    /*private void exampleHandler(Context context) {
        context.json("sample text");
    }*/
    
    private void registerHandler(Context ctx) {
        ObjectMapper mapper = new ObjectMapper();
        try{
            Account account = mapper.readValue(ctx.body(),Account.class);
            Account addedAccount = accountService.register(account);
            
            ctx.json(mapper.writeValueAsString(addedAccount));
            ctx.status(200);

        }catch(JsonProcessingException e){
            System.out.println(e.getMessage());
        }catch(InvalidAccountException e){
            ctx.status(400);
            System.out.println(e.getMessage());
        }
    }

    private void loginHandler(Context ctx){
        ObjectMapper mapper = new ObjectMapper();
        try{
            Account account = mapper.readValue(ctx.body(),Account.class);
            Account loggedAccount = accountService.login(account);
            if(loggedAccount != null){
                ctx.json(mapper.writeValueAsString(loggedAccount));
                ctx.status(200);
            }

        }catch(JsonProcessingException e){
            System.out.println(e.getMessage());
        }catch(InvalidAccountException e){
            ctx.status(401);
            System.out.println(e.getMessage());
        }
    }

    private void newMessageHandler(Context ctx){
        ObjectMapper mapper = new ObjectMapper();
        try{
            Message message = mapper.readValue(ctx.body(),Message.class);
            Message postMessage = messageService.submitNewMessage(message);
            ctx.json(mapper.writeValueAsString(postMessage));
            ctx.status(200);
        }catch(JsonProcessingException e){
            
        }
        catch(InvalidMessageException e){
            ctx.status(400);
            System.out.println(e.getMessage());
        }
    }

    private void getAllMessagesHandler(Context ctx){
        ctx.json(messageService.getAllMessages());
        ctx.status(200);
    }

    private void getMessageByIdHandler(Context ctx){
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        ctx.status(200);
        try{
            ctx.json(messageService.getMessageById(messageId));
        }catch(InvalidMessageException e){
            ctx.json("");
        }                  
    }

    private void deleteMessageByIdHandler(Context ctx){
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        ctx.status(200);
        try{
            ctx.json(messageService.deleteMessageById(messageId));
        }catch(InvalidMessageException e){
            ctx.json("");
        }
    }

    private void updateMessageByIdHandler(Context ctx){
        ObjectMapper mapper = new ObjectMapper();
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        
        try{
            Message message = mapper.readValue(ctx.body(),Message.class);
            Message updatedMessage = messageService.updateMessage(message_id,message);
            ctx.json(updatedMessage);
            ctx.status(200);
        }catch(JsonProcessingException e){

        }catch(InvalidMessageException e){
            ctx.status(400);
        }
    }

    private void retrieveMessageFromUser(Context ctx){
        int account_id = Integer.parseInt(ctx.pathParam("account_id"));
        ctx.json(messageService.retrieveAllMessagesFromAccount(account_id));
        ctx.status(200);
    }
}