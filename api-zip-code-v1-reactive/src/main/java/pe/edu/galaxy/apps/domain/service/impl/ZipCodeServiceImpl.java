package pe.edu.galaxy.apps.domain.service.impl;

import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.PersistenceException;
import pe.edu.galaxy.apps.api.dto.request.ZipCodeRequest;
import pe.edu.galaxy.apps.api.dto.response.ZipCodeResponse;
import pe.edu.galaxy.apps.data.model.entites.ZipCodeEntity;
import pe.edu.galaxy.apps.data.repositories.ZipCodeRepository;
import pe.edu.galaxy.apps.domain.service.ZipCodeService;
import java.util.Map;

@ApplicationScoped
public class ZipCodeServiceImpl implements ZipCodeService {

	@Inject
	ZipCodeRepository zipCodeRepository;

	@Override
	@WithTransaction
	public Uni<Map.Entry<ZipCodeResponse, Boolean>> create(ZipCodeRequest request) {
		return zipCodeRepository
				.findById(request.zip())
				.onItem().ifNotNull().transform(existing ->
						Map.entry(toResponse(existing), Boolean.FALSE))
				.onItem().ifNull().switchTo(() ->
						zipCodeRepository.persist(toEntity(request))
								.replaceWith(Map.entry(toResponse(request), Boolean.TRUE)))
				.onFailure(PersistenceException.class)
				.recoverWithUni(() ->
						zipCodeRepository.findById(request.zip())
								.onItem().ifNotNull().transform(existing ->
										Map.entry(toResponse(existing), Boolean.FALSE))
								.onItem().ifNull().switchTo(() ->
										Uni.createFrom().failure(() -> new RuntimeException("ZipCode not found after failure")))
				);
	}

	ZipCodeResponse toResponse(ZipCodeEntity entity) {
		return new ZipCodeResponse(
				entity.getZip(),
				entity.getCity(),
				entity.getCounty(),
				entity.getState(),
				entity.getTimezone(),
				entity.getType()
		);
	}

	ZipCodeEntity toEntity(ZipCodeRequest request) {
		return new ZipCodeEntity(
				request.zip(),
				request.city(),
				request.county(),
				request.state(),
				request.timezone(),
				request.type()
		);
	}

	ZipCodeResponse toResponse(ZipCodeRequest request) {
		return new ZipCodeResponse(
				request.zip(),
				request.city(),
				request.county(),
				request.state(),
				request.timezone(),
				request.type()
		);
	}
}
