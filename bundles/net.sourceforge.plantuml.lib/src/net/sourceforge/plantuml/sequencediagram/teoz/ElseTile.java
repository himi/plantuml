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
package net.sourceforge.plantuml.sequencediagram.teoz;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.SkinParamBackcolored;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.real.Real;
import net.sourceforge.plantuml.sequencediagram.Event;
import net.sourceforge.plantuml.sequencediagram.GroupingLeaf;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.ComponentType;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.ugraphic.UGraphic;

public class ElseTile extends AbstractTile implements TileWithCallbackY {

	private final Rose skin;
	private final ISkinParam skinParam;
	private final GroupingLeaf anElse;
	private final Tile parent;

	public Event getEvent() {
		return anElse;
	}

	@Override
	public double getYPoint(StringBounder stringBounder) {
		return 0;
	}

	public ElseTile(GroupingLeaf anElse, Rose skin, ISkinParam skinParam, Tile parent) {
		this.anElse = anElse;
		this.skin = skin;
		this.skinParam = skinParam;
		this.parent = parent;
	}

	public Component getComponent(StringBounder stringBounder) {
		// final Display display = Display.create(anElse.getTitle());
		final ISkinParam tmp = new SkinParamBackcolored(skinParam, anElse.getBackColorElement(),
				anElse.getBackColorGeneral());

		final Display display = Display.create(anElse.getComment());
		final Component comp = skin.createComponent(anElse.getUsedStyles(), ComponentType.GROUPING_ELSE, null, tmp,
				display);
		return comp;
	}

	public void drawU(UGraphic ug) {
		// final StringBounder stringBounder = ug.getStringBounder();
		// final Component comp = getComponent(stringBounder);
		// final Dimension2D dim = comp.getPreferredDimension(stringBounder);
		// final Real min = getMinX(stringBounder);
		// final Real max = getMaxX(stringBounder);
		// final Context2D context = (Context2D) ug;
		// double height = dim.getHeight();
		// // if (context.isBackground() && parent instanceof GroupingTile) {
		// // final double startingY = ((GroupingTile) parent).getStartY();
		// // final double totalParentHeight = parent.getPreferredHeight(stringBounder);
		// // height = totalParentHeight - (startingY - y);
		// // }
		// final Area area = new Area(max.getCurrentValue() - min.getCurrentValue(), height);
		// ug = ug.apply(new UTranslate(min.getCurrentValue(), 0));
		// comp.drawU(ug, area, context);
	}

	public double getPreferredHeight(StringBounder stringBounder) {
		final Component comp = getComponent(stringBounder);
		final Dimension2D dim = comp.getPreferredDimension(stringBounder);

		double height = dim.getHeight();
		if (anElse.getComment() != null) {
			height += 10;
		}
		return height;
	}

	public void addConstraints(StringBounder stringBounder) {
		// final Component comp = getComponent(stringBounder);
		// final Dimension2D dim = comp.getPreferredDimension(stringBounder);
		// final double width = dim.getWidth();
	}

	public Real getMinX(StringBounder stringBounder) {
		return parent.getMinX(stringBounder);
	}

	public Real getMaxX(StringBounder stringBounder) {
		final Component comp = getComponent(stringBounder);
		final Dimension2D dim = comp.getPreferredDimension(stringBounder);
		return getMinX(stringBounder).addFixed(dim.getWidth());
	}

	private double y;

	public void callbackY(double y) {
		this.y = y;
	}

	public double getCallbackY() {
		return y;
	}

}
