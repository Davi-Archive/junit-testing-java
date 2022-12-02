package br.ce.wcaquino.servicos;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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
	
	private List<Filme> filmes = new ArrayList<Filme>();
	private Locacao locacao;
	private LocacaoService service;
	private Usuario usuario;

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
			
		Filme f1 = new Filme("Senhor dos Aneis", 2, 20.0);
		Filme f2 = new Filme("Harry Potter", 6, 20.0);
		Filme f3 = new Filme("Naruto", 4, 10.0);

		filmes.addAll(Arrays.asList(f1, f2, f3));
		
		

	}

	@After
	public void tearDown() {
		// System.out.println("After");
	}

	@Test
	public void testeLocacao() throws Exception {

		
		usuario = new Usuario("usuario 1");

		locacao = service.alugarFilme(usuario, filmes);

		// verificacao

		error.checkThat(locacao.getValor(), is(equalTo(50.0)));
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
		
		filmes.addAll(Arrays.asList(filme));

		// acao
		service.alugarFilme(usuario, filmes);

	}

	@Test
	public void testLocacao_filmeSemEstoque_2() {

		// cenario
		LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("usuario 1");
		Filme filme = new Filme("Filme 1", 0, 5.0);

		filmes.addAll(Arrays.asList(filme));
		// acao
		try {
			service.alugarFilme(usuario, filmes);
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

		filmes.addAll(Arrays.asList(filme));
		
		exception.expect(Exception.class);
		exception.expectMessage("Filme sem estoque");

		// acao
		service.alugarFilme(usuario, filmes);
	}

	@Test
	public void testLocacao_UsuarioVazio() throws FilmeSemEstoqueException {
		// cenario

		LocacaoService service = new LocacaoService();
		Filme filme = new Filme("Filme 2", 1, 4.0);

		// acao
		try {
			service.alugarFilme(null, filmes);
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
