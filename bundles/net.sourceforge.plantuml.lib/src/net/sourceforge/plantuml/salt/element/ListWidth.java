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
package net.sourceforge.plantuml.salt.element;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ListWidth {

	private final List<Double> allWidth = new ArrayList<Double>();

	public void add(double width) {
		this.allWidth.add(width);
	}

	public ListWidth mergeMax(ListWidth other) {
		final ListWidth result = new ListWidth();
		for (int i = 0; i < this.allWidth.size() || i < other.allWidth.size(); i++) {
			final double w1 = this.getWidthSafe(i);
			final double w2 = other.getWidthSafe(i);
			result.add(Math.max(w1, w2));
		}
		return result;
	}

	private double getWidthSafe(int i) {
		if (i < allWidth.size()) {
			return allWidth.get(i);
		}
		return 0;
	}

	public double getTotalWidthWithMargin(final double margin) {
		double result = 0;
		for (Double w : allWidth) {
			if (result > 0) {
				result += margin;
			}
			result += w;
		}
		return result;
	}

	public Iterator<Double> iterator() {
		return allWidth.iterator();
	}

}
