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
package net.sourceforge.plantuml.skin;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.graphic.AbstractTextBlock;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.SymbolContext;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.ugraphic.UEllipse;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UPath;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class ActorAwesome extends AbstractTextBlock implements TextBlock {

	private final double headDiam = 32;
	private final double bodyWidth = 54;
	private final double shoulder = 16;
	private final double collar = 4;
	private final double radius = 8;
	private final double bodyHeight = 28;

	private final SymbolContext symbolContext;

	public ActorAwesome(SymbolContext symbolContext) {
		this.symbolContext = symbolContext.withStroke(new UStroke(1.5));
	}

	public void drawU(UGraphic ug) {

		final UEllipse head = new UEllipse(headDiam, headDiam);
		final double centerX = getPreferredWidth() / 2;

		final UPath path = new UPath();
		path.moveTo(0, collar);
		path.cubicTo(collar, collar, bodyWidth / 2 - shoulder - collar, collar, bodyWidth / 2 - shoulder, 0);
		path.cubicTo(bodyWidth / 2 - shoulder / 2, 0, bodyWidth / 2, shoulder / 2, bodyWidth / 2, shoulder);
		path.lineTo(bodyWidth / 2, bodyHeight - radius);
		path.cubicTo(bodyWidth / 2, bodyHeight - radius / 2, bodyWidth / 2 - radius / 2, bodyHeight, bodyWidth / 2
				- radius, bodyHeight);
		path.lineTo(-bodyWidth / 2 + radius, bodyHeight);
		path.cubicTo(-bodyWidth / 2 + radius / 2, bodyHeight, -bodyWidth / 2, bodyHeight - radius / 2, -bodyWidth / 2,
				bodyHeight - radius);
		path.lineTo(-bodyWidth / 2, shoulder);
		path.cubicTo(-bodyWidth / 2, shoulder / 2, -bodyWidth / 2 + shoulder / 2, 0, -bodyWidth / 2 + shoulder, 0);
		path.cubicTo(-bodyWidth / 2 + shoulder + collar, collar, -collar, collar, 0, collar);
		path.closePath();

		if (symbolContext.getDeltaShadow() != 0) {
			head.setDeltaShadow(symbolContext.getDeltaShadow());
			path.setDeltaShadow(symbolContext.getDeltaShadow());
		}
		ug = symbolContext.apply(ug);
		ug.apply(new UTranslate(centerX - head.getWidth() / 2, thickness())).draw(head);
		ug.apply(new UTranslate(centerX, head.getHeight() + thickness())).draw(path);

	}

	private double thickness() {
		return symbolContext.getStroke().getThickness();
	}

	public double getPreferredWidth() {
		return bodyWidth + thickness() * 2;
	}

	public double getPreferredHeight() {
		return headDiam + bodyHeight + thickness() * 2;
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		return new Dimension2DDouble(getPreferredWidth(), getPreferredHeight());
	}
}
