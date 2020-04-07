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
/*******************************************************************************
 * Copyright (c) 2015 EclipseSource.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package net.sourceforge.plantuml.json;

import java.io.IOException;
import java.io.Writer;


/**
 * A lightweight writing buffer to reduce the amount of write operations to be performed on the
 * underlying writer. This implementation is not thread-safe. It deliberately deviates from the
 * contract of Writer. In particular, it does not flush or close the wrapped writer nor does it
 * ensure that the wrapped writer is open.
 */
class WritingBuffer extends Writer {

  private final Writer writer;
  private final char[] buffer;
  private int fill = 0;

  WritingBuffer(Writer writer) {
    this(writer, 16);
  }

  WritingBuffer(Writer writer, int bufferSize) {
    this.writer = writer;
    buffer = new char[bufferSize];
  }

  @Override
  public void write(int c) throws IOException {
    if (fill > buffer.length - 1) {
      flush();
    }
    buffer[fill++] = (char)c;
  }

  @Override
  public void write(char[] cbuf, int off, int len) throws IOException {
    if (fill > buffer.length - len) {
      flush();
      if (len > buffer.length) {
        writer.write(cbuf, off, len);
        return;
      }
    }
    System.arraycopy(cbuf, off, buffer, fill, len);
    fill += len;
  }

  @Override
  public void write(String str, int off, int len) throws IOException {
    if (fill > buffer.length - len) {
      flush();
      if (len > buffer.length) {
        writer.write(str, off, len);
        return;
      }
    }
    str.getChars(off, off + len, buffer, fill);
    fill += len;
  }

  /**
   * Flushes the internal buffer but does not flush the wrapped writer.
   */
  @Override
  public void flush() throws IOException {
    writer.write(buffer, 0, fill);
    fill = 0;
  }

  /**
   * Does not close or flush the wrapped writer.
   */
  @Override
  public void close() throws IOException {
  }

}
