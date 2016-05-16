package preprocessing;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSStream;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.PDGraphicsState;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDXObjectForm;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDXObjectImage;
import org.apache.pdfbox.util.Matrix;
import org.apache.pdfbox.util.PDFOperator;
import org.apache.pdfbox.util.PDFStreamEngine;
import org.apache.pdfbox.util.ResourceLoader;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class PrintImageLocations extends PDFStreamEngine
{
	private boolean flag = false;

	public boolean isFlag()
	{
		return flag;
	}

	public void setFlag(boolean flag)
	{
		this.flag = flag;
	}

	private static final String INVOKE_OPERATOR = "Do";

	/**
	 * Default constructor.
	 *
	 * @throws IOException
	 *             If there is an error loading text stripper properties.
	 */
	public PrintImageLocations() throws IOException
	{
		super(ResourceLoader.loadProperties("org/apache/pdfbox/resources/PDFTextStripper.properties", true));
	}

	Boolean[] image(File input) throws Exception
	{
		Boolean[] pagelist = null;
		PDDocument document = null;
		try
		{
			document = PDDocument.load(input);
			if (document.isEncrypted())
			{
				document.decrypt("");
			}
			PrintImageLocations printer = new PrintImageLocations();
			List allPages = document.getDocumentCatalog().getAllPages();
			pagelist = new Boolean[allPages.size()];
			for (int i = 0; i < allPages.size(); i++)
			{
				printer.setFlag(false);
				PDPage page = (PDPage) allPages.get(i);
				printer.processStream(page, page.findResources(), page.getContents().getStream());
				if (printer.isFlag())
				{
					pagelist[i] = true;
					System.out.println("page " + (i + 1) + " has image.");
				}
				else
				{
					pagelist[i] = false;
					System.out.println("page " + (i + 1) + " has not image.");
				}
			}
		} finally
		{
			if (document != null)
			{
				document.close();
			}
		}
		return pagelist;
	}

	/**
	 * This is used to handle an operation.
	 *
	 * @param operator
	 *            The operation to perform.
	 * @param arguments
	 *            The list of arguments.
	 *
	 * @throws IOException
	 *             If there is an error processing the operation.
	 */
	protected void processOperator(PDFOperator operator, List arguments) throws IOException
	{
		String operation = operator.getOperation();
		if (INVOKE_OPERATOR.equals(operation))
		{
			COSName objectName = (COSName) arguments.get(0);
			Map<String, PDXObject> xobjects = getResources().getXObjects();
			PDXObject xobject = (PDXObject) xobjects.get(objectName.getName());
			if (xobject instanceof PDXObjectImage)
			{
				this.flag = true;
			}
			else if (xobject instanceof PDXObjectForm)
			{
				// save the graphics state
				getGraphicsStack().push((PDGraphicsState) getGraphicsState().clone());
				PDPage page = getCurrentPage();

				PDXObjectForm form = (PDXObjectForm) xobject;
				COSStream invoke = (COSStream) form.getCOSObject();
				PDResources pdResources = form.getResources();
				if (pdResources == null)
				{
					pdResources = page.findResources();
				}
				// if there is an optional form matrix, we have to
				// map the form space to the user space
				Matrix matrix = form.getMatrix();
				if (matrix != null)
				{
					Matrix xobjectCTM = matrix.multiply(getGraphicsState().getCurrentTransformationMatrix());
					getGraphicsState().setCurrentTransformationMatrix(xobjectCTM);
				}
				processSubStream(page, pdResources, invoke);
				// restore the graphics state
				setGraphicsState((PDGraphicsState) getGraphicsStack().pop());
			}
		}
		else
		{
			super.processOperator(operator, arguments);
		}
	}
}
