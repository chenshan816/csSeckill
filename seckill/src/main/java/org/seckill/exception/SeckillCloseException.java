package main.java.org.seckill.exception;
/**
 * ��ɱ�ر��쳣
 * @author dell
 *
 */
public class SeckillCloseException extends ServiceException{

	public SeckillCloseException(String message, Throwable cause) {
		super(message, cause);
	}

	public SeckillCloseException(String message) {
		super(message);
	}

}
