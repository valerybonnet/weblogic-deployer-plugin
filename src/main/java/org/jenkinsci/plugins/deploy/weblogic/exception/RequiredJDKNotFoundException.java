/**
 * 
 */
package org.jenkinsci.plugins.deploy.weblogic.exception;

/**
 * @author rchaumie
 *
 */
public class RequiredJDKNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6864231926686153095L;
	
	/**
	 * 
	 */
	public RequiredJDKNotFoundException() {
		super();
	}
	
	/**
	 * 
	 * @param message
	 * @param cause
	 */
	public RequiredJDKNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * 
	 * @param message
	 */
	public RequiredJDKNotFoundException(String message) {
		super(message);
	}

	/**
	 * 
	 * @param cause
	 */
	public RequiredJDKNotFoundException(Throwable cause) {
		super(cause);
	}
	
	

}
