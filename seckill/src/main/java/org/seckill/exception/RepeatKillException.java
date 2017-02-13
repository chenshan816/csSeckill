package main.java.org.seckill.exception;
/**
 * ÷ÿ∏¥√Î…±“Ï≥£
 * @author dell
 *
 */
public class RepeatKillException extends ServiceException{

	public RepeatKillException(String message) {
		super(message);
	}

	public RepeatKillException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
