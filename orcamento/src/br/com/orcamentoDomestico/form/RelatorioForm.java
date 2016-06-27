package br.com.orcamentoDomestico.form;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import br.com.orcamentoDomestico.control.RelatorioControl;
import br.com.orcamentoDomestico.dao.entidade.Categoria;

import br.com.orcamentoDomestico.dao.entidade.Lancamento;
import br.com.orcamentoDomestico.dao.entidade.Natureza;

import br.com.orcamentoDomestico.dao.entidade.Usuario;
import ui.util.layouts.ParagraphLayout;

public class RelatorioForm extends JDialog implements ActionListener {

	private static final long serialVersionUID = 646370277184134473L;

	// panels
	protected JPanel pnData;
	protected JPanel pnActions;

	// inputs
	protected JLabel lbSaida;
	protected JRadioButton rbTerminal;
	protected JRadioButton rbArquivo;

	protected JLabel lbDescricao;
	protected JTextField txArquivo;

	// actions
	protected JButton btGerar, btCancelar, btProcurar;

	// controls
	protected RelatorioControl control;
	protected File file;

	protected Usuario usuarioCorrente;
	protected ArrayList<Categoria> categorias;
	protected ArrayList<Lancamento> lancamentos;

	protected HashMap<Integer, ArrayList<Lancamento>> lanctosPorCategoria;

	public RelatorioForm() {
		this.control = RelatorioControl.getInstance();

		this.setTitle("Geração Relatório");
		this.setSize(450, 140);
		this.setResizable(false);
		this.setModal(true);
		this.setLocationRelativeTo(null);

		this.doData();
		this.doActions();

		this.setVisible(true);
	}

	protected void doData() {
		this.pnData = new JPanel(new ParagraphLayout());

		this.lbSaida = new JLabel("Saída:");
		this.rbTerminal = new JRadioButton("Terminal");
		this.rbTerminal.setName("terminal");
		this.rbTerminal.addActionListener(this);
		this.rbArquivo = new JRadioButton("Arquivo");
		this.rbArquivo.setName("arquivo");
		this.rbArquivo.addActionListener(this);
		this.pnData.add(this.lbSaida, ParagraphLayout.NEW_PARAGRAPH);
		this.pnData.add(this.rbTerminal);
		this.pnData.add(this.rbArquivo);

		ButtonGroup bg = new ButtonGroup();
		bg.add(this.rbTerminal);
		bg.add(this.rbArquivo);

		this.rbTerminal.setSelected(true);

		this.lbDescricao = new JLabel("Arquivo:");
		this.txArquivo = new JTextField(25);
		this.txArquivo.setEditable(false);
		this.pnData.add(this.lbDescricao, ParagraphLayout.NEW_PARAGRAPH);
		this.pnData.add(this.txArquivo);

		this.btProcurar = new JButton("Procurar");
		this.btProcurar.setName("procurar");
		this.btProcurar.addActionListener(this);
		this.btProcurar.setEnabled(false);
		this.pnData.add(this.btProcurar);

		getContentPane().add(this.pnData);
	}

	protected void doActions() {
		this.pnActions = new JPanel(new BorderLayout());
		this.pnActions.setBackground(Color.gray);
		this.pnActions.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		this.btCancelar = new JButton("Cancelar");
		this.btCancelar.setName("cancelar");
		this.btCancelar.setBackground(this.pnActions.getBackground());
		this.btCancelar.addActionListener(this);
		this.pnActions.add(this.btCancelar, BorderLayout.WEST);

		this.btGerar = new JButton("Gerar");
		this.btGerar.getSize().width = 15;
		this.btGerar.setName("salvar");
		this.btGerar.setBackground(this.pnActions.getBackground());
		this.btGerar.addActionListener(this);
		this.pnActions.add(this.btGerar, BorderLayout.EAST);

		getContentPane().add(this.pnActions, BorderLayout.SOUTH);
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		if (evt.getSource() instanceof Component) {
			Component c = (Component) evt.getSource();

			if (c.getName().equals("salvar")) {
				this.imprimir();
			} else if (c.getName().equals("procurar")) {

				JFileChooser fc = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Relatório", "rpt", "txt");
				fc.addChoosableFileFilter(filter);
				fc.setAcceptAllFileFilterUsed(false);

				if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
					this.file = fc.getSelectedFile();
					this.txArquivo.setText(this.file.getAbsolutePath());
				}

				fc.setVisible(false);
			} else if (c.getName().equals("terminal")) {
				if (this.rbTerminal.isSelected()) {
					this.txArquivo.setText("");
					this.btProcurar.setEnabled(false);
				}
			} else if (c.getName().equals("arquivo")) {
				if (this.rbArquivo.isSelected()) {
					if (this.file != null)
						this.txArquivo.setText(this.file.getAbsolutePath());
					this.btProcurar.setEnabled(true);
				}
			} else if (c.getName().equals("cancelar")) {
				this.dispose();
			}
		}
	}

	public void imprimir() {

		if (this.rbArquivo.isSelected() && (this.file == null || !this.file.canWrite())) {
			JOptionPane.showMessageDialog(this, "Não foi selecionado um diretório de saída válido !", "Erro",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		try {
			if (this.rbTerminal.isSelected())
				this.file = File.createTempFile("rel_orcamento", ".txt");

			this.file.createNewFile();

			DecimalFormat nf = new DecimalFormat("###,##0.00");
			DateFormat df = DateFormat.getDateInstance();

			FileWriter fw = new FileWriter(this.file);

			Date agora = new Date();
			float totalCat = 0, totalOrc = 0, fator = 1;

			this.montarListas();

			fw.write(
					"---------------------------------------------------------------------------------------------------------------\n");
			fw.write(String.format("%s - %s\n", //
					formatString(this.usuarioCorrente.getNome(), 98), //
					df.format(agora)));
			fw.write(
					"---------------------------------------------------------------------------------------------------------------\n");

			for (Categoria c : this.categorias) {
				totalCat = 0;
				fw.write("\n");
				fw.write(String.format(" Categoria: %s (%s)", //
						c.getDescricao(), //
						c.getTipo().name()));
				fw.write("\n\n");

				if (c.getTipo() == Natureza.DESPESA)
					fator = -1;
				else
					fator = 1;

				fw.write(
						"  Número      Data       Descrição                                                                   Valor\n");
				fw.write(
						"  ----------- ---------- --------------------------------------------------------------------------- ----------\n");

				if (this.lanctosPorCategoria.containsKey(c.hashCode()))
					for (Lancamento l : this.lanctosPorCategoria.get(c.hashCode())) {
						fw.write(String.format("  %11d %s %s %10s\n", //
								l.getNumero(), //
								df.format(l.getData()), //
								formatString(l.getDescricao(), 75), //
								nf.format(l.getValor()) //
						));

						totalCat += l.getValor();
					}
				else
					fw.write("                                           *** Nenhum lançamento ***\n");

				fw.write(
						"                                                                                                     ----------\n");
				fw.write(String.format(
						"                                                                                        Valor Total: %10s\n", //
						nf.format(totalCat)));

				totalOrc += totalCat * fator;
			}

			fw.write("\n");
			fw.write(String.format(
					"                                                                             *** Saldo do Orçamento: %10s\n", //
					nf.format(totalOrc)));
			fw.write("\n");
			fw.write(
					"---------------------------------------------------------------------------------------------------------------\n");

			fw.close();

			if (this.rbTerminal.isSelected())
				Desktop.getDesktop().open(this.file);

		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "Ocorreu algum erro e não foi possível gerar o relatório !",
					"Erro Exportação", JOptionPane.WARNING_MESSAGE);
			e.printStackTrace();
			return;
		} finally {
		}

		this.dispose();
	}

	protected String formatString(String value, int length) {
		StringBuffer sb = new StringBuffer();

		if (value.length() < length) {
			sb.append(value);
			for (int i = value.length(); i < length; i++)
				sb.append(" ");
		} else if (value.length() > length) {
			sb.append(value.subSequence(0, length));
		} else
			sb.append(value);

		return sb.toString();
	}

	protected void montarListas() {
		this.usuarioCorrente = this.control.getUsuario();
		this.categorias = this.control.buscarCategorias();
		this.lancamentos = this.control.buscarLancamentos();
		this.lanctosPorCategoria = new HashMap<>();

		// montando listas de lancamentos
		for (Lancamento lnc : this.lancamentos) {
			if (!this.lanctosPorCategoria.containsKey(//
					lnc.getCategoria().hashCode()))
				this.lanctosPorCategoria // criando lista lancto por
											// categoria
						.put(lnc.getCategoria().hashCode(), new ArrayList<Lancamento>());

			this.lanctosPorCategoria.get(lnc.getCategoria().hashCode()).add(lnc);
		}
	}
}
