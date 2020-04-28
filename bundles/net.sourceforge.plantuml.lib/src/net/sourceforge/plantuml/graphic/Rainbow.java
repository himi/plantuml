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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.SkinParam;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColorSet;

public class Rainbow {

	private final static Rose rose = new Rose();

	private final List<HtmlColorAndStyle> colors = new ArrayList<HtmlColorAndStyle>();
	private final int colorArrowSeparationSpace;

	private Rainbow(int colorArrowSeparationSpace) {
		this.colorArrowSeparationSpace = colorArrowSeparationSpace;
	}

	@Override
	public String toString() {
		return colors.toString();
	}

	public static Rainbow none() {
		return new Rainbow(0);
	}
	
	public static Rainbow fromColor(HColor color) {
		if (color == null) {
			return Rainbow.none();
		}
		return Rainbow.build(new HtmlColorAndStyle(color));
	}

	public static Rainbow build(ISkinParam skinParam) {
		if (SkinParam.USE_STYLES()) {
			throw new IllegalStateException();
		}
		return fromColor(rose.getHtmlColor(skinParam, ColorParam.arrow));
	}

	public static Rainbow build(Style style, HColorSet set) {
		final HColor color = style.value(PName.LineColor).asColor(set);
		return fromColor(color);
	}



	public Rainbow withDefault(Rainbow defaultColor) {
		if (this.size() == 0) {
			return defaultColor;
		}
		return this;
	}

	public static Rainbow build(HtmlColorAndStyle color) {
		if (color == null) {
			throw new IllegalArgumentException();
		}
		final Rainbow result = new Rainbow(0);
		result.colors.add(color);
		return result;
	}

	public static Rainbow build(ISkinParam skinParam, String colorString, int colorArrowSeparationSpace) {
		if (colorString == null) {
			return Rainbow.none();
		}
		final Rainbow result = new Rainbow(colorArrowSeparationSpace);
		for (String s : colorString.split(";")) {
			result.colors.add(HtmlColorAndStyle.build(skinParam, s));
		}
		return result;
	}

	public boolean isInvisible() {
		for (HtmlColorAndStyle style : colors) {
			if (style.getStyle().isInvisible()) {
				return true;
			}
		}
		return false;
	}

	public List<HtmlColorAndStyle> getColors() {
		return Collections.unmodifiableList(colors);
	}

	public HColor getColor() {
		return colors.get(0).getColor();
	}

	public int getColorArrowSeparationSpace() {
		return colorArrowSeparationSpace;
	}

	public int size() {
		return colors.size();
	}

}