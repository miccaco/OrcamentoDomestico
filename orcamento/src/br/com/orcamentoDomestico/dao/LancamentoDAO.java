package br.com.orcamentoDomestico.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import br.com.orcamentoDomestico.dao.entidade.Categoria;
import br.com.orcamentoDomestico.dao.entidade.Lancamento;
import br.com.orcamentoDomestico.dao.entidade.Usuario;

public class LancamentoDAO extends AbstractDAO<Lancamento> {

	private CategoriaDAO catDAO = DAOFactory.newCategoriaDAO();
	private static String[] campos = { "categoria", "valor", "data", "descricao" };

	private static LancamentoDAO instance;

	public static LancamentoDAO getInstance() {
		if (instance == null)
			instance = new LancamentoDAO();

		return instance;
	}

	private LancamentoDAO() {
		super("lancamento", "numero", "categoria", "valor", "data", "descricao");
	}

	@Override
	protected Lancamento montarObjeto(ResultSet rs) {

		Lancamento lanc = new Lancamento();

		try {
			lanc.setNumero(rs.getInt(1));

			if (this.buffer.get(lanc.hashCode()) != null)
				lanc = this.buffer.get(lanc.hashCode());

			lanc.setValor(rs.getFloat(3));
			lanc.setData(rs.getDate(4));
			lanc.setDescricao(rs.getString(5));

			lanc.setCategoria(this.catDAO.buscar(rs.getInt(2)));

			this.buffer.put(Integer.valueOf(lanc.hashCode()), lanc);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return lanc;
	}

	@Override
	public void incluir(Lancamento lanc) {
		int numero = this.incluirPorCampos(campos, //
				lanc.getCategoria().getCodigo(), //
				lanc.getValor(), //
				lanc.getData(), //
				lanc.getDescricao());
		lanc.setNumero(numero);
		this.buffer.put(lanc.hashCode(), lanc);
	}

	@Override
	public void alterar(Lancamento lanc) {
		this.alterarPorCampos(new String[] { "numero" }, //
				new Object[] { lanc.getNumero() }, campos, lanc.getCategoria().getCodigo(), //
				lanc.getValor(), //
				lanc.getData(), //
				lanc.getDescricao());
	}

	@Override
	public void eliminar(Lancamento entidade) {
		this.eliminarPorCampos(new String[] { "numero" }, entidade.getNumero());
	}

	@Override
	public Lancamento buscar(int numero) {

		int hashCode = 31 * 1 + numero;

		if (this.buffer.containsKey(hashCode))
			return this.buffer.get(hashCode);

		ArrayList<Lancamento> list = this.buscarPorCampos(new String[] { "numero" }, numero);

		if (list.isEmpty())
			return null;
		else {
			Lancamento lanc = list.get(0);
			this.buffer.put(lanc.hashCode(), lanc);
			return list.get(0);
		}
	}

	@Override
	public ArrayList<Lancamento> buscarTodos(Usuario usuario) {

		ArrayList<Lancamento> entidades = new ArrayList<>();

		try {
			String sql = "SELECT l.numero,l.categoria,l.valor,l.data,l.descricao FROM lancamento l " //
					+ "INNER JOIN categoria c ON c.codigo = l.categoria " //
					+ "WHERE c.usuario = ?";

			// System.out.println(sql);
			PreparedStatement ps = this.conn.prepareStatement(sql);
			ps.setString(1, usuario.getLogin());
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				entidades.add(this.montarObjeto(rs));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return entidades;
	}

	public ArrayList<Lancamento> buscarPorCategoria(Categoria cat) {
		return this.buscarPorCampos(new String[] { "categoria" }, //
				cat.getCodigo());
	}

}
