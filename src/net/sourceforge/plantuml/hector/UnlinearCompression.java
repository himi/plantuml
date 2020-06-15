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
package net.sourceforge.plantuml.hector;

class UnlinearCompression {

	static enum Rounding {
		BORDER_1, CENTRAL, BORDER_2;
	}

	private final double inner;
	private final double outer;

	public UnlinearCompression(double inner, double outer) {
		this.inner = inner;
		this.outer = outer;
	}

	public double compress(double x) {
		final double pour = x / (inner + outer);
		final double pourInter = Math.floor(pour);
		x -= pourInter * (inner + outer);
		if (x < inner) {
			return pourInter * outer;
		}
		return x - inner + pourInter * outer;
	}

	public double uncompress(double x, Rounding rounding) {
		final int pourInter = nbOuterBefore(x);
		final boolean onBorder = equals(x, pourInter * outer);
		if (onBorder && rounding == Rounding.BORDER_1) {
			// Nothing
		} else if (onBorder && rounding == Rounding.CENTRAL) {
			x += inner / 2.0;
		} else {
			x += inner;
		}
		x += pourInter * inner;
		return x;
	}

	private static boolean equals(double d1, double d2) {
		return Math.abs(d1 - d2) < .001;
	}

	private int nbOuterBefore(double x) {
		final double pour = x / outer;
		final int pourInter = (int) Math.floor(pour);
		return pourInter;
	}

	public double[] encounteredSingularities(double from, double to) {
		final int outer1 = nbOuterBefore(from) + 1;
		int outer2 = nbOuterBefore(to) + 1;
		if (equals(to, (outer2 - 1) * outer)) {
			outer2--;
		}
		final double result[];
		if (from <= to) {
			result = new double[outer2 - outer1];
			for (int i = 0; i < result.length; i++) {
				result[i] = (outer1 + i) * outer;
			}
		} else {
			result = new double[outer1 - outer2];
			for (int i = 0; i < result.length; i++) {
				result[i] = (outer1 - 1 - i) * outer;
			}

		}
		return result;
	}

	public double innerSize() {
		return inner;
	}

}
