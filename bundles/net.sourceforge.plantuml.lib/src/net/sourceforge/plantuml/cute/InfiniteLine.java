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
package net.sourceforge.plantuml.cute;

import java.awt.geom.Point2D;

import net.sourceforge.plantuml.geom.AbstractLineSegment;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class InfiniteLine {

	private final Point2D a;
	private final Point2D b;

	public InfiniteLine(Point2D a, Point2D b) {
		this.a = a;
		this.b = b;
	}

	public InfiniteLine(AbstractLineSegment segment) {
		this(segment.getP1(), segment.getP2());
	}

	@Override
	public String toString() {
		return "{" + a + ";" + b + "}";
	}

	public double getDeltaX() {
		return b.getX() - a.getX();
	}

	public double getDeltaY() {
		return b.getY() - a.getY();
	}

	public double getDr() {
		return a.distance(b);
	}

	public double getDiscriminant() {
		return a.getX() * b.getY() - b.getX() * a.getY();
	}

	public InfiniteLine translate(UTranslate translate) {
		return new InfiniteLine(translate.getTranslated(a), translate.getTranslated(b));
	}

}
