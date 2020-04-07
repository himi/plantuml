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

public class TranscoderSmart2 implements Transcoder {

	// Legacy encoder
	private final Transcoder oldOne = new TranscoderImpl(new AsciiEncoder(), new ArobaseStringCompressor2(),
			new CompressionHuffman());
	private final Transcoder zlib = new TranscoderImpl(new AsciiEncoder(), new ArobaseStringCompressor2(),
			new CompressionZlib());
	private final Transcoder brotli = new TranscoderImpl(new AsciiEncoder(), new ArobaseStringCompressor2(),
			new CompressionBrotli());
	
	
	private final Transcoder zlibBase64 = new TranscoderImpl(new AsciiEncoderBase64(), new ArobaseStringCompressor2(),
			new CompressionZlib());
	private final Transcoder brotliBase64 = new TranscoderImpl(new AsciiEncoderBase64(), new ArobaseStringCompressor2(),
			new CompressionBrotli());
	private final Transcoder base64only = new TranscoderImpl(new AsciiEncoderBase64(), new ArobaseStringCompressor2(),
			new CompressionNone());
	private final Transcoder hexOnly = new TranscoderImpl(new AsciiEncoderHex(), new ArobaseStringCompressor2(),
			new CompressionNone());

	public String decode(String code) throws IOException {
		// Work in progress
		// See https://github.com/plantuml/plantuml/issues/117

		// Two char headers
		if (code.startsWith("0A")) {
			return zlibBase64.decode(code.substring(2));
		}
		if (code.startsWith("0B")) {
			return brotliBase64.decode(code.substring(2));
		}
		if (code.startsWith("0C")) {
			return base64only.decode(code.substring(2));
		}
		if (code.startsWith("0D")) {
			return hexOnly.decode(code.substring(2));
		}
		// Text prefix
		// Just a wild try: use them only for testing
		if (code.startsWith("-deflate-")) {
			return zlibBase64.decode(code.substring("-deflate-".length()));
		}
		if (code.startsWith("-brotli-")) {
			return brotliBase64.decode(code.substring("-brotli-".length()));
		}
		if (code.startsWith("-base64-")) {
			return base64only.decode(code.substring("-base64-".length()));
		}
		if (code.startsWith("-hex-")) {
			return hexOnly.decode(code.substring("-hex-".length()));
		}

		// Legacy decoding : you should not use it any more.
		if (code.startsWith("0")) {
			return brotli.decode(code.substring(1));
		}
		try {
			return zlib.decode(code);
		} catch (Exception ex) {
			return oldOne.decode(code);
		}
		// return zlib.decode(code);
	}

	public String encode(String text) throws IOException {
		// Right now, we still use the legacy encoding.
		// This will be changed in the incoming months
		return zlib.encode(text);
	}
}
