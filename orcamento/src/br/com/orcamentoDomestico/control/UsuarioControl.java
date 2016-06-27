package br.com.orcamentoDomestico.control;

import br.com.orcamentoDomestico.dao.DAOFactory;
import br.com.orcamentoDomestico.dao.UsuarioDAO;
import br.com.orcamentoDomestico.dao.entidade.Usuario;
import br.com.orcamentoDomestico.exception.CampoInvalidoRNException;
import br.com.orcamentoDomestico.exception.LoginInvalidoRNException;

public class UsuarioControl {
	private static UsuarioControl instance;

	private Usuario usuario;
	private UsuarioDAO dao;

	public static UsuarioControl getInstance() {
		if (instance == null)
			instance = new UsuarioControl();

		return instance;
	}

	private UsuarioControl() {
	};

	public Usuario validaLogin(String login, String senha) throws LoginInvalidoRNException {

		if (login == null || login.trim().isEmpty() //
				|| senha == null || senha.trim().isEmpty())
			throw new LoginInvalidoRNException();

		this.usuario = this.dao().buscarUsuario(login, senha);

		if (this.usuario == null)
			throw new LoginInvalidoRNException();

		return this.usuario;
	}

	protected UsuarioDAO dao() {
		if (this.dao == null)
			this.dao = DAOFactory.newUsuarioDAO();

		return this.dao;
	}

	public Usuario getUsuario() {
		return this.usuario;
	}

	public void registrar(Usuario usuario) throws CampoInvalidoRNException {

		if (usuario.getLogin() == null || usuario.getLogin().trim().isEmpty())
			throw new CampoInvalidoRNException("Campo Login é obrigatório !");

		if (usuario.getNome() == null || usuario.getNome().trim().isEmpty())
			throw new CampoInvalidoRNException("Campo Nome é obrigatório !");

		if (usuario.getSenha() == null || usuario.getSenha().trim().isEmpty())
			throw new CampoInvalidoRNException("Campo Senha é obrigatório !");

		if (usuario.getSenha().length() < 8)
			throw new CampoInvalidoRNException("A senha deve ter no mínimo 8 caracteres !");

		Usuario old = this.dao().buscar(usuario.getLogin());

		if (old != null)
			throw new CampoInvalidoRNException(String.format("Já existe um usuário com o Login %s !", //
					usuario.getLogin()));

		usuario.setLogin(usuario.getLogin().toLowerCase());

		this.dao().incluir(usuario);

		this.usuario = usuario;
	}
}
