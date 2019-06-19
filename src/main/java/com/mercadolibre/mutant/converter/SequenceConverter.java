package com.mercadolibre.mutant.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.mercadolibre.mutant.pojo.Sequence;

@Component
public class SequenceConverter implements Converter<Sequence, Character[][]> {

	@Override
	public Character[][] convert(Sequence sequence) {

		Character[][] sequence2D = new Character[sequence.getAdn().size()][sequence.getAdn().get(0).length()];
		int y = 0;
		for (String adn : sequence.getAdn()) {
			char[] characters = adn.toCharArray();
			for (int x = 0; x < characters.length; x++) {
				sequence2D[y][x] = characters[x];
			}
			y++;
		}

		return sequence2D;
	}

}
