package pe.edu.galaxy.apps.domain.service;

import io.smallrye.mutiny.Uni;
import pe.edu.galaxy.apps.api.dto.request.ZipCodeRequest;
import pe.edu.galaxy.apps.api.dto.response.ZipCodeResponse;

import java.util.Map;

public interface ZipCodeService {

	Uni<Map.Entry<ZipCodeResponse, Boolean>> create(ZipCodeRequest request);
}
