import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.util.ArrayList;
import java.util.List;

public class GeradorDeClasses extends JFrame {

    private JTextField txtNomeClasse;
    private JTextField txtPropriedade;
    private DefaultListModel<String> listModel;
    private JList<String> lstPropriedades;

    public GeradorDeClasses() {
        // Configurações básicas da janela
        setTitle("Gerador de Classes");
        setSize(450, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centraliza a janela
        setLayout(new BorderLayout(10, 10));

        // ── Painel Superior (Inputs) ─────────────────────────────────────────
        JPanel panelTop = new JPanel(new GridLayout(2, 2, 5, 5));
        panelTop.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panelTop.add(new JLabel("Nome da Classe:"));
        txtNomeClasse = new JTextField();
        panelTop.add(txtNomeClasse);

        panelTop.add(new JLabel("Nova Propriedade:"));
        JPanel panelAdd = new JPanel(new BorderLayout(5, 0));
        txtPropriedade = new JTextField();
        JButton btnAdd = new JButton("Adicionar");
        panelAdd.add(txtPropriedade, BorderLayout.CENTER);
        panelAdd.add(btnAdd, BorderLayout.EAST);
        panelTop.add(panelAdd);

        add(panelTop, BorderLayout.NORTH);

        // ── Lista Central ─────────────────────────
        listModel = new DefaultListModel<>();
        lstPropriedades = new JList<>(listModel);
        add(new JScrollPane(lstPropriedades), BorderLayout.CENTER);

        // ── Painel Inferior (Botões) ─────────────────────────────────────────
        JPanel panelBottom = new JPanel(new FlowLayout());
        JButton btnLimpar = new JButton("Limpar");
        JButton btnGerar = new JButton("Gerar Classe");

        panelBottom.add(btnLimpar);
        panelBottom.add(btnGerar);
        add(panelBottom, BorderLayout.SOUTH);

        // ── Eventos ───────────────
        btnLimpar.addActionListener(e -> {
            txtPropriedade.setText("");
            txtNomeClasse.setText("");
            listModel.clear();
        });

        btnAdd.addActionListener(e -> adicionarPropriedade());
        btnGerar.addActionListener(e -> gerarEExibirClasse());
    }

    // ── Lógica de Adicionar Propriedade ──────────────────────────────────────
    private void adicionarPropriedade() {
        String prop = txtPropriedade.getText().trim().toLowerCase();

        if (prop.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Digite o nome da propriedade antes de adicionar.",
             "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (listModel.contains(prop)) {
            JOptionPane.showMessageDialog(this, "Essa propriedade já foi adicionada.",
            "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        listModel.addElement(prop);
        txtPropriedade.setText("");
        txtPropriedade.requestFocus();
    }

    // ── Lógica Principal de Geração ──────────────────────────────────────────
    private void gerarEExibirClasse() {
        String nomeClasse = txtNomeClasse.getText().trim();

        if (nomeClasse.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe o nome da classe.",
            "Validação", JOptionPane.WARNING_MESSAGE);
            txtNomeClasse.requestFocus();
            return;
        }

        if (!Character.isLetter(nomeClasse.charAt(0))) {
            JOptionPane.showMessageDialog(this, "O nome da classe deve começar com uma letra.",
            "Validação", JOptionPane.WARNING_MESSAGE);
            txtNomeClasse.requestFocus();
            return;
        }

        if (listModel.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Adicione pelo menos uma propriedade à lista.",
            "Validação", JOptionPane.WARNING_MESSAGE);
            txtPropriedade.requestFocus();
            return;
        }

        // Coleta as propriedades da lista
        List<String> props = new ArrayList<>();
        for (int i = 0; i < listModel.size(); i++) {
            props.add(listModel.get(i));
        }

        String codigo = construirCodigoJava(nomeClasse, props);
        exibirJanelaResultado(nomeClasse, codigo);
    }

    // ── Construtor do Código em String ───────────────────────────────────────
    private String construirCodigoJava(String nomeClasse, List<String> props) {
        String i1 = "    ";
        String i2 = "        ";
        StringBuilder sb = new StringBuilder();

        sb.append("public class ").append(nomeClasse).append(" {\n\n");

        // Campos privados
        for (String p : props) {
            sb.append(i1).append("private String ").append(p).append(";\n");
        }
        sb.append("\n");

        // Construtor padrão
        sb.append(i1).append("public ").append(nomeClasse).append("() {\n");
        for (String p : props) {
            sb.append(i2).append("this.").append(p).append(" = \"\";\n");
        }
        sb.append(i1).append("}\n\n");

        // Construtor com parâmetros
        sb.append(i1).append("public ").append(nomeClasse).append("(");
        for (int i = 0; i < props.size(); i++) {
            sb.append("String ").append(props.get(i));
            if (i < props.size() - 1) sb.append(", ");
        }
        sb.append(") {\n");
        for (String p : props) {
            sb.append(i2).append("set").append(cap(p)).append("(").append(p).append(");\n");
        }
        sb.append(i1).append("}\n\n");

        // Getters e Setters
        for (String p : props) {
            // Get
            sb.append(i1).append("public String get").append(cap(p)).append("() {\n");
            sb.append(i2).append("return ").append(p).append(";\n");
            sb.append(i1).append("}\n\n");

            // Set (Com validação igual a do seu C#)
            sb.append(i1).append("public void set").append(cap(p)).append("(String valor) {\n");
            sb.append(i2).append("if (valor == null || valor.trim().isEmpty()) {\n");
            sb.append(i2).append("    throw new IllegalArgumentException(\"").append(cap(p)).append(" não pode ser vazio.\");\n");
            sb.append(i2).append("}\n");
            sb.append(i2).append("this.").append(p).append(" = valor.trim();\n");
            sb.append(i1).append("}\n\n");
        }

        // ToString
        sb.append(i1).append("@Override\n");
        sb.append(i1).append("public String toString() {\n");
        sb.append(i2).append("return \"").append(nomeClasse).append(" [");
        for (int i = 0; i < props.size(); i++) {
            sb.append(cap(props.get(i))).append(": \" + ").append(props.get(i));
            if (i < props.size() - 1) sb.append(" + \", ");
            else sb.append(" + \" ]\";\n");
        }
        sb.append(i1).append("}\n");
        sb.append("}\n");

        return sb.toString();
    }

    // ── Método Auxiliar: Primeira letra maiúscula ────────────────────────────
    private String cap(String s) {
        if (s == null || s.isEmpty()) return s;
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }

    // ── Tela de Resultado ────────────────────────────────────────────────────
    private void exibirJanelaResultado(String nomeClasse, String codigo) {
        JDialog dialog = new JDialog(this, "Classe Gerada: " + nomeClasse, true);
        dialog.setSize(620, 550);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JTextArea txtArea = new JTextArea(codigo);
        txtArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        txtArea.setBackground(new Color(30, 30, 30));
        txtArea.setForeground(new Color(220, 220, 220));
        txtArea.setEditable(false);
        dialog.add(new JScrollPane(txtArea), BorderLayout.CENTER);

        JButton btnCopiar = new JButton("Copiar Código");
        btnCopiar.setBackground(new Color(0, 122, 204));
        btnCopiar.setForeground(Color.BLACK);
        btnCopiar.setPreferredSize(new Dimension(0, 40));

        btnCopiar.addActionListener(e -> {
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(codigo), null);
            JOptionPane.showMessageDialog(dialog, "Código copiado!", "OK", JOptionPane.INFORMATION_MESSAGE);
        });

        dialog.add(btnCopiar, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    // ── Ponto de Entrada (Main) ──────────────────────────────────────────────
    public static void main(String[] args) {
        // Define o visual padrão do sistema operacional
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            new GeradorDeClasses().setVisible(true);
        });
    }
}