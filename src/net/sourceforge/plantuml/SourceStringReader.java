/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
 *
 * Project Info:  https://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * https://plantuml.com/patreon (only 1$ per month!)
 * https://plantuml.com/paypal
 * 
 * This file is part of PlantUML.
 *
 * THE ACCOMPANYING PROGRAM IS PROVIDED UNDER THE TERMS OF THIS ECLIPSE PUBLIC
 * LICENSE ("AGREEMENT"). [Eclipse Public License - v 1.0]
 * 
 * ANY USE, REPRODUCTION OR DISTRIBUTION OF THE PROGRAM CONSTITUTES
 * RECIPIENT'S ACCEPTANCE OF THIS AGREEMENT.
 * 
 * You may obtain a copy of the License at
 * 
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 *
 * Original Author:  Arnaud Roques
 */
package net.sourceforge.plantuml;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.graphic.GraphicStrings;
import net.sourceforge.plantuml.preproc.Defines;
import net.sourceforge.plantuml.security.SFile;
import net.sourceforge.plantuml.svek.TextBlockBackcolored;
import net.sourceforge.plantuml.ugraphic.ImageBuilder;
import net.sourceforge.plantuml.ugraphic.color.ColorMapperIdentity;

public class SourceStringReader {

	final private List<BlockUml> blocks;

	public SourceStringReader(String source) {
		this(Defines.createEmpty(), source, Collections.<String>emptyList());
	}

	public SourceStringReader(String source, String charset) {
		this(Defines.createEmpty(), source, "UTF-8", Collections.<String>emptyList());
	}

	public SourceStringReader(Defines defines, String source, List<String> config) {
		this(defines, source, "UTF-8", config);
	}

	public SourceStringReader(Defines defines, String source) {
		this(defines, source, "UTF-8", Collections.<String>emptyList());
	}

	public SourceStringReader(String source, SFile newCurrentDir) {
		this(Defines.createEmpty(), source, "UTF-8", Collections.<String>emptyList(), newCurrentDir);
	}

	public SourceStringReader(Defines defines, String source, String charset, List<String> config) {
		this(defines, source, charset, config, FileSystem.getInstance().getCurrentDir());
	}

	public SourceStringReader(Defines defines, String source, String charset, List<String> config, SFile newCurrentDir) {
		// // WARNING GLOBAL LOCK HERE
		// synchronized (SourceStringReader.class) {
		try {
			final BlockUmlBuilder builder = new BlockUmlBuilder(config, charset, defines, new StringReader(source),
					newCurrentDir, "string");
			this.blocks = builder.getBlockUmls();
		} catch (IOException e) {
			Log.error("error " + e);
			throw new IllegalStateException(e);
		}
		// }
	}

	@Deprecated
	public String generateImage(OutputStream os) throws IOException {
		return outputImage(os).getDescription();
	}

	public DiagramDescription outputImage(OutputStream os) throws IOException {
		return outputImage(os, 0);
	}

	@Deprecated
	public String generateImage(SFile f) throws IOException {
		return outputImage(f).getDescription();
	}

	public DiagramDescription outputImage(SFile f) throws IOException {
		final OutputStream os = f.createBufferedOutputStream();
		DiagramDescription result = null;
		try {
			result = outputImage(os, 0);
		} finally {
			os.close();
		}
		return result;
	}

	@Deprecated
	public String generateImage(OutputStream os, FileFormatOption fileFormatOption) throws IOException {
		return outputImage(os, fileFormatOption).getDescription();
	}

	public DiagramDescription outputImage(OutputStream os, FileFormatOption fileFormatOption) throws IOException {
		return outputImage(os, 0, fileFormatOption);
	}

	@Deprecated
	public String generateImage(OutputStream os, int numImage) throws IOException {
		return outputImage(os, numImage).getDescription();
	}

	public DiagramDescription outputImage(OutputStream os, int numImage) throws IOException {
		return outputImage(os, numImage, new FileFormatOption(FileFormat.PNG));
	}

	@Deprecated
	public String generateImage(OutputStream os, int numImage, FileFormatOption fileFormatOption) throws IOException {
		return outputImage(os, numImage, fileFormatOption).getDescription();
	}

	public DiagramDescription outputImage(OutputStream os, int numImage, FileFormatOption fileFormatOption)
			throws IOException {
		if (blocks.size() == 0) {
			noStartumlFound(os, fileFormatOption, 42);
			return null;
		}
		for (BlockUml b : blocks) {
			final Diagram system = b.getDiagram();
			final int nbInSystem = system.getNbImages();
			if (numImage < nbInSystem) {
				// final CMapData cmap = new CMapData();
				final ImageData imageData = system.exportDiagram(os, numImage, fileFormatOption);
				// if (imageData.containsCMapData()) {
				// return system.getDescription().getDescription() + BackSlash.BS_N +
				// imageData.getCMapData("plantuml");
				// }
				return system.getDescription();
			}
			numImage -= nbInSystem;
		}
		Log.error("numImage is too big = " + numImage);
		return null;

	}

	public DiagramDescription generateDiagramDescription(int numImage, FileFormatOption fileFormatOption) {
		if (blocks.size() == 0) {
			return null;
		}
		for (BlockUml b : blocks) {
			final Diagram system = b.getDiagram();
			final int nbInSystem = system.getNbImages();
			if (numImage < nbInSystem) {
				// final ImageData imageData = system.exportDiagram(os, numImage,
				// fileFormatOption);
				// if (imageData.containsCMapData()) {
				// return
				// system.getDescription().withCMapData(imageData.getCMapData("plantuml"));
				// }
				return system.getDescription();
			}
			numImage -= nbInSystem;
		}
		Log.error("numImage is too big = " + numImage);
		return null;
	}

	public DiagramDescription generateDiagramDescription() {
		return generateDiagramDescription(0);
	}

	public DiagramDescription generateDiagramDescription(FileFormatOption fileFormatOption) {
		return generateDiagramDescription(0, fileFormatOption);
	}

	public DiagramDescription generateDiagramDescription(int numImage) {
		return generateDiagramDescription(numImage, new FileFormatOption(FileFormat.PNG));
	}

	public String getCMapData(int numImage, FileFormatOption fileFormatOption) throws IOException {
		if (blocks.size() == 0) {
			return null;
		}
		for (BlockUml b : blocks) {
			final Diagram system = b.getDiagram();
			final int nbInSystem = system.getNbImages();
			if (numImage < nbInSystem) {
				final ImageData imageData = system.exportDiagram(new NullOutputStream(), numImage, fileFormatOption);
				if (imageData.containsCMapData()) {
					return imageData.getCMapData("plantuml");
				}
				return null;
			}
			numImage -= nbInSystem;
		}
		return null;

	}

	private void noStartumlFound(OutputStream os, FileFormatOption fileFormatOption, long seed) throws IOException {
		final TextBlockBackcolored error = GraphicStrings.createForError(Arrays.asList("No @startuml/@enduml found"),
				fileFormatOption.isUseRedForError());
		final ImageBuilder imageBuilder = ImageBuilder.buildA(new ColorMapperIdentity(), false, null, null, null, 1.0,
				error.getBackcolor());
		imageBuilder.setUDrawable(error);
		imageBuilder.writeImageTOBEMOVED(fileFormatOption, seed, os);
	}

	public final List<BlockUml> getBlocks() {
		return Collections.unmodifiableList(blocks);
	}

}
