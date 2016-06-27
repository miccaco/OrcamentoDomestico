package br.com.orcamentoDomestico.dao.entidade;

public class Categoria {
	private Usuario usuario;
	private int codigo;
	private String descricao;
	private Natureza tipo;

	public Categoria() {
	}

	public Categoria(Usuario usuario, int codigo, String descricao, Natureza tipo) {
		this.usuario = usuario;
		this.codigo = codigo;
		this.descricao = descricao;
		this.tipo = tipo;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public void setTipo(Natureza tipo) {
		this.tipo = tipo;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public int getCodigo() {
		return codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public Natureza getTipo() {
		return tipo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + codigo;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Categoria other = (Categoria) obj;
		if (codigo != other.codigo)
			return false;
		return true;
	}

}
