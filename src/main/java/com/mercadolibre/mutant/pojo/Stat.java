package com.mercadolibre.mutant.pojo;

import com.fasterxml.jackson.annotation.JsonGetter;

public class Stat {	
	private int countMutantDna;
	private int countHumanDna;
	private double ratio;

	public Stat() {
		this.countHumanDna = 0;
		this.countMutantDna = 0;
	}

	/**
	 * @return the countMutantDna
	 */
	@JsonGetter("count_mutant_dna")
	public int getCountMutantDna() {
		return countMutantDna;
	}

	/**
	 * @param countMutantDna the countMutantDna to set
	 */
	public void setCountMutantDna(int countMutantDna) {
		this.countMutantDna = countMutantDna;
	}

	/**
	 * @return the countHumanDna
	 */
	@JsonGetter("count_human_dna")
	public int getCountHumanDna() {
		return countHumanDna;
	}

	/**
	 * @param countHumanDna the countHumanDna to set
	 */
	public void setCountHumanDna(int countHumanDna) {
		this.countHumanDna = countHumanDna;
	}

	/**
	 * @return the ratio
	 */
	public double getRatio() {
		return ratio;
	}

	/**
	 * @param ratio the ratio to set
	 */
	public void setRatio(double ratio) {
		this.ratio = ratio;
	}
}
