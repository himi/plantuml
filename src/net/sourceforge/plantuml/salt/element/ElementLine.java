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

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColorSet;

public class ElementLine extends AbstractElement {

	private final char separator;

	public ElementLine(char separator) {
		this.separator = separator;
	}

	public Dimension2D getPreferredDimension(StringBounder stringBounder, double x, double y) {
		return new Dimension2DDouble(10, 6);
	}

	public void drawU(UGraphic ug, int zIndex, Dimension2D dimToUse) {
		if (zIndex != 0) {
			return;
		}
		ug = ug.apply(new UChangeColor(HColorSet.instance().getColorIfValid("#AAAAAA")));
		double y2 = dimToUse.getHeight() / 2;
		if (separator == '=') {
			y2 = y2 - 1;
		}
		drawLine(ug, 0, y2, dimToUse.getWidth(), separator);
	}

	private static void drawLine(UGraphic ug, double x, double y, double widthToUse, char separator) {
		if (separator == '=') {
			ug.apply(new UStroke()).apply(new UTranslate(x, y)).draw(ULine.hline(widthToUse));
			ug.apply(new UStroke()).apply(new UTranslate(x, y + 2)).draw(ULine.hline(widthToUse));
		} else if (separator == '.') {
			ug.apply(new UStroke(1, 2, 1)).apply(new UTranslate(x, y)).draw(ULine.hline(widthToUse));
		} else if (separator == '-') {
			ug.apply(new UStroke()).apply(new UTranslate(x, y)).draw(ULine.hline(widthToUse));
		} else {
			ug.apply(new UStroke(1.5)).apply(new UTranslate(x, y)).draw(ULine.hline(widthToUse));
		}
	}

}
