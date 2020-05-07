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
package net.sourceforge.plantuml.ugraphic.tikz;

import java.awt.Color;

import net.sourceforge.plantuml.tikz.TikzGraphics;
import net.sourceforge.plantuml.ugraphic.UDriver;
import net.sourceforge.plantuml.ugraphic.UParam;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UShape;
import net.sourceforge.plantuml.ugraphic.color.ColorMapper;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColorGradient;
import net.sourceforge.plantuml.utils.MathUtils;

public class DriverRectangleTikz implements UDriver<TikzGraphics> {

	public void draw(UShape shape, double x, double y, ColorMapper mapper, UParam param, TikzGraphics tikz) {
		final URectangle rect = (URectangle) shape;

		final double width = rect.getWidth();
		final double height = rect.getHeight();
		final double r = MathUtils.min(rect.getRx(), rect.getRy(), width / 2, height / 2);

		final HColor back = param.getBackcolor();
		if (back instanceof HColorGradient) {
			final HColorGradient gr = (HColorGradient) back;
			final Color color1 = mapper.getMappedColor(gr.getColor1());
			final Color color2 = mapper.getMappedColor(gr.getColor2());
			tikz.setGradientColor(color1, color2, gr.getPolicy());
		} else {
			tikz.setFillColor(mapper.getMappedColor(back));
		}
		tikz.setStrokeColor(mapper.getMappedColor(param.getColor()));
		tikz.setStrokeWidth(param.getStroke().getThickness(), param.getStroke().getDashTikz());
		if (r == 0) {
			tikz.rectangle(x, y, width, height);
		} else {
			tikz.rectangleRound(x, y, width, height, r);
		}
	}

}