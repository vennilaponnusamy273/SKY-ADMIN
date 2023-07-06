package in.codifi.api.config;

import javax.inject.Singleton;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Singleton
public class ApplicationProperties {

	
	@ConfigProperty(name = "appconfig.file.basepath")
	String fileBasePath;
	@ConfigProperty(name = "appconfig.razorpay.ifsc")
	String razorpayIfscUrl;
	
	@ConfigProperty(name = "appconfig.mail.password")
	String mailPassword;
	@ConfigProperty(name = "appconfig.mail.from")
	String mailFrom;
	@ConfigProperty(name = "appconfig.mail.port")
	String mailPort;
	@ConfigProperty(name = "appconfig.mail.username")
	String mailUserName;
	@ConfigProperty(name = "appconfig.mail.host")
	String mailHost;
}
