package br.com.orcamentoDomestico.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import br.com.orcamentoDomestico.dao.entidade.Categoria;
import br.com.orcamentoDomestico.dao.entidade.Natureza;
import br.com.orcamentoDomestico.dao.entidade.Usuario;

public class CategoriaDAO extends AbstractDAO<Categoria> {

	private UsuarioDAO usuarioDAO = DAOFactory.newUsuarioDAO();
	private static String[] campos = { "usuario", "descricao", "tipo" };

	private static CategoriaDAO instance = null;

	public static CategoriaDAO getInstance() {
		if (instance == null)
			instance = new CategoriaDAO();

		return instance;
	}

	private CategoriaDAO() {
		super("categoria", "codigo", "usuario", "descricao", "tipo");
	}

	@Override
	protected Categoria montarObjeto(ResultSet rs) {

		Categoria cat = null;

		try {
			int hashCode = 31 * 1 + rs.getInt(1);

			if (this.buffer.containsKey(hashCode)) {
				cat = this.buffer.get(hashCode);
			} else {
				cat = new Categoria();
				cat.setCodigo(rs.getInt(1));
			}

			cat.setDescricao(rs.getString(3));

			boolean tipo = rs.getBoolean(4);

			if (tipo)
				cat.setTipo(Natureza.DESPESA);
			else
				cat.setTipo(Natureza.RECEITA);

			cat.setUsuario(this.usuarioDAO.buscar(rs.getString(2)));

			this.buffer.put(cat.hashCode(), cat);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return cat;
	}

	@Override
	public void incluir(Categoria cat) {
		int codigo = this.incluirPorCampos(campos, //
				cat.getUsuario().getLogin(), //
				cat.getDescricao(), //
				cat.getTipo() == Natureza.DESPESA);
		cat.setCodigo(codigo);
		this.buffer.put(cat.hashCode(), cat);
	}

	@Override
	public void alterar(Categoria cat) {
		this.alterarPorCampos(new String[] { "codigo" }, //
				new Object[] { cat.getCodigo() }, campos, //
				cat.getUsuario().getLogin(), //
				cat.getDescricao(), //
				cat.getTipo() == Natureza.DESPESA);
	}

	@Override
	public void eliminar(Categoria entidade) {
		this.eliminarPorCampos(new String[] { "codigo" }, entidade.getCodigo());
	}

	@Override
	public Categoria buscar(int codigo) {

		int hashCode = 31 * 1 + codigo;

		if (this.buffer.containsKey(hashCode))
			return this.buffer.get(hashCode);

		ArrayList<Categoria> list = this.buscarPorCampos(new String[] { "codigo" }, codigo);

		if (list.isEmpty())
			return null;
		else {
			Categoria cat = list.get(0);
			this.buffer.put(cat.hashCode(), cat);
			return list.get(0);
		}
	}

	@Override
	public ArrayList<Categoria> buscarTodos(Usuario usuario) {
		return this.buscarPorCampos(new String[] { "usuario" }, //
				usuario.getLogin());
	}

}
