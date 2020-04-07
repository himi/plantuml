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

import net.sourceforge.plantuml.graphic.UGraphicDelegator;
import net.sourceforge.plantuml.ugraphic.UChange;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UShape;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class UGraphicNewpages extends UGraphicDelegator {

	private final double ymin;
	private final double ymax;
	private final double dy;

	public UGraphicNewpages(UGraphic ug, double ymin, double ymax) {
		this(ug, ymin, ymax, 0);
	}

	private UGraphicNewpages(UGraphic ug, double ymin, double ymax, double dy) {
		super(ug);
		this.ymin = ymin;
		this.ymax = ymax;
		this.dy = dy;
	}

	public void draw(UShape shape) {
		System.err.println("UGraphicNewpages " + shape.getClass());
		if (dy >= ymin && dy < ymax) {
			getUg().draw(shape);
		} else {
			System.err.println("Removing " + shape);
		}

	}

	public UGraphic apply(UChange change) {
		double newdy = dy;
		if (change instanceof UTranslate) {
			newdy += ((UTranslate) change).getDy();
		}
		return new UGraphicNewpages(getUg().apply(change), ymin, ymax, newdy);
	}

}
