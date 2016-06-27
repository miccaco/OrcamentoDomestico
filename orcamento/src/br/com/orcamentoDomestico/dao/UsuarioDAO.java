package br.com.orcamentoDomestico.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import br.com.orcamentoDomestico.dao.entidade.Usuario;

public class UsuarioDAO extends AbstractDAO<Usuario> {

	private static String[] campos = { "nome", "login", "senha" };

	private static UsuarioDAO instance;

	public static UsuarioDAO getInstance() {
		if (instance == null)
			instance = new UsuarioDAO();

		return instance;
	}

	private UsuarioDAO() {
		super("usuario", campos);
	}

	@Override
	protected Usuario montarObjeto(ResultSet rs) {

		Usuario usuario = new Usuario();

		try {
			usuario.setLogin(rs.getString(2));

			if (this.buffer.get(usuario.hashCode()) != null)
				usuario = this.buffer.get(usuario.hashCode());

			usuario.setNome(rs.getString(1));
			usuario.setSenha(rs.getString(3));

			this.buffer.put(usuario.hashCode(), usuario);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return usuario;
	}

	@Override
	public void incluir(Usuario usuario) {
		this.incluirPorCampos(campos, //
				usuario.getNome(), //
				usuario.getLogin(), //
				usuario.getSenha());
		this.buffer.put(usuario.hashCode(), usuario);
	}

	@Override
	public void alterar(Usuario usuario) {
		this.alterarPorCampos(new String[] { "login" }, //
				new Object[] { usuario.getLogin() }, campos, //
				usuario.getNome(), //
				usuario.getLogin(), //
				usuario.getSenha());
	}

	public Usuario buscar(String login) {

		int hashCode = 31 * 1 + login.hashCode();

		if (this.buffer.containsKey(hashCode))
			return this.buffer.get(hashCode);

		ArrayList<Usuario> list = this.buscarPorCampos(new String[] { "login" }, login);

		if (list.isEmpty())
			return null;
		else
			return list.get(0);
	}

	public Usuario buscarUsuario(String login, String senha) {
		ArrayList<Usuario> list = this.buscarPorCampos(//
				new String[] { "login", "senha" }, login, senha);

		if (list.isEmpty())
			return null;
		else
			return list.get(0);
	}

	@Override
	public void eliminar(Usuario entidade) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Usuario buscar(int codigo) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ArrayList<Usuario> buscarTodos(Usuario usuario) {
		throw new UnsupportedOperationException();
	}

}
