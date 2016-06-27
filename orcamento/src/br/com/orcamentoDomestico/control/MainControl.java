package br.com.orcamentoDomestico.control;

import br.com.orcamentoDomestico.dao.entidade.Usuario;
import br.com.orcamentoDomestico.form.MainForm;

public class MainControl {
	private MainForm form;

	public MainControl(MainForm form) {
		this.form = form;
	}

	public void iniciar() {
		if (this.getUsuarioCorrente() == null) {
			this.form.logar();
		}
	}

	public Usuario getUsuarioCorrente() {
		return UsuarioControl.getInstance().getUsuario();
	}
}
