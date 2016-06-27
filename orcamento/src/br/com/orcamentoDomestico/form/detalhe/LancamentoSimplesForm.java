package br.com.orcamentoDomestico.form.detalhe;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.DateFormatter;
import javax.swing.text.NumberFormatter;

import br.com.orcamentoDomestico.control.LancamentoControl;
import br.com.orcamentoDomestico.dao.entidade.Categoria;
import br.com.orcamentoDomestico.dao.entidade.Lancamento;
import br.com.orcamentoDomestico.exception.CampoInvalidoRNException;
import ui.util.layouts.ParagraphLayout;

public class LancamentoSimplesForm extends JDialog implements ActionListener {
	private static final long serialVersionUID = 8349835347530112826L;

	// panels
	protected JPanel pnData;
	protected JPanel pnActions;

	// inputs
	protected JLabel lbNumero;
	protected JTextField txNumero;

	protected JLabel lbCategoria;
	protected JComboBox<String> cbCategoria;

	protected JLabel lbDescricao;
	protected JTextField txDescricao;

	protected JLabel lbData;
	protected JFormattedTextField txData;

	protected JLabel lbValor;
	protected JFormattedTextField txValor;

	// actions
	protected JButton btSalvar, btCancelar;

	// controls
	protected LancamentoControl control;
	protected NumberFormatter nf;
	protected DateFormatter df;
	protected Lancamento model;
	protected ArrayList<Categoria> categorias;
	protected boolean editavel;
	protected boolean efetivar;

	public LancamentoSimplesForm(Lancamento model, boolean editavel) {
		this.model = model;
		this.editavel = editavel;
		this.efetivar = false;
		this.control = LancamentoControl.getInstance();

		this.setTitle("Detalhe Lançamento");
		this.setSize(380, 230);
		this.setResizable(false);
		this.setModal(true);
		this.setLocationRelativeTo(null);

		this.nf = new NumberFormatter(new DecimalFormat("###,##0.00"));
		this.df = new DateFormatter(DateFormat.getDateInstance());

		this.doData();
		this.doActions();

		this.setVisible(true);

		if (!this.efetivar)
			this.model = null;
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		if (evt.getSource() instanceof Component) {
			Component c = (Component) evt.getSource();

			if (c.getName().equals("salvar")) {
				this.salvar();
			} else if (c.getName().equals("cancelar")) {
				this.model = null;
				this.dispose();
			}
		}
	}

	public void salvar() {

		try {
			Lancamento validar = new Lancamento( //
					this.model.getNumero(), //
					this.model.getCategoria(), //
					this.model.getValor(), //
					this.model.getData(), //
					this.model.getDescricao() //
			);

			if (this.cbCategoria.getSelectedIndex() >= 0)
				validar.setCategoria(this.categorias.get(this.cbCategoria.getSelectedIndex()));
			validar.setDescricao(this.txDescricao.getText().trim());

			if (!this.txData.getText().isEmpty())
				validar.setData((Date) this.df.stringToValue(this.txData.getText()));
			else
				validar.setData(null);

			Number valor = (Number) this.nf.stringToValue(this.txValor.getText());
			validar.setValor(valor.floatValue());

			this.control.validar(validar);
			this.efetivar = true;

			this.model.setCategoria(validar.getCategoria());
			this.model.setValor(validar.getValor());
			this.model.setData(validar.getData());
			this.model.setDescricao(validar.getDescricao());

			this.dispose();
		} catch (CampoInvalidoRNException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Erro ao Salvar !", JOptionPane.ERROR_MESSAGE);
			// e.printStackTrace();
		} catch (ParseException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Erro ao Salvar !", JOptionPane.ERROR_MESSAGE);
			// e.printStackTrace();
		}

	}

	protected void doData() {
		this.pnData = new JPanel(new ParagraphLayout());

		if (this.model.getNumero() > 0) {
			this.lbNumero = new JLabel("Número:");
			this.txNumero = new JTextField();
			this.txNumero.setColumns(14);
			this.txNumero.setEditable(false);
			this.pnData.add(this.lbNumero, ParagraphLayout.NEW_PARAGRAPH);
			this.pnData.add(this.txNumero);
		}

		this.lbCategoria = new JLabel("Categoria:");

		this.cbCategoria = new JComboBox<String>();
		this.cbCategoria.setBackground(this.pnData.getBackground());

		this.categorias = this.control.buscarCategorias();

		for (int i = 0; i < categorias.size(); i++)
			this.cbCategoria.addItem(categorias.get(i).getDescricao());

		this.pnData.add(this.lbCategoria, ParagraphLayout.NEW_PARAGRAPH);
		this.pnData.add(this.cbCategoria);

		this.lbDescricao = new JLabel("Descrição:");
		this.txDescricao = new JTextField(25);
		this.pnData.add(this.lbDescricao, ParagraphLayout.NEW_PARAGRAPH);
		this.pnData.add(this.txDescricao);

		this.lbData = new JLabel("Data:");
		this.txData = new JFormattedTextField(this.df);
		this.txData.setColumns(12);
		this.pnData.add(this.lbData, ParagraphLayout.NEW_PARAGRAPH);
		this.pnData.add(this.txData);

		this.lbValor = new JLabel("Valor R$:");
		this.txValor = new JFormattedTextField(this.nf);
		this.txValor.setColumns(14);
		this.pnData.add(this.lbValor, ParagraphLayout.NEW_PARAGRAPH);
		this.pnData.add(this.txValor);

		this.cbCategoria.setEnabled(this.editavel);
		this.txDescricao.setEditable(this.editavel);
		this.txData.setEditable(this.editavel);
		this.txValor.setEditable(this.editavel);

		try {
			if (this.model.getNumero() > 0)
				this.txNumero.setText(this.nf.valueToString(this.model.getNumero()));

			if (this.model.getCategoria() != null)
				this.cbCategoria.setSelectedIndex(this.categorias.indexOf(this.model.getCategoria()));
			else if (!this.categorias.isEmpty())
				this.cbCategoria.setSelectedIndex(0);

			this.txDescricao
					.setText(String.valueOf(this.model.getDescricao() == null ? "" : this.model.getDescricao()));

			if (this.model.getData() != null)
				this.txData.setText(this.df.valueToString(this.model.getData()));
			else
				this.txData.setText(this.df.valueToString(GregorianCalendar.getInstance().getTime()));

			this.txValor.setText(this.nf.valueToString(this.model.getValor()));

		} catch (ParseException e) {
			e.printStackTrace();
		}
		getContentPane().add(this.pnData);

	}

	protected void doActions() {
		this.pnActions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		this.pnActions.setBackground(Color.gray);

		if (this.editavel) {
			this.btSalvar = new JButton("Salvar");
			this.btSalvar.setName("salvar");
			this.btSalvar.setBackground(this.pnActions.getBackground());
			this.btSalvar.addActionListener(this);
			this.pnActions.add(this.btSalvar);
		}

		this.btCancelar = new JButton(this.editavel ? "Cancelar" : "Fechar");
		this.btCancelar.setName("cancelar");
		this.btCancelar.setBackground(this.pnActions.getBackground());
		this.btCancelar.addActionListener(this);
		this.pnActions.add(this.btCancelar);

		getContentPane().add(this.pnActions, BorderLayout.SOUTH);
	}

	public Lancamento getModel() {
		return model;
	}

	public boolean isEditavel() {
		return editavel;
	}
}
