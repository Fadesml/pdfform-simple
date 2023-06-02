package ru.fadesml;

import com.itextpdf.io.font.PdfEncodings;
import org.apache.fontbox.util.autodetect.FontFileFinder;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDTrueTypeFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.apache.pdfbox.pdmodel.interactive.form.PDTextField;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

	public static Map<String, String> getData() {
		Map<String, String> data = new HashMap<>();
		data.put(
				"first_name",
				"Andrew"
		);
		data.put(
				"last_name",
				"Буняк"
		);
		data.put(
				"middle_name",
				"Александрович"
		);

		data.put(
				"null_field_example_key",
				"null_200"
		);

		return data;
	}

	public static void main(String[] args) throws IOException {
		PDDocument pdfDocument = PDDocument.load(new File("examples/form.pdf"));
		PDDocumentCatalog catalog = pdfDocument.getDocumentCatalog();
		PDAcroForm acroForm = catalog.getAcroForm();

		PDResources formResources = acroForm.getDefaultResources();
		final PDType0Font load = PDType0Font.load(
				pdfDocument,
				new FileInputStream("examples/timesnewroman.ttf"),
				true
		);

		System.out.println("LOAD: " + load);
		formResources.put(COSName.getPDFName("F0"), load);
		acroForm.exportFDF().getCatalog().getFDF().setEncoding("cp1251");

		putArguments(acroForm, getData());



		pdfDocument.save("examples/result/filled_form.pdf");
		pdfDocument.close();
	}

	public static void putArguments(
			PDAcroForm form,
			Map<String, String> data
	) {
		if (form != null) {
			for (Map.Entry<String, String> entry : data.entrySet()) {
				final PDField field = form.getField(entry.getKey());

				if (field != null) {
					try {
						System.out.println("Trying to set value and set default appearance for field: " + entry.getKey());
						PDTextField pdTextField = (PDTextField) field;
						pdTextField.setDefaultAppearance("/F0 0 Tf 0 g");

						field.setValue(entry.getValue());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}




































