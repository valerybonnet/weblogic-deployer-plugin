/**
 * 
 */
package org.jenkinsci.plugins.deploy.weblogic.exception;


/**
 * @author rchaumie
 *
 */
public class LoadingFileException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1969850732609489498L;

	/**
	 * 
	 */
	public LoadingFileException(){
		super();
	}
	
	/**
	 * 
	 * @param message
	 * @param cause
	 */
	public LoadingFileException(String message, Throwable cause) {
		super(message, cause);
	}
	
	/**
	 * 
	 * @param message
	 */
	public LoadingFileException(String message) {
		super(message);
	}
	
	/**
	 * 
	 * @param cause
	 */
	public LoadingFileException(Throwable cause) {
		super(cause);
	}
}
