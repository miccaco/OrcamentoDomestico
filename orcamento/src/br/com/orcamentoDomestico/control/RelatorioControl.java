package br.com.orcamentoDomestico.control;

import java.util.ArrayList;

import br.com.orcamentoDomestico.dao.entidade.Categoria;
import br.com.orcamentoDomestico.dao.entidade.Lancamento;
import br.com.orcamentoDomestico.dao.entidade.Usuario;

public class RelatorioControl {
	private static RelatorioControl instance;

	private UsuarioControl uc;
	private CategoriaControl ctc;
	private LancamentoControl lc;

	public static RelatorioControl getInstance() {
		if (instance == null)
			instance = new RelatorioControl();

		return instance;
	}

	private RelatorioControl() {
		this.uc = UsuarioControl.getInstance();
		this.ctc = CategoriaControl.getInstance();
		this.lc = LancamentoControl.getInstance();
	};

	public Usuario getUsuario() {
		return this.uc.getUsuario();
	}

	public ArrayList<Lancamento> buscarLancamentos() {
		return this.lc.buscarTodos();
	}

	public ArrayList<Categoria> buscarCategorias() {
		return this.ctc.buscarTodos();
	}
}
