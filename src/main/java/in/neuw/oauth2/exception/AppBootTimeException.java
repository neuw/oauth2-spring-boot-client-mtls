package in.neuw.oauth2.exception;

/**
 * @author Karanbir Singh on 07/19/2020
 */
public class AppBootTimeException extends RuntimeException {

    public AppBootTimeException(String message, Exception ex) {
        super(message, ex);
    }

}
