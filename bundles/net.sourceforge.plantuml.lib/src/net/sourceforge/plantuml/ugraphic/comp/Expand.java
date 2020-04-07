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
package net.sourceforge.plantuml.ugraphic.comp;

public class Expand implements Comparable<Expand> {

	private final double position;
	private final double extend;
	private final ExpandType type;

	public Expand(ExpandType type, double position, double extend) {
		if (extend <= 0) {
			throw new IllegalArgumentException();
		}
		this.type = type;
		this.position = position;
		this.extend = extend;
	}

	@Override
	public String toString() {
		return "(" + position + "==>+" + extend + " " + type + ")";
	}

	public int compareTo(Expand other) {
		if (this.position < other.position) {
			return -1;
		}
		if (this.position > other.position) {
			return 1;
		}
		return 0;
	}

	public final double getPosition() {
		return position;
	}

	public final double getExtend() {
		return extend;
	}

	public final ExpandType getType() {
		return type;
	}

}
