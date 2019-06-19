package com.mercadolibre.mutant.service;

import com.mercadolibre.mutant.exception.MutantException;
import com.mercadolibre.mutant.pojo.Sequence;
import com.mercadolibre.mutant.pojo.Stat;

public interface IFirebaseService {
	void save(Sequence sequence, boolean mutant) throws MutantException;
	Stat getAll() throws MutantException;
}
