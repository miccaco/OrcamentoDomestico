package src.br.com.orcamentoDomestico;

import javax.swing.UIManager;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import br.com.orcamentoDomestico.dao.ConnectionUtil;
import br.com.orcamentoDomestico.exception.LoginInvalidoRNException;
import br.com.orcamentoDomestico.form.MainForm;

public class Main {
	public static void main(String[] args) throws LoginInvalidoRNException {

		ConnectionUtil.connectionType = ConnectionUtil.MY_SQL;

		// UsuarioControl.getInstance().validaLogin("RODRIGO", "12345678");

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			UIManager.setLookAndFeel(NimbusLookAndFeel.class.getCanonicalName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		new MainForm();
	}
}
