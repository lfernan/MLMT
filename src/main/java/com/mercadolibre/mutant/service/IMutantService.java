package com.mercadolibre.mutant.service;

import com.mercadolibre.mutant.exception.MutantException;
import com.mercadolibre.mutant.pojo.Sequence;
import com.mercadolibre.mutant.pojo.Stat;

public interface IMutantService {
	boolean isMutant(Sequence sequence) throws MutantException;
	Stat getStats() throws MutantException;
}
