package main.java.org.seckill.dto;

/**
 * 用于封装所有ajax的json结果
 * @author dell
 *
 * @param <T>
 */
public class SeckillResult<T> {
	
	private boolean success; //判断是否成功
	
	private T data;
	
	private String error;

	public SeckillResult(boolean success, String error) {
		super();
		this.success = success;
		this.error = error;
	}

	public SeckillResult(boolean success, T data) {
		super();
		this.success = success;
		this.data = data;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
	
	
}
