package com.example.financeiro.api.expeceptionHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ApiFinanceiroException extends ResponseEntityExceptionHandler {
	
	@Autowired
	private MessageSource messageSource;
	
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		
		String mensagemUsuario = messageSource.getMessage("messagem.invalida", null, LocaleContextHolder.getLocale());
		String mensagemDev = ex.getCause().toString();
		
		List<Erro>  erros = Arrays.asList(new Erro (mensagemUsuario, mensagemDev));
		return handleExceptionInternal(ex, erros, headers, HttpStatus.BAD_REQUEST, request);
	}
	
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		
		List<Erro> erros = listaDeErros(ex.getBindingResult());
		return handleExceptionInternal(ex, erros, headers, HttpStatus.BAD_REQUEST, request);
	}
	
	private List<Erro> listaDeErros(BindingResult bindingResult){
		List<Erro> erros = new ArrayList<>(); 
		
		for(FieldError fieldError : bindingResult.getFieldErrors()) {
		
		String mensagemUsuario = messageSource.getMessage(fieldError, LocaleContextHolder.getLocale());
		String mensagemDev = fieldError.toString();
		erros.add(new Erro (mensagemUsuario, mensagemDev));
		}
		return erros;
	}

	public static class Erro {
		private String mensagemUsuario;
		private String mensagemDev;
		public Erro(String mensagemUsuario, String mensaegemDev) {
			this.mensagemUsuario = mensagemUsuario;
			this.mensagemDev = mensaegemDev;
		} 
		public String getMensagemUsuario() {
			return mensagemUsuario;
		}
		public String getMensaegemDev() {
			return mensagemDev;
		}
	}
	
}
