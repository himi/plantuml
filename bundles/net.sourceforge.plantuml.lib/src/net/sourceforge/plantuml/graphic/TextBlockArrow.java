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
package net.sourceforge.plantuml.graphic;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.Direction;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UPolygon;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class TextBlockArrow extends AbstractTextBlock implements TextBlock {

	private final double size;
	private final Direction arrow;
	private final HColor color;

	public TextBlockArrow(Direction arrow, FontConfiguration fontConfiguration) {
		if (arrow == null) {
			throw new IllegalArgumentException();
		}
		this.arrow = arrow;
		// this.size = fontConfiguration.getFont().getSize2D() * 0 + 30;
		this.size = fontConfiguration.getFont().getSize2D();
		this.color = fontConfiguration.getColor();

	}

	public void drawU(UGraphic ug) {
		ug = ug.apply(color.bg());
		ug = ug.apply(color);
		int triSize = (int) (size * .8 - 3);
		if (triSize % 2 == 1) {
			triSize--;
		}
		final UPolygon triangle = getTriangle(triSize);
		if (arrow == Direction.RIGHT || arrow == Direction.LEFT) {
			ug.apply(new UTranslate(2, (size - triSize) - 2)).draw(triangle);
		} else {
			ug.apply(new UTranslate(2, (size - triSize) - 2)).draw(triangle);
		}
	}

	private UPolygon getTriangle(int triSize) {
		final UPolygon triangle = new UPolygon();
		if (arrow == Direction.RIGHT) {
			triangle.addPoint(0, 0);
			triangle.addPoint(triSize, triSize / 2);
			triangle.addPoint(0, triSize);
			triangle.addPoint(0, 0);
		} else if (arrow == Direction.LEFT) {
			triangle.addPoint(triSize, 0);
			triangle.addPoint(0, triSize / 2);
			triangle.addPoint(triSize, triSize);
			triangle.addPoint(triSize, 0);
		} else if (arrow == Direction.UP) {
			triangle.addPoint(0, triSize);
			triangle.addPoint(triSize / 2, 0);
			triangle.addPoint(triSize, triSize);
			triangle.addPoint(0, triSize);
		} else if (arrow == Direction.DOWN) {
			triangle.addPoint(0, 0);
			triangle.addPoint(triSize / 2, triSize);
			triangle.addPoint(triSize, 0);
			triangle.addPoint(0, 0);
		} else {
			throw new IllegalStateException();
		}
		return triangle;
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		return new Dimension2DDouble(size, size);
	}
}
