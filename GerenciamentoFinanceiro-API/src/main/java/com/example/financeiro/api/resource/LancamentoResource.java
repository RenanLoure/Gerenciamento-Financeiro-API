package com.example.financeiro.api.resource;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.financeiro.api.event.RecursoCriadoEvent;
import com.example.financeiro.api.expeceptionHandler.ApiFinanceiroException.Erro;
import com.example.financeiro.api.model.Lancamento;
import com.example.financeiro.api.repository.LancamentoRepository;
import com.example.financeiro.api.repository.filter.LancamentoFilter;
import com.example.financeiro.api.service.LancamentoService;
import com.example.financeiro.api.service.exception.PessoaInexistenteOuInativaException;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/lancamentos")
public class LancamentoResource {


	@Autowired 
	private LancamentoRepository lancamentoRepository; 
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@Autowired
	private MessageSource messageSource;

	@Autowired
	private LancamentoService lancamentoService;
	
	@GetMapping
	public Page<Lancamento> pesquisar(LancamentoFilter lancamentofilter, Pageable pageable) {
		return lancamentoRepository.filtar(lancamentofilter, pageable);
	}
	
	@GetMapping("/{codigo}")
	public ResponseEntity<Optional <Lancamento>> buscarPeloCodigo(@PathVariable Long codigo) {
		Optional<Lancamento> lancamento = lancamentoRepository.findById(codigo);
		return !lancamento.isEmpty() ? ResponseEntity.ok(lancamento) : ResponseEntity.notFound().build();
	}
	
	@PostMapping
	public ResponseEntity<Lancamento> criar(@Valid @RequestBody Lancamento lancamento, HttpServletResponse response) {
		Lancamento lancamentoSalvo =  lancamentoService.salvar(lancamento);
		
		publisher.publishEvent(new RecursoCriadoEvent(this, response, lancamentoSalvo.getCodigo()));
		
	    return ResponseEntity.status(HttpStatus.CREATED).body(lancamentoSalvo);
	    
	}
	
	@DeleteMapping("/{codigo}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	private void remover(@PathVariable Long codigo) {
		lancamentoRepository.deleteById(codigo);
	}
	
	@ExceptionHandler({ PessoaInexistenteOuInativaException.class})
	public ResponseEntity<Object> handlePessoaInexistenteOuInativaException(PessoaInexistenteOuInativaException ex){
		String mensagemUsuario = messageSource.getMessage("pessoa.inexistente-ou-inativa", null, LocaleContextHolder.getLocale());
		String mensagemDev = ex.toString();
		List<Erro>  erros = Arrays.asList(new Erro (mensagemUsuario, mensagemDev));
		return ResponseEntity.badRequest().body(erros);
	}
	
	
}
