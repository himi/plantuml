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
package net.sourceforge.plantuml.ugraphic.svg;

import java.awt.geom.Rectangle2D;

import net.sourceforge.plantuml.svg.SvgGraphics;
import net.sourceforge.plantuml.ugraphic.ClipContainer;
import net.sourceforge.plantuml.ugraphic.UClip;
import net.sourceforge.plantuml.ugraphic.UDriver;
import net.sourceforge.plantuml.ugraphic.UParam;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UShape;
import net.sourceforge.plantuml.ugraphic.color.ColorMapper;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColorGradient;

public class DriverRectangleSvg implements UDriver<SvgGraphics> {

	private final ClipContainer clipContainer;

	public DriverRectangleSvg(ClipContainer clipContainer) {
		this.clipContainer = clipContainer;
	}

	public void draw(UShape ushape, double x, double y, ColorMapper mapper, UParam param, SvgGraphics svg) {
		final URectangle rect = (URectangle) ushape;

		final double rx = rect.getRx();
		final double ry = rect.getRy();
		double width = rect.getWidth();
		double height = rect.getHeight();

		final HColor back = param.getBackcolor();
		if (back instanceof HColorGradient) {
			final HColorGradient gr = (HColorGradient) back;
			final String id = svg.createSvgGradient(mapper.toRGB(gr.getColor1()),
					mapper.toRGB(gr.getColor2()), gr.getPolicy());
			svg.setFillColor("url(#" + id + ")");
			applyColor(svg, mapper, param);
		} else {
			final String backcolor = mapper.toSvg(back);
			svg.setFillColor(backcolor);
			applyColor(svg, mapper, param);
		}

		svg.setStrokeWidth(param.getStroke().getThickness(), param.getStroke().getDasharraySvg());

		final UClip clip = clipContainer.getClip();
		if (clip != null) {
			final Rectangle2D.Double r = clip.getClippedRectangle(new Rectangle2D.Double(x, y, width, height));
			x = r.x;
			y = r.y;
			width = r.width;
			height = r.height;
			if (height <= 0) {
				return;
			}
		}
		svg.svgRectangle(x, y, width, height, rx / 2, ry / 2, rect.getDeltaShadow(), rect.getComment());
	}

	public static void applyColor(SvgGraphics svg, ColorMapper mapper, UParam param) {
		final HColor color = param.getColor();
		if (color instanceof HColorGradient) {
			final HColorGradient gr = (HColorGradient) color;
			final String id = svg.createSvgGradient(mapper.toRGB(gr.getColor1()),
					mapper.toRGB(gr.getColor2()), gr.getPolicy());
			svg.setStrokeColor("url(#" + id + ")");
		} else {
			svg.setStrokeColor(mapper.toSvg(color));
		}
	}
}
