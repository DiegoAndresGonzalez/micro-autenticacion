package co.com.pragma.usecase.user.exceptions;

public class EmailAlreadyExists extends RuntimeException {

    public EmailAlreadyExists(String message){
        super(message);
    }

}
