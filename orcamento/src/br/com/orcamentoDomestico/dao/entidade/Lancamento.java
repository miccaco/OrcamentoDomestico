package br.com.orcamentoDomestico.dao.entidade;

import java.util.Date;

public class Lancamento {
	private int numero;
	private Categoria categoria;
	private float valor;
	private Date data;
	private String descricao;

	public Lancamento() {
	}

	public Lancamento(int numero, Categoria categoria, float valor, Date data, String descricao) {
		super();
		this.numero = numero;
		this.categoria = categoria;
		this.valor = valor;
		this.data = data;
		this.descricao = descricao;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public float getValor() {
		return valor;
	}

	public void setValor(float valor) {
		this.valor = valor;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + numero;
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
		Lancamento other = (Lancamento) obj;
		if (numero != other.numero)
			return false;
		return true;
	}

}
