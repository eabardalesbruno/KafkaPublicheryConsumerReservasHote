package pe.edu.galaxy.apps.api.dto.response;

public record ZipCodeResponse(
		String zip,
		String city,
		String county,
		String state,
		String timezone,
		String type
) {
}
