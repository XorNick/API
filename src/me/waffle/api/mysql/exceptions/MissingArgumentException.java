package me.waffle.api.mysql.exceptions;

@SuppressWarnings("serial")
public class MissingArgumentException extends Exception {
	public MissingArgumentException(String error) {
		super(error);
	}
}
