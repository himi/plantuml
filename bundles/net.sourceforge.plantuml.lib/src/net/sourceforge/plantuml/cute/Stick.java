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

import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UPath;

public class Stick implements CuteShape {

	private final double width;
	private final double height;
	private final RotationZoom rotationZoom;

	public Stick(VarArgs varArgs) {
		final Point2D dim = varArgs.getAsPoint("dimension");
		this.width = dim.getX();
		this.height = dim.getY();
		this.rotationZoom = RotationZoom.none();
	}

	private Stick(double width, double height, RotationZoom rotation) {
		this.width = width;
		this.height = height;
		this.rotationZoom = rotation;
	}

	public void drawU(UGraphic ug) {
		if (width > height) {
			drawRotate1(ug);
		} else {
			drawRotate2(ug);
		}
	}

	private void drawRotate1(UGraphic ug) {
		assert width > height;
		final UPath path = new UPath();
		final double small = height / 2;
		path.moveTo(rotationZoom.getPoint(small, 0));
		path.lineTo(rotationZoom.getPoint(width - small, 0));
		path.arcTo(rotationZoom.getPoint(width - small, height), small, 0, 1);
		path.lineTo(rotationZoom.getPoint(small, height));
		path.arcTo(rotationZoom.getPoint(small, 0), small, 0, 1);
		path.closePath();
		ug.draw(path);
	}

	private void drawRotate2(UGraphic ug) {
		assert height > width;
		final UPath path = new UPath();
		final double small = width / 2;
		path.moveTo(rotationZoom.getPoint(width, small));
		path.lineTo(rotationZoom.getPoint(width, height - small));
		path.arcTo(rotationZoom.getPoint(0, height - small), small, 0, 1);
		path.lineTo(rotationZoom.getPoint(0, small));
		path.arcTo(rotationZoom.getPoint(width, small), small, 0, 1);
		path.closePath();
		ug.draw(path);
	}

	public Stick rotateZoom(RotationZoom other) {
		return new Stick(width, height, this.rotationZoom.compose(other));
	}

}
