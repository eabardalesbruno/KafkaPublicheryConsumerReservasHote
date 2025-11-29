package pe.edu.galaxy.apps.domain.service.impl;

import io.smallrye.mutiny.Uni;
import io.smallrye.reactive.messaging.kafka.api.OutgoingKafkaRecordMetadata;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.jboss.logging.Logger;
import pe.edu.galaxy.apps.api.dto.request.ZipCodeRequest;
import pe.edu.galaxy.apps.api.dto.response.ZipCodeResponse;
import pe.edu.galaxy.apps.domain.service.ZipCodeService;
import pe.edu.galaxy.apps.model.ZipCode;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@ApplicationScoped
public class ZipCodeServiceImpl implements ZipCodeService {

    private final static Logger log = Logger.getLogger(ZipCodeServiceImpl.class.getName());

    @Channel("zip-code-topic")
    Emitter<ZipCode> emitter;

    @Override
    public Uni<ZipCodeResponse> create(ZipCodeRequest request) {
        // Ya no se valida duplicidad en memoria, siempre se permite el registro
        var zipCode = this.toZipCodeAvro(request);
        ZipCodeResponse response = new ZipCodeResponse(
            request.zip(),
            request.city(),
            request.county(),
            request.state(),
            request.timezone(),
            request.type()
        );
        log.infof("ZipCode creado y enviado a Kafka: %s", toJson(response));
        return Uni.createFrom().voidItem()
                .invoke(() -> {
                    var message = Message.of(zipCode)
                            .addMetadata(
                                    OutgoingKafkaRecordMetadata.builder()
                                            .withKey(UUID.randomUUID().toString())
                                            .withHeaders(new RecordHeaders()
                                                    .add("endpointType", "TYPE-2".getBytes(StandardCharsets.UTF_8)))
                                            .build()
                            );
                    emitter.send(message);
                })
                .replaceWith(response);
    }

    private ZipCode toZipCodeAvro(ZipCodeRequest request) {
        return new ZipCode(
                request.zip(),
                request.city(),
                request.county(),
                request.state(),
                request.timezone(),
                request.type()
        );
    }

    // Utilidad para convertir a JSON
    private static String toJson(Object obj) {
        try {
            return new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            return obj.toString();
        }
    }
}
