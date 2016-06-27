package br.com.orcamentoDomestico.control;

import java.util.ArrayList;

import br.com.orcamentoDomestico.dao.DAOFactory;
import br.com.orcamentoDomestico.dao.LancamentoDAO;
import br.com.orcamentoDomestico.dao.entidade.Categoria;
import br.com.orcamentoDomestico.dao.entidade.Lancamento;
import br.com.orcamentoDomestico.dao.entidade.Usuario;
import br.com.orcamentoDomestico.exception.CampoInvalidoRNException;

public class LancamentoControl {
	private static LancamentoControl instance;

	public static LancamentoControl getInstance() {
		if (instance == null)
			instance = new LancamentoControl();

		return instance;
	}

	protected LancamentoDAO dao = DAOFactory.newLancamentoDAO();

	public void validar(Lancamento lanc) throws CampoInvalidoRNException {

		if (lanc.getData() == null)
			throw new CampoInvalidoRNException("Campo data do lançamento deve ser informado !");

		if (lanc.getValor() < 0)
			throw new CampoInvalidoRNException("Campo valor do lançamento não pode ser negativo !");

		if (lanc.getCategoria() == null)
			throw new CampoInvalidoRNException("Todo o lançamento deve possuir uma Categoria !");
	}

	public void incluir(Lancamento lanc) throws CampoInvalidoRNException {
		this.validar(lanc);
		this.dao.incluir(lanc);
	}

	public void alterar(Lancamento lanc) throws CampoInvalidoRNException {
		this.validar(lanc);
		this.dao.alterar(lanc);
	}

	public void eliminar(Lancamento lanc) {
		this.dao.eliminar(lanc);
	}

	public Lancamento buscar(int numero) {
		Lancamento lanc = this.dao.buscar(numero);

		if (lanc.getCategoria().getUsuario().equals(this.getUsuarioCorrente()))
			return lanc;
		else
			return null;
	}

	public ArrayList<Lancamento> buscarTodos() {
		return this.dao.buscarTodos(this.getUsuarioCorrente());
	}

	public ArrayList<Lancamento> buscarPorCategoria(Categoria cat) {
		if (cat.getUsuario().equals(this.getUsuarioCorrente()))
			return this.dao.buscarPorCategoria(cat);
		else
			return new ArrayList<>();
	}

	public Usuario getUsuarioCorrente() {
		return UsuarioControl.getInstance().getUsuario();
	}

	public ArrayList<Categoria> buscarCategorias() {
		return CategoriaControl.getInstance().buscarTodos();
	}
}
