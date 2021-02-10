package in.nit.util;

import javax.mail.internet.MimeMessage;

import org.apache.commons.codec.CharEncoding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
@Component
public class EmailUtil {
	
	@Autowired
	private JavaMailSender sender;
	
	/**
	 * Actual logic
	 * @param to
	 * @param subject
	 * @param text
	 * @return
	 */
	public boolean send(String to,
						String subject,
						String text,
						String cc[],
						String bcc[],
						MultipartFile file) {
		
		boolean sent=false;
		MimeMessage message=null;
		MimeMessageHelper hepler=null;
		
		try {
			//1.Create Mime Message Object
			message=sender.createMimeMessage();
			
			//2.Use hepler class object
			hepler=new MimeMessageHelper(message);
			
			//3.set details to object
			hepler.setTo(to);
			hepler.setSubject(subject);
			hepler.setText(text);
			if(cc!=null && cc.length>0)
				hepler.setCc(cc);
			if(bcc!=null && bcc.length>0)
				hepler.setBcc(bcc);
			
			if(file!=null) {
				hepler.addAttachment(file.getOriginalFilename(), file);
			}
			//4.send mail
			sender.send(message);
			sent=true;
		} catch (Exception e) {
			sent=false;
			e.printStackTrace();
		}
		
		return sent;
	}
	
	/**
	 * Overloaded method
	 * @param to
	 * @param subject
	 * @param text
	 * @return
	 */
	public boolean send(String to,
						String subject,
						String text) {
		return send(to,subject,text,null,null,null);
	}

	/**
	 * Overloaded method
	 * @param to
	 * @param subject
	 * @param text
	 * @param file
	 * @return
	 */
	public boolean send(
			String to,
			String subject,
			String text,
			MultipartFile file)
	{
		return send(to, subject, text, null, null, file);
	}
}
