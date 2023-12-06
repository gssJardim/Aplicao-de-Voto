package pt.ul.fc.css.project.javafx;

import javax.swing.*;

import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import pt.ul.fc.css.project.entities.enumerated.TipoDeVoto;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BorderLayoutDemo {
	public static void addComponentsToPane(Container pane) throws JSONException {
		if (!(pane.getLayout() instanceof BorderLayout)) {
			pane.add(new JLabel("Container doesn't use BorderLayout!"));
			return;
		}

		// Content
		CardLayout cards = new CardLayout();
		JPanel content = new JPanel(cards);
		content.add(criarProjetoLeiUI(), "criar-projeto-lei");
		content.add(verNaoExpiradosUI(), "ver-nao-expirados");
		content.add(verEmVotacaoUI(), "ver-em-votacao");
		content.add(apoiarUI(), "apoiar");
		content.add(votarUI(), "votar");
		pane.add(content, BorderLayout.CENTER);

		// Navigation menu
		JPanel navigationMenu = new JPanel();
		navigationMenu.setLayout(new BoxLayout(navigationMenu, BoxLayout.Y_AXIS));

		// Criar botões
		JButton criarConta = new JButton("Criar conta");
		JButton login = new JButton("Login");
		JButton logout = new JButton("Logout");
		JButton criar = new JButton("Criar");
		JButton verNaoExpirados = new JButton("Ver não expirados");
		JButton verEmVotacao = new JButton("Ver em votação");
		JButton apoiar = new JButton("Apoiar");
		JButton votar = new JButton("Votar");
		JButton escolherDelegado = new JButton("Escolher delegado");
		JButton tornameDelegado = new JButton("Torna-me delegado");

		// Adicionar os event listeners
		criar.addActionListener(e -> cards.show(content, "criar-projeto-lei"));
		verNaoExpirados.addActionListener(e -> cards.show(content, "ver-nao-expirados"));
		content.add(verNaoExpiradosUI(), "ver-nao-expirados");
		verEmVotacao.addActionListener(e -> cards.show(content, "ver-em-votacao"));
		apoiar.addActionListener(e -> cards.show(content, "apoiar"));
		votar.addActionListener(e -> cards.show(content, "votar"));
		escolherDelegado.addActionListener(e -> cards.show(content, "escolher-delegado"));

		// Alinhar os botões
		criarConta.setAlignmentX(Component.CENTER_ALIGNMENT);
		login.setAlignmentX(Component.CENTER_ALIGNMENT);
		logout.setAlignmentX(Component.CENTER_ALIGNMENT);
		criar.setAlignmentX(Component.CENTER_ALIGNMENT);
		verNaoExpirados.setAlignmentX(Component.CENTER_ALIGNMENT);
		verEmVotacao.setAlignmentX(Component.CENTER_ALIGNMENT);
		apoiar.setAlignmentX(Component.CENTER_ALIGNMENT);
		votar.setAlignmentX(Component.CENTER_ALIGNMENT);
		escolherDelegado.setAlignmentX(Component.CENTER_ALIGNMENT);
		tornameDelegado.setAlignmentX(Component.CENTER_ALIGNMENT);
		navigationMenu.add(criarConta);
		navigationMenu.add(login);
		navigationMenu.add(logout);
		navigationMenu.add(criar);
		navigationMenu.add(verNaoExpirados);
		navigationMenu.add(verEmVotacao);
		navigationMenu.add(apoiar);
		navigationMenu.add(votar);
		navigationMenu.add(escolherDelegado);
		navigationMenu.add(tornameDelegado);
		pane.add(navigationMenu, BorderLayout.LINE_START);
	}
	
	private static JPanel criarProjetoLeiUI() throws JSONException {
		JPanel jp = new JPanel();
		jp.setLayout(new BoxLayout(jp, BoxLayout.Y_AXIS));

		JLabel tituloPagina = new JLabel();
		tituloPagina.setText("Criar Projeto lei");
		tituloPagina.setFont(new Font("Verdana", Font.PLAIN, 15));

		JLabel tituloLabel = new JLabel();
		tituloLabel.setText("Titulo:");
		JTextField tituloInput = new JTextField();
		tituloInput.setMaximumSize(new Dimension(Integer.MAX_VALUE, tituloInput.getPreferredSize().height));

		JLabel textoLabel = new JLabel();
		textoLabel.setText("Texto:");
		JTextField textoInput = new JTextField();
		textoInput.setMaximumSize(new Dimension(Integer.MAX_VALUE, textoInput.getPreferredSize().height));

		JLabel pdfLabel = new JLabel();
		pdfLabel.setText("PDF:");
		JTextField pdfInput = new JTextField();
		pdfInput.setEditable(false);
		pdfInput.setMaximumSize(new Dimension(Integer.MAX_VALUE, pdfInput.getPreferredSize().height));
		JButton uploadButton = new JButton("Upload");
		uploadButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				int result = fileChooser.showOpenDialog(jp);
				if (result == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					try {
						byte[] fileBytes = Files.readAllBytes(selectedFile.toPath());
						String base64String = Base64.getEncoder().encodeToString(fileBytes);
						pdfInput.setText(base64String);
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}
			}
		});

		JLabel dataValidadeLabel = new JLabel();
		dataValidadeLabel.setText("Data Inicio:");
		JTextField dataValidadeField = new JTextField(10);
		JButton dateValidadePickerButton = new JButton("...");
		dateValidadePickerButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				JFrame parentFrame = (JFrame) SwingUtilities.getRoot((Component) e.getSource());

				JSpinner spinner = new JSpinner(new SpinnerDateModel());
				JSpinner.DateEditor editor = new JSpinner.DateEditor(spinner, "yyyy-MM-dd");
				spinner.setEditor(editor);

				try {
					SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
					Calendar selectedDate = Calendar.getInstance();
					selectedDate.setTime(dateFormatter.parse(dataValidadeField.getText()));
					spinner.setValue(selectedDate.getTime());
				} catch (ParseException ex) {
					// Handle parsing exception
				}

				int option = JOptionPane.showOptionDialog(parentFrame, spinner, "Select Date",
						JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);

				if (option == JOptionPane.OK_OPTION) {
					Calendar selectedDate = Calendar.getInstance();
					selectedDate.setTime((java.util.Date) spinner.getValue());

					SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
					dataValidadeField.setText(dateFormatter.format(selectedDate.getTime()));
				}
			}
		});
		JLabel temaLabel = new JLabel();
		temaLabel.setText("Tema");
		JSONObject json = get("http://localhost:8080/api/temas");
		JSONArray temasJSONArray = json.getJSONArray("data");
		ArrayList<String> temasNames = new ArrayList<String>();
		ArrayList<Long> temasIds = new ArrayList<Long>();
		for (int i = 0; i < temasJSONArray.length(); i++) {
		    JSONObject temaJSON = temasJSONArray.getJSONObject(i);
		    String name = temaJSON.getString("nome");
		    Long id = temaJSON.getLong("id");
		    temasNames.add(name);
		    temasIds.add(id);
		}
		JComboBox<String> temaComboBox = new JComboBox<String>(temasNames.toArray(new String[0]));
		temaComboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, temaComboBox.getPreferredSize().height));

		JLabel delegadoLabel = new JLabel();
		delegadoLabel.setText("Delegado");
		JSONObject jsonDel = get("http://localhost:8080/api/delegados");
		JSONArray delegadosJSONArray = jsonDel.getJSONArray("data");
		ArrayList<String> delegadosNames = new ArrayList<String>();
		ArrayList<Long> delegadosIds = new ArrayList<Long>();
		for (int i = 0; i < delegadosJSONArray.length(); i++) {
		    JSONObject delegadoJSON = delegadosJSONArray.getJSONObject(i);
		    String name = delegadoJSON.getString("nome");
		    Long id = delegadoJSON.getLong("id");
		    delegadosNames.add(name);
		    delegadosIds.add(id);
		}
		JComboBox<String> delegadoComboBox = new JComboBox<String>(delegadosNames.toArray(new String[0]));
		delegadoComboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, delegadoComboBox.getPreferredSize().height));

		JLabel errorMessage = new JLabel();
		errorMessage.setForeground(Color.RED);
		JLabel successMessage = new JLabel();
		successMessage.setForeground(Color.GREEN);

		JButton button = new JButton("Criar");
		button.addActionListener(e -> {
			// Ler toda a informação do form
			String titulo = tituloInput.getText();
			System.out.println("tituloInput getText()" + titulo);
			String texto = textoInput.getText();
			System.out.println("textoInput getText()" + texto);
			String anexoPDF = pdfInput.getText();
			System.out.println("anexoPDF getText()" + anexoPDF);
			String dataValidade = dataValidadeField.getText();
			Long tema = temasIds.get(temasNames.indexOf(String.valueOf(temaComboBox.getSelectedItem())));
			Long proponente = delegadosIds
					.get(delegadosNames.indexOf(String.valueOf(delegadoComboBox.getSelectedItem())));

			// Montar string em formato JSON
			String body = String.format(
					"{ \"titulo\": \"%s\", \"texto\": \"%s\", \"anexoPDF\": \"%s\", \"dataValidade\": \"%s\", \"tema\": %d, \"proponente\": %d}",
					titulo, texto, anexoPDF, 
					dataValidade, tema, proponente);

			// Validações
			errorMessage.setText("");
			if (titulo.length() == 0) {
				errorMessage.setText("Titulo não pode estar vazio");
				return;
			}
			if (texto.length() == 0) {
				errorMessage.setText("Texto não pode estar vazio");
				return;
			}
			if (anexoPDF.length() == 0) {
				errorMessage.setText("PDF não pode estar vazio");
				return;
			}
			if (dataValidade.length() == 0) {
				errorMessage.setText("Data de Inicio não pode estar vazio");
				return;
			}
			if (tema == 0) {
				errorMessage.setText("Tema não pode estar vazio");
				return;
			}
			if (proponente == 0) {
				errorMessage.setText("Delegado não pode estar vazio");
				return;
			}

			// Fazer pedido POST ao servidor
			HttpResponse<String> response = post("http://localhost:8080/api/apresentar-projeto", body);

			if (response.statusCode() == 200) {
				successMessage.setText("Projeto criado com sucesso!");
			} else {
				errorMessage.setText("Houve um erro desconhecido!");
			}
		});

		jp.add(tituloPagina);

		jp.add(tituloLabel);
		jp.add(tituloInput);

		jp.add(textoLabel);
		jp.add(textoInput);

		jp.add(pdfLabel);
		jp.add(pdfInput);
		jp.add(uploadButton);

		jp.add(dataValidadeLabel);
		jp.add(dataValidadeField);
		jp.add(dateValidadePickerButton);

		jp.add(temaLabel);
		jp.add(temaComboBox);

		jp.add(delegadoLabel);
		jp.add(delegadoComboBox);

		jp.add(successMessage);
		jp.add(errorMessage);

		jp.add(button);
		return jp;
	}
	
	private static JPanel verNaoExpiradosUI() throws JSONException {
		JPanel jp = new JPanel();
		jp.setLayout(new BoxLayout(jp, BoxLayout.Y_AXIS));

		JLabel tituloPagina = new JLabel();
		tituloPagina.setText("Ver Projetos Lei Não Expirados");
		tituloPagina.setFont(new Font("Verdana", Font.PLAIN, 15));

		jp.add(tituloPagina);

		JSONObject jsonExp = get("http://localhost:8080/api/projetosnaoexpirados");
		JSONArray ExpiredJSONArray = jsonExp.getJSONArray("data");
		System.out.println(ExpiredJSONArray);
		for (int i = 0; i < ExpiredJSONArray.length(); i++) {
		    JSONObject expiredJSON = ExpiredJSONArray.getJSONObject(i);
		    String titulo = expiredJSON.getString("titulo");
		    String texto = expiredJSON.getString("texto");
		    //String pdf = votJSON.getString("pdf");
		    String dataValidade = expiredJSON.getString("dataValidade");
		    String tema = expiredJSON.getString("tema");
		    String proponente = expiredJSON.getString("proponente");

		    JLabel tituloLabel = new JLabel();
		    tituloLabel.setFont(new Font("Verdana", Font.PLAIN, 15));
		    tituloLabel.setText(titulo);

		    JLabel textoLabel = new JLabel();
		    textoLabel.setText("Texto: " + texto);

		    //JLabel pdfLabel = new JLabel();
		    //pdfLabel.setText("PDF: " + pdf);

		    JLabel startDateLabel = new JLabel();
		    startDateLabel.setText("Data Validade: " + dataValidade);

		    JLabel temaLabel = new JLabel();
		    temaLabel.setText("Tema: " + tema);

		    JLabel delegadoLabel = new JLabel();
		    delegadoLabel.setText("Delegado: " + proponente);

		    jp.add(tituloLabel);
		    jp.add(textoLabel);
		    //jp.add(pdfLabel);
		    jp.add(startDateLabel);
		    jp.add(temaLabel);
		}
		return jp;
	}
	
	private static JPanel verEmVotacaoUI() throws JSONException {
		JPanel jp = new JPanel();
		jp.setLayout(new BoxLayout(jp, BoxLayout.Y_AXIS));

		JLabel tituloPagina = new JLabel();
		tituloPagina.setText("Ver em Votação");
		tituloPagina.setFont(new Font("Verdana", Font.PLAIN, 15));

		jp.add(tituloPagina);

		JSONObject jsonVot = get("http://localhost:8080/api/votacoes");
		//System.out.println( jsonVot );
		JSONArray VotationSONArray = jsonVot.getJSONArray("data");
		System.out.println(VotationSONArray);
		for (int i = 0; i < VotationSONArray.length(); i++) {
		    JSONObject votJSON = VotationSONArray.getJSONObject(i);
		    String id = votJSON.getString("id");
		    String projetoLeiDTO = votJSON.getString("projetoLeiDTO");
		    String dataFecho = votJSON.getString("dataFecho");
		    String votosDeDelegados = votJSON.getString("votosDeDelegados");
		    String aFavor = votJSON.getString("aFavor");
		    String estadoVotacao = votJSON.getString("estadoVotacao");
		    String eContra = votJSON.getString("econtra");

		    JLabel idLabel = new JLabel();
		    idLabel.setFont(new Font("Verdana", Font.PLAIN, 15));
		    idLabel.setText(id);

		    JLabel projetoLeiLabel = new JLabel();
		    projetoLeiLabel.setText("Projeto Lei: " + projetoLeiDTO);
		    
		    JLabel dataFechoLabel = new JLabel();
		    dataFechoLabel.setText("Expira em: " + dataFecho);
		    
		    JLabel votosDeDelegadosLabel = new JLabel();
		    votosDeDelegadosLabel.setText("Votos de delegados: " + votosDeDelegados);

		    JLabel aFavorLabel = new JLabel();
		    aFavorLabel.setText("Nº a favor: " + aFavor);
		    
		    JLabel estadoVotacaoLabel = new JLabel();
		    estadoVotacaoLabel.setText("Votos de delegados: " + estadoVotacao);
		    
		    JLabel eContraLabel = new JLabel();
		    eContraLabel.setText("Nº contra: " + eContra);



		    jp.add(idLabel);
		    jp.add(projetoLeiLabel);
		    jp.add(dataFechoLabel);
		    jp.add(votosDeDelegadosLabel);
		    jp.add(aFavorLabel);
		    jp.add(estadoVotacaoLabel);
		    jp.add(eContraLabel);
		}

		return jp;
	}
	
	private static JPanel apoiarUI() throws JSONException {
		JPanel jp = new JPanel();
		jp.setLayout(new BoxLayout(jp, BoxLayout.Y_AXIS));

		JLabel tituloPagina = new JLabel();
		tituloPagina.setText("Apoiar");
		tituloPagina.setFont(new Font("Verdana", Font.PLAIN, 15));

		JLabel cidadaoLabel = new JLabel();
		cidadaoLabel.setText("Cidadão");
		JSONObject jsonCid = get("http://localhost:8080/api/cidadaos");
		JSONArray cidadaosJSONArray = jsonCid.getJSONArray("data");
		ArrayList<String> cidadaosNames = new ArrayList<String>();
		ArrayList<Long> cidadaosIds = new ArrayList<Long>();
		for (int i = 0; i < cidadaosJSONArray.length(); i++) {
		    JSONObject cidadaoJSON = cidadaosJSONArray.getJSONObject(i);
		    String name = cidadaoJSON.getString("nome");
		    Long id = cidadaoJSON.getLong("id");
		    cidadaosNames.add(name);
		    cidadaosIds.add(id);
		}
		JComboBox<String> cidadaoComboBox = new JComboBox<String>(cidadaosNames.toArray(new String[0]));
		cidadaoComboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, cidadaoComboBox.getPreferredSize().height));

		JLabel projetoLabel = new JLabel();
		projetoLabel.setText("Projeto Lei");
		JSONObject jsonPL = get("http://localhost:8080/api/projetos");
		JSONArray projetosJSONArray = jsonPL.getJSONArray("data");
		ArrayList<String> projetosTitles = new ArrayList<String>();
		ArrayList<Long> projetosIds = new ArrayList<Long>();
		for (int i = 0; i < projetosJSONArray.length(); i++) {
		    JSONObject projetoJSON = projetosJSONArray.getJSONObject(i);
		    String titulo = projetoJSON.getString("titulo");
		    Long id = projetoJSON.getLong("id");
		    projetosTitles.add(titulo);
		    projetosIds.add(id);
		}
		JComboBox<String> projetoComboBox = new JComboBox<String>(projetosTitles.toArray(new String[0]));
		projetoComboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, projetoComboBox.getPreferredSize().height));

		JLabel errorMessage = new JLabel();
		errorMessage.setForeground(Color.RED);
		JLabel successMessage = new JLabel();
		successMessage.setForeground(Color.GREEN);
		
		JButton button = new JButton("Apoiar");
		button.addActionListener(e -> {
			// Ler toda a informação do form
			Long cidadaoId = cidadaosIds.get(cidadaosNames.indexOf(String.valueOf(cidadaoComboBox.getSelectedItem())));
			Long projetoLeiId = projetosIds
					.get(projetosTitles.indexOf(String.valueOf(projetoComboBox.getSelectedItem())));

			// Montar string em formato JSON
			String body = String.format(
					"{ \"cidadaoId\": %d, \"projetoLeiId\": %d }", cidadaoId, projetoLeiId);
			//System.out.println(body);
			
			String url = String.format("http://localhost:8080/api/projeto/apoiar?cidadaoId=%d&projetoLeiId=%d", cidadaoId, projetoLeiId);

			// Fazer pedido PATCH ao servidor
			HttpResponse<String> response = patch(url, body); //erro
			// System.out.println(response.body());
			
			if (response.statusCode() == 200) {
				successMessage.setText("Apoio adicionado com sucesso!");
			} else {
				errorMessage.setText("Houve um erro desconhecido!");
			}
		});

		jp.add(tituloPagina);
		jp.add(cidadaoLabel);
		jp.add(cidadaoComboBox);
		jp.add(projetoLabel);
		jp.add(projetoComboBox);
		jp.add(successMessage);
		jp.add(errorMessage);
		jp.add(button);

		return jp;
	}
	
	private static JPanel votarUI() throws JSONException {
	    JPanel jp = new JPanel();
	    jp.setLayout(new BoxLayout(jp, BoxLayout.Y_AXIS));

	    JLabel tituloPagina = new JLabel();
	    tituloPagina.setText("Votar");
	    tituloPagina.setFont(new Font("Verdana", Font.PLAIN, 15));

	    JLabel cidadaoLabel = new JLabel();
	    cidadaoLabel.setText("Cidadão");
	    JSONObject jsonCid = get("http://localhost:8080/api/cidadaos");
	    JSONArray cidadaosJSONArray = jsonCid.getJSONArray("data");
	    ArrayList<String> cidadaosNames = new ArrayList<String>();
	    ArrayList<Long> cidadaosIds = new ArrayList<Long>();
	    for (int i = 0; i < cidadaosJSONArray.length(); i++) {
	        JSONObject cidadaoJSON = cidadaosJSONArray.getJSONObject(i);
	        String name = cidadaoJSON.getString("nome");
	        Long id = cidadaoJSON.getLong("id");
	        cidadaosNames.add(name);
	        cidadaosIds.add(id);
	    }
	    JComboBox<String> cidadaoComboBox = new JComboBox<String>(cidadaosNames.toArray(new String[0]));
	    cidadaoComboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, cidadaoComboBox.getPreferredSize().height));

	    JLabel votacaoLabel = new JLabel();
	    votacaoLabel.setText("Votacao");
	    JSONObject jsonVotar = get("http://localhost:8080/api/votacoes");
	    JSONArray votarJSONArray = jsonVotar.getJSONArray("data");
	    ArrayList<String> votacoesDesc = new ArrayList<String>();
	    ArrayList<Long> votacoesIds = new ArrayList<Long>();
	    for (int i = 0; i < votarJSONArray.length(); i++) {
	    	JSONObject votJSON = votarJSONArray.getJSONObject(i);
	    	Long id = votJSON.getLong("id");
		    String projetoLeiDTO = votJSON.getString("projetoLeiDTO");
		    String votosDeDelegados = votJSON.getString("votosDeDelegados");
		    String aFavor = votJSON.getString("aFavor");
		    String eContra = votJSON.getString("econtra");

		    votacoesDesc.add(projetoLeiDTO);
		    votacoesDesc.add(votosDeDelegados);
		    votacoesDesc.add(aFavor);
		    votacoesDesc.add(eContra);
		    votacoesIds.add(id);
	    }
	    JComboBox<String> projetoComboBox = new JComboBox<String>(votacoesDesc.toArray(new String[0]));
	    projetoComboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, projetoComboBox.getPreferredSize().height));

	    JLabel tipoDeVotoLabel = new JLabel();
	    tipoDeVotoLabel.setText("Tipo de voto");
	    JRadioButton inFavorRadioButton = new JRadioButton("a favor");
	    JRadioButton againstRadioButton = new JRadioButton("contra");
	    ButtonGroup buttonGroup = new ButtonGroup();
	    buttonGroup.add(inFavorRadioButton);
	    buttonGroup.add(againstRadioButton);

	    JLabel errorLabel = new JLabel("You must select whether the vote is in favor or against.");
	    errorLabel.setForeground(Color.RED);
	    errorLabel.setVisible(false);

	    // Add action listener to radio buttons
	    ActionListener radioButtonListener = new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            errorLabel.setVisible(false);
	        }
	    };

	    inFavorRadioButton.addActionListener(radioButtonListener);
	    againstRadioButton.addActionListener(radioButtonListener);

	    JLabel successMessage = new JLabel();
	    successMessage.setForeground(Color.GREEN);
	    JLabel errorMessage = new JLabel();
	    errorMessage.setForeground(Color.RED);

	    JButton voteButton = new JButton("Votar");
	    voteButton.addActionListener(e -> {
	        // Read all the form information
	        TipoDeVoto tipoDeVoto = inFavorRadioButton.isSelected() ? TipoDeVoto.FAVOR : TipoDeVoto.CONTRA;
	        Long cidadaoId = cidadaosIds.get(cidadaosNames.indexOf(String.valueOf(cidadaoComboBox.getSelectedItem())));
			Long votacaoId = votacoesIds
					.get(votacoesDesc.indexOf(String.valueOf(projetoComboBox.getSelectedItem())));

	        if (cidadaoId == null || votacaoId == null) {
	            errorMessage.setText("Failed to get the corresponding ID values.");
	            return;
	        }
	        
	        // Montar string em formato JSON
	        String body = String.format(
	                "{ \"cidadaoId\": %d, \"votacaoId\": %d, \"tipoDeVoto\": \"%s\" }", cidadaoId, votacaoId, tipoDeVoto.toString());

	        successMessage.setText("");

	        String url = String.format("http://localhost:8080/api/votacao/votar?cidadaoId=%d&votacaoId=%d&tipoDeVoto=%s", cidadaoId, votacaoId, tipoDeVoto.toString());

	        // Fazer pedido PATCH ao servidor
	        HttpResponse<String> response = patch(url, body);

	        if (response.statusCode() == 200) {
	            successMessage.setText("Votado com sucesso!");
	        } else {
	            errorMessage.setText("Não podes votar 2 vezes no mesmo projeto!");
	        }
	    });

	    // Add components to the panel
	    jp.add(tituloPagina);
	    jp.add(cidadaoLabel);
	    jp.add(cidadaoComboBox);
	    jp.add(votacaoLabel);
	    jp.add(projetoComboBox);
	    jp.add(tipoDeVotoLabel);
	    jp.add(inFavorRadioButton);
	    jp.add(againstRadioButton);
	    jp.add(errorLabel);
	    jp.add(successMessage);
	    jp.add(errorMessage);
	    jp.add(voteButton);

	    return jp;
	}
	
	public static void main(String[] args) {
		/* Use an appropriate Look and Feel */
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} catch (UnsupportedLookAndFeelException ex) {
			ex.printStackTrace();
		} catch (IllegalAccessException ex) {
			ex.printStackTrace();
		} catch (InstantiationException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}
		/* Turn off metal's use bold fonts */
		UIManager.put("swing.boldMetal", Boolean.FALSE);

		// Schedule a job for the event dispatch thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					createAndShowGUI();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	private static void createAndShowGUI() throws JSONException {
		// Create and set up the window.
		JFrame frame = new JFrame("BorderLayoutDemo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Set up the content pane.
		addComponentsToPane(frame.getContentPane());
		// Display the window.
		frame.pack();
		frame.setVisible(true);
	}
	
	public static HttpResponse<String> post(String url, String body) {
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(url))
				.POST(HttpRequest.BodyPublishers.ofString(body))
				.header("Content-Type", "application/json")
				.build();

		HttpResponse<String> response = null;
		try {
			response = client.send(request, HttpResponse.BodyHandlers.ofString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	public static JSONObject get(String uri) throws JSONException {
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(uri))
				.build();

		HttpResponse<String> response = null;
		try {
			response = client.send(request, BodyHandlers.ofString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new JSONObject(String.format("{ data: %s }", response.body()));
	}

	public static HttpResponse<String> patch(String url, String body) {
	    HttpClient client = HttpClient.newHttpClient();
	    HttpRequest request = HttpRequest.newBuilder()
	    		.uri(URI.create(url))
	    	    .method("PATCH", HttpRequest.BodyPublishers.ofString(body))
	    	    .header("Content-Type", "application/json")
	    	    .build();

	    HttpResponse<String> response = null;
	    try {
	        response = client.send(request, HttpResponse.BodyHandlers.ofString());
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return response;
	}
}
