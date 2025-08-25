package co.com.pragma.usecase.user.exceptions;

public class DocumentAlreadyExists extends RuntimeException{
    public DocumentAlreadyExists(String message){
        super(message);
    }
}
