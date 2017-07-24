package me.waffle.api.mysql.exceptions;

@SuppressWarnings("serial")
public class NotConnectedException extends Exception {
	public NotConnectedException(String error) {
		super(error);
	}
}
