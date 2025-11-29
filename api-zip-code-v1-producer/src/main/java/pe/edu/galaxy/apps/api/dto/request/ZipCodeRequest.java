package pe.edu.galaxy.apps.api.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ZipCodeRequest(
		@NotBlank(message = "Campo obligatorio")
		String zip,
		@NotBlank(message = "Campo obligatorio")
		String city,
		String county,
		String state,
		String timezone,
		@NotBlank(message = "Campo obligatorio")
		String type
) {
}
