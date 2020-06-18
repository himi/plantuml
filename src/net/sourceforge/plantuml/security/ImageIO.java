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
package net.sourceforge.plantuml.security;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;

import javax.imageio.ImageReader;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;

public class ImageIO {

	public static ImageOutputStream createImageOutputStream(OutputStream os) throws IOException {
		return javax.imageio.ImageIO.createImageOutputStream(os);
	}

	public static void write(RenderedImage image, String format, OutputStream os) throws IOException {
		javax.imageio.ImageIO.write(image, format, os);
	}

	public static void write(RenderedImage image, String format, java.io.File file) throws IOException {
		javax.imageio.ImageIO.write(image, format, file);
	}

	public static void write(RenderedImage image, String format, SFile file) throws IOException {
		javax.imageio.ImageIO.write(image, format, file.conv());
	}

	public static BufferedImage read(java.io.File file) throws IOException {
		return javax.imageio.ImageIO.read(file);
	}

	public static BufferedImage read(SFile file) throws IOException {
		return javax.imageio.ImageIO.read(file.conv());
	}

	public static BufferedImage read(InputStream is) throws IOException {
		return javax.imageio.ImageIO.read(is);
	}

	public static ImageInputStream createImageInputStream(java.io.File file) throws IOException {
		return javax.imageio.ImageIO.createImageInputStream(file);
	}

	public static ImageInputStream createImageInputStream(SFile file) throws IOException {
		return javax.imageio.ImageIO.createImageInputStream(file.conv());
	}

	public static ImageInputStream createImageInputStream(Object obj) throws IOException {
		if (obj instanceof SFile) {
			obj = ((SFile) obj).conv();
		}
		return javax.imageio.ImageIO.createImageInputStream(obj);
	}

	public static ImageInputStream createImageInputStream(InputStream is) throws IOException {
		return javax.imageio.ImageIO.createImageInputStream(is);
	}

	public static Iterator<ImageReader> getImageReaders(ImageInputStream iis) {
		return javax.imageio.ImageIO.getImageReaders(iis);
	}

	public static Iterator<ImageWriter> getImageWritersBySuffix(String string) {
		return javax.imageio.ImageIO.getImageWritersBySuffix(string);
	}

}
