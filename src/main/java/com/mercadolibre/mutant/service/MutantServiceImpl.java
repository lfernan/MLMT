package com.mercadolibre.mutant.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mercadolibre.mutant.converter.SequenceConverter;
import com.mercadolibre.mutant.exception.MutantException;
import com.mercadolibre.mutant.pojo.Direction;
import com.mercadolibre.mutant.pojo.Sequence;
import com.mercadolibre.mutant.pojo.Stat;
import com.mercadolibre.mutant.validator.MutantValidator;

@Service
public class MutantServiceImpl implements IMutantService{

	@Autowired
	private MutantValidator mutantValidator;
	@Autowired
	private SequenceConverter sequenceConverter;
	@Autowired
	private IFirebaseService firebaseService;

	private static final Direction[] allDirections = { new Direction(0, 1), new Direction(1, 0), new Direction(1, 1),
			new Direction(1, -1) };

	private List<char[]> nitrogenousBases = new ArrayList<char[]>();

	public MutantServiceImpl() {
		nitrogenousBases.add(new char[] { 'A', 'A', 'A', 'A' });
		nitrogenousBases.add(new char[] { 'T', 'T', 'T', 'T' });
		nitrogenousBases.add(new char[] { 'C', 'C', 'C', 'C' });
		nitrogenousBases.add(new char[] { 'G', 'G', 'G', 'G' });
	}
	
	@Override
	public boolean isMutant(Sequence sequence) throws MutantException{
		int count = 0;
		boolean isMutant = false;
		if(!mutantValidator.validateSequence(sequence))
			throw new  MutantException("La secuencia no es valida");
		
		Character[][] sequence2D = sequenceConverter.convert(sequence);

		for (char[] adn : nitrogenousBases) {
			if (count == 2)
				break;
			for (int x = 0; x < sequence2D[0].length; x++) {
				for (int y = 0; y < sequence2D.length; y++) {
					for (Direction direction : allDirections) {
						if (sequence2D[y][x] != adn[0])
							break;
						if (search(x, y, direction, adn, sequence2D))
							count++;
					}
				}
			}
		}
		isMutant = count >= 2;
		firebaseService.save(sequence, isMutant);
		return isMutant;
	}
	
	private boolean search(int x, int y, Direction direction, char[] adn, Character[][] sequence2D) {
		int adnIndex = 0;

		while (true) {
			if (x < 0 || x >= sequence2D[0].length || y < 0 || y >= sequence2D.length || adnIndex >= adn.length
					|| sequence2D[y][x] != adn[adnIndex]) {
				return false;
			}

			if (adnIndex == adn.length - 1) {
				return true;
			}

			x += direction.x;
			y += direction.y;
			adnIndex++;
		}
	}

	@Override
	public Stat getStats() throws MutantException{
		return firebaseService.getAll();
	}
}
