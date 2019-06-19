package com.mercadolibre.mutant.pojo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Adn {
	private String[] sequence;
	private boolean mutant;

	public Adn(String[] sequence, boolean mutant) {
		super();
		this.sequence = sequence;
		this.mutant = mutant;
	}

}
