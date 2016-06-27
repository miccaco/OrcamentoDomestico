package br.com.orcamentoDomestico.form.detalhe;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import br.com.orcamentoDomestico.control.UsuarioControl;
import br.com.orcamentoDomestico.dao.entidade.Usuario;
import br.com.orcamentoDomestico.exception.CampoInvalidoRNException;
import ui.util.layouts.ParagraphLayout;

public class UsuarioRegistroForm extends JDialog implements ActionListener {
	private static final long serialVersionUID = -4286045236590371512L;

	// control
	protected Usuario model;
	protected UsuarioControl control;

	// panels
	protected JPanel pnData;
	protected JPanel pnActions;

	// inputs
	protected JLabel lbLogin;
	protected JTextField txLogin;

	protected JLabel lbNome;
	protected JTextField txNome;

	protected JLabel lbSenha;
	protected JPasswordField txSenha;

	// actions
	protected JButton btCancelar, btRegistrar;

	public UsuarioRegistroForm() {
		this.setTitle("Registra-se");
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setSize(300, 170);
		this.setModal(true);
		this.setResizable(false);
		this.setLocationRelativeTo(null);

		this.control = UsuarioControl.getInstance();

		this.doData();
		this.doActions();

		this.setVisible(true);
	}

	protected void doData() {
		this.pnData = new JPanel(new ParagraphLayout());

		this.lbNome = new JLabel("Nome:");
		this.txNome = new JTextField(20);

		this.pnData.add(this.lbNome, ParagraphLayout.NEW_PARAGRAPH);
		this.pnData.add(this.txNome);

		this.lbLogin = new JLabel("Login:");
		this.txLogin = new JTextField(20);

		this.pnData.add(this.lbLogin, ParagraphLayout.NEW_PARAGRAPH);
		this.pnData.add(this.txLogin);

		this.lbSenha = new JLabel("Senha:");
		this.txSenha = new JPasswordField(20);

		this.pnData.add(this.lbSenha, ParagraphLayout.NEW_PARAGRAPH);
		this.pnData.add(this.txSenha);

		getContentPane().add(this.pnData);
	}

	protected void doActions() {
		this.pnActions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		this.pnActions.setBackground(Color.GRAY);

		this.btRegistrar = new JButton("Registrar");
		this.btRegistrar.setName("registrar");
		this.btRegistrar.setBackground(this.pnActions.getBackground());
		this.pnActions.add(this.btRegistrar);

		this.btCancelar = new JButton("Cancelar");
		this.btCancelar.setName("cancelar");
		this.btCancelar.setBackground(this.pnActions.getBackground());
		this.pnActions.add(this.btCancelar);

		this.btRegistrar.addActionListener(this);
		this.btCancelar.addActionListener(this);

		getContentPane().add(this.pnActions, BorderLayout.SOUTH);
	}

	public Usuario getModel() {
		return this.model;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() instanceof Component) {
			Component c = (Component) e.getSource();

			if (c.getName().equals("registrar")) {
				Usuario model = new Usuario();

				model.setLogin(this.txLogin.getText());
				model.setNome(this.txNome.getText());
				model.setSenha(new String(this.txSenha.getPassword()));

				try {
					this.control.registrar(model);
					this.model = model;
					this.dispose();
				} catch (CampoInvalidoRNException ex) {
					JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro ao Registrar",
							JOptionPane.ERROR_MESSAGE);
					// ex.printStackTrace();
				}

			} else if (c.getName().equals("cancelar")) {
				this.dispose();
			}
		}
	}
}
