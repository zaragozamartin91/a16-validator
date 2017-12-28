package ast.cts.ws;

import ast.cts.ws.a16.A16Doc;
import ast.cts.ws.a16.A16DocReader;
import ast.cts.ws.config.Configuration;
import ast.cts.ws.util.ConsolePrinter;
import ast.cts.ws.util.TypeComparator;
import ast.cts.ws.xsd.XsdReader;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Hello world!
 */
public class App {
	public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
		Configuration configuration = Configuration.getInstance();

		int nameCol = configuration.getNameCol();
		int typeCol = configuration.getTypeCol();
		String colDelim = configuration.getColDelim().delim;
		String inputName = configuration.getInputName();
		String outputName = configuration.getOutputName();
		boolean lenientRead = configuration.isLenientSwitchOn();
		int subtitleRowCount = configuration.getSubtitleRowCount();
		A16DocReader a16DocReader = new A16DocReader(nameCol, typeCol, colDelim, inputName, outputName, lenientRead, subtitleRowCount);

		File a16txtFile = selectFile("Seleccionar archivo de tablas a16", "txt");
		if (a16txtFile == null) { return; }
		InputStream a16stream = new FileInputStream(a16txtFile);
		A16Doc a16doc = a16DocReader.readA16Txt(a16stream);

		File xsdFile = selectFile("Seleccionar archivo XSD", "xsd");
		if (xsdFile == null) { return; }
		InputStream xsdStream = new FileInputStream(xsdFile);
		XsdReader xsdReader = XsdReader.fromStream(xsdStream);

		String xsdBasicTypePrefix = configuration.getXsdBasicTypePrefix();
		String xsdComplexTypePrefix = configuration.getXsdComplexTypePrefix();

		List<String> intAliases = configuration.getIntAliases();
		List<String> stringAliases = configuration.getStringAliases();
		List<String> decimalAliases = configuration.getDecimalAliases();
		TypeComparator typeComparator = new TypeComparator(intAliases, stringAliases, decimalAliases);

		MainValidator mainValidator = new MainValidator(a16doc, xsdReader, xsdBasicTypePrefix, xsdComplexTypePrefix, typeComparator);
		boolean lenientCheck = lenientRead;
		List<MainValidator.ValidatorMessage> validatorMessages = mainValidator.validate(lenientCheck);

		System.out.println();
		System.out.println("MENSAJES:");
		validatorMessages.forEach(valMsg -> {
			ConsolePrinter printer = valMsg.ok ? ConsolePrinter.OK : ConsolePrinter.ERROR;
			printer.println(valMsg.msg);
		});

		pressAnyKeyToContinue();
	}

	private static File selectFile(String title, String extension) {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File("./"));
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

	private static void pressAnyKeyToContinue() {
		System.out.println("Presiona ENTER para continuar...");
		try { System.in.read(); } catch (Exception e) { }
	}
}
