package br.ce.wcaquino.servicos;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Date;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.utils.DataUtils;

public class LocacaoServiceTest {

	private LocacaoService service;


	@Rule
	public ErrorCollector error = new ErrorCollector();

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@BeforeClass
	public static void setupClass() {
		System.out.println("b4 class");
		
	}
	
	@AfterClass
	public static void afterClass() {
		System.out.println("After class");
		
	}
	
	@Before
	public void setup() {
		service = new LocacaoService();
		
	}

	@After
	public void tearDown() {
		// System.out.println("After");
	}

	@Test
	public void testeLocacao() throws Exception {

		Usuario usuario = new Usuario("usuario 1");
		Filme filme = new Filme("Filme 1", 2, 5.0);

		
		
		// acao

		Locacao locacao = service.alugarFilme(usuario, filme);

		// verificacao

		error.checkThat(locacao.getValor(), is(equalTo(5.0)));
		error.checkThat(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
		error.checkThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)),
				is(true));

	}

	@Test(expected = FilmeSemEstoqueException.class)
	public void testLocacao_filmeSemEstoque_1() throws Exception {

		// cenario
		LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("usuario 1");
		Filme filme = new Filme("Filme 1", 0, 5.0);

		System.out.println("teste");

		
		
		// acao
		service.alugarFilme(usuario, filme);

	}

	@Test
	public void testLocacao_filmeSemEstoque_2() {

		// cenario
		LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("usuario 1");
		Filme filme = new Filme("Filme 1", 0, 5.0);

		
		
		// acao
		try {
			service.alugarFilme(usuario, filme);
			Assert.fail("Deveria ter lancao uma excecao");
		} catch (Exception e) {
			assertThat(e.getMessage(), is("Filme sem estoque"));
		}

	}

	@Test
	public void testLocacao_filmeSemEstoque_3() throws Exception {

		// cenario
		LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("usuario 1");
		Filme filme = new Filme("Filme 1", 0, 5.0);

		
		
		exception.expect(Exception.class);
		exception.expectMessage("Filme sem estoque");

		// acao
		service.alugarFilme(usuario, filme);
	}

	@Test
	public void testLocacao_UsuarioVazio() throws FilmeSemEstoqueException {
		// cenario

		LocacaoService service = new LocacaoService();
		Filme filme = new Filme("Filme 2", 1, 4.0);

		
		
		// acao
		try {
			service.alugarFilme(null, filme);
			Assert.fail();
		} catch (LocadoraException e) {
			assertThat(e.getMessage(), is("Usuario vazio"));
		}

		System.out.println("Forma Robusta");
	}

	@Test
	public void testLocacao_FilmeVazio() throws FilmeSemEstoqueException, LocadoraException {
		// cenario
		LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("Usuario 1");

		
		
		exception.expect(LocadoraException.class);
		exception.expectMessage("Filme vazio");
		// acao
		service.alugarFilme(usuario, null);

		System.out.println("Forma nova");

	}
}