package br.com.orcamentoDomestico.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import br.com.orcamentoDomestico.dao.entidade.Usuario;

public abstract class AbstractDAO<T> {

	protected String[] colunas = {};

	protected Connection conn = null;
	protected String table = null;

	public HashMap<Integer, T> buffer;

	public AbstractDAO(String table, String... colunas) {
		this.table = table;
		this.colunas = colunas;
		this.conn = ConnectionUtil.getConnection();
		this.buffer = new HashMap<>();
	}

	abstract protected T montarObjeto(ResultSet resultSet);

	protected void setParams(PreparedStatement ps, Object... valores) throws SQLException {

		Object valor;
		for (int i = 1; i <= valores.length; i++) {
			valor = valores[i - 1];

			// System.out.println(valor);
			if (valor instanceof String)
				ps.setString(i, (String) valor);
			else if (valor instanceof Integer)
				ps.setInt(i, (Integer) valor);
			else if (valor instanceof java.sql.Date)
				ps.setDate(i, (java.sql.Date) (valor));
			else if (valor instanceof Date)
				ps.setDate(i, new java.sql.Date(((Date) valor).getTime()));
			else if (valor instanceof Float)
				ps.setFloat(i, (Float) valor);
			else if (valor instanceof Boolean)
				ps.setBoolean(i, (Boolean) valor);
			else if (valor != null)
				ps.setString(i, valor.toString());
			else
				ps.setString(i, null);
		}
	}

	protected int incluirPorCampos(String[] campos, Object... valores) {

		String fields = "";
		String mask = "";
		int genKey = -1;

		for (String campo : campos) {
			fields += campo + ",";
			mask += "?,";
		}

		fields = fields.substring(0, fields.length() - 1);
		mask = mask.substring(0, mask.length() - 1);

		try {
			String sql = String.format("INSERT INTO %s (%s) VALUES(%s)", this.table, fields, mask);
			// System.out.println(sql);
			PreparedStatement ps = this.conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			this.setParams(ps, valores);
			ps.executeUpdate();

			ResultSet genKeys = ps.getGeneratedKeys();

			if (genKeys.first()) {
				genKey = genKeys.getInt(1);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return genKey;
	}

	protected boolean alterarPorCampos(String[] chave, Object[] chaveVal, String[] campos, Object... valores) {

		String fields = "";
		String query = "";

		Object[] params = new Object[chave.length + campos.length];

		int i = 0;
		String campo = null;

		for (int j = 0; j < campos.length; j++, i++) {
			campo = campos[j];
			fields += campo + " = ?,";
			params[i] = valores[j];
		}

		for (int j = 0; j < chave.length; j++, i++) {
			campo = chave[j];
			query += String.format("%s = ? AND", campo);
			params[i] = chaveVal[j];
		}

		fields = fields.substring(0, fields.length() - 1);
		if (!query.isEmpty())
			query = query.substring(0, query.length() - 4);

		try {
			String sql = String.format("UPDATE %s SET %s WHERE %s", this.table, fields, query);

			// System.out.println(sql);
			PreparedStatement ps = this.conn.prepareStatement(sql);

			// System.out.println(params);

			this.setParams(ps, params);

			int numAffecteds = ps.executeUpdate();

			if (numAffecteds > 0)
				return true;
			else
				return false;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	protected boolean eliminarPorCampos(String[] campos, Object... valores) {
		String query = "";

		for (String campo : campos)
			query += String.format("%s = ? AND", campo);

		if (!query.isEmpty())
			query = query.substring(0, query.length() - 4);

		try {
			String sql = String.format("DELETE FROM %s", this.table);

			if (!query.isEmpty())
				sql += " WHERE " + query;

			// System.out.println(sql);
			PreparedStatement ps = this.conn.prepareStatement(sql);
			this.setParams(ps, valores);

			int numAffected = ps.executeUpdate();

			if (numAffected > 0)
				return true;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	protected ArrayList<T> buscarPorCampos(String[] campos, Object... valores) {

		ArrayList<T> entidades = new ArrayList<>();

		String fields = "";
		String query = "";

		for (String campo : this.colunas)
			fields += campo + ",";

		for (String campo : campos)
			query += String.format(" %s = ? AND", campo);

		fields = fields.substring(0, fields.length() - 1);

		if (!query.isEmpty())
			query = query.substring(0, query.length() - 4);

		try {
			String sql = String.format("SELECT %s FROM %s", fields, this.table, query);

			if (!query.isEmpty())
				sql += " WHERE " + query;

			// System.out.println(sql);
			PreparedStatement ps = this.conn.prepareStatement(sql);
			this.setParams(ps, valores);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				entidades.add(this.montarObjeto(rs));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return entidades;
	}

	abstract public void incluir(T entidade);

	abstract public void alterar(T entidade);

	abstract public void eliminar(T entidade);

	abstract public T buscar(int codigo);

	abstract public ArrayList<T> buscarTodos(Usuario usuario);

}
