package pe.edu.galaxy.apps.domain.service.impl;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.jboss.logging.Logger;
import pe.edu.galaxy.apps.data.model.entites.ZipCodeEntity;
import pe.edu.galaxy.apps.data.repositories.ZipCodeRepository;
import pe.edu.galaxy.apps.domain.service.ZipCodeService;
import pe.edu.galaxy.apps.model.ZipCode;
import java.util.concurrent.CompletionStage;

@ApplicationScoped
public class ZipCodeServiceImpl implements ZipCodeService {

    private final static Logger log = Logger.getLogger(ZipCodeServiceImpl.class.getName());

    @Inject
    ZipCodeRepository zipCodeRepository;

    @Override
    @Incoming("zip-code")
    public CompletionStage<Void> processZipCode(Message<ZipCode> zipCodeMessage) {
        var zipCode = zipCodeMessage.getPayload();

        return Uni.createFrom()
                .deferred(() ->
                        Panache.withTransaction(() ->
                                zipCodeRepository
                                        .findById(zipCode.getZip() != null ? zipCode.getZip().toString() : null)
                                        .onItem().ifNotNull().invoke(() -> {
                                            log.infof("ZipCode already exists %s", zipCode.getZip());
                                        })
                                        .onItem().ifNull().switchTo(() -> {
                                            var entity = zipCodeRepository.persist(this.toEntity(zipCode));
                                            log.infof("ZipCode inserted: %s", zipCode.getZip());
                                            return entity;
                                        })
                        )
                )
                .onFailure().invoke(err -> {
                    log.errorf("Error get event %s: %s", zipCode.getZip(), err.getMessage());
                })
                .onItem().transformToUni(v ->
                        Uni.createFrom().completionStage(zipCodeMessage.ack())
                )
                .onFailure().recoverWithUni(err ->
                        Uni.createFrom().completionStage(zipCodeMessage.nack(err))
                )
                .replaceWithVoid()
                .subscribeAsCompletionStage();
    }

    private ZipCodeEntity toEntity(ZipCode zipCode) {
        return new ZipCodeEntity(
                zipCode.getZip() != null ? zipCode.getZip().toString() : null,
                zipCode.getCity() != null ? zipCode.getCity().toString() : null,
                zipCode.getCounty() != null ? zipCode.getCounty().toString() : null,
                zipCode.getState() != null ? zipCode.getState().toString() : null,
                zipCode.getTimezone() != null ? zipCode.getTimezone().toString() : null,
                zipCode.getType() != null ? zipCode.getType().toString() : null
        );
    }
}
