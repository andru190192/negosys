package ec.com.redepronik.negosys.utils;

import static ec.com.redepronik.negosys.utils.UtilsMath.parametro;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.sun.mail.smtp.SMTPTransport;

import ec.com.redepronik.negosys.invfac.entity.TipoServidor;
import ec.com.redepronik.negosys.rrhh.entity.Persona;

public class UtilsMail {

	public static boolean enviarCorreo(String nombreEstablecimiento, Persona p,
			String numDocumento, String fechaEmision, File xml, File pdf) {
		try {
			String asunto = "FACTURA EMITIDA POR " + nombreEstablecimiento;
			String detalle = "Estimado(a)"
					+ "\n\n"
					+ p.getApellido()
					+ " "
					+ p.getNombre()
					+ ","
					+ "\n\nReciba un atento saludo de parte de "
					+ nombreEstablecimiento
					+ "."
					+ "\n\nEl presente correo es para notificarle la generacion de su documento electronico el mismo que se encuentra disponible en "
					+ "nuestro Portal de Documentos Electronicos e-comprobantes.supernisho.com.ec y en el Portal del SRI para su consulta y descarga."
					+ "\n\nDocumento numero: "
					+ numDocumento
					+ " a su nombre con fecha "
					+ fechaEmision
					+ "\n\nRECUERDE QUE LA PRIMERA VEZ SU USUARIO Y LA CONTRASEÑA ES SU CÉDULA O RUC"
					+ "\n\n\nCON EL APOYO TECNOLOGICO DE REDEPRONIK SYSTEM WWW.REDEPRONIK.COM.EC";

			List<File> listAdjunto = new ArrayList<File>();
			listAdjunto.add(xml);
			listAdjunto.add(pdf);

			envioCorreo(asunto, detalle, p.getEmail(), listAdjunto);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private static void envioCorreo(String asunto, String detalle,
			String destinatario, List<File> listAdjunto) throws Exception {

		TipoServidor ts = parametro.getTipoServidor();
		Properties properties = new Properties();
		properties.put("mail.smtp.host", ts.getMail_smtp_host());
		properties.put("mail.smtp.auth", ts.getMail_smtp_auth());
		properties.put("mail.smtp.port", ts.getMail_smtp_port());
		if (ts == TipoServidor.GM)
			properties.put("mail.smtp.ssl.trust", ts.getMail_smtp_host());
		properties.put("mail.smtp.starttls.enable",
				ts.getMail_smtp_starttls_enable());

		Session session = Session.getInstance(properties, null);
		Message mensaje = new MimeMessage(session);
		// agregamos la dirección que envía el email
		mensaje.setFrom(new InternetAddress(parametro.getEmail()));
		try {
			mensaje.addRecipient(Message.RecipientType.TO, new InternetAddress(
					destinatario));
			// Message.RecipientType.TO; para
			// Message.RecipientType.CC; con copia
			// Message.RecipientType.BCC; con copia oculta
		} catch (Exception e) {
			e.printStackTrace();
		}

		mensaje.setSubject(asunto);
		mensaje.setSentDate(new Date());
		Multipart multipart = new MimeMultipart();
		BodyPart messageBodyPart = new MimeBodyPart();
		messageBodyPart.setText(detalle);
		multipart.addBodyPart(messageBodyPart);
		for (File adjunto : listAdjunto) {
			messageBodyPart = new MimeBodyPart();
			messageBodyPart.setDataHandler(new DataHandler(new FileDataSource(
					adjunto)));
			messageBodyPart.setFileName(adjunto.getName());
			multipart.addBodyPart(messageBodyPart);
		}

		mensaje.setContent(multipart);
		System.clearProperty("javax.net.ssl.keyStore");
		System.clearProperty("javax.net.ssl.keyStorePassword");
		System.clearProperty("javax.net.ssl.trustStore");
		System.clearProperty("javax.net.ssl.trustStorePassword");

		SMTPTransport transport = (SMTPTransport) session.getTransport("smtp");
		try {
			transport.connect(parametro.getEmail(), parametro.getPassEmail());
			transport.sendMessage(mensaje, mensaje.getAllRecipients());
		} finally {
			transport.close();
		}
	}
}
