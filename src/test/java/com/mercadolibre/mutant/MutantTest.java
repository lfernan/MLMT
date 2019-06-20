package com.mercadolibre.mutant;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibre.mutant.pojo.Sequence;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MutantTest {
	@Autowired
	private MockMvc mockMvc;

	private Sequence adnMutant;
	private Sequence adnNotMutant;
	private Sequence adnNull = new Sequence();
	private Sequence adnWrong;
	ObjectMapper objectMapper;

	private static final String BASE_MUTANT_PATH = "/mutant/";
	private static final String BASE_STATS_PATH = "/stats/";

	@BeforeEach
	void init() {
		adnMutant = new Sequence();
		adnNotMutant = new Sequence();
		objectMapper = new ObjectMapper();

		/* MUTANTE */
		adnMutant = new Sequence();
		List<String> sequenceMutant = new ArrayList<String>();
		sequenceMutant.add("ATGCGA");
		sequenceMutant.add("CAGTGC");
		sequenceMutant.add("TTATGT");
		sequenceMutant.add("AGAAGG");
		sequenceMutant.add("CCCCTA");
		sequenceMutant.add("TCACTG");

		adnMutant.setAdn(sequenceMutant);

		/* NO MUTANTE */
		adnNotMutant = new Sequence();
		List<String> sequenceNotMutant = new ArrayList<String>();
		sequenceNotMutant.add("ATGCGA");
		sequenceNotMutant.add("CAGTGC");
		sequenceNotMutant.add("TTATTT");
		sequenceNotMutant.add("AGACGG");
		sequenceNotMutant.add("GCGTCA");
		sequenceNotMutant.add("TCACTG");

		adnNotMutant.setAdn(sequenceNotMutant);

		/* WRONG */
		adnWrong = new Sequence();
		List<String> sequenceWrong = new ArrayList<String>();
		sequenceWrong.add("ASET");
		sequenceWrong.add("CAGTGC");
		sequenceWrong.add("TTATTT");
		sequenceWrong.add("AGACGG");
		sequenceWrong.add("GCGTCA");
		sequenceWrong.add("TCACTG");

		adnWrong.setAdn(sequenceWrong);
	}

	@Test
	@Order(1)
	void mutant() throws Exception {
		mockMvc.perform(post(BASE_MUTANT_PATH).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(adnMutant))).andExpect(status().isOk());

	}

	@Test
	@Order(2)
	void notMutant() throws Exception {
		mockMvc.perform(post(BASE_MUTANT_PATH).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(adnNotMutant))).andExpect(status().isForbidden());

	}

	@Test
	@Order(3)
	void stats() throws Exception {
		mockMvc.perform(get(BASE_STATS_PATH).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

	}

	@Test
	@Order(4)
	void serverError() throws Exception {
		mockMvc.perform(post(BASE_MUTANT_PATH).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(adnNotMutant))).andExpect(status().isForbidden());

	}

	@Test
	@Order(4)
	void serverErrorNullSequence() throws Exception {
		mockMvc.perform(post(BASE_MUTANT_PATH).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(adnNull))).andExpect(status().isBadRequest());

	}
}
