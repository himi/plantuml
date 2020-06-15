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
package net.sourceforge.plantuml.geom.kinetic;

import java.awt.geom.Point2D;

import net.sourceforge.plantuml.geom.LineSegmentDouble;

public class Frame {

	private double x;
	private double y;

	private final int width;
	private final int height;

	public Frame(double x, double y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	LineSegmentDouble getSide1() {
		return new LineSegmentDouble(x, y, x, y + height);
	}

	LineSegmentDouble getSide2() {
		return new LineSegmentDouble(x, y, x + width, y);
	}

	LineSegmentDouble getSide3() {
		return new LineSegmentDouble(x + width, y, x + width, y + height);
	}

	LineSegmentDouble getSide4() {
		return new LineSegmentDouble(x, y + height, x + width, y + height);
	}

	public Point2D getFrontierPointViewBy(Point2D point) {
		final LineSegmentDouble seg = new LineSegmentDouble(point, getCenter());
		Point2D p = seg.getSegIntersection(getSide1());
		if (p != null) {
			return p;
		}
		p = seg.getSegIntersection(getSide2());
		if (p != null) {
			return p;
		}
		p = seg.getSegIntersection(getSide3());
		if (p != null) {
			return p;
		}
		p = seg.getSegIntersection(getSide4());
		if (p != null) {
			return p;
		}
		return null;
	}

	private Point2D getCenter() {
		return new Point2D.Double(x + width / 2.0, y + height / 2.0);
	}

	public Point2D getMainCorner() {
		return new Point2D.Double(x, y);
	}

	public final double getX() {
		return x;
	}

	public final double getY() {
		return y;
	}

	public final int getWidth() {
		return width;
	}

	public final int getHeight() {
		return height;
	}

}
