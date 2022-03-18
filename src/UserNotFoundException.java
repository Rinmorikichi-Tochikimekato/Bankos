public class UserNotFoundException extends RuntimeException{
    UserNotFoundException(String exceptionMessage){
        super(exceptionMessage);
    }
}
