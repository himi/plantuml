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
package net.sourceforge.plantuml.sequencediagram;

import java.text.DecimalFormat;

public class AutoNumber {

	private boolean running = false;
	private DottedNumber current;
	private int increment;

	private DecimalFormat format;
	private String last = "";

	public final void go(DottedNumber startingNumber, int increment, DecimalFormat format) {
		this.running = true;
		this.current = startingNumber;
		this.increment = increment;
		this.format = format;
	}

	public final void stop() {
		this.running = false;
	}

	public final void resume(DecimalFormat format) {
		this.running = true;
		if (format != null) {
			this.format = format;
		}
	}

	public final void resume(int increment, DecimalFormat format) {
		this.running = true;
		this.increment = increment;
		if (format != null) {
			this.format = format;
		}
	}

	public void incrementIntermediate() {
		current.incrementIntermediate();
	}

	public void incrementIntermediate(int position) {
		current.incrementIntermediate(position);
	}

	public String getNextMessageNumber() {
		if (running == false) {
			return null;
		}
		last = current.format(format);
		current.incrementMinor(increment);
		return last;
	}

	public String getCurrentMessageNumber(boolean formatted) {
		if (formatted) {
			return last;
		}
		return last.replace("<b>", "").replace("</b>", "");
	}
}
