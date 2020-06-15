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
package net.sourceforge.plantuml.code;

import java.io.IOException;

import net.sourceforge.plantuml.zopfli.Options;
import net.sourceforge.plantuml.zopfli.Options.BlockSplitting;
import net.sourceforge.plantuml.zopfli.Options.OutputFormat;
import net.sourceforge.plantuml.zopfli.Zopfli;

public class CompressionZopfliZlib implements Compression {

	public byte[] compress(byte[] in) {
		if (in.length == 0) {
			return null;
		}
		int len = in.length * 2;
		if (len < 100) {
			len = 100;
		}
		final Zopfli compressor = new Zopfli(len);
		final Options options = new Options(OutputFormat.DEFLATE, BlockSplitting.FIRST, 30);

		return compressor.compress(options, in).getResult();
	}

	public byte[] decompress(byte[] in) throws IOException {
		return new CompressionZlib().decompress(in);
	}

}
