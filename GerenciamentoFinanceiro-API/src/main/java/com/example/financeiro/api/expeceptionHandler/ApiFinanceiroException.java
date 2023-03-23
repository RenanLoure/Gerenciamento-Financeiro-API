package com.example.financeiro.api.expeceptionHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
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
		return handleExceptionInternal(ex, new Erro (mensagemUsuario, mensagemDev), headers, HttpStatus.BAD_REQUEST, request);
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
