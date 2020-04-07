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
package net.sourceforge.plantuml.ugraphic.hand;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.Random;

import net.sourceforge.plantuml.ugraphic.Shadowable;
import net.sourceforge.plantuml.ugraphic.UPolygon;

public class UPolygonHand {

	private final UPolygon poly;

	public UPolygonHand(UPolygon source, Random rnd) {
		final List<Point2D.Double> pt = source.getPoints();
		if (pt.size() == 0) {
			poly = new UPolygon();
			return;
		}
		final HandJiggle jiggle = new HandJiggle(pt.get(0), 1.5, rnd);
		for (int i = 1; i < pt.size(); i++) {
			jiggle.lineTo(pt.get(i));
		}
		jiggle.lineTo(pt.get(0));

		this.poly = jiggle.toUPolygon();
		this.poly.setDeltaShadow(source.getDeltaShadow());
	}

	public Shadowable getHanddrawn() {
		return this.poly;
	}

}
