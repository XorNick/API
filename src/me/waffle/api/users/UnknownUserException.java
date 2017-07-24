package me.waffle.api.users;

@SuppressWarnings("serial")
public class UnknownUserException extends Exception {
	public UnknownUserException(String error) {
		super(error);
	}
}
