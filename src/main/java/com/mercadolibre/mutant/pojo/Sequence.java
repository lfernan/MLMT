package com.mercadolibre.mutant.pojo;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class Sequence {
	@NotEmpty(message = "La secuencia de adn no puede ser vacia")
	@NotNull(message = "La secuencia de adn no puede ser nul")
	private List<String> adn;
}
