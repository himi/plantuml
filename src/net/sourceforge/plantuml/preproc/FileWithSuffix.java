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
package net.sourceforge.plantuml.preproc;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import net.sourceforge.plantuml.AFile;
import net.sourceforge.plantuml.AFileRegular;
import net.sourceforge.plantuml.AParentFolder;
import net.sourceforge.plantuml.Log;

public class FileWithSuffix {

	private final AFile file;
	private final String suffix;
	private final String entry;
	private final String description;

	@Override
	public String toString() {
		if (file == null) {
			return super.toString();
		}
		return file.toString();
	}

	public Reader getReader(String charset) throws IOException {
		if (file == null) {
			return null;
		}
		if (entry == null) {
			if (charset == null) {
				Log.info("Using default charset");
				return new InputStreamReader(file.open());
			}
			Log.info("Using charset " + charset);
			return new InputStreamReader(file.open(), charset);
		}
		final InputStream is = getDataFromZip(file.open(), entry);
		if (is == null) {
			return null;
		}
		if (charset == null) {
			Log.info("Using default charset");
			return new InputStreamReader(is);
		}
		Log.info("Using charset " + charset);
		return new InputStreamReader(is, charset);
	}

	private InputStream getDataFromZip(InputStream is, String name) throws IOException {
		final ZipInputStream zis = new ZipInputStream(is);
		ZipEntry ze = zis.getNextEntry();

		while (ze != null) {
			final String fileName = ze.getName();
			if (ze.isDirectory()) {
			} else if (fileName.equals(name)) {
				return zis;
			}
			ze = zis.getNextEntry();
		}
		zis.closeEntry();
		zis.close();
		return null;
	}

	public boolean fileOk() {
		return file != null && file.isOk();
	}

	FileWithSuffix(File file, String suffix) {
		this.file = new AFileRegular(file);
		this.suffix = suffix;
		this.entry = null;
		this.description = getFileName(file);
	}

	FileWithSuffix(String description, String suffix, AFile file, String entry) {
		this.description = description;
		this.suffix = suffix;
		this.file = file;
		this.entry = entry;
	}

	static FileWithSuffix none() {
		return new FileWithSuffix("NONE", null, null, null);
	}

	@Override
	public int hashCode() {
		int v = 0;
		if (file != null) {
			v += file.hashCode();
		}
		if (suffix != null) {
			v += suffix.hashCode() * 43;
		}
		if (entry != null) {
			v += entry.hashCode();
		}
		return v;
	}

	@Override
	public boolean equals(Object arg) {
		final FileWithSuffix other = (FileWithSuffix) arg;
		return this.file.equals(other.file) && equals(suffix, other.suffix) && same(entry, other.entry);
	}

	private static boolean same(String s1, String s2) {
		if (s1 == null && s2 == null) {
			return true;
		}
		if (s1 != null && s2 != null) {
			return s1.equals(s2);
		}
		return false;
	}

	public AParentFolder getParentFile() {
		if (file == null) {
			return null;
		}
		Log.info("Getting parent of " + file);
		Log.info("-->The parent is " + file.getParentFile());
		return file.getParentFile();
	}

	public String getDescription() {
		return description;
	}

	public final String getSuffix() {
		return suffix;
	}

	private static boolean equals(String s1, String s2) {
		if (s1 == null && s2 == null) {
			return true;
		}
		if (s1 != null && s2 != null) {
			return s1.equals(s2);
		}
		assert (s1 == null && s2 != null) || (s1 != null && s2 == null);
		return false;
	}

	public static Set<File> convert(Set<FileWithSuffix> all) {
		final Set<File> result = new HashSet<File>();
		for (FileWithSuffix f : all) {
			result.add(f.file.getUnderlyingFile());
		}
		return result;
	}

	public static String getFileName(File file) {
		return file.getName();
	}

}
