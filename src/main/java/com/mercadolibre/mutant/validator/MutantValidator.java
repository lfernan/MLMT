package com.mercadolibre.mutant.validator;

import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import com.mercadolibre.mutant.pojo.Sequence;

@Component
public class MutantValidator {
	
	public boolean validateSequence(Sequence sequence) {
		Pattern pattern = Pattern.compile("[ATCGatcg]*");
		return sequence.getAdn().stream()
				.allMatch(adn -> pattern.matcher(adn).matches() && sequence.getAdn().get(0).length() == adn.length());			
	}
}
