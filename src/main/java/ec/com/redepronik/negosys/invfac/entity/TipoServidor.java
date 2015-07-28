package ec.com.redepronik.negosys.invfac.entity;

public enum TipoServidor {

	GM("smtp.gmail.com", "true", "587", "true"), HM("smtp.live.com", "true",
			"25", "true");

	private final String mail_smtp_host;
	private final String mail_smtp_auth;
	private final String mail_smtp_port;
	private final String mail_smtp_starttls_enable;

	private TipoServidor(String mail_smtp_host, String mail_smtp_auth,
			String mail_smtp_port, String mail_smtp_starttls_enable) {
		this.mail_smtp_host = mail_smtp_host;
		this.mail_smtp_auth = mail_smtp_auth;
		this.mail_smtp_port = mail_smtp_port;
		this.mail_smtp_starttls_enable = mail_smtp_starttls_enable;
	}

	public String getMail_smtp_host() {
		return mail_smtp_host;
	}

	public String getMail_smtp_auth() {
		return mail_smtp_auth;
	}

	public String getMail_smtp_port() {
		return mail_smtp_port;
	}

	public String getMail_smtp_starttls_enable() {
		return mail_smtp_starttls_enable;
	}

}
