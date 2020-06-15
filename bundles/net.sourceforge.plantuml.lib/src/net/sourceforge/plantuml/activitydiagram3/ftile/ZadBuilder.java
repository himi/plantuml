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
package net.sourceforge.plantuml.activitydiagram3.ftile;

import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.MinMax;
import net.sourceforge.plantuml.ugraphic.UChange;
import net.sourceforge.plantuml.ugraphic.UChangeBackColor;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UParam;
import net.sourceforge.plantuml.ugraphic.UParamNull;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UShape;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.ColorMapper;

public class ZadBuilder implements UGraphic {

	public UGraphic apply(UChange change) {
		if (change instanceof UTranslate) {
			return new ZadBuilder(stringBounder, translate.compose((UTranslate) change), this.context);
		} else if (change instanceof UStroke) {
			return new ZadBuilder(this);
		} else if (change instanceof UChangeBackColor) {
			return new ZadBuilder(this);
		} else if (change instanceof UChangeColor) {
			return new ZadBuilder(this);
		}
		throw new UnsupportedOperationException();
	}

	private final StringBounder stringBounder;
	private final UTranslate translate;
	private final Context context;

	static class Context {
		private final Zad zad = new Zad();
	}

	public ZadBuilder(StringBounder stringBounder) {
		this(stringBounder, new UTranslate(), new Context());
	}

	private ZadBuilder(StringBounder stringBounder, UTranslate translate, Context context) {
		this.stringBounder = stringBounder;
		this.translate = translate;
		this.context = context;
	}

	private ZadBuilder(ZadBuilder other) {
		this(other.stringBounder, other.translate, other.context);
	}

	public StringBounder getStringBounder() {
		return stringBounder;
	}

	public UParam getParam() {
		return new UParamNull();
	}

	public void draw(UShape shape) {
		if (shape instanceof URectangle) {
			drawRectangle((URectangle) shape);
		}
	}

	private void drawRectangle(URectangle shape) {
		final MinMax area = shape.getMinMax().translate(translate);
		// System.err.println("ZadBuilder " + shape + " " + area);
		context.zad.add(area);
	}

	public ColorMapper getColorMapper() {
		throw new UnsupportedOperationException();
	}

	public void startUrl(Url url) {
	}

	public void closeAction() {
	}

	public void flushUg() {
	}

	public boolean matchesProperty(String propertyName) {
		return false;
	}

	public double dpiFactor() {
		return 1;
	}

	public Zad getZad() {
		return context.zad;
	}

}
