package pe.edu.galaxy.apps.domain.service;

import org.eclipse.microprofile.reactive.messaging.Message;
import pe.edu.galaxy.apps.model.ZipCode;
import java.util.concurrent.CompletionStage;

public interface ZipCodeService {

    CompletionStage<Void> processZipCode(Message<ZipCode> zipCodeMessage);
}
