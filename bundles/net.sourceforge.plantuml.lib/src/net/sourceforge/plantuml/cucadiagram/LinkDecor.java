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
package net.sourceforge.plantuml.cucadiagram;

import net.sourceforge.plantuml.OptionFlags;
import net.sourceforge.plantuml.svek.extremity.ExtremityFactory;
import net.sourceforge.plantuml.svek.extremity.ExtremityFactoryArrow;
import net.sourceforge.plantuml.svek.extremity.ExtremityFactoryArrowAndCircle;
import net.sourceforge.plantuml.svek.extremity.ExtremityFactoryCircle;
import net.sourceforge.plantuml.svek.extremity.ExtremityFactoryCircleConnect;
import net.sourceforge.plantuml.svek.extremity.ExtremityFactoryCircleCross;
import net.sourceforge.plantuml.svek.extremity.ExtremityFactoryCircleCrowfoot;
import net.sourceforge.plantuml.svek.extremity.ExtremityFactoryCircleLine;
import net.sourceforge.plantuml.svek.extremity.ExtremityFactoryCrowfoot;
import net.sourceforge.plantuml.svek.extremity.ExtremityFactoryDiamond;
import net.sourceforge.plantuml.svek.extremity.ExtremityFactoryDoubleLine;
import net.sourceforge.plantuml.svek.extremity.ExtremityFactoryHalfArrow;
import net.sourceforge.plantuml.svek.extremity.ExtremityFactoryLineCrowfoot;
import net.sourceforge.plantuml.svek.extremity.ExtremityFactoryNotNavigable;
import net.sourceforge.plantuml.svek.extremity.ExtremityFactoryParenthesis;
import net.sourceforge.plantuml.svek.extremity.ExtremityFactoryPlus;
import net.sourceforge.plantuml.svek.extremity.ExtremityFactorySquarre;
import net.sourceforge.plantuml.svek.extremity.ExtremityFactoryTriangle;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public enum LinkDecor {

	NONE(2, false, 0), EXTENDS(30, false, 2), COMPOSITION(15, true, 1.3), AGREGATION(15, false, 1.3),
	NOT_NAVIGABLE(1, false, 0.5),

	CROWFOOT(10, true, 0.8), CIRCLE_CROWFOOT(14, false, 0.8), CIRCLE_LINE(10, false, 0.8), DOUBLE_LINE(7, false, 0.7),
	LINE_CROWFOOT(10, false, 0.8),

	ARROW(10, true, 0.5), ARROW_TRIANGLE(10, true, 0.8), ARROW_AND_CIRCLE(10, false, 0.5),

	CIRCLE(0, false, 0.5), CIRCLE_FILL(0, false, 0.5), CIRCLE_CONNECT(0, false, 0.5),
	PARENTHESIS(0, false, OptionFlags.USE_INTERFACE_EYE2 ? 0.5 : 1.0), SQUARE(0, false, 0.5),

	CIRCLE_CROSS(0, false, 0.5), PLUS(0, false, 1.5), HALF_ARROW(0, false, 1.5), SQUARRE_toberemoved(30, false, 0);

	private final double arrowSize;
	private final int margin;
	private final boolean fill;

	private LinkDecor(int margin, boolean fill, double arrowSize) {
		this.margin = margin;
		this.fill = fill;
		this.arrowSize = arrowSize;
	}

	public int getMargin() {
		return margin;
	}

	public boolean isFill() {
		return fill;
	}

	public double getArrowSize() {
		return arrowSize;
	}

	public ExtremityFactory getExtremityFactory(HColor backgroundColor) {
		if (this == LinkDecor.PLUS) {
			return new ExtremityFactoryPlus();
		} else if (this == LinkDecor.HALF_ARROW) {
			return new ExtremityFactoryHalfArrow();
		} else if (this == LinkDecor.ARROW_TRIANGLE) {
			return new ExtremityFactoryTriangle();
		} else if (this == LinkDecor.CROWFOOT) {
			return new ExtremityFactoryCrowfoot();
		} else if (this == LinkDecor.CIRCLE_CROWFOOT) {
			return new ExtremityFactoryCircleCrowfoot();
		} else if (this == LinkDecor.LINE_CROWFOOT) {
			return new ExtremityFactoryLineCrowfoot();
		} else if (this == LinkDecor.CIRCLE_LINE) {
			return new ExtremityFactoryCircleLine();
		} else if (this == LinkDecor.DOUBLE_LINE) {
			return new ExtremityFactoryDoubleLine();
		} else if (this == LinkDecor.CIRCLE_CROSS) {
			return new ExtremityFactoryCircleCross();
		} else if (this == LinkDecor.ARROW) {
			return new ExtremityFactoryArrow();
		} else if (this == LinkDecor.ARROW_AND_CIRCLE) {
			return new ExtremityFactoryArrowAndCircle();
		} else if (this == LinkDecor.NOT_NAVIGABLE) {
			return new ExtremityFactoryNotNavigable();
		} else if (this == LinkDecor.AGREGATION) {
			return new ExtremityFactoryDiamond(false, backgroundColor);
		} else if (this == LinkDecor.COMPOSITION) {
			return new ExtremityFactoryDiamond(true, backgroundColor);
		} else if (this == LinkDecor.CIRCLE) {
			return new ExtremityFactoryCircle(false);
		} else if (this == LinkDecor.CIRCLE_FILL) {
			return new ExtremityFactoryCircle(true);
		} else if (this == LinkDecor.SQUARE) {
			return new ExtremityFactorySquarre();
		} else if (this == LinkDecor.PARENTHESIS) {
			return new ExtremityFactoryParenthesis();
		} else if (this == LinkDecor.CIRCLE_CONNECT) {
			return new ExtremityFactoryCircleConnect();
		}

		return null;
	}

}
