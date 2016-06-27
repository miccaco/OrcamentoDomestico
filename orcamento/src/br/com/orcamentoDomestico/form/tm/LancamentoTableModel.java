package br.com.orcamentoDomestico.form.tm;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import br.com.orcamentoDomestico.dao.entidade.Lancamento;

public class LancamentoTableModel extends AbstractTableModel {
	private static final long serialVersionUID = -8364167098125153147L;

	protected ArrayList<Lancamento> models;
	protected String[] columnNames = new String[] { "Número", "Cod. Cat", "Categoria", "Valor", "Data", "Descrição" };
	protected NumberFormat nf;
	protected DateFormat df;

	public LancamentoTableModel() {
		this.nf = new DecimalFormat("###,##0.00");
		this.df = DateFormat.getDateInstance();
	}

	public void setModels(ArrayList<Lancamento> models) {
		this.models = models;
	}

	public ArrayList<Lancamento> getModels() {
		return models;
	}

	@Override
	public String getColumnName(int column) {
		return this.columnNames[column];
	}

	@Override
	public int getColumnCount() {
		return this.columnNames.length;
	}

	@Override
	public int getRowCount() {
		if (this.models == null)
			return 0;

		return this.models.size();
	}

	@Override
	public Object getValueAt(int row, int column) {
		switch (column) {
		case 0:
			return this.models.get(row).getNumero();
		case 1:
			return this.models.get(row).getCategoria().getCodigo();
		case 2:
			return this.models.get(row).getCategoria().getDescricao();
		case 3:
			return this.nf.format(this.models.get(row).getValor());
		case 4:
			return this.df.format(this.models.get(row).getData());
		case 5:
			return this.models.get(row).getDescricao();
		}

		return null;
	}

}
