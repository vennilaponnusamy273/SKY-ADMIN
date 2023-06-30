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
}
