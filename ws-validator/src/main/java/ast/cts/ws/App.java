package ast.cts.ws;

import ast.cts.ws.a16.A16ServiceDoc;
import ast.cts.ws.a16.A16ServiceDocReader;
import ast.cts.ws.config.Configuration;
import ast.cts.ws.util.ConsolePrinter;
import ast.cts.ws.util.TypeComparator;
import ast.cts.ws.xsd.XsdMemReader;
import ast.cts.ws.xsd.XsdReader;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Hello world!
 */
public class App {
	public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException, XPathExpressionException {
		Configuration configuration = Configuration.getInstance();

		A16ServiceDocReader a16ServiceDocReader = buildA16DocReader(configuration);

		File a16txtFile = selectFile("Seleccionar archivo de tablas a16", "txt");
		if (a16txtFile == null) { return; }
		InputStream a16stream = new FileInputStream(a16txtFile);
		A16ServiceDoc a16ServiceDoc = a16ServiceDocReader.readA16Txt(a16stream);

		File xsdFile = selectFile("Seleccionar archivo XSD", "xsd");
		if (xsdFile == null) { return; }
		InputStream xsdStream = new FileInputStream(xsdFile);
		//		XsdReader xsdReader = XsdFileReader.fromStream(xsdStream);
		XsdReader xsdReader = XsdMemReader.fromStream(xsdStream, configuration.isLenientComplexTypeNames(), configuration.isLenientElementNames());

		String xsdBasicTypePrefix = configuration.getXsdBasicTypePrefix();
		String xsdComplexTypePrefix = configuration.getXsdComplexTypePrefix();

		List<String> intAliases = configuration.getIntAliases();
		List<String> stringAliases = configuration.getStringAliases();
		List<String> decimalAliases = configuration.getDecimalAliases();
		TypeComparator typeComparator = new TypeComparator(intAliases, stringAliases, decimalAliases);

		MainValidator mainValidator = new MainValidator(a16ServiceDoc, xsdReader, xsdBasicTypePrefix, xsdComplexTypePrefix, typeComparator);
		List<MainValidator.ValidatorMessage> validatorMessages = mainValidator.validate();

		System.out.println();
		System.out.println("MENSAJES:");
		validatorMessages.forEach(valMsg -> {
			ConsolePrinter printer = valMsg.ok ? ConsolePrinter.OK : ConsolePrinter.ERROR;

			if (configuration.isOutModeAll() ||
					(valMsg.ok && configuration.isOutModeOk()) ||
					(!valMsg.ok && configuration.isOutModeError())) {
				printer.println(valMsg.msg);
			}

		});

		pressEnterToContinue();
	}

	private static A16ServiceDocReader buildA16DocReader(Configuration configuration) {
		int nameCol = configuration.getNameCol();
		int typeCol = configuration.getTypeCol();
		String colDelim = configuration.getColDelim().delim;
		String inputName = configuration.getInputName();
		String outputName = configuration.getOutputName();

		int subtitleRowCount = configuration.getSubtitleRowCount();
		return new A16ServiceDocReader(nameCol, typeCol, colDelim, inputName, outputName, subtitleRowCount);
	}

	private static File selectFile(String title, String extension) {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File("./"));
		FileNameExtensionFilter filter = new FileNameExtensionFilter("ARCHIVOS " + extension, extension, extension);
		fileChooser.setFileFilter(filter);
		fileChooser.setDialogTitle(title);
		fileChooser.showOpenDialog(null);
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

		File file = fileChooser.getSelectedFile();
		boolean cancelButtonPressed = file == null || !file.exists();
		if (cancelButtonPressed) { return null; }

		boolean extensionOk = file.getName().toLowerCase().endsWith(extension);
		if (extensionOk) {return file;}

		JOptionPane.showMessageDialog(null, "Extension esperada: " + extension, "Archivo invalido!", JOptionPane.ERROR_MESSAGE);
		return selectFile(title, extension);
	}

	private static void pressEnterToContinue() {
		System.out.println("Presiona ENTER para continuar...");
		try { System.in.read(); } catch (Exception e) { }
	}
}
