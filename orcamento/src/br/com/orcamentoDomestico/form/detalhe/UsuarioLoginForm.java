package br.com.orcamentoDomestico.form.detalhe;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import br.com.orcamentoDomestico.control.UsuarioControl;
import br.com.orcamentoDomestico.exception.LoginInvalidoRNException;
import ui.util.layouts.ParagraphLayout;

public class UsuarioLoginForm extends JDialog implements ActionListener {
	private static final long serialVersionUID = -4286045236590371512L;

	// control
	protected UsuarioControl control;

	// panels
	protected JPanel pnData;
	protected JPanel pnActions;

	// inputs
	protected JLabel lbLogin;
	protected JTextField txLogin;

	protected JLabel lbSenha;
	protected JPasswordField txSenha;

	// actions
	protected JButton btEntrar, btCancelar, btRegistrar;

	public UsuarioLoginForm() {
		this.setTitle("Entrar no Sistema");
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setSize(300, 140);
		this.setModal(true);
		this.setResizable(false);
		this.setLocationRelativeTo(null);

		this.control = UsuarioControl.getInstance();

		this.doData();
		this.doActions();

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				if (control.getUsuario() == null)
					System.exit(-1);
			}
		});

		this.setVisible(true);
	}

	protected void doData() {
		this.pnData = new JPanel(new ParagraphLayout());

		this.lbLogin = new JLabel("Login:");
		this.txLogin = new JTextField(20);

		this.pnData.add(this.lbLogin, ParagraphLayout.NEW_PARAGRAPH);
		this.pnData.add(this.txLogin);

		this.lbSenha = new JLabel("Senha:");
		this.txSenha = new JPasswordField(20);

		this.pnData.add(this.lbSenha, ParagraphLayout.NEW_PARAGRAPH);
		this.pnData.add(this.txSenha);

		this.add(this.pnData);
	}

	protected void doActions() {
		this.pnActions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		this.pnActions.setBackground(Color.GRAY);

		this.btEntrar = new JButton("Entrar");
		this.btEntrar.setName("entrar");
		this.btEntrar.setBackground(this.pnActions.getBackground());
		this.pnActions.add(this.btEntrar);

		this.btRegistrar = new JButton("Registrar");
		this.btRegistrar.setName("registrar");
		this.btRegistrar.setBackground(this.pnActions.getBackground());
		this.pnActions.add(this.btRegistrar);

		this.btCancelar = new JButton("Cancelar");
		this.btCancelar.setName("cancelar");
		this.btCancelar.setBackground(this.pnActions.getBackground());
		this.pnActions.add(this.btCancelar);

		this.btEntrar.addActionListener(this);
		this.btRegistrar.addActionListener(this);
		this.btCancelar.addActionListener(this);

		this.add(this.pnActions, BorderLayout.SOUTH);
	}

	public void registrarUsuario() {
		new UsuarioRegistroForm();

		if (this.control.getUsuario() != null)
			this.dispose();
	}

	public void logar() {

		String login = this.txLogin.getText();
		String senha = new String(this.txSenha.getPassword());

		try {
			this.control.validaLogin(login, senha);
			this.dispose();
		} catch (LoginInvalidoRNException e) {
			// e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Login ou senha informados não estão válidos!", "Falhar Autenticação",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof Component) {
			Component c = (Component) e.getSource();

			if (c.getName().equals("entrar")) {
				this.logar();
			} else if (c.getName().equals("registrar")) {
				this.registrarUsuario();
			} else if (c.getName().equals("cancelar")) {
				System.exit(-1);
			}
		}
	}
}
