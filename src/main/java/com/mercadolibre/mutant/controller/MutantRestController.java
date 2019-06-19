package com.mercadolibre.mutant.controller;

import javax.validation.Valid;
import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibre.mutant.exception.MutantException;
import com.mercadolibre.mutant.pojo.Sequence;
import com.mercadolibre.mutant.pojo.Stat;
import com.mercadolibre.mutant.service.IMutantService;
import com.mercadolibre.mutant.service.MutantServiceImpl;

@RestController
@RequestMapping("/")

public class MutantRestController {
	@Autowired
	private IMutantService mutantService;

	@PostMapping("/mutant")
	public ResponseEntity<String> mutant(@Valid @RequestBody Sequence sequence) throws MutantException {
		boolean isMutant = mutantService.isMutant(sequence);
		if (isMutant) {
			return new ResponseEntity<>("Es mutante", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("No es mutante", HttpStatus.FORBIDDEN);
		}
	}

	@GetMapping("/stats")
	@Produces(MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Object> stats() throws MutantException, JsonProcessingException {
		Stat stat = mutantService.getStats();
		String response = new ObjectMapper().writeValueAsString(stat);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@ExceptionHandler(value = {MutantException.class, JsonProcessingException.class})
	protected ResponseEntity<Object> handleConflict(Exception ex) {		
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
