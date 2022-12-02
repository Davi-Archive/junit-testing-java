package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.utils.DataUtils.adicionarDias;

import java.util.Date;
import java.util.List;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;

public class LocacaoService {

	public Locacao alugarFilme(Usuario usuario, List<Filme> filmes) throws FilmeSemEstoqueException, LocadoraException {
		if (usuario == null) {
			throw new LocadoraException("Usuario vazio");
		}

		if (filmes == null || filmes.isEmpty()) {
			throw new LocadoraException("Filme vazio");
		}

		for (Filme filme : filmes) {
			if (filme.getEstoque() == 0) {
				throw new FilmeSemEstoqueException();
			}
		}

		Locacao locacao = new Locacao();
		locacao.setFilmes(filmes);
		locacao.setUsuario(usuario);
		locacao.setDataLocacao(new Date());
		Integer totalDeFilmes = 0;
		Double valorTotal = 0d;
		for (Filme filme : filmes) {

			for (int i = 0; i < filmes.size(); i++) {
				totalDeFilmes += 1;
				if (totalDeFilmes > 0 && totalDeFilmes < 3) {
					valorTotal += filme.getPrecoLocacao();
				} else if (totalDeFilmes == 3) {
					valorTotal += filme.getPrecoLocacao() * 0.75;
				} else if (totalDeFilmes == 4) {
					valorTotal += filme.getPrecoLocacao() * 0.50;
				} else if (totalDeFilmes == 5) {
					valorTotal += filme.getPrecoLocacao() * 0.25;
				} else {
					valorTotal += 0;
				}
				System.out.println("Preco: " + valorTotal + " , TOTAL: " + totalDeFilmes);
			}

		}

		locacao.setValor(valorTotal);
		valorTotal=0.0;

		// Entrega no dia seguinte
		Date dataEntrega = new Date();
		dataEntrega =

				adicionarDias(dataEntrega, 1);
		locacao.setDataRetorno(dataEntrega);

		// Salvando a locacao...
		// TODO adicionar método para salvar

		return locacao;
	}
}