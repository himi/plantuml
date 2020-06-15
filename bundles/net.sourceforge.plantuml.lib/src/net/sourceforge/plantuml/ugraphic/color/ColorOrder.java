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
package net.sourceforge.plantuml.ugraphic.color;

import java.awt.Color;

public enum ColorOrder {
	RGB, RBG, GRB, GBR, BRG, BGR;

	public Color getColor(Color color) {
		if (this == RGB) {
			return new Color(color.getRed(), color.getGreen(), color.getBlue());
		}
		if (this == RBG) {
			return new Color(color.getRed(), color.getBlue(), color.getGreen());
		}
		if (this == GRB) {
			return new Color(color.getGreen(), color.getRed(), color.getBlue());
		}
		if (this == GBR) {
			return new Color(color.getGreen(), color.getBlue(), color.getRed());
		}
		if (this == BRG) {
			return new Color(color.getBlue(), color.getRed(), color.getGreen());
		}
		if (this == BGR) {
			return new Color(color.getBlue(), color.getGreen(), color.getRed());
		}
		throw new IllegalStateException();
	}

	public Color getReverse(Color color) {
		color = this.getColor(color);
		return new Color(255 - color.getRed(), 255 - color.getGreen(), 255 - color.getBlue());
	}

	public static ColorOrder fromString(String order) {
		try {
			return ColorOrder.valueOf(order.toUpperCase());
		} catch (Exception e) {
			return null;
		}
	}

}
